<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;

	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");

		writeDataSession();
	}

	function writeDataSession() {
		if (idFilial != null &&
			sgFilial != null &&
			nmFilial != null &&
			document.getElementById("filial.idFilial").masterLink != "true") {
			setElementValue("filial.idFilial",idFilial);
			setElementValue("filial.sgFilial",sgFilial);
			setElementValue("filial.pessoa.nmFantasia",nmFilial);
		}
	}

	function initWindow(eventObj) {
		if (eventObj.name == "newButton_click" || eventObj.name == "tab_click" || eventObj.name == "removeButton")
			stateNew();
	}

	function stateNew() {
		setElementValue("tpOperacao", "V");
		setDisabled("tpOperacao", false);
		setDisabled("motorista.idMotorista", false);
		setDisabled("meioTransporte.idMeioTransporte", false);
		setDisabled("meioTransporte2.idMeioTransporte", false);
		setDisabled("filial.idFilial", false);
		setDisabled("nrLiberacao", false);
		setDisabled("reguladoraSeguro.idReguladora", false);
		writeDataSession();
		setFocusOnFirstFocusableField();
	}

	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterLiberacoesReguladoraAction.getFilialUsuario","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}

	var blDisableForm = false;
	function dataLoad_cb(data) {
		onDataLoad_cb(data);
		blDisableForm = !(getNestedBeanPropertyValue(data, "blEnableUpdate") == "true");
		enableForm();
		
		enableFields(data);
	}

	function storeButton_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		store_cb(data);
		blDisableForm = !(getNestedBeanPropertyValue(data, "blEnableUpdate") == "true");
		enableForm();
	}

	function enableForm() {
		setDisabled(document,true);

		setDisabled("btnStore", blDisableForm);
		setDisabled("dtVencimento", blDisableForm);

		setDisabled("newButton_click", false);
		setDisabled("removeButton_click", false);
		setFocus("newButton_click", false);
	}

	function loadProprietario(idMeioTransporte) {
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterLiberacoesReguladoraAction.findProprietarioByMeioTransporte","dataLoadSamples",{e:idMeioTransporte});
		xmit({serviceDataObjects:[sdo]});
	}

	function dataLoadSamples_cb(data) {
		fillFormWithFormBeanData(document.Lazy.tabIndex, data);
	}


	function enableFields(data) {
		var blVigenteAgregFunc = getNestedBeanPropertyValue(data,
				"blVigenteAgregFunc");

		if (blVigenteAgregFunc == "true") {
			setDisabled("dtVencimento", false);
			setDisabled("btnStore", false);
		}
	}

	//MEIO TRANSPORTE
	function callBackMeioTransporte2_cb(data) {
		var flag = meioTransporte2_nrFrota_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			loadProprietario(getNestedBeanPropertyValue(data[0],"idMeioTransporte"));
		return flag;
	}

	function callBackMeioTransporte_cb(data) {
		var flag = meioTransporte_nrIdentificador_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			loadProprietario(getNestedBeanPropertyValue(data[0],"idMeioTransporte"));
		return flag;
	}

	function changeMeioTransporte2() {
		var flag = meioTransporte2_nrFrotaOnChangeHandler();
		if (getElementValue("meioTransporte.idMeioTransporte") == "") {
			resetValue("proprietario.idProprietario");
			resetValue("proprietario.pessoa.nmPessoa");
		}
		return flag;
	}

	function changeMeioTransporte() {
		var flag = meioTransporte_nrIdentificadorOnChangeHandler();
		if (getElementValue("meioTransporte.idMeioTransporte") == "") {
			resetValue("meioTransporte2.idMeioTransporte");
			resetValue("proprietario.idProprietario");
			resetValue("proprietario.pessoa.nmPessoa");
		}
		return flag;
	}

	function popUpMeioTransporte(data) {
		loadProprietario(getNestedBeanPropertyValue(data,"idMeioTransporte"));
		return true;
	}
//-->
</script>
<adsm:window service="lms.contratacaoveiculos.manterLiberacoesReguladoraAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/contratacaoVeiculos/manterLiberacoesReguladora" idProperty="idLiberacaoReguladora" onDataLoadCallBack="dataLoad">
	<adsm:hidden property="tpSituacao" serializable="false" value="A"/>

	<adsm:combobox
		label="reguladora"
		property="reguladoraSeguro.idReguladora"
		optionProperty="idReguladora"
		optionLabelProperty="pessoa.nmPessoa"
		service="lms.contratacaoveiculos.manterLiberacoesReguladoraAction.findComboReguladoraSeguro"
		onlyActiveValues="true"
		boxWidth="300"
		labelWidth="20%"
		width="80%"
		required="true"
	/>

	<adsm:combobox
		label="tipoOperacao"
		property="tpOperacao"
		domain="DM_TIPO_CONTROLE_CARGAS"
		labelWidth="20%"
		width="80%"
		boxWidth="150"
		required="true"
		onlyActiveValues="true"
	/>

	<adsm:textbox dataType="text" property="nrLiberacao" label="numeroLiberacao" size="18" maxLength="17" labelWidth="20%" width="30%" required="true"/>

	<adsm:lookup
		service="lms.contratacaoveiculos.manterLiberacoesReguladoraAction.findLookupFilial" dataType="text"
		property="filial" labelWidth="20%" criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
		width="80%" action="/municipios/manterFiliais" idProperty="idFilial" required="true"
	>
		<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
		<adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="filial.empresa.tpEmpresa"/>
		<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="40" disabled="true" serializable="false"/>
		<adsm:hidden property="filial.empresa.tpEmpresa" value="M"/>
	</adsm:lookup>
	<adsm:hidden property="motorista.tpVinculo.value"/>

	<adsm:lookup dataType="text" property="motorista" idProperty="idMotorista"
		service="lms.contratacaoveiculos.manterLiberacoesReguladoraAction.findLookupMototorista"
		action="/contratacaoVeiculos/manterMotoristas" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
		criteriaProperty="pessoa.nrIdentificacao" label="motorista" labelWidth="20%" width="80%" size="20" maxLength="20" required="true"
	>
		<adsm:propertyMapping relatedProperty="motorista.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
		<adsm:propertyMapping relatedProperty="motorista.tpVinculo.value" modelProperty="tpVinculo.value"/>
		<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
		
		<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario" addChangeListener="false"/>
		<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" />
		<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa" />
		
		<adsm:hidden property="motorista.showFilialUsuarioLogado" value="true"/>
		<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="50" disabled="true"/>
	</adsm:lookup>

	<adsm:lookup 
		dataType="text" 
		property="meioTransporte2" 
		idProperty="idMeioTransporte" 
		cellStyle="vertical-align:bottom;"
		service="lms.contratacaoveiculos.manterLiberacoesReguladoraAction.findLookupMeioTransporte" 
		picker="false" 
		maxLength="6"
		action="/contratacaoVeiculos/manterMeiosTransporte" 
		cmd="list" 
		criteriaProperty="nrFrota"
		onDataLoadCallBack="callBackMeioTransporte2" 
		onchange="return changeMeioTransporte2()"
		label="meioTransporte" 
		labelWidth="20%" 
		width="8%" 
		size="7" 
		serializable="false">
		
		<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
		<adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador" />

		<adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte" />		
		<adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador" />

		<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario" addChangeListener="false"/>
		<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false" />
		<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario2.pessoa.nmPessoa" inlineQuery="false" />
	</adsm:lookup>

	<adsm:lookup 
		dataType="text" 
		property="meioTransporte" 
		idProperty="idMeioTransporte"
		service="lms.contratacaoveiculos.manterLiberacoesReguladoraAction.findLookupMeioTransporte" 
		picker="true" 
		maxLength="25"
		action="/contratacaoVeiculos/manterMeiosTransporte" 
		cmd="list" 
		criteriaProperty="nrIdentificador"
		onDataLoadCallBack="callBackMeioTransporte" 
		onPopupSetValue="popUpMeioTransporte" 
		onchange="return changeMeioTransporte()"
		width="60%" size="20" cellStyle="vertical-align:bottom;">

		<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
		<adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota" modelProperty="nrFrota" disable="false"/>

		<adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte" modelProperty="idMeioTransporte"/>	
		<adsm:propertyMapping relatedProperty="meioTransporte2.nrFrota" modelProperty="nrFrota" blankFill="false"/>	

		<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario" addChangeListener="false"/>
		<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false" />
		<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario2.pessoa.nmPessoa" inlineQuery="false" />
	</adsm:lookup>

	<adsm:lookup dataType="text" property="proprietario" idProperty="idProprietario"
		service="lms.contratacaoveiculos.manterLiberacoesReguladoraAction.findLookupProprietario"
		action="/contratacaoVeiculos/manterProprietarios" criteriaProperty="pessoa.nrIdentificacao" disabled="true" picker="false"
		label="proprietario" labelWidth="20%" width="80%" size="20" maxLength="20" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado">
		<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" serializable="false" size="50" disabled="true"/>
	</adsm:lookup>

	<adsm:textbox dataType="JTDate" disabled="true" property="dtLiberacao" label="dataLiberacao" labelWidth="20%" width="30%"/>
	<adsm:textbox dataType="JTDate" disabled="true" property="dtVencimento" label="dataVencimento" labelWidth="20%" width="30%"/>

	<adsm:buttonBar>
		<adsm:storeButton id="btnStore" callbackProperty="storeButton"/>
		<adsm:newButton id="newButton_click"/>
		<adsm:removeButton id="removeButton_click"/>
	</adsm:buttonBar>
	</adsm:form>
</adsm:window>