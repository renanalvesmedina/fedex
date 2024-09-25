<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function limpaMunicipio(){	
	setElementValue(document.getElementById("municipio.idMunicipio"),"");
	setElementValue(document.getElementById("municipio.nmMunicipio"),"");
	unidadeFederativa_sgUnidadeFederativaOnChangeHandler();
}
</script>
<adsm:window service="lms.municipios.consultarMunicipiosAction" onPageLoadCallBack="setDisabledTabs">
	<adsm:form action="/municipios/consultarMunicipios" idProperty="idMunicipio">
	
		<adsm:lookup service="lms.municipios.consultarMunicipiosAction.findLookupMunicipio" dataType="text" property="municipio"
					criteriaProperty="nmMunicipio" idProperty="idMunicipio" label="municipio" size="30" maxLength="60" width="35%"
					action="/municipios/manterMunicipios" minLengthForAutoPopUpSearch="3" exactMatch="false" >
			<adsm:propertyMapping relatedProperty="unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="pais.idPais" modelProperty="unidadeFederativa.pais.idPais"/>
			<adsm:propertyMapping relatedProperty="pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais"/>
			<adsm:propertyMapping relatedProperty="nrCep" modelProperty="nrCep"/>
		</adsm:lookup>
		
		<adsm:textbox dataType="text" property="nrCep" label="cep" width="35%" size="8" maxLength="8"/>

		<adsm:lookup property="unidadeFederativa" idProperty="idUnidadeFederativa"
					criteriaProperty="sgUnidadeFederativa" label="uf" dataType="text"
					service="lms.municipios.consultarMunicipiosAction.findLookupUnidadeFederativa" width="35%" size="3"	maxLength="3"				
					action="/municipios/manterUnidadesFederativas" >
    		<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="pais.idPais" modelProperty="pais.idPais"/>
			<adsm:propertyMapping relatedProperty="pais.nmPais" modelProperty="pais.nmPais"/>
			<adsm:propertyMapping relatedProperty="municipio.nmMunicipio" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="municipio.idMunicipio" modelProperty=""/>
						
			<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa"  size="30" serializable="false" disabled="true"/>
		</adsm:lookup>
				
		
		<adsm:lookup property="pais" 
					idProperty="idPais" 
					service="lms.municipios.consultarMunicipiosAction.findLookupPais" 
					dataType="text" 
					criteriaProperty="nmPais" 
					label="pais" size="30" action="/municipios/manterPaises" exactMatch="false"
					minLengthForAutoPopUpSearch="3" width="35%" >
					<adsm:propertyMapping relatedProperty="municipio.nmMunicipio" modelProperty=""/>
					<adsm:propertyMapping relatedProperty="municipio.idMunicipio" modelProperty=""/>
					<adsm:propertyMapping relatedProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty=""/>
					<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty=""/>
					<adsm:propertyMapping relatedProperty="unidadeFederativa.idUnidadeFederativa" modelProperty=""/>
		</adsm:lookup>	
		
		<adsm:combobox domain="DM_SIM_NAO" property="blDistrito" label="indDistrito"  width="35%" disabled="false"/>	
		<adsm:lookup service="lms.municipios.consultarMunicipiosAction.findLookupMunicipio" dataType="text" property="municipioDistrito"
					criteriaProperty="nmMunicipio" idProperty="idMunicipio" label="municDistrito" size="30" maxLength="60" width="35%"
					action="/municipios/manterMunicipios" minLengthForAutoPopUpSearch="3" exactMatch="false" >
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa"/>
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="unidadeFederativa.pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" inlineQuery="false"/>
			
		</adsm:lookup>	
	
		<adsm:lookup idProperty="idFilial" property="filial" criteriaProperty="sgFilial"
					service="lms.municipios.consultarMunicipiosAction.findLookupFilial" dataType="text"
					label="filialResponsavel" size="5" maxLength="3" width="45%"
					action="/municipios/manterFiliais" minLengthForAutoPopUpSearch="3" exactMatch="true">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
		</adsm:lookup>
		

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="municipio" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idMunicipio" selectionMode="none" property="municipio" unique="true" rows="10" onRowClick="onRowClickMunicipio" detailFrameName="cad" service="lms.municipios.consultarMunicipiosAction.findPaginatedMunicipios" rowCountService="lms.municipios.consultarMunicipiosAction.getRowCountMunicipios">
		<adsm:gridColumn title="municipio" property="nmMunicipio" width="35%" />
		<adsm:gridColumn title="uf" property="unidadeFederativa.siglaDescricao" width="30%" />
		<adsm:gridColumn title="indDistrito" property="blDistrito" width="15%" renderMode="image-check"/>
		<adsm:gridColumn title="municDistrito" property="municipioDistrito.nmMunicipio" width="20%"/>
		<adsm:buttonBar/>
	</adsm:grid>
</adsm:window>
<script>

	function onRowClickMunicipio(){
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("cad", false);
		
		
	}
		
	function initWindow(eventObj){
		
		if(eventObj.name == 'tab_click')
			setDisabledTabsFilhas(true);
	}
		
	
	function setDisabledTabs_cb() {
	    onPageLoad_cb();
		//setDisabledTabsFilhas(true);
	}
	
	function setDisabledTabsFilhas(disabled) {
	    var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("cad", disabled);
		tabGroup.setDisabledTab("aten", disabled);
		tabGroup.setDisabledTab("feriados", disabled);
	}
</script>
