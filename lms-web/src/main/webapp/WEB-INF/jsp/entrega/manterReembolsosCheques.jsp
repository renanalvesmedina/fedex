<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.entrega.manterReembolsosAction">
	<adsm:form action="/entrega/manterReembolsos" >

	<adsm:i18nLabels>
		<adsm:include key="LMS-09021"/>
		<adsm:include key="LMS-09022"/>
		<adsm:include key="LMS-09005"/>
	</adsm:i18nLabels>

	<adsm:textbox dataType="text" property="filial.sgFilial" label="filial" labelWidth="18%" width="32%" disabled="true" size="3">
	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" />
	</adsm:textbox>

	<adsm:textbox dataType="integer" property="nrReciboReembolso" label="numeroReembolso" maxLength="8" size="16" labelWidth="18%" width="32%" disabled="true" mask="00000000"/>
	<adsm:textbox dataType="JTDate" property="dhEmissaoReciboReembolso" label="dataEmissao" maxLength="8" size="16" labelWidth="18%" width="32%" disabled="true"/>
	<adsm:textbox dataType="currency" property="vlReembolso" label="valorReembolso" size="16" labelWidth="18%" width="32%" disabled="true" />	
		<adsm:label key="branco" style="border:none;height:25px;" width="100%"/>
		<adsm:hidden property="pesquisa" value="true"/>
	</adsm:form>

	<adsm:grid
		property="chequeReembolso"
		idProperty="idChequeReembolso" 
		rows="8"
		unique="true"
		gridHeight="280" 
		onValidate="validaBancoAgencia" 
		scrollBars="vertical"
		showPagging="false" 
		service="lms.entrega.manterReembolsosAction.findCheques"
		onRowClick="returnFalse();"
		onPopulateRow="sobrescreveOnkeydown"
		onInsertRow="onInsertRowGrid"
		onSelectRow="enableExcluirCheques(false);"
		onSelectAll="enableExcluirCheques(true);"
		onDataLoadCallBack="verificaRegistros"
	>
		<adsm:editColumn property="nrBanco" maxLength="3" dataType="integer" field="textBox" title="banco" width="100" align="center" />
		<adsm:editColumn property="nrAgencia" maxLength="4" dataType="integer" field="textBox" title="agencia" width="100" align="center"/>
		<adsm:editColumn property="dvAgencia" maxLength="2" dataType="text" field="textBox" title="" width="30" align="center" />

		<adsm:editColumn property="nrCheque" maxLength="8" dataType="integer" field="textBox" title="cheque" width="150" align="center"/>
		<adsm:editColumn property="valorCheque" dataType="currency" field="textBox" title="valor" align="center" width="150" />
		<adsm:editColumn property="data" dataType="JTDate" field="textBox" title="data" width="150" align="center"/>
		<adsm:buttonBar>
			<adsm:button
				buttonType="storeButton"
				caption="salvarTudo"
				onclick="onStoreCheques();"
				id="storeButton"
			/>
			<adsm:button
				caption="excluirCheques"
				onclick="deleteRowsGrid();"
				id="removeButton"
			/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
<script type="text/javascript">
function sobrescreveOnkeydown(tr) {
	chequeReembolsoGridDef.getCellObject(chequeReembolsoGridDef.currentRowCount - 1, 'data').onkeydown = 'return false;';
}

function onInsertRowGrid() {
	var lastRowIndex = chequeReembolsoGridDef.gridState.rowCount-1;
	var newRowIndex = chequeReembolsoGridDef.gridState.rowCount;

	var elementBanco = document.getElementById("chequeReembolso:" + lastRowIndex + "." + "nrBanco");
	var elementAgencia = document.getElementById("chequeReembolso:" + lastRowIndex + "." + "nrAgencia");
	var dvAgencia = document.getElementById("chequeReembolso:" + lastRowIndex + "." + "dvAgencia");

	if (elementBanco != null)
		if(getElementValue(elementBanco)!= "" && getElementValue(elementBanco)!= null) {
			setElementValue("chequeReembolso:" + newRowIndex + "." + "nrBanco", getElementValue(elementBanco));
		}

		if (elementAgencia != null)
			if(getElementValue(elementAgencia)!= "" && getElementValue(elementAgencia)!= null) {
				setElementValue("chequeReembolso:" + newRowIndex + "." + "nrAgencia", getElementValue(elementAgencia));
		}

		if (dvAgencia != null)
			if(getElementValue(dvAgencia)!= "" && getElementValue(dvAgencia)!= null) {
				setElementValue("chequeReembolso:" + newRowIndex + "." + "dvAgencia", getElementValue(dvAgencia));
		}

	habilitaDesabilitaGrid();
	
}

function initWindow(eventObj) {
	if (eventObj.name=="tab_click") {
		var dataId = new Array();
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		var idDoctoServico = tabDet.getFormProperty("idDoctoServico");

		setNestedBeanPropertyValue(dataId,"idDoctoServico",idDoctoServico);
		if(getElementValue("pesquisa")=="true") {
			chequeReembolsoGridDef.executeSearch(dataId);
		}
		habilitaDesabilitaGrid();
	}
}

function habilitaDesabilitaGrid() {	
	var tabGroup = getTabGroup(this.document);
	var tabDet = tabGroup.getTab("cad");
	var tpSituacaoRecibo = tabDet.getFormProperty("tpSituacaoRecibo");
	var documentoMir = tabDet.getFormProperty("documentoMir");
	if(tpSituacaoRecibo != null || tpSituacaoRecibo != "") {
		if( (documentoMir == "true") && (tpSituacaoRecibo != "CR") ) {
			setDisabledForm(document.getElementById("chequeReembolso" + ".form"), true);
			document.getElementById("chequeReembolso"+".chkSelectAll").disabled = true;
		} else {
			setDisabledForm(document.getElementById("chequeReembolso"+".form"), false);
			document.getElementById("chequeReembolso"+".chkSelectAll").disabled = false;
		}
	}
	enableExcluirCheques(false);
}

//função chamada no onDataLoad da grid
function verificaRegistros_cb(data, errorMessage) {
	if(errorMessage) {
		alert(errorMessage);
		return false;
	}
	setElementValue("pesquisa", "false");
	if(!data) {
		chequeReembolsoGridDef.resetGrid();
		chequeReembolsoGridDef.insertRow(chequeReembolsoGridDef.gridState.rowCount);
		var linhaAtual = chequeReembolsoGridDef.gridState.rowCount;
		if(document.getElementById("chequeReembolso"+".chkSelectAll").disabled == false) {
			setFocus(document.getElementById("chequeReembolso:" + linhaAtual + "." + "nrBanco"));
		}
	}
	habilitaDesabilitaGrid();
}

//função chamada no onRowClick da grid
function returnFalse() {
	return false;
}

//função chamada no onValidate da grid
function validaBancoAgencia(rowIndex, columnName, objCell) {
	var gridDef = document.getElementById("chequeReembolso.dataTable").gridDefinition;
	if(columnName == "nrBanco") {
		var nrBanco = getElementValue(objCell);
		if(nrBanco!=undefined && nrBanco!=null && nrBanco != "") {
			_serviceDataObjects = new Array();
			addServiceDataObject(createServiceDataObject("lms.entrega.manterReembolsosAction.validaNrBanco", "verificaExcecao", {nrBanco:nrBanco}));
			xmit();
		}
	}
	if(columnName == "nrAgencia") {
		var nrAgencia = getElementValue(objCell);
		if(nrAgencia !=null && nrAgencia != undefined && nrAgencia != "") {
			var parameters = new Array();
			setNestedBeanPropertyValue(parameters,"nrAgencia", nrAgencia);
			_serviceDataObjects = new Array();
			addServiceDataObject(createServiceDataObject("lms.entrega.manterReembolsosAction.validaNrAgencia", "verificaExcecao", parameters));
			xmit();
		}
	}

	var data = new Array();
	if(verificaCamposPreenchidos(columnName, rowIndex, data)) {
		var mapVerificacao = new Array();

		setNestedBeanPropertyValue(mapVerificacao,"listaCheques",editGridFormBean(undefined, document.forms[1]));
		setNestedBeanPropertyValue(mapVerificacao,"vlReembolso",getElementValue("vlReembolso"));

		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject("lms.entrega.manterReembolsosAction.validaTotalCheques", "verifyTotalCheques", mapVerificacao));
		xmit();
	}	
}
function verifyTotalCheques_cb(data, error, errorKey) {
	if(error) {
		alert(error);
		return false;
	} else {
		var isTotalReembolso = getNestedBeanPropertyValue(data,"_value");
		if(isTotalReembolso == "false") {
			var linhaAtual = chequeReembolsoGridDef.gridState.rowCount;
			chequeReembolsoGridDef.insertRow(chequeReembolsoGridDef.gridState.rowCount);
			if(document.getElementById("chequeReembolso"+".chkSelectAll").disabled == false) {
				setFocus(document.getElementById("chequeReembolso:" + linhaAtual + "." + "nrCheque"));
			}
		}
	}
}

function verificaCamposPreenchidos(columnName, rowIndex, data) {
	for(var columnName in chequeReembolsoGridDef.columns) {
		if(columnName != 'dvAgencia') {
			element = document.getElementById("chequeReembolso:" + rowIndex + "." + chequeReembolsoGridDef.columns[columnName].name);
			if (element.widgetType=="textbox") {
				if(getElementValue(element)== "") {
					return false;
				} 
			}
		}
	}
	// verifica se na ultima linha tem algum campo vazio com excessao dvAgencia, se tiver não insere uma nova linha
	for(var columnName in chequeReembolsoGridDef.columns) {
		if(columnName != 'dvAgencia') {
			rowIndex = chequeReembolsoGridDef.gridState.rowCount-1;
			element = document.getElementById("chequeReembolso:" + rowIndex + "." + chequeReembolsoGridDef.columns[columnName].name);
			if (element.widgetType=="textbox") {
				if(getElementValue(element)== "") {
					return false;
				} 
			}
		}
	}
	return true;
}

function verificaExcecao_cb(data, error, errorKey) {
	if(error) {
		alert(error);
		return false;
	}	

	if(data && data.key) {
		if(data.key == "LMS-09021")
			alert(i18NLabel.getLabel("LMS-09021"));
		if(data.key == "LMS-09022")
			alert(i18NLabel.getLabel("LMS-09022"));	
		return false;
	}
}

//fim das funçoes usadas pelo onValidate

//---------------------------FUNÇÃO DOS BOTOES------------------------------------------------------------------

//funcao chamada no botao store
function onStoreCheques() {
	var tabGroup = getTabGroup(this.document);
	var tabDet = tabGroup.getTab("cad");
	tabDet.tabOwnerFrame.onStoreClick();
}

//função chamada no botao delete

function deleteRowsGrid() {
	var idProperty = "idChequeReembolso";
	var total = chequeReembolsoGridDef.currentRowCount - 1;
	var totRegistros = 0;
	var data = new Array();
	for (var rowIndex = 0; rowIndex <= total; rowIndex++) {
		element = document.getElementById("chequeReembolso:" + rowIndex + "." + chequeReembolsoGridDef.columns[idProperty].name);
		if (element) {
			element = element.checked; //FIXME: deve ser tirado apos corrigir arquitetura
			if(element == false) {
				for(var columnName in chequeReembolsoGridDef.columns) {
					element = document.getElementById("chequeReembolso:" + rowIndex + "." + chequeReembolsoGridDef.columns[columnName].name);
					setNestedBeanPropertyValue(data, ":"+totRegistros+"."+columnName , getElementValue(element));
				}
				var idChequeReembolso = document.getElementById("chequeReembolso:" + rowIndex + "."
						+ chequeReembolsoGridDef.columns["idChequeReembolso"].name).value;
				setNestedBeanPropertyValue(data, ":"+totRegistros+".idChequeReembolso" , idChequeReembolso);
				totRegistros++;
			}
		}
	}

	chequeReembolsoGridDef.resetGrid();

	var table = document.getElementById("chequeReembolso.dataTable");
	if (table.tBodies.length > 0) {
		var tableBody = table.tBodies[0];
	} else {
		var tableBody = document.createElement("tbody");
		table.appendChild(tableBody);
	}
	if(data.length > 0 ) {
		for(var index = 0; index < data.length ; index++) {
			chequeReembolsoGridDef.createRow(new Array(), index , data[index], tableBody, undefined);
		}
	} else if (data.length == 0)
		chequeReembolsoGridDef.insertRow();

	var tabGroup = getTabGroup(document);
	tabGroup.selectedTab.setChanged(true);
}

function myOnShow() {
	var tabGroup = getTabGroup(this.document);	
	setElementValue("filial.pessoa.nmFantasia",tabGroup.getTab('cad').getElementById("filial.pessoa.nmFantasia").value);
	setElementValue("filial.sgFilial",tabGroup.getTab('cad').getElementById("filial.sgFilial").value);
	setElementValue("nrReciboReembolso",tabGroup.getTab('cad').getElementById("nrReciboReembolso").value);
	setElementValue("dhEmissaoReciboReembolso",tabGroup.getTab('cad').getFormProperty("dhEmissaoReciboReembolso"));
	setElementValue("vlReembolso",tabGroup.getTab('cad').getElementById("vlReembolso").value);
	document.getElementById("vlReembolso").dataType = "currency";
	return false; // retorna falso para cancelar o 'executeLastSearch' padrão da arquitetura.
}

function enableExcluirCheques(isChkSelectAll) {
	if (isChkSelectAll) {
		setDisabled("removeButton", getElementValue("chequeReembolso.chkSelectAll") == false);
	} else {
		setDisabled("removeButton", getSelectedRowsCount() <= 0);
	}
}

function getSelectedRowsCount() {
	var idProperty = "idChequeReembolso";
	var total = chequeReembolsoGridDef.currentRowCount - 1;
	var selectedRows = 0;
	for (var rowIndex = 0; rowIndex <= total; rowIndex++) {
		element = document.getElementById("chequeReembolso:" + rowIndex + "." + chequeReembolsoGridDef.columns[idProperty].name);
		if (element) {
			element = element.checked; //FIXME: deve ser tirado apos corrigir arquitetura
			if(element == true) {
				selectedRows++;
			}
		}
	}
	return selectedRows;
}

</script>