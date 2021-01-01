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

import it.polimi.db2.project.entities.User;

/**
 * Servlet Filter implementation class BlockedUserFilter
 */
@WebFilter("/BlockedUserFilter")
public class BlockedUserFilter implements Filter {

    /**
     * Default constructor. 
     */
    public BlockedUserFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		System.out.println("Checking if User is blocked...");

		// java.lang.String loginpath = "/index.html";
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String blockedPath = req.getServletContext().getContextPath() + "/GoToBlocked";

		HttpSession s = req.getSession();
		
		User user = (User) s.getAttribute("user");
		if (s.isNew() || s.getAttribute("user") != null) {
			
			if (user.getBlocked()) {
				System.out.println("Check failed, redirect to BLOCKED...");
				res.sendRedirect(blockedPath);
				return;
			} else {
				System.out.println("Check succeded");
			}
			
		} 
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
