<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window
		service="lms.contasreceber.manterBoletosAction"
		onPageLoad="onPageLoadManterBoletosObservacao">
	<adsm:form
			action="/contasReceber/manterBoletos">

		<adsm:hidden
				property="idBoleto" />

		<adsm:combobox
				label="motivoOcorrencia"
				property="idMotivoOcorrencia"
				onlyActiveValues="true"
				boxWidth="320"
				labelWidth="20%"
				width="80%"
				optionLabelProperty="dsMotivoOcorrencia"
				optionProperty="idMotivoOcorrencia"
				service="lms.contasreceber.manterBoletosAction.findComboMotivoOcorrenciaCancelamento"
				required="true" />
		<adsm:textarea
				label="observacao"
				property="dsHistoricoBoleto"
				width="80%"
				maxLength="500"
				labelWidth="20%"
				columns="68"
				rows="5" />
		<adsm:combobox
				label="tipoManutencaoFatura"
				property="tpManutencaoFatura"
				domain="DM_TP_MANUTENCAO_FATURA"
				labelWidth="20%"
				width="80%"
				onchange="return onChangeTpManutencaoFatura(this);"
				required="true" />

		<adsm:listbox
				label="documentosServico"
				property="doctoServicoList"
				labelWidth="20%"
				width="80%"
				boxWidth="320"
				size="5"
				optionProperty="idDoctoServico"
				onContentChange="onContentChangeDoctoServicoList"
				required="true">
			<adsm:combobox
					property="tpDocumentoServico"
					optionProperty="value"
					optionLabelProperty="description"
					service="lms.contasreceber.manterFaturasAction.findTipoDocumentoServico"
					onchange="return onChangeTpDocumentoServico(this);">
				<adsm:lookup
						property="filial"
						idProperty="idFilial"
						criteriaProperty="sgFilial"
						dataType="text"
						action=""
						size="3"
						maxLength="3"
						exactMatch="true"
						picker="false"
						service="lms.contasreceber.manterBoletosAction.findLookupFilial"
						onDataLoadCallBack="onDataLoadFilial" />
				<adsm:lookup
						property="doctoServico"
						idProperty="idDoctoServico"
						dataType="integer"
						criteriaProperty="nrDoctoServico"
						action="/contasReceber/pesquisarDevedorDocServFatLookUp"
						service="lms.contasreceber.manterBoletosAction.findDoctoServico"
						cmd="pesq"
						size="11"
						maxLength="10"
						exactMatch="true"
						popupLabel="pesquisarDocumentoServico"
						onDataLoadCallBack="onDataLoadDoctoServico"
						onPopupSetValue="onPopupSetValueDoctoServico">
					<adsm:propertyMapping
							modelProperty="idBoleto"
							criteriaProperty="idBoleto" />
					<adsm:propertyMapping
							modelProperty="doctoServico.tpDocumentoServico"
							criteriaProperty="doctoServicoList_tpDocumentoServico"
							disable="false" />
					<adsm:propertyMapping
							modelProperty="doctoServico.filialByIdFilialOrigem.idFilial"
							criteriaProperty="doctoServicoList_filial.idFilial"
							disable="false" />
					<adsm:propertyMapping
							modelProperty="doctoServico.filialByIdFilialOrigem.sgFilial"
							criteriaProperty="doctoServicoList_filial.sgFilial" />
				</adsm:lookup>
			</adsm:combobox>
		</adsm:listbox>

		<adsm:buttonBar>
			<adsm:button
					id="btnConfirmar"
					caption="confirmar"
					service="lms.contasreceber.manterBoletosAction.cancelarBoleto"
					callbackProperty="cancelarBoleto" />
			<adsm:button
					id="btnCancelar"
					caption="cancelar"
					buttonType="reset"
					onclick="javascript:window.close();" />
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
<script src="/<%= request.getContextPath().substring(1) %>/lib/formatNrDocumento.js" type="text/javascript"></script>
<script type="text/javascript">

	function onPageLoadManterBoletosObservacao() {
		addServiceDataObject(createServiceDataObject(
				"lms.contasreceber.manterBoletosAction.findManterBoletosObservacao",
				"onPageLoadManterBoletosObservacao",
				{ idBoleto : window.dialogArguments.idBoleto }));
		onPageLoad();
	}

	function onPageLoadManterBoletosObservacao_cb(data, error) {
		if (error) {
			alert(error);
			return;
		}
		fillFormWithFormBeanData(0, data);
		setDisabledDoctoServicoList(data.tpManutencaoFatura != 'E');
	}

	function setDisabledDoctoServicoList(disabled) {
		getElement("doctoServicoList").disabled = disabled;
		setDisabled("doctoServicoList_tpDocumentoServico", disabled);
		setDisabled("doctoServicoList_doctoServico.idDoctoServico", disabled);
		setDisabled("doctoServicoList", disabled);
		setDisplay("doctoServicoList_Clean_a", !disabled);
		getElement("doctoServicoList").required = !disabled;
		disabled = disabled || !getElementValue("doctoServicoList_tpDocumentoServico");
		setDisabled("doctoServicoList_filial.idFilial", disabled);
		disabled = disabled || !getElementValue("doctoServicoList_filial.idFilial");
		setDisabled("doctoServicoList_doctoServico.nrDoctoServico", disabled);
		setDisabled("btnConfirmar", false);
		setDisabled("btnCancelar", false);
	}

	function onChangeTpManutencaoFatura(e) {
		var disabled = getElementValue("tpManutencaoFatura") != "E";
		setDisabledDoctoServicoList(disabled);
		if (disabled) {
			resetValue("doctoServicoList");
		}
		return comboboxChange({ e : e });
	}

	function onChangeTpDocumentoServico(e) {
		setDisabledDoctoServicoList(false);
		setMaskNrDocumento("doctoServicoList_doctoServico.nrDoctoServico", getElementValue("doctoServicoList_tpDocumentoServico"))
		return comboboxChange({ e : e });
	}

	function onDataLoadFilial_cb(data, errorMessage, errorCode, event) {
		if (data.length == 1) {
			__lookupSetValue({ e : getElement("doctoServicoList_filial.idFilial"), data : data[0] });
			setDisabledDoctoServicoList(false);
			setFocus("doctoServicoList_doctoServico.nrDoctoServico");
			return true;
		} else {
			alert(lookup_noRecordFound);
		}
	}

	function onPopupSetValueDoctoServico(data, dialogWindow) {
		if (data) {
			setElementValue("doctoServicoList_tpDocumentoServico", data.tpDocumentoServico);
			setElementValue("doctoServicoList_filial.idFilial", data.idFilialOrigem);
			setElementValue("doctoServicoList_filial.sgFilial", data.sgFilialOrigem);
			setElementValue("doctoServicoList_doctoServico.nrDoctoServico", data.nrDoctoServico);
			setDisabledDoctoServicoList(false);
			setFocus("doctoServicoList_doctoServico.nrDoctoServico");
			lookupChange({ e : getElement("doctoServicoList_doctoServico.idDoctoServico"), forceChange : true });
		}
	}

	function onDataLoadDoctoServico_cb(data, error) {
		if (error) {
			alert(error);
			getElement("doctoServicoList_doctoServico.nrDoctoServico").previousValue = undefined;
			return;
		}
		if (data.idDoctoServico) {
			__lookupSetValue({ e : getElement("doctoServicoList_doctoServico.idDoctoServico"), data : data });
			return true;
		} else {
			alert(lookup_noRecordFound);
		}
	}

	function onContentChangeDoctoServicoList() {
		var result = getElementValue("doctoServicoList_doctoServico.idDoctoServico") != "";
		return result;
	}

	function cancelarBoleto_cb(data, errorMessage, errorCode, event){
		if (errorMessage) {
			alert(errorMessage);
			return;
		}
		window.dialogArguments.onDataLoadCallBack(data, errorMessage, errorCode, event);
		window.close();
	}

</script>