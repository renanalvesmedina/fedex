package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.HistoricoBoleto;
import com.mercurio.lms.contasreceber.model.MotivoOcorrencia;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.questionamentoFaturas.model.service.QuestionamentoFaturasService;
import com.mercurio.lms.questionamentofaturas.model.QuestionamentoFatura;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.prorrogarVencimentoBoletoService"
 */
public class ProrrogarVencimentoBoletoService {
	private BoletoService boletoService;
	private ClienteService clienteService;
	private HistoricoBoletoService historicoBoletoService;
	private WorkflowPendenciaService workflowPendenciaService;
	private FaturaService faturaService;
	private GerarFaturaBoletoService gerarFaturaService;
	private BoletoAnexoService boletoAnexoService;
	private QuestionamentoFaturasService questionamentoFaturasService;

	public void executeProrrogaVencimentoBoleto(Boleto boleto, MotivoOcorrencia motivoOcorrencia , String dsHistoricoBoleto, ItemList listAnexos){
		YearMonthDay dtVencimento = validateBoleto(boleto);

		String tpSituacao = boleto.getTpSituacaoBoleto().getValue();

		//Se a situação é 'Em banco'
		if (tpSituacao.equals("BN")){			
			//LMS-2770 / Se a lista de anexos for nula é proveniente da tela
			if(listAnexos != null){
				//LMS-2770 
				boletoAnexoService.validaBoletoAnexo(boleto, listAnexos);
			}
			prorrogaVencimentoBoletoComplexo(boleto, dtVencimento, motivoOcorrencia, dsHistoricoBoleto);
		} else {
			prorrogaVencimentoBoletoSimple(boleto, dtVencimento, motivoOcorrencia, dsHistoricoBoleto);
		}
		
		// Regra 3.8 'd' da ET.
		retransmiteBoleto(boleto);
		
		boletoAnexoService.storeAnexos(listAnexos);
	}
	
    /**
     * 
     * @param boleto
     */
    private void retransmiteBoleto(Boleto boleto){
    	boolean hasTransmissao = CollectionUtils.exists(boleto.getHistoricoBoletos(), new Predicate() {	
			@Override
			public boolean evaluate(Object arg0) {
				HistoricoBoleto hb = (HistoricoBoleto) arg0;
				return "REM".equals(hb.getOcorrenciaBanco().getTpOcorrenciaBanco().getValue()) && "A".equals(hb.getTpSituacaoHistoricoBoleto().getValue());
			}
		});
    	
    	//Se o BOLETO.TP_SITUACAO_BOLETO seja (“EM”)
    	if (boleto.getTpSituacaoBoleto().getValue().equals("EM") && !hasTransmissao){
				// Atualiza a fatura.
				boleto.getFatura().setDhTransmissao(null);
				faturaService.store(boleto.getFatura());
				// Gera um histórico de retransmissão.
				historicoBoletoService.generateHistoricoBoleto(boleto, ConstantesConfiguracoes.CD_OCORRENCIA_RETRANSMITIR, "REM");
			}
		}
		

	private YearMonthDay validateBoleto(Boleto boleto){
		String tpSituacao = boleto.getTpSituacaoBoleto().getValue();

		Cliente cliente = clienteService.findById(boleto.getFatura().getCliente().getIdCliente());

		//A situação só pode ser:
		//'Digitado',
		//'Em banco',
		//'Emitido'
		if (!tpSituacao.equals("DI") &&
			!tpSituacao.equals("BN") &&
			!tpSituacao.equals("EM")){
			throw new BusinessException("LMS-36089");			
		}

		//A nova data de vencimento tem que ser depois da data de vencimento atual
		if (!boleto.getDtVencimentoNovo().isAfter(boleto.getDtVencimento())){
			throw new BusinessException("LMS-36090");
		}

		//A nova data de vencimento tem que ser depois da data atual
		if (!boleto.getDtVencimentoNovo().isAfter(JTDateTimeUtils.getDataAtual())){
			throw new BusinessException("LMS-36091");
		}

		return boleto.getDtVencimentoNovo();
	}


	public YearMonthDay calculateDtVencimentoNovo(Boleto boleto) {
		YearMonthDay date = JTDateTimeUtils.getDataAtual().plusDays(5);

		if (boleto.getDtVencimentoNovo().isBefore(date) || boleto.getDtVencimentoNovo().equals(date))
			return date;

		return boleto.getDtVencimentoNovo();
	}

	/**
	 * Faz os update em cima das entidades do boleto que é cancelado
	 * */
	private void prorrogaVencimentoBoletoSimple(Boleto boleto, YearMonthDay dtVencimentoNovo, MotivoOcorrencia motivoOcorrencia , String dsHistoricoBoleto){
		updateBoleto(boleto, dtVencimentoNovo);
		updateFatura(boleto, dtVencimentoNovo);
	}

	/**
	 * Atualiza com a nova data de vencimento o boleto informado
	 * */	
	private void updateBoleto(Boleto boleto, YearMonthDay dtVencimentoNovo){
		boleto.setDtVencimento(dtVencimentoNovo);
		boleto.setDtVencimentoNovo(null);

		boletoService.store(boleto);
	}

	/**
	 * Atualiza com a nova data de vencimento a fatura do boleto informado
	 *
	 * Atualiza a fatura com a nova data de vencimento e zera o atributo vlJuroCalculado 
	 */
	private void updateFatura(Boleto boleto, YearMonthDay dtVencimentoNovo){
		Fatura fatura = boleto.getFatura();

		fatura.setDtVencimento(dtVencimentoNovo);
		fatura.setDhTransmissao(null);
		fatura.setVlJuroCalculado(BigDecimal.ZERO);

		faturaService.store(fatura);
	}

	/**
	 * Faz os update em cima das entidades do boleto que é cancelado
	 * */
	private void prorrogaVencimentoBoletoComplexo(Boleto boleto, YearMonthDay dtVencimentoNovo, MotivoOcorrencia motivoOcorrencia , String dsHistoricoBoleto){
		//Faz o update, seta a dtVencimentoNovo com a data determinada
		boletoService.store(boleto);

		String dsProcesso = "Aprovação para prorrogação de vencimento referente a fatura: " 
			   + boleto.getFatura().getFilialByIdFilial().getSgFilial() + " "
			   + FormatUtils.formataNrDocumento(boleto.getFatura().getNrFatura().toString(), "FAT")
			   + " - novo vencimento: " + JTFormatUtils.format(boleto.getDtVencimentoNovo());

		HistoricoBoleto historicoBoleto = historicoBoletoService.generateHistoricoBoletoAndCancelActives(boleto, ConstantesConfiguracoes.CD_OCORRENCIA_ALTERACAO_VENCIMENTO, "REM", motivoOcorrencia, dsHistoricoBoleto, new DomainValue("E"));			
		
		DoctoServico doctoServico =  gerarFaturaService.getFirstDoctoServicoFromFatura(boleto.getFatura(), boleto.getFatura().getItemFaturas());
			historicoBoletoService.generateQuestionamentoFatura(historicoBoleto, dsProcesso,doctoServico,true, false,boleto.getDtVencimento(), dtVencimentoNovo,null,motivoOcorrencia);
		historicoBoletoService.store(historicoBoleto);
		boletoAnexoService.executeMoverAnexosToQuestionamento(historicoBoleto);
	}	
	

	/**
	 * Gera uma pendencia de cancelamento de boleto caso o boleto esté 'Em banco'
	 * */
	private HistoricoBoleto generatePendenciaWorkflow(HistoricoBoleto historicoBoleto, String dsProcesso){

		historicoBoleto.setIdPendencia(workflowPendenciaService.generatePendencia(historicoBoleto.getBoleto().getFatura().getFilialByIdFilial().getIdFilial(), 
				ConstantesWorkflow.NR3609_PRORRO_VENC_BOL, historicoBoleto.getIdHistoricoBoleto(), dsProcesso, 
				JTDateTimeUtils.getDataHoraAtual()).getIdPendencia() );			
		historicoBoletoService.store(historicoBoleto);

		return historicoBoleto;
	}
	
	public String executeWorkflow(List lstIdHistorico, List lstTpSituacao){
		String tpSituacao = (String)lstTpSituacao.get(0);
		Long idHistorico = (Long)lstIdHistorico.get(0); 

		HistoricoBoleto historicoBoleto = historicoBoletoService.findById(idHistorico);

		historicoBoleto.setTpSituacaoAprovacao(new DomainValue(tpSituacao));		

		Boleto boleto = historicoBoleto.getBoleto();

		/*
		 * Se não foi encontrada a data de vencimento no boleto, usa a data solicitada no questionamento de fatura.
		 */
		if (boleto.getDtVencimentoNovo() == null){
			QuestionamentoFatura questionamentoFatura = questionamentoFaturasService.findById(historicoBoleto.getIdPendencia());
			if (questionamentoFatura != null){
				boleto.setDtVencimentoNovo(questionamentoFatura.getDtVencimentoSolicitado());
			}
		}
		
		//Se foi aprovado, fazer update
		if (tpSituacao.equals("A")){
			boleto = historicoBoleto.getBoleto();
			boleto.setDtVencimentoNovo(calculateDtVencimentoNovo(boleto));

			updateBoleto(boleto, boleto.getDtVencimentoNovo());
			updateFatura(boleto, boleto.getDtVencimento());
		} else { //senão cancelar o historico
			historicoBoleto.setTpSituacaoHistoricoBoleto(new DomainValue("C"));

			// Como a prorrogação não foi aprovada, seta null no atributo dtVencimentoNovo 
			boleto.setDtVencimentoNovo(null);
			boletoService.storeProrrogarVencimentoBoleto(boleto);
		}

		historicoBoletoService.store(historicoBoleto);

		return null;
	}	
	
	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

	public void setGerarFaturaService(GerarFaturaBoletoService gerarFaturaService) {
		this.gerarFaturaService = gerarFaturaService;
	}

	public GerarFaturaBoletoService getGerarFaturaService() {
		return gerarFaturaService;
	}

	public void setBoletoAnexoService(BoletoAnexoService boletoAnexoService) {
		this.boletoAnexoService = boletoAnexoService;
	}

	public void setQuestionamentoFaturasService(
			QuestionamentoFaturasService questionamentoFaturasService) {
		this.questionamentoFaturasService = questionamentoFaturasService;
	}
	
}