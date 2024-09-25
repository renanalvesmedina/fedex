<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/municipios/manterTrechosRotaPostosPassagem">
		<adsm:textbox dataType="text" property="nomeRodovia" size="35" maxLength="50" disabled="true" label="rota" width="75%" labelWidth="17%"/>
		<adsm:textbox dataType="text" property="nomeRodovia" size="35" maxLength="50" disabled="true" label="postoPassagem" width="75%" labelWidth="17%"/>
		<adsm:listbox labelWidth="17%" property="" width="83%" size="3" label="valorPedagio" prototypeValue="Valor por eixo - seg - 11:00 - 12:00 - Truck - 6,00|Valor por eixo - ter - 11:00 - 12:00 - Truck - 12,00" optionLabelProperty="2|1"  optionProperty="dd12dsa" service="false" useRowspan="false"/>
		<adsm:combobox property="filial.id" label="filialOrigem" service="" optionLabelProperty="sigla" optionProperty="id" width="40%" required="true" labelWidth="17%"/>
		<adsm:combobox property="filial.id" label="filialDestino" service="" optionLabelProperty="sigla" optionProperty="id" width="40%" required="true" labelWidth="17%"/>
</adsm:form>
	<adsm:buttonBar>
		<adsm:button caption="novo"/>
		<adsm:button caption="salvar"/>
		<adsm:button caption="excluir"/>
	</adsm:buttonBar>
</adsm:window>