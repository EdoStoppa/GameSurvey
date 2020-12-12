package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
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

import it.polimi.db2.project.entities.AnswerLog;
import it.polimi.db2.project.entities.ProdOfDay;
import it.polimi.db2.project.entities.User;
import it.polimi.db2.project.services.ProdOfDayService;


@WebServlet("/GoToAdminDeletePage")
public class GoToAdminDeletePage extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	@EJB(name = "it.polimi.db2.project.services/ProdOfayService")
	private ProdOfDayService productOfDayService;
	
    public GoToAdminDeletePage() {
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
		
		final WebContext ctx = setProducts(request, response);
		String path = "/HTML/adminDeletePage.html";

		templateEngine.process(path, ctx, response.getWriter());
			
	}

	// POST
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private WebContext setProducts(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		List<ProdOfDay> prodList = null;
		
		try {
			
			prodList = productOfDayService.getAll();

			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			
			ctx.setVariable("productsOfDay", prodList);
			
			return ctx;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
			throw new IOException();
		}
		
	}

}
