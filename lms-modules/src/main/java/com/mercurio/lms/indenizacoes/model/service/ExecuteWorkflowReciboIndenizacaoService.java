package com.mercurio.lms.indenizacoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.indenizacoes.executeWorkflowReciboIndenizacaoService"
 */
public class ExecuteWorkflowReciboIndenizacaoService {
	
	private ReciboIndenizacaoService reciboIndenizacaoService;
	private WorkflowPendenciaService workflowPendenciaService;
	private HistoricoFilialService historicoFilialService;
	
	public String executeWorkflow(List<Long>ids, List<String>situacoes) {
		String mensagemWorkflow = null;
		
		for (int i=0; i<ids.size(); i++) {
			Long idReciboIndenizacao = (Long) ids.get(i);
			ReciboIndenizacao reciboIndenizacao = reciboIndenizacaoService.findById(idReciboIndenizacao);
			
			String tpSituacaoAprovacao = (String)situacoes.get(i);
			reciboIndenizacao.setTpSituacaoWorkflow(new DomainValue(tpSituacaoAprovacao));
			
			// se o workflow da geracao do rim foi aprovado
			
			
			if ("A".equals(tpSituacaoAprovacao) && !historicoFilialService.validateFilialUsuarioMatriz(reciboIndenizacao.getFilial().getIdFilial())) {
				mensagemWorkflow = reciboIndenizacaoService.getDsProcessoWorkflowParaReciboIndenizacao(reciboIndenizacao);
				
				workflowPendenciaService.generatePendencia(reciboIndenizacao.getFilial().getIdFilial(), getNrTipoEventoAprovacao(reciboIndenizacao),
						idReciboIndenizacao, mensagemWorkflow, JTDateTimeUtils.getDataHoraAtual());
				}
			
			gerarWorkflowReciboIndenizacao(reciboIndenizacao);
			
			reciboIndenizacaoService.store(reciboIndenizacao);
			}
		
		return null;
	}
	
	private void gerarWorkflowReciboIndenizacao(ReciboIndenizacao reciboIndenizacao){
		if("A".equals(reciboIndenizacao.getTpSituacaoWorkflow().getValue()) || "R".equals(reciboIndenizacao.getTpSituacaoWorkflow().getValue())){
			String dsProcesso = reciboIndenizacaoService.getDsProcessoWorkflowParaReciboIndenizacao(reciboIndenizacao);					
			workflowPendenciaService.generatePendencia(reciboIndenizacao.getFilial().getIdFilial(), selecionarNumEventoReciboInd(reciboIndenizacao),
					reciboIndenizacao.getIdReciboIndenizacao(), dsProcesso, JTDateTimeUtils.getDataHoraAtual());
		}
	}
	
	/**
	 * Retorna evento de alerta de acordo com o tipo de evento que foi criado na geração da pendência.
	 * 
	 * Modo simplificado e equivalente de aplicar a regra para verificação de faixas que já é executada em
	 * <code>com.mercurio.lms.indenizacoes.model.service.ReciboIndenizacaoService.executeWorkflowPendencia(ReciboIndenizacao)</code>, para
	 * encontrar o nrTipoEvento que deve ser criado.
	 * 
	 * @param ri
	 * @return
	 */
	private Short getNrTipoEventoAprovacao(ReciboIndenizacao ri) {
		Short nrTipoEvento = ri.getPendencia().getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento();
		
		if(nrTipoEvento.equals(ConstantesWorkflow.NR2101_VALOR_INDENIZACAO)) {
			return ConstantesWorkflow.NR2102_VALOR_INDENIZACAO;
			
		} else if (nrTipoEvento.equals(ConstantesWorkflow.NR2118_VALOR_INDENIZACAO)) {
			return ConstantesWorkflow.NR2119_VALOR_INDENIZACAO;	
			
		} else if(nrTipoEvento.equals(ConstantesWorkflow.NR2104_VALOR_INDENIZACAO)) {
			return ConstantesWorkflow.NR2105_VALOR_INDENIZACAO;
		
		} else if (nrTipoEvento.equals(ConstantesWorkflow.NR2121_VALOR_INDENIZACAO)) {
			return ConstantesWorkflow.NR2122_VALOR_INDENIZACAO;	
			
		} else if(nrTipoEvento.equals(ConstantesWorkflow.NR2107_VALOR_INDENIZACAO)) {
			return ConstantesWorkflow.NR2108_VALOR_INDENIZACAO;
		
		} else if (nrTipoEvento.equals(ConstantesWorkflow.NR2124_VALOR_INDENIZACAO)) {
			return ConstantesWorkflow.NR2125_VALOR_INDENIZACAO;
			
		} else if(nrTipoEvento.equals(ConstantesWorkflow.NR2110_VALOR_INDENIZACAO)) {
			return ConstantesWorkflow.NR2111_VALOR_INDENIZACAO;

		} else if (nrTipoEvento.equals(ConstantesWorkflow.NR2127_VALOR_INDENIZACAO)) {
			return ConstantesWorkflow.NR2128_VALOR_INDENIZACAO;
			
		} else if(nrTipoEvento.equals(ConstantesWorkflow.NR2113_VALOR_INDENIZACAO)) {
			return ConstantesWorkflow.NR2114_VALOR_INDENIZACAO;
		
		} else if (nrTipoEvento.equals(ConstantesWorkflow.NR2130_VALOR_INDENIZACAO)) {
			return ConstantesWorkflow.NR2131_VALOR_INDENIZACAO;	
			
		} else if(nrTipoEvento.equals(ConstantesWorkflow.NR2116_VALOR_INDENIZACAO)) {
			return ConstantesWorkflow.NR2117_VALOR_INDENIZACAO;
		
		} else if (nrTipoEvento.equals(ConstantesWorkflow.NR2133_VALOR_INDENIZACAO)) {
			return ConstantesWorkflow.NR2134_VALOR_INDENIZACAO;
		
		} else if (nrTipoEvento.equals(ConstantesWorkflow.NR2133_VALOR_INDENIZACAO)) {
			return ConstantesWorkflow.NR2134_VALOR_INDENIZACAO;
		} else if (nrTipoEvento.equals(ConstantesWorkflow.NR2100_VALOR_INDENIZACAO)) {
			return ConstantesWorkflow.NR2140_VALOR_INDENIZACAO;
		}
		
		return nrTipoEvento;
	}

	private Short selecionarNumEventoReciboInd(ReciboIndenizacao reciboIndenizacao){
		Short nrTipoEvento = null;
		
		if ("NC".equals(reciboIndenizacao.getTpIndenizacao().getValue() )){
			if ("A".equals(reciboIndenizacao.getTpSituacaoWorkflow().getValue())){
				nrTipoEvento = ConstantesWorkflow.NR2135_VALOR_INDENIZACAO;
			}else {
				nrTipoEvento = ConstantesWorkflow.NR2136_VALOR_INDENIZACAO;
			}			
		}else{
			if ("A".equals(reciboIndenizacao.getTpSituacaoWorkflow().getValue())){
				nrTipoEvento = ConstantesWorkflow.NR2137_VALOR_INDENIZACAO;
			}else {
				nrTipoEvento = ConstantesWorkflow.NR2138_VALOR_INDENIZACAO;
			}	
		}
		
		return nrTipoEvento;
	}

	public void setReciboIndenizacaoService(ReciboIndenizacaoService reciboIndenizacaoService) {
		this.reciboIndenizacaoService = reciboIndenizacaoService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	
	public void setHistoricoFilialService(
			HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}

}
