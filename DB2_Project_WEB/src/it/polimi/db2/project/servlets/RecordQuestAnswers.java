package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.project.services.*;
import it.polimi.db2.project.entities.*;

/**
 * Servlet implementation class RecordAnswers
 */
@WebServlet("/RecordQuestAnswers")
public class RecordQuestAnswers extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
    
    public RecordQuestAnswers() {
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
		
		// First, retrieve the questService to get the correct answers
		QuestionService questService = (QuestionService) request.getSession().getAttribute("questService");
		if(questService == null) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error (no session variable found)");
			return;
		}
		
		// Now extract all the answers from user
		List<String> answList = new ArrayList<String>();
		String answ;
		try {
			
			// Get the array of answers given by the user
			String[] answArray = (request.getParameterMap()).get("answList");
			if(answArray == null) {
				throw new Exception("Internal Error");
			}
			
			// If there's a discrepancy between # of questions and # of answers, throw exception
			if(questService.getQuestList().size() != answArray.length) {
				setError(request, response, questService.getQuestList());
				return;
			}
			
			// Get the current user
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession s = req.getSession();
			User user = (User) s.getAttribute("user");
			
			// For every answer, sanitize and insert it in the corresponding position
			for(int i=0; i<answArray.length; i++) {
					
				// Get the correct answer
				answ = StringEscapeUtils.escapeJava(answArray[i]);
					
				// (if it doesn't exists or is empty reload page)
				if (answ == null || answ.isEmpty()) {
					setError(request, response, questService.getQuestList());
					return;
				}					
					
				// Add the retrieved answer at the index corresponding to its question
				answList.add(i, answ);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		// Save the answer given by the user
		questService.setAnswerList(answList);
		
		// After saving the answer, redirect to second part of the Survey
		String path = getServletContext().getContextPath() + "/GoToStat";
		response.sendRedirect(path);
		
	}
	
	private void setError(HttpServletRequest request, HttpServletResponse response, List<Question> questList) throws IOException{
		
		// Get back to login page displaying an error message
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("questList", questList);
		ctx.setVariable("errorMsg", "An error occurred. Please, check your answers");
		String path = "/HTML/surveyPage.html";
		templateEngine.process(path, ctx, response.getWriter());
		
	}

}
