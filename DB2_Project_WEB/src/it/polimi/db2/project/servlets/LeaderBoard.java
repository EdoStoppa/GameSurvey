package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.db2.project.entities.*;
import it.polimi.db2.project.services.*;


/**
 * Servlet implementation class LeaderBoard
 */
@WebServlet("/LeaderBoard")
public class LeaderBoard extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	// Services
	@EJB(name = "it.polimi.db2.project.services/UserService")
	private UserService userService;
	@EJB(name = "it.polimi.db2.project.services/AnswerLog")
	private AnswerLogService answerLogService;
	@EJB(name = "it.polimi.db2.project.services/ProdOfDayService")
	private ProdOfDayService prodOfDayService;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LeaderBoard() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        ProdOfDay currentProductOfDay;
        List<AnswerLog> todayAnswersLog;
        List<User> todayActiveUsers;
        
        try {
        	
        	//currentProductOfDay = prodOfDayService.getCurrenProdOfDay();
        	todayAnswersLog = answerLogService.getAnswersForProduct(1);
    		
        	// Gets all the users from the todayAnswerLog list
        	todayActiveUsers = new ArrayList<User>();
        	for (AnswerLog answerLog : todayAnswersLog) {
        		todayActiveUsers.add(answerLog.getUser());
        	}
        	
        	// Sorts the user by their totalPoints field
        	Collections.sort(todayActiveUsers, new Comparator<User>() {
        		
        		@Override
        	    public int compare(User lhs, User rhs) {
        	        if (lhs.getTotPoints() < rhs.getTotPoints()) { return 1; }
        	        if (lhs.getTotPoints() > rhs.getTotPoints()) { return -1; }
        	        return 0;
        	    }
        		
        	});
        	
        } catch (Exception e) {
        	printHtmlHeader(out);
        	printHtmlError(out, e.getMessage());
            printHtmlFooter(out);
            
            return;
        }
		
        // Create the page from the users' list ...
        printHtmlHeader(out);
        printHtml(out, createTableFromUsers(todayActiveUsers));
        printHtmlFooter(out);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void printHtmlHeader(PrintWriter out) throws IOException {
				
        out.println("<body>");
        out.println("<html>");
        out.println("<head><title> LeaderBoard </title></head>");
        out.println("<center><h1> LeaderBoard </h1></center>");
        
    }
	
	private String createTableFromUsers(List<User> usersList) {
		
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("<center> <table style = 'width:75%'>");
		
		// Title's section
		stringBuilder.append("<tr>");
		stringBuilder.append("<th> Username </th>");
		stringBuilder.append("<th> Mail </th>");
		stringBuilder.append("<th> Total points </th>");
		stringBuilder.append("</tr>");
		
		// Users' section
		for(User user : usersList) {

			stringBuilder.append("<tr>");
			
			// Username
			stringBuilder.append("<td>");
			stringBuilder.append(user.getUsername());
			stringBuilder.append("</td>");
			
			// Mail
			stringBuilder.append("<td>");
			stringBuilder.append(user.getEmail());
			stringBuilder.append("</td>");
			
			// Points
			stringBuilder.append("<td>");
			stringBuilder.append(user.getTotPoints());
			stringBuilder.append("</td>");
			
			stringBuilder.append("</tr>");
			
		}

		stringBuilder.append("</table> </center>");
		
		return stringBuilder.toString();
		
	}
	
	private void printHtml(PrintWriter out, String htmlCode) throws IOException {
        out.println("<div>" + htmlCode + "</div>");
	}
	
	private void printHtmlError(PrintWriter out, String msg) throws IOException {
        out.println("<p>" + msg + "</p>");
	}
	
	private void printHtmlFooter(PrintWriter out) throws IOException {
        out.println("</html>");
        out.println("</body>");
        out.close();
    }

}