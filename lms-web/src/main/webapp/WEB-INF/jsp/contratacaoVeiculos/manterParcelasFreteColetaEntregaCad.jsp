<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/contratacaoVeiculos/manterParcelasFreteColetaEntrega" >
		<adsm:textbox dataType="text" property="filial" disabled="true" label="filial" maxLength="10" labelWidth="20%" width="30%"/>
		<adsm:textbox dataType="text" property="numeroSolicitacao" disabled="true" label="numeroSolicitacao" maxLength="20" size="20" labelWidth="18%" width="32%"/>

		<adsm:combobox property="tipoParcela" label="tipoParcela" service="" optionLabelProperty="" optionProperty="" prototypeValue="Diária por período|Diária por horário de corte| Hora excedente|Quilometragem|Peso|Fração de peso|Percentual sobre valor do frete|Percentual sobre valor de mercado" width="60%" labelWidth="20%" required="true"/>

		<adsm:lookup service="" dataType="text" property="moeda.id" size="20" criteriaProperty="moeda.codigo" label="moeda" action="/configuracoes/manterMoedas" labelWidth="20%" width="60%"/>

		<adsm:textbox dataType="text" property="franquia" label="franquia" maxLength="20" size="20" labelWidth="20%" width="30%"/>
		<adsm:textbox dataType="text" property="valorSugerido" label="valorSugerido" maxLength="20" size="20" labelWidth="18%" width="32%"/>

		<adsm:textbox dataType="text" property="valorMaximoAprovado" label="valorMaximoAprovado" maxLength="20" size="20" labelWidth="20%" width="30%"/>
		<adsm:textbox dataType="text" property="valorNegociado" label="valorNegociado" maxLength="20" size="20" labelWidth="18%" width="32%"/>

		<adsm:buttonBar>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>