package it.polimi.db2.project.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoggedIn
 */
@WebFilter("/AdminFilter")
public class AdminFilter implements Filter {

    /**
     * Default constructor. 
     */
    public AdminFilter() { }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() { }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.print("Admin filter executing ...\n");

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String redirectPath = req.getServletContext().getContextPath() + "/Homepage";

		HttpSession s = req.getSession();
		if (s.isNew() || s.getAttribute("admin") == null || (boolean)s.getAttribute("admin") == false) {
			res.sendRedirect(redirectPath);
			return;
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException { }

}
