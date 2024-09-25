<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.coleta.manterLocalidadesEspeciaisAction" >
	<adsm:form action="/coleta/manterLocalidadesEspeciais" idProperty="idLocalidadeEspecial" onDataLoadCallBack="carregaDados" >
		<adsm:textbox dataType="text" property="dsLocalidade" label="descricao" size="62" maxLength="60" width="85%" required="true"/>
		<adsm:hidden property="dsAtivo" serializable="false" value="A"/>

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
		            property="pais" 
		            required="true" 
		            criteriaProperty="nmPais"
		            idProperty="idPais" 
		            exactMatch="false"
		            minLengthForAutoPopUpSearch="3" 
		            label="pais" 
		            maxLength="40"
		            onPopupSetValue="retornoLookupPais"
		            onchange="return pais_OnChange()"
		            action="/municipios/manterPaises">
			<adsm:propertyMapping criteriaProperty="dsAtivo" modelProperty="tpSituacao"/>
		</adsm:lookup>

		<adsm:combobox property="unidadeFederativa.idUnidadeFederativa" 
					   optionProperty="idUnidadeFederativa" 
					   optionLabelProperty="sgUnidadeFederativa" 
					   label="uf" width="85%" required="true" />
		
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true" renderOptions="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

/**
 * Função chamada ao entrar na tela
 */
function initWindow(eventObj) {
	var event = eventObj.name
	if (event != "gridRow_click" && event != "storeButton") { 
		limpaComboUf();
		setDisabled('unidadeFederativa.idUnidadeFederativa', true);
		setDisabled('pais.idPais', false);
	}
}

/**
 * Chamada ao retornar do carregamento dos dados
 */
function carregaDados_cb(data, error) {
	onDataLoad_cb(data, error);
	if ( getElementValue("filial.idFilial") != '') {
		setDisabled('unidadeFederativa.idUnidadeFederativa', true);
		setDisabled('pais.idPais', true);
	} else {
		setDisabled('unidadeFederativa.idUnidadeFederativa', false);
		setDisabled('pais.idPais', false);

		var arrayUnidadesFederativas = getNestedBeanPropertyValue(data,"unidadesFederativas");
		var idUnidadeFederativaDefault = getNestedBeanPropertyValue(data,"unidadeFederativa.idUnidadeFederativa");
		populaComboUnidadeFederativa(arrayUnidadesFederativas);
		document.getElementById("unidadeFederativa.idUnidadeFederativa").value = idUnidadeFederativaDefault;
	}
	setFocusOnFirstFocusableField();
}

function populaComboUnidadeFederativa(arrayUnidadesFederativas) {
	limpaComboUf();
	var combo = document.getElementById("unidadeFederativa.idUnidadeFederativa");
	for (i=0; i< arrayUnidadesFederativas.length; i++) {
		combo.options[i+1] = new Option(arrayUnidadesFederativas[i].sgUnidadeFederativa, arrayUnidadesFederativas[i].idUnidadeFederativa);
	}
}

/**
 * Função para tratar o evento OnChange da lookup de Filial
 */
function filial_OnChange() {
	if ( getElementValue('filial.sgFilial') == '' ) {
		setDisabled('unidadeFederativa.idUnidadeFederativa', true);
		setDisabled('pais.idPais', false);
		resetValue(document.getElementById('unidadeFederativa.idUnidadeFederativa'));
		resetValue(document.getElementById('pais.idPais'));
		limpaComboUf();
	}
	return filial_sgFilialOnChangeHandler();
}

/**
 * Função para tratar o evento OnChange da lookup de Pais
 */
function pais_OnChange() {
	if ( getElementValue('pais.nmPais') == '' ) {
		setDisabled('unidadeFederativa.idUnidadeFederativa', true);
		limpaComboUf();
		resetValue(document.getElementById('unidadeFederativa.idUnidadeFederativa'));
		resetValue(document.getElementById('pais.idPais'));
	}
	return pais_nmPaisOnChangeHandler();
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
	var x = lookupExactMatch({e:document.getElementById("pais.idPais"), data:data, callBack:"retornoPaisLikeEnd"});
	if (x == true){
		buscarUfsByIdPais(getElementValue("pais.idPais"));
	}
	return x;
}

function retornoPaisLikeEnd_cb(data) {
	return retornoPais_default(pais_nmPais_likeEndMatch_cb(data));
}

function retornoPais_default(flag) {
	if (flag == true) {
		buscarUfsByIdPais(getElementValue("pais.idPais"));
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
			
			document.getElementById("pais.idPais").value = idPais;
			document.getElementById("pais.nmPais").value = nmPais;
			setDisabled('pais.idPais', true);

			adicionaItemCombo(idUnidadeFederativa, nmUnidadeFederativa);
			
			document.getElementById("unidadeFederativa.idUnidadeFederativa").value = idUnidadeFederativa;
			
			setDisabled('unidadeFederativa.idUnidadeFederativa', true);
			setFocus('tpSituacao');
		} else {
			setDisabled('unidadeFederativa.idUnidadeFederativa', true);
			setDisabled('pais.idPais', false);
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
