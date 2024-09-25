<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.pendencia.manterDispositivosLegaisAction">
	<adsm:form action="/pendencia/manterDispositivosLegais" height="100">
		<adsm:combobox label="uf" property="unidadeFederativa.idUnidadeFederativa" optionLabelProperty="siglaDescricao" optionProperty="idUnidadeFederativa" service="lms.pendencia.manterDispositivosLegaisAction.findUfsByPais" labelWidth="20%" width="80%"/>
		<adsm:textarea label="dispositivoLegal" property="dsDispositivoLegal" maxLength="200" rows="5" columns="70" labelWidth="20%" width="80%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="dispositivosLegais" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="dispositivosLegais" idProperty="idUfDispositivoLegal" service="lms.pendencia.manterDispositivosLegaisAction.findPaginatedDispositivosLegais" defaultOrder="unidadeFederativa_.sgUnidadeFederativa, dsDispositivoLegal"
			rowCountService="lms.pendencia.manterDispositivosLegaisAction.getRowCountDispositivosLegais" gridHeight="200" unique="true">
		<adsm:gridColumn property="unidadeFederativa.sgUnidadeFederativa" title="uf" width="8%"/>
		<adsm:gridColumn property="dsDispositivoLegal" title="dispositivoLegal" width="92%"/>
	
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>