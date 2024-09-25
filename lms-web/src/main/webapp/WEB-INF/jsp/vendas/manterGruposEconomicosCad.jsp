<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.grupoEconomicoService">
	<adsm:form action="/vendas/manterGruposEconomicos" idProperty="idGrupoEconomico">
		<adsm:textbox maxLength="60" dataType="text" property="dsGrupoEconomico" label="descricao" size="70" width="85%" labelWidth="15%" required="true"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true"/>
		<adsm:buttonBar>
		    <adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>