<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/inserirParametros" >


		<adsm:combobox property="parcelaPrecoId" label="generalidade" optionLabelProperty="" optionProperty="" service="" required="true" labelWidth="16%" width="40%" />
		<adsm:combobox property="indicador" label="indicador" optionLabelProperty="" optionProperty="" service="" required="true" prototypeValue="Tabela|Valor|Desconto|Acréscimo" labelWidth="16%" width="20%" />
		<adsm:combobox property="moedaId" label="moeda" optionLabelProperty="" optionProperty="" service="" required="true" disabled="true" labelWidth="16%" width="40%" />
		<adsm:textbox dataType="decimal" property="valor" label="valor" maxLength="18" size="18" required="true" labelWidth="16%" width="20%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="170" unique="true" rows="6">
		<adsm:gridColumn title="generalidade" property="parcelaPrecoId" width="40%" />
		<adsm:gridColumn title="indicador" property="indicador" width="30%" />
		<adsm:gridColumn title="valorIndicador" property="valor" align="right" width="30%" />
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
