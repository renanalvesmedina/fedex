<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/gerarSimulacaoCliente" >
		<adsm:textbox dataType="text" property="numeroSimulacao" label="numeroSimulacao" labelWidth="26%" width="74%" disabled="true" />
		<adsm:lookup property="cliente.id" label="cliente" action="/vendas/manterDadosIdentificacao" service="" dataType="text" size="11" maxLength="11" required="true"  labelWidth="26%" width="74%" >
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="cliente.nome" />
			<adsm:textbox dataType="text" property="cliente.nome" disabled="true" size="30"/>
		</adsm:lookup>
		<adsm:combobox property="tabelaDivCliente.divisaoId" optionLabelProperty="value" optionProperty="0" service="" labelWidth="26%" width="35%" label="divisao" />
		<adsm:combobox property="tabelaDivCliente.servicoId" optionLabelProperty="value" optionProperty="0" service="" labelWidth="16%" width="23%" label="servico" required="true" />
		<adsm:combobox property="formaInsercao" optionLabelProperty="value" optionProperty="0" service="" labelWidth="26%" width="35%" label="formaInsercaoDados" required="true" prototypeValue="Digitação|Importação|Histórico" />
		<adsm:combobox property="integranteFrete" optionLabelProperty="value" optionProperty="0" service="" labelWidth="16%" width="23%" label="integrante" required="true" prototypeValue="Remetente|Destinatário|Consignatário|Redespacho|Devedor" />
		<adsm:lookup property="cliente.id" label="clienteBase" action="/vendas/manterDadosIdentificacao" service="" dataType="text" size="11" maxLength="11"  labelWidth="26%" width="74%" >
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="cliente.nome" />
			<adsm:textbox dataType="text" property="cliente.nome" disabled="true" size="30"/>
		</adsm:lookup>
		<adsm:combobox property="tipoSimulacao" optionLabelProperty="value" optionProperty="0" service="" labelWidth="26%" width="74%" label="tipoSimulacao" required="true" prototypeValue="Simulação|Conferência" />
		<adsm:lookup action="/" service="" dataType="integer" property="funcionario.codigo" criteriaProperty="funcionario.nome" label="funcionario" size="5" maxLength="5" disabled="true" labelWidth="26%" width="35%">
		   	<adsm:propertyMapping modelProperty="funcionario.codigo" formProperty="funcionario.nome"/>
            <adsm:textbox dataType="text" property="funcionario.nome" size="30" maxLength="30" disabled="true"/>
        </adsm:lookup>
		<adsm:textbox dataType="dateTime" property="dataGeracao" label="dataGeracao" labelWidth="16%" width="23%" picker="true" disabled="true" />
		<adsm:textbox dataType="text" property="arquivoId" label="arquivoGerado" size="50" disabled="true" labelWidth="26%" width="74%" />

		<adsm:combobox label="agruparTotalizadoresPor" property="agruparTotalizadoresPor" optionLabelProperty="value" optionProperty="0" service="" labelWidth="26%" width="74%" required="true" prototypeValue="UF|Rota" />

		<adsm:checkbox property="tabelaAtualCliente" label="compararTabelaAtualCliente"  labelWidth="30%" width="70%" />
		<adsm:checkbox property="tabelaReferencial" label="compararTabelaReferencial" labelWidth="30%" width="70%" />
		<adsm:buttonBar lines="3" >
			<adsm:button caption="servicosAdicionais" boxWidth="130" onClick="showModalDialog('vendas/inserirParametrosServicosAdicionais.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:685px;dialogHeight:385px;');"/>
			<adsm:button caption="parametrosSimulacao" action="/vendas/inserirParametros" cmd="main"/>
			<adsm:button caption="digitacaoNFs" action="/vendas/gerarSimulacaoClienteDigitacaoNFs" cmd="main"/>
			<adsm:button caption="dadosHistorico" action="/vendas/gerarSimulacaoClienteDadosHistorico" cmd="main"/>
			<adsm:button caption="importacao" action="/vendas/gerarSimulacaoClienteImportacao" cmd="main"/>
			<adsm:reportViewerButton caption="imprimirConferencia" reportName="vendas/imprimirConferenciaFretesCliente.jasper" />
			<adsm:reportViewerButton caption="imprimirCompTabRef" reportName="vendas/imprimirResultados_1.jasper" />
			<adsm:reportViewerButton caption="imprimirCompTabAtual" reportName="vendas/imprimirResultados_2.jasper" />
			<adsm:reportViewerButton caption="executar" reportName="vendas/imprimirResultados.jasper" />
			<adsm:button caption="cancelar"/>
			<adsm:reportViewerButton caption="imprimirCompTabRefDigImp" reportName="vendas/imprimirResultadosDigitacao_1.jasper" />
			
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
