package com.mercurio.lms.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.web.ServletContextHolder;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.AutenticacaoService;

import br.com.tntbrasil.integracao.domains.autenticacao.AutenticacaoDMN;
import br.com.tntbrasil.integracao.domains.autenticacao.PermissaoDMN;

public class PermissaoServicesFilter implements Filter{
	
	@InjectInJersey
	private AutenticacaoService autenticacaoService;
	
	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
		
		HttpServletRequest httpServletRequest = getHttpSession(request);
		ApplicationContext applicationContext = getApplicationContext();
		
		autenticacaoService = (AutenticacaoService) applicationContext.getBean( "lms.configuracoes.autenticacaoService");
		
		AutenticacaoDMN autenticacaoDMN = getAutenticacaoDMN(httpServletRequest);
		if(StringUtils.isBlank(autenticacaoDMN.getLogin()) || StringUtils.isBlank(autenticacaoDMN.getSenha())){
			configuracoesFacade = (ConfiguracoesFacade) applicationContext.getBean( "lms.configuracoesFacade");
			throw new InfrastructureException(configuracoesFacade.getMensagem("LMS-49002", new String[]{"LMS"}));
		}
		
		autenticacaoService.validateAutenticacao(autenticacaoDMN);
		
		List<PermissaoDMN> listPermissao = autenticacaoService.findPermissoes(autenticacaoDMN);
		//String dsUri = httpServletRequest.getServletPath().concat(httpServletRequest.getPathInfo());
		String dsUri = httpServletRequest.getPathInfo();
		
		boolean hasPermissao = validaPermissao(listPermissao, dsUri);
		if(!hasPermissao){
			configuracoesFacade = (ConfiguracoesFacade) applicationContext.getBean( "lms.configuracoesFacade");
			throw new InfrastructureException(configuracoesFacade.getMensagem("LMS-49003", new Object[]{dsUri}), new String[]{"LMS"});
		}
	
		chain.doFilter(request, response);
	}

	private HttpServletRequest getHttpSession(ServletRequest request) {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		httpServletRequest.getSession(true);
		return httpServletRequest;
	}

	@Override
	public void destroy() {
		
	}

	private ApplicationContext getApplicationContext(){
		
		final ServletContext servletContext = ServletContextHolder.getServletContext();
		ApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		webApplicationContext = (ApplicationContext) webApplicationContext.getBean("com.mercurio.adsm.beanfactory");
		
		return webApplicationContext;
	}

	private boolean validaPermissao(List<PermissaoDMN> listPermissao,String dsUri){
		boolean hasPermissao = false;
		for(PermissaoDMN permissaoDMN : listPermissao ){
			
			if( dsUri.equalsIgnoreCase(permissaoDMN.getCdRecurso().replace("s:LMS,m:", "")) ){
				hasPermissao = true;
				break;
			}
	
		}
		
		return hasPermissao;
	}
	
	private AutenticacaoDMN getAutenticacaoDMN(HttpServletRequest httpServletRequest){
		
		String login = httpServletRequest.getHeader("loginservices");
		String senha = httpServletRequest.getHeader("senhaservices");
				
		httpServletRequest.getHeaderNames();
		
		AutenticacaoDMN autenticacaoDMN = new AutenticacaoDMN();
		autenticacaoDMN.setLogin(login);
		autenticacaoDMN.setSenha(senha);
		
		return autenticacaoDMN;
		
	}


}
