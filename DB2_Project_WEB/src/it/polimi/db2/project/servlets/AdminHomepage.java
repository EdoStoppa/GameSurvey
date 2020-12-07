package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Homepage
 */
@WebServlet("/AdminHomepage")
public class AdminHomepage extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminHomepage() {
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
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing button action");
			return;
		}
		
		if (pressedButton.equals("Create")) {
			String path = getServletContext().getContextPath() + "/AdminCreate";
			response.sendRedirect(path);
		}
		
		if (pressedButton.equals("Inspect")) {
			String path = getServletContext().getContextPath() + "/AdminInspect";
			response.sendRedirect(path);
		}

		if (pressedButton.equals("Delete")) {
			String path = getServletContext().getContextPath() + "/AdminDelete";
			response.sendRedirect(path);
		}
		
	}
	
	private void printHtmlHeader(PrintWriter out) throws IOException {
        out.println("<body>");
        out.println("<html>");
        out.println("<head><title> Admin </title></head>");
        out.println("<center><h1> Administration Page </h1></center>");
        out.println("<p> Please select an action </p>");
        out.println("</hr>");
        out.println("<form action=\"AdminHomepage\" method=\"POST\">");
        out.println("<input name=\"action\" type=\"submit\" value=\"Create\"/>");
        out.println("<input name=\"action\" type=\"submit\" value=\"Inspect\"/>");
        out.println("<input name=\"action\" type=\"submit\" value=\"Delete\"/>");
        out.println("</form>");
        out.println("<hr/>");
    }
	
	private void printHtmlFooter(PrintWriter out) throws IOException {
        out.println("</html>");
        out.println("</body>");
        out.close();
    }

}
