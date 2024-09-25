package com.mercurio.lms.expedicao.swt.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.entrega.model.NotaFiscalOperada;
import com.mercurio.lms.entrega.model.service.NotaFiscalOperadaService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoDevolucaoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.service.CotacaoService;

/**
 * Generated by: ADSM ActionGenerator
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.swt.digitarDadosNotaDevolucaoAction"
 */

public class DigitarDadosNotaDevolucaoAction extends CrudAction {

	private CotacaoService cotacaoService;
	private FilialService filialService;
	private ParcelaPrecoService parcelaPrecoService;
	private ConhecimentoDevolucaoService conhecimentoDevolucaoService;
	private ParametroGeralService parametroGeralService;

	public Map<String, Object> validaPrimeiraFase(Map params) {
		Map<String, Object> result = new HashMap<String, Object>();
		DoctoServico doctoServicoOriginal = new DoctoServico();
		doctoServicoOriginal.setIdDoctoServico((Long) params.get("idDoctoServico"));
		doctoServicoOriginal.setDhEmissao((DateTime) params.get("dhEmissao"));
		doctoServicoOriginal.setTpDocumentoServico(new DomainValue((String) params.get("tpDocumentoServico")));

		Conhecimento conhecimento = new Conhecimento();
		conhecimento.setDoctoServicoOriginal(doctoServicoOriginal);
		conhecimento.setDtAutDevMerc((YearMonthDay) params.get("dtAutorizacao"));
		conhecimento.setDsRespAutDevMerc((String) params.get("dsRespAutDevMerc"));

		// Seta as observa��es
		List observacoesDoctoServico = new ArrayList();
		for(int i=1; i<5; i++) {
			String observacao = (String) params.get("observacao" + i);
			if(StringUtils.isNotBlank(observacao)) {
				ObservacaoDoctoServico observacaoDoctoServico = new ObservacaoDoctoServico();
				observacaoDoctoServico.setDsObservacaoDoctoServico(observacao);
				observacaoDoctoServico.setBlPrioridade(Boolean.TRUE);
				observacoesDoctoServico.add(observacaoDoctoServico);
			}
		}
		conhecimento.setObservacaoDoctoServicos(observacoesDoctoServico);

		conhecimentoDevolucaoService.validateConhecimentoDevolucao(conhecimento);

		result.put("conhecimento", conhecimento);
		return result;
	}

	public Map calcularCTRCDevolucao(Map params) {
		Conhecimento conhecimento = (Conhecimento) params.get("conhecimento");
		conhecimentoDevolucaoService.executeCalculoFreteDevolucao(conhecimento, (String) params.get("tipoCalculo"), (Long) params.get("idCotacao"));
		
		return createReturnMap(conhecimento);
	}

	public void executeValidacoesParaBloqueioValores(TypedFlatMap map){
		conhecimentoDevolucaoService.executeValidacoesParaBloqueioValores(map);
	}
		
	public Map executaCalculoManual(Map parameters) {
		Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
		List parcelasCalculo = (List) parameters.get("parcela");
		conhecimentoDevolucaoService.executeCalculoFreteDevolucaoManual(conhecimento, parcelasCalculo);

		return createReturnMap(conhecimento);
	}

	public Serializable gravarCTRCDevolucao(Map parameters) {
		Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
		Long idDoctoServico = (Long) parameters.get(ConstantesExpedicao.ID_DOCTO_SERVICO_IN_SESSION);
		List<Map<String, Object>> mapNotasFiscaisConhecimento = (List) parameters.get("notasFiscaisConhecimento");
		Boolean blBloqueiaSubcontratada = (Boolean) parameters.get("blBloqueiaSubcontratada");
		Boolean isDocumentoEletronico = (Boolean) parameters.get("isDocumentoEletronico");
		Boolean isEntregaParcial = (Boolean) parameters.get("isEntregaParcial");
		
		List<NotaFiscalConhecimento> notasFiscaisConhecimento;
		if(isDocumentoEletronico != null && isDocumentoEletronico){
			if (isEntregaParcial) {
				mapNotasFiscaisConhecimento = this.extractNotasFiscaisConhecimentoSelecionadas(mapNotasFiscaisConhecimento);
			}
			notasFiscaisConhecimento = extractNotasFiscaisConhecimento(conhecimento, mapNotasFiscaisConhecimento);
		} else {
			notasFiscaisConhecimento = null;
		}

		Serializable retorno = conhecimentoDevolucaoService.storeConhecimentoDevolucao(idDoctoServico, conhecimento, mapNotasFiscaisConhecimento, notasFiscaisConhecimento, blBloqueiaSubcontratada,isEntregaParcial);

		return retorno;	
	}

	private List<Map<String, Object>> extractNotasFiscaisConhecimentoSelecionadas(List<Map<String, Object>> mapNotasFiscaisConhecimento) {
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>();
		
		for (Map<String, Object> nfc : mapNotasFiscaisConhecimento) {
			if (nfc.get("blDevolverNf").equals(true)) retorno.add(nfc);
		}
		
		return retorno;
	}
	
	private List<NotaFiscalConhecimento> extractNotasFiscaisConhecimento(final Conhecimento conhecimento, final List<Map<String, Object>> mapNotasFiscaisConhecimento) {
		List<NotaFiscalConhecimento> notasFiscaisConhecimento = new ArrayList<NotaFiscalConhecimento>();
		for (Map<String, Object> map : mapNotasFiscaisConhecimento) {
			NotaFiscalConhecimento notaFiscalConhecimento = new NotaFiscalConhecimento();
			notaFiscalConhecimento.setIdNotaFiscalConhecimento((Long) map.get("id"));
			notaFiscalConhecimento.setConhecimento(conhecimento);
			notaFiscalConhecimento.setNrChave((String) map.get("nrChave"));
			if(map.get("nrCfop") != null){
				notaFiscalConhecimento.setNrCfop(new BigInteger(String.valueOf(map.get("nrCfop"))));
			}
			notaFiscalConhecimento.setVlBaseCalculo((BigDecimal) map.get("vlBaseCalculo"));
			notaFiscalConhecimento.setVlIcms((BigDecimal) map.get("vlIcms"));
			notaFiscalConhecimento.setVlBaseCalculoSt((BigDecimal) map.get("vlBaseCalculoSt"));
			notaFiscalConhecimento.setVlIcmsSt((BigDecimal) map.get("vlIcmsSt"));
			notaFiscalConhecimento.setVlTotalProdutos((BigDecimal) map.get("vlTotalProdutos"));
			notaFiscalConhecimento.setTpDocumento((String) map.get("tpDocumento"));
			if(map.get("nrPinSuframa") != null){
				notaFiscalConhecimento.setNrPinSuframa(Integer.valueOf((String.valueOf(map.get("nrPinSuframa")))));
			}
			
			notasFiscaisConhecimento.add(notaFiscalConhecimento);
			 
		}
		return notasFiscaisConhecimento;
	}

	public List<Map<String, Object>> findDoctoServico(Map<String, Object> criteria) {
		Long nrDoctoServico = (Long) criteria.get("nrDoctoServico");
		Long idFilialOrigem = (Long) criteria.get("idFilial");
		String tpDocumento = (String) criteria.get("tpDocumentoServico");
		List<Map<String, Object>> result = conhecimentoDevolucaoService.findConhecimentoDevolucao(nrDoctoServico, idFilialOrigem, tpDocumento);
		
		if(result != null) {
			Map<String, Object> doctoServico = (Map<String, Object>) result.get(0);
			Map<String, Object> remetente = (Map<String, Object>) doctoServico.remove("remetente");
			Map<String, Object> destinatario = (Map<String, Object>) doctoServico.remove("destinatario");
			Map<String, Object> redespacho = (Map<String, Object>) doctoServico.remove("redespacho");
			Long idClienteRemetente = (Long) remetente.get("idCliente");
			Long idClienteRedespacho = (Long) (redespacho!= null ? redespacho.get("idCliente") : null);
			
			doctoServico.put("nrDoctoServico", FormatUtils.formataNrDocumento(doctoServico.get("nrConhecimento").toString(), ConstantesExpedicao.CONHECIMENTO_NACIONAL));
			doctoServico.put("nrConhecimento", FormatUtils.formataNrDocumento(doctoServico.get("nrConhecimento").toString(), ConstantesExpedicao.CONHECIMENTO_NACIONAL));
			doctoServico.put("cotacoes", findCotacao(idClienteRemetente, tpDocumento));

			if(idClienteRedespacho!=null){
			    doctoServico.put("sgFilialDestino", doctoServico.get("sgFilialOrigem"));
			    doctoServico.put("nmFilialDestino", doctoServico.get("nmFilialOrigem"));
			    doctoServico.put("idClienteRemetente", destinatario.get("idCliente"));
	            doctoServico.put("nmPessoaRemetente", destinatario.get("nmPessoa"));
	            doctoServico.put("nrIdentificacaoRemetente", FormatUtils.formatIdentificacao(destinatario));
	            doctoServico.put("idClienteDestinatario", remetente.get("idCliente"));
	            doctoServico.put("nmPessoaDestinatario", remetente.get("nmPessoa"));
	            doctoServico.put("nrIdentificacaoDestinatario", FormatUtils.formatIdentificacao(remetente));  
			}else{
			    doctoServico.put("idClienteRemetente", remetente.get("idCliente"));
	            doctoServico.put("nmPessoaRemetente", remetente.get("nmPessoa"));
	            doctoServico.put("nrIdentificacaoRemetente", FormatUtils.formatIdentificacao(remetente));
	            doctoServico.put("idClienteDestinatario", destinatario.get("idCliente"));
	            doctoServico.put("nmPessoaDestinatario", destinatario.get("nmPessoa"));
	            doctoServico.put("nrIdentificacaoDestinatario", FormatUtils.formatIdentificacao(destinatario));
			}
		}
		return result;
	}

	public Map findParcelasCalculoManual() {
		Map dados = new HashMap();
		Map parcela = parcelaPrecoService.findParcelaByCdParcelaPreco(ConstantesExpedicao.CD_FRETE_PESO);
		parcela.put("dsParcela", parcela.remove("nmParcelaPreco"));
		parcela.put("idParcela", parcela.remove("idParcelaPreco"));
		dados.put("parcelaFrete", parcela);
		return dados;
	}

	public List findFilialConhecimento(Map criteria) {
		List<Map> result = filialService.findLookupBySgFilial((String) criteria.get("sgFilial"), (String) criteria.get("tpAcesso"));
		if (result != null) {
			for (Map<String, Object> filial : result) {
				Map<String, Object> pessoa = (Map) filial.remove("pessoa");
				filial.put("nmFantasia", pessoa.get("nmFantasia"));
				
				filial.remove("empresa");
			}
		}
		return result;
	}

	public List findCotacao(Long idClienteRemetente, String tpDocumento) {
		List result = cotacaoService.findCotacoes(idClienteRemetente, tpDocumento);
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			map.put("nrCotacao", map.remove("sgFilial") + "-" + map.get("nrCotacao"));
		}
		return result;
	}

	/**
	 * Valida o local da emissao do documento e cria os dados necessarios para
	 * a emissao do mesmo na sessao.
	 * @author Luis Carlos Poletto
	 * 
	 * @param parameters
	 */
	public void createSessionData(Map parameters) {
		DateTime sysDate = JTDateTimeUtils.getDataHoraAtual();
		DateTime dhChegada = (DateTime) parameters.get("dhChegada");
		if(dhChegada != null && parameters.get("nrFrota") != null && parameters.get("nrIdentificador") != null) {
		if(sysDate.compareTo(dhChegada) < 0) {
			throw new BusinessException("LMS-00074");
	}
			
			BigDecimal tmpMinimoDataColeta = (BigDecimal)parametroGeralService.findConteudoByNomeParametro("TMP_MINIMO_DATA_COLETA", false);
	
			if (tmpMinimoDataColeta != null) {
				sysDate = sysDate.minusHours(tmpMinimoDataColeta.intValue());
				if(sysDate.compareTo(dhChegada) > 0) {
					throw new BusinessException("LMS-04250");
	}
			}
		}
	}
	
	/*
	 * METODOS PRIVADOS
	 */
	private Map<String, Object> createReturnMap(Conhecimento conhecimento) {
		Map<String, Object>  retorno = new HashMap<String, Object> ();
		retorno.put("totalCTRC", conhecimento.getVlTotalDocServico());
		retorno.put("vlDesconto", conhecimento.getVlDesconto());

		List<Map<String, Object>> parcelas = new ArrayList<Map<String, Object>>();
		for (Iterator<ParcelaDoctoServico> iter = conhecimento.getParcelaDoctoServicos().iterator(); iter.hasNext();) {
			ParcelaDoctoServico parcelaDoctoServico = iter.next();
			Map<String, Object>  parcela = new HashMap<String, Object> ();
			parcela.put("dsParcela", parcelaDoctoServico.getParcelaPreco().getDsParcelaPreco().getValue());
			parcela.put("vlParcela", parcelaDoctoServico.getVlParcela());
			parcelas.add(parcela);
		}
		retorno.put("parcelasFrete", parcelas);
		retorno.put("conhecimento", conhecimento);
		return retorno;		
	}

	/*
	 * GETTERS E SETTERS
	 */
	public void setCotacaoService(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}
	
	public void setConhecimentoDevolucaoService(ConhecimentoDevolucaoService conhecimentoDevolucaoService) {
		this.conhecimentoDevolucaoService = conhecimentoDevolucaoService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

}
