<%@taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.expedicao.motivoCancelamentoService">
	<adsm:form action="/expedicao/manterMotivosCancelamentoConhecimento">
		<adsm:textbox label="motivoDeCancelamento" property="dsMotivoCancelamento" dataType="text" maxLength="80" size="90" labelWidth="20%" width="80%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" labelWidth="20%" width="80%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motivoCancelamentoConhecimento"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="motivoCancelamentoConhecimento" idProperty="idMotivoCancelamento" defaultOrder="dsMotivoCancelamento" gridHeight="200" unique="true" rows="13">
		<adsm:gridColumn title="motivoDeCancelamento" property="dsMotivoCancelamento" width="90%"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>