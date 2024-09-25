package com.mercurio.lms.rest.configuracoes;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.rest.utils.SuggestResponseBuilder;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Rest responsável pelo UsuarioLMS.
 * 
 */
@Path("/configuracoes/usuarioLms")
public class UsuarioLmsRest {

	@InjectInJersey
	private UsuarioLMSService usuarioLMSService;
	
	@InjectInJersey
	private ParametroGeralService parametroGeralService;
	
	private Integer getLimiteRegistros(String value) {
		Integer limiteRegistros = null;
		Integer minimoCaracteresSuggest = Integer.parseInt(parametroGeralService.findByNomeParametro("VL_MINIMO_CARACTERES_SUGGEST", false).getDsConteudo());
		
		if (value.length() <= minimoCaracteresSuggest) {
			limiteRegistros = Integer.parseInt(parametroGeralService.findByNomeParametro("VL_LIMITE_REGISTROS", false).getDsConteudo());
		}
		
		return limiteRegistros;
	}
	
	/**
	 * Retorna dados para a suggest de UsuárioLMS.
	 * 
	 * @param data
	 * @return Response
	 */
	@POST
	@Path("findUsuarioLmsSuggest")
	public Response findUsuarioLmsSuggest(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");
		if (StringUtils.isBlank(value)) {	
			return Response.ok().build();
		}
		Integer limiteRegistros = getLimiteRegistros(value);
		return new SuggestResponseBuilder(usuarioLMSService.findUsuarioLmsSuggest(value, limiteRegistros == null ? null : limiteRegistros + 1), limiteRegistros).build();		
	}
	
	/**
	 * LMS-5590
	 * 
	 * Retorna o id e o nome do UsuárioLMS logado
	 * @return
	 */
	@GET
	@Path("findUsuarioLmsLogado")
	public TypedFlatMap findUsuarioLmsLogado() { 
		Usuario user = SessionUtils.getUsuarioLogado();
				
		TypedFlatMap result = new TypedFlatMap();
		result.put("idUsuario", user.getIdUsuario());
		result.put("nmUsuario", user.getNmUsuario());
		
		return result;
	}
}