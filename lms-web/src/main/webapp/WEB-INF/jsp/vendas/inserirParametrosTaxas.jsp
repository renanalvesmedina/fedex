<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/inserirParametros" >

		<adsm:combobox property="parcelaPrecoId" label="taxa" optionLabelProperty="" optionProperty="" service="" required="true" labelWidth="16%" width="40%" />
		<adsm:combobox property="taxaIndicador" label="indicador" optionLabelProperty="" optionProperty="" service="" required="true" prototypeValue="Tabela|Valor|Desconto|Acréscimo" labelWidth="16%" width="20%" />
		<adsm:combobox property="moedaId" label="moeda" optionLabelProperty="" optionProperty="" service="" required="true" disabled="true" labelWidth="16%" width="40%" />
		<adsm:textbox dataType="decimal" property="taxaValor" label="valor" maxLength="15" size="15" required="true" labelWidth="16%" width="20%" />
		<adsm:textbox dataType="decimal" property="pesoMinimo" label="pesoMinimo" maxLength="15" size="15" labelWidth="16%" width="40%" unit="kg" />
		<adsm:textbox dataType="decimal" property="excedente" label="valorExcedente" maxLength="15" size="15" labelWidth="16%" width="20%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="170" unique="true" rows="6">
		<adsm:gridColumn title="taxa" property="parcelaPrecoId" />
		<adsm:gridColumn title="indicador" property="taxaIndicador" width="15%" />
		<adsm:gridColumn title="valorIndicador" property="taxaValor" align="right" width="15%" />
		<adsm:gridColumn title="pesoMinimoKg" property="pesoMinimo" align="right" width="15%" />
		<adsm:gridColumn title="valorExcedenteReal" property="excedente" align="right" width="20%"/>
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
