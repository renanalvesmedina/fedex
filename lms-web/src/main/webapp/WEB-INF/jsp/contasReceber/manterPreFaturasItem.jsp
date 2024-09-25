<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contasreceber.manterPreFaturasAction">
	<adsm:form action="/contasReceber/manterPreFaturas" service="lms.contasreceber.manterPreFaturasAction">
	
		<adsm:masterLink idProperty="idFatura" showSaveAll="true">
			<adsm:masterLinkItem property="sgFilialNrFatura" label="numeroPreFatura" itemWidth="50" />
			<adsm:masterLinkItem property="moeda" label="moeda"  itemWidth="50" />
		</adsm:masterLink>

        <adsm:section caption="totais"/>
        
        <adsm:hidden property="idCliente"/>

		<adsm:textbox dataType="integer" label="qtdeTotalDocumentos" 
			property="qtdeTotalDocumentos" size="10" labelWidth="20%"
			maxLength="6" disabled="true" width="130" />

		<adsm:textbox dataType="currency" label="valorTotalDocumentos"
			property="valorTotalDocumentos" size="10" labelWidth="21%"
			maxLength="18" disabled="true" width="130" />

		<adsm:textbox dataType="currency" label="valorTotalDesconto"
			property="valorTotalDesconto" size="10" labelWidth="21%"
			maxLength="18" width="100" disabled="true" />
		
	</adsm:form>
	
	<adsm:grid idProperty="idItemFatura" property="itemFatura"
		unique="true" rows="12" showGotoBox="true" autoSearch="true"
		showPagging="true" onRowClick="emptyFunction"
		onDataLoadCallBack="myItemFatura"
		service="lms.contasreceber.manterPreFaturasAction.findPaginatedItemFatura"
		rowCountService="lms.contasreceber.manterPreFaturasAction.getRowCountItemFatura">

		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="documentoServico" isDomain="true"
				property="tpDocumentoServico" width="150" />
			<adsm:gridColumn title="" property="sgFilial" width="20" />
			<adsm:gridColumn title="" property="nrDoctoServico" width="30" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="dataEmissao" dataType="JTDate" property="dtEmissao" width="150"/>		
		<adsm:gridColumn title="valorMercadoria" dataType="currency" property="vlMercadoria" width="150" />
		<adsm:gridColumn title="valorFrete" dataType="currency" property="vlTotal" width="120" />
		<adsm:gridColumn title="valorDesconto" dataType="currency" property="vlDesconto"  />
		
			
		<adsm:buttonBar>
            <adsm:button id="adicionarDocumentosServicoTitulo" caption="adicionarDocumentosServico" onclick="adicionaDocumentosServicoScript();"/>		
			<adsm:removeButton id="removeButton"
				caption="excluirDocumentoServico"
				service="lms.contasreceber.manterPreFaturasAction.removeByIdsItemFatura" />
		</adsm:buttonBar>
	</adsm:grid>	

</adsm:window>
<script>
	document.getElementById("qtdeTotalDocumentos").masterLink = "true";
	document.getElementById("valorTotalDocumentos").masterLink = "true";
	document.getElementById("valorTotalDesconto").masterLink = "true";

	var objFiltros = new Object();

	function emptyFunction(){
		return false;
	}
	
	/** Função para buscar os somatórios dos itens de fatura */
	
	function populaSomatorios(){
	      var remoteCall = {serviceDataObjects:new Array()};
	      var dataCall = createServiceDataObject("lms.contasreceber.manterPreFaturasAction.findSomatorios", "populaSomatorios", 
	            {
	                  idFatura:getElementValue("masterId")
	            }
	      );
	      remoteCall.serviceDataObjects.push(dataCall);
	      xmit(remoteCall);  
	
	}
	
	/** Função (callBack) para popular os dados dos somatórios */
	
	function populaSomatorios_cb(data, error, erromsg){
		if (error != undefined) {
			alert(error+'');
			return;
		}
		
		setElementValue(getElement("valorTotalDocumentos"), setFormat(getElement("valorTotalDocumentos"), data.valorTotalDocumentos));
		setElementValue(getElement("valorTotalDesconto"), setFormat(getElement("valorTotalDesconto"), data.valorTotalDesconto));		
	    setElementValue("qtdeTotalDocumentos",data.qtdeTotalDocumentos);
		
		document.getElementById("adicionarDocumentosServicoTitulo").focus();
	}
	
	function initWindow(eventObj){
	
		if (getElementValue("masterId") != "") {
		// Caso esteja detalhando ou tenha incluído uma pre fatura, a mesma não pode mais ser editada.
			var tabGroup = getTabGroup(this.document);
			var tabCad = tabGroup.getTab("cad")
			var telaCad = tabCad.tabOwnerFrame;	
			
			setDisabled("storeAllButtton", telaCad.document.getElementById("storeButton").disabled );
		}
		if (eventObj.name == "tab_click" || eventObj.name == "storeItemButton" || eventObj.name == "removeButton_grid"){
		    itemFaturaGridDef.executeSearch();
		    //populaSomatorios();
		}
		if (eventObj.name == "removeButton_grid"){
			setDisabled("adicionarDocumentosServicoTitulo", false);
			document.getElementById("adicionarDocumentosServicoTitulo").focus();
			
			if (!(itemFaturaGridDef.currentRowCount > 0)){
				resetObjFiltros();
			}
		}
	}

	function adicionaDocumentosServicoScript(){
	
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		var telaCad = tabCad.tabOwnerFrame;	
		var elemTab = getTab(telaCad.document, false);	
				
		var currentElem = telaCad.document.getElementById("cliente.pessoa.nrIdentificacao");		
		if (currentElem.required != null) {
			if (currentElem.required == 'true' && trim(currentElem.value.toString()).length == 0) {
				if(currentElem == '[object]') {				
					if ((currentElem.label != undefined)) {
						alert(getMessage(erRequired, new Array(currentElem.label)));
					}
					if (elemTab != null) {
						// se a aba do elemento n?o estiver selecionada, muda a aba
						// para a correta.
						if (elemTab.properties.id != tabGroup.selectedTab.properties.id) {
							var eventObj = {name:"tab_click", src:elemTab}; 	 // simula um clique na aba, para garantir funcionamento
							tabGroup.selectTab(elemTab.properties.id, eventObj); // uniforme das rotinas de habilita??o de botoes.
						}
					}
					setFocus(currentElem);
					return false;
				}
			}
		}	
	
		//var dataGrid = itemFaturaGridDef.gridState.data[0];
		var url = '/contasReceber/manterPreFaturas.do?cmd=proc&idFatura=' + getElementValue('masterId');
		
		url = mountUrl(objFiltros.tpDocumentoServico, "doctoServico.tpDocumentoServico", url);
		url = mountUrl(objFiltros.idMoeda, "idMoeda", url);
		url = mountUrl(objFiltros.idServico, "idServico", url);
		url = mountUrl(objFiltros.tpModal, "tpModal", url);
		url = mountUrl(objFiltros.tpAbrangencia, "tpAbrangencia", url);
		url = mountUrl(objFiltros.tpFrete, "tpFrete", url);
		url = mountUrl(objFiltros.idCliente, "idCliente", url);
		url = mountUrl(objFiltros.idDivisaoCliente, "idDivisaoCliente", url);
		url = mountUrl(telaCad.document.getElementById("tpSituacaoFatura").value, "tpSituacaoFatura", url);
		url = mountUrl(telaCad.document.getElementById("divisaoCliente.idDivisaoCliente").value, "idDivisaoCliente", url);
		showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');
	}
	
	function mountUrl(valor, campo, url){
		if (valor != undefined){
			return url + "&"+campo+"=" + valor;
		}
		return url;
	}	
	
	function myOnShow(e){
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		var telaCad = tabCad.tabOwnerFrame;	
			
		if (e.properties.id == "pesq"){
			telaCad.newButtonScript();
		}
		
		newButtonScript();
		
		return false; 
	}
	
	function myItemFatura_cb(searchFilters) {
		populaSomatorios();
		if (itemFaturaGridDef.currentRowCount > 0){
			var dataGrid = itemFaturaGridDef.gridState.data[0];	
			objFiltros.tpDocumentoServico = dataGrid.tpDocumentoServico.value;
			objFiltros.idMoeda = dataGrid.idMoeda;
			objFiltros.idServico = dataGrid.idServico;
			objFiltros.tpModal = dataGrid.tpModal;
			objFiltros.tpAbrangencia = dataGrid.tpAbrangencia;	
			objFiltros.tpFrete = dataGrid.tpFrete;	
			objFiltros.idDivisaoCliente = dataGrid.idDivisaoCliente;			
		} else {
			resetObjFiltros();
		}
		
		setFocus("adicionarDocumentosServicoTitulo");				
	}
	
	function resetObjFiltros(){
		objFiltros = new Object();
		objFiltros.tpModal   = getTabGroup(this.document).getTab("cad").tabOwnerFrame.getElementValue("tpModal");			
		objFiltros.idCliente = getTabGroup(this.document).getTab("cad").tabOwnerFrame.getElementValue("cliente.idCliente");
		objFiltros.tpAbrangencia = getTabGroup(this.document).getTab("cad").tabOwnerFrame.getElementValue("tpAbrangencia");	
		objFiltros.idDivisaoCliente = getTabGroup(this.document).getTab("cad").tabOwnerFrame.getElementValue("divisaoCliente.idDivisaoCliente");	
	}
	
	function setValueTelaCad(tpModal, tpAbrangencia, idDivisaoCliente) {
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		var telaCad = tabCad.tabOwnerFrame;	
		
		setElementValue(telaCad.document.getElementById("tpModal"), tpModal);
		setElementValue(telaCad.document.getElementById("tpAbrangencia"), tpAbrangencia);
		setElementValue(telaCad.document.getElementById("divisaoCliente.idDivisaoClienteTmp"), idDivisaoCliente);
		telaCad.findComboDivisaoCliente();
	}		
</script>