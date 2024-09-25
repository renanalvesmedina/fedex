package com.mercurio.lms.rest.contratacaoveiculos;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.rest.utils.SuggestResponseBuilder;

@Path("/contratacaoveiculos/meioTransporte")
public class MeioTransporteRest {
	
	@InjectInJersey MeioTransporteService meioTransporteService;
	@InjectInJersey ParametroGeralService parametroGeralService;
	
	private static final int MAX_LENGTH_ID_MEIO_TRANSPORTE = 7;
	
	/**
	 * Retorna dados para a suggest de meio de transportes.
	 * 
	 * @param data
	 * @return Response
	 */
	@POST
	@Path("findMeioTransporteSuggest")
	public Response findProprietarioSuggest(Map<String, Object> data) {
		Long idProprietario = MapUtils.getLong(data, "idProprietario");	
		String value = MapUtils.getString(data, "value");
		if (StringUtils.isBlank(value)) {	
			return Response.ok().build();
		}
			
		Integer limiteRegistros = getLimiteRegistros(value);
								
		String nrIdentificacao = null;
		String nrFrota = null;		
		
		if (StringUtils.isAlphanumeric(value)) {
			if(value.length() < MAX_LENGTH_ID_MEIO_TRANSPORTE){
				nrFrota = value;
			} else {
				nrIdentificacao = value;
			}			
		}
			
		if(nrIdentificacao == null && nrFrota == null){
			return Response.ok().build();
		}
		
		return new SuggestResponseBuilder(meioTransporteService.findMeioTransporteSuggest(idProprietario, nrIdentificacao, nrFrota, limiteRegistros == null ? null : limiteRegistros + 1), limiteRegistros).build();	
		
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