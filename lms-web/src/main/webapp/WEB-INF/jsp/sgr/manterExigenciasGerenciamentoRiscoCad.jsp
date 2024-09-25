<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.manterExigenciasGerenciamentoRiscoAction">
	<adsm:form action="/sgr/manterExigenciasGerenciamentoRisco" idProperty="idExigenciaGerRisco" onDataLoadCallBack="formOnDataLoad">
		<adsm:hidden property="nrNivel" />

		<adsm:combobox
			label="tipoExigencia"
			labelWidth="20%"
			onchange="return tipoExigenciaGerRiscoOnChange(this);"
			onlyActiveValues="true"
			optionLabelProperty="dsTipoExigenciaGerRisco"
			optionProperty="idTipoExigenciaGerRisco"
			property="tipoExigenciaGerRisco.idTipoExigenciaGerRisco"
			required="true"
			service="lms.sgr.tipoExigenciaGerRiscoService.findOrdenadoPorDescricao"
			width="80%"
		/>
		<adsm:textbox
			dataType="text"
			label="descricaoResumida"
			labelWidth="20%"
			maxLength="60"
			property="dsResumida"
			required="true"
			size="70"
			width="80%"
		/>
		<adsm:textarea
			columns="70"
			label="descricaoCompleta"
			labelWidth="20%"
			maxLength="1000"
			property="dsCompleta"
			rows="5"
			width="80%"
		/>
		<adsm:combobox
			domain="DM_STATUS"
			label="situacao"
			labelWidth="20%"
			property="tpSituacao"
			renderOptions="true"
			required="true"
			width="80%"
		/>
		<adsm:combobox
			domain="DM_TIPO_CRITERIO_AGRUPAMENTO"
			label="criterioAgrupamento"
			labelWidth="20%"
			property="tpCriterioAgrupamento"
			renderOptions="true"
			required="true"
			width="80%"
		/>
		<adsm:checkbox
			label="utilizaAreaRisco"
			labelWidth="20%"
			property="blAreaRisco"
			width="80%"
		/>
		<adsm:listbox
			boxWidth="200"
			label="rastreadorPerifericos"
			labelWidth="20%"
			orderProperty="dsPerifericoRastreador"
			optionProperty="idPerifericoRastreador"
			property="perifericosRastreador"
			size="5"
			width="80%"
		>
			<adsm:combobox
				boxWidth="200"
				disabled="true"
				optionLabelProperty="dsPerifericoRastreador"
				optionProperty="idPerifericoRastreador"
				property="perifericosRastreador.idPerifericoRastreador"
				service="lms.contratacaoveiculos.manterMeiosTransporteAction.findPerifericoRastreador"
			/>
		</adsm:listbox>
		<adsm:textbox
			dataType="text"
			label="identificador"
			labelWidth="20%"
			maxLength="30"
			onchange="return cdExigenciaGerRiscoOnChange(this);"
			property="cdExigenciaGerRisco"
			size="30"
			width="80%"
		/>

		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	function formOnDataLoad_cb(data, error) {
		if (error) {
			alert(error);
			return;
		}
		onDataLoad_cb(data, error);
		tipoExigenciaGerRiscoOnChange(getElement("tipoExigenciaGerRisco.idTipoExigenciaGerRisco"));
	}

	function tipoExigenciaGerRiscoOnChange(element) {
		var idTipoExigenciaGerRisco = getElementValue(element);
		if (idTipoExigenciaGerRisco) {
			var sdo = createServiceDataObject(
					"lms.sgr.manterExigenciasGerenciamentoRiscoAction.tipoExigenciaGerRiscoOnChange",
					"tipoExigenciaGerRiscoOnChange",
					{ idTipoExigenciaGerRisco : parseInt(idTipoExigenciaGerRisco) });
			xmit({ serviceDataObjects : [ sdo ] });
		}
		return comboboxChange({ e : element });
	}

	function tipoExigenciaGerRiscoOnChange_cb(data, error) {
		if (error) {
			alert(error);
			return;
		}
		disablePerifericosRastreador(data.disablePerifericosRastreador);
	}

	function disablePerifericosRastreador(disabled) {
		setDisabled("perifericosRastreador", disabled);
		setDisabled("perifericosRastreador_perifericosRastreador.idPerifericoRastreador", disabled);
		if (disabled) {
			resetValue("perifericosRastreador");
			resetValue("perifericosRastreador_perifericosRastreador.idPerifericoRastreador");
		}
	}

	function cdExigenciaGerRiscoOnChange(element) {
		var cdExigenciaGerRisco = getElementValue(element);
		setElementValue(element, cdExigenciaGerRisco.replace(/\s/g, ""));
		return true;
	}
</script>
