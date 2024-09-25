<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.tabelaprecos.simularNovosPrecosCiasAereasAction">
	
	<adsm:form 
		action="/tabelaPrecos/simularNovosPrecosCiasAereas"
		idProperty="idTabelaPreco"
		onDataLoadCallBack="formDataLoadCallBack">
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-30043" />
			<adsm:include key="LMS-30038" />
			<adsm:include key="LMS-30041" />
			<adsm:include key="LMS-01114" />
		</adsm:i18nLabels>
		
		<adsm:hidden property="tabelaPreco.idTabelaPreco"/>
		<adsm:hidden property="tipoTabelaPreco.idTabelaPreco"/>
		<adsm:hidden property="tipoTabelaPreco.idTipoTabelaPreco"/>
		<adsm:hidden property="tabelaBase.moeda.dsMoeda"/>
		<adsm:hidden property="empresaByIdEmpresaCadastrada.idEmpresa"/>
		<adsm:hidden property="funcionario.idUsuario"/>
		
		<adsm:hidden property="tpSituacao" value="A" />
		
		<%-------------------%>
		<%-- TABELA LOOKUP --%>
		<%-------------------%>
		<adsm:lookup 
			property="tabelaBase"
			label="tabelaBase"  
			service="lms.tabelaprecos.simularNovosPrecosCiasAereasAction.findLookupTabelaPreco" 
			action="/tabelaPrecos/manterTabelasPreco" 
			idProperty="idTabelaPreco"
			criteriaProperty="tabelaPrecoString"
			dataType="text" 
			onDataLoadCallBack="tabelaBaseDataLoad"
			afterPopupSetValue="tabelaBasePopupSetValue"
			onchange="return changeTabelaBase();"
			size="7"
			maxLength="7"
			width="38%"
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
		   		modelProperty="subtipoTabelaPreco.idSubtipoTabelaPreco" 
		   		relatedProperty="subtipoTabelaPreco.idSubtipoTabelaPreco"/>
		   		
	   		<adsm:propertyMapping 
		   		modelProperty="moeda.siglaSimbolo" 
		   		relatedProperty="tabelaBase.moeda.dsMoeda"/>
		   		
		   	<adsm:propertyMapping 
		   		modelProperty="tipoTabelaPreco.empresaByIdEmpresaCadastrada.pessoa.nmPessoa"
		   		relatedProperty="empresaByIdEmpresaCadastrada.pessoa.nmPessoa"/>
		   	
            <adsm:textbox 
            	dataType="text" 
            	property="tabelaBase.dsDescricao" 
            	size="30" 
            	maxLength="30" 
            	disabled="true"/>
            	
        </adsm:lookup>
        
       	<adsm:textbox 
			label="ciaAerea" 
			property="empresaByIdEmpresaCadastrada.pessoa.nmPessoa" 
			dataType="text"
			size="35"
			width="20%"
			disabled="true"/>
			
		<adsm:complement
			label="cliente" 
			labelWidth="15%"
			width="85%">
			
			<adsm:textbox 
				property="cliente.pessoa.nrIdentificacao"
				dataType="text"
				disabled="true"
				maxLength="20"
				size="20"/>
			
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				serializable="false"
				size="58"/>
			
		</adsm:complement>
		
		<adsm:textbox 
        	property="tipoTabelaPreco.tpTipoTabelaPreco"
        	label="tipoTabela"
        	dataType="text"
			labelWidth="15%"
			width="38%"
			disabled="true"
			required="true"/>
		
		<adsm:combobox 
			label="subtipoTabela"
			property="subtipoTabelaPreco.idSubtipoTabelaPreco"
			service="lms.tabelaprecos.simularNovosPrecosCiasAereasAction.findSubtiposTabelaPrecos"
			optionLabelProperty="tpSubtipoTabelaPreco"
			optionProperty="idSubtipoTabelaPreco"
			width="18%"
			required="true"/>
			
		<adsm:textbox 
			label="percentualReajuste" 
			property="pcReajuste" 
			dataType="percent" 
			size="6" 
			maxLength="6" 
			width="38%" 
			required="true"/>
		
		<adsm:checkbox 
			label="efetivada" 
			property="blEfetivada" 
			disabled="true" 
			width="20%"/>
		
		<adsm:combobox 
			label="tarifa" 
			property="tpTarifaReajuste" 
			domain="DM_TARIFA_REAJUSTE"
			required="true" 
			width="38%"/>
		
		<adsm:range label="vigencia" width="32%">
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
			label="dataGeracao" 
			property="dtGeracao" 
			dataType="JTDate" 
			disabled="true" 
			width="32%"
			picker="false"/>

		<adsm:buttonBar>
			<adsm:button 
				id="btnGerarSimulacao"
				caption="gerarSimulacao"
				onclick="return clickGerarSimulacao();"/>
			<adsm:reportViewerButton 
				id="btnImprimir"
				caption="imprimir"
				service="lms.tabelaprecos.emitirTabelasMercurioAction.execute"/>
			<adsm:button 
				id="btnEfetivarTabela"
				caption="efetivarTabela"
				onclick="return onClickEfetivarTabela();"/>
			<adsm:newButton 
				id="btnLimpar"/>
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
	
	changeAbasStatus(true);
	changeFieldStatus();
}

function changeTabelaBase() {
	if (getElementValue("tabelaBase.tabelaPrecoString") == "") {
		changeAbasStatus(true);
		resetValue("cliente.pessoa.nrIdentificacao");
		resetValue("cliente.pessoa.nmPessoa");
	}
	reconfiguraSessao();
	cleanExcecoes();
	return tabelaBase_tabelaPrecoStringOnChangeHandler();
}

function tabelaBasePopupSetValue(data) {
	if (data != undefined) {
		if (data.tipoTabelaPreco.tpTipoTabelaPreco.value != "C") {
			alertI18nMessage("LMS-30043");
			clearDadosTabela();
			changeAbasStatus(true);
			setFocus("tabelaBase.tabelaPrecoString", false);
		} else {
			changeAbasStatus(false);
			findClienteByIdTabelaPreco(data.idTabelaPreco);
		}
	}
}

function tabelaBaseDataLoad_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	if (data.length == 1) {
		changeAbasStatus(false);
		findClienteByIdTabelaPreco(data[0].idTabelaPreco);
	}
	tabelaBase_tabelaPrecoString_exactMatch_cb(data);
}

function findClienteByIdTabelaPreco(idTabelaPreco) {
	resetValue("cliente.pessoa.nrIdentificacao");
	resetValue("cliente.pessoa.nmPessoa");
	var dados = {
		idTabelaPreco : idTabelaPreco
	}
	var service = "lms.tabelaprecos.simularNovosPrecosCiasAereasAction.findClienteByIdTabelaPreco";
	var sdo = createServiceDataObject(service, "findClienteByIdTabelaPreco", dados);
	xmit({serviceDataObjects:[sdo]});
}

function findClienteByIdTabelaPreco_cb(data, error) {
	if (error != undefined) {	
		alert(error);
		return false;
	}
	if (data.cliente != undefined) {
		setElementValue("cliente.pessoa.nrIdentificacao", data.cliente.pessoa.nrIdentificacao);
		setElementValue("cliente.pessoa.nmPessoa", data.cliente.pessoa.nmPessoa);
	}
}

function validaDtVigenciaInicial() {
	var service = "lms.tabelaprecos.simularNovosPrecosCiasAereasAction.validaDtVigenciaInicial";
	var sdo = createServiceDataObject(service, "validaDtVigenciaInicial", {dt:getElementValue("dtVigenciaInicial")});
	xmit({serviceDataObjects:[sdo]});
}

function validaDtVigenciaInicial_cb(data, error) {
	if (error != undefined) {
		alert(error);
		setElementValue("dtVigenciaInicial", "");
		setFocus("dtVigenciaInicial", false);
		return false;
	}
}

function clickGerarSimulacao() {
	if (validateTabScript(document.forms[0])) {
		if(confirm(i18NLabel.getLabel("LMS-30038"))) {
			var tabGroup = getTabGroup(document);
			var tabExce = tabGroup.getTab("reajExce");
			var formExce = tabExce.getDocument().forms[0];
			
			var data = buildFormBeanFromForm(document.forms[0]);
			merge(data, buildFormBeanFromForm(formExce));
			
			var service = "lms.tabelaprecos.simularNovosPrecosCiasAereasAction.gerarSimulacaoCiaAerea";
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
	
	setElementValue("tabelaPreco.idTabelaPreco", data.tabelaPreco.idTabelaPreco);
	setElementValue("tipoTabelaPreco.idTabelaPreco", data.tabelaPreco.idTabelaPreco);
	setElementValue("dtGeracao", setFormat("dtGeracao", data.tabelaPreco.dtGeracao));
	changeFieldStatus();
	changeAbasStatus(true);
	store_cb(data, error);
	setFocus("btnLimpar");
}

function onClickEfetivarTabela() {
	if(getElementValue("dtVigenciaInicial") == "") {
		alertI18nMessage("LMS-01114");
		setFocus("dtVigenciaInicial");
		return;
	}

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
	
	var service = "lms.tabelaprecos.simularNovosPrecosCiasAereasAction.efetivarTabela";
	var sdo = createServiceDataObject(service, "efetivarTabela", data);
	xmit({serviceDataObjects:[sdo]});
}

function efetivarTabela_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	setElementValue("blEfetivada",true);
	changeFieldStatus();
	alertI18nMessage("LMS-30041");
	store_cb(data, error);
	setFocus("btnLimpar");
}

function prepareInsert() {
	var service = "lms.tabelaprecos.simularNovosPrecosCiasAereasAction.findDadosSessao";
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
	setFocusOnFirstFocusableField(document);
}

function clearDadosTabela() {
	resetValue("tabelaBase.tabelaPrecoString");
	resetValue("tabelaBase.dsDescricao");
	resetValue("tipoTabelaPreco.tpTipoTabelaPreco");
	resetValue("tipoTabelaPreco.idTipoTabelaPreco");
	resetValue("subtipoTabelaPreco.idSubtipoTabelaPreco");
	resetValue("empresaByIdEmpresaCadastrada.pessoa.nmPessoa");
	resetValue("cliente.pessoa.nrIdentificacao");
	resetValue("cliente.pessoa.nmPessoa");
}

function changeFieldStatus() {
	if (getElementValue("tabelaPreco.idTabelaPreco") == "") {
		enableFieldsInsert();
	} else {
		if (getElementValue("blEfetivada") == true) {
			disableFields();
		} else {
			enableFieldsEdit();
			setFocus("pcReajuste", false);
		}
	}
}

function changeAbasStatus(status) {
	var tabGroup = getTabGroup(this.document);
	if(tabGroup != undefined) {
		tabGroup.setDisabledTab("reajEsp", status);
		tabGroup.setDisabledTab("reajExce", status);
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
	setDisabled("blEfetivada", true);
	setDisabled("funcionario.nmFuncionario", true);
	setDisabled("funcionario.nrMatricula", true);
	setDisabled("dtGeracao", true);
	setDisabled("dtVigenciaFinal", true);
	setDisabled("btnImprimir", true);
	setDisabled("btnEfetivarTabela", true);
	setDisabled("empresaByIdEmpresaCadastrada.pessoa.nmPessoa", true);
	setDisabled("cliente.pessoa.nrIdentificacao", true);
	setDisabled("cliente.pessoa.nmPessoa", true);
}

function enableFieldsEdit() {
	disableFields();
	setDisabled("pcReajuste", false);
	setDisabled("tpTarifaReajuste", false);
	setDisabled("dtVigenciaInicial", false);
	setDisabled("btnGerarSimulacao", false);

	if (getElementValue("blEfetivada") == "true") {
		setDisabled("btnEfetivarTabela", true);
	} else {
		setDisabled("btnEfetivarTabela", false);
	}
}

function cleanExcecoes() {
	var tabGroup = getTabGroup(this.document);
	var tab = tabGroup.getTab("reajExce");
	cleanButtonScript(tab.getDocument(), false);
}

function reconfiguraSessao() {
	var service = "lms.tabelaprecos.simularNovosPrecosAction.reconfiguraSessao";
	var sdo = createServiceDataObject(service);
	xmit({serviceDataObjects:[sdo]});
}
//-->
</script>