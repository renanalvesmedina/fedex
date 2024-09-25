<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.manterPerifericosRastreadorAction">
	<adsm:form action="/sgr/manterPerifericosRastreador" idProperty="idPerifericoRastreador" service="lms.sgr.manterPerifericosRastreadorAction.findByIdCustom">

		<adsm:textbox label="nomePeriferico" property="dsPerifericoRastreador" dataType="text" maxLength="60" size="60" width="85%" required="true"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" required="true" width="85%" renderOptions="true"/>

		<adsm:buttonBar>
			<adsm:storeButton service="lms.sgr.manterPerifericosRastreadorAction.storeCustom" />
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

</script>
