<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.tipoProdutoService">
	<adsm:form action="/expedicao/manterTiposProduto" idProperty="idTipoProduto">
		<adsm:textbox label="tipoProduto" property="dsTipoProduto" dataType="text" required="true" maxLength="60" size="50" labelWidth="20%" width="80%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" required="true" labelWidth="20%"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>