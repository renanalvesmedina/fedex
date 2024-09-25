<%@ taglib prefix="adsm" uri="/WEB-INF/adsm.tld" %>
<adsm:window service="lms.sgr.manterSMPAction">
	<adsm:form
		action="/sgr/manterSMP"
		idProperty="idSolicMonitPreventivo"
		onDataLoadCallBack="retornoCarregaDados"
		service="lms.sgr.manterSMPAction.findSMPById"
	>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="smp"
			labelWidth="20%"
			property="filial.sgFilial"
			size="3"
			width="80%"
		>
			<adsm:textbox dataType="integer" disabled="true" mask="00000000" property="nrSmp" size="8" />
		</adsm:textbox>
		<adsm:textbox
			dataType="JTDateTimeZone"
			disabled="true"
			label="dataGeracao"
			labelWidth="20%"
			picker="false"
			property="dhGeracao"
			width="80%"
		/>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="valor"
			labelWidth="20%"
			property="moedaPais.moeda.siglaSimbolo"
			size="6"
			width="80%"
		>
			<adsm:textbox dataType="currency" disabled="true" property="vlSmp" size="18" />
		</adsm:textbox>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="controleCargas"
			labelWidth="20%"
			picker="false"
			property="controleTrecho.controleCarga.filialByIdFilialOrigem.sgFilial"
			size="3"
			width="80%"
		>
			<adsm:textbox
				dataType="integer"
				disabled="true"
				mask="00000000"
				property="controleTrecho.controleCarga.nrControleCarga"
				size="8"
			/>
		</adsm:textbox>
		<adsm:combobox
			boxWidth="200"
			disabled="true"
			domain="DM_STATUS_SOLIC_MONIT_PREVENTIVO"
			label="statusSMP"
			labelWidth="20%"
			onlyActiveValues="true"
			property="tpStatusSmp"
			width="80%"
		/>
		<adsm:textbox
			label="meioTransporte"
			labelWidth="20%"
			width="80%"
			size="6"
			dataType="text"
			property="meioTransporteByIdMeioTransporte.nrFrota"
			disabled="true"
		>
			<adsm:textbox dataType="text" disabled="true" property="meioTransporteByIdMeioTransporte.nrIdentificador" size="20" />
		</adsm:textbox>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="semiReboque"
			labelWidth="20%"
			property="meioTransporteByIdMeioSemiReboque.nrFrota"
			size="6"
			width="80%"
		>
			<adsm:textbox dataType="text" disabled="true" property="meioTransporteByIdMeioSemiReboque.nrIdentificador" size="20" />
		</adsm:textbox>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="motorista"
			labelWidth="20%"
			property="motorista.pessoa.nmPessoa"
			size="50"
			width="80%"
		/>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="origem"
			labelWidth="20%"
			property="controleTrecho.filialByIdFilialOrigem.sgFilial"
			size="3"
			width="80%"
		>
			<adsm:textbox dataType="text" disabled="true" property="controleTrecho.filialByIdFilialOrigem.pessoa.nmFantasia" size="50" />
		</adsm:textbox>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="destino"
			labelWidth="20%"
			property="controleTrecho.filialByIdFilialDestino.sgFilial"
			size="3"
			width="80%"
		>
			<adsm:textbox dataType="text" disabled="true" property="controleTrecho.filialByIdFilialDestino.pessoa.nmFantasia" size="50" />
		</adsm:textbox>
		<adsm:textbox
			dataType="integer"
			disabled="true"
			label="rota"
			labelWidth="20%"
			property="controleTrecho.controleCarga.rotaIdaVolta.nrRota"
			size="4"
			width="80%"
		>
			<adsm:textbox dataType="text" disabled="true" property="controleTrecho.controleCarga.rotaIdaVolta.rota.dsRota" size="30" />
		</adsm:textbox>
		<adsm:textbox
			disabled="true"
			dataType="integer"
			label="anoNumeroSMPGR"
			labelWidth="20%"
			property="nrSmpAnoGR"
			size="4"
			width="80%"
		>
			<adsm:textbox dataType="integer" disabled="true" property="nrSmpGR" size="20" />
		</adsm:textbox>
		<adsm:combobox
			boxWidth="200"
			disabled="true"
			domain="DM_STATUS_SMP_GR"
			label="statusSMPGR"
			labelWidth="20%"
			onlyActiveValues="true"
			property="tpStatusSmpGR"
			width="80%"
		/>
		<adsm:textarea
			columns="80"
			disabled="true"
			label="observacaoGR"
			labelWidth="20%"
			maxLength="400"
			property="dsRetornoGR"
			rows="5"
			width="80%"
		/>

		<adsm:buttonBar>
			<adsm:reportViewerButton caption="visualizarSMP" id="idVisualizarSMP" service="lms.sgr.manterSMPAction" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	function initWindow(eventObj) {
		var event = eventObj.name;
		if (event == "removeButton" || event == "newButton_click") {
			desabilitaTabs(true);
		} else if (event == "gridRow_click") {
			desabilitaTabs(false);
		}
		setFocus(getElement("idVisualizarSMP"), true, true);
	}

	function desabilitaTabs(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("cad", disabled);
		tabGroup.setDisabledTab("providencias", disabled);
		tabGroup.setDisabledTab("eventos", disabled);
	}

	function retornoCarregaDados_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		onDataLoad_cb(data, error);
		if (getElementValue("controleTrecho.controleCarga.rotaIdaVolta.rota.dsRota") == "") {
			setElementValue(
					"controleTrecho.controleCarga.rotaIdaVolta.rota.dsRota",
					getNestedBeanPropertyValue(data, "controleCarga.rota.dsRota"));
		}
	}
</script>
