package it.polimi.db2.project.servlets;

import java.io.IOException;
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

import org.apache.commons.lang3.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.project.entities.*;
import it.polimi.db2.project.services.*;

/**
 * Servlet implementation class CompleteSurvey
 */
@WebServlet("/CompleteSurvey")
public class CompleteSurvey extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.project.services/ProdOfayService")
	private ProdOfDayService pOfDayService;
	@EJB(name = "it.polimi.db2.project.services/AnswerLogService")
	private AnswerLogService answLogService;
	@EJB(name = "it.polimi.db2.project.services/FullAnswerService")
	private FullAnswerService fullAnswService;
	@EJB(name = "it.polimi.db2.project.services/StatService")
	private StatService statService;
	@EJB(name = "it.polimi.db2.project.services/OffensiveWordService")
	private OffensiveWordService offensiveWordService;
	@EJB(name = "it.polimi.db2.project.services/UserService")
	private UserService userService;
	
	private final String noString = " -- ";

    public CompleteSurvey() {
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
		doPost(request, response);
	}

	// DO POST
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 

		
		// PREP WORK:
		// This section is focused on integrity check and on retrieving everything "technical" that is necessary to complete the survey
		
		// Retrieve the questService
		QuestionService questService = (QuestionService) request.getSession().getAttribute("questService");
		if(questService == null) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error (no session variable found)");
			return;
		}
		
		// Control if the pOfDay is still current, if not send immediately to a "sorry" page
		try {
			
			// if methods return false, everything is already taken care, just return
			if(!checkCurrent(request, response, questService)) { return; }
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error");
			return;
		}
		
		// Get which button was pressed
		String pressedButton = null;
		try {
			pressedButton = request.getParameter("action");
							
			if (pressedButton == null) {
				throw new Exception("Missing parameter in request");
			}
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
			return;
		}
		
		if(pressedButton.equals("Previous")) {
			// Go back at the start of the Survey
			String path = getServletContext().getContextPath() + "/GoToSurvey";
			path = path + "?ID=reDo";
			response.sendRedirect(path);
			return;
		}
		
		// Retrieve user
		User usr = (User) request.getSession().getAttribute("user");
		
		// If confirmed, check if any offensive-word was used, and if that's the case, ban the user
		if(pressedButton.equals("Confirm")) {
			
			try {
				
				if(offensiveWordService.isUsedOffensiveWord(questService.getAnswerList())) {
					
					// Ban the user
					userService.banUser(usr.getId());
					usr.blockUser();
					request.getSession().setAttribute("user", usr);
					
					// Remove the questService of the user
					request.getSession().setAttribute("questService", null);
					questService.remove();
					
					//  Redirect to Homepage (filter on homepage will complete the ban)
					response.sendRedirect(getServletContext().getContextPath()+"/GoToHomepage");
					return;

				}
				
			} catch(Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error banning user");
				return;
			}
			
		}

		
		// ---------------------------------------------------------------------------------------------------------------------
		
		// COMPLETE SURVEY:
		// Prep Work is finished, so proceed with retrieving all the input data
		
		// Retrive Statistics
		String usrSex = null;
		String usrAge = null;
		String usrExp = null;
		try {
			
			usrSex = StringEscapeUtils.escapeJava(request.getParameter("usrSex"));
			usrAge = StringEscapeUtils.escapeJava(request.getParameter("usrAge"));
			usrExp = StringEscapeUtils.escapeJava(request.getParameter("usrExp"));
				
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error parsing Statistics");
			return;
		}
		
		// Get all remaining data necessary to log the answers
		ProdOfDay pOfDay = null;
		Date logTime = null;
		boolean confirmed = false;
		int totPoints = 0;
		try {
			
			pOfDay = pOfDayService.getProductOfDayFor((int) request.getSession().getAttribute("pOfDayId"));
			logTime = Calendar.getInstance().getTime();
			if(pressedButton.equals("Confirm")) { 
				confirmed = true; 
				totPoints = questService.getQuestList().size() + getStatPoints(usrSex, usrAge, usrExp);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while logging the answer");
			return;
		}
		
		// Log the submission (even if it isn't confirmed)
		AnswerLog log = answLogService.logSubmission(usr, pOfDay, logTime, confirmed, totPoints);
		
		// Only if User confirmed save all the answers inserted
		if(pressedButton.equals("Confirm")) {
			
			// Save the full answers
			fullAnswService.logAnswers(log, questService.getQuestList(), questService.getAnswerList());
			
			// Save Stats on DB
			statService.logStat(log, usrSex, usrAge, usrExp);
			
		}
		
		
		// ---------------------------------------------------------------------------------------------------------------------
		
		
		// WRAP UP:
		// Almost done, need to destroy the questService and redirect to correct page
		
		// QeustService removal
		questService.remove();
		request.getSession().setAttribute("questService", null);
		
		// Redirect to correct page
		String path = getServletContext().getContextPath() + (pressedButton.equals("Confirm") ? "/GoToEndSurvey" : "/GoToHomepage");
		response.sendRedirect(path);
		
	}
	
	// This method is executed at the start of the GET, to know if the user completed in time the survey.
	// If he/she didn't finished in time, redirect to the correct page and return false... otherwise everything is good
	// so the app continues to process and save all the data
	private boolean checkCurrent(HttpServletRequest request, HttpServletResponse response, QuestionService questService) throws Exception{
		
		Object pOfDayId;
		try {
			
			// Obtain the pOfDay that user saved in Homepage
			pOfDayId = request.getSession().getAttribute("pOfDayId");
			if (pOfDayId == null) {
				// destroy the questService
				questService.remove();
				
		       	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error (session variable not present)");
				return false;
			}
			
			// if not current just redirect to homepage
			if(!pOfDayService.isCurrent((int) pOfDayId)) {
				
				// remove and destroy the questService
				questService.remove();
				request.getSession().setAttribute("questService", null);
				
				// complete the redirection
				request.getSession().setAttribute("pOfDayId", null);
				String path = getServletContext().getContextPath() + "/sorryPage.html";
				response.sendRedirect(path);
				return false;
			}
				
		} catch (Exception e) {
			// remove and destroy the questService
			questService.remove();
			request.getSession().setAttribute("questService", null);
			
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error");
			return false;
		}
		
		return true;
	}
	
	// Simple method used to calculate the points from the Statistic part of the survey
	private int getStatPoints(String usrSex, String usrAge, String usrExp) {
		int points = 0;
		
		if (!usrSex.equals(noString))
			points++;
		
		if (!usrExp.equals(noString))
			points++;
		
		try {
			int temp = Integer.parseInt(usrAge);
			points++;
		} catch (Exception e) { /* Don't do anything, simply no points attributed*/ }
		
		return points*2;
	}
	
	
	
	
	
}
