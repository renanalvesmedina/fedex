package com.mercurio.lms.rest.vendas;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.rest.vendas.dto.CargoComissaoSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.CargoComissaoSuggestFilterDTO;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.vendas.model.service.CargoComissaoService;

@Path("/vendas/cargoComissaoSuggest")
public class CargoComissaoSuggestRest extends BaseSuggestRest<CargoComissaoSuggestDTO, CargoComissaoSuggestFilterDTO> { //TODO: criar classes 'suggest' para esses dto e referenciá-los aqui

	@InjectInJersey 
	CargoComissaoService cargoComissaoService;

	@Override
	protected Map<String, Object> filterConvert(CargoComissaoSuggestFilterDTO filter) {
		String value = filter.getValue();
		String modal = filter.getModal();
		String abrangencia = filter.getAbrangencia();
		String aux = PessoaUtils.clearIdentificacao(value);

		Map<String, Object> data = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(aux)) {
			
			if (StringUtils.isNumeric(aux)) {
				value = aux;
			}
			
			String nrIdentificacao = null;
			String nmPessoa = null;
			Long nrConta = null;
			
			if (StringUtils.isNumeric(value) && value.length() >= 8 && value.length() <= 14) {
				nrIdentificacao = value;
			}
			
			if (StringUtils.isNumeric(value) &&  value.length() <= 10) {
				nrConta = Long.valueOf(value);
			}
			
			if (nrIdentificacao == null && nrConta == null) {
				nmPessoa = value;
			}
			
			data.put("value",value);
			data.put("nrIdentificacao", nrIdentificacao);
			data.put("nrConta",nrConta);
			data.put("nmPessoa",nmPessoa);
			data.put("abrangencia",abrangencia);
			data.put("modal",modal);
		}
		return data;
	}

	@Override
	protected CargoComissaoSuggestDTO responseConvert(Map<String, Object> map) {
		CargoComissaoSuggestDTO cargoComissao = new CargoComissaoSuggestDTO();
		
		if (map.get("IDCLIENTE") != null) {
			cargoComissao.setId(Long.valueOf(((BigDecimal)map.get("IDCLIENTE")).toString()));
		}
		if (map.get("NMFANTASIA") != null) {
			cargoComissao.setNmFantasia(map.get("NMFANTASIA").toString());
		}
		if (map.get("NMMUNICIPIO") != null) {
			cargoComissao.setNmMunicipio(map.get("NMMUNICIPIO").toString());
		}
		if (map.get("NMPESSOA") != null) {
			cargoComissao.setNmPessoa(map.get("NMPESSOA").toString());
		}
		if (map.get("NRCONTA") != null) {
			cargoComissao.setNrConta(Long.valueOf(map.get("NRCONTA").toString()));
		} else {
			cargoComissao.setNrConta(0L);
		}
		if (map.get("NRIDENTIFICACAO") != null) {
			cargoComissao.setNrIdentificacao(map.get("NRIDENTIFICACAO").toString());
		}
		if (map.get("TP_CLIENTE") != null) {
			cargoComissao.setTpCliente(map.get("TP_CLIENTE").toString());
		}
		if (map.get("SGUNIDADEFEDERATIVA") != null) {
			cargoComissao.setSgUnidadeFederativa(map.get("SGUNIDADEFEDERATIVA").toString());
		}

		return cargoComissao;
	}

	@Override
	protected CargoComissaoService getService() {
		return cargoComissaoService;
	}

	
	
	
	
/*	
	/**
	 * Retorna dados para a suggest de FuncionarioComissionado.
	 * 
	 * @param data
	 * @return Response
	 *
	@POST
	@Path("findUsuarioComissionadoSuggest")
	public Response findUsuarioComissionadoSuggest(Map<String, Object> data) {
		String value = MapUtilsPlus.getString(data, "value");
		if (StringUtils.isBlank(value)) {	
			return Response.ok().build();
		}
		Integer limiteRegistros = getLimiteRegistros(value);
		return new SuggestResponseBuilder(cargoComissaoService.findUsuarioComissionadoSuggest(value, limiteRegistros == null ? null : limiteRegistros + 1), limiteRegistros).build();		
	}

	
	private Integer getLimiteRegistros(String value) {
		Integer limiteRegistros = null;
		Integer minimoCaracteresSuggest = Integer.parseInt(parametroGeralService.findByNomeParametro("VL_MINIMO_CARACTERES_SUGGEST", false).getDsConteudo());
		
		if (value.length() <= minimoCaracteresSuggest) {
			limiteRegistros = Integer.parseInt(parametroGeralService.findByNomeParametro("VL_LIMITE_REGISTROS", false).getDsConteudo());
		}
		
		return limiteRegistros;
	}
*/
	
}