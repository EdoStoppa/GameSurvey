package it.polimi.db2.project.servlets;

import java.io.IOException;
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
import it.polimi.db2.project.entities.FullAnswer;
import it.polimi.db2.project.services.AnswerLogService;
import it.polimi.db2.project.services.ProdOfDayService;
import it.polimi.db2.project.services.FullAnswerService;

/**
 * Servlet implementation class GoToUserAnswers
 */
@WebServlet("/GoToUserAnswers")
public class GoToUserAnswers extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "it.polimi.db2.project.services/ProdOfDayService")
	private ProdOfDayService prodOfDayService;
	@EJB(name = "it.polimi.db2.project.services/AnswerLogService")
	private AnswerLogService answerLogService;
	@EJB(name = "it.polimi.db2.project.services/FullAnswerLog")
	private FullAnswerService fullAnswerService;

	public GoToUserAnswers() {
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		Integer userId = null;
		Integer productOfDayId = null;
		AnswerLog answerLog = null;
		List<FullAnswer> fullAnswers = null;
		
		try {
			
			userId = Integer.parseInt(request.getParameter("userId"));
			productOfDayId = Integer.parseInt(request.getParameter("productOfDayId"));
						
			answerLog = answerLogService.getConfirmedAnswerForProductAndUser(productOfDayId, userId);
			fullAnswers = fullAnswerService.getAnswersForAnswerLog(answerLog.getId());
			
			ctx.setVariable("userId", userId);
			ctx.setVariable("fullAnswers", fullAnswers);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		
		String path = "/HTML/adminAnswersLookup.html";
		templateEngine.process(path, ctx, response.getWriter());

	}

	public void destroy() { }

}
