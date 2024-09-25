<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterMotivosParadaAction">
	<adsm:form action="/municipios/manterMotivosParada" idProperty="idMotivoParada">
	<adsm:textbox dataType="text" property="dsMotivoParada" label="descricao" maxLength="60" size="60" width="80%" />
	<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motivoParada"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="motivoParada" idProperty="idMotivoParada" selectionMode="check" gridHeight="200" unique="true" defaultOrder="dsMotivoParada" rows="12">
		<adsm:gridColumn title="descricao" property="dsMotivoParada"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
