<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterIntervaloUFAction">
     
	<adsm:form action="/municipios/manterIntervaloUF" idProperty="idIntervaloCepUF">
		
		<adsm:lookup property="unidadeFederativa"  idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
					 service="lms.municipios.manterIntervaloUFAction.findLookupUF" dataType="text"
					 width="35%" label="uf" size="2" maxLength="2" required="true"
					 action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="false">
    		<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
    		<adsm:propertyMapping relatedProperty="unidadeFederativa.pais.idPais" modelProperty="pais.idPais" />
    		<adsm:hidden property="unidadeFederativa.pais.idPais"/>
    		<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" size="25" serializable="false" disabled="true"/>	
		</adsm:lookup>
		<adsm:hidden property="unidadeFederativa.pais.sgPais"/>
		
		<adsm:textbox dataType="text" label="pais" property="unidadeFederativa.pais.nmPais" size="30" maxLength="60" labelWidth="15%" width="35%" serializable="false"/>
		
		<adsm:textbox dataType="text" label="cepInicial" maxLength="8" size="8" property="nrCepInicial" required="true" width="35%"/>
        <adsm:textbox dataType="text" label="cepFinal" maxLength="8" size="8" property="nrCepFinal"  width="35%" required="true"/>
        
        <adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true"/>
		
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton/> 
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
