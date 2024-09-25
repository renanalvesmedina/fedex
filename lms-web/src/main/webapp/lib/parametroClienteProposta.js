var indicador = {ACRESCIMO:'A', DESCONTO:'D', PESO:'P', PONTOS:'P', TABELA:'T', VALOR:'V'};

function setDefaultValues() {
	setElementValue("tpIndicadorMinFretePeso", indicador.TABELA);
	setElementValue("tpIndicadorAdvalorem", indicador.TABELA);

	setElementValue("tpIndicadorFreteMinimo", indicador.DESCONTO);
	setElementValue("tpIndicadorFretePeso", indicador.DESCONTO);

	var vlDefault = "0.00";
	setFormattedValue("vlMinFretePeso", vlDefault);
	setDisabled("vlMinFretePeso", true);
	setFormattedValue("vlFreteMinimo", vlDefault);
	setFormattedValue("vlFretePeso", vlDefault);
	setDisabled("vlFretePeso", true);
	setFormattedValue("vlAdvalorem", vlDefault);
	setDisabled("vlAdvalorem", true);
	setFormattedValue("pcDiferencaAdvalorem", vlDefault);
	
	setElementValue("blFreteExpedido", true);
	verifyIndicadoresTabela();
	disableWorkflow();
}

/** Funções criadas para unificar a validação dos campos em telas e grids */
function getElementByGrid(rowIndex, property) {
	return destinoPropostaGridDef.getCellObject(rowIndex, property);
}
function getElementValueByGrid(rowIndex, property) {
	return getElementValue(getElementByGrid(rowIndex,property));
}
function getElementCustom(rowIndex, property) {
	if(rowIndex == null) {
		return getElement(property);
	} else {
		return getElementByGrid(rowIndex, property);
	}
}
function getElementValueCustom(rowIndex, property) {
	return getElementValue(getElementCustom(rowIndex, property));
}
function getLabelField(field, rowIndex, property) {
	if(rowIndex == null) {
		return field.label;
	}
	return document.getElementById("destinoProposta.header."+property).innerHTML;
}

/**
* Habilita/desabilita os valores de acordo com seus respectivos indicadores
*/
function verifyIndicadoresTabela(rowIndex) {
	/* Campos nao validade pela GRID */
	if(rowIndex == undefined) {
		verifyIndicador(rowIndex, 'tpIndicadorMinFretePeso', 'vlMinFretePeso', 'minimoFretePesoFlag');
	}
	verifyIndicador(rowIndex, 'tpIndicadorFretePeso', 'vlFretePeso');
	verifyIndicador(rowIndex, 'tpIndicadorAdvalorem', 'vlAdvalorem');
}

function validateParametroCliente(rowIndex) {
	return validateGrupoFretePeso(rowIndex) &&
			validateGrupoFreteValor(rowIndex);
}
function validateParametroClienteDestinoProposta(rowIndex) {
	return validateTpPercentualMinimo(rowIndex) &&
			validateTpFretePeso(rowIndex) &&
			validateTpAdvalorem1(rowIndex);
}

function disableWorkflow() {
	var url = new URL(parent.location.href);

	if (url.parameters != undefined 
			&& url.parameters.idProcessoWorkflow != undefined 
			&& url.parameters.idProcessoWorkflow != '') {

   		setDisabled(document, true);
   	}
}

/************************************************
* Metodos do grupo Frete Peso                   *
/************************************************/
function validateTpMinimoFretePeso(rowIndex) {
	verifyIndicador(rowIndex, 'tpIndicadorMinFretePeso', 'vlMinFretePeso', 'minimoFretePesoFlag');
	return validateVlMinimoFretePeso(rowIndex);
}

/** Converte String to Number, e seta o valor novamente no filed para evitar nullPointers nas Actions */
function getCurrentValue(rowIndex, property) {
	var currentValue = stringToNumber(getElementValueCustom(rowIndex, property));
	setFormattedValue(property, ''+currentValue, rowIndex);
	return currentValue;
}

function validateVlMinimoFretePeso(rowIndex) {
	var field = getElementCustom(rowIndex, "tpIndicadorMinFretePeso");
	var tpMinimoFretePeso = getElementValue(field);
	var vlMinimoFretePeso = getCurrentValue(rowIndex, "vlMinFretePeso");
	if( (isPeso(tpMinimoFretePeso) && ltZero(vlMinimoFretePeso)) || (isValor(tpMinimoFretePeso) && ltZero(vlMinimoFretePeso)) ) {
		alertI18nMessage("LMS-01050", getLabelField(field, rowIndex, "tpIndicadorMinFretePeso"), false);
		setFocus(getElementCustom(rowIndex, "vlMinFretePeso"), false);
		return false;
	}
	return true;
}

function validateTpPercentualMinimo(rowIndex) {
	return validateVlPercentualMinimo(rowIndex);
}

function validateVlPercentualMinimo(rowIndex) {
	var field = getElementCustom(rowIndex, "tpIndicadorFreteMinimo");
	var tpPercentualMinimo = getElementValue(field);
	var vlFreteMinimo = getCurrentValue(rowIndex, "vlFreteMinimo");
	if(ltZero(vlFreteMinimo)) {
		alertI18nMessage("LMS-01050", getLabelField(field, rowIndex, "tpIndicadorFreteMinimo"), false);
		setFocus(getElementCustom(rowIndex, "vlFreteMinimo"), false);
		return false;
	}
	if(isDesconto(tpPercentualMinimo)) {
		if(!isPercent(vlFreteMinimo)) {
			alertI18nMessage("LMS-01050", getLabelField(field, rowIndex, "tpIndicadorFreteMinimo"), false);
			setFocus(getElementCustom(rowIndex, "vlFreteMinimo"), false);
			return false;
		}
	}
	return true;
}

function validateTpFretePeso(rowIndex) {
	verifyIndicador(rowIndex, 'tpIndicadorFretePeso', 'vlFretePeso') && validateVlFretePeso(rowIndex);
	var field = getElementCustom(rowIndex, "tpIndicadorFretePeso");
	var tpFretePeso = getElementValue(field);
	if(isValor(tpFretePeso)) {
		var tpIndicadorFreteMinimo = getElementValueCustom(rowIndex, "tpIndicadorFreteMinimo");
		var vlFreteMinimo = getCurrentValue(rowIndex, "vlFreteMinimo");
		if( !(isDesconto(tpIndicadorFreteMinimo) && eqZero(vlFreteMinimo)) ) {
			alertI18nMessage("LMS-01040", getLabelField(field, rowIndex, "tpIndicadorFretePeso"), false);
			setFocus(getElementCustom(rowIndex, "vlFretePeso"), false);
			return false;
		}
	}
	return validateVlFretePeso(rowIndex);
}

function validateVlFretePeso(rowIndex) {
	var field = getElementCustom(rowIndex, "tpIndicadorFretePeso");
	var tpFretePeso = getElementValue(field);
	var vlFretePeso = getCurrentValue(rowIndex, "vlFretePeso");
	if(isDesconto(tpFretePeso)) {
		if(!isPercent(vlFretePeso)) {
			alertI18nMessage("LMS-01050", getLabelField(field, rowIndex, "tpIndicadorFretePeso"), false);
			setFocus(getElementCustom(rowIndex, "vlFretePeso"), false);
			return false;
		}
	} else if(isIn(tpFretePeso, [indicador.ACRESCIMO, indicador.VALOR]) && ltZero(vlFretePeso)) {
		alertI18nMessage("LMS-01050", getLabelField(field, rowIndex, "tpIndicadorFretePeso"), false);
		setFocus(getElementCustom(rowIndex, "vlFretePeso"), false);
		return false;
	}
	return true;
}

function validateGrupoFretePeso(rowIndex) {
	return validateTpMinimoFretePeso(rowIndex) && 
			validateTpPercentualMinimo(rowIndex) &&
			validateTpFretePeso(rowIndex);
}

function disableGrupoFretePeso(bool) {
	setDisabled("tpIndicadorMinFretePeso", bool);
	setDisabled("vlMinFretePeso", bool);
	setElementValue('minimoFretePesoFlag', bool);
	setDisabled("blPagaPesoExcedente", bool);
	disableWorkflow();
}

/************************************************
* Metodos do grupo Frete Valor                  *
/************************************************/
function validateTpAdvalorem1(rowIndex) {
	return validateVlAdvalorem1(rowIndex);
}

function validateVlAdvalorem1(rowIndex) {
	verifyIndicador(rowIndex, "tpIndicadorAdvalorem", "vlAdvalorem");
	var field = getElementCustom(rowIndex, "tpIndicadorAdvalorem");
	var tpAdvalorem1 = getElementValue(field);
	var vlAdvalorem1 = getCurrentValue(rowIndex, "vlAdvalorem");
	if(isDesconto(tpAdvalorem1)) {
		if(!isPercent(vlAdvalorem1)) {
			alertI18nMessage("LMS-01050", getLabelField(field, rowIndex, "tpIndicadorAdvalorem"), false);
			setFocus(getElementCustom(rowIndex, "vlAdvalorem"), false);
			return false;
		}
	} else if(isIn(tpAdvalorem1, [indicador.ACRESCIMO, indicador.VALOR]) && ltZero(vlAdvalorem1)) {
		alertI18nMessage("LMS-01050", getLabelField(field, rowIndex, "tpIndicadorAdvalorem"), false);
		setFocus(getElementCustom(rowIndex, "vlAdvalorem"), false);
		return false;
	}
	return true;
}

function validateGrupoFreteValor(rowIndex) {
	return validateTpAdvalorem1(rowIndex);
}

function verifyIndicador(rowIndex, comboName, relatedTextName, flagName) {
	var flagValue = false;
	if(flagName != undefined) {
		flagValue = getElementValueCustom(rowIndex, flagName);
	}
	if(flagValue==false || flagValue=="false") {
		var comboValue = getElementValueCustom(rowIndex, comboName);
		if(comboValue != '' && isTabela(comboValue)) {
			setFormattedValue(relatedTextName, "0", rowIndex);
			setDisabled(getElementCustom(rowIndex, relatedTextName), true);
		} else {
			setDisabled(getElementCustom(rowIndex, relatedTextName), false);
		}
	}
}

function setFormattedValue(field, value, rowIndex) {
	setElementValue(getElementCustom(rowIndex, field), setFormat(getElementCustom(rowIndex, field), value));
}

function isAcrescimo(indic) {
	return (indic == indicador.ACRESCIMO);
}

function isDesconto(indic) {
	return (indic == indicador.DESCONTO);
}

function isPeso(indic) {
	return (indic == indicador.PESO);
}

function isPonto(indic) {
	return (indic == indicador.PONTOS);
}

function isTabela(indic) {
	return (indic == indicador.TABELA);
}

function isValor(indic) {
	return (indic == indicador.VALOR);
}

function isIn(val, arr){
	for(var k in arr) if(arr[k] == val) return true;
	return false;
}

function eqZero(val) {
	return (val == 0);
}

function leZero(val) {
	return (val <= 0);
}

function ltZero(val) {
	return (val < 0);
}

function isPercent(val) {
	return !( (val < 0) || (val > 100) );
}
