<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.simularNovosPrecosAction">
	<adsm:form 
		action="/tabelaPrecos/simularNovosPrecos"
		idProperty="idTabelaPreco"
		onDataLoadCallBack="formDataLoadCallBack">
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-30043" />
			<adsm:include key="LMS-30038" />
			<adsm:include key="LMS-30041" />
		</adsm:i18nLabels>
		
		<adsm:hidden property="tabelaPreco.idTabelaPreco" serializable="true"/>
		<adsm:hidden property="tipoTabelaPreco.idTipoTabelaPreco" serializable="true"/>
		<adsm:hidden property="tabelaBase.tipoTabelaPreco.nrVersao" serializable="false"/>
		<adsm:hidden property="subtipoTabelaPreco.idSubtipoTabelaPreco"/>
		
		<adsm:hidden property="tabelaBase.moeda.dsMoeda"/>
		<adsm:hidden property="funcionario.idUsuario"/>
		
		<adsm:hidden property="emitirDensidades" value="false" serializable="true"/>
		<adsm:hidden property="emitirTonelada" value="true" serializable="true"/>
		<adsm:hidden property="emitirCargaCompleta" value="false" serializable="true"/>
		
		
		<%-------------------%>
		<%-- TABELA LOOKUP --%>
		<%-------------------%>
		<adsm:lookup 
			property="tabelaBase"
			label="tabelaBase"  
			service="lms.tabelaprecos.simularNovosPrecosAction.findLookupTabelaPreco" 
			action="/tabelaPrecos/manterTabelasPreco" 
			idProperty="idTabelaPreco"
			criteriaProperty="tabelaPrecoString"
			dataType="text" 
			onDataLoadCallBack="tabelaBaseDataLoad"
			afterPopupSetValue="tabelaBasePopupSetValue"
			onchange="return changeTabelaBase();"
			size="10"
			maxLength="9"
			width="60%"
			required="true">
			
		   	<adsm:propertyMapping 
		   		modelProperty="dsDescricao" 
		   		relatedProperty="tabelaBase.dsDescricao"/>
		   	
		   	<adsm:propertyMapping 
		   		modelProperty="tipoTabelaPreco.tpTipoTabelaPreco.value" 
		   		relatedProperty="tipoTabelaPreco.tpTipoTabelaPreco"/>
		   	
		   	<adsm:propertyMapping 
		   		modelProperty="tipoTabelaPreco.idTipoTabelaPreco" 
		   		relatedProperty="tipoTabelaPreco.idTipoTabelaPreco"/>

		   	<adsm:propertyMapping 
		   		modelProperty="tipoTabelaPreco.nrVersao" 
		   		relatedProperty="tabelaBase.tipoTabelaPreco.nrVersao"/>

		   	<adsm:propertyMapping 
		   		modelProperty="subtipoTabelaPreco.tpSubtipoTabelaPreco" 
		   		relatedProperty="subtipoTabelaPreco.tpSubtipoTabelaPreco"/>
		   		
		   	<adsm:propertyMapping 
		   		modelProperty="subtipoTabelaPreco.idSubtipoTabelaPreco" 
		   		relatedProperty="subtipoTabelaPreco.idSubtipoTabelaPreco"/>
		   		
	   		<adsm:propertyMapping 
		   		modelProperty="moeda.siglaSimbolo" 
		   		relatedProperty="tabelaBase.moeda.dsMoeda"/>
		   	
            <adsm:textbox 
            	dataType="text" 
            	property="tabelaBase.dsDescricao" 
            	size="30" 
            	maxLength="30" 
            	disabled="true"/>
            	
        </adsm:lookup>
        
        <adsm:textbox 
        	property="tipoTabelaPreco.tpTipoTabelaPreco"
        	label="tipoTabela"
        	dataType="text"
			width="20%"
			labelWidth="15%"
			disabled="true"
			required="true"/>
		
		<adsm:textbox 
			property="tipoTabelaPreco.nrVersao"
			dataType="integer"
			minValue="0"
			label="versao"
			size="5"
			maxLength="6"
			labelWidth="10%"
			width="12%"
			disabled="true"
			required="true"/>
			
		<adsm:textbox 
			dataType="text"
			label="subtipoTabela"
			property="subtipoTabelaPreco.tpSubtipoTabelaPreco"
			width="18%"
			labelWidth="17%"
			disabled="true"
			required="true"/>
		
		<adsm:textbox 
			label="percentualReajuste" 
			property="pcReajuste" 
			dataType="percent" 
			minValue="0.01"
			size="6" 
			maxLength="6" 
			width="42%" 
			required="true"/>
		
		<adsm:checkbox 
			label="efetivada" 
			property="blEfetivada" 
			disabled="true" 
			labelWidth="17%" 
			width="20%"/>
			
		<adsm:range label="vigencia" width="85%">
			<adsm:textbox 
				dataType="JTDate" 
				property="dtVigenciaInicial" 
				onchange="return validaDtVigenciaInicial();"/>
				
			<adsm:textbox 
				dataType="JTDate" 
				property="dtVigenciaFinal" />
				
		</adsm:range>
		
		<adsm:complement
			label="funcionario"
			width="42%">
			
			<adsm:textbox
				property="funcionario.nrMatricula"
				dataType="text"
				size="16"
				maxLength="16"
				disabled="true"/>
				
			<adsm:textbox 
				property="funcionario.nmFuncionario"
				dataType="text"  
				size="25" 
				disabled="true"/>
				
		</adsm:complement>
				
		<adsm:textbox 
			property="dtGeracao" 
			label="dataGeracao" 
			dataType="JTDate" 
			disabled="true" 
			picker="false"
			labelWidth="17%"
			width="25%"/>

		<adsm:buttonBar>
			<adsm:button 
				id="btnGerarSimulacao"
				caption="gerarSimulacao"
				onclick="return clickGerarSimulacao();"/>
			<adsm:reportViewerButton 
				service="lms.tabelaprecos.emitirTabelasMercurioAction" 
				id="btnImprimir"
				caption="imprimir"/>
			<adsm:button 
				id="btnEfetivarTabela"
				caption="efetivarTabela"
				onclick="return onClickEfetivarTabela();"/>
			<adsm:newButton 
				id="btnLimpar"
				onclick="return reconfiguraSessao();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
function initWindow(eventObj) {
	changeFieldStatus();
	if (eventObj.name == "tab_click") {
		if (getElementValue("tabelaPreco.idTabelaPreco") == "" &&
		    getElementValue("tabelaBase.idTabelaPreco") == "") {
			prepareInsert();
		}
	} else if (eventObj.name == "newButton_click") {
		prepareInsert();
	}
}

function formDataLoadCallBack_cb(data, errorMessage, errorCode, eventObj) {
	onDataLoad_cb(data, errorMessage, errorCode, eventObj);
	
	changeFieldStatus();
	changeAbasStatus(false);
}

function tabelaBaseDataLoad_cb(data, error) {
	tabelaBase_tabelaPrecoString_exactMatch_cb(data);
	if (error != undefined) {
		alert(error);
		return false;
	}
	updateNrVersao();
}

function changeTabelaBase() {
	if (getElementValue("tabelaBase.tabelaPrecoString") == "") {
		changeAbasStatus(true);
		resetValue("tipoTabelaPreco.nrVersao");
		resetValue("tabelaBase.tipoTabelaPreco.nrVersao");
	}
	reconfiguraSessao();
	return tabelaBase_tabelaPrecoStringOnChangeHandler();
}

function reconfiguraSessao() {
	var service = "lms.tabelaprecos.simularNovosPrecosAction.reconfiguraSessao";
	var sdo = createServiceDataObject(service);
	xmit({serviceDataObjects:[sdo]});
	return true;
}

function tabelaBasePopupSetValue(data) {
	if (data != undefined) {
		if (data.tipoTabelaPreco.tpTipoTabelaPreco.value == "C") {
			alert(i18NLabel.getLabel("LMS-30043"));
			clearDadosTabela();
			changeAbasStatus(true);
			setFocus("tabelaBase.tabelaPrecoString", false);
			return;
		}
		reconfiguraSessao();
		updateNrVersao();
	}
}

function clickGerarSimulacao() {
	if (validateTabScript(document.forms[0])) {
		if(confirm(i18NLabel.getLabel("LMS-30038"))) {
			var data = buildFormBeanFromForm(document.forms[0]);
			var service = "lms.tabelaprecos.simularNovosPrecosAction.scheduleGerarSimulacao";
			var sdo = createServiceDataObject(service, "gerarSimulacao", data);
			xmit({serviceDataObjects:[sdo]});
		}
	}
}

function gerarSimulacao_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	
	if (data.resultado != undefined) {
		alert(data.resultado);
	}

	/* comentado devido a transforma��o do processo em Batch, avaliar a melhor forma de fazer isso
	setElementValue("tabelaPreco.idTabelaPreco", data.tabelaPreco.idTabelaPreco);
	setElementValue("dtGeracao", setFormat("dtGeracao", data.tabelaPreco.dtGeracao));
	changeFieldStatus();
	store_cb(data, error);
	setFocus("btnLimpar", false);
	*/
}

function getDadosFromReajuste() {
	var data = {
		tabelaPreco : {
			idTabelaPreco : getElementValue("tabelaPreco.idTabelaPreco")
		},
		tabelaBase : {
			idTabelaPreco : getElementValue("tabelaBase.idTabelaPreco"),
			tabelaPrecoString : getElementValue("tabelaBase.tabelaPrecoString"),
			dsDescricao : getElementValue("tabelaBase.dsDescricao"),
			moeda : {
				dsMoeda : getElementValue("tabelaBase.moeda.dsMoeda")
			}
		}
	}
	
	return data;
}

function updateNrVersao() {
	var data = {
		subtipoTabelaPreco : {
			tpSubtipoTabelaPreco : getElementValue("subtipoTabelaPreco.tpSubtipoTabelaPreco")
		},
		tipoTabelaPreco : {
			tpTipoTabelaPreco : getElementValue("tipoTabelaPreco.tpTipoTabelaPreco")
		}
	}
	if (data.subtipoTabelaPreco.tpSubtipoTabelaPreco != "" &&
	    data.tipoTabelaPreco.tpTipoTabelaPreco != "") {

	    if(data.tipoTabelaPreco.tpTipoTabelaPreco != "D" &&
	       data.tipoTabelaPreco.tpTipoTabelaPreco != "E") {
			var service = "lms.tabelaprecos.simularNovosPrecosAction.findProximaVersaoTabela";
			var sdo = createServiceDataObject(service, "updateNrVersao", data);
			xmit({serviceDataObjects:[sdo]});
		} else {
			data.disable = "false";
			data.nrVersao = getElementValue("tabelaBase.tipoTabelaPreco.nrVersao");
			updateNrVersao_cb(data);
		}
	}
}

function updateNrVersao_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	
	if (data.disable == "true") {
		setDisabled("tipoTabelaPreco.nrVersao", true);
	} else if (data.disable == "false") {
		setDisabled("tipoTabelaPreco.nrVersao", false);
	}
	setElementValue("tipoTabelaPreco.nrVersao", setFormat("tipoTabelaPreco.nrVersao", data.nrVersao));
	changeAbasStatus(false);
}

function validaDtVigenciaInicial() {
	var data = getElementValue("dtVigenciaInicial");
	if (data != "") {
		var service = "lms.tabelaprecos.simularNovosPrecosAction.validaDtVigenciaInicial";
		var sdo = createServiceDataObject(service, "validaDtVigenciaInicial", {dt:data});
		xmit({serviceDataObjects:[sdo]});
	}
}

function validaDtVigenciaInicial_cb(data, error) {
	if (error != undefined) {
		alert(error);
		setElementValue("dtVigenciaInicial", "");
		setFocus("dtVigenciaInicial", false);
		return false;
	}
}

function clearDadosTabela() {
	setElementValue("tabelaBase.tabelaPrecoString", "");
	setElementValue("tabelaBase.dsDescricao", "");
	setElementValue("tipoTabelaPreco.tpTipoTabelaPreco", "");
	setElementValue("tipoTabelaPreco.idTipoTabelaPreco", "");
	setElementValue("subtipoTabelaPreco.tpSubtipoTabelaPreco", "");
	setElementValue("subtipoTabelaPreco.idSubtipoTabelaPreco", "");
	setElementValue("tipoTabelaPreco.nrVersao", "");
}

function changeAbasStatus(status) {
	var tabGroup = getTabGroup(this.document);
	if(tabGroup != undefined) {
		tabGroup.setDisabledTab("taxas", status);
		tabGroup.setDisabledTab("gen", status);
	}
}

function prepareInsert() {
	var service = "lms.tabelaprecos.simularNovosPrecosAction.findDadosSessao";
	var sdo = createServiceDataObject(service, "prepareInsert");
	xmit({serviceDataObjects:[sdo]});
}

function prepareInsert_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	
	setElementValue("funcionario.idUsuario", data.funcionario.idUsuario);
	setElementValue("funcionario.nrMatricula", data.funcionario.nrMatricula);
	setElementValue("funcionario.nmFuncionario", data.funcionario.nmFuncionario);
	
	if (getElementValue("tabelaBase.idTabelaPreco") == "") {
		changeAbasStatus(true);
	} else {
		changeAbasStatus(false);
	}
	setFocusOnFirstFocusableField();
}

function changeFieldStatus() {
	if (getElementValue("tabelaPreco.idTabelaPreco") == "") {
		enableFieldsInsert();
	} else {
		if (getElementValue("blEfetivada") == true) {
			disableFields();
		} else if (getElementValue("blEfetivada") == false) {
			enableFieldsEdit();
		}
	}
}

function disableFields() {
	setDisabled(document, true);
	setDisabled("btnImprimir", false);
	setDisabled("btnLimpar", false);
}

function enableFieldsInsert() {
	setDisabled(document, false);
	setDisabled("tabelaBase.dsDescricao", true);
	setDisabled("tipoTabelaPreco.tpTipoTabelaPreco", true);
	setDisabled("tipoTabelaPreco.nrVersao", true);
	setDisabled("subtipoTabelaPreco.tpSubtipoTabelaPreco", true);
	setDisabled("blEfetivada", true);
	setDisabled("funcionario.nmFuncionario", true);
	setDisabled("funcionario.nrMatricula", true);
	setDisabled("dtGeracao", true);
	setDisabled("dtVigenciaFinal", true);
	setDisabled("btnImprimir", true);
	setDisabled("btnEfetivarTabela", true);
}

function enableFieldsEdit() {
	disableFields();
	setDisabled("pcReajuste", false);
	setDisabled("dtVigenciaInicial", false);
	setDisabled("btnGerarSimulacao", false);
	if (getElementValue("blEfetivada") == "true") {
		setDisabled("btnEfetivarTabela", true);
	} else {
		setDisabled("btnEfetivarTabela", false);
	}
}

function onClickEfetivarTabela() {
	var data = {
		dtVigenciaInicial : getElementValue("dtVigenciaInicial"),
		idTabelaPreco : getElementValue("tabelaPreco.idTabelaPreco"),
		subtipoTabelaPreco : {
			idSubtipoTabelaPreco : getElementValue("subtipoTabelaPreco.idSubtipoTabelaPreco")
		},
		tipoTabelaPreco : {
			tpTipoTabelaPreco : getElementValue("tipoTabelaPreco.tpTipoTabelaPreco")
		}
	}

	var service = "lms.tabelaprecos.simularNovosPrecosAction.efetivarTabela";
	var sdo = createServiceDataObject(service, "efetivarTabela", data);
	xmit({serviceDataObjects:[sdo]});
}

function efetivarTabela_cb(data, error) {
	store_cb(data, error);
	if (error != undefined) {
		return;
	}
	setElementValue("blEfetivada",true);
	changeFieldStatus();
	alert(i18NLabel.getLabel("LMS-30041"));
	setFocus("btnLimpar", false);
}
//-->
</script>