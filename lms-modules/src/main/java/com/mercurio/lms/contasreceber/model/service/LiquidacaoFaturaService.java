package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.financeiro.BoletoDMN;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.Recibo;

public class LiquidacaoFaturaService {

	private FaturaService faturaService;
	private BoletoService boletoService;
	private ReciboService reciboService;
	private DevedorDocServFatService devedorDocServFatService;

	public Fatura executeLiquidacao(Fatura fatura, BoletoDMN boletoDmn) {
		validateSituacaoFatura(fatura);
		
		Boleto boleto = boletoService.findByFatura(fatura.getIdFatura());
		if (boleto != null && !boleto.getTpSituacaoBoleto().getValue().equals("CA")) {
			boleto.setTpSituacaoBoleto(new DomainValue("LI"));
			boletoService.store(boleto);
		}

		Recibo recibo = reciboService.findByFatura(fatura.getIdFatura());
		if (recibo != null && !recibo.getTpSituacaoRecibo().equals("CA")) {
			recibo.setTpSituacaoRecibo(new DomainValue("R"));
			reciboService.store(recibo);
		}

		List<DevedorDocServFat> listDev = getDevedorDocServFatService().findByFatura(fatura.getIdFatura());

		for (DevedorDocServFat devedorDocServFat : listDev) {
			devedorDocServFat.setDtLiquidacao(boletoDmn.getDtMovimento());
			devedorDocServFat.setTpSituacaoCobranca(new DomainValue("L"));
			devedorDocServFat.setFatura(fatura);
		}

		getDevedorDocServFatService().storeAll(listDev);

		fatura.setTpSituacaoFatura(new DomainValue("LI"));
		fatura.setDtLiquidacao(boletoDmn.getDtMovimento());
		fatura.setVlJuroRecebido(boletoDmn.getVlJuros());
		fatura.setVlTotalRecebido(boletoDmn.getVlTotal().add(boletoDmn.getVlJuros()).subtract(boletoDmn.getVlDesconto()));
		
		faturaService.store(fatura);
		
		return fatura;
	}

	private void validateSituacaoFatura(Fatura fatura) {
		Set<String> situacoesFaturasValidas = getSituacoesFaturasValidas();
		
		if (situacoesFaturasValidas.contains(fatura.getTpSituacaoFatura().getValue())) {
			throw new BusinessException("LMS-36339", new String[] {
					fatura.getFilialByIdFilial() == null ? "-" : fatura.getFilialByIdFilial().getSgFilial(),
					fatura.getNrFatura().toString() });
		}		
	}

	private Set<String> getSituacoesFaturasValidas() {
		Set<String> situacoesFaturasValidas = new HashSet<String>();
		
		situacoesFaturasValidas.add("CA");
		situacoesFaturasValidas.add("LI");
		
		return situacoesFaturasValidas;
	}
	
	public FaturaService getFaturaService() {
		return faturaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public BoletoService getBoletoService() {
		return boletoService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public ReciboService getReciboService() {
		return reciboService;
	}

	public void setReciboService(ReciboService reciboService) {
		this.reciboService = reciboService;
	}

	public DevedorDocServFatService getDevedorDocServFatService() {
		return devedorDocServFatService;
	}

	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}
	
}
