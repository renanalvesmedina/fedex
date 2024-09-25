package com.mercurio.lms.rest.configuracoes;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.autenticacao.AutenticacaoDMN;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.rest.configuracoes.dto.MeuPerfilDTO;

import java.util.Map;

@Path("/configuracoes/meuPerfil") 
public class MeuPerfilRest extends BaseRest { 
	
	@InjectInJersey
	UsuarioService usuarioService;

	@InjectInJersey
	private IntegracaoJwtService integracaoJwtService;
	
	@POST
	@Path("atualizarFilial")
	public Response atualizarFilial(MeuPerfilDTO meuPerfilDTO) {
		usuarioService.loadMeuPerfil(convertDTOtoTypedFlatMap(meuPerfilDTO));
		return Response.ok().build();
	}

	@POST
	@Path("loadMeuPerfilByToken")
	public Response loadMeuPerfilByToken(MeuPerfilDTO meuPerfilDTO, @Context HttpHeaders headers) {
		String token = headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0);
		Map<String, Object> usuarioLogado = integracaoJwtService.montarDados(
			integracaoJwtService.findAutenticacaoDMN(token),
			integracaoJwtService.getUsuarioSessao(integracaoJwtService.findAutenticacaoDMN(token)),
			integracaoJwtService.getFilialSessao(meuPerfilDTO.getIdFilial()),
			integracaoJwtService.getEmpresaSessao(meuPerfilDTO.getIdEmpresa())
		);
		return Response.ok(usuarioLogado).build();
	}

	private TypedFlatMap convertDTOtoTypedFlatMap(MeuPerfilDTO meuPerfilDTO) {
		TypedFlatMap m = new TypedFlatMap();
		
		m.put("empresaLogado.idEmpresa", meuPerfilDTO.getIdEmpresa());
		m.put("filialLogado.idFilial", meuPerfilDTO.getIdFilial());
		
		return m;
	}

	public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
		this.integracaoJwtService = integracaoJwtService;
	}
} 
