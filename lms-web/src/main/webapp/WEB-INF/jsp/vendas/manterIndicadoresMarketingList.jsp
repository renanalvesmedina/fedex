<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterIndicadoresMarketingAction">
	<adsm:form action="/vendas/manterIndicadoresMarketing">
		<adsm:textbox label="indicadorMarketing" property="dsCampanhaMarketing" dataType="text"  labelWidth="18%" maxLength="60" size="63" width="75%" required="false"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" labelWidth="18%" width="25%" required="false"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="campanhaMarketing" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idCampanhaMarketing" property="campanhaMarketing" 
				selectionMode="check" gridHeight="200" unique="true" rows="13" defaultOrder="dsCampanhaMarketing:asc" >
		<adsm:gridColumn title="indicadorMarketing" property="dsCampanhaMarketing" width="90%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
