package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
	@EJB(name = "it.polimi.db2.project.services/LeaderboardService")
	private LeaderboardService leaderboardService;
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
		
		// Get if the user already answered to the survey today, if anything goes wrong redirect to loginpage
		// If the user already took the survey, the homepage is loaded
        boolean alreadyAnsw = false;
        try {
        	
        	// Retrieve the user
        	User usr = (User) request.getSession().getAttribute("user");
        	if(usr == null) { throw new Exception("No User found"); }
        	
			alreadyAnsw = leaderboardService.isUserInLeaderboard(usr.getId());
        	
        } catch (Exception e) {
        	
        	e.printStackTrace();
        	request.getSession().invalidate();	
    		String path = getServletContext().getContextPath() + "/login.html";
			response.sendRedirect(path);
			return;
        }
		
        // If User already took the survey, reload the homepage (user should not be here)
        if (alreadyAnsw) {		
        	String path = getServletContext().getContextPath() + "/GoToHomepage";
			response.sendRedirect(path);
			return;
        }

		// Inject the QuestionService
		try { 
			
			initQuestionService(request); 
			
		} catch (Exception e) { 
			e.printStackTrace(); 
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error (no session variable found)");
			return;
		}
		
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
				
				// If questService is present in session, simply delete it
				QuestionService qSrv = (QuestionService) request.getSession().getAttribute("questService");
				if(qSrv != null) { qSrv.remove(); }
				
				request.getSession().setAttribute("questService", null);
				
				goHomepage(request, response);
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
			processPage(request, response, questService.getQuestList(), questService.getAnswerList());
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
	
	
	// Method used to obtain the QuestService (or create it if not present)
	private void initQuestionService(HttpServletRequest request) throws Exception {
		
		// Get the ID that's present only if user clicked on "Previous" from Stat page
		String ID = request.getParameter("ID");
		// If ID is present, inject the used QuestService
		if(ID != null) {
			
			// Retrieve the questService
			QuestionService questService = (QuestionService) request.getSession().getAttribute("questService");
			
			if(questService == null) {
					throw new IOException();
			} else {
				
				this.questService = questService;
				
			}
					
		} else {
			
			// Using JNDI lookup inject a new QuestionsService
			InitialContext ic = new InitialContext();
			this.questService = (QuestionService) ic.lookup("java:comp/env/it.polimi.db2.project.services/QuestionService");
			
		}
		
	}
	
	// Generate the survey page
	private void processPage(HttpServletRequest request, HttpServletResponse response, List<Question> questList, List<String> answList) throws IOException {
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		List<Integer> intList = new ArrayList<Integer>();
		
		for(int i=0; i<questList.size(); i++)
			intList.add(i);
		
		ctx.setVariable("intList", intList);
		ctx.setVariable("questList", questList);
		ctx.setVariable("answList", answList);
		
		String path = "/HTML/surveyPage.html";
		templateEngine.process(path, ctx, response.getWriter());

	}
	
	// Generate Homepage (used in case of error)
	private void goHomepage(HttpServletRequest request, HttpServletResponse response) throws IOException{
		request.getSession().setAttribute("pOfDayId", null);
		String path = getServletContext().getContextPath() + "/GoToHomepage";
		response.sendRedirect(path);
	}

}
