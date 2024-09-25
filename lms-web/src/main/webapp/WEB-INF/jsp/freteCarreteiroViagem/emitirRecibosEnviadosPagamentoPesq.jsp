<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/freteCarreteiroViagem/emitirRecibosEnviadosPagamento">
		<adsm:combobox property="regional.id" label="regional" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%"/>
		<adsm:combobox property="filial.id" label="filial" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%"/>
		<adsm:combobox property="situacao" label="situacao" service="" prototypeValue="Gerado|Emitido|Liberado|Bloqueado|Em relação de pagamento" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%"/>
		<adsm:textbox dataType="integer" property="relacaoPagamento" label="relacaoPagamento" maxLength="10" labelWidth="20%" width="80%" />
		<adsm:range label="geracaoRelacao" labelWidth="20%" width="80%" >
			<adsm:textbox dataType="date" property="periodoInicial"/> 
			<adsm:textbox dataType="date" property="periodoFinal"/>
		</adsm:range>
		<adsm:buttonBar>
			<adsm:reportViewerButton reportName="/freteCarreteiroViagem/emitirRecibosEnviadosPagamento.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>