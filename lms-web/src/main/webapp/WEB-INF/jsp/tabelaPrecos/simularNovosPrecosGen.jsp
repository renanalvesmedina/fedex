<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.simularNovosPrecosGeneralidadesAction">
	<adsm:form 
		action="/tabelaPrecos/simularNovosPrecos"
		idProperty="idGeneralidade"
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
			width="18%" />

		<adsm:combobox 
			property="generalidade.idTabelaPrecoParcela" 
			label="generalidade" 
			optionLabelProperty="nmParcelaPreco" 
			optionProperty="idTabelaPrecoParcela" 
			autoLoad="false"
			required="true"
			onchange="return changeGeneralidade(false);"
			labelWidth="20%"
			width="82%"/>

		<adsm:textbox
			property="vlGeneralidade"
			label="valorAtual"
			dataType="currency"
			size="18"
			maxLength="18"
			disabled="true"
			labelWidth="20%"
			width="38%"/>

		<adsm:textbox
			property="vlGeneralidadeReajustado"
			label="valorReajustado"
			dataType="currency"
			required="true"
			size="18"
			maxLength="18"
			labelWidth="24%"
			width="18%"/>

		<adsm:textbox 
			property="vlMinimo" 
			label="valorMinimoAtual" 
			dataType="currency" 
			size="18" 
			maxLength="18" 
			disabled="true" 
			labelWidth="20%" 
			width="38%"/>
			
		<adsm:textbox 
			property="vlMinimoReajustado"
			label="valorMinimoReajustado"
			dataType="currency" 
			size="18" 
			maxLength="18" 
			labelWidth="24%" 
			width="18%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton callbackProperty="afterStore" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid 
		idProperty="idGeneralidade"
		property="generalidadesList"
		gridHeight="200" 
		unique="true"
		detailFrameName="gen"
		autoSearch="false"
		onSelectAll="generalidadesSelectAll"
		onSelectRow="generalidadesSelectRow"
		onDataLoadCallBack="generalidadesDataLoad">
		
		<adsm:gridColumn 
			property="parcelaPreco.dsParcelaPreco"
			title="generalidade"
			width="34%"/>
			
		<adsm:gridColumn 
			property="generalidade.vlGeneralidade"
			title="valorReajustado"
			dataType="currency"
			width="34%" 
			align="right"/>
			
		<adsm:gridColumn 
			property="generalidade.vlMinimo"
			title="valorMinimo"
			dataType="currency"
			width="34%" 
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
		loadGeneralidades();
	} else if (eventObj.name == "cleanButton_click") {
		copyDadosFromReajuste();
	}
	setElementValue("_edit", "");
}

function hide() {
	cleanButtonScript(document, false);
	generalidadesListGridDef.resetGrid();
	tab_onHide();
	return true;
}

function formDataLoad_cb(data, errorMessage, errorCode, eventObj) {
	onDataLoad_cb(data, errorMessage, errorCode, eventObj);
	copyDadosFromReajuste();
	changeGeneralidade(false);

	setElementValue("_edit", "true");
}

function loadGeneralidades() {
	var data = {
			tabelaPreco : {
				idTabelaPreco : getElementValue("tabelaPreco.idTabelaPreco")
			},
			tabelaBase : {
				idTabelaPreco : getElementValue("tabelaBase.idTabelaPreco")
			}
		}
	var service = "lms.tabelaprecos.simularNovosPrecosGeneralidadesAction.findGeneralidadesByTabela";
	var sdo = createServiceDataObject(service, "loadGeneralidades", data);
	xmit({serviceDataObjects:[sdo]});
}

function loadGeneralidades_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	generalidade_idTabelaPrecoParcela_cb(data);
	populaGrid();
}

function populaGrid() {
	generalidadesListGridDef.executeSearch(buildFormBeanFromForm(document.forms[0]), true);
}

function changeGeneralidade(resetValues) {
	var idTabelaPrecoParcela = getElementValue("generalidade.idTabelaPrecoParcela");
	if (idTabelaPrecoParcela != "") {
		var data = {
			generalidade : {
				idTabelaPrecoParcela : idTabelaPrecoParcela
			},
			tabelaBase : {
				idTabelaPreco : getElementValue("tabelaBase.idTabelaPreco")
			},
			resetValues : resetValues
		}
		var service = "lms.tabelaprecos.simularNovosPrecosGeneralidadesAction.findValoresGeneralidade";
		var sdo = createServiceDataObject(service, "changeGeneralidade", data);
		xmit({serviceDataObjects:[sdo]});
	} else {
		setElementValue("vlGeneralidade", "");
		setElementValue("vlMinimo", "");
	}
}

function changeGeneralidade_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue("vlGeneralidade", setFormat("vlGeneralidade", data.vlGeneralidade));
	setElementValue("vlMinimo", setFormat("vlMinimo", data.vlMinimo));
	if (data.resetValues == "true") {
		setElementValue("vlGeneralidadeReajustado", "");
		setElementValue("vlMinimoReajustado", "");
	}
}

function generalidadesDataLoad_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	changeExcluirStatus(true);
}

function clickExcluir() {
	if(!confirmI18nMessage("LMS-02067")) {
		return;
	}
	var data = {
		tabelaPreco : {
			idTabelaPreco : getElementValue("tabelaPreco.idTabelaPreco")
		},
		ids : generalidadesListGridDef.getSelectedIds().ids
	}
	
	var service = "lms.tabelaprecos.simularNovosPrecosGeneralidadesAction.removeGeneralidades";
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

function generalidadesSelectAll(estado) {
	changeExcluirStatus(!estado);
}

function generalidadesSelectRow(rowRef) {
	var gridData = generalidadesListGridDef.getSelectedIds();
	changeExcluirStatus(gridData.ids.length <= 0);
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
	setDisabled("vlMinimo", true);
	setDisabled("vlGeneralidade", true);
	setDisabled("btnExcluir", true);
}
//-->
</script>