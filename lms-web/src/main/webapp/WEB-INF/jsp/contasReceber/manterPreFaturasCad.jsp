<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contasreceber.manterPreFaturasAction">
	<adsm:form action="/contasReceber/manterPreFaturas" idProperty="idFatura" onDataLoadCallBack="myOnDataLoadCallBack"
		newService="lms.contasreceber.manterPreFaturasAction.newMaster">
		
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-02067"/>
		</adsm:i18nLabels>
		
		<adsm:hidden property="tpSituacaoFatura" />
		<adsm:hidden property="tpSituacaoAprovacao" />
		<adsm:hidden property="sgFilialNrFatura" />

		<adsm:textbox dataType="text" label="filialFaturamento" property="filialByIdFilial.sgFilial" size="10" maxLength="10" disabled="true" labelWidth="20%" width="30%"/>
		<adsm:textbox dataType="integer" label="numeroPreFatura" property="nrFatura" size="10" maxLength="10" disabled="true" mask="0000000000"
		labelWidth="20%" width="30%"/>
		
		<adsm:hidden property="tpSituacao" value="A"/>
		
		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			dataType="text" 
			exactMatch="true"  
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			idProperty="idCliente" 
			label="clienteResponsavel" 
			maxLength="20" 
			property="cliente" 
			service="lms.contasreceber.manterFaturasAction.findLookupCliente" 
			size="20" 
			onPopupSetValue="popupCliente"
			onDataLoadCallBack="onDataLoadCliente"			
			onchange="return myClienteOnChange();"
			labelWidth="20%"
			width="83%" required="true">
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				serializable="false"
				size="58"/>
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
		</adsm:lookup>

		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" labelWidth="20%"
			width="30%" required="true" onchange="findComboDivisaoCliente();"/>
		
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" labelWidth="20%"
			width="30%" required="true" defaultValue="N" disabled="true" onchange="findComboDivisaoCliente();"/>	
			
		<adsm:hidden property="divisaoCliente.idDivisaoClienteTmp"/>
		<adsm:hidden property="agrupamentoCliente.idAgrupamentoClienteTmp"/>
		<adsm:hidden property="tipoAgrupamento.idTipoAgrupamentoTmp"/>
			
        <adsm:combobox property="divisaoCliente.idDivisaoCliente" label="divisao" 
        		service="lms.contasreceber.manterFaturasAction.findComboDivisaoCliente" 
				optionLabelProperty="dsDivisaoCliente" 
				labelWidth="20%"
				width="30%"
				boxWidth="180"
				autoLoad="false"
				onchange="onChangeComboDivisao();"
				optionProperty="idDivisaoCliente"
				onlyActiveValues="false">							
		</adsm:combobox>
		
		<adsm:combobox property="agrupamentoCliente.idAgrupamentoCliente" 
				label="formaAgrupamento" 
				labelWidth="20%"
				width="30%"
				boxWidth="180"
				service="lms.contasreceber.manterFaturasAction.findAgrupamentoCliente" 
				optionLabelProperty="formaAgrupamento.dsFormaAgrupamento" 
				autoLoad="false"
				onchange="onChangeComboAgrupamentoCliente();"
				optionProperty="idAgrupamentoCliente">
				<adsm:propertyMapping criteriaProperty="divisaoCliente.idDivisaoCliente" modelProperty="divisaoCliente.idDivisaoCliente"/>
		</adsm:combobox>
				
		<adsm:combobox property="tipoAgrupamento.idTipoAgrupamento" 
			label="tipoAgrupamento" service="lms.contasreceber.manterFaturasAction.findComboTipoAgrupamento" 
			optionLabelProperty="dsTipoAgrupamento" optionProperty="idTipoAgrupamento" labelWidth="20%"
			boxWidth="180"
			width="30%">
			<adsm:propertyMapping criteriaProperty="agrupamentoCliente.idAgrupamentoCliente" modelProperty="agrupamentoCliente.idAgrupamentoCliente"/>
		</adsm:combobox>

		<adsm:textbox dataType="JTDate" label="dataEmissao" property="dtEmissao" size="8" maxLength="20"
			labelWidth="20%" width="30%" picker="false"
			disabled="true"/>
		<adsm:textbox dataType="JTDate" label="dataVencimento" property="dtVencimento" size="8" maxLength="20"
			labelWidth="20%" width="30%" picker="false"
			disabled="true"/>

		<adsm:section caption="totaisImpostos" />

		<adsm:textbox labelWidth="20%" dataType="integer" label="qtdeTotalDocumentos" property="qtDocumentos" size="10" 
			maxLength="6" width="30%" disabled="true"/>
		<adsm:textbox labelWidth="20%" dataType="text" label="moeda" width="30%" property="moeda" size="10" maxLength="20" disabled="true"/>

	<adsm:textbox labelWidth="20%" dataType="currency" label="valorIVA" property="vlIva" size="10" maxLength="20" width="30%" disabled="true"/>
	<adsm:textbox labelWidth="20%" width="30%" dataType="currency" label="valorTotalFrete" property="vlTotal" size="10" maxLength="18"  disabled="true"/>

		<adsm:textbox labelWidth="20%" dataType="currency" label="valorTitulo" property="vlTotalRecebido" size="10" maxLength="18" width="13%" disabled="true"/>

		<adsm:buttonBar>
			<adsm:button buttonType="storeButton" caption="salvar" id="storeButton" onclick="return myStore()"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script>

	

	function initWindow(eventObj){
		var documentTabDocumentos = getTabGroup(this.document).getTab("documentoServico").tabOwnerFrame.document;
		
		if ((eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq") 
			|| eventObj.name == "newButton_click" || eventObj.name == "removeButton") {
			buscaMoeda();
			setElementValue("vlTotal", "0,00");
			resetComboDivisao()
		}
		if (eventObj.name == "storeButton"){
			copyMasterLink(this.document, documentTabDocumentos);
		}
	}
	
	/**
	*	Busca a moeda padrao do brasil
	*/
	function buscaMoeda(){
		
			var dados = new Array();
			_serviceDataObjects = new Array();
            addServiceDataObject(createServiceDataObject("lms.contasreceber.manterPreFaturasAction.findMoedaBrasil",
                                                     "buscaMoeda",
                                                     dados));
        	xmit(false);
    
	}


/* retorno da busca da moeda */
	function buscaMoeda_cb(data, erro, errorCode){

		if( erro != undefined ){
			alert(erro);
			return false;		
		}
		
		if (data != undefined){
			setElementValue('moeda', data.moeda);
		}
	}


	function myStore(){

		if (document.getElementById("divisaoCliente.idDivisaoCliente").options.length > 1) {
			document.getElementById("divisaoCliente.idDivisaoCliente").required = "true";
		} else {
			document.getElementById("divisaoCliente.idDivisaoCliente").required = "false";
		}

		if (document.getElementById("agrupamentoCliente.idAgrupamentoCliente").options.length > 1) {
			document.getElementById("agrupamentoCliente.idAgrupamentoCliente").required = "true";
		} else {
			document.getElementById("agrupamentoCliente.idAgrupamentoCliente").required = "false";
		}
		
		if (document.getElementById("tipoAgrupamento.idTipoAgrupamento").options.length > 1) {
			document.getElementById("tipoAgrupamento.idTipoAgrupamento").required = "true";
		} else {
			document.getElementById("tipoAgrupamento.idTipoAgrupamento").required = "false";
		}			
	
		storeButtonScript("lms.contasreceber.manterPreFaturasAction.store", "myStore", document.forms[0]);
	}

	function myStore_cb(data, error){
		if (error != undefined) {
			alert(error);
			return false;
		}		
		
		setElementValue("sgFilialNrFatura",	data.filialByIdFilial.sgFilial + " " +  
			setFormat('nrFatura', data.nrFatura));		
		store_cb(data, error);
		
		/*
		var documentTabDocumentos = getTabGroup(this.document).getTab("documentoServico").tabOwnerFrame.document;
		documentTabDocumentos.getElementById("storeAllButtton").disabled = true;
		*/
		
	}
	
	function myOnShow(evento){
		setElementValue("qtDocumentos", getTabGroup(this.document).getTab("documentoServico").tabOwnerFrame.getElementValue("qtdeTotalDocumentos"));
		setElementValue("vlTotal", setFormat(getElement("vlTotal"), getTabGroup(this.document).getTab("documentoServico").tabOwnerFrame.getElementValue("valorTotalDocumentos")));
	}
	
	/**
	*	Habilita/desabilita o botão Excluir
	*/	
	function myOnDataLoadCallBack_cb(d, erro, errorCode, eventObj){
		onDataLoad_cb(d,erro, errorCode, eventObj);
		
		setElementValue("sgFilialNrFatura",getElementValue('filialByIdFilial.sgFilial') + " " + setFormat('nrFatura',getElementValue( 'nrFatura' ))  );		
		
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false};
		
		var objDivisao = {idCliente: d.cliente.idCliente, tpModal: d.tpModal, tpAbrangencia: d.tpAbrangencia};
		
		if (d.divisaoCliente != undefined && d.divisaoCliente.idDivisaoClienteTmp != undefined){
			objDivisao.idDivisao = d.divisaoCliente.idDivisaoClienteTmp;
		}		 
		
		var sdoDivisao = createServiceDataObject("lms.contasreceber.manterFaturasAction.findComboDivisaoCliente","findComboDivisaoCliente", objDivisao);
		remoteCall.serviceDataObjects.push(sdoDivisao); 
		
		if (d.divisaoCliente != undefined&& d.divisaoCliente.idDivisaoClienteTmp != undefined) {
			var objAgrupamento = {divisaoCliente:{idDivisaoCliente:d.divisaoCliente.idDivisaoClienteTmp}};
			
			if (d.agrupamentoCliente != undefined && d.agrupamentoCliente.idAgrupamentoClienteTmp != null){
				objAgrupamento.idAgrupamentoCliente = d.agrupamentoCliente.idAgrupamentoClienteTmp;
			}
			
			var sdoAgrupamento = createServiceDataObject("lms.contasreceber.manterFaturasAction.findAgrupamentoCliente","findComboAgrupamentoCliente", objAgrupamento);
			remoteCall.serviceDataObjects.push(sdoAgrupamento);
		}
		
		if (d.agrupamentoCliente != undefined && d.agrupamentoCliente.idAgrupamentoClienteTmp != null) {
			var sdoForma = createServiceDataObject("lms.contasreceber.manterFaturasAction.findComboTipoAgrupamento","findComboTipoAgrupamento", {agrupamentoCliente:{idAgrupamentoCliente: d.agrupamentoCliente.idAgrupamentoClienteTmp}});
			remoteCall.serviceDataObjects.push(sdoForma);
		}

		xmit(remoteCall);
		
		setElementValue("tpSituacaoAprovacao",d.tpSituacaoAprovacao);
		// Caso esteja detalhando ou tenha incluído uma pre fatura, a mesma não pode mais ser editada.
		//if( getElementValue("tpSituacaoAprovacao")!= "E" ){
		//	setDisabled("storeButton", true);
		//}
	}
	
	/** Busca as divisões do cliente */
	function findComboDivisaoCliente(idCliente){
		if (idCliente == undefined){
			idCliente = getElementValue("cliente.idCliente");
		}
		
		if (getElementValue("tpModal") != "" && getElementValue("tpAbrangencia") != "" && idCliente != ""){
			var data = new Array();	   
			
			setNestedBeanPropertyValue(data, "idCliente", idCliente);
			setNestedBeanPropertyValue(data, "tpModal", getElementValue("tpModal"));
			setNestedBeanPropertyValue(data, "tpAbrangencia", getElementValue("tpAbrangencia"));
			
			var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findComboDivisaoCliente","findComboDivisaoCliente", data);
			xmit({serviceDataObjects:[sdo]});
		} else {
			resetComboDivisao();
		}
	}
	
	/** 
	  * CallBack da função findComboDivisaoCliente 
	  */
	function findComboDivisaoCliente_cb(data, error){
		
		if(error != undefined){
			alert(error);
		}
		
		divisaoCliente_idDivisaoCliente_cb(data);
		
		if(getElement("divisaoCliente.idDivisaoCliente").options.length > 1){
			getElement("divisaoCliente.idDivisaoCliente").required = "true";
			setDisabled("divisaoCliente.idDivisaoCliente", false);
			
			if(getElement("divisaoCliente.idDivisaoCliente").options.length == 2)
				setElementValue("divisaoCliente.idDivisaoCliente", getElement("divisaoCliente.idDivisaoCliente").options[1]);
			
		}else{
			resetComboDivisao();
		}
		
		if (getElementValue("divisaoCliente.idDivisaoClienteTmp") != ""){
			setElementValue("divisaoCliente.idDivisaoCliente", getElementValue("divisaoCliente.idDivisaoClienteTmp"));
		} else {
			getElement("divisaoCliente.idDivisaoCliente").options[0].selected = true;
		}
	}
	
	function resetComboDivisao(){
		getElement("divisaoCliente.idDivisaoCliente").options.length = 1;
		setElementValue("divisaoCliente.idDivisaoClienteTmp", "");
		getElement("divisaoCliente.idDivisaoCliente").options[0].selected = true;
		
		resetComboAgrupamentoCliente();
		resetComboTipoAgrupamento();
	}
	
	function onChangeComboDivisao(){
		setElementValue("divisaoCliente.idDivisaoClienteTmp", "");
		resetComboAgrupamentoCliente()
		
		if (getElementValue("divisaoCliente.idDivisaoCliente") != ""){
			findComboAgrupamentoCliente(getElementValue("divisaoCliente.idDivisaoCliente"));
		}
	}
	
	
	/** Busca as divisões do cliente */
	function findComboAgrupamentoCliente(idDivisao){
		var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findAgrupamentoCliente","agrupamentoCliente.idAgrupamentoCliente", {divisaoCliente:{idDivisaoCliente:idDivisao}});
		xmit({serviceDataObjects:[sdo]});
	}
	
	/** 
	  * CallBack da função findComboDivisaoCliente 
	  */
	function findComboAgrupamentoCliente_cb(data, error){
		
		if(error != undefined){
			alert(error);
		}
		
		agrupamentoCliente_idAgrupamentoCliente_cb(data);
		
		if(getElement("agrupamentoCliente.idAgrupamentoCliente").options.length > 1){
			getElement("agrupamentoCliente.idAgrupamentoCliente").required = "true";
			
			if(getElement("agrupamentoCliente.idAgrupamentoCliente").options.length == 2)
				getElement("agrupamentoCliente.idAgrupamentoCliente").options[1].selected = true;
			
		}else{
			resetComboAgrupamentoCliente();
		}
		
		if (getElementValue("agrupamentoCliente.idAgrupamentoClienteTmp") != ""){
			setElementValue("agrupamentoCliente.idAgrupamentoCliente", getElementValue("agrupamentoCliente.idAgrupamentoClienteTmp"));
		} else {
			getElement("agrupamentoCliente.idAgrupamentoCliente").options[0].selected = true;
		}
	}
	
	function resetComboAgrupamentoCliente(){
		getElement("agrupamentoCliente.idAgrupamentoCliente").options.length = 1;
		setElementValue("agrupamentoCliente.idAgrupamentoClienteTmp", "");
		getElement("agrupamentoCliente.idAgrupamentoCliente").options[0].selected = true;
		resetComboTipoAgrupamento();
	}

	function onChangeComboAgrupamentoCliente(){
		setElementValue("agrupamentoCliente.idAgrupamentoClienteTmp", "");
		resetComboTipoAgrupamento();
		
		if (getElementValue("agrupamentoCliente.idAgrupamentoCliente") != ""){
			findComboTipoAgrupamento(getElementValue("agrupamentoCliente.idAgrupamentoCliente"));
		}
	}	

	/** Busca as divisões do cliente */
	function findComboTipoAgrupamento(idAgrupamento){
		var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findComboTipoAgrupamento","findComboTipoAgrupamento", {agrupamentoCliente:{idAgrupamentoCliente:idAgrupamento}});
		xmit({serviceDataObjects:[sdo]});
	}
	
	/** 
	  * CallBack da função findComboDivisaoCliente 
	  */
	function findComboTipoAgrupamento_cb(data, error){
		
		if(error != undefined){
			alert(error);
		}
		
		tipoAgrupamento_idTipoAgrupamento_cb(data);
		
		if(getElement("tipoAgrupamento.idTipoAgrupamento").options.length > 1){
			getElement("tipoAgrupamento.idTipoAgrupamento").required = "true";
			
			if(getElement("tipoAgrupamento.idTipoAgrupamento").options.length == 2)
				getElement("tipoAgrupamento.idTipoAgrupamento").options[1].selected = true;
			
		}else{
			resetComboTipoAgrupamento();
		}

		if (getElementValue("tipoAgrupamento.idTipoAgrupamentoTmp") != ""){
			setElementValue("tipoAgrupamento.idTipoAgrupamento", getElementValue("tipoAgrupamento.idTipoAgrupamentoTmp"));
		} else {
			getElement("tipoAgrupamento.idTipoAgrupamento").options[0].selected = true;
		}
	}
	
	function resetComboTipoAgrupamento(){
		getElement("tipoAgrupamento.idTipoAgrupamento").options.length = 1;
		getElement("tipoAgrupamento.idTipoAgrupamento").options[0].selected = true;
		setElementValue("tipoAgrupamento.idTipoAgrupamentoTmp", "");
	}

	function popupCliente(data){
		if (data == undefined) {
			return;
		}
		resetComboDivisao();
		findComboDivisaoCliente(data.idCliente);
	}

	function onDataLoadCliente_cb(data){
		cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
		if (data != undefined && data.length == 1) {
			findComboDivisaoCliente(data[0].idCliente);
		}
	}
	
	function myClienteOnChange(){
		resetComboDivisao();
		return cliente_pessoa_nrIdentificacaoOnChangeHandler();
	}	
</script>