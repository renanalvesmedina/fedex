<%@ taglib prefix="adsm" uri="/WEB-INF/adsm.tld" %>
<adsm:window onPageLoadCallBack="pageLoad" service="lms.sgr.manterSMPAction">
	<adsm:form action="/sgr/manterSMP" idProperty="idSolicMonitPreventivo" height="143">
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" serializable="false" value="false" />
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" serializable="true" value="true" />

		<adsm:range label="periodo" labelWidth="20%" width="80%">
			<adsm:textbox dataType="JTDateTimeZone" picker="true" property="dataInicial" />
			<adsm:textbox dataType="JTDateTimeZone" picker="true" property="dataFinal" />
		</adsm:range>
		<adsm:lookup
			action="/municipios/manterFiliais"
			criteriaProperty="sgFilial"
			dataType="text"
			idProperty="idFilial"
			label="smp"
			labelWidth="20%"
			maxLength="3"
			picker="false"
			popupLabel="pesquisarFilial"
			property="solicMonitPreventivo.filial"
			service="lms.sgr.manterSMPAction.findLookupFilial"
			size="3"
			width="80%"
		>
			<adsm:textbox dataType="integer" mask="00000000" maxLength="8" property="solicMonitPreventivo.nrSmp" size="8" />
		</adsm:lookup>
		<adsm:combobox
			label="valor"
			labelWidth="20%"
			optionLabelProperty="siglaSimbolo"
			optionProperty="idMoeda"
			property="moeda.idMoeda"
			service="lms.sgr.manterSMPAction.findComboMoeda"
			width="80%"
		>
			<adsm:range>
				<adsm:textbox dataType="currency" maxLength="18" picker="true" property="vlFrotaMinimo" size="18" />
				<adsm:textbox dataType="currency" maxLength="18" picker="true" property="vlFrotaMaximo" size="18" />
			</adsm:range>
		</adsm:combobox>
		<adsm:lookup
			action="/municipios/manterFiliais"
			criteriaProperty="sgFilial"
			dataType="text"
			disabled="false"
			idProperty="idFilial"
			label="origemTrecho"
			labelWidth="20%"
			maxLength="3"
			property="controleTrecho.filialByIdFilialOrigem"
			service="lms.sgr.manterSMPAction.findLookupFilial"
			size="3"
			width="80%"
		>
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado" />
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="controleTrecho.filialByIdFilialOrigem.pessoa.nmFantasia" />
			<adsm:textbox dataType="text" disabled="true" property="controleTrecho.filialByIdFilialOrigem.pessoa.nmFantasia" size="50" />
		</adsm:lookup>
		<adsm:lookup
			action="/municipios/manterFiliais"
			criteriaProperty="sgFilial"
			dataType="text"
			idProperty="idFilial"
			label="destinoTrecho"
			labelWidth="20%"
			maxLength="3"
			property="controleTrecho.filialByIdFilialDestino"
			service="lms.sgr.manterSMPAction.findLookupFilial"
			size="3"
			width="80%"
		>
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado" />
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="controleTrecho.filialByIdFilialDestino.pessoa.nmFantasia" />
			<adsm:textbox dataType="text" disabled="true" property="controleTrecho.filialByIdFilialDestino.pessoa.nmFantasia" size="50" />
		</adsm:lookup>
		<adsm:lookup
			action="/contratacaoVeiculos/manterMeiosTransporte"
			criteriaProperty="nrFrota"
			dataType="text"
			idProperty="idMeioTransporte"
			label="meioTransporte"
			labelWidth="20%"
			maxLength="6"
			picker="false"
			property="meioTransporteByIdTransportado2"
			serializable="false"
			service="lms.sgr.manterSMPAction.findLookupMeioTransporte"
			size="6"
			width="80%"
		>
			<adsm:propertyMapping criteriaProperty="meioTransporteByIdTransportado.nrIdentificador" disable="false" modelProperty="nrIdentificador" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />
			<adsm:propertyMapping disable="false" modelProperty="nrIdentificador" relatedProperty="meioTransporteByIdTransportado.nrIdentificador" />
			<adsm:lookup
				action="/contratacaoVeiculos/manterMeiosTransporte"
				criteriaProperty="nrIdentificador"
				dataType="text"
				idProperty="idMeioTransporte"
				maxLength="20"
				picker="true"
				property="meioTransporteByIdTransportado"
				service="lms.sgr.manterSMPAction.findLookupMeioTransporte"
				size="20"
			>
				<adsm:propertyMapping criteriaProperty="meioTransporteByIdTransportado2.nrFrota" modelProperty="nrFrota" />
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdTransportado2.idMeioTransporte" />
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdTransportado2.nrFrota" />
			</adsm:lookup>
		</adsm:lookup>
		<adsm:lookup
			action="/municipios/manterFiliais"
			criteriaProperty="sgFilial"
			dataType="text"
			idProperty="idFilial"
			label="controleCargas"
			labelWidth="20%"
			maxLength="3"
			onchange="return sgFilialOnChangeHandler();"
			onDataLoadCallBack="disableNrControleCarga"
			picker="false"
			popupLabel="pesquisarFilial"
			property="controleCarga.filialByIdFilialOrigem"
			serializable="false"
			service="lms.sgr.manterSMPAction.findLookupFilial"
			size="3"
			width="80%"
		>
			<adsm:lookup
				action="/carregamento/manterControleCargas"
				criteriaProperty="nrControleCarga"
				dataType="integer"
				idProperty="idControleCarga"
				onPopupSetValue="onControleCargaPopupSetValue"
				popupLabel="pesquisarControleCarga"
				property="controleCarga"
				mask="00000000"
				maxLength="8"
				service="lms.sgr.manterSMPAction.findControleCarga"
				size="8"
			>
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial" />
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:combobox
			property="solicMonitPreventivo.tpStatusSmp"
			label="statusSMP"
			domain="DM_STATUS_SOLIC_MONIT_PREVENTIVO"
			labelWidth="20%"
			width="80%"
			renderOptions="true"
		/>
		
		
		<adsm:combobox
			label="tipoExigencia"
			labelWidth="20%"
			optionLabelProperty="dsTipoExigenciaGerRisco"
			optionProperty="idTipoExigenciaGerRisco"
			onchange="return onChangeTipoExigencia(this)"
			property="tipoExigenciaGerRisco.idTipoExigenciaGerRisco"
			service="lms.sgr.manterSMPAction.findTipoExigenciaGerRisco"
			width="80%"
		/>
		<adsm:combobox
			label="exigencia"
			labelWidth="20%"
			optionLabelProperty="dsResumida"
			optionProperty="idExigenciaGerRisco"
			property="exigenciaGerRisco.idExigenciaGerRisco"
			service="lms.sgr.manterSMPAction.findExigenciaGerRisco"
			width="80%"
		>
			<adsm:propertyMapping criteriaProperty="tipoExigenciaGerRisco.idTipoExigenciaGerRisco" modelProperty="tipoExigenciaGerRisco.idTipoExigenciaGerRisco" />
		</adsm:combobox>
		<adsm:lookup
			action="/municipios/consultarRotas"
			cmd="idaVolta"
			criteriaProperty="nrRota"
			dataType="integer"
			exactMatch="true"
			idProperty="idRotaIdaVolta"
			label="rota"
			labelWidth="20%"
			maxLength="4"
			property="rotaIdaVolta"
			service="lms.sgr.manterSMPAction.findLookupRotaIdaVolta"
			size="4"
			width="80%"
		>
			<adsm:propertyMapping modelProperty="rota.dsRota" relatedProperty="rotaIdaVolta.rota.dsRota" />
			<adsm:textbox dataType="text" disabled="true" property="rotaIdaVolta.rota.dsRota" serializable="false" size="30" />
		</adsm:lookup>
		<adsm:textbox
			dataType="integer"
			label="anoNumeroSMPGR"
			labelWidth="20%"
			minValue="2000"
			property="solicMonitPreventivo.nrSmpAnoGR"
			size="4"
			width="80%"
		>
			<adsm:textbox dataType="integer" minValue="1" property="solicMonitPreventivo.nrSmpGR" size="20" />
		</adsm:textbox>
		<adsm:combobox
			property="solicMonitPreventivo.tpStatusSmpGR"
			label="statusSMPGR"
			domain="DM_STATUS_SMP_GR"
			labelWidth="20%"
			width="80%"
			renderOptions="true"
		/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="solicMonitPreventivos" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		gridHeight="128"
		idProperty="idSolicMonitPreventivo"
		onRowClick="listRowClick"
		property="solicMonitPreventivos"
		rowCountService="lms.sgr.manterSMPAction.getRowCountSMP"
		rows="6"
		scrollBars="horizontal"
		selectionMode="true"
		service="lms.sgr.manterSMPAction.findPaginatedSMP"
		unique="true"
	>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn dataType="text" property="filial.sgFilial" title="smp" width="20" />
			<adsm:gridColumn dataType="integer" mask="00000000" property="nrSmp" title="" width="50" />
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn property="controleTrecho.controleCarga.filialByIdFilialOrigem.sgFilial" title="controleCargas" width="20" />
			<adsm:gridColumn dataType="integer" property="controleTrecho.controleCarga.nrControleCarga" mask="00000000" title="" width="50" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn align="center" dataType="JTDateTimeZone" property="dhGeracao" title="dataGeracao" width="120" />
		<adsm:gridColumn dataType="text" property="moedaPais.moeda.siglaSimbolo" title="valor" width="40" />
		<adsm:gridColumn align="right" dataType="decimal" property="vlSmp" title="" width="70" />
		<adsm:gridColumn property="meioTransporteByIdMeioTransporte.nrFrota" title="meioTransporte" width="40" />
		<adsm:gridColumn property="meioTransporteByIdMeioTransporte.nrIdentificador" title="" width="60" />
		<adsm:gridColumn align="center" property="controleTrecho.filialByIdFilialOrigem.sgFilial" title="origem" width="50" />
		<adsm:gridColumn align="center" property="controleTrecho.filialByIdFilialDestino.sgFilial" title="destino" width="50" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn dataType="integer" mask="0000" property="nrSmpAnoGR" title="anoNumeroSMPGR" width="10" />
			<adsm:gridColumn dataType="integer"  property="nrSmpGR" title="" width="70" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn dataType="text" isDomain="true" property="tpStatusSmpGR" title="statusSMPGR" width="110" />
		<adsm:gridColumn
			align="center"
			image="/images/printer.gif"
			linkIdProperty="idSolicMonitPreventivo"
			property="visualizarSMP"
			reportName="lms.sgr.manterSMPAction"
			title="visualizarSMP"
			width="60"
		/>
		<adsm:gridColumn
			align="center"
			image="images/popup.gif"
			link="/sgr/consultarConteudosVeiculoManifestos.do?cmd=main"
			linkIdProperty="idSolicMonitPreventivo"
			property="conteudoVeiculo"
			title="conteudoVeiculo"
			width="90"
		/>

		<adsm:buttonBar freeLayout="false">
			<adsm:button buttonType="closeButton" caption="fechar" id="btnFechar" onclick="self.close();" />
		</adsm:buttonBar>
		<adsm:buttonBar>
			<adsm:button buttonType="button" caption="reenviarSMP" id="bt_reenviar_smp" onclick="reenviarSmpOnClick()" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	function reenviarSmpOnClick() {
		var idsMap = solicMonitPreventivosGridDef.getSelectedIds();
		if (idsMap.ids.length > 0) {
			var data = new Array();
			setNestedBeanPropertyValue(data, "idsSMP", idsMap);
			var sdo = createServiceDataObject("lms.sgr.manterSMPAction.storeReenviarSmp", "disabledEnviar", data);
			xmit({ serviceDataObjects : [ sdo ] });
		}
	}

	function disabledEnviar_cb() {
		
	}

	function pageLoad_cb(data, error) {
		onPageLoad_cb(data, error);
		showHideCloseButton();
		disableNrControleCarga(true);
	}

	function showHideCloseButton() {
		var isLookup = window.dialogArguments && window.dialogArguments.window;
		if (isLookup) {
			setDisabled('btnFechar', false);
		} else {
			setVisibility('btnFechar', false);
		}
	}

	function initWindow(event) {
		if (event.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			if (tabGroup != null) {
				tabGroup.setDisabledTab("cad", true);
				tabGroup.setDisabledTab("providencias", true);
				tabGroup.setDisabledTab("eventos", true);
			}
		}
		if (event.name == "tab_load") {
			setElementValue('blMostrarCancelados', false);
		}
		disableNrControleCarga(true);
		disableComboExigencia(true);
		if (event.name == "tab_load" || event.name == 'cleanButton_click') {
			loadDadosSessao();
		}
	}

	//Chama o servico que retorna os dados do usuario logado 
	function loadDadosSessao() {
		var data = new Array();
		var sdo = createServiceDataObject("lms.sgr.manterSMPAction.findDadosSessao", "preencheDadosSessao", data);
		xmit({ serviceDataObjects : [ sdo ] });
	}

	//Funcao de callback do servico que retorna os dados do usuario logado. 
	function preencheDadosSessao_cb(data, exception) {
	}

	function onControleCargaPopupSetValue(data) {
		setDisabled('controleCarga.nrControleCarga', false);
		setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', data.filialByIdFilialOrigem.sgFilial);
		setElementValue('controleCarga.filialByIdFilialOrigem.idFilial', data.filialByIdFilialOrigem.idFilial);
	}

	function onChangeTipoExigencia(combo) {
		var tipoExigencia = combo.value;
		if (tipoExigencia != undefined && tipoExigencia != "") {
			disableComboExigencia(false);
		} else {
			disableComboExigencia(true);
		}
		return comboboxChange({ e : combo });
	}

	function disableComboExigencia(disable) {
		var e = getElement("exigenciaGerRisco.idExigenciaGerRisco")
		setDisabled(e, disable);
		e.required = String(!disable);
	}

	/**
	 * Controla o objeto de controle carga
	 */
	function sgFilialOnChangeHandler() {
		if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial") == "") {
			disableNrControleCarga(true);
			resetValue("controleCarga.idControleCarga");
		} else {
			disableNrControleCarga(false);
		}
		return lookupChange({ e : document.forms[0].elements["controleCarga.filialByIdFilialOrigem.idFilial"] });
	}

	function disableNrControleCarga_cb(data, error) {
		if (data.length == 0) {
			disableNrControleCarga(false);
		}
		return lookupExactMatch({
			e : getElement("controleCarga.filialByIdFilialOrigem.idFilial"),
			data : data
		});
	}

	function disableNrControleCarga(disable) {
		if (disable == true) {
			if (getElementValue("controleCarga.nrControleCarga") != "")
				return;
		}
		setDisabled('controleCarga.nrControleCarga', disable);
	}

	function listRowClick() {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("cad", false);
	}
</script>
