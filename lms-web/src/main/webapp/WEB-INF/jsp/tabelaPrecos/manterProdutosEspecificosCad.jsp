<%@taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.tabelaprecos.produtoEspecificoService">
	<adsm:form action="/tabelaPrecos/manterProdutosEspecificos" idProperty="idProdutoEspecifico">
		<adsm:textbox label="tarifaEspecifica" property="nrTarifaEspecifica" dataType="integer" maxLength="3" size="11" required="true"/>
		<adsm:textbox label="produtoEspecifico" property="dsProdutoEspecifico" dataType="text" maxLength="60" size="33" required="true"/>

		<adsm:textbox label="pesoMinimo" property="psMinimo" dataType="weight" minValue="0.01" unit="kg" maxLength="8" size="10"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>