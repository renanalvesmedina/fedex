<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.integracao.paisCorporativoService">
	<adsm:form action="/integracao/manterPaisesCorporativo" idProperty="idPais"> 
		
		
		<adsm:textbox dataType="text" property="nmPais" 
					  label="pais" maxLength="50" size="36" 
					  labelWidth="18%" width="32%" />
					  
		<adsm:textbox dataType="integer" property="cdIso" 
					  label="codISO" size="8" maxLength="6" 
					  labelWidth="18%" width="32%"/>

		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="paisCorporativo"/>
			<adsm:resetButton />
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idPais" property="paisCorporativo" defaultOrder="nmPais" rows="14" selectionMode="check" gridHeight="200" unique="true">
		<adsm:gridColumn title="pais" property="nmPais" width="82%" />
		<adsm:gridColumn title="codISO" property="cdIso" align="center" width="18%" />

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar> 
	</adsm:grid>
</adsm:window>