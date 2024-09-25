package com.mercurio.lms.integracao.util;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class StringConverter extends AbstractSingleValueConverter {

	@Override
	@SuppressWarnings("unchecked")
	public boolean canConvert(Class arg0) {
		return String.class.equals(arg0); 
	}
	
	/**
	 * Metodo que converte os Objetos de String para String.
	 * Esse metodo leve em considera��a o token [NULLO] e
	 * outras opera��es sobre Strings.
	 * 
	 * @param String a ser convertida
	 */
	@Override
	public Object fromString(String arg0) {
		
		/*
		 * Quando o BPEL mandar o token: [NULO] sempre ser� retornado
		 * null.
		 */
		if ("[NULO]".equalsIgnoreCase(arg0)){ 
			return null;
		}	
		
		/*
		 *  Ajuste temporarrio: O Xstream assume que qualquer tag:
		 *  EX: <ex/> ou <ex><ex/> como uma String vazia. Esso � um problema
		 *  em layout de tabela por que l� tem null e isso est� gerando erro
		 *  l� no binder. Temporariamente estamos assumindo que se vem "" retornamos null.
		 *  Estou esperando pelo Marcelo Menezes para melhorar a solu��o pois depende do bpel tbm.
		 */		
		if ("".equals(arg0)){
			return null;
		}
		
		/*
		 * 
		 */
		String newStr = (String)arg0.trim().toString();
		return newStr;
	}
	

}
