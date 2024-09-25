<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function popPais(data) {
		enabledDisabledPercent();
		return true;
	}
	function cbPais_cb(data,exception) {
		var flag = moedaPais_pais_nmPais_exactMatch_cb(data);
		if (flag)
			enabledDisabledPercent();
		return flag;
	}
	/* foi comentando por causa que estava limpando o valor ..
	function changePais() {
		var flag = moedaPais_pais_nmPaisOnChangeHandler();
		if (flag && getElementValue("pais) 
			enabledDisabledPercent();
		return flag;
	}
	*/
	function enabledDisabledPercent(isPageLoad) {
		if (getElementValue("moedaPais.idMoedaPais") != "" &&
			getElementValue("tipoCombustivel.idTipoCombustivel") != "" &&
			getElementValue("dtVigenciaInicial") != "") {
			var sdo = createServiceDataObject("lms.fretecarreteiroviagem.manterValoresCombustivelAction.validateEnabledPercentual",
						((isPageLoad == true) ? "validatePercentPageLoad" : "validatePercent"),{idMoedaPais:getElementValue("moedaPais.idMoedaPais"),idTipoCombustivel:getElementValue("tipoCombustivel.idTipoCombustivel")
						,dtVigenciaInicial:getElementValue("dtVigenciaInicial")});
			xmit({serviceDataObjects:[sdo]});
		}else
			clearPercent();
	}
	function validatePercent_cb(data,execption) {
		if (getNestedBeanPropertyValue(data,"_value") == "false") {
			clearPercent();
			return;
		}
		setDisabled("pcAumento",false);
		setElementValue("vlOld",getNestedBeanPropertyValue(data,"_value"));
	}
	function validatePercentPageLoad_cb(data) {
		if (getNestedBeanPropertyValue(data,"_value") == "false")
			return;
		setDisabled("pcAumento",false);
		setElementValue("vlOld",getNestedBeanPropertyValue(data,"_value"));
	}

	function changeVigenciaInicial(field) {
		var flag = validate(field);
		enabledDisabledPercent();
		return flag;
	}
	function changeMoedaPais(field) {
		comboboxChange({e:field});
		enabledDisabledPercent();
	}
	function changeTipoCombustivel(field) {
		comboboxChange({e:field});
		enabledDisabledPercent();
	}
	function clearPercent() {
		resetValue("pcAumento");
		setDisabled("pcAumento",true);
		resetValue("vlOld");
		resetValue("vlValorCombustivel");
	}
	
	function calculaPercent(field) {
		var flag = validate(field);
		if (flag && getElementValue("vlValorCombustivel") != "") {
			var sdo = createServiceDataObject("lms.fretecarreteiroviagem.manterValoresCombustivelAction.calculaPercentual",
						"onDataLoadCalculo",{vlNow:getElementValue("vlOld"),value:getElementValue("vlValorCombustivel")});
			xmit({serviceDataObjects:[sdo]});
		}
		return flag;
	}
	function calculaValor(field) {
		//var flag = validate(field);
		var flag = validateMinMaxValue(field);
		if (flag && getElementValue("pcAumento") != "") {
			var sdo = createServiceDataObject("lms.fretecarreteiroviagem.manterValoresCombustivelAction.calculaValor",
						"onDataLoadCalculo",{vlNow:getElementValue("vlOld"),value:getElementValue("pcAumento")});
			xmit({serviceDataObjects:[sdo]});
		}
		return flag;
	}
	function onDataLoadCalculo_cb(data,error) {
		if (error != undefined) {
			alert(error);
			resetValue("vlValorCombustivel");
			setFocus("vlValorCombustivel");
			return false;
		}
		if (getNestedBeanPropertyValue(data,"pcAumento")) {
			setElementValue("pcAumento",getNestedBeanPropertyValue(data,"pcAumento"));
		}
		if (getNestedBeanPropertyValue(data,"vlValorCombustivel")) {
			setElementValue("vlValorCombustivel",getNestedBeanPropertyValue(data,"vlValorCombustivel"));
		}
	}
	//Padrão de vigencias...
	function pageLoad_cb(data,error) {
		onDataLoad_cb(data,error);
		acaoVigencia(data);
	}
	
	function acaoVigencia(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			  enabledFields();
			  setDisabled("removeButton",false);
			  enabledDisabledPercent(true);
			  setFocusOnFirstFocusableField();
		}else if (acaoVigenciaAtual == 1) {
		      setDisabled(document,true);
		      setDisabled("storeButton",false);
		      setDisabled("newButton",false);
		      setDisabled("dtVigenciaFinal");
		      setFocusOnFirstFocusableField();
		}else if (acaoVigenciaAtual == 2) {
		      setDisabled(document,true);
		      setDisabled("newButton",false);
		      setFocusOnNewButton();
		}
	}
	function enabledFields() {
		setDisabled(document,false);
		setDisabled("removeButton",true);
		setDisabled("pcAumento",true);
		setDisabled("tipoCombustivel.idTipoCombustivel",document.getElementById("tipoCombustivel.idTipoCombustivel").masterLink == "true");
		setFocusOnFirstFocusableField();
	}
	
	function initWindow(eventObj) {
		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click"))
			enabledFields();
	}
	
	function afterStore_cb(data,exception,key) {
    	store_cb(data,exception,key);
		if (exception == undefined) {
			acaoVigencia(data);
			setFocusOnNewButton();
		}
    }  
//-->
</script>
<adsm:window service="lms.fretecarreteiroviagem.manterValoresCombustivelAction">
	<adsm:form action="/freteCarreteiroViagem/manterValoresCombustivel" idProperty="idValorCombustivel" onDataLoadCallBack="pageLoad">
		<adsm:hidden property="tpSituacao" value="A"/>
		<adsm:lookup property="moedaPais.pais" idProperty="idPais" service="lms.fretecarreteiroviagem.manterValoresCombustivelAction.findLookupPais"
					dataType="text" criteriaProperty="nmPais" label="pais" size="35" maxLength="60" action="/municipios/manterPaises"
					onPopupSetValue="popPais" onDataLoadCallBack="cbPais"
					exactMatch="false" 	minLengthForAutoPopUpSearch="3" labelWidth="19%" width="81%" required="true">
					<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
		</adsm:lookup>
					
		<adsm:combobox property="tipoCombustivel.idTipoCombustivel" label="tipoCombustivel" service="lms.fretecarreteiroviagem.manterValoresCombustivelAction.findComboTpCombustivel" 
						onchange="changeTipoCombustivel(this)" required="true"
						optionProperty="idTipoCombustivel" optionLabelProperty="dsTipoCombustivel" labelWidth="19%" width="81%" onlyActiveValues="true"/>
        
		<adsm:combobox property="moedaPais.idMoedaPais" label="moeda" service="lms.fretecarreteiroviagem.manterValoresCombustivelAction.findComboMoedaPais" onchange="changeMoedaPais(this)"
						optionLabelProperty="moeda.siglaSimbolo" optionProperty="idMoedaPais" labelWidth="19%" width="60%" required="true" onlyActiveValues="true">        
						<adsm:propertyMapping criteriaProperty="moedaPais.pais.idPais" modelProperty="pais.idPais"/>
		</adsm:combobox>

		<adsm:range label="vigencia" labelWidth="19%" width="81%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" onchange="return changeVigenciaInicial(this);"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/> 
		</adsm:range>
		<adsm:hidden property="vlOld"/> 
        <adsm:textbox dataType="percent" onchange="return calculaValor(this);"  
        			  property="pcAumento" label="percentualReajuste" size="20" maxLength="20" 
        			  labelWidth="19%" width="31%" serializable="false" disabled="true" 	
        			  minValue="-100"
					  maxValue="100"
					  mask="##0.000"/>
        <adsm:textbox dataType="currency" mask="##0.000" 
        			  onchange="return calculaPercent(this);" 
        			  property="vlValorCombustivel" label="valor"
        			  size="20" maxLength="20" labelWidth="19%" 
        			  width="31%" required="true" />
		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="afterStore" id="storeButton"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>