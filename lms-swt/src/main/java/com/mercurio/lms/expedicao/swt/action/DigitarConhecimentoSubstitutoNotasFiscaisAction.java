package com.mercurio.lms.expedicao.swt.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.expedicao.DoctoServicoValidateFacade;
import com.mercurio.lms.expedicao.model.service.ConhecimentoSubstitutoService;
import com.mercurio.lms.expedicao.model.service.DigitarDadosNotaNormalNotasFiscaisService;
import com.mercurio.lms.expedicao.model.service.DigitarNotaService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;

@SuppressWarnings("rawtypes")
public class DigitarConhecimentoSubstitutoNotasFiscaisAction extends CrudAction {
	private EnderecoPessoaService enderecoPessoaService;
	private DigitarDadosNotaNormalNotasFiscaisService digitarDadosNotaNormalNotasFiscaisService;	
	private DoctoServicoValidateFacade doctoServicoValidateFacade;
	private DigitarNotaService digitarNotaService;
	private ConhecimentoSubstitutoService conhecimentoSubstitutoService;
	
	public ResultSetPage findPaginated(Map criteria) {
		return ResultSetPage.EMPTY_RESULTSET;
	}

	@SuppressWarnings("unchecked")
	public Map getRowCountUfFronteiraRapidaFilial(){
		Long idPessoa = SessionUtils.getFilialSessao().getIdFilial();
		Serializable rowCount = enderecoPessoaService.getRowCountByPessoaUfFronteiraRapida(idPessoa, Boolean.TRUE);
		Map retorno = new HashMap();
		retorno.put("fronteiraRapida", rowCount);
		return retorno;
	}

	@SuppressWarnings("unchecked")
	public Map findLimitePesoVolume() {
		final BigDecimal limitePesoVolume = doctoServicoValidateFacade.findLimitePesoVolumeCalculoFrete();
		final Map retorno = new TypedFlatMap();
		retorno.put("limitePesoVolume", limitePesoVolume);
		return retorno;
	}
	
	public Map<String, Object> hasIndicadorPinSuframa(Map<String, Object> parameters) {
		Long idClienteConsignatario;
		Cliente clienteConsignatario = (Cliente) parameters.get("clienteConsignatario");
		if(clienteConsignatario == null){
			idClienteConsignatario = null;
		} else {
			idClienteConsignatario = clienteConsignatario.getIdCliente();
		}
		
		Boolean obrigaPinSuframa = digitarNotaService.findObrigatoriedadePinSuframa((Long) parameters.get("idClienteDestinatario"), idClienteConsignatario);

		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("obrigaPinSuframa", obrigaPinSuframa);

		return retorno;
	}
	
	public Map validateDhEmissaoNFAnulacao(Map<String, Object> criteria){
		return conhecimentoSubstitutoService.validateDhEmissaoNFAnulacao(criteria);
	}
	
	/**
	 * Valida Notas Fiscais Conhecimento
	 * @param criteria
	 * @return Warnings
	 * 
	 */
	@SuppressWarnings("unchecked")
    public Map validateNotasConhecimento(Map<String, Object> parameters) {
		convertViewMapToModelMap((List<Map<String, Object>>) parameters.get("notasFiscais"));
		return conhecimentoSubstitutoService.validateNotasConhecimento(parameters);
	}

	private void convertViewMapToModelMap(List<Map<String, Object>> notasFiscais) {
		for (Map<String, Object> notaFiscal : notasFiscais) {
			notaFiscal.put("nrNotaFiscal", notaFiscal.get("nrDocumento"));
			notaFiscal.put("dtEmissao", notaFiscal.get("dataEmissao"));
			notaFiscal.put("qtVolumes", notaFiscal.get("quantidadeVolumes2"));
			notaFiscal.put("psMercadoria", notaFiscal.get("pesoDeclarado"));
			notaFiscal.put("psCubado", notaFiscal.get("pesoCubado"));
			notaFiscal.put("vlTotal", notaFiscal.get("valorTotal"));
			notaFiscal.put("nrVolumeEtiquetaInicial", notaFiscal.get("etiquetaInicial"));
			notaFiscal.put("nrVolumeEtiquetaFinal", notaFiscal.get("etiquetaFinal"));
			notaFiscal.put("tpDocumento", notaFiscal.get("lbTpLayoutDocumento"));
		}
	}
	
	/**
	 * 
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map storeNotas(Map parameters) {
		convertViewMapToModelMap((List<Map<String, Object>>)parameters.get("notaFiscalConhecimento"));
		//LMS-4068 / Não validar limite peso
		parameters.put("validaLimiteValorMercadoria", false);
		return digitarDadosNotaNormalNotasFiscaisService.storeNotas(parameters);
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setDigitarDadosNotaNormalNotasFiscaisService(DigitarDadosNotaNormalNotasFiscaisService digitarDadosNotaNormalNotasFiscaisService) {
		this.digitarDadosNotaNormalNotasFiscaisService = digitarDadosNotaNormalNotasFiscaisService;
	}		
	public void setDoctoServicoValidateFacade(DoctoServicoValidateFacade doctoServicoValidateFacade) {
		this.doctoServicoValidateFacade = doctoServicoValidateFacade;
	}		

	public void setDigitarNotaService(DigitarNotaService digitarNotaService) {
		this.digitarNotaService = digitarNotaService;
	}

	public void setConhecimentoSubstitutoService(
			ConhecimentoSubstitutoService conhecimentoSubstitutoService) {
		this.conhecimentoSubstitutoService = conhecimentoSubstitutoService;
	}		
}