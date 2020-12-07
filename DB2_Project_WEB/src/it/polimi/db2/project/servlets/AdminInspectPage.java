package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.db2.project.services.ProdOfDayService;
import it.polimi.db2.project.entities.AnswerLog;
import it.polimi.db2.project.entities.ProdOfDay;
import it.polimi.db2.project.entities.User;
import it.polimi.db2.project.services.AnswerLogService;


/**
 * Servlet implementation class AdminInspectPage
 */
@WebServlet("/AdminInspect")
public class AdminInspectPage extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.db2.project.services/ProdOfayService")
	private ProdOfDayService productOfDayService;
	@EJB(name = "it.polimi.db2.project.services/AnswerLogService")
	private AnswerLogService answerLogService;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminInspectPage() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		HttpSession session = req.getSession();
		
		Date selectedDate = (Date) session.getAttribute("date");
		
		// Checks if a date has been selected or not
		if (selectedDate == null) {
			
		    printHtmlHeader(out);
			printHtmlDatePicker(out);
		    printHtmlFooter(out);
			
		} else {
			
			printHtmlHeader(out);
			printHtmlStatsSection(out, selectedDate);
		    printHtmlFooter(out);
		    
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {

			// Extract the date and sets it into the session
			String selectedDate = null;
			selectedDate = request.getParameter("date");
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date actualDate = (Date) dateFormat.parse(selectedDate);
			
			request.getSession().setAttribute("date", actualDate);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error in data parsing");
			return;
		}
		
		String path = getServletContext().getContextPath() + "/AdminInspect";
		response.sendRedirect(path);
		
	}
	
	private void printHtmlHeader(PrintWriter out) throws IOException {
		
        out.println("<body>");
        out.println("<html>");
        out.println("<head><title> Admin </title></head>");
        out.println("<center><h1> Inspection Page </h1></center>");
        
    }
	
	private void printHtmlDatePicker(PrintWriter out) throws IOException {
		
		out.println("<form action=\"AdminInspect\" method=\"POST\">");
        out.println("<input type=\"date\" name=\"date\" value=\"01-01-1971\"/>");	
        out.println("<input name=\"action\" type=\"submit\" value=\"Select\"/>");
		out.println("</form>");
		
	}
	
	private void printHtmlStatsSection(PrintWriter out, Date date) throws IOException {
		
		
		ProdOfDay prodOfDay = null;
		List<AnswerLog> answersLog = null;
		
		try {
			
			prodOfDay = productOfDayService.getProductOfDayFor(date);
			answersLog = answerLogService.getAnswersForProduct(prodOfDay.getId());
			
			List<String> confirmedUsers = new ArrayList<String>();
			List<String> cancelledUsers = new ArrayList<String>();
			
			// Filters the answers between confirmed and not, populating the lists of
			// users previously defined
			for (AnswerLog answerLog : answersLog) {
				
				if (answerLog.getConfirmed()) {
					confirmedUsers.add(answerLog.getUser().getEmail());
				} else {
					cancelledUsers.add(answerLog.getUser().getEmail());
				}
				
			}
			
			// Gets the tables
			String confirmedTable = createTableFrom(Arrays.asList("Confirmed"), confirmedUsers);
			String cancelledTable = createTableFrom(Arrays.asList("Cancelled"), cancelledUsers);
			
			// Prints the tables
			out.println(confirmedTable);
			out.println(cancelledTable);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		}
		
	}
	
	private String createTableFrom(List<String> titles, List<String> fields) {
		
		StringBuilder stringBuilder = new StringBuilder();
		int rowLength = titles.size();
		
		stringBuilder.append("<center> <table style = 'width:75%'>");
		
		// Title's section
		stringBuilder.append("<tr>");
		for (String entry : titles) {
			stringBuilder.append("<th>" + entry + "</th>");
		}
		stringBuilder.append("</tr>");
		
		// Body's section
		for(int index = 0; index < fields.size(); index++) {

			int relativeIndex = index % rowLength;
			
			if (relativeIndex == 0) {
				stringBuilder.append("<tr>");
			}
			
			stringBuilder.append("<td>");
			stringBuilder.append(fields.get(index));
			stringBuilder.append("</td>");
			
			if (relativeIndex == (rowLength - 1)) {
				stringBuilder.append("</tr>");
			}
			
		}

		stringBuilder.append("</table> </center>");
		
		return stringBuilder.toString();
		
	}
	
	private void printHtmlFooter(PrintWriter out) throws IOException {
        out.println("</html>");
        out.println("</body>");
        out.close();
    }

}
