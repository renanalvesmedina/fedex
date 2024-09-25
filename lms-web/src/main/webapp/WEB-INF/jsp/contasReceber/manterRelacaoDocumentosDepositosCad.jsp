<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contasreceber.manterRelacaoDocumentosDepositosAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form idProperty="idDepositoCcorrente"
				id="depositoCcorrente.form"
				action="/contasReceber/manterRelacaoDocumentosDepositos" 
				newService="lms.contasreceber.manterRelacaoDocumentosDepositosAction.newMaster" onDataLoadCallBack="myOnDataLoad">
		
		<adsm:hidden property="blRelacaoIdentificada"/>		
		
		<adsm:hidden property="tpSitucacao" value="A"/>
		<adsm:lookup label="cliente"			                                      
			service="lms.contasreceber.manterRelacaoDocumentosDepositosAction.findLookupClienteResponsavelAtivo" 
			dataType="text"
			property="cliente" 
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			exactMatch="true" 
			size="20"
			maxLength="20" 
			width="85%" 
			action="/vendas/manterDadosIdentificacao" required="true">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="cliente.pessoa.nmPessoa"/>
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSitucacao"/>
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" disabled="true" size="60"/>  
		</adsm:lookup>
		
		
		<adsm:textbox label="numeroProtocolo" dataType="integer" property="idDepositoCcorrenteTmp" size="10"  disabled="true"/>
		<adsm:combobox 
			service="lms.contasreceber.manterRelacaoDocumentosDepositosAction.findComboByActiveValues" 
			optionLabelProperty="comboText" 
			optionProperty="idCedente" 
			property="cedente.idCedente" 
			boxWidth="230"
			label="banco"
			onlyActiveValues="true"
			required="true"> 
		</adsm:combobox>
		<adsm:textbox label="dataDeposito" property="dtDeposito" dataType="JTDate" required="true"/>

		
		<adsm:textbox label="valorDeposito" 
					  dataType="text" 
					  property="siglaSimbolo" 
					  disabled="true" 
					  size="10" 
					  maxLength="10">
		<adsm:textbox property="vlDeposito" dataType="currency" required="true"/>
		</adsm:textbox>
		
        <adsm:checkbox label="relacaoFechada" property="blRelacaoFechada" disabled="true"/>

        <adsm:combobox defaultValue="A" label="situacao" property="tpSituacaoRelacao" domain="DM_STATUS_RELACAO" width="85%" required="true" disabled="true"/>

		<adsm:textarea label="observacao" property="obDepositoCcorrente" maxLength="255" columns="100" rows="3" width="100%" />
		<adsm:buttonBar>
				<adsm:reportViewerButton id="reportViewerButton" caption="imprimirDeposito" service="lms.contasreceber.emitirDepositoContaCorrenteAction" disabled="true"/>
				<adsm:button id="btnIdentificarDeposito" caption="identificarDeposito" onclick="executeidentificarDepositoCcorrente();"/>
				<adsm:button id="btnFecharRelacao" caption="fecharRelacao" onclick="fecharRelacaoScript();"/>
				<adsm:storeButton id="storeButton" />
				<adsm:newButton/>
				<adsm:removeButton/>
		</adsm:buttonBar>
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-36070"/>
		</adsm:i18nLabels>
	
	
	</adsm:form>
</adsm:window>

<script>
	getElement('vlDeposito').label = getElement('siglaSimbolo').label;

	function myStore_cb(d, e){
		store_cb(d);
		onDataLoad(d.idDepositoCcorrente);
	}
	
	function myOnPageLoad_cb(d,e,c,x){
		onPageLoad_cb(d,e,c,x);
		findInitValues();
	}

	function myOnDataLoad_cb(d,e,c,x){
		
		if (e != undefined) {
			alert(e+'');
		}
		
		onDataLoad_cb(d,e,c,x);
		
		// Caso a relação esteja encerrada, desabilitar todos campos exceto a observação
		var blRelacaoFechada = d.blRelacaoFechada;
		disableFields(blRelacaoFechada);
		validateFields(blRelacaoFechada);
		validateBtnIdentificarDeposito();
		validateFilial();
		
	}	
	
	function validateFields (blRelacaoFechada) {
		setDisabled("btnFecharRelacao", blRelacaoFechada == "true");
		setDisabled("vlDeposito", blRelacaoFechada == "true");
	}
	
	function validateBtnIdentificarDeposito () {
		var blRelacaoFechada = getElementValue("blRelacaoFechada");
		var blRelacaoIdentificada = getElementValue("blRelacaoFechada");
		
		// Caso a relação não esteja identificada e esteja fechada,
		// habilita o botão.
		if (blRelacaoFechada && !blRelacaoIdentificada ) {
			setDisabled("btnIdentificarDeposito", false);
		} else {
			setDisabled("btnIdentificarDeposito", true);
		}
	}
	
	function findInitValues(){
		_serviceDataObjects = new Array(); 
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterRelacaoDocumentosDepositosAction.findInitValues", "findInitValues")); 
		
		xmit(false);		
	} 
	
	/** Busca os valores padroes para moeda e valida o campo tpSituacaoRelacao de acordo com o tipo do cliente */
	function findInitValues_cb(d, e){	
		
		if (e == null) {
			setElementValue("siglaSimbolo", d.siglaSimbolo);
		}
	}
	
	function fecharRelacao_cb(d, e){	
		if (e == null) {
			setElementValue("blRelacaoFechada", "true");
			showSuccessMessage();
			disableFields(true);
			setDisabled("btnFecharRelacao", true);
			setDisabled("vlDeposito", true);
			setFocusOnNewButton();
		} else {
			alert(e+'');
		}
	}
	
	/**
	  * Altera a situação do depósito para identificado
	  */
	function executeidentificarDepositoCcorrente(){
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		
		// Caso tenha ocorrido alguma alteração nas outras abas
		// lança a mensagem solicitando que as alterações sejam salvas.
		if (tabCad.hasChanged()) {
			alert(i18NLabel.getLabel("LMS-36244"));
			return false;
		}
		
		var data = new Array();
		setNestedBeanPropertyValue(data, "idDepositoCcorrente", getElementValue("idDepositoCcorrente"));
		setNestedBeanPropertyValue(data, "dtDeposito", getElementValue("dtDeposito"));
		_serviceDataObjects = new Array(); 
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterRelacaoDocumentosDepositosAction.executeIdentificarDepositoCcorrente", "executeidentificarDepositoCcorrente", data)); 
		
		xmit(false);
		
	}
	
	/**
	  * CallBack da função executeidentificarDepositoCcorrente
	  */
	function executeidentificarDepositoCcorrente_cb(data, error){
		
		if(error != undefined){
			alert(error);
		}else{
			setDisabled("btnIdentificarDeposito", true);
			setElementValue("tpSituacaoRelacao", data.tpSituacaoRelacao);
			showSuccessMessage();
		}
		
	}
	
	function fecharRelacaoScript(){
		
		var data = buildFormBeanFromForm(document.getElementById("depositoCcorrente.form"));
		_serviceDataObjects = new Array(); 
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterRelacaoDocumentosDepositosAction.executeFecharDepositoCcorrente", "fecharRelacao", data)); 
		xmit(false); // deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.	
	}
	
	function validateBtnFecharRelacao(){
		if (getElementValue("blRelacaoFechada")) {
			setDisabled("btnFecharRelacao", true);
		}
	}
	
	function initWindow(evento){
		if (evento.name == 'tab_click' && getElementValue("idDepositoCcorrente") != "") {
			validateBtnFecharRelacao();
			validateBtnIdentificarDeposito();
		}
		
		if ((evento.name == "tab_click" || evento.name == "storeButton") && getElementValue("idDepositoCcorrente") != ""){
			setDisabled("reportViewerButton", false);
			setElementValue('idDepositoCcorrenteTmp',getElementValue("idDepositoCcorrente"));
		} else {
			setDisabled("reportViewerButton", true);
		}

		//Ao detalhar o registro, verifica a filial do usuario logado 
		if ((evento.name == "tab_click" || evento.name == "storeButton" ||evento.name == "gridRow_click" ) 
															&& getElementValue("idDepositoCcorrente") != ""){
			validateFilial();	
		}

		if (evento.name != "storeButton"
				&& evento.name != "gridRow_click"
				&& getElementValue("idDepositoCcorrente") == ""){
			disableFields(false);
			setDisabled("vlDeposito", false);
			setFocusOnFirstFocusableField();
		}
		
	}
	
	function validateFilial(){
		var data = new Object();
			_serviceDataObjects = new Array(); 
			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterRelacaoDocumentosDepositosAction.findFilialUsuarioLogado", "findFilialUsuarioLogado", data)); 
			xmit(false);
	}
	
	function findFilialUsuarioLogado_cb(data,error){
		if (data.sgFilial != "MTZ"){
			setDisabled("btnIdentificarDeposito",true);
		}else{
			if (getElementValue("blRelacaoFechada")) {
				setDisabled("btnIdentificarDeposito",false);
			}else{
				setDisabled("btnIdentificarDeposito",true);
			}
		}
		if (getElementValue("tpSituacaoRelacao")!="A"){
			setDisabled("btnIdentificarDeposito", true);
		}
	}
	
	/**
	  * habilita ou desabilita os campos da tela
	  */
	function disableFields(simNao){
		
		setDisabled("cliente.idCliente", simNao);
		setDisabled("cedente.idCedente", simNao);
		setDisabled("dtDeposito", simNao);
		setDisabled("siglaSimbolo", simNao);
		//setDisabled("vlDeposito", simNao);
	
	}
	
	document.getElementById("siglaSimbolo").masterLink = "true";
</script>