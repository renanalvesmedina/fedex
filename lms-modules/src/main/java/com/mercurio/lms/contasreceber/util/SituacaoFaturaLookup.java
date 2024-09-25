package com.mercurio.lms.contasreceber.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Determina as possiveis combinações de situação de fatura
 *
 * @author Mickaël Jalbert
 * @since 29/11/2006
 */
public class SituacaoFaturaLookup {
	
	private Integer intSituacaoFatura;

	private final String digitada = "DI";
	private final String emitida = "EM";
	private final String emBoleto = "BL";
	private final String emRecibo = "RC";
	private final String emRedeco = "RE";
	


	public static final int DISPONIVEL_BOLETO = 0;
	public static final int DISPONIVEL_RECIBO = 1;
	public static final int DISPONIVEL_REDECO = 2;
	public static final int DISPONIVEL_REEMISAO_BOLETO = 3;
	
	
	private final String[] disponivelBoletoArray = {digitada, emitida};
	private final String[] disponivelReciboArray = {digitada, emitida, emBoleto};
	private final String[] disponivelRedecoArray = {digitada, emitida, emBoleto, emRecibo, emRedeco};
	private final String[] disponivelReemissaoBoletoArray = {emitida, emBoleto, emRecibo};
	private final String[] disponivelReemissaoArray = {emitida, emBoleto};	
	
	private Object[] tpSituacaoMatriz = {disponivelBoletoArray, disponivelReciboArray, disponivelRedecoArray, disponivelReemissaoBoletoArray, disponivelReemissaoArray};

	public SituacaoFaturaLookup(Integer intSituacaoFaturaAtual) {
		intSituacaoFatura = intSituacaoFaturaAtual;
	}
	
	public List getTpSituacoesFatura(){ 
		return Arrays.asList((String[]) tpSituacaoMatriz[intSituacaoFatura]);
	}
	
	/**
	 * Valida se a situação informada é valida de acordo com as combinações pré estabelecidas 
	 *
	 * @author Mickaël Jalbert
	 * @since 29/11/2006
	 *
	 * @param strTpSituacaoFatura
	 * @return
	 */
	public boolean validateTpSituacaoFatura(String strTpSituacaoFatura){
		if (intSituacaoFatura != null){
			for (Iterator iter = getTpSituacoesFatura().iterator(); iter.hasNext();){
				String tpSituacaoFatura = (String)iter.next();
				
				//Se a situação informada está dentro da lista
				if (tpSituacaoFatura.equals(strTpSituacaoFatura)){
					return true;
				}
			}
		}
		
		return false;
	}
}