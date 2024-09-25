<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.manterRecibosReembolsoAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/seguros/manterRecibosReembolso" onDataLoadCallBack="dataLoad"
				idProperty="idReciboReembolsoProcesso">

		<adsm:hidden property="idMoedaHidden" serializable="false"/>
		<adsm:hidden property="processoSinistro.idProcessoSinistro" serializable="true"/>
		<adsm:hidden property="processoSinistro.nrProcesso" serializable="false"/>
		<adsm:textbox label="numeroProcesso" property="processoSinistro.nrProcessoSinistro" dataType="text" labelWidth="15%" width="85%" disabled="true" />
		<adsm:textbox property="nrReciboReembolso" label="numeroRecibo" maxLength="12" size="12" dataType="integer" labelWidth="15%" width="80%" required="true" />
		<adsm:textbox property="dtEmissao" label="data" dataType="JTDate" labelWidth="15%" width="80%" required="true" />

		<adsm:complement label="valor" labelWidth="15%" width="80%" separator="branco">

		<adsm:hidden property="idMoedaHidden"        serializable="false"/>
		<adsm:combobox property="moeda.idMoeda" disabled="false"
					   boxWidth="85"
					   service="lms.seguros.manterRecibosReembolsoAction.findComboMoeda" 
					   optionProperty="idMoeda" 
					   optionLabelProperty="siglaSimbolo" 
					   onlyActiveValues="true"					   
		/>
		
		<adsm:textbox property="vlReembolso" dataType="currency" size="20" maxLength="20" disabled="true"/>
		</adsm:complement>
		
		<adsm:textbox property="vlReembolsoAvulso" label="valorAvulso" dataType="currency" labelWidth="15%" size="38" maxLength="20" required="true"/>
		
		<adsm:textarea property="obReciboReembolso" label="observacoes" maxLength="1500" columns="121" rows="5" labelWidth="15%" width="83%"/>
		
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton" service="lms.seguros.manterRecibosReembolsoAction.storeReciboReembolsoProcesso" callbackProperty="storeCallback"/>
			<adsm:newButton   id="newButton"/>
			<adsm:button id="removeButton" caption="excluir" buttonType="removeButton" onclick="onRemoveButtonClick();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	var tabGroup = getTabGroup(this.document);

	// page load callback
	function pageLoad_cb() {
		onPageLoad_cb();
		
		document.getElementById("idMoedaHidden").masterLink="true";	
		document.getElementById("processoSinistro.idProcessoSinistro").masterLink="true";
		document.getElementById("processoSinistro.nrProcessoSinistro").masterLink="true";

		if (dialogArguments.document.forms[0].elements["idProcessoSinistro"])
			setElementValue("processoSinistro.idProcessoSinistro", dialogArguments.document.forms[0].elements["idProcessoSinistro"].value);
	
		if (dialogArguments.document.forms[0].elements["nrProcessoSinistro"])
			setElementValue("processoSinistro.nrProcessoSinistro", dialogArguments.document.forms[0].elements["nrProcessoSinistro"].value);
	
		if (dialogArguments.document.forms[0].elements["moeda.idMoeda"])
			setElementValue("idMoedaHidden", dialogArguments.document.forms[0].elements["moeda.idMoeda"].value);		
		
	}
	
	// data load callback
	function dataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		if (error==undefined) {
			setDisabled('moeda.idMoeda', true);
		}
	}

	// botao remove
	function onRemoveButtonClick() {
		removeButtonScript('lms.seguros.manterRecibosReembolsoAction.removeById', 'remove', 'idReciboReembolsoProcesso', document); 
	}
	
	// callback do remove
	function remove_cb(data, error) {
		removeById_cb(data, error);		
		if (error==undefined) {
			atualizaValoresProcessoSinistro();
			resetMoeda();
		}
	}
	
	// atualiza os valore na tela de processo de sinistro
	function atualizaValoresProcessoSinistro() {
		dialogArguments.window.findAtualizaValoresDetalhamento();
	}
	
	// init window
	function initWindow(event) {
		if ((event.name=="newButton_click") || (event.name=="tab_click" && event.src.tabGroup.oldSelectedTab.properties.id=="pesq")) {
			newMaster();
			resetMoeda();
		}
	}
	
	// seta a moeda padrao
	function resetMoeda() {
			setElementValue('moeda.idMoeda', getElementValue('idMoedaHidden'));
			setDisabled('moeda.idMoeda', false);
	}
	
	// callback do store
	function storeCallback_cb(data, error) {
		store_cb(data, error);

		// se <salvar> ok, habilita (documentos)
		if(error==undefined) {
			// chamando a atualizacao dos valores da tela de processo de sinistro
			atualizaValoresProcessoSinistro()
			setDisabled('moeda.idMoeda', true);
		} 		
	}
	
	// obtém a moeda da sessão e seta nos campos de valor
	function newMaster() {
		data = new Array();
		var remoteCall = {serviceDataObjects:new Array()}; 
		remoteCall.serviceDataObjects.push(createServiceDataObject("lms.seguros.manterRecibosReembolsoAction.newMaster", null, data)); 		
		xmit(remoteCall); 		
	}
	
</script>
