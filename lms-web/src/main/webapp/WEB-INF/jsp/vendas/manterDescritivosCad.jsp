<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
 function carregaComboEventoOcorrencia_cb(data,exception){
  	 onPageLoad_cb(data,exception);
  	 if(document.getElementById("idEventoPceTemp").masterLink == "true"){
  	 	document.getElementById("ocorrenciaPce.eventoPce.processoPce.idProcessoPce").masterLink = "true";
		var idProcessoPce = getElementValue("idProcessoPceTemp");
 		var dsProcessoPceCombo = getElementValue("cdProcessoPceTemp") + " - " +
 				 getElementValue("dsProcessoPceTemp") + ((getElementValue("tpModaTemp") == "") ? "" : " - " + getElementValue("tpModaTemp"))
 				 + ((getElementValue("tpAbrangenciaTemp") == "") ? "" : " - " + getElementValue("tpAbrangenciaTemp"));
 		setComboBoxElementValue(document.getElementById("ocorrenciaPce.eventoPce.processoPce.idProcessoPce"), idProcessoPce, idProcessoPce, dsProcessoPceCombo);
 		document.getElementById("ocorrenciaPce.eventoPce.processoPce.idProcessoPce").disabled = true;
  	   	notifyElementListeners({e:document.getElementById("ocorrenciaPce.eventoPce.processoPce.idProcessoPce")});
  	 }
 }
 
 function setaValorComboEvento_cb(data){
 	ocorrenciaPce_eventoPce_idEventoPce_cb(data);
 	if(document.getElementById("idEventoPceTemp").masterLink == "true"){
 		setElementValue(document.getElementById("ocorrenciaPce.eventoPce.idEventoPce"),document.getElementById("idEventoPceTemp").value);
 		document.getElementById("ocorrenciaPce.eventoPce.idEventoPce").masterLink = "true";
 		var idEventoPce = getElementValue("idEventoPceTemp");
 		var dsEventoPceCombo = getElementValue("cdEventoPceTemp")+ " - " + getElementValue("dsEventoPceTemp");
 		setComboBoxElementValue(document.getElementById("ocorrenciaPce.eventoPce.idEventoPce"), idEventoPce, idEventoPce, dsEventoPceCombo);
 		setDisabled("ocorrenciaPce.eventoPce.idEventoPce",true);
 		if(document.getElementById("idEventoPceTemp").masterLink == "true")
	  	    notifyElementListeners({e:document.getElementById("ocorrenciaPce.eventoPce.idEventoPce")});
  	}
  	//document.getElementById("ocorrenciaPce.eventoPce.idEventoPce").disabled = document.getElementById("ocorrenciaPce.eventoPce.idEventoPce").masterLink == "true";
 }
 
 function setaValorComboOcorrencia_cb(data){
 	ocorrenciaPce_idOcorrenciaPce_cb(data);
 	if(document.getElementById("idOcorrenciaPceTemp").masterLink == "true"){
 		setElementValue(document.getElementById("ocorrenciaPce.idOcorrenciaPce"),document.getElementById("idOcorrenciaPceTemp").value);
 		document.getElementById("ocorrenciaPce.idOcorrenciaPce").masterLink = "true";
 		var idOcorrenciaPce = getElementValue("idOcorrenciaPceTemp");
 		var dsOcorrenciaPceCombo = getElementValue("cdOcorrenciaPceTemp")+ " - " + getElementValue("dsOcorrenciaPceTemp");
 		setComboBoxElementValue(document.getElementById("ocorrenciaPce.idOcorrenciaPce"), idOcorrenciaPce, idOcorrenciaPce, dsOcorrenciaPceCombo);
 		setDisabled("ocorrenciaPce.idOcorrenciaPce",true);
  	}
 	//document.getElementById("ocorrenciaPce.idOcorrenciaPce").disabled = document.getElementById("ocorrenciaPce.idOcorrenciaPce").masterLink == "true";
 }
 function afterStore_cb(data,exception) {
	store_cb(data,exception);
 } 
 
</script>
<adsm:window service="lms.vendas.manterDescritivosAction" onPageLoadCallBack="carregaComboEventoOcorrencia" >
	<adsm:form action="/vendas/manterDescritivos" idProperty="idDescritivoPce">
		<adsm:hidden property="empresa.idEmpresa"/>
		<adsm:hidden property="ocorrenciaPce.eventoPce.tpSituacao" value="A"/>

		<adsm:hidden property="idEventoPceTemp"/>
		<adsm:hidden property="cdEventoPceTemp"/>
		<adsm:hidden property="dsEventoPceTemp"/>
		
		<adsm:hidden property="idOcorrenciaPceTemp"/>
		<adsm:hidden property="cdOcorrenciaPceTemp"/>
		<adsm:hidden property="dsOcorrenciaPceTemp"/>
		
		<adsm:hidden property="idProcessoPceTemp"/>
		<adsm:hidden property="cdProcessoPceTemp"/>
		<adsm:hidden property="dsProcessoPceTemp"/>
		<adsm:hidden property="tpModaTemp"/>
		<adsm:hidden property="tpAbrangenciaTemp"/>


		
		<adsm:combobox 
		property="ocorrenciaPce.eventoPce.processoPce.idProcessoPce" 
		service="lms.vendas.manterDescritivosAction.findProcessoPceAtivoByUsuarioLogado" 
		label="processo" required="true" 
		optionLabelProperty="processoPceCombo" 
		optionProperty="idProcessoPce" boxWidth="185"/>
		
		<adsm:combobox 
		onDataLoadCallBack="setaValorComboEvento" 
		property="ocorrenciaPce.eventoPce.idEventoPce"  
		service="lms.vendas.manterDescritivosAction.find" 
		label="evento" 
		optionLabelProperty="eventoPceCombo" 
		optionProperty="idEventoPce" width="35%" required="true" boxWidth="230">
			 <adsm:propertyMapping criteriaProperty="ocorrenciaPce.eventoPce.processoPce.idProcessoPce" modelProperty="processoPce.idProcessoPce"/>
		     <adsm:propertyMapping criteriaProperty="ocorrenciaPce.eventoPce.tpSituacao" modelProperty="tpSituacao"/>
		</adsm:combobox>
		
		<adsm:combobox 
		onDataLoadCallBack="setaValorComboOcorrencia" 
		property="ocorrenciaPce.idOcorrenciaPce" 
		service="lms.vendas.manterDescritivosAction.findComboOcorrencia" 
		optionLabelProperty="ocorrenciaPceCombo" 
		optionProperty="idOcorrenciaPce" label="ocorrencia" required="true" width="85%" boxWidth="185">
			<adsm:propertyMapping criteriaProperty="ocorrenciaPce.eventoPce.idEventoPce" modelProperty="eventoPce.idEventoPce"/>
		    <adsm:propertyMapping criteriaProperty="ocorrenciaPce.eventoPce.tpSituacao" modelProperty="tpSituacao"/>
		</adsm:combobox>
		
		<adsm:textbox maxLength="10" size="10" dataType="integer" property="cdDescritivoPce" label="codigo" labelWidth="15%" width="20%" required="true" />
		
		<adsm:combobox property="tpAcao" label="acao" domain="DM_ACOES_PCE" labelWidth="8%" width="21%" required="true" renderOptions="true"/>
		
		<adsm:checkbox property="blIndicadorAviso" label="indicadorAviso"  labelWidth="16%" width="20%"/>
		
		<adsm:textarea required="true" columns="99" rows="7" maxLength="250" property="dsDescritivoPce" label="descritivo" width="85%"/>
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" width="84%" required="true" renderOptions="true"/>
		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="afterStore"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>