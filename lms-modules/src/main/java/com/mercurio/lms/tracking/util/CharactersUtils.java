package com.mercurio.lms.tracking.util;


public class CharactersUtils {

	/**
	 * Remove os caracteres PT_BR» do início da String
	 * str e o caractere ¦ do fim. Tais caracteres estão contidos
	 * nos valores das colunas do banco de dados da TNT para indicar
	 * a linguagem do registro, e não são utilizados pela aplicação.
	 * 
	 * @param str
	 * @return
	 */
	public static String cleanLocalization(String str){
		if(str == null){
			return str;
		}
		String result =	str.replaceAll("[pP][tT]_[Bb][Rr]»", "");
		result = result.replaceAll("¦", "");
		return result;
	}
	
	/**
	 * Metodo responsavel por remover String no final da frase (em/de/para)
	 * 
	 * @param frase
	 * @return <String>
	 */
	public static String cleanPreposicaoFinalStr(String frase){
		if(frase != null){
			frase = frase.substring(0,1).toUpperCase()
					.concat(frase.substring(1).toLowerCase());
			
			int indexOfLastSpace = frase.trim().lastIndexOf(" ");
				if(indexOfLastSpace > 0){
					String lastStrLocalizacaoMercadoria = frase.substring(indexOfLastSpace+1);
					
					if(("para".equals(lastStrLocalizacaoMercadoria) 
						|| "em".equals(lastStrLocalizacaoMercadoria)
						|| "de".equals(lastStrLocalizacaoMercadoria))
					){
						return frase.substring(0,indexOfLastSpace);
					}
				}
		}
		
		return frase;
	}
}
