<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.rodoviaService" >
	<adsm:form action="/municipios/manterRodovias" idProperty="idRodovia" >
		<adsm:textbox dataType="text" property="sgRodovia" label="rodovia" maxLength="10" size="10" onchange="validaSigla();" required="true" />
		<adsm:textbox dataType="text" property="dsRodovia" label="nomeRodovia" maxLength="60" size="30" />
		
		<adsm:lookup property="pais" idProperty="idPais" 
					 criteriaProperty="nmPais" service="lms.municipios.paisService.findLookup" dataType="text" 
			         label="pais" size="30" minLengthForAutoPopUpSearch="3" maxLength="60" exactMatch="false"
			         action="/municipios/manterPaises" required="true">
			<adsm:propertyMapping criteriaProperty="pais.tpSituacao" modelProperty="tpSituacao" />
		</adsm:lookup>
		
		<adsm:hidden property="pais.tpSituacao" value="A" serializable="false"/>
		
		<adsm:lookup property="unidadeFederativa"  
					idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
				 	service="lms.municipios.unidadeFederativaService.findLookup" dataType="text" 
					width="7%" label="uf" size="2" maxLength="2"
					action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="false">
			<adsm:propertyMapping criteriaProperty="pais.nmPais" modelProperty="pais.nmPais" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="pais.idPais" />
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="pais.nmPais" modelProperty="pais.nmPais" blankFill="false" />
			<adsm:propertyMapping relatedProperty="pais.idPais" modelProperty="pais.idPais" blankFill="false" />
		</adsm:lookup>
		
		<adsm:hidden property="unidadeFederativa.tpSituacao" value="A" serializable="false" /> 
		
		<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" width="28%" size="30" serializable="false" disabled="true"/>
		

		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true"/>
	<adsm:buttonBar>
			<adsm:storeButton callbackProperty="storeRodovia" />
			<adsm:newButton /> 		
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window> 
<script language="javascript">

	function storeRodovia_cb(data, errorMsg, errorKey){
		var showError = true;
		if (errorMsg != null && errorKey == 'uniqueConstraintViolated'){
			alert(errorMsg);
			setFocusOnFirstFocusableField(document);
			showError = false;
		}
	
		store_cb(data, errorMsg, errorKey, showError);
		
	}

	function validaSigla(){
	var sigla = document.getElementById("sgRodovia")
	sigla.value = sigla.value.toUpperCase();
	}

</script>