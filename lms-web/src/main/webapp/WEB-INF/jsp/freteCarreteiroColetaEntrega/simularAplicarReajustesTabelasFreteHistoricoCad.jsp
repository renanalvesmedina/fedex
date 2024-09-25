<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--

	function dataLoad_cb(data) {
		onDataLoad_cb(data);
		//FIXME: Aqui eu devo chamar a função "validateTpViculoFromMeioTransporte" passando como parametro
		// o valor do dominio tpVinculo do meio transporte
		//DEVE chamar a função changeBlPercentual para ajeitar os campos da grid
		changeBlPercentual();
		validateRegra31();
		validateRegra3_11();
		onDataLoad_cb(data);
		validateTpViculoFromMeioTransporte(getNestedBeanPropertyValue(data,"meioTransporte.tpVinculo.value"));
		calculeValuesGrid();
	}
	
	//Regra 3.10
	function changeBlPercentual() {
		comboboxChange({e:document.getElementById("blPercentual")});
		var blPercentual = getElementValue("blPercentual");
		if (blPercentual == "")
			for (var x = 0; x < 4; x++) {
				setDisabled("ncParcelaSimulacao:" + x + ".pcReajuste",true);
				setDisabled("ncParcelaSimulacao:" + x + ".vlReajuste",true);
				resetValue("ncParcelaSimulacao:" + x + ".pcReajuste");
				resetValue("ncParcelaSimulacao:" + x + ".vlReajuste");
			}
		else if (blPercentual == "V")
			for (var x = 0; x < 4; x++) {
				setDisabled("ncParcelaSimulacao:" + x + ".pcReajuste",true);
				setDisabled("ncParcelaSimulacao:" + x + ".vlReajuste",false);
				//resetValue("ncParcelaSimulacao:" + x + ".pcReajuste");
			}
		else if (blPercentual == "P")
			for (var x = 0; x < 4; x++) {
				setDisabled("ncParcelaSimulacao:" + x + ".pcReajuste",false);
				setDisabled("ncParcelaSimulacao:" + x + ".vlReajuste",true);
				//resetValue("ncParcelaSimulacao:" + x + ".vlReajuste");
			}
	}
	
	//VERIFICA SE A ALGUM CAMPO PREENCHIDO NA AGRAUPAMENTO PARAMETROS
	function comboChangeTipoTabelaColetaEntrega_idTipoTabelaColetaEntrega() {
		comboboxChange({e:document.getElementById("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega")});
		validateRegra31();
		validateRegra3_11();
	}
	function comboChangeTipoMeioTransporte_idTipoMeioTransporte() {
		comboboxChange({e:document.getElementById("tipoMeioTransporte.idTipoMeioTransporte")});
		validateRegra31();
		validateRegra3_11();
	}
	function setPopUpValue(data) {
		executeRegra31(false);
		validateTpViculoFromMeioTransporte(getNestedBeanPropertyValue(data,"meioTransporte.tpVinculo.value"));
		return true;
	}
	function onLookupChangeMeioTransporteRodoviario() {
		var flag = meioTransporteRodoviario_meioTransporte_nrIdentificadorOnChangeHandler();
		validateRegra31();
		if (getElementValue("meioTransporteRodoviario.meioTransporte.nrIdentificador") == "")
			validateTpViculoFromMeioTransporte(null);
		return flag;
	}
	function onLookupChangeMeioTransporteRodoviario2() {
		var flag = meioTransporteRodoviario2_meioTransporte_nrFrotaOnChangeHandler();
		validateRegra31();
		if (getElementValue("meioTransporteRodoviario2.meioTransporte.nrFrota") == "")
			validateTpViculoFromMeioTransporte(null);
		return flag;
	}
	function onLookupDataLoadMeioTransporteRodoviario2_cb(data) {
		var flag = meioTransporteRodoviario2_meioTransporte_nrFrota_exactMatch_cb(data);
		validateRegra31();
		if (data != undefined && data.length == 1)
			validateTpViculoFromMeioTransporte(getNestedBeanPropertyValue(data,":0.meioTransporte.tpVinculo.value"));
		return flag;
	}
	function onLookupDataLoadMeioTransporteRodoviario_cb(data) {
		var flag = meioTransporteRodoviario_meioTransporte_nrIdentificador_exactMatch_cb(data);
		validateRegra31();
		if (data != undefined && data.length == 1)
			validateTpViculoFromMeioTransporte(getNestedBeanPropertyValue(data,":0.meioTransporte.tpVinculo.value"));
		return flag;
	}


	function validateRegra31() {
		executeRegra31((getElementValue("meioTransporteRodoviario2.meioTransporte.nrFrota") == "" &&
						getElementValue("meioTransporteRodoviario.meioTransporte.nrIdentificador") == "" &&
						getElementValue("dtEmissaoInicial") == "" &&
						getElementValue("dtEmissaoFinal") == ""));
	}
	function executeRegra31(isNullFields) {
		var numLines = 4;
		if (isNullFields) {
			for (var x = 0; x < numLines; x++)
				setDisabled("ncParcelaSimulacao:" + x + ".qtNcParcelaSimulacao",false);
		}else{
			for (var x = 0; x < numLines; x++) {
				setElementValue("ncParcelaSimulacao:" + x + ".qtNcParcelaSimulacao","0");
				setDisabled("ncParcelaSimulacao:" + x + ".qtNcParcelaSimulacao",true);
				if (getElementValue("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega") != "" &&
						getElementValue("tipoMeioTransporte.idTipoMeioTransporte") != "")
					setDisabled("ncParcelaSimulacao:" + x + ".vlOriginal",true);
				
				setElementValue("ncParcelaSimulacao:" + x + ".vlAtual",
						setFormat(document.getElementById("ncParcelaSimulacao:" + x + ".vlAtual"),"0")); 
				resetValue("ncParcelaSimulacao:" + x + ".vlReajustado");
				resetValue("ncParcelaSimulacao:" + x + ".vlReajuste");
				resetValue("ncParcelaSimulacao:" + x + ".pcReajuste");
			}
			resetValue("vlTotalAtual");
			resetValue("vlTotalReajustado");
		}
	}

	//Implementação da filial do usuário logado como padrão
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	var nmMoeda = null;
	
	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
		nmMoeda  = getNestedBeanPropertyValue(data,"moeda.dsMoeda");
		writeDataSession();
	}

	function writeDataSession() {
		if (idFilial != null &&
			sgFilial != null &&
			nmFilial != null) {
			setElementValue("filial.idFilial",idFilial);
			setElementValue("filial.sgFilial",sgFilial);
			setElementValue("filial.pessoa.nmFantasia",nmFilial);
		}
		if (nmMoeda != null)
			setElementValue("moeda.dsMoeda",nmMoeda);
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "newButton_click" || eventObj.name == "tab_click" || eventObj.name == "removeButton")
			newState();	
	}
	
	function newState() { 
		writeDataSession();
		//setDisabled("carregarDadosHistoricos",false);
		setDisabled("carregarSimulacao",true);
		setDisabled("vlTotalReajustado",true);
		setDisabled("vlTotalAtual",true);
		for (var x = 0; x < 4; x++) {
			setDisabled("ncParcelaSimulacao:" + x + ".pcReajuste",true);
			setDisabled("ncParcelaSimulacao:" + x + ".vlReajuste",true);
			setDisabled("ncParcelaSimulacao:" + x + ".qtNcParcelaSimulacao",false);
			setDisabled("ncParcelaSimulacao:" + x +  ".vlOriginal",false);
		}
		changeBlPercentual();
		setFocusOnFirstFocusableField();
	}
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.findDataSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
		document.getElementById("dhCriacao").required = false;
	}
	//FUNÇÕES RELACIONADAS AO CARREGAR DADOS HISTORICOS
	function onClickCarregarDadosHistorico() {
		strCallBack = "carregarDadosHistorico";
		strService = "lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.loadHistoryData";
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false};
		var storeSDO = createServiceDataObject(strService, strCallBack, buildFormBeanFromForm(document.Lazy));
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
	}
	function carregarDadosHistorico_cb(data,exception,key) {
		if (exception != undefined) {
			alert(exception);
			return;
		}
		
		setElementValue("ncParcelaSimulacao:0.qtNcParcelaSimulacao",setFormat(document.getElementById("ncParcelaSimulacao:0.qtNcParcelaSimulacao"),((getNestedBeanPropertyValue(data,"DH_qt") != undefined) ? getNestedBeanPropertyValue(data,"DH_qt") : "0")));
		setElementValue("ncParcelaSimulacao:1.qtNcParcelaSimulacao",setFormat(document.getElementById("ncParcelaSimulacao:1.qtNcParcelaSimulacao"),((getNestedBeanPropertyValue(data,"EV_qt") != undefined) ? getNestedBeanPropertyValue(data,"EV_qt") : "0")));
		setElementValue("ncParcelaSimulacao:2.qtNcParcelaSimulacao",setFormat(document.getElementById("ncParcelaSimulacao:2.qtNcParcelaSimulacao"),((getNestedBeanPropertyValue(data,"FP_qt") != undefined) ? getNestedBeanPropertyValue(data,"FP_qt") : "0")));
		setElementValue("ncParcelaSimulacao:3.qtNcParcelaSimulacao",setFormat(document.getElementById("ncParcelaSimulacao:3.qtNcParcelaSimulacao"),((getNestedBeanPropertyValue(data,"QU_qt") != undefined) ? getNestedBeanPropertyValue(data,"QU_qt") : "0")));
		calculeValuesGrid();
	}
	//Regra 3.7 chamada pelo detalhamento e pela lookup de meio transporte
	function validateTpViculoFromMeioTransporte(tpVinculo) {
		if (tpVinculo == "E")
			setDisabled("carregarSimulacao",true);
		else
			setDisabled("carregarSimulacao",false);
	}
	
	function validateRegra3_11() {
		if (getElementValue("tipoMeioTransporte.idTipoMeioTransporte") != "" &&
			getElementValue("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega") != "" &&
			getElementValue("filial.idFilial") != "") {
			
			strCallBack = "callbackVlOriginal";
			strService = "lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.findVlOriginal";
			var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false};
			var storeSDO = createServiceDataObject(strService, strCallBack, buildFormBeanFromForm(document.Lazy));
			remoteCall.serviceDataObjects.push(storeSDO);
			xmit(remoteCall);
		}else
			validateRegra31();
			for (var x = 0; x < 4; x++) {
				var strObj = "ncParcelaSimulacao:" + x + ".vlOriginal";
				setElementValue(strObj, setFormat(document.getElementById(strObj),"0"));
				setDisabled(strObj,false);
			}
	}
	function callbackVlOriginal_cb(data,exeption) {
		if (getNestedBeanPropertyValue(data,"isValidResult") == "false") {
			for (var x = 0; x < 4; x++) {
				var strObj = "ncParcelaSimulacao:" + x + ".vlOriginal";
				setElementValue(strObj, setFormat(document.getElementById(strObj),"0"));
				setDisabled(strObj,false);
			}
			alert(i18NLabel.getLabel("LMS-25024"));
		}else{
			for (var x = 0; x < 4; x++) {
				var obj = document.getElementById("ncParcelaSimulacao:" + x + ".vlOriginal");
				setDisabled(obj,true);
				if (getNestedBeanPropertyValue(data,"qtde_" + x) == undefined)
					setElementValue(obj,setFormat(obj,"0"));
				else
					setElementValue(obj,setFormat(obj,getNestedBeanPropertyValue(data,"qtde_" + x)));
			}
			calculeValuesGrid();
		}
	}
	
	function validateRegra312_315(field) {
		if (field.previousValue != getElementValue(field))
			getTabGroup(this.document).getTab("cad").setChanged(true);

		var flag = onBeforeDeactivateElement(field,undefined);
		
		var position = field.name.substring(19,20);
		if ((getElementValue("ncParcelaSimulacao:" + position + ".qtNcParcelaSimulacao") == "" || 
			getElementValue("ncParcelaSimulacao:" + position + ".vlOriginal") == "" ||
			parseFloat(getElementValue("ncParcelaSimulacao:" + position + ".vlOriginal")) == 0) && getElementValue(field) != "") {
			var keyMsg;
			if (getElementValue("blPercentual") == "P"){
				keyMsg = "LMS-25016";
			}else{
				keyMsg = "LMS-25015";
			}
			alert(i18NLabel.getLabel(keyMsg));
			resetValue(field);
			return false;
		}
		calculeValuesGrid();
		return flag;
	}
	 
	 
	 function calculeValuesGrid() {
	 	strCallBack = "callbackCalculeValuesGrid";
		strService = "lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.calculeValuesGrid";
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false};
		var storeSDO = createServiceDataObject(strService, strCallBack, buildFormBeanFromForm(document.Lazy));
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
	 }
	 function callbackCalculeValuesGrid_cb(data,exception) {
	 	fillFormWithFormBeanData(document.Lazy.tabIndex, data)
	 }
	 
	 function controlChangeValuesGrid(field) {
	 	var flag = onBeforeDeactivateElement(field,undefined); 
	  	if (flag)
	  		calculeValuesGrid();
	  	return flag;
	 }
	 
	 function storeCustom_cb(data,exception) {
	 	if (exception)
	 		alert(exception);
	 	else{
	 		store_cb(data);
	 		validateTpViculoFromMeioTransporte(getElementValue("meioTransporte.tpVinculo.value"));
	 	}
	 } 
	 
	 function carregaSimulacao() {
		var tabGroup = getTabGroup(this.document);
		if (tabGroup.getTab("cad").hasChanged())
			alert(i18NLabel.getLabel("LMS-25013"));
		else
			parent.parent.redirectPage("freteCarreteiroColetaEntrega/aplicarReajustesTabelasFreteTabelas.do?cmd=main&idParamSimulacaoHistorica=" + getElementValue("idParamSimulacaoHistorica"));
	 }
	 function popUpFilial(data) {
	 	fillFormWithFormBeanData(document.Lazy.tabIndex, data);
		validateRegra3_11();
	 	return true;
	 }
	 function dataLoadFilial_cb(data) {
		var flag = filial_sgFilial_exactMatch_cb(data);
		validateRegra3_11();
		return flag;
	}
	function changeFilial() {
		var flag = filial_sgFilialOnChangeHandler();
		validateRegra3_11();
		return flag;
	}
	
	function changeDatas() {
		validateRegra31();
		return true;
	}
//-->
</script>
<adsm:window service="lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction" onPageLoadCallBack="pageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-25016"/>
		<adsm:include key="LMS-25015"/>
		<adsm:include key="LMS-25013"/>
		<adsm:include key="LMS-25024"/>
		
	</adsm:i18nLabels>
	<adsm:form action="/freteCarreteiroColetaEntrega/simularAplicarReajustesTabelasFreteHistorico" idProperty="idParamSimulacaoHistorica" onDataLoadCallBack="dataLoad">

		<adsm:hidden property="tpMeioTransporte" value="R" serializable="false"/>
		
		<adsm:lookup property="filial" labelWidth="18%" dataType="text" action="municipios/manterFiliais" onDataLoadCallBack="dataLoadFilial"
			maxLength="3" service="lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.findLookupFilial" onchange="return changeFilial();"
			label="filial" idProperty="idFilial" criteriaProperty="sgFilial" required="true" width="32%" size="3" onPopupSetValue="popUpFilial" disabled="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia" />
			<adsm:propertyMapping modelProperty="moeda.siglaSimbolo" relatedProperty="moeda.dsMoeda"/>
			
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:textbox dataType="JTDateTimeZone" label="dataSimulacao" property="dhCriacao" labelWidth="18%" width="32%" required="true" disabled="true"/>

		<adsm:textbox dataType="text" property="dsParamSimulacaoHistorica" label="descricao" size="34"  maxLength="60" labelWidth="18%" width="32%" required="true"/>
		<adsm:combobox property="blPercentual" onchange="changeBlPercentual();" label="tipoSimulacao" domain="DT_TIPO_SIMULACAO_REAJUSTE" labelWidth="18%" width="32%" required="true"/>


		<adsm:textbox dataType="text" label="moeda" property="moeda.dsMoeda" labelWidth="18%" width="32%" size="10" disabled="true"/>

		<adsm:section caption="parametrosParaSimulacaoHistorica"/>
		
		<adsm:combobox property="tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega" label="tipoTabela" labelWidth="18%" boxWidth="180" onlyActiveValues="true"
						service="lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.findComboTipoTabelaColetaEntrega"
						optionProperty="idTipoTabelaColetaEntrega" optionLabelProperty="dsTipoTabelaColetaEntrega" width="80%" onchange="comboChangeTipoTabelaColetaEntrega_idTipoTabelaColetaEntrega()"/>

		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" boxWidth="180" cellStyle="vertical-align:bottom;" labelWidth="18%"
						optionProperty="idTipoMeioTransporte" optionLabelProperty="dsTipoMeioTransporte" label="tipoMeioTransporte" width="80%" onlyActiveValues="true"
						service="lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.findComboTipoMeioTransporte" onchange="comboChangeTipoMeioTransporte_idTipoMeioTransporte()"/>

		<adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte" onchange="return onLookupChangeMeioTransporteRodoviario2()"
			service="lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.findLookupMeioTransporteRodoviario" onDataLoadCallBack="onLookupDataLoadMeioTransporteRodoviario2"
			picker="false" action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota" onPopupSetValue="setPopUpValue"
			label="meioTransporte" labelWidth="18%" width="8%" size="7" maxLength="6" serializable="false" cellStyle="vertical-align:bottom">
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte" modelProperty="idMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />

			<adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="meioTransporte.tpVinculo.value" modelProperty="meioTransporte.tpVinculo.value" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" blankFill="false"/>
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"/>
		</adsm:lookup>

		<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte"  onPopupSetValue="setPopUpValue" onDataLoadCallBack="onLookupDataLoadMeioTransporteRodoviario"
			service="lms.fretecarreteirocoletaentrega.simularAplicarReajustesTabelasFreteHistoricoAction.findLookupMeioTransporteRodoviario"
			action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
			width="69%" size="25" cellStyle="vertical-align:bottom" maxLength="25" onchange="return onLookupChangeMeioTransporteRodoviario();"> 
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota"/>
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte" modelProperty="idMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota"/>

			<adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="meioTransporte.tpVinculo.value" modelProperty="meioTransporte.tpVinculo.value" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" blankFill="false"/>
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"/>
		</adsm:lookup>
		<adsm:hidden property="meioTransporte.tpVinculo.value" serializable="false"/>
		

		<adsm:range label="periodoEmissaoNotasCredito" labelWidth="18%" width="80%">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" cellStyle="vertical-align:bottom" onchange="changeDatas()"/>
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal"   cellStyle="vertical-align:bottom" onchange="changeDatas()"/>
		</adsm:range>

		<adsm:section caption="parcelas" width="100%"/>
		<adsm:label key="parcela"  width="10%" style="height:22px;text-Align:center;"/>
		<adsm:label key="quantidade" width="15%" style="height:22px;text-Align:center;"/>
		<adsm:label key="valorAtual" width="15%" style="height:22px;text-Align:center;"/>
		<adsm:label key="totalAtual" width="15%" style="height:22px;text-Align:center;"/>
		<adsm:label key="valorReajustado" width="15%" style="height:22px;text-Align:center;"/>		
		<adsm:label key="percentualReajuste" width="15%" style="height:22px;text-Align:center;"/>
		<adsm:label key="totalReajuste" width="15%" style="height:22px;text-Align:center;"/>

		<adsm:label key="diaria" width="10%"  style="height:23px" />
		<adsm:hidden property="ncParcelaSimulacao:0.idNcParcelaSimulacao"/>
		<adsm:textbox dataType="integer"  property="ncParcelaSimulacao:0.qtNcParcelaSimulacao" onchange="return controlChangeValuesGrid(this);" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="16"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:0.vlOriginal" onchange="return controlChangeValuesGrid(this);" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="19"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:0.vlAtual" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" disabled="true" maxLength="19"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:0.vlReajuste" onchange="return validateRegra312_315(this)" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="19"/>
		<adsm:textbox dataType="percent"  property="ncParcelaSimulacao:0.pcReajuste" onchange="return validateRegra312_315(this)" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="5"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:0.vlReajustado" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" disabled="true" maxLength="19"/>

		<adsm:label key="evento" width="10%" style="height:23px" />
		<adsm:hidden property="ncParcelaSimulacao:1.idNcParcelaSimulacao"/>
		<adsm:textbox dataType="integer" property="ncParcelaSimulacao:1.qtNcParcelaSimulacao" onchange="return controlChangeValuesGrid(this);" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="16"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:1.vlOriginal" onchange="return controlChangeValuesGrid(this);" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="19"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:1.vlAtual" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" disabled="true" maxLength="19"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:1.vlReajuste" size="11" width="15%" onchange="return validateRegra312_315(this)" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="19"/>
		<adsm:textbox dataType="percent" property="ncParcelaSimulacao:1.pcReajuste" size="11" width="15%" onchange="return validateRegra312_315(this)" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="5"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:1.vlReajustado" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" disabled="true" maxLength="19"/>
		
		<adsm:hidden property="ncParcelaSimulacao:2.idNcParcelaSimulacao"/>
		<adsm:label key="fracaoPeso" width="10%" style="height:23px" />
		<adsm:textbox dataType="integer" property="ncParcelaSimulacao:2.qtNcParcelaSimulacao" onchange="return controlChangeValuesGrid(this);" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="16"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:2.vlOriginal" onchange="return controlChangeValuesGrid(this);" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="19"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:2.vlAtual" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" disabled="true" maxLength="19"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:2.vlReajuste"  onchange="return validateRegra312_315(this)" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="19"/>
		<adsm:textbox dataType="percent" property="ncParcelaSimulacao:2.pcReajuste" onchange="return validateRegra312_315(this)" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="5"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:2.vlReajustado" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" disabled="true" maxLength="19"/>
		
		<adsm:hidden property="ncParcelaSimulacao:3.idNcParcelaSimulacao"/>
		<adsm:label key="kmExcedente" width="10%" style="height:23px" />
		<adsm:textbox dataType="integer" property="ncParcelaSimulacao:3.qtNcParcelaSimulacao" onchange="return controlChangeValuesGrid(this);" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="16"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:3.vlOriginal" onchange="return controlChangeValuesGrid(this);" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="19"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:3.vlAtual" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" disabled="true" maxLength="19"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:3.vlReajuste" onchange="return validateRegra312_315(this)" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;"  maxLength="19"/>
		<adsm:textbox dataType="percent" property="ncParcelaSimulacao:3.pcReajuste" onchange="return validateRegra312_315(this)" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" maxLength="5"/>
		<adsm:textbox dataType="currency" property="ncParcelaSimulacao:3.vlReajustado" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;" disabled="true" maxLength="19"/>
		
		 
		<adsm:label key="branco" width="40%" style="height:23px;border:none" />
		<adsm:textbox dataType="currency"  disabled="true" size="11" property="vlTotalAtual" width="15%"  cellStyle="text-Align:center;height:23px;vertical-align=bottom;"/>
		<adsm:label key="branco" width="30%" style="height:23px;border:none" />
		<adsm:textbox dataType="currency" property="vlTotalReajustado" disabled="true" size="11" width="15%" cellStyle="text-Align:center;height:23px;vertical-align=bottom;"/>

		<adsm:buttonBar>
			<adsm:button caption="carregarDadosHistoricos" id="carregarDadosHistoricos" onclick="onClickCarregarDadosHistorico()" disabled="false"/>
			<adsm:button caption="carregarSimulacao" onclick="carregaSimulacao()" id="carregarSimulacao" cmd="main"/>
			<adsm:storeButton callbackProperty="storeCustom"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>