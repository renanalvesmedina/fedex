<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.expedicao.cadastrarClientesAction"
	onPageLoad="myOnPageLoad"
	onPageLoadCallBack="myOnPageLoad">
	<script type="text/javascript">
		function myOnPageLoad(){
			onPageLoad();
			var u = new URL(parent.location.href);
			var origem = u.parameters["origem"];
			var objOrigem = document.getElementById("origem");
			setElementValue(objOrigem, origem);
			objOrigem.masterLink = "true";
			if(origem != "exp") {
				// se a tela não for chamada do módulo expedição
				getElement("contato.nmContato").required = "true";
				getElement("pessoa.tpIdentificacao").required = "true";
				getElement("pessoa.nrIdentificacao").required = "true";
				getElement("telefoneEndereco.nrDdd").required = "true";
				getElement("telefoneEndereco.nrTelefone").required = "true";
			}
		}
	</script>
	<adsm:form
		action="/expedicao/cadastrarClientes"
		idProperty="idCliente">

		<adsm:hidden
			property="pessoa.idPessoa"/>

		<adsm:hidden
			property="origem"/>
		
		<adsm:section
			caption="dadosDoCliente"
			width="98"/>

		<adsm:combobox
			definition="TIPO_PESSOA.cad"
			label="tipoPessoa"
			labelWidth="13%"
			width="42%"
			onchange="return changeTipoPessoa();"/>
			
		<adsm:complement
			label="identificacao"
			labelWidth="13%" width="32%">

			<adsm:combobox
				definition="TIPO_IDENTIFICACAO_PESSOA.cad"
				required="false"
				onchange="return changeIdentificationTypePessoaWidget({tpIdentificacaoElement:this, numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'cad'});" />

			<adsm:textbox
				definition="IDENTIFICACAO_PESSOA" 
				onchange="return pessoa_nrIdentificacao_onChange(this);"/>
		</adsm:complement>

		<adsm:textbox
			dataType="text"
			size="57"
			property="pessoa.nmPessoa" 
			label="nomeRazaoSocial"
			maxLength="50"
			required="true" 
			labelWidth="13%"
			width="42%"/>

		<adsm:textbox
			dataType="text"
			property="inscricaoEstadual.nrInscricaoEstadual"
			label="ie"
			labelWidth="13%"
			width="32%"
			maxLength="20"
			size="15"/>
			
		<adsm:combobox 
			property="ramoAtividade.idRamoAtividade" 
			onlyActiveValues="true" 
			optionLabelProperty="dsRamoAtividade" 
			optionProperty="idRamoAtividade" 
			service="lms.expedicao.cadastrarClientesAction.findRamosAtividades"
			label="ramoAtividade" 
			labelWidth="13%" 
			width="35%" 
			boxWidth="260"/>

		<adsm:lookup
			service="lms.expedicao.cadastrarClientesAction.findLookupPais" 
			action="/municipios/manterPaises"
			serializable="false"
			property="enderecoPessoa.municipio.unidadeFederativa.pais" 
			idProperty="idPais"
			criteriaProperty="nmPais"
			onchange="return changePais();"
			label="pais"
			minLengthForAutoPopUpSearch="3"
			onDataLoadCallBack="lookupPais"
			onPopupSetValue="popupPais"
			exactMatch="false"
			dataType="text"
			labelWidth="13%"
			width="42%"
			size="25"
			maxLength="30">
		</adsm:lookup>
		
		<adsm:lookup
			label="cep"
			property="nrCepLookup"
			dataType="text"
			idProperty="nrCep"
			labelWidth="13%"
			width="32%"
			criteriaProperty="cepCriteria"
			exactMatch="false"
			service="lms.expedicao.cadastrarClientesAction.findCepLookup"
			size="20"
			maxLength="8"
			serializable="false"
			allowInvalidCriteriaValue="true"
			action="/configuracoes/pesquisarCEP"
			onDataLoadCallBack="nrCepLookup"
			onPopupSetValue="popupNrCep"
			onchange="return cepChange();"
			onclickPicker="onclickPickerLookupCep()">

			<adsm:propertyMapping
				criteriaProperty="enderecoPessoa.municipio.unidadeFederativa.pais.idPais"
				modelProperty="municipio.unidadeFederativa.pais.idPais"
				
				addChangeListener="false"/>

			<adsm:propertyMapping
				criteriaProperty="enderecoPessoa.municipio.unidadeFederativa.pais.nmPais" 
				modelProperty="municipio.unidadeFederativa.pais.nmPais"
				addChangeListener="false"
				inlineQuery="false"/>

			<adsm:propertyMapping
				criteriaProperty="enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa"
				modelProperty="municipio.unidadeFederativa.idUnidadeFederativa"
				addChangeListener="false"/>

			<adsm:propertyMapping
				criteriaProperty="enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa" 
				modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa"
				addChangeListener="false"
				inlineQuery="false"/>

			<adsm:propertyMapping
				criteriaProperty="enderecoPessoa.municipio.unidadeFederativa.nmUnidadeFederativa"
				modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa"
				addChangeListener="false"
				inlineQuery="false"/>

			<adsm:propertyMapping
				criteriaProperty="enderecoPessoa.municipio.idMunicipio" 
				modelProperty="municipio.idMunicipio"
				addChangeListener="false"/>

			<adsm:propertyMapping
				criteriaProperty="enderecoPessoa.municipio.nmMunicipio"
				modelProperty="municipio.nmMunicipio"
				addChangeListener="false"
				inlineQuery="false"/>
				
			<adsm:propertyMapping
				relatedProperty="enderecoPessoa.nrCep"
				modelProperty="nrCep"/>

			<adsm:propertyMapping
				relatedProperty="enderecoPessoa.dsBairro"
				modelProperty="nmBairro"/>

			<adsm:propertyMapping
				relatedProperty="enderecoPessoa.dsEndereco"
				blankFill="false"
				modelProperty="nmLogradouro"/>

			<adsm:propertyMapping
				relatedProperty="enderecoPessoa.tipoLogradouro.dsTipoLogradouro"
				blankFill="false"
				modelProperty="dsTipoLogradouro"/>

		</adsm:lookup>

		<adsm:hidden
			property="enderecoPessoa.tipoLogradouro.dsTipoLogradouro"/>

		<adsm:complement
			label="endereco"
			width="87%"
			labelWidth="13%"
			required="true">

			<adsm:combobox
				property="enderecoPessoa.tipoLogradouro.idTipoLogradouro"
				width="15%"
				service="lms.expedicao.cadastrarClientesAction.findTipoLogradouro"
				optionLabelProperty="dsTipoLogradouro"
				optionProperty="idTipoLogradouro"
				onlyActiveValues="true">

				<adsm:propertyMapping
					relatedProperty="enderecoPessoa.tipoLogradouro.dsTipoLogradouro"
					modelProperty="dsTipoLogradouro"/>

			</adsm:combobox>

			<adsm:textbox
				property="enderecoPessoa.dsEndereco"
				dataType="text"
				size="82"
				maxLength="100"
				width="65%"/>

		</adsm:complement>

		<adsm:textbox
			dataType="integer"
			property="enderecoPessoa.nrEndereco"
			label="numero"
			required="true"
			labelWidth="13%"
			width="42%"
			size="5"
			maxLength="5"/>

		<adsm:textbox
			dataType="text"
			property="enderecoPessoa.dsComplemento"
			label="complemento"
			size="28"
			labelWidth="13%"
			width="32%"
			maxLength="60"/>

		<adsm:textbox
			dataType="text"
			property="enderecoPessoa.dsBairro" 
			label="bairro"
			maxLength="60"
			size="30"
			labelWidth="13%"
			width="42%"/>
		
		<adsm:hidden
			property="enderecoPessoa.nrCep"/>

		<adsm:hidden
			property="enderecoPessoa.idEnderecoPessoa"/>
		
		<adsm:hidden
			property="tpSituacaoAtivo"
			value="A"
			serializable="false"/>

		<adsm:lookup
			label="municipio"
			property="enderecoPessoa.municipio" 
			idProperty="idMunicipio" 
			dataType="text"
			criteriaProperty="nmMunicipio"
			service="lms.expedicao.cadastrarClientesAction.findMunicipioLookup"
			size="30"
			maxLength="60"
			onchange="return changeMunicipio();"
			onDataLoadCallBack="municipio"
			onPopupSetValue="popupMunicipio"
			required="true"
			labelWidth="13%"
			width="32%"
			action="/municipios/manterMunicipios"
			exactMatch="false"
			minLengthForAutoPopUpSearch="5">

			<adsm:propertyMapping
				criteriaProperty="enderecoPessoa.municipio.unidadeFederativa.pais.idPais"
				modelProperty="unidadeFederativa.pais.idPais"
				addChangeListener="false"/>

			<adsm:propertyMapping
				criteriaProperty="enderecoPessoa.municipio.unidadeFederativa.pais.nmPais"
				modelProperty="unidadeFederativa.pais.nmPais"
				inlineQuery="false"/>

			<adsm:propertyMapping
				criteriaProperty="enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa"
				modelProperty="unidadeFederativa.idUnidadeFederativa"
				disable="false"
				addChangeListener="false"/>

			<adsm:propertyMapping
				criteriaProperty="enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa"
				modelProperty="unidadeFederativa.sgUnidadeFederativa"
				addChangeListener="false"
				disable="false"
				inlineQuery="false"/>

			<adsm:propertyMapping
				criteriaProperty="enderecoPessoa.municipio.unidadeFederativa.nmUnidadeFederativa"
				modelProperty="unidadeFederativa.nmUnidadeFederativa"
				addChangeListener="false"
				inlineQuery="false"/>

			<adsm:propertyMapping
				modelProperty="tpSituacao"
				criteriaProperty="tpSituacaoAtivo"/>

			<adsm:propertyMapping
				relatedProperty="_idUf"
				modelProperty="unidadeFederativa.idUnidadeFederativa"/>

			<adsm:propertyMapping
				relatedProperty="enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa"
				modelProperty="unidadeFederativa.sgUnidadeFederativa"/>

			<adsm:propertyMapping
				relatedProperty="enderecoPessoa.municipio.unidadeFederativa.nmUnidadeFederativa"
				modelProperty="unidadeFederativa.nmUnidadeFederativa"/>
		</adsm:lookup>
		
		
		<adsm:hidden
			property="_idUf"
			serializable="false"/>

		<adsm:hidden
			property="enderecoPessoa.municipio.unidadeFederativa.nmUnidadeFederativa"/>

		<adsm:hidden
			property="enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa"/>

		<adsm:combobox 
			service="lms.expedicao.cadastrarClientesAction.findUnidadeFederativaByPais" 
			property="enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa" 
			optionLabelProperty="siglaDescricao"
			optionProperty="idUnidadeFederativa"
			required="true"
			label="uf"
			labelWidth="13%"
			width="87%"
			onchange="changeUf(this);"
			boxWidth="150"
			onDataLoadCallBack="ufOrigemOnDataLoad">

			<adsm:propertyMapping
				criteriaProperty="enderecoPessoa.municipio.unidadeFederativa.pais.idPais"
				modelProperty="pais.idPais"/>

			<adsm:propertyMapping
				relatedProperty="enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa"
				modelProperty="sgUnidadeFederativa"/>

			<adsm:propertyMapping
				relatedProperty="enderecoPessoa.municipio.unidadeFederativa.nmUnidadeFederativa"
				modelProperty="nmUnidadeFederativa"/>
		</adsm:combobox>
		
			
		<adsm:complement
			label="telefone"
			labelWidth="13%"
			width="42%">

			<adsm:textbox
				dataType="integer"
				property="telefoneEndereco.nrDdd"
				maxLength="5"
				size="5"/>

			<adsm:textbox
				dataType="integer"
				property="telefoneEndereco.nrTelefone"
				maxLength="10"
				size="10"/>

		</adsm:complement>

		<adsm:textbox
			dataType="text"
			property="contato.nmContato"
			label="contato"
			labelWidth="13%"
			width="32%"
			maxLength="40"
			size="38"/>

		<adsm:buttonBar>
			<adsm:storeButton
				callbackProperty="myStore"
				id="storeButton"
				caption="salvarCliente"/>

			<adsm:button
				caption="fechar"
				buttonType="closeButton"
				id="closeButton"
				disabled="false"
				onclick="closeme();"/>
		</adsm:buttonBar>
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-04016"/>
		</adsm:i18nLabels>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
	document.getElementById("enderecoPessoa.tipoLogradouro.idTipoLogradouro").required = "true";
	document.getElementById("enderecoPessoa.municipio.nmMunicipio").serializable = true;
	getElement("ramoAtividade.idRamoAtividade").required = "true";
	/************************************************************\
	*
	\************************************************************/
	function myStore_cb(dados, erros, errorMsg, eventObj) {
		if(!erros) {
			var tpIdentificacao = getElementValue("pessoa.tpIdentificacao");
			var nrIdentificacao = getElementValue("pessoa.nrIdentificacao");
			var nrIdentificacaoFormatado = getFormattedValue(tpIdentificacao,nrIdentificacao, "", true);

			setNestedBeanPropertyValue(dados,"nrIdentificacaoFormatado",nrIdentificacaoFormatado);
			handleSave(dados);
		} else {
			alert(erros);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function closeme() {
		window.close();
	}
	/************************************************************\
	*
	\************************************************************/
	// Handle click of Save button
	function handleSave(data) {
		if(getElementValue("origem") != "col" && getElementValue("origem") != "mda") {
			window.returnValue = data;
			closeme();
		} else {
			// Rotina própria para a tela de Pedido de Coleta e Abrir MDA
			var windowPai = window.dialogArguments.window.dialogArguments.window;
			var doc = window.dialogArguments.window.dialogArguments.window.document;		
			var nrIdentificacao = getElementValue("pessoa.nrIdentificacao");
				
			// Seta parâmetros na tela pai.
			windowPai.setaNumeroIdentificacao(nrIdentificacao);		
			window.close();
			window.dialogArguments.window.close();
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function cepChange(){
		var isFounded = true;
		var cep = getElementValue("nrCepLookup.cepCriteria");
		var isCepPreenchido = cep == "";

		if (isCepPreenchido) {
			resetEnderecoCep();
			controlDisableUfAndMunicipio(false);

			resetRangeFields([
				'enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa'
				,'enderecoPessoa.municipio.nmMunicipio'
			]);

		} else {
			isFounded = nrCepLookup_cepCriteriaOnChangeHandler();
		}

		return isFounded;
	}
	/************************************************************\
	*
	\************************************************************/
	function onclickPickerLookupCep(){
		var cepCriteria = getElementValue("nrCepLookup.cepCriteria");
		lookupClickPicker({e:getElement("nrCepLookup.nrCep")});
		if(getElementValue("nrCepLookup.cepCriteria") == "" && cepCriteria != "") {
			setElementValue("nrCepLookup.cepCriteria", cepCriteria);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function myOnPageLoad_cb(dados, erros){
		onPageLoad_cb(dados, erros);
		setElementValue("pessoa.tpPessoa", "J");
		changeTypePessoaWidget({tpTipoElement:document.getElementById('pessoa.tpPessoa'), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'cad'});
		findPaisUsuarioLogado();
	}
	/************************************************************\
	*
	\************************************************************/
	function findPaisUsuarioLogado() {
		var sdo = createServiceDataObject("lms.expedicao.cadastrarClientesAction.findPaisUsuarioLogado", "populaPais");
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function populaPais_cb(data, erros) {
		//Erro do Service
		if (erros != undefined){
			alert(erros);
			return false;
		}
		data = (data[0] != undefined) ? data[0] : data;
		var	idPais = getNestedBeanPropertyValue(data, "idPais");
		var nmPais = getNestedBeanPropertyValue(data, "nmPais");
		if (nmPais != undefined){
			resetUf();
			resetMunicipio();
			resetEnderecoCep();
			setElementValue("enderecoPessoa.municipio.unidadeFederativa.pais.idPais", idPais);
			setElementValue("enderecoPessoa.municipio.unidadeFederativa.pais.nmPais", nmPais);
			notifyElementListeners({e:document.getElementById("enderecoPessoa.municipio.unidadeFederativa.pais.idPais")});
		}
		return true;
	}
	/************************************************************\
	*
	\************************************************************/
	function lookupPais_cb(data) {
		lookupExactMatch({e:document.getElementById("enderecoPessoa.municipio.unidadeFederativa.pais.idPais"), data:data, callBack:'lookupPaisLikeAndMatch'});
		populatePais(( (data[0] != undefined) ? data[0] : data));
	}
	/************************************************************\
	*
	\************************************************************/
	function lookupPaisLikeAndMatch_cb(data){
		lookupLikeEndMatch({e:document.getElementById("enderecoPessoa.municipio.unidadeFederativa.pais.idPais"), data:data});
		populatePais(( (data[0] != undefined) ? data[0] : data));
	}
	/************************************************************\
	*
	\************************************************************/
	function populatePais(data) {
		if(data == undefined) return;
		resetUf();
		resetMunicipio();
		resetEnderecoCep();
	}
	/************************************************************\
	*
	\************************************************************/
	/**
	 * Seta dataType para pessoa.nrIdentificacao conforme Tipo Pessoa (Física/Jurídica) e
	 * Tipo de identificação for CPF/CNPJ
	 */
	function trocaTpIdentificacao() {
		var tpPessoa = getElementValue("pessoa.tpPessoa");
		var tpIdentificacao = getElementValue("pessoa.tpIdentificacao");
		resetValue("pessoa.nrIdentificacao");
		resetValue("pessoa.idPessoa");
		resetValue("pessoa.nmPessoa");
		resetValue("enderecoPessoa.idEnderecoPessoa");
		setDisabledEndereco(false);
		if(tpPessoa == "" || tpPessoa == undefined){
			resetValue("pessoa.tpIdentificacao");
			setDisabled("pessoa.nrIdentificacao", true);
			setDisabled("pessoa.tpIdentificacao", true);
			return;
		} else {
			setDisabled("pessoa.tpIdentificacao", false);
			setDisabled("pessoa.nrIdentificacao", false);
		}
		if(tpIdentificacao == "" || tpIdentificacao == undefined) {
			setDisabled("pessoa.nrIdentificacao", true);
		}
		var nrIdObj = document.getElementById("pessoa.nrIdentificacao");
		if (tpPessoa == "F" && tpIdentificacao == "C" ) {
			nrIdObj.dataType = "CPF";
		} else if (tpPessoa == "J" && tpIdentificacao == "C" ){
			nrIdObj.dataType = "CNPJ";
		} else if (tpPessoa == "J" && tpIdentificacao == "I" ){
			nrIdObj.dataType = "CUIT";
		} else if (tpPessoa == "F" && tpIdentificacao == "I" ){
			nrIdObj.dataType = "DNI";
		} else if (tpPessoa == "F" && tpIdentificacao == "R" ){
			nrIdObj.dataType = "RUT";
		} else if (tpPessoa == "F" && tpIdentificacao == "U" ){
			nrIdObj.dataType = "RUC";
		} else nrIdObj.dataType = "text";
	}
	/************************************************************\
	*
	\************************************************************/
	function popupMunicipio(data) {
		populatePaisMunicipio(data);
		return true;
	}
	/************************************************************\
	*
	\************************************************************/
	function popupNrCep(data) {
		populateCep(data);
		return true;
	}
	/************************************************************\
	*
	\************************************************************/
	function populateCep(data) {
		if(data == undefined) {
			controlDisableUfAndMunicipio(false);
			return;
		}

		controlDisableUfAndMunicipio(true);

		setElementValue("nrCepLookup.nrCep", data.nrCep);
		setElementValue("nrCepLookup.cepCriteria", data.nrCep);
		setElementValue("enderecoPessoa.nrCep", data.nrCep);
		setElementValue("enderecoPessoa.dsBairro", data.nmBairro);
		setElementValue("enderecoPessoa.dsEndereco", data.nmLogradouro);
		var idTpLog = getIdTipoLogradouroByDs(data.dsTipoLogradouro);
		if (idTpLog != undefined) {
			setElementValue("enderecoPessoa.tipoLogradouro.dsTipoLogradouro", data.dsTipoLogradouro);
			setElementValue("enderecoPessoa.tipoLogradouro.idTipoLogradouro", idTpLog);
		}
		var nmPais = getNestedBeanPropertyValue(data, "municipio.unidadeFederativa.pais.nmPais");
		if (nmPais != undefined){
			var	idPais = getNestedBeanPropertyValue(data, "municipio.unidadeFederativa.pais.idPais");
			var pais = getElementValue("enderecoPessoa.municipio.unidadeFederativa.pais.idPais");
			var idUf = getNestedBeanPropertyValue(data, "municipio.unidadeFederativa.idUnidadeFederativa");
			var sgUf = getNestedBeanPropertyValue(data, "municipio.unidadeFederativa.sgUnidadeFederativa");
			var nmUf = getNestedBeanPropertyValue(data, "municipio.unidadeFederativa.nmUnidadeFederativa");
			var idMun = getNestedBeanPropertyValue(data, "municipio.idMunicipio");
			var nmMun = getNestedBeanPropertyValue(data, "municipio.nmMunicipio");
			setElementValue("_idUf", idUf);
			setElementValue("enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa", sgUf);
			setElementValue("enderecoPessoa.municipio.unidadeFederativa.nmUnidadeFederativa", nmUf);
			setElementValue("enderecoPessoa.municipio.idMunicipio", idMun);
			setElementValue("enderecoPessoa.municipio.nmMunicipio", nmMun);
			if(idPais != pais) {
				setElementValue("enderecoPessoa.municipio.unidadeFederativa.pais.idPais", idPais);
				setElementValue("enderecoPessoa.municipio.unidadeFederativa.pais.nmPais", nmPais);
				notifyElementListeners({e:document.getElementById("enderecoPessoa.municipio.unidadeFederativa.pais.idPais")});
			} else {
				var idUf = getElementValue("_idUf");
				if(idUf) {
					setElementValue("enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa",idUf);
					comboboxChange({e:document.getElementById("enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa")});
				}	
				
			}
		}
			
	}
	/************************************************************\
	*
	\************************************************************/
	function controlDisableUfAndMunicipio(isDisable){
		setDisabled('enderecoPessoa.municipio.nmMunicipio', isDisable);
		setDisabled('enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa', isDisable);
	}
	/************************************************************\
	*
	\************************************************************/
	function nrCepLookup_cb(data) {
		controlDisableUfAndMunicipio(data.length > 0);

		if (data.length <= 0){
			var cep = getElementValue("nrCepLookup.cepCriteria");
			setElementValue("enderecoPessoa.nrCep", cep);
			return;
		}
		if (data.length > 1){
			lookupClickPicker({e:document.forms[0].elements['nrCepLookup.nrCep']});
		} else 
			populateCep(( (data[0] != undefined) ? data[0] : data));
	}
	/************************************************************\
	*
	\************************************************************/
	function municipio_cb(data) {
		lookupExactMatch({e:document.getElementById("enderecoPessoa.municipio.idMunicipio"), data:data, callBack:'municipioLikeAndMatch'});
		populatePaisMunicipio(( (data[0] != undefined) ? data[0] : data));
	}
	/************************************************************\
	*
	\************************************************************/
	function municipioLikeAndMatch_cb(data){
		lookupLikeEndMatch({e:document.getElementById("enderecoPessoa.municipio.idMunicipio"), data:data});
		populatePaisMunicipio(( (data[0] != undefined) ? data[0] : data));
	}
	/************************************************************\
	*
	\************************************************************/
	function populatePaisMunicipio(data) {
		if(data == undefined) return;
		var nmPais = getNestedBeanPropertyValue(data, "unidadeFederativa.pais.nmPais");
		if (nmPais != undefined){
			var idPais = getNestedBeanPropertyValue(data, "unidadeFederativa.pais.idPais");
			var pais = getElementValue("enderecoPessoa.municipio.unidadeFederativa.pais.idPais");
			if(idPais != pais) {
				setElementValue("enderecoPessoa.municipio.unidadeFederativa.pais.idPais", idPais);
				setElementValue("enderecoPessoa.municipio.unidadeFederativa.pais.nmPais", nmPais);
				notifyElementListeners({e:document.getElementById("enderecoPessoa.municipio.unidadeFederativa.pais.idPais")});
			} else {
				var idUf = getElementValue("_idUf");
				var id = '';

				if(idUf) id = idUf;
				else if(data && data.unidadeFederativa) id = data.unidadeFederativa.idUnidadeFederativa;

				setElementValue("enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa", id);
				comboboxChange({e:document.getElementById("enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa")});
			}
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function resetEnderecoCep() {
		resetRangeFields([
			"nrCepLookup.nrCep"
			,"nrCepLookup.cepCriteria"
			,"enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa"
			,"enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa"
			,"enderecoPessoa.municipio.unidadeFederativa.nmUnidadeFederativa"
			,"enderecoPessoa.municipio.idMunicipio"
			,"enderecoPessoa.municipio.nmMunicipio"
		]);
	}
	/************************************************************\
	*
	\************************************************************/
	function resetMunicipio() {
		resetRangeFields([
			"enderecoPessoa.municipio.idMunicipio"
			,"enderecoPessoa.municipio.nmMunicipio"
		]);
		
	}
	/************************************************************\
	*
	\************************************************************/
	function resetRangeFields(arrNmFields){
		for(var k in arrNmFields) setElementValue(arrNmFields[k], '');
	}
	/************************************************************\
	*
	\************************************************************/
	function resetUf() {
		resetRangeFields([
			"_idUf"
			,"enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa"
		])
	}
	/************************************************************\
	*
	\************************************************************/
	function changeMunicipio() {
		var r = true;
		if(getElementValue("enderecoPessoa.municipio.nmMunicipio") == "") {
			resetRangeFields([
				"enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa"
				,"enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa"
				,"enderecoPessoa.municipio.unidadeFederativa.nmUnidadeFederativa"
				,"_idUf"
			]);
		} else {
			r = enderecoPessoa_municipio_nmMunicipioOnChangeHandler();
		}	
		return r;
	}
	/************************************************************\
	*
	\************************************************************/
	function changeUf(combo) {
		comboboxChange({e:combo});
		resetMunicipio();
	}
	/************************************************************\
	*
	\************************************************************/
	function changePais() {
		var r = true;
		if(getElementValue("enderecoPessoa.municipio.unidadeFederativa.pais.nmPais") == "") {
			resetValue("enderecoPessoa.municipio.unidadeFederativa.pais.idPais");
			resetUf();
			resetMunicipio();
			resetEnderecoCep()
		} else {
			r = enderecoPessoa_municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		}
		return r; 
	}
	/************************************************************\
	*
	\************************************************************/
	function popupPais(data) {
		resetEnderecoCep();
		resetMunicipio();
		resetUf();
		return true;
	}
	/************************************************************\
	*
	\************************************************************/
	/**
	 * Verifica o ID da combobox de Tipo de Logradouro pelo seu texto.
	 *@param dsTipoLogradouro Descrição contida na combobox
	 *@return ID da combobox
	 */
	function getIdTipoLogradouroByDs(dsTipoLogradouro){
		var opt = document.getElementById("enderecoPessoa.tipoLogradouro.idTipoLogradouro").options;
		for (i = 0; i < opt.length; i++){
			if (opt[i].text == dsTipoLogradouro){
				return opt[i].value;
			}
		}
	}
	/************************************************************\
	*
	\************************************************************/
	/*
	* Callback para combo de UF origem.
	*/
	function ufOrigemOnDataLoad_cb(dados, erro){
		enderecoPessoa_municipio_unidadeFederativa_idUnidadeFederativa_cb(dados);
		var idUf = getElementValue("_idUf");
		if (idUf != undefined && idUf != ""){
			setElementValue("enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa",idUf);
			comboboxChange({e:document.getElementById("enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa")});
		}
	}
	/************************************************************\
	*
	\************************************************************/
	/**
	 * Valida se já existe uma Pessoa cadastrada como despachante 
	 */
	function pessoa_nrIdentificacao_onChange(obj) {
		var tpPessoa = getElementValue("pessoa.tpPessoa");
		var nrIdentificacao = getElementValue("pessoa.nrIdentificacao");
		var tpIdentificacao = getElementValue("pessoa.tpIdentificacao");
		var origem = getElementValue("origem");
		if (nrIdentificacao == "" || tpIdentificacao == "" ) {
			resetValue("enderecoPessoa.idEnderecoPessoa");
			resetValue("pessoa.idPessoa");
			resetValue("pessoa.nmPessoa");
			setDisabledEndereco(false);
			return true;
		}
		if (validateIdentificacao(obj)) {
			//Faz a busca em Pessoa
			var sdo = createServiceDataObject("lms.expedicao.cadastrarClientesAction.findPessoa",
				"pessoa_nrIdentificacao",{nrIdentificacao:nrIdentificacao, tpIdentificacao:tpIdentificacao, tpPessoa:tpPessoa, origem:origem});
			xmit({serviceDataObjects:[sdo]});
		}
		return true;
	}

	function validateIdentificacao(obj) {
		var type = obj.dataType.toLowerCase();
		var result = false; 
		switch (type) {
			case "cpf" :
				result = isCpf(obj.value);
				break;
			case "cnpj" :
				result = isCnpj(obj.value);
				break;
			case "cuit" :
				result = validateCUIT(obj.value);
				break;
			case "rut" :
				result = validateRUT(obj.value);
				break;
			case "ruc" :
				result = validateRUC(obj.value);
				break;
			case "dni" :
				result = validateDNI(obj.value);
				break;
		}
		return result;
	}
	
	/************************************************************\
	*
	\************************************************************/
	/**
	 * Popula dados de Pessoa no formulário.
	 * 
	 */
	function pessoa_nrIdentificacao_cb(data, erro){
		//Erro do Service
		if (erro != undefined){
			alert(erro);
			resetValue("pessoa.nrIdentificacao");
			resetValue("pessoa.tpIdentificacao");
			resetValue("pessoa.idPessoa");
			return false;
		}
		// Se Pessoa cadastrada
		if(data.idPessoa) {
			setElementValue("pessoa.idPessoa",data.idPessoa);
			setElementValue("pessoa.nmPessoa",data.nmPessoa);
			var endereco = data.endereco;
			if(endereco && endereco.idEnderecoPessoa) {
				setElementValue("enderecoPessoa.tipoLogradouro.idTipoLogradouro", endereco.tipoLogradouro.idTipoLogradouro);
				comboboxChange({e:document.getElementById("enderecoPessoa.tipoLogradouro.idTipoLogradouro")});
				setElementValue("enderecoPessoa.nrCep", endereco.nrCep);
				setElementValue("nrCepLookup.cepCriteria", endereco.nrCep);
				setElementValue("nrCepLookup.nrCep", endereco.nrCep);
				setElementValue("enderecoPessoa.idEnderecoPessoa", endereco.idEnderecoPessoa);
				setElementValue("enderecoPessoa.nrEndereco", endereco.nrEndereco);
				setElementValue("enderecoPessoa.dsEndereco", endereco.dsEndereco);
				setElementValue("enderecoPessoa.dsBairro", endereco.dsBairro);
				setElementValue("enderecoPessoa.dsComplemento", endereco.dsComplemento);
				setElementValue("enderecoPessoa.municipio.idMunicipio", endereco.municipio.idMunicipio);
				setElementValue("enderecoPessoa.municipio.nmMunicipio", endereco.municipio.nmMunicipio);
				setElementValue("_idUf", endereco.municipio.unidadeFederativa.idUnidadeFederativa);	
				var pais = getElementValue("enderecoPessoa.municipio.unidadeFederativa.pais.idPais");
				if(endereco.municipio.unidadeFederativa.pais.idPais != pais) {
					setElementValue("enderecoPessoa.municipio.unidadeFederativa.pais.idPais", endereco.municipio.unidadeFederativa.pais.idPais);
					setElementValue("enderecoPessoa.municipio.unidadeFederativa.pais.nmPais", endereco.municipio.unidadeFederativa.pais.nmPais);
					notifyElementListeners({e:document.getElementById("enderecoPessoa.municipio.unidadeFederativa.pais.idPais")});
				} else {
					setElementValue("enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa",endereco.municipio.unidadeFederativa.idUnidadeFederativa);
					comboboxChange({e:document.getElementById("enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa")});
				}
				setDisabledEndereco(true);
				return true;
			} 
		}
		return true;
	}
	/************************************************************\
	*
	\************************************************************/
	function resetEndereco() {
		setElementValue("pessoa.idPessoa","");
		setElementValue("pessoa.nmPessoa","");
		resetEnderecoCep();
		setElementValue("enderecoPessoa.idEnderecoPessoa", "");
		setElementValue("enderecoPessoa.nrEndereco", "");
		resetMunicipio();
		resetUf();
		findPaisUsuarioLogado();
		setDisabledEndereco(false);
	}
	/************************************************************\
	*
	\************************************************************/
	function setDisabledEndereco(dis) {
		setDisabled("enderecoPessoa.tipoLogradouro.idTipoLogradouro", dis);
		setDisabled("enderecoPessoa.nrCep", dis);
		setDisabled("nrCepLookup.cepCriteria", dis);
		setDisabled("nrCepLookup.nrCep", dis);
		setDisabled("enderecoPessoa.nrEndereco", dis);
		setDisabled("enderecoPessoa.dsEndereco", dis);
		setDisabled("enderecoPessoa.dsBairro", dis);
		setDisabled("enderecoPessoa.dsComplemento", dis);
		setDisabled("enderecoPessoa.municipio.nmMunicipio", dis);
		setDisabled("enderecoPessoa.municipio.unidadeFederativa.pais.nmPais", dis);
		setDisabled("enderecoPessoa.municipio.idMunicipio", dis);
		setDisabled("enderecoPessoa.municipio.unidadeFederativa.pais.idPais", dis);
		setDisabled("enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa", dis);
	}
	
	function changeTipoPessoa() {
		var tpPessoa = getElementValue("pessoa.tpPessoa");
		if (tpPessoa == "F") {
			setElementValue("ramoAtividade.idRamoAtividade", "");
			setDisabled("ramoAtividade.idRamoAtividade", true);
			getElement("ramoAtividade.idRamoAtividade").required = "false";
		} else if (tpPessoa == "J") {
			setDisabled("ramoAtividade.idRamoAtividade", false);
			getElement("ramoAtividade.idRamoAtividade").required = "true";
		}
		
		return changeTypePessoaWidget({tpTipoElement:document.getElementById('pessoa.tpPessoa'), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'cad'});
	}
-->
</script>