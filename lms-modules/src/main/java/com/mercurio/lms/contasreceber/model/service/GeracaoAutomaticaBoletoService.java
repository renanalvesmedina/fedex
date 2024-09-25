package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.util.DataVencimentoService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.geracaoAutomaticaBoletoService"
 */
@Assynchronous
public class GeracaoAutomaticaBoletoService extends CrudService<Boleto, Long> {

	private BoletoService boletoService;
	private FaturaService faturaService;
	private HistoricoBoletoService historicoBoletoService;
	private DataVencimentoService dataVencimentoService;
	
	
	@SuppressWarnings("rawtypes")
	@AssynchronousMethod(name = "contasareceber.GeracaoAutomaticaBoleto", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeGerarBoletoAutomatico(){
		List faturas = faturaService.findFaturasWithoutBoleto();
		
		YearMonthDay emissao = new YearMonthDay(); 
		
		for (Object object : faturas) {
			Fatura fatura = (Fatura) object;
			if(faturaService.updateCedente(fatura)){
				faturaService.store(fatura);
			}
			
			YearMonthDay vencimento = createDatavencimento(fatura,emissao);
			
			Boleto boleto = boletoService.generateBoletoDeFatura(fatura, emissao, vencimento,true);
			if(boleto != null){
				fatura.setBoleto(boleto);
				fatura.setTpSituacaoFatura(new DomainValue("BL"));
				fatura.setDtVencimento(vencimento);
				faturaService.store(fatura);
			}
		}
	}

	private YearMonthDay createDatavencimento(Fatura fatura,YearMonthDay emissao) {
		YearMonthDay vencimento = null;
		
		if(fatura != null){
				Long filial = fatura.getFilialByIdFilial() == null?null:fatura.getFilialByIdFilial().getIdFilial();
				Long divisao = fatura.getDivisaoCliente() == null?null:fatura.getDivisaoCliente().getIdDivisaoCliente();
			
				String tpFrete = fatura.getTpFrete() == null?null:fatura.getTpFrete().getValue();
				String tpModal = fatura.getTpModal() == null?null:fatura.getTpModal().getValue();
				String tpAbrangencia = fatura.getTpAbrangencia() == null?null:fatura.getTpAbrangencia().getValue();
				Long idServico = fatura.getServico() == null?null:fatura.getServico().getIdServico();
			
				vencimento = dataVencimentoService.generateDataVencimento(filial, divisao,tpFrete, emissao, tpModal, tpAbrangencia, idServico);
		}
			
		return vencimento;
	}

	
	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

	public HistoricoBoletoService getHistoricoBoletoService() {
		return historicoBoletoService;
	}
	
	public DataVencimentoService getDataVencimentoService() {
		return dataVencimentoService;
	}

	public void setDataVencimentoService(DataVencimentoService dataVencimentoService) {
		this.dataVencimentoService = dataVencimentoService;
	}

	public BoletoService getBoletoService() {
		return boletoService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public FaturaService getFaturaService() {
		return faturaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}
}