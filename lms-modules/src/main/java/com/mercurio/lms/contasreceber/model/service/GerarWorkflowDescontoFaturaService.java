package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.service.GenerateNrEventoDescontoServise.TpDocumento;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.PendenciaService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
/**
 * @author Mickaël Jalbert
 * @since 05/01/2007
 * 
 * @spring.bean id="lms.contasreceber.gerarWorkflowDescontoFaturaService"
 */
public class GerarWorkflowDescontoFaturaService {
	
	private WorkflowPendenciaService workflowPendenciaService;
	private MoedaService moedaService;
	private FaturaService faturaService;
	private ConfiguracoesFacade configuracoesFacade;
	private DescontoService descontoService;
	private GenerateNrEventoDescontoServise generateNrEventoDescontoService;
	private PendenciaService pendenciaService;
	
	/**
	 * Gera um desconto para o item fatura.
	 * 
	 * Regra 5.3
	 * @param valorDevido 
	 */
	public Long generateDesconto(Fatura fatura, List<ItemFatura> lstItemFatura, BigDecimal valorDevido) {
		
		Long idPendencia = generatePendenciaDeDesconto(fatura, valorDevido);
		
		updateSituacaoDesconto(fatura, lstItemFatura);
		
		return idPendencia;
	}	
	
	/**
	 * Gera uma pendencia (workflow) para liberar um valor especial de cotação
	 * (quando a cotação não é igual à cotação que vem do banco)
	 */
	private Long generatePendenciaDeDesconto(Fatura fatura,BigDecimal valorDevido) {
		
		
			BigDecimal pcDesconto = fatura.getVlDesconto().multiply(BigDecimalUtils.HUNDRED).divide(valorDevido, 4, BigDecimal.ROUND_HALF_UP).setScale(4,BigDecimal.ROUND_HALF_UP);
		
			//Se tem uma pendencia pendente, cancelar
			if (fatura.getIdFatura() != null && fatura.getIdPendenciaDesconto() != null){
				Pendencia pendencia = getPendenciaService().findById(fatura.getIdPendenciaDesconto());
				if(pendencia.getTpSituacaoPendencia().getValue().equals("E")) {
					workflowPendenciaService.cancelPendencia(fatura.getIdPendenciaDesconto());
				}
			}

			// Gera uma nova pendencia
			faturaService.storeBasic(fatura);
			
			String descricao = getDescricaoWorkflowDesconto(fatura, fatura.getVlTotal(), fatura.getVlDesconto());
			Short nrEvento = generateNrEventoDescontoService
					.getNrEventoDesconto(
							pcDesconto, 
							fatura.getVlDesconto(),
							TpDocumento.FAT);
			return workflowPendenciaService.generatePendencia(fatura.getFilialByIdFilial().getIdFilial(), nrEvento, fatura.getIdFatura(), descricao, JTDateTimeUtils.getDataHoraAtual()).getIdPendencia();
     
						}
					
	/**
	 * Aprova automaticamente os desconto em aprovação com fatura aprovada
	 * 
	 * @author Mickaël Jalbert
	 * @since 04/01/2007
	 * 
	 * @param fatura
	 * @param lstItemFatura
	 */
	private void updateSituacaoDesconto(Fatura fatura, List lstItemFatura) {
		//Se a fatura não está 'Em aprovação'
    	if (fatura.getTpSituacaoAprovacao() != null && fatura.getTpSituacaoAprovacao().getValue().equals("A")){
			for (Iterator iter = lstItemFatura.iterator(); iter.hasNext();) {
				ItemFatura itemFatura = (ItemFatura) iter.next();
				
				//Se tem desconto
				if (itemFatura.getDevedorDocServFat().getDescontos() != null && itemFatura.getDevedorDocServFat().getDescontos().size() == 1) {
					Desconto desconto = (Desconto)itemFatura.getDevedorDocServFat().getDescontos().get(0);
					
					//Se o valor do desconto é maior que zero
					if (desconto.getVlDesconto().compareTo(BigDecimal.ZERO) > 0){

						//Se o desconto está 'Em aprovação', aprovar automaticamente o desconto
						if (desconto.getTpSituacaoAprovacao() != null && desconto.getTpSituacaoAprovacao().getValue().equals("E")){
							desconto.setTpSituacaoAprovacao(new DomainValue("A"));
							descontoService.storePadrao(desconto);
						}
					}
				}
			}
    	}
	}	
	

	/**
	 * Monta o string de descrição do workflow de desconto
	 * 
	 * @author Mickaël Jalbert
	 * @since 04/01/2007
	 * 
	 * @param fatura
	 * @return String
	 */
	private String getDescricaoWorkflowDesconto(Fatura fatura, BigDecimal vlFatura, BigDecimal vlDesconto) {
		StringBuffer descricaoWorkflow = new StringBuffer(); 
		
		Moeda moeda = moedaService.findById(fatura.getMoeda().getIdMoeda());
		
		descricaoWorkflow.append(configuracoesFacade.getMensagem("fatura") + ": " + fatura.getFilialByIdFilial().getSgFilial() + " " + FormatUtils.formataNrDocumento(fatura.getNrFatura().toString(), "FAT") + "\n");
		descricaoWorkflow.append(configuracoesFacade.getMensagem("valorTotal") + ": " + moeda.getDsSimbolo() + " " + FormatUtils.formatDecimal("0.00",vlFatura) + "\n");
		descricaoWorkflow.append(configuracoesFacade.getMensagem("valorTotalDesconto") + ": " + moeda.getDsSimbolo() + " " + FormatUtils.formatDecimal("0.00",vlDesconto, true) + ".");
		
		return descricaoWorkflow.toString();
	}	

	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}


	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	public void setGenerateNrEventoDescontoService(
			GenerateNrEventoDescontoServise generateNrEventoDescontoService) {
		this.generateNrEventoDescontoService = generateNrEventoDescontoService;
	}
	
	public void setPendenciaService(PendenciaService pendenciaService) {
		this.pendenciaService = pendenciaService;
	}

	public PendenciaService getPendenciaService() {
		return pendenciaService;
	}
}
