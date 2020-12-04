package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Base64;

import it.polimi.db2.project.entities.*;
import it.polimi.db2.project.services.*;

/**
 * Servlet implementation class Homepage
 */
@WebServlet("/Homepage")
public class Homepage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.project.services/ProdOfayService")
	private ProdOfDayService pOfDayService;
	@EJB(name = "it.polimi.db2.project.services/ProductService")
	private ProductService prodService;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Homepage() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        ProdOfDay pOfDay;
        try {
        	pOfDay = pOfDayService.getCurrenProdOfDay();
        } catch (NonUniqueResultException e) {
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Conflicting information (multiple products for the day)");
			return;
        } catch (Exception e) {
        	printHtmlHeader(out, null);
            printHtmlFooter(out);
            return;
        }
        
        Product prod;
        try {
        	prod = prodService.getProdById(pOfDay.getId());
        } catch (Exception e) {
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Missing product info");
            return;
        }
        
        System.out.println("Product of the day retrieved ...");
        
        printHtmlHeader(out, prod);
        printHtmlFooter(out);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void printHtmlHeader(PrintWriter out, Product prod) throws IOException {
		String TITLE = (prod != null ? "Product of the day: " + prod.getProdName() : "No product today :(");
		String PHOTO = (prod != null ? "<img src=\"data:image/png;base64, " + prod.getPhotoData() + "\"></img>" : "");
		
        out.println("<body>");
        out.println("<html>");
        out.println("<head><title>" + TITLE + "</title></head>");
        out.println("<center><h1>" + TITLE + "</h1></center>");
        out.println("<div><center>" + PHOTO + "</center></div>");
        // Now I should insert all the reviews, but that's easy, so another day :)
    }
	
	private void printHtmlFooter(PrintWriter out) throws IOException {
        out.println("</html>");
        out.println("</body>");
        out.close();
    }

}
