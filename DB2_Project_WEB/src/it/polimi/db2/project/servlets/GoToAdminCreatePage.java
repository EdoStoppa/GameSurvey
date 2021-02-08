package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.project.entities.ProdOfDay;
import it.polimi.db2.project.entities.Product;
import it.polimi.db2.project.entities.Question;
import it.polimi.db2.project.exceptions.NotFoundException;
import it.polimi.db2.project.services.ProdOfDayService;
import it.polimi.db2.project.services.ProductService;

/**
 * Servlet implementation class GoToAdminCreatePage
 */
@WebServlet("/GoToAdminCreatePage")
public class GoToAdminCreatePage extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	       
	@EJB(name = "it.polimi.db2.project.services/ProductService")
	private ProductService productService;
	@EJB(name = "it.polimi.db2.project.services/ProductOfDayService")
	private ProdOfDayService productOfDayService;
	
	private ProdOfDay productOfDay = null;
	
	
	// Inits
    public GoToAdminCreatePage() {
    	super();
	  }
	    
    public void init() throws ServletException {
    	
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		
		templateResolver.setSuffix(".html");
		
    }

    // Helper function: returns true if the dates are in the same day (ignoring the hour and minutes)
    private boolean sameDay(Date leftDate, Date rightDate) {
    	return (leftDate.getDay() == rightDate.getDay()) && (leftDate.getMonth() == rightDate.getMonth()) && (leftDate.getYear() == rightDate.getYear());
    }
    
	// GET
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		String path = "/HTML/adminCreatePage.html";
	
		List<Product> products = null;
		
		Product product = null;
		Date date = null;
		Integer numberOfQuestions = null;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		// Get the list of products and sets it in the context to display the drop-down
		// containing all the available products of the day
		try {
			
			products = productService.getAll();
			ctx.setVariable("products", products);
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error in data request to the DB");
			return;
		}
		
		// Set current date into the session
		Date currentDate = Calendar.getInstance().getTime();
		
		String currentDateAsString = dateFormat.format(currentDate);
		ctx.setVariable("currentDate", currentDateAsString);
		
		// Get the number of questions, the date and the productId. The former is set in the context
		// to display the correct number of text-inputs, while the latters are used to create a
		// productOfDay object
		try {

			String numberOfQuestionsParam = (String) session.getAttribute("numberOfQuestions");
			String productIdParam = (String) session.getAttribute("productId");
			String dateParam = (String) session.getAttribute("date");
			
			String errorParam = (String) session.getAttribute("errorMessage");
			
			// Checks if this is a new session
			if (numberOfQuestionsParam != null && productIdParam != null && dateParam != null) {	// Not a new session 

				// Parse the data
				numberOfQuestions = Integer.parseInt(numberOfQuestionsParam);
				Integer productId = Integer.parseInt(productIdParam);
				date = (Date) dateFormat.parse(dateParam);
			
				ctx.setVariable("numberOfQuestions", numberOfQuestions);
				
				product = productService.getProdById(productId);
				
				// Initial setup of the product of the day
				productOfDay = new ProdOfDay();
				productOfDay.setProduct(product);
				productOfDay.setChosenDate(date);
				
			} else if (errorParam != null) {			// There is an error to display
				
				ctx.setVariable("errorMessage", errorParam);
				
			}
			
		} catch (ParseException e) {			// Date parsing
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error in date parsing");
			return;
		} catch (NumberFormatException e) {		// Integer parsing
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error in number of questions parsing");
			return;
		} catch (NotFoundException e) {			// ProductOfDay retrieving
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error in retrieving the specified product of the day from the DB");
			return;
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error");
		}
		
		templateEngine.process(path, ctx, response.getWriter());
			
	}

	// POST
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String productId = null;
		String selectedDate = null;
		String numberOfQuestions = null;

		// Extract the productId, the date and the number of questions
		// and sets it into the session
		try {
			
			productId = request.getParameter("productId");
			selectedDate = request.getParameter("date");
			numberOfQuestions = request.getParameter("numberOfQuestions");
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error in data retrieving");
			return;
		}
		
		// Try to retrieve the questions
		Boolean questionsFound = composeQuestionnaire(request);
		
		// If any question has been found the object is persisted (this is the 2nd step)
		if (questionsFound) {
			
			try {
				productOfDayService.addProdOfDay(productOfDay);
			} catch (Exception e) { 
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cannot persist the object in the DB");
				return;
			}
			
			String path = getServletContext().getContextPath() + "/GoToAdminHomepage";
			response.sendRedirect(path);
			
		} else if (productId != null && selectedDate != null && numberOfQuestions != null) {
		// Otherwise, if all the parameters above are set, we are in 
		// the 1st step: we have to check the inserted date to find if it is
		// consistent and, if so, insert it into the session
			try {

				if (selectedDate.length() != 10) { throw new ParseException(selectedDate, 0); } 
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				
				Integer numberOfQuestionsAsInt = Integer.parseInt(numberOfQuestions);
				Date currentDate = Calendar.getInstance().getTime();
				Date dateObject = (Date) dateFormat.parse(selectedDate);
			
				ProdOfDay alreadyExistingProductOfDay = productOfDayService.getProductOfDayFor(dateObject);
				
				if (alreadyExistingProductOfDay != null) {												// There already is a product of the day with the selected date
					request.getSession().setAttribute("errorMessage", "There already is a product of the day with the selected date");
				} else if (numberOfQuestionsAsInt < 1) {												// Low number of questions selected
					request.getSession().setAttribute("errorMessage", "Please select a valid number of questions");
				} else if (!(dateObject.after(currentDate) || sameDay(dateObject, currentDate))) {		// Invalid date selected
					request.getSession().setAttribute("errorMessage", "Please select an appropriate date");
				} else {
				
					request.getSession().setAttribute("productId", productId);
					request.getSession().setAttribute("date", selectedDate);
					request.getSession().setAttribute("numberOfQuestions", numberOfQuestions);
					
				}
			
				String path = getServletContext().getContextPath() + "/GoToAdminCreatePage";
				response.sendRedirect(path);
		
			} catch (ParseException e) { 
			
				request.getSession().setAttribute("errorMessage", "Please select an appropriate date format (dd-mm-yyyy)");
				String path = getServletContext().getContextPath() + "/GoToAdminCreatePage";
				response.sendRedirect(path);
				
			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error");
			}
			
		} else {
		// This is the step 0, when we first load the page
			String path = getServletContext().getContextPath() + "/GoToAdminCreatePage";
			response.sendRedirect(path);
		}
		
	}
	
	// Compose the questionnaire retrieving each question
	private Boolean composeQuestionnaire(HttpServletRequest request) {
		
		Boolean questionsFound = false;
		Boolean continueParsing = true;
		
		for (Integer i = 1; continueParsing; i++) {
			
			try {
				
				String tag = i.toString();
				String questionText = (String) request.getParameter(tag);
			
				// If the question is present (= the tag exists) it is added in the product
				// of the day questionnaire
				if (questionText != null) {
				
					Question question = new Question();
					question.setQuestion(questionText);
					
					productOfDay.addQuestion(question);
				
					questionsFound = true;
					
				} else {
				// Otherwise the process gets halted
					
					continueParsing = false;
					
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return questionsFound;
		
	}
	

}
