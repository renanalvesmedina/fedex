package com.mercurio.lms.rest.vendas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.rest.vendas.dto.FuncionarioPromotorSuggestDTO;
import com.mercurio.lms.util.FormatUtils;

@Path("/vendas/promotor")
public class PromotorVendasRest {

	@InjectInJersey private UsuarioService usuarioService;
	
	@POST
	@Path("findFuncionarioPromotorSuggest")
	public Response findFuncionarioPromotorSuggest(Map<String, Object> criteria) {
		String value = criteria.get("value").toString();
		
		String nmFuncionario = null;
		String nrMatricula = null;
				
		if (StringUtils.isNumeric(value)){
			nrMatricula = FormatUtils.fillNumberWithZero(value, 9); 
		}else{
			nmFuncionario = "%"+value.trim()+"%";
		}
		
		List entidades = usuarioService.findSuggestUsuarioPromotor(nrMatricula, nmFuncionario);
		return Response.ok(this.converteListaParaJSON(entidades)).build();
	}
	
	private List<FuncionarioPromotorSuggestDTO> converteListaParaJSON(List<Usuario> usuarios) {
		List<FuncionarioPromotorSuggestDTO> resultado = new ArrayList<FuncionarioPromotorSuggestDTO>();
		for (Usuario usuario : usuarios) {
			resultado.add(converteUsuarioParaJSON(usuario));
		}
		return resultado;
	}

	private FuncionarioPromotorSuggestDTO converteUsuarioParaJSON(Usuario usuario) {
		return new FuncionarioPromotorSuggestDTO(usuario.getIdUsuario(), usuario.getNmUsuario(), usuario.getNrMatricula(), usuario.getVfuncionario().getNrCpf());
	}

	
}
