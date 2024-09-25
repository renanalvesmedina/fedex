package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.DemonstrativoDesconto;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.ReciboDesconto;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.questionamentoFaturas.model.service.QuestionamentoFaturasService;
import com.mercurio.lms.questionamentofaturas.model.QuestionamentoFatura;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.contasreceber.zerarDescontoService"
 */
public class ZerarDescontoService {

	// Variáveis de instância.
	private DescontoService descontoService;
		
	private FaturaService faturaService;
	
	private BoletoService boletoService;

	private HistoricoBoletoService historicoBoletoService;
	
	private WorkflowPendenciaService workflowPendenciaService;
	
	private ReciboDescontoService reciboDescontoService;
	
	private DemonstrativoDescontoService demonstrativoDescontoService;
	
	private GerarFaturaFaturaService gerarFaturaFaturaService;
	
	private QuestionamentoFaturasService questionamentoFaturasService;
	
	/**
	 * Zera o desconto do documento de serviço.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 18/07/2007
	 *
	 *
	 */
	public void executeProcess(DoctoServico ds){
		
		// Caso tenha sido informado um conhecimento 
		if ( ds != null 
				&& ("CTR".equals(ds.getTpDocumentoServico().getValue()) || "CTE".equals(ds.getTpDocumentoServico()
						.getValue()))) {
			processDescontoByDoctoServico(ds);
		} 
		
	}

	/**
	 * Zera os descontos da fatura.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 18/07/2007
	 *
	 *
	 */
	public void executeProcess(Long idFatura){
		
		// Caso tenha sido informada uma fatura.
		if ( idFatura != null ) {
			Fatura fatura = faturaService.findFaturaByIdFatura(idFatura);
			
			if (!SessionUtils.isIntegrationRunning() && !SessionUtils.isBatchJobRunning()
					&& Boolean.TRUE.equals(fatura.getBlOcorrenciaCorp())){
				throw new BusinessException("LMS-36266",new Object[]{fatura.getFilialByIdFilial().getSgFilial(), fatura.getNrFatura().toString()});
			}
			
			processDescontoByFatura(fatura);
		}
		
	}
	
	/**
	 * Zera os descontos da fatura.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 18/07/2007
	 *
	 *
	 */
	public void executeProcessIntegracao(Long idFatura){
		
		// Caso tenha sido informada uma fatura.
		if ( idFatura != null ) {
			processDescontoByFaturaIntegracao(faturaService.findFaturaByIdFatura(idFatura));
		}
		
	}
	
	/**
	 * Executa o processo para um documento de serviço.
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 19/07/2007
	 *
	 * @param ds
	 *
	 */
	private void processDescontoByDoctoServico(DoctoServico ds) {
		DevedorDocServFat devedorDocServFat = (DevedorDocServFat) ds.getDevedorDocServFats().get(0);
		Desconto desconto;
		desconto = devedorDocServFat.getDesconto();
		Fatura faturaByDoctoServico = devedorDocServFat.getItemFatura().getFatura();
		Boleto boletoByDoctoServico = faturaByDoctoServico.getBoleto();
		
		processDesconto(desconto);
		
		// Caso exista fatura, subtrai o desconto.vlDesconto de fatura.vlDesconto.
		if (faturaByDoctoServico != null) {
			faturaByDoctoServico.setVlDesconto(
					faturaByDoctoServico.getVlDesconto().subtract(desconto.getVlDesconto()));
			faturaService.storeBasic(faturaByDoctoServico);
			
			// Caso exista boleto, subtrai o desconto.vlDesconto de boleto.vlDesconto.
			if (boletoByDoctoServico != null) {
				boletoByDoctoServico.setVlDesconto(
						boletoByDoctoServico.getVlDesconto().subtract(desconto.getVlDesconto()));
				boletoService.storeBasic(boletoByDoctoServico);
			}
		}
	}

	/**
	 * Executa o processo para uma fatura.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 19/07/2007
	 *
	 * @param fatura
	 * @param reciboDesconto
	 * @param demonstrativoDesconto
	 * @param boleto
	 *
	 */
	private void processDescontoByFatura(Fatura fatura) {

		Boleto boleto = fatura.getBoleto();
		processFatura(fatura);
		
		// Caso exista o boleto, zera o desconto.
		if (boleto != null) {
			boleto.setVlDesconto(BigDecimal.ZERO);
			
			if ("BL".equals(fatura.getTpSituacaoFatura().getValue())) {
				historicoBoletoService.cancelHistoricoBoletoAbatimento(boleto);
			}
			boletoService.storeBasic(boleto);
		}
		
		// Itera os itens de fatura.
		processItensFatura(fatura);
	}

	/**
	 * Executa o processo para uma fatura.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 19/07/2007
	 *
	 * @param fatura
	 * @param reciboDesconto
	 * @param demonstrativoDesconto
	 * @param boleto
	 *
	 */
	private void processDescontoByFaturaIntegracao(Fatura fatura) {

		Boleto boleto = fatura.getBoleto();
		processFatura(fatura);
		
		// Caso exista o boleto, zera o desconto.
		if (boleto != null) {
			boleto.setVlDesconto(BigDecimal.ZERO);
			boletoService.storeBasic(boleto);
		}

		processItensFatura(fatura);
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 15/10/2007
	 *
	 * @param fatura
	 * @return
	 *
	 */
	private void processFatura(Fatura fatura) {
		DoctoServico ds = gerarFaturaFaturaService.getFirstDoctoServicoFromFatura(fatura, fatura.getItemFaturas());
		
		// Caso tpSituacaoAprovacao seja 'E', cancelar a pendência da fatura.
		if (fatura.getTpSituacaoAprovacao() != null && "E".equals(fatura.getTpSituacaoAprovacao().getValue())){
			if ( faturaService.isQuestionamentoFatura(ds.getTpDocumentoServico()) ) {
				
				QuestionamentoFatura questFatura = questionamentoFaturasService.findById(fatura.getIdPendenciaDesconto());
				questionamentoFaturasService.storeCancelarQuestionamento(questFatura,"");
		}
			else{
				workflowPendenciaService.cancelPendencia(fatura.getIdPendenciaDesconto());
			}	
		}
		
		// Zera alguns atributos da fatura.
		fatura.setVlDesconto(BigDecimal.ZERO);
		fatura.setIdPendenciaDesconto(null);
		fatura.setTpSituacaoAprovacao(null);
		fatura.setTpSetorCausadorAbatimento(null);
		fatura.setMotivoDesconto(null);
		fatura.setObAcaoCorretiva(null);
		fatura.setObFatura(null);
		
		//LMS-5727
		fatura.setFilialByIdFilialDebitada(null);
		
		faturaService.storeBasic(fatura);
	}
	
	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 15/10/2007
	 *
	 * @param fatura
	 *
	 */
	private void processItensFatura(Fatura fatura) {
		Desconto desconto;
		// Itera os itens de fatura.
		for (ItemFatura item : (List<ItemFatura>) fatura.getItemFaturas()) {
			desconto = item.getDevedorDocServFat().getDesconto();
			if (desconto != null) {
//				item.setVersao(item.getVersao() + 1);
//				itemFaturaService.store(item);
//				itemFaturaService.flush();
				processDesconto(desconto);
			}
		}
	}

	/**
	 * Excecuta as regras relativas ao demonstrativo e ao recibo de desconto.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 18/07/2007
	 *
	 * @param desconto
	 *
	 */
	private void processDesconto(Desconto desconto) {
		
		ReciboDesconto reciboDesconto = desconto.getReciboDesconto();
		DemonstrativoDesconto demonstrativoDesconto = desconto.getDemonstrativoDesconto();
		
		// Caso o desconto esteja associado a um reciboDesconto, 
		// subtrai o reciboDesconto.vlReciboDesconto do desconto.vlDesconto.
		if (reciboDesconto != null) {
			reciboDesconto.setVlReciboDesconto(
					reciboDesconto.getVlReciboDesconto().subtract(
							desconto.getVlDesconto()));
			
			// Caso o vlReciboDesconto seja 0, cancela o reciboDesconto.
			if (reciboDesconto.getVlReciboDesconto().equals(BigDecimal.ZERO)) {
				reciboDesconto.setTpSituacaoReciboDesconto(new DomainValue("C"));
			}
			
			reciboDescontoService.store(reciboDesconto);
			desconto.setReciboDesconto(null);
		}
		
		// Caso o desconto esteja associado a um demonstrativoDesconto, 
		// subtrai o demonstrativoDesconto.vlDemonstrativoDesconto do desconto.vlDesconto.
		if (demonstrativoDesconto != null) {
			demonstrativoDesconto.setVlDemonstrativoDesconto(
					demonstrativoDesconto.getVlDemonstrativoDesconto().subtract(
							desconto.getVlDesconto()));
			
			// Caso o vlDemonstrativoDesconto seja 0, cancela o demonstrativoDesconto.
			if (demonstrativoDesconto.getVlDemonstrativoDesconto().equals(BigDecimal.ZERO)) {
				demonstrativoDesconto.setTpSituacaoDemonstrativoDesc(new DomainValue("C"));
			}
			
			demonstrativoDescontoService.store(demonstrativoDesconto);
			desconto.setDemonstrativoDesconto(null);
		}
		
		//Remove o desconto
		descontoService.removeById(desconto.getIdDesconto());
	
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setDemonstrativoDescontoService(
			DemonstrativoDescontoService demonstrativoDescontoService) {
		this.demonstrativoDescontoService = demonstrativoDescontoService;
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setHistoricoBoletoService(
			HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

	public void setReciboDescontoService(ReciboDescontoService reciboDescontoService) {
		this.reciboDescontoService = reciboDescontoService;
	}

	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setGerarFaturaFaturaService(GerarFaturaFaturaService gerarFaturaFaturaService) {
		this.gerarFaturaFaturaService = gerarFaturaFaturaService;
	}

	public void setQuestionamentoFaturasService(
			QuestionamentoFaturasService questionamentoFaturasService) {
		this.questionamentoFaturasService = questionamentoFaturasService;
	}
}
