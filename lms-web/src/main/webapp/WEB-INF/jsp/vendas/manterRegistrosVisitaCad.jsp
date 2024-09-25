<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	//Carrega dados do usuario
	function loadDataObjectsCad() {	
		onPageLoad(); 
		var data = new Array();	
		var sdo = createServiceDataObject("lms.vendas.manterRegistrosVisitaAction.getBasicData", "dataSession", data);
		xmit({serviceDataObjects:[sdo]});
	}
	function myPageLoadCallBack_cb(data, error){
		var url = new URL(parent.location.href);
		if (url.parameters != undefined 
				&& url.parameters.idProcessoWorkflow != undefined 
				&& url.parameters.idProcessoWorkflow != '') {   
			onDataLoad(url.parameters.idProcessoWorkflow);		
		}
		onPageLoad_cb(data, error);
	}
</script>
<adsm:window service="lms.vendas.manterRegistrosVisitaAction" onPageLoad="loadDataObjectsCad" onPageLoadCallBack="myPageLoadCallBack">
	<adsm:form
		action="/vendas/manterRegistrosVisita" idProperty="idVisita" 
		service="lms.vendas.manterRegistrosVisitaAction.findByIdCustom"
		newService="lms.vendas.manterRegistrosVisitaAction.newMaster" onDataLoadCallBack="visitaDataLoad">

		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:hidden property="pendencia.idPendencia" serializable="false"/>
		<adsm:hidden property="filial.idFilial"/>
		<adsm:hidden property="isAprovador"/>
		<adsm:hidden property="versao"/>

		<adsm:textbox
			label="filial"
			dataType="text"
			property="filial.sgFilial"
			size="3"
			maxLength="3"
			required="true"
			disabled="true"
			serializable="false"
			labelWidth="14%"
			width="40%"
		>
			<adsm:textbox
				dataType="text"
				property="filial.pessoa.nmFantasia"
				size="25"
				maxLength="60"
				disabled="true"
				serializable="false"
			/>
		</adsm:textbox>

		<adsm:hidden property="idRegional" serializable="false"/>
		<adsm:textbox
			dataType="text"
			label="regional"
			property="siglaDescricao"
			disabled="true"
			size="32"
			maxLength="60"
			required="true"
			width="32%"
			labelWidth="14%"
			serializable="false"/>

		<adsm:lookup
			action="/configuracoes/consultarFuncionariosView"
			service="lms.vendas.manterRegistrosVisitaAction.findLookupFuncionario"
			dataType="text"
			property="usuarioByIdUsuario"
			idProperty="idUsuario"
			criteriaProperty="nrMatricula"
			criteriaSerializable="true"
			label="funcionario"
			size="17"
			maxLength="10"
			disabled="true"
			exactMatch="true"
			required="true"
			width="60%"
			labelWidth="14%"
			onDataLoadCallBack="funcCallBack"
			onPopupSetValue="funcCallBack">
			<adsm:propertyMapping
				relatedProperty="usuarioByIdUsuario.nmUsuario" 
				modelProperty="nmUsuario"/>
			<adsm:propertyMapping
				relatedProperty="usuarioByIdUsuario.vfuncionario.dsFuncao" 
				modelProperty="dsFuncao"/>
			<adsm:textbox
				dataType="text" 
				property="usuarioByIdUsuario.nmUsuario" 
				size="35" 
				maxLength="50" 
				disabled="true" 
				serializable="true"/>
		</adsm:lookup>
		
		<adsm:textbox
			dataType="text" 
			label="cargo"
			property="usuarioByIdUsuario.vfuncionario.dsFuncao" 
			maxLength="30" 
			size="32"
			required="true"
			disabled="true"
			labelWidth="14%"
			serializable="false"/>

		<adsm:listbox
			optionProperty="idFuncionarioVisita" 
			property="funcionarioVisitas"
			size="5" 
			boxWidth="450"
			width="100%"
			label="acompanhantesBRcargo" 
			labelStyle="vertical-align: text-top; margin-top: 4px; padding-top:2px"
			labelWidth="14%">
			<adsm:lookup action="/configuracoes/consultarFuncionariosView"
				service="lms.vendas.manterRegistrosVisitaAction.findLookupFuncionario" 
				dataType="text" 
				property="funcionarioVisita"
				idProperty="idUsuario" 
				criteriaProperty="nrMatricula"
				popupLabel="pesquisarAcompanhantes"
				size="20"
				maxLength="20"
				exactMatch="true"
				required="true">

				<adsm:propertyMapping relatedProperty="funcionarioVisitas_funcionarioVisita.vfuncionario.dsFuncao" 
					modelProperty="dsFuncao"/>
					<adsm:propertyMapping relatedProperty="funcionarioVisitas_funcionarioVisita.nmUsuario" 
						modelProperty="nmUsuario"/>
						<adsm:textbox
							dataType="text"
							property="funcionarioVisita.nmUsuario" 
							maxLength="20"
							disabled="true"
							size="20"/>
						<adsm:textbox dataType="text"
							property="funcionarioVisita.vfuncionario.dsFuncao" 
							maxLength="20"
							disabled="true" 
							size="20"/>
			</adsm:lookup>			
		</adsm:listbox>
		
		<adsm:lookup
			action="/vendas/manterDadosIdentificacao" 
			service="lms.vendas.manterRegistrosVisitaAction.findLookupCliente"
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="cliente" 
			size="17" 
			maxLength="18"
			required="true"
			dataType="text"
			width="40%"
			labelWidth="14%"
			onPopupSetValue="clientePopUp">
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping
				relatedProperty="cliente.tpCliente" 
				modelProperty="tpCliente"/>
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.enderecoPessoa.dsEndereco" 
				modelProperty="pessoa.enderecoPessoa.dsEndereco"/>
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.enderecoPessoa.nrEndereco" 
				modelProperty="pessoa.enderecoPessoa.nrEndereco"/>
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.enderecoPessoa.dsComplemento" 
				modelProperty="pessoa.enderecoPessoa.dsComplemento"/>
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.enderecoPessoa.municipio.nmMunicipio" 
				modelProperty="pessoa.enderecoPessoa.municipio.nmMunicipio"/>
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa" 
				modelProperty="pessoa.enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa"/>
			<adsm:textbox
				dataType="text" 
				property="cliente.pessoa.nmPessoa" 
				size="28" 
				maxLength="50"
				disabled="true" />
		</adsm:lookup>

		<adsm:combobox
			property="cliente.tpCliente" 
			label="tipoCliente"
			domain="DM_TIPO_CLIENTE"
			onlyActiveValues="true"
			disabled="true"
			labelWidth="14%"
			width="31%"
			serializable="false"/>

		<adsm:textbox
			dataType="text" 
			property="cliente.pessoa.enderecoPessoa.dsEndereco" 
			label="endereco" 
			maxLength="30" 
			size="33" 
			disabled="true" 
			labelWidth="14%" 
			width="40%"
			serializable="false"/>

		<adsm:textbox
			dataType="text" 
			property="cliente.pessoa.enderecoPessoa.nrEndereco" 
			label="numero" 
			maxLength="10" 
			size="13" 
			disabled="true" 
			labelWidth="14%" 
			width="31%"
			serializable="false"/>

		<adsm:textbox
			dataType="text" 
			property="cliente.pessoa.enderecoPessoa.dsComplemento" 
			label="complemento" 
			maxLength="30" 
			size="30"
			disabled="true" 
			labelWidth="14%" 
			width="40%"
			serializable="false"/>

		<adsm:textbox
			label="municipio" 
			property="cliente.pessoa.enderecoPessoa.municipio.nmMunicipio" 
			dataType="text" 
			size="30" 
			disabled="true" 
			width="40%"
			labelWidth="14%"
			serializable="false"/>

		<adsm:textbox
			label="uf" 
			property="cliente.pessoa.enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa" 
			dataType="text" 
			size="3" 
			disabled="true" 
			width="20%"
			labelWidth="14%"
			serializable="false"/>

		<adsm:textbox
			dataType="JTDate" 
			property="dtVisita" 
			label="dataVisita" 
			required="true" 
			labelWidth="14%" 
			width="40%"/>

		<adsm:textbox
			dataType="JTDate" 
			property="dtRegistro" 
			label="dataGeracao" 
			disabled="true" 
			required="true" 
			labelWidth="14%" 
			width="31%"/>

		<adsm:range
			label="periodo"
			labelWidth="14%"
			width="32%">

				<adsm:textbox
					dataType="JTTime" 
					property="hrInicial" 
					label="horarioInicial" 
					required="true"
					labelWidth="14%" 
					width="37%"/>

				<adsm:textbox
					dataType="JTTime" 
					property="hrFinal" 
					label="horarioFinal"
					labelWidth="14%"
					width="31%"/>

		</adsm:range>

		<adsm:textarea
			columns="112" 
			rows="5" 
			maxLength="500" 
			property="dsVisita" 
			label="descricao" 
			width="95%" 
			labelWidth="14%"/>


		<adsm:buttonBar>
			<adsm:button id="btnClientes" caption="manterClientes" onclick="return onClickManterClientes();" disabled="false" />
			<adsm:storeButton id="storeButton" callbackProperty="afterStore"/>
			<adsm:newButton id="resetButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
 
</adsm:window>
<script type="text/javascript">
	
	/*DADOS BÁSICOS DA TELA*/
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	var idRegional = null;
	var siglaRegional = null;	

	var dtAtual = null;

	var idUsuario = null;
	var nrMatricula = null;
	var nmUsuario = null;
	var dsFuncaoUsuario = null;

	var idUsuarioVisto = null;
	var nmUsuarioVisto = null;
	var isGerente = null;
	var changeFilial = null;	

	// Copia dados para da sessao recebidos para variáveis globais da tela
	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data, "filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data, "filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia");

		idRegional = getNestedBeanPropertyValue(data, "regional.idRegional");
		siglaRegional = getNestedBeanPropertyValue(data, "regional.siglaDescricao");

		idUsuario = getNestedBeanPropertyValue(data, "usuario.idUsuario"); //usuario logado
		nmUsuario = getNestedBeanPropertyValue(data, "usuario.nmUsuario");
		nrMatricula = getNestedBeanPropertyValue(data, "usuario.nrMatricula");
		dsFuncaoUsuario = getNestedBeanPropertyValue(data, "usuario.dsFuncao");		
		dtAtual = getNestedBeanPropertyValue(data, "dtAtual");

		//gerente
		isGerente = getNestedBeanPropertyValue(data, "usuarioByIdUsuarioVisto.isGerente");
		changeFilial = getNestedBeanPropertyValue(data, "usuarioByIdUsuarioVisto.changeFilial");
	}

	function verifyWorkflow() {
		/** Caso exista idProcessoWorkflow, desabilita todo documento */
		var url = new URL(parent.location.href);
		if (url.parameters != undefined 
				&& url.parameters.idProcessoWorkflow != undefined 
				&& url.parameters.idProcessoWorkflow != '') {   
			setElementValue("idProcessoWorkflow", url.parameters.idProcessoWorkflow);		
		}
		if (hasValue(getElementValue("idProcessoWorkflow"))) {
			setDisabled(document, true);
			disableAllFields(true);
			setDisabled("storeButton", true); 
			setDisabled("removeButton", true);
			setDisabled("resetButton", true);
			return true;
		}
		return false;
	}

	function initWindow(eventObj) {
		/** Caso exista idProcessoWorkflow, desabilita todo documento */
		if (verifyWorkflow()) {
			return;
		}

		if(isGerente == 'true') {
			//é gerente
			disableAllFields(false);
			configuraEtapas(false);
			writeDataSession();

			//pode trocar a filial
			setDisabled("filial.idFilial", (changeFilial != 'true')); 

			//todo gerente pode trocar o funcionario
			setDisabled("usuarioByIdUsuario.idUsuario", false); 

			setDisabled("storeButton", false);
		} else {
			//se for um click na aba
			if(eventObj.name == "tab_click"){
				//é uma inserção de visita
				if(getElementValue("idVisita") == "" || getElementValue("idVisita") == null){
					disableAllFields(false); //abilita os campos editáveis
					setDisabled("storeButton", false); 
					setDisabled("removeButton", true);
					setDisabled("resetButton", false);
					configuraEtapas(false);

					writeDataSession();
				} else {
					disableAllFields(false); //abilita os campos editáveis
					setDisabled("storeButton", false); 
					setDisabled("removeButton", false);
					setDisabled("resetButton", false);
					configuraEtapas(false);
				}
			} else if (eventObj.name == "storeButton"){
				setDisabled("removeButton", false);
			} else{
				//evento diferente de tab_click e storeButton (novo, clean, excluir)
				writeDataSession();
				disableAllFields(false);
				configuraEtapas(false);
				setDisabled("removeButton", true);
			}
		}

	}

	/**
	 * Carrega os dados basicos da tela
	 */
	function writeDataSession() {
		var elUsuario = getElementValue("usuarioByIdUsuario.nmUsuario");
		if(elUsuario == null || elUsuario == '') {
			setElementValue("usuarioByIdUsuario.idUsuario", idUsuario);
			setElementValue("usuarioByIdUsuario.nrMatricula", nrMatricula);
			setElementValue("usuarioByIdUsuario.nmUsuario", nmUsuario);
			setElementValue("usuarioByIdUsuario.vfuncionario.dsFuncao", dsFuncaoUsuario);
		}

		elFilial = getElementValue("filial.idFilial");
		if(elFilial == null || elFilial == '') {
			setElementValue("filial.idFilial", idFilial);
			setElementValue("filial.sgFilial", sgFilial);
			setElementValue("filial.pessoa.nmFantasia", nmFilial);				
		}				
		setElementValue("idRegional", idRegional);
		setElementValue("siglaDescricao", siglaRegional);	
		setElementValue("dtRegistro", setFormat(document.getElementById("dtRegistro"),dtAtual));

		configuraEtapas(false);	
	}

	//copia valores para a aba etapas
	function configuraEtapas(disabled){
		if (hasValue(getElementValue("idProcessoWorkflow"))){
			return;
		}

		var tabGroup = getTabGroup(this.document);
		var tab = tabGroup.getTab("etapas");

		var etapa = tab.tabOwnerFrame.document;
		setElementValue(etapa.getElementById("idUsuario"), idUsuario);

		tabGroup.setDisabledTab("etapas", disabled);
	}	

	function visitaDataLoad_cb(data, error){
		onDataLoad_cb(data, error);
		setElementValue("cliente.pessoa.nrIdentificacao", getNestedBeanPropertyValue(data, "cliente.pessoa.nrIdentificacao" ));

		/* Se a proposta foi aberta a partir da tela de workflow. */
		if (hasValue(getElementValue("idProcessoWorkflow"))) {
			getTabGroup(document).getTab("pesq").setDisabled(true);
			showSuccessMessage();
		}
		/** Caso exista idProcessoWorkflow, desabilita todo documento */
		if (verifyWorkflow()) {
			return;
		}
		configuraEtapas(false);

			disableAllFields(false); //abilita os campos editáveis
			setDisabled("storeButton", false); 
			setDisabled("removeButton", false);
			setDisabled("resetButton", false);
		}

	// abilita/desabilita TODOS os campos editáveis da tela
	function disableAllFields(value){
		setDisabled("funcionarioVisitas", value);
		setDisabled("funcionarioVisitas_idFuncionarioVisita", value);
		setDisabled("funcionarioVisitas_funcionarioVisita.nrMatricula", value);
		setDisabled("funcionarioVisitas_funcionarioVisita.idUsuario", value);

		setDisabled("cliente.pessoa.nrIdentificacao", value);
		setDisabled("cliente.idCliente", value);

		setDisabled("dtVisita", value);
		setDisabled("hrFinal", value);
		setDisabled("hrInicial", value);
		setDisabled("dsVisita", value);	

		setDisabled("btnClientes", value);
	}

/***** SALVAR *****/
	function afterStore_cb(data, error) {
		store_cb(data,error);
		if(error == undefined){
			configuraEtapas(false);
		}
		document.getElementById("resetButton").focus;
	}

/***** FILIAL *****/
	function filialCallBack_cb(data){
		if(data[0] != undefined) {
			setElementValue("filial.idFilial", data[0].idFilial);
			setElementValue("filial.sgFilial", data[0].sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data[0].pessoa.nmFantasia);
		}
		return;
	}

/***** FUNCIONARIO *****/
	function funcCallBack_cb(data){
		if(!usuarioByIdUsuario_nrMatricula_exactMatch_cb(data))
			return;
		if(data[0] != undefined) {
			setElementValue("usuarioByIdUsuario.idUsuario", data[0].idUsuario);
			setElementValue("usuarioByIdUsuario.nmUsuario", data[0].nmFuncionario);
			setElementValue("usuarioByIdUsuario.vfuncionario.dsFuncao", data[0].dsFuncao);
		}
		return;
	}

	function funcCallBack(data){
		if(data != undefined) {
			setElementValue("usuarioByIdUsuario.idUsuario", data.idUsuario);
			setElementValue("usuarioByIdUsuario.nmUsuario", data.nmUsuario);
			setElementValue("usuarioByIdUsuario.vfuncionario.dsFuncao", data.dsFuncao);
		}
		return;
	}

/***** CLIENTE *****/
	function clientePopUp(data){
		// Se Pessoa cadastrada
		if (data != undefined){
			setElementValue("cliente.idCliente", data.idCliente);
			setElementValue("cliente.tpCliente", data.tpCliente);
			setElementValue("cliente.pessoa.nmPessoa", data.pessoa.nmPessoa);
			setElementValue("cliente.pessoa.nrIdentificacao", data.pessoa.nrIdentificacao);
			//setElementValue("cliente.pessoa.nrIdentificacaoFormatado", data.pessoa.nrIdentificacao);

			var dataEndereco = new Array();
			dataEndereco.idPessoa = data.idCliente;
			var sdo = createServiceDataObject("lms.vendas.manterRegistrosVisitaAction.findEnderecoCliente", "loadEnderecoCliente", dataEndereco);
			xmit({serviceDataObjects:[sdo]});
		} else {
			limpaCamposCliente();
		}
		return true;
	}	

	function loadEnderecoCliente_cb(data){
		if(data != undefined) {
			setElementValue("cliente.pessoa.enderecoPessoa.dsEndereco", data.dsEndereco);
			setElementValue("cliente.pessoa.enderecoPessoa.nrEndereco", data.nrEndereco);
			setElementValue("cliente.pessoa.enderecoPessoa.municipio.nmMunicipio", data.municipio.nmMunicipio);
			setElementValue("cliente.pessoa.enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa", data.municipio.unidadeFederativa.sgUnidadeFederativa);
		}
	}

	function clienteOnChange(obj){
		var retorno = cliente_pessoa_nrIdentificacaoOnChangeHandler();		
		return retorno;
	}

	function limpaCamposCliente(){
		setElementValue("cliente.tpCliente", "");
		setElementValue("cliente.pessoa.enderecoPessoa.dsEndereco", "");
		setElementValue("cliente.pessoa.enderecoPessoa.nrEndereco", "");
		setElementValue("cliente.pessoa.enderecoPessoa.municipio.nmMunicipio", "");
		setElementValue("cliente.pessoa.enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa", "");		
	}

	function onClickManterClientes() {
		var url = "vendas/manterDadosIdentificacao.do?cmd=main";
		var nrIdentificacao = getElementValue("cliente.pessoa.nrIdentificacao");
		if (nrIdentificacao != "") {
			url += "&pessoa.nrIdentificacao=" + nrIdentificacao;
		}

		var nmPessoa = getElementValue("cliente.pessoa.nmPessoa");
		if (nmPessoa != "") {
			url += "&pessoa.nmPessoa=" + nmPessoa;
		}
		parent.parent.redirectPage(url);
	}
</script>