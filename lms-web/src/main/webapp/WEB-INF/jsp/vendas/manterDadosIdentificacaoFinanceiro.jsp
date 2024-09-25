<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterClienteAction"
	onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form
		action="/vendas/manterDadosIdentificacao"
		id="formFinanceiro"
		idProperty="idCliente"
		height="230">

		<adsm:hidden property="pessoa.tpPessoa" serializable="false"/>
		<adsm:hidden property="pessoa.tpIdentificacao" serializable="false"/>
		<adsm:hidden property="pessoa.nrIdentificacao" serializable="false"/>
		<adsm:hidden property="pessoa.nmPessoa" serializable="false"/>
		<adsm:hidden property="moedaByIdMoedaSaldoAtual.idMoeda"/>
		<adsm:hidden property="analiseCreditoCliente.idAnaliseCreditoCliente"/>
		<adsm:hidden property="analiseCreditoCliente.tpSituacao" serializable="false"/>
		<adsm:hidden property="hasAccess" serializable="false"/>
		<adsm:hidden property="temPendenciaWKFilialCob" serializable="false"/>
		<adsm:hidden property="tpAcesso" value="A" serializable="false"/>
		<adsm:hidden property="tpCobrancaAprovado" serializable="true"/>

		<adsm:lookup
			label="filialResponsavel"
			property="filialByIdFilialCobranca"
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
				relatedProperty="filialByIdFilialCobranca.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia"/>

			<adsm:textbox
				dataType="text"
				property="filialByIdFilialCobranca.pessoa.nmFantasia"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			label="filialRespSolicitada"
			property="filialByIdFilialCobrancaSolicitada"
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
				relatedProperty="filialByIdFilialCobrancaSolicitada.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia"/>

			<adsm:textbox
				dataType="text"
				property="filialByIdFilialCobrancaSolicitada.pessoa.nmFantasia"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			label="regionalResponsavel"
			property="regionalFinanceiro"
			service="lms.vendas.manterClienteAction.findLookupRegional"
			action="/municipios/manterRegionais"
			idProperty="idRegional"
			criteriaProperty="sgRegional"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="29%"
			width="71%"
		>
			<adsm:propertyMapping
				relatedProperty="regionalFinanceiro.dsRegional"
				modelProperty="dsRegional"/>

			<adsm:textbox
				dataType="text"
				property="regionalFinanceiro.dsRegional"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:combobox
			label="valorLimiteCredito"
			onlyActiveValues="true"
			property="moedaByIdMoedaLimCred.idMoeda"
			optionLabelProperty="siglaSimbolo"
			optionProperty="idMoeda"
			service="lms.vendas.manterClienteAction.findMoedaPaisCombo"
			labelWidth="29%"
			width="21%"
			boxWidth="80"
			autoLoad="false"
			renderOptions="true"
		>
			<adsm:textbox
				dataType="currency"
				property="vlLimiteCredito"
				size="10"
				maxLength="18"/>
		</adsm:combobox>

		<adsm:textbox
			label="diasLimiteDebito"
			dataType="integer"
			property="nrDiasLimiteDebito"
			labelWidth="29%"
			width="21%"
			size="5"
			maxLength="3"/>

		<adsm:combobox
			label="tipoCobranca"
			property="tpCobranca"
			onlyActiveValues="true"
			domain="DM_TIPO_COBRANCA"
			labelWidth="29%"
			width="21%"
			boxWidth="150"
			autoLoad="false"
			renderOptions="true"
			required="true"/>

		<adsm:combobox
			label="tipoCobrancaSolicitado"
			property="tpCobrancaSolicitado"
			onlyActiveValues="true"
			domain="DM_TIPO_COBRANCA"
			labelWidth="29%"
			width="21%"
			autoLoad="false"
			onchange="return onChangeTpCobrancaSolicitado();"
			renderOptions="true"/>

		<adsm:lookup
			label="responsavel"
			property="cliente"
			service="lms.vendas.manterClienteAction.findLookupCliente"
			action="/vendas/manterDadosIdentificacao"
			idProperty="idCliente"
			dataType="text"
			size="20"
			maxLength="20"
			onDataLoadCallBack="lookupCliente"
			afterPopupSetValue="aferPopupCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			labelWidth="29%"
			width="39%"
		>
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="cliente.pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				property="cliente.pessoa.nmPessoa"
				disabled="true"
				size="25"/>
		</adsm:lookup>

		<adsm:checkbox
			label="baseCalculo"
			property="blBaseCalculo"
			labelWidth="11%"
			width="21%"/>

		<adsm:hidden property="hidden.idDivisaoCliente" serializable="false"/>
		<adsm:combobox
			property="divisaoClienteResponsavel.idDivisaoCliente"
			label="divisaoResponsavel"
			autoLoad="false"
			disabled="true"
        	service="lms.contasreceber.manterDevedoresDocumentosServicoAction.findComboDivisaoCliente"
			optionLabelProperty="dsDivisaoCliente"
			optionProperty="idDivisaoCliente"
			onchange="onChangeDivisaoCliente(this);"
			width="71%"
			boxWidth="155"
			labelWidth="29%">

			<adsm:propertyMapping
				modelProperty="cliente.idCliente"
				relatedProperty="idCliente"/>

		</adsm:combobox>

		<adsm:combobox
			label="bancoPreferencia"
			property="cedente.idCedente"
			optionLabelProperty="comboText"
			onlyActiveValues="true"
			optionProperty="idCedente"
			service="lms.vendas.manterClienteAction.findActiveCedentes"
			labelWidth="29%"
			width="21%"
			boxWidth="155"
			autoLoad="false"
			renderOptions="true"/>

		<adsm:textbox
			label="percentualJuroDiario"
			property="pcJuroDiario"
			dataType="percent"
			labelWidth="29%"
			width="21%"
			size="6"
			maxLength="5"
			required="true"/>

		<adsm:checkbox
			label="ressarceFreteFOB"
			property="blRessarceFreteFob"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			label="preFatura"
			property="blPreFatura"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			label="cobrancaCentralizada"
			property="blCobrancaCentralizada"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			label="protestar"
			property="blIndicadorProtesto"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			label="emiteBloquetoClienteDestino"
			property="blEmiteBoletoCliDestino"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			label="geraReciboFreteEntrega"
			property="blGeraReciboFreteEntrega"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			label="faturaDocumentosConferidos"
			property="blFaturaDocsConferidos"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			label="faturaDocumentosEntregues"
			property="blFaturaDocsEntregues"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			label="agrupaFaturamentoMes"
			property="blAgrupaFaturamentoMes"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			label="faturaDocumentosCReferencia"
			property="blFaturaDocReferencia"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			label="indicadorFronteiraRapida"
			property="blFronteiraRapida"
			labelWidth="29%"
			width="21%"/>

		<adsm:combobox
			label="periodicidadeTransferencia"
			property="tpPeriodicidadeTransf"
			onlyActiveValues="true"
			domain="DM_PERIODICIDADE_FATURAMENTO"
			labelWidth="29%"
			width="21%"
			autoLoad="false"
			renderOptions="true"/>

		<adsm:combobox
			label="valorLimiteDocumentosVencimento"
			property="moedaByIdMoedaLimDoctos.idMoeda"
			optionLabelProperty="siglaSimbolo"
			optionProperty="idMoeda"
			service="lms.vendas.manterClienteAction.findMoedaPaisCombo"
			onlyActiveValues="true"
			labelWidth="29%"
			width="21%"
			boxWidth="80"
			autoLoad="false"
			renderOptions="true">

			<adsm:textbox
				dataType="currency"
				property="vlLimiteDocumentos"
				size="10"
				maxLength="18"/>
		</adsm:combobox>

		<adsm:combobox
			label="meioEnvioCobranca"
			property="tpMeioEnvioBoleto"
			onlyActiveValues="true"
			domain="DM_MEIO_ENVIO"
			labelWidth="29%"
			width="21%"
			autoLoad="false"
			renderOptions="true"/>

		<adsm:checkbox
			label="separaFaturasModal"
			property="blSeparaFaturaModal"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			label="calculoEnviaArquivoPreFatura"
			property="blCalculoArqPreFatura"
			labelWidth="29%"
			width="21%"/>

		<adsm:checkbox
			label="emiteDacteFaturamento"
			property="blEmiteDacteFaturamento"
			labelWidth="29%"
			width="21%"/>

		<adsm:textbox
			dataType="text"
			property="obFatura"
			label="obFatura"
			labelWidth="20%"
			width="30%"
			maxLength="60"
			size="36"
		/>

		<adsm:checkbox
			label="liberacaoRimSomenteMatriz"
			property="blMtzLiberaRIM"
			labelWidth="29%"
			width="21%"
			disabled="true"
		/>		

		<adsm:buttonBar>
			<adsm:button
				caption="manterAnaliseCredito"
				id="btnManterAnaliseCredito"
				onclick="return changePage('vendas/manterAnaliseCreditoCliente.do?cmd=main&idAnaliseCreditoCliente='+getElementValue('analiseCreditoCliente.idAnaliseCreditoCliente'));"
				disabled="true"/>
			<adsm:button
				caption="gerarAnaliseCredito"
				id="btnGerarAnaliseCredito"
				onclick="return validateAnaliseCreditoCliente();"
				disabled="true"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	var tpCliente = {EVENTUAL:'E', ESPECIAL:'S', POTENCIAL:'P', FILIAL:'F'};
	var modo = {INCLUSAO:"1", EDICAO:"2"};

	/** Seta label para alerta de campo obrigatório */
	getElement("vlLimiteDocumentos").label = getElement("moedaByIdMoedaLimDoctos.idMoeda").label;
	getElement("tpAcesso").masterLink = "true";

	function myOnShow() {
		hideMessage(this.document);
		return false;
	}

	function loadData(data) {
		onDataLoad_cb(data);
		verificaDadosDefault();

		validateResponsavel();

	}

	function disableButtons(status) {
		setDisabled("btnManterAnaliseCredito", status);
		setDisabled("btnGerarAnaliseCredito", status);

	}

	function validateAnaliseCredito(tipoCliente) {
		setDisabled("btnManterAnaliseCredito", true);
		setDisabled("btnGerarAnaliseCredito", true);

		if (tipoCliente == tpCliente.ESPECIAL) {
			if(hasValue(getElementValue("analiseCreditoCliente.idAnaliseCreditoCliente"))) {
				/* Se Geração Concluida */
				if(getElementValue("analiseCreditoCliente.tpSituacao") == "C") {
					setDisabled("btnGerarAnaliseCredito", false);
				}
				setDisabled("btnManterAnaliseCredito", false);
			} else if(getElementValue("hasAccess") == "true") {
				setDisabled("btnGerarAnaliseCredito", false);
			}
		}
	}
	
	function getTpSituacaoAnaliseCreditoCliente(){
		return getElementValue("analiseCreditoCliente.tpSituacao");
	}

	function clean() {
		resetValue(document);
	}

	function exibirPopupWKFilial(){
		return getElementValue("filialByIdFilialCobranca.idFilial") !== getElementValue("filialByIdFilialCobrancaSolicitada.idFilial") && getElementValue("temPendenciaWKFilialCob") !== "true";
	}

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;
			var mode = telaCad.getModoTela();
			var tipoCliente = telaCad.getTpCliente();

			enableFields(mode, telaCad.getTpClienteRegras());

			validateResponsavel();

			if(telaCad.getIdProcessoWorkflow() != undefined && telaCad.getIdProcessoWorkflow() != ''){
				disableButtons(true);
				disableAll();
			}
		}
	}

	function enableFields(mode, tipoCliente) {
		disableAll();
		changeAbaStatus(true);

		if (tipoCliente == tpCliente.ESPECIAL) {
			changeAbaStatus(false);
			setDisabled(this.document, false);
		} else if (tipoCliente == tpCliente.FILIAL) {
			changeAbaStatus(false);
			setDisabled("cliente.idCliente", false);
			setDisabled("blBaseCalculo", false);
		}

		if (mode == modo.INCLUSAO) {
			setDisabled("filialByIdFilialCobrancaSolicitada.idFilial", true);
			setDisabled("regionalFinanceiro.idRegional", true);
		} else {
			if(getElementValue("temPendenciaWKFilialCob") == "true" || getTpSituacaoCliente() === "N"){
				setDisabled("filialByIdFilialCobrancaSolicitada.idFilial", true);
			} else {
				setDisabled("filialByIdFilialCobrancaSolicitada.idFilial", false);
			}

			setDisabled("regionalFinanceiro.idRegional", false);
		}

		/* Verifica o estado dos botões */
		validateAnaliseCredito(tipoCliente);

		setDisabled("moedaByIdMoedaLimCred.idMoeda", true);
		setDisabled("vlLimiteCredito", true);
		setDisabled("tpCobranca", true);
		setDisabled("filialByIdFilialCobranca.idFilial", true);
		setDisabled("filialByIdFilialCobranca.pessoa.nmFantasia", true);
		setDisabled("filialByIdFilialCobrancaSolicitada.pessoa.nmFantasia", true);
		setDisabled("cliente.pessoa.nmPessoa", true);
		setDisabled("regionalFinanceiro.dsRegional", true);
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
		tabGroup.setDisabledTab("financeiro", status);
	}

	function ajustaDadosDefault(data) {
		setElementValue("filialByIdFilialCobranca.idFilial", data.filial.idFilial);
		setElementValue("filialByIdFilialCobranca.sgFilial", data.filial.sgFilial);
		setElementValue("filialByIdFilialCobranca.pessoa.nmFantasia", data.filial.pessoa.nmFantasia);

		setElementValue("filialByIdFilialCobrancaSolicitada.idFilial", data.filial.idFilial);
		setElementValue("filialByIdFilialCobrancaSolicitada.sgFilial", data.filial.sgFilial);
		setElementValue("filialByIdFilialCobrancaSolicitada.pessoa.nmFantasia", data.filial.pessoa.nmFantasia);

		if(data.regional != undefined) {
			setElementValue("regionalFinanceiro.idRegional", data.regional.idRegional);
			setElementValue("regionalFinanceiro.sgRegional", data.regional.sgRegional);
			setElementValue("regionalFinanceiro.dsRegional", data.regional.dsRegional);
		}
		setElementValue("tpCobranca", "3");
		setElementValue("tpCobrancaSolicitado", "3");
		setElementValue("blSeparaFaturaModal", "S");
		setElementValue("pcJuroDiario", setFormat("pcJuroDiario", "0"));
	}

	function verificaDadosDefault() {
		if (getElementValue("tpCobranca") == "") {
			setElementValue("tpCobranca", "3");
		}
		if (getElementValue("tpCobrancaSolicitado") == "") {
			setElementValue("tpCobrancaSolicitado", getElementValue("tpCobranca"));
		}
	}

	function changeCamposObrigatorios() {
		if (!isBlank(getElementValue("moedaByIdMoedaLimDoctos.idMoeda"))) {
			getElement("vlLimiteDocumentos").required = true;
		} else {
			getElement("vlLimiteDocumentos").required = false;
		}
		if (prepareStringToNumber(getElementValue("vlLimiteDocumentos")) > 0) {
			getElement("moedaByIdMoedaLimDoctos.idMoeda").required = true;
		} else {
			getElement("moedaByIdMoedaLimDoctos.idMoeda").required = false;
		}
	}

	function lookupCliente_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		var retorno = cliente_pessoa_nrIdentificacao_exactMatch_cb(data);

		if (data.length > 0) {
			setElementValue("cliente.pessoa.nrIdentificacao", data[0].pessoa.nrIdentificacaoFormatado);
		}

		validateResponsavel();

		return retorno;
	}

	function aferPopupCliente(data) {
		setElementValue("cliente.pessoa.nrIdentificacao", data.pessoa.nrIdentificacaoFormatado);

		validateResponsavel();
	}

	function ajustaDadosDefaultEdicao(tipoCliente, data) {
		var idFilial = getElementValue("filialByIdFilialCobranca.idFilial");
		var sgFilial = getElementValue("filialByIdFilialCobranca.sgFilial");
		var nmFantasia = getElementValue("filialByIdFilialCobranca.pessoa.nmFantasia");

		var idRegional = getElementValue("regionalFinanceiro.idRegional");
		var sgRegional = getElementValue("regionalFinanceiro.sgRegional");
		var dsRegional = getElementValue("regionalFinanceiro.dsRegional");

		clean();
		ajustaDadosDefault(data);

		setElementValue("filialByIdFilialCobranca.idFilial", idFilial);
		setElementValue("filialByIdFilialCobranca.sgFilial", sgFilial);
		setElementValue("filialByIdFilialCobranca.pessoa.nmFantasia", nmFantasia);

		setElementValue("filialByIdFilialCobrancaSolicitada.idFilial", idFilial);
		setElementValue("filialByIdFilialCobrancaSolicitada.sgFilial", sgFilial);
		setElementValue("filialByIdFilialCobrancaSolicitada.pessoa.nmFantasia", nmFantasia);

		setElementValue("regionalFinanceiro.idRegional", idRegional);
		setElementValue("regionalFinanceiro.sgRegional", sgRegional);
		setElementValue("regionalFinanceiro.dsRegional", dsRegional);
	}

	function myOnPageLoadCallBack_cb() {
		onPageLoad_cb();
		unblockUI();
	}

	function validateAnaliseCreditoCliente() {
		var service = "lms.vendas.manterAnaliseCreditoClienteAction.validateAnaliseCreditoCliente";
		var sdo = createServiceDataObject(service, "afterValidateAnaliseCreditoCliente", {idCliente: getElementValue("idCliente")});
		xmit({serviceDataObjects:[sdo]});
	}
	function afterValidateAnaliseCreditoCliente_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		/* Se não retornou erro, o processo foi validado */
		openGeracaoAnalisePopup();
	}

	function openGeracaoAnalisePopup() {
		var data = showModalDialog("vendas/gerarAnaliseCreditoCliente.do?cmd=main&idCliente="+getElementValue("idCliente"),window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:200px;');
		if(data != undefined && hasValue(data.obEvento)) {
		/* Se não retornou erro, a analise de credito foi gerada */
		setDisabled("btnGerarAnaliseCredito", true);

		setElementValue("analiseCreditoCliente.idAnaliseCreditoCliente", data.idAnaliseCreditoCliente);
		setElementValue("analiseCreditoCliente.tpSituacao", data.tpSituacao.value);
		setDisabled("btnManterAnaliseCredito", false);
		setFocus("btnManterAnaliseCredito");
		showSuccessMessage();
	}
	}

	function onChangeTpCobrancaSolicitado() {
		if(!hasValue(getElementValue("tpCobranca")) || !hasValue(getElementValue("idCliente")) || getElementValue("hasAccess") != "true") {
			setElementValue("tpCobranca", getElementValue("tpCobrancaSolicitado"));
		}
	}

	function changePage(url) {
		var tabGroup = getTabGroup(this.document);
		var telaCad = tabGroup.parentTabGroup.getTab("cad").tabOwnerFrame;

		url += "&idFilial="+getElementValue("filialByIdFilialCobranca.idFilial");
		telaCad.changePageCliente(url);
	}

	function findDivisaoClienteByResponsavel(idCliente){
		var data = {"idCliente": idCliente};
		var sdo = createServiceDataObject("lms.contasreceber.manterDevedoresDocumentosServicoAction.findComboDivisaoCliente","findDivisaoClienteByResponsavel", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function findDivisaoClienteByResponsavel_cb(data, error){
		if (error != undefined){
			alert(error);
		}
		var idDivisaoCliente = getElementValue("hidden.idDivisaoCliente");
		comboboxLoadOptions({e:document.getElementById("divisaoClienteResponsavel.idDivisaoCliente"), data:data});
		if (idDivisaoCliente != ""){
			setElementValue("divisaoClienteResponsavel.idDivisaoCliente", idDivisaoCliente);
		}
	}

	function cliente_pessoa_nrIdentificacaoOnChangeHandler() {
		var criteriaElement = document.getElementById('cliente.pessoa.nrIdentificacao');

		if(!validate(criteriaElement) || criteriaElement.value == ''){
			validateResponsavel();
		}

		var retorno = lookupChange({e:document.getElementById("cliente.idCliente")});


		return retorno;
	}

	function validateResponsavel() {
		if (getElementValue("cliente.pessoa.nrIdentificacao") == '' || !validate(document.getElementById('cliente.pessoa.nrIdentificacao')) || getElementValue("pessoa.nrIdentificacao").replace(/[\./-]/g, "") == getElementValue("cliente.pessoa.nrIdentificacao").replace(/[\./-]/g, "")){
			setDisabled("divisaoClienteResponsavel.idDivisaoCliente", true);
			setElementValue("divisaoClienteResponsavel.idDivisaoCliente", '');
			setElementValue("hidden.idDivisaoCliente", '');
		} else {
			setDisabled("divisaoClienteResponsavel.idDivisaoCliente", false);
			findDivisaoClienteByResponsavel(document.getElementById("cliente.idCliente").value);
		}
	}

	function findDivisaoClienteByResponsavel(idCliente){
		var data = {"idCliente": idCliente};
		var sdo = createServiceDataObject("lms.contasreceber.manterDevedoresDocumentosServicoAction.findComboDivisaoCliente","findDivisaoClienteByResponsavel", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function findDivisaoClienteByResponsavel_cb(data, error){
		if (error != undefined){
			alert(error);
		}
		var idDivisaoCliente = getElementValue("hidden.idDivisaoCliente");
		comboboxLoadOptions({e:document.getElementById("divisaoClienteResponsavel.idDivisaoCliente"), data:data});
		if (idDivisaoCliente != ""){
			setElementValue("divisaoClienteResponsavel.idDivisaoCliente", idDivisaoCliente);
		}
	}

	function onChangeDivisaoCliente(obj){
		setElementValue("hidden.idDivisaoCliente", obj.value);
	}

</script>