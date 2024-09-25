<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/pendencia/registrarVendasLotesSalvados">
		<adsm:masterLink showSaveAll="true">
			<adsm:masterLinkItem label="lote" property="lote" itemWidth="100"/>
		</adsm:masterLink>

		<adsm:textbox dataType="integer" label="numeroCheque" property="numeroCheque" labelWidth="17%" width="33%" />
		<adsm:combobox label="valor" property="moedaValor" optionLabelProperty="" optionProperty="" service="" width="35%" >
			<adsm:textbox property="valor" dataType="currency" />
		</adsm:combobox>

		<adsm:textbox dataType="JTDate" label="dataVencimento" property="data" labelWidth="17%" width="83%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="novoCheque"/>
			<adsm:button caption="salvarCheque"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="id" property="id" unique="true">
		<adsm:gridColumn property="numeroCheque" title="numeroCheque" align="right"/>
		<adsm:gridColumn property="valor" title="valor" unit="reais" align="right" />
		<adsm:gridColumn property="data" title="dataVencimento" align="center"/>
		<adsm:buttonBar>
			<adsm:button caption="excluirCheque"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
