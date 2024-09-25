<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/pendencia/registrarVendasLotesSalvados">

		<adsm:range label="periodo" labelWidth="17%" width="83%" >
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataFinal" picker="true"/>
		</adsm:range>

		<adsm:textbox dataType="text" label="lote" property="lote" labelWidth="17%" width="83%"/>

		<adsm:textbox dataType="JTDate" label="dataEntrega" property="nomeModulo" labelWidth="17%" width="33%" />
		<adsm:textbox dataType="JTDate" label="dataVenda" property="nomeModulo"/>

		<adsm:lookup property="comprador.id" label="comprador" dataType="text" criteriaProperty="" action="/vendas/manterDadosIdentificacao" service="" labelWidth="17%" width="83%" >
			<adsm:propertyMapping modelProperty="comprador.id" formProperty="nomeComprador"/>
			<adsm:textbox property="nomeComprador" dataType="text" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="id" property="id">
		<adsm:gridColumn property="lote" title="lote" align="right" width="13%" />
		<adsm:gridColumn property="disposicao" title="disposicao" width="16%"/>
		<adsm:gridColumn property="dataGeracao" title="dataGeracao" width="11%" align="center"/>
		<adsm:gridColumn property="dataEntrega" title="dataEntrega" width="11%" align="center"/>
		<adsm:gridColumn property="dataVenda" title="dataVenda" width="11%" align="center"/>
		<adsm:gridColumn property="comprador" title="comprador" width="26%"/>
		<adsm:gridColumn property="valorVenda" title="valorVenda" width="12%" unit="reais" align="right"/>
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
