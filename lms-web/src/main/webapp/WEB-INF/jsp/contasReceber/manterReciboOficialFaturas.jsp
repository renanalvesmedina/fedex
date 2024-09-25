<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/contasReceber/manterReciboOficial">
		<adsm:masterLink>
			<adsm:masterLinkItem property="filialCobranca" label="filialCobranca" />
			<adsm:masterLinkItem property="numeroRecibo" label="numeroRecibo" />
		</adsm:masterLink>

        <adsm:combobox property="tipoDocumento" label="tipoDocumento" labelWidth="20%" service="" prototypeValue="Nota de débito|Nota de crédito|Fatura" width="80%" optionLabelProperty="" optionProperty="" required="true"/>

		<adsm:lookup action="/configuracaoes/manterFiliais" service="" dataType="integer" property="filial.id" criteriaProperty="filial.codigo" label="filialDocumento" size="15" maxLength="15" width="80%" labelWidth="20%" required="true">
			<adsm:propertyMapping modelProperty="numFilial" formProperty="numFilial"/>
			<adsm:textbox dataType="text" property="nomeFilial" size="50" maxLength="50" disabled="true"/>		
		</adsm:lookup>

		<adsm:lookup action="/contasReceber/manterFaturas" size="10" maxLength="10" label="numeroDocumento" labelWidth="20%" dataType="text" service="" property="numFatura" width="30%" required="true"/>

        <adsm:textbox label="valorDocumento" labelWidth="20%" dataType="text" size="10" property="valor"  width="30%" disabled="true" maxLength="18"/>

        <adsm:section caption="totaisReciboOficial"/>
		<adsm:textbox dataType="text" label="quantidadeDocumentos" property="quantidadeDocumentos" size="10" labelWidth="23%"	maxLength="6" disabled="true" width="13%"/>
		<adsm:textbox dataType="text" label="valorTotalDocumentos" property="valorTotalFaturas" size="10" labelWidth="23%" maxLength="18" disabled="true" width="12%"/>
		

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="novoDocumento"/>
			<adsm:button caption="salvarDocumento"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" unique="true">
        <adsm:gridColumn title="tipoDocumento" property="tipoDocumento"/>
		<adsm:gridColumn title="filialDocumento" property="filialCobranca"/>
		<adsm:gridColumn title="numeroDocumento" property="numeroFatura"/>
        <adsm:gridColumn title="valorTotalDocumentos" property="valorFatura"/>
	</adsm:grid>

	<adsm:buttonBar>
		<adsm:button caption="excluirDocumento"/>
	</adsm:buttonBar>

</adsm:window>