<%@taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.expedicao.tipoProdutoService">
	<adsm:form action="/expedicao/manterTiposProduto">
		<adsm:textbox label="tipoProduto" property="dsTipoProduto" dataType="text" maxLength="60" size="50" labelWidth="20%" width="80%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" labelWidth="20%" width="80%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoProduto"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="tipoProduto" idProperty="idTipoProduto" defaultOrder="dsTipoProduto" gridHeight="200" unique="true" rows="13">
		<adsm:gridColumn title="tipoProduto" property="dsTipoProduto" width="90%"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>