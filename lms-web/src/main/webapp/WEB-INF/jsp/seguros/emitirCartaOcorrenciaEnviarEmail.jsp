<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="destinatarioCartas" service="lms.seguros.emitirCartaOcorrenciaEnviarEmailAction" onPageLoad="loadDataObjects" >
	<adsm:form action="/seguros/emitirCartaOcorrencia">
		<adsm:section caption="destinatarioCarta"/>					  
		<adsm:hidden property="destinatarioCarta"/>
		<adsm:hidden property="tipoCarta"/>
		<adsm:hidden property="idProcessoSinistro"/>
	</adsm:form> 
	
	<adsm:grid property="emailDestinatario" idProperty="idPessoa" showPagging="false"
			   service="lms.seguros.emitirCartaOcorrenciaAction.findPaginatedMails" 
			   rowCountService="lms.seguros.emitirCartaOcorrenciaAction.getRowCountMails"
			   onRowClick="onGridRowClick();" selectionMode="none" onDataLoadCallBack="gridLoad"
			   gridHeight="365"
			   scrollBars="vertical" >
		<adsm:gridColumn property="nmPessoa" title="destinatarioCarta"/>
		<adsm:editColumn property="dsEmail" title="email" dataType="text" field="textbox" width="390"/>
		<adsm:buttonBar>
			<adsm:button caption="confirmar" onclick="generateEmail(this.form);"/>
			<adsm:button caption="fechar" id="fechar" onclick="returnToParent()"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	function loadDataObjects() {
		onPageLoad();
		getDataFromURL();
		loadObjects();
		loadGridData();
	}
	
	function getDataFromURL(){
		var url = new URL(parent.location.href);
		setElementValue("destinatarioCarta", url.parameters["destinatarioCarta"]);
		setElementValue("tipoCarta", url.parameters["tipoCarta"]);
	}
	
	function loadObjects(){
		setElementValue("idProcessoSinistro", dialogArguments.window.document.getElementById("processoSinistro.idProcessoSinistro").value);
	}
	
	function loadGridData() {
		var dataObject = new Object();
		setNestedBeanPropertyValue(dataObject, "idsSinistroDoctoServico", dialogArguments.window.idsSinistroDoctoServico);
		setNestedBeanPropertyValue(dataObject, "idProcessoSinistro",  getElementValue("idProcessoSinistro"));
		setNestedBeanPropertyValue(dataObject, "destinatarioCarta",  getElementValue("destinatarioCarta"));
		
		emailDestinatarioGridDef.executeSearch(dataObject, true);
	}

	function generateEmail(form) {		
		var storeSDO = createServiceDataObject('lms.seguros.emitirCartaOcorrenciaAction.generateEmailCartaOcorrencia', 'generateEmail', editGridFormBean(document.forms[0], form));
		storeSDO._data = getData(storeSDO._data);
		
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false}; 
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
		return true;
	}
	
	function generateEmail_cb(data, error) {
		returnToParent();
	}
	
	//###################################################################
    // Funcoes referentes a captura de ids de destinatarios e remetentes
	//###################################################################
	
	function getData(dataObject){
		var sinistroDoctoServicoGridDef = dialogArguments.window.idsSinistroDoctoServico;
		
		var idObject = null;
		var list = new Array();
		var listSize = 0;
		
		for (i=0; i<sinistroDoctoServicoGridDef.length; i++) {
			idObject = sinistroDoctoServicoGridDef[i];
			
			for (j=0; j<sinistroDoctoServicoGridDef.length; j++) {
				var rowData = sinistroDoctoServicoGridDef[j];
				
				if (rowData == idObject){    				
   					var data = new Object();
   					
   					setNestedBeanPropertyValue(data, "idSinistroDoctoServico", rowData);
   					
   					list[listSize] = data;
   					listSize++;
				}
			}
		}	
		//Seta o objeto com os ids de destinatario...
		setNestedBeanPropertyValue(dataObject, "idsDestinatario", list);
		return dataObject;
	}
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * fecha a atual janela
	 */
	function returnToParent(){
		self.close();
	}
	
	function onGridRowClick() {
		return false;
	}
    
    function gridLoad_cb(data,error){
    	if (document.getElementById("emailDestinatario:0.dsEmail") != null) {
        	document.getElementById("emailDestinatario:0.dsEmail").focus();
        }
    }
</script>
