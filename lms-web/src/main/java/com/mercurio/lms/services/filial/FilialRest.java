package com.mercurio.lms.services.filial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.integration.convert.Sigla2Filial;
import com.mercurio.adsm.framework.integration.convert.Sigla2FilialConverter;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.integracao.model.dao.DominioVinculoIntegracaoDAO;
import com.mercurio.lms.integracao.model.service.DominioVinculoIntegracaoService;
import com.mercurio.lms.municipios.dto.FilialAtendimentoDto;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;

@Path("/filial")
public class FilialRest {

	@InjectInJersey FilialService filialService;
	@InjectInJersey UsuarioService usuarioService;
	@InjectInJersey EmpresaService empresaService;
	
	/**
	 * Responsavel por retornar dados da filial de atendimento
	 * @param id
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findFilialAtendimento")
	public Response findContato(Long idFilialAtendimentoCliente) {
		FilialAtendimentoDto filialAtendimento = null;
		if(idFilialAtendimentoCliente != null && idFilialAtendimentoCliente.longValue() > 0){
			filialAtendimento = filialService.findFilialAtendimento(idFilialAtendimentoCliente);
		}
		return Response.ok(filialAtendimento).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findFilialUsuario")
	public Response findFilialUsuario(String login) {
		String sgFilial = null;
		if(login != null) {
			Usuario user = usuarioService.findUsuarioByLogin(login);
			Empresa empresa = empresaService.findById(user.getEmpresaPadrao().getIdEmpresa());
			Filial filial = filialService.findFilialPadraoByUsuarioEmpresa(user, empresa);
			
			Map<Long, String> mapSgFilial = filialService.findSiglaComDoisDigitos();
			
			sgFilial = mapSgFilial.get(filial.getIdFilial());
		}
		return Response.ok(sgFilial).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findFiliaisUsuario")
	public Response findFiliaisUsuario(String login) {
		List<String> filialList = new ArrayList<String>();
		if(login != null) {
			Usuario user =  usuarioService.findUsuarioByLogin(login);
			Empresa empresa = empresaService.findById(user.getEmpresaPadrao().getIdEmpresa());
			List<Filial> filiais = filialService.findFiliaisByUsuarioEmpresa(user, empresa, null);
			
			Map<Long, String> mapSgFilial = filialService.findSiglaComDoisDigitos();
			
			for (Filial filial : filiais) {
				filialList.add(mapSgFilial.get(filial.getIdFilial()));
			}
		}
		return Response.ok(filialList).build();
	}
	
}
