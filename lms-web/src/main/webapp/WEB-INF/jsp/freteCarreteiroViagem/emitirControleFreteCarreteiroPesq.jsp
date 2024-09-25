<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/freteCarreteiroViagem/emitirControleFreteCarreteiro">
		<adsm:combobox property="regional.id" label="regional" service="" optionLabelProperty="" optionProperty="" labelWidth="24%" width="76%"/>
		<adsm:combobox property="filial.id" label="filial" service="" optionLabelProperty="" optionProperty="" labelWidth="24%" width="76%"/>
		<adsm:combobox property="filial.id" label="classificacao" service="" optionLabelProperty="" optionProperty="" labelWidth="24%" width="26%" prototypeValue="Origem/Data|Data/Origem"/>
		<adsm:combobox property="filial.id" label="tipoVinculo" service="" optionLabelProperty="" optionProperty="" labelWidth="24%" width="26%" prototypeValue="Próprio|Terceiros"/>
		<adsm:combobox property="filial.id" label="somenteTotais" service="" optionLabelProperty="" optionProperty="" labelWidth="24%" width="26%" prototypeValue="Sim|Não"/>
		<adsm:combobox property="filial.id" label="somenteDiferentes" service="" optionLabelProperty="" optionProperty="" labelWidth="24%" width="26%" prototypeValue="Sim|Não"/>
		<adsm:combobox property="filial.id" label="abrangencia" service="" optionLabelProperty="" optionProperty="" labelWidth="24%" width="76%" prototypeValue="Nacional|Internacional|Ambos"/>
		<adsm:range label="periodoEmissaoControleCarga" labelWidth="24%" width="76%" required="true">
			<adsm:textbox dataType="date" property="periodoInicial" cellStyle="vertical-align:bottom;"/>
			<adsm:textbox dataType="date" property="periodoFinal" cellStyle="vertical-align:bottom;"/>
		</adsm:range>
		<adsm:buttonBar>
			<adsm:reportViewerButton reportName="/freteCarreteiroViagem/emitirControleFreteCarreteiro.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>