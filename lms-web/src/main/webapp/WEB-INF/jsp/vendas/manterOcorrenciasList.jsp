<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
  function setaValorComboEventoPce_cb(data,exception){
  	onPageLoad_cb(data,exception);
  	
  	 if(getElement("idProcessoPceTemp").masterLink == "true"){
 		setElementValue(getElement("eventoPce.processoPce.idProcessoPce"),getElement("idProcessoPceTemp").value);
 		document.getElementById("eventoPce.processoPce.idProcessoPce").masterLink = "true";
		var idProcessoPce = getElementValue("idProcessoPceTemp");
 		var dsProcessoPceCombo = getElementValue("cdProcessoPceTemp") + " - " +
 				 getElementValue("dsProcessoPceTemp") + ((getElementValue("tpModaTemp") == "") ? "" : " - " + getElementValue("tpModaTemp"))
 				 + ((getElementValue("tpAbrangenciaTemp") == "") ? "" : " - " + getElementValue("tpAbrangenciaTemp"));
 		setComboBoxElementValue(getElement("eventoPce.processoPce.idProcessoPce"), idProcessoPce, idProcessoPce, dsProcessoPceCombo, false);
 		setDisabled("eventoPce.processoPce.idProcessoPce", true);
  	   	notifyElementListeners({e:getElement("eventoPce.processoPce.idProcessoPce")});
  	 }
 }
 
 function setaValorCombo_cb(data){
 	eventoPce_idEventoPce_cb(data);
 	if(document.getElementById("idEventoPceTemp").masterLink == "true"){
 		setElementValue(document.getElementById("eventoPce.idEventoPce"),document.getElementById("idEventoPceTemp").value);
 		document.getElementById("eventoPce.idEventoPce").masterLink = "true";
 		var idEventoPce = getElementValue("idEventoPceTemp");
 		var dsEventoPceCombo = getElementValue("cdEventoPceTemp")+ " - " + getElementValue("dsEventoPceTemp");
 		setComboBoxElementValue(document.getElementById("eventoPce.idEventoPce"), idEventoPce, idEventoPce, dsEventoPceCombo, false);
 		setFocus(document.getElementById("cdOcorrenciaPce"));
 		setDisabled("eventoPce.idEventoPce",true);
 	}
 	setDisabled("eventoPce.idEventoPce", false);
 }

 
 function changeProcesso(){
 	if(getElementValue("eventoPce.processoPce.idProcessoPce") != "") {
	 	setDisabled("eventoPce.idEventoPce", true);
	}
 	return 	comboboxChange({e:getElement("eventoPce.processoPce.idProcessoPce")});
 }
 
</script>

<adsm:window service="lms.vendas.manterOcorrenciasAction" onPageLoadCallBack="setaValorComboEventoPce">
	<adsm:form action="/vendas/manterOcorrencias">
		<adsm:hidden property="idEventoPceTemp"/>
		<adsm:hidden property="cdEventoPceTemp"/>
		<adsm:hidden property="dsEventoPceTemp"/>
		<adsm:hidden property="idProcessoPceTemp"/>
		<adsm:hidden property="cdProcessoPceTemp"/>
		<adsm:hidden property="dsProcessoPceTemp"/>
		<adsm:hidden property="tpModaTemp"/>
		<adsm:hidden property="tpAbrangenciaTemp"/>

		<adsm:combobox property="eventoPce.processoPce.idProcessoPce" 
				optionLabelProperty="processoPceCombo" 
				optionProperty="idProcessoPce" 
				onchange="return changeProcesso();"
				service="lms.vendas.manterOcorrenciasAction.findProcessoPceByUsuarioLogado" 
				label="processo" width="35%" boxWidth="220"/>
		
		<adsm:combobox onDataLoadCallBack="setaValorCombo" property="eventoPce.idEventoPce" optionLabelProperty="eventoPceCombo" optionProperty="idEventoPce" service="lms.vendas.manterOcorrenciasAction.find" label="evento" width="35%" boxWidth="220">	
			<adsm:propertyMapping criteriaProperty="eventoPce.processoPce.idProcessoPce" modelProperty="processoPce.idProcessoPce"/>
		</adsm:combobox>
		
		<adsm:textbox maxLength="10" size="10" dataType="integer" property="cdOcorrenciaPce" serializable="true" label="codigo" />
		
		<adsm:textbox maxLength="60" dataType="text" property="dsOcorrenciaPce" label="ocorrencia" size="40"/>
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao"  width="35%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ocorrenciaPce"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="ocorrenciaPce" idProperty="idOcorrenciaPce" gridHeight="220" rows="11" scrollBars="horizontal" unique="true" defaultOrder="eventoPce_processoPce_.tpModal,eventoPce_processoPce_.tpAbrangencia,eventoPce_processoPce_.cdProcessoPce,eventoPce_.idEventoPce,dsOcorrenciaPce" >
		<adsm:gridColumn title="modal" property="eventoPce.processoPce.tpModal" width="90" isDomain="true"/>
		<adsm:gridColumn title="abrangencia" property="eventoPce.processoPce.tpAbrangencia" width="90" isDomain="true"/>
		<adsm:gridColumn title="processo" property="eventoPce.processoPce.processoPceGrid" width="200" />
		<adsm:gridColumn title="evento" property="eventoPce.eventoPceGrid" width="240" />
		<adsm:gridColumn title="codigo" property="cdOcorrenciaPce" width="100" dataType="integer"/>
		<adsm:gridColumn title="ocorrencia" property="dsOcorrenciaPce" width="240" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="70" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
