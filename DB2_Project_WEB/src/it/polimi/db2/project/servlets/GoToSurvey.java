package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.project.entities.*;
import it.polimi.db2.project.services.*;

/**
 * Servlet implementation class GoToSurvey
 */
@WebServlet("/GoToSurvey")
public class GoToSurvey extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.project.services/ProdOfayService")
	private ProdOfDayService pOfDayService;
	@EJB(name = "it.polimi.db2.project.services/QuestionService")
	private QuestionService questService;
       
    public GoToSurvey() {
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

    // DO GET
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Check if the product of the day obtained is still current
		Object pOfDayId;
		try {
			
			pOfDayId = request.getSession().getAttribute("pOfDayId");
			if (pOfDayId == null) {
	        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error (session variable not present)");
				return;
			}
			
			// if not current just redirect to homepage
			if(!pOfDayService.isCurrent((int) pOfDayId)) {
				request.getSession().setAttribute("pOfDayId", null);
				String path = getServletContext().getContextPath() + "/GoToHomepage";
				response.sendRedirect(path);
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error");
			return;
		}
		
		
		// Main part of this servlet
		try {
			
			// Retrieve from DB all the questions for the product of the day and save them
			questService.setQuestList((int) pOfDayId);
			
			// Save the QuestionService as a session variable
			request.getSession().setAttribute("questService", questService);
			
			// Finally load SurveyPage.html
			processPage(request, response, questService.getQuestList());
			return;
			
		} catch (Exception e) {
			
			e.printStackTrace();
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error");
			return;
			
		}
		
	}

	// DO POST
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	// Generate the survey page
	private void processPage(HttpServletRequest request, HttpServletResponse response, List<Question> questList) throws IOException {
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("questList", questList);
		String path = "/HTML/surveyPage.html";
		templateEngine.process(path, ctx, response.getWriter());

	}

}
