package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
        	
        	currentProductOfDay = prodOfDayService.getCurrenProdOfDay();
        	todayAnswersLog = answerLogService.getAnswersForProduct(currentProductOfDay.getId());
        	
        	// Gets all the users from the todayAnswerLog list
        	todayActiveUsers = new ArrayList<User>();
        	for (AnswerLog answerLog : todayAnswersLog) {
        		todayActiveUsers.add(answerLog.getUser());
        	}
        	
        } catch (Exception e) {
        	printHtmlHeader(out);
            printHtmlFooter(out);
            return;
        }
        
        // Create the page from the users' list ...
        printHtmlHeader(out);
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