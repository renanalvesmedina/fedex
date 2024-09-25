package com.mercurio.lms.expedicao.swt.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.service.ConhecimentoNormalService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoSubstitutoService;
import com.mercurio.lms.expedicao.model.service.DigitarDadosNotaNormalCalculoCTRCService;
import com.mercurio.lms.vendas.model.service.CotacaoService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;

public class DigitarConhecimentoSubstitutoAction extends DigitarNotaAction {

	private ConhecimentoSubstitutoService conhecimentoSubstitutoService ;
	private ConhecimentoNormalService conhecimentoNormalService;
	private ParametroGeralService parametroGeralService;
	private DigitarDadosNotaNormalCalculoCTRCService digitarDadosNotaNormalCalculoCTRCService;
	private DivisaoClienteService divisaoClienteService;
	
	public List<Map<String, Object>> findDoctoServicoOriginalParaSubstituir(Map<String, Object> criteria) {
		return conhecimentoSubstitutoService.findDoctoServicoOriginalParaSubstituir(criteria);
	}
	
	public Map validateDhEmissaoNFAnulacao(Map<String, Object> criteria){
		return conhecimentoSubstitutoService.validateDhEmissaoNFAnulacao(criteria);
	}
	
	
	public Map<String, Object> executePrimeiraFase(Map<String, Object> params) {
		return conhecimentoSubstitutoService.executePrimeiraFase(params);
	}
	
	public Map<String, Object> executeSegundaFase(Map<String, Object> parameters) {
		return conhecimentoSubstitutoService.executeSegundaFase(parameters);
	}
	
	public Serializable storeConhecimentoSubstitutoManual(Map<String, Object> parameters) {
		HashMap<String, Object> retorno = conhecimentoNormalService.validateExistenciaPCE((Conhecimento) parameters.get("conhecimento"));
		if(retorno == null) {
			return conhecimentoSubstitutoService.storeConhecimentoSubstitutoManual(parameters);
		}
		return retorno;
	}
	
	public Serializable storeConhecimentoSubstituto(Map<String, Object> parameters) {
		return conhecimentoSubstitutoService.storeConhecimentoSubstituto(parameters);
	}
	
	public Map findDivisaoCliente(Map parameters) {
		return conhecimentoSubstitutoService.findDivisaoClienteByConhecimento(parameters);
	}

	public Map<String, Object> validaParametroGeralFilial() {
		return this.conhecimentoSubstitutoService.validaParametroGeralFilial();
	}
	
	/*
	 * GETTERS E SETTERS
	 */
	public void setCotacaoService(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}

	public void setConhecimentoNormalService(ConhecimentoNormalService conhecimentoNormalService) {
		this.conhecimentoNormalService = conhecimentoNormalService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setConhecimentoSubstitutoService(
			ConhecimentoSubstitutoService conhecimentoSubstitutoService) {
		this.conhecimentoSubstitutoService = conhecimentoSubstitutoService;
	}

	public DigitarDadosNotaNormalCalculoCTRCService getDigitarDadosNotaNormalCalculoCTRCService() {
		return digitarDadosNotaNormalCalculoCTRCService;
	}

	public void setDigitarDadosNotaNormalCalculoCTRCService(
			DigitarDadosNotaNormalCalculoCTRCService digitarDadosNotaNormalCalculoCTRCService) {
		this.digitarDadosNotaNormalCalculoCTRCService = digitarDadosNotaNormalCalculoCTRCService;
	}

	public DivisaoClienteService getDivisaoClienteService() {
		return divisaoClienteService;
	}

	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

}
