<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterTiposVisitaAction">
	<adsm:form action="/vendas/manterTiposVisita" idProperty="idTipoVisita" >
        <adsm:textbox label="tipoVisita" property="dsTipoVisita" dataType="text"  maxLength="60" size="53" width="75%" required="true" />
        <adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" labelWidth="15%" width="25%" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>