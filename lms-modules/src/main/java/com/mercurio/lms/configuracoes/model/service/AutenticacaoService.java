package com.mercurio.lms.configuracoes.model.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.security.model.service.AuthorizationService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.util.session.SessionKey;
import br.com.tntbrasil.integracao.domains.autenticacao.AutenticacaoDMN;
import br.com.tntbrasil.integracao.domains.autenticacao.MenuItemDMN;
import br.com.tntbrasil.integracao.domains.autenticacao.PermissaoDMN;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import javax.servlet.ServletContext;
import javax.ws.rs.NotAuthorizedException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.mercurio.adsm.core.web.ServletContextHolder;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;

@ServiceSecurity
public class AutenticacaoService {
	
	private static final String METODO_AUTENTICAR = "autenticar";
	private static final String METODO_FIND_PERMISSOES = "findPermissoes";
	private static final String METODO_FIND_MENU = "findMenuItens";
	private static final String SISTEMA = "LMS";
	private static final String SERVICO_AUTENTICACAO = "autenticacaousuariointerno";
	private static final String SMS_AUTH_ADDRESS = "sms.auth.address";
	

	private SessionContentLoaderService sessionContentLoaderService;
	private UsuarioService usuarioService;	
	private ConfiguracoesFacade configuracoesFacade;
	
	@MethodSecurity(processGroup = "lms.security", processName = "Authentication-byMap-SMS", authenticationRequired = false)
	public void login(Map<?,?> map) {
		AutenticacaoDMN autenticacao = new AutenticacaoDMN();
		autenticacao.setLogin(map.get("username").toString());
		autenticacao.setSenha(map.get("password").toString());
		autenticacao.setSistema(map.get("system").toString());
		autenticar(autenticacao);
	}
	
	public void validarTokenOkta(String token) {
		
		ApplicationContext applicationContext = getApplicationContext();		
		
		configuracoesFacade = (ConfiguracoesFacade) applicationContext.getBean("lms.configuracoesFacade");
		
		String JWT_SERVICE_URL = (String) configuracoesFacade.getValorParametro("JWT_SERVICE_HOST");
		Client client = ClientBuilder.newClient();
		
		WebTarget target = client.target(JWT_SERVICE_URL).path("okta");

		Response responseValidation = target.request(MediaType.APPLICATION_JSON_TYPE)
		    .post(Entity.entity(token,MediaType.APPLICATION_JSON),
		    		Response.class);
		
		if(responseValidation.getStatus() != 200){
			throw new NotAuthorizedException("Token inválido");
		}
		
	}
	
	private ApplicationContext getApplicationContext(){
		
		final ServletContext servletContext = ServletContextHolder.getServletContext();
		ApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		webApplicationContext = (ApplicationContext) webApplicationContext.getBean("com.mercurio.adsm.beanfactory");
		
		return webApplicationContext;
	}

	public void autenticar(AutenticacaoDMN autenticacaoDMN) {
		AutenticacaoDMN autenticado = postLogin(autenticacaoDMN);
		if (autenticado.isAutenticado()) {
			autenticado.setSenha(autenticacaoDMN.getSenha());
			initSessao(autenticado);
			return;
		}
		logout();
		throw new InfrastructureException(autenticado.getMensagem());
    }

	public AutenticacaoDMN autenticarLmsFront(AutenticacaoDMN autenticacaoDMN) {
		return postLogin(autenticacaoDMN);
	}

	public void validateAutenticacao(AutenticacaoDMN autenticacaoDMN){
		AutenticacaoDMN autenticado = postLogin(autenticacaoDMN);
		if (!autenticado.isAutenticado()) {
			throw new InfrastructureException(autenticado.getMensagem());
		}
    }

	private void initSessao(AutenticacaoDMN autenticacaoDMN) {
		Usuario usuario = null;
		if(autenticacaoDMN.getLoginOkta() != null && autenticacaoDMN.getLoginOkta()) {
			usuario = usuarioService.findUsuarioByLoginIdFedex(autenticacaoDMN.getLoginIdFedex());
		}else{
			usuario = usuarioService.findUsuarioByLogin(autenticacaoDMN.getLogin());
		}
		if (usuario == null) {
			throw new InfrastructureException("ADSM_INVALID_USER_EXCEPTION_KEY", new String[]{SISTEMA});
		}
		autenticacaoDMN.setAutenticado(true);
		SessionContext.setUser(usuario);
		sessionContentLoaderService.execute();
		SessionContext.set(AuthorizationService.LOGGED_ON_USER_PERMISSIONS_KEY,
				buildPermissoesUsuario(findPermissoes(autenticacaoDMN)));
		SessionContext.set(SessionKey.MENU_KEY, findMenu(autenticacaoDMN));
	}

	public void logout() {
		SessionContext.removeAll();
    }

	private Map<String, String> buildPermissoesUsuario(List<PermissaoDMN> permissoes) {
		Map<String, String> permissoesList = new HashMap<String, String>();
		for (PermissaoDMN permissaoDMN : permissoes) {
			permissoesList.put(permissaoDMN.getCdRecurso(), permissaoDMN.getTpPermissao());
		}
		return permissoesList;
	}
	
	private AutenticacaoDMN postLogin(AutenticacaoDMN autenticacaoDMN) {
		return buildWebTarget(METODO_AUTENTICAR)
				.post(Entity.entity(autenticacaoDMN, MediaType.APPLICATION_JSON), AutenticacaoDMN.class);
	}

	/**
	 * Chama o serviço sms-auth
	 * Os parametros da Url esta dentro das variáveis de ambiente da VM do Weblogic
	 * */
	public List<PermissaoDMN> findPermissoes(AutenticacaoDMN autenticacaoDMN) {
		autenticacaoDMN.setSistema(SISTEMA);
		/*Modified by Lucas Afonso Bello for upgrade to Weblogic 12.2*/
		Response response = buildWebTarget(METODO_FIND_PERMISSOES).post(Entity.entity(autenticacaoDMN, MediaType.APPLICATION_JSON));
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		List<PermissaoDMN> permissoes = new ArrayList<PermissaoDMN>();
		try {
			List<MultilevelJson> objectList = null;
			objectList = Arrays.asList(mapper.readValue(response.readEntity(String.class), MultilevelJson[].class));
			for (MultilevelJson obj : objectList) {
				PermissaoDMN permissao = new PermissaoDMN(obj.tpPermissao.asText(), obj.cdRecurso);
				permissoes.add(permissao);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return permissoes;

	}

	public List<MenuItemDMN> findMenu(AutenticacaoDMN autenticacaoDMN) {
		autenticacaoDMN.setSistema(SISTEMA);

		/*Modified by Lucas Afonso Bello for upgrade to Weblogic 12.2*/
		Response response = buildWebTarget(METODO_FIND_MENU).post(Entity.entity(autenticacaoDMN, MediaType.APPLICATION_JSON));
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		List<MenuItemDMN> objectList = new ArrayList<MenuItemDMN>();
		try {
			objectList = mapper.readValue(response.readEntity(String.class), new TypeReference<List<MenuItemDMN>>() {
			});
			return objectList;
		} catch (IOException e) {
			e.printStackTrace();
			return objectList;
		}

	}

	private Builder buildWebTarget(String metodo) {
		/*Modified by Lucas Afonso Bello for upgrade to Weblogic 12.2*/
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(System.getProperty(SMS_AUTH_ADDRESS)).path(SERVICO_AUTENTICACAO + "/" + metodo);
		return target.request(MediaType.APPLICATION_JSON);

	}
	
	public void setSessionContentLoaderService(SessionContentLoaderService sessionContentLoaderService) {
		this.sessionContentLoaderService = sessionContentLoaderService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	/**
	 * Class created because Jackson needs a static class and a empty constructor to receive a internal Json.
	 * It's a clone of PermissaoDMN class with static property and a empty constructor.
	 * @author Lucas Afonso Bello
	 * */
	private static class MultilevelJson {

		private String cdRecurso;

		private JsonNode tpPermissao;

		public MultilevelJson() {
		}

		public MultilevelJson(JsonNode tpPermissao, String cdRecurso) {
			this.tpPermissao = tpPermissao;
			this.cdRecurso = cdRecurso;
		}

		public String getCcRecurso() {
			return cdRecurso;
		}

		public void setCdRecurso(String cdRecurso) {
			this.cdRecurso = cdRecurso;
		}

		public JsonNode getTpPermissao() {
			return tpPermissao;
		}

		public void setTpPermissao(JsonNode tpPermissao) {
			this.tpPermissao = tpPermissao;
		}

	}

}
