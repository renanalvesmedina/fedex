<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>

	function afterStore_cb(data,exception) {
		store_cb(data,exception);
 		if (data != undefined){
 			setElementValue("idProcessoPceTemp", getNestedBeanPropertyValue(data, "processoPce.idProcessoPce"));
 			setElementValue("cdProcessoPceTemp", getNestedBeanPropertyValue(data, "processoPce.cdProcessoPce"));
 			setElementValue("dsProcessoPceTemp", getNestedBeanPropertyValue(data, "processoPce.dsProcessoPce"));
 			setElementValue("tpAbrangenciaValueTemp", getNestedBeanPropertyValue(data, "processoPce.tpAbrangencia.value"));
 			setElementValue("tpModaValueTemp", getNestedBeanPropertyValue(data, "processoPce.tpModal.value"));
 			setElementValue("tpAbrangenciaTemp", getNestedBeanPropertyValue(data, "processoPce.tpAbrangencia.description"));
 			setElementValue("tpModaTemp", getNestedBeanPropertyValue(data, "processoPce.tpModal.description"));
 		}
	}
		
  function setaValorComboProcessoPce_cb(data,exception){
  	 onPageLoad_cb(data,exception);
  	 if(document.getElementById("idProcessoPceTemp").masterLink == "true"){
 		setElementValue(document.getElementById("processoPce.idProcessoPce"),document.getElementById("idProcessoPceTemp").value);
 		document.getElementById("processoPce.idProcessoPce").masterLink = "true";
		var idProcessoPce = getElementValue("idProcessoPceTemp");
 		var dsProcessoPceCombo = getElementValue("cdProcessoPceTemp") + " - " +
 				 getElementValue("dsProcessoPceTemp") + ((getElementValue("tpModaTemp") == "") ? "" : " - " + getElementValue("tpModaTemp"))
 				 + ((getElementValue("tpAbrangenciaTemp") == "") ? "" : " - " + getElementValue("tpAbrangenciaTemp"));
 		setComboBoxElementValue(document.getElementById("processoPce.idProcessoPce"), idProcessoPce, idProcessoPce, dsProcessoPceCombo);
 		document.getElementById("processoPce.idProcessoPce").disabled = true;
 	}
 }

 	function eventosDataLoad_cb(data){
 		onDataLoad_cb(data);
 		if (data != undefined){
 			setElementValue("idProcessoPceTemp", getNestedBeanPropertyValue(data, "processoPce.idProcessoPce"));
 			setElementValue("cdProcessoPceTemp", getNestedBeanPropertyValue(data, "processoPce.cdProcessoPce"));
 			setElementValue("dsProcessoPceTemp", getNestedBeanPropertyValue(data, "processoPce.dsProcessoPce"));
 			setElementValue("tpAbrangenciaValueTemp", getNestedBeanPropertyValue(data, "processoPce.tpAbrangencia.value"));
 			setElementValue("tpModaValueTemp", getNestedBeanPropertyValue(data, "processoPce.tpModal.value"));
 			setElementValue("tpAbrangenciaTemp", getNestedBeanPropertyValue(data, "processoPce.tpAbrangencia.description"));
 			setElementValue("tpModaTemp", getNestedBeanPropertyValue(data, "processoPce.tpModal.description"));
 		}
 	}
</script>

<adsm:window service="lms.vendas.manterEventosAction" onPageLoadCallBack="setaValorComboProcessoPce">
	<adsm:form action="/vendas/manterEventos" idProperty="idEventoPce" onDataLoadCallBack="eventosDataLoad">
	    <adsm:hidden property="idProcessoPceTemp"/>
	    <adsm:hidden property="cdProcessoPceTemp"/>
		<adsm:hidden property="dsProcessoPceTemp"/>
		<adsm:hidden property="tpModaTemp"/>
		<adsm:hidden property="tpAbrangenciaTemp"/>
		<adsm:hidden property="tpModaValueTemp"/>
		<adsm:hidden property="tpAbrangenciaValueTemp"/>
		<adsm:hidden property="processoPce.tpSituacao" value="A"/>
		
		<adsm:combobox property="processoPce.idProcessoPce" optionLabelProperty="processoPceCombo" 
			optionProperty="idProcessoPce" 
			service="lms.vendas.manterEventosAction.findProcessoPceAtivoByUsuarioLogado" 
			label="processo" width="35%" required="true" boxWidth="220" />
		<adsm:textbox maxLength="10" size="10" dataType="integer" property="cdEventoPce" label="codigo" width="35%" required="true" />
		<adsm:textbox maxLength="60" dataType="text" property="dsEventoPce" label="evento" size="40" required="true" width="35%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true" width="35%"/>
		<adsm:buttonBar>
			<adsm:button caption="ocorrencias" action="/vendas/manterOcorrencias" cmd="main">
				<adsm:linkProperty src="idProcessoPceTemp" target="idProcessoPceTemp"/>
				<adsm:linkProperty src="cdProcessoPceTemp" target="cdProcessoPceTemp"/>
				<adsm:linkProperty src="dsProcessoPceTemp" target="dsProcessoPceTemp"/>
				<adsm:linkProperty src="tpModaTemp" target="tpModaTemp"/>
				<adsm:linkProperty src="tpAbrangenciaTemp" target="tpAbrangenciaTemp"/>
				<adsm:linkProperty src="tpModaValueTemp" target="tpModaValueTemp"/>
				<adsm:linkProperty src="tpAbrangenciaValueTemp" target="tpAbrangenciaValueTemp"/>
				<adsm:linkProperty src="idEventoPce" target="idEventoPceTemp"/>
				<adsm:linkProperty src="cdEventoPce" target="cdEventoPceTemp"/>
				<adsm:linkProperty src="dsEventoPce" target="dsEventoPceTemp"/>
			</adsm:button>
			<adsm:storeButton callbackProperty="afterStore"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>