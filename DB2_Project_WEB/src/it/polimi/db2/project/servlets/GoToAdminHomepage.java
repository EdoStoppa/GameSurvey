package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * Servlet implementation class Homepage
 */
@WebServlet("/GoToAdminHomepage")
public class GoToAdminHomepage extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;

	
    public GoToAdminHomepage() {
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
        
        ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		String path = "/HTML/adminHomepage.html";
		templateEngine.process(path, ctx, response.getWriter());
		
	}

	// POST
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
		/*
		String pressedButton = null;
		
		try {

			pressedButton = request.getParameter("action");
			
			if (pressedButton == null) {
				throw new Exception("Missing parameter in request");
			}
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing button action");
			return;
		}
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession s = req.getSession();
		
		request.getSession().setAttribute("admin", true);
		request.getSession().setAttribute("user", s.getAttribute("user"));
		
		System.out.println("1");
		if (pressedButton.equals("Create")) {
			System.out.println("2");
			String path = getServletContext().getContextPath() + "/AdminCreate";
			response.sendRedirect(path);
		}
		
		if (pressedButton.equals("Inspect")) {
			System.out.println("3");
			String path = getServletContext().getContextPath() + "/AdminInspect";
			response.sendRedirect(path);
		}

		if (pressedButton.equals("Delete")) {
			System.out.println("4");
			String path = getServletContext().getContextPath() + "/AdminDelete";
			response.sendRedirect(path);
		}
	*/	
	}

}
