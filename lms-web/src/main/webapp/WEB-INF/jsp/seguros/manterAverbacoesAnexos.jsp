<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.manterAverbacoesAction">

	<adsm:form idProperty="idAverbacaoAnexo" action="/seguros/manterAverbacoes" 
			   service="lms.seguros.manterAverbacoesAction.findAnexos">
	
		<adsm:masterLink idProperty="idAverbacao" showSaveAll="true"/>
	
		<adsm:textbox dataType="text" property="dsAnexo" label="descricao" size="80" labelWidth="8%" width="92%" maxLength="100" required="true"/>		
		<adsm:textbox dataType="file" property="dcArquivo" label="arquivo" size="80" labelWidth="8%" width="92%" required="true" serializable="true"/>
		 				
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarItem" id="salvarItemAnexoButton" callbackProperty="storeItem" 
							  service="lms.seguros.manterAverbacoesAction.storeAnexos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid property="averbacoesAnexo" idProperty="idAverbacaoAnexo" selectionMode="check"
			   detailFrameName="anexos"
 			   service="lms.seguros.manterAverbacoesAction.findPaginatedAverbacoesAnexos" 
 			   rowCountService="lms.seguros.manterAverbacoesAction.getRowCountAverbacoesAnexos"
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
		    addServiceDataObject(createServiceDataObject("lms.seguros.manterAverbacoesAction.clearSessionItens",
	                null,
	                null));
			xmit(false);
	    }
	
}

function storeItem_cb(data, error, eventObj){
	if(error == undefined){
		averbacoesAnexoGridDef.executeSearch();
		
		atualizaTela(eventObj);	
		resetTela();
	}else{
		alert(error);
	}
}


function removeItem(){
	var mapCriteria = new Array();	   
	setNestedBeanPropertyValue(mapCriteria, "ids", averbacoesAnexoGridDef.getSelectedIds().ids);
	var sdo = createServiceDataObject("lms.seguros.manterAverbacoesAction.removeByIdsAnexo", "removeItem", mapCriteria);
	xmit({serviceDataObjects:[sdo]});
}


function removeItem_cb(data, error, eventObj){
	if(error == undefined){
		averbacoesAnexoGridDef.executeSearch();
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