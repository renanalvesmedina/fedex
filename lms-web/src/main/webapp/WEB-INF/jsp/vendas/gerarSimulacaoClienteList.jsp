<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/gerarSimulacaoCliente" >
		<adsm:lookup property="cliente.id" label="cliente" action="/vendas/manterDadosIdentificacao" service="" dataType="text" size="11" maxLength="11"  labelWidth="16%" width="40%" >
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="cliente.nome" />
			<adsm:textbox dataType="text" property="cliente.nome" disabled="true" size="30"/>
		</adsm:lookup>
		<adsm:combobox property="tabelaDivCliente.divisaoId" optionLabelProperty="value" optionProperty="0" service="" labelWidth="16%" width="14%" label="divisao" />
		<adsm:lookup property="tabelaDivCliente.tabelaId" label="tabela" action="/tabelaPrecos/manterTabelasPreco" service="" dataType="text" size="11" maxLength="15" labelWidth="16%" width="40%" >
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="tipoTabelaPreco.descricao" />
			<adsm:textbox dataType="text" property="tipoTabelaPreco.descricao" disabled="true" size="20"/>
		</adsm:lookup>
		<adsm:combobox property="tabelaDivCliente.servicoId" optionLabelProperty="value" optionProperty="0" service="" labelWidth="16%" width="14%" label="servico" />	
		<adsm:textbox dataType="text" property="numeroSimulacao" label="numeroSimulacao" labelWidth="16%" width="84%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="170" unique="true" rows="6">
		<adsm:gridColumn title="data" property="dataId" width="15%" />
		<adsm:gridColumn title="numeroSimulacao" property="numeroPropostaId" width="15%" />
		<adsm:gridColumn title="cliente" property="clienteId" width="45%" />
		<adsm:gridColumn title="tabela" property="tabelaId" width="15%" />
		<adsm:gridColumn title="aprovada" property="aprovadaId" width="10%" />
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
