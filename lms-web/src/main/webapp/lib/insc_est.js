function validateInscricaoEstadual(sgUf,nrIns){
      var retorno = true;
      switch(sgUf) {    
           case "AC":
                  nrIns = strZero(nrIns,13);
                  retorno = validateAcre(sgUf, nrIns);
            break; 	     
            case "AL":
                  nrIns = strZero(nrIns,9);
                  retorno = validateAlagoas(sgUf, nrIns);
            break;      
            case "AM":
                  nrIns = strZero(nrIns,9);
                  retorno = validateAmazonas(sgUf, nrIns);
            break;      
            case "AP":
                  nrIns = strZero(nrIns,9);
                  retorno = validateAmapa(sgUf, nrIns);
            break;
            case "BA":
                  nrIns = strZero(nrIns,8);
                  retorno = validateBahia(sgUf, nrIns);
            break;
            case "DF":
                  nrIns = strZero(nrIns,13);
                  retorno = validateDistritoFederal(sgUf, nrIns);
            break;
            case "GO":
                  nrIns = strZero(nrIns,9);
                  retorno = validateGoias(sgUf, nrIns);
            break;
            case "MG":
                  nrIns = strZero(nrIns,13);
                  retorno = validateMinasGerais(sgUf, nrIns);
            break;
            case "MS":
                  nrIns = strZero(nrIns,9);
                  retorno = validateMatoGrossoSul(sgUf, nrIns);
            break;
            case "MT":
                  nrIns = strZero(nrIns,11);
                  retorno = validateMatoGrosso(sgUf, nrIns);
            break;
            case "PA":
                  nrIns = strZero(nrIns,9);
                  retorno = validatePara(sgUf, nrIns);
            break;
            case "PE":
                  nrIns = strZero(nrIns,9);
                  retorno = validatePernambuco(sgUf, nrIns);
            break;
            case "PI":
                  nrIns = strZero(nrIns,9);
                  retorno = validatePiaui(sgUf, nrIns);
            break;
            case "PR":
                  nrIns = strZero(nrIns,10);
                  retorno = validateParana(sgUf, nrIns);
            break;
            case "RJ":
                  nrIns = strZero(nrIns,8);
                  retorno = validateRioJaneiro(sgUf, nrIns);
            break;
            case "RN":
                  nrIns = strZero(nrIns,9);
                  retorno = validateRioGrandeNorte(sgUf, nrIns);
            break;
            case "RO":
                  nrIns = strZero(nrIns,9);
                  retorno = validateRondonia(sgUf, nrIns);
            break;
            case "RR":
                  nrIns = strZero(nrIns,9);
                  retorno = validateRoraima(sgUf, nrIns);
            break;
            case "RS":
                  nrIns = strZero(nrIns,10);
                  retorno = validateRioGrandeSul(sgUf, nrIns);
            break;
            case "SC":
                  nrIns = strZero(nrIns,9);
                  retorno = validateSantaCatarina(sgUf, nrIns);
            break;
            case "TO":
                  nrIns = strZero(nrIns,9);
                  retorno = validateTocatins(sgUf, nrIns);
            break;
            case "SP":
                  nrIns = strZero(nrIns,12);
                  retorno = validateSaoPaulo(sgUf, nrIns);
            break;
            default:
                  nrIns = strZero(nrIns,9);
                  retorno = validatePacote(sgUf, nrIns);
            break;
      }
      if (retorno == false) {
            alert("Número de inscrição estadual inválido.");
      }
      return retorno;
}

function validateAcre(sgUf, nrIns){
	return validateDistritoFederal(sgUf, nrIns);
}

function validateAlagoas(sgUf, nrIns){
	if (trim(nrIns).length == 9 && Number(nrIns) > 0 && nrIns.substr(0,2) == "24") {
		var valor = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2)) * 10;
		var mod11 = valor%11;
		if (mod11==10) {
			mod11 = 0;
		}
		if (valNum(nrIns,8,1) == mod11) {
			return true;
		}
	}
	return false;
}

function validateAmazonas(sgUf, nrIns){
	if (trim(nrIns).length == 9 && Number(nrIns) > 0) {
		var valor = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
		if (valor<11) {
			mod11 = String(11-valor).substr(0,1);
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

function validateAmapa(sgUf, nrIns){
	if (trim(nrIns).length == 9 && Number(nrIns) > 0 && nrIns.substr(0,2) == "03") {		
		var valorSemDig = Number(nrIns.substr(0,8));
		var valorTemp1 = 0;
		var valorTemp2 = 0;
		if (valorSemDig >= 3000001 && valorSemDig <= 3017000) {
			valorTemp1 = 5;
		} else if (valorSemDig >= 3017001 && valorSemDig <= 3019022) {
			valorTemp1 = 9;
			valorTemp2 = 1;
		}
		var valor = valorTemp1 + (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
		mod11 = 11-(valor%11);
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

function validateBahia(sgUf, nrIns){
	if (trim(nrIns).length == 8 && Number(nrIns) > 0) {	
		var valorTemp1 = 0;
		var valorTemp2 = 0;
		if (nrIns.substr(0,1) == "0" || nrIns.substr(0,1) == "1" || nrIns.substr(0,1) == "2" || nrIns.substr(0,1) == "3" || nrIns.substr(0,1) == "4" || nrIns.substr(0,1) == "5" || nrIns.substr(0,1) == "8") {
			var valor = (valNum(nrIns,0,7) + 
						valNum(nrIns,1,6) +
						valNum(nrIns,2,5) +
						valNum(nrIns,3,4) +
						valNum(nrIns,4,3) +
						valNum(nrIns,5,2));
			mod10 = 10-(valor%10);

			if (mod10 == 10){
				mod10 = 0;
			}

			var valor2 = (valNum(nrIns,0,8) + 
						valNum(nrIns,1,7) +
						valNum(nrIns,2,6) +
						valNum(nrIns,3,5) +
						valNum(nrIns,4,4) +
						valNum(nrIns,5,3) + (mod10*2));
			valor2 = 10-valor2%10;

			if (valor2 == 10){
				valor2 = 0;
			}
			mod10 = String(valor2) + String(mod10);
			
			if (Number(nrIns.substr(6,2)) == mod10) {
				return true;
			}
		} else {
			var valor = (valNum(nrIns,0,7) + 
						valNum(nrIns,1,6) +
						valNum(nrIns,2,5) +
						valNum(nrIns,3,4) +
						valNum(nrIns,4,3) +
						valNum(nrIns,5,2));			
			mod11 = valor%11;
			if (mod11 < 2) {
				mod11 = 0;
			} else {
				mod11 = 11 - mod11;
			}
			var valor2 = (valNum(nrIns,0,8) + 
						valNum(nrIns,1,7) +
						valNum(nrIns,2,6) +
						valNum(nrIns,3,5) +
						valNum(nrIns,4,4) +
						valNum(nrIns,5,3) + (mod11*2));
			mod11Temp = valor2%11;
			if (mod11Temp < 2) {
				mod11Temp = 0;
			} else {
				mod11Temp = 11 - mod11Temp;
			}
			mod11Temp = String(mod11Temp) + String(mod11);
			if (Number(nrIns.substr(6,2)) == mod11Temp) {
				return true;
			}
		}
	}else if (trim(nrIns).length == 9 && Number(nrIns) > 0) {	
		if (nrIns.substr(1,1) == "0" || nrIns.substr(1,1) == "1" || nrIns.substr(1,1) == "2" || nrIns.substr(1,1) == "3" || nrIns.substr(1,1) == "4" || nrIns.substr(1,1) == "5" || nrIns.substr(1,1) == "8") {
			var valor = (valNum(nrIns,0,8) + 
					valNum(nrIns,1,7) +
					valNum(nrIns,2,6) +
					valNum(nrIns,3,5) +
					valNum(nrIns,4,4) +
					valNum(nrIns,5,3) +
					valNum(nrIns,6,2));
			var mod10 = 10-(valor%10);
			if (mod10 == 10){
				mod10 = 0;
			}

			var valor2 = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +(mod10*2));
			valor2 = 10-valor2%10;
			if (valor2 == 10){
				valor2 = 0;
			}

			mod10 = String(valor2) + String(mod10);
			if (Number(nrIns.substr(7,2)) == mod10) {
				return true;
			}
		}else{
			var valor = (valNum(nrIns,0,8) + 
					valNum(nrIns,1,7) +
					valNum(nrIns,2,6) +
					valNum(nrIns,3,5) +
					valNum(nrIns,4,4) +
					valNum(nrIns,5,3) + 
					valNum(nrIns,6,2));
			var mod11 = valor%11;
			if (mod11 < 2) {
				mod11 = 0;
			} else {
				mod11 = 11 - mod11;
			}

			var valor2 = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +(mod11*2));
			var mod11Temp = valor2%11;
			if (mod11Temp < 2) {
				mod11Temp = 0;
			} else {
				mod11Temp = 11 - mod11Temp;
			}
			
			var mod11String = String(mod11Temp) + String(mod11);
			if (Number(nrIns.substr(7,2)) == mod11String) {
				return true;
			}
		}	
	}	
	return false;
}

function validatePacote(sgUf, nrIns){
	if (trim(nrIns).length == 9 && Number(nrIns) > 0) {
		var valor = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
		var mod11 = valor%11;
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


function validateDistritoFederal(sgUf, nrIns){
	if (trim(nrIns).length == 13 && Number(nrIns) > 0) {		
		var valor = (valNum(nrIns,0,4) + 
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
		mod11 = valor%11;
		if (mod11 < 2) {
			mod11 = 0;
		} else {
			mod11 = 11 - mod11;
		}
		var valor2 = (valNum(nrIns,0,5) + 
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
		mod11Temp = valor2%11;
		if (mod11Temp < 2){
			mod11Temp = 0;
		} else {
			mod11Temp = 11-mod11Temp;
		}
		mod11Temp = String(mod11)+String(mod11Temp);
		if (Number(nrIns.substr(11,2)) == mod11Temp) {
			return true;
		}
	}
	return false;
}

function validateGoias(sgUf, nrIns){
	if (trim(nrIns).length == 9 && Number(nrIns) > 0) {
		var valor = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
		var mod11 = valor%11;
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
			if (valNum(nrIns,8,1) == "0" || valNum(nrIns,8,1) == "1") {
				return true;
			}
		}
	}
	return false;
}

function validateMinasGerais(sgUf, nrIns){
	if (trim(nrIns).length == 13 && Number(nrIns) > 0) {                       
		var valorTemp = 0;
 
		var valor = (strZero(valNum(nrIns,0,1),2) + 
						strZero(valNum(nrIns,1,2),2) +
						strZero(valNum(nrIns,2,1),2) +
						strZero(valNum(nrIns,3,1),2) +
						strZero(valNum(nrIns,4,2),2) +
						strZero(valNum(nrIns,5,1),2) +
						strZero(valNum(nrIns,6,2),2) +
						strZero(valNum(nrIns,7,1),2) +
						strZero(valNum(nrIns,8,2),2) +
						strZero(valNum(nrIns,9,1),2) +
						strZero(valNum(nrIns,10,2),2));
		
		for (i=0; i<22; i++) {
			valorTemp = Number(valorTemp)+Number(valor.substr(i,1));
		}
		mod10 = 10-valorTemp%10;
		
		if (mod10 == 10){
			mod10 = 0;
		}
 
		var valor2 = (valNum(nrIns,0,3) + 
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
		mod11 = valor2%11;
		if (mod11 < 2){
			mod11 = 0;
		} else {
			mod11 = 11-mod11;
		}
		mod11 = String(mod10)+String(mod11);
		if (Number(nrIns.substr(11,2)) == mod11) {
			return true;
		}
      }
      return false;
}
 
function validateMatoGrossoSul(sgUf, nrIns){
	if (nrIns.substr(0,2) == "28") {
		return validatePacote(sgUf, nrIns);
	}
	return false;
}

function validateMatoGrosso(sgUf, nrIns){
	if (trim(nrIns).length == 11 && Number(nrIns) > 0) {
		var valor = (valNum(nrIns,0,3) + 
					valNum(nrIns,1,2) +
					valNum(nrIns,2,9) +
					valNum(nrIns,3,8) +
					valNum(nrIns,4,7) +
					valNum(nrIns,5,6) +
					valNum(nrIns,6,5) +
					valNum(nrIns,7,4) +
					valNum(nrIns,8,3) +
					valNum(nrIns,9,2));
		var mod11 = valor%11;
		if (mod11<2) {
			mod11 = 0;
		} else {
			mod11 = 11-mod11;
		}
		if (valNum(nrIns,10,1) == mod11) {
			return true;
		}
	}
	return false;
}

function validatePara(sgUf, nrIns){
	if (nrIns.substr(0,2) == "15") {
		return validatePacote(sgUf, nrIns);
	}
	return false;
}

function validatePernambuco(sgUf, nrIns){
		
	if (nrIns.length == 9 && Number(nrIns) > 0) {                                             
            var valor = (
                valNum(nrIns,0,8) +
                valNum(nrIns,1,7) +
                valNum(nrIns,2,6) +
                valNum(nrIns,3,5) +
                valNum(nrIns,4,4) +
                valNum(nrIns,5,3) +
                valNum(nrIns,6,2) );
        
            var mod11 = valor%11;                          
            var digit = (mod11 == 0 || mod11 == 1)?0:11 - mod11;

            if(digit != nrIns.substr(7,1)){
                return false;
            }
                                          
            valor =  (
                valNum(nrIns,0,9) +
                valNum(nrIns,1,8) +
                valNum(nrIns,2,7) +
                valNum(nrIns,3,6) +
                valNum(nrIns,4,5) +
                valNum(nrIns,5,4) +
                valNum(nrIns,6,3) + (Number(digit)*2));

            mod11 = valor%11;                         
            digit = (mod11 == 0 || mod11 == 1)?0:11 - mod11;
            
            if(digit == nrIns.substr(8,1)){
                return true;
            }
    }
	return false;
	
}

function validatePiaui(sgUf, nrIns){
	if (nrIns.substr(0,2) == "19") {
		return validatePacote(sgUf, nrIns);
	}
	return false;
}

function validateParana(sgUf, nrIns){
	if (trim(nrIns).length == 10 && Number(nrIns) > 0) {		
		var valor = (valNum(nrIns,0,3) + 
					valNum(nrIns,1,2) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
		mod11 = valor%11;
		if (mod11 < 2) {
			mod11 = 0;
		} else {
			mod11 = 11 - mod11;
		}
		var valor2 = (valNum(nrIns,0,4) + 
					valNum(nrIns,1,3) +
					valNum(nrIns,2,2) +
					valNum(nrIns,3,7) +
					valNum(nrIns,4,6) +
					valNum(nrIns,5,5) +
					valNum(nrIns,6,4) +
					valNum(nrIns,7,3) + (mod11*2));
		mod11Temp = valor2%11;
		if (mod11Temp < 2){
			mod11Temp = 0;
		} else {
			mod11Temp = 11-mod11Temp;
		}
		mod11Temp = String(mod11)+String(mod11Temp);
		if (Number(nrIns.substr(8,2)) == mod11Temp) {
			return true;
		}
	}
	return false;
}

function validateRioJaneiro(sgUf, nrIns){
	if (trim(nrIns).length == 8 && Number(nrIns) > 0) {
		var valor = (valNum(nrIns,0,2) + 
					valNum(nrIns,1,7) +
					valNum(nrIns,2,6) +
					valNum(nrIns,3,5) +
					valNum(nrIns,4,4) +
					valNum(nrIns,5,3) +
					valNum(nrIns,6,2));
		var mod11 = valor%11;
		if (mod11<2) {
			mod11 = 0;
		} else {
			mod11 = 11-mod11;
		}
		if (valNum(nrIns,7,1) == mod11) {
			return true;
		}
	}
	return false;
}

function validateRioGrandeNorte(sgUf, nrIns){
	if (trim(nrIns).length == 9 && Number(nrIns) > 0) {
		var valor = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
		var mod11 = (valor * 10)%11;
		if (mod11==10) {
			mod11 = 0;
		}
		if (valNum(nrIns,8,1) == mod11) {
			return true;
		}
	}
	return false;
}

function validateRondonia(sgUf, nrIns){
	if (trim(nrIns).length == 14 && Number(nrIns) > 0) {
		var valor = (valNum(nrIns,0,6) + 
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
					 valNum(nrIns,12,2) );		
		var mod11 = valor%11;
		if (mod11<2) {
			mod11 = 1-mod11;
		} else {
			mod11 = 11-mod11;
		}
		if (valNum(nrIns,13,1) == mod11) {
			return true;
		}
	}
	return false;
}

function validateRoraima(sgUf, nrIns){
	if (trim(nrIns).length == 9 && Number(nrIns) > 0 && nrIns.substr(0,2) == "24") {
		var valor = (valNum(nrIns,0,1) + 
					valNum(nrIns,1,2) +
					valNum(nrIns,2,3) +
					valNum(nrIns,3,4) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,6) +
					valNum(nrIns,6,7) +
					valNum(nrIns,7,8));
		var mod9 = valor%9;
		if (valNum(nrIns,8,1) == mod9) {
			return true;
		}
	}
	return false;
}

function validateRioGrandeSul(sgUf, nrIns){
	if (trim(nrIns).length == 10 && Number(nrIns) > 0) {
		var valor = (valNum(nrIns,0,2) + 
					valNum(nrIns,1,9) +
					valNum(nrIns,2,8) +
					valNum(nrIns,3,7) +
					valNum(nrIns,4,6) +
					valNum(nrIns,5,5) +
					valNum(nrIns,6,4) +
					valNum(nrIns,7,3) +
					valNum(nrIns,8,2));
		var mod11 = valor%11;
		if (mod11<2) {
			mod11 = 0;
		} else {
			mod11 = 11-mod11;
		}
		if (valNum(nrIns,9,1) == mod11) {
			return true;
		}
	}
	return false;
}

function validateSantaCatarina(sgUf, nrIns){
	if (trim(nrIns).length == 9 && Number(nrIns) > 0) {
		var valor = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
		var mod11 = (valor * 10)%11;
		if (mod11==10) {
			mod11 = 0;
		}
		if (valNum(nrIns,8,1) == mod11) {
			return true;
		}
	}
	return false;
}

function validateTocatins(sgUf, nrIns){
	if (trim(nrIns).length == 9 && Number(nrIns) > 0) {
		var valor = (valNum(nrIns,0,9) + 
					valNum(nrIns,1,8) +
					valNum(nrIns,2,7) +
					valNum(nrIns,3,6) +
					valNum(nrIns,4,5) +
					valNum(nrIns,5,4) +
					valNum(nrIns,6,3) +
					valNum(nrIns,7,2));
		var mod11 = (valor * 10)%11;
		if (mod11==10) {
			mod11 = 0;
		}
		if (valNum(nrIns,8,1) == mod11) {
			return true;
		}
	}
	return false;
}

function validateSaoPaulo(sgUf, nrIns){
	if (trim(nrIns).length == 12 && Number(nrIns) > 0) {		
		var valor = (valNum(nrIns,0,1) + 
					valNum(nrIns,1,3) +
					valNum(nrIns,2,4) +
					valNum(nrIns,3,5) +
					valNum(nrIns,4,6) +
					valNum(nrIns,5,7) +
					valNum(nrIns,6,8) +
					valNum(nrIns,7,10));
		mod11 = valor%11;
		if (mod11 == 10) {
			mod11 = 0;
		}
		var valor2 = (valNum(nrIns,0,3) + 
					valNum(nrIns,1,2) +
					valNum(nrIns,2,10) +
					valNum(nrIns,3,9) +
					valNum(nrIns,4,8) +
					valNum(nrIns,5,7) +
					valNum(nrIns,6,6) +
					valNum(nrIns,7,5) + (mod11*4) +
					valNum(nrIns,9,3) +
					valNum(nrIns,10,2));
		mod11Temp = valor2%11;
		if (mod11Temp == 10) {
			mod11Temp = 0;
		}
		if (Number(nrIns.substr(8,1)) == mod11 && Number(nrIns.substr(11,1)) == mod11Temp) {
			return true;
		}
	}
	return false;
}

function valNum(nrIns, pos, multi) {
	return Number(nrIns.substr(pos,1))*multi;
}

function strZero(valor,length) {
	var retorno = String(valor);
	if (retorno.length < length){
		count = length - retorno.length;
		for (i=0; i < count; i++) {
			retorno = "0"+retorno;
		}
	}
	return retorno;
}
