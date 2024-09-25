package com.mercurio.lms.rest.security;

import java.util.Collection;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.security.Resource;
import com.mercurio.adsm.framework.security.model.service.AuthorizationService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.security.dto.PermissionTabCmdDto;
import com.mercurio.lms.security.dto.PermissionViewActionDto;
import com.mercurio.lms.security.dto.PermissionWidgetPropertyDto;

@Path("/security/permission")
public class PermissionRest {
	
	private static final String TEM_PERMISSAO = "1";
	private static final String NAO_TEM_PERMISSAO = "0";

	@SuppressWarnings("unchecked")
	@GET
	@Path("t/{tela:.*}")
	public Response getPermissionsByTela(@PathParam("tela") String tela) {
	
		PermissionViewActionDto recursoDaTela = null;

		Map<String, String> permissions = (Map<String, String>) SessionContext.get(AuthorizationService.LOGGED_ON_USER_PERMISSIONS_KEY);
		if (permissions != null) {
			
			Collection<String> chavesRecursoTela = findPermissoesDaTela(tela, permissions);

			recursoDaTela = findRecursoTela(recursoDaTela, permissions, chavesRecursoTela);
			
			findRecursoAbas(recursoDaTela, permissions, chavesRecursoTela);
			
			findRecursosComponentes(recursoDaTela, permissions, chavesRecursoTela);
		}
		
		return Response.ok(recursoDaTela).build();
	}

	private void findRecursosComponentes(PermissionViewActionDto recursoDaTela, Map<String, String> permissions, Collection<String> chavesRecursoTela) {
		//Busca as permissões dos componentes
		if (recursoDaTela != null) {
			for (String chaveRecurso: chavesRecursoTela) {
				Resource resource = new Resource(chaveRecurso);
				if (resource.isWidget()) {
					
					PermissionTabCmdDto tab = recursoDaTela.getPermissions().get(resource.getTabCmd());
					//Se não existe a permissão específica da tab, herda da tela.
					if (tab == null) {
						tab = new PermissionTabCmdDto(resource.getTabCmd(), recursoDaTela.isPermission() ? TEM_PERMISSAO : NAO_TEM_PERMISSAO);
						recursoDaTela.getPermissions().put(resource.getTabCmd(), tab);
					}
					
					tab.getPermissions().put(resource.getWidgetProperty(), new PermissionWidgetPropertyDto(resource.getWidgetProperty(),  permissions.get(chaveRecurso)));
				}
			}
		}
	}

	private void findRecursoAbas(PermissionViewActionDto recursoDaTela, Map<String, String> permissions, Collection<String> chavesRecursoTela) {
		//Se tem permissão na tela, busca as permissões das abas
		if (recursoDaTela != null) {
			for (String chaveRecurso: chavesRecursoTela) {
				Resource resource = new Resource(chaveRecurso);
				if (resource.isTab()) {
					recursoDaTela.getPermissions().put(resource.getTabCmd(), new PermissionTabCmdDto(resource.getTabCmd(), permissions.get(chaveRecurso)));
				}
			}
		}
	}

	private PermissionViewActionDto findRecursoTela(PermissionViewActionDto recursoDaTela,	Map<String, String> permissions, Collection<String> chavesRecursoTela) {
		//Busca a permissão da tela
		for (String chaveRecurso: chavesRecursoTela) {
			Resource resource = new Resource(chaveRecurso);
			if (resource.isView()) {
				recursoDaTela = new PermissionViewActionDto(resource.getViewAction(), TEM_PERMISSAO.equals(permissions.get(chaveRecurso)));
			}
		}
		return recursoDaTela;
	}

	@SuppressWarnings({ "unchecked" })
	private Collection<String> findPermissoesDaTela(String urlTela,	Map<String, String> permissions) {
		final String chaveTela = criarChaveTela(urlTela);
		
		//Cria uma lista com as chaves pertinentes a tela
		Collection<String> keys = (Collection<String>)CollectionUtils.select(permissions.keySet(), new Predicate() {
			
			@Override
			public boolean evaluate(Object arg0) {
				String key = (String)arg0;
				if (key.contains(chaveTela+",") || key.endsWith(chaveTela)) {
					return true;
				}
				return false;
			}
		});
		return keys;
	}
	
	private String criarChaveTela(String urlTela) {
		String urlTelaTemp = urlTela;
		if (StringUtils.isNotBlank(urlTelaTemp)) {
			urlTelaTemp = adicionarBarraInicioUrl(urlTelaTemp);
			urlTelaTemp = removerBarraFinalUrl(urlTelaTemp);
		}
		
		return "t:"+urlTelaTemp;
	}

	private String removerBarraFinalUrl(String urlTelaTemp) {
		if ("/".equals(urlTelaTemp.substring(urlTelaTemp.length()-1))) {
			return urlTelaTemp.substring(0, urlTelaTemp.length()-1);
		}
		return urlTelaTemp;
	}

	private String adicionarBarraInicioUrl(String urlTelaTemp) {
		if (!urlTelaTemp.startsWith("/")) {
			return "/"+urlTelaTemp;
		}
		return urlTelaTemp;
	}
}
