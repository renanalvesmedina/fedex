<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.rodoviaService" >
	<adsm:form action="/municipios/manterRodovias" idProperty="idRodovia" >
		<adsm:hidden property="flag"/>
		<adsm:textbox dataType="text" property="sgRodovia" onchange="validaSigla()" label="rodovia" maxLength="10" size="10" />
		<adsm:textbox dataType="text" property="dsRodovia" label="nomeRodovia" maxLength="60" size="30" />

		<adsm:lookup property="pais" idProperty="idPais" 
			criteriaProperty="nmPais" service="lms.municipios.paisService.findLookup" dataType="text" 
			label="pais" size="30" minLengthForAutoPopUpSearch="3" maxLength="60" exactMatch="false"
			action="/municipios/manterPaises">
		</adsm:lookup>
		
		<adsm:lookup property="unidadeFederativa"  
					idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
				 	service="lms.municipios.unidadeFederativaService.findLookup" dataType="text" 
					width="7%" label="uf" size="3" maxLength="3"  
					action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="false">
			<adsm:propertyMapping criteriaProperty="pais.nmPais" modelProperty="pais.nmPais" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="pais.idPais" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="pais.nmPais" modelProperty="pais.nmPais" blankFill="false" />
			<adsm:propertyMapping relatedProperty="pais.idPais" modelProperty="pais.idPais" blankFill="false" />
		</adsm:lookup>
		
		<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" width="28%" size="30" serializable="false" disabled="true"/>
		

		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="rodovia" />
			<adsm:resetButton /> 
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idRodovia" property="rodovia" selectionMode="check"
			unique="true" rows="12"
			service="lms.municipios.manterRodoviasAction.findPaginatedCustom"
			rowCountService="lms.municipios.manterRodoviasAction.getRowCountCustom">
		<adsm:gridColumn title="rodovia" property="sgRodovia" width="10%" />
		<adsm:gridColumn title="nomeRodovia" property="dsRodovia" width="55%"/>		
		<adsm:gridColumn title="pais" property="pais.nmPais" width="20%" />
		<adsm:gridColumn title="uf" property="unidadeFederativa.sgUnidadeFederativa" width="5%"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" width="10%" isDomain="true" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	function validaSigla(){
		var sigla = document.getElementById("sgRodovia")
		sigla.value = sigla.value.toUpperCase();
	}

</script>
