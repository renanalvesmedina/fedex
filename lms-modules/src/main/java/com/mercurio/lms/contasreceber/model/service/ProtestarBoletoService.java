package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.HistoricoBoleto;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.protestarBoletoService"
 */
public class ProtestarBoletoService {
	private ClienteService clienteService;
	private HistoricoBoletoService historicoBoletoService;
	private WorkflowPendenciaService workflowPendenciaService;
	private BoletoService boletoService;
	
	public void executeProtestarBoleto(Boleto boleto){
		validateBoleto(boleto);

		String dsProcesso = "Aprova��o de protesto de boleto referente a fatura: " 
						   + boleto.getFatura().getFilialByIdFilial().getSgFilial() + " "
						   + FormatUtils.formataNrDocumento(boleto.getFatura().getNrFatura().toString(), "FAT");

		boolean isFilialSessaoMatriz = SessionUtils.isFilialSessaoMatriz();
		DomainValue tpSituacaoAprovacao=null;
		if(!isFilialSessaoMatriz){
			tpSituacaoAprovacao = new DomainValue("E");
		}
		
		HistoricoBoleto historicoBoleto = historicoBoletoService.generateHistoricoBoleto(boleto, ConstantesConfiguracoes.CD_OCORRENCIA_PEDIDO_PROTESTO, "REM", tpSituacaoAprovacao);			
		// Caso a filial do usu�rio logado n�o seja MTZ, gerar workflow.
		if (!isFilialSessaoMatriz) {
			historicoBoleto = generatePendenciaWorkflow(historicoBoleto, dsProcesso);
		}
		
		boletoService.storeBasic(boleto);
	}

	private void validateBoleto(Boleto boleto){
		String tpSituacao = boleto.getTpSituacaoBoleto().getValue();

		Cliente cliente = clienteService.findById(boleto.getFatura().getCliente().getIdCliente()); 

		//A situa��o s� pode ser:
		//'Em banco'
		if (!tpSituacao.equals("BN")){
			throw new BusinessException("LMS-36190");			
		}

		//Se o cliente � do tipo especial ou filial de cliente especial e n�o tem 
		// protesto, lan�ar uma exception
		if (("S".equals(cliente.getTpCliente().getValue()) ||
				"F".equals(cliente.getTpCliente().getValue())) &&
				!cliente.getBlIndicadorProtesto().equals(Boolean.TRUE)){ 
			throw new BusinessException("LMS-36083");
		}
	}		

	/**
	 * Gera uma pendencia de cancelamento de boleto caso o boleto est� 'Em banco'
	 * */
	private HistoricoBoleto generatePendenciaWorkflow(HistoricoBoleto historicoBoleto, String dsProcesso){

		historicoBoleto.setIdPendencia(workflowPendenciaService.generatePendencia(historicoBoleto.getBoleto().getFatura().getFilialByIdFilial().getIdFilial(), 
				ConstantesWorkflow.NR3608_PROT_BOL, historicoBoleto.getIdHistoricoBoleto(), dsProcesso, 
				JTDateTimeUtils.getDataHoraAtual()).getIdPendencia());			
		historicoBoletoService.store(historicoBoleto);

		return historicoBoleto;
	}

	public String executeWorkflow(List lstIdHistorico, List lstTpSituacao){
		String tpSituacao = (String)lstTpSituacao.get(0);
		Long idHistorico = (Long)lstIdHistorico.get(0);

		HistoricoBoleto historicoBoleto = historicoBoletoService.findById(idHistorico);

		historicoBoleto.setTpSituacaoAprovacao(new DomainValue(tpSituacao));		

		//Se n�o foi aprovado ou o boleto n�o � 'Em banco'
		if (!tpSituacao.equals("A") || !historicoBoleto.getBoleto().getTpSituacaoBoleto().getValue().equals("BN")){
			historicoBoleto.setTpSituacaoHistoricoBoleto(new DomainValue("C"));
		}

		//Se foi aprovado, trocar a situa��o do boleto por "Banco protesto"
		if (tpSituacao.equals("A")){
			historicoBoleto.getBoleto().setTpSituacaoBoleto(new DomainValue("BP"));
			boletoService.storeBasic(historicoBoleto.getBoleto());
		}

		historicoBoletoService.store(historicoBoleto);
		return null;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}
	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}
}