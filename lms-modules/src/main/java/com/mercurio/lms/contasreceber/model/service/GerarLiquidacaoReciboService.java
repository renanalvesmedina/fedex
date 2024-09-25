package com.mercurio.lms.contasreceber.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.Recibo;

/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.contasreceber.gerarLiquidacaoReciboService"
 */
public class GerarLiquidacaoReciboService {
	
	private ReciboService reciboService;
	
	private GerarLiquidacaoFaturaService gerarLiquidacaoFaturaService;
	
	private FaturaService faturaService;
	
	/**
	 * Atualiza o recibo, as fatura e os devedores (passando por a tabela itemFatura).
	 * 
	 * @author Micka�l Jalbert
	 * @since 14/07/2006
	 * 
	 * @param Recibo recibo
	 */
	public void executeLiquidarFatura(Recibo recibo, YearMonthDay dtLiquidacao){
		
		updateRecibo(recibo);
		
		updateFatura(recibo, dtLiquidacao);
	}

	public void executeLiquidarFaturas(List<Recibo> recibos, YearMonthDay dtLiquidacao){
		updateRecibos(recibos);
		updateFaturas(recibos, dtLiquidacao);
	}

	
	/**
	 * Atualiza as fatura do recibo informado passando por a tabela faturaRecibo
	 * 
	 * @author Micka�l Jalbert
	 * @since 14/07/2006
	 * 
	 * @param Recibo recibo
	 */
	private void updateFatura(Recibo recibo, YearMonthDay dtLiquidacao) {
		List lstFatura = faturaService.findByRecibo(recibo.getIdRecibo());
		//Por cada fatura, atualizar-lo e os devedores ligado a ele passando por a tabela itemFatura
		for (Iterator iter = lstFatura.iterator(); iter.hasNext();) {
			Fatura fatura = (Fatura) iter.next();
			
			gerarLiquidacaoFaturaService.executeLiquidarFatura(fatura, dtLiquidacao);
		}
	}

	
	private void updateFaturas(List<Recibo> recibos, YearMonthDay dtLiquidacao) {
		List<Long> idRecibos = extractIdRecibos(recibos);
		List<Fatura> faturas = faturaService.findByRecibos(idRecibos);
		
		gerarLiquidacaoFaturaService.executeLiquidarFaturas(faturas, dtLiquidacao);
	}

	private List<Long> extractIdRecibos(List<Recibo> recibos) {
		List<Long> idRecibos = new ArrayList<Long>();
		
		for(Recibo recibo : recibos) {
			idRecibos.add(recibo.getIdRecibo());
		}
		
		return idRecibos;
	}

	/**
	 * Atualiza o recibo
	 * 
	 * @author Micka�l Jalbert
	 * @since 14/07/2006
	 * 
	 * @param Long idFatura
	 */
	private void updateRecibo(Recibo recibo) {			
		recibo.setTpSituacaoRecibo(new DomainValue("R"));
		reciboService.storeBasic(recibo);
	}

	private void updateRecibos(List<Recibo> recibos) {
		for(Recibo recibo : recibos) {
			recibo.setTpSituacaoRecibo(new DomainValue("R"));	
		}
		
		reciboService.storeAll(recibos);
	}

	public void setGerarLiquidacaoFaturaService(GerarLiquidacaoFaturaService gerarLiquidacaoFaturaService) {
		this.gerarLiquidacaoFaturaService = gerarLiquidacaoFaturaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setReciboService(ReciboService reciboService) {
		this.reciboService = reciboService;
	}
}
