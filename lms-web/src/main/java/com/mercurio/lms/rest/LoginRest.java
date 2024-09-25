package com.mercurio.lms.rest;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import br.com.tntbrasil.integracao.domains.autenticacao.AutenticacaoDMN;
import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.security.model.service.AuthorizationService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.configuracoes.model.service.AutenticacaoService;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vol.model.service.VolEquipamentosService;

@Public
@Path("/")
public class LoginRest {
		
	@InjectInJersey
	AutenticacaoService autenticacaoService;
	
	@InjectInJersey
	VolEquipamentosService volEquipamentosService;

	@InjectInJersey
	IntegracaoJwtService integracaoJwtService;

	@POST
	@Path("/login")
	public Response login(AutenticacaoDMN login) {
		try {
			this.autenticacaoService.autenticar(login);
			return getDadosUsuarioLogado();
		} catch (Exception e){
			return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/loginLmsFront")
	public Response loginLmsFront(AutenticacaoDMN login) {
		login = autenticacaoService.autenticarLmsFront(login);
		return Response.ok(integracaoJwtService.montarUsuarioAutenticado(login)).build();
	}

    @POST
    @Path("/loginMWW")
    public Response loginMWW(AutenticacaoDMN login) {
        SessionContext.set("RedirectKey", "mww");
        
        Response response = this.login(login);
        
        Map<String, String> permissoes = SessionContext.get(AuthorizationService.LOGGED_ON_USER_PERMISSIONS_KEY);
        
        if (permissoes != null) {
        	for (String key : permissoes.keySet()) {
				if(key.contains("coletor_mww")){
					return response;
				}
			}
        }
        
        throw new InfrastructureException("ADSM_INVALID_USER_EXCEPTION_KEY", new String[]{"MWW"});
    }
    
	@GET
	@Path("/currentUser")
	public Response getDadosUsuarioLogado() {
		Map<String, Object> dados = new HashMap<>();
		try {
			dados.put("nmUsuario", SessionUtils.getUsuarioLogado().getNmUsuario());
			dados.put("login", SessionUtils.getUsuarioLogado().getLogin());
			dados.put("filial", SessionUtils.getFilialSessao().getPessoa().getNmFantasia());
			dados.put("empresa", SessionUtils.getEmpresaSessao().getPessoa().getNmPessoa());
			dados.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
			dados.put("idEmpresa", SessionUtils.getEmpresaSessao().getIdEmpresa());
			dados.put("sgFilial", SessionUtils.getFilialSessao().getSgFilial());
			dados.put("blSorter", SessionUtils.getFilialSessao().getBlSorter());
			dados.put("dsEmail", SessionUtils.getUsuarioLogado().getDsEmail());
			
			return Response.ok(dados).build();

		} catch (Exception e) {
			return Response.ok(dados).build();
		}
	}

	@GET
	@Path("/logout")
	public Response logout() {
		this.autenticacaoService.logout();
		return Response.ok().build();
	}
}
