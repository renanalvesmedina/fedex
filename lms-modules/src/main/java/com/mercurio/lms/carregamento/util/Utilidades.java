package com.mercurio.lms.carregamento.util;

public abstract class Utilidades {

	private Utilidades() throws InstantiationException {
		throw new InstantiationException("N�o � permitido instanciar a classe Utilidades.");
	}
	
	
	// M�todos a serem declarados
	/**
	 * Realiza o replaceAll em uma string de acordo com os caracteres 
	 * especificados testando se ela � null ou vazia para evitar NullPointerException
	 * 
	 * @author andre.wasem
	 * @since 19/04/2018
	 * 
	 * @param str vari�vel a ser realizado o replaceAll
	 * @param oldChar String a ser substituida pela nova String
	 * @param newChar String que vai substituir a velha String
	 * @return str com valor da String atualizada
	 */
	public static String replaceAll(String str,  String oldChar, String newChar) {
		if (str == null || str.equals("")) {
			return str;
		}
		return str.replaceAll(oldChar, newChar);
	}
}
