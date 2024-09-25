package com.mercurio.lms.contasreceber.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;




public class SituacaoDevedorDocServFatLookup {
	
	private Integer intSituacaoDevedor;

	private static final String PENDENTE = "P";
	private static final String EM_CARTEIRA = "C";
	private static final String EM_FATURA = "F";
	
	public static final String[] DISPONIVEL_FATURA_ARRAY = {PENDENTE, EM_CARTEIRA};
	public static final String[] DISPONIVEL_BOLETO_ARRAY = {PENDENTE, EM_CARTEIRA, EM_FATURA};
	
	private Object[] tpSituacaoMatriz = {DISPONIVEL_FATURA_ARRAY, DISPONIVEL_BOLETO_ARRAY};

	
	public SituacaoDevedorDocServFatLookup(int intSituacaoDevedorAtual) {
		intSituacaoDevedor = intSituacaoDevedorAtual;
	}
	
	public List getTpSituacaoDevedor(){
		return Arrays.asList((String[])tpSituacaoMatriz[intSituacaoDevedor]);
	}
	
	public boolean validateTpSituacaoDevedor(String strTpSituacaoDevedor){
		if (intSituacaoDevedor != null){
			for (Iterator iter = getTpSituacaoDevedor().iterator(); iter.hasNext();){
				String tpSituacaoDevedor = (String)iter.next();
				
				//Se a situação informada está dentro da lista
				if (tpSituacaoDevedor.equals(strTpSituacaoDevedor)){
					return true;
				}
			}
		}
		
		return false;
	}
}