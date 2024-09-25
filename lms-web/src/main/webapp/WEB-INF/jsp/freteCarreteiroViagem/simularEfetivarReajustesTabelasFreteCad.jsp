 <%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
<!--

	/*
	* AQUI SÃO AS FUNCIONALIDADES NECESSARIAS PARA O COMPORTAMENTO INTEGRADO DO PAIS COM A MOEDA
	*/
	var idMoedaPais = undefined;
	function changeP() {
		var flag = moedaPais_pais_nmPaisOnChangeHandler();
		if (flag == true && getElementValue("moedaPais.pais.nmPais") == "")
		   resetValue("moedaPais.idMoedaPais");
		return flag;
	}
	function dataLoadP_cb(data) {
		return dataLoadP_default(lookupExactMatch({e:document.getElementById("moedaPais.pais.idPais"), data:data, callBack:"dataLoadP_LikeEnd"}));
	}
	function dataLoadP_LikeEnd_cb(data,exception) {
		return dataLoadP_default(moedaPais_pais_nmPais_likeEndMatch_cb(data));
	}
	function dataLoadP_default(flag) {
		if (flag == true)
			loadMoedas();
		return flag;
	}
	function popUpP(data) {
		setElementValue("moedaPais.pais.idPais",getNestedBeanPropertyValue(data,"idPais"));
		loadMoedas();
		return true;
	}
	function loadMoedas() {
	    var e = document.getElementById("moedaPais.idMoedaPais");
	    var sdo = createServiceDataObject(e.service, e.callBack, {pais:{idPais:getElementValue("moedaPais.pais.idPais")}});
	    xmit({serviceDataObjects:[sdo]});
	    notifyElementListeners({e:document.getElementById("moedaPais.pais.idPais")});
	}
	function dataLoadMoeda_cb(data) {
		moedaPais_idMoedaPais_cb(data);
		for (var x = 0; x < data.length; x++) {
			if ((idMoedaPais == undefined && getNestedBeanPropertyValue(data[x],"blIndicadorMaisUtilizada") == "true") ||
				 idMoedaPais == getNestedBeanPropertyValue(data[x],"idMoedaPais")) {
				document.getElementById("moedaPais.idMoedaPais").selectedIndex = x + 1;
				idMoedaPais = undefined;
				break;
			}
		}
		comboboxChange({e:document.getElementById("moedaPais.idMoedaPais")});
	}
	/*
	* AQUI É O FIM DAS FUNCIONALIDADES NECESSARIAS
	*/
	/*
	*	INITWINDOW
	* Regras: 3.1
	*/
	function initWindow(eventObj) { 
		if (eventObj.name == "newButton_click" || eventObj.name == "tab_click") {
			setElementValue("tpReajuste","P");
			behavior();
			tpReajusteChange();	
			idMoedaPais = undefined;
			setDisabled("emitir",true);
			setDisabled("efetivar",true);
			setDisabled("remover",true);
		}
		
	}
	/*
	* Regra 3.2
	*/
	function tpReajusteChange(field) {
		var isDataLoad = (field == undefined);
		if (field != undefined)
			comboboxChange({e:field});
		else
			field = document.getElementById("tpReajuste");
			
		var vlReajuste = document.getElementById("vlReajuste");
		
		if (getElementValue(field) == "V") {
			if (getElementValue("tpSituacaoRota.value") != "E") {
				setDisabled("moedaPais.pais.idPais",false);
				setDisabled("moedaPais.idMoedaPais",false);
			}
			if (!isDataLoad)
				writeDataSession();
			vlReajuste.dataType = "currency";
			if (!isDataLoad)
				setElementValue(vlReajuste,"");
			vlReajuste.mask = "##,###,###,###,##0.000";
		}else{
			if (!isDataLoad) {
				resetValue("moedaPais.pais.idPais");
				resetValue("moedaPais.idMoedaPais");
			}
			setDisabled("moedaPais.pais.idPais",true);
			setDisabled("moedaPais.idMoedaPais",true);
			vlReajuste.dataType = "percent";
			if (!isDataLoad)
				setElementValue(vlReajuste,"");
			vlReajuste.mask = "##0.00";
		}
	}
	
	/*
	*	BUSCA O PAIS DA SESSÃO
	*/
	function pageLoad_cb(data) {
		onPageLoad_cb(data)
		var sdo = createServiceDataObject("lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findPaisSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}
	
	var idPais = null;
	var nmPais = null;
	var idRegional = null;
	function dataSession_cb(data) {
		idPais = getNestedBeanPropertyValue(data,"idPais");
		nmPais = getNestedBeanPropertyValue(data,"nmPais");
		idRegional = getNestedBeanPropertyValue(data,"regional.id");
		writeDataSession();
	}
	function writeDataSession() {
		if (idPais != null &&
			nmPais != null) {
			setElementValue("moedaPais.pais.idPais",idPais);
			setElementValue("moedaPais.pais.nmPais",nmPais);
			loadMoedas();
		}
		if (idRegional != null) {
			setElementValue("regionalOrigem.idRegional",idRegional);
			setDisabled("regionalOrigem.idRegional",true);
			document.getElementById("regionalOrigem.idRegional").masterLink = "true";
			changeCombo(undefined,true);
			document.getElementById("dtVigenciaInicialRO").masterLink = "true";
			document.getElementById("dtVigenciaFinalRO").masterLink = "true";
		}
	}
	/**
	 * Regras 3.6/3.7
	 */
	function contentChangeF(eventObj) {
		if (eventObj.name == "modifyButton_click") {
			resetValue("rotasT");
			resetValue("rotasT_rotaIdaVolta.idRotaIdaVolta");
			resetValue("rotasT_rotaViagem.dsRota");
		}
	}
	function contentChangeT(eventObj) {
		if (eventObj.name == "modifyButton_click") {
			resetValue("rotasF");
			resetValue("rotasF_rotaIdaVolta.idRotaIdaVolta");
			resetValue("rotasF_rotaViagem.dsRota");
		}
	}
	/**
	 * ON DATA LOAD
	 */
	 function dataLoad_cb(data,error) {
		var vlReajuste = document.getElementById("vlReajuste");
	
		if (getNestedBeanPropertyValue(data,"tpReajuste.value") == "V") {
			vlReajuste.dataType = "currency";
	 		vlReajuste.mask = "##,###,###,###,##0.000";
		}else{
			vlReajuste.dataType = "percent";
			vlReajuste.mask = "##0.00";
		}
	 	onDataLoad_cb(data,error);
	 	behavior();
	 	idMoedaPais = getNestedBeanPropertyValue(data,"moedaPais.idMoedaPais");
	 	tpReajusteChange();
	 	if (idMoedaPais != undefined)
		 	loadMoedas();

		changeCombo(undefined,true);
		changeCombo(undefined,false);
	 }
	 
	 function emitirReajuste() {
		var tabGroup = getTabGroup(this.document);
		if (tabGroup.getTab("cad").hasChanged())
			alert(i18NLabel.getLabel("LMS-24030"));
		else
			reportButtonScript('lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction', 'openPdf', document.forms[0]);
	 }
	 function efetivarReajuste() {
		var tabGroup = getTabGroup(this.document);
		if (tabGroup.getTab("cad").hasChanged())
			alert(i18NLabel.getLabel("LMS-24031"));
		else{
		 	var sdo = createServiceDataObject("lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.executeEfetivacao", "efetivarReajuste", buildFormBeanFromForm(document.forms[0])); 
			xmit({serviceDataObjects:[sdo]});
		}
	}
	function efetivarReajuste_cb(data,error) {
		onDataLoad_cb(data,error);		
		if (error == undefined)
			behavior();
		else
			alert(error);
	}
	
	function behavior() {
		if (getElementValue("tpSituacaoRota.value") == "E") {
			setDisabled(document,true);
			setDisabled("limpar",false);
			setFocus("limpar",false);
		}else{
			setDisabled(document,false);
			setDisabled("proprietario.pessoa.nmPessoa",true);
			setDisabled("rotasT_rotaViagem.dsRota",true);
			setDisabled("rotasF_rotaViagem.dsRota",true);
			setDisabled("filialOrigem.pessoa.nmFantasia",true);
			setDisabled("filialDestino.pessoa.nmFantasia",true);
			setDisabled("tpSituacaoRota.description",true);
			setDisabled("dtVigenciaInicialRO",true);
			setDisabled("dtVigenciaInicialRD",true);
			setDisabled("dtVigenciaFinalRO",true);
			setDisabled("dtVigenciaFinalRD",true);
			
			setFocusOnFirstFocusableField();
		}
	}
	
	function changeCombo(field,isRegionalOrigem) {
		if (field != undefined)
			comboboxChange({e:field});
		else
			field = document.getElementById(((isRegionalOrigem) ? "regionalOrigem.idRegional" : "regionalDestino.idRegional"));
		var dtVigenciaI = document.getElementById("dtVigenciaInicialR" + ((isRegionalOrigem) ? "O" : "D"));
		var dtVigenciaF = document.getElementById("dtVigenciaFinalR" + ((isRegionalOrigem) ? "O" : "D"));
		if (field.selectedIndex > 0) {
			var data = field.data[field.selectedIndex - 1];
			setElementValue(dtVigenciaI,setFormat(dtVigenciaI,getNestedBeanPropertyValue(data,"dtVigenciaInicial")));
			setElementValue(dtVigenciaF,setFormat(dtVigenciaF,getNestedBeanPropertyValue(data,"dtVigenciaFinal")));
		}else{
			resetValue(dtVigenciaI);
			resetValue(dtVigenciaF);
		}		
	}
	
	function filialDChange() {
		var flag = filialDestino_sgFilialOnChangeHandler();
		changeCombo(undefined,false);
		return flag;
	}
	
	function filialDCallBack_cb(data) {
		filialDestino_sgFilial_exactMatch_cb(data);
		changeCombo(undefined,false);
	}
	function filialDPopUp(data) {
		setElementValue("regionalDestino.idRegional",getNestedBeanPropertyValue(data,"lastRegional.idRegional"));
		changeCombo(undefined,false);
		return true;
	}
	
	function filialOChange() {
		var flag = filialOrigem_sgFilialOnChangeHandler();
		changeCombo(undefined,true);
		return flag;
	}
	function filialOCallBack_cb(data) {
		filialOrigem_sgFilial_exactMatch_cb(data);
		changeCombo(undefined,true);
	}
	function filialOPopUp(data) {
		setElementValue("regionalOrigem.idRegional",getNestedBeanPropertyValue(data,"lastRegional.idRegional"));
		changeCombo(undefined,true);
		return true;
	}
	
	function regionalOrigem_cb(data) {
		regionalOrigem_idRegional_cb(data);
		if (idRegional != null) {
			setElementValue("regionalOrigem.idRegional",idRegional);
			setDisabled("regionalOrigem.idRegional",true);
			document.getElementById("regionalOrigem.idRegional").masterLink = "true";
			changeCombo(undefined,true);
			document.getElementById("dtVigenciaInicialRO").masterLink = "true";
			document.getElementById("dtVigenciaFinalRO").masterLink = "true";
		}
	}
	
	function nrFrotaDataLoad_cb(data) {
		meioTransporte2_nrFrota_exactMatch_cb(data);
		treatTipoMeioTransporte();
	}
	
	function nrIdentificadorDataLoad_cb(data) {
		meioTransporte_nrIdentificador_exactMatch_cb(data);
		treatTipoMeioTransporte();
	}
	
	function nrIdentificadorPopup(data) {
		treatTipoMeioTransporte();
	}

	function treatTipoMeioTransporte() {
		if (getElementValue("tipoMeioTransporte.idTipoMeioTransporte") == undefined ||
				getElementValue("tipoMeioTransporte.idTipoMeioTransporte") == "") {
			document.getElementById("tipoMeioTransporte.idTipoMeioTransporte").selectedIndex = 0;
		}
	}

//-->
</script>
<adsm:window service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/freteCarreteiroViagem/simularEfetivarReajustesTabelasFrete" idProperty="idSimulacaoReajusteRota" onDataLoadCallBack="dataLoad">
		<adsm:hidden property="tpSituacao" serializable="false" value="A"/>
		<adsm:hidden property="vigentes" serializable="false" value="S"/>

		<adsm:i18nLabels>
			<adsm:include key="LMS-24030"/>
			<adsm:include key="LMS-24031"/>
		</adsm:i18nLabels>
		
		<adsm:textbox dataType="text" property="dsSimulacaoReajusteRota" label="descricaoReajuste" maxLength="60" size="28" labelWidth="18%" width="32%" required="true"/>
		<adsm:textbox dataType="text" property="tpSituacaoRota.description" label="situacaoReajuste" maxLength="60" size="25" labelWidth="18%" width="32%" disabled="true"/>
		<adsm:hidden property="tpSituacaoRota.value" serializable="false"/>
		<adsm:combobox property="tpReajuste" label="tipoReajuste" domain="DM_TIPO_REAJUSTE_ROTA" labelWidth="18%" width="82%" onchange="tpReajusteChange(this)" required="true"/>
		
		<adsm:lookup property="moedaPais.pais" idProperty="idPais" service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findLookupPais"
					dataType="text" criteriaProperty="nmPais" label="pais" size="35" maxLength="60" action="/municipios/manterPaises"
					exactMatch="false" minLengthForAutoPopUpSearch="3" onDataLoadCallBack="dataLoadP" onPopupSetValue="popUpP"
					labelWidth="18%" width="32%" onchange="return changeP();">
					<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
		</adsm:lookup>
		
		<adsm:combobox service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findComboMoedaPais"
				 optionLabelProperty="moeda.siglaSimbolo" optionProperty="idMoedaPais" label="moeda" property="moedaPais.idMoedaPais"
				 onDataLoadCallBack="dataLoadMoeda" labelWidth="18%" width="32%" autoLoad="false">
			<adsm:propertyMapping criteriaProperty="moedaPais.pais.idPais" modelProperty="pais.idPais"/>
		</adsm:combobox>
		

		<adsm:textbox dataType="currency" property="vlReajuste" label="valorDoReajuste" labelWidth="18%" width="82%" required="true"/>
		
		<adsm:range label="vigenciaReajuste" labelWidth="18%" width="82%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>		
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>		
		</adsm:range>
		
		<adsm:section caption="criteriosGeraisSimulacao" />
		
		<adsm:combobox property="regionalOrigem.idRegional" label="regionalOrigem" onchange="changeCombo(this,true)"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findComboRegional" onDataLoadCallBack="regionalOrigem"
				optionLabelProperty="siglaDescricao" optionProperty="idRegional" labelWidth="18%" width="32%" boxWidth="200" />

		<adsm:lookup property="filialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" exactMatch="false" labelWidth="18%"
				dataType="text" size="3" maxLength="3"  minLengthForAutoPopUpSearch="3" action="/municipios/manterFiliais" label="filialOrigem"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findLookupFilial" width="32%"
				onDataLoadCallBack="filialOCallBack" onPopupSetValue="filialOPopUp" onchange="return filialOChange();">
			<adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="regionalOrigem.idRegional" modelProperty="regionalFiliais.regional.idRegional"/>
			<adsm:propertyMapping relatedProperty="regionalOrigem.idRegional" modelProperty="lastRegional.idRegional" blankFill="false"/>
			<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" disabled="true" serializable="false" size="30"/>
		</adsm:lookup>

			
		<adsm:range label="vigencia" labelWidth="18%" width="82%">
			<adsm:textbox size="12" property="dtVigenciaInicialRO" dataType="JTDate" picker="false" disabled="true" serializable="false"/>
			<adsm:textbox size="12" property="dtVigenciaFinalRO" dataType="JTDate" picker="false" disabled="true" serializable="false"/>
		</adsm:range>
		
		
		<adsm:combobox property="regionalDestino.idRegional" label="regionalDestino" onchange="changeCombo(this,false)"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findComboRegional"
				optionLabelProperty="siglaDescricao" optionProperty="idRegional" labelWidth="18%" width="32%" boxWidth="200" />

		<adsm:lookup property="filialDestino" idProperty="idFilial" criteriaProperty="sgFilial" exactMatch="false" labelWidth="18%"
				dataType="text" size="3" maxLength="3"  minLengthForAutoPopUpSearch="3" action="/municipios/manterFiliais" label="filialDestino"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findLookupFilial" width="32%"
				onDataLoadCallBack="filialDCallBack" onPopupSetValue="filialDPopUp" onchange="return filialDChange();">
			<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="regionalDestino.idRegional" modelProperty="regionalFiliais.regional.idRegional"/>
			<adsm:propertyMapping relatedProperty="regionalDestino.idRegional" modelProperty="lastRegional.idRegional" blankFill="false"/>
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" disabled="true" serializable="false" size="30"/>
		</adsm:lookup>  
		
		<adsm:range label="vigencia" labelWidth="18%" width="82%">
			<adsm:textbox size="12" property="dtVigenciaInicialRD" dataType="JTDate" picker="false" disabled="true" serializable="false"/>
			<adsm:textbox size="12" property="dtVigenciaFinalRD" dataType="JTDate" picker="false" disabled="true" serializable="false"/>
		</adsm:range>

		<adsm:combobox property="tpRota" label="tipoRota" domain="DM_TIPO_ROTA_VIAGEM" labelWidth="18%" width="32%"/>


		<adsm:lookup dataType="text" property="proprietario" idProperty="idProprietario" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction.findLookupProprietario" width="82%" size="20" maxLength="20"
				action="/contratacaoVeiculos/manterProprietarios" criteriaProperty="pessoa.nrIdentificacao" label="proprietario" labelWidth="18%">
			<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" serializable="false" size="35" disabled="true"/>
		</adsm:lookup>


		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" boxWidth="180" cellStyle="vertical-align:bottom;" labelWidth="18%"
			optionProperty="idTipoMeioTransporte" optionLabelProperty="dsTipoMeioTransporte" label="tipoMeioTransporte" width="32%"
			service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findComboTipoMeioTransporte" onlyActiveValues="true">		
				<adsm:propertyMapping relatedProperty="tpMeioTransporte" modelProperty="tpMeioTransporte.value"/>
		</adsm:combobox>
		<adsm:hidden property="tpMeioTransporte" serializable="false"/>
			
		<adsm:lookup dataType="text" property="meioTransporte2" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom;" criteriaProperty="nrFrota"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findLookupMeioTransporte" picker="false" maxLength="6" size="7"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" label="meioTransporte" labelWidth="18%" width="32%" serializable="false"
				onDataLoadCallBack="nrFrotaDataLoad" >
			<adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador"/>
			<adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte"/>		
			<adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador"/>
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" blankFill="false"/>
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario"/>
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario2.pessoa.nmPessoa" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="tpMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte.value" inlineQuery="false"/>
		
			<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporte" criteriaProperty="nrIdentificador"
					service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findLookupMeioTransporte" picker="true" maxLength="25"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" size="20" cellStyle="vertical-align:bottom;"
					afterPopupSetValue="nrIdentificadorPopup"
					onDataLoadCallBack="nrIdentificadorDataLoad" >
				<adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota" modelProperty="nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte" modelProperty="idMeioTransporte"/>	
				<adsm:propertyMapping relatedProperty="meioTransporte2.nrFrota" modelProperty="nrFrota"/>	
				<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"/>
				<adsm:propertyMapping relatedProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" blankFill="false"/>
				<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario"/>
				<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario2.pessoa.nmPessoa" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
				<adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" inlineQuery="false"/>
				<adsm:propertyMapping relatedProperty="tpMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte.value" inlineQuery="false"/>
			</adsm:lookup>
		
		</adsm:lookup>   
			

		<adsm:listbox property="rotasF" optionProperty="idRota" optionLabelProperty="dsRota" label="reajusteNaoSeAplicaPara" size="4"
				 labelWidth="18%" width="32%" showOrderControls="false" boxWidth="230" showIndex="false" serializable="true" onContentChange="contentChangeF">
			<adsm:lookup service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findLookupRotasViagem" dataType="integer"
						criteriaProperty="nrRota" size="4"action="/municipios/consultarRotas" cmd="idaVolta" disabled="false" mask="0000"
						maxLength="4" exactMatch="false" cellStyle="vertical-align:bottom;" idProperty="idRotaIdaVolta" property="rotaIdaVolta">
	                  <adsm:propertyMapping relatedProperty="rotasF_rotaViagem.dsRota" modelProperty="rota.dsRota"/>
	                  <adsm:propertyMapping criteriaProperty="tpRota" modelProperty="tpRota"/>

	                  <adsm:propertyMapping criteriaProperty="dtVigenciaInicialRO" modelProperty="dtVigenciaInicialRO"/>
	                  <adsm:propertyMapping criteriaProperty="dtVigenciaFinalRO" modelProperty="dtVigenciaFinalRO"/>
	                  <adsm:propertyMapping criteriaProperty="dtVigenciaInicialRD" modelProperty="dtVigenciaInicialRD"/>
	                  <adsm:propertyMapping criteriaProperty="dtVigenciaFinalRD" modelProperty="dtVigenciaFinalRD"/>
	                  <adsm:propertyMapping criteriaProperty="meioTransporte.idMeioTransporte" modelProperty="meioTransporte.idMeioTransporte"/>
	                  <adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador"/>
	                  <adsm:propertyMapping criteriaProperty="meioTransporte2.idMeioTransporte" modelProperty="meioTransporte2.idMeioTransporte"/>
	                  <adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota" modelProperty="meioTransporte2.nrFrota"/>
	                  <adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="tipoMeioTransporte.idTipoMeioTransporte"/>
	                  <adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario"/>
	                  <adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao"/>
	                  <adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa"/>
	                  <adsm:propertyMapping criteriaProperty="regionalOrigem.idRegional" modelProperty="regionalOrigem.idRegional"/>
	                  <adsm:propertyMapping criteriaProperty="regionalDestino.idRegional" modelProperty="regionalDestino.idRegional"/>		
	                  <adsm:propertyMapping criteriaProperty="vigentes" modelProperty="vigentes"/>
		
	                  <adsm:propertyMapping criteriaProperty="filialOrigem.idFilial" modelProperty="filialOrigem.idFilial"/>
	                  <adsm:propertyMapping criteriaProperty="filialOrigem.sgFilial" modelProperty="filialOrigem.sgFilial"/>
	                  <adsm:propertyMapping criteriaProperty="filialOrigem.pessoa.nmFantasia" modelProperty="filialOrigem.nmFilial"/>
	                  <adsm:propertyMapping criteriaProperty="filialDestino.idFilial" modelProperty="filialDestino.idFilial"/>
	                  <adsm:propertyMapping criteriaProperty="filialDestino.sgFilial" modelProperty="filialDestino.sgFilial"/>
	                  <adsm:propertyMapping criteriaProperty="filialDestino.pessoa.nmFantasia" modelProperty="filialDestino.nmFilial"/>
	                  <adsm:textbox dataType="text" property="rotaViagem.dsRota" size="18" cellStyle="vertical-align:bottom;" disabled="true" serializable="false"/>
	        </adsm:lookup>
	   </adsm:listbox>  
	   
	   <adsm:listbox property="rotasT" optionProperty="idRota" optionLabelProperty="dsRota" label="reajusteSeAplicaPara" size="4"
				 labelWidth="18%" width="32%" showOrderControls="false" boxWidth="230" showIndex="false" serializable="true" onContentChange="contentChangeT">
			<adsm:lookup service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findLookupRotasViagem" dataType="integer"
						criteriaProperty="nrRota" exactMatch="false" action="/municipios/consultarRotas" cmd="idaVolta" disabled="false"
						maxLength="4" size="4" cellStyle="vertical-align:bottom;" mask="0000" idProperty="idRotaIdaVolta" property="rotaIdaVolta">
	                  <adsm:propertyMapping relatedProperty="rotasT_rotaViagem.dsRota" modelProperty="rota.dsRota"/>
	                  <adsm:propertyMapping criteriaProperty="tpRota" modelProperty="tpRota"/>
	                  
	                  <adsm:propertyMapping criteriaProperty="dtVigenciaInicialRO" modelProperty="dtVigenciaInicialRO"/>
	                  <adsm:propertyMapping criteriaProperty="dtVigenciaFinalRO" modelProperty="dtVigenciaFinalRO"/>
	                  <adsm:propertyMapping criteriaProperty="dtVigenciaInicialRD" modelProperty="dtVigenciaInicialRD"/>
	                  <adsm:propertyMapping criteriaProperty="dtVigenciaFinalRD" modelProperty="dtVigenciaFinalRD"/>
	                  <adsm:propertyMapping criteriaProperty="meioTransporte.idMeioTransporte" modelProperty="meioTransporte.idMeioTransporte"/>
	                  <adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador"/>
	                  <adsm:propertyMapping criteriaProperty="meioTransporte2.idMeioTransporte" modelProperty="meioTransporte2.idMeioTransporte"/>
	                  <adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota" modelProperty="meioTransporte2.nrFrota"/>
	                  <adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="tipoMeioTransporte.idTipoMeioTransporte"/>
	                  <adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario"/>
	                  <adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao"/>
	                  <adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa"/>
	                  <adsm:propertyMapping criteriaProperty="regionalOrigem.idRegional" modelProperty="regionalOrigem.idRegional"/>
	                  <adsm:propertyMapping criteriaProperty="regionalDestino.idRegional" modelProperty="regionalDestino.idRegional"/>
	                  <adsm:propertyMapping criteriaProperty="vigentes" modelProperty="vigentes"/>

	                  <adsm:propertyMapping criteriaProperty="filialOrigem.idFilial" modelProperty="filialOrigem.idFilial"/>
	                  <adsm:propertyMapping criteriaProperty="filialOrigem.sgFilial" modelProperty="filialOrigem.sgFilial"/>
	                  <adsm:propertyMapping criteriaProperty="filialOrigem.pessoa.nmFantasia" modelProperty="filialOrigem.nmFilial"/>
	                  <adsm:propertyMapping criteriaProperty="filialDestino.idFilial" modelProperty="filialDestino.idFilial"/>
	                  <adsm:propertyMapping criteriaProperty="filialDestino.sgFilial" modelProperty="filialDestino.sgFilial"/>
	                  <adsm:propertyMapping criteriaProperty="filialDestino.pessoa.nmFantasia" modelProperty="filialDestino.nmFilial"/>
	                  <adsm:textbox dataType="text" property="rotaViagem.dsRota" size="18" cellStyle="vertical-align:bottom;" disabled="true" serializable="false"/>
	        </adsm:lookup>
	   </adsm:listbox>

		<adsm:buttonBar>
			<adsm:button id="emitir" caption="emitir" onclick="emitirReajuste()"/>
			<adsm:button id="efetivar" caption="efetivarReajuste" onclick="efetivarReajuste()"/>
			<adsm:storeButton id="salvar"/>
			<adsm:newButton id="limpar"/>
			<adsm:removeButton id="remover"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>