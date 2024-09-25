/**
 *	Fun??o que valida o n?mero de PIS informado
 *  Par?metro passado ? o pr?prio objeto
 */
 
function isPis(obj) {
	if (validatePis(obj)) return true 
	else { 
		alert(getMessage(erPis));
		return false;
	}

}
 
function validatePis(obj){

	strValor = getElementValue(obj);

	if (strValor == "") return true;
	
	strValor = cleanString(strValor,'numeros');
	
	if (strValor.length != 11) return false;
	
	var i;
	var digito;  
	var coeficient; 
	var soma;        // soma do d?gito multiplicado pelo coeficiente
	var dvEncontrado;    
	var dv = parseInt( strValor.charAt( strValor.length - 1 ), 10 );
	soma = 0;
	coeficient = 2;
	for ( i = strValor.length - 2; i >= 0 ; i-- ){
		digito = parseInt( strValor.charAt( i ), 10 );               
		soma += digito * coeficient;
		coeficient ++;
		if ( coeficient > 9 ) coeficient = 2;                
	}                
	dvEncontrado = 11 - soma % 11;
	if ( dvEncontrado >= 10 ) dvEncontrado = 0;        
	
	if ( dv == dvEncontrado ) {
		return true;
	} else {
        return false;
	}
}