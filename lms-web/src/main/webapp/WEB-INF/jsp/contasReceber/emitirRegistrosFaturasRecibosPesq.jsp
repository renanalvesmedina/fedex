<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window>
	<adsm:form action="/contasReceber/emitirRegistrosFaturasRecibos">

     	<adsm:lookup label="filialEmissora" service="" dataType="integer" property="filial.id" criteriaProperty="filial.codigo" size="3" maxLength="3" action="/municipios/manterFiliais" width="80%" labelWidth="20%">
			<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/>
			<adsm:textbox dataType="text" property="nomeFilial" size="60" disabled="true" width="25%"/>
		</adsm:lookup>

        <adsm:lookup action="/configuracoes/manterPessoas"  service="" dataType="integer" property="cliente.id" criteriaProperty="cliente.codigo" label="cliente" size="15" maxLength="15" labelWidth="20%"  width="80%">
			<adsm:propertyMapping modelProperty="cliente" formProperty="nomeCliente"/>
			<adsm:textbox dataType="text" property="nomeCliente" size="30" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:combobox label="opcaoImpressao" optionLabelProperty="" optionProperty="" property="opcaoImpressao" service="" prototypeValue="Fatura / Nota de débito / Nota de crédito|Recibo" width="80%" labelWidth="20%"/>

		<adsm:range label="periodoEmissao" labelWidth="20%" width="80%" required="true">
			<adsm:textbox property="periodoIni" dataType="JTDate"/>
			<adsm:textbox property="periodoFim" dataType="JTDate"/>
		</adsm:range>
       <adsm:combobox label="moeda" property="moeda" service="" optionProperty="" optionLabelProperty="" prototypeValue="" labelWidth="20%" width="80%"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton caption="recibo" reportName="/contasReceber/emitirRegistrosFaturasRecibosPorRecibo.jasper"/>
			<adsm:reportViewerButton caption="visualizar" reportName="/contasReceber/emitirRegistrosFaturasRecibos.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>