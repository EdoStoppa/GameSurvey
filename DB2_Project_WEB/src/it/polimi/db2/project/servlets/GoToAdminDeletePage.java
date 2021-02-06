package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import it.polimi.db2.project.entities.ProdOfDay;
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
	
	// Helper function: returns true if the dates are in the same day (ignoring the hour and minutes)
    private boolean sameDay(Date leftDate, Date rightDate) {
    	return (leftDate.getDay() == rightDate.getDay()) && (leftDate.getMonth() == rightDate.getMonth()) && (leftDate.getYear() == rightDate.getYear());
    }
	
	// Set the products into the context
	private WebContext setProducts(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		List<ProdOfDay> prodList = null;
		
		try {
			
			// Products list
			prodList = productOfDayService.getAll();

			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			
			// Date dividing
			Date currentDate = Calendar.getInstance().getTime();
			
			List<ProdOfDay> deletableProducts = new ArrayList<ProdOfDay>();
			List<ProdOfDay> nonDeletableProducts = new ArrayList<ProdOfDay>();
			
			// Divides products by their date
			for (ProdOfDay product: prodList) {
				
				if (product.getChosenDate().after(currentDate) || sameDay(product.getChosenDate(), currentDate)) {		// Current or future date
					nonDeletableProducts.add(product);
				} else {	// Past date
					deletableProducts.add(product);
				}
				
			}
			
			ctx.setVariable("deletableProducts", deletableProducts);
			ctx.setVariable("nonDeletableProducts", nonDeletableProducts);
			
			return ctx;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
			throw new IOException();
		}
		
	}

}
