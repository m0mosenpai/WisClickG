package org.wisclick;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Creates a CORS filter for requests to WisClick by
 * setting required CORS headers
 * 
 * @author Amelia Sitzberger
 */
public class CorsFilter implements Filter {

	/**
	 * Initialization of filter is not needed
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * Sets required headers for CORS requests
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setHeader("Access-Control-Allow-Origin", "http://example.com");
		httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		httpResponse.setHeader("Access-Control-Max-Age", "3600");
		chain.doFilter(request, response);
	}

	/**
	 * Cleanup if needed
	 */
	@Override
	public void destroy() {
	}
}
