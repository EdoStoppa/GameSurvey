package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.NonUniqueResultException;
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
 * Servlet implementation class LeaderBoard
 */
@WebServlet("/GoToLeaderBoard")
public class GoToLeaderBoard extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	// Services
	@EJB(name = "it.polimi.db2.project.services/LeaderboardService")
	private LeaderboardService leaderboardService;
	
       
    public GoToLeaderBoard() {
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

        List<Leaderboard> leaderboardEntries;

        ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
        try {
        	
        	leaderboardEntries = leaderboardService.getLeaderboard();
    		ctx.setVariable("leaderboardEntries", leaderboardEntries);
        	
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
		
		String path = "/HTML/leaderboard.html";
		templateEngine.process(path, ctx, response.getWriter());
		
	}

	// POST
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}