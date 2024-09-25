<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarParcelasFreteColetaEntrega">
	<adsm:form action="/contratacaoVeiculos/manterParcelasFreteColetaEntrega">
		<adsm:textbox dataType="text" property="filial" disabled="true" label="filial" maxLength="10" labelWidth="20%" width="30%"/>
		<adsm:textbox dataType="text" property="numeroSolicitacao" disabled="true" label="numeroSolicitacao" maxLength="20" size="20" labelWidth="18%" width="32%"/>

		<adsm:combobox property="tipoParcela" label="tipoParcela" service="" optionLabelProperty="" optionProperty="" prototypeValue="Diária por período|Diária por horário de corte| Hora excedente|Quilometragem|Peso|Fração de peso|Percentual sobre valor do frete|Percentual sobre valor de mercado" width="60%" labelWidth="20%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="180" unique="true">
		<adsm:gridColumn title="tipoParcela" property="tp" width="40%" />
		<adsm:gridColumn title="franquia" property="franq" width="15%" align="right"/>
		<adsm:gridColumn title="valorSugerido" property="valSug" width="15%" align="right"/>
		<adsm:gridColumn title="valorMaximoAprovado" property="valMax" width="15%" align="right"/>
		<adsm:gridColumn title="valorNegociado" property="valNeg" width="15%" align="right"/>

		<adsm:buttonBar>
			<adsm:button caption="excluir" /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>