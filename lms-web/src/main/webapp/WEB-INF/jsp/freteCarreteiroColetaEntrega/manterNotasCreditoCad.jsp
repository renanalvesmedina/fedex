<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function disableTabAnexos(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("anexo", disabled);
	}

	var changeParcelas = false;
	function dataLoad_cb(data) {
		onDataLoad_cb(data);
		ParcelasGridDef.executeSearch({idNotaCredito:getNestedBeanPropertyValue(data, "idNotaCredito")});
		changeParcelas = false;
		
		defineDisabled();
	}
	
	function defineDisabled(){
		if ("0" == getElementValue("behaviorScreen")
			|| getElementValue("idProcessoWorkflow") != "") {
			setDisabled(document,true);			
		} else if ("1" == getElementValue("behaviorScreen")) {
			setDisabled("vlDescontoSugerido", false);
			setDisabled("vlAcrescimoSugerido", getElementValue("isDIVOP") == "false");
			setDisabled("storeButtom", false);
			enabledDisbaleOb(undefined);
		} else {
			setDisabled("vlDescontoSugerido", true);
			setDisabled("vlAcrescimoSugerido", true);
			setDisabled("obNotaCredito", true);
		}
		setDisabled("emitir", true);
		setDisabled("exportarExcel", true);
		setDisabled("visualizarPrevia", true);
		setDisabled("eventos", false);
		
		if (getElementValue("isExibeBtnNotaCredito") == 'true') {
			setDisabled("controleCarga", false);
			setDisabled("tabelaButton", false);
		}else{
			setDisabled("controleCarga", true);
			setDisabled("tabelaButton", true);
		}
		
		if (getElementValue("idProcessoWorkflow") == "") {			
			setDisabled("emitir", getElementValue("tpProprietario") == "P");
			setDisabled("exportarExcel", getElementValue("tpProprietario") == "P");
			setDisabled("visualizarPrevia", getElementValue("tpProprietario") == "P");
			setDisabled("controleCarga", false);
			setDisabled("tabelaButton", false);
		} else {
			setDisabled("storeButtom", false);
		}
		if (getElementValue("idProcessoWorkFlow") != "") {
			setDisabled("storeButtom", true);
		}
		changeParcelas = false;
		document.getElementById("obNotaCredito").required = "false";
		
		var idNotaCredito = getElementValue("idNotaCredito");
		
		if (idNotaCredito != undefined) {
			disableTabAnexos(false);
		} else {
			disableTabAnexos(true);
		}
				
		if(getElementValue("executedWorkFlow") == 'true'){
			setDisabled("storeButtom",true);
			setDisabled("vlDescontoSugerido", true);
			setDisabled("vlAcrescimoSugerido", true);
		}
	}

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			// Apenas zera form se vier da aba pesq
			if(eventObj.src.tabGroup && eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq"){		
				setDisabled(document,true);
				ParcelasGridDef.resetGrid();
			} 
			
			if(eventObj.src.tabGroup && eventObj.src.tabGroup.oldSelectedTab.properties.id == "anexo"){
				defineDisabled();
			}
		}	
		
		if (eventObj.name == "gridRow_click" && eventObj.name == "storeButton") {
			disableTabAnexos(false);
		}
	}

	function validateGrid(rowIndex, columnName, objCell) {
		var gridDef = document.getElementById("Parcelas.dataTable").gridDefinition;
		if(columnName == "qtde"){
			if(gridDef.gridState.data[rowIndex].tpParcela.value	!= "DH"){
				var qtde = getElementValue(objCell);
				if(qtde!="")
					if(qtde.indexOf(".00")== -1){
						alert(i18NLabel.getLabel("LMS-25053"));
						return false;
					} 
			}	
		}
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.calculeValuesGrid","writeSubTotais",buildFormBeanFromForm(document.forms[1]));
		xmit({serviceDataObjects:[sdo]}); 
		if (getElementValue("idProcessoWorkflow") == "") {
			setDisabled("obNotaCredito",false);
			document.getElementById("obNotaCredito").required = "true";
		}
		changeParcelas = true;
	}

	function writeSubTotais_cb(data) {
		if (data != undefined) {
			var evlParcelas = document.getElementById("vlParcelas");
			setElementValue(evlParcelas, setFormat(evlParcelas, getNestedBeanPropertyValue(data,"vlParcelas")));
			var evlNota = document.getElementById("vlNota");
			setElementValue(evlNota, setFormat(evlNota, getNestedBeanPropertyValue(data,"vlNota")));
			
			if (getNestedBeanPropertyValue(data,"Parcelas") == undefined)
				return;
			var parcelas = getNestedBeanPropertyValue(data,"Parcelas");
			for(var x = 0; x < parcelas.length; x++) {
				var element = document.forms[1].elements["Parcelas:" + x + ".vlTot"];
				setElementValue(element,setFormat(element,getNestedBeanPropertyValue(parcelas[x],"vlTot")));
			}
		}
	}

	function disabledClickGrid() {
		return false;
	}
	
	function onClickStore() {
		var forms = document.forms;
		storeEditGridScript('lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.store', 'storeCustom', forms[0], forms[1]);
	}

	function storeCustom_cb(data,exception) {
		store_cb(data,exception);
		if (getElementValue("idProcessoWorkFlow") != "") {			
			setDisabled("emitir",true);
			setDisabled("exportarExcel",true);
			setDisabled("visualizarPrevia",true);
			setDisabled("tabelaButton",true);
			setDisabled("storeButtom",true);
		} else {
			setDisabled("emitir", getElementValue("tpProprietario") == "P");
			setDisabled("exportarExcel", getElementValue("tpProprietario") == "P");
			setDisabled("visualizarPrevia", getElementValue("tpProprietario") == "P");
			
			var executedWorkFlow = getNestedBeanPropertyValue(data,"executedWorkFlow");
			
			if (exception == undefined && data != undefined && executedWorkFlow == "true"){
				
				// LMS 4901 - Desabilita os campos ao salvar				
				setDisabled("storeButtom",true);
				setDisabled("vlDescontoSugerido", true);
				setDisabled("vlAcrescimoSugerido", true);
								
				alert(i18NLabel.getLabel("LMS-00065"));
			}			
			
			setElementValue("executedWorkFlow", executedWorkFlow);
		}
	}

	function dataLoadGrid_cb(data) {
		// LMS-4901 -- Desabilitar os campos parcelas da nota de crédito, independentemente da situação da nota de crédito
		ParcelasGridDef.setDisabledColumn("vlTot",true);
		ParcelasGridDef.setDisabledColumn("qtde", true);
		ParcelasGridDef.setDisabledColumn("vlUnit", true);
		
		
		// Se a nota de crédito estiver com a situação submetida a aprovação então desabilita os campos e o botão salvar
		if(document.getElementById("notaCreditoTpSituacao").value == "S"){
			setDisabled("vlDescontoSugerido", true);
			setDisabled("vlAcrescimoSugerido", true);
			setDisabled("obNotaCredito", true);
			setDisabled("storeButtom",true);
		}
		
		
	}

	function enabledDisbaleOb(field) {
		if (changeParcelas != true) {
			if (getElementValue("vlDescontoSugerido") == "" && getElementValue("vlAcrescimoSugerido") == "") {
				setDisabled("obNotaCredito",true);
				if (field != undefined)
					resetValue("obNotaCredito");
			}else
				setDisabled("obNotaCredito",false);
		}
		if (field != undefined)
			return onBeforeDeactivateElement(field, undefined);
	}

	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		if (getElementValue("idProcessoWorkflow") != "") {
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
			xmit({serviceDataObjects:[sdo]});
			getTabGroup(this.document).setDisabledTab("cad",false);
			getTabGroup(this.document).selectTab(1);
			getTabGroup(this.document).setDisabledTab("pesq",true);
		}
	}

	function emitirReport(tpViewReport) {
		setElementValue('tpViewReport', tpViewReport);
		executeReportWithCallback('lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction.executeReport', 'emitirReport', document.forms[0]);
	}

	function emitirReport_cb(data,error) {
		if (error == null) {
			if(data.workflow == "true" || getNestedBeanPropertyValue(data,"texto")) {
				alert(data.texto);
			}else{
				openReportWithLocator(data.path);
			}
		} else {
			alert(error);
		}

		if (getElementValue("dhEmissao") == "")
			setElementValue("dhEmissao",setFormat(document.getElementById("dhEmissao"),getNestedBeanPropertyValue(data,"dateTimeNow")));	
	}
	
	function getIdProcessoWorkflow() {
		var url = new URL(parent.location.href);
		return url.parameters["idProcessoWorkflow"];
	}
	
	function consultarControleCargas(){
		if (getIdProcessoWorkflow() != undefined) {
			showModalDialog('carregamento/consultarControleCargas.do?cmd=main'+ buildLinkPropertiesQueryString_controleCarga(),window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
		} else {
			parent.parent.redirectPage('carregamento/consultarControleCargas.do?cmd=main' + buildLinkPropertiesQueryString_controleCarga());
		}
	}
	
	function buildLinkPropertiesQueryString_controleCarga() {
		var qs = "";
		
		if (document.getElementById("idFilial").type == 'checkbox') {
			qs += "&notaCredito.filial.idFilial=" + document.getElementById("idFilial").checked;
		} else {
			qs += "&notaCredito.filial.idFilial=" + encodeString(document.getElementById("idFilial").value);
		}
		
		if (document.getElementById("sgFilial").type == 'checkbox') {
			qs += "&notaCredito.filial.sgFilial=" + document.getElementById("sgFilial").checked;
		} else {
			qs += "&notaCredito.filial.sgFilial=" + encodeString(document.getElementById("sgFilial").value);
		}
		
		if (document.getElementById("idFilial").type == 'checkbox') {
			qs += "&filialByIdFilialOrigem.idFilial=" + document.getElementById("idFilial").checked;
		} else {
			qs += "&filialByIdFilialOrigem.idFilial=" + encodeString(document.getElementById("idFilial").value);
		}
		
		if (document.getElementById("sgFilial").type == 'checkbox') {
			qs += "&filialByIdFilialOrigem.sgFilial=" + document.getElementById("sgFilial").checked;
		} else {
			qs += "&filialByIdFilialOrigem.sgFilial=" + encodeString(document.getElementById("sgFilial").value);
		}
		
		if (document.getElementById("nmFilial").type == 'checkbox') {
			qs += "&filialByIdFilialOrigem.pessoa.nmFantasia=" + document.getElementById("nmFilial").checked;
		} else {
			qs += "&filialByIdFilialOrigem.pessoa.nmFantasia=" + encodeString(document.getElementById("nmFilial").value);
		}
		
		if (document.getElementById("idNotaCredito").type == 'checkbox') {
			qs += "&notaCredito.idNotaCredito=" + document.getElementById("idNotaCredito").checked;
		} else {
			qs += "&notaCredito.idNotaCredito=" + encodeString(document.getElementById("idNotaCredito").value);
		}
		
		if (document.getElementById("nrNotaCredito").type == 'checkbox') {
			qs += "&notaCredito.nrNotaCredito=" + document.getElementById("nrNotaCredito").checked;
		} else {
			qs += "&notaCredito.nrNotaCredito=" + encodeString(document.getElementById("nrNotaCredito").value);
		}
		
		return qs;
	}
	
	function manterTabelasFretesAgendados(){
		if (getIdProcessoWorkflow() != undefined){
			if (getElementValue("tabelaValue") == "E"){ 
				showModalDialog('freteCarreteiroColetaEntrega/manterTabelasFretesEventuais.do?cmd=main&idProcessoWorkflow=' + getElementValue("idTabelaColetaEntrega"),
					window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');  				
			}else{
				showModalDialog('freteCarreteiroColetaEntrega/manterTabelasFretesAgregados.do?cmd=main&idProcessoWorkflow=' + getElementValue("idTabelaColetaEntrega"),
					window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
			}
		}else{
			if (getElementValue("tabelaValue") == "E"){
				parent.parent.redirectPage('freteCarreteiroColetaEntrega/manterTabelasFretesEventuais.do?cmd=main&idProcessoWorkflow=' + getElementValue("idTabelaColetaEntrega"));
			}else{
				parent.parent.redirectPage('freteCarreteiroColetaEntrega/manterTabelasFretesAgregados.do?cmd=main&idProcessoWorkflow=' + getElementValue("idTabelaColetaEntrega"));
			}
		}
	
	}
	
	function exibeEventos() {
		showModalDialog('freteCarreteiroColetaEntrega/consultarEventosNotaCredito.do?cmd=main' ,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}
	
//-->
</script>
<adsm:window service="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="freteCarreteiroColetaEntrega/manterNotasCredito" idProperty="idNotaCredito" onDataLoadCallBack="dataLoad">
		<adsm:i18nLabels>
			<adsm:include key="LMS-25061"/>
			<adsm:include key="LMS-00065"/>
			<adsm:include key="LMS-25053"/>
			<adsm:include key="LMS-25043"/>
			<adsm:include key="LMS-25044"/>
		</adsm:i18nLabels>
		<adsm:hidden property="filial.idFilial"/>
		<adsm:hidden property="idProprietario" serializable="false"/>
		<adsm:hidden property="tpIdentificacaoProprietario" serializable="false"/>
		<adsm:hidden property="idTabelaColetaEntrega" serializable="false"/>
		<adsm:hidden property="tabelaValue" serializable="false"/>
		<adsm:hidden property="idRecibo" serializable="false"/>
		<adsm:hidden property="behaviorScreen" serializable="false"/>
		<adsm:hidden property="isDIVOP" serializable="false"/>
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:hidden property="tpPessoa" serializable="false"/>
		<adsm:hidden property="tpProprietario" serializable="false"/>
		<adsm:hidden property="tpViewReport"/>
		<adsm:hidden property="nrControleCarga" serializable="false" />
		<adsm:hidden property="isExibeBtnNotaCredito"/>
		<adsm:hidden property="notaCreditoTpSituacao"/>
		<adsm:hidden property="executedWorkFlow" serializable="false"/>		

		<adsm:textbox dataType="text" property="sgFilial" label = "filial" size="3" maxLength="10" labelWidth="18%" width="5%" disabled="true"/>
		<adsm:textbox dataType="text" property="nmFilial" size="24" maxLength="50" disabled="true" width="27%" required="true"/>
		<adsm:textbox dataType="integer" mask="0000000000" property="nrNotaCredito" size="12" maxLength="50" width="32%" labelWidth="18%" label="numeroNota" disabled="true" required="true"/>
		<adsm:hidden property="idFilial" serializable="false"/>

		<adsm:textbox dataType="text" label="recibo" property="reciboFreteCarreteiro.filial.sgFilial" size="3" serializable="false" disabled="true" labelWidth="18%" width="5%" />
		<adsm:textbox dataType="integer" property="reciboFreteCarreteiro.nrReciboFreteCarreteiro" size="10" mask="0000000000" serializable="false" disabled="true" width="27%" />
		<adsm:textbox dataType="text" property="situacaoNotaCredito" size="25" maxLength="20" width="32%" labelWidth="18%" label="situacao" disabled="true"/>

		<adsm:textbox dataType="text" property="nrIdentificacao" label = "proprietario" size="20" maxLength="10" labelWidth="18%" width="16%" disabled="true" cellStyle="vertical-align:bottom;"/>
		<adsm:textbox dataType="text" property="nmProprietario" size="23" maxLength="50" disabled="true" width="16%" cellStyle="vertical-align:bottom;"/>

		<adsm:textbox dataType="text" property="nrFrota" label ="meioTransporte" size="9" maxLength="10" labelWidth="18%" width="9%" disabled="true" cellStyle="vertical-align:bottom;"/>
		<adsm:textbox dataType="text" property="nrIdentificador" size="24" maxLength="50" disabled="true" width="23%" required="true" cellStyle="vertical-align:bottom;"/>

		<adsm:textbox dataType="text" property="tabela" size="25" label="tabela" labelWidth="18%" maxLength="50" disabled="true" width="32%"/>
		<adsm:textbox dataType="text" property="tpTabela" size="25" label="tipoTabela" labelWidth="18%" maxLength="50" disabled="true" width="32%"/>

		<adsm:textbox dataType="text" property="moeda" label = "moeda" size="25" maxLength="20" labelWidth="18%" width="32%" disabled="true"/>

		<adsm:textbox dataType="currency" property="vlParcelas" size="25" maxLength="20" width="32%" labelWidth="18%" label="totalDasParcelas" disabled="true"/>

		<adsm:textbox dataType="currency" property="vlDescontoSugerido" size="25" maxLength="20" width="32%" labelWidth="18%" label="descontoSugerido" onchange="return enabledDisbaleOb(this);"/>
		<adsm:textbox dataType="currency" property="vlDesconto" size="25" maxLength="20" width="32%" labelWidth="18%" label="valorDesconto" disabled="true"/>
		<adsm:textbox dataType="currency" property="vlAcrescimoSugerido" size="25" maxLength="20" width="32%" labelWidth="18%" label="acrescimoSugerido" onchange="return enabledDisbaleOb(this);"/>
		<adsm:textbox dataType="currency" property="vlAcrescimo" size="25" maxLength="20" width="32%" labelWidth="18%" label="valorAcrescimo" disabled="true"/>

		<adsm:textbox dataType="currency" property="vlDescUsoEquipamento" size="25" maxLength="20" width="82%" labelWidth="18%" label="descontoUsoEquipamento" disabled="true"/>

		<adsm:textbox dataType="currency" property="vlNota" size="25" maxLength="20" width="32%" labelWidth="18%" label="valorNota" disabled="true"/>
		<adsm:textbox dataType="text" property="tpSituacaoAprovacao" size="25" maxLength="20" width="32%" labelWidth="18%" label="situacaoAprovacao" disabled="true"/>

		<adsm:textbox label="dataGeracao" labelWidth="18%" width="32%" dataType="JTDateTimeZone" property="dhGeracao" required="true" disabled="true"/>
		<adsm:textbox label="dataEmissao" labelWidth="18%" width="32%" dataType="JTDateTimeZone" property="dhEmissao" disabled="true"/>
		<adsm:textarea maxLength="500" label="observacao" property="obNotaCredito" rows="2" labelWidth="18%" width="82%" columns="90"/>
	<adsm:buttonBar> 
		<adsm:button id="eventos" caption="eventos" onclick="exibeEventos()"/>
		<adsm:button id="exportarExcel" caption="exportarExcel" onclick="emitirReport('EX')"/>
		<adsm:button id="visualizarPrevia" caption="visualizarPrevia" onclick="emitirReport('VP')"/>
		<adsm:button id="emitir" caption="emitir" onclick="emitirReport('EM')"/>		
		<adsm:button id="controleCarga" caption="controleCarga" onclick="consultarControleCargas();" buttonType="findButton">
		 	<adsm:linkProperty src="idFilial" target="notaCredito.filial.idFilial"/>
			<adsm:linkProperty src="sgFilial" target="notaCredito.filial.sgFilial"/>

			<adsm:linkProperty src="idFilial" target="filialByIdFilialOrigem.idFilial"/>
			<adsm:linkProperty src="sgFilial" target="filialByIdFilialOrigem.sgFilial"/>
			<adsm:linkProperty src="nmFilial" target="filialByIdFilialOrigem.pessoa.nmFantasia"/>  

			<adsm:linkProperty src="idNotaCredito" target="notaCredito.idNotaCredito"/>
			<adsm:linkProperty src="nrNotaCredito" target="notaCredito.nrNotaCredito"/>
		</adsm:button>
		<adsm:button id="tabelaButton" caption="tabela" onclick="manterTabelasFretesAgendados();" buttonType="findButton" />		
		<adsm:button id="storeButtom" caption="salvar" onclick="onClickStore()"/>
	</adsm:buttonBar> 
	</adsm:form>
		<adsm:grid property="Parcelas" idProperty="idNotaCreditoParcela"
			selectionMode="none"
			gridHeight="120" showPagging="false" scrollBars="vertical"
			onRowClick="disabledClickGrid" autoSearch="false" onValidate="validateGrid"
			service="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.findParcelas" onDataLoadCallBack="dataLoadGrid"
		>
 			<adsm:gridColumn width="" title="tipoParcela" property="tpParcela.description" />
			<adsm:editColumn dataType="decimal" property="qtde" title="quantidade" field="textbox" align="right" width="150"/>
			<adsm:editColumn dataType="currency" property="vlUnit" title="valor" field="textbox" align="right" width="150"/>
			<adsm:editColumn dataType="currency" property="vlTot" title="total" field="textbox" align="right" width="150"/>
			<adsm:editColumn property="tpParcela.value" title="" field="hidden" width="1"/>
		</adsm:grid>
</adsm:window>
