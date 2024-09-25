<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/pendencia/manterLotesSalvados">

		<adsm:range label="periodo" width="85%" >
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataFinal" picker="true"/>
		</adsm:range>

		<adsm:textbox dataType="text" label="lote" property="lote"/>
		<adsm:combobox label="disposicao" property="disposicao" optionLabelProperty="" optionProperty="" service="" prototypeValue="Venda|Acionista|Lixo|Administrativo|Guardar"/>

		<adsm:checkbox label="armazenados" property="armazenados"/>
		<adsm:checkbox label="entregues" property="entregues"/>

		<adsm:textbox dataType="JTDate" label="dataEntrega" property="nomeModulo"/>
		<adsm:textbox dataType="JTDate" label="dataVenda" property="nomeModulo"/>

		<adsm:lookup property="comprador.id" label="comprador" dataType="text" criteriaProperty="" action="/vendas/manterDadosIdentificacao" service="" width="85%">
			<adsm:propertyMapping modelProperty="comprador.id" formProperty="nomeComprador"/>
			<adsm:textbox property="nomeComprador" dataType="text" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="id" property="id" >
		<adsm:gridColumn property="lote" title="lote" align="right" width="15%" />
		<adsm:gridColumn property="disposicao" title="disposicao" width="15%"/>
		<adsm:gridColumn property="dataGeracao" title="dataGeracao" width="15%" align="center"/>
		<adsm:gridColumn property="dataEntrega" title="dataEntrega" width="15%" align="center"/>
		<adsm:gridColumn property="dataVenda" title="dataVenda" width="15%" align="center"/>
		<adsm:gridColumn property="comprador" title="comprador" width="25%"/>
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
