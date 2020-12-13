package it.polimi.db2.project.servlets;

import java.io.IOException;

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
import it.polimi.db2.project.entities.Product;
import it.polimi.db2.project.services.ProdOfDayService;
import it.polimi.db2.project.services.ProductService;
import it.polimi.db2.project.exceptions.*;

/**
 * Servlet implementation class GoToHomepage
 */
@WebServlet("/GoToHomepage")
public class GoToHomepage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.project.services/ProdOfayService")
	private ProdOfDayService pOfDayService;
	@EJB(name = "it.polimi.db2.project.services/ProductService")
	private ProductService prodService;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToHomepage() {
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Retrieving the product of the day
		ProdOfDay pOfDay = null;
        try {
        	pOfDay = pOfDayService.getCurrenProdOfDay();
        } catch (DatabaseException | NotUniqueException e) {
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Conflicting information (multiple pOfDay)");
			return;
        } catch (Exception e) {
        	e.printStackTrace();
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error");
			return;
        }
        
        // If a product of the day isn't present, pass null to render a "sorry" page
        if(pOfDay == null) {
        	processPage(request, response, null);
    	   	return;
        }
        
        Product prod = pOfDay.getProduct();
        if(prod == null) {
        	// For some unknown reason no product info is stored on DB, so send an error
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Product info missing");
			return;
        }
        
        request.getSession().setAttribute("pOfDayId", pOfDay.getId());
        processPage(request, response, prod);
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void processPage(HttpServletRequest request, HttpServletResponse response, Product prod) throws IOException {
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("prod", prod);
		ctx.setVariable("image", (prod != null ? prod.getPhotoData() : null));
		String path = "/HTML/homepage.html";
		templateEngine.process(path, ctx, response.getWriter());

	}

}
