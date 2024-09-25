<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window
		service="lms.vendas.manterPropostasClienteProcAction">

	<adsm:form
			idProperty="idSimulacaoAnexo"
			action="/vendas/manterPropostasCliente"
			service="lms.vendas.manterPropostasClienteProcAction.findSimulacaoAnexo"
			onDataLoadCallBack="onDataLoadSimulacaoAnexo">
		<adsm:hidden
				property="simulacao.idSimulacao" />
		<adsm:textbox
				property="dsAnexo"
				dataType="text"
				label="descricao"
				size="60"
				labelWidth="20%"
				width="80%"
				required="true" />
		<adsm:textbox
				property="dsDocumento"
				dataType="file"
				blobColumnName="DS_DOCUMENTO"
				tableName="SIMULACAO_ANEXO"
				primaryKeyColumnName="ID_SIMULACAO_ANEXO"
				primaryKeyValueProperty="idSimulacaoAnexo"
				label="arquivo"
				size="60"
				labelWidth="20%"
				width="80%"
				required="true" />

		<adsm:buttonBar
				freeLayout="true">
			<adsm:storeButton
					caption="adicionar"
					id="btnAdicionar"
					service="lms.vendas.manterPropostasClienteProcAction.storeSimulacaoAnexo"
					callbackProperty="storeSimulacaoAnexo" />
			<adsm:resetButton
					caption="limpar"
					id="btnLimpar" />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
			property="simulacaoAnexo"
			idProperty="idSimulacaoAnexo"
			selectionMode="check"
			detailFrameName="anexo"
			unique="true"
			rows="12"
			autoSearch="false"
			defaultOrder="dhInclusao"
			service="lms.vendas.manterPropostasClienteProcAction.findSimulacaoAnexoList"
			rowCountService="lms.vendas.manterPropostasClienteProcAction.findSimulacaoAnexoRowCount">
		<adsm:gridColumn
				property="dsAnexo"
				title="nomeArquivo"
				width="70%" />
		<adsm:gridColumn
				property="dhInclusao"
				title="dataHoraDeInclusao"
				width="30%"
				dataType="JTDateTimeZone" />
		<adsm:buttonBar>
			<adsm:button
					caption="excluir"
					id="btnExcluir"
					buttonType="removeButton"
					onclick="removeSimulacaoAnexo();" />
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
<script type="text/javascript">

	var tabCad = getTabGroup(document).getTab("cad");

	function initWindow(event) {
		setElementValue("simulacao.idSimulacao", tabCad.getFormProperty("simulacao.idSimulacao"));
		simulacaoAnexoGridDef.selectionMode = isDisabled() ? 0 : 1;
		updateForm();
	}

	function isDisabled() {
		return tabCad.getFormProperty("filial.idFilial") != tabCad.getFormProperty("usuario.idFilial")
				|| tabCad.getFormProperty("tpSituacaoAprovacao.value") == "F" || tabCad.getFormProperty("tpSituacaoAprovacao.value") == "M" ;
	}

	function onDataLoadSimulacaoAnexo_cb(data, error) {
		onDataLoad_cb(data, error);
		disableButtons();
	}

	function updateForm() {
		var data = new Object();
		setNestedBeanPropertyValue(data, "simulacao.idSimulacao", tabCad.getFormProperty("simulacao.idSimulacao"));
		simulacaoAnexoGridDef.executeSearch(data);
		resetValue("dsAnexo");
		resetValue("dsDocumento");
		disableButtons();
	}

	function disableButtons() {
		var disabled = isDisabled();
		setDisabled("btnAdicionar", disabled);
		setDisabled("btnLimpar", disabled);
		setDisabled("btnExcluir", disabled);
		setDisabled("dsDocumento_browse", disabled);
		setDisplay("dsDocumento_delete", false);
		setDisplay(getElement("simulacaoAnexo.chkSelectAll").parentNode, !disabled);
	}

	function storeSimulacaoAnexo_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		showSuccessMessage();
		updateForm();
	}

	function removeSimulacaoAnexo() {
		simulacaoAnexoGridDef.removeByIds(
				"lms.vendas.manterPropostasClienteProcAction.removeByIdSimulacaoAnexo",
				"removeSimulacaoAnexo");
	}

	function removeSimulacaoAnexo_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		cleanButtonScript(document);
		showSuccessMessage();
		updateForm();
	}

</script>
