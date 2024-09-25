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
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.rest.utils.SuggestResponseBuilder;

@Path("/municipios/manterMunicipio")
public class MunicipioRest {
	
	@InjectInJersey
	private MunicipioService municipioService; 
	
	@InjectInJersey
	protected ParametroGeralService parametroGeralService;
	
	/**
	 * Retorna dados para a suggest de município.
	 * 
	 * @param data
	 *            mapa com entrada "value" concatenado com nmMunicipio" e
	 *            entrada para filtro opcional "idPais", "idUnidadeFederativa".
	 * @return Response
	 */
	@POST
	@Path("findMunicipioSuggest")
	public Response findMunicipioSuggest(Map<String, Object> data) {
		Long idPais = MapUtils.getLong(data, "idPais");
		Long idUnidadeFederativa = MapUtils.getLong(data, "idUnidadeFederativa");
		String value = MapUtils.getString(data, "value");
		
		if (StringUtils.isBlank(value)) {	
			return Response.ok().build();
		}
			
		Integer limiteRegistros = getLimiteRegistros(value);
		
		String nmMunicipio = null;		
		
		if(!StringUtils.isNumeric(value)) {
			nmMunicipio = value;			
		}
			
		if(nmMunicipio == null){
			return Response.ok().build();
		}
		
		List<Map<String, Object>> findMunicipioSuggest = municipioService.findMunicipioSuggest(idPais, idUnidadeFederativa, nmMunicipio, limiteRegistros == null ? null : limiteRegistros + 1);
				
		return new SuggestResponseBuilder(findMunicipioSuggest, limiteRegistros).build();		
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