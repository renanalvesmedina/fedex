package com.mercurio.lms.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * @author Anibal Maffioletti de Deus
 * 
 * Funções de validação CUIT (Argentina)
 * 						RUT (Chile)
 *
 */
public class ValidateUtils {
	
	private static final Pattern VALID_EMAIL_PATTERN = Pattern.compile("^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$");
	private static final Pattern NO_NUMBERS = Pattern.compile("\\D");

	/**
	 * <p>Verifica se o 'e-mail' informado segue padroes basicos de um 'e-mail'.</p>
	 * <p>Retorna um <code>boolean</code> para caso o e-mail seja verdadeiro ou falso.</p>
	 * 
	 * @param email e-mail a ser informado.
	 * @return boolean
	 */
	public static boolean validateEmail(String email) {
	    return VALID_EMAIL_PATTERN.matcher(email).matches();
	}

	/**
	 * Retira todos os caracteres não numéricos e devolve somente os números
	 * 
	 * @param str
	 * @return
	 */
	private static String clearNoNumbers(String str){
		return NO_NUMBERS.matcher(str).replaceAll("");
	}

	/**
	 * Valida se a string e ou nao uma CPF/CNPJ valido. O CPF/CNPJ
	 * podem estar tanto no formato formatado quanto no formato
	 * somente numérico. 
	 * 
	 * @param cpfOrCnpj
	 * @return
	 */
	public static boolean validateCpfOrCnpj(String cpfOrCnpj){
        if (cpfOrCnpj == null) return false;
		String numbers = clearNoNumbers(cpfOrCnpj);
		int length = numbers.length();
		if (!(length == 11 || length == 14)) return false;
		boolean isCnpj = length == 14;

		int[] foundDv = {0,0};
        int dv1 = Integer.parseInt(String.valueOf(numbers.charAt(length-2)));
        int dv2 = Integer.parseInt(String.valueOf(numbers.charAt(length-1)));

        for (int j = 0; j < 2; j++) {
        	int sum = 0;
            int coeficient = 2;
            for (int i = length - 3 + j; i >= 0 ; i--){
            	int digit = Integer.parseInt(String.valueOf(numbers.charAt(i)));               
                sum += digit * coeficient;
                coeficient ++;
                if (coeficient > 9 && isCnpj) coeficient = 2;                
            }
            foundDv[j] = 11 - sum % 11;
            if (foundDv[j] >= 10) foundDv[j] = 0;
        }
        return dv1 == foundDv[0] && dv2 == foundDv[1];
	}
	
	/**
	 * Valida se a string nrIns eh ou nao uma Inscricao Estadual valida. O nrIns deve ser 
	 * informado sem mascara. O parametro sgUf deve ser informado, devido cada estado 
	 * possuir sua validacao de IE. 
	 * 
	 * @param sgUf
	 * @param nrIns
	 * @return
	 */
	public static boolean validateInscricaoEstadual(String sgUf, String nrIns) {
		return ValidateInscricaoEstadual.validateInscricaoEstadual(sgUf, nrIns);
	}
	
	
	public static Boolean validateChaveNfe(String chaveNfe) {
		return chaveNfe.length() == 44 && validateDigitoVerificadorNfe(chaveNfe);
	}
	/**
	 * Valida o digito verificador da Chave Nfe
	 * @param String chaveNfe
	 * @return boolean
	 */
	public static boolean validateDigitoVerificadorNfe(String chaveNfe){	
		String dvChaveNfe = chaveNfe.substring(chaveNfe.length() - 1, chaveNfe.length());	
		String chave = chaveNfe.substring(0, chaveNfe.length() - 1);
		Integer calculoChaveNfe = modulo11(chave);
		
		if(dvChaveNfe.equals(calculoChaveNfe.toString())){
			return true;
		}else{
			return false; 
		}
	}
	
	
	public static int modulo11(String numero){
		char[] n = numero.toCharArray();
		int peso = 2;
		int soma = 0;
		for (int i = n.length-1; i >= 0; i--) {
			soma += Integer.valueOf(String.valueOf(n[i])) * peso++;
			if(peso == 10){
				peso = 2;
			}
		}
		int mod = soma % 11;
		int dv;
		
		if(mod == 0 || mod == 1){
			dv = 0;
		} else {
			dv = 11 - mod;
		}
		
		return dv;
	}
		
	/**
	 * Executa validação de um PIS. <br>
	 * <b>O PIS pode estar tanto no formato formatado quanto no formato somente
	 * numérico.</b>
	 * 
	 * @param nrPis
	 * @return boolean
	 */
	public static boolean validatePis(String nrPis){
		if(StringUtils.isEmpty(nrPis)){
			return false;
		}
		
		nrPis = clearNoNumbers(nrPis);
		
		if(nrPis.length() != 11){
			return false;
		}
		
		char[] charArray = nrPis.toCharArray();
		
		int digito = 0;  
		int coeficient = 2; 
		int soma = 0;        
		int dvEncontrado = 0;
		int dv = Character.getNumericValue(charArray[charArray.length - 1]);		
		
		for (int i = charArray.length - 2; i >= 0 ; i--){
			digito = Character.getNumericValue(charArray[i]);               
			soma += digito * coeficient;
			coeficient ++;
			
			if (coeficient > 9) { 
				coeficient = 2;			
			}
		}   
		
		dvEncontrado = 11 - soma % 11;
		
		if (dvEncontrado >= 10) {
			dvEncontrado = 0;        
		}
		
		if (dv == dvEncontrado) {
			return true;
		}
		
	    return false;
	}
}