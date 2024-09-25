<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.manterProcedimentosFaixasValoresAction">
	<adsm:form action="/sgr/manterProcedimentosFaixasValores">
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="enquadramentoRegra"
			labelWidth="20%"
			property="enquadramentoRegra.dsEnquadramentoRegra"
			serializable="false"
			size="100"
			width="80%"
		/>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="limites"
			labelWidth="20%"
			property="enquadramentoRegra.moeda.siglaSimbolo"
			serializable="false"
			size="8"
			width="8%"
		/>
		<adsm:range width="72%">
			<adsm:textbox
				dataType="currency"
				disabled="true"
				mask="#,###,###,##0.00"
				property="vlLimiteMinimo"
				serializable="false"
				size="15"
			/>
			<adsm:textbox
				dataType="currency"
				disabled="true"
				mask="#,###,###,##0.00"
				property="vlLimiteMaximo"
				serializable="false"
				size="15"
			/>
		</adsm:range>

		<adsm:hidden property="idFaixaValorNatureza" />
		<adsm:hidden property="faixaDeValor.idFaixaDeValor" />

		<adsm:combobox
			label="naturezaProduto"
			labelWidth="20%"
			onlyActiveValues="true"
			optionLabelProperty="dsNaturezaProduto"
			optionProperty="idNaturezaProduto"
			property="naturezaProduto.idNaturezaProduto"
			required="true"
			service="lms.expedicao.naturezaProdutoService.find"
			width="80%"
		/>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="limitePermitido"
			labelWidth="20%"
			property="faixaValorNatureza.moeda.siglaSimbolo"
			serializable="false"
			size="8"
			width="8%"
		/>
		<adsm:textbox
			dataType="currency"
			mask="#,###,###,##0.00"
			property="vlLimitePermitido"
			size="15"
			width="72%"
		/>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton
				caption="salvarNatureza"
				service="lms.sgr.manterProcedimentosFaixasValoresAction.storeFaixaValorNaturezaImpedida"
			/>
			<adsm:resetButton caption="limpar" />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		autoSearch="false"
		defaultOrder="naturezaProduto_.dsNaturezaProduto"
		idProperty="idFaixaValorNatureza"
		onPopulateRow="populateRowFaixaValorNaturezasImpedidas"
		onRowClick="faixaValorNaturezasImpedidasOnRowClick"
		paginatedService="lms.sgr.manterProcedimentosFaixasValoresAction.findPaginatedFaixaValorNaturezaImpedida"
		property="faixaValorNaturezasImpedidas"
		rowCountService="lms.sgr.manterProcedimentosFaixasValoresAction.getRowCountFaixaValorNaturezaImpedida"
		rows="12"
		selectionMode="check"
	>
		<adsm:gridColumn property="naturezaProduto.dsNaturezaProduto" title="natureza" width="70%" />
		<adsm:gridColumn dataType="text" property="moeda.siglaSimbolo" title="moeda" width="10%" />
		<adsm:gridColumn align="right" dataType="currency" property="vlLimitePermitido" title="limitePermitido" width="20%" />

		<adsm:buttonBar>
			<adsm:removeButton
				caption="excluirNatureza"
				service="lms.sgr.manterProcedimentosFaixasValoresAction.removeFaixaValorNaturezaImpedida"
			/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	function initWindow(event) {
		var tab = getTabGroup(document).getTab("cad");
		setElementValue("faixaDeValor.idFaixaDeValor", tab.getFormProperty("idFaixaDeValor"));
		setElementValue("enquadramentoRegra.dsEnquadramentoRegra", tab.getFormProperty("enquadramentoRegra.dsEnquadramentoRegra"));
		var siglaSimbolo = tab.getFormProperty("enquadramentoRegra.moeda.siglaSimbolo");
		setElementValue("enquadramentoRegra.moeda.siglaSimbolo", siglaSimbolo);
		setElementValue("faixaValorNatureza.moeda.siglaSimbolo", siglaSimbolo);
		setElementValue("vlLimiteMinimo", tab.getFormProperty("vlLimiteMinimo"));
		setElementValue("vlLimiteMaximo", tab.getFormProperty("vlLimiteMaximo"));
		updateFaixaValorNaturezasImpedidas();
		if (event.name === "storeButton") {
			cleanButtonScript(document);
		}
	}

	function updateFaixaValorNaturezasImpedidas() {
		faixaValorNaturezasImpedidasGridDef.executeSearch({
			faixaDeValor : { idFaixaDeValor : getElementValue("faixaDeValor.idFaixaDeValor") }
		}, true);
	}

	function populateRowFaixaValorNaturezasImpedidas(tableRow, dataRow) {
		var siglaSimbolo = getElementValue("enquadramentoRegra.moeda.siglaSimbolo");
		tableRow.cells[2].innerText = siglaSimbolo;
	}

	function faixaValorNaturezasImpedidasOnRowClick(idFaixaValorNatureza) {
		var sdo = createServiceDataObject(
				"lms.sgr.manterProcedimentosFaixasValoresAction.findFaixaValorNaturezaImpedida",
				"findFaixaValorNaturezaImpedida", { idFaixaValorNatureza : parseInt(idFaixaValorNatureza) });
		xmit({ serviceDataObjects : [ sdo ] });
		return false;
	}

	function findFaixaValorNaturezaImpedida_cb(data, error) {
		if (error) {
			alert(error);
			return;
		}
		setElementValue("idFaixaValorNatureza", data.idFaixaValorNatureza);
		setElementValue("naturezaProduto.idNaturezaProduto", data.idNaturezaProduto);
		setElementValue("vlLimitePermitido", setFormat("vlLimitePermitido", data.vlLimitePermitido));
	}
</script>
