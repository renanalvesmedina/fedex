<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.simularNovosPrecosTaxasAction">
	<adsm:form 
		action="/tabelaPrecos/simularNovosPrecos"
		idProperty="idValorTaxa"
		onDataLoadCallBack="formDataLoad">
		<adsm:i18nLabels>
			<adsm:include key="LMS-02067" />
		</adsm:i18nLabels>
		
		<adsm:hidden property="tabelaBase.idTabelaPreco" />
		<adsm:hidden property="tabelaPreco.idTabelaPreco" />
		<adsm:hidden property="_edit" />

		<adsm:complement
			label="tabelaBase"
			labelWidth="20%"
			width="38%">
			<adsm:textbox 
				property="tabelaBase.tabelaPrecoString"
				dataType="text"
				size="5"
				maxLength="5"
				disabled="true"/>
			<adsm:textbox 
				property="tabelaBase.dsDescricao"
				dataType="text"
				size="30"
				maxLength="30"
				disabled="true"/>
		</adsm:complement>

		<adsm:textbox
			property="tabelaBase.moeda.dsMoeda"
			dataType="text"
			size="18"
			disabled="true"
			serializable="false"
			label="moeda"
			labelWidth="24%"
			width="18%"/>

		<adsm:combobox
			property="taxa.idTabelaPrecoParcela"
			label="taxa"
			optionLabelProperty="nmParcelaPreco"
			optionProperty="idTabelaPrecoParcela"
			autoLoad="false"
			onchange="return changeTaxa(false);"
			required="true"
			labelWidth="20%"
			width="38%"/>

		<adsm:textbox 
			property="psTaxado"
			label="peso"
			mask="#,###,###,###,##0.000"
			dataType="decimal" 
			size="18" 
			maxLength="18" 
			labelWidth="24%" 
			width="18%" 
			unit="kg"
			disabled="true"/>

		<adsm:textbox 
			property="vlTaxa"
			label="valorAtual"
			dataType="currency" 
			size="18" 
			maxLength="18" 
			disabled="true" 
			labelWidth="20%" 
			width="38%"/>

		<adsm:textbox 
			property="vlTaxaReajustado"
			label="valorReajustado"
			dataType="currency"
			required="true"
			size="18"
			maxLength="18"
			labelWidth="24%"
			width="18%"/>

		<adsm:textbox 
			property="vlExcedente" 
			label="valorExcedenteAtual" 
			dataType="currency" 
			size="18" 
			maxLength="18" 
			disabled="true" 
			labelWidth="20%" 
			width="38%"/>

		<adsm:textbox 
			property="vlExcedenteReajustado"
			label="valorExcedenteReajustado"
			dataType="currency" 
			size="18" 
			maxLength="18" 
			labelWidth="24%" 
			width="18%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton callbackProperty="afterStore"/>
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid 
		idProperty="idValorTaxa"
		property="taxasList" 
		showPagging="true"	
		gridHeight="200" 
		unique="true"
		detailFrameName="taxas"
		autoSearch="false"
		onSelectAll="taxasSelectAll"
		onSelectRow="taxasSelectRow"
		onDataLoadCallBack="taxasDataLoad">
		
		<adsm:gridColumn 
			property="parcelaPreco.dsParcelaPreco"
			title="taxa"
			width="25%"/>
			
		<adsm:gridColumn 
			property="valorTaxa.vlTaxa"
			title="valorReajustado"
			dataType="currency"
			width="25%" 
			align="right"/>
			
		<adsm:gridColumn 
			property="valorTaxa.psTaxado"
			title="peso"
			dataType="decimal"
			mask="#,###,###,###,##0.000"
			width="25%" 
			unit="kg" 
			align="right"/>
			
		<adsm:gridColumn 
			property="valorTaxa.vlExcedente"
			title="valorExcedente"
			dataType="currency"
			width="25%" 
			align="right"/>
		
		<adsm:buttonBar>
			<adsm:button
				id="btnExcluir"
				onclick="return clickExcluir();"
				caption="excluir"
				disabled="true" />
		</adsm:buttonBar>
		
	</adsm:grid>
	
</adsm:window>
<script type="text/javascript">
<!--
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		copyDadosFromReajuste();
		loadTaxas();
	} else if (eventObj.name == "cleanButton_click") {
		copyDadosFromReajuste();
	}
	setElementValue("_edit", "");
}

function hide() {
	cleanButtonScript(document, false);
	taxasListGridDef.resetGrid();
	tab_onHide();
	return true;
}

function formDataLoad_cb(data, errorMessage, errorCode, eventObj) {
	onDataLoad_cb(data, errorMessage, errorCode, eventObj);
	copyDadosFromReajuste();
	changeTaxa(false);
	setElementValue("_edit", "true");
}

function loadTaxas() {
	var service = "lms.tabelaprecos.simularNovosPrecosTaxasAction.findTaxasByTabela";
	var sdo = createServiceDataObject(service, "loadTaxas", {id:getElementValue("tabelaBase.idTabelaPreco")});
	xmit({serviceDataObjects:[sdo]});
}

function loadTaxas_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	taxa_idTabelaPrecoParcela_cb(data);
	populaGrid();
}

function populaGrid() {
	taxasListGridDef.executeSearch(buildFormBeanFromForm(document.forms[0]), true);
}

function changeTaxa(resetValues) {
	var idTabelaPrecoParcela = getElementValue("taxa.idTabelaPrecoParcela");
	if (idTabelaPrecoParcela != "") {
		var data = {
			taxa : {
				idTabelaPrecoParcela : idTabelaPrecoParcela
			},
			tabelaBase : {
				idTabelaPreco : getElementValue("tabelaBase.idTabelaPreco")
			},
			resetValues : resetValues
		}
		var service = "lms.tabelaprecos.simularNovosPrecosTaxasAction.findValoresTaxa";
		var sdo = createServiceDataObject(service, "changeTaxa", data);
		xmit({serviceDataObjects:[sdo]});
	} else {
		setElementValue("psTaxado", "");
		setElementValue("vlTaxa", "");
		setElementValue("vlExcedente", "");
	}
}

function changeTaxa_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue("psTaxado", setFormat("psTaxado", data.psTaxado));
	setElementValue("vlTaxa", setFormat("vlTaxa", data.vlTaxa));
	setElementValue("vlExcedente", setFormat("vlExcedente", data.vlExcedente));
	if (data.resetValues == "true") {
		setElementValue("vlTaxaReajustado", "");
		setElementValue("vlExcedenteReajustado", "");
	}
}

function clickExcluir() {
	if(!confirmI18nMessage("LMS-02067")) {
		return;
	}
	var data = {
		tabelaPreco : {
			idTabelaPreco : getElementValue("tabelaPreco.idTabelaPreco")
		},
		ids : taxasListGridDef.getSelectedIds().ids
	}
	
	var service = "lms.tabelaprecos.simularNovosPrecosTaxasAction.removeTaxas";
	var sdo = createServiceDataObject(service, "excluir", data);
	xmit({serviceDataObjects:[sdo]});
}

function excluir_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	populaGrid();
	cleanButtonScript();
}

function taxasSelectAll(estado) {
	changeExcluirStatus(!estado);
}

function taxasSelectRow(rowRef) {
	var gridData = taxasListGridDef.getSelectedIds();
	changeExcluirStatus(gridData.ids.length <= 0);
}

function taxasDataLoad_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	changeExcluirStatus(true);
}

function changeExcluirStatus(status) {
	setDisabled("btnExcluir", status);
}

function copyDadosFromReajuste() {
	var reajuste = parent.document.frames["reaj_iframe"];
	var dados = reajuste.getDadosFromReajuste();
	ajustaDados(dados);
	
	if (dados.tabelaPreco.idTabelaPreco != "") {
		setDisabled(document, true);
	} else {
		enableFields();
	}
}

function ajustaDados(data) {
	setElementValue("tabelaPreco.idTabelaPreco", data.tabelaPreco.idTabelaPreco);
	setElementValue("tabelaBase.idTabelaPreco", data.tabelaBase.idTabelaPreco);
	setElementValue("tabelaBase.tabelaPrecoString", data.tabelaBase.tabelaPrecoString);
	setElementValue("tabelaBase.dsDescricao", data.tabelaBase.dsDescricao);
	setElementValue("tabelaBase.moeda.dsMoeda", data.tabelaBase.moeda.dsMoeda);
}

function afterStore_cb(data, errorMsg, errorKey, showErrorAlert, eventObj) {
	if (errorMsg != undefined) {
		alert(errorMsg);
		return false;
	}
	store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);
	populaGrid();
	setElementValue("_edit", "true");
	cleanButtonScript();
}

function enableFields() {
	setDisabled(document, false);
	setDisabled("tabelaBase.tabelaPrecoString", true);
	setDisabled("tabelaBase.dsDescricao", true);
	setDisabled("tabelaBase.moeda.dsMoeda", true);
	setDisabled("vlExcedente", true);
	setDisabled("vlTaxa", true);
	setDisabled("psTaxado", true);
	setDisabled("btnExcluir", true);
}
//-->
</script>