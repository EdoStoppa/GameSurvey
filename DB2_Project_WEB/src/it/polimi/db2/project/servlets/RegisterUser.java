package it.polimi.db2.project.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.db2.project.exceptions.UserTakenException;
import it.polimi.db2.project.services.UserService;

/**
 * Servlet implementation class RegisterUser
 */
@WebServlet("/RegisterUser")
public class RegisterUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine; 
    @EJB(name = "it.polimi.db2.project.services/UserService")
	private UserService usrService;
    
    private final String CREDTAKEN = "Sorry, one or more of your info is used by another user. Please, try again";
	private final String INFOMISSING = "Missing or empty credentials, please try again";
	private final String PWDNOTMATCH = "Sorry, your passwords don't match. Please, try again";
	private final String RNTIMEERR = "Could not check credentials";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterUser() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Getting all data inserted by the user
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
				// re-elaborate the page showing the correct error
				userErrorManagement(request, response, INFOMISSING);
				return;
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, RNTIMEERR);
			return;
		}
		
		
		// If pwds match try to send info to DB
		if(!pwd.equals(confirmedPwd)) {
			
			// try to re-elaborate the page showing the correct error
			try {
				
				userErrorManagement(request, response, PWDNOTMATCH);
				return;
				
			} catch (IOException e) {
				
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, RNTIMEERR);
				return;
			}
			
			
		} else {

			try {
				
				// insert user info in DB and redirect to login page
				usrService.registerUser(mail, usrn, pwd);
				String path = getServletContext().getContextPath() + "/login.html";
				response.sendRedirect(path);
				return;
				
			} catch (UserTakenException e) {
				
				// Try to re-compute register page, if fail throw server error 
				userErrorManagement(request, response, CREDTAKEN);
				return;
				
			} catch (Exception e) {
				
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, RNTIMEERR);
				return;
				
			}
			
		}
		
		
	}
	
	private void userErrorManagement(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
		
		System.out.println("UserErrorManagement is executing ...");
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("errorMsg", message);
		String path = "/HTML/register.html";
		templateEngine.process(path, ctx, response.getWriter());

	}

}
