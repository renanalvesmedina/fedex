<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarServicosMeioTransporte">
	<adsm:form action="/contratacaoVeiculos/manterServicosMeioTransporte">
		<adsm:textbox dataType="text" disabled="true" property="meioTransporte" label="meioTransporte" maxLength="30" size="30"/>
		<adsm:combobox property="servico" label="servico" service="" optionLabelProperty="" optionProperty=""/>

		<adsm:textbox dataType="JTDate" property="dataVigenciaInicial" label="vigencia" maxLength="10"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="200" unique="true">
		<adsm:gridColumn title="meioTransporte" property="mt" width="40%" />
		<adsm:gridColumn title="servico" property="srv" width="30%" />
		<adsm:gridColumn title="vigenciaInicial" property="vigenciaInicial" width="15%" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="vigenciaFinal" width="15%" dataType="JTDate" />
		<adsm:buttonBar>
			<adsm:button caption="excluir" /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>