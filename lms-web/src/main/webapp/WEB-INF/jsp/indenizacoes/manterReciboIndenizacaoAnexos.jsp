<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.indenizacoes.manterReciboIndenizacaoAction">

	<adsm:form action="/indenizacoes/manterReciboIndenizacao" 
			   service="lms.indenizacoes.manterReciboIndenizacaoAction.findByIdAnexoRim"
			   idProperty="idAnexoRim"		   
			   height="87">
		
		<adsm:masterLink idProperty="idReciboIndenizacao" showSaveAll="true">
			<adsm:masterLinkItem property="nrReciboComposto" label="numeroRIM" />			
		</adsm:masterLink>
		
		<adsm:hidden property="idAnexoRimIndenizacao" serializable="true" />
		<adsm:textbox label="descricao" 
					  dataType="text"
					  property="descAnexo"
					  width="93%"
					  labelWidth="7%" 
					  maxLength="120" 
					  size="120"
					  required="true" />
					  
		<adsm:textbox label="arquivo" 
					  dataType="file"
					  property="dcArquivo"
					  blobColumnName="DC_ARQUIVO"
					  tableName="ANEXO_RIM"
					  primaryKeyColumnName="ID_ANEXO_RIM"
					  primaryKeyValueProperty="idAnexoRim"
					  width="93%"
					  labelWidth="7%"
					  size="100" 
					  serializable="true"
					  required="true" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvar" 
							  service="lms.indenizacoes.manterReciboIndenizacaoAction.saveAnexoRim" 
							  callbackProperty="storeAnexo"/>		
			<adsm:newButton id="newButton"/>
		</adsm:buttonBar>
							  
	</adsm:form>
		
	<adsm:grid idProperty="idAnexoRim"
			   property="anexoRim" 
			   service="lms.indenizacoes.manterReciboIndenizacaoAction.findPaginatedAnexoRim"
			   rowCountService="lms.indenizacoes.manterReciboIndenizacaoAction.getRowCountAnexoRim"
			   selectionMode="check" 
			   gridHeight="160" 
			   detailFrameName="anexo"		
			   scrollBars="none" 		
			   unique="true" 
			   rows="10"
			   autoSearch="false">			  
	
		<adsm:gridColumn title="nomeArquivo" property="nomeArquivo" width="25%"/>
		<adsm:gridColumn title="descricao" property="descricaoAnexo" width="40%" />
		<adsm:gridColumn title="dataHoraDeInclusao" property="dtHoraCriacao" width="15%" dataType="JTDateTimeZone"/>
		<adsm:gridColumn title="usuario" property="nomeUsuario" width="20%" />
		
		<adsm:buttonBar>
			<adsm:button caption="excluir" buttonType="removeButton" onclick="removeAnexo();"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	var tabGroup = getTabGroup(this.document);
	var abaRim = tabGroup.getTab("rim");

	function initWindow(eventObj){		
	    if (eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq"){
		    addServiceDataObject(createServiceDataObject("lms.indenizacoes.manterReciboIndenizacaoAction.clearSessionItens", null,null));
			xmit(false);
	    }
	}
	
	function removeAnexo(){
		var mapCriteria = new Array();	   
		setNestedBeanPropertyValue(mapCriteria, "ids", anexoRimGridDef.getSelectedIds().ids);
		var sdo = createServiceDataObject("lms.indenizacoes.manterReciboIndenizacaoAction.removeAnexoRimByIds", "removeAnexo", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function removeAnexo_cb(data, error, eventObj){
		if(error == undefined){
			anexoRimGridDef.executeSearch();
			showSuccessMessage();
			atualizarDadosTela(eventObj);			
		}else{
			alert(error);	
		}
	}
	
	function pageLoad_cb() {
		onPageLoad_cb();		
	}
	
	function remove_cb(data, error) {
		resetValue(document);
		anexoRimGridDef.removeByIds_cb(data, error);
	}
	
	function storeAnexo_cb(data, error, eventObj) {
		if(error == undefined){
			anexoRimGridDef.executeSearch();
			showSuccessMessage();
			atualizarDadosTela(eventObj);
			limparCamposTela();
		}else{
			alert(error);
		}
	}
	
	function atualizarDadosTela(eventObj){
		if (eventObj == undefined) {
			eventObj = {name:"storeItemButton"};
		}

		var tab = getTab(document, false);
		if (tab) {
			eventObj.src = tab.tabGroup.selectedTab;
		}
	   	initWindowScript(document.parentWindow, eventObj);
	}
	
	function limparCamposTela(){
		resetValue("descAnexo");
		resetValue("dcArquivo");
		resetValue("idAnexoRimIndenizacao");
		resetValue("idAnexoRim");
	}
	
</script>