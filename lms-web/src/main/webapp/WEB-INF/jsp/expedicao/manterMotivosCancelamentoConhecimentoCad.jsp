<%@taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.expedicao.motivoCancelamentoService">
	<adsm:form action="/expedicao/manterMotivosCancelamentoConhecimento" idProperty="idMotivoCancelamento">
		<adsm:textbox dataType="text" property="dsMotivoCancelamento" label="motivoDeCancelamento" maxLength="80" size="90" required="true" labelWidth="20%" width="80%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" required="true" labelWidth="20%"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>