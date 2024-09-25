<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterRelacaoDocumentosDepositos" onDataLoadCallBack="myOnDataLoad">
		<adsm:masterLink showSaveAll="true" idProperty="idDepositoCcorrente">
			<adsm:masterLinkItem property="cliente.pessoa.nrIdentificacao" label="cliente" itemWidth="35"/>
			<adsm:masterLinkItem property="cedente.idCedente" label="banco" itemWidth="35" />
		</adsm:masterLink>
		
		<adsm:hidden property="idCliente"/>
		
		<adsm:textbox property="vlDeposito" label="valorDeposito" dataType="currency" labelWidth="20%" width="13%" size="14" disabled="true"/>
		<adsm:textbox property="vlDocumento" label="valorLiquidoDocumentos" dataType="currency" labelWidth="21%" width="13%" size="14" disabled="true"/>
		<adsm:textbox property="vlDiferenca" label="diferencaDeposito" dataType="currency" labelWidth="20%" width="13%" size="14" disabled="true"/>				
	</adsm:form>

	<adsm:grid idProperty="idItemDepositoCcorrente" property="itemDepositoCcorrente"
			unique="true" autoSearch="false" rows="12" showGotoBox="true"
			showPagging="true" detailFrameName="documento" onRowClick="returnNull"
			service="lms.contasreceber.manterRelacaoDocumentosDepositosAction.findPaginatedItemDepositoCcorrente"
			rowCountService="lms.contasreceber.manterRelacaoDocumentosDepositosAction.getRowCountItemDepositoCcorrente"
			onPopulateRow="myOnPopulateRow">
			
		<adsm:gridColumn title="tipoDocumento" property="tpDocumento" isDomain="true" width="30%"/>
		<adsm:gridColumn title="documento" property="nrDocumento" dataType="integer" width="12%"/>
		<adsm:gridColumn title="dataEmissao" property="dtEmissao" dataType="JTDate" width="12%"/>
		<adsm:gridColumn title="moeda" property="siglaSimbolo" dataType="text" width="10%"/>
		<adsm:gridColumn title="valorDocumento" property="vlDocumento" dataType="currency" width="12%"/>
		<adsm:gridColumn title="valorDesconto" property="vlDesconto" dataType="currency" width="12%"/>
		<adsm:gridColumn title="valorLiquido" property="vlLiquido" dataType="currency" width="12%"/>
		
		<adsm:buttonBar>
            <adsm:button caption="adicionarDocumento" onclick="myStoreButton()" id="btnAdicionar"/>
			<adsm:removeButton caption="excluirDocumento" 
			                   service="lms.contasreceber.manterRelacaoDocumentosDepositosAction.removeByIdsItemDepositoCcorrente"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
	var dominioDocumento = new Object();
	var voltaDaExclusao = false;
	
	document.getElementById("vlDeposito").masterLink = "true";	
	document.getElementById("vlDocumento").masterLink = "true";
	document.getElementById("vlDiferenca").masterLink = "true";
	getElement("idCliente").masterLink = 'true';
	
	/** Função para buscar os somatórios dos itens de depósito */
	
	function populaSomatorios(){
	      var remoteCall = {serviceDataObjects:new Array()};
	      var dataCall = createServiceDataObject("lms.contasreceber.manterRelacaoDocumentosDepositosAction.findSomatorios", "populaSomatorios", 
	            {
	                  masterId:getElementValue("masterId"), vlDeposito:getElementValue(getTabGroup(this.document).getTab("cad").tabOwnerFrame.document.getElementById("vlDeposito"))
	            }
	      );
	      remoteCall.serviceDataObjects.push(dataCall);
	      xmit(remoteCall);  
	
	}
	
	/** Função (callBack) para popular os dados dos somatórios */
	
	function populaSomatorios_cb(data, error, erromsg){
		if (error != undefined) {
			alert(error+'');
			if( voltaDaExclusao == true ){
				voltaDaExclusao = false;
			}
			setFocusOnFirstFocusableField();
			return;
		}
		
		setElementValue(getElement("vlDeposito"), setFormat(getElement("vlDeposito"), data.vlDeposito));
		setElementValue(getElement("vlDocumento"), setFormat(getElement("vlDocumento"), data.vlDocumento));		
	    setElementValue(getElement("vlDiferenca"), setFormat(getElement("vlDiferenca"), data.vlDiferenca));
	    
	    if( voltaDaExclusao == true ){
	    	setDisabled('btnAdicionar',false);
	    	setFocus('btnAdicionar',true,true);
	    }	    
	    	    
	}
	
	function initWindow(eventObj){
		if(eventObj.name == "tab_click" || eventObj.name == "storeItemButton" || eventObj.name == "removeButton_grid"){
			if( eventObj.name == 'removeButton_grid' || eventObj.name == "tab_click"){
				voltaDaExclusao = true;
			} else {
				voltaDaExclusao = false;
			}
		    populaSomatorios();
			//disabledButtonsItens();		    
		}
	}		
	
	function myStoreButton(){ 
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
		adicionaDocumentosServicoScript();
	}

	function adicionaDocumentosServicoScript(){
		var dataGrid = itemDepositoCcorrenteGridDef.gridState.data[0];
		var url = '/contasReceber/manterRelacaoDocumentosDepositos.do?cmd=proc&masterId=' + getElementValue('masterId');
		
		if (dataGrid != null){
			url = url + '&idMoeda='+dataGrid.idMoeda;
			url = url + '&tpDocumentoAnterior='+dataGrid.tpDocumento.value;
		} else {
			url = url + '&idMoeda=';		
			url = url + '&tpDocumentoAnterior=';
		}
		url = url + '&idCliente='+getElementValue("idCliente");

		showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:780px;dialogHeight:550px;');
	}
	
	function myOnDataLoad_cb(d,e,c,x){
		onDataLoad_cb(d,e,c,x);
		populaSomatorios();
	}
	
	function myOnPageLoad_cb(d,e,c,x){
		var remoteCall = {serviceDataObjects:new Array()};
		var dataCall = createServiceDataObject("lms.contasreceber.manterRelacaoDocumentosDepositosAction.findDominioDocumento", "findDominioDocumento");
		remoteCall.serviceDataObjects.push(dataCall);
		xmit(remoteCall);  	
		onPageLoad_cb(d,e,c,x);
	}
	
	function findDominioDocumento_cb(d,e,c,x){
		if (d != undefined) {
			dominioDocumento.crt = d.CRT;
			dominioDocumento.ctr = d.CTR;
			dominioDocumento.ndn = d.NDN;
			dominioDocumento.nfs = d.NFS;
			dominioDocumento.nft = d.NFT;
			dominioDocumento.fat = d.FAT;
		}
	}
	
	function myOnPopulateRow(row, d){
		if (d.tpDocumento.value == "CTR") {
			row.childNodes[1].childNodes[0].outerText = dominioDocumento.ctr;
		}	
		if (d.tpDocumento.value == "CRT") {
			row.childNodes[1].childNodes[0].outerText = dominioDocumento.crt;
		}
		if (d.tpDocumento.value == "NDN") {
			row.childNodes[1].childNodes[0].outerText = dominioDocumento.ndn;
		}
		if (d.tpDocumento.value == "NFS") {
			row.childNodes[1].childNodes[0].outerText = dominioDocumento.nfs;
		}
		if (d.tpDocumento.value == "NFT") {
			row.childNodes[1].childNodes[0].outerText = dominioDocumento.nft;
		}
		if (d.tpDocumento.value == "FAT") {
			row.childNodes[1].childNodes[0].outerText = dominioDocumento.fat;
		}
	}

	function returnNull(){
		return false;
	}
	
	function myOnShow(){
	
		getTabGroup(this.document).getTab("cad").tabOwnerFrame;
		setElementValue("idCliente", getTabGroup(this.document).getTab("cad").tabOwnerFrame.getElementValue("cliente.idCliente"));
		return true;
		
	}

</script>