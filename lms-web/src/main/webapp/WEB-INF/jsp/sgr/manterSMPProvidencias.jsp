<%@ taglib prefix="adsm" uri="/WEB-INF/adsm.tld" %>
<adsm:window service="lms.sgr.manterSMPAction">
	<adsm:form action="/sgr/manterSMP" idProperty="idExigenciaSmp" service="lms.sgr.manterSMPAction.findExigenciaSmpById">
		<adsm:hidden property="solicMonitPreventivo.idSolicMonitPreventivo" />
		<adsm:hidden property="tpManutRegistro" />

		<adsm:textbox
			dataType="text"
			disabled="true"
			label="smp"
			labelWidth="20%"
			property="filial.sgFilial"
			size="3"
			width="80%"
		>
			<adsm:textbox dataType="integer" disabled="true" property="nrSmp" mask="00000000" size="8" />
		</adsm:textbox>
		<adsm:combobox
			label="tipoExigencia"
			labelWidth="20%"
			onchange="return onChangeTipoExigencia(this)"
			onlyActiveValues="true"
			optionLabelProperty="dsTipoExigenciaGerRisco"
			optionProperty="idTipoExigenciaGerRisco"
			property="exigenciaGerRisco.tipoExigenciaGerRisco.idTipoExigenciaGerRisco"
			required="true"
			service="lms.sgr.manterSMPAction.findTipoExigenciaGerRisco"
			width="80%"
		/>
		<adsm:combobox
			label="exigencia"
			labelWidth="20%"
			onchange="return exigenciaGerRiscoOnChange(this);"
			onlyActiveValues="true"
			optionLabelProperty="dsResumida"
			optionProperty="idExigenciaGerRisco"
			property="exigenciaGerRisco.idExigenciaGerRisco"
			required="true"
			service="lms.sgr.manterSMPAction.findExigenciaGerRisco"
			width="80%"
		>
			<adsm:propertyMapping criteriaProperty="exigenciaGerRisco.tipoExigenciaGerRisco.idTipoExigenciaGerRisco" modelProperty="tipoExigenciaGerRisco.idTipoExigenciaGerRisco" />
			<adsm:propertyMapping modelProperty="dsCompleta" relatedProperty="exigenciaGerRisco.dsCompleta" />
		</adsm:combobox>

		<adsm:textarea
			columns="100"
			disabled="true"
			label="descricaoCompleta"
			labelWidth="20%"
			maxLength="1000"
			property="exigenciaGerRisco.dsCompleta"
			rows="5"
			serializable="false"
			width="80%"
		/>
		<!-- LSM 6853 -->
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
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialInicio.pessoa.nmFantasia" />
			<adsm:textbox dataType="text" disabled="true" property="filialInicio.pessoa.nmFantasia" serializable="false" size="30" />
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
			<adsm:storeButton caption="salvarProvidencia" id="salvarProvidencia" service="lms.sgr.manterSMPAction.storeExigenciaSmp" />
			<adsm:newButton caption="limpar" id="novaProvidencia" />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		autoSearch="false"
		gridHeight="180"
		idProperty="idExigenciaSmp"
		onRowClick="populaForm"
		property="exigenciasSmp"
		rowCountService="lms.sgr.manterSMPAction.getRowCountExigenciaSmpByIdSmp"
		rows="4"
		selectionMode="true"
		service="lms.sgr.manterSMPAction.findPaginatedExigenciaSmpByIdSmp"
		unique="true"
	>
		<adsm:gridColumn property="exigenciaGerRisco.tipoExigenciaGerRisco.dsTipoExigenciaGerRisco" title="tipoExigencia" width="13%" />
		<adsm:gridColumn property="exigenciaGerRisco.dsResumida" title="exigencia" width="35%" />
		<adsm:gridColumn dataType="integer" property="qtExigida" title="qtde" width="5%" />
		<adsm:gridColumn align="center" property="filialInicio.sgFilial" title="filialIni" width="5%" />
		<adsm:gridColumn dataType="integer" property="vlKmFranquia" title="kmExigida" width="9%" />
		<adsm:gridColumn dataType="text" isDomain="true" property="tpManutRegistro" title="origem" width="14%" />
		<adsm:gridColumn
			align="center"
			image="/images/popup.gif"
			link="sgr/manterSMP.do?cmd=providenciasDet"
			linkIdProperty="exigenciaGerRisco.idExigenciaGerRisco"
			popupDimension="550,220"
			property="detalhe"
			title="detalhe"
			width="7%"
		/>
		<adsm:gridColumn
			align="center"
			image="/images/popup.gif"
			link="sgr/manterSMP.do?cmd=providenciasHist"
			linkIdProperty="idExigenciaSmp"
			popupDimension="550,220"
			property="historico"
			title="historico"
			width="7%"
		/>

		<adsm:buttonBar>
			<adsm:button buttonType="removeButton" caption="excluirProvidencia" onclick="excluirProvidenciaOnClick()" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	var exigenciasGerRisco = [];
	var smp = [];

	function excluirProvidenciaOnClick() {
		var idsMap = exigenciasSmpGridDef.getSelectedIds();
		if (idsMap.ids.length > 0) {
			if (window.confirm(erExcluir)) {
				var data = [];
				var idSMP = getElementValue("solicMonitPreventivo.idSolicMonitPreventivo");
				setNestedBeanPropertyValue(data, "idsExigenciasSMP", idsMap);
				setNestedBeanPropertyValue(data, "idSMP", idSMP);
				var sdo = createServiceDataObject(
						"lms.sgr.manterSMPAction.removeExigenciasSmp",
						"exigenciasSmpGridDef#removeByIds",
						data);
				xmit({ serviceDataObjects : [ sdo ] });
			}
		}
	}

	function onChangeTipoExigencia(combo) {
		var tipoExigencia = combo.value;
		var idTipoExigenciaGerRisco = getElementValue(combo);
		if (tipoExigencia != undefined && tipoExigencia != "") {
			var sdo = createServiceDataObject(
					"lms.sgr.manterSMPAction.tipoExigenciaGerRiscoOnChange",
					"tipoExigenciaGerRiscoOnChange",
					{ idTipoExigenciaGerRisco : parseInt(idTipoExigenciaGerRisco) });
			xmit({ serviceDataObjects : [ sdo ] });
			disableComboExigencia(false);
		} else {
			disableComboExigencia(true);
			disableQtExigida(true);
			updateExigenciasGerRisco([]);
		}
		return comboboxChange({ e : combo });
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
		disableAreaRisco(disabled);
		if (disabled) {
			resetValue("filialInicio.idFilial");
			resetValue("vlKmFranquia");
		}
	}

	function disableAreaRisco(disabled) {
		setDisabled("filialInicio.idFilial", disabled);
		setDisabled("vlKmFranquia", disabled);
		getElement("filialInicio.sgFilial").required = !disabled;
		getElement("vlKmFranquia").required = !disabled;
	}

	function disableComboExigencia(disable) {
		setDisabled(getElement("exigenciaGerRisco.idExigenciaGerRisco"), disable);
	}

	function disableComboTipoExigencia(disable) {
		setDisabled(getElement("exigenciaGerRisco.tipoExigenciaGerRisco.idTipoExigenciaGerRisco"), disable);
	}

	function disabledFieldOnLoad(valor) {
		var sdo = createServiceDataObject(
				"lms.sgr.manterSMPAction.loadExigenciaSMP",
				"disabledFields",
				{ idExigenciaSMP : parseInt(valor) });
		xmit({ serviceDataObjects : [ sdo ] });
	}

	function disabledFields_cb(data, error) {
		if (error) {
			alert(error);
			return;
		}
		disableQtExigida(!data.blExigeQuantidade);
		disableComboExigencia(false);
		disableComboTipoExigencia(false)
		if (data.blAreaRisco) {
			disableAreaRisco(false);
		} else {
			disableAreaRisco(true);
		}
		if (data.disabledAll) {
			disableAll(true);
		}
	}

	function disableAll(disabled) {
		setDisabled("exigenciaGerRisco.idExigenciaGerRisco", disabled);
		getElement("exigenciaGerRisco.idExigenciaGerRisco").required = !disabled;
		setDisabled("exigenciaGerRisco.tipoExigenciaGerRisco.idTipoExigenciaGerRisco", disabled);
		disableAreaRisco(disabled);
	}

	function initWindow(eventObj) {
		if (eventObj.name == "removeButton_grid" || eventObj.name == "newButton_click") {
			novaProvidencia();
		} else if (eventObj.name == "tab_click") {
			carregaSMP();
		} else if (eventObj.name == "storeButton") {
			populaGrid();
		}
	}

	function carregaSMP() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		var idSolicMonitPreventivo = tabDet.getFormProperty("idSolicMonitPreventivo");
		if (idSolicMonitPreventivo != undefined && idSolicMonitPreventivo != '') {
			var frame = parent.document.frames["cad_iframe"];
			var sgFilial = tabDet.getFormProperty("filial.sgFilial");
			var nrSmp = tabDet.getFormProperty("nrSmp");
			setElementValue("filial.sgFilial", sgFilial);
			setElementValue("nrSmp", nrSmp);
			setElementValue("solicMonitPreventivo.idSolicMonitPreventivo", idSolicMonitPreventivo);
			setNestedBeanPropertyValue(smp, "filial.sgFilial", sgFilial);
			setNestedBeanPropertyValue(smp, "nrSmp", nrSmp);
			setNestedBeanPropertyValue(smp, "solicMonitPreventivo.idSolicMonitPreventivo", idSolicMonitPreventivo);
			populaGrid();
			novaProvidencia();
		}
	}

	function populaGrid() {
		var map = [];
		var idSolicMonitPreventivo = getElementValue("solicMonitPreventivo.idSolicMonitPreventivo");
		setNestedBeanPropertyValue(map, "solicMonitPreventivo.idSolicMonitPreventivo", idSolicMonitPreventivo);
		exigenciasSmpGridDef.executeSearch(map);
	}

	function populaForm(valor) {
		onDataLoad(valor);
		populaDadosMaster();
		disabledFieldOnLoad(valor);
		return false;
	}

	function novaProvidencia() {
		populaDadosMaster();
		resetValue("exigenciaGerRisco.dsCompleta");
		setDefaultFieldsValues();
		disableComboExigencia(true);
		disableComboTipoExigencia(false);
		disableQtExigida(true);
		disableAreaRisco(true);
	}

	function populaDadosMaster() {
		setElementValue(
				"filial.sgFilial",
				getNestedBeanPropertyValue(smp, "filial.sgFilial"));
		setElementValue(
				"nrSmp",
				getNestedBeanPropertyValue(smp, "nrSmp"));
		setElementValue(
				"solicMonitPreventivo.idSolicMonitPreventivo",
				getNestedBeanPropertyValue(smp, "solicMonitPreventivo.idSolicMonitPreventivo"));
	}
</script>
