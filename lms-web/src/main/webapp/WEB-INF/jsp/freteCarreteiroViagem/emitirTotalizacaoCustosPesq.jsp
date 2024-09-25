<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirTotalizacaoCustos">
	<adsm:form action="/freteCarreteiroViagem/emitirTotalizacaoCustos">
		<adsm:combobox property="regionalId" label="regional" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%" />		
		<adsm:combobox property="filial" label="filial" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="80%" />		
		<adsm:combobox property="tipoCustoId" label="tipoCusto" service="" optionLabelProperty="" optionProperty="" prototypeValue="Próprio|Agregados|Eventuais|Agregado + Eventual" labelWidth="20%" width="80%" />		
        <adsm:textbox dataType="date" mask="mm/yyyy" property="competenciaId" label="dataContabilizacao" labelWidth="20%" width="80%" required="true" />
		<adsm:buttonBar>
			<adsm:reportViewerButton reportName="freteCarreteiroViagem/emitirTotalizacaoCustos.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>