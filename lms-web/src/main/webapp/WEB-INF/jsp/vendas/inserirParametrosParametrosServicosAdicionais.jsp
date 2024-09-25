<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/manterServicosAdicionaisCliente">

		<adsm:combobox property="servicoAdicional" optionLabelProperty="value" optionProperty="0" service="" label="servicoAdicional" prototypeValue="" labelWidth="16%" width="34%" required="true"/>
		<adsm:combobox property="indicador" optionLabelProperty="value" optionProperty="0" service="" label="indicador" prototypeValue="Desconto|Acréscimo|Tabela|Valor" required="true"/>
		<adsm:combobox property="moeda" optionLabelProperty="value" optionProperty="0" service="" label="moeda" prototypeValue="" labelWidth="16%" width="34%" required="true" disabled="true"/>
		<adsm:textbox dataType="currency" property="valor" label="valor" size="10" maxLength="18" required="true"/>
		<adsm:textbox dataType="integer" property="quantidadeDias" label="quantidadeDias" size="5" labelWidth="16%" width="34%" maxLength="5"/>
		<adsm:textbox dataType="currency" property="valorMinimo" label="valorMinimo" size="10" maxLength="18"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="200" unique="true">
		<adsm:gridColumn title="servicoAdicional" property="servicoAdicional" width="50%" />
		<adsm:gridColumn title="indicador" property="indicador" width="20%" />
		<adsm:gridColumn title="valorIndicador" property="valorIndicador" width="30%" align="right"/>
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>   