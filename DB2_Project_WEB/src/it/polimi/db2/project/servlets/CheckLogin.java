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

import it.polimi.db2.project.entities.User;
import it.polimi.db2.project.services.AdminService;
import it.polimi.db2.project.services.UserService;

/**
 * Servlet implementation class CheckLogin
 */
@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.project.services/UserService")
	private UserService usrService;
	@EJB(name = "it.polimi.db2.project.services/AdminService")
	private AdminService admService;
       
    public CheckLogin() {
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

    
    // DO POST 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
		
		// Performs a different action based on the pressed button
		
		// Login action
		if(pressedButton.equals("Login")) {
			
			// Get login parameters
			String usrn = null;
			String pwd = null;
			try {
				usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
				pwd = StringEscapeUtils.escapeJava(request.getParameter("password"));
				
				if (usrn == null || pwd == null || pressedButton == null || usrn.isEmpty() || pwd.isEmpty()) {
					throw new Exception("Missing or empty credential value");
				}
	
			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
				return;
			}
			
			// Search in DB if user's credentials exist
			User user = null;
			try {
				user = usrService.checkCredentials(usrn, pwd);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
				return;
			}
			
			String path;
			if(user == null) {
				
				// Get back to login page displaying an error message
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("errorMsg", "Incorrect username or password");
				path = "/login.html";
				templateEngine.process(path, ctx, response.getWriter());
				return;
				
			} else {
				
				// Query DB to know if user is an admin, and set corresponding landing page
				String landingPage;
				try {
					
					if(user.getBlocked()) {
						response.sendRedirect(getServletContext().getContextPath() + "/GoToBlocked");
						return;
					}
					
					if(admService.isAdmin(user.getId())) {
						landingPage = "/GoToAdminHomepage";
						request.getSession().setAttribute("admin", true);
					} else {
						landingPage = "/GoToHomepage";
						request.getSession().setAttribute("admin", false);
					}
				} catch (Exception e) {
					e.printStackTrace();
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
					return;
				}

				request.getSession().setAttribute("user", user);
				path = getServletContext().getContextPath() + landingPage;
				response.sendRedirect(path);	
				
			}
			
		} else if(pressedButton.equals("Register")) {
			
			// Send the user to register page
			String path = getServletContext().getContextPath() + "/GoToRegisterPage";
			
			response.sendRedirect(path);
		}
		
	}
	
	public void destroy() { }

}
