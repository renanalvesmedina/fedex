<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.configuracoes.manterEnderecoPessoaAction" onPageLoadCallBack="myOnPageLoad"> 

	<adsm:i18nLabels>
		<adsm:include key="LMS-29172"/>
		<adsm:include key="LMS-27094"/>
	</adsm:i18nLabels>
	
	<adsm:form id="enderecoPessoa.form" action="/configuracoes/manterEnderecoPessoa" idProperty="idEnderecoPessoa" 
	service="lms.configuracoes.manterEnderecoPessoaAction.findById" onDataLoadCallBack="manterEnderecoPessoa" >

		<adsm:hidden property="blDisableVigenciaFinal" value="false"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>
		<adsm:hidden property="tpSituacaoAtivo" value="A" serializable="false"/>
		<adsm:hidden property="pessoa.idPessoa"/>
		<adsm:hidden property="blCepOpcional"/>
		<adsm:hidden property="flagAbaIntegrantes"/>
		<adsm:hidden property="municipio.unidadeFederativa.pais.blCepAlfanumerico"/>
		
		<adsm:label key="branco" style="width:0"/>
		<td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>
		<adsm:textbox property="pessoa.nrIdentificacao" dataType="text" size="20" width="13%" maxLength="20"
			disabled="true" serializable="false">
			<adsm:textbox property="pessoa.nmPessoa" dataType="text" size="60" disabled="true" serializable="false" width="74%" />
		</adsm:textbox>

	 	<adsm:lookup label="pais" property="municipio.unidadeFederativa.pais" idProperty="idPais" dataType="text" criteriaProperty="nmPais"
			service="lms.configuracoes.manterEnderecoPessoaAction.findLookupPais" size="40" maxLength="60" serializable="false" minLengthForAutoPopUpSearch="3"
			action="/municipios/manterPaises" required="true" exactMatch="false"
			onchange="return paisOnChange(this);"
			onDataLoadCallBack="paisOnDataLoadCallBack"
			onPopupSetValue="paisOnPopupSetValue">
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
		</adsm:lookup>

		<!-- Manter o campo como text e com a mascara setada para habilitar o listener da mascara -->
	 	<adsm:lookup label="cep" 
	 	             property="nrCepLookup" 
	 	             dataType="text"
	 	             idProperty="nrCep"
	 	             criteriaProperty="cepCriteria"
	 	             service="lms.configuracoes.manterEnderecoPessoaAction.findCepLookup" 
	 	             size="20" maxLength="8" serializable="false" 
	 	             required="false" 
	 	             allowInvalidCriteriaValue="true"
					 action="/configuracoes/pesquisarCEP" 
					 onDataLoadCallBack="nrCepLookup" 
					 onPopupSetValue="popupNrCep"
					 onchange="return cepOnChange(this);"
					 mask="00000000">
			
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais" addChangeListener="false"
				modelProperty="municipio.unidadeFederativa.pais.nmPais" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" addChangeListener="false" 
				modelProperty="municipio.unidadeFederativa.pais.idPais" inlineQuery="true"/>
				
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" inlineQuery="false"/>

			<adsm:propertyMapping criteriaProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" inlineQuery="false"/>
			
			<adsm:propertyMapping criteriaProperty="dsBairro" modelProperty="nmBairro" inlineQuery="false"/>

			<adsm:propertyMapping relatedProperty="nrCep" modelProperty="nrCep"/>
			<adsm:propertyMapping relatedProperty="dsBairro" modelProperty="nmBairro"/>
			<adsm:propertyMapping relatedProperty="dsEndereco" modelProperty="nmLogradouro"/>
			<adsm:propertyMapping relatedProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="municipio.nmMunicipio" modelProperty="municipio.nmMunicipio"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="municipio.unidadeFederativa.pais.idPais" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="_dsTipoLogradouro" modelProperty="dsTipoLogradouro"/>

			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" 
				modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.idUnidadeFederativa" 
				modelProperty="municipio.unidadeFederativa.idUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" 
				modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" />

		</adsm:lookup>

		<adsm:hidden property="nrCep" serializable="true"/>
		<adsm:hidden property="_dsTipoLogradouro" serializable="false"/>

		<adsm:complement label="endereco" width="85%" required="true">
			<adsm:combobox property="tipoLogradouro.idTipoLogradouro" width="15%" service="lms.configuracoes.manterEnderecoPessoaAction.findLookupTipoLogradouro"
				optionLabelProperty="dsTipoLogradouro" optionProperty="idTipoLogradouro" onlyActiveValues="true" required="true" boxWidth="75">
				<adsm:propertyMapping relatedProperty="_dsTipoLogradouro" modelProperty="dsTipoLogradouro" addChangeListener="false"/>
			</adsm:combobox>
			<adsm:textbox property="dsEndereco" dataType="text" size="91" maxLength="100" width="65%"/>
		</adsm:complement>

		<adsm:textbox label="numero" property="nrEndereco" dataType="text" size="10" maxLength="5" required="true"/>
		<adsm:textbox label="complemento" property="dsComplemento" dataType="text" size="35" maxLength="60"/>
		<adsm:textbox label="bairro" property="dsBairro" dataType="text" size="40" maxLength="60"/>

		<adsm:lookup label="municipio" property="municipio" idProperty="idMunicipio" dataType="text" criteriaProperty="nmMunicipio"
			service="lms.configuracoes.manterEnderecoPessoaAction.findLookupMunicipio" size="35" maxLength="60" onDataLoadCallBack="municipio"
			required="true"
			action="/municipios/manterMunicipios" exactMatch="false" minLengthForAutoPopUpSearch="3">

			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
			
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais" 
				addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" 
				addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais"
				blankFill="false"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais"
				blankFill="false" />
	
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.idUnidadeFederativa" 
				modelProperty="unidadeFederativa.idUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" 
				modelProperty="unidadeFederativa.sgUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" 
				modelProperty="unidadeFederativa.nmUnidadeFederativa"/>
	
		</adsm:lookup>

		<adsm:hidden property="municipio.unidadeFederativa.idUnidadeFederativa" serializable="true"/>
		<adsm:textbox property="municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf"
			serializable="false" disabled="true" size="5">
				<adsm:textbox property="municipio.unidadeFederativa.nmUnidadeFederativa" disabled="true" dataType="text" 
					serializable="false" size="32"/>
		</adsm:textbox>

		<adsm:range label="vigencia">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" size="10" required="true" onchange="onChangeDtVigenciaInicial();"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"   size="10"/>
		</adsm:range>
		
        <adsm:combobox property="enderecoSubstituido.idEnderecoPessoa" boxWidth="442" width="85%" 
					   optionLabelProperty="enderecoCompleto" optionProperty="idEnderecoPessoa" autoLoad="false"
					   onchange="return onChangeEnderecoSubstituido(this);"
		               service="lms.configuracoes.manterEnderecoPessoaAction.findEnderecoSubstituidoCombo" 
		               label="enderecoSubstituido" required="false"
		               onDataLoadCallBack="myEnderecoPessoa">
		</adsm:combobox>

		<adsm:textbox property="nrLatitude" label="latitude" dataType="text" size="20"  maxLength="18" labelWidth="15%" width="35%"
			onchange="return validaLatitude();"/>
 
		<adsm:textbox property="nrLongitude" label="longitude" dataType="text" size="20"  maxLength="18" labelWidth="15%" width="35%"
			onchange="return validaLongitude();"/>
		
		<adsm:textbox property="nrLatitudeTmp" label="latitudeTemp" dataType="text" size="20"  maxLength="18" labelWidth="15%" width="35%"
			onchange="return validaLatitudeTmp();"/>
		
		<adsm:textbox property="nrLongitudeTmp" label="longitudeTemp" dataType="text" size="20"  maxLength="18" labelWidth="15%" width="35%"
			onchange="return validaLongitudeTmp();"/>
			
		<adsm:textbox property="qualidade" label="qualidade" dataType="text" size="10" maxLength="2" labelWidth="15%" width="35%" />
		
		<adsm:textarea width="85%" columns="100" rows="3" maxLength="500" property="obEnderecoPessoa" label="observacao" labelWidth="15%"/>

		<adsm:textbox property="nmUsuarioInclusao" label="usuarioInclusao" disabled="true" dataType="text" labelWidth="15%" width="35%"/>
		
		<adsm:textbox property="nmUsuarioAlteracao" label="usuarioAlteracao" disabled="true" dataType="text" labelWidth="15%" width="35%"/>

		<adsm:hidden property="enderecoCompleto" serializable="false"/>
		
		<adsm:buttonBar>
			<adsm:button id="tiposEndereco" caption="tiposEndereco" onclick="tiposEnderecoClick()">
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="enderecoPessoa.pessoa.nrIdentificacaoFormatado"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="enderecoPessoa.pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoaTemp" target="labelPessoaTemp"/>
				<adsm:linkProperty src="enderecoCompleto" target="enderecoPessoa.enderecoCompleto"/>
				<adsm:linkProperty src="idEnderecoPessoa" target="enderecoPessoa.idEnderecoPessoa"/>
				<adsm:linkProperty src="pessoa.idPessoa" target="enderecoPessoa.pessoa.idPessoa"/>
			</adsm:button>
			<adsm:button id="telefones" caption="telefones" onclick="telefonesClick()">
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa"/>
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoaTemp" target="labelPessoaTemp"/>
				
				<adsm:linkProperty src="idEnderecoPessoa" target="idEnderecoPessoaTmp"/>
				
			</adsm:button>
			<adsm:button caption="salvar" buttonType="storeButton" onclick="store(this);" id="storeButton"/>
			<adsm:button id="btLimpar" caption="limpar" buttonType="newButton" onclick="limpar();"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>


	function store(obj){
		// Caso o cep seja obrigatório e não tenha sido preenchido, não deve salvar a agencia.
		if (!validateBlCepOpcional()) {
			return false;
		}
		storeButtonScript('lms.configuracoes.manterEnderecoPessoaAction.store', 'myStore', obj.form);
	}

	/**
 	* Function que limpa os campos da tela e desabilita todos os campos.
 	*
 	* chamado por: botão limpar
 	*/
	function limpar(){
		newButtonScript(this.document, true, {name:'newButton_click'});
		desabilitaTodosCampos(false);
		clearCepEndereco();

		var sdo = createServiceDataObject("adsm.security.usuarioService.findDadosUsuarioLogado", "loadUserInfo", null);
		xmit({serviceDataObjects:[sdo]});

	}
	
	function loadUserInfo_cb(data, error) {
		if (error != null) {
		} else {
			if (data["nomeUsuario"] != "") {
				setElementValue("nmUsuarioInclusao",data["nomeUsuario"]); 
			}
		}
	}
	
	
	/**
 	* Function que desabilita todos os campos da tela e seta os valores default
 	*
 	* chamado por: limpar, initWindow
 	*/
	function desabilitaTodosCampos(val){
		if (val == undefined){ 
			val = true;	
		}
		
		setDisabled('nrCepLookup.nrCep',val);
		setDisabled('nrCep',val);
		setDisabled('_dsTipoLogradouro',val);
		setDisabled('tipoLogradouro.idTipoLogradouro',val);
		setDisabled('dsEndereco',val);
		setDisabled('nrEndereco',val);
		setDisabled('dsComplemento',val);
		setDisabled('dsBairro',val);
		setDisabled('municipio.idMunicipio',val);
		setDisabled('dtVigenciaInicial',val);
		setDisabled('nrLatitude',val);
		setDisabled('nrLatitudeTmp',val);
		setDisabled('nrLongitude',val);
		setDisabled('nrLongitudeTmp',val);
		setDisabled('qualidade',val);
		setDisabled('obEnderecoPessoa',val);
		setDisabled('municipio.unidadeFederativa.pais.idPais',val);
		setDisabled('enderecoSubstituido.idEnderecoPessoa',val);
		setDisabled('dtVigenciaFinal',true);
		setDisabled('nmUsuarioAlteracao',true);
		setDisabled('nmUsuarioInclusao',true);

		initPage();
		setFocusOnFirstFocusableField();
	}
	
	
	/**
 	* Function chamada ao carregar a página
 	*/
	function initWindow(eventObj){
		validateExisteFilialByIdPessoa();
		
		setDisabled("btLimpar",false);
		
		if (eventObj.name != "storeButton") {
			validaQuantidadeEnderecoPessoa();
		}
		
		
		
		// Atualiza o endereço substituído
		findEnderecoPessoa();	

		var u = new URL(parent.location.href);
		var idCliente = u.parameters["idCliente"];
   		var nrIdentificacao  = u.parameters["nrIdentificacao"];
   		var nmPessoa = u.parameters["nmPessoa"];
   		var tipoCliente = u.parameters["tipoCliente"];
   		var flagAbaIntegrantes = u.parameters["flagAbaIntegrantes"];
   		
   		if(idCliente!= null){
			setElementValue("pessoa.idPessoa",idCliente);
			document.getElementById("pessoa.idPessoa").masterLink="true";
		}
   		
		if(nrIdentificacao!= null){
			setElementValue("pessoa.nrIdentificacao",nrIdentificacao);
			document.getElementById("pessoa.nrIdentificacao").masterLink="true";
		}	
        if(nmPessoa!= null){
        	setElementValue("pessoa.nmPessoa",nmPessoa);
        	document.getElementById("pessoa.nmPessoa").masterLink="true";
        }	
        if(tipoCliente != null)	
			setElementValue("labelPessoaTemp",tipoCliente);
			
		if(flagAbaIntegrantes != null){
			setDisabled("__buttonBar:0.storeButton",true);
        	setDisabled("__buttonBar:0.removeButton",true);
        	setDisabled("btLimpar",true);
        	setDisabled("__buttonBar:0_2",true);
        	setDisabled("__buttonBar:0_1",true);
        	document.getElementById("flagAbaIntegrantes").masterLink="true";
        	
		}	
		carregaLabelPessoa();	

		if (eventObj.name == "tab_click" ){
			limpar();		
		}
		
		if( eventObj.name == "removeButton" ){
			limpar();	
			initPage();
			setFocusOnFirstFocusableField();	
			showSuccessMessage();
		}

		if( eventObj.name == "storeButton" ) {
			setFocus('btLimpar',true,true);
		}else{
			setFocusOnFirstFocusableField();
		}
		
		setDisabled('nmUsuarioAlteracao',true);
		setDisabled('nmUsuarioInclusao',true);
	}

	function preencherCoordenadasTemporarias() {
		var idEnderecoPessoa = getElementValue("idEnderecoPessoa");
		var nrLatitudeTmp = getElementValue("nrLatitudeTmp");
		var nrLongitudeTmp = getElementValue("nrLongitudeTmp");
		var qualidade = getElementValue("qualidade");
		
		if ((nrLatitudeTmp != null && nrLatitudeTmp !="") 
				&& (nrLongitudeTmp != null && nrLongitudeTmp !="")
					&& (qualidade != null && qualidade !="")) {
			return;
		}
		
		loadCoordenadasTemporaria();
	}
	
	function onChangePreencherQualidadePadrao() {
		var nrLatitudeTmp = getElementValue("nrLatitudeTmp");
		var nrLongitudeTmp = getElementValue("nrLongitudeTmp");
		
		if (nrLatitudeTmp != "" && nrLongitudeTmp != "") {
			setElementValue("qualidade", 1);
		}
		
	}
	
	function loadCoordenadasTemporaria() {
		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject(
			"lms.configuracoes.manterEnderecoPessoaAction.loadCoordenadasTemporaria", 
			"loadCoordenadasTemporaria",
			{idPessoa:getElementValue("pessoa.idPessoa")}));
		xmit(false);		
	}
	
	function loadCoordenadasTemporaria_cb(data, error){
		if (error != undefined) {
			alert(error);
		}
		
		if(data != null && data != undefined) {
			setElementValue("nrLatitudeTmp", getNestedBeanPropertyValue(data, "nrLatitudeTmp"));
			setElementValue("nrLongitudeTmp", getNestedBeanPropertyValue(data, "nrLongitudeTmp"));
			setElementValue("qualidade", getNestedBeanPropertyValue(data, "qualidade"));
		}
		
	}
	
	function validateExisteFilialByIdPessoa(){
		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject(
			"lms.configuracoes.manterEnderecoPessoaAction.validateExisteFilialByIdPessoa", 
			"validateExisteFilialByIdPessoa",
				{idPessoa:getElementValue("pessoa.idPessoa")}));
		xmit(false);
	}
	
	function validateExisteFilialByIdPessoa_cb(data, error){
		if (error != undefined) {
			alert(error);
		}
		
		var required = data._value;
			
		getElement("nrLatitudeTmp").setAttribute("required", required);
		getElement("nrLongitudeTmp").setAttribute("required", required);
		getElement("qualidade").setAttribute("required", required);
	}	
	
	function validaQuantidadeEnderecoPessoa(){
		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject(
			"lms.configuracoes.manterEnderecoPessoaAction.validaQuantidadeEnderecoPessoa", 
			"validaQuantidadeEnderecoPessoa",
				{idPessoa:getElementValue("pessoa.idPessoa")}));
		xmit(false);
	}
	
	function validaQuantidadeEnderecoPessoa_cb(data, error){
		if (error != undefined) {
			alert(error);
		}
		
			setDisabled("dtVigenciaFinal", true);
		
	}
	
	/**
 	* Function que carrega os valores padrões da tela
 	*
 	* chamado desabilitaTodosCampos
 	*/
	function initPage(){
	
		//TODO Fazer somente um xmit
		var sdo = createServiceDataObject("lms.configuracoes.manterEnderecoPessoaAction.findPaisUsuarioLogadoDataAtual",
			"initPage",new Array());
		xmit({serviceDataObjects:[sdo]});
	}
	
	
	/* Retorno do initPage */
	function initPage_cb(data,erro) {
		if (erro != undefined) {
			alert(erro);
			return;
		}
	
		setElementValue("dtVigenciaInicial", setFormat("dtVigenciaInicial",getNestedBeanPropertyValue(data, "dtVigenciaInicial")));
		setDisabled("tiposEndereco", true);
		setDisabled("telefones", true);
		setDisabled("removeButton", true);
		setElementValue("municipio.unidadeFederativa.pais.nmPais", getNestedBeanPropertyValue(data, "nmPais"));
		setElementValue("municipio.unidadeFederativa.pais.idPais", getNestedBeanPropertyValue(data, "idPais"));
		setElementValue("blCepOpcional", data.blCepOpcional);
		carregaLabelPessoa();
		doSetBlCepAlfanumerico();
		preencherCoordenadasTemporarias();
	}
	
	function enderecoCompleto(){
		var tl = document.getElementById("tipoLogradouro.idTipoLogradouro");
		var tpLogradouro = tl.options[tl.selectedIndex].text;
		var dsEndereco = document.getElementById("dsEndereco").value;
		var nrEndereco = document.getElementById("nrEndereco").value;
		var dsComplemento = document.getElementById("dsComplemento").value;

		var enderecoCompleto = tpLogradouro + " " + dsEndereco + ", " + nrEndereco;
		if (dsComplemento != ""){
			enderecoCompleto += " - " + dsComplemento;
		}

		enderecoCompleto = encodeString(enderecoCompleto);

		setElementValue(document.getElementById("enderecoCompleto"), enderecoCompleto);
	}

	function checkNome() {
		var nmPessoa = document.getElementById("pessoa.nmPessoa").value;
		nmPessoa = encodeString(nmPessoa);
		setElementValue(document.getElementById("pessoa.nmPessoa"), nmPessoa);
	}
	
	function telefonesClick(){
		enderecoCompleto();
		parent.parent.redirectPage('configuracoes/manterTelefonesPessoa.do?cmd=main' + buildLinkPropertiesQueryString_telefones());
	}

	function tiposEnderecoClick(){
		enderecoCompleto();
		checkNome();
		parent.parent.redirectPage('configuracoes/manterTiposEndereco.do?cmd=main' + buildLinkPropertiesQueryString_tiposEndereco());
	}

	function carregaLabelPessoa(){
		var labelPessoa = getElementValue("labelPessoaTemp");
		if (labelPessoa != ""){
			document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
		}
	}
	
	function municipio_cb(data) {
		lookupExactMatch({e:document.getElementById("municipio.idMunicipio"), data:data, callBack:'municipioLikeAndMatch'});
		var nmPais = getNestedBeanPropertyValue(( (data[0] != undefined) ? data[0] : data), "unidadeFederativa.pais.nmPais");
		if (nmPais != undefined && nmPais != document.getElementById("municipio.unidadeFederativa.pais.nmPais").value){
			municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		}
	}

	function municipioLikeAndMatch_cb(data){
		lookupLikeEndMatch({e:document.getElementById("municipio.idMunicipio"), data:data});
		var nmPais = getNestedBeanPropertyValue(( (data[0] != undefined) ? data[0] : data), "unidadeFederativa.pais.nmPais");
		if (data == undefined) return;
		if (nmPais != undefined && nmPais != document.getElementById("municipio.unidadeFederativa.pais.nmPais").value){
			municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		}
	}
	
	function nrCepLookup_cb(data, erro) {
		
		if ( erro != undefined ) {
			resetValue("nrCepLookup.nrCep");
			setFocus("nrCepLookup.cepCriteria");
			alert(erro);
			return false;
		}
		
		if (data.length <= 0) {
			var cep = document.getElementById("nrCepLookup.cepCriteria").value;
			setElementValue("nrCep", cep);
			clearEndereco();
			return;
		}

		var retorno = nrCepLookup_cepCriteria_exactMatch_cb(data);
		
		var idTpLog = getIdTipoLogradouroByDs(getElementValue("_dsTipoLogradouro"));
		if (idTpLog != undefined){
			setElementValue("tipoLogradouro.idTipoLogradouro", idTpLog);
		}
		
		if (data.length > 1){
			lookupClickPicker({e:document.forms[0].elements['nrCepLookup.nrCep']});
		} else {
			resetValue('nrEndereco');
			resetValue('dsComplemento');
		}
		
		var nmPais = getNestedBeanPropertyValue(data, "unidadeFederativa.pais.nmPais");
		
		if (nmPais != document.getElementById("municipio.unidadeFederativa.pais")){
			municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		}
		
		return retorno;

	}

	function popupNrCep(data) {
	
		// Caso o Municipio, a uf ou o pais seja inativo, lançar a exceção.
		if ( data.municipio.tpSituacao.value == 'I'
				|| data.municipio.unidadeFederativa.tpSituacao.value == 'I'
				|| data.municipio.unidadeFederativa.pais.tpSituacao.value == 'I' ) {
			
			setElementValue("nrCep", "");
			alert(i18NLabel.getLabel("LMS-29172")+'');	
			return false;
		}

		data.cepCriteria = data.nrCep;
		
		var backupOnPopupSetValue = document.getElementById("nrCepLookup.nrCep").onPopupSetValue;
		document.getElementById("nrCepLookup.nrCep").onPopupSetValue = undefined;
		__lookupSetValue({e:document.getElementById("nrCepLookup.nrCep"), data:data});
		document.getElementById("nrCepLookup.nrCep").onPopupSetValue = backupOnPopupSetValue;
		
		var idTpLog = getIdTipoLogradouroByDs(data.dsTipoLogradouro);
		if (idTpLog != undefined){
			setElementValue("tipoLogradouro.idTipoLogradouro", idTpLog);
		}
		
		var nmPais = getNestedBeanPropertyValue(( (data[0] != undefined) ? data[0] : data), "unidadeFederativa.pais.nmPais");
		if (nmPais != document.getElementById("municipio.unidadeFederativa.pais.nmPais").value){
			municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		}
		resetValue('nrEndereco');
		resetValue('dsComplemento');
		return false;
	}

	/**
	 * Verifica o ID da combobox de Tipo de Logradouro pelo seu texto.
	 *@param dsTipoLogradouro Descrição contida na combobox
	 *@return ID da combobox
	 */
	function getIdTipoLogradouroByDs(dsTipoLogradouro){
		var opt = document.getElementById("tipoLogradouro.idTipoLogradouro").options;
		for (i = 0; i < opt.length; i++){
			if (opt[i].text == dsTipoLogradouro){
				return opt[i].value;
			}
		}
	}

	/**
	* Function chamada no retorno do store, habilita os botões.
	*
	* chamado por: botão store
	*/
	function myStore_cb(dados, erros){
	
		if(erros != undefined){
			alert(erros);
			setFocusOnFirstFocusableField();
		}else{
			store_cb(dados, erros);
			if (dados.blDisableTela == "true"){
				setDisabled("enderecoPessoa.form", true);
				setDisabled("dtVigenciaFinal", true);
				setDisabled("tiposEndereco", false);
				setDisabled("telefones", false);
				setDisabled("storeButton", false);
				setDisabled("btLimpar", false);
				setDisabled("removeButton", false);		
			}else{
				setDisabled("enderecoPessoa.form", false);
			}
			manterEnderecoPessoa_cb(dados, erros);
			setFocus('btLimpar',true,true);	
		}
		setElementValue('nmUsuarioAlteracao',dados.nmUsuarioAlteracao);
		setElementValue('nmUsuarioInclusao',dados.nmUsuarioInclusao);
		setDisabled('nmUsuarioAlteracao',true);
		setDisabled('nmUsuarioInclusao',true);
		
		desabilitaUF();
		
	}
	
	function desabilitaUF(){
		setDisabled('municipio.unidadeFederativa.sgUnidadeFederativa',true);
		setDisabled('municipio.unidadeFederativa.nmUnidadeFederativa',true);
	}

	/**
	  * CallBack do form
	  */
	function manterEnderecoPessoa_cb(dados, erros){
		onDataLoad_cb(dados, erros);
		carregaLabelPessoa();
		
		if (dados.nrCep != undefined ){
			document.getElementById("nrCepLookup.cepCriteria").value = dados.nrCep;
		}		
		
		setFocusOnFirstFocusableField();
		
		// Se a flag blDisableTela == "true" desabilita os campos da tela.
		if (dados.blDisableTela == "true"){
			setDisabled("enderecoPessoa.form", true);
			
			setDisabled("dtVigenciaFinal", false);
			setDisabled("tiposEndereco", false);
			setDisabled("telefones", false); 
			setDisabled("storeButton", false);
			setDisabled("btLimpar", false);
			setDisabled("removeButton", false);	
		
		// Se a flag blDisableTela != "true" habilita os campos da tela.	
		}else{
			setDisabled("enderecoPessoa.form", false);
		}
		
		if (dados.blDisableVigenciaFinal == "true"){
			setDisabled("dtVigenciaFinal", true);
			setDisabled("enderecoSubstituido.idEnderecoPessoa", true);
		} else {
			setDisabled("enderecoSubstituido.idEnderecoPessoa", false);
		}
		
		if(getElementValue("flagAbaIntegrantes") != ''){
		    setDisabled("__buttonBar:0.storeButton",true);
        	setDisabled("__buttonBar:0.removeButton",true);
        	setDisabled("btLimpar",true);
        	setDisabled("__buttonBar:0_2",true);
        	setDisabled("__buttonBar:0_1",true);
        }
        
        if (dados.mensagem != undefined){
        	alert(dados.mensagem+'');
        }
        
        if ( dados.blDisableComboEnderecoSubstituido != undefined ) {
			setDisabled("enderecoSubstituido.idEnderecoPessoa", true);
		}
		
		// Seta este flag para desabilitar ou não a vigencia final no onChange da combo de 
		// endereço substituído.
		setElementValue("blDisableVigenciaFinal", dados.blDisableVigenciaFinal);
		findEnderecoPessoa();
		
        desabilitaUF();
		doSetBlCepAlfanumerico();
		setDisabled('nmUsuarioAlteracao',true);
		setDisabled('nmUsuarioInclusao',true);
		preencherCoordenadasTemporarias();
	}
	
	function myOnPageLoad_cb(d,e,c,x){
		onPageLoad_cb(d,e,c,x);
		findEnderecoPessoa();
		setDisabled('nmUsuarioAlteracao',true);
		setDisabled('nmUsuarioInclusao',true);
		preencherCoordenadasTemporarias();
	}
	
	function findEnderecoPessoa(){
		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject(
			"lms.configuracoes.manterEnderecoPessoaAction.findEnderecoSubstituidoCombo", 
			"findEnderecoPessoa",
				{idPessoa:getElementValue("pessoa.idPessoa"), 
				notIdEnderecoPessoa:getElementValue("idEnderecoPessoa"), 
				dtVigenciaInicial: getElementValue("dtVigenciaInicial")}));
		xmit(false);
	}
	
	function findEnderecoPessoa_cb(data,erro){
		enderecoSubstituido_idEnderecoPessoa_cb(data,erro);
		
		// Caso o flag(setado no findById) seja true, não deve habilitar a data final
		if (getElementValue("blDisableVigenciaFinal") == "false") {
			setDisabled("dtVigenciaFinal", false);
		}
	}
	
	
	
	
	// ButtonTag.linkProperties support function for dynamic queryString build
	function buildLinkPropertiesQueryString_tiposEndereco() {
	
		var qs = "";
		qs += "&enderecoPessoa.enderecoCompleto=" + document.getElementById("enderecoCompleto").value;
		if (document.getElementById("pessoa.idPessoa").type == 'checkbox') {
			qs += "&enderecoPessoa.pessoa.idPessoa=" + document.getElementById("pessoa.idPessoa").checked;
		} else {
			qs += "&enderecoPessoa.pessoa.idPessoa=" + document.getElementById("pessoa.idPessoa").value;
		}
		if (document.getElementById("pessoa.nrIdentificacao").type == 'checkbox') {
			qs += "&enderecoPessoa.pessoa.nrIdentificacaoFormatado=" + document.getElementById("pessoa.nrIdentificacao").checked;
		} else {
			qs += "&enderecoPessoa.pessoa.nrIdentificacaoFormatado=" + document.getElementById("pessoa.nrIdentificacao").value;
		}
		if (document.getElementById("pessoa.nmPessoa").type == 'checkbox') {
			qs += "&enderecoPessoa.pessoa.nmPessoa=" + document.getElementById("pessoa.nmPessoa").checked;
		} else {
			qs += "&enderecoPessoa.pessoa.nmPessoa=" + document.getElementById("pessoa.nmPessoa").value;
		}
		if (document.getElementById("labelPessoaTemp").type == 'checkbox') {
			qs += "&labelPessoaTemp=" + document.getElementById("labelPessoaTemp").checked;
		} else {
			qs += "&labelPessoaTemp=" + document.getElementById("labelPessoaTemp").value;
		}
		if (document.getElementById("idEnderecoPessoa").type == 'checkbox') {
			qs += "&enderecoPessoa.idEnderecoPessoa=" + document.getElementById("idEnderecoPessoa").checked;
		} else {
			qs += "&enderecoPessoa.idEnderecoPessoa=" + document.getElementById("idEnderecoPessoa").value;
		}	

		return qs;
	}



	// ButtonTag.linkProperties support function for dynamic queryString build
	function buildLinkPropertiesQueryString_telefones() {
	
		var qs = "";
		qs += "&enderecoPessoa.enderecoCompleto=" + document.getElementById("enderecoCompleto").value;
		if (document.getElementById("pessoa.idPessoa").type == 'checkbox') {
			qs += "&pessoa.idPessoa=" + document.getElementById("pessoa.idPessoa").checked;
		} else {
			qs += "&pessoa.idPessoa=" + document.getElementById("pessoa.idPessoa").value;
		}
		if (document.getElementById("pessoa.nrIdentificacao").type == 'checkbox') {
			qs += "&pessoa.nrIdentificacao=" + document.getElementById("pessoa.nrIdentificacao").checked;
		} else {
			qs += "&pessoa.nrIdentificacao=" + document.getElementById("pessoa.nrIdentificacao").value;
		}
		if (document.getElementById("pessoa.nmPessoa").type == 'checkbox') {
			qs += "&pessoa.nmPessoa=" + document.getElementById("pessoa.nmPessoa").checked;
		} else {
			qs += "&pessoa.nmPessoa=" + document.getElementById("pessoa.nmPessoa").value;
		}
		if (document.getElementById("labelPessoaTemp").type == 'checkbox') {
			qs += "&labelPessoaTemp=" + document.getElementById("labelPessoaTemp").checked;
		} else {
			qs += "&labelPessoaTemp=" + document.getElementById("labelPessoaTemp").value;
		}
		if (document.getElementById("idEnderecoPessoa").type == 'checkbox') {
			qs += "&idEnderecoPessoaTmp=" + document.getElementById("idEnderecoPessoa").checked;
		} else {
			qs += "&idEnderecoPessoaTmp=" + document.getElementById("idEnderecoPessoa").value;
		}
		return qs;
	}
	
	function clearCepEndereco() {
		resetValue('nrCepLookup.nrCep');
		resetValue('nrCep');
		clearEndereco();
	}
	
	function paisOnChange() {
		var r = municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		clearCepEndereco();

		var idPais = getElementValue("municipio.unidadeFederativa.pais.idPais");
		if (idPais != undefined && idPais != "") {
			doClearCepMask();
		} else {
			setElementValue("blCepOpcional", "true");
		}
		
		return r;
	}
	
	function cepOnChange() {
		var r = nrCepLookup_cepCriteriaOnChangeHandler();
		if (getElementValue("nrCepLookup.cepCriteria") == "") {
			clearEndereco();
		}
		return r;
	}
	
	function paisOnDataLoadCallBack_cb(data, exception) {
		if (exception != undefined) {
			alert(exception);
			return;
		}
		var toReturn = municipio_unidadeFederativa_pais_nmPais_exactMatch_cb(data);
		if (data != undefined && data.length > 0) {
			setElementValue("blCepOpcional", data[0].blCepOpcional);
			doSetBlCepAlfanumerico();
		} else {
			doClearCepMask();
			//setFocus("municipio.unidadeFederativa.pais.nmPais");
		}
		return toReturn;
	}

	function paisOnPopupSetValue(data, exception) {
		__lookupSetValue({e:document.getElementById("municipio.unidadeFederativa.pais.idPais"), data:data});
		setElementValue("blCepOpcional", data.blCepOpcional);
		doSetBlCepAlfanumerico();
		return false;
	}

	/**
	  * Valida se o cep é opcional
	  */
	function validateBlCepOpcional(){
		var retorno = true;
		
		// Caso blCepOpcional seja false, o cep deve ser obrigatório.
		if (getElementValue('blCepOpcional') == 'false' && getElementValue("nrCepLookup.cepCriteria") == '') {
			alert(i18NLabel.getLabel("LMS-27094"));
			setFocus("nrCepLookup.cepCriteria");
			retorno = false;
		}
		
		return retorno;
	}
	
	function doSetBlCepAlfanumerico() {
		var idPais = getElementValue("municipio.unidadeFederativa.pais.idPais");
		if (idPais != undefined && idPais != "") {
			var data = new Array();
			setNestedBeanPropertyValue(data, "idPais", idPais);
			var sdo = createServiceDataObject("lms.configuracoes.manterEnderecoPessoaAction.findPaisByIdPais","doSetBlCepAlfanumerico",data);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	function doSetBlCepAlfanumerico_cb(data, exception) {
		if (exception != undefined) {
			alert(exception);
			return;
		}
		setElementValue("municipio.unidadeFederativa.pais.blCepAlfanumerico", getNestedBeanPropertyValue(data, "blCepAlfanumerico"));
		doSetCepMask();
	}
	
	function doSetCepMask() {
		if (getElementValue("municipio.unidadeFederativa.pais.blCepAlfanumerico") == "true") {
			document.getElementById("nrCepLookup.cepCriteria").dataType = "text";
			document.getElementById("nrCepLookup.cepCriteria").mask = "";
		} else {
			document.getElementById("nrCepLookup.cepCriteria").dataType = "integer";
			document.getElementById("nrCepLookup.cepCriteria").mask = "00000000";
		}
	}
	
	function doClearCepMask() {
		setElementValue("municipio.unidadeFederativa.pais.blCepAlfanumerico", "true");
		doSetCepMask();
	}
	
	function clearEndereco() {
		resetValue('_dsTipoLogradouro');
		resetValue('tipoLogradouro.idTipoLogradouro');
		resetValue('dsEndereco');
		resetValue('nrEndereco');
		resetValue('dsComplemento');
		resetValue('dsBairro');
		resetValue('municipio.idMunicipio');
		resetValue('municipio.unidadeFederativa.idUnidadeFederativa');
		resetValue('municipio.unidadeFederativa.sgUnidadeFederativa');
		resetValue('municipio.unidadeFederativa.nmUnidadeFederativa');
	}
	
	function onChangeDtVigenciaInicial(){
		// Atualiza o endereço substituído
		findEnderecoPessoa();	
	}
	
	function onChangeEnderecoSubstituido(object){
		// Caso esteja no option selecione, habilita a data final 
		if (object.selectedIndex == 0) {
			setDisabled("dtVigenciaFinal", true);
		// Caso não esteja no option selecione, desabilita a data final.
		} else {
			resetValue("dtVigenciaFinal");
			setDisabled("dtVigenciaFinal", true);
		}
		
	}
	
	function validaLatitude() {
		var nrLatitude = getElementValue("nrLatitude");
		if (nrLatitude != ""){
			var er = new RegExp("^(\-)?(?:90(?:(?:\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:(\,|.)[0-9]{1,18})?))$");  
	        if (!er.test(nrLatitude))  {
	        	resetValue("nrLatitude");
	        }
	        
	        if (nrLatitude > 90 || nrLatitude < -90){
				resetValue("nrLatitude");
			} 
		}
	}
	
	function validaLongitude() {
		var nrLongitude = getElementValue("nrLongitude");
		if (nrLongitude != ""){
			var er = new RegExp("^(\-)?(?:180(?:(?:\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:(\,|.)[0-9]{1,18})?))$");  
	        if (!er.test(nrLongitude))  {
	        	resetValue("nrLongitude");
	        }
	        
	        if (nrLongitude > 180 || nrLongitude < -180){
				resetValue("nrLongitude");
			} 
		}
	}
	
	function validaLatitudeTmp() {
		var nrLatitudeTmp = getElementValue("nrLatitudeTmp");
		if (nrLatitudeTmp != ""){
			var er = new RegExp("^(\-)?(?:90(?:(?:\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:(\,|.)[0-9]{1,18})?))$");  
	        if (!er.test(nrLatitudeTmp))  {
	        	resetValue("nrLatitudeTmp");
	        }
	        
	        if (nrLatitudeTmp > 90 || nrLatitudeTmp < -90){
				resetValue("nrLatitudeTmp");
			} 
		}
		onChangePreencherQualidadePadrao();
	}
	
	function validaLongitudeTmp() {
		var nrLongitudeTmp = getElementValue("nrLongitudeTmp");
		if (nrLongitudeTmp != ""){
			var er = new RegExp("^(\-)?(?:180(?:(?:\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:(\,|.)[0-9]{1,18})?))$");  
	        if (!er.test(nrLongitudeTmp))  {
	        	resetValue("nrLongitudeTmp");
	        }
	        
	        if (nrLongitudeTmp > 180 || nrLongitudeTmp < -180){
				resetValue("nrLongitudeTmp");
			} 
		}
		onChangePreencherQualidadePadrao();
	}
</script>
