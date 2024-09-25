<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.tipoSinistroService" >
	<adsm:form action="/seguros/manterTiposSinistro" idProperty="idTipoSinistro" >
		<adsm:textbox dataType="text" property="dsTipo" label="descricao" size="40" maxLength="30" width="85%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" renderOptions="true"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="tipos" idProperty="idTipoSinistro" 
			   rows="13" defaultOrder="dsTipo:asc" selectionMode="check" gridHeight="200" unique="true">
		<adsm:gridColumn title="descricao" 	property="dsTipo" width="90%" />
		<adsm:gridColumn title="situacao" 	property="tpSituacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
