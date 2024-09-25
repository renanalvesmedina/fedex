<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.unidadeFederativaService">
	<adsm:form action="/municipios/manterUnidadesFederativas" idProperty="idUnidadeFederativa" height="100">
		<adsm:hidden property="municipios.idMunicipio"/>
		<adsm:lookup property="pais"  service="lms.municipios.paisService.findLookup" dataType="text" criteriaProperty="nmPais" label="pais" size="30" action="/municipios/manterPaises" labelWidth="16%" width="32%" idProperty="idPais" exactMatch="false" minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping relatedProperty="pais.tpSituacao.description" modelProperty="tpSituacao.description"/>
	    </adsm:lookup>	  
		<adsm:textbox dataType="text" property="pais.tpSituacao.description" label="situacao" labelWidth="16%" width="34%" disabled="true" serializable="false"/>
		 
		<adsm:combobox property="regiaoGeografica.idRegiaoGeografica" label="regiaoGeografica" service="lms.municipios.regiaoGeograficaService.find" optionLabelProperty="dsRegiaoGeografica" optionProperty="idRegiaoGeografica" labelWidth="16%" width="84%" boxWidth="170" onlyActiveValues="false"/>
		<adsm:textbox dataType="text" property="sgUnidadeFederativa" onchange="validaSigla()" label="siglaUf" size="3" maxLength="3" labelWidth="16%" width="32%"/>
		<adsm:textbox dataType="text" property="nmUnidadeFederativa" label="nomeUf"  size="30" maxLength="60" labelWidth="16%" width="36%"/>

		<adsm:lookup
			label="capital"
			property="municipio"
			criteriaProperty="nmMunicipio"
			idProperty="idMunicipio"
			service="lms.municipios.municipioService.findLookup"
			action="/municipios/manterMunicipios"
			dataType="text"
			size="30"
			maxLength="30"
			labelWidth="16%"
			width="84%"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"
		>
			<adsm:propertyMapping criteriaProperty="pais.idPais"  modelProperty="unidadeFederativa.pais.idPais" />
			<adsm:propertyMapping criteriaProperty="pais.nmPais"  modelProperty="unidadeFederativa.pais.nmPais" />
	  </adsm:lookup>

		<adsm:combobox property="blIcmsPedagio" label="icmsPedagio" domain="DM_SIM_NAO" labelWidth="16%" width="32%" />
		<adsm:combobox property="blFronteiraRapida" label="fronteiraRapida" domain="DM_SIM_NAO" labelWidth="16%" width="36%" />
		<adsm:combobox property="blCobraTas" label="cobraTAS" domain="DM_SIM_NAO" labelWidth="16%" width="32%" />
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="16%" width="34%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="unidadeFederativa"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idUnidadeFederativa" property="unidadeFederativa" unique="true"  selectionMode="check" scrollBars="horizontal"  rows="9" gridHeight="180" defaultOrder="pais_.nmPais, sgUnidadeFederativa, nmUnidadeFederativa">
		<adsm:gridColumn title="siglaUf" property="sgUnidadeFederativa" width="90" />
		<adsm:gridColumn title="nomeUf" property="nmUnidadeFederativa" width="130" />
		<adsm:gridColumn title="pais" property="pais.nmPais" width="100" />
		<adsm:gridColumn title="situacao" property="pais.tpSituacao" width="80" isDomain="true"/>
		<adsm:gridColumn title="regiao" property="regiaoGeografica.dsRegiaoGeografica" width="130" />
		<adsm:gridColumn title="capital" property="municipio.nmMunicipio" width="130" />
		<adsm:gridColumn title="ICMS" property="blIcmsPedagio" width="50"  renderMode="image-check"/>
		<adsm:gridColumn title="TAS" property="blCobraTas" width="50"  renderMode="image-check"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" width="80" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	function validaSigla(){
		var sigla = document.getElementById("sgUnidadeFederativa")
		sigla.value = sigla.value.toUpperCase();
	}

</script>

