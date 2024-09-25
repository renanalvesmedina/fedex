<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterTiposVisitaAction">
	<adsm:form action="/vendas/manterTiposVisita">
        <adsm:textbox dataType="text" property="dsTipoVisita" label="tipoVisita" maxLength="60" size="53" width="60%"/>
        <adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" required="false" width="40%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoVisita" />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idTipoVisita" property="tipoVisita" 
			   selectionMode="check" gridHeight="200" unique="true" rows="13" defaultOrder="dsTipoVisita:asc" >
		<adsm:gridColumn title="tipoVisita" property="dsTipoVisita" width="90%" />
	 	<adsm:gridColumn title="situacao" property="tpSituacao"  isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
