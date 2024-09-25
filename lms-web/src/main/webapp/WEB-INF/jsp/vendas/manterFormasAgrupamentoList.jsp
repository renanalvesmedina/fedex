<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.formaAgrupamentoService">

    <adsm:form action="/vendas/manterFormasAgrupamento">
		<adsm:lookup
			service="lms.vendas.clienteService.findLookup" 
			action="/vendas/manterDadosIdentificacao"
			dataType="text" 
			property="cliente" 
			idProperty="idCliente" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="cliente" 
			size="20" 
			maxLength="20" 
			labelWidth="15%" 
			width="50%" 
			exactMatch="false">
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.nmPessoa"
				modelProperty="pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				property="cliente.pessoa.nmPessoa"
				size="30"
				maxLength="50"
				disabled="true"/>
		</adsm:lookup>

		<adsm:combobox
			property="tpSituacao"
			label="situacao"
			domain="DM_STATUS"
			width="15%"
			labelWidth="15%"/>

		<adsm:textbox
			label="descricao"
			property="dsFormaAgrupamento"
			maxLength="60"
			dataType="text"
			size="63"
			labelWidth="15%" width="50%"/>

		<adsm:combobox
			property="blAutomatico"
			label="automatico"
			domain="DM_SIM_NAO"
			width="15%"
			labelWidth="15%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="formaAgrupamento"/>
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>
	<adsm:grid
		idProperty="idFormaAgrupamento"
		property="formaAgrupamento"
		gridHeight="200"
		unique="true"
		defaultOrder="dsFormaAgrupamento:asc"
		rows="13"
	    service="lms.vendas.manterFormasAgrupamentoAction.findPaginatedSpecific" 
	    rowCountService="lms.vendas.manterFormasAgrupamentoAction.getRowCountSpecific">
		<adsm:gridColumn title="cliente"    property="nmCliente" dataType="text" width=""/>
		<adsm:gridColumn title="prioridade" property="nrOrdemPrioridade" dataType="integer" width="70"/>
		<adsm:gridColumn title="descricao"  property="dsFormaAgrupamento" width="300"/>
		<adsm:gridColumn title="situacao"   property="tpSituacao" width="100" isDomain="true"/>
		<adsm:gridColumn title="automatico" property="blAutomatico" width="100" renderMode="image-check"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>