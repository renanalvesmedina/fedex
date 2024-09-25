package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.edi.dto.RelatorioErrosRecalculoFreteDTO;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.RecalculoFrete;
import com.mercurio.lms.expedicao.model.RecalculoFreteArquivoDTO;
import com.mercurio.lms.util.JTDateTimeUtils;


/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser
 * utilizado para referenciar este
 * serviço.
 * 
 * @author JonasFE
 *
 * @spring.bean id="lms.expedicao.recalculoFreteCallerService"
 */
public class RecalculoFreteCallerService {

	private Logger log = LogManager.getLogger(this.getClass());
	private RecalculoFreteService recalculoFreteService;
	private ConhecimentoService conhecimentoService;
	private ConfiguracoesFacade configuracoesFacade;
	
	public Map<String, Object> executeRecalculo(RecalculoFrete recalculoFrete){
		Long nrProcesso = recalculoFreteService.generateNrProcessoRecalculo();

		recalculoFrete.setTpSituacaoProcesso(new DomainValue("I"));
		recalculoFrete.setNrProcesso(nrProcesso);
		recalculoFrete.setDtInicio(JTDateTimeUtils.getDataAtual()); 

		recalculoFreteService.storeRecalculoFrete(recalculoFrete, true);
		
		List<RelatorioErrosRecalculoFreteDTO> erros = new ArrayList<RelatorioErrosRecalculoFreteDTO>();

		Long count = 0L;
		Long countProcessados = 0L;
		List<ListOrderedMap> list = recalculoFreteService.findDocsRecalculo(recalculoFrete);
		if(list == null || list.isEmpty()){
			throw new BusinessException("LMS-01203");
		} else {
			for(ListOrderedMap map: list){
				Long idDoctoServico = MapUtils.getLong(map, "ID_DOCTO_SERVICO");

				final Conhecimento conhecimento = conhecimentoService.findById(idDoctoServico);
				try {
					count++;
					recalculoFreteService.executeProcessamentoItem(recalculoFrete.getIdRecalculoFrete(), idDoctoServico, erros);
					countProcessados++;
				} catch (BusinessException e) {
					gravaLog(erros, conhecimento, e);
				} catch (Exception e) {
					gravaLog(erros, conhecimento, e);
				}
			}
		}

		recalculoFrete.setNrTotalRegistros(count);
		recalculoFrete.setNrTotalRegistrosProcessados(countProcessados);
		recalculoFrete.setDtFim(JTDateTimeUtils.getDataAtual());
		recalculoFrete.setTpSituacaoProcesso(new DomainValue("E"));

		/*Atualiza Tabela RECALCULO_FRETE*/
		recalculoFreteService.storeRecalculoFrete(recalculoFrete, false);
		
		Map<String, Object> retorno = new HashMap<String, Object>();
		Collections.sort(erros);
		
		retorno.put("erros", erros);
		retorno.put("recalculoFrete", recalculoFrete);
		
		return retorno;
	}
	
	private void gravaLog(List<RelatorioErrosRecalculoFreteDTO> erros, Conhecimento conhecimento, Exception e){
		String mensagem = getDsmensagemErro(e);
		RelatorioErrosRecalculoFreteDTO erro = new RelatorioErrosRecalculoFreteDTO();
		erro.setConhecimento(conhecimento);
		erro.setDsMensagemErro(mensagem);
		erros.add(erro);
	}
	
	private void gravaLog(List<RelatorioErrosRecalculoFreteDTO> erros, RecalculoFreteArquivoDTO recalculoFreteArquivo, Exception e){
		log.error(e);
		String mensagem = getDsmensagemErro(e);
		RelatorioErrosRecalculoFreteDTO erro = new RelatorioErrosRecalculoFreteDTO();
		erro.setNrDocumentoArquivo(recalculoFreteArquivo.getNrDocumento());
		erro.setDsMensagemErro(mensagem);
		erros.add(erro);
	}

	private String getDsmensagemErro(Exception e) {
		String mensagem;
		if(e.getClass().isAssignableFrom(BusinessException.class)){
			BusinessException ex = (BusinessException) e;
			mensagem = ex.getMessageKey() + " - " + configuracoesFacade.getMensagem(ex.getMessageKey(), ex.getMessageArguments());
		} else {
			mensagem = e.getMessage();
			if(mensagem == null){
				mensagem = e.getClass().getName();
			}
		}
		return mensagem;
	}
	
	/**
	 * Executa o recalculo do frete
	 *
	 * @param recalculo
	 */
	public Map<String, Object> executeRecalculo(List<RecalculoFreteArquivoDTO> list){
		
		RecalculoFrete recalculoFrete = new RecalculoFrete();
		
		Long nrProcesso = recalculoFreteService.generateNrProcessoRecalculo();

		recalculoFrete.setTpSituacaoProcesso(new DomainValue("I"));
		recalculoFrete.setNrProcesso(nrProcesso);
		recalculoFrete.setDtInicio(JTDateTimeUtils.getDataAtual()); 
		recalculoFrete.setDsProcesso("PROCESSAMENTO DE ARQUIVO");
		recalculoFrete.setDtInicial(new YearMonthDay());
		recalculoFrete.setDtFinal(new YearMonthDay());

		recalculoFreteService.storeRecalculoFrete(recalculoFrete, true);
		
		List<RelatorioErrosRecalculoFreteDTO> erros = new ArrayList<RelatorioErrosRecalculoFreteDTO>();

		Long count = 0L;
		Long countProcessados = 0L;
		
		if(list == null || list.isEmpty()){
			throw new BusinessException("LMS-01203");
		} else {
			for (RecalculoFreteArquivoDTO recalculo : list) {
				try {
					count++;
					recalculoFreteService.executeProcessamentoItemArquivo(recalculoFrete, recalculo);
					countProcessados++;
				} catch (BusinessException e) {
					gravaLog(erros, recalculo, e);
				} catch (Exception e) {
					gravaLog(erros, recalculo, e);
				}
			}
		}
		
		recalculoFrete.setNrTotalRegistros(count);
		recalculoFrete.setNrTotalRegistrosProcessados(countProcessados);
		recalculoFrete.setDtFim(JTDateTimeUtils.getDataAtual());
		recalculoFrete.setTpSituacaoProcesso(new DomainValue("E"));

		/*Atualiza Tabela RECALCULO_FRETE*/
		recalculoFreteService.storeRecalculoFrete(recalculoFrete, false);
		
		Map<String, Object> retorno = new HashMap<String, Object>();
		Collections.sort(erros);
		
		retorno.put("erros", erros);
		retorno.put("recalculoFrete", recalculoFrete);
		
		return retorno;
	}

	public void setRecalculoFreteService(RecalculoFreteService recalculoFreteService) {
		this.recalculoFreteService = recalculoFreteService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}
