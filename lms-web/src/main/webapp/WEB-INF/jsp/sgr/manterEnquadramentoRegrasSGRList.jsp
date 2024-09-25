<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.manterEnquadramentoRegrasSGRAction" onPageLoadCallBack="enquadramentoOnDataLoad">
	<adsm:form action="/sgr/manterEnquadramentoRegrasSGR">
		<adsm:textbox
			dataType="text"
			label="descricao"
			maxLength="100"
			property="dsEnquadramentoRegra"
			size="100"
			width="85%"
		/>
		<adsm:textbox
			dataType="JTDate"
			label="vigenteData"
			property="dtVigencia"
		/>
		<adsm:combobox
			label="moeda"
			onlyActiveValues="true"
			optionLabelProperty="siglaSimbolo"
			optionProperty="idMoeda"
			property="moeda.idMoeda"
			service="lms.sgr.manterEnquadramentoRegrasSGRAction.findMoeda"
		/>

		<adsm:combobox
			autoLoad="true"
			boxWidth="240"
			label="naturezaProduto"
			optionLabelProperty="dsNaturezaProduto"
			optionProperty="idNaturezaProduto"
			property="naturezaProduto.idNaturezaProduto"
			service="lms.expedicao.naturezaProdutoService.findOrderByDsNaturezaProduto"
		/>
		<adsm:combobox
			boxWidth="240"
			domain="DM_TIPO_OPERACAO_ENQ_REGRA"
			label="tipoOperacao"
			property="tpOperacao"
			renderOptions="true"
		/>
		<adsm:combobox
			boxWidth="240"
			domain="DM_ABRANGENCIA"
			label="abrangencia"
			property="tpAbrangencia"
			renderOptions="true"
		/>
		<adsm:combobox
			boxWidth="240"
			domain="DM_SIM_NAO"
			label="regraGeral"
			property="blRegraGeral"
		/>

		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			criteriaProperty="pessoa.nrIdentificacao"
			dataType="text"
			idProperty="idCliente"
			label="cliente"
			maxLength="18"
			property="cliente"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			serializable="true"
			service="lms.vendas.clienteService.findLookup"
			size="20"
			width="85%"
		>
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="cliente.pessoa.nmPessoa" />
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="cliente.pessoa.nmPessoa"
				maxLength="50"
				serializable="false"
				size="50"
			/>
		</adsm:lookup>

		<adsm:combobox
			boxWidth="240"
			domain="DM_CRITERIO_REGIAO_SGR"
			label="criterioOrigem"
			property="tpCriterioOrigem"
			renderOptions="true"
		/>
		<adsm:combobox
			boxWidth="240"
			domain="DM_CRITERIO_REGIAO_SGR"
			property="tpCriterioDestino"
			label="criterioDestino"
			renderOptions="true"
		/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="enquadramentoRegra" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		defaultOrder="dsEnquadramentoRegra"
		gridHeight="200"
		idProperty="idEnquadramentoRegra"
		property="enquadramentoRegra"
		rowCountService="lms.sgr.manterEnquadramentoRegrasSGRAction.getRowCountGridEnquadramento"
		rows="8"
		selectionMode="check"
		service="lms.sgr.manterEnquadramentoRegrasSGRAction.findPaginatedGridEnquadramento"
		unique="true"
	>
		<adsm:gridColumn property="dsEnquadramentoRegra" title="descricao" width="30%" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="moeda.sgMoeda" title="moeda" width="30" />
			<adsm:gridColumn property="moeda.dsSimbolo" title="" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="naturezaProduto.dsNaturezaProduto" title="naturezaProduto" width="14%" />
		<adsm:gridColumn align="center" property="dtVigencia" title="vigencia" width="140" />
		<adsm:gridColumn isDomain="true" property="tpOperacao" title="tipoOperacao" width="15%" />
		<adsm:gridColumn isDomain="true" property="tpAbrangencia" title="abrangencia" width="11%" />
		<adsm:gridColumn property="blRegraGeral" renderMode="image-check" title="regraGeral" width="6%" />

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	function enquadramentoOnDataLoad_cb(data, error) {
		getDataAtual();
	}

	function getDataAtual() {
		var sdo = createServiceDataObject("lms.sgr.manterEnquadramentoRegrasSGRAction.getDataAtual", "setDataAtual");
		xmit({ serviceDataObjects : [ sdo ] });
	}

	function setDataAtual_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		setElementValue("dtVigencia", setFormat("dtVigencia", data._value));
	}
</script>
