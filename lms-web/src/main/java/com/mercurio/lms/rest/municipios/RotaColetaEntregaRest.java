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
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.rest.utils.SuggestResponseBuilder;

@Path("/municipios/manterRotaColetaEntrega")
public class RotaColetaEntregaRest {
	
	@InjectInJersey
	private RotaColetaEntregaService rotaColetaEntregaService; 
	
	@InjectInJersey
	protected ParametroGeralService parametroGeralService;
	
	/**
	 * Retorna dados para a suggest de rota de coleta/entrega.
	 * 
	 * @param data
	 *            mapa com entrada "value" concatenado com dsRota/nrRota e
	 *            entrada para filtro opcional "idFilial".
	 * @return Response
	 */
	@POST
	@Path("findRotaColetaEntregaSuggest")
	public Response findRotaColetaEntregaSuggest(Map<String, Object> data) {
		Long idFilial = MapUtils.getLong(data, "idFilial");
		String value = MapUtils.getString(data, "value");
		
		if (StringUtils.isBlank(value)) {	
			return Response.ok().build();
		}
			
		Integer limiteRegistros = getLimiteRegistros(value);
		
		String dsRota = null;
		String nrRota = null;
		
		if(!StringUtils.isNumeric(value)) {
			dsRota = value;			
		} else {
			nrRota = value;
		}
		
		List<Map<String, Object>> findRotaColetaEntregaSuggest = rotaColetaEntregaService.findRotaColetaEntregaSuggest(idFilial, nrRota, dsRota, limiteRegistros == null ? null : limiteRegistros + 1);
				
		return new SuggestResponseBuilder(findRotaColetaEntregaSuggest, limiteRegistros).build();		
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