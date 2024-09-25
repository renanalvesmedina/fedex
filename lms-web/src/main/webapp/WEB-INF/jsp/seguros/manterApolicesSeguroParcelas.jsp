<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.manterApolicesSeguroAction">

	<adsm:form idProperty="idApoliceSeguroParcela" action="/seguros/manterApolicesSeguro" 
				service="lms.seguros.manterApolicesSeguroAction.findParcelas">
	
		<adsm:masterLink idProperty="idApoliceSeguro" showSaveAll="true">
			<adsm:masterLinkItem property="nrApolice" label="numeroApolice"/>
		</adsm:masterLink>
		
		<adsm:textbox dataType="integer" property="nrParcela" label="numeroParcela" size="12" width="85%" maxLength="10" required="true"/>		
		<adsm:textbox dataType="currency" property="vlParcela" label="valorParcela" size="12" width="85%" maxLength="18" mask="###,###,###,###,##0.00" minValue="0.01" required="true"/>
		<adsm:textbox dataType="JTDate" property="dtVencimento" label="dataVencimento" width="40%" picker="true" required="true"/>									   
		<adsm:combobox property="tpSituacaoPagamento" label="situacaoPagamento" domain="DM_TP_SIT_PAGTO_PARCELA_APOLICE" renderOptions="true" required="true"/>

		<adsm:textbox dataType="file"
					  property="dcComprovante"
					  label="comprovantePagamento"
					  blobColumnName="DC_COMPROVANTE"
					  tableName="APOLICE_SEGURO_PARCELA"
					  primaryKeyColumnName="ID_APOLICE_SEGURO_PARCELA"
					  primaryKeyValueProperty="idApoliceSeguroParcela"
					  size="80"
					  labelWidth="18%"
					  width="82%"
					  serializable="true"/>
		 				
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarItem" id="salvarItemParcelaButton" callbackProperty="storeItem" 
							  service="lms.seguros.manterApolicesSeguroAction.storeItemParcela"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid property="apoliceSeguroParcela" idProperty="idApoliceSeguroParcela" selectionMode="check" 
 			   service="lms.seguros.manterApolicesSeguroAction.findPaginatedParcela" 
 			   detailFrameName="parcelas"
 			   rowCountService="lms.seguros.manterApolicesSeguroAction.getRowCountApoliceSeguroParcela"
 			   unique="true" scrollBars="horizontal" rows="7" gridHeight="180"> 
 		<adsm:gridColumn property="nrParcela" title="numeroParcela" width="100"/> 
 		<adsm:gridColumn property="vlParcela" title="valorParcela" width="170" dataType="currency"/> 
 		<adsm:gridColumn property="dtVencimento" title="dataVencimento" width="130" dataType="JTDate"/> 
 		<adsm:gridColumn property="tpSituacaoPagamento" title="situacaoPagamento" width="200" isDomain="true"/> 
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
		apoliceSeguroParcelaGridDef.executeSearch();
				
		atualizaTela(eventObj);	
		resetTela();
	}else{
		alert(error);
	}
}

function removeItem(){
	var mapCriteria = new Array();	   
	setNestedBeanPropertyValue(mapCriteria, "ids", apoliceSeguroParcelaGridDef.getSelectedIds().ids);
	var sdo = createServiceDataObject("lms.seguros.manterApolicesSeguroAction.removeByIdsParcela", "removeItem", mapCriteria);
	xmit({serviceDataObjects:[sdo]});
}


function removeItem_cb(data, error, eventObj){
	if(error == undefined){
		apoliceSeguroParcelaGridDef.executeSearch();
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
	resetValue("nrParcela");
	resetValue("vlParcela");
	resetValue("dtVencimento");
	resetValue("tpSituacaoPagamento");
	resetValue("dcComprovante");
}

</script>