<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.manterApolicesSeguroAction">

	<adsm:form idProperty="idApoliceSeguroAnexo" action="/seguros/manterApolicesSeguro" 
			   service="lms.seguros.manterApolicesSeguroAction.findAnexos">
	
		<adsm:masterLink idProperty="idApoliceSeguro" showSaveAll="true">
			<adsm:masterLinkItem property="nrApolice" label="numeroApolice"/>
		</adsm:masterLink>
	
		<adsm:textbox dataType="text" property="dsAnexo" label="descricao" size="80" labelWidth="8%" width="92%" maxLength="100" required="true"/>		

		<adsm:textbox dataType="file"
					  property="dcArquivo"
					  label="arquivo"
					  blobColumnName="DC_ARQUIVO"
					  tableName="APOLICE_SEGURO_ANEXO"
					  primaryKeyColumnName="ID_APOLICE_SEGURO_ANEXO"
					  primaryKeyValueProperty="idApoliceSeguroAnexo"
					  size="80"
					  labelWidth="8%"
					  width="92%"
					  required="true"
					  serializable="true"/>
		 				
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarItem" id="salvarItemAnexoButton" callbackProperty="storeItem" 
							  service="lms.seguros.manterApolicesSeguroAction.storeAnexos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid property="apoliceSeguroAnexo" idProperty="idApoliceSeguroAnexo" selectionMode="check"
			   detailFrameName="anexos"
 			   service="lms.seguros.manterApolicesSeguroAction.findPaginatedApoliceSeguroAnexos" 
 			   rowCountService="lms.seguros.manterApolicesSeguroAction.getRowCountApoliceSeguroAnexos"
 			   unique="true" scrollBars="horizontal" rows="7" gridHeight="180"> 
 		<adsm:gridColumn property="dsAnexo" title="descricao" align="center" width="250"/> 
 		<adsm:gridColumn property="dhCriacao" title="dataHoraInclusao" align="center" width="130" dataType="JTDateTimeZone"/> 
 		<adsm:gridColumn property="usuario" title="usuario" align="center" width="130" dataType=""/> 
 		<adsm:buttonBar>
			<adsm:button caption="excluirItem" buttonType="removeButton" onclick="removeItem();"/>
 		</adsm:buttonBar>
 	</adsm:grid>
	
</adsm:window>

<script>

var tabGroup = getTabGroup(this.document);
var abaCad = tabGroup.getTab("cad");

function initWindow(eventObj){
	
	 if (eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq"){
		    addServiceDataObject(createServiceDataObject("lms.seguros.manterApolicesSeguroAction.clearSessionItens",
	                null,
	                null));
			xmit(false);
	    }
	
}

function storeItem_cb(data, error, eventObj){
	if(error == undefined){
		apoliceSeguroAnexoGridDef.executeSearch();
		
		atualizaTela(eventObj);	
		resetTela();
	}else{
		alert(error);
	}
}


function removeItem(){
	var mapCriteria = new Array();	   
	setNestedBeanPropertyValue(mapCriteria, "ids", apoliceSeguroAnexoGridDef.getSelectedIds().ids);
	var sdo = createServiceDataObject("lms.seguros.manterApolicesSeguroAction.removeByIdsAnexo", "removeItem", mapCriteria);
	xmit({serviceDataObjects:[sdo]});
}


function removeItem_cb(data, error, eventObj){
	if(error == undefined){
		apoliceSeguroAnexoGridDef.executeSearch();
		showSuccessMessage();

		atualizaTela(eventObj);
	}else{
		alert(error);	
	}
}


function atualizaTela(eventObj){
	if (eventObj == undefined) {
		eventObj = {name:"storeItemButton"};
	}

	var tab = getTab(document, false);
	if (tab) {
		eventObj.src = tab.tabGroup.selectedTab;
	}
   	initWindowScript(document.parentWindow, eventObj);
}

function resetTela(){
	resetValue("dsAnexo");
	resetValue("dcArquivo");
}

</script>