<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.naturezaProdutoService">
	<adsm:form action="/expedicao/manterNaturezasProduto" idProperty="idNaturezaProduto">
		<adsm:textbox maxLength="30" dataType="text" property="dsNaturezaProduto" label="naturezaProduto" size="40" labelWidth="20%" width="40%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="20%" width="20%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="naturezaProduto"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="naturezaProduto" idProperty="idNaturezaProduto" defaultOrder="dsNaturezaProduto" gridHeight="200" unique="true" rows="14">
		<adsm:gridColumn title="naturezaProduto" property="dsNaturezaProduto" width="70%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="30%" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
