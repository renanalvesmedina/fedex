<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.indenizacoes.incluirReciboIndenizacaoAction" onPageLoadCallBack="pageLoad">
	
	<adsm:form action="/indenizacoes/incluirReciboIndenizacao" 
			   service="lms.indenizacoes.incluirReciboIndenizacaoAction.findByIdAnexoRim"
			   idProperty="idAnexoRim"
			   height="87" 
			   onDataLoadCallBack="dataLoad">
				
		<adsm:masterLink idProperty="idReciboIndenizacao" showSaveAll="true">
		    <adsm:masterLinkItem property="reciboIndenizacao" label="RIM" itemWidth="50"/>
	    </adsm:masterLink>	 
	    <adsm:hidden property="idCliente"/>   
		
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
					  width="93%"
					  labelWidth="7%"
					  size="100" 
					  serializable="true"
					  required="true" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvar" service="lms.indenizacoes.incluirReciboIndenizacaoAction.saveAnexoRim" callbackProperty="storeItem"/>		
			<adsm:newButton id="newButton"/>
		</adsm:buttonBar>
							  
	</adsm:form>
	
	<adsm:grid idProperty="idAnexoRim"
			   property="anexoRim" 
			   service="lms.indenizacoes.incluirReciboIndenizacaoAction.findPaginatedAnexoRim"
			   rowCountService="lms.indenizacoes.incluirReciboIndenizacaoAction.getRowCountAnexoRim"
			   selectionMode="check" 
			   gridHeight="160" 
			   detailFrameName="anexo"		
			   scrollBars="none" 		
			   unique="true" 
			   rows="11"
			   autoSearch="false">			  
	
		<adsm:gridColumn title="nomeArquivo" property="nomeArquivo" width="25%"/>
		<adsm:gridColumn title="descricao" property="descricaoAnexo" width="35%" />
		<adsm:gridColumn title="dataHoraDeInclusao" property="dtHoraCriacao" width="20%" dataType="JTDateTimeZone"/>
		<adsm:gridColumn title="usuario" property="nomeUsuario" width="20%" />
		
		<adsm:buttonBar>
			<adsm:removeButton caption="excluir" service="lms.indenizacoes.incluirReciboIndenizacaoAction.removeAnexoRimByIds"/>
		</adsm:buttonBar>
	</adsm:grid>	
	
</adsm:window>

<script>
	
	document.getElementById('idCliente').masterLink='true';
	var tabGroup = getTabGroup(this.document);
	var abaRim = tabGroup.getTab("rim");
	
	function pageLoad_cb() {
		onPageLoad_cb();		
	}
	
	function onRemoveButtonClick() {
		
		var idsMap = anexoRimGridDef.getSelectedIds();
		if (idsMap.ids.length>0) { 
			if (window.confirm(erExcluir)) {

				var data = new Array();
				data.ids = idsMap.ids;
				data.masterId = getElementValue('masterId');

				var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.removeAnexoRimByIds", "remove", data);
		    	xmit({serviceDataObjects:[sdo]});		

			}
		} else {
			alert(erSemRegistro);
		}
	}
	
	function remove_cb(data, error) {
		resetValue(document);
		anexoRimGridDef.removeByIds_cb(data, error);
	}
	
	function executeSearch() {
		var data = new Array();
		data.masterId = getElementValue('masterId');
		anexoRimGridDef.executeSearch(data);
	}
	
	function storeCallback_cb(data, error) {
		store_cb(data, error);
		
		if (error==undefined) {
			resetValue(document);
			executeSearch();	
		}
	}
	
	function storeItem_cb(data, error, eventObj){
		if(error == undefined){
			anexoRimGridDef.executeSearch();
			showSuccessMessage();
			
			atualizaTela(eventObj);			
			resetValue(document);
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
	
	function onTabShow(fromTab) {
		resetValue(document);
	}
	
	function dataLoad_cb(data,error){
		onDataLoad_cb(data,error);
		
	}
	
</script>