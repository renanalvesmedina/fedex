<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.grupoEconomicoService">
	<adsm:form action="/vendas/manterGruposEconomicos" >
		<adsm:textbox maxLength="60" dataType="text" property="dsGrupoEconomico" label="descricao" size="70" width="85%" labelWidth="15%"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gruposEconomico"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idGrupoEconomico" property="gruposEconomico" defaultOrder="dsGrupoEconomico" gridHeight="200" unique="true" rows="13">
		<adsm:gridColumn title="descricao" property="dsGrupoEconomico" width="80%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="20%" isDomain="true"/>
		<adsm:buttonBar >
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
