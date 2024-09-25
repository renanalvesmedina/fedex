var indicador = {ACRESCIMO:'A', DESCONTO:'D', PESO:'P', PONTOS:'P', TABELA:'T', VALOR:'V'};
var limiteDesconto = {};
var telaOrigem = undefined;
var isProposta = false;

function setDefaultValues(parameterType) {
	var def = "T";
	setElementValue("tpIndicadorMinFretePeso", def);
	setElementValue("tpIndicadorFretePeso", def);
	setElementValue("tpTarifaMinima", def);
	setElementValue("tpIndicVlrTblEspecifica", def);
	setElementValue("tpIndicadorAdvalorem", def);
	setElementValue("tpIndicadorAdvalorem2", def);
	setElementValue("tpIndicadorValorReferencia", def);
	setElementValue("tpIndicadorPercentualGris", def);
	setElementValue("tpIndicadorMinimoGris", def);
	setElementValue("tpIndicadorPercentualTrt", def);
	setElementValue("tpIndicadorMinimoTrt", def);
	setElementValue("tpIndicadorPedagio", def);

	def = "D";
	setElementValue("tpIndicadorPercMinimoProgr", def);

	var vlDefault = "0.00";
	setFormattedValue("vlMinFretePeso", vlDefault);
	setDisabled("vlMinFretePeso", true);
	setFormattedValue("vlPercMinimoProgr", vlDefault);
	setFormattedValue("vlFretePeso", vlDefault);
	setDisabled("vlFretePeso", true);
	setFormattedValue("vlMinimoFreteQuilo", vlDefault);
	setFormattedValue("vlFreteVolume", vlDefault);
	setFormattedValue("vlTarifaMinima", vlDefault);
	setDisabled("vlTarifaMinima", true);
	setFormattedValue("vlTblEspecifica", vlDefault);
	setDisabled("vlTblEspecifica", true);
	setFormattedValue("vlAdvalorem", vlDefault);
	setDisabled("vlAdvalorem", true);
	setFormattedValue("vlAdvalorem2", vlDefault);
	setDisabled("vlAdvalorem2", true);
	setFormattedValue("vlValorReferencia", vlDefault);
	setDisabled("vlValorReferencia", true);
	setFormattedValue("pcFretePercentual", vlDefault);
	setFormattedValue("vlMinimoFretePercentual", vlDefault);
	setFormattedValue("vlToneladaFretePercentual", vlDefault);
	setFormattedValue("psFretePercentual", vlDefault);
	setFormattedValue("vlPercentualGris", vlDefault);
	setDisabled("vlPercentualGris", true);
	setFormattedValue("vlMinimoGris", vlDefault);
	setDisabled("vlMinimoGris", true);
	setFormattedValue("vlPercentualTrt", vlDefault);
	setDisabled("vlPercentualTrt", true);
	setFormattedValue("vlMinimoTrt", vlDefault);
	setDisabled("vlMinimoTrt", true);
	setFormattedValue("vlPercentualTde", vlDefault);
	setDisabled("vlPercentualTde", true);
	setFormattedValue("vlMinimoTde", vlDefault);
	setDisabled("vlMinimoTde", true);
	setFormattedValue("vlPedagio", vlDefault);
	setDisabled("vlPedagio", true);
	setFormattedValue("pcDescontoFreteTotal", vlDefault);
	setFormattedValue("pcCobrancaReentrega", vlDefault);
	setFormattedValue("pcCobrancaDevolucoes", vlDefault);

	disableWorkflow();
}

function findLimitesDescontos(service) {
	var idSubtipoTabelaPreco = getElementValue("tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco");
	var tpTipoTabelaPreco = getElementValue("tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco");
	if (service == undefined) {
		service = "lms.vendas.manterParametrosClienteAction.findLimitesDescontos";
	}
	var args = {idSubtipoTabelaPreco:idSubtipoTabelaPreco, tpTipoTabelaPreco:tpTipoTabelaPreco};
	var sdo = createServiceDataObject(service, "findLimitesDescontos", args);
	xmit({serviceDataObjects:[sdo]});
}

function findLimitesDescontos_cb(data, erros){
	if (erros) {
		alert(erros);
		setFocusOnFirstFocusableField(document);
		return false;
	}

	limiteDesconto.pcLimiteDescontoFretePeso = stringToNumber(data.pcLimiteDescontoFretePeso);
	limiteDesconto.pcLimiteDescontoFreteValor = stringToNumber(data.pcLimiteDescontoFreteValor);
	limiteDesconto.pcLimiteDescontoGris = stringToNumber(data.pcLimiteDescontoGris);
	limiteDesconto.pcLimiteDescontoTrt = stringToNumber(data.pcLimiteDescontoTrt);
	limiteDesconto.pcLimiteDescontoPedagio = stringToNumber(data.pcLimiteDescontoPedagio);
	limiteDesconto.pcLimiteDescontoTde = stringToNumber(data.pcLimiteDescontoTde);
	limiteDesconto.pcLimiteDescontoTotalFrete = stringToNumber(data.pcLimiteDescontoTotalFrete);

	var step = stringToNumber(data.STEP_FRETE_PESO);
	if(step == 0 || step == 5) {
		disableGrupoFretePeso(false);
	} else {
		disableGrupoFretePeso(true);
	}

	step = stringToNumber(data.STEP_FRETE_VALOR);
	if(step == 0 || step == 5) {
		disableGrupoFretePercentual(false);
		disableGrupoFreteValor(false);
	} else if( (step > 0) && (step < 5) ) {
		disableGrupoFretePercentual(true);
		disableGrupoFreteValor(true);
	}
	verifyIndicadoresTabela();
}

/**
* Habilita/desabilita os valores de acordo com seus respectivos indicadores
*/
function verifyIndicadoresTabela() {
	verifyIndicador('tpIndicadorMinFretePeso', 'vlMinFretePeso', 'minimoFretePesoFlag');
	verifyIndicador('tpIndicadorFretePeso', 'vlFretePeso');
	verifyIndicador('tpTarifaMinima', 'vlTarifaMinima', 'tarifaMinimaFlag');
	verifyIndicador('tpIndicVlrTblEspecifica', 'vlTblEspecifica', 'tarifaEspecificaFlag');
	verifyIndicador('tpIndicadorAdvalorem', 'vlAdvalorem');
	verifyIndicador('tpIndicadorAdvalorem2', 'vlAdvalorem2');
	verifyIndicador('tpIndicadorValorReferencia', 'vlValorReferencia', 'valorReferenciaFlag');
	verifyIndicador('tpIndicadorPercentualGris', 'vlPercentualGris');
	verifyIndicador('tpIndicadorMinimoGris', 'vlMinimoGris');
	verifyIndicador('tpIndicadorPercentualTrt', 'vlPercentualTrt');
	verifyIndicador('tpIndicadorMinimoTrt', 'vlMinimoTrt');
	verifyIndicador('tpIndicadorPercentualTde', 'vlPercentualTde');
	verifyIndicador('tpIndicadorMinimoTde', 'vlMinimoTde');
	verifyIndicador('tpIndicadorPedagio', 'vlPedagio');
}

function validateParametroCliente() {
	return validateGrupoFretePeso() &&
			validateGrupoFreteValor() &&
			validateGrupoFretePercentual() &&
			validateGrupoGris() &&
			validateGrupoPedagio() &&
			validateGrupoTrt() &&
			validateGrupoTde() &&
			validateGrupoTotalFrete();
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
function validateTpMinimoFretePeso() {
	verifyIndicador('tpIndicadorMinFretePeso', 'vlMinFretePeso', 'minimoFretePesoFlag');
	return validateVlMinimoFretePeso();
}

/** Converte String to Number, e seta o valor novamente no filed para evitar nullPointers nas Actions */
function getCurrentValue(property) {
	var currentValue = stringToNumber(getElementValue(property));
	setFormattedValue(property, ''+currentValue);
	return currentValue;
}

function validateVlMinimoFretePeso() {
	var field = getElement("tpIndicadorMinFretePeso");
	var tpMinimoFretePeso = getElementValue(field);
	var vlMinimoFretePeso = getCurrentValue("vlMinFretePeso");
	if( (isPeso(tpMinimoFretePeso) && leZero(vlMinimoFretePeso)) || (isValor(tpMinimoFretePeso) && ltZero(vlMinimoFretePeso)) ) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return true;
}

function validateTpPercentualMinimoProgressivo() {
	return validateVlPercentualMinimoProgressivo();
}

function validateVlPercentualMinimoProgressivo() {
	var field = getElement("tpIndicadorPercMinimoProgr");
	var tpPercentualMinimoProgressivo = getElementValue(field);
	var vlPercentualMinimoProgressivo = getCurrentValue("vlPercMinimoProgr");
	if(ltZero(vlPercentualMinimoProgressivo)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	if(isDesconto(tpPercentualMinimoProgressivo)) {
		if(!isPercent(vlPercentualMinimoProgressivo)) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
		if(vlPercentualMinimoProgressivo > limiteDesconto.pcLimiteDescontoFretePeso) {
			alertI18nMessage("LMS-01060", field.label, false);
			return false;
		}
	}
	return true;
}

function validateTpFretePeso() {
	verifyIndicador('tpIndicadorFretePeso', 'vlFretePeso') && validateVlFretePeso();
	var field = getElement("tpIndicadorFretePeso");
	var tpFretePeso = getElementValue(field);
	if(isValor(tpFretePeso)) {
		var tpPercentualMinimoProgressivo = getElementValue("tpIndicadorPercMinimoProgr");
		var vlPercentualMinimoProgressivo = getCurrentValue("vlPercMinimoProgr");
		if( !(isDesconto(tpPercentualMinimoProgressivo) && eqZero(vlPercentualMinimoProgressivo)) ) {
			alertI18nMessage("LMS-01040", field.label, false);
			return false;
		}
	}
	if(limiteDesconto.pcLimiteDescontoFretePeso < 100) {
		if(!isIn(tpFretePeso, [indicador.TABELA, indicador.DESCONTO, indicador.ACRESCIMO])) {
			alertI18nMessage("LMS-01070", field.label, false);
			return false;
		}
	}
	return validateVlFretePeso();
}

function validateVlFretePeso() {
	var field = getElement("tpIndicadorFretePeso");
	var tpFretePeso = getElementValue(field);
	var vlFretePeso = getCurrentValue("vlFretePeso");
	if(isDesconto(tpFretePeso)) {
		if(!isPercent(vlFretePeso)) {
			alertI18nMessage("LMS-01050", field.label, false);
			setFocus("vlFretePeso");
			return false;
		}
		if(vlFretePeso > limiteDesconto.pcLimiteDescontoFretePeso) {
			alertI18nMessage("LMS-01060", field.label, false);
			return false;
		}
	} else if(isIn(tpFretePeso, [indicador.ACRESCIMO, indicador.VALOR]) && ltZero(vlFretePeso)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	if (telaOrigem != "proposta") {
		if (limiteDesconto.pcLimiteDescontoFretePeso < 100.0) {
			var vlMinimoFreteQuilo = getCurrentValue("vlMinimoFreteQuilo");
			var tpMinimoFretePeso = getElementValue("tpIndicadorMinFretePeso");
			var vlMinimoFretePeso = getCurrentValue("vlMinFretePeso");
			var blPagaPesoExcedente = getElementValue("blPagaPesoExcedente");
			var tpTarifaMinima = getElementValue("tpTarifaMinima");
			var vlTarifaMinima = getCurrentValue("vlTarifaMinima");
			var vlFreteVolume = getCurrentValue("vlFreteVolume");
			var tpTarifaEspecifica = getElementValue("tpIndicVlrTblEspecifica");
			var vlTarifaEspecifica = getCurrentValue("vlTblEspecifica");
			
			if (!eqZero(vlMinimoFreteQuilo) || tpMinimoFretePeso != "T" || 
				!eqZero(vlMinimoFretePeso) || blPagaPesoExcedente != false ||
				tpTarifaMinima != "T" || !eqZero(vlTarifaMinima) ||
				!eqZero(vlFreteVolume) || tpTarifaEspecifica != "T" ||
				!eqZero(vlTarifaEspecifica)) {
				alertI18nMessage("LMS-01155");
				return false;
			}
		}
	}
	return true;
}

function validateVlMinimoFreteQuilo() {
	var field = getElement("vlMinimoFreteQuilo");
	var vlMinimoFreteQuilo = getCurrentValue("vlMinimoFreteQuilo");
	if(ltZero(vlMinimoFreteQuilo)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	if(!eqZero(vlMinimoFreteQuilo)) {
		var tpFretePeso = getElementValue("tpIndicadorFretePeso");
		if(!isValor(tpFretePeso)) {
			alertI18nMessage("LMS-01040", field.label, false);
			return false;
		}
	}
	return true;
}

function validateBlPagaPesoExcedente() {
	var field = getElement("blPagaPesoExcedente");
	var blPagaPesoExcedente = getElementValue(field);
	if(blPagaPesoExcedente == true) {
		var vlMinimoFreteQuilo = getCurrentValue("vlMinimoFreteQuilo");
		var tpMinimoFretePeso = getElementValue("tpIndicadorMinFretePeso");
		if(eqZero(vlMinimoFreteQuilo) || !isPeso(tpMinimoFretePeso)) {
			alertI18nMessage("LMS-01040", field.label, false);
			return false;
		}
	}
	return true;
}

function validateTpTarifaMinima() {
	verifyIndicador('tpTarifaMinima', 'vlTarifaMinima', 'tarifaMinimaFlag')
	return validateVlTarifaMinima();
}

function validateVlTarifaMinima() {
	var field = getElement("tpTarifaMinima");
	var tpTarifaMinima = getElementValue(field);
	var vlTarifaMinima = getCurrentValue("vlTarifaMinima");
	if(isDesconto(tpTarifaMinima)) {
		if(!isPercent(vlTarifaMinima)) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
	} else if(isIn(tpTarifaMinima, [indicador.ACRESCIMO, indicador.VALOR]) && ltZero(vlTarifaMinima)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return true;
}

function validateVlFreteVolume() {
	var field = getElement("vlFreteVolume");
	var vlFreteVolume = getCurrentValue("vlFreteVolume");
	if(ltZero(vlFreteVolume)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	if(!eqZero(vlFreteVolume)) {
		var tpFretePeso = getElementValue("tpIndicadorFretePeso");
		var tpPercentualMinimoProgressivo = getElementValue("tpIndicadorPercMinimoProgr");
		var vlPercentualMinimoProgressivo = getCurrentValue("vlPercMinimoProgr");
		if( !(isTabela(tpFretePeso) && isDesconto(tpPercentualMinimoProgressivo) && eqZero(vlPercentualMinimoProgressivo)) ) {
			alertI18nMessage("LMS-01040", field.label, false);
			return false;
		}
	}
	return true;
}

function validateTpTarifaEspecifica() {
	verifyIndicador('tpIndicVlrTblEspecifica', 'vlTblEspecifica', 'tarifaEspecificaFlag')
	return validateVlTarifaEspecifica();
}

function validateVlTarifaEspecifica() {
	var field = getElement("tpIndicVlrTblEspecifica");
	var tpTarifaEspecifica = getElementValue(field);
	var vlTarifaEspecifica = getCurrentValue("vlTblEspecifica");
	if(isDesconto(tpTarifaEspecifica)) {
		if(!isPercent(vlTarifaEspecifica)) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
	} else if(isIn(tpTarifaEspecifica, [indicador.ACRESCIMO, indicador.VALOR]) && ltZero(vlTarifaEspecifica)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return true;
}

function validateGrupoFretePeso() {
	return validateTpMinimoFretePeso() && 
			validateTpPercentualMinimoProgressivo() &&
			validateTpFretePeso() &&
			validateVlMinimoFreteQuilo() &&
			validateBlPagaPesoExcedente() &&
			validateTpTarifaMinima() &&
			validateVlFreteVolume() &&
			validateTpTarifaEspecifica();
}

function disableGrupoFretePeso(bool) {
	setDisabled("tpIndicadorMinFretePeso", bool);
	setDisabled("vlMinFretePeso", bool);

	/* Ajuste referente a Parametros da Proposta
	 * by André Valadas
	 */
	if(isProposta) {
		setDisabled("tpIndicadorPercMinimoProgr", bool);
		setDisabled("vlPercMinimoProgr", bool);
		setDisabled("tpIndicadorFretePeso", bool);
		setDisabled("vlFretePeso", bool);
	}

	setElementValue('minimoFretePesoFlag', bool);
	setDisabled("vlMinimoFreteQuilo", bool);
	setDisabled("vlFreteVolume", bool);
	setDisabled("tpTarifaMinima", bool);
	setDisabled("vlTarifaMinima", bool);
	setElementValue('tarifaMinimaFlag', bool);
	setDisabled("blPagaPesoExcedente", bool);
	setDisabled("tpIndicVlrTblEspecifica", bool);
	setDisabled("vlTblEspecifica", bool);
	setElementValue('tarifaEspecificaFlag', bool);
	disableWorkflow();
}

/************************************************
* Metodos do grupo Frete Valor                  *
/************************************************/
function validateTpAdvalorem1() {
	verifyIndicador("tpIndicadorAdvalorem", "vlAdvalorem")
	var field = getElement("tpIndicadorAdvalorem");
	var tpAdvalorem1 = getElementValue(field);
	if(limiteDesconto.pcLimiteDescontoFreteValor < 100) {
		if(!isIn(tpAdvalorem1, [indicador.TABELA, indicador.DESCONTO, indicador.ACRESCIMO])) {
			alertI18nMessage("LMS-01070", field.label, false);
			return false;
		}
	}
	return validateVlAdvalorem1();
}

function validateVlAdvalorem1() {
	var field = getElement("tpIndicadorAdvalorem");
	var tpAdvalorem1 = getElementValue(field);
	var vlAdvalorem1 = getCurrentValue("vlAdvalorem");
	if(isDesconto(tpAdvalorem1)) {
		if(!isPercent(vlAdvalorem1)) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
		if(vlAdvalorem1 > limiteDesconto.pcLimiteDescontoFreteValor) {
			alertI18nMessage("LMS-01060", field.label, false);
			return false;
		}
	} else if(isIn(tpAdvalorem1, [indicador.ACRESCIMO, indicador.VALOR]) && ltZero(vlAdvalorem1)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return true;
}

function validateTpAdvalorem2() {
	verifyIndicador('tpIndicadorAdvalorem2', 'vlAdvalorem2');
	var field = getElement("tpIndicadorAdvalorem2");
	var tpAdvalorem2 = getElementValue(field);
	if(limiteDesconto.pcLimiteDescontoFreteValor < 100) {
		if(!isIn(tpAdvalorem2, [indicador.TABELA, indicador.DESCONTO, indicador.ACRESCIMO])) {
			alertI18nMessage("LMS-01070", field.label, false);
			return false;
		}
	}
	return validateVlAdvalorem2();
}

function validateVlAdvalorem2() {
	var field = getElement("tpIndicadorAdvalorem2");
	var tpAdvalorem2 = getElementValue(field);
	var vlAdvalorem2 = getCurrentValue("vlAdvalorem2");
	if(isDesconto(tpAdvalorem2)) {
		if(!isPercent(vlAdvalorem2)) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
		if(vlAdvalorem2 > limiteDesconto.pcLimiteDescontoFreteValor) {
			alertI18nMessage("LMS-01060", field.label, false);
			return false;
		}
	} else if(isIn(tpAdvalorem2, [indicador.ACRESCIMO, indicador.VALOR]) && ltZero(vlAdvalorem2)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return true;
}

function validateTpValorReferencia() {
	verifyIndicador('tpIndicadorValorReferencia', 'vlValorReferencia', 'valorReferenciaFlag');
	return validateVlValorReferencia();
}

function validateVlValorReferencia() {
	var field = getElement("tpIndicadorValorReferencia");
	var tpValorReferencia = getElementValue(field);
	var vlValorReferencia = getCurrentValue("vlValorReferencia");
	if(isDesconto(tpValorReferencia)) {
		if(!isPercent(vlValorReferencia)) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
	} else if(isIn(tpValorReferencia, [indicador.ACRESCIMO, indicador.VALOR]) && ltZero(vlValorReferencia)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return true;
}

function validateGrupoFreteValor() {
	return validateTpAdvalorem1() &&
			validateTpAdvalorem2() &&
			validateTpValorReferencia();
}

function disableGrupoFreteValor(bool) {
	/* Ajuste referente a Parametros da Proposta
	 * by André Valadas
	 */
	if(isProposta) {
		setDisabled("tpIndicadorAdvalorem", bool);
		setDisabled("vlAdvalorem", bool);
		setDisabled("tpIndicadorAdvalorem2", bool);
		setDisabled("vlAdvalorem2", bool);
	}

	setDisabled("tpIndicadorValorReferencia", bool);
	setDisabled("vlValorReferencia", bool);
	setElementValue('valorReferenciaFlag', bool);
	disableWorkflow();
}

/************************************************
* Metodos do grupo Frete Percentual             *
/************************************************/
function validateVlPercentualFretePercentual() {
	var field = getElement("pcFretePercentual");
	var vlPercentualFretePercentual = getCurrentValue("pcFretePercentual");
	if(!isPercent(vlPercentualFretePercentual)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	if(eqZero(vlPercentualFretePercentual)) {
		setFormattedValue("vlMinimoFretePercentual", "0");
		setFormattedValue("vlToneladaFretePercentual", "0");
		setFormattedValue("psFretePercentual", "0");
	}
	if(!eqZero(vlPercentualFretePercentual)) {
		var tpAdvalorem1 = getElementValue("tpIndicadorAdvalorem");
		var tpAdvalorem2 = getElementValue("tpIndicadorAdvalorem2");
		var tpIndicadorMinFretePeso = getElementValue("tpIndicadorMinFretePeso");
		var vlPercentualMinimoProgressivo = getCurrentValue("vlPercMinimoProgr");
		var tpIndicadorFretePeso = getElementValue("tpIndicadorFretePeso");
		var vlMinimoFreteQuilo = getCurrentValue("vlMinimoFreteQuilo");
		var tpTarifaMinima = getElementValue("tpTarifaMinima");
		var vlFreteVolume = getCurrentValue("vlFreteVolume");
		var tpTarifaEspecifica = getElementValue("tpIndicVlrTblEspecifica");
		if( (!isTabela(tpAdvalorem1) || !isTabela(tpAdvalorem2)) ||
			(!isTabela(tpIndicadorMinFretePeso) || !isTabela(tpIndicadorFretePeso) || !isTabela(tpTarifaMinima) || !isTabela(tpTarifaEspecifica)) ||
			(!eqZero(vlPercentualMinimoProgressivo) || !eqZero(vlMinimoFreteQuilo) || !eqZero(vlFreteVolume) )
		) {
			alertI18nMessage("LMS-01040", field.label, false);
			return false;
		}
	}
	return true;

}

function validateVlMinimoFretePercentual() {
	var field = getElement("vlMinimoFretePercentual");
	var vlMinimoFretePercentual = getCurrentValue("vlMinimoFretePercentual");
	if(ltZero(vlMinimoFretePercentual)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return validateFretePercentual();
}

function validateVlToneladaFretePercentual() {
	var field = getElement("vlToneladaFretePercentual");
	var vlToneladaFretePercentual = getCurrentValue("vlToneladaFretePercentual");
	if(ltZero(vlToneladaFretePercentual)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return validateFretePercentual();
}

function validatePsReferenciaFretePercentual() {
	var field = getElement("psFretePercentual");
	var psFretePercentual = getCurrentValue("psFretePercentual");
	if(ltZero(psFretePercentual)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return validateFretePercentual();
}

function validateFretePercentual() {
	var vlMinimoFretePercentual = getCurrentValue("vlMinimoFretePercentual");
	var vlToneladaFretePercentual = getCurrentValue("vlToneladaFretePercentual");
	var psFretePercentual = getCurrentValue("psFretePercentual");
	var vlPercentualFretePercentual = getCurrentValue("pcFretePercentual");
	if( (!eqZero(vlMinimoFretePercentual) || !eqZero(vlToneladaFretePercentual) || !eqZero(psFretePercentual)) && eqZero(vlPercentualFretePercentual) ) {
		alertI18nMessage("LMS-01045");
		return false;
	}
	return true;
}

function validateGrupoFretePercentual() {
	return validateVlPercentualFretePercentual() &&
			validateVlMinimoFretePercentual() &&
			validateVlToneladaFretePercentual() &&
			validatePsReferenciaFretePercentual();
}

function disableGrupoFretePercentual(bool) {
	setDisabled("pcFretePercentual", bool);
	setDisabled("vlMinimoFretePercentual", bool);
	setDisabled("vlToneladaFretePercentual", bool);
	setDisabled("psFretePercentual", bool);
	disableWorkflow();
}

/************************************************
* Metodos do grupo GRIS                         *
/************************************************/
function validateTpPercentualGris() {
	verifyIndicador('tpIndicadorPercentualGris', 'vlPercentualGris');
	var field = getElement("tpIndicadorPercentualGris");
	var tpPercentualGris = getElementValue(field);
	if(limiteDesconto.pcLimiteDescontoGris <100) {
		if(!isIn(tpPercentualGris, [indicador.TABELA, indicador.DESCONTO, indicador.ACRESCIMO])) {
			alertI18nMessage("LMS-01070", field.label, false);
			return false;
		}
	}
	return validateVlPercentualGris();
}

function validateVlPercentualGris() {
	var field = getElement("tpIndicadorPercentualGris");
	var tpPercentualGris = getElementValue(field);
	var vlPercentualGris = getCurrentValue("vlPercentualGris");
	if(isDesconto(tpPercentualGris)) {
		if(!isPercent(vlPercentualGris)) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
		if(vlPercentualGris > limiteDesconto.pcLimiteDescontoGris) {
			alertI18nMessage("LMS-01060", field.label, false);
			return false;
		}
	} else if(isIn(tpPercentualGris, [indicador.ACRESCIMO, indicador.VALOR]) && ltZero(vlPercentualGris)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	var pcCobrancaDevolucoes = getCurrentValue("pcCobrancaDevolucoes");
	if(!isPercent(pcCobrancaDevolucoes)) {
		alertI18nMessage("LMS-01070", field.label, false);
		return false;
	}
	return true;
}

function validateTpMinimoGris() {
	verifyIndicador('tpIndicadorMinimoGris', 'vlMinimoGris');
	var field = getElement("tpIndicadorMinimoGris");
	var tpMinimoGris = getElementValue(field);
	if(limiteDesconto.pcLimiteDescontoGris < 100) {
		if(!isIn(tpMinimoGris, [indicador.TABELA, indicador.DESCONTO, indicador.ACRESCIMO])) {
			alertI18nMessage("LMS-01070", field.label, false);
			return false;
		}
	}
	return validateVlMinimoGris();
}

function validateVlMinimoGris() {
	var field = getElement("tpIndicadorMinimoGris");
	var tpMinimoGris = getElementValue(field);
	var vlMinimoGris = getCurrentValue("vlMinimoGris");
	if(isDesconto(tpMinimoGris)) {
		if(!isPercent(vlMinimoGris)) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
		if(vlMinimoGris > limiteDesconto.pcLimiteDescontoGris) {
			alertI18nMessage("LMS-01060", field.label, false);
			return false;
		}
	} else if(isIn(tpMinimoGris, [indicador.ACRESCIMO, indicador.VALOR]) && ltZero(vlMinimoGris)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return true;
}

function validateGrupoGris() {
	return validateTpPercentualGris() && validateTpMinimoGris();
}

/************************************************
* Metodos do grupo Ped?gio                      *
/************************************************/
function validateTpPedagio() {
	verifyIndicador('tpIndicadorPedagio', 'vlPedagio');
	var field = getElement("tpIndicadorPedagio");
	var tpPedagio = getElementValue(field);
	if(limiteDesconto.pcLimiteDescontoPedagio < 100) {
		if(!isIn(tpPedagio, [indicador.TABELA, indicador.DESCONTO, indicador.ACRESCIMO])) {
			alertI18nMessage("LMS-01070", field.label, false);
			return false;
		}
	}
	return validateVlPedagio();
}

function validateVlPedagio() {
	var field = getElement("tpIndicadorPedagio");
	var tpPedagio = getElementValue(field);
	var vlPedagio = getCurrentValue("vlPedagio");
	if(isDesconto(tpPedagio)) {
		if(!isPercent(vlPedagio)) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
		if(vlPedagio > limiteDesconto.pcLimiteDescontoPedagio) {
			alertI18nMessage("LMS-01060", field.label, false);
			return false;
		}
	}
	else if( (isPeso(tpPedagio) && eqZero(vlPedagio)) || ltZero(vlPedagio) ) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return true;
}

function validateGrupoPedagio() {
	return validateTpPedagio();
}


/************************************************
* Metodos do grupo TRT                         *
/************************************************/
function validateTpPercentualTrt() {
	verifyIndicador('tpIndicadorPercentualTrt', 'vlPercentualTrt');
	var field = getElement("tpIndicadorPercentualTrt");
	var tpPercentualTrt = getElementValue(field);
	if(limiteDesconto.pcLimiteDescontoTrt <100) {
		if(!isIn(tpPercentualTrt, [indicador.TABELA, indicador.DESCONTO, indicador.ACRESCIMO])) {
			alertI18nMessage("LMS-01070", field.label, false);
			return false;
		}
	}
	return validateVlPercentualTrt();
}

function validateVlPercentualTrt() {
	var field = getElement("tpIndicadorPercentualTrt");
	var tpPercentualTrt = getElementValue(field);
	var vlPercentualTrt = getCurrentValue("vlPercentualTrt");
	if(isDesconto(tpPercentualTrt)) {
		if(!isPercent(vlPercentualTrt)) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
		if(vlPercentualTrt > limiteDesconto.pcLimiteDescontoTrt) {
			alertI18nMessage("LMS-01060", field.label, false);
			return false;
		}
	} else if(isIn(tpPercentualTrt, [indicador.ACRESCIMO, indicador.VALOR]) && ltZero(vlPercentualTrt)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	var pcCobrancaDevolucoes = getCurrentValue("pcCobrancaDevolucoes");
	if(!isPercent(pcCobrancaDevolucoes)) {
		alertI18nMessage("LMS-01070", field.label, false);
		return false;
	}
	return true;
}

function validateTpMinimoTrt() {
	verifyIndicador('tpIndicadorMinimoTrt', 'vlMinimoTrt');
	var field = getElement("tpIndicadorMinimoTrt");
	var tpMinimoTrt = getElementValue(field);
	if(limiteDesconto.pcLimiteDescontoTrt < 100) {
		if(!isIn(tpMinimoTrt, [indicador.TABELA, indicador.DESCONTO, indicador.ACRESCIMO])) {
			alertI18nMessage("LMS-01070", field.label, false);
			return false;
		}
	}
	return validateVlMinimoTrt();
}

function validateVlMinimoTrt() {
	var field = getElement("tpIndicadorMinimoTrt");
	var tpMinimoTrt = getElementValue(field);
	var vlMinimoTrt = getCurrentValue("vlMinimoTrt");
	if(isDesconto(tpMinimoTrt)) {
		if(!isPercent(vlMinimoTrt)) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
		if(vlMinimoTrt > limiteDesconto.pcLimiteDescontoTrt) {
			alertI18nMessage("LMS-01060", field.label, false);
			return false;
		}
	} else if(isIn(tpMinimoTrt, [indicador.ACRESCIMO, indicador.VALOR]) && ltZero(vlMinimoTrt)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return true;
}

function validateGrupoTrt() {
	return validateTpPercentualTrt() && validateTpMinimoTrt();
}

/************************************************
* Metodos do grupo TDE                          *
/************************************************/
function validateTpPercentualTde() {
	verifyIndicador('tpIndicadorPercentualTde', 'vlPercentualTde');
	var field = getElement("tpIndicadorPercentualTde");
	var tpPercentualTde = getElementValue(field);
	if(limiteDesconto.pcLimiteDescontoTde < 100) {
		if(!isIn(tpPercentualTde, [indicador.TABELA, indicador.DESCONTO, indicador.ACRESCIMO])) {
			alertI18nMessage("LMS-01070", field.label, false);
			return false;
		}
	}
	return validateVlPercentualTde();
}

function validateVlPercentualTde() {
	var field = getElement("tpIndicadorPercentualTde");
	var tpPercentualTde = getElementValue(field);
	var vlPercentualTde = getCurrentValue("vlPercentualTde");
	if(isDesconto(tpPercentualTde)) {
		if(!isPercent(vlPercentualTde)) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
		if(vlPercentualTde > limiteDesconto.pcLimiteDescontoTde) {
			alertI18nMessage("LMS-01060", field.label, false);
			return false;
		}
	} else if(isIn(tpPercentualTde, [indicador.ACRESCIMO, indicador.PONTOS, indicador.VALOR]) && ltZero(vlPercentualTde)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return true;
}

function validateTpMinimoTde() {
	verifyIndicador('tpIndicadorMinimoTde', 'vlMinimoTde');
	var field = getElement("tpIndicadorMinimoTde");
	var tpMinimoTde = getElementValue(field);
	if(limiteDesconto.pcLimiteDescontoTde < 100) {
		if(!isIn(tpMinimoTde, [indicador.TABELA, indicador.DESCONTO, indicador.ACRESCIMO])) {
			alertI18nMessage("LMS-01070", field.label, false);
			return false;
		}
	}
	return validateVlMinimoTde();
}

function validateVlMinimoTde() {
	var field = getElement("tpIndicadorMinimoTde");
	var tpMinimoTde = getElementValue(field);
	var vlMinimoTde = getCurrentValue("vlMinimoTde");
	if(isDesconto(tpMinimoTde)) {
		if(!isPercent(vlMinimoTde)) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
		if(vlMinimoTde > limiteDesconto.pcLimiteDescontoTde) {
			alertI18nMessage("LMS-01060", field.label, false);
			return false;
		}
	} else if(isIn(tpMinimoTde, [indicador.ACRESCIMO, indicador.VALOR]) && ltZero(vlMinimoTde)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return true;
}

function validateGrupoTde() {
	return validateTpPercentualTde() && validateTpMinimoTde();
}

/************************************************
* Metodos do grupo Total Frete                  *
/************************************************/
function validateVlPercentualDescontoFreteTotal() {
	var field = getElement("pcDescontoFreteTotal");
	var pcDescontoFreteTotal = getCurrentValue("pcDescontoFreteTotal");
	if(!isPercent(pcDescontoFreteTotal)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}

	if(!eqZero(pcDescontoFreteTotal)) {
		var tpMinimoFretePeso = getElementValue("tpIndicadorMinFretePeso");
		var tpFretePeso = getElementValue("tpIndicadorFretePeso");
		var tpTarifaMinima = getElementValue("tpTarifaMinima");
		var tpTarifaEspecifica = getElementValue("tpIndicVlrTblEspecifica");
		var tpAdvalorem1 = getElementValue("tpIndicadorAdvalorem");
		var tpAdvalorem2 = getElementValue("tpIndicadorAdvalorem2");
		var tpValorReferencia = getElementValue("tpIndicadorValorReferencia");
		var tpPercentualGris = getElementValue("tpIndicadorPercentualGris");
		var tpMinimoGris = getElementValue("tpIndicadorMinimoGris");
		var tpPercentualTrt = getElementValue("tpIndicadorPercentualTrt");
		var tpMinimoTrt = getElementValue("tpIndicadorMinimoTrt");
		var tpMinimoTde = getElementValue("tpIndicadorMinimoTde");
		var tpPercentualTde = getElementValue("tpIndicadorPercentualTde");
		var tpPedagio = getElementValue("tpIndicadorPedagio");

		if(!isTabela(tpMinimoFretePeso) || !isTabela(tpFretePeso) || 
			!isTabela(tpTarifaMinima) || !isTabela(tpTarifaEspecifica) || 
			!isTabela(tpAdvalorem1) || !isTabela(tpAdvalorem2) || 
			!isTabela(tpValorReferencia) || !isTabela(tpPercentualGris) || 
			!isTabela(tpMinimoGris) || !isTabela(tpPercentualTrt) || 
			!isTabela(tpMinimoTrt) || !isTabela(tpMinimoTde) ||
			!isTabela(tpPercentualTde) || !isTabela(tpPedagio)) {
			alertI18nMessage("LMS-01052");
			return false;
		}

		var vlMinimoFretePeso = getCurrentValue("vlMinFretePeso");
		var vlTarifaMinima = getCurrentValue("vlTarifaMinima");
		var vlTarifaEspecifica = getCurrentValue("vlTblEspecifica");
		var vlPercentualFretePercentual = getCurrentValue("pcFretePercentual");
		if(!eqZero(vlMinimoFretePeso) || !eqZero(vlTarifaMinima) || !eqZero(vlTarifaEspecifica) || !eqZero(vlPercentualFretePercentual)) {
			alertI18nMessage("LMS-01052");
			return false;
		}

		if (pcDescontoFreteTotal > limiteDesconto.pcLimiteDescontoTotalFrete) {
			alertI18nMessage("LMS-01060", field.label, false);
			return false;
		}
	}
	return true;
}

function validateVlPercentualCobrancaReentrega() {
	var field = getElement("pcCobrancaReentrega");
	var valor = getCurrentValue("pcCobrancaReentrega");
	if(!isPercent(valor)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return true;
}

function validateVlPercentualCobrancaDevolucoes() {
	var field = getElement("pcCobrancaDevolucoes");
	var valor = getCurrentValue("pcCobrancaDevolucoes");
	if(!isPercent(valor)) {
		alertI18nMessage("LMS-01050", field.label, false);
		return false;
	}
	return true;
}

function validateGrupoTotalFrete() {
	return validateVlPercentualDescontoFreteTotal() &&
			validateVlPercentualCobrancaReentrega() &&
			validateVlPercentualCobrancaDevolucoes();
}

function disableGrupoTotalFrete(bool) {
	setDisabled("pcDescontoFreteTotal", bool);
	setDisabled("pcCobrancaReentrega", bool);
	setDisabled("pcCobrancaDevolucoes", bool);
	disableWorkflow();
}

function verifyIndicador(comboName, relatedTextName, flagName) {
	var flagValue = false;
	if(flagName != undefined) {
		flagValue = getElementValue(flagName);
	}
	if(flagValue==false || flagValue=="false") {
		var comboValue = getElementValue(comboName);
		if(comboValue != '' && isTabela(comboValue)) {
			setFormattedValue(relatedTextName, "0");
			setDisabled(relatedTextName, true);
		} else {
			setDisabled(relatedTextName, false);
		}
	}
}

function setFormattedValue(field, value) {
	setElementValue(field, setFormat(field, value));
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