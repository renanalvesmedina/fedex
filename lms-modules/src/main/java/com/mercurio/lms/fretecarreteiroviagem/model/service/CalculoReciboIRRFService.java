package com.mercurio.lms.fretecarreteiroviagem.model.service;

import java.math.BigDecimal;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ReciboAnuarioRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.ReciboAnuarioRfcService;
import com.mercurio.lms.fretecarreteiroviagem.dto.CalculoReciboIRRFDTO;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.dao.CalculoReciboIRRFDAO;

public class CalculoReciboIRRFService {

	/**
	 * Parâmetros necessários a execucão do cálculo.
	 */
	private static final String ATIVA_CALCULO_IR_RFC = "ATIVA_CALCULO_IR_RFC";	
	private static final String PER_BASE_CARRETEIRO = "PER_BASE_CARRETEIRO";
	private static final String VALOR_MINIMO_IRRF = "VALOR_MINIMO_IRRF";
	
	private static final String PESSOA_JURIDICA = "J";
	private static final String PROPRIETARIO_PROPRIO = "P";
	private static final String RFC_CANCELADO = "CA";
	private static final String RECIBO_COLETA_ENTREGA = "C";
	
	private CalculoReciboIRRFDAO calculoReciboIRRFDAO;
	
	private ParametroGeralService parametroGeralService;
	private ProprietarioService proprietarioService;
	private ReciboFreteCarreteiroService reciboFreteCarreteiroService;
	private ReciboAnuarioRfcService reciboAnuarioRfcService;
		
	/**
	 * Executa cálculo do IRRF do recibo/proprietário requisitado.
	 * 
	 * @param rfc
	 * @param atualizarVlLiquido
	 */
	public void executeCalcularReciboIRRF(ReciboFreteCarreteiro rfc, Boolean atualizarVlLiquido) {	
		Proprietario proprietario = proprietarioService.findById(rfc.getProprietario().getIdProprietario());
		
		if(!isCalcularReciboIRRF(rfc, proprietario, Boolean.FALSE)){
			return;
		}
		
		CalculoReciboIRRFDTO.OUT out = callProcedureCalcIprRenda(rfc, proprietario);				
		
		BigDecimal vlrIRRF = out.getVlIRRF();
		BigDecimal pcAliquotaIrrf = out.getVlAliqIRRF();
		BigDecimal vlAcumuladoIrrf = out.getVlAcumuloIRRF();
		
		if(vlrIRRF != null && vlrIRRF.compareTo(BigDecimal.ZERO) > 0){			
			if(atualizarVlLiquido){
				rfc.setVlLiquido(rfc.getVlLiquido().subtract(vlrIRRF));
			}
			
			rfc.setPcAliquotaIrrf(pcAliquotaIrrf);
			rfc.setVlIrrf(vlrIRRF);
			
			reciboFreteCarreteiroService.store(rfc);
		}
		storeReciboAnuario(rfc, vlAcumuladoIrrf);
	}

	/**
	 * @param rfc
	 * @param vlAcumuladoIrrf
	 */
	private void storeReciboAnuario(ReciboFreteCarreteiro rfc, BigDecimal vlAcumuladoIrrf) {
		ReciboAnuarioRfc reciboAnuarioRfc = new ReciboAnuarioRfc();
		reciboAnuarioRfc.setIdProprietario(rfc.getProprietario().getIdProprietario());
		reciboAnuarioRfc.setIdReciboFreteCarreteiro(rfc.getIdReciboFreteCarreteiro());
		reciboAnuarioRfc.setVlAcumulado(vlAcumuladoIrrf);			
		reciboAnuarioRfcService.store(reciboAnuarioRfc);
	}
	
	/**
	 * Executa estorno do IRRF do recibo/proprietário requisitado.
	 * 
	 * @param rfc
	 */
	public void executeEstornoReciboIRRF(ReciboFreteCarreteiro rfc) {	
		Proprietario proprietario = proprietarioService.findById(rfc.getProprietario().getIdProprietario());
		
		if(!isEstornarReciboIRRF(rfc, proprietario)){
			return;
		}
		
		executeEstornoReciboIRRFDAO(rfc, proprietario);
	}
	
	/**
	 * Invoca procedure 'P_MGC_CALC_IMP_RENDA' para efetuar o cálculo do recibo.
	 * 	
	 * @return CalculoReciboIRRFDTO.OUT
	 * 
	 * @see CalculoReciboIRRFDTO.OUT
	 */
	private void executeEstornoReciboIRRFDAO(ReciboFreteCarreteiro rfc, Proprietario proprietario){		
		CalculoReciboIRRFDTO.IN in = new CalculoReciboIRRFDTO().new IN();
		in.setNrCpf(proprietario.getPessoa().getNrIdentificacao());
		in.setDhEmissaoRFC(rfc.getDhEmissao());
		in.setVlrBruto(rfc.getVlBruto());
		in.setVlInssRFC(rfc.getVlInss());
		in.setVlIRRF(rfc.getVlIrrf());
		in.setPcAliquotaIRRF(rfc.getPcAliquotaIrrf());
		
		calculoReciboIRRFDAO.executeEstornoReciboIRRFDAO(in);
	}
	
	/**
	 * Invoca procedure 'P_MGC_CALC_IMP_RENDA' para efetuar o cálculo do recibo.
	 * 	
	 * @return CalculoReciboIRRFDTO.OUT
	 * 
	 * @see CalculoReciboIRRFDTO.OUT
	 */
	private CalculoReciboIRRFDTO.OUT callProcedureCalcIprRenda(ReciboFreteCarreteiro rfc, Proprietario proprietario){		
		CalculoReciboIRRFDTO.IN in = new CalculoReciboIRRFDTO().new IN();
		in.setAtualizaIRRF(false);
		in.setNrCpf(proprietario.getPessoa().getNrIdentificacao());
		in.setNrDependentes(proprietario.getNrDependentes());
		in.setDhEmissaoRFC(rfc.getDhEmissao());
		in.setVlrMinimoIRRF(getVlrMinimoIRRF());
		in.setVlrPerBaseCarreteiro(getPerBaseCarreteiro());
		in.setVlrBruto(rfc.getVlBruto());
		in.setVlrComplPago(BigDecimal.ZERO);
		in.setVlInssRFC(rfc.getVlInss());		
		
		return calculoReciboIRRFDAO.executeCalculoReciboIRRFDAO(in);
	}

	/**
	 * Verifica se é possível calcular o IRRF para o recibo requisitado.
	 * 
	 * @param rfc
	 * @param proprietario
	 * 
	 * @return boolean
	 */
	private boolean isCalcularReciboIRRF(ReciboFreteCarreteiro rfc, Proprietario proprietario, Boolean isEstorno) {
		if (!isAtivaCalculoIR()) {
			return false;
		}
		
		if(rfc == null || proprietario == null){
			throw new BusinessException("LMS-24050");
		}	
		
		
		if(!isEstorno && RFC_CANCELADO.equals(rfc.getTpSituacaoRecibo().getValue())){
			return false;
		}
		
		if(PROPRIETARIO_PROPRIO.equals(proprietario.getTpProprietario().getValue())){
			return false;
		}
		
		if(PESSOA_JURIDICA.equals(proprietario.getPessoa().getTpPessoa().getValue())){
			return false;
		}
		if(Boolean.TRUE.equals(rfc.getBlAdiantamento())){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Verifica se é possível estornar o valor do recibo.
	 * <p>
	 * 
	 * <b>Por definição apenas é possível estornar recibos que não sejam de COLETA/ENTREGA.<b/>
	 * 
	 * @param rfc
	 * @param proprietario
	 * 
	 * @return boolean
	 */
	private boolean isEstornarReciboIRRF(ReciboFreteCarreteiro rfc, Proprietario proprietario) {
		if(!isCalcularReciboIRRF(rfc, proprietario, Boolean.TRUE)){
			return false;
		}
		
		if(RECIBO_COLETA_ENTREGA.equals(rfc.getTpReciboFreteCarreteiro().getValue())){
			return false;
		}
		
		return true;
	}
	
	private boolean isAtivaCalculoIR() {
		ParametroGeral calcular = parametroGeralService.findByNomeParametro(ATIVA_CALCULO_IR_RFC, false);

		if (calcular != null) {
			return "S".equalsIgnoreCase(calcular.getDsConteudo());
		}

		return false;
	}
	
	private BigDecimal getPerBaseCarreteiro() {
		ParametroGeral param = parametroGeralService.findByNomeParametro(PER_BASE_CARRETEIRO, false);

		if (param != null) {
			return new BigDecimal(param.getDsConteudo());
		}

		return null;
	}
	
	private BigDecimal getVlrMinimoIRRF() {
		ParametroGeral param = parametroGeralService.findByNomeParametro(VALOR_MINIMO_IRRF, false);

		if (param != null) {
			return new BigDecimal(param.getDsConteudo());
		}

		return null;
	}
	
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setCalculoReciboIRRFDAO(CalculoReciboIRRFDAO calculoReciboIRRFDAO) {
		this.calculoReciboIRRFDAO = calculoReciboIRRFDAO;
	}

	public void setProprietarioService(ProprietarioService proprietarioService) {
		this.proprietarioService = proprietarioService;
	}

	public void setReciboFreteCarreteiroService(
			ReciboFreteCarreteiroService reciboFreteCarreteiroService) {
		this.reciboFreteCarreteiroService = reciboFreteCarreteiroService;
	}

	public void setReciboAnuarioRfcService(ReciboAnuarioRfcService reciboAnuarioRfcService) {
		this.reciboAnuarioRfcService = reciboAnuarioRfcService;
	}	
}