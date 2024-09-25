<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	function manterEnderecoPessoaPageLoad_cb(dados, error){
		onPageLoad_cb(dados, error);
		if(error){
			alert(error);
			return false;
		}
		// Rotina que pega a referência da tela pai para usar parametros ou chamar funções
		// que serão usadas na tela filho.
		var doc;
		if (window.dialogArguments != undefined && window.dialogArguments.window != undefined) {
			doc = window.dialogArguments.window.document;
		} else {
		   doc = document;
		}
		// Pega parâmetros da tela pai.
		setElementValue("pessoa.idPessoa", doc.getElementById("pessoa.idPessoa").value);		
		setElementValue("pessoa.nmPessoa", doc.getElementById("pessoa.nmPessoa").value);
		
		var mapCriteria = new Array();
		setNestedBeanPropertyValue(mapCriteria, "tpIdentificacao", doc.getElementById("pessoa.tpIdentificacao").value);
		setNestedBeanPropertyValue(mapCriteria, "nrIdentificacao", doc.getElementById("pessoa.nrIdentificacao").value);
		
		var sdo = createServiceDataObject("lms.pendencia.abrirMDACadastrarEnderecoAction.formataNrIdentificacao", 
			"formataNrIdentificacao", mapCriteria);
		xmit({serviceDataObjects:[sdo]});	
		
		var sdo2 = createServiceDataObject("lms.pendencia.abrirMDACadastrarEnderecoAction.findPaisUsuarioLogado",
			"carregarPaisSession",new Array());
		xmit({serviceDataObjects:[sdo2]});
	}
	
</script>

<adsm:window title="cadastrarEnderecos" service="lms.pendencia.abrirMDACadastrarEnderecoAction" 
			 onPageLoadCallBack="manterEnderecoPessoaPageLoad">
	<adsm:form action="/pendencia/abrirMDA" idProperty="idEnderecoPessoa" onDataLoadCallBack="manterEnderecoPessoa">

		<adsm:hidden property="tpSituacaoAtivo" value="A" serializable="false"/>
		<adsm:hidden property="pessoa.idPessoa"/>
		
		<adsm:label key="branco" style="width:0"/>
				
		<adsm:textbox label="cliente" property="pessoa.nrIdentificacao" dataType="text" 
					  size="20" width="74%" maxLength="20"
					  disabled="true" serializable="false">
			<adsm:textbox property="pessoa.nmPessoa" dataType="text" 
						  size="60" disabled="true" serializable="false" />
		</adsm:textbox>
		
  		<adsm:combobox label="telefone" property="tipoTelefone" domain="DM_TIPO_TELEFONE" renderOptions="true"
  					   width="74%" required="true" onchange="return setaObrigatoriedade()" >

			<adsm:textbox property="nrDdd" dataType="integer"
						  size="5" maxLength="5" />
			<adsm:textbox property="nrTelefone" dataType="integer"
						  size="10" maxLength="10" />						  
  		</adsm:combobox>		

	 	<adsm:lookup label="pais" property="municipio.unidadeFederativa.pais" idProperty="idPais" dataType="text" criteriaProperty="nmPais"
			service="lms.pendencia.abrirMDACadastrarEnderecoAction.findLookupPais" size="37" maxLength="60" serializable="false" minLengthForAutoPopUpSearch="3"
			action="/municipios/manterPaises" required="true" exactMatch="false" width="74%">
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
		</adsm:lookup>

	 	<adsm:lookup label="cep" size="13" maxLength="8" dataType="text" allowInvalidCriteriaValue="true"
	 				 property="nrCepLookup" idProperty="nrCep" criteriaProperty="cepCriteria"
		 			 service="lms.pendencia.abrirMDACadastrarEnderecoAction.findLookupCep" 
					 action="/configuracoes/pesquisarCEP" onDataLoadCallBack="nrCepLookup" onPopupSetValue="popupNrCep"
					 required="true" serializable="false" >
			
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais" addChangeListener="false"
				modelProperty="municipio.unidadeFederativa.pais.nmPais" inlineQuery="true"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" addChangeListener="false" 
				modelProperty="municipio.unidadeFederativa.pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" inlineQuery="true"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" inlineQuery="true"/>
			<adsm:propertyMapping criteriaProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" inlineQuery="true"/>
			<adsm:propertyMapping criteriaProperty="dsBairro" modelProperty="nmBairro"/>
			<adsm:propertyMapping criteriaProperty="dsEndereco" modelProperty="nmLogradouro" inlineQuery="true"/>

						
			<adsm:propertyMapping relatedProperty="nrCep" modelProperty="nrCep" />
			<adsm:propertyMapping relatedProperty="dsBairro" modelProperty="nmBairro" />
			<adsm:propertyMapping relatedProperty="dsEndereco" modelProperty="nmLogradouro" />
			<adsm:propertyMapping relatedProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="municipio.nmMunicipio" modelProperty="municipio.nmMunicipio"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa"/>						
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa"	modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="municipio.unidadeFederativa.pais.idPais"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais"/>
			<adsm:propertyMapping relatedProperty="_dsTipoLogradouro" modelProperty="dsTipoLogradouro"/>

		</adsm:lookup>

		<adsm:hidden property="nrCep" serializable="true"/>
		<adsm:hidden property="_dsTipoLogradouro" serializable="false"/>
		
		<adsm:complement label="endereco" width="85%" required="true">
			<adsm:combobox property="tipoLogradouro.idTipoLogradouro" width="15%" service="lms.pendencia.abrirMDACadastrarEnderecoAction.findTipoLogradouro" boxWidth="110"
				optionLabelProperty="dsTipoLogradouro" optionProperty="idTipoLogradouro" onlyActiveValues="true" required="true"/>
			<adsm:textbox property="dsEndereco" dataType="text" size="96" maxLength="100" width="65%"/>
		</adsm:complement>

		<adsm:textbox label="numero" property="nrEndereco" dataType="text" size="13" maxLength="5" required="true"/>
		<adsm:textbox label="complemento" property="dsComplemento" dataType="text" size="40" maxLength="60"/>
		<adsm:textbox label="bairro" property="dsBairro" dataType="text" size="35" maxLength="60" required="true" width="74%"/>

		<adsm:lookup label="municipio" property="municipio" idProperty="idMunicipio" dataType="text" criteriaProperty="nmMunicipio"
			service="lms.pendencia.abrirMDACadastrarEnderecoAction.findLookupMunicipio" size="35" maxLength="60" onDataLoadCallBack="municipio"
			required="true" 
			action="/municipios/manterMunicipios" exactMatch="false" minLengthForAutoPopUpSearch="3">

			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais" 
				addChangeListener="false"/>
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />

			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" />
	
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.idUnidadeFederativa" 
				modelProperty="unidadeFederativa.idUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" 
				modelProperty="unidadeFederativa.sgUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" 
				modelProperty="unidadeFederativa.nmUnidadeFederativa" />
	
		</adsm:lookup>
		
		<adsm:hidden property="municipio.unidadeFederativa.idUnidadeFederativa" serializable="true"/>
		<adsm:textbox property="municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf"
			serializable="false" disabled="true" size="5">
				<adsm:textbox property="municipio.unidadeFederativa.nmUnidadeFederativa" dataType="text" 
					serializable="false" disabled="true" size="30"/>
		</adsm:textbox>

		<adsm:hidden property="enderecoCompleto" serializable="false"/>

		<adsm:buttonBar>		
			<adsm:storeButton callbackProperty="salvarRegistro"/>
			<adsm:button caption="limpar" id="cleanButton" disabled="false" onclick="limpaCampos()" />
			<adsm:button caption="fechar" id="closeButton" disabled="false" onclick="window.close()"/>			
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">	
	var PAIS_ID_PAIS = undefined;
	var PAIS_NM_PAIS = undefined;

	function carregarPaisSession_cb(data, error){
		if(error){
			alert(error);
			return false;
		}
	
		PAIS_ID_PAIS = data.idPais;
		PAIS_NM_PAIS = data.nmPais;
		setElementValue("municipio.unidadeFederativa.pais.idPais", PAIS_ID_PAIS);
		setElementValue("municipio.unidadeFederativa.pais.nmPais", PAIS_NM_PAIS);
	}

	function initWindow(eventObj) {
		setElementValue("municipio.unidadeFederativa.pais.idPais", PAIS_ID_PAIS);
		setElementValue("municipio.unidadeFederativa.pais.nmPais", PAIS_NM_PAIS);
		
		setDisabled("cleanButton", false);
		setDisabled("closeButton", false);
	}

	function setaObrigatoriedade() {		
		document.getElementById("nrDdd").label = document.getElementById("tipoTelefone").label;
		document.getElementById("nrDdd").required = "true";
		document.getElementById("nrTelefone").label = document.getElementById("tipoTelefone").label;
		document.getElementById("nrTelefone").required = "true";
		
		return true;
	}
	
	function salvarRegistro_cb(data, error, errorMsg, eventObj) {
		store_cb(data, error, errorMsg, eventObj);
		if(error){
			alert(error);
			return false;
		}
		
		var doc = window.dialogArguments.window;
		doc.carregaGrid();
		window.close();
	}	
	
	
	function limpaCampos() {
		var nrIdentificacao = getElementValue("pessoa.nrIdentificacao");
		var nmPessoa = getElementValue("pessoa.nmPessoa");
		
		newButtonScript();
		
		setElementValue("pessoa.nrIdentificacao", nrIdentificacao);
		setElementValue("pessoa.nmPessoa", nmPessoa);
	}
	
	function municipio_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
		lookupExactMatch({e:document.getElementById("municipio.idMunicipio"), data:data, callBack:'municipioLikeAndMatch'});
		var nmPais = getNestedBeanPropertyValue(( (data[0] != undefined) ? data[0] : data), "unidadeFederativa.pais.nmPais");
		if (nmPais != undefined && nmPais != document.getElementById("municipio.unidadeFederativa.pais.nmPais").value){
			municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		}
	}

	function municipioLikeAndMatch_cb(data, error){
		if(error){
			alert(error);
			return false;
		}
		lookupLikeEndMatch({e:document.getElementById("municipio.idMunicipio"), data:data});
		var nmPais = getNestedBeanPropertyValue(( (data[0] != undefined) ? data[0] : data), "unidadeFederativa.pais.nmPais");
		if (data == undefined) return;
		if (nmPais != undefined && nmPais != document.getElementById("municipio.unidadeFederativa.pais.nmPais").value){
			municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		}
	}

	function popupNrCep(data) {
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
		//alert('nao encontrado');
	}

	function nrCepLookup_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
	
		if (data.length <= 0){
			var cep = document.getElementById("nrCepLookup.cepCriteria").value;
			setElementValue("nrCep", cep);
			return;
		}
		
		nrCepLookup_cepCriteria_exactMatch_cb(data);
		var idTpLog = getIdTipoLogradouroByDs(getElementValue("_dsTipoLogradouro"));
		if (idTpLog != undefined){
			setElementValue("tipoLogradouro.idTipoLogradouro", idTpLog);
		}
		if (data.length > 1){
			lookupClickPicker({e:document.forms[0].elements['nrCepLookup.nrCep']});
		}

		var nmPais = getNestedBeanPropertyValue(data, "unidadeFederativa.pais.nmPais");
		if (nmPais != document.getElementById("municipio.unidadeFederativa.pais")){
			municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		}

	}

	function manterEnderecoPessoa_cb(data, error){
		if(error){
			alert(error);
			return false;
		}
	
		try{
			onDataLoad_cb(data, error);
			if (data.nrCep != undefined ){
				document.getElementById("nrCepLookup.cepCriteria").value = data.nrCep;
			}
		}catch(e){
			alert(e);
		}
	}
	
	function formataNrIdentificacao_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
	
		setElementValue("pessoa.nrIdentificacao", data._value);
	}
	
</script>
