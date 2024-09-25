package com.mercurio.lms.rest.perfil;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.service.PerfilService;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.rest.utils.SuggestResponseBuilder;

/**
 * 
 * Rest responsável pela consulta de perfil
 *
 */
@Path("/perfil/perfilAdsm")
public class PerfilRest {
	
	@InjectInJersey
	private PerfilService perfilService;
	
	@InjectInJersey
	private ParametroGeralService parametroGeralService;
	
	/**
	 * Retorna dados para a suggest de Perfil.
	 * 
	 * @param data
	 * @return Response
	 */
	
	@POST
	@Path("findPerfilAdsmSuggest")
	public Response findPerfilAdsmSuggest(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");
		if (StringUtils.isBlank(value)) {	
			return Response.ok().build();
		}
		Integer limiteRegistros = getLimiteRegistros(value);
		return new SuggestResponseBuilder(perfilService.findPerfilAdsmSuggest(
				value, limiteRegistros == null ? null : limiteRegistros + 1), limiteRegistros).build();		
	}
	
	private Integer getLimiteRegistros(String value) {
		Integer limiteRegistros = null;
		Integer minimoCaracteresSuggest = Integer.parseInt(parametroGeralService.findByNomeParametro("VL_MINIMO_CARACTERES_SUGGEST", false).getDsConteudo());
		
		if (value.length() <= minimoCaracteresSuggest) {
			limiteRegistros = Integer.parseInt(parametroGeralService.findByNomeParametro("VL_LIMITE_REGISTROS", false).getDsConteudo());
		}
		
		return limiteRegistros;
	}
}