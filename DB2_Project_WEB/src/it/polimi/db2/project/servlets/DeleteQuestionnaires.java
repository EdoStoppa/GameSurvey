package it.polimi.db2.project.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.db2.project.services.ProdOfDayService;

/**
 * Servlet implementation class DeleteQuestionnares
 */
@WebServlet("/DeleteQuestionnaires")
public class DeleteQuestionnaires extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.project.services/ProdOfDayService")
	private ProdOfDayService prodOfDayService;

	public DeleteQuestionnaires() {
		super();
	}

	public void init() throws ServletException {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Integer productOfDayId = null;
		try {
			productOfDayId = Integer.parseInt(request.getParameter("productOfDayId"));
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}

		// Remove questionnaires
		try {
			prodOfDayService.deleteFor(productOfDayId);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting questionnaire data");
			return;
		}

		// Return view
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToAdminDeletePage";
		response.sendRedirect(path);

	}

	public void destroy() { }

}
