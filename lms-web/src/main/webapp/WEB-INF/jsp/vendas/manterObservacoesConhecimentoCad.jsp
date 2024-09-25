<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterObservacoesConhecimentoAction">
	<adsm:form action="/vendas/manterObservacoesConhecimento" idProperty="idObservacaoConhecimento">


		<adsm:textbox property="dsObservacaoConhecimento" dataType="text" label="observacao" size="60" maxLength="60" width="70%" required="true"/>

		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true"/>



		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>


