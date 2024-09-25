<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterIndicadoresMarketingAction">
	<adsm:form action="/vendas/manterIndicadoresMarketing" idProperty="idCampanhaMarketing">
        <adsm:textbox label="indicadorMarketing" property="dsCampanhaMarketing" dataType="text"  labelWidth="18%" maxLength="60" size="63" width="75%" required="true"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" labelWidth="18%" width="25%" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window> 