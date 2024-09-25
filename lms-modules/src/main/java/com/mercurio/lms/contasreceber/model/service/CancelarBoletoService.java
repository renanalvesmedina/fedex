package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.ParametroBoletoFilial;
import com.mercurio.lms.configuracoes.model.service.ParametroBoletoFilialService;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.HistoricoBoleto;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.MotivoOcorrencia;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.cancelarBoletoService"
 */
public class CancelarBoletoService {
	private BoletoService boletoService;
	private WorkflowPendenciaService workflowPendenciaService;
	private HistoricoBoletoService historicoBoletoService;
	private FaturaService faturaService;
	private ParametroBoletoFilialService parametroBoletoFilialService;
	private GerarFaturaBoletoService gerarFaturaService;
 	private BoletoAnexoService boletoAnexoService;
 	private DevedorDocServFatService devedorDocServFatService;
 	private DescontoService descontoService;
 	private ItemFaturaService itemFaturaService;

 	public Boleto executeCancelarBoleto(Boleto boleto, MotivoOcorrencia motivoOcorrencia , String dsHistoricoBoleto, ItemList listAnexos){
		validateBoleto(boleto);
		String tpSituacao = boleto.getTpSituacaoBoleto().getValue();

		boletoAnexoService.storeAnexos(listAnexos);
		//Se a situação é 'Em banco' ou 'Em banco com protesto' E
		//que o parametro da filial pede a geração de um workflow
		if (tpSituacao.equals("BN") || tpSituacao.equals("BP")){
			if(listAnexos != null){
				boletoAnexoService.validaBoletoAnexo(boleto, listAnexos);
			}
			boleto = cancelBoletoComplexo(boleto, motivoOcorrencia, dsHistoricoBoleto);
		} else {
			boleto = cancelBoletoSimple(boleto, motivoOcorrencia, dsHistoricoBoleto);
		}

		return boleto;
	}

	/**
	 * Cancela o boleto (Integração), difere do cancelar padrão pq não gera historico nem workflow.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/06/2007
	 *
	 * @param boleto
	 * @return
	 *
	 */
	public Boleto executeCancelarBoletoIntegracao(Boleto boleto){
		validateBoleto(boleto);
		updateRegistros(boleto);
		return boleto;
	}

	private void validateBoleto(Boleto boleto){
		String tpSituacao = boleto.getTpSituacaoBoleto().getValue();

		//A situação só pode ser:
		//'Digitado', 'Emitido',
		//'Em banco', 'Devilvido pelo banco'
		//'Gerado por manifesto de viagem', 'Gerado por manifesto de entrega'
		//'Em banco com protesto'
		if (!tpSituacao.equals("DI") && !tpSituacao.equals("EM") && 
			!tpSituacao.equals("BN") && !tpSituacao.equals("DB") && 
			!tpSituacao.equals("GM") && !tpSituacao.equals("GE") && 
			!tpSituacao.equals("BP")){
			throw new BusinessException("LMS-36093");			
		}
	}

	/**
	 * Faz os update em cima das entidades do boleto que é cancelado
	 * */
	private Boleto cancelBoletoSimple(Boleto boleto, MotivoOcorrencia motivoOcorrencia , String dsHistoricoBoleto){
		String tpSituacaoBoleto = boleto.getTpSituacaoBoleto().getValue();

		Short cdOcorrenciaBanco = ConstantesConfiguracoes.CD_OCORRENCIA_RETRANSMITIR;

		// Se o boleto está em banco, o códico da ocorrencia deve ser '2'
		if (tpSituacaoBoleto.equals("BN") || tpSituacaoBoleto.equals("BP")){
			cdOcorrenciaBanco = ConstantesConfiguracoes.CD_OCORRENCIA_PEDIDO_BAIXA;
		}
		historicoBoletoService.generateHistoricoBoleto(boleto, cdOcorrenciaBanco, "REM", motivoOcorrencia, dsHistoricoBoleto);
		boleto = boletoService.findByIdTela(boleto.getIdBoleto()); 
		updateRegistros(boleto);
		
		return boleto;
	}

	/**
	 * Cancela o boleto informado
	 * */	
	private void updateBoleto(Boleto boleto){
		boleto.setTpSituacaoBoleto(new DomainValue("CA"));
		boletoService.store(boleto);
	}

	/**
	 * Troca a situação da fatura para 'Emitido'
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 06/03/2007
	 *
	 * @param fatura
	 */
	private void updateFatura(Fatura fatura) {

		if(SessionUtils.isIntegrationRunning() && fatura.getNrFatura().compareTo(Long.valueOf(10000000)) > 0){
			fatura.setTpSituacaoFatura(new DomainValue("CA"));
		}else{
		fatura.setTpSituacaoFatura(new DomainValue("EM"));
		}
		fatura.setDhTransmissao(null);
		fatura.setBoleto(null);
		fatura.setVlJuroCalculado(BigDecimal.ZERO);

		faturaService.storeBasic(fatura);
	}

	/**
	 * Valida se tem que gerar um workflow na hora de cancelar
	 */
	private boolean validateParametroBoletoFilial(Long idFilial){
		ParametroBoletoFilial parametroBoletoFilial = parametroBoletoFilialService.findParametroBoletoFilialVigenteByIdFilial(idFilial, JTDateTimeUtils.getDataAtual());
		if (parametroBoletoFilial != null){
			return parametroBoletoFilial.getBlWorkflowCancelamento().booleanValue();
		} else {
			return false;
		}
	}

	/**
	 * Gera o historico e a pendencia de workflow do boleto a ser cancelado.
	 * */	
	private Boleto cancelBoletoComplexo(Boleto boleto, MotivoOcorrencia motivoOcorrencia , String dsHistoricoBoleto){
		
		String dsProcesso = "Aprovação para cancelamento referente a fatura: " 
			   + boleto.getFatura().getFilialByIdFilial().getSgFilial() + " "
			   + FormatUtils.formataNrDocumento(boleto.getFatura().getNrFatura().toString(), "FAT");

		HistoricoBoleto historicoBoleto = historicoBoletoService.generateHistoricoBoletoAndCancelActives(boleto, ConstantesConfiguracoes.CD_OCORRENCIA_PEDIDO_BAIXA, "REM", motivoOcorrencia, dsHistoricoBoleto, new DomainValue("E"));			
		
		Fatura fatura = faturaService.findById(boleto.getFatura().getIdFatura());
		
		/* Solicitado na ET 36.02.03.06 para retirar essa validação
		
			
		if(  faturaService.isQuestionamentoFatura(doctoServico.getTpDocumentoServico())){
			
		} else {
		historicoBoleto = generatePendenciaWorkflow(historicoBoleto, dsProcesso);
		}
	 	*/
		DoctoServico doctoServico =  getGerarFaturaService().getFirstDoctoServicoFromFatura(fatura, fatura.getItemFaturas());

		generateQuestionamentoFatura(historicoBoleto, dsProcesso,doctoServico);
		historicoBoletoService.store(historicoBoleto);
		boletoAnexoService.executeMoverAnexosToQuestionamento(historicoBoleto);
		
		if(SessionUtils.isIntegrationRunning() && fatura.getNrFatura().compareTo(Long.valueOf(10000000)) > 0){
			fatura.setTpSituacaoFatura(new DomainValue("CA"));
		}
		
		return boleto; 
	}

	private void generateQuestionamentoFatura(HistoricoBoleto historicoBoleto,
												String dsProcesso, 
												DoctoServico documentoServico) {
		historicoBoletoService.generateQuestionamentoFatura(historicoBoleto, dsProcesso, documentoServico,false,true,historicoBoleto.getBoleto().getDtVencimento(), null,historicoBoleto.getMotivoOcorrencia(),null);

	}

	/**
	 * Gera uma pendencia de cancelamento de boleto caso o boleto esté 'Em banco'
	 * */
	private HistoricoBoleto generatePendenciaWorkflow(HistoricoBoleto historicoBoleto, String dsProcesso) {

		historicoBoleto.setIdPendencia(workflowPendenciaService.generatePendencia(historicoBoleto.getBoleto().getFatura().getFilialByIdFilial().getIdFilial(), 
				ConstantesWorkflow.NR3610_CANCEL_BOLE_BCO, historicoBoleto.getIdHistoricoBoleto(), dsProcesso, 
				JTDateTimeUtils.getDataHoraAtual()).getIdPendencia());			
		historicoBoletoService.store(historicoBoleto);

		return historicoBoleto;
	}

	public String executeWorkflow(List lstIdHistorico, List lstTpSituacao){
		String tpSituacao = (String)lstTpSituacao.get(0);
		Long idHistorico = (Long)lstIdHistorico.get(0);
		HistoricoBoleto historicoBoleto = historicoBoletoService.findById(idHistorico);
		historicoBoleto.setTpSituacaoAprovacao(new DomainValue(tpSituacao));		
		String tpSituacaoBoleto = historicoBoleto.getBoleto().getTpSituacaoBoleto().getValue();
		//Se foi aprovado e o boleto estar 'Em banco' ou 'Em banco com protesto'
		if (tpSituacao.equals("A") && (tpSituacaoBoleto.equals("BN") || tpSituacaoBoleto.equals("BP"))){
			Boleto boleto = historicoBoleto.getBoleto();

			updateRegistros(boleto);
		} else { //Senão cancelar o historico
			historicoBoleto.setTpSituacaoHistoricoBoleto(new DomainValue("C"));
		}
		historicoBoletoService.store(historicoBoleto);

		return null;
	}

	/**
	 * @param boleto
	 */
	private void updateRegistros(Boleto boleto) {
		Fatura fatura = boleto.getFatura();
		
		updateBoleto(boleto);
		updateFatura(fatura);
		
		if(!fatura.getBlCancelaFaturaInteira() && (fatura.getTpSituacaoAprovacao() == null || "A".equals(fatura.getTpSituacaoAprovacao().getValue()))){
			BigDecimal vlTotalDoctosExcluidos = new BigDecimal(0L);
			Integer vlQtDoctosExcluidos = 0;
			BigDecimal vlTotalDescontosExcluidos = new BigDecimal(0L);
			
			List<ItemFatura> items = fatura.getItemFaturas();
			for (ItemFatura itemFatura : items) {
				if(itemFatura.getBlExcluir()){
					DevedorDocServFat devedor = itemFatura.getDevedorDocServFat();
					devedor.setTpSituacaoCobranca(new DomainValue("C"));
					devedor.setFatura(null);
					
					List<Desconto> descontos =devedor.getDescontos();
					
					for (Desconto desconto : descontos) {
						if(desconto.getIdPendencia() == null){
							vlTotalDescontosExcluidos = vlTotalDescontosExcluidos.add(desconto.getVlDesconto());
							descontoService.removeById(desconto.getIdDesconto());
						}
					}
					
					devedorDocServFatService.store(devedor);
					itemFaturaService.removeById(itemFatura.getIdItemFatura());
					
					vlTotalDoctosExcluidos = vlTotalDoctosExcluidos.add(devedor.getVlDevido());
					vlQtDoctosExcluidos++;
				}
			}
			
			if(vlQtDoctosExcluidos > 0){
				if(IntegerUtils.hasValue(fatura.getQtDocumentos())){
					fatura.setQtDocumentos(fatura.getQtDocumentos().intValue() - vlQtDoctosExcluidos);
				}
				if(BigDecimalUtils.hasValue(fatura.getVlTotal())){
				fatura.setVlTotal(fatura.getVlTotal().subtract(vlTotalDoctosExcluidos));
				}
				if(BigDecimalUtils.hasValue(fatura.getVlDesconto())){
				fatura.setVlDesconto(fatura.getVlDesconto().subtract(vlTotalDescontosExcluidos));
				}
				
				faturaService.updateCedente(fatura);
				faturaService.store(fatura);
				
				boletoService.criaBoleto(fatura);
			}
			
		} 
		
		if (fatura.getBlCancelaFaturaInteira()) {
			faturaService.cancelFatura(fatura.getIdFatura());
		}

		/**
		 * Não faz mais update no devedor, pedido por Joelson
		 * 
		 * @author Mickaël Jalbert
		 * @since 10/11/2006
		 */		
		//updateHistoricoBoleto(boleto);
		/**
		 * Não faz mais update no devedor, pedido por Hugo e Rita
		 * 
		 * @author Mickaël Jalbert
		 * @since 16/08/2006
		 */
		//updateDevedorDocServFat(boleto.getFatura());
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setParametroBoletoFilialService(ParametroBoletoFilialService parametroBoletoFilialService) {
		this.parametroBoletoFilialService = parametroBoletoFilialService;
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

	public DevedorDocServFatService getDevedorDocServFatService() {
		return devedorDocServFatService;
	}

	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public DescontoService getDescontoService() {
		return descontoService;
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	public ItemFaturaService getItemFaturaService() {
		return itemFaturaService;
	}

	public void setItemFaturaService(ItemFaturaService itemFaturaService) {
		this.itemFaturaService = itemFaturaService;
	}
}