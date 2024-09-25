<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.manterSetorAction">
	<adsm:form action="/carregamento/manterSetor" idProperty="idSetor" service="lms.carregamento.manterSetorAction.findByIdCustom">
		<adsm:textbox label="codigoRH" property="cdSetorRh" dataType="integer" maxLength="2" size="2" width="85%" required="false"/>
		<adsm:textbox label="nomeSetor" property="dsSetor" dataType="text" maxLength="60" size="60" width="85%" required="true"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" required="true" width="85%" renderOptions="true"/>
		<adsm:buttonBar>
			<adsm:storeButton service="lms.carregamento.manterSetorAction.storeCustom" />
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

</script>
