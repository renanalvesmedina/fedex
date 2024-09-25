<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterClienteAction"
	onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:i18nLabels>
		<adsm:include key="cliente"/>
	</adsm:i18nLabels>

	<adsm:form
		action="/vendas/manterDadosIdentificacao"
		id="formComercial"
		idProperty="idCliente"
		height="190">

		<adsm:hidden property="pessoa.tpPessoa" serializable="false"/>
		<adsm:hidden property="pessoa.tpIdentificacao" serializable="false"/>
		<adsm:hidden property="pessoa.nrIdentificacao" serializable="false"/>
		<adsm:hidden property="pessoa.nmPessoa" serializable="false"/>
		<adsm:hidden property="origem" value="param"/>
		<adsm:hidden property="temPendenciaWKFilialCom" serializable="false"/>
		<adsm:hidden property="tpAcesso" value="A" serializable="false"/>

		<adsm:lookup
			label="filialResponsavel"
			property="filialByIdFilialAtendeComercial"
			service="lms.municipios.filialService.findLookup"
			action="/municipios/manterFiliais"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="29%"
			width="71%"
			required="true">
			<adsm:propertyMapping
				criteriaProperty="tpAcesso"
				relatedProperty="tpAcesso"/>
			<adsm:propertyMapping
				relatedProperty="filialByIdFilialAtendeComercial.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia"/>

			<adsm:textbox
				dataType="text"
				property="filialByIdFilialAtendeComercial.pessoa.nmFantasia"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			label="filialRespSolicitada"
			property="filialByIdFilialComercialSolicitada"
			service="lms.municipios.filialService.findLookup"
			action="/municipios/manterFiliais"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="29%"
			width="71%"
			required="true">
			<adsm:propertyMapping
				criteriaProperty="tpAcesso"
				relatedProperty="tpAcesso"/>
			<adsm:propertyMapping
				relatedProperty="filialByIdFilialComercialSolicitada.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia"/>

			<adsm:textbox
				dataType="text"
				property="filialByIdFilialComercialSolicitada.pessoa.nmFantasia"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			label="regionalResponsavel"
			property="regionalComercial"
			service="lms.vendas.manterClienteAction.findLookupRegional"
			action="/municipios/manterRegionais"
			idProperty="idRegional"
			criteriaProperty="sgRegional"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="29%"
			width="71%">

			<adsm:propertyMapping
				relatedProperty="regionalComercial.dsRegional"
				modelProperty="dsRegional"/>

			<adsm:textbox
				dataType="text"
				property="regionalComercial.dsRegional"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:combobox
			label="faturamentoPrevisto"
			onlyActiveValues="true"
			property="moedaByIdMoedaFatPrev.idMoeda"
			optionLabelProperty="siglaSimbolo"
			optionProperty="idMoeda"
			service="lms.vendas.manterClienteAction.findMoedaPaisCombo"
			labelWidth="29%"
			width="21%"
			boxWidth="80"
			autoLoad="false"
			renderOptions="true">

			<adsm:textbox
				dataType="currency"
				property="vlFaturamentoPrevisto"
				size="9"
				maxLength="18"/>
		</adsm:combobox>

		<adsm:checkbox
			property="blOperadorLogistico"
			label="operadorLogistico"
			onclick="changeOperadorLogistico()"
			labelWidth="29%"
			width="16%"/>

		<adsm:textbox
			dataType="integer"
			property="nrCasasDecimaisPeso"
			label="casasDecimaisPesoMercadoria"
			size="5"
			maxLength="1"
			labelWidth="29%"
			width="21%"
			required="true"/>

		<adsm:combobox
			property="tpFormaArredondamento"
			onlyActiveValues="true"
			domain="DM_FORMA_ARREDONDAMENTO"
			label="formaArredondamentoPeso"
			labelWidth="29%"
			width="16%"
			required="true"
			autoLoad="false"
			renderOptions="true"/>

		<adsm:checkbox
			property="blCobraReentrega"
			label="pagaReentrega"
			labelWidth="29%"
			width="21%"/>

		<adsm:textbox
			property="nrReentregasCobranca"
			label="cobrarReentrega"
			labelWidth="29%"
			width="16%"
			dataType="integer"
			size="5"
			maxLength="1"
			/>

		<adsm:checkbox
			property="blPesoAforadoPedagio"
			label="calculoPedagioPesoReal"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blIcmsPedagio"
			label="incideICMSPedagio"
			labelWidth="29%"
			width="16%"/>

		<adsm:checkbox
			property="blFobDirigidoAereo"
			label="fobDirigidoAereo"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blFobDirigido"
			label="fobDirigidoRodoviario"
			labelWidth="29%"
			width="16%"/>

		<adsm:combobox
			property="tpFrequenciaVisita"
			onlyActiveValues="true"
			domain="DM_FREQUENCIA_VISITA"
			label="frequenciaVisita"
			labelWidth="29%"
			width="21%"
			autoLoad="false"
			renderOptions="true"
			required="true"/>

		<adsm:checkbox
			property="blPermanente"
			label="permanente"
			labelWidth="29%"
			width="16%"/>			

		<adsm:checkbox
			property="blDivulgaLocalizacao"
			label="divulgaLocalizacao"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blCobraDevolucao"
			label="pagaDevolucao"
			labelWidth="29%"
			width="16%"/>

		<adsm:checkbox
			property="blAceitaFobGeral"
			label="liberarFobQualquerDestino"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			property="blClienteEstrategico"
			label="clienteEstrategico"
			labelWidth="29%"
			width="21%"/>

		<adsm:buttonBar lines="3">
			<adsm:button
				caption="municipiosTRT"
				id="btnMunicipiosTRT"
				onclick="return changePage('vendas/manterTrtCliente.do?cmd=main');"
				boxWidth="118"/>

			<adsm:button
				caption="classificacoes"
				id="btnClassificacoes"
				onclick="return changePage('vendas/manterClassificacoesCliente.do?cmd=main');"/>

			<adsm:button
				caption="promotores"
				id="btnPromotores"
				onclick="return changePage('vendas/manterPromotoresCliente.do?cmd=main');"/>

			<adsm:button
				caption="regionais"
				id="btnRegionais"
				onclick="return changePage('vendas/manterGerenciasRegionaisCliente.do?cmd=main');"/>

			<adsm:button
				caption="potencialComercializacao"
				id="btnPotencialComercializacao"
				onclick="return changePage('vendas/manterPotencialComercializacaoCliente.do?cmd=main');"
				boxWidth="190"/>

			<adsm:button
				caption="produtos"
				id="btnProdutos"
				onclick="return changePage('vendas/manterInformacoesProdutosCliente.do?cmd=main');"
				breakBefore="true"/>

			<adsm:button
				caption="usuarioResponsavel"
				id="btnUsuariosResponsaveis"
				onclick="return changePage('vendas/manterUsuariosResponsaveisCliente.do?cmd=main');"
				boxWidth="118"/>

			<adsm:button
				caption="clientesOperados"
				id="btnClientesOperados"
				disabled="true"
				onclick="return changePage('vendas/manterClienteOperadorLogistico.do?cmd=main');"/>

			<adsm:button
				caption="embarquesProibidos"
				id="btnEmbarquesProibidos"
				onclick="return changePage('vendas/manterEmbarquesProibidos.do?cmd=main');"
				boxWidth="150"/>

			<adsm:button
				caption="liberacoesEmbarque"
				id="btnLiberacoesEmbarque"
				onclick="return changePage('vendas/manterLiberacoesEmbaqueMunicipiosCliente.do?cmd=main');"
				boxWidth="170"/>

			<adsm:button
				caption="divisoes"
				id="btnDivisoes"
				onclick="return changePage('vendas/manterDivisoesCliente.do?cmd=main');"/>

			<adsm:button
				caption="parametrizacoes"
				id="btnParametrizacoes"
				onclick="return changePageParametros();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	var tpCliente = {EVENTUAL:'E', ESPECIAL:'S', POTENCIAL:'P', FILIAL:'F'};
	var modo = {INCLUSAO:"1", EDICAO:"2"};

	/** Seta label para alerta de campo obrigatório */
	getElement("vlFaturamentoPrevisto").label = getElement("moedaByIdMoedaFatPrev.idMoeda").label;

	getElement("tpAcesso").masterLink = "true";

	function changeOperadorLogistico(){
		setDisabled("btnClientesOperados",!(getElementValue("blOperadorLogistico")));
	}

	function myOnShow() {
		hideMessage(this.document);
		return false;
	}

	function loadData(data) {
		onDataLoad_cb(data);
		verificaDadosDefault(data);
		changeOperadorLogistico();
	}

	function clean() {
		resetValue(document);
		changeOperadorLogistico();
	}

	function exibirPopupWKFilial(){
		return getElementValue("filialByIdFilialAtendeComercial.idFilial") !== getElementValue("filialByIdFilialComercialSolicitada.idFilial") && getElementValue("temPendenciaWKFilialCom") !== "true";
	}

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;
			var mode = telaCad.getModoTela();
			var tipoCliente = telaCad.getTpCliente();

			enableFields(mode, telaCad.getTpClienteRegras());

			if(telaCad.getIdProcessoWorkflow() != undefined && telaCad.getIdProcessoWorkflow() != ''){
				disableButtons(true);
				disableAll();
			}
		}
		changeOperadorLogistico();
	}

	function enableFields(mode, tipoCliente) {
		changeAbaStatus(false);
		disableAll();
		setDisabled("btnUsuariosResponsaveis", false);

		if (tipoCliente == tpCliente.POTENCIAL ||
			tipoCliente == tpCliente.EVENTUAL) {
			setDisabled("tpFrequenciaVisita", false);

			if (mode == modo.EDICAO) {
				enableButtonsPotencialEventual();
			} else if(mode == modo.INCLUSAO) {
				setElementValue("blFobDirigido", "N");
				setElementValue("blFobDirigidoAereo", "N");
			}
		} else if (tipoCliente == tpCliente.ESPECIAL) {
			setDisabled(this.document, false);

			if (mode == modo.INCLUSAO) {
				setElementValue("blFobDirigido", "S");
				setElementValue("blFobDirigidoAereo", "N");
				disableButtons(true);
			} else if (mode == modo.EDICAO) {				
				disableButtons(false);
			}
		} else if (tipoCliente == tpCliente.FILIAL) {
			setDisabled(this.document, false);

			if (mode == modo.INCLUSAO) {
				setElementValue("blFobDirigido", "S");
				setElementValue("blFobDirigidoAereo", "N");				
				disableButtons(true);
			} else if (mode == modo.EDICAO) {
				enableButtonsFilial();
			}
		}

		if (mode == modo.INCLUSAO) {
			setDisabled("filialByIdFilialComercialSolicitada.idFilial", true);
			setDisabled("regionalComercial.idRegional", true);
		} else {
			if(getElementValue("temPendenciaWKFilialCom") == "true" || getTpSituacaoCliente() === "N"){
				setDisabled("filialByIdFilialComercialSolicitada.idFilial", true);
			} else {
				setDisabled("filialByIdFilialComercialSolicitada.idFilial", false);
			}

			setDisabled("regionalComercial.idRegional", false);
		}

		setDisabled("filialByIdFilialAtendeComercial.idFilial", true);
		setDisabled("filialByIdFilialAtendeComercial.pessoa.nmFantasia", true);
		setDisabled("filialByIdFilialComercialSolicitada.pessoa.nmFantasia", true);
		setDisabled("regionalComercial.dsRegional", true);
		changeOperadorLogistico();
	}

	function getTpSituacaoCliente(){
		var tabGroup = getTabGroup(this.document);
		var telaIdentificacao = tabGroup.getTab("identificacao").tabOwnerFrame;
		return telaIdentificacao.getTpSituacao();
	}

	function disableAll() {
		setDisabled(this.document, true);
	}

	function changeAbaStatus(status) {
		var tabGroup = getTabGroup(document);
		tabGroup.setDisabledTab("comercial", status);
	}

	function enableButtonsPotencialEventual() {
		setDisabled("btnPromotores", false);
		setDisabled("btnPotencialComercializacao", false);
		setDisabled("btnProdutos", false);
		setDisabled("btnEmbarquesProibidos", false);
	}

	function disableButtons(status) {
		setDisabled("btnRegionais", status);
		setDisabled("btnClassificacoes", status);
		setDisabled("btnPromotores", status);
		setDisabled("btnPotencialComercializacao", status);
		setDisabled("btnProdutos", status);
		setDisabled("btnEmbarquesProibidos", status);
		setDisabled("btnLiberacoesEmbarque", status);
		setDisabled("btnDivisoes", status);
		setDisabled("btnParametrizacoes", status);
		setDisabled("btnMunicipiosTRT", status);
	}

	function enableButtonsFilial() {
		setDisabled("btnRegionais", false);
		setDisabled("btnClassificacoes", false);
		setDisabled("btnPromotores", false);
		setDisabled("btnPotencialComercializacao", false);
		setDisabled("btnProdutos", false);
		setDisabled("btnEmbarquesProibidos", false);
		setDisabled("btnLiberacoesEmbarque", false);
		setDisabled("btnDivisoes", true);
		setDisabled("btnParametrizacoes", true);
		setDisabled("btnMunicipiosTRT", false);
	}

	function ajustaDadosDefault(data) {
		setElementValue("filialByIdFilialAtendeComercial.idFilial", data.filial.idFilial);
		setElementValue("filialByIdFilialAtendeComercial.sgFilial", data.filial.sgFilial);
		setElementValue("filialByIdFilialAtendeComercial.pessoa.nmFantasia", data.filial.pessoa.nmFantasia);

		setElementValue("filialByIdFilialComercialSolicitada.idFilial", data.filial.idFilial);
		setElementValue("filialByIdFilialComercialSolicitada.sgFilial", data.filial.sgFilial);
		setElementValue("filialByIdFilialComercialSolicitada.pessoa.nmFantasia", data.filial.pessoa.nmFantasia);

		if(data.regional != undefined) {
			setElementValue("regionalComercial.idRegional", data.regional.idRegional);
			setElementValue("regionalComercial.sgRegional", data.regional.sgRegional);
			setElementValue("regionalComercial.dsRegional", data.regional.dsRegional);
		}
		setElementValue("nrCasasDecimaisPeso", data.nrCasasDecimaisPeso);
		setElementValue("tpFormaArredondamento", "P");
		setElementValue("tpFrequenciaVisita", "M");
		setElementValue("blCobraReentrega", "S");
		setElementValue("blCobraDevolucao", "S");
		setElementValue("nrReentregasCobranca", "1");
		setElementValue("blDivulgaLocalizacao", "S");
		setElementValue("moedaByIdMoedaFatPrev.idMoeda", "");
	}

	function verificaDadosDefault(data) {
		if(isBlank(data.tpFormaArredondamento)) {
			setElementValue("tpFormaArredondamento", "P");
		}
		if(isBlank(data.tpFrequenciaVisita)) {
			setElementValue("tpFrequenciaVisita", "M");
		}
		if(isBlank(data.blCobraReentrega)) {
			setElementValue("blCobraReentrega", "S");
		}
		if(isBlank(data.blCobraDevolucao)) {
			setElementValue("blCobraDevolucao", "S");
		}
		if(isBlank(data.blDivulgaLocalizacao)) {
			setElementValue("blDivulgaLocalizacao", "S");
		}
		if(isBlank(data.nrReentregasCobranca)) {
			setElementValue("nrReentregasCobranca", "1");
		}
	}

	function changeCamposObrigatorios() {
		var tabGroup = getTabGroup(this.document);
		var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;
		var tipoCliente = telaCad.getTpClienteRegras();

		var isRequired = (tipoCliente != tpCliente.POTENCIAL && tipoCliente != tpCliente.EVENTUAL);
		getElement("moedaByIdMoedaFatPrev.idMoeda").required = isRequired;
		getElement("vlFaturamentoPrevisto").required = isRequired;
	}

	function changePage(url) {
		var tabGroup = getTabGroup(this.document);
		var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;

		url += "&idFilial="+getElementValue("filialByIdFilialAtendeComercial.idFilial");
		telaCad.changePageCliente(url);
	}

	function changePageParametros() {
		var tabGroup = getTabGroup(this.document);
		var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;

		var url = "vendas/manterParametrosCliente.do?cmd=main";
		url += '&origem=param';
		url += '&idFilial='+getElementValue("filialByIdFilialAtendeComercial.idFilial");
		url += '&tabelaDivisaoCliente.divisaoCliente.cliente.idCliente=' + telaCad.getElementValue("idCliente");
		url += '&tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa=' + telaCad.getElementValue("pessoa.nmPessoa");
		url += '&tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao=' + getFormattedValue(telaCad.getElementValue("pessoa.tpIdentificacao"), telaCad.getElementValue("pessoa.nrIdentificacao"));
		telaCad.changePageCliente(url);
	}

	function ajustaDadosDefaultEdicao(tipoCliente, data) {
		if (tipoCliente == tpCliente.POTENCIAL ||
			tipoCliente == tpCliente.EVENTUAL) {

			var idFilial = getElementValue("filialByIdFilialAtendeComercial.idFilial");
			var sgFilial = getElementValue("filialByIdFilialAtendeComercial.sgFilial");
			var nmFantasia = getElementValue("filialByIdFilialAtendeComercial.pessoa.nmFantasia");

			var idRegional = getElementValue("regionalComercial.idRegional");
			var sgRegional = getElementValue("regionalComercial.sgRegional");
			var dsRegional = getElementValue("regionalComercial.dsRegional");

			clean();
			ajustaDadosDefault(data);

			setElementValue("filialByIdFilialAtendeComercial.idFilial", idFilial);
			setElementValue("filialByIdFilialAtendeComercial.sgFilial", sgFilial);
			setElementValue("filialByIdFilialAtendeComercial.pessoa.nmFantasia", nmFantasia);

			setElementValue("filialByIdFilialComercialSolicitada.idFilial", idFilial);
			setElementValue("filialByIdFilialComercialSolicitada.sgFilial", sgFilial);
			setElementValue("filialByIdFilialComercialSolicitada.pessoa.nmFantasia", nmFantasia);

			setElementValue("regionalComercial.idRegional", idRegional);
			setElementValue("regionalComercial.sgRegional", sgRegional);
			setElementValue("regionalComercial.dsRegional", dsRegional);
		}
	}

	function myOnPageLoadCallBack_cb() {
		onPageLoad_cb();
		unblockUI();
		changeOperadorLogistico();
	}
</script>