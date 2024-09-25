<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.contasreceber.inclusaoCobrancaInadimplentesAction">

	<adsm:i18nLabels>
		<adsm:include key="LMS-36111"/>
		<adsm:include key="LMS-00064"/>
	</adsm:i18nLabels>
	
	<adsm:form 
		action="/contasReceber/manterFaturas" 
		id="itemCobrancaForm" service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findByIdItemCobranca"
		idProperty="idItemCobranca" onDataLoadCallBack="myDataloadCallBack">
		
		<adsm:masterLink showSaveAll="true" idProperty="idCobrancaInadimplencia">

			<adsm:masterLinkItem property="nmPessoa" label="cliente" />

            <adsm:masterLinkItem property="nmUsuario" label="responsavel" />

            <adsm:masterLinkItem property="dsCobrancaInadimplencia" label="descricao" />

		</adsm:masterLink>
		
		<adsm:hidden property="idCliente" serializable="true"/>
		
		<adsm:hidden property="nrIdentificacaoCliente" serializable="true"/>
		
		<adsm:hidden property="siglaFilial" serializable="true"/>
		
		<adsm:hidden property="nrFatura" serializable="true"/>
		
		<adsm:hidden property="filialByIdFilial.pessoa.nmFantasia" serializable="false"/>
		
		<adsm:hidden property="tpSituacaoFaturaValido" value="2"/>
		
		<adsm:lookup label="fatura"
					 action="/municipios/manterFiliais" 
					 service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findLookupFilial" 
					 dataType="text" 
					 property="filialByIdFilial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 onchange="return changeFilial()"
					 picker="false"
					 popupLabel="pesquisarFilial"
					 size="3" 
					 maxLength="3" 
					 labelWidth="20%"
					 width="5%"
					 exactMatch="true">

							 
			<adsm:propertyMapping relatedProperty="siglaFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			
			<adsm:lookup serializable="true"
   					 	 service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findLookupFatura" 
   					 	 dataType="integer" 
   					 	 mask="0000000000"
   					 	 property="fatura" 
   	 					 idProperty="idFatura"
   	 					 criteriaProperty="nrFatura" 
   	 					 onPopupSetValue="setaFilialFatura"
   	 					 popupLabel="pesquisarFatura"
   	 					 onDataLoadCallBack="fatura"
   	 					 required="true"
   						 size="20"
   						 maxLength="16"
   					 	 width="75%"
   					  	 action="/contasReceber/manterFaturas">
   		 		
   		 		<adsm:propertyMapping formProperty="nrFatura" modelProperty="nrFatura"/>
   				
   				<adsm:propertyMapping criteriaProperty="tpSituacaoFaturaValido" modelProperty="tpSituacaoFaturaValido" inlineQuery="true"/> 
   				
   				<adsm:propertyMapping criteriaProperty="filialByIdFilial.idFilial" modelProperty="filialByIdFilial.idFilial"/> 
   				<adsm:propertyMapping criteriaProperty="filialByIdFilial.sgFilial" modelProperty="filialByIdFilial.sgFilial" inlineQuery="true"/> 
   				<adsm:propertyMapping criteriaProperty="filialByIdFilial.pessoa.nmFantasia" modelProperty="filialByIdFilial.pessoa.nmFantasia" inlineQuery="true"/> 
   				
   				<adsm:propertyMapping criteriaProperty="_nmPessoa" modelProperty="cliente.pessoa.nmPessoa" inlineQuery="true" disable="true"/> 
   				<adsm:propertyMapping criteriaProperty="idCliente" modelProperty="cliente.idCliente" inlineQuery="true" disable="true"/> 
   				<adsm:propertyMapping criteriaProperty="nrIdentificacaoCliente" modelProperty="cliente.pessoa.nrIdentificacao" disable="true"/> 
   				
	            
	            
	         </adsm:lookup>
    
        </adsm:lookup>

		<adsm:section caption="totalCobrancaInadimplentes"/>

		<adsm:textbox dataType="currency" label="valorTotalFaturas" property="somaFaturas" size="10" labelWidth="20%" width="13%" maxLength="6" disabled="true" />
		
		<adsm:textbox dataType="integer" label="qtdFaturasPendentes" property="nrFaturas" size="10" labelWidth="20%" maxLength="6" disabled="true" width="13%"/>

        <adsm:textbox dataType="text" label="moedaFatura" property="moedaFatura" size="10" labelWidth="20%" maxLength="6" disabled="true"  width="14%"/>

		<adsm:textbox dataType="currency" label="valorFaturasPendentes" property="somaFaturasPendentes" size="10" labelWidth="20%" width="13%" maxLength="6" disabled="true" />

		<adsm:textbox dataType="percent" label="valorTotalJuros" property="somaJurosFaturas" size="10" labelWidth="20%" width="13%" maxLength="6" disabled="true" />

		<adsm:textbox dataType="currency" label="valorTotalCobrado" property="totalCobrado" size="10" labelWidth="20%" width="14%" maxLength="6" disabled="true" />

		<adsm:buttonBar freeLayout="true">

			<adsm:button caption="selecionarFaturaBtn" id="selecionarFatura" onclick="openSelecaoFatura();"/>

			<adsm:button buttonType="storeButton"
						 id="storeButton"
						 onclick="myStoreButton();" 
						 caption="salvarFatura"/>
						 
			<adsm:resetButton caption="novoFatura"/>

		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid selectionMode="check" 
		idProperty="idItemCobranca" 
		property="itemCobranca" 
		service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findFaturaByIdItemCobranca"
		rowCountService="lms.contasreceber.inclusaoCobrancaInadimplentesAction.getRowCountFaturaByIdItemCobranca"
		rows="6"
		detailFrameName="item"
		autoSearch="false"
		unique="true">

		<adsm:gridColumn title="fatura" property="nrFatura" width=" " />
		<adsm:gridColumn title="cliente" property="cliente" width="110" />
		<adsm:gridColumn title="situacaoFatura" property="tpSituacaoFatura" width="65" isDomain="true"/>
		<adsm:gridColumn title="valor" dataType="text" property="moedaVlTotal" width="40"/>
		<adsm:gridColumn title="" dataType="currency" property="vlTotal" width="50"/>
		<adsm:gridColumn title="dataVencimento" dataType="JTDate" align="center" property="dtVencimento" width="10%"/>
		<adsm:gridColumn title="jurosDia" dataType="text" property="moedaJurosDia" width="40"/>
		<adsm:gridColumn title="" dataType="currency" property="jurosDia" width="35"/>
		<adsm:gridColumn title="diasAtraso" property="diasAtraso" align="right" width="52"/>
		<adsm:gridColumn title="jurosAteData" dataType="text" property="moedaJuroCalculado" width="40"/>
		<adsm:gridColumn title="" dataType="currency" property="vlJuroCalculado" width="50"/>
        <adsm:gridColumn title="percentualJurosMes" dataType="percent" property="percJurosMes" align="right" width="75"/>

	</adsm:grid>

</adsm:window>

 
<script type="text/javascript">

getElement("tpSituacaoFaturaValido").masterLink = "true";
getElement("filialByIdFilial.sgFilial").required = "true";
setDisabled("fatura.nrFatura", true);

var cobrancaInadimplencia = new Array();

/** Função para buscar os dados das faturas de uma cobrança inadimplencia */
function populaDadosFaturasByCobrancaInadimplencia(){
	var remoteCall = {serviceDataObjects:new Array()};
	var dataCall = createServiceDataObject("lms.contasreceber.inclusaoCobrancaInadimplentesAction.findDadosFaturasByCobrancaInadimplencia", "populaDadosFaturasByCobrancaInadimplencia", 
		{ idCobrancaInadimplencia:getElementValue("masterId") }
	);
	remoteCall.serviceDataObjects.push(dataCall);
	xmit(remoteCall);
		
}

/** Função (callBack) para popular os dados das faturas de uma cobrança inadimplencia */
function populaDadosFaturasByCobrancaInadimplencia_cb(data, error, erromsg){
	onDataLoad_cb(data); 
	document.getElementById("nrFaturas").masterLink = "true";
	document.getElementById("somaFaturas").masterLink = "true";
	document.getElementById("somaJurosFaturas").masterLink = "true";
	document.getElementById("totalCobrado").masterLink = "true";
	document.getElementById("moedaFatura").masterLink = "true";
	document.getElementById("somaFaturasPendentes").masterLink = "true";
}

function initWindow(eventObj){
	var tabGroup = getTabGroup(this.document);	
	
	var tabCad = tabGroup.getTab("cad");	
	var telaCad = tabCad.tabOwnerFrame;	
	
	setElementValue("nrIdentificacaoCliente", telaCad.document.getElementById("cliente.pessoa.nrIdentificacao").value);
	setElementValue("idCliente", telaCad.document.getElementById("cliente.idCliente").value);
			
	if(eventObj.name == "tab_click" || eventObj.name == "storeItemButton" || eventObj.name == "removeButton_grid"){
		populaDadosFaturasByCobrancaInadimplencia();
	}

	if (eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq"){	
		telaCad.findUsuarioLogado();
	}
	
	// Limpa todos campos da tela caso tenha mudado de tab.
	if (eventObj.name == "tab_click") {
		cleanButtonScript(this.document);
	}
	setDisabled("fatura.nrFatura", true);

}

function myDataloadCallBack_cb(data, error){
	
	if( error != undefined ){
		alert(error);
	}
	
	onDataLoad_cb(data);
	setDisabled("fatura.nrFatura", false);
}

function resetLookupFatura(){
	setElementValue("filialByIdFilial.sgFilial", "");
	setElementValue("filialByIdFilial.idFilial", "");
	setElementValue("filialByIdFilial.pessoa.nmFantasia", "");
	setElementValue("fatura.nrFatura", "");
	setDisabled("fatura.nrFatura", true);
	setElementValue("fatura.idFatura", "");
}

function populateGridAndDadosFaturas(){
	populaDadosFaturasByCobrancaInadimplencia();
	itemCobranca_cb();
}

/**
 * Função de retorno da lokup de fatura 
 */ 
function fatura_cb(d, e){
	if (e == undefined){
		if (d.length == 1){
			findFaturaInCobrancaInadimplencia(d[0].idFatura);
			setElementValue("filialByIdFilial.pessoa.nmFantasia", d[0].filialByIdFilial.pessoa.nmFantasia);
			setElementValue("filialByIdFilial.sgFilial", d[0].filialByIdFilial.sgFilial);
			setElementValue("filialByIdFilial.idFilial", d[0].filialByIdFilial.idFilial); 			
		}
	}
	return fatura_nrFatura_exactMatch_cb(d);
}

/** 
 * Função de retorno da popUp da lookup de fatura
 */
function setaFilialFatura(d, e){
	findFaturaInCobrancaInadimplencia(d.idFatura);
	setElementValue("filialByIdFilial.pessoa.nmFantasia", d.filialByIdFilial.pessoa.nmFantasia);
	setElementValue("filialByIdFilial.sgFilial", d.filialByIdFilial.sgFilial);
	setElementValue("filialByIdFilial.idFilial", d.filialByIdFilial.idFilial); 
	
	setDisabled("fatura.nrFatura", false);
	
	return true;
}

/**
 * Valida se a fatura retornada já está associada a uma cobrana de inadimplencia
 */
function findFaturaInCobrancaInadimplencia(idFatura){
	var sdo = createServiceDataObject("lms.contasreceber.inclusaoCobrancaInadimplentesAction.findFaturaInCobrancaInadimplencia",
			"findFaturaInCobrancaInadimplencia", { idFatura:idFatura, idCobranca: getElementValue("masterId") } );
	xmit({serviceDataObjects:[sdo]});
}

/**
 * CallBack da função findFaturaInCobrancaInadimplencia
 */
function findFaturaInCobrancaInadimplencia_cb(data, errors){
	// Caso a fatura esteja associada a um ou mais itemCobranca
	if(data.resp == "false"){
		if(!confirm(data.message)){
			resetLookupFatura();
		}
	}
}

function changeFilial(){
	var retorno = filialByIdFilial_sgFilialOnChangeHandler();
	
	if( retorno ){
		setDisabled("fatura.nrFatura", true);
	}else{
		setDisabled("fatura.nrFatura", false); 	
	}
	
	return retorno;
}

// Seleciona varias faturas - INICIO

	var gridSelectedIds = null;

	function openSelecaoFatura(){
		var url = '/contasReceber/manterFaturas.do?cmd=selecionarFaturaPopup&idCliente=' + getElementValue("idCliente");
		var data = showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:480px;');
	}

	function setGridSelectedIds(ids) {
		if (ids != null) {
			// Chama rotina de validacao
			myStoreButton(ids.ids);
		}
	}

	function getGridSelectedIds() {
		return this.gridSelectedIds;
	}

	function isNotExistent(id) {
		for (var i = 0; gridSelectedIds.ids.length > i; i++) {
			if (gridSelectedIds.ids[i] == id) {
				return false;
			}
		}
		return true;
	}


// Seleciona varias faturas - FIM 

function myStoreButton(ids){
	
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
	if(ids == undefined) {
		storeButtonScript('lms.contasreceber.inclusaoCobrancaInadimplentesAction.saveFatura', 'myStoreButton', document.forms[0]);	
	} else {
		var sdo = createServiceDataObject("lms.contasreceber.inclusaoCobrancaInadimplentesAction.saveFaturaBatch", "saveFaturaBatch", 
					{idFaturas:ids, idCobrancaInadimplencia:getElementValue("masterId")});
			
		xmit({serviceDataObjects:[sdo]});
	}
	
}

function saveFaturaBatch_cb(data, errorMsg, errorKey, showErrorAlert){	
	var eventObj = {name:"storeItemButton"};
	store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);		
}

function myStoreButton_cb(data, errorMsg, errorKey, showErrorAlert){	
	var eventObj = {name:"storeItemButton"};
	store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);		
}

</script>