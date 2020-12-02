package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import it.polimi.db2.project.services.UserService;

/**
 * Servlet implementation class RegisterPage
 */
@WebServlet("/RegisterPage")
public class RegisterPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.project.services/UserService")
	private UserService usrService;
	
	private final String TITLE = "Register Page";
	private final String DESCRIPTION = "Please, register to use the application";
	
	private final String USERTAKEN = "Sorry, your username is already taken. Please, choose another one";
	private final String INFOMISSING = "Please, input every info";
	private final String PWDNOTMATCH = "Sorry, your passwords don't match. Please, try again";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        printHtmlHeader(out);
        printHtmlFooter(out);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mail = null;
		String usrn = null;
		String pwd = null;
		String confirmedPwd = null;
		
		try {
			mail = StringEscapeUtils.escapeJava(request.getParameter("email"));
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("password"));
			confirmedPwd = StringEscapeUtils.escapeJava(request.getParameter("confirmedPassword"));
			Boolean nullCheck = (mail == null || usrn == null || pwd == null || confirmedPwd == null);
			Boolean emptyCheck = (mail.isEmpty() ||  usrn.isEmpty() || pwd.isEmpty() || confirmedPwd.isEmpty());
			if (nullCheck || emptyCheck) {
				throw new Exception("Missing or empty credential value");
			}
		} catch (Exception e) {
			// for debugging only e.printStackTrace();
			response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	        printHtmlHeader(out);
	        printHtmlError(out, INFOMISSING);
	        printHtmlFooter(out);
			return;
		}
		
		Boolean userTaken = false;
		try {
			// query db to authenticate for user
			userTaken = usrService.checkUsrnTaken(usrn);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not register user");
			return;
		}
		
		if(userTaken || pwd.equals(confirmedPwd)) {
			response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	        String msg = (userTaken ? USERTAKEN : PWDNOTMATCH);
	        
	        printHtmlHeader(out);
	        printHtmlError(out, msg);
	        printHtmlFooter(out);
		} else {
			// TODO: We have to persist the new user that we create using userService
			// usrService.registerUser(mail, usrn, pwd);
			
			String path = getServletContext().getContextPath() + "/LoginPage";
			response.sendRedirect(path);
		}
	}
	
	private void printHtmlHeader(PrintWriter out) throws IOException {
        out.println("<body>");
        out.println("<html>");
        out.println("<head><title>" + TITLE + "</title></head>");
        out.println("<center><h1>" + TITLE + "</h1></center>");
        out.println("<p>" + DESCRIPTION + "</p>");
        out.println("</hr>");
        out.println("<form action=\"RegisterPage\" method=\"POST\">");
        out.println("<table><tbody>");
        out.println("<tr><td>Email:</td><td><input type=\"text\" name=\"email\"/></td></tr>");
        out.println("<tr><td>Username:</td><td><input type=\"text\" name=\"username\"/></td></tr>");
        out.println("<tr><td>Password:</td><td><input type=\"password\" name=\"password\"/></td></tr>");
        out.println("<tr><td>Confirm pw:</td><td><input type=\"password\" name=\"confirmedPassword\"/></td></tr>");
        out.println("</tbody></table>");
        out.println("<input name=\"action\" type=\"submit\" value=\"Go\"/>");
        out.println("</form>");
        out.println("<hr/>");
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
