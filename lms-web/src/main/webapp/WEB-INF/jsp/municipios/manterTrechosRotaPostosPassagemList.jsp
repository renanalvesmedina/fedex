<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarTrechosRotaPostosPassagem">
	<adsm:form action="/municipios/manterTrechosRotaPostosPassagem">
		<adsm:textbox dataType="text" property="nomeRodovia" size="35" maxLength="50" disabled="true" label="rota" width="75%" labelWidth="17%"/>
		<adsm:textbox dataType="text" property="nomeRodovia" size="35" maxLength="50" disabled="true" label="postoPassagem" width="75%" labelWidth="17%"/>
		<adsm:combobox property="filial.id" label="filialOrigem" service="" optionLabelProperty="sigla" optionProperty="id" width="40%" labelWidth="17%"/>
		<adsm:combobox property="filial.id" label="filialDestino" service="" optionLabelProperty="sigla" optionProperty="id" width="40%" labelWidth="17%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" selectionMode="check" gridHeight="200" unique="true">
		<adsm:gridColumn title="postoPassagem" property="fi" width="34%" />
		<adsm:gridColumn title="filialOrigem" property="mu" width="33%" />
		<adsm:gridColumn title="filialDestino" property="ro" width="33%" />
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>