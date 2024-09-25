<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.manterProcedimentosFaixasValoresAction">
	<adsm:form
			action="/sgr/manterProcedimentosFaixasValores"
			height="180"
	>
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

		<adsm:hidden property="idExigenciaFaixaValor" />
		<adsm:hidden property="faixaDeValor.idFaixaDeValor" />

		<adsm:combobox
				label="tipoExigencia"
				labelWidth="20%"
				onchange="return tipoExigenciaGerRiscoOnChange(this);"
				onlyActiveValues="true"
				optionLabelProperty="dsTipoExigenciaGerRisco"
				optionProperty="idTipoExigenciaGerRisco"
				property="exigenciaGerRisco.tipoExigenciaGerRisco.idTipoExigenciaGerRisco"
				required="true"
				serializable="false"
				service="lms.sgr.tipoExigenciaGerRiscoService.findOrdenadoPorDescricao"
				width="80%"
		/>
		<adsm:combobox
				disabled="true"
				label="exigencia"
				labelWidth="20%"
				property="exigenciaGerRisco.idExigenciaGerRisco"
				onchange="return exigenciaGerRiscoOnChange(this);"
				onlyActiveValues="true"
				optionLabelProperty="dsResumida"
				optionProperty="idExigenciaGerRisco"
				required="true"
				width="80%"
		/>
		<adsm:textarea
				columns="100"
				disabled="true"
				label="descricaoCompleta"
				labelWidth="20%"
				maxLength="1000"
				property="exigenciaGerRisco.dsCompleta"
				rows="5"
				serializable="false"
				style="overflow: visible;"
				width="80%"
		/>
		<adsm:textbox
				dataType="integer"
				disabled="true"
				label="quantidade"
				labelWidth="20%"
				minValue="1"
				property="qtExigida"
				required="true"
				size="6"
				width="80%"
		/>
		<adsm:lookup
				action="/municipios/manterFiliais"
				criteriaProperty="sgFilial"
				dataType="text"
				disabled="true"
				idProperty="idFilial"
				label="filialDeInicio"
				labelWidth="20%"
				maxLength="3"
				property="filialInicio"
				required="true"
				service="lms.carregamento.carregarVeiculoDocumentosManifestoAction.findLookupFilial"
				size="3"
				width="80%"
		>
			<adsm:propertyMapping
					modelProperty="pessoa.nmFantasia"
					relatedProperty="filialInicio.pessoa.nmFantasia"
			/>
			<adsm:textbox
					dataType="text"
					disabled="true"
					property="filialInicio.pessoa.nmFantasia"
					serializable="false"
					size="30"
			/>
		</adsm:lookup>
		<adsm:textbox
				dataType="integer"
				disabled="true"
				label="kmExigida"
				labelWidth="20%"
				minValue="1"
				property="vlKmFranquia"
				required="true"
				size="6"
				width="80%"
		/>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton
					caption="salvarExigencia"
					service="lms.sgr.manterProcedimentosFaixasValoresAction.storeExigenciaFaixaValor"
			/>
			<adsm:resetButton caption="limpar" />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
			autoSearch="false"
			defaultOrder="exigenciaGerRisco_tipoExigenciaGerRisco_.dsTipoExigenciaGerRisco:asc,exigenciaGerRisco_.dsResumida"
			idProperty="idExigenciaFaixaValor"
			onRowClick="faixaValorExigenciasOnRowClick"
			paginatedService="lms.sgr.manterProcedimentosFaixasValoresAction.findPaginatedExigenciaFaixaValor"
			property="faixaValorExigencias"
			rowCountService="lms.sgr.manterProcedimentosFaixasValoresAction.getRowCountExigenciaFaixaValor"
			rows="6"
			selectionMode="check"
	>
		<adsm:gridColumn
				property="exigenciaGerRisco.tipoExigenciaGerRisco.dsTipoExigenciaGerRisco"
				title="tipoExigencia"
				width="20%"
		/>
		<adsm:gridColumn
				property="exigenciaGerRisco.dsResumida"
				title="exigencia"
				width="48%"
		/>
		<adsm:gridColumn
				dataType="integer"
				property="qtExigida"
				title="quantidade"
				width="10%"
		/>
		<adsm:gridColumn
				align="center"
				property="filialInicio.sgFilial"
				title="filialDeInicio"
				width="12%"
		/>
		<adsm:gridColumn
				dataType="integer"
				property="vlKmFranquia"
				title="kmExigida"
				width="10%"
		/>

		<adsm:buttonBar>
			<adsm:removeButton
					caption="excluirExigencia"
					service="lms.sgr.manterProcedimentosFaixasValoresAction.removeExigenciasFaixaValor"
			/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	function initWindow(event) {
		var tab = getTabGroup(document).getTab("cad");
		setElementValue("faixaDeValor.idFaixaDeValor", tab.getFormProperty("idFaixaDeValor"));
		setElementValue("enquadramentoRegra.dsEnquadramentoRegra", tab.getFormProperty("enquadramentoRegra.dsEnquadramentoRegra"));
		setElementValue("enquadramentoRegra.moeda.siglaSimbolo", tab.getFormProperty("enquadramentoRegra.moeda.siglaSimbolo"));
		setElementValue("vlLimiteMinimo", tab.getFormProperty("vlLimiteMinimo"));
		setElementValue("vlLimiteMaximo", tab.getFormProperty("vlLimiteMaximo"));
		updateFaixaValorExigencias();
		if (event.name === "storeButton") {
			cleanButtonScript(document);
			tipoExigenciaGerRiscoOnChange(getElement("exigenciaGerRisco.tipoExigenciaGerRisco.idTipoExigenciaGerRisco"));
		}
	}

	function updateFaixaValorExigencias() {
		faixaValorExigenciasGridDef.executeSearch({
			faixaDeValor : { idFaixaDeValor : getElementValue("faixaDeValor.idFaixaDeValor") }
		}, true);
	}

	function tipoExigenciaGerRiscoOnChange(element) {
		var idTipoExigenciaGerRisco = getElementValue(element);
		if (idTipoExigenciaGerRisco) {
			var sdo = createServiceDataObject(
					"lms.sgr.manterProcedimentosFaixasValoresAction.tipoExigenciaGerRiscoOnChange",
					"tipoExigenciaGerRiscoOnChange",
					{ idTipoExigenciaGerRisco : parseInt(idTipoExigenciaGerRisco) });
			xmit({ serviceDataObjects : [ sdo ] });
		} else {
	 		disableQtExigida(true);
	 		updateExigenciasGerRisco([]);
		}
		return comboboxChange({ e : element });
	}

	function tipoExigenciaGerRiscoOnChange_cb(data, error) {
		if (error) {
			alert(error);
			return;
		}
 		disableQtExigida(!data.blExigeQuantidade);
 		updateExigenciasGerRisco(data.exigenciasGerRisco);
	}

	function disableQtExigida(disabled) {
		setDisabled("qtExigida", disabled);
		getElement("qtExigida").required = !disabled;
		if (disabled) {
			resetValue("qtExigida");
		}
	}

	var exigenciasGerRisco = [];

	function updateExigenciasGerRisco(data) {
 		var disabled = !data.length;
 		setDisabled("exigenciaGerRisco.idExigenciaGerRisco", disabled);
 		getElement("exigenciaGerRisco.idExigenciaGerRisco").required = !disabled;
 		exigenciaGerRisco_idExigenciaGerRisco_cb(data);
 		exigenciasGerRisco = data;
 		resetValue("exigenciaGerRisco.dsCompleta");
 		disableBlAreaRisco(true);
	}

	function exigenciaGerRiscoOnChange(element) {
		var idExigenciaGerRisco = getElementValue(element);
		if (idExigenciaGerRisco) {
			var exigenciaGerRisco = exigenciasGerRisco[element.selectedIndex - 1];
			setElementValue("exigenciaGerRisco.dsCompleta", exigenciaGerRisco.dsCompleta);
			disableBlAreaRisco(!exigenciaGerRisco.blAreaRisco);
		} else {
			resetValue("exigenciaGerRisco.dsCompleta");
		}
		return comboboxChange({ e : element });
	}

	function disableBlAreaRisco(disabled) {
		setDisabled("filialInicio.idFilial", disabled);
		setDisabled("vlKmFranquia", disabled);
		getElement("filialInicio.sgFilial").required = !disabled;
		getElement("vlKmFranquia").required = !disabled;
		if (disabled) {
 			resetValue("filialInicio.idFilial");
			resetValue("vlKmFranquia");
		}
	}

	function faixaValorExigenciasOnRowClick(idExigenciaFaixaValor) {
		var sdo = createServiceDataObject(
				"lms.sgr.manterProcedimentosFaixasValoresAction.findExigenciaFaixaValor",
				"findExigenciaFaixaValor",
				{ idExigenciaFaixaValor : parseInt(idExigenciaFaixaValor) });
		xmit({ serviceDataObjects : [ sdo ] });
		return false;
	}

	function findExigenciaFaixaValor_cb(data, error) {
		if (error) {
			alert(error);
			return;
		}
		setElementValue("idExigenciaFaixaValor", data.idExigenciaFaixaValor);
		setElementValue("exigenciaGerRisco.tipoExigenciaGerRisco.idTipoExigenciaGerRisco", data.idTipoExigenciaGerRisco);
		tipoExigenciaGerRiscoOnChange_cb(data.tipoExigenciaGerRisco);
		setElementValue("exigenciaGerRisco.idExigenciaGerRisco", data.idExigenciaGerRisco);
		exigenciaGerRiscoOnChange(getElement("exigenciaGerRisco.idExigenciaGerRisco"));
		setElementValue("qtExigida", data.qtExigida);
		setElementValue("filialInicio.idFilial", data.idFilial);
		setElementValue("filialInicio.sgFilial", data.sgFilial);
		setElementValue("filialInicio.pessoa.nmFantasia", data.nmFantasia);
		setElementValue("vlKmFranquia", data.vlKmFranquia);
	}
</script>
