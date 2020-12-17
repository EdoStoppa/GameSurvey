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
 * Servlet implementation class EndSurvey
 */
@WebServlet("/GoToEndSurvey")
public class GoToEndSurvey extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
    
    public GoToEndSurvey() {
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
		
		String chk = null;
		boolean cnf = false;
		try {
			
			// Get the parameter's content
			chk = (String) request.getParameter("chk");
			// If parameter is present, check the submission
			if(chk != null)
				cnf = chk.equals("Confirm");
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Parameters");
			return;
		}

		// Show "thanks" page
 	 	ServletContext servletContext = getServletContext();
 	 	final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
 	 	ctx.setVariable("cnf", cnf);
 	 	String path = "/HTML/surveyCompleted.html";
 	 	templateEngine.process(path, ctx, response.getWriter());
 	 	
		
	}

	// DO POST
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
