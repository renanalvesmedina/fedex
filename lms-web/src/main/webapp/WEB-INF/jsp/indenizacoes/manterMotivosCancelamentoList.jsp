<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.motivoCancelamentoRimService">
	<adsm:form action="/indenizacoes/manterMotivosCancelamento">
		<adsm:combobox property="tpCancelamento" label="tipoCancelamento" domain="DM_TIPO_CANCELAMENTO_INDENIZACAO" renderOptions="true" />
		<adsm:textbox  property="dsMotivoCancelamentoRim" label="descricao" dataType="text" size="95%" width="85%" maxLength="60"/>
		<adsm:combobox property="tpSituacao"label="situacao" optionLabelProperty="" domain="DM_STATUS" labelWidth="15%" width="85%" renderOptions="true" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motivoCancelamentoRim"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="motivoCancelamentoRim" idProperty="idMotivoCancelamentoRim" selectionMode="checkbox" defaultOrder="tpCancelamento, dsMotivoCancelamentoRim:asc" gridHeight="200" unique="true" rows="12">
		<adsm:gridColumn property="tpCancelamento" title="tipoCancelamento" width="30%" isDomain="true"/>
		<adsm:gridColumn property="dsMotivoCancelamentoRim" title="descricao" width="60%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" width="10%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
