<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterMotivosParadaAction">
	<adsm:form action="/municipios/manterMotivosParada" idProperty="idMotivoParada">
		<adsm:textbox dataType="text" property="dsMotivoParada" label="descricao" required="true" maxLength="60" size="60" width="80%" />
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>     