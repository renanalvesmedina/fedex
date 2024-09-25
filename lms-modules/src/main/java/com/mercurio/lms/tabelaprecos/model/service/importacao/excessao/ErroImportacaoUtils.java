package com.mercurio.lms.tabelaprecos.model.service.importacao.excessao;

public class ErroImportacaoUtils {

	private static final int TOTAL_LETRAS_ALFABETO = 26;
	private static final String[] ALFABETO = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	
	public String converteColuna(int col){
        String celula = "";
        if (col > TOTAL_LETRAS_ALFABETO){
            int idxPrimeiraLetra = Math.abs(col / TOTAL_LETRAS_ALFABETO);
            int idxSegundaLetra = col - (Math.abs(col / TOTAL_LETRAS_ALFABETO) * TOTAL_LETRAS_ALFABETO);
            if (idxSegundaLetra == 0){
                celula = String.valueOf(ALFABETO[idxPrimeiraLetra - 2] + ALFABETO[25]);
            }else{
                celula = String.valueOf(ALFABETO[idxPrimeiraLetra - 1] + ALFABETO[idxSegundaLetra - 1]);
            }
        }else{
        	celula = ALFABETO[col -1];
        }
        return celula;
    }
	
}
