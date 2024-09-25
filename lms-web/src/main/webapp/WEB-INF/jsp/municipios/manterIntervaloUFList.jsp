<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.municipios.manterIntervaloUFAction">
	<adsm:form action="/municipios/manterIntervaloUF" idProperty="idIntervaloCepUF">
	
		<adsm:lookup property="unidadeFederativa"  idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
					 service="lms.municipios.manterIntervaloUFAction.findLookupUF" dataType="text"
					 width="35%" label="uf" size="2" maxLength="2"
					 action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="false">
    		<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
    		<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" size="25" serializable="false" disabled="true"/>	
		</adsm:lookup>
        
        <adsm:textbox dataType="text" label="pais" property="unidadeFederativa.pais.nmPais" size="30" maxLength="60" labelWidth="15%" width="35%" serializable="false" />
        
        <adsm:range label="cep" width="32%">
			<adsm:textbox property="nrCepInicial" dataType="text" maxLength="8"  size="10"/>
			<adsm:textbox property="nrCepFinal" dataType="text" maxLength="8" size="10"/>		
		</adsm:range>
        
        
        <adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="intervaloCepUF"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="intervaloCepUF" idProperty="idIntervaloCepUF" selectionMode="check" rows="12"
			service="lms.municipios.manterIntervaloUFAction.findPaginatedCustom"
			rowCountService="lms.municipios.manterIntervaloUFAction.getRowCountCustom"
			unique="true" defaultOrder="nrCepInicial, tpSituacao"> 
		<adsm:gridColumn title="cepInicial" property="nrCepInicial" width="35%" dataType="text"/>
		<adsm:gridColumn title="cepFinal" property="nrCepFinal" width="35%" dataType="text"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="30%" dataType="text"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>