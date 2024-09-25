<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.naturezaProdutoService">
	<adsm:form action="/expedicao/manterNaturezasProduto" idProperty="idNaturezaProduto">
		<adsm:textbox maxLength="30" dataType="text" property="dsNaturezaProduto" label="naturezaProduto" size="40" labelWidth="20%" width="40%" required="true" />
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="20%" width="20%" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>