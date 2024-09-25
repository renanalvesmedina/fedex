package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.financeiro.BoletoDMN;
import br.com.tntbrasil.integracao.domains.financeiro.FaturaDMN;
import br.com.tntbrasil.integracao.domains.jms.Queues;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.RelacaoPagtoParcial;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.util.CompareUtils;

public class RetornoOcorrenciaBoletoEncontradoService {

	private static final Set<String> SITUACOES_BOLETO = 
			new HashSet<String>(Arrays.asList(new String[]{ "EM", "BN", "BP" }));
	
	private static final Set<String> SITUACOES_SEM_PROTESTO = 
			new HashSet<String>(Arrays.asList(new String[]{ "EM", "BN" }));
	
	private static final Set<String> SITUACOES_PROTESTO = 
			new HashSet<String>(Arrays.asList(new String[]{ "BN", "BP" }));
	
	private static final Set<Long> OCORRENCIAS_LIQUIDACAO = 
			new HashSet<Long>(Arrays.asList(new Long[]{ 6L, 7L, 15L, 16L, 31L, 32L, 33L, 36L, 38L, 39L }));
	
	private static final Set<Long> OCORRENCIAS_PROTESTO = 
			new HashSet<Long>(Arrays.asList(new Long[]{ 3L, 9L, 17L, 23L }));
	
	private static final Set<Long> OCORRENCIAS_CONFIRMACAO_ENVIO = 
			new HashSet<Long>(Arrays.asList(new Long[]{ 2L, 10L, 12L, 13L, 14L, 21L, 27L, 37L, 49L, 69L, 72L }));
	
	private static final Set<Long> OCORRENCIAS_BAIXA_BOLETO = 
			new HashSet<Long>(Arrays.asList(new Long[]{ 9L, 3L, 23L }));
	
	private static final Set<Long> OCORRENCIAS_LISTA_COMPLETA = 
			new HashSet<Long>(Arrays.asList(new Long[]{ 2L, 3L, 6L, 7L, 9L, 10L, 12L, 13L, 14L, 15L, 16L, 17L, 
					21L, 23L, 27L, 31L, 32L, 33L, 36L, 37L, 38L, 39L, 49L, 69L, 72L }));

	private LiquidacaoFaturaService liquidacaoFaturaService;
	private RetornoBancoService retornoBancoService;
	private CreditoBancarioService creditoBancarioService;
	private RelacaoPagtoParcialService relacaoPagtoParcialService;
	private HistoricoBoletoService historicoBoletoService;
	private BoletoService boletoService;
	private FaturaService faturaService;
	private IntegracaoJmsService integracaoJmsService;

	public void processaBoletoEncontrado(Boleto boleto, BoletoDMN boletoDmn) {
		if (OCORRENCIAS_LIQUIDACAO.contains(boletoDmn.getNrOcorrencia())) {
			executeOcorrenciasLiquidacao(boleto, boletoDmn);
		} else {
			executeOcorrenciasDiversas(boleto, boletoDmn);
		}
	}

	private void executeOcorrenciasDiversas(Boleto boleto, BoletoDMN boletoDmn) {
		if (OCORRENCIAS_PROTESTO.contains(boletoDmn.getNrOcorrencia()) && 
				!SITUACOES_BOLETO.contains(boleto.getTpSituacaoBoleto().getValue())) {
			
			retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36354");
		} else if(OCORRENCIAS_CONFIRMACAO_ENVIO.contains(boletoDmn.getNrOcorrencia())) {
			if (!historicoBoletoService.rotinaInclusaoHistoricoBoleto(boletoDmn, boleto)) {
				return;
			}
			retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36358");
		} else if(boletoDmn.getNrOcorrencia().equals(17l)) {
			if (!historicoBoletoService.rotinaInclusaoHistoricoBoleto(boletoDmn, boleto)){
				return;
			}
			retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36358");
			
			updateSituacaoBoleto(SITUACOES_SEM_PROTESTO, boleto, "BP");
		} else if(OCORRENCIAS_BAIXA_BOLETO.contains(boletoDmn.getNrOcorrencia())) {
			if (!historicoBoletoService.rotinaInclusaoHistoricoBoleto(boletoDmn, boleto)) {
				return;
			}
			retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36358");

			updateSituacaoBoleto(SITUACOES_PROTESTO, boleto, "EM");
		} else if(!OCORRENCIAS_LISTA_COMPLETA.contains(boletoDmn.getNrOcorrencia())) {
			retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36359");
		} 
	}
	
	private void updateSituacaoBoleto(Set<String> condition, 
			Boleto boleto, String situacaoBoleto) {
		
		if (condition.contains(boleto.getTpSituacaoBoleto().getValue())) {
			boleto.setTpSituacaoAntBoleto(boleto.getTpSituacaoBoleto());
			boleto.setTpSituacaoBoleto(new DomainValue(situacaoBoleto));
			
			boletoService.store(boleto);
		}
	}
	
	private void executeOcorrenciasLiquidacao(Boleto boleto, BoletoDMN boletoDmn) {
		Long idFatura = boleto.getFatura().getIdFatura();
		Fatura fatura = faturaService.findById(idFatura);
		
		List<RelacaoPagtoParcial> pagamentosParciais  = relacaoPagtoParcialService.findByIdFatura(idFatura);
		if (!pagamentosParciais.isEmpty()) {
			retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36353");
			creditoBancarioService.rotinaInclusaoCreditoBancario(boletoDmn, boleto, "LMS-36352");
		} else if (!SITUACOES_BOLETO.contains(boleto.getTpSituacaoBoleto().getValue())) {
			retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36354");
			creditoBancarioService.rotinaInclusaoCreditoBancario(boletoDmn, boleto, "LMS-36352");
		} else if (!CompareUtils.eq(boleto.getVlTotal(), boletoDmn.getVlTotal())) {
			retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36355");
			creditoBancarioService.rotinaInclusaoCreditoBancario(boletoDmn, boleto, "LMS-36352");
		} else if (!descontoBoletoValidation(boleto, boletoDmn)) {
			retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36356");
			creditoBancarioService.rotinaInclusaoCreditoBancario(boletoDmn, boleto, "LMS-36352");
		} else if (!descontoFaturaValidation(boletoDmn, fatura)) {
			retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36374");
			creditoBancarioService.rotinaInclusaoCreditoBancario(boletoDmn, boleto, "LMS-36352");
		} else if (null != fatura.getTpSituacaoAprovacao() && ("E".equals(fatura.getTpSituacaoAprovacao().getValue()) || ("R".equals(fatura.getTpSituacaoAprovacao().getValue())))) {
			retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36375");
			creditoBancarioService.rotinaInclusaoCreditoBancario(boletoDmn, boleto, "LMS-36352");
		} else if (!CompareUtils.eq(boleto.getVlTotal(), boleto.getFatura().getVlTotal())) {
			retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36357");
			creditoBancarioService.rotinaInclusaoCreditoBancario(boletoDmn, boleto, "LMS-36352");
		} else {
			if (!historicoBoletoService.rotinaInclusaoHistoricoBoleto(boletoDmn, boleto)){
				return;
			}
			retornoBancoService.inclusaoMensagemRetorno(boletoDmn, boleto, "LMS-36358");
			liquidacaoFaturaService.executeLiquidacao(boleto.getFatura(), boletoDmn);

			JmsMessageSender jmsMessageSender = 
					integracaoJmsService.createMessage(Queues.FINANCEIRO_BAIXA_FATURA);
			
			jmsMessageSender.addMsg(convertToFaturaIntegrationDomain(boleto.getFatura(), boletoDmn));
			
			integracaoJmsService.storeMessage(jmsMessageSender);
		}
	}
	
	
	public FaturaDMN convertToFaturaIntegrationDomain(Fatura fatura, BoletoDMN boletoDmn) {
		Long idFatura = fatura.getIdFatura();
		String sgFilial = fatura.getFilialByIdFilial().getSgFilial();
		Long idFilial = fatura.getFilialByIdFilial().getIdFilial();
		Long nrFatura = fatura.getNrFatura();
		YearMonthDay dtEmissao = fatura.getDtEmissao();
		YearMonthDay dtVencimento = fatura.getDtVencimento();
		YearMonthDay dtLiquidacao = fatura.getDtLiquidacao();
		BigDecimal vlTotal = fatura.getVlTotal();
		BigDecimal vlDesconto = fatura.getVlDesconto();
		Integer qtDocumentos = fatura.getQtDocumentos();
		String tpSituacaoFatura = fatura.getTpSituacaoFatura().getValue();
		String tpDocumentoServico = getTpDocumentoServico(fatura);
		Long nrBanco = boletoDmn.getNrBanco();
		BigDecimal vlJuroRecebido = boletoDmn.getVlJuros();

		FaturaDMN faturaDMN = new FaturaDMN(idFatura, sgFilial, nrFatura,
				dtEmissao, dtVencimento, dtLiquidacao, vlTotal,
				vlDesconto, qtDocumentos, tpSituacaoFatura,
				tpDocumentoServico, nrBanco, vlJuroRecebido);

		faturaDMN.setIdFilial(idFilial);

		return faturaDMN;
	}

	private String getTpDocumentoServico(Fatura fatura) {
		List itensFatura = faturaService.findItemFatura(fatura.getIdFatura());
		
		if(itensFatura.isEmpty()) {
			return null;
		}
		
		ItemFatura primeiroItemFatura = (ItemFatura) itensFatura.iterator().next();
		
		return primeiroItemFatura.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue();
	}

	private boolean descontoBoletoValidation(Boleto boleto, BoletoDMN boletoDmn) {
		BigDecimal add = boletoDmn.getVlDesconto().add(boletoDmn.getVlAbatimento());
		
		return CompareUtils.eq(boleto.getVlDesconto(), add);
	}

	private boolean descontoFaturaValidation(BoletoDMN boletoDmn, Fatura fatura) {
		BigDecimal vlDescontoFat = fatura.getVlDesconto(); 
		BigDecimal add = boletoDmn.getVlDesconto().add(boletoDmn.getVlAbatimento());
		
		return CompareUtils.eq(vlDescontoFat, add);
	}

	public LiquidacaoFaturaService getLiquidacaoFaturaService() {
		return liquidacaoFaturaService;
	}

	public void setLiquidacaoFaturaService(
			LiquidacaoFaturaService liquidacaoFaturaService) {
		this.liquidacaoFaturaService = liquidacaoFaturaService;
	}

	public CreditoBancarioService getCreditoBancarioService() {
		return creditoBancarioService;
	}

	public void setCreditoBancarioService(
			CreditoBancarioService creditoBancarioService) {
		this.creditoBancarioService = creditoBancarioService;
	}

	public RelacaoPagtoParcialService getRelacaoPagtoParcialService() {
		return relacaoPagtoParcialService;
	}

	public void setRelacaoPagtoParcialService(
			RelacaoPagtoParcialService relacaoPagtoParcialService) {
		this.relacaoPagtoParcialService = relacaoPagtoParcialService;
	}

	public HistoricoBoletoService getHistoricoBoletoService() {
		return historicoBoletoService;
	}

	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

	public RetornoBancoService getRetornoBancoService() {
		return retornoBancoService;
	}

	public void setRetornoBancoService(RetornoBancoService retornoBancoService) {
		this.retornoBancoService = retornoBancoService;
	}

	public BoletoService getBoletoService() {
		return boletoService;
	}
	
	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public IntegracaoJmsService getIntegracaoJmsService() {
		return integracaoJmsService;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public FaturaService getFaturaService() {
		return faturaService;
	}
	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}
}
