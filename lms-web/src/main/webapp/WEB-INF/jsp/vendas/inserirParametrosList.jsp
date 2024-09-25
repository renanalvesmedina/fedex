<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/vendas/inserirParametros" >
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
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" gridHeight="275" unique="true" scrollBars="horizontal" rows="6">
		<adsm:gridColumn title="ufOrigem2" property="ufOrigem" width="50" />
		<adsm:gridColumn title="filialOrigem" property="filialOrigem" width="50" />
		<adsm:gridColumn title="municipioOrigem" property="municipioOrigem" width="200" />
		<adsm:gridColumn title="aeroportoOrigem" property="aeroportoOrigem" width="60" />
		<adsm:gridColumn title="localizacaoOrigem" property="localizacaoOrigem" width="100" />
		<adsm:gridColumn title="ufDestino2" property="ufDestino" width="50" />
		<adsm:gridColumn title="filialDestino" property="filialDestino" width="50" />
		<adsm:gridColumn title="municipioDestino" property="municipioDestino" width="200" />
		<adsm:gridColumn title="aeroportoDestino" property="aeroportoDestino" width="60" />
		<adsm:gridColumn title="localizacaoDestino" property="localizacaoDestino" width="100" />
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
