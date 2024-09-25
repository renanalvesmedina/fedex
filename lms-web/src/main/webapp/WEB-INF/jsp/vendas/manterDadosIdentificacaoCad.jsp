<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript" type="text/javascript">
	function myOnPageLoad() {
		onPageLoad();

		initPessoaWidget({
			tpTipoElement:document.getElementById("pessoa.tpPessoa"),
			tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"),
			numberElement:document.getElementById("pessoa.idPessoa")
		});

	}

	function validateUsuariosResponsaveisByCliente(){
		var data = new Object();
		data.idCliente = getElementValue("pessoa.idPessoa");
		var service = "lms.vendas.manterClienteAction.validateUsuariosResponsaveisByCliente";
		var sdo = createServiceDataObject(service, "validateUsuariosResponsaveisByCliente", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function validateUsuariosResponsaveisByCliente_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
	}
</script>
<adsm:window
	service="lms.vendas.manterClienteAction"
	onPageLoad="myOnPageLoad">

	<adsm:i18nLabels>
		<adsm:include key="cliente"/>
		<adsm:include key="LMS-01104"/>
		<adsm:include key="LMS-01140"/>
		<adsm:include key="LMS-01142"/>
		<adsm:include key="LMS-01146"/>
		<adsm:include key="LMS-01157"/>
		<adsm:include key="LMS-01336"/>
		<adsm:include key="LMS-01343"/>
	</adsm:i18nLabels>

	<adsm:form
		action="/vendas/manterDadosIdentificacao"
		id="formCad"
		idProperty="idCliente"
		onDataLoadCallBack="myOnDataLoad">

		<adsm:hidden property="pessoa.dsEmail"/>
		<adsm:hidden property="tpClienteOriginal"/>
		<adsm:hidden property="pessoa.idPessoa"/>
		<adsm:hidden property="pessoa.dhInclusao"/>
		<adsm:hidden property="temPendenciaWKTpCliente"/>
		<adsm:hidden property="usuarioByIdUsuarioInclusao.idUsuario"/>
		<adsm:hidden property="usuarioByIdUsuarioAlteracao.idUsuario"/>
		<adsm:hidden property="dsMotivoSolicitacao"/>
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:hidden property="blClientePostoAvancado"  serializable="true"/>
		<adsm:hidden property="informacaoDoctoClienteConsolidado.idInformacaoDoctoCliente"  serializable="true"/>
		<adsm:hidden property="blLiberaEtiquetaEdiLinehaul"  serializable="true"/>

		<adsm:hidden property="blProdutoPerigoso"  serializable="true"/>
		<adsm:hidden property="blControladoPoliciaCivil"  serializable="true"/>
		<adsm:hidden property="blControladoPoliciaFederal"  serializable="true"/>
		<adsm:hidden property="blControladoExercito"  serializable="true"/>

		<adsm:hidden property="blNaoPermiteSubcontratacao"  serializable="true"/>
		<adsm:hidden property="blEnviaDocsFaturamentoNas"  serializable="true"/>

		<adsm:hidden property="blValidaCobrancDifTdeDest"  serializable="true"/>
		<adsm:hidden property="blCobrancaTdeDiferenciada"  serializable="true"/>

		<adsm:combobox
			definition="TIPO_PESSOA.cad"
			labelWidth="15%"
			width="35%"
			label="tipoPessoa"
			domain="DM_TIPO_PESSOA"
			required="true"
			onchange="return tpPessoaOnChange();"
			autoLoad="false"
			renderOptions="true"/>

		<adsm:complement label="identificacao" required="true" labelWidth="17%" width="33%">
			<adsm:combobox
				definition="TIPO_IDENTIFICACAO_PESSOA.cad"
				autoLoad="false"
				renderOptions="true"/>

			<adsm:lookup
				definition="IDENTIFICACAO_PESSOA"
				service="lms.vendas.manterClienteAction.findPessoa"
				onDataLoadCallBack="pessoaCallback">

				<adsm:propertyMapping
					modelProperty="tpIdentificacao"
					criteriaProperty="pessoa.tpIdentificacao" />

				<adsm:propertyMapping
					modelProperty="tpPessoa"
					criteriaProperty="pessoa.tpPessoa" />

				<adsm:propertyMapping
					modelProperty="nmPessoa"
					relatedProperty="pessoa.nmPessoa"
					blankFill="false" />

				<adsm:propertyMapping
					modelProperty="dsEmail"
					relatedProperty="pessoa.dsEmail" />
			</adsm:lookup>
		</adsm:complement>

		<adsm:textbox
			dataType="text"
			size="47"
			property="pessoa.nmPessoa"
			label="nomeRazaoSocial"
			maxLength="50"
			labelWidth="15%"
			width="70%"
			required="true"/>

		<adsm:combobox
			property="tpCliente"
			onlyActiveValues="true"
			domain="DM_TIPO_CLIENTE"
			label="tipoCliente"
			labelWidth="15%"
			width="35%"
			required="true"
			autoLoad="false"
			renderOptions="true"
			disabled="true"/>

		<adsm:combobox
			property="tpClienteSolicitado"
			onlyActiveValues="true"
			domain="DM_TIPO_CLIENTE"
			label="tipoClienteSolicitado"
			labelWidth="17%"
			onchange="return tpClienteOnChange();"
			width="33%"
			required="true"
			autoLoad="false"
			renderOptions="true"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button
				caption="concluirCadastro"
				id="btnConcluirCadastro"
				onclick="saveCliente(true);"
				disabled="true"
				boxWidth="140"/>
			<adsm:button
				caption="salvar"
				buttonType="storeButton"
				onclick="saveCliente(false);"
				id="btnStoreButton"
				disabled="false"/>
			<adsm:newButton />
			<adsm:removeButton/>
			<adsm:button caption="historicoWK" id="btnHistoricoWK" onclick="openModalHistoricoWorkflow()" disabled="false"/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:tabGroup selectedTab="0">
		<adsm:tab onShow="myOnShow" title="identificacao" id="identificacao" src="/vendas/manterDadosIdentificacao" cmd="identificacao" height="295"/>
		<adsm:tab onShow="myOnShow" masterTabId="identificacao" title="comercial" id="comercial" src="/vendas/manterDadosIdentificacao" cmd="comercial" height="295"/>
		<adsm:tab onShow="myOnShow" masterTabId="identificacao" title="operacional" id="operacional" src="/vendas/manterDadosIdentificacao" cmd="operacional" height="295"/>
		<adsm:tab onShow="myOnShow" masterTabId="identificacao" title="financeiro" id="financeiro" src="/vendas/manterDadosIdentificacao" cmd="financeiro" height="295"/>
	</adsm:tabGroup>

</adsm:window>
<script language="javascript" type="text/javascript">
	var tpPessoa = {FISICA:'F', JURIDICA:'J'};
	var tpCliente = {EVENTUAL:'E', ESPECIAL:'S', POTENCIAL:'P', FILIAL:'F'};
	var modo = {INCLUSAO:"1", EDICAO:"2"};

	var tabIdentificacao;
	var tabComercial;
	var tabOperacional;
	var tabFinanceiro;
	var telaIdentificacao;
	var telaComercial;
	var telaOperacional;
	var telaFinanceiro;
	var mostrouPopupMotivo;

	var _modoTela;

	function openModalHistoricoWorkflow() {
		var param = "&idProcesso=" + getElementValue("idCliente");
		param += "&nmTabela=CLIENTE";
		param += "&cliente.idCliente=" + document.getElementById("idCliente").value;
		param += "&cliente.pessoa.nmPessoa=" + escape(document.getElementById("pessoa.nmPessoa").value);
		param += "&cliente.pessoa.nrIdentificacao=" + document.getElementById("pessoa.nrIdentificacao").value;

		var url = '/workflow/historicoWorkflow.do?cmd=list' + param;
		var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;';
		showModalDialog(url, window, wProperties);
	}

	function pessoaCallback_cb(data,erro) {
		if(erro != undefined) {
			alert(erro);
			newButtonScript(this.document);
		}

		var retorno = pessoa_nrIdentificacao_exactMatch_cb(data);
		// Se Pessoa cadastrada
		if (data.idPessoa != undefined){
			setElementValue("pessoa.nmPessoa", data.nmPessoa);
			setElementValue("pessoa.idPessoa", data.idPessoa);
			telaIdentificacao.setDadosPessoa(data);
		}
		return retorno;
	}

	function initWindow(eventObj) {

		if (eventObj.name == "tab_click") {
			initTabVariables();
			selectTabIdentificacao();
			setModoTela(modo.INCLUSAO);
			initInclusao();
			controlarCamposWK();
		} else if (eventObj.name == "gridRow_click") {
			initTabVariables();
			selectTabIdentificacao();
// 			validateUsuariosResponsaveisByCliente();
		} else if (eventObj.name == "newButton_click" || eventObj.name == "removeButton") {
			selectTabIdentificacao();
			setModoTela(modo.INCLUSAO);
			initInclusao();
		}

		viewWorkflow();
	}

	function getIdProcessoWorkflow(){
		var url = new URL(parent.location.href);
		return url.parameters.idProcessoWorkflow;
	}

	function viewWorkflow(){
		var idProcessoWorkflow = getIdProcessoWorkflow();
		if (idProcessoWorkflow != undefined && idProcessoWorkflow != "") {
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service, 'viewWorkflow',{id:idProcessoWorkflow});
			xmit({serviceDataObjects:[sdo]});
	 	}
	}

	function viewWorkflow_cb(data, errorMessage, errorMsg, eventObj) {
		if (errorMessage != undefined) {
			alert(errorMessage);
			return false;
		}
		onDataLoad_cb(data, errorMessage, errorMsg, eventObj);
		setElementValue("pessoa.nmPessoa", data.pessoa.nmPessoa);
		setElementValue("pessoa.nrIdentificacao", data.pessoa.nrIdentificacao);
		setElementValue("pessoa.idPessoa", data.pessoa.idPessoa);
		onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), numberElement:document.getElementById("pessoa.nrIdentificacao")});
		setModoTela(modo.EDICAO);

		if (data.pessoa.nrIdentificacao != undefined &&
			data.pessoa.tpIdentificacao != undefined &&
			data.pessoa.tpPessoa != undefined) {
			setDisabled("pessoa.tpPessoa", true);
			setDisabled("pessoa.tpIdentificacao", true);
			setDisabled("pessoa.nrIdentificacao", true);
		} else {
			setDisabled("pessoa.tpPessoa", false);
			setDisabled("pessoa.nrIdentificacao", false);
		}
		setElementValue("tpClienteOriginal", data.tpCliente.value);

		initTabVariables();

		waitLoadWK(data);
	}

	function waitLoadWK(data) {
		if (getBlockUISemaphore().value == 0) {
			var doc = getMessageDocument(document);
			var messageSign = doc.getElementById("message.div");
			if (messageSign) {
				if (messageSign.systemMsg == true) {
					messageSign.systemMsg = false;
					messageSign.style.visibility = "hidden";
				}
				cancelAnimation(messageSign, doc);
				clearInterval(messageSign.timerId);
				messageSign.timerId = null; // remove o timerId do elemento
			}

			changeSubAbasFieldStatus(getModoTela());
			telaIdentificacao.loadData(data);
			telaComercial.loadData(data);
			telaOperacional.loadData(data);
			telaFinanceiro.loadData(data);
			telaIdentificacao.tpPessoaOnChange(getElementValue("pessoa.tpPessoa"));

			// Bloquear campos
			setDisabled(document, true);
			setDisabled('btnStoreButton', true);
			setDisabled('btnConcluirCadastro', true);

			telaIdentificacao.disableButtons(true);
			telaIdentificacao.disableAll();
			telaComercial.disableButtons(true);
			telaComercial.disableAll();
			telaOperacional.disableButtons(true);
			telaOperacional.disableAll();
			telaFinanceiro.disableButtons(true);
			telaFinanceiro.disableAll();

			return;
		}

		setTimeout(function() {
			waitLoadWK(data);
		}, 100)
	}


	function initInclusao() {
		resetValue(document);
		// limpa os dados das sub-abas
		cleanSubAbas();
		// desabilita as abas de detalhamento
		disableSubAbas();
		// busca os valores default
		findDadosSessao();
		setDisabled("pessoa.tpPessoa", false);
	}

	function initTabVariables() {
		if (tabIdentificacao == undefined) {
			var tabGroup = getTabGroup(document);
			var childTabGroup = tabGroup.getTab("cad").childTabGroup;
			tabIdentificacao = childTabGroup.getTab("identificacao");
			tabComercial = childTabGroup.getTab("comercial");
			tabOperacional = childTabGroup.getTab("operacional");
			tabFinanceiro = childTabGroup.getTab("financeiro");

			telaIdentificacao = tabIdentificacao.tabOwnerFrame;
			telaComercial = tabComercial.tabOwnerFrame;
			telaOperacional = tabOperacional.tabOwnerFrame;
			telaFinanceiro = tabFinanceiro.tabOwnerFrame;
		}
	}

	/**
	 * Habilita ou desabilita as abas internas da tela de detalhamento.
	 */
	function changeChildTabsStatus(status) {
		var tabGroup = getTabGroup(document);
		var childTabGroup = tabGroup.getTab("cad").childTabGroup;
		childTabGroup.setDisabledTab("identificacao", status);
		childTabGroup.setDisabledTab("comercial", status);
		childTabGroup.setDisabledTab("operacional", status);
		childTabGroup.setDisabledTab("financeiro", status);
	}

	function tpClienteOnChange() {
		var value = getElementValue("tpClienteSolicitado");
		if(executarAlteracoesTpCliente()){

			var tabGroup = getTabGroup(document);
			var childTabGroup = tabGroup.getTab("cad").childTabGroup;

			if (childTabGroup.selectedTab.properties.id == "financeiro") {
				selectTabIdentificacao();
			}
		}

		changeSubAbasFieldStatus(getModoTela());
		tpPessoaOnChangeIdentificacao();
		findDadosSessao("ajustaDadosDefaultEdicao");
		return true;
	}

	function disableSubAbas() {
		changeChildTabsStatus(true);
		telaIdentificacao.disableAll();
		telaComercial.disableAll();
		telaOperacional.disableAll();
		telaFinanceiro.disableAll();
	}

	function cleanSubAbas() {
		telaIdentificacao.clean();
		telaComercial.clean();
		telaOperacional.clean();
		telaFinanceiro.clean();
	}

	function findDadosSessao(callback) {
		if (callback == undefined) {
			callback = "findDadosSessao";
		}
		var service = "lms.vendas.manterClienteAction.findDadosSessao";
		var sdo = createServiceDataObject(service, callback);
		xmit({serviceDataObjects:[sdo]});
	}

	function findDadosSessao_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data != undefined) {
			setElementValue("pessoa.tpPessoa", "J");
			setElementValue("tpCliente", tpCliente.POTENCIAL);
			setElementValue("tpClienteSolicitado", tpCliente.POTENCIAL);
			setElementValue("pessoa.tpIdentificacao", data.pessoa.tpIdentificacao);
			setFocus("pessoa.nrIdentificacao");

			telaIdentificacao.ajustaDadosDefault(data);
			telaComercial.ajustaDadosDefault(data);
			telaOperacional.ajustaDadosDefault(data);
			telaFinanceiro.ajustaDadosDefault(data);
			tpClienteOnChange();
		}
	}

	function ajustaDadosDefaultEdicao_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data != undefined) {
			if(executarAlteracoesTpCliente()){
				telaIdentificacao.ajustaDadosDefaultEdicao(getTpClienteRegras(), data);
				telaFinanceiro.ajustaDadosDefaultEdicao(getTpClienteRegras(), data);
			}
		}
	}

	function myOnDataLoad_cb(data, errorMessage, errorMsg, eventObj) {
		if (errorMessage != undefined) {
			alert(errorMessage);
			return false;
		}
		onDataLoad_cb(data, errorMessage, errorMsg, eventObj);
		setElementValue("pessoa.nmPessoa", data.pessoa.nmPessoa);
		setElementValue("pessoa.nrIdentificacao", data.pessoa.nrIdentificacao);
		setElementValue("pessoa.idPessoa", data.pessoa.idPessoa);
		onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), numberElement:document.getElementById("pessoa.nrIdentificacao")});
		setModoTela(modo.EDICAO);
		
		if (data.blClientePostoAvancado != undefined){
			setElementValue("blClientePostoAvancado",data.blClientePostoAvancado);
		}
		
		
		if (data.informacaoDoctoClienteConsolidado != undefined && data.informacaoDoctoClienteConsolidado.idInformacaoDoctoCliente != undefined){
			setElementValue("informacaoDoctoClienteConsolidado.idInformacaoDoctoCliente", data.informacaoDoctoClienteConsolidado.idInformacaoDoctoCliente);
		}
		
		if (data.blLiberaEtiquetaEdiLinehaul != undefined){
			setElementValue("blLiberaEtiquetaEdiLinehaul",data.blLiberaEtiquetaEdiLinehaul);
		}
		
		if (data.blProdutoPerigoso != undefined){
			setElementValue("blProdutoPerigoso",data.blProdutoPerigoso);
		}
		if (data.blControladoPoliciaCivil != undefined){
			setElementValue("blControladoPoliciaCivil",data.blControladoPoliciaCivil);
		}
		if (data.blControladoPoliciaFederal != undefined){
			setElementValue("blControladoPoliciaFederal",data.blControladoPoliciaFederal);
		}
		if (data.blControladoExercito != undefined){
			setElementValue("blControladoExercito",data.blControladoExercito);
		}
		
		if (data.blNaoPermiteSubcontratacao != undefined){
			setElementValue("blNaoPermiteSubcontratacao", data.blNaoPermiteSubcontratacao);
		}
		
		if (data.blEnviaDocsFaturamentoNas != undefined){
			setElementValue("blEnviaDocsFaturamentoNas", data.blEnviaDocsFaturamentoNas);
		}
		
		if (data.pessoa.nrIdentificacao != undefined &&
			data.pessoa.tpIdentificacao != undefined &&
			data.pessoa.tpPessoa != undefined) {
			setDisabled("pessoa.tpPessoa", true);
			setDisabled("pessoa.tpIdentificacao", true);
			setDisabled("pessoa.nrIdentificacao", true);
		} else {
			setDisabled("pessoa.tpPessoa", false);
			setDisabled("pessoa.nrIdentificacao", false);
		}
		setElementValue("tpClienteOriginal", data.tpCliente.value);

		if(data.blValidaCobrancDifTdeDest != undefined){
			setElementValue("blValidaCobrancDifTdeDest", data.blValidaCobrancDifTdeDest);
		}

		if(data.blCobrancaTdeDiferenciada != undefined){
			setElementValue("blCobrancaTdeDiferenciada", data.blCobrancaTdeDiferenciada);
		}

		changeSubAbasFieldStatus(getModoTela());
		telaIdentificacao.loadData(data);
		telaComercial.loadData(data);
		telaOperacional.loadData(data);
		telaFinanceiro.loadData(data);

		telaIdentificacao.tpPessoaOnChange(getElementValue("pessoa.tpPessoa"));

		validateUsuariosResponsaveisByCliente();

		controlarCamposWK();
	}

	function controlarCamposWK(){
		if (getElementValue("tpClienteSolicitado") == "") {
			setElementValue("tpClienteSolicitado", getElementValue("tpCliente"));
		}
		
		if(telaIdentificacao.getTpSituacao() === "N" && getTpCliente() == tpCliente.ESPECIAL && telaFinanceiro.getTpSituacaoAnaliseCreditoCliente() == "C"  && getElementValue("temPendenciaWKTpCliente") == "false" ){
			setDisabled("tpClienteSolicitado", false);
		}else if(getElementValue("temPendenciaWKTpCliente") == "true" || (telaIdentificacao.getTpSituacao() === "N" && getModoTela() == modo.EDICAO) ){
			setDisabled("tpClienteSolicitado", true);
		} 
	}

	function changeSubAbasFieldStatus(mode) {
		var value = getTpClienteRegras();
		if (mode == modo.INCLUSAO) {
			if (value == "") {
				selectTabIdentificacao();
				disableSubAbas();
				return;
			}
		}

		if(executarAlteracoesTpCliente()){
			telaIdentificacao.enableFields(mode, value);
			telaComercial.enableFields(mode, value);
			telaOperacional.enableFields(mode, value);
			telaFinanceiro.enableFields(mode, value);
		}
	}

	function executarAlteracoesTpCliente(){
		var tpClienteOriginal = getTpCliente();
		var tpClienteSolicitado = getTpClienteSolicitado();

		if((tpClienteOriginal == tpCliente.ESPECIAL && (tpClienteSolicitado == tpCliente.EVENTUAL || tpClienteSolicitado == tpCliente.POTENCIAL))
			|| (tpClienteOriginal == tpCliente.FILIAL && (tpClienteSolicitado == tpCliente.EVENTUAL || tpClienteSolicitado == tpCliente.POTENCIAL))
			|| (tpClienteOriginal == tpCliente.FILIAL && tpClienteSolicitado == tpCliente.ESPECIAL)){

			return false;
		}
		return true;
	}

	function changeConcluirStatus(status) {
		setDisabled("btnConcluirCadastro", status);
	}

	function tpPessoaOnChange() {
		tpPessoaOnChangeIdentificacao();
		return changeTypePessoaWidget({
			tpTipoElement:getElement("pessoa.tpPessoa"),
			tpIdentificacaoElement:getElement('pessoa.tpIdentificacao'),
			numberElement:getElement('pessoa.idPessoa'),
			tabCmd:'cad'
		});
	}

	function tpPessoaOnChangeIdentificacao() {
		var value = getElementValue("pessoa.tpPessoa");
		if (telaIdentificacao != undefined) {
			telaIdentificacao.tpPessoaOnChange(value);
		}
	}

	function getModoTela() {
		return _modoTela;
	}

	function setModoTela(mode) {
		_modoTela = mode;
	}

	function getTpCliente() {
		return getElementValue("tpCliente");
	}

	function getTpClienteSolicitado() {
		return getElementValue("tpClienteSolicitado");
	}

	function getTpClienteRegras() {
		if(getElementValue("temPendenciaWKTpCliente") === "true"){
			return getTpCliente();
		} else if(executarAlteracoesTpCliente()){
			return getTpClienteSolicitado();
		} else {
			return getTpCliente();
		}
	}

	/**Valida os dados do cliente*/
	function validateManterCliente(){

		telaIdentificacao.changeCamposObrigatorios();
		telaComercial.changeCamposObrigatorios();
		telaOperacional.changeCamposObrigatorios();
		telaFinanceiro.changeCamposObrigatorios();

		if(!validateCliente()) {
			return false;
		}
		return true;

	}

	/**Obtem os dados da tela do cliente*/
	function getDataManterCliente(){

		var dataCad = buildFormBeanFromForm(getElement("formCad"));
		var dataIdentificacao = buildFormBeanFromForm(getElement("formIdentificacao", telaIdentificacao.document));
		var dataComercial = buildFormBeanFromForm(getElement("formComercial", telaComercial.document));
		var dataOperacional = buildFormBeanFromForm(getElement("formOperacional", telaOperacional.document));
		var dataFinanceiro = buildFormBeanFromForm(getElement("formFinanceiro", telaFinanceiro.document));

        if(!dataIdentificacao.pessoa.enderecoPessoa.idEnderecoPessoa) {
            delete dataIdentificacao.pessoa.enderecoPessoa;
        }

		var data = new Array();
		merge(data, dataComercial);
		merge(data, dataOperacional);
		merge(data, dataFinanceiro);
		merge(data, dataIdentificacao);
		mergeDataCad(data, dataCad);
		carregarParemetrosBlEnviaDocsFaturamentoNas(data, dataCad);

		return data;
	}

	/** Salva ou conclui o cadastro do cliente */
	function saveCliente(actionConclusion){
		var saveCallback = "saveCliente";
		if(actionConclusion){
			saveCallback = "concCliente";
		}

		if(validateManterCliente()){
			if(executeValidacaoCliente()){
				var data = getDataManterCliente();
				var sdo  = createServiceDataObject("lms.vendas.manterClienteAction.validaCliente", saveCallback, data);
				xmit({serviceDataObjects:[sdo]});
			}else{
				if(actionConclusion){
					save('lms.vendas.manterClienteAction.storeConcluirCadastro');
				}else{
					save();
				}
			}
		}
	}

	/**Salva o cliente*/
	function saveCliente_cb(data,error){
		if (error != undefined) {
			alert(error);
			return false;
		}
		save();
	}

	/**Conclui o cadastro do cliente*/
	function concCliente_cb(data,error){
		if (error != undefined) {
			alert(error);
			return false;
		}
		save('lms.vendas.manterClienteAction.storeConcluirCadastro');
	}

	/**Executa a action lms.vendas.manterClienteAction.store do LMS salvando os dados do cliente*/
	function save(service) {

		if (isClienteNovo()) {
			setElementValue("tpCliente", getElementValue("tpClienteSolicitado"));
		}
		mostrouPopupMotivo = false;

		var tpClientePopup = confirmaAlteracaoTpCliente();

		if (tpClientePopup == true || telaComercial.exibirPopupWKFilial() || telaOperacional.exibirPopupWKFilial() || telaFinanceiro.exibirPopupWKFilial()) {
			openPopupMotivoSolicitacao();
		} else if (tpClientePopup == false) {
			return false;
		}

		if ((mostrouPopupMotivo && isMotivoSetado()) || !mostrouPopupMotivo) {
			if (service == undefined) {
				service = "lms.vendas.manterClienteAction.store";
			}
			if(validateManterCliente()){
				var data = getDataManterCliente();
				var sdo = createServiceDataObject(service, "afterStore", data);
				xmit({serviceDataObjects:[sdo]});
			}
		}
	}


	/**Valida se é necessário executar lms.vendas.manterClienteAction.validaCliente*/
	function executeValidacaoCliente() {

		if (getModoTela() == modo.EDICAO) {
			var tipo = getElementValue("tpCliente");
			var tipoOriginal = getElementValue("tpClienteOriginal");

			if (tipoOriginal == tpCliente.ESPECIAL && (tipo == tpCliente.EVENTUAL || tipo == tpCliente.POTENCIAL)) {
				return true
			}

			if (tipoOriginal == tpCliente.FILIAL && (tipo == tpCliente.EVENTUAL || tipo == tpCliente.POTENCIAL)) {
				return true
			}

			if (tipoOriginal == tpCliente.FILIAL && tipo == tpCliente.ESPECIAL) {
				return true
			}
		}
		return false;
	}

	function validateCliente() {
		if(!validateForm(getElement("formCad"))) {
			return false;
		}
		if(!validateForm(getElement("formIdentificacao", telaIdentificacao.document))) {
			return false;
		}
		if(!validateForm(getElement("formComercial", telaComercial.document))) {
			return false;
		}
		if(!validateForm(getElement("formOperacional", telaOperacional.document))) {
			return false;
		}
		if(!validateForm(getElement("formFinanceiro", telaFinanceiro.document))) {
			return false;
		}
		var blEmiteBoletoCliDestino = getElementValue(getElement("blEmiteBoletoCliDestino", telaFinanceiro.document));
		var blGeraReciboFreteEntrega = getElementValue(getElement("blGeraReciboFreteEntrega", telaFinanceiro.document));
		
		if(blEmiteBoletoCliDestino && blGeraReciboFreteEntrega) {
			alertI18nMessage("LMS-01140");
			return false;
		}
		validateLiberaEmbarqueEtiquetaVolume();
		validateEmissaoDiaNaoUtil();
		return true;
	}
	
	function validateLiberaEmbarqueEtiquetaVolume() {
		var blLiberaEtiquetaEdi = getElementValue(getElement("blLiberaEtiquetaEdi", telaOperacional.document));
		var blEtiquetaPorVolume = getElementValue(getElement("blEtiquetaPorVolume", telaOperacional.document));
		
		if(blLiberaEtiquetaEdi && blEtiquetaPorVolume) {
			telaOperacional.reloadEtiquetaPorVolume();
			alertI18nMessage("LMS-01336");
		}
	}

	function validateEmissaoDiaNaoUtil() {
		var blEmissaoDiaNaoUtil = getElementValue(getElement("blEmissaoDiaNaoUtil", telaOperacional.document));
		var blEmissaoSabado = getElementValue(getElement("blEmissaoSabado", telaOperacional.document));
		
		if(blEmissaoDiaNaoUtil && blEmissaoSabado) {
			telaOperacional.reloadBlEmissaoSabado();
			alertI18nMessage("LMS-01343");
		}
	}
	
	function afterStore_cb(data, error) {
		if (error != undefined) {
			alert(error);
			if (data._exception._key == "LMS-01201") {
				selectTabFinanceiro();
				setFocus(telaFinanceiro.document.getElementById("divisaoClienteResponsavel.idDivisaoCliente"));
			}
			return false;
		}

		store_cb(data, error);
		displayWarnings(data);

		telaComercial.loadData(data);
		telaOperacional.loadData(data);
		telaFinanceiro.loadData(data);
		telaIdentificacao.afterStore(data);
		telaIdentificacao.tpPessoaOnChange(getElementValue("pessoa.tpPessoa"));

		setModoTela(modo.EDICAO);
		changeSubAbasFieldStatus(getModoTela());
		setDisabled("pessoa.tpPessoa", true);
		setDisabled("pessoa.tpIdentificacao", true);
		setDisabled("pessoa.nrIdentificacao", true);
		setElementValue("pessoa.idPessoa", data.idCliente);
		setElementValue("usuarioByIdUsuarioInclusao.idUsuario", data.usuarioByIdUsuarioInclusao.idUsuario);
		setElementValue("usuarioByIdUsuarioAlteracao.idUsuario", data.usuarioByIdUsuarioAlteracao.idUsuario);
		setElementValue("tpClienteOriginal", getElementValue("tpCliente"));
		controlarCamposWK();

		telaComercial.enableFields(modo.EDICAO, getTpClienteRegras());
		telaOperacional.enableFields(modo.EDICAO, getTpClienteRegras());
		telaFinanceiro.enableFields(modo.EDICAO, getTpClienteRegras());
	}

	function displayWarnings(data) {
		if(data) {
			if(data.warnings && data.warnings.length > 0) {
				for(var i = 0; i < data.warnings.length; i++) {
					alert(data.warnings[i].warning);
				}
			}
		}
	}

	function changePage(url) {
		var qs = "&labelPessoaTemp=" + i18NLabel.getLabel("cliente");
		qs += "&pessoa.idPessoa=" + document.getElementById("idCliente").value;
		qs += "&pessoa.nmPessoa=" + escape(document.getElementById("pessoa.nmPessoa").value);
		qs += "&pessoa.nrIdentificacao=" + document.getElementById("pessoa.nrIdentificacao").value;

		parent.parent.redirectPage(url+qs);
	}

	function changePageCliente(url) {
		var qs = "&labelPessoaTemp=" + i18NLabel.getLabel("cliente");
		qs += "&pessoa.idPessoa=" + document.getElementById("idCliente").value;
		qs += "&cliente.idCliente=" + document.getElementById("idCliente").value;
		qs += "&cliente.pessoa.nmPessoa=" + escape(document.getElementById("pessoa.nmPessoa").value);
		qs += "&cliente.pessoa.nrIdentificacao=" + document.getElementById("pessoa.nrIdentificacao").value;

		parent.parent.redirectPage(url+qs);
	}

	function confirmaAlteracaoTpCliente() {
		if (getModoTela() == modo.EDICAO) {

			if (getElementValue("temPendenciaWKTpCliente") == "true") {
				return;
			}

			var tipo = getElementValue("tpClienteSolicitado");
			var tipoOriginal = getElementValue("tpCliente");
			if (tipoOriginal == tpCliente.ESPECIAL && (tipo == tpCliente.EVENTUAL || tipo == tpCliente.POTENCIAL)) {
				return confirmI18nMessage("LMS-01142");
			}

			if (tipoOriginal == tpCliente.FILIAL && (tipo == tpCliente.EVENTUAL || tipo == tpCliente.POTENCIAL)) {
				return confirmI18nMessage("LMS-01146");
			}

			if (tipoOriginal == tpCliente.FILIAL && tipo == tpCliente.ESPECIAL) {
				return confirmI18nMessage("LMS-01157");
			}
		}
	}

	function carregarParemetrosBlEnviaDocsFaturamentoNas(data, dataCad) {
		data.blEnviaDocsFaturamentoNas = (dataCad.blEnviaDocsFaturamentoNas === "true") && !data.blEnviaDacteXmlFat;
		setElementValue("blEnviaDocsFaturamentoNas", data.blEnviaDocsFaturamentoNas);
	}

	function mergeDataCad(data, dataCad) {
		data.idCliente = dataCad.idCliente;
		data.tpClienteOriginal = dataCad.tpClienteOriginal;
		data.tpCliente = dataCad.tpCliente;
		data.tpClienteSolicitado = dataCad.tpClienteSolicitado;
		data.dsMotivoSolicitacao = dataCad.dsMotivoSolicitacao;

		data.pessoa.tpPessoa = dataCad.pessoa.tpPessoa;
		data.pessoa.tpIdentificacao = dataCad.pessoa.tpIdentificacao;
		data.pessoa.idPessoa = dataCad.pessoa.idPessoa;
		data.pessoa.nrIdentificacao = dataCad.pessoa.nrIdentificacao;
		data.pessoa.nmPessoa = dataCad.pessoa.nmPessoa;
		data.pessoa.dhInclusao = dataCad.pessoa.dhInclusao;
		
		data.blClientePostoAvancado = dataCad.blClientePostoAvancado != undefined && dataCad.blClientePostoAvancado == "true";
		data.informacaoDoctoClienteConsolidado = dataCad.informacaoDoctoClienteConsolidado; 
		data.blLiberaEtiquetaEdiLinehaul = dataCad.blLiberaEtiquetaEdiLinehaul != undefined && dataCad.blLiberaEtiquetaEdiLinehaul == "true";
		
		data.blProdutoPerigoso = dataCad.blProdutoPerigoso != undefined && dataCad.blProdutoPerigoso == "true";
		data.blControladoPoliciaCivil = dataCad.blControladoPoliciaCivil != undefined && dataCad.blControladoPoliciaCivil == "true";
		data.blControladoPoliciaFederal = dataCad.blControladoPoliciaFederal != undefined && dataCad.blControladoPoliciaFederal === "true";
		data.blControladoExercito = dataCad.blControladoExercito != undefined && dataCad.blControladoExercito == "true";
		
		data.blNaoPermiteSubcontratacao = dataCad.blNaoPermiteSubcontratacao != undefined && dataCad.blNaoPermiteSubcontratacao == "true";
		data.blValidaCobrancDifTdeDest= dataCad.blValidaCobrancDifTdeDest;
		data.blCobrancaTdeDiferenciada = dataCad.blCobrancaTdeDiferenciada;

		data.usuarioByIdUsuarioAlteracao = {
			idUsuario : dataCad.usuarioByIdUsuarioAlteracao.idUsuario
		};
		data.usuarioByIdUsuarioInclusao = {
			idUsuario : dataCad.usuarioByIdUsuarioInclusao.idUsuario
		};
	}

	function selectTabIdentificacao() {
		var tabGroup = getTabGroup(document);
		var childTabGroup = tabGroup.getTab("cad").childTabGroup;
		childTabGroup.selectTab("identificacao");
	}

	function selectTabFinanceiro() {
		var tabGroup = getTabGroup(document);
		var childTabGroup = tabGroup.getTab("cad").childTabGroup;
		childTabGroup.selectTab("financeiro");
	}

	function openPopupMotivoSolicitacao() {
		mostrouPopupMotivo = true;
		setElementValue("dsMotivoSolicitacao", '');
		var url = '/vendas/manterDadosIdentificacao.do?cmd=motivoSolicitacao';
		var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:600px;dialogHeight:200px;';
		showModalDialog(url, window, wProperties);
	}

	function isClienteNovo() {
		if (_hasValue(getElementValue("idCliente"))) {
			return false;
		} return true;
	}

	function isMotivoSetado() {
		if (_hasValue(getElementValue("dsMotivoSolicitacao"))) {
			return true;
		} return false;
	}
	function _hasValue(x) {
		if (x != '' && x != null && x != undefined) {
			return true;
		} return false;
	}
</script>
