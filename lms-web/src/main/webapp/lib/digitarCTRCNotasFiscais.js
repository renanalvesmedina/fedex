function initWindow(eventObj) {
	var event = eventObj.name;
	if(event == "tab_click"){
		initGrid();
	} 
}

function setMaxNotas(nrNotas) {
	var gridDef = document.getElementById("notaFiscalConhecimento.dataTable").gridDefinition;
	var max = gridDef.maxRows;
	gridDef.setMaxRows(nrNotas);
	if(nrNotas > max) {
		var vl = getElementValue(document.getElementById("notaFiscalConhecimento" + ":" + (gridDef.currentRowCount - 1) + "." + "nrNotaFiscal"));
		if(vl != "" && vl != undefined)
			gridDef.insertRow();
	}
}

function desabilitaFronteiraRapida(t) {
	var gridDef = document.getElementById("notaFiscalConhecimento.dataTable").gridDefinition;
	if(t && t != "0") {
		gridDef.setDisabledColumn("nrCfop", false);
		gridDef.setDisabledColumn("vlIcms", false);
		gridDef.setDisabledColumn("vlBaseCalculo", false);
	} else {
		gridDef.setDisabledColumn("nrCfop", true);
		gridDef.setDisabledColumn("vlIcms", true);
		gridDef.setDisabledColumn("vlBaseCalculo", true);
	}
	if(gridDef.currentRowCount == gridDef.maxRows) {
		setDisabled("confirmarDadosButton", false);
	}
}

function validateNotas(rowIndex, columnName, objCell) {
	var fieldValue = getElementValue(objCell);
	if(isBlank(fieldValue)) {
		return true;
	}
	var gridDef = document.getElementById("notaFiscalConhecimento.dataTable").gridDefinition;
	if(columnName == "nrNotaFiscal") {
		var nrNota = stringToNumber(fieldValue);
		if(nrNota < 1 || nrNota > 100000000) {
			alertI18nMessage("LMS-04017");
			resetValue(objCell);
			return false;
		}
		for(var i = 0; i < gridDef.currentRowCount; i++) {
			if(i != rowIndex) {
				if(nrNota == stringToNumber(getElementValue("notaFiscalConhecimento" + ":" + i + "." + "nrNotaFiscal"))) {
					setElementValue(objCell, "");
					alertI18nMessage("LMS-04018");
					return false;
				}
			}
		}
		if(gridDef.currentRowCount == gridDef.maxRows) {
			setDisabled("confirmarDadosButton", false);
		}
	}
	if(columnName == "qtVolumes") {
		var vols = stringToNumber(fieldValue);
		if((rowIndex == 0 && vols < 1) || vols < 0) {
			alertI18nMessage("LMS-04021", new Array("qtdeVolumes"));
			return false;
		}
	}
	if(columnName == "psMercadoria") {
		var ps = stringToNumber(fieldValue);
		if ((rowIndex == 0 && ps <= 0) || ps < 0) {
			alertI18nMessage("LMS-04021", new Array("peso"));
			return false;
		}
	} 
	if(columnName == "vlTotal") {
		var vl = stringToNumber(fieldValue);
		if (vl <= 0) {
			alertI18nMessage("LMS-04021", new Array("valor"));
			return false;
		}
	}
	if(objCell.disabled == false) {
		if(columnName == "nrCfop") {
			var cf = stringToNumber(fieldValue);
			if (cf <= 0) {
			alertI18nMessage("LMS-04021", new Array("cfop"));
				return false;
			}
		}
		if(columnName == "vlBaseCalculo") {
			var vl = stringToNumber(fieldValue);
			if (vl < 0) {
				alertI18nMessage("LMS-04021", new Array("baseCalculo"));
				return false;
			}
		}
		if(columnName == "vlIcms") {
			var vl = stringToNumber(fieldValue);
			if (vl < 0) {
				alertI18nMessage("LMS-04021", new Array("icms"));
				return false;
			}
		}
	}
	if(rowIndex > 0) {
		/* Data de Emissao */
		var dtEmissaoValue = getElementValue("notaFiscalConhecimento:" + (rowIndex - 1) + ".dtEmissao");
		var dtEmissao2 = getElement("notaFiscalConhecimento:" + rowIndex + ".dtEmissao");
		if(dtEmissaoValue.length > 0 && getElementValue(dtEmissao2).length < 1) {
			setElementValue(dtEmissao2, setFormat(dtEmissao2, dtEmissaoValue));
		}

		/* Numero CFOP */
		var nrCfopValue = getElementValue("notaFiscalConhecimento:" + (rowIndex - 1) + ".nrCfop");
		var nrCfop2 = getElement("notaFiscalConhecimento:" + rowIndex + ".nrCfop");
		if(nrCfopValue.length > 0 && getElementValue(nrCfop2).length < 1) {
			setElementValue(nrCfop2, nrCfopValue);
		}
	}
	return true;
}

function confirmarDados() {
	var tabGroup = getTabGroup(this.document);
	var tabNotas = tabGroup.getTab("notasFiscais");
	var gridDef = document.getElementById("notaFiscalConhecimento.dataTable").gridDefinition;
	if(comparaGrids(tabNotas, gridDef) == true) {
		storeNotas();
	} 
	return true;
}

function comparaGrids(tabNotas, gridDef) {
	for(var i = 0; i < gridDef.currentRowCount; i++) {
		if(!comparaCampos(tabNotas, "nrNotaFiscal", i, "numero")) {
			return false;
		}
		if(!comparaCampos(tabNotas, "dsSerie", i, "serie")) {
			return false;
		}
		if(!comparaCampos(tabNotas, "dtEmissao", i, "dataEmissao")) {
			return false;
		}
		if(!comparaCampos(tabNotas, "qtVolumes", i, "qtdeVolumes")) {
			return false;
		}
		if(!comparaCampos(tabNotas, "psMercadoria", i, "peso")) {
			return false;
		}
		if(!comparaCampos(tabNotas, "vlTotal", i, "valor")) {
			return false;
		}
		if(!comparaCampos(tabNotas, "nrCfop", i, "cfop")) {
			return false;
		}
		if(!comparaCampos(tabNotas, "vlBaseCalculo", i, "baseCalculo")) {
			return false;
		}
		if(!comparaCampos(tabNotas, "vlIcms", i, "icms")) {
			return false;
		}
	}
	return true;
}

function comparaCampos(tabNotas, campo, linha, label) {
	var nrNotaFiscal = getElementValue(tabNotas.getElementById("notaFiscalConhecimento:" + linha + ".nrNotaFiscal"));
	var obj = document.getElementById("notaFiscalConhecimento:" + linha + "." + campo);
	var v1 = getElementValue(obj);
	var v2 = getElementValue(tabNotas.getElementById("notaFiscalConhecimento:" + linha + "." + campo));
	if(v1 != v2) {
		alertI18nMessage("LMS-04034", new Array(i18NLabel.getLabel(label), nrNotaFiscal, linha+1), false);
		setFocus(obj, false);
		return false;
	}
	return true;
}

