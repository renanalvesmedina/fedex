<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/gerarSimulacaoCliente" height="370">
		<adsm:combobox property="periodicidadeFaturamento" optionLabelProperty="value" optionProperty="0" service="" labelWidth="26%" width="36%" label="periodicidadeFaturamento" prototypeValue="Diário|Semanal|Decendial|Quinzenal|Mensal" />
		<adsm:textbox dataType="integer" property="prazoPagamento" label="prazoPagamento" size="3" labelWidth="22%" width="15%" unit="dias" />
		<adsm:textbox dataType="date" property="dataDeValidadeProposta" label="dataValidadeProposta" labelWidth="26%" width="70%" picker="true" />
		<adsm:textbox dataType="date" property="aceiteCliente" label="dataAceitacaoCliente" labelWidth="26%" width="36%" picker="true" />
		<adsm:textbox dataType="date" property="data_Inicial" label="dataInicioVigencia" labelWidth="22%" width="15%" picker="true" />
		<adsm:lookup action="/" service="" dataType="integer" property="funcionario.codigo" criteriaProperty="funcionario.nome" label="funcionarioAprovador" size="5" maxLength="5" disabled="true" labelWidth="26%" width="36%">
		   	<adsm:propertyMapping modelProperty="funcionario.codigo" formProperty="funcionario.nome"/>
            <adsm:textbox dataType="text" property="funcionario.nome" size="30" maxLength="30"/>
        </adsm:lookup>
		<adsm:textbox dataType="date" property="aprovacao" label="dataAprovacao" labelWidth="22%" width="15%" disabled="true" picker="true" />
    	<adsm:textbox dataType="text" property="percentualreentrega" label="percentualReentrega" labelWidth="26%" width="36%" />
		<adsm:textbox dataType="date" property="dataEmissaoTabela" label="dataEmissaoTabela" labelWidth="22%" width="15%" disabled="true" picker="true" />
		<adsm:combobox property="situacaoAprovacao" optionLabelProperty="value" optionProperty="0" service="" labelWidth="26%" width="36%" label="situacaoAprovacao" prototypeValue="Aprovado|Em aprovação|Cancelado|Visualizado" disabled="true"/>
		
		<adsm:buttonBar>	
			<adsm:button caption="historicoAprovacao" onClick="showModalDialog('vendas/consultarHistoricoAprovacao.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:400px;');"/>
			<adsm:button caption="aprovacao" />			
			<adsm:reportViewerButton caption="imprimirProposta" reportName="vendas/imprimirTabelaProposta.jasper" />
			<adsm:button caption="efetivarProposta" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   