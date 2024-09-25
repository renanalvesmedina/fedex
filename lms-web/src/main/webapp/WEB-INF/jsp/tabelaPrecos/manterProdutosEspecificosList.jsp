<%@taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.tabelaprecos.produtoEspecificoService">
	<adsm:form action="/tabelaPrecos/manterProdutosEspecificos">
		<adsm:textbox label="tarifaEspecifica" property="nrTarifaEspecifica" dataType="integer" maxLength="3" size="11"/>
		<adsm:textbox label="produtoEspecifico" property="dsProdutoEspecifico" dataType="text" maxLength="30" size="33"/>

		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="produtoEspecifico"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="produtoEspecifico" idProperty="idProdutoEspecifico" defaultOrder="nrTarifaEspecifica" gridHeight="200" unique="true" rows="13">
		<adsm:gridColumn title="tarifaEspecifica" property="nrTarifaEspecifica" dataType="integer" width="20%"/>
		<adsm:gridColumn title="produtoEspecifico" property="dsProdutoEspecifico" width="50%"/>
		<adsm:gridColumn title="pesoMinimo" property="psMinimo" dataType="weight" unit="kg" width="20%"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
