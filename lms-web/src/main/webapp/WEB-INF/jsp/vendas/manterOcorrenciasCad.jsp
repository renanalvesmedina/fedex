<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	

  function setaValorComboEventoPce_cb(data,exception){
  	 onPageLoad_cb(data,exception);
  	 if(document.getElementById("idProcessoPceTemp").masterLink == "true"){
 		setElementValue(document.getElementById("eventoPce.processoPce.idProcessoPce"),document.getElementById("idProcessoPceTemp").value);
 		document.getElementById("eventoPce.processoPce.idProcessoPce").masterLink = "true";
		var idProcessoPce = getElementValue("idProcessoPceTemp");
 		var dsProcessoPceCombo = getElementValue("cdProcessoPceTemp") + " - " +
 				 getElementValue("dsProcessoPceTemp") + ((getElementValue("tpModaTemp") == "") ? "" : " - " + getElementValue("tpModaTemp"))
 				 + ((getElementValue("tpAbrangenciaTemp") == "") ? "" : " - " + getElementValue("tpAbrangenciaTemp"));
 		setComboBoxElementValue(document.getElementById("eventoPce.processoPce.idProcessoPce"), idProcessoPce, idProcessoPce, dsProcessoPceCombo);
 		document.getElementById("eventoPce.processoPce.idProcessoPce").disabled = true;
  	   	notifyElementListeners({e:document.getElementById("eventoPce.processoPce.idProcessoPce")});
  	 }
 }
 
 function setaValorCombo_cb(data){
 	eventoPce_idEventoPce_cb(data);
 	
 	if(document.getElementById("idEventoPceTemp").masterLink == "true"){
 		setElementValue(document.getElementById("eventoPce.idEventoPce"),document.getElementById("idEventoPceTemp").value);
 		document.getElementById("eventoPce.idEventoPce").masterLink = "true";
 		var idEventoPce = getElementValue("idEventoPceTemp");
 		var dsEventoPceCombo = getElementValue("cdEventoPceTemp")+ " - " + getElementValue("dsEventoPceTemp");
 		setComboBoxElementValue(document.getElementById("eventoPce.idEventoPce"), idEventoPce, idEventoPce, dsEventoPceCombo);
 		setDisabled("eventoPce.idEventoPce",true);
 	}
 	setDisabled("eventoPce.idEventoPce", false);
 }
 function afterStore_cb(data,exception) {
	store_cb(data,exception);
 }

function buildLinkPropertiesQueryString_descritivos() {

	var qs = "";
	qs += "&idOcorrenciaPceTemp=" + document.getElementById("idOcorrenciaPce").value;
	qs += "&cdOcorrenciaPceTemp=" + document.getElementById("cdOcorrenciaPce").value;
	qs += "&dsOcorrenciaPceTemp=" + document.getElementById("dsOcorrenciaPce").value;
	qs += "&idProcessoPceTemp=" + document.getElementById("idProcessoPceTemp").value;
	qs += "&cdProcessoPceTemp=" + document.getElementById("cdProcessoPceTemp").value;
	qs += "&dsProcessoPceTemp=" + document.getElementById("dsProcessoPceTemp").value;
	qs += "&tpModaTemp=" + document.getElementById("tpModaTemp").value;
	qs += "&tpAbrangenciaTemp=" + document.getElementById("tpAbrangenciaTemp").value;
	qs += "&tpModaValueTemp=" + document.getElementById("tpModaValueTemp").value;
	qs += "&tpAbrangenciaValueTemp=" + document.getElementById("tpAbrangenciaValueTemp").value;
	qs += "&idEventoPceTemp=" + document.getElementById("idEventoPceTemp").value;
	qs += "&cdEventoPceTemp=" + document.getElementById("cdEventoPceTemp").value;
	qs += "&dsEventoPceTemp=" + document.getElementById("dsEventoPceTemp").value;
	return qs;
}

	function ocorrenciasDataLoad_cb(data){
 		onDataLoad_cb(data);
 		if (data != undefined){
 			setElementValue("idProcessoPceTemp", getNestedBeanPropertyValue(data, "eventoPce.processoPce.idProcessoPce"));
 			setElementValue("cdProcessoPceTemp", getNestedBeanPropertyValue(data, "eventoPce.processoPce.cdProcessoPce"));
 			setElementValue("dsProcessoPceTemp", getNestedBeanPropertyValue(data, "eventoPce.processoPce.dsProcessoPce"));
 			setElementValue("tpAbrangenciaValueTemp", getNestedBeanPropertyValue(data, "eventoPce.processoPce.tpAbrangencia.value"));
 			setElementValue("tpModaValueTemp", getNestedBeanPropertyValue(data, "eventoPce.processoPce.tpModal.value"));
 			setElementValue("tpAbrangenciaTemp", getNestedBeanPropertyValue(data, "eventoPce.processoPce.tpAbrangencia.description"));
 			setElementValue("tpModaTemp", getNestedBeanPropertyValue(data, "eventoPce.processoPce.tpModal.description"));
 			setElementValue("idEventoPceTemp", getNestedBeanPropertyValue(data, "eventoPce.idEventoPce"));
 			setElementValue("cdEventoPceTemp", getNestedBeanPropertyValue(data, "eventoPce.cdEventoPce"));
 			setElementValue("dsEventoPceTemp", getNestedBeanPropertyValue(data, "eventoPce.dsEventoPce"));
 		}
	}
  
   function changeProcesso(){
	 	if(getElementValue("eventoPce.processoPce.idProcessoPce") != "") {
		 	setDisabled("eventoPce.idEventoPce", true);
		}
	 	return 	comboboxChange({e:getElement("eventoPce.processoPce.idProcessoPce")});
 	}
 
</script>
<adsm:window service="lms.vendas.manterOcorrenciasAction" onPageLoadCallBack="setaValorComboEventoPce">
	<adsm:form action="/vendas/manterOcorrencias" idProperty="idOcorrenciaPce" onDataLoadCallBack="ocorrenciasDataLoad">
		<adsm:hidden property="eventoPce.tpSituacao" value="A"/>
		<adsm:hidden property="eventoPce.cdEventoPce" />
		<adsm:hidden property="idEventoPceTemp"/>
		<adsm:hidden property="cdEventoPceTemp"/>
		<adsm:hidden property="dsEventoPceTemp"/>
	    <adsm:hidden property="idProcessoPceTemp"/>
	    <adsm:hidden property="cdProcessoPceTemp"/>
		<adsm:hidden property="dsProcessoPceTemp"/>
		<adsm:hidden property="tpModaTemp"/>
		<adsm:hidden property="tpAbrangenciaTemp"/>
		<adsm:hidden property="tpModaValueTemp"/>
		<adsm:hidden property="tpAbrangenciaValueTemp"/>
		
		
 	   <adsm:hidden property="eventoPce.processoPce.cdProcessoPce" serializable="false"/>
 	   <adsm:hidden property="eventoPce.processoPce.tpModal.value" serializable="false"/>
       <adsm:hidden property="eventoPce.processoPce.tpAbrangencia.value" serializable="false"/>
       <adsm:hidden property="eventoPce.processoPce.tpModal.description" serializable="false"/>
       <adsm:hidden property="eventoPce.processoPce.tpAbrangencia.description" serializable="false"/>
       <adsm:hidden property="eventoPce.processoPce.processoPceCombo" serializable="false"/>
       
       
       <adsm:hidden property="eventoPce.eventoPceCombo" serializable="false"/>
		
		<adsm:combobox property="eventoPce.processoPce.idProcessoPce" 
			optionLabelProperty="processoPceCombo" optionProperty="idProcessoPce" 
			service="lms.vendas.manterOcorrenciasAction.findProcessoPceAtivoByUsuarioLogado" 
			label="processo" width="35%" required="true" boxWidth="220"
			onchange="return changeProcesso();">
			<adsm:propertyMapping modelProperty="tpModal.description"       relatedProperty="eventoPce.processoPce.tpModal.description"/>
			<adsm:propertyMapping modelProperty="tpAbrangencia.description" relatedProperty="eventoPce.processoPce.tpAbrangencia.description"/>
			<adsm:propertyMapping modelProperty="dsProcessoPce" relatedProperty="eventoPce.processoPce.processoPceCombo"/>
			<adsm:propertyMapping modelProperty="tpModal.value"       relatedProperty="eventoPce.processoPce.tpModal.value"/>
			<adsm:propertyMapping modelProperty="tpAbrangencia.value" relatedProperty="eventoPce.processoPce.tpAbrangencia.value"/>
		</adsm:combobox>
		
		<adsm:combobox property="eventoPce.idEventoPce" optionLabelProperty="eventoPceCombo" 
			onDataLoadCallBack="setaValorCombo" optionProperty="idEventoPce" 
			service="lms.vendas.manterOcorrenciasAction.find" 
			label="evento" width="35%" required="true" boxWidth="220">
		  <adsm:propertyMapping criteriaProperty="eventoPce.processoPce.idProcessoPce" modelProperty="processoPce.idProcessoPce"/>
		  <adsm:propertyMapping criteriaProperty="eventoPce.tpSituacao" modelProperty="tpSituacao"/>
		  <adsm:propertyMapping relatedProperty="eventoPce.eventoPceCombo" modelProperty="eventoPceCombo"/>
		</adsm:combobox>
		
		<adsm:textbox maxLength="10" size="10" dataType="integer" property="cdOcorrenciaPce" label="codigo" required="true" />
		<adsm:textbox maxLength="60" dataType="text" property="dsOcorrenciaPce" label="ocorrencia" size="40" required="true" />
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true" width="35%"/>
		<adsm:buttonBar>
			<adsm:button caption="descritivos" onclick="parent.parent.redirectPage('vendas/manterDescritivos.do?cmd=main' + buildLinkPropertiesQueryString_descritivos());"/>
			<adsm:storeButton callbackProperty="afterStore"/>
			<adsm:newButton />
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	function afterStore_cb(data,exception) {
		store_cb(data,exception);
 		if (data != undefined){
 			setElementValue("idProcessoPceTemp", getNestedBeanPropertyValue(data, "eventoPce.processoPce.idProcessoPce"));
 			setElementValue("cdProcessoPceTemp", getNestedBeanPropertyValue(data, "eventoPce.processoPce.cdProcessoPce"));
 			setElementValue("dsProcessoPceTemp", getNestedBeanPropertyValue(data, "eventoPce.processoPce.dsProcessoPce"));
 			setElementValue("tpAbrangenciaValueTemp", getNestedBeanPropertyValue(data, "eventoPce.processoPce.tpAbrangencia.value"));
 			setElementValue("tpModaValueTemp", getNestedBeanPropertyValue(data, "eventoPce.processoPce.tpModal.value"));
 			setElementValue("tpAbrangenciaTemp", getNestedBeanPropertyValue(data, "eventoPce.processoPce.tpAbrangencia.description"));
 			setElementValue("tpModaTemp", getNestedBeanPropertyValue(data, "eventoPce.processoPce.tpModal.description"));
 			setElementValue("idEventoPceTemp", getNestedBeanPropertyValue(data, "eventoPce.idEventoPce"));
 			setElementValue("cdEventoPceTemp", getNestedBeanPropertyValue(data, "eventoPce.cdEventoPce"));
 			setElementValue("dsEventoPceTemp", getNestedBeanPropertyValue(data, "eventoPce.dsEventoPce"));
 		}
	}

</script>