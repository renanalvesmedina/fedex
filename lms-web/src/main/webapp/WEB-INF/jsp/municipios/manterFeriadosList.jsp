<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	// UF
	function verificaOnchangeUF(){
			
		var idPais = getElementValue("pais.idPais");
		var nmPais = getElementValue("pais.nmPais");
				
		unidadeFederativa_sgUnidadeFederativaOnChangeHandler();
		
		if (getElementValue("unidadeFederativa.sgUnidadeFederativa") == ''){
			
			setElementValue("pais.nmPais", nmPais);
			setElementValue("pais.idPais", idPais);
			
			resetValue("municipio.nmMunicipio");
		  	resetValue("municipio.idMunicipio");

		}
		return false;
	}	
	//-------------------------------------------------------
    
	// Pais
    function verificaOnchangePais(){
				
		pais_nmPaisOnChangeHandler();
		
		if (getElementValue("pais.nmPais") == ''){
			
			resetValue("unidadeFederativa.idUnidadeFederativa");
		  	resetValue("unidadeFederativa.nmUnidadeFederativa");
		  	resetValue("unidadeFederativa.sgUnidadeFederativa");
		  	
		  	resetValue("municipio.nmMunicipio");
		  	resetValue("municipio.idMunicipio");
		  	
		}
		return false;
	}
    
	//-------------------------------------------------------
    

	// Municipio
	function verificaOnchangeMunicipio(){
		
		var nmPais = getElementValue("pais.nmPais");
		var idPais = getElementValue("pais.idPais");;
		
		var idUnidadeFederativa = getElementValue("unidadeFederativa.idUnidadeFederativa");
		var nmUnidadeFederativa = getElementValue("unidadeFederativa.nmUnidadeFederativa");
		var sgUnidadeFederativa = getElementValue("unidadeFederativa.sgUnidadeFederativa");
		
		var r = municipio_nmMunicipioOnChangeHandler();
		
		if (getElementValue("municipio.nmMunicipio") == ''){
		
			setElementValue("pais.nmPais", nmPais);
			setElementValue("pais.idPais", idPais);
			
			setElementValue("unidadeFederativa.idUnidadeFederativa", idUnidadeFederativa);
		  	setElementValue("unidadeFederativa.nmUnidadeFederativa", nmUnidadeFederativa);
		  	setElementValue("unidadeFederativa.sgUnidadeFederativa", sgUnidadeFederativa);

		}
		return r;
	}
</script>

<adsm:window service="lms.municipios.feriadoService">
	<adsm:form idProperty="idFeriado" action="/municipios/manterFeriados">
		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
		</adsm:i18nLabels>
		<adsm:lookup onchange="return verificaOnchangeUF()" property="unidadeFederativa"  
					idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
				 	service="lms.municipios.unidadeFederativaService.findLookup" dataType="text" 
					width="7%" label="uf" size="2" maxLength="2" 
					action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="false">
			<adsm:propertyMapping criteriaProperty="pais.nmPais" modelProperty="pais.nmPais" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="pais.idPais" addChangeListener="false"/>
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="pais.nmPais" modelProperty="pais.nmPais"  blankFill="false"/>
			<adsm:propertyMapping relatedProperty="pais.idPais" modelProperty="pais.idPais" blankFill="false"/>
		</adsm:lookup>
		
		<adsm:textbox maxLength="60" dataType="text" property="unidadeFederativa.nmUnidadeFederativa" width="28%" size="19" serializable="false" disabled="true"/>
		
		<adsm:lookup onchange="return verificaOnchangePais()" property="pais" idProperty="idPais" 
					 criteriaProperty="nmPais" service="lms.municipios.paisService.findLookup" dataType="text" 
			         label="pais" size="30" minLengthForAutoPopUpSearch="3" maxLength="60" exactMatch="false"
			         action="/municipios/manterPaises" />
			         			         
		<adsm:lookup onchange="return verificaOnchangeMunicipio();" property="municipio" criteriaProperty="nmMunicipio" idProperty="idMunicipio" dataType="text" 
					 service="lms.municipios.municipioService.findLookup" 
					 label="municipio" size="30" action="/municipios/manterMunicipios" 
					 exactMatch="false" minLengthForAutoPopUpSearch="4" maxLength="60">
				<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="unidadeFederativa.pais.idPais"/>	
				<adsm:propertyMapping criteriaProperty="pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" inlineQuery="false"/> 
				<adsm:propertyMapping criteriaProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" addChangeListener="false"/>
	 			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" />
				<adsm:propertyMapping relatedProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" />
				<adsm:propertyMapping relatedProperty="unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" />
				<adsm:propertyMapping relatedProperty="pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="pais.idPais" modelProperty="unidadeFederativa.pais.idPais" blankFill="false"/>
		 </adsm:lookup>
		
		<adsm:textbox dataType="JTDate" property="dtFeriado" mask="dd/MM" label="diaMes" size="6" picker="false"/>
		
		<adsm:textbox dataType="text" property="dsFeriado" label="descricao" size="30" maxLength="60"/>
		
		<adsm:combobox property="tpFeriado" label="tipo" domain="DM_TIPO_FERIADO"/>
		
		<adsm:combobox property="blFacultativo" label="facultativo" domain="DM_SIM_NAO"/>
		
		<adsm:combobox property="abrangencia" label="abrangencia" domain="DM_ABRANGENCIA_FERIADO"/>
		
		<adsm:range label="vigencia"> 
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>	 
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="feriado" />
			<adsm:resetButton />
		</adsm:buttonBar>
		
	</adsm:form> 
	
	<adsm:grid idProperty="idFeriado" property="feriado" selectionMode="check" gridHeight="200" rows="9" unique="true">
		<adsm:gridColumn title="diaMes" mask="dd/MM" dataType="JTDate" property="dtFeriado" width="10%"/>
		<adsm:gridColumn title="descricao" property="dsFeriado" width="25%" />
		<adsm:gridColumn title="tipo" property="tpFeriado" isDomain="true" width="7%"/>
		<adsm:gridColumn title="abrangencia" property="abrangencia" width="12%"/>
		<adsm:gridColumn title="local" property="local" width="25%"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="10%"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="10%"/>
		
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window>

