<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.manterPostosControleAction">
	<adsm:form action="/sgr/manterPostosControle">
		
		<adsm:textbox dataType="text" property="nmPostoControlePassaporte" label="nomePosto" maxLength="50" width="55%" size="45%"/>
		
		<adsm:lookup dataType="text" property="reguladoraSeguro" idProperty="idReguladora" 
					 criteriaProperty="pessoa.nrIdentificacao"
					 service="lms.sgr.manterPostosControleAction.findLookupReguladoraSeguro" action="/seguros/manterReguladorasSeguro"
					 label="reguladoraSeguro" maxLength="20" size="20" width="85%" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="reguladoraSeguro.pessoa.nmPessoa"/>
			
			<adsm:textbox dataType="text" property="reguladoraSeguro.pessoa.nmPessoa" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:textbox dataType="text" property="nmLocal" label="nomeLocal" maxLength="50" width="55%" size="45%"/>
		<adsm:combobox property="tpBandeiraPosto" domain="DM_BANDEIRA_POSTO" label="bandeira" width="15%" renderOptions="true"/>
		
		<adsm:lookup dataType="text" property="municipio" idProperty="idMunicipio" criteriaProperty="nmMunicipio"
					 service="lms.municipios.municipioService.findLookup" action="/municipios/manterMunicipios" 
					 label="municipio" maxLength="50" width="55%" size="45%" exactMatch="false" minLengthForAutoPopUpSearch="4"/>
					 
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao"  labelWidth="15%" width="15%" renderOptions="true"/>
				
		<adsm:lookup dataType="text" property="rodovia" idProperty="idRodovia" criteriaProperty="sgRodovia" 
					 service="lms.municipios.rodoviaService.findLookup" action="/municipios/manterRodovias"
					 label="rodovia" size="5" maxLength="10" width="55%" exactMatch="false" minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping modelProperty="dsRodovia" relatedProperty="rodovia.dsRodovia"/>
			<adsm:textbox dataType="text" property="rodovia.dsRodovia" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="postosControle"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="postosControle" idProperty="idPostoControle" defaultOrder="nmPostoControlePassaporte:asc" gridHeight="200" unique="true">
		<adsm:gridColumn property="nmPostoControlePassaporte" title="nomePosto"  width="20%" />
		<adsm:gridColumn property="nmLocal" title="nomeLocal" width="20%" />
		<adsm:gridColumn property="municipio.nmMunicipio" title="municipio" width="20%" />
		<adsm:gridColumn property="municipio.unidadeFederativa.sgUnidadeFederativa" title="uf" width="6%" />
		<adsm:gridColumn property="rodovia.sgRodovia" title="rodovia" width="8%" />
		<adsm:gridColumn property="nrKm" title="km" width="8%" align="right"/>
		<adsm:gridColumn property="tpBandeiraPosto" isDomain="true" title="bandeira" width="10%" />
		<adsm:gridColumn property="tpSituacao" isDomain="true" title="situacao" width="8%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
