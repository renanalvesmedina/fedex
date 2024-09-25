package com.mercurio.lms.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessControlHeadersFilter implements Filter {

	private String oldUrlSufix = "old.";
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String urlAccept = request.getRequestURL().toString();
		urlAccept = urlAccept.substring(0, urlAccept.indexOf(request.getContextPath()));
		urlAccept = urlAccept.replace(oldUrlSufix, "");
		response.setHeader("Access-Control-Allow-Origin", urlAccept);
		response.setHeader("Access-Control-Allow-Headers", "content-type");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String oldUrlSufix = filterConfig.getInitParameter("old-url-sufix");
		if (oldUrlSufix != null) {
			this.oldUrlSufix = oldUrlSufix;
		}
	}
	
	@Override
	public void destroy() {}
	
}
