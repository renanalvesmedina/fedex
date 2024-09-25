<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.pendencia.manterDispositivosLegaisAction">
	<adsm:form action="/pendencia/manterDispositivosLegais" idProperty="idUfDispositivoLegal">
		<adsm:combobox label="uf" property="unidadeFederativa.idUnidadeFederativa" optionLabelProperty="siglaDescricao" optionProperty="idUnidadeFederativa" service="lms.pendencia.manterDispositivosLegaisAction.findUfsByPais" labelWidth="20%" width="80%" onlyActiveValues="true" required="true"/>
		<adsm:textarea label="dispositivoLegal" property="dsDispositivoLegal" maxLength="200" rows="5" columns="70" labelWidth="20%" width="80%" required="true"/>
		<adsm:buttonBar freeLayout="false">
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>