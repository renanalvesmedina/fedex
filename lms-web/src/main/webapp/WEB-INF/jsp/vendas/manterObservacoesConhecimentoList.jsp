<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterObservacoesConhecimentoAction">
	<adsm:form action="/vendas/manterObservacoesConhecimento">
		

		<adsm:textbox property="dsObservacaoConhecimento" dataType="text" label="observacao" size="60" maxLength="60" width="70%"/>

		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridObservacao" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idObservacaoConhecimento" property="gridObservacao" rows="13"
			   service="lms.vendas.manterObservacoesConhecimentoAction.findPaginated"
			   rowCountService="lms.vendas.manterObservacoesConhecimentoAction.getRowCount">
			   
		<adsm:gridColumn property="dsObservacaoConhecimento" title="observacao" dataType="text"/>
		<adsm:gridColumn property="tpSituacao" title="situacao" dataType="text" isDomain="true"/>


		<adsm:buttonBar>
			<adsm:removeButton service="lms.vendas.manterObservacoesConhecimentoAction.removeByIds"/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

