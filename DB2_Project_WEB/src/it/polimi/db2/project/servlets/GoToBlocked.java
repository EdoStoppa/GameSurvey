package it.polimi.db2.project.servlets;

import java.io.IOException;

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
 * Servlet implementation class GoToBlocked
 */
@WebServlet("/GoToBlocked")
public class GoToBlocked extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToBlocked() {
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
 	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
 	 */
 	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 		
 	// Extremely simple, but needed to maintain consistency within the project
 	 	ServletContext servletContext = getServletContext();
 	 	final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
 	 	String path = "/HTML/blocked.html";
 	 	templateEngine.process(path, ctx, response.getWriter());
 		
 		HttpSession session = request.getSession(false);
 		if (session != null) {
 			session.invalidate();
 		}
 		
 	}

 	/**
 	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
 	 */
 	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 		doGet(request, response);
 	}

}
