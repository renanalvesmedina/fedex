package com.mercurio.lms.util;

class ValidateInscricaoEstadual {

	enum Uf {AC, AL, AM, AP, BA, CE, DF, ES, GO, MA, MG, MS, MT, PA, PB, PE, PI, PR, RJ, RN, RO, RR, RS, SC, SE, SP, TO};	

	protected static boolean validateInscricaoEstadual(String sgUf, String nrIns){

		if (sgUf == null || "".equals(sgUf.trim())) {
			return false;
		}

		if (nrIns == null || "".equals(nrIns.trim()))
			return false;
		else if ("ISENTO".equals(nrIns.toUpperCase())) return true;
		
		Uf uf = Uf.valueOf(sgUf);
		boolean retorno = true;

		switch (uf) {    
		case AC:
			nrIns = strZero(nrIns,13);
			retorno = validateAcre(sgUf, nrIns);
			break;      
		case AL:
			nrIns = strZero(nrIns,9);
			retorno = validateAlagoas(sgUf, nrIns);
			break;      
		case AM:
			nrIns = strZero(nrIns,9);
			retorno = validateAmazonas(sgUf, nrIns);
			break;      
		case AP:
			nrIns = strZero(nrIns,9);
			retorno = validateAmapa(sgUf, nrIns);
			break;
		case BA:
			nrIns = strZero(nrIns,8);
			retorno = validateBahia(sgUf, nrIns);
			break;
		case CE:
			nrIns = strZero(nrIns,9);
			retorno = validateCeara(sgUf, nrIns);
			break;
		case DF:
			nrIns = strZero(nrIns,13);
			retorno = validateDistritoFederal(sgUf, nrIns);
			break;
		case ES:
			nrIns = strZero(nrIns,9);
			retorno = validateEspiritoSanto(sgUf, nrIns);
			break;
		case GO:
			nrIns = strZero(nrIns,9);
			retorno = validateGoias(sgUf, nrIns);
			break;
		case MA:
			nrIns = strZero(nrIns,9);
			retorno = validateMaranhao(sgUf, nrIns);
			break;      
		case MG:
			nrIns = strZero(nrIns,13);
			retorno = validateMinasGerais(sgUf, nrIns);
			break;
		case MS:
			nrIns = strZero(nrIns,9);
			retorno = validateMatoGrossoSul(sgUf, nrIns);
			break;
		case MT:
			nrIns = strZero(nrIns,11);
			retorno = validateMatoGrosso(sgUf, nrIns);
			break;
		case PA:
			nrIns = strZero(nrIns,9);
			retorno = validatePara(sgUf, nrIns);
			break;
		case PB:
			nrIns = strZero(nrIns,9);
			retorno = validateParaiba(sgUf, nrIns);
			break;
		case PE:
			nrIns = strZero(nrIns,9);
			retorno = validatePernambuco(sgUf, nrIns);
			break;
		case PI:
			nrIns = strZero(nrIns,9);
			retorno = validatePiaui(sgUf, nrIns);
			break;
		case PR:
			nrIns = strZero(nrIns,10);
			retorno = validateParana(sgUf, nrIns);
			break;
		case RJ:
			nrIns = strZero(nrIns,8);
			retorno = validateRioJaneiro(sgUf, nrIns);
			break;
		case RN:
			nrIns = strZero(nrIns,9);
			retorno = validateRioGrandeNorte(sgUf, nrIns);
			break;
		case RO:
			retorno = validateRondonia(sgUf, nrIns);
			break;
		case RR:
			nrIns = strZero(nrIns,9);
			retorno = validateRoraima(sgUf, nrIns);
			break;
		case RS:
			nrIns = strZero(nrIns,10);
			retorno = validateRioGrandeSul(sgUf, nrIns);
			break;
		case SC:
			nrIns = strZero(nrIns,9);
			retorno = validateSantaCatarina(sgUf, nrIns);
			break;
		case SE:
			nrIns = strZero(nrIns,9);
			retorno = validateSergipe(sgUf, nrIns);
			break;
		case TO:
			retorno = validateTocatins(sgUf, nrIns);
			break;
		case SP:
			nrIns = strZero(nrIns,12);
			retorno = validateSaoPaulo(sgUf, nrIns);
			break;
		default:
			nrIns = strZero(nrIns,9);
		retorno = validatePacote(sgUf, nrIns);
		break;
		}
		return retorno;
	}

	private static boolean validateAcre(String sgUf, String nrIns){
		return validateDistritoFederal(sgUf, nrIns);
	}

	private static boolean validateAlagoas(String sgUf, String nrIns){
		if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0 && "24".equals(nrIns.substring(0,2)) ) {
			long valor = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2)) * 10;
			long mod11 = valor%11;
			if (mod11 == 10) {
				mod11 = 0;
			}
			if (valNum(nrIns,8,1) == mod11) {
				return true;
			}
		}
		return false;
	}

	private static boolean validateAmazonas(String sgUf, String nrIns){
		if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0) {
			long valor = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
			long mod11;
			if (valor<11) {
				mod11 = Long.valueOf(String.valueOf(11-valor).substring(0,1));
			} else {
				mod11 = valor%11;
				if (mod11 < 2) {
					mod11 = 0;
				} else {
					mod11 = 11-mod11;
				}			
			}
			if (valNum(nrIns,8,1) == mod11) {
				return true;
			}
		}
		return false;
	}

	private static boolean validateAmapa(String sgUf, String nrIns){
		if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0 && "03".equals(nrIns.substring(0,2))) {
			int valorSemDig = Integer.valueOf(nrIns.substring(0,8));
			int valorTemp1 = 0;
			int valorTemp2 = 0;
			if (valorSemDig >= 3000001 && valorSemDig <= 3017000) {
				valorTemp1 = 5;
			} else if (valorSemDig >= 3017001 && valorSemDig <= 3019022) {
				valorTemp1 = 9;
				valorTemp2 = 1;
			}
			long valor = valorTemp1 + (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
			long mod11 = 11-(valor%11);
			if (mod11 == 10) {
				mod11 = 0;
			} else if (mod11 == 11){
				mod11 = valorTemp2;
			}
			if (valNum(nrIns,8,1) == mod11) {
				return true;
			}
		}
		return false;
	}

	private static boolean validateBahia(String sgUf, String nrIns){
		if (nrIns.trim().length() == 8 && Long.valueOf(nrIns).longValue() > 0) {		
			if ( "0".equals(nrIns.substring(0,1)) || "1".equals(nrIns.substring(0,1)) || "2".equals(nrIns.substring(0,1)) || "3".equals(nrIns.substring(0,1)) || "4".equals(nrIns.substring(0,1)) || "5".equals(nrIns.substring(0,1)) || "8".equals(nrIns.substring(0,1))) {
				long valor = (valNum(nrIns,0,7) + 
						valNum(nrIns,1,6) +
						valNum(nrIns,2,5) +
						valNum(nrIns,3,4) +
						valNum(nrIns,4,3) +
						valNum(nrIns,5,2));
				long mod10 = 10-(valor%10);

				if (mod10 == 10){
					mod10 = 0;
				}

				long valor2 = (valNum(nrIns,0,8) + 
						valNum(nrIns,1,7) +
						valNum(nrIns,2,6) +
						valNum(nrIns,3,5) +
						valNum(nrIns,4,4) +
						valNum(nrIns,5,3) + (mod10*2));
				valor2 = 10-valor2%10;

				if (valor2 == 10){
					valor2 = 0;
				}

				String mod10String = String.valueOf(valor2) + String.valueOf(mod10);

				if (nrIns.substring(6,8).equals(mod10String)) {
					return true;
				}
			} else {
				long valor = (valNum(nrIns,0,7) + 
						valNum(nrIns,1,6) +
						valNum(nrIns,2,5) +
						valNum(nrIns,3,4) +
						valNum(nrIns,4,3) +
						valNum(nrIns,5,2));			
				long mod11 = valor%11;
				if (mod11 < 2) {
					mod11 = 0;
				} else {
					mod11 = 11 - mod11;
				}
				long valor2 = (valNum(nrIns,0,8) + 
						valNum(nrIns,1,7) +
						valNum(nrIns,2,6) +
						valNum(nrIns,3,5) +
						valNum(nrIns,4,4) +
						valNum(nrIns,5,3) + (mod11*2));
				long mod11Temp = valor2%11;
				if (mod11Temp < 2) {
					mod11Temp = 0;
				} else {
					mod11Temp = 11 - mod11Temp;
				}
				if (nrIns.substring(6,8).equals(String.valueOf(mod11Temp) + String.valueOf(mod11))) {
					return true;
				}
			}
		}else if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0) {
			if ( "0".equals(nrIns.substring(1,2)) || "1".equals(nrIns.substring(1,2)) || "2".equals(nrIns.substring(1,2)) || "3".equals(nrIns.substring(1,2)) || "4".equals(nrIns.substring(1,2)) || "5".equals(nrIns.substring(1,2)) || "8".equals(nrIns.substring(1,2))) {
				long valor =(valNum(nrIns,0,8) + 
						valNum(nrIns,1,7) +
						valNum(nrIns,2,6) +
						valNum(nrIns,3,5) +
						valNum(nrIns,4,4) +
						valNum(nrIns,5,3) +  
						valNum(nrIns,6,2));
				long mod10 = 10-(valor%10);
				if (mod10 == 10){
					mod10 = 0;
		}
				
				long valor2 = (valNum(nrIns,0,9) + 
						valNum(nrIns,1,8) +
						valNum(nrIns,2,7) +
						valNum(nrIns,3,6) +
						valNum(nrIns,4,5) +
						valNum(nrIns,5,4) +
						valNum(nrIns,6,3) + (mod10*2));
				valor2 = 10-valor2%10;
				if (valor2 == 10){
					valor2 = 0;
				}

				String mod10String = String.valueOf(valor2) + String.valueOf(mod10);

				if (nrIns.substring(7,9).equals(mod10String)) {
					return true;
				}
			}else{
				long valor = (valNum(nrIns,0,8) + 
						valNum(nrIns,1,7) +
						valNum(nrIns,2,6) +
						valNum(nrIns,3,5) +
						valNum(nrIns,4,4) +
						valNum(nrIns,5,3) + 
						valNum(nrIns,6,2));
				long mod11 = valor%11;
				if (mod11 < 2) {
					mod11 = 0;
				} else {
					mod11 = 11 - mod11;
				}
				
				long valor2 = (valNum(nrIns,0,9) + 
						valNum(nrIns,1,8) +
						valNum(nrIns,2,7) +
						valNum(nrIns,3,6) +
						valNum(nrIns,4,5) +
						valNum(nrIns,5,4) +
						valNum(nrIns,6,3) +(mod11*2));
				long mod11Temp = valor2%11;
				if (mod11Temp < 2) {
					mod11Temp = 0;
				} else {
					mod11Temp = 11 - mod11Temp;
				}

				String mod11String = String.valueOf(mod11Temp) + String.valueOf(mod11);

				if (nrIns.substring(7,9).equals(mod11String)) {
					return true;
				}
			}	
		}
		return false;
	}

	private static boolean validatePacote(String sgUf, String nrIns){
		if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0) {
			long valor = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
			long mod11 = valor%11;
			if (mod11<2) {
				mod11 = 0;
			} else {
				mod11 = 11-mod11;
			}
			if (valNum(nrIns,8,1) == mod11) {
				return true;
			}
		}
		return false;
	}


	private static boolean validateDistritoFederal(String sgUf, String nrIns){
		if (nrIns.trim().length() == 13 && Long.valueOf(nrIns).longValue() > 0) {		
			long valor = (valNum(nrIns,0,4) + 
					valNum(nrIns,1,3) +
					valNum(nrIns,2,2) +
					valNum(nrIns,3,9) +
					valNum(nrIns,4,8) +
					valNum(nrIns,5,7) +
					valNum(nrIns,6,6) +
					valNum(nrIns,7,5) +
					valNum(nrIns,8,4) +
					valNum(nrIns,9,3) +
					valNum(nrIns,10,2));
			long mod11 = valor%11;
			if (mod11 < 2) {
				mod11 = 0;
			} else {
				mod11 = 11 - mod11;
			}
			long valor2 = (valNum(nrIns,0,5) + 
					valNum(nrIns,1,4) +
					valNum(nrIns,2,3) +
					valNum(nrIns,3,2) +
					valNum(nrIns,4,9) +
					valNum(nrIns,5,8) +
					valNum(nrIns,6,7) +
					valNum(nrIns,7,6) +
					valNum(nrIns,8,5) +
					valNum(nrIns,9,4) +
					valNum(nrIns,10,3) + (mod11*2));
			long mod11Temp = valor2%11;
			if (mod11Temp < 2){
				mod11Temp = 0;
			} else {
				mod11Temp = 11-mod11Temp;
			}
			if (nrIns.substring(11,13).equals(String.valueOf(mod11) + String.valueOf(mod11Temp))) {
				return true;
			}
		}
		return false;
	}

	private static boolean validateCeara(String sgUf, String nrIns){
		if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0) {		
			long valor = (valNum(nrIns,0,9) +
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2) );
			long mod11 = valor%11;

			mod11 = 11 - mod11;

			if (mod11 == 10 || mod11 == 11) {
				mod11 = 0;
			}

			if (nrIns.substring(8,9).equals(String.valueOf(mod11))) {
				return true;
			}

		}
		return false;
	}

	private static boolean validateMaranhao(String sgUf, String nrIns){
		if (nrIns.substring(0,2).equals("12") && nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0) {		
			long valor = (valNum(nrIns,0,9) +
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2) );
			long mod11 = valor%11;

			if (mod11 == 0 || mod11 == 1) {
				mod11 = 0;
			} else {
				mod11 = 11 - mod11;
			}

			if (nrIns.substring(8,9).equals(String.valueOf(mod11))) {
				return true;
			}

		}
		return false;
	}

	private static boolean validateParaiba(String sgUf, String nrIns){
		if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0) {		
			long valor = (valNum(nrIns,0,9) +
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2) );
			long mod11 = valor%11;

			mod11 = 11 - mod11;

			if (mod11 == 10 || mod11 == 11) {
				mod11 = 0;
			}

			if (nrIns.substring(8,9).equals(String.valueOf(mod11))) {
				return true;
			}

		}
		return false;
	}

	private static boolean validateEspiritoSanto(String sgUf, String nrIns){
		if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0) {		
			long valor = (valNum(nrIns,0,9) +
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2) );
			long mod11 = valor%11;

			if (mod11 < 2) {
				mod11 = 0;
			} else {
				mod11 = 11 - mod11;
			}

			if (nrIns.substring(8,9).equals(String.valueOf(mod11))) {
				return true;
			}

		}
		return false;
	}

	private static boolean validateSergipe(String sgUf, String nrIns){
		if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0) {		
			long valor = (valNum(nrIns,0,9) +
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2) );
			long mod11 = valor%11;

			mod11 = 11 - mod11;

			if (mod11 == 10 || mod11 == 11) {
				mod11 = 0;
			}

			if (nrIns.substring(8,9).equals(String.valueOf(mod11))) {
				return true;
			}			
			
		}
		return false;
	}

	private static boolean validateGoias(String sgUf, String nrIns){
		if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0) {
			long valor = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
			long mod11 = valor%11;
			if (mod11 != 1) {
				if (mod11 < 2){
					mod11 = 0;
				} else {
					mod11 = 11-mod11;
				}
				if (valNum(nrIns,8,1) == mod11) {
					return true;
				}
			} else {
				if (valNum(nrIns,8,1) == 0 || valNum(nrIns,8,1) == 1) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean validateMinasGerais(String sgUf, String nrIns){
		if (nrIns.trim().length() == 13 && Long.valueOf(nrIns).longValue() > 0) {                       
			long valorTemp = 0;

			String valor = (strZero(String.valueOf(valNum(nrIns,0,1)),2) + 
					strZero(String.valueOf(valNum(nrIns,1,2)),2) +
					strZero(String.valueOf(valNum(nrIns,2,1)),2) +
					strZero(String.valueOf(valNum(nrIns,3,1)),2) +
					strZero(String.valueOf(valNum(nrIns,4,2)),2) +
					strZero(String.valueOf(valNum(nrIns,5,1)),2) +
					strZero(String.valueOf(valNum(nrIns,6,2)),2) +
					strZero(String.valueOf(valNum(nrIns,7,1)),2) +
					strZero(String.valueOf(valNum(nrIns,8,2)),2) +
					strZero(String.valueOf(valNum(nrIns,9,1)),2) +
					strZero(String.valueOf(valNum(nrIns,10,2)),2));

			for (int i = 0; i < 22; i++) {
				valorTemp = valorTemp + Integer.valueOf(valor.substring(i,i+1));
			}
			long mod10 = 10-valorTemp%10;

			if (mod10 == 10){
				mod10 = 0;
			}

			long valor2 = (valNum(nrIns,0,3) + 
					valNum(nrIns,1,2) +
					valNum(nrIns,2,11) +
					valNum(nrIns,3,10) +
					valNum(nrIns,4,9) +
					valNum(nrIns,5,8) +
					valNum(nrIns,6,7) +
					valNum(nrIns,7,6) +
					valNum(nrIns,8,5) +
					valNum(nrIns,9,4) +
					valNum(nrIns,10,3) + (mod10*2));
			long mod11 = valor2%11;
			if (mod11 < 2){
				mod11 = 0;
			} else {
				mod11 = 11-mod11;
			}
			if (nrIns.substring(11,13).equals(String.valueOf(mod10) + String.valueOf(mod11))) {
				return true;
			}
		}
		return false;
	}

	private static boolean validateMatoGrossoSul(String sgUf, String nrIns){
		if ("28".equals(nrIns.substring(0,2))) {
			return validatePacote(sgUf, nrIns);
		}
		return false;
	}

	private static boolean validateMatoGrosso(String sgUf, String nrIns){
		if (nrIns.trim().length() == 11 && Long.valueOf(nrIns).longValue() > 0) {
			long valor = (valNum(nrIns,0,3) + 
					valNum(nrIns,1,2) +
					valNum(nrIns,2,9) +
					valNum(nrIns,3,8) +
					valNum(nrIns,4,7) +
					valNum(nrIns,5,6) +
					valNum(nrIns,6,5) +
					valNum(nrIns,7,4) +
					valNum(nrIns,8,3) +
					valNum(nrIns,9,2));
			long mod11 = valor%11;
			if (mod11 < 2) {
				mod11 = 0;
			} else {
				mod11 = 11 - mod11;
			}
			if (valNum(nrIns,10,1) == mod11) {
				return true;
			}
		}
		return false;
	}

	private static boolean validatePara(String sgUf, String nrIns){
		if ("15".equals(nrIns.substring(0,2))) {
			return validatePacote(sgUf, nrIns);
		}
		return false;
	}

	private static boolean validatePernambuco(String sgUf, String nrIns){
		if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0) {                                             
            long valor = (
                valNum(nrIns,0,8) +
                valNum(nrIns,1,7) +
                valNum(nrIns,2,6) +
                valNum(nrIns,3,5) +
                valNum(nrIns,4,4) +
                valNum(nrIns,5,3) +
                valNum(nrIns,6,2) );
        
			long mod11 = valor%11;
            long digit = (mod11 == 0 || mod11 == 1)?0:11 - mod11;

            if(digit != valNum(nrIns,7,1)){
                return false;
			}

            valor =  (
                valNum(nrIns,0,9) +
                valNum(nrIns,1,8) +
                valNum(nrIns,2,7) +
                valNum(nrIns,3,6) +
                valNum(nrIns,4,5) +
                valNum(nrIns,5,4) +
                valNum(nrIns,6,3) + 
                		 digit*2);

            mod11 = valor%11;                         
            digit = (mod11 == 0 || mod11 == 1)?0:11 - mod11;
            
            if(digit == valNum(nrIns,8,1)){
				return true;
			}
		}
		return false;
	}

	private static boolean validatePiaui(String sgUf, String nrIns){
		if ("19".equals(nrIns.substring(0,2))) {
			return validatePacote(sgUf, nrIns);
		}
		return false;
	}

	private static boolean validateParana(String sgUf, String nrIns){
		if (nrIns.trim().length() == 10 && Long.valueOf(nrIns).longValue() > 0) {		
			long valor = (valNum(nrIns,0,3) + 
					valNum(nrIns,1,2) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
			long mod11 = valor%11;
			if (mod11 < 2) {
				mod11 = 0;
			} else {
				mod11 = 11 - mod11;
			}

			long valor2 = (valNum(nrIns,0,4) + 
					valNum(nrIns,1,3) +
					valNum(nrIns,2,2) +
					valNum(nrIns,3,7) +
					valNum(nrIns,4,6) +
					valNum(nrIns,5,5) +
					valNum(nrIns,6,4) +
					valNum(nrIns,7,3) + (mod11*2));
			long mod11Temp = valor2%11;
			if (mod11Temp < 2){
				mod11Temp = 0;
			} else {
				mod11Temp = 11-mod11Temp;
			}
			if (nrIns.substring(8,10).equals(String.valueOf(mod11) + String.valueOf(mod11Temp))) {
				return true;
			}
		}
		return false;
	}

	private static boolean validateRioJaneiro(String sgUf, String nrIns){
		if (nrIns.trim().length() == 8 && Long.valueOf(nrIns).longValue() > 0) {
			long valor = (valNum(nrIns,0,2) + 
					valNum(nrIns,1,7) +
					valNum(nrIns,2,6) +
					valNum(nrIns,3,5) +
					valNum(nrIns,4,4) +
					valNum(nrIns,5,3) +
					valNum(nrIns,6,2));
			long mod11 = valor%11;
			if (mod11 < 2) {
				mod11 = 0;
			} else {
				mod11 = 11 - mod11;
			}
			if (valNum(nrIns,7,1) == mod11) {
				return true;
			}
		}
		return false;
	}

	private static boolean validateRioGrandeNorte(String sgUf, String nrIns){
		if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0) {
			long valor = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
			long mod11 = (valor * 10)%11;
			if (mod11 == 10) {
				mod11 = 0;
			}
			if (valNum(nrIns,8,1) == mod11) {
				return true;
			}
		}
		return false;
	}

	private static boolean validateRondonia(String sgUf, String nrIns){
		//Regra abaixo e para Validar IE anteriores a 01/08/2000
		if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0) {
			nrIns = nrIns.substring(3, 9); // Removo os tres digitos da cidade
			String digito = nrIns.substring(5); // Guardo Digito Verificador 
			nrIns = nrIns.substring(0, 5); // Removo digito verificador

			long valor = (valNum(nrIns,0,6) + 
					valNum(nrIns,1,5) +
					valNum(nrIns,2,4) +
					valNum(nrIns,3,3) +
					valNum(nrIns,4,2));

			long mod11 = valor%11;

			mod11 = 11 - mod11;

			if (mod11 > 9){
				mod11 = 10 - mod11;
			}

			if (digito.equals(String.valueOf(mod11))) {
				return true;
			}
			return false;
		}

		// Regra abaixo para validar IEs posteriores a 01/08/2000
		nrIns = strZero(nrIns,14);		
		if (nrIns.trim().length() == 14 && Long.valueOf(nrIns).longValue() > 0) {
			long valor = (valNum(nrIns,0,6) + 
					valNum(nrIns,1,5) +
					valNum(nrIns,2,4) +
					valNum(nrIns,3,3) +
					valNum(nrIns,4,2) +
					valNum(nrIns,5,9) +
					valNum(nrIns,6,8) +
					valNum(nrIns,7,7) +
					valNum(nrIns,8,6) +
					valNum(nrIns,9,5) +
					valNum(nrIns,10,4) +
					valNum(nrIns,11,3) +
					valNum(nrIns,12,2));

			long mod11 = valor%11;
	
			mod11 = 11 - mod11;
			if (mod11 > 9){
				mod11 = mod11 - 10;
			}
			

			if (valNum(nrIns,13,1) == mod11) {
				return true;
			}
		}
		return false;
	}

	private static boolean validateRoraima(String sgUf, String nrIns){
		if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0 && "24".equals(nrIns.substring(0,2))) {
			long valor = (valNum(nrIns,0,1) + 
					valNum(nrIns,1,2) +
					valNum(nrIns,2,3) +
					valNum(nrIns,3,4) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,6) +
					valNum(nrIns,6,7) +
					valNum(nrIns,7,8));
			long mod9 = valor%9;
			if (valNum(nrIns,8,1) == mod9) {
				return true;
			}
		}
		return false;
	}

	private static boolean validateRioGrandeSul(String sgUf, String nrIns){
		if (nrIns.trim().length() == 10 && Long.valueOf(nrIns).longValue() > 0) {
			long valor = (valNum(nrIns,0,2) + 
					valNum(nrIns,1,9) +
					valNum(nrIns,2,8) +
					valNum(nrIns,3,7) +
					valNum(nrIns,4,6) +
					valNum(nrIns,5,5) +
					valNum(nrIns,6,4) +
					valNum(nrIns,7,3) +
					valNum(nrIns,8,2));
			long mod11 = valor%11;
			if (mod11 < 2) {
				mod11 = 0;
			} else {
				mod11 = 11 - mod11;
			}
			if (valNum(nrIns,9,1) == mod11) {
				return true;
			}
		}
		return false;
	}

	private static boolean validateSantaCatarina(String sgUf, String nrIns){
		if (nrIns.trim().length() == 9 && Long.valueOf(nrIns).longValue() > 0) {
			long valor = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
			long mod11 = (valor * 10)%11;
			if (mod11 == 10) {
				mod11 = 0;
			}
			if (valNum(nrIns,8,1) == mod11) {
				return true;
			}
		}
		return false;
	}

	private static boolean validateTocatins(String sgUf, String nrIns){
		if (
				(
						(nrIns.trim().length() == 11 && 
								("01".equals(nrIns.substring(2,4)) || 
										"02".equals(nrIns.substring(2,4)) || 
										"03".equals(nrIns.substring(2,4)) || 
										"99".equals(nrIns.substring(2,4))
								) 
			 )// Calculo da Inscricao Estadual Antiga (Valida ate dezembro de 2.003) 
						||
			  (nrIns.trim().length() == 9) // Calculo da Inscricao Estadual Nova (Em vigor desde junho de 2.002) 
				)
				&& 
				(Long.valueOf(nrIns).longValue() > 0)
		)
		{

			if (nrIns.trim().length() == 11) { // Calculo antigo, ate 2003
				long valor = (valNum(nrIns,0,9) + 
						valNum(nrIns,1,8) +
						valNum(nrIns,4,7) +
						valNum(nrIns,5,6) +
						valNum(nrIns,6,5) +
						valNum(nrIns,7,4) +
						valNum(nrIns,8,3) +
						valNum(nrIns,9,2));

				long mod11 = valor%11;

				if (mod11 < 2) {
					mod11 = 0;
				} else {
					mod11 = 11 - mod11;
				}

				if (valNum(nrIns,10,1) == mod11) {
					return true;
				}
			} else if (nrIns.trim().length() == 9) { // Calculo novo, a partir de 2002
				long valor = (valNum(nrIns,0,9) + 
						valNum(nrIns,1,8) +
						valNum(nrIns,2,7) +
						valNum(nrIns,3,6) +
						valNum(nrIns,4,5) +
						valNum(nrIns,5,4) +
						valNum(nrIns,6,3) +
						valNum(nrIns,7,2));

				long mod11 = valor%11;

				if (mod11 < 2) {
					mod11 = 0;
				} else {
					mod11 = 11 - mod11;
				}

				if (valNum(nrIns,8,1) == mod11) {
					return true;
				}
			}			
		}
		return false;
	}

	private static boolean validateSaoPaulo(String sgUf, String nrIns){
		if (nrIns.trim().length() == 12 && Long.valueOf(nrIns).longValue()  > 0) {		
			long valor = (valNum(nrIns,0,1) + 
					valNum(nrIns,1,3) +
					valNum(nrIns,2,4) +
					valNum(nrIns,3,5) +
					valNum(nrIns,4,6) +
					valNum(nrIns,5,7) +
					valNum(nrIns,6,8) +
					valNum(nrIns,7,10));
			long mod11 = valor%11;
			if (mod11 == 10) {
				mod11 = 0;
			}
			long valor2 = (valNum(nrIns,0,3) + 
					valNum(nrIns,1,2) +
					valNum(nrIns,2,10) +
					valNum(nrIns,3,9) +
					valNum(nrIns,4,8) +
					valNum(nrIns,5,7) +
					valNum(nrIns,6,6) +
					valNum(nrIns,7,5) + (mod11*4) +
					valNum(nrIns,9,3) +
					valNum(nrIns,10,2));
			long mod11Temp = valor2%11;
			if (mod11Temp == 10) {
				mod11Temp = 0;
			}
			if (Long.valueOf(nrIns.substring(8,9)).longValue() == mod11 && Long.valueOf(nrIns.substring(11,12)).longValue() == mod11Temp) {
				return true;
			}
		}
		return false;
	}

	private static long valNum(String nrIns, int pos, int multi) {
		return Long.valueOf(nrIns.substring(pos, pos + 1)).longValue() * multi;
	}

	private static String strZero(String valor, int length) {
		String retorno = valor;
		if (retorno.length() < length){
			int count = length - retorno.length();
			for (int i = 0; i < count; i++) {
				retorno = "0" + retorno;
			}
		}
		return retorno;
	}

}
