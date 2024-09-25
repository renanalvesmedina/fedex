package com.mercurio.lms.rest.vendas;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestFilterDTO;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

@Path("/vendas/clienteSuggest")
public class ClienteSuggestRest extends BaseSuggestRest<ClienteSuggestDTO, ClienteSuggestFilterDTO> {
	
	@InjectInJersey ClienteService clienteService;

	@Override
	protected Map<String, Object> filterConvert(ClienteSuggestFilterDTO filter) {
		String value = filter.getValue();
		String modal = filter.getModal();
		String abrangencia = filter.getAbrangencia();
		String aux = PessoaUtils.clearIdentificacao(value);
		String tpCliente = filter.getTpCliente();

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
			data.put("tpCliente", tpCliente);
		}
		return data;
	}

	@Override
	protected ClienteSuggestDTO responseConvert(Map<String, Object> map) {
		ClienteSuggestDTO cliente = new ClienteSuggestDTO();
		
		if (map.get("IDCLIENTE") != null) {
			cliente.setId(Long.valueOf(((BigDecimal)map.get("IDCLIENTE")).toString()));
		}
		if (map.get("NMFANTASIA") != null) {
			cliente.setNmFantasia(map.get("NMFANTASIA").toString());
		}
		if (map.get("NMMUNICIPIO") != null) {
			cliente.setNmMunicipio(map.get("NMMUNICIPIO").toString());
		}
		if (map.get("NMPESSOA") != null) {
			cliente.setNmPessoa(map.get("NMPESSOA").toString());
		}
		if (map.get("NRCONTA") != null) {
			cliente.setNrConta(Long.valueOf(map.get("NRCONTA").toString()));
		} else {
			cliente.setNrConta(0L);
		}
		if (map.get("NRIDENTIFICACAO") != null) {
			cliente.setNrIdentificacao(map.get("NRIDENTIFICACAO").toString());
		}
		if (map.get("TP_CLIENTE") != null) {
			cliente.setTpCliente(map.get("TP_CLIENTE").toString());
		}
		if (map.get("SGUNIDADEFEDERATIVA") != null) {
			cliente.setSgUnidadeFederativa(map.get("SGUNIDADEFEDERATIVA").toString());
		}

		return cliente;
	}

	@Override
	protected ClienteService getService() {
		return clienteService;
	}

}