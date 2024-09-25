<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.manterBoletosAction" >

	<adsm:form idProperty="idBoletoAnexo"  
				action="/contasReceber/manterBoletos"  
				service="lms.contasreceber.manterBoletosAction.findAnexos"  >
				
		<adsm:masterLink idProperty="idBoleto" showSaveAll="true">
			<adsm:masterLinkItem property="boletoDsConcatenado" label="boleto"/>
		</adsm:masterLink>

		<adsm:textbox 
					property="dsAnexo" 
					dataType="text" 
					label="descricao" 
					size="61"
					maxLength="60" 
					labelWidth="18%" 
					width="82%"
					required="true"  />

		<adsm:textbox property="dcArquivo" 
					dataType="file"
					label="arquivo"
				    blobColumnName="DC_ARQUIVO"
				    tableName="BOLETO_ANEXO"
				    primaryKeyColumnName="ID_BOLETO_ANEXO"
				    primaryKeyValueProperty="idBoletoAnexo"
					labelWidth="18%"
					width="82%"
					size="60"
					required="true"
					serializable="true" />
					
		<adsm:combobox property="blEnvAnexoQuestFat" 
					label="incluirQuestionamento" 
					domain="DM_SIM_NAO" 
					labelWidth="18%" 
					width="82%" 
					renderOptions="true"
					required="true"
					defaultValue="S"/>
	
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarItem" id="salvarItemMdaButton" callbackProperty="storeItem"  
							  service="lms.contasreceber.manterBoletosAction.storeAnexo" />
			<adsm:newButton id="newButton"/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid property="boletoAnexo" idProperty="idBoletoAnexo" selectionMode="check" gridHeight="82" 
			   unique="true" rows="8" detailFrameName="anexo"
			   service="lms.contasreceber.manterBoletosAction.findPaginatedBoletoAnexo" autoSearch="false"
			   rowCountService="lms.contasreceber.manterBoletosAction.getRowCountBoletoAnexo">
			   
			   <adsm:gridColumn property="descricao" dataType="text" title="descricao" width="50%" />
			   <adsm:gridColumn property="dhinclusao" dataType="JTDateTimeZone" title="dataHoraDeInclusao" width="20%" />
			   <adsm:gridColumn property="nmusuario" dataType="text" title="usuario" />
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
	    addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBoletosAction.clearSessionItens",
                null,
                null));
		xmit(false);
    }
}

//Habilita o botão salvar da aba Detalhamento, para ser usado nesta aba como 'Salvar Tudo'
function habilitaSave(){
	var tpSituacaoBoleto = abaCad.getFormProperty("tpSituacaoBoleto");
	
	if(tpSituacaoBoleto == 'EM' || tpSituacaoBoleto == 'BP' || tpSituacaoBoleto == 'BN'){
		setDisabled(abaCad.getElementById("storeButton"), false);		
	}
}

function removeItem(){
	var mapCriteria = new Array();	   
	setNestedBeanPropertyValue(mapCriteria, "ids", boletoAnexoGridDef.getSelectedIds().ids);
	var sdo = createServiceDataObject("lms.contasreceber.manterBoletosAction.removeByIdsAnexoBoleto", "removeItem", mapCriteria);
	xmit({serviceDataObjects:[sdo]});
}

function removeItem_cb(data, error, eventObj){
	if(error == undefined){
		boletoAnexoGridDef.executeSearch();
		showSuccessMessage();

		atualizaTela(eventObj);
		habilitaSave();
	}else{
		alert(error);	
	}
}

function storeItem_cb(data, error, eventObj){
	if(error == undefined){
		boletoAnexoGridDef.executeSearch();
		showSuccessMessage();
		
		atualizaTela(eventObj);
		habilitaSave();		
		resetTela();
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
	resetValue("blEnvAnexoQuestFat");
}
</script>