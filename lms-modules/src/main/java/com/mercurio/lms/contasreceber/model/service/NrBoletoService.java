package com.mercurio.lms.contasreceber.model.service;

import java.math.BigInteger;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.ComplementoEmpresaCedente;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.nrBoletoService"
 */
public class NrBoletoService {
	private ComplementoEmpresaCedenteService complementoEmpresaCedenteService;
	private CedenteService cedenteService;
	private EmpresaService empresaService;

	/**
	 * Retorna o novo número de Boleto a partir da empresa informada.
	 * 
	 * @author Mickaël Jalbert
	 * @since 20/04/2006
	 * 
	 * @param Long idFilial
	 * @param Long idCedente
	 * @return String
	 */
	public String findNextNrBoleto(Long idFilial, Long idCedente) {		
		// Busca a empresa da filial para ver o próximo boleto
		Long idEmpresa = empresaService.findIdEmpresaByIdFilial(idFilial);

		ComplementoEmpresaCedente complementoEmpresaCedente = complementoEmpresaCedenteService.findLastNrBoleto(idEmpresa, idCedente);
		if (complementoEmpresaCedente == null){
			throw new BusinessException("LMS-36134");
		}

		if (complementoEmpresaCedente.getNrIntervaloFinalBoleto().equals(complementoEmpresaCedente.getNrUltimoBoleto())){
			throw new BusinessException("LMS-36106");
		}

		//Atualiza no banco
		Long nrBoleto = complementoEmpresaCedenteService.generateNextNrBoleto(complementoEmpresaCedente.getIdComplementoEmpresaCedente());


		Cedente cedente = cedenteService.findById(idCedente);

		String nrBoletoCompleto = nrBoleto.toString(); 

		try {
			//Se banco = Banrisul
			if (cedente.getAgenciaBancaria().getBanco().getNrBanco().equals(ConstantesConfiguracoes.COD_BANRISUL)) {
				nrBoletoCompleto = generateNrBoletoBanrisul(FormatUtils.fillNumberWithZero(nrBoleto.toString(),8));
			}

			//Se banco = HSBC
			if (cedente.getAgenciaBancaria().getBanco().getNrBanco().equals(ConstantesConfiguracoes.COD_HSBC)) {
				nrBoletoCompleto = generateNrBoletoHSBC(FormatUtils.fillNumberWithZero(nrBoleto.toString(),10));
			}

			//Se banco = Bradesco
			if (cedente.getAgenciaBancaria().getBanco().getNrBanco().equals(ConstantesConfiguracoes.COD_BRADESCO)) {
				String nrCarteira = FormatUtils.fillNumberWithZero(cedente.getNrCarteira().toString(),3).substring(1,3);
				nrBoletoCompleto = generateNrBoletoBradesco(nrCarteira + ("" + JTDateTimeUtils.getDataAtual().getYear()).substring(2) + FormatUtils.fillNumberWithZero(nrBoleto.toString(),9));
			}	

			//Se banco = Itau
			if (cedente.getAgenciaBancaria().getBanco().getNrBanco().equals(ConstantesConfiguracoes.COD_ITAU)) {
				String nrAgencia = FormatUtils.fillNumberWithZero(cedente.getAgenciaBancaria().getNrAgenciaBancaria().toString(), 4);
				String nrConta = FormatUtils.fillNumberWithZero(cedente.getNrContaCorrente(), 12).substring(6, 11);

				String nrCarteira = null;
				if (cedente.getNrCarteira() != null){
					nrCarteira = FormatUtils.fillNumberWithZero(cedente.getNrCarteira().toString(),3);
				}
				nrBoletoCompleto = generateNrBoletoItau(nrAgencia + nrConta + nrCarteira + FormatUtils.fillNumberWithZero(nrBoleto.toString(),8));
			}
		} catch (Exception e) {
			throw new BusinessException("LMS-36135");
		}

		return FormatUtils.fillNumberWithZero(nrBoletoCompleto, 13);
	}
	
	/**
	 * Gera o número de boleto para o banco Banrisul
	 * */
	public static String generateNrBoletoBanrisul(String nrBoleto){
		nrBoleto = nrBoleto + generatePrimeiroDigitoBanrisul(nrBoleto);

		int digito = generateSegundoDigitoBanrisul(nrBoleto);
		int digitoAnterior = Integer.valueOf(nrBoleto.substring(8, 9)).intValue();
		while (digito == 10){
			if (digitoAnterior == 9){
				digitoAnterior = 0;
			} else {
				digitoAnterior = digitoAnterior + 1;
			}
			nrBoleto = nrBoleto.substring(0,8) + digitoAnterior;

			digito = generateSegundoDigitoBanrisul(nrBoleto);
		}

		nrBoleto = nrBoleto + digito;		
		return nrBoleto;
	}

	private static int generatePrimeiroDigitoBanrisul(String nrBoleto){
		int somatorio = 0;

		somatorio = adicionaUmDigito(nrBoleto, somatorio, 0, 1, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 1, 2, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 2, 1, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 3, 2, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 4, 1, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 5, 2, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 6, 1, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 7, 2, true);

		BigInteger mod10 = new BigInteger(Integer.valueOf(somatorio).toString());

		int digito = 10 - mod10.mod(new BigInteger("10")).intValue();

		if (digito > 9){
			return 0;
		} else {
			return digito;
		}
	}

	private static int generateSegundoDigitoBanrisul(String nrBoleto){
		int somatorio = 0;

		somatorio = adicionaUmDigito(nrBoleto, somatorio, 0, 4, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 1, 3, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 2, 2, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 3, 7, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 4, 6, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 5, 5, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 6, 4, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 7, 3, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 8, 2, false);

		BigInteger mod11 = new BigInteger(Integer.valueOf(somatorio).toString());

		int digito = 11 - mod11.mod(new BigInteger("11")).intValue();

		if (digito > 10){
			return 0;
		} else {			
			return digito;
		}
	}

	/**
	 * Gera o número de boleto para o banco HSBC
	 * */	
	public static String generateNrBoletoHSBC(String nrBoleto){
		int somatorio = 0;

		somatorio = adicionaUmDigito(nrBoleto, somatorio, 0, 5, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 1, 4, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 2, 3, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 3, 2, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 4, 7, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 5, 6, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 6, 5, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 7, 4, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 8, 3, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 9, 2, false);		

		BigInteger mod11 = new BigInteger(Integer.valueOf(somatorio).toString());

		int digito = 11 - mod11.mod(new BigInteger("11")).intValue();

		if (digito > 9) {
			return nrBoleto + 0;
		} else {			
			return nrBoleto + digito;
		}
	}

	/**
	 * Gera o número de boleto para o banco Bradesco
	 * */	
	public static String generateNrBoletoBradesco(String nrBoleto){
		int somatorio = 0;

		somatorio = adicionaUmDigito(nrBoleto, somatorio, 0, 2, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 1, 7, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 2, 6, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 3, 5, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 4, 4, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 5, 3, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 6, 2, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 7, 7, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 8, 6, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 9, 5, false);		
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 10, 4, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 11, 3, false);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 12, 2, false);

		BigInteger mod11 = new BigInteger(Integer.valueOf(somatorio).toString());

		int digito = 11 - mod11.mod(new BigInteger("11")).intValue();

		if (digito == 10){
			return nrBoleto.substring(2, 13) + "P";
		} else {
			if (digito > 10) {
				digito = 0;
			}
			return nrBoleto.substring(2, 13) + digito;
		}
	}

	/**
	 * Gera o número de boleto para o banco Itau
	 * */	
	public static String generateNrBoletoItau(String nrBoleto){
		int somatorio = 0;

		somatorio = adicionaUmDigito(nrBoleto, somatorio, 0, 1, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 1, 2, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 2, 1, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 3, 2, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 4, 1, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 5, 2, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 6, 1, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 7, 2, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 8, 1, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 9, 2, true);		
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 10, 1, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 11, 2, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 12, 1, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 13, 2, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 14, 1, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 15, 2, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 16, 1, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 17, 2, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 18, 1, true);
		somatorio = adicionaUmDigito(nrBoleto, somatorio, 19, 2, true);

		BigInteger mod10 = new BigInteger(Integer.valueOf(somatorio).toString());

		int digito = 10 - mod10.mod(new BigInteger("10")).intValue();
		if (digito > 9) {
			return nrBoleto.substring(12, 20) + 0;
		} else {		
			return nrBoleto.substring(12, 20) + digito;
		}
	}

	/**
	 * Adiciona o valor multiplicado do número selecionado
	 * */
	private static int adicionaUmDigito(String nrBoleto, int somatorio, int posicao, int multiplicador, boolean blMaiorQueNove){
		somatorio = somatorio + multiplica(nrBoleto.substring(posicao, posicao + 1), multiplicador, blMaiorQueNove);
		return somatorio;
	}

	/**
	 * Multiplica o número por o multiplicador, se o blMaiorQueNove = true e que o valor
	 * multiplicado  é maior que nove, ele retorna o valor menos 9
	 * */
	private static int multiplica(String nrBoletoDigit, int multiplicador, boolean blMaiorQueNove){
		int digit = Integer.valueOf(nrBoletoDigit).intValue();
		digit = digit * multiplicador;

		if (digit > 9 && blMaiorQueNove == true){
			digit = digit - 9;
		}

		return digit;
	}	

	public void setCedenteService(CedenteService cedenteService) {
		this.cedenteService = cedenteService;
	}

	public void setComplementoEmpresaCedenteService(ComplementoEmpresaCedenteService complementoEmpresaCedenteService) {
		this.complementoEmpresaCedenteService = complementoEmpresaCedenteService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
}