<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.expedicao.manterProdutosAction">

	<adsm:form action="/expedicao/manterProdutos" idProperty="idNomeProduto"
		service="lms.expedicao.manterProdutosAction.findByIdProduto">
		
		<adsm:masterLink showSaveAll="true" idProperty="idProduto">
			<adsm:masterLinkItem property="dsNomeProduto" label="nome" />
		</adsm:masterLink>
		
		<adsm:textbox label="nome" 
					property="dsNomeProduto" 
					dataType="text" 
					size="60"
					required="true" 
					labelWidth="18%" 
					width="82%" 
					maxLength="60"/>
					
		<adsm:combobox label="tpNomeProduto" 
					property="tpNomeProduto" 
					domain="DM_TIPO_NOME_PRODUTO" 
					required="true" 
					labelWidth="18%" 
					width="82%"
			 		renderOptions="true"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvar" 
						id="salvarNomeProduto" 
						service="lms.expedicao.manterProdutosAction.storeNomeProduto" callbackProperty="storeItem"/>					
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="nomeProduto" 
			idProperty="idNomeProduto"
			selectionMode="check"
			gridHeight="82"
			unique="true"
			detailFrameName="prod"
			rows="8"
			service="lms.expedicao.manterProdutosAction.findPaginatedNomeProduto"
			autoSearch="false" 
		 	rowCountService="lms.expedicao.manterProdutosAction.getRowCountNomeProduto">

		<adsm:gridColumn title="nome" property="dsNomeProduto" width="65%" />
		<adsm:gridColumn title="tpNomeProduto" property="tpNomeProduto.description" width="30%" />

		<adsm:buttonBar>
			<adsm:button caption="excluirItem" buttonType="removeButton" onclick="removeItem();"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	var tabGroup = getTabGroup(this.document);
	var abaCad = tabGroup.getTab("cad");

	function initWindow(eventObj) {
	    if (eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq"){
		    addServiceDataObject(createServiceDataObject("lms.expedicao.manterProdutosAction.clearSessionItens",
	                null,
	                null));
			xmit(false);
	    }
	}
	
	function store_cb(data, erros, errorMsg, eventObj){
		if(!erros) {
			alert(errorMsg);
		}else{
			alert('OPA');
		}
	}
	
	
	function storeItem_cb(data, error, eventObj){
		if(error == undefined){
			nomeProdutoGridDef.executeSearch();
			showSuccessMessage();
			atualizaTela(eventObj);	
			resetTela();
			setDisabled("salvarNomeProduto", false);
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
		setDisabled("salvarNomeProduto", false);
	   	initWindowScript(document.parentWindow, eventObj);
	}
	
	function resetTela(){
		resetValue("dsNomeProduto");
		resetValue("tpNomeProduto");
	}
	
	function removeItem(){
		var mapCriteria = new Array();	   
		setNestedBeanPropertyValue(mapCriteria, "ids", nomeProdutoGridDef.getSelectedIds().ids);
		var sdo = createServiceDataObject("lms.expedicao.manterProdutosAction.removeByIdsNomeProduto", "removeItem", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}

	function removeItem_cb(data, error, eventObj){
		if(error == undefined){
			nomeProdutoGridDef.executeSearch();
			showSuccessMessage();
			atualizaTela(eventObj);	
			resetTela();
			setDisabled("salvarNomeProduto", false);
		}else{
			alert(error);	
		}
	}
	
</script>
