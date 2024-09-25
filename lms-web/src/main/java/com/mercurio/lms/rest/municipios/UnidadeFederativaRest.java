package com.mercurio.lms.rest.municipios;

import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.rest.utils.SuggestResponseBuilder;

@Path("/municipios/manterUnidadeFederativa")
public class UnidadeFederativaRest {
	
	@InjectInJersey
	private UnidadeFederativaService unidadeFederativaService; 
	
	@InjectInJersey
	protected ParametroGeralService parametroGeralService;
	
	/**
	 * Retorna dados para a suggest de unidade federativa.
	 * 
	 * @param data
	 *            mapa com entrada "value" concatenado com "sgUnidadeFederativa"
	 *            e "nmUnidadeFederativa" e entrada para filtro opcional
	 *            "idPais".
	 * @return Response
	 */
	@POST
	@Path("findUnidadeFederativaSuggest")
	public Response findUnidadeFederativaSuggest(Map<String, Object> data) {
		Long idPais = MapUtils.getLong(data, "idPais");		
		String value = MapUtils.getString(data, "value");
		
		if (StringUtils.isBlank(value)) {	
			return Response.ok().build();
		}
			
		Integer limiteRegistros = getLimiteRegistros(value);
		
		String sgUnidadeFederativa = null;
		String nmUnidadeFederativa = null;		
		
		if(!StringUtils.isNumeric(value)) {
			if(value.length() < 3){
				sgUnidadeFederativa = value;
			} else {
				nmUnidadeFederativa = value;
			}
		}
			
		if(sgUnidadeFederativa == null && nmUnidadeFederativa == null){
			return Response.ok().build();
		}
		
		List<Map<String, Object>> findUnidadeFederativaSuggest = unidadeFederativaService.findUnidadeFederativaSuggest(idPais, sgUnidadeFederativa, nmUnidadeFederativa, limiteRegistros == null ? null : limiteRegistros + 1);
				
		return new SuggestResponseBuilder(findUnidadeFederativaSuggest, limiteRegistros).build();		
	}
	
	/**
	 * Retorna o número de resultados máximos para a suggest listar.
	 * 
	 * @param value
	 * @return Integer
	 */
	private Integer getLimiteRegistros(String value) {
		Integer limiteRegistros = null;
		Integer minimoCaracteresSuggest = Integer.parseInt(parametroGeralService.findByNomeParametro("VL_MINIMO_CARACTERES_SUGGEST", false).getDsConteudo());
		
		if (value.length() <= minimoCaracteresSuggest) {
			limiteRegistros = Integer.parseInt(parametroGeralService.findByNomeParametro("VL_LIMITE_REGISTROS", false).getDsConteudo());
		}
    		
		return limiteRegistros;
	}	
}