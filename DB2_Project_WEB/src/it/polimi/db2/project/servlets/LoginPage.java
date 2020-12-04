package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJB;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;

import it.polimi.db2.project.entities.User;
import it.polimi.db2.project.services.*;

/**
 * Servlet implementation class LoginPage
 */
@WebServlet("/LoginPage")
public class LoginPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.project.services/UserService")
	private UserService usrService;
	
	private final String TITLE = "Login Page";
	private final String DESCRIPTION = "Please, login to use the application";
	private final String USRPWERROR = "Sorry, your username or password is incorrect. Please try again";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginPage() {
        super();
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
		
		// Performs a different action based on the pressed button
		
		// Login action
		if (pressedButton.equals("Go")) {
			
			String usrn =null;
			String pwd = null;
			
			try {
				usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
				pwd = StringEscapeUtils.escapeJava(request.getParameter("password"));
				
				if (usrn == null || pwd == null || pressedButton == null || usrn.isEmpty() || pwd.isEmpty()) {
					throw new Exception("Missing or empty credential value");
				}
	
			} catch (Exception e) {
				// for debugging only e.printStackTrace();
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
				return;
			}
		
		
			User user;
			try {
				// query db to authenticate for user
				user = usrService.checkCredentials(usrn, pwd);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
				return;
			}
			
			if (user == null) {			
				response.setContentType("text/html");
		        PrintWriter out = response.getWriter();
		        printHtmlHeader(out);
		        printHtmlUserPasswError(out);
		        printHtmlFooter(out);
			} else {
				request.getSession().setAttribute("user", user);
				String path = getServletContext().getContextPath() + "/Homepage";
				response.sendRedirect(path);	
			}
			
		} else if (pressedButton.equals("Registrati")) {
		// Register action
			
			String path = getServletContext().getContextPath() + "/RegisterPage";
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
        out.println("<form action=\"LoginPage\" method=\"POST\">");
        out.println("<table><tbody>");
        out.println("<tr><td>Username:</td><td><input type=\"text\" name=\"username\"/></td></tr>");
        out.println("<tr><td>Password:</td><td><input type=\"password\" name=\"password\"/></td></tr>");
        out.println("</tbody></table>");
        out.println("<input name=\"action\" type=\"submit\" value=\"Go\"/>");
        out.println("<input name=\"action\" type=\"submit\" value=\"Registrati\"/>");
        out.println("</form>");
        out.println("<hr/>");
    }
	
	private void printHtmlUserPasswError(PrintWriter out) throws IOException {
        out.println("<p>" + USRPWERROR + "</p>");

	}
	
	private void printHtmlFooter(PrintWriter out) throws IOException {
        out.println("</html>");
        out.println("</body>");
        out.close();
    }
}

