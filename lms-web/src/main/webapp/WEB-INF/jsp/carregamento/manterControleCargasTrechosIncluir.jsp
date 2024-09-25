<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.carregamento.manterControleCargasAction" onPageLoadCallBack="retornoCarregaPagina">

	<adsm:form action="/carregamento/manterControleCargas">
		<adsm:hidden property="idControleCarga" serializable="true" />
		<adsm:hidden property="flagInclusaoTrecho" value="true" serializable="false" />

		<adsm:lookup label="novaFilial" dataType="text" 
					 property="filial"
				 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.carregamento.manterControleCargasAction.findLookupFilial" 
					 action="/municipios/manterFiliais" 
					 onchange="return novaFilial_OnChange()"
					 onDataLoadCallBack="retornoNovaFilial"
					 onPopupSetValue="popupNovaFilial"
					 size="3" maxLength="3" labelWidth="18%" width="82%" required="true" >
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />
		</adsm:lookup>


		<adsm:hidden property="filialAnteriorRota.sgFilial" />
		<adsm:hidden property="filialAnteriorRota.nrOrdem" />
		<adsm:hidden property="idFilialRotaCcAnterior" />

		<adsm:combobox label="filialAnteriorRota" 
					   property="filialAnteriorRota.idFilial"
					   service="lms.carregamento.manterControleCargasAction.findFilialRotaCc"
					   optionProperty="filial.idFilial" optionLabelProperty="filial.sgFilialConcatenado"
					   onchange="return filialAnteriorRota_OnChange(this)"
					   boxWidth="230" labelWidth="18%" width="82%" required="true" disabled="true" >
			<adsm:propertyMapping modelProperty="idControleCarga" criteriaProperty="idControleCarga" />
			<adsm:propertyMapping modelProperty="flagInclusaoTrecho" criteriaProperty="flagInclusaoTrecho" />
			<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="filialAnteriorRota.sgFilial" />
			<adsm:propertyMapping modelProperty="nrOrdem" relatedProperty="filialAnteriorRota.nrOrdem" />
			<adsm:propertyMapping modelProperty="idFilialRotaCc" relatedProperty="idFilialRotaCcAnterior" />
		</adsm:combobox>

		
		<adsm:hidden property="filialPosteriorRota.idFilial" />
		<adsm:hidden property="filialPosteriorRota.nrOrdem" serializable="false" />
		<adsm:textbox label="filialPosteriorRota" dataType="text" property="filialPosteriorRota.sgFilial" 
					  size="3" labelWidth="18%" width="82%" disabled="true" >
			<adsm:textbox dataType="text" property="filialPosteriorRota.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:textbox label="trecho1" dataType="text" property="trecho1" labelWidth="18%" width="82%" disabled="true" />

		<adsm:textbox label="dataHoraSaida" dataType="JTDateTimeZone" property="dhSaida1" labelWidth="18%" width="32%" 
					  required="true" onchange="return dhSaida1_OnChange()" />

		<adsm:textbox label="tempoViagem" dataType="JTTime" property="nrTempoViagem1" mask="hhh:mm" size="6" maxLength="6" labelWidth="18%" width="32%" required="true" />


		<adsm:textbox label="trecho2" dataType="text" property="trecho2" labelWidth="18%" width="82%" disabled="true" />

		<adsm:textbox label="dataHoraSaida" dataType="JTDateTimeZone" property="dhSaida2" labelWidth="18%" width="32%" 
					  required="true" onchange="return dhSaida2_OnChange(this)" />

		<adsm:textbox label="tempoViagem" dataType="JTTime" property="nrTempoViagem2" mask="hhh:mm" size="6" maxLength="6" labelWidth="18%" width="32%" required="true" />

		<script>
			var labelDataHoraSaidaPrevista = '<adsm:label key="dataHoraSaidaPrevista"/>';
			var LMS_05162 = "<adsm:label key='LMS-05162'/>";
		</script>


		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="salvar" id="botaoSalvar" onclick="salvar_onClick(this.form);" disabled="false" />
			<adsm:button caption="limpar" id="botaoLimpar" onclick="limpar_onClick(this.form);" disabled="false" />
		</adsm:buttonBar>
	</adsm:form>


	<adsm:grid property="controleTrechos" idProperty="idControleTrecho" 
			   selectionMode="none" gridHeight="230" showPagging="false" scrollBars="vertical" 
			   service="lms.carregamento.manterControleCargasAction.findPaginatedControleTrechoByFilialRotaCc"
			   rowCountService=""
			   onRowClick="trechos_OnClick" 
			   onDataLoadCallBack="retornoGrid"
			   onValidate="validateTrechos"
			   autoSearch="false"
			   >
		<adsm:gridColumn title="origem" 				property="filialByIdFilialOrigem.sgFilial" width="190" />
		<adsm:gridColumn title="destino" 				property="filialByIdFilialDestino.sgFilial" width="190" />
		<adsm:editColumn title="dataHoraSaidaPrevista" 	property="dhPrevisaoSaida" dataType="JTDateTimeZone" field="textbox" width="200" mask="dd/MM/yyyy HH:mm" />
		<adsm:gridColumn title="tempoViagem" 			property="hrTempoViagem" dataType="JTTime" width="" align="center"/>
		<adsm:buttonBar> 
			<adsm:button id="botaoSalvarGrid" caption="salvar" onclick="javascript:salvaGrid_onClick(this.form)" />
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

<script>
function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		povoaDadosMaster();
		notifyElementListeners({e:document.getElementById("idControleCarga")});
		disabledCamposByFilialAnterior();
	}
}

function povoaDadosMaster() {
	setElementValue("idControleCarga", dialogArguments.window.document.getElementById('idControleCarga').value);
}


function filialAnteriorRota_OnChange(combo) {
	var r = comboboxChange({e:combo});
	if (getElementValue('filialAnteriorRota.idFilial') != "") {
		var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.findDadosFilialRotaCc", "resultado_findDadosFilialRotaCc", 
			{idFilialAnterior:getElementValue("filialAnteriorRota.idFilial"),
			 idFilialNova:getElementValue("filial.idFilial"),
			 idControleCarga:getElementValue("idControleCarga"),
			 idFilialRotaCcAnterior:getElementValue("idFilialRotaCcAnterior")});
	   	xmit({serviceDataObjects:[sdo]});
	}
	else {
		resetCamposByFilialAnterior();
	}
	return r;
}


function resultado_findDadosFilialRotaCc_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue("filialPosteriorRota.idFilial", getNestedBeanPropertyValue(data, "filialPosteriorRota.idFilial"));
	setElementValue("filialPosteriorRota.sgFilial", getNestedBeanPropertyValue(data, "filialPosteriorRota.sgFilial"));
	setElementValue("filialPosteriorRota.pessoa.nmFantasia", getNestedBeanPropertyValue(data,"filialPosteriorRota.pessoa.nmFantasia"));
	setElementValue("trecho1", getElementValue("filialAnteriorRota.sgFilial") + " - " + getElementValue("filial.sgFilial"));
	setElementValue("filialPosteriorRota.nrOrdem", getNestedBeanPropertyValue(data, "filialPosteriorRota.nrOrdem"));
	setElementValue("trecho2", getElementValue("filial.sgFilial") + " - " + getElementValue("filialPosteriorRota.sgFilial"));
	resetValue("dhSaida1");
	resetValue("nrTempoViagem2");
	resetValue("dhSaida2");

	var dhPrevisaoSaida = getNestedBeanPropertyValue(data,"dhPrevisaoSaida");
	if (dhPrevisaoSaida != undefined && dhPrevisaoSaida != "") {
		setElementValue("dhSaida1", setFormat(document.getElementById("dhSaida1"), dhPrevisaoSaida));
		setDisabled("dhSaida1", false);
		setDisabled("dhSaida2", false);
	}
	else 
		setDisabled("dhSaida1", false);
	
	setDisabled("nrTempoViagem1", false);
	setDisabled("nrTempoViagem2", false);
	setDisabled("dhSaida2", false);

	setElementValue("nrTempoViagem1", getNestedBeanPropertyValue(data, "nrTempoViagem1"));
	setElementValue("nrTempoViagem2", getNestedBeanPropertyValue(data, "nrTempoViagem2"));
}



function dhSaida1_OnChange() {
	if (getElementValue('dhSaida1') != "") {
		var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.validateDadosDhSaida1", "retorno_validateDadosDhSaida1", 
			{dhSaida1:getElementValue("dhSaida1"),
			 idControleCarga:getElementValue("idControleCarga"),
			 idFilialAnterior:getElementValue("filialAnteriorRota.idFilial")});
	   	xmit({serviceDataObjects:[sdo]});
	}
	else
		setDisabled("dhSaida2", true);

	return true;
}

function retorno_validateDadosDhSaida1_cb(data, error) {
	if (error != undefined) {
		alert(error);
		resetValue("dhSaida1");
		setFocus(document.getElementById("dhSaida1"));
		return false;
	}
	setDisabled("dhSaida2", false);
}


function dhSaida2_OnChange(valor) {
	if (onBeforeDeactivateElement(valor, undefined) == true && getElementValue('dhSaida2') != "") {
		var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.validateDadosDhSaida2", "retorno_validateDadosDhSaida2", 
			{dhSaida1:getElementValue("dhSaida1"),
			 dhSaida2:getElementValue("dhSaida2"),
			 idControleCarga:getElementValue("idControleCarga"),
			 idFilialPosterior:getElementValue("filialPosteriorRota.idFilial")});
	   	xmit({serviceDataObjects:[sdo]});
	}
	return true;
}

function retorno_validateDadosDhSaida2_cb(data, error) {
	if (error != undefined) {
		alert(error);
		resetValue("dhSaida2");
		setFocus(document.getElementById("dhSaida2"));
		return false;
	}
}



function resetCamposByFilialAnterior() {
	resetValue("filialPosteriorRota.idFilial");
	resetValue("filialPosteriorRota.sgFilial");
	resetValue("filialPosteriorRota.pessoa.nmFantasia");
	resetValue("trecho1");
	resetValue("trecho2");
	resetValue("dhSaida1");
	resetValue("nrTempoViagem1");
	resetValue("dhSaida2");
	resetValue("nrTempoViagem2");
}


function disabledCamposByFilialAnterior() {
	setDisabled("dhSaida1", true);
	setDisabled("nrTempoViagem1", true);
	setDisabled("dhSaida2", true);
	setDisabled("nrTempoViagem2", true);
	resetValue("dhSaida2");
	resetValue("nrTempoViagem2");
}


function novaFilial_OnChange() {
	var r = filial_sgFilialOnChangeHandler();
	setElementValue('filialAnteriorRota.idFilial', document.getElementById('filialAnteriorRota.idFilial').options[0].value);
	resetCamposByFilialAnterior();
	disabledCamposByFilialAnterior();
	if (getElementValue("filial.sgFilial") == "") {
		setDisabled("filialAnteriorRota.idFilial", true);
	}
	return r;
}

function retornoNovaFilial_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = filial_sgFilial_exactMatch_cb(data)
	if (r == true) {
		callBackNovaFilial(getNestedBeanPropertyValue(data,"0:idFilial"));
	}
}


function popupNovaFilial(data) {
	callBackNovaFilial(getNestedBeanPropertyValue(data,"idFilial"));
}


function callBackNovaFilial(idFilial) {
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.validateFilialNaRota", 
		"retornoCallBackNovaFilial", {idFilial:idFilial,idControleCarga:getElementValue("idControleCarga")});
    xmit({serviceDataObjects:[sdo]});
}


function retornoCallBackNovaFilial_cb(data, error) {
	if (error != undefined) {
		alert(error);
		resetValue("filial.idFilial");
		setDisabled("filialAnteriorRota.idFilial", true);
		setFocus(document.getElementById("filial.sgFilial"));
		return false;
	}
	setDisabled("filialAnteriorRota.idFilial", false);
}


function salvar_onClick(form) {
	if (!validateForm(form)) {
		return false;
	}
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.storeIncluirTrecho", 
		"retornoStoreIncluirTrecho", buildFormBeanFromForm(form));
    xmit({serviceDataObjects:[sdo]});
}


function retornoStoreIncluirTrecho_cb(data, error) {
	if (error != undefined) {
		alert(error);
		window.close();
		return false;
	}
	povoaGrid();
	limpar_onClick(document.forms[0]);
	showSuccessMessage();

	var tabDet = getTabGroup(dialogArguments.window.document).getTab("cad");
	tabDet.tabOwnerFrame.window.setElementValue("hrTempoViagem", getNestedBeanPropertyValue(data, "hrTempoViagem"));

	var blNecessitaCartaoPedagio = getNestedBeanPropertyValue(data, "blNecessitaCartaoPedagio");
	if (blNecessitaCartaoPedagio != undefined && blNecessitaCartaoPedagio == "true") {
		alert(LMS_05162);
	}
}


function limpar_onClick(form) {
	resetFormValue(form);
	controleTrechosGridDef.resetGrid();
	setDisabled("filialAnteriorRota.idFilial", true);
	disabledCamposByFilialAnterior();
	povoaDadosMaster();
	povoaComboFilialAnterior();
	setFocusOnFirstFocusableField();
}


function povoaComboFilialAnterior() {
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.findFilialRotaCc", 
		"filialAnteriorRota.idFilial", {idControleCarga:getElementValue("idControleCarga")});
    xmit({serviceDataObjects:[sdo]});
}


function trechos_OnClick(id) {
	return false;
}


function salvaGrid_onClick(form) {
	if (!validateForm(form)) {
		return false;
	}
	var data = editGridFormBean(null, form);
	setNestedBeanPropertyValue(data, "idControleCarga", getElementValue("idControleCarga"));

	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.storeControleTrechoByFilialRotaCc", 
		"retornoStoreGridControleTrecho", data);
   	xmit({serviceDataObjects:[sdo]});
}


function retornoStoreGridControleTrecho_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	window.close();
}


function povoaGrid() {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "idControleCarga", getElementValue("idControleCarga"));
    setNestedBeanPropertyValue(filtro, "nrOrdemPosterior", getElementValue("filialPosteriorRota.nrOrdem"));
    controleTrechosGridDef.executeSearch(filtro);
    return false;
}


function retornoGrid_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	var gridDef = document.getElementById("controleTrechos.dataTable").gridDefinition;
	for(var i = 0; i < gridDef.currentRowCount; i++) {
		document.getElementById("controleTrechos:" + i + ".dhPrevisaoSaida").required="true";
		document.getElementById("controleTrechos:" + i + ".dhPrevisaoSaida").label=labelDataHoraSaidaPrevista;
	}
}


var idSelecionadoTrecho;
function validateTrechos(rowIndex, columnName, objCell) {
	var gridDef = document.getElementById("controleTrechos.dataTable").gridDefinition;
	if (columnName == "dhPrevisaoSaida") {
		if (objCell.value != "") {
			var idOperadora = getElementValue("controleTrechos:" + rowIndex + ".dhPrevisaoSaida");
			idSelecionadoTrecho = rowIndex;
		}
	}
	return true;
}
</script>