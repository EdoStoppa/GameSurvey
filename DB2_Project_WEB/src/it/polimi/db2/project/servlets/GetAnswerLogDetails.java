package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import it.polimi.db2.project.entities.AnswerLog;
import it.polimi.db2.project.entities.ProdOfDay;
import it.polimi.db2.project.entities.User;
import it.polimi.db2.project.services.AnswerLogService;
import it.polimi.db2.project.services.ProdOfDayService;

/**
 * Servlet implementation class GetAnswerLogDetails
 */
@WebServlet("/GetAnswerLogDetails")
public class GetAnswerLogDetails extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.project.services/ProdOfayService")
	private ProdOfDayService productOfDayService;
	@EJB(name = "it.polimi.db2.project.services/AnswerLogService")
	private AnswerLogService answerLogService;
	
    
    public GetAnswerLogDetails() {
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
		
		try {

			// Extract the date and sets it into the session
			String selectedDate = request.getParameter("date");
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date actualDate = (Date) dateFormat.parse(selectedDate);
			
			request.getSession().setAttribute("date", actualDate);
			
			final WebContext ctx = setStats(request, response, actualDate);
			
			String path = "/HTML/adminInspectPage.html";
			templateEngine.process(path, ctx, response.getWriter());
			
		} catch (ParseException e) {			// Date parsing
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error in date parsing");
			return;
		} catch (Exception e) {					// Generic exception
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error in data retrieving");
			return;
		}
		
	}
	
	private WebContext setStats(HttpServletRequest request, HttpServletResponse response, Date date) throws IOException {
		
		ProdOfDay prodOfDay = null;
		List<AnswerLog> answersLog = null;
		
		try {
			
			prodOfDay = productOfDayService.getProductOfDayFor(date);
			
			// Checks if a product has been found for the specfied date
			if (prodOfDay == null) {
				
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				
				ctx.setVariable("errorMessage", "No questionnaire found for the specified date");
				
				return ctx;
				
			}
			
			answersLog = answerLogService.getAnswersForProduct(prodOfDay.getId());
			
			List<User> confirmedUsers = new ArrayList<User>();
			List<User> cancelledUsers = new ArrayList<User>();
			
			// Filters the answers between confirmed and not, populating the lists of
			// users previously defined
			for (AnswerLog answerLog : answersLog) {
				
				if (answerLog.getConfirmed()) {
					confirmedUsers.add(answerLog.getUser());
				} else {
					cancelledUsers.add(answerLog.getUser());
				}
				
			}

			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			
			ctx.setVariable("confirmedUsers", confirmedUsers);
			ctx.setVariable("cancelledUsers", cancelledUsers);
			
			ctx.setVariable("productOfDayId", prodOfDay.getId());
			
			return ctx;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		}
		
	}

	public void destroy() {
	}
	
}
