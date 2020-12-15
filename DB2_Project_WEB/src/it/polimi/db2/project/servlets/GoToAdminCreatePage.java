package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

    
	// GET
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		String path = "/HTML/adminCreatePage.html";
	
		List<Product> products = null;
		
		Product product = null;
		Date date = null;
		Integer numberOfQuestions = null;
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		// Get the list of products
		try {
			
			products = productService.getAll();
			ctx.setVariable("products", products);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error in data request to the DB");
			return;
		}
		
		// Get the number of questions (if set)
		try {

			String numberOfQuestionsParam = (String) session.getAttribute("numberOfQuestions");
			numberOfQuestions = Integer.parseInt(numberOfQuestionsParam);
			ctx.setVariable("numberOfQuestions", numberOfQuestions);
			System.out.println("-1");
			
			String productIdParam = (String) session.getAttribute("productId");
			Integer productId = Integer.parseInt(productIdParam);
			product = productService.getProdById(productId);
			System.out.println("-2");
			
			String dataParam = (String) session.getAttribute("date");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			date = (Date) dateFormat.parse(dataParam);
			System.out.println("-3");
			
			// Initial setup of the product of the day
			if (product != null && date != null) {

				System.out.println("-4");
				productOfDay = new ProdOfDay();
				productOfDay.setProduct(product);
				productOfDay.setChosenDate(date);
				System.out.println("-5");
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		templateEngine.process(path, ctx, response.getWriter());
			
	}

	// POST
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String productId = null;
		String selectedDate = null;
		String numberOfQuestions = null;
		
		try {

			// Extract the product, the date and the number of questions
			// and sets it into the session
			productId = request.getParameter("productId");
			selectedDate = request.getParameter("date");
			numberOfQuestions = request.getParameter("numberOfQuestions");
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error in data retrieving");
			return;
		}

		System.out.println("OKKKKK");
		
		// Retrieve the number of questions
		Boolean continueParsing = true;
		for (Integer i = 1; continueParsing; i++) {
			
			try {
				
				String tag = i.toString();
				System.out.println(tag);
				
				String questionText = (String) request.getParameter(tag);
				System.out.println(questionText);
			
				if (questionText != null) {
				
					Question question = new Question();
					question.setQuestion(questionText);
					
					productOfDay.addQuestion(question);
					System.out.println(questionText);
				
				} else {
					continueParsing = false;
					
					if (i != 1) {
						try {
							productOfDayService.addProdOfDay(productOfDay);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
				}
					
			} catch (Exception e) {
				e.printStackTrace();
				continueParsing = false;

				System.out.println("Stopped");
				System.out.print(i);
				
				if (i != 1) {
					try {
						productOfDayService.addProdOfDay(productOfDay);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
		}
		
		request.getSession().setAttribute("productId", productId);
		request.getSession().setAttribute("date", selectedDate);
		request.getSession().setAttribute("numberOfQuestions", numberOfQuestions);
	
		String path = getServletContext().getContextPath() + "/GoToAdminCreatePage";
		response.sendRedirect(path);
		
	}

}
