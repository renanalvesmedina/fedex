package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.tntbrasil.integracao.domains.financeiro.BoletoDMN;
import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;
import br.com.tntbrasil.integracao.domains.financeiro.FaturaDMN;
import br.com.tntbrasil.integracao.domains.financeiro.RetornoArquivoRemessaDTO;
import br.com.tntbrasil.integracao.domains.jms.Queues;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.BancoService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.HistoricoBoleto;
import com.mercurio.lms.contasreceber.model.HistoricoMotivoOcorrencia;
import com.mercurio.lms.contasreceber.model.MotivoOcorrenciaBanco;
import com.mercurio.lms.contasreceber.model.OcorrenciaBanco;
import com.mercurio.lms.contasreceber.model.RelacaoPagtoParcial;
import com.mercurio.lms.contasreceber.model.RetornoBanco;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;


public class RetornoOcorrenciaBoletoBancoService {

	private static final Long FILIAL_MTZ = 361L;
	private static final Long BANCO_BRADESCO = 237L;
	private static final long USUARIO_INTEGRACAO = 5000L;
	private static final int DECIMAL_DECSIZE = 2;
	private static final int DECIMAL_INTSIZE = 13;
	private static final String PATTERN_YMD = "ddMMyy";
	private BoletoService boletoService;
	private RetornoOcorrenciaBoletoNaoEncontradoService retornoBoletoNaoEncontradoService;
	private RetornoOcorrenciaBoletoEncontradoService retornoBoletoEncontradoService;
	
	private RetornoBancoService retornoBancoService;
	private CreditoBancarioService creditoBancarioService;
	private RelacaoPagtoParcialService relacaoPagtoParcialService;
	private HistoricoBoletoService historicoBoletoService;
	private OcorrenciaBancoService ocorrenciaBancoService;
	private MotivoOcorrenciaBancoService motivoOcorrenciaBancoService;
	private LiquidacaoFaturaService liquidacaoFaturaService;
	private FaturaService faturaService;
	private IntegracaoJmsService integracaoJmsService;
	private UsuarioService usuarioService;
	private HistoricoMotivoOcorrenciaService historicoMotivoOcorrenciaService;
	private ParametroGeralService parametroGeralService;
	private BancoService bancoService;
	
	public void setRetornoBancoService(RetornoBancoService retornoBancoService) {
		this.retornoBancoService = retornoBancoService;
	}

	public BoletoDMN execute(BoletoDMN boletoDmn) {
		validateBoleto(boletoDmn);
		Boleto boleto = boletoService.findByNrBoleto(boletoDmn.getNrBoleto());
		
		if (boleto == null) {
			retornoBoletoNaoEncontradoService.processaBoletoNaoEncontrado(boletoDmn);
		} else {
			boleto.setBrokerIntegration(true);
			retornoBoletoEncontradoService.processaBoletoEncontrado(boleto, boletoDmn);	
		}
		
		return boletoDmn;
	}

	private void validateBoleto(BoletoDMN boletoDmn) {
		List<String> atributosNulos = new ArrayList<String>();
		
		if(boletoDmn.getNrBanco() == null) {
			atributosNulos.add("nr_banco");
		}
		
		if(boletoDmn.getNrBoleto() == null) {
			atributosNulos.add("nr_boleto");
		}
		
		if(boletoDmn.getDtMovimento() == null) {
			atributosNulos.add("dt_movimento");
		}
		
		if (boletoDmn.getVlTotal() == null) {
			atributosNulos.add("vl_total");
		}

		if(boletoDmn.getVlAbatimento() == null) {
			atributosNulos.add("vl_abatimento");
		}
		
		if(boletoDmn.getVlDesconto() == null) {
			atributosNulos.add("vl_desconto");
		}
		
		if(boletoDmn.getVlJuros() == null) {
			atributosNulos.add("vl_juros");	
		}
		
		if(boletoDmn.getNrOcorrencia() == null) {
			atributosNulos.add("nr_ocorrencia");	
		}
			
		if (!atributosNulos.isEmpty()) {
			throw new BusinessException("LMS-36337");
		}
	}
	
	private boolean validateRequiredFields(RetornoArquivoRemessaDTO retornoArquivoRemessaDTO) {
		return StringUtils.isNotBlank(retornoArquivoRemessaDTO.getNrBanco())
				&&StringUtils.isNotBlank(retornoArquivoRemessaDTO.getNrBoleto())
				&&StringUtils.isNotBlank(retornoArquivoRemessaDTO.getDtMovimento())
				&&StringUtils.isNotBlank(retornoArquivoRemessaDTO.getVlTotal())
				&&StringUtils.isNotBlank(retornoArquivoRemessaDTO.getVlAbatimento())
				&&StringUtils.isNotBlank(retornoArquivoRemessaDTO.getVlDesconto())
				&&StringUtils.isNotBlank(retornoArquivoRemessaDTO.getVlJuros())
				&&StringUtils.isNotBlank(retornoArquivoRemessaDTO.getNrOcorrencia())
				&&StringUtils.isNotBlank(retornoArquivoRemessaDTO.getNrMotivoRejeicao())
				&&StringUtils.isNotBlank(retornoArquivoRemessaDTO.getDsLinha())
				;
	}

	public void executeProcessarRetornoBancarioBradesco(RetornoArquivoRemessaDTO retornoArquivoRemessaDTO){
		
		if (!validateRequiredFields(retornoArquivoRemessaDTO)){
			throw new BusinessException("LMS-36337");
		}
		
		List<RetornoBanco> retornosAnteriores = retornoBancoService.fingByNrBoletoDsLinha(StringUtils.leftPad(retornoArquivoRemessaDTO.getNrBoleto(), 13, "0"), 
				JTDateTimeUtils.convertDataStringToYearMonthDay(retornoArquivoRemessaDTO.getDtMovimento(), PATTERN_YMD), 
				retornoArquivoRemessaDTO.getDsLinha());
		
		if (retornosAnteriores == null || retornosAnteriores.isEmpty()){
			Boleto boleto = boletoService.findByNrBoleto(StringUtils.leftPad(retornoArquivoRemessaDTO.getNrBoleto(), 13, "0"));
			if (boleto == null){
				storeRetornoBanco(retornoArquivoRemessaDTO, null, "LMS-36351");
				if (isValidNrOcorrencia(retornoArquivoRemessaDTO.getNrOcorrencia())){
					storeInclusaoCreditoBancario(retornoArquivoRemessaDTO, null, "LMS-36352");
				}
			}else{
				if (isValidNrOcorrencia(retornoArquivoRemessaDTO.getNrOcorrencia())){
					if (validateBoleto(retornoArquivoRemessaDTO, boleto)){
						storeHistoricoBoleto(retornoArquivoRemessaDTO, boleto);
						
						storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36358");
						
						BoletoDMN boletoDmn = getBoletoDtoFromRetornoBanco(retornoArquivoRemessaDTO);
						liquidacaoFaturaService.executeLiquidacao(boleto.getFatura(), boletoDmn);
						
						Fatura fatura = boleto.getFatura();
						fatura.setVlJuroRecebido(extractBigDecimal(retornoArquivoRemessaDTO.getVlJuros(), DECIMAL_INTSIZE ,DECIMAL_DECSIZE));
						faturaService.store(fatura);
						
						FaturaDMN faturaDMN =  retornoBoletoEncontradoService.convertToFaturaIntegrationDomain(fatura, boletoDmn);
						
						JmsMessageSender jmsMessageSender =  integracaoJmsService.createMessage(Queues.FINANCEIRO_BAIXA_FATURA,faturaDMN);
						integracaoJmsService.storeMessage(jmsMessageSender);
						
					}
				}else if(
						Arrays.binarySearch(new String[]{"03", "09", "23", "24", "34"}, retornoArquivoRemessaDTO.getNrOcorrencia()) >= 0
						&&
						!(Arrays.binarySearch(new String[]{"EM","BN","BP"}, boleto.getTpSituacaoBoleto().getValue()) >= 0)
						){
					storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36354");
					
				}else if (Arrays.binarySearch(new String[]{"02", "10", "12", "13", "14", "19", "20", "27", "30", "32", "33"}, retornoArquivoRemessaDTO.getNrOcorrencia()) >= 0){
					storeHistoricoBoleto(retornoArquivoRemessaDTO, boleto);
					storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36358");
				}else if ("23".equals(retornoArquivoRemessaDTO.getNrOcorrencia())){
					storeHistoricoBoleto(retornoArquivoRemessaDTO, boleto);
					storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36358");
					boleto.setTpSituacaoBoleto(new DomainValue("BP"));
					boletoService.store(boleto);
				}else if (Arrays.binarySearch(new String[]{"03", "09", "24", "34"}, retornoArquivoRemessaDTO.getNrOcorrencia()) >= 0){
					storeHistoricoBoleto(retornoArquivoRemessaDTO, boleto);
					storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36358");
					
					if (boleto.getTpSituacaoBoleto().getValue().equals("BN")
							|| boleto.getTpSituacaoBoleto().getValue().equals("BP"))
						boleto.setTpSituacaoBoleto(new DomainValue("EM"));
					boletoService.store(boleto);
				}else if (!(Arrays.binarySearch(new String[]{"02", "03", "06", "09", "10", "12", "13", "14", "15" , "16", "17", "19", "20", "23", "24", "27", "30", "32", "33", "34"}, 
						retornoArquivoRemessaDTO.getNrOcorrencia()) >= 0)){
					storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36359");
				}
			}
		}
	}
	
	private BoletoDMN getBoletoDtoFromRetornoBanco(RetornoArquivoRemessaDTO retornoArquivoRemessaDTO) {
		BoletoDMN boletoDmn = new BoletoDMN();
		boletoDmn.setNrBanco(Long.valueOf(retornoArquivoRemessaDTO.getNrBanco()));
		boletoDmn.setDtMovimento(JTDateTimeUtils.convertDataStringToYearMonthDay(retornoArquivoRemessaDTO.getDtMovimento(),PATTERN_YMD));
		boletoDmn.setVlTotal(extractBigDecimal(retornoArquivoRemessaDTO.getVlTotal(),  DECIMAL_INTSIZE ,DECIMAL_DECSIZE));
		boletoDmn.setVlAbatimento(extractBigDecimal(retornoArquivoRemessaDTO.getVlAbatimento(),  DECIMAL_INTSIZE ,DECIMAL_DECSIZE));
		boletoDmn.setVlJuros(extractBigDecimal(retornoArquivoRemessaDTO.getVlJuros(),  DECIMAL_INTSIZE ,DECIMAL_DECSIZE));
		boletoDmn.setVlDesconto(extractBigDecimal(retornoArquivoRemessaDTO.getVlDesconto(),  DECIMAL_INTSIZE ,DECIMAL_DECSIZE));
		return boletoDmn;
	}

	private void storeHistoricoBoleto(RetornoArquivoRemessaDTO retornoArquivoRemessaDTO, Boleto boleto) {
		List<OcorrenciaBanco> findOcorrenciaBancoForRetornoBanco = ocorrenciaBancoService.findOcorrenciaBancoForRetornoBanco(Short.valueOf(retornoArquivoRemessaDTO.getNrOcorrencia()), 
				boleto.getCedente().getIdCedente());
		
		if (findOcorrenciaBancoForRetornoBanco.isEmpty()) {
			storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36360",new Object[]{retornoArquivoRemessaDTO.getNrOcorrencia()});
		} else {
			
			HistoricoBoleto historicoBoleto = new HistoricoBoleto();
			historicoBoleto.setOcorrenciaBanco(findOcorrenciaBancoForRetornoBanco.iterator().next());
			historicoBoleto.setBoleto(boleto);
			historicoBoleto.setUsuario(getUsuario());
			historicoBoleto.setDhOcorrencia(JTDateTimeUtils.convertDataStringToYearMonthDay(retornoArquivoRemessaDTO.getDtMovimento(),PATTERN_YMD).toDateTimeAtMidnight());
			historicoBoleto.setTpSituacaoHistoricoBoleto(new DomainValue("T"));
			historicoBoletoService.store(historicoBoleto);
			
			if (retornoArquivoRemessaDTO.getNrMotivoRejeicao() != null) {
				
				Long nrOcorrenciaBanco = Long.valueOf(retornoArquivoRemessaDTO.getNrOcorrencia());
				Long idCedente = boleto.getCedente().getIdCedente();
				List<Short> listMotivos = extraiMotivos(retornoArquivoRemessaDTO);
				
				List<MotivoOcorrenciaBanco> findMotivoOcorrenciaForRetornoBanco = motivoOcorrenciaBancoService.findMotivoOcorrenciaForRetornoBanco(nrOcorrenciaBanco.shortValue(), idCedente, listMotivos);
				if(findMotivoOcorrenciaForRetornoBanco.isEmpty()) {
					storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36361",new Object[]{listMotivos});
				} else {
					for (MotivoOcorrenciaBanco motivoOcorrenciaBanco : findMotivoOcorrenciaForRetornoBanco) {
						HistoricoMotivoOcorrencia historicoMotivoOcorrencia = new HistoricoMotivoOcorrencia();
						historicoMotivoOcorrencia.setHistoricoBoleto(historicoBoleto);
						historicoMotivoOcorrencia.setMotivoOcorrenciaBanco(motivoOcorrenciaBanco);
						historicoMotivoOcorrenciaService.store(historicoMotivoOcorrencia);
					}
				}
			}
			
		}
		
	}

	private List<Short> extraiMotivos(RetornoArquivoRemessaDTO retornoArquivoRemessaDTO) {
		List<Short> listMotivos = new ArrayList<Short>();
		String motivos = retornoArquivoRemessaDTO.getNrMotivoRejeicao();
		
		Short m1 = Short.valueOf(motivos.substring(0, 2));
		Short m2 = Short.valueOf(motivos.substring(2, 4));
		Short m3 = Short.valueOf(motivos.substring(4, 6));
		Short m4 = Short.valueOf(motivos.substring(6, 8));
		Short m5 = Short.valueOf(motivos.substring(8, 10));
		
		if (!"00".equals(m1.toString())) { listMotivos.add(m1); }
		if (!"00".equals(m2.toString())) { listMotivos.add(m2); }
		if (!"00".equals(m3.toString())) { listMotivos.add(m3); }
		if (!"00".equals(m4.toString())) { listMotivos.add(m4); }
		if (!"00".equals(m5.toString())) { listMotivos.add(m5); }
		
		return listMotivos;
	}

	private Usuario getUsuario() {
		if (SessionUtils.getFilialSessao() == null) {
			return usuarioService.findById(USUARIO_INTEGRACAO);
		}
		
		return SessionUtils.getUsuarioLogado();
	}

	private boolean validateBoleto(RetornoArquivoRemessaDTO retornoArquivoRemessaDTO, Boleto boleto){
		List<RelacaoPagtoParcial> pagamentosParciais = relacaoPagtoParcialService.findByIdFatura(boleto.getFatura().getIdFatura());
		if (pagamentosParciais != null && pagamentosParciais.size() > 0){
			storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36353");
			storeInclusaoCreditoBancario(retornoArquivoRemessaDTO, boleto, "LMS-36352");
			return false;
		}
		
		
		if (Arrays.binarySearch(new String[]{"EM","BN","BP"}, boleto.getTpSituacaoBoleto().getValue()) < 0){
			storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36354");
			storeInclusaoCreditoBancario(retornoArquivoRemessaDTO, boleto, "LMS-36352");
			return false;
		}
		
		if (!CompareUtils.eq(boleto.getVlTotal(), extractBigDecimal(retornoArquivoRemessaDTO.getVlTotal(), DECIMAL_INTSIZE, DECIMAL_DECSIZE))){
			storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36355");
			storeInclusaoCreditoBancario(retornoArquivoRemessaDTO, boleto, "LMS-36352");
			return false;
		}
		
		BigDecimal vlDesconto = extractBigDecimal(retornoArquivoRemessaDTO.getVlDesconto(), DECIMAL_INTSIZE, DECIMAL_DECSIZE).add(
				extractBigDecimal(retornoArquivoRemessaDTO.getVlAbatimento(), DECIMAL_INTSIZE, DECIMAL_DECSIZE));
		
		if (!CompareUtils.eq(boleto.getVlDesconto(), vlDesconto)){
			storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36356");
			storeInclusaoCreditoBancario(retornoArquivoRemessaDTO, boleto, "LMS-36352");
			return false;
		}
		
		if (!CompareUtils.eq(boleto.getFatura().getVlDesconto(), vlDesconto)){
			storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36374");
			storeInclusaoCreditoBancario(retornoArquivoRemessaDTO, boleto, "LMS-36374");
			return false;
		}

		if (boleto.getFatura().getTpSituacaoAprovacao() != null && Arrays.binarySearch(new String[]{"E","R"}, boleto.getFatura().getTpSituacaoAprovacao().getValue()) >= 0){
			storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36375");
			storeInclusaoCreditoBancario(retornoArquivoRemessaDTO, boleto, "LMS-36375");
			return false;
		}
		
		if (!CompareUtils.eq(boleto.getVlTotal(), boleto.getFatura().getVlTotal())){
			storeRetornoBanco(retornoArquivoRemessaDTO, boleto, "LMS-36357");
			storeInclusaoCreditoBancario(retornoArquivoRemessaDTO, boleto, "LMS-36352");
			return false;
		}
		
		return true;	
	}
	
	
	private boolean isValidNrOcorrencia(String nrOcorrencia){
		return Arrays.binarySearch(new String[]{"06", "15", "16", "17"}, nrOcorrencia) >= 0;
	}
	
	
	private void storeRetornoBanco(RetornoArquivoRemessaDTO retornoArquivoRemessaDTO, Boleto boleto, String msg, Object[] objects) {
		RetornoBanco retornoBanco = new RetornoBanco();
		if(boleto != null){
			retornoBanco.setNrBoleto(retornarNrBoletoFormatado(boleto.getNrBoleto()));
		}else{
			retornoBanco.setNrBoleto(retornarNrBoletoFormatado(retornoArquivoRemessaDTO.getNrBoleto()));
		}
		
		retornoBanco.setBoleto(boleto);
		retornoBanco.setNrBanco(Long.valueOf(retornoArquivoRemessaDTO.getNrBanco()));
		retornoBanco.setDtMovimento(JTDateTimeUtils.convertDataStringToYearMonthDay(retornoArquivoRemessaDTO.getDtMovimento(),PATTERN_YMD));

		retornoBanco.setVlTotal(extractBigDecimal(retornoArquivoRemessaDTO.getVlTotal(),  DECIMAL_INTSIZE ,DECIMAL_DECSIZE));
		retornoBanco.setVlAbatimento(extractBigDecimal(retornoArquivoRemessaDTO.getVlAbatimento(),  DECIMAL_INTSIZE ,DECIMAL_DECSIZE));
		retornoBanco.setVlJuros(extractBigDecimal(retornoArquivoRemessaDTO.getVlJuros(),  DECIMAL_INTSIZE ,DECIMAL_DECSIZE));
		retornoBanco.setVlDesconto(extractBigDecimal(retornoArquivoRemessaDTO.getVlDesconto(),  DECIMAL_INTSIZE ,DECIMAL_DECSIZE));
		
		retornoBanco.setNrOcorrencia(retornoArquivoRemessaDTO.getNrOcorrencia());
		retornoBanco.setNrMotivoRejeicao(retornoArquivoRemessaDTO.getNrMotivoRejeicao());
		retornoBanco.setDsRetornoBanco(parametroGeralService.getMessage(msg, objects));
		retornoBanco.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		retornoBanco.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		retornoBanco.setDsLinha(retornoArquivoRemessaDTO.getDsLinha());
		
		retornoBancoService.store(retornoBanco);
		
	}
	
	private String retornarNrBoletoFormatado(String nrBoleto){
		if (nrBoleto != null && nrBoleto.length() < 13){
			return StringUtils.leftPad(nrBoleto, 13, "0");
		}
		return nrBoleto;
	}
	
	private void storeRetornoBanco(RetornoArquivoRemessaDTO retornoArquivoRemessaDTO, Boleto boleto, String msg) {
		this.storeRetornoBanco(retornoArquivoRemessaDTO, boleto, msg,null);
	}
	
	
	public void storeInclusaoCreditoBancario(RetornoArquivoRemessaDTO retornoArquivoRemessaDTO, Boleto boleto, String msg){
		CreditoBancario creditoBancario = new CreditoBancario();
		
		if (boleto != null){
			creditoBancario.setIdFilial(boleto.getFatura().getFilialByIdFilial().getIdFilial());
			creditoBancario.setIdBanco(boleto.getCedente().getAgenciaBancaria().getBanco().getIdBanco());
			creditoBancario.setDsBoleto(boleto.getNrBoleto());
		}else{
			creditoBancario.setIdFilial(FILIAL_MTZ);
			creditoBancario.setIdBanco(bancoService.findByNrBanco(retornoArquivoRemessaDTO.getNrBanco()).getIdBanco());
			creditoBancario.setDsBoleto(StringUtils.leftPad(retornoArquivoRemessaDTO.getNrBoleto(), 13, "0"));
		}
		creditoBancario.setDtCredito(JTDateTimeUtils.convertDataStringToYearMonthDay(retornoArquivoRemessaDTO.getDtMovimento(), PATTERN_YMD));
		
		BigDecimal vlTotal = extractBigDecimal(retornoArquivoRemessaDTO.getVlTotal(), DECIMAL_INTSIZE, DECIMAL_DECSIZE);
		BigDecimal vlAbatimento = extractBigDecimal(retornoArquivoRemessaDTO.getVlAbatimento(),  DECIMAL_INTSIZE ,DECIMAL_DECSIZE);
		BigDecimal vlDesconto = extractBigDecimal(retornoArquivoRemessaDTO.getVlDesconto(),  DECIMAL_INTSIZE ,DECIMAL_DECSIZE);
		BigDecimal vlJuros = extractBigDecimal(retornoArquivoRemessaDTO.getVlJuros(),  DECIMAL_INTSIZE ,DECIMAL_DECSIZE);

		creditoBancario.setVlCredito(vlTotal.subtract(vlAbatimento).subtract(vlDesconto).add(vlJuros));
		creditoBancario.setTpModalidade("BO");
		creditoBancario.setTpOrigem("R");
		creditoBancario.setTpSituacao("D");
		creditoBancario.setObCreditoBancario(parametroGeralService.getMessage(msg));
		creditoBancario.setIdUsuario(getUsuario().getIdUsuario());
		creditoBancario.setDhAlteracao(JTDateTimeUtils.getDataAtual());
		creditoBancarioService.storeCreditoBancario(creditoBancario);
	}
	
	
	public BoletoService getBoletoService() {
		return boletoService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public RetornoOcorrenciaBoletoNaoEncontradoService getRetornoBoletoNaoEncontradoService() {
		return retornoBoletoNaoEncontradoService;
	}

	public void setRetornoBoletoNaoEncontradoService(RetornoOcorrenciaBoletoNaoEncontradoService retornoBoletoNaoEncontradoService) {
		this.retornoBoletoNaoEncontradoService = retornoBoletoNaoEncontradoService;
	}

	public RetornoOcorrenciaBoletoEncontradoService getRetornoBoletoEncontradoService() {
		return retornoBoletoEncontradoService;
	}

	public void setRetornoBoletoEncontradoService(RetornoOcorrenciaBoletoEncontradoService retornoBoletoEncontradoService) {
		this.retornoBoletoEncontradoService = retornoBoletoEncontradoService;
	}
	
	
	private static BigDecimal extractBigDecimal(String formatedDecimal, int length, int decimal){
		BigDecimal value = BigDecimal.ZERO;
		if (formatedDecimal != null){
			value =  new BigDecimal(formatedDecimal.substring(0,length-decimal).concat(".").concat(formatedDecimal.substring(length-decimal,length)));
		}
		return value;
	}

	public void setCreditoBancarioService(
			CreditoBancarioService creditoBancarioService) {
		this.creditoBancarioService = creditoBancarioService;
	}

	public void setRelacaoPagtoParcialService(
			RelacaoPagtoParcialService relacaoPagtoParcialService) {
		this.relacaoPagtoParcialService = relacaoPagtoParcialService;
	}

	public void setHistoricoBoletoService(
			HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

	public void setOcorrenciaBancoService(
			OcorrenciaBancoService ocorrenciaBancoService) {
		this.ocorrenciaBancoService = ocorrenciaBancoService;
	}

	public void setMotivoOcorrenciaBancoService(
			MotivoOcorrenciaBancoService motivoOcorrenciaBancoService) {
		this.motivoOcorrenciaBancoService = motivoOcorrenciaBancoService;
	}

	public void setLiquidacaoFaturaService(
			LiquidacaoFaturaService liquidacaoFaturaService) {
		this.liquidacaoFaturaService = liquidacaoFaturaService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setHistoricoMotivoOcorrenciaService(HistoricoMotivoOcorrenciaService historicoMotivoOcorrenciaService) {
		this.historicoMotivoOcorrenciaService = historicoMotivoOcorrenciaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setBancoService(BancoService bancoService) {
		this.bancoService = bancoService;
	}
	

}