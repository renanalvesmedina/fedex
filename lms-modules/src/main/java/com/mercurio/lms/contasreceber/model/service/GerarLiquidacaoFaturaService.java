package com.mercurio.lms.contasreceber.model.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.Recibo;
import com.mercurio.lms.util.JTDateTimeUtils;





/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.contasreceber.gerarLiquidacaoFaturaService"
 */
public class GerarLiquidacaoFaturaService {
	
	private FaturaService faturaService;
	
	private DevedorDocServFatService devedorDocServFatService;
	
	private BoletoService boletoService;
	
	private ReciboService reciboService;
	
	public void executeLiquidarFatura(Fatura fatura){
		executeLiquidarFatura(fatura, JTDateTimeUtils.getDataAtual());
	}
	
	public void executeLiquidarFatura(Fatura fatura, YearMonthDay dtLiquidacao){
 
		updateFatura(fatura, dtLiquidacao);
		
		// Liquida os devedores da fatura
		devedorDocServFatService.liquidaDevedorDocServFat(fatura.getIdFatura()
															, dtLiquidacao, "L");
		
		updateBoleto(fatura.getIdFatura());
		
		updateRecibo(fatura.getIdFatura());
	}

	
	
	public void executeLiquidarFaturas(List<Fatura> faturas, YearMonthDay dtLiquidacao) {
		executeLiquidarFaturas(faturas, dtLiquidacao, null);
	}	
	
	public void executeLiquidarFaturas(List<Fatura> faturas, YearMonthDay dtLiquidacao, Long idRedeco){
	
		updateFaturas(faturas, dtLiquidacao);
		
		for(Fatura fatura : faturas) {
			devedorDocServFatService.liquidaDevedorDocServFat(fatura.getIdFatura(), dtLiquidacao, "L");	
		}
		
		updateBoletos(idRedeco);
		
		updateRecibos(faturas);
	}

	
	/**
	 * Atualiza o recibo ativo da fatura
	 * 
	 * @author Micka�l Jalbert
	 * @since 14/07/2006
	 * 
	 * @param Long idFatura
	 */
	private void updateRecibo(Long idFatura) {
		Recibo recibo = reciboService.findByFatura(idFatura);
		//Se existe recibo ativo na fatura, atualizar-lo com recebido
		if (recibo != null){			
			recibo.setTpSituacaoRecibo(new DomainValue("R"));
			reciboService.storeBasic(recibo);
		}
	}

	
	
	
	private void updateRecibos(List<Fatura> faturas) {
		List<Long> idFaturas = extractIdFaturas(faturas);

		List<Recibo> recibos = reciboService.findByFaturas(idFaturas);
		
		for(Recibo recibo : recibos) {
			recibo.setTpSituacaoRecibo(new DomainValue("R"));
		}
		
		reciboService.storeAll(recibos);
	}

	
	
	/**
	 * Atualiza o boleto ativo da fatura
	 * 
	 * @author Micka�l Jalbert
	 * @since 14/07/2006
	 * 
	 * @param Long idFatura
	 */
	private void updateBoleto(Long idFatura) {
		Boleto boleto = boletoService.findByFatura(idFatura);
		//Se existe boleto ativo na fatura, atualizar-lo com liquidado
		if (boleto != null){			
			boleto.setTpSituacaoBoleto(new DomainValue("LI"));
			boletoService.storeBasic(boleto);
		}
	}

	
	private void updateBoletos(Long idRedeco) {
		List<Boleto> boletos = boletoService.findByRedeco(idRedeco);
		
		for(Boleto boleto : boletos) {
			boleto.setTpSituacaoBoleto(new DomainValue("LI"));
		}
		
		boletoService.storeAll(boletos);
	}

	
	private List<Long> extractIdFaturas(List<Fatura> faturas) {
		List<Long> idFaturas = new ArrayList<Long>();
		
		for(Fatura fatura : faturas) {
			idFaturas.add(fatura.getIdFatura());
		}
		
		return idFaturas;
	}

	/**
	 * Atualiza a situa��o e a data de liquida��o do devedorDocServFat
	 * 
	 * @author Micka�l Jalbert
	 * @since 14/07/2006
	 * 
	 * @param DevedorDocServFat devedorDocServFat
	 */
	private void updateDevedorDocServFat(DevedorDocServFat devedorDocServFat, YearMonthDay dtLiquidacao) {
		//Seta a situa��o do devedor para liquidado e seta a data de liquida��o por a data atual
		devedorDocServFat.setTpSituacaoCobranca(new DomainValue("L"));
		devedorDocServFat.setDtLiquidacao(dtLiquidacao);
		devedorDocServFatService.store(devedorDocServFat);
	}

	/**
	 * Atualiza a situa��o da fatura
	 * 
	 * @author Micka�l Jalbert
	 * @since 14/07/2006
	 * 
	 * @param Fatura fatura
	 */
	private void updateFatura(Fatura fatura, YearMonthDay dtLiquidacao) {
		//Seta a situa��o da fatura para 'Liquidado'
		fatura.setTpSituacaoFatura(new DomainValue("LI"));
		fatura.setDtLiquidacao(dtLiquidacao);
		faturaService.store(fatura);
	}

	
	private void updateFaturas(List<Fatura> faturas, YearMonthDay dtLiquidacao) {
		for(Fatura fatura : faturas) {
			fatura.setTpSituacaoFatura(new DomainValue("LI"));
			fatura.setDtLiquidacao(dtLiquidacao);
		}
		faturaService.storeAll(faturas);
	}

	
	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setReciboService(ReciboService reciboService) {
		this.reciboService = reciboService;
	}
}
