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
	 * Esse metodo leve em consideraçõa o token [NULLO] e
	 * outras operações sobre Strings.
	 * 
	 * @param String a ser convertida
	 */
	@Override
	public Object fromString(String arg0) {
		
		/*
		 * Quando o BPEL mandar o token: [NULO] sempre será retornado
		 * null.
		 */
		if ("[NULO]".equalsIgnoreCase(arg0)){ 
			return null;
		}	
		
		/*
		 *  Ajuste temporarrio: O Xstream assume que qualquer tag:
		 *  EX: <ex/> ou <ex><ex/> como uma String vazia. Esso é um problema
		 *  em layout de tabela por que lá tem null e isso está gerando erro
		 *  lá no binder. Temporariamente estamos assumindo que se vem "" retornamos null.
		 *  Estou esperando pelo Marcelo Menezes para melhorar a solução pois depende do bpel tbm.
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
