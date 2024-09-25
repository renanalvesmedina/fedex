package com.mercurio.lms.rest;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mercurio.adsm.core.web.HttpServletRequestHolder;

public abstract class BeanUtils {

	private static final String ADSM_BEANFACTORY = "com.mercurio.adsm.beanfactory";
	
	private BeanUtils() {
	}

	public static Object getBean(String bean) {
		
		HttpSession session = HttpServletRequestHolder.getHttpServletRequest().getSession();
		return getBean(bean, session);
	}
	
	public static <T> T getBean(Class<T> clazz) {

		HttpSession session = HttpServletRequestHolder.getHttpServletRequest().getSession();
		return getBean(clazz, session);
		
	}

	public static Object getBean(String bean, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		return getBean(bean, session);
	}

	private static Object getBean(String bean, HttpSession session) {
		ApplicationContext webApplicationContext = getApplicationContext(session);
		return webApplicationContext.getBean(bean);
	}
	
	public static <T> T getBean(Class<T> clazz, HttpServletRequest request) {

		HttpSession session = request.getSession();
		return getBean(clazz, session);
		
	}

	private static <T> T getBean(Class<T> clazz, HttpSession session) {
		
		ApplicationContext webApplicationContext = getApplicationContext(session);

		Map beansOfType = webApplicationContext.getBeansOfType(clazz);
		
		if (beansOfType == null || beansOfType.values().isEmpty() || beansOfType.values().size() > 1) {
			return null;
		}
		
		return (T) beansOfType.values().iterator().next();
	}

	private static ApplicationContext getApplicationContext(HttpSession session) {
		final ServletContext ctx = session.getServletContext();
		ApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(ctx);
		webApplicationContext = (ApplicationContext) webApplicationContext.getBean(ADSM_BEANFACTORY);
		return webApplicationContext;
	}
	
}
