<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="destinatarios" service="lms.seguros.comunicarUnidadesEmissaoRIMAction" onPageLoad="loadDataObjects">
	<adsm:form action="/seguros/comunicarUnidadesEmissaoRIM">
		<adsm:section caption="destinatarioCarta"/>
		<adsm:hidden property="filial"/>
		<adsm:hidden property="idProcessoSinistro"/>
	</adsm:form> 
	
	<adsm:grid property="emailDestinatario" idProperty="idPessoa" showPagging="false"
			   service="lms.seguros.comunicarUnidadesEmissaoRIMAction.findPaginatedEnviarEmailRim" 
			   rowCountService=""
			   onRowClick="onGridRowClick();" selectionMode="none"
			   gridHeight="365"
			   scrollBars="vertical">
		<adsm:gridColumn property="nmFantasia" title="destinatarioCarta" width="50%"/>
		<adsm:editColumn property="dsEmail" title="email" dataType="text" field="textbox" width="390"/>
		<adsm:buttonBar>
			<adsm:button caption="confirmar" onclick="generateEmail(this.form);"/>
			<adsm:button caption="fechar" id="fechar" onclick="returnToParent();"/>
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
		setElementValue("filial", url.parameters["destinatarioCarta"]);
	}
	
	function loadObjects(){
		var parentWindow = dialogArguments.window.document;
		setElementValue("idProcessoSinistro", parentWindow.getElementById("idProcessoSinistro").value);
	}
	
	function getParentGrid(){
		var parentWindow = dialogArguments.window;
		var sinistroDoctoServicoIds = parentWindow.getSelectedIds;
		
		return sinistroDoctoServicoIds;
	}

	function loadGridData() {
		var idsSinistroDoctoServico = getParentGrid();
		
		var dataObject = new Object();
		setNestedBeanPropertyValue(dataObject, "idsSinistroDoctoServico", idsSinistroDoctoServico.ids);
		setNestedBeanPropertyValue(dataObject, "filial",  getElementValue("filial"));
		setNestedBeanPropertyValue(dataObject, "_currentPage", 1);
		setNestedBeanPropertyValue(dataObject, "_pageSize", 100);
		
		emailDestinatarioGridDef.executeSearch( dataObject, true);
	}

	function generateEmail(form) {		
		
		var storeSDO = createServiceDataObject('lms.seguros.comunicarUnidadesEmissaoRIMAction.generateEmailRim', 'generateEmail', editGridFormBean(document.forms[0], form));
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
		
		var parentWindow = dialogArguments.window;
		var idsSinistroDoctoServico = parentWindow.getSelectedIds;
		
		var idObject = null;
		var list = new Array();
		var listSize = 0;
		
		for (i=0; i<idsSinistroDoctoServico.ids.length; i++) {
			idObject = idsSinistroDoctoServico.ids[i];
			
			for (j=0; j<idsSinistroDoctoServico.ids.length; j++) {
				var rowData = idsSinistroDoctoServico.ids[j];
				
				if (rowData == idObject){    				
   					var data = new Object();
   					
   					setNestedBeanPropertyValue(data, "idSinistroDoctoServico", rowData);
//    					setNestedBeanPropertyValue(data, "origem", rowData.idFilialOrigem);
//    					setNestedBeanPropertyValue(data, "destino", rowData.idFilialDestino);
   					
   					list[listSize] = data;
   					listSize++;
				}
			}
		}	
		//Seta o objeto com os ids de destinatario...
		setNestedBeanPropertyValue(dataObject, "idsDestinatario", list);
		setNestedBeanPropertyValue(dataObject, "idsSinistroDoctoServico", idsSinistroDoctoServico.ids);
		setNestedBeanPropertyValue(dataObject, "nrProcessoSinistro", parentWindow.getElementValue("nrProcessoSinistro"));
		
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
</script>