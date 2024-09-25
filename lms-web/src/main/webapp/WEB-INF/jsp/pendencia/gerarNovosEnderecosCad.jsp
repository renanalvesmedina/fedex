<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="gerarNovosEnderecos">
	<adsm:form action="/pendencia/gerarNovosEnderecos">
		<adsm:section caption="gerarNovosEnderecos"/>

		<adsm:lookup property="filial.id" label="filial" dataType="text" criteriaProperty="" action="/vendas/manterDadosIdentificacao" service="" labelWidth="23%" width="77%" size="4" maxLength="3" disabled="true" required="true" >
			<adsm:propertyMapping modelProperty="filial.id" formProperty="nomeFilial"/>
			<adsm:textbox property="nomeFilial" dataType="text" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:combobox label="terminal" property="terminal" optionLabelProperty="" optionProperty="" service="" labelWidth="23%" width="27%" required="true" />

		<adsm:combobox label="modulo" property="modulo" optionLabelProperty="" optionProperty="" service="" labelWidth="23%" width="27%" required="true" />

		<adsm:textbox dataType="text" label="ruaInicial" property="ruaInicial" maxLength="4" size="4" labelWidth="23%" width="27%" required="true" />
		<adsm:textbox dataType="text" label="ruaFinal" property="nomeModulo" maxLength="4" size="4" labelWidth="23%" width="27%" required="true" />
		<adsm:textbox dataType="text" label="quantidadePredios" property="quantidadePredios" maxLength="4" size="4" labelWidth="23%" width="27%" required="true" />
		<adsm:textbox dataType="text" label="quantidadeAndaresPredio" property="quantidadeAndaresPredio" maxLength="4" size="4" labelWidth="23%" width="27%" required="true" />
		<adsm:textbox dataType="text" label="quantidadeApartamentosAndar" property="quantidadeApartamentosAndar" maxLength="4" size="4" labelWidth="23%" width="27%" required="true" />

		<adsm:buttonBar>
			<adsm:button caption="gerar"/>
			<adsm:button caption="fechar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
