<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.motivoCancelamentoRimService">
	<adsm:form action="/indenizacoes/manterMotivosCancelamento" idProperty="idMotivoCancelamentoRim">
		<adsm:combobox property="tpCancelamento" label="tipoCancelamento" domain="DM_TIPO_CANCELAMENTO_INDENIZACAO" required="true" renderOptions="true"/>
		<adsm:textbox  property="dsMotivoCancelamentoRim" label="descricao" dataType="text" size="95%" width="85%" maxLength="60" required="true"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="15%" width="85%" required="true" renderOptions="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>			
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>