<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/contratacaoVeiculos/manterServicosMeioTransporte">

		<adsm:textbox dataType="text" disabled="true" property="meioTransporte" label="meioTransporte" maxLength="30" size="30"/>
		<adsm:combobox property="servico" label="servico" service="" optionLabelProperty="" optionProperty=""/>

		<adsm:range label="vigencia">
			<adsm:textbox dataType="JTDate" required="true" property="dataVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dataVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
