<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/freteCarreteiroViagem/emitirTabelao">
		<adsm:combobox property="regional.id" label="regional" service="" optionLabelProperty="" optionProperty=""  labelWidth="19%" width="81%"/>
		<adsm:combobox property="filial.id" label="filial" service="" optionLabelProperty="" optionProperty="" labelWidth="19%" width="81%"/>
		<adsm:combobox property="unidadeFederativa.id" label="unidadeFederativa" service="" optionLabelProperty="" optionProperty="" labelWidth="19%" width="81%"/>
		<adsm:combobox property="meioTransporte.id" label="tipoMeioTransporte" service="" optionLabelProperty="" optionProperty="" labelWidth="19%" width="81%"/>
		<adsm:lookup service="" dataType="text" property="filial.id" criteriaProperty="filial.codigo" label="proprietario" size="12" maxLength="14" action="/contratacaoVeiculos/manterProprietarios" labelWidth="19%" width="15%">
         	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/>
	    </adsm:lookup>
		<adsm:textbox dataType="text" property="nome" size="25" maxLength="50"  width="66%" disabled="true"/>
		<adsm:textbox dataType="date" property="periodoInicial" label="dataAnalise" labelWidth="19%" width="81%" required="true"/>
		<adsm:combobox property="classificacao.id" label="classificacao" service="" optionLabelProperty="" optionProperty="" labelWidth="19%" width="81%" prototypeValue="Rota|Proprietário|km - distância da rota"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton reportName="/freteCarreteiroViagem/emitirTabelao.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>