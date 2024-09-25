<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.manterHistoricoAWBsAction">

	<adsm:form 
		idProperty="idHistoricoAwb"
		action="/expedicao/consultarAWBs"
		onDataLoadCallBack="formLoad">
		
		<adsm:hidden property="awb.idAwb" />
		<adsm:hidden property="usuario.idUsuario" />
		
		<adsm:complement 
			label="usuario"
			labelWidth="20%"
			width="60%">
			
			<adsm:textbox 
				dataType="text" 
				property="usuario.nrMatricula" 
				size="16" 
				maxLength="16" 
			    disabled="true"/>
			
			<adsm:textbox 
				dataType="text" 
				property="usuario.nmUsuario" 
				size="35" 
				maxLength="50" 
			    disabled="true"/>
			    
		</adsm:complement>
		
		<adsm:textbox 
			property="dhInclusao" 
			dataType="JTDateTimeZone" 
			label="dataHoraOcorrencia" 
			required="true" 
			labelWidth="20%" 
			width="30%" />
			
		<adsm:checkbox 
			property="blGerarMensagem" 
			label="gerarMensagem" 
			labelWidth="20%" 
			width="30%" />
			
		<adsm:textarea 
			maxLength="500" 
			property="dsHistoricoAwb" 
			label="descricao" 
			rows="5" 
			columns="76" 
			required="true" 
			labelWidth="20%" 
			width="80%" />
			
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton callbackProperty="afterStore"/>
			<adsm:newButton id="btnLimpar"/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid 
		idProperty="idHistoricoAwb" 
		property="historicoList" 
		gridHeight="155" 
		unique="true" 
		scrollBars="horizontal"
		detailFrameName="hist"
		rows="7">
		
		<adsm:gridColumn 
			dataType="JTDateTimeZone"
			title="dataHoraOcorrencia" 
			property="dhInclusao" 
			width="140" 
			align="center"/>
			
		<adsm:gridColumn 
			title="mensagem" 
			property="blGerarMensagem" 
			renderMode="image-check"
			width="80" />
			
		<adsm:gridColumn 
			title="descricao" 
			property="dsHistoricoAwb" 
			width="600" />
		
		<adsm:buttonBar>
			<adsm:removeButton id="btnExcluir"/>
		</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
//getElement("usuario.nmUsuario").masterLink = "true";
//getElement("usuario.nrMatricula").masterLink = "true";
//getElement("usuario.idUsuario").masterLink = "true";
getElement("awb.idAwb").masterLink = "true";

function initWindow(eventObj) {
	//oldAlert(eventObj.name);
	if (eventObj.name == "tab_click") {
		changeFieldsStatus();
		findDadosSessao();
		populaGrid();
	} else if (eventObj.name == "newButton_click") {
		changeFieldsStatus();
		findDadosSessao();
		setFocusOnFirstFocusableField();
	} else if (eventObj.name == "removeButton_grid") {
		if (getElementValue("dhInclusao") == "" && 
			getElementValue("blGerarMensagem") == "" &&
			getElementValue("dsHistoricoAwb") == "") {
			newButtonScript();
		}
	}
}

function findDadosSessao() {
	var service = "lms.expedicao.manterHistoricoAWBsAction.findDadosSessao";
	var sdo = createServiceDataObject(service, "findDadosSessao");
	xmit({serviceDataObjects:[sdo]});
}

function findDadosSessao_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue("usuario.nmUsuario", data.usuario.nmUsuario);
	setElementValue("usuario.nrMatricula", data.usuario.nrMatricula);
	setElementValue("usuario.idUsuario", data.usuario.idUsuario);
}

function populaGrid() {
	var idAwb = getTabGroup(document).getTab("cad").getElementById("awb.idAwb").value;
	setElementValue("awb.idAwb", idAwb);
	historicoListGridDef.executeSearch(buildFormBeanFromForm(document.forms[0]), true);
}

function changeFieldsStatus() {
	var tpStatusAwb = getTabGroup(document).getTab("cad").getElementById("vlTpStatusAwb").value;
	if (tpStatusAwb == "C") {
		disableFields();
	} else if (tpStatusAwb == "E") {
		enableFields();
	}
}

function disableFields() {
	setDisabled(document, true);
	getElement("historicoList.chkSelectAll").disabled = true;
	setDisabled("dsHistoricoAwb", false);
	getElement("dsHistoricoAwb").readOnly = true;
}

function enableFields() {
	setDisabled(document, false);
	setDisabled("usuario.nmUsuario", true);
	setDisabled("usuario.nrMatricula", true);
	setDisabled("btnExcluir", true);
	if (historicoListGridDef.getRowCount() > 0) {
		getElement("historicoList.chkSelectAll").disabled = false;
	}
	getElement("dsHistoricoAwb").readOnly = false;
}

function afterStore_cb(data, errorMsg, errorKey, showErrorAlert, eventObj) {
	if (errorMsg != undefined) {
		alert(errorMsg);
	}
	store_cb(data, undefined, errorKey, showErrorAlert, eventObj);
	populaGrid();
	newButtonScript();
	/*disableFields();
	setDisabled("btnLimpar", false);
	setFocus("btnLimpar", false);*/
}

function formLoad_cb(data, error) {
	onDataLoad_cb(data, error);
	disableFields();
	setDisabled("btnLimpar", false);
	getElement("historicoList.chkSelectAll").disabled = true;
}

function hide() {
	newButtonScript();
}
//-->
</script>
