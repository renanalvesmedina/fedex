<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.manterLocalidadesEspeciaisAction" >
	<adsm:form action="/coleta/manterLocalidadesEspeciais" idProperty="idLocalidadeEspecial" >
		<adsm:textbox dataType="text" property="dsLocalidade" label="descricao" size="62" maxLength="60" width="85%" />
	
		<adsm:lookup dataType="text" 
					property="filial"
					idProperty="idFilial"
					criteriaProperty="sgFilial" 
					 service="lms.coleta.manterLocalidadesEspeciaisAction.findFilialLookup" 
					 action="/municipios/manterFiliais" 
					onchange="return filial_OnChange()"
					onDataLoadCallBack="retornoFilial"
					onPopupSetValue="retornoLookupFilial"
					 label="filialDestino" size="3" maxLength="3" width="85%" serializable="true" >
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="50" maxLength="50" serializable="false" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup width="85%"
		            dataType="text"
		            service="lms.municipios.manterPaisesAction.findLookup" 
		            onDataLoadCallBack="retornoPais"
		            property="unidadeFederativa.pais" 
		            required="false" 
		            criteriaProperty="nmPais"
		            idProperty="idPais" 
		            exactMatch="false"
		            minLengthForAutoPopUpSearch="3" 
		            label="pais" 
		            maxLength="40"
		            onPopupSetValue="retornoLookupPais"
		            onchange="return pais_OnChange()"
		            action="/municipios/manterPaises">
		</adsm:lookup>

		<adsm:combobox property="unidadeFederativa.idUnidadeFederativa" 
					optionProperty="idUnidadeFederativa"
					optionLabelProperty="sgUnidadeFederativa" 
					   label="uf" width="85%" />

		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" renderOptions="true"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="localidades"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="localidades" idProperty="idLocalidadeEspecial" defaultOrder="dsLocalidade:asc" selectionMode="check" gridHeight="200" unique="true">
		<adsm:gridColumn property="dsLocalidade" title="descricao" width="40%" />
		<adsm:gridColumn property="pais.nmPais" title="pais" width="20%" />
		<adsm:gridColumn property="filial.sgFilial" title="filialDestino" width="18%" />
		<adsm:gridColumn property="unidadeFederativa.sgUnidadeFederativa" title="uf" width="12%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

/**
 * Função chamada ao entrar na tela
 */
function initWindow(eventObj) {
	var event = eventObj.name
	if (event == "cleanButton_click") { 
		setDisabled('unidadeFederativa.idUnidadeFederativa', true);
		setDisabled('unidadeFederativa.pais.idPais', false);
	}
}

/**
 * Função para tratar o evento OnChange da lookup de Filial
 */
function filial_OnChange() {
	if ( getElementValue('filial.sgFilial') == '' ) {
		setDisabled('unidadeFederativa.idUnidadeFederativa', true);
		setDisabled('unidadeFederativa.pais.idPais', false);
		resetValue(document.getElementById('unidadeFederativa.idUnidadeFederativa'));
		resetValue(document.getElementById('unidadeFederativa.pais.idPais'));
		limpaComboUf();
	}
	return filial_sgFilialOnChangeHandler();
}

/**
 * Função para tratar o evento OnChange da lookup de Pais
 */
function pais_OnChange() {
	if ( document.getElementById('unidadeFederativa.pais.nmPais').value == '' ) {
		setDisabled('unidadeFederativa.idUnidadeFederativa', true);
		limpaComboUf();
		resetValue(document.getElementById('unidadeFederativa.idUnidadeFederativa'));
		resetValue(document.getElementById('unidadeFederativa.pais.idPais'));
	}
	return unidadeFederativa_pais_nmPaisOnChangeHandler();
}

function limpaComboUf() {
	var combo = document.getElementById("unidadeFederativa.idUnidadeFederativa");
	for(var i = combo.options.length; i >= 1; i--) {
		combo.options.remove(i);
	}
	combo.selectedIndex = 0
}

function adicionaItemCombo(idItem, nmItem){
	limpaComboUf();
	var combo = document.getElementById("unidadeFederativa.idUnidadeFederativa");
	combo.options[combo.options.length] = new Option(nmItem, idItem); 
}


/**
 * Callback da lookup de Filial
 */
function retornoFilial_cb(data) {
	filial_sgFilial_exactMatch_cb(data);
	if ( getElementValue("filial.idFilial") != '') {
		resetValue('unidadeFederativa.idUnidadeFederativa');
		buscarUfByIdFilial(getElementValue("filial.idFilial"));
		//TODO buscarPais
	} else {
		setDisabled('unidadeFederativa.idUnidadeFederativa', false);
	}
}

/**
 * Callback da lookup de Pais
 */
function retornoPais_cb(data) {
	var x = lookupExactMatch({e:document.getElementById("unidadeFederativa.pais.idPais"), data:data, callBack:"retornoPaisLikeEnd"});
	if (x == true){
		buscarUfsByIdPais(getElementValue("unidadeFederativa.pais.idPais"));
	}
	return x;
}

function retornoPaisLikeEnd_cb(data) {
	return retornoPais_default(unidadeFederativa_pais_nmPais_likeEndMatch_cb(data));
}

function retornoPais_default(flag) {
	if (flag == true) {
		buscarUfsByIdPais(getElementValue("unidadeFederativa.pais.idPais"));
	}
	return flag;
}

/**
 * Função para buscar a UF a partir de um idFilial
 */
function buscarUfByIdFilial(idFilial) {
	var sdo = createServiceDataObject("lms.coleta.manterLocalidadesEspeciaisAction.findEnderecoPessoa", "resultado_buscarUfByIdFilial", 
	{ idPessoa:idFilial });
    xmit({serviceDataObjects:[sdo]});
}

/**
 * Função para buscar as UFs a partir de um idPais
 */
function buscarUfsByIdPais(idPais) {
	var sdo = createServiceDataObject("lms.coleta.manterLocalidadesEspeciaisAction.findUfsByPais", "resultado_buscarUfsByIdPais", 
	{ idPais:idPais });
    xmit({serviceDataObjects:[sdo]});
}

/**
 * Callback do método buscarUfByIdFilial
 */
function resultado_buscarUfByIdFilial_cb(data, error) {
	if (data != undefined) {
		if (getNestedBeanPropertyValue(data,"unidadeFederativa.idUnidadeFederativa") != undefined) {

			var idPais = getNestedBeanPropertyValue(data,"unidadeFederativa.pais.idPais");
			var nmPais = getNestedBeanPropertyValue(data,"unidadeFederativa.pais.nmPais");
			var idUnidadeFederativa = getNestedBeanPropertyValue(data,"unidadeFederativa.idUnidadeFederativa");
			var nmUnidadeFederativa = getNestedBeanPropertyValue(data,"unidadeFederativa.sgUnidadeFederativa");
			
			document.getElementById("unidadeFederativa.pais.idPais").value = idPais;
			document.getElementById("unidadeFederativa.pais.nmPais").value = nmPais;
			setDisabled('unidadeFederativa.pais.idPais', true);

			adicionaItemCombo(idUnidadeFederativa, nmUnidadeFederativa);
			document.getElementById("unidadeFederativa.idUnidadeFederativa").value = idUnidadeFederativa;
			
			setDisabled('unidadeFederativa.idUnidadeFederativa', true);
			setFocus('tpSituacao');
		} else {
			setDisabled('unidadeFederativa.idUnidadeFederativa', true);
			setDisabled('unidadeFederativa.pais.idPais', false);
		}
	}
}

/**
 * Callback do método buscarUfsByIdPais
 */
function resultado_buscarUfsByIdPais_cb(data, error) {
 	if (data != undefined) {
 		unidadeFederativa_idUnidadeFederativa_cb(data);
 		setDisabled('unidadeFederativa.idUnidadeFederativa', false);
 		setFocus('unidadeFederativa.idUnidadeFederativa');
	}
}
  
/**
 * Retorno da popup de Filial
 */
function retornoLookupFilial(data) {
	var idFilial = getNestedBeanPropertyValue(data,"idFilial");
	if ( idFilial != undefined && idFilial != '') {
		resetValue('unidadeFederativa.idUnidadeFederativa');
		buscarUfByIdFilial(idFilial);
	} else { 
		setDisabled('unidadeFederativa.idUnidadeFederativa', false);
	}
}

/**
 * Retorno da popup de Pais
 */
function retornoLookupPais(data) {
	var idPais = getNestedBeanPropertyValue(data,"idPais");
	if ( idPais != undefined && idPais != '') {
		resetValue('unidadeFederativa.idUnidadeFederativa');
		buscarUfsByIdPais(idPais);
	} else { 
		setDisabled('unidadeFederativa.idUnidadeFederativa', false);
	}
}
</script>
