<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function manterEnderecoPessoaPageLoad_cb(dados,errors){
		onPageLoad_cb(dados,errors);
		carregaLabelPessoa();
		initPage();
	}
	
	/**
 	* Function que carrega os valores padrões da tela
 	*
 	* chamado desabilitaTodosCampos
 	*/
	function initPage(){
		
		_serviceDataObjects = new Array();
		
		var sdo = createServiceDataObject("lms.configuracoes.manterEnderecoPessoaAction.findPaisUsuarioLogadoDataAtual",
			                              "carregarPaisSession",new Array());

		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
 	* Function que trata o retorno do xmit que busco o pais da sessao
 	*
 	* chamado por: initPage
 	*/
	function carregarPaisSession_cb(data,erro){
		setElementValue("municipio.unidadeFederativa.pais.nmPais", getNestedBeanPropertyValue(data, "nmPais"));
		setElementValue("municipio.unidadeFederativa.pais.idPais", getNestedBeanPropertyValue(data, "idPais"));
	}
	
	function findDados(dados,errors) {

		//necessario para manter a funcionalidade da tela de manterPessoas
		onPageLoad(dados,errors);
		
		var u = new URL(parent.location.href);
		
		var isTelaLocMerc = u.parameters["isTelaLocMerc"];
   		var idCliente = u.parameters["idCliente"];
   		var nrIdentificacao  = u.parameters["nrIdentificacao"];
   		var nmPessoa = u.parameters["nmPessoa"];
   		var tipoCliente = u.parameters["tipoCliente"];
   		
   		if(idCliente!= null){
			setElementValue("pessoa.idPessoa",idCliente);
			document.getElementById("pessoa.idPessoa").masterLink=true;
		}	
		if(nrIdentificacao!= null){
			setElementValue("pessoa.nrIdentificacao",nrIdentificacao);
			document.getElementById("pessoa.nrIdentificacao").masterLink=true;
		}	
        if(nmPessoa!= null){
        	setElementValue("pessoa.nmPessoa",nmPessoa);
        	document.getElementById("pessoa.nmPessoa").masterLink=true;
        }	
        if(tipoCliente != null)	
			setElementValue("labelPessoaTemp",tipoCliente);
		carregaLabelPessoa();
		
		if(isTelaLocMerc != null){
			setElementValue("isTelaLocMerc",isTelaLocMerc);
			document.getElementById("isTelaLocMerc").masterLink=true;
		}
   }
   
   function callBackGrid_cb(data,errorMessage){
 	   if(getElementValue("isTelaLocMerc") != ""){
 		    enderecoPessoaGridDef.disabled = true;
 		    setDisabled(document.getElementById("enderecoPessoa"+".form"),true);
 	   	
 	   }     
   }
   
   
   


 

	
</script>
<adsm:window service="lms.configuracoes.manterEnderecoPessoaAction" onPageLoadCallBack="manterEnderecoPessoaPageLoad" onPageLoad="findDados" >
	<adsm:form action="/configuracoes/manterEnderecoPessoa" onDataLoadCallBack="manterEnderecoPessoa">

		<adsm:hidden property="onlyActives" value="False"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>
		<adsm:hidden property="pessoa.idPessoa"/>
		<adsm:hidden property="isTelaLocMerc"/>
		
		<adsm:label key="branco" style="width:0"/>
		<td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>
		<adsm:textbox property="pessoa.nrIdentificacao" dataType="text" size="20" width="13%" maxLength="20"
			disabled="true" serializable="false">
			<adsm:textbox property="pessoa.nmPessoa" dataType="text" size="60" disabled="true" serializable="false" width="74%" />
		</adsm:textbox>			

	 	<adsm:lookup label="pais" property="municipio.unidadeFederativa.pais" idProperty="idPais" dataType="text" criteriaProperty="nmPais"
			service="lms.configuracoes.manterEnderecoPessoaAction.findLookupPais" size="40" maxLength="60" minLengthForAutoPopUpSearch="3"
			action="/municipios/manterPaises" onchange="return onChangePais(this);" exactMatch="false"/>

	 	<adsm:lookup label="cep" 
	 	             property="nrCepLookup" 
	 	             dataType="text" 
	 	             idProperty="nrCep" 
	 	             criteriaProperty="cepCriteria" 
	 	             serializable="false"
	 	             service="lms.configuracoes.manterEnderecoPessoaAction.findCepLookup" 
	 	             size="20" maxLength="8" exactMatch="true"
	 	             action="/configuracoes/pesquisarCEP" 
	 	             onchange="return onChangeCep(this);"
	 	             onDataLoadCallBack="nrCepLookup" 
	 	             onPopupSetValue="popupNrCep" 
	 	             allowInvalidCriteriaValue="true">			
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais"  
			                      modelProperty="municipio.unidadeFederativa.pais.nmPais" inlineQuery="true"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" 
			                      addChangeListener="false"  
								  modelProperty="municipio.unidadeFederativa.pais.idPais"/>				
				
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" inlineQuery="true"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" inlineQuery="true"/>

			<adsm:propertyMapping criteriaProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" inlineQuery="true"/>
			
			<adsm:propertyMapping criteriaProperty="dsBairro" modelProperty="nmBairro"/>
			<adsm:propertyMapping criteriaProperty="onlyActives" modelProperty="onlyActives"/>
			
			<adsm:propertyMapping relatedProperty="nrCep" modelProperty="nrCep"/>
			<adsm:propertyMapping relatedProperty="dsBairro" modelProperty="nmBairro"/>
			<adsm:propertyMapping relatedProperty="dsEndereco" modelProperty="nmLogradouro"/>
			<adsm:propertyMapping relatedProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="municipio.nmMunicipio" modelProperty="municipio.nmMunicipio"/>
			<adsm:propertyMapping relatedProperty="_dsTipoLogradouro" modelProperty="dsTipoLogradouro"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="municipio.unidadeFederativa.pais.idPais" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais" blankFill="false"/>
			
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" 
							      modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.idUnidadeFederativa" 
								  modelProperty="municipio.unidadeFederativa.idUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" 
								  modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" />
		</adsm:lookup>
		<adsm:hidden property="nrCep" serializable="true"/>
		<adsm:hidden property="_dsTipoLogradouro" serializable="false"/>

		<adsm:complement label="endereco" width="85%">
			<adsm:combobox property="tipoLogradouro.idTipoLogradouro" width="15%" service="lms.configuracoes.manterEnderecoPessoaAction.findLookupTipoLogradouro" boxWidth="75"
				optionLabelProperty="dsTipoLogradouro" optionProperty="idTipoLogradouro"/>
			<adsm:textbox property="dsEndereco" dataType="text" size="93" maxLength="100" width="65%"/>
		</adsm:complement>

		<adsm:textbox label="numero" property="nrEndereco" dataType="text" size="10" maxLength="5"/>
		<adsm:textbox label="complemento" property="dsComplemento" dataType="text" size="35" maxLength="60"/>
		<adsm:textbox label="bairro" property="dsBairro" dataType="text" size="40" maxLength="60"/>

		<adsm:lookup label="municipio" property="municipio" idProperty="idMunicipio" dataType="text" criteriaProperty="nmMunicipio"
			service="lms.configuracoes.manterEnderecoPessoaAction.findLookupMunicipio" size="35" maxLength="60" onDataLoadCallBack="municipio"
			onchange="return municipio_onChange();"
			action="/municipios/manterMunicipios" exactMatch="false" minLengthForAutoPopUpSearch="3">

			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais" 
				addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" 
				addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais"
				blankFill="false"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais"
				blankFill="false" />
		</adsm:lookup>

	 	<adsm:lookup label="uf" property="municipio.unidadeFederativa" idProperty="idUnidadeFederativa" 
	 		dataType="text" criteriaProperty="sgUnidadeFederativa"
			service="lms.configuracoes.manterEnderecoPessoaAction.findLookupUnidadeFederativa"
			size="3" maxLength="3" 
			minLengthForAutoPopUpSearch="3"	action="/municipios/manterUnidadesFederativas" exactMatch="true">
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="pais.idPais" inlineQuery="true" copyPopup="true"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:textbox property="municipio.unidadeFederativa.nmUnidadeFederativa" dataType="text" serializable="false" size="32" disabled="true"/>
		</adsm:lookup>

		<adsm:textbox label="vigencia" dataType="JTDate" property="dtVigencia" size="10"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="enderecoPessoa"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid rows="9" property="enderecoPessoa" idProperty="idEnderecoPessoa" gridHeight="200" 
		defaultOrder="tipoLogradouro_.dsTipoLogradouro, dsEndereco, nrEndereco, dsComplemento" onDataLoadCallBack="callBackGrid">
		<adsm:gridColumn width="20%" title="endereco" property="enderecoCompleto"/>
		<adsm:gridColumn width="10%" title="bairro" property="dsBairro"/>
		<adsm:gridColumn width="10%" title="cidade" property="municipio.nmMunicipio"/>
		<adsm:gridColumn width="5%" title="uf" property="municipio.unidadeFederativa.sgUnidadeFederativa"/>
		<adsm:gridColumn width="12%" title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn width="12%" title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:gridColumn width="13%" title="usuarioInclusao" property="usuarioInclusao.nmUsuario"/>
		<adsm:gridColumn width="14%" title="usuarioAlteracao" property="usuarioAlteracao.nmUsuario"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
<script>

	var PAIS_NM_PAIS = undefined;
	var PAIS_ID_PAIS = undefined;

	function carregaLabelPessoa(){
		var labelPessoa = getElementValue("labelPessoaTemp");
		if (labelPessoa != ""){
			document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
		}
	}

	function municipio_cb(data) {
		lookupExactMatch({e:document.getElementById("municipio.idMunicipio"), data:data, callBack:'municipioLikeAndMatch'});
		var nmPais = getNestedBeanPropertyValue(( (data[0] != undefined) ? data[0] : data), "unidadeFederativa.pais.nmPais");
		if (nmPais != document.getElementById("municipio.unidadeFederativa.pais.nmPais").value){
			municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		}
	}

	function municipioLikeAndMatch_cb(data){
		lookupLikeEndMatch({e:document.getElementById("municipio.idMunicipio"), data:data});
		var nmPais = getNestedBeanPropertyValue(( (data[0] != undefined) ? data[0] : data), "unidadeFederativa.pais.nmPais");
		if (nmPais != document.getElementById("municipio.unidadeFederativa.pais.nmPais").value){
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

	function municipio_onChange(){
		var retorno = municipio_nmMunicipioOnChangeHandler();
		return retorno;
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

	function manterEnderecoPessoa_cb(dados, erros){
		try{
			onDataLoad_cb(dados, erros);
			carregaLabelPessoa();
			if (dados.nrCep != undefined ){
				document.getElementById("nrCepLookup.cepCriteria").value = dados.nrCep;
			}
		}catch(e){
			alert(e);
		}
	}
	
	function initWindow(evento){
		if( evento.name == 'cleanButton_click' ){
			initPage();
		}
	}
	
	function onChangeCep(object){
		var r = nrCepLookup_cepCriteriaOnChangeHandler();
		if (getElementValue("nrCepLookup.cepCriteria") == "") {
			clearEndereco();
		}
		return r;
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
	
	function clearCepEndereco() {
		resetValue('nrCepLookup.nrCep');
		resetValue('nrCep');
		clearEndereco();
	}
	
	function onChangePais(object){
		var retorno = municipio_unidadeFederativa_pais_nmPaisOnChangeHandler();
		clearCepEndereco();
			
		return retorno;
	}
</script>