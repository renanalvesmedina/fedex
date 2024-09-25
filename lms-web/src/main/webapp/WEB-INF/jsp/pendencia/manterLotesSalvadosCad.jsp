<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/pendencia/manterLotesSalvados">

		<adsm:textbox dataType="text" label="lote" property="lote" disabled="true"/>
		
		<adsm:combobox label="disposicao" property="disposicao" optionLabelProperty="" optionProperty="" service="" prototypeValue="Venda|Acionista|Lixo|Administrativo|Guardar" required="true"/>
		<adsm:textbox dataType="text" label="quantidadeVolumes" property="quantidadeVolumes" disabled="true"/>

		<adsm:textbox dataType="JTDate" label="dataEntrega" property="nomeModulo"/>

		<adsm:section caption="venda"/>

		<adsm:textbox dataType="JTDate" label="dataVenda" property="nomeModulo" disabled="true"/>

		<adsm:combobox label="valorPago" property="moedaValorPago" optionLabelProperty="" optionProperty="" service="" disabled="true">
			<adsm:textbox property="valorFreteCarreteiro" dataType="currency" disabled="true"/>
		</adsm:combobox>

		<adsm:lookup property="comprador.id" label="comprador" dataType="text" criteriaProperty="" action="/vendas/manterDadosIdentificacao" service="" width="85%" disabled="true">
			<adsm:propertyMapping modelProperty="comprador.id" formProperty="nomeComprador"/>
			<adsm:textbox property="nomeComprador" dataType="text" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:buttonBar>
			<adsm:button caption="emitir"/>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
