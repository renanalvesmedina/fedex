<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
  function carregaComboEventoOcorrencia_cb(data,exception){
  	 onPageLoad_cb(data,exception);
  	 if(document.getElementById("tpModaValueTemp").masterLink == "true" && document.getElementById("tpAbrangenciaValueTemp").masterLink == "true"){
  	 	setElementValue("ocorrenciaPce.eventoPce.processoPce.tpModal",getElementValue("tpModaValueTemp"));
  	 	setElementValue("ocorrenciaPce.eventoPce.processoPce.tpAbrangencia",getElementValue("tpAbrangenciaValueTemp"));
  	 	//notifyElementListeners({e:document.getElementById("ocorrenciaPce.eventoPce.processoPce.tpModal")});
  	 	//notifyElementListeners({e:document.getElementById("ocorrenciaPce.eventoPce.processoPce.tpAbrangencia")});
  	 	setDisabled("ocorrenciaPce.eventoPce.processoPce.tpModal",true);
  	 	setDisabled("ocorrenciaPce.eventoPce.processoPce.tpAbrangencia",true);
  	 	if (getElementValue("idOcorrenciaPceTemp") != null && getElementValue("idOcorrenciaPceTemp") != "") {
  	 		document.getElementById("ocorrenciaPce.eventoPce.processoPce.idProcessoPce").disabled = true;
  	 		setDisabled("ocorrenciaPce.eventoPce.idEventoPce",true);
  	 		setDisabled("ocorrenciaPce.idOcorrenciaPce",true);
  	 	}
  	 	//
		var idProcessoPce = getElementValue("idProcessoPceTemp");
 		var dsProcessoPceCombo = getElementValue("cdProcessoPceTemp") + " - " + getElementValue("dsProcessoPceTemp");
 		setComboBoxElementValue(document.getElementById("ocorrenciaPce.eventoPce.processoPce.idProcessoPce"), idProcessoPce, idProcessoPce, dsProcessoPceCombo);
  	   	//notifyElementListeners({e:document.getElementById("ocorrenciaPce.eventoPce.processoPce.idProcessoPce")});
  	   	//
 		setElementValue(document.getElementById("ocorrenciaPce.eventoPce.idEventoPce"),document.getElementById("idEventoPceTemp").value);
 		var idEventoPce = getElementValue("idEventoPceTemp");
 		var dsEventoPceCombo = getElementValue("cdEventoPceTemp")+ " - " + getElementValue("dsEventoPceTemp")  ;
 		setComboBoxElementValue(document.getElementById("ocorrenciaPce.eventoPce.idEventoPce"), idEventoPce, idEventoPce, dsEventoPceCombo);
 		//notifyElementListeners({e:document.getElementById("ocorrenciaPce.eventoPce.idEventoPce")});
 		//
 		setElementValue(document.getElementById("ocorrenciaPce.idOcorrenciaPce"),document.getElementById("idOcorrenciaPceTemp").value);
 		var idOcorrenciaPce = getElementValue("idOcorrenciaPceTemp");
 		var dsOcorrenciaPceCombo = getElementValue("cdOcorrenciaPceTemp")+ " - " + getElementValue("dsOcorrenciaPceTemp")  ;
 		setComboBoxElementValue(document.getElementById("ocorrenciaPce.idOcorrenciaPce"), idOcorrenciaPce, idOcorrenciaPce, dsOcorrenciaPceCombo);
  	 }
 }
 
 function setaValorComboProcesso_cb(data) {
 	ocorrenciaPce_eventoPce_processoPce_idProcessoPce_cb(data);
  	 if(document.getElementById("idProcessoPceTemp").masterLink == "true"){
  	 	document.getElementById("ocorrenciaPce.eventoPce.processoPce.idProcessoPce").masterLink = "true";
		var idProcessoPce = getElementValue("idProcessoPceTemp");
 		var dsProcessoPceCombo = getElementValue("cdProcessoPceTemp") + " - " + getElementValue("dsProcessoPceTemp");
 		setComboBoxElementValue(document.getElementById("ocorrenciaPce.eventoPce.processoPce.idProcessoPce"), idProcessoPce, idProcessoPce, dsProcessoPceCombo);
 		//document.getElementById("ocorrenciaPce.eventoPce.processoPce.idProcessoPce").disabled = true;
  	   	notifyElementListeners({e:document.getElementById("ocorrenciaPce.eventoPce.processoPce.idProcessoPce")});
  	 }
 }
 function setaValorComboEvento_cb(data){
 	ocorrenciaPce_eventoPce_idEventoPce_cb(data);
 	if(document.getElementById("idEventoPceTemp").masterLink == "true"){
 		setElementValue(document.getElementById("ocorrenciaPce.eventoPce.idEventoPce"),document.getElementById("idEventoPceTemp").value);
 		document.getElementById("ocorrenciaPce.eventoPce.idEventoPce").masterLink = "true";
 		var idEventoPce = getElementValue("idEventoPceTemp");
 		var dsEventoPceCombo = getElementValue("cdEventoPceTemp")+ " - " + getElementValue("dsEventoPceTemp")  ;
 		setComboBoxElementValue(document.getElementById("ocorrenciaPce.eventoPce.idEventoPce"), idEventoPce, idEventoPce, dsEventoPceCombo);
 		//setDisabled("ocorrenciaPce.eventoPce.idEventoPce",true);
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
 		var dsOcorrenciaPceCombo = getElementValue("cdOcorrenciaPceTemp")+ " - " + getElementValue("dsOcorrenciaPceTemp")  ;
 		setComboBoxElementValue(document.getElementById("ocorrenciaPce.idOcorrenciaPce"), idOcorrenciaPce, idOcorrenciaPce, dsOcorrenciaPceCombo);
 		setFocus(document.getElementById("cdDescritivoPce"));
 		//setDisabled("ocorrenciaPce.idOcorrenciaPce",true);
  	}
  	//document.getElementById("ocorrenciaPce.idOcorrenciaPce").disabled = document.getElementById("ocorrenciaPce.idOcorrenciaPce").masterLink == "true";
 }

</script>
<adsm:window service="lms.vendas.manterDescritivosAction" onPageLoadCallBack="carregaComboEventoOcorrencia" >
	<adsm:form action="/vendas/manterDescritivos">
	   
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
		<adsm:hidden property="tpModaValueTemp"/>
		<adsm:hidden property="tpAbrangenciaValueTemp"/>
		
		
		<adsm:combobox property="ocorrenciaPce.eventoPce.processoPce.tpModal" domain="DM_MODAL" label="modal" renderOptions="true"/>
		<adsm:combobox property="ocorrenciaPce.eventoPce.processoPce.tpAbrangencia" domain="DM_ABRANGENCIA" label="abrangencia" renderOptions="true"/>

		
		<adsm:combobox property="ocorrenciaPce.eventoPce.processoPce.idProcessoPce" 
			optionLabelProperty="processoPceCombo" 
			optionProperty="idProcessoPce" 
			service="lms.vendas.manterDescritivosAction.findProcessoPceByUsuarioLogado"
			label="processo" boxWidth="185">
			 <adsm:propertyMapping criteriaProperty="ocorrenciaPce.eventoPce.processoPce.tpModal" modelProperty="tpModal"/>
		     <adsm:propertyMapping criteriaProperty="ocorrenciaPce.eventoPce.processoPce.tpAbrangencia" modelProperty="tpAbrangencia"/>
		</adsm:combobox>
		
		
		<adsm:combobox 
		onDataLoadCallBack="setaValorComboEvento" 
		property="ocorrenciaPce.eventoPce.idEventoPce"  
		service="lms.vendas.manterDescritivosAction.find" 
		label="evento" 
		optionLabelProperty="eventoPceCombo" 
		optionProperty="idEventoPce" width="35%"  boxWidth="230">
			 <adsm:propertyMapping criteriaProperty="ocorrenciaPce.eventoPce.processoPce.idProcessoPce" modelProperty="processoPce.idProcessoPce"/>
		</adsm:combobox>
		
		<adsm:combobox 
		onDataLoadCallBack="setaValorComboOcorrencia"
		property="ocorrenciaPce.idOcorrenciaPce" 
		service="lms.vendas.manterDescritivosAction.findComboOcorrencia" 
		optionLabelProperty="ocorrenciaPceCombo" 
		optionProperty="idOcorrenciaPce" label="ocorrencia" width="85%" boxWidth="185">
			<adsm:propertyMapping criteriaProperty="ocorrenciaPce.eventoPce.idEventoPce" modelProperty="eventoPce.idEventoPce"/>
		</adsm:combobox>
		
		<adsm:textbox maxLength="10" size="10" dataType="integer" property="cdDescritivoPce" label="codigo" labelWidth="15%" width="20%"/>
		
		<adsm:combobox property="tpAcao" domain="DM_ACOES_PCE" label="acao" labelWidth="8%" width="20%" renderOptions="true"/>
		
		<adsm:combobox property="blIndicadorAviso" label="indicadorAviso" domain="DM_SIM_NAO" labelWidth="15%" width="17%" renderOptions="true"/>
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="15%" width="85%" renderOptions="true"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="descritivoPce"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="descritivoPce" idProperty="idDescritivoPce" scrollBars="horizontal" gridHeight="180" rows="9" unique="true" defaultOrder="ocorrenciaPce_eventoPce_processoPce_.dsProcessoPce, ocorrenciaPce_eventoPce_.dsEventoPce, ocorrenciaPce_.dsOcorrenciaPce">
		<adsm:gridColumn title="modal" property="ocorrenciaPce.eventoPce.processoPce.tpModal" width="90" isDomain="true"/>
		<adsm:gridColumn title="abrangencia" property="ocorrenciaPce.eventoPce.processoPce.tpAbrangencia" width="90" isDomain="true"/>
		<adsm:gridColumn title="processo" property="ocorrenciaPce.eventoPce.processoPce.processoPceGrid" width="200" />
		<adsm:gridColumn title="evento" property="ocorrenciaPce.eventoPce.eventoPceGrid" width="250"/>
		<adsm:gridColumn title="ocorrencia" property="ocorrenciaPce.ocorrenciaPceGrid" width="200" />
		<adsm:gridColumn title="codigo" property="cdDescritivoPce" width="80" dataType="integer"/>
		<adsm:gridColumn title="acao" property="tpAcao" width="80" isDomain="true"/>
		<adsm:gridColumn title="descritivo" property="dsDescritivoPce" width="300" />
		<adsm:gridColumn title="indicadorAviso" property="blIndicadorAviso" width="120" renderMode="image-check"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="70"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
