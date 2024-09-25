<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarPontosParada" service="lms.municipios.manterPontosParadaRotaAction">
	<adsm:form action="/municipios/manterPontosParadaRota" idProperty="idPontoParada" >
        <adsm:textbox dataType="text" property="nmPontoParada" size="40" maxLength="60" label="local" required="false" />
        
		<adsm:lookup service="lms.municipios.manterPontosParadaRotaAction.findLookupMunicipio" required="false" dataType="text" disabled="false" property="municipio" criteriaProperty="nmMunicipio" label="municipio" size="30" maxLength="30" action="/municipios/manterMunicipios" labelWidth="15%" width="85%" idProperty="idMunicipio" exactMatch="false" minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.siglaDescricao" modelProperty="unidadeFederativa.siglaDescricao" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais" />			
			<adsm:propertyMapping relatedProperty="municipio.nmMunicipio" modelProperty="nmMunicipio"/>
		</adsm:lookup>
		
		<adsm:textbox dataType="text" property="municipio.unidadeFederativa.siglaDescricao" label="uf" size="30" disabled="true" width="35%" serializable="false"/>
		<adsm:textbox dataType="text" property="municipio.unidadeFederativa.pais.nmPais" label="pais" disabled="true" size="30" width="35%" serializable="false"/>
		<adsm:hidden property="municipio.unidadeFederativa.pais.idPais" serializable="false"/>
		

		<adsm:combobox property="blAduana" label="aduana" domain="DM_SIM_NAO" width="35%"/>
		
		<adsm:lookup service="lms.municipios.manterPontosParadaRotaAction.findLookupRodovia" dataType="text" property="rodovia" idProperty="idRodovia" criteriaProperty="sgRodovia" label = "rodovia" size="10" maxLength="10" width="35%" action="/municipios/manterRodovias" >
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais" inlineQuery="false"/>			
			<adsm:propertyMapping relatedProperty="rodovia.dsRodovia" modelProperty="dsRodovia" />
			<adsm:textbox dataType="text" property="rodovia.dsRodovia" size="20" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="pontoParada"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form> 

	<adsm:grid property="pontoParada" idProperty="idPontoParada" selectionMode="check" gridHeight="200" unique="true" scrollBars="horizontal" rows="10" defaultOrder="municipio_.nmMunicipio, municipio_unidadeFederativa_.sgUnidadeFederativa, municipio_unidadeFederativa_pais_.nmPais, nmPontoParada">
		<adsm:gridColumn title="local" property="nmPontoParada" width="170" />
		<adsm:gridColumn title="municipio" property="municipio.nmMunicipio" width="150" />
		<adsm:gridColumn title="uf" property="municipio.unidadeFederativa.siglaDescricao" width="150" />
		<adsm:gridColumn title="pais" property="municipio.unidadeFederativa.pais.nmPais" width="150" />
		<adsm:gridColumn title="rodovia" property="rodovia.sgRodovia" width="80" />
		<adsm:gridColumn title="km" property="nrKm" width="60" mask="###,##" dataType="integer"/>
		<adsm:gridColumn title="aduana" property="blAduana"  renderMode="image-check" width="50" />
		<adsm:gridColumn title="sigla" property="sgAduana" width="80" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>