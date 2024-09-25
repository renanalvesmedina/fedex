/**
 * Vari�veis Global
 * 
 * INICIO
 */

var tpDocumento = "COT";
var calculado;
var idServicoPadrao;
var idMoedaPadrao;
var clienteByIdClienteSolicitouData = undefined;
var clienteByIdClienteDestinoData = undefined;
var fromGlobal;
var idFilialUsuarioLogado = null;

/** FIM* */

/**
 * Rotinas Gerais
 * 
 * INICIO
 */
function validaTipoPessoa() {
	var tpPessoa = getElementValue("tpDevedorFrete");
	var tpFrete = getElementValue("tpFrete");

	if (tpPessoa == 'F' && tpFrete == 'C') {
		alertI18nMessage("LMS-01195");
		resetValue("tpDevedorFrete");
		return false;
	}
	return true;
}

function showDimensoes() {
	var idCotacao = getElementValue('idCotacao');
	openDimensoesTpDocumento(idCotacao);
	var args = {
		tpModal : getElementValue('servico.tpModal')
	}
	calculaPesoCubado();
}

function calculaPesoCubado() {
	var tpModal = getElementValue("servico.tpModal");
	if (tpModal && tpModal != "") {
		params = {
			tpModal : tpModal
		};
		var sdo = createServiceDataObject(
				"lms.vendas.gerarCotacoesAction.calculaPesoCubado",
				"calculaPesoCubado", params);
		xmit({
			serviceDataObjects : [ sdo ]
		});
	} else {
		resetValue("psCubado");
	}
}

function calculaPesoCubado_cb(dados, erros) {
	if (erros != undefined || erros == "") {
		alert(erros);
		return false;
	}
	if (dados != undefined) {
		setElementValue("psCubado", setFormat("psCubado", dados.psCubado));
	}
}

function calculaPpe() {
	var idO = getElementValue("municipioByIdMunicipioOrigem.idMunicipio");
	var idD = getElementValue("municipioByIdMunicipioDestino.idMunicipio");
	var idServico = getElementValue("servico.idServico");
	var nrIdentificacaoClienteRemetente = getElementValue("clienteByIdClienteSolicitou.pessoa.nrIdentificacao");
	var nrIdentificacaoClienteDestinatario = getElementValue("clienteByIdClienteDestino.pessoa.nrIdentificacao");
	var nrIdentificacaoClienteDevedorFrete = getElementValue("clienteByIdCliente.pessoa.nrIdentificacao");
	var idMunicipioFilialOrigem = getElementValue("municipioByIdMunicipioOrigem.idMunicipio");
	var idFilialOrigem = getElementValue("filialByIdFilialOrigem.idFilial");
	var idFilialDestino = getElementValue("filialByIdFilialDestino.idFilial");

	if (idO && idD && idFilialOrigem && idFilialDestino
			&& nrIdentificacaoClienteRemetente
			&& nrIdentificacaoClienteDestinatario
			&& nrIdentificacaoClienteDevedorFrete) {
		var params = {
			idServico : getElementValue("servico.idServico"),
			idMunicipioOrigem : idO,
			idMunicipioDestino : idD,
			idServico : idServico,
			nrIdentificacaoClienteRemetente : nrIdentificacaoClienteRemetente,
			nrIdentificacaoClienteDestinatario : nrIdentificacaoClienteDestinatario,
			nrIdentificacaoClienteDevedorFrete : nrIdentificacaoClienteDevedorFrete,
			idMunicipioFilialOrigem : idMunicipioFilialOrigem,
			idFilialOrigem : idFilialOrigem,
			idFilialDestino : idFilialDestino
		};
		var sdo = createServiceDataObject(
				"lms.vendas.gerarCotacoesAction.calculaPpe", "calculaPpe",
				params);
		xmit({
			serviceDataObjects : [ sdo ]
		});
	}
}

function calculaPpe_cb(dados, erros) {
	if (erros != undefined || erros == "") {
		alert(erros);
		return false;
	}
	if (dados != undefined) {
		setElementValue("nrPpe", dados.nrPpe);
	}
}

function isUpdateMode() {
	var idCotacao = getElementValue("idCotacao");
	return (idCotacao && (idCotacao != ""));
}

function imprime(idCotacao) {
	if (idCotacao != "") {
		var sdo = createServiceDataObject(
				"lms.vendas.emitirCotacaoPrecoAction", "openPdf", {
					idCotacao : idCotacao
				});
		executeReportWindowed(sdo, "pdf");
	}
}
function openObservacoes() {
	var ob = getElementValue("obCotacao");
	var idCotacao = getElementValue("idCotacao");
	showModalDialog(
			'vendas/gerarCotacoesObservacoes.do?cmd=main&ob=' + ob
					+ '&idCotacao=' + idCotacao,
			window,
			'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:550px;dialogHeight:200px;');
}

function openReprovar() {
	var mot = getElementValue("dsMotivo");
	var idCotacao = getElementValue("idCotacao");

	var args = {
		w : window,
		tpSituacao : getElementValue("tpSituacao")
	}

	var data = showModalDialog(
			'vendas/gerarCotacoesMotivo.do?cmd=main&dsMotivo=' + mot
					+ '&idCotacao=' + idCotacao,
			args,
			'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:550px;dialogHeight:200px;');
	if (data) {
		setElementValue("dsMotivo", data.dsMotivo);
		setElementValue("tpSituacao", data.tpSituacao.value);
		setElementValue("tpSituacao.description", data.tpSituacao.description);
		setDisabled("pedidoButton", true);
	}
}

function validarColetaEntregaEmergencia() {
	var blEntregaEmergencia = getElementValue("blEntregaEmergencia");
	var blColetaEmergencia = getElementValue("blColetaEmergencia");
	var tpModal = getElementValue("servico.tpModal");
	if (blColetaEmergencia === false && blEntregaEmergencia === false
			&& tpModal == 'A') {
		alert("LMS-01263 - " + i18NLabel.getLabel("LMS-01263"));
	}

}

function criaCotacao() {
	var sdo = createServiceDataObject(
			"lms.vendas.gerarCotacoesAction.criaCotacao", "");
	xmit({
		serviceDataObjects : [ sdo ]
	});
}

function findUsuarioLogado() {
	var sdo = createServiceDataObject(
			"lms.vendas.gerarCotacoesAction.findUsuarioLogado",
			"findUsuarioLogado");
	xmit({
		serviceDataObjects : [ sdo ]
	});
}

function findUsuarioLogado_cb(data) {
	var objId = getElement("usuarioByIdUsuarioRealizou.idUsuario");
	setElementValue(objId, data.idUsuario);
	objId.masterLink = "true";
	var objNrMatricula = getElement("usuarioByIdUsuarioRealizou.funcionario.nrMatricula");
	setElementValue(objNrMatricula, data.nrMatricula);
	objNrMatricula.masterLink = "true";
	var objNm = getElement("usuarioByIdUsuarioRealizou.funcionario.nmUsuario");
	setElementValue(objNm, data.nmUsuario);
	objNm.masterLink = "true";
}

function validateTab() {
	if (getElementValue("clienteByIdClienteSolicitou.pessoa.nmPessoa")) {
		getElement("clienteByIdClienteSolicitou.pessoa.nrIdentificacao").required = "false";
		getElement("clienteByIdClienteSolicitou.idCliente").required = "false";
		getElement("clienteByIdClienteSolicitou.pessoa.nmPessoa").required = "false";
	} else {
		getElement("clienteByIdClienteSolicitou.pessoa.nrIdentificacao").required = "true";
		getElement("clienteByIdClienteSolicitou.idCliente").required = "true";
		getElement("clienteByIdClienteSolicitou.pessoa.nmPessoa").required = "true";
	}

	if (getElementValue("clienteByIdClienteDestino.pessoa.nmPessoa")) {
		getElement("clienteByIdClienteDestino.pessoa.nrIdentificacao").required = "false";
		getElement("clienteByIdClienteDestino.idCliente").required = "false";
		getElement("clienteByIdClienteDestino.pessoa.nmPessoa").required = "false";
	} else {
		getElement("clienteByIdClienteDestino.pessoa.nrIdentificacao").required = "true";
		getElement("clienteByIdClienteDestino.idCliente").required = "true";
		getElement("clienteByIdClienteDestino.pessoa.nmPessoa").required = "true";
	}

	if (getElementValue("clienteByIdCliente.pessoa.nmPessoa")) {
		getElement("clienteByIdCliente.pessoa.nrIdentificacao").required = "false";
		getElement("clienteByIdCliente.idCliente").required = "false";
		getElement("clienteByIdCliente.pessoa.nmPessoa").required = "false";
	} else {
		getElement("clienteByIdCliente.pessoa.nrIdentificacao").required = "true";
		getElement("clienteByIdCliente.idCliente").required = "true";
		getElement("clienteByIdCliente.pessoa.nmPessoa").required = "true";
	}

	return validateServicosAdicionaisFrete()
			&& validateTabScript(document.forms);
}

function setLabelRequiredFields() {
	getElement("clienteByIdClienteSolicitou.pessoa.nmPessoa").label = getI18nMessage("remetente");
	getElement("clienteByIdClienteDestino.pessoa.nmPessoa").label = getI18nMessage("destinatario");
	getElement("clienteByIdCliente.pessoa.nmPessoa").label = getI18nMessage("responsavelFrete");

	getElement("municipioByIdMunicipioOrigem.nmMunicipio").label = getI18nMessage("municipioOrigem");
	getElement("municipioByIdMunicipioDestino.nmMunicipio").label = getI18nMessage("municipioDestino");

	getElement("vlMercadoria").label = getI18nMessage("valorMercadoria");
}

function validateServicosAdicionaisFrete() {
	if (getElementValue("blFrete") == false
			&& getElementValue("blServicosAdicionais") == false) {
		alertI18nMessage("LMS-04063");
		return false;
	}
	return true;
}

function validatePsReal() {
	if (getElementValue("psReal") == "")
		return true;
	var vl = stringToNumber(getElementValue("psReal"));
	if (vl <= 0) {
		alertI18nMessage("LMS-01099");
		return false;
	}
	return true;
}

function removeServicosAdicionais() {
	var sdo = createServiceDataObject(
			"lms.vendas.gerarCotacoesAction.removeServicosAdicionais", "");
	xmit({
		serviceDataObjects : [ sdo ]
	});
}

function convertTipo(tipo) {
	if (tipo == "Solicitou") {
		return "Origem";
	}
	if (tipo == "") {
		return "Responsavel";
	}

	return tipo;
}

/**
 * Carrega a filial que o usuario está logado
 * 
 */
function carregaDadosFilial() {
	var criteria = new Array();

	var sdo = createServiceDataObject(
			"lms.coleta.cadastrarPedidoColetaAction.getFilialUsuarioLogado",
			"resultadoBusca", criteria);
	xmit({
		serviceDataObjects : [ sdo ]
	});
}

function resultadoBusca_cb(data, error) {

	if (data.idFilial == undefined || data.idFilial == "") {
		return false;
	} else {
		idFilialUsuarioLogado = data.idFilial;

	}
}

function validarFOB() {

	var tpFrete = getElementValue("tpFrete");
	if (getElementValue("tpFrete") == "F"
			&& (getElementValue("clienteByIdClienteDestino.idInscricaoEstadual") != "" || getElementValue("clienteByIdClienteDestino.tpSituacaoTributaria") != "")) {

		var remoteCall = {
			serviceDataObjects : new Array()
		};
		var dataCall = createServiceDataObject(
				"lms.vendas.gerarCotacoesAction.validaFOB",
				"validarFOB",
				{
					idInscricaoEstadualDestinatario : getElementValue("clienteByIdClienteDestino.idInscricaoEstadual"),
					tipoTributacao : getElementValue("clienteByIdClienteDestino.tpSituacaoTributaria"),
					idFilial : idFilialUsuarioLogado

				});
		remoteCall.serviceDataObjects.push(dataCall);
		xmit(remoteCall);
	} else {
		document.getElementById("calcularButton").disabled = false;
	}

}

function validarFOB_cb(data, error) {

	if (!error) {
		
		if (data.validaFOB == "true") {

			alert("LMS-01321 - " + i18NLabel.getLabel("LMS-01321"));
			document.getElementById("calcularButton").disabled = true;

		} else {

			alert("LMS-01322 - " + i18NLabel.getLabel("LMS-01322"));
			document.getElementById("calcularButton").disabled = false;
		}
	} else {
		alert(error);

	}

}

/** FIM */

/**
 * Rotinas Iniciais de tela
 * 
 * INICIO
 */

function myOnDataLoad_cb(data, errorMessage, errorCode, eventObj) {

	onDataLoad_cb(data, errorMessage, errorCode, eventObj);
	if (!errorCode) {
		if (data.pendencia) {
			setElementValue("pendencia_idPendencia", data.pendencia.idPendencia);
		} else {
			resetValue("pendencia_idPendencia");
		}
		habilitaDesabilitaCampos();
		var newUrl = new URL(document.location.href);
		var idProcessoWorkflow = newUrl.parameters.idProcessoWorkflow;
		if (idProcessoWorkflow != undefined && idProcessoWorkflow != ""
				&& data.servico.tpModal.value == "A") {
			desabilitaCamposEBotoes();
		} else {
			if (isUpdateMode()) {
				setElementValue("nrCotacao", data.filial.sgFilial + " "
						+ data.nrCotacao);
				configureButtons();
				setDisabledSubParametros(true);

				var comParametros = (stringToNumber(data.countParametros) > 0);
				setDisabledParametrosCliente(!comParametros);

				var comServAdd = (stringToNumber(data.countServicosAdicionais) > 0);
				setDisabledServicosCliente(!comServAdd);

				setElementValue('blServicosAdicionais', comServAdd);
				setElementValue('blFrete',
						stringToNumber(data.vlTotalParcelas) > 0);
			} else {
				setDisabledParametrosCliente(true);
				setDisabledServicosCliente(true);
				setDisabledSubParametros(true);
			}
		}
	}
}

function desabilitaCamposEBotoes() {
	setDisabled("observacaoButton", true);
	setDisabled("servicoButton", true);
	setDisabled("calcularButton", true);
	setDisabled("pedidoButton", true);
	setDisabled("reprovarButton", true);
	setDisabled("imprimirButton", true);
	setDisabled("cancelarButton", true);
	setDisabled("servico.idServico", true);
	setDisabled("tpCalculo", true);
	setDisabled("nrTelefone", true);
	setDisabled("dsEmail", true);
	setDisabled("nrFax", true);
	setDisabled("nrNotaFiscal", true);
	setDisabled("moeda.idMoeda", true);
	setDisabled("vlMercadoria", true);
	setDisabled("qtVolumes", true);
	setDisabled("psReal", true);
	setDisabled("psCubado", true);
	setDisabled("blMercadoriaExportacao", true);
	setDisabled("naturezaProduto.idNaturezaProduto", true);
}

function initWindow(eventObj) {
	var event = eventObj.name;
	var tabGroup = getTabGroup(this.document);
	var tab = null
	if (tabGroup) {
		tab = tabGroup.getTab("cad");
	}
	if (event == "newButton_click") {
		inicializaTela();
	} else if (event == "tab_click") {
		if (tabGroup) {
			if (tabGroup.oldSelectedTab.properties.id == "pesq") {
				inicializaTela();
			}
		}
		setDisabledSubParametros(true);
	}
	configureButtons();
	setFocusOnFirstFocusableField();
	if (tab) {
		tab.setChanged(false);
	}
}

function inicializaTela() {
	findUsuarioLogado();
	carregaDadosFilial();
	clienteByIdClienteSolicitouData = undefined;
	clienteByIdClienteDestinoData = undefined;

	var nrIdR = getElement("clienteByIdClienteSolicitou.pessoa.nrIdentificacao");
	nrIdR.required = "false";
	nrIdR.serializable = true;

	var nrIdD = getElement("clienteByIdClienteDestino.pessoa.nrIdentificacao");
	nrIdD.required = "false";
	nrIdD.serializable = true;

	var nrId = getElement("clienteByIdCliente.pessoa.nrIdentificacao");
	nrId.required = "false";
	nrId.serializable = true;

	getElement("moeda.idMoeda").required = "true";
	setElementValue("blFrete", "true");
	setElementValue("tpCalculo", "N");
	setElementValue("tpFrete", "C");
	habilitaDesabilitaCampos();
	setServicoPadrao();
	habilitaSituacaoIe();
	setDisabledParametros(true);
	setMoedaPadrao();
	loadInscricaoEstadual("Solicitou");
	loadInscricaoEstadual("Destino");
	loadInscricaoEstadual("");
	calculado = false;

	var sdo = createServiceDataObject(
			"lms.vendas.gerarCotacoesAction.inicializaTela", "inicializaTela");
	xmit({
		serviceDataObjects : [ sdo ]
	});
}

function inicializaTela_cb(data) {
	setElementValue('dtValidade', setFormat('dtValidade', data.dtValidade));
}
/** FIM */

/**
 * Rotinas de Configura��es de telas e Campos
 * 
 * INICIO
 */

function configureButtons() {
	if (isUpdateMode()) {
		setDisabled("servicoButton", false);
		setDisabled("imprimirButton", false);
		setDisabled("cancelarButton", false);
		setDisabled("observacaoButton", false);
		setDisabled("calcularButton", false);
		var sit = getElementValue("tpSituacao");
		if (sit == "O" || sit == "P" || sit == "E")
			setDisabled("pedidoButton", true);
		else
			setDisabled("pedidoButton", false);
		if (sit == "O" || sit == "E")
			setDisabled("reprovarButton", true);
		else
			setDisabled("reprovarButton", false);
	} else {
		if (calculado == true) {
			setDisabled("servicoButton", true)
		} else {
			setDisabled("observacaoButton", false);
			var check = getElementValue("blServicosAdicionais");
			if (check == true || check == "true")
				setDisabled("servicoButton", false);
			else
				setDisabled("servicoButton", true);
		}
		setDisabled("observacaoButton", false);
		setDisabled("calcularButton", false);
		setDisabled("cancelarButton", false);
		setDisabled("reprovarButton", true);
		setDisabled("pedidoButton", true);
		setDisabled("imprimirButton", true);
	}
	setDisabled("btnFluxoAprovacao",
			getElementValue("pendencia_idPendencia") == "");
	setDisabled("dimensaoButton", false);
}

/** FIM */

/**
 * INICIO Rotina para validar quantidade de caracteres digitados no nome do
 * cliente
 */
function clienteSolicitouNameChange() {
	return clienteOnNameChange("Solicitou", "remetente")
}

function clienteDestinoNameChange() {
	return clienteOnNameChange("Destino", "destinatario")
}

function clienteNameChange() {
	return clienteOnNameChange("", "responsavelFrete")
}

function clienteOnNameChange(tipo, msg) {
	var name = "clienteByIdCliente" + tipo + ".pessoa.nmPessoa";
	var val = getElementValue(name);
	if (val.length > 0 && val.length < 3) {
		alertI18nMessage("LMS-04021", new Array(msg));
		setFocus(name, true);
		return false
	}
	return true
}
/** FIM */

/**
 * Rotina referente a Servi�os
 * 
 * INICIO
 */
function servicoOnChange(combo) {
	comboboxChange({
		e : combo
	});
	if (getElementValue(combo) == "") {
		resetFilial("Origem");
		resetFilial("Destino");
	} else {
		findFilial(getElementValue("municipioByIdMunicipioOrigem.idMunicipio"),
				"Origem");
		findFilial(
				getElementValue("municipioByIdMunicipioDestino.idMunicipio"),
				"Destino");
	}
	var tpModal = getElementValue("servico.tpModal");
	if (tpModal == 'A') {
		removerItemTpCalculo(document.getElementById("tpCalculo"));
		desabilitaAereo(false, false);
		if (getElementValue("municipioByIdMunicipioOrigem.idMunicipio") != "")
			findAeroporto(getElementValue("filialByIdFilialOrigem.idFilial"),
					"Origem");
		if (getElementValue("municipioByIdMunicipioDestino.idMunicipio") != "")
			findAeroporto(getElementValue("filialByIdFilialDestino.idFilial"),
					"Destino");
	} else {
		desabilitaAereo(true, true);
		adicionarItemTpCalculo(document.getElementById("tpCalculo"));
	}
	calculaPesoCubado();
	calculaPpe();
}

/**
 * remove item cortesia se servico combo for Aereo
 * 
 * @param selectTpCalculo
 */
function removerItemTpCalculo(selectTpCalculo) {
	for (i = 0; i < selectTpCalculo.length; i++) {
		if (selectTpCalculo.options[i].text == "Cortesia") {
			selectTpCalculo.remove(i);
			selectTpCalculo[1].selected = true;
		}
	}
}

/**
 * adiciona item se foi removido (size==2)
 * 
 * @param selectTpCalculo
 */
function adicionarItemTpCalculo(selectTpCalculo) {
	if (selectTpCalculo.length < 3) {
		var opt3 = document.createElement("option");
		opt3.value = "G";
		opt3.text = "Cortesia";
		selectTpCalculo.add(opt3);
		opt3.innerText = "Cortesia";
	}
}

function servico_cb(data) {
	if (data) {

		servico_idServico_cb(data);
		for (var i = 0; i < data.length; i++) {
			if (data[i].servicoPadrao) {
				idServicoPadrao = data[i].idServico;
				setServicoPadrao();
				return;
			}
		}

	}
}

// seta o servico padrao
function setServicoPadrao() {
	if (idServicoPadrao) {
		setElementValue("servico.idServico", idServicoPadrao);
		servicoOnChange(getElement("servico.idServico"));
	}
}

/** FIM */

/**
 * Rotina Referente a Moeda do Pais
 * 
 * INICIO
 */
function moedaPais_cb(dados) {
	moeda_idMoeda_cb(dados);
	if (dados) {
		for (var i = 0; i < dados.length; i++) {
			var indUtil = dados[i].blIndicadorMaisUtilizada;
			if (indUtil == true || indUtil == 'true') {
				idMoedaPadrao = dados[i].idMoeda;
				setElementValue("moeda.idMoeda", idMoedaPadrao);
				return;
			}
		}
	}
}

function setMoedaPadrao() {
	setElementValue("moeda.idMoeda", idMoedaPadrao);
}
/** FIM* */

/**
 * Rotinas referente ao lookup de Clientes
 * 
 * INICIO
 */
function clienteOnChange(tipo) {
	var preFix = "clienteByIdCliente" + tipo;

	var isEmpty = getElementValue(preFix + ".pessoa.nrIdentificacao") == "";

	if (isEmpty) {
		setDisabled(preFix + ".pessoa.nmPessoa", false);
		setDisabled(preFix + ".idInscricaoEstadual", false);

		configuraInscricaoEstadual(tipo, undefined);
		resetValue("municipioByIdMunicipio" + convertTipo(tipo)
				+ ".idMunicipio");
		resetFilial(tipo);
	}
	setDisabled(preFix + ".tpSituacaoTributaria", !isEmpty);
}

function clienteOnCallback(data, tipo) {
	if (data == undefined || data.length == 0) {
		var nrId = getElementValue("clienteByIdCliente" + tipo
				+ ".pessoa.nrIdentificacao");
		resetValue("clienteByIdCliente" + tipo + ".idCliente");
		setElementValue(
				"clienteByIdCliente" + tipo + ".pessoa.nrIdentificacao", nrId);
		setDisabled("clienteByIdCliente" + tipo + ".pessoa.nmPessoa", false);
		setFocus(getElement("clienteByIdCliente" + tipo + ".pessoa.nmPessoa"),
				true);
		clienteByIdClienteSolicitouData = undefined;
		clienteByIdClienteDestinoData = undefined;
		configuraInscricaoEstadual(tipo, undefined);
		setDisabled("clienteByIdCliente" + tipo + ".tpSituacaoTributaria",
				false);
	} else {
		setDisabled("clienteByIdCliente" + tipo + ".pessoa.nmPessoa", true);
		configuraInscricaoEstadual(tipo, data[0].inscricaoEstadual);
		populaMunicipio(data[0].pessoa.endereco, tipo);
		setDisabled("clienteByIdCliente" + tipo + ".tpSituacaoTributaria", true);
	}
}

function clienteOnPopup(data, tipo) {
	if (data) {
		var callback = "cliente" + tipo;
		var sdo = createServiceDataObject(
				"lms.vendas.gerarCotacoesAction.findCliente", callback, {
					pessoa : {
						nrIdentificacao : data.pessoa.nrIdentificacao
					}
				});
		xmit({
			serviceDataObjects : [ sdo ]
		});
	}
}

/*
 * Cliente Origem
 * ===========================================================================
 */
function clienteSolicitouChange() {
	clienteByIdClienteSolicitouData = undefined;
	clienteOnChange("Solicitou");
	return clienteByIdClienteSolicitou_pessoa_nrIdentificacaoOnChangeHandler();
}

function clienteSolicitou_cb(data) {
	clienteByIdClienteSolicitouData = data;
	clienteOnCallback(data, "Solicitou");
	return clienteByIdClienteSolicitou_pessoa_nrIdentificacao_exactMatch_cb(data);
}

function clienteSolicitouPopup(data) {
	clienteOnPopup(data, "Solicitou");
	return true;
}

/*
 * Cliente Destino
 * ===========================================================================
 */
function clienteDestinoChange() {
	clienteByIdClienteDestinoData = undefined;
	clienteOnChange("Destino");
	return clienteByIdClienteDestino_pessoa_nrIdentificacaoOnChangeHandler();
}

function clienteDestino_cb(data) {
	clienteByIdClienteDestinoData = data;
	clienteOnCallback(data, "Destino");
	return clienteByIdClienteDestino_pessoa_nrIdentificacao_exactMatch_cb(data);
}

function clienteDestinoPopup(data) {
	clienteOnPopup(data, "Destino");
	return true;
}

/*
 * Cliente Responsavel
 * ===========================================================================
 */
function clienteChange() {
	resetValue("clienteByIdCliente.idInscricaoEstadual");
	clienteOnChange("");
	return clienteByIdCliente_pessoa_nrIdentificacaoOnChangeHandler();
}

function cliente_cb(data) {
	clienteOnCallback(data, "");
	clienteByIdCliente_pessoa_nrIdentificacao_exactMatch_cb(data);
	return validaTipoPessoa();
}

function clientePopup(data) {
	clienteOnPopup(data, "");
	return true;
}

/** FIM */

/**
 * Rotinas do Lookup de Municipios
 * 
 */
function lookupMunicipioExactMatch(data, tipo) {
	var cb = "lookupMunicipio" + tipo + "ExactMatch";
	var result = lookupExactMatch({
		e : document.getElementById("municipioByIdMunicipio" + tipo
				+ ".idMunicipio"),
		data : data,
		callBack : cb
	});

	if (data.length == 1) {
		findFilial(data[0].idMunicipio, tipo);
		calculaPpe();
	}

	return result;
}

function lookupMunicipioLikeEnd(data, tipo) {
	var result = eval("municipioByIdMunicipio" + tipo
			+ "_nmMunicipio_likeEndMatch_cb(data);");

	if (data == undefined || data.length == 0) {
		resetValue("municipioByIdMunicipio" + tipo + ".idMunicipio");
		resetFilial(tipo);
	} else if (data.length == 1) {
		findFilial(data[0].idMunicipio, tipo);
	}

	return result;
}

function resetFilial(tipo) {
	tipo = convertTipo(tipo);
	resetValue("filialByIdFilial" + tipo + ".idFilial");
	resetValue("filialByIdFilial" + tipo + ".sgFilial");
	if (tipo != "Responsavel") {
		resetValue("idTipoLocalizacao" + tipo);
	}
}

/*
 * Municipio Origem
 * ==============================================================================
 */
function municipioOrigemChange() {
	var nm = getElementValue("municipioByIdMunicipioOrigem.nmMunicipio");
	if (nm == "") {
		resetFilial("Origem");
		calculaPpe();
	}
	return municipioByIdMunicipioOrigem_nmMunicipioOnChangeHandler();
}

function municipioOrigem_cb(data) {
	return lookupMunicipioExactMatch(data, "Origem");
}

function lookupMunicipioOrigemExactMatch_cb(data) {
	return lookupMunicipioLikeEnd(data, "Origem");
}

function municipioOrigemPopup(data) {
	if (data)
		findFilial(data.idMunicipio, "Origem");
	calculaPpe();
	return true;
}

/*
 * Municipio Destino
 * ==============================================================================
 */
function municipioDestinoChange() {
	var nm = getElementValue("municipioByIdMunicipioDestino.nmMunicipio");
	if (nm == "") {
		resetFilial("Destino");
		calculaPpe();
	}
	return municipioByIdMunicipioDestino_nmMunicipioOnChangeHandler();
}

function municipioDestinoPopup(data) {
	if (data)
		findFilial(data.idMunicipio, "Destino");
	calculaPpe();
	return true;
}

function municipioDestino_cb(data) {
	return lookupMunicipioExactMatch(data, "Destino");
}

function lookupMunicipioDestinoExactMatch_cb(data) {
	return lookupMunicipioLikeEnd(data, "Destino");
}

/*
 * Municipio Responsavel
 * ==============================================================================
 */
function municipioResponsavelChange() {
	var nm = getElementValue("municipioByIdMunicipioResponsavel.nmMunicipio");
	if (nm == "")
		resetFilial("Responsavel");
	return municipioByIdMunicipioResponsavel_nmMunicipioOnChangeHandler();
}

function municipioResponsavelPopup(data) {
	if (data)
		findFilial(data.idMunicipio, "Responsavel");
	return true;
}

function municipioResponsavel_cb(data) {
	return lookupMunicipioExactMatch(data, "Responsavel");
}

function lookupMunicipioResponsavelExactMatch_cb(data) {
	return lookupMunicipioLikeEnd(data, "Responsavel");
}

/** FIM */

/**
 * Rotinas referente ao Lookup Filial
 * 
 */

function populaMunicipio(data, tipo) {
	tipo = convertTipo(tipo);
	if (data) {
		setElementValue("municipioByIdMunicipio" + tipo + ".nmMunicipio",
				data.nmMunicipio);
		setElementValue("municipioByIdMunicipio" + tipo + ".idMunicipio",
				data.idMunicipio);
		notifyElementListeners({
			e : getElement("municipioByIdMunicipio" + tipo + ".idMunicipio")
		});
		findFilial(data.idMunicipio, tipo);
		if (tipo != "Responsavel") {
			calculaPpe();
		}
	} else {
		resetValue("municipioByIdMunicipio" + tipo + ".idMunicipio");
		resetFilial(tipo);
	}
}

function findMunicipioByPessoa(tipo, data) {
	if (data) {
		var sdo = createServiceDataObject(
				"lms.vendas.gerarCotacoesAction.findMunicipioByPessoa",
				"filial" + tipo + "Popup", {
					idPessoa : data.pessoa.idPessoa
				});
		xmit({
			serviceDataObjects : [ sdo ]
		});
	}
}

function resetMunicipio(tipo) {
	tipo = convertTipo(tipo);
	var sg = getElementValue("filialByIdFilial" + tipo + ".sgFilial");
	if (sg == "") {
		resetValue("municipioByIdMunicipio" + tipo + ".nmMunicipio");
		resetValue("municipioByIdMunicipio" + tipo + ".idMunicipio");
		if (tipo != "Responsavel") {
			resetValue("idTipoLocalizacao" + tipo);
		}
	}
}

function lookupExactMatchFilial(tipo, data) {
	tipo = convertTipo(tipo);
	return lookupExactMatch({
		e : document.getElementById("filialByIdFilial" + tipo + ".idFilial"),
		data : data
	});
}

/*
 * Filial Origem
 * ==============================================================================
 */
function filialOrigemLoad_cb(data) {
	var result = lookupExactMatchFilial("Origem", data);
	if (data.length == 1) {
		findMunicipioByPessoa("Origem", data[0]);
	}
	return result;
}

function filialOrigemChange() {
	resetMunicipio("Origem");
	return filialByIdFilialOrigem_sgFilialOnChangeHandler();
}

function filialOrigemPopup(data) {
	findMunicipioByPessoa("Origem", data);
}

function filialOrigemPopup_cb(data) {
	populaMunicipio(data, "Origem");
}

/*
 * Filial Destino
 * ==============================================================================
 */
function filialDestinoLoad_cb(data) {
	var result = lookupExactMatchFilial("Destino", data);
	if (data.length == 1) {
		findMunicipioByPessoa("Destino", data[0]);
	}
	return result;
}

function filialDestinoChange() {
	resetMunicipio("Destino");
	return filialByIdFilialDestino_sgFilialOnChangeHandler();
}

function filialDestinoPopup(data) {
	findMunicipioByPessoa("Destino", data);
}

function filialDestinoPopup_cb(data) {
	populaMunicipio(data, "Destino");
}

/*
 * Filial Responsavel
 * ==============================================================================
 */
function filialResponsavelLoad_cb(data) {
	var result = lookupExactMatchFilial("Responsavel", data);
	if (data.length == 1) {
		findMunicipioByPessoa("Responsavel", data[0]);
	}
	return result;
}

function filialResponsavelChange() {
	resetMunicipio("Responsavel");
	return filialByIdFilialResponsavel_sgFilialOnChangeHandler();
}

function filialResponsavelPopup(data) {
	findMunicipioResponsavelByPessoa("Responsavel", data);
}

function filialResponsavelPopup_cb(data) {
	populaMunicipio(data, "Responsavel");
}
/** FIM */

/**
 * Rotina para buscar o municipio atendido pela Filial
 * 
 * INICIO
 */
function findFilial(idMunicipio, tipo) {
	if (idMunicipio) {
		var nrIdentificacaoRemet = getElementValue("clienteByIdClienteSolicitou.pessoa.nrIdentificacao");
		var nrIdentificacaoResp = getElementValue("clienteByIdCliente.pessoa.nrIdentificacao");
		var params = {
			idMunicipio : idMunicipio,
			idServico : getElementValue("servico.idServico"),
			tipoIntegrante : tipo,
			nrIdentificacaoRemetente : nrIdentificacaoRemet,
			nrIdentificacaoResponsavel : nrIdentificacaoResp
		};
		var sdo = createServiceDataObject(
				"lms.vendas.gerarCotacoesAction.findFilialAtendimento",
				"filial" + tipo, params);
		xmit({
			serviceDataObjects : [ sdo ]
		});
	} else
		resetFilial(tipo);
}

function filialOrigem_cb(data, erros) {
	filialCallback(data, erros, "Origem");
}

function filialDestino_cb(data, erros) {
	filialCallback(data, erros, "Destino");
}

function filialResponsavel_cb(data, erros) {
	filialCallback(data, erros, "Responsavel");
}

function filialCallback(data, erros, tipo) {
	if (erros) {
		resetFilial(tipo);
		alert(erros);
		return false;
	}
	setElementValue("filialByIdFilial" + tipo + ".sgFilial", data.sgFilial);
	setElementValue("filialByIdFilial" + tipo + ".idFilial", data.idFilial);
	if (tipo != "Responsavel") {
		setElementValue("idTipoLocalizacao" + tipo,
				data.idTipoLocalizacaoMunicipio);
	}
	findAeroporto(data.idFilial, tipo);
	calculaPpe();
}
/** FIM */

/**
 * Rotinas para altera��o da Inscri��o Estadual INICIO
 */
function changeInscricaoEstadual(objCaller) {

	comboboxChange({
		e : objCaller
	});

	var tpFrete = getElementValue("tpFrete");

	if (tpFrete == "C") {
		tipo = "Solicitou";
	} else if (tpFrete == "F") {
		tipo = "Destino";
		if (objCaller.id == "clienteByIdClienteDestino.idInscricaoEstadual") {
			validarFOB();
		}

	} else {
		return false;
	}

	var clienteOrigem = "clienteByIdCliente" + tipo;
	var clienteDestino = "clienteByIdCliente";

	setElementValue(clienteDestino + ".tpSituacaoTributaria",
			getElementValue(clienteOrigem + ".tpSituacaoTributaria"));

	var selectedIe = getElementValue(clienteOrigem + ".idInscricaoEstadual");
	setElementValue(clienteDestino + ".idInscricaoEstadual", selectedIe);

	return true;
}

function configuraInscricaoEstadual(tipo, data) {
	var from = convertTipo(tipo);
	loadInscricaoEstadual(tipo, data);
	resetValue("clienteByIdCliente" + tipo + ".tpSituacaoTributaria");
	disabledIEByTpCliente(tipo);
	if (!data) {
		return false;
	}
	if (data.length == 1) {
		setElementValue("clienteByIdCliente" + tipo + ".tpSituacaoTributaria",
				data[0].tpSituacaoTributaria.value);
		if (tipo != "") {
			setFocus(getElement("municipioByIdMunicipio" + from
					+ ".nmMunicipio"), true);
		} else {
			setFocus(getElement("tpDevedorFrete"), true);
		}
	}
	setDisabled("clienteByIdCliente" + tipo + ".idInscricaoEstadual",
			(data.length == 1));

	if ((tipo == "Solicitou") || (tipo == "Destino")) {
		fromGlobal = tipo;
		copyToResponsavelFrete();
	}
	var tpFrete = getElementValue("tpFrete");
	if (tipo == "Destino" && tpFrete == "F") {
		validarFOB();
	}
	return true;
}
/** FIM */

/**
 * Rotina para pesquisa de Aeroportos
 * 
 * INICIO
 */
function findAeroporto(idFilial, tipo) {
	if (tipo != 'Responsavel') {
		var tpModal = getElementValue("servico.tpModal");
		if (tpModal == "A" && idFilial != "") {
			var parameters = {
				idFilial : idFilial
			};
			var service = "lms.vendas.gerarCotacoesAction.findAeroportoAtendido";
			var sdo = createServiceDataObject(service, "findAeroporto" + tipo,
					parameters);
			xmit({
				serviceDataObjects : [ sdo ]
			});
		}
	}
}

function findAeroportoOrigem_cb(data, error) {
	setAeroporto(data, "aeroportoByIdAeroportoOrigem");
}

function findAeroportoDestino_cb(data, error) {
	setAeroporto(data, "aeroportoByIdAeroportoDestino");
}

function findAeroportoResponsavel_cb(data, error) {

}

function setAeroporto(data, tipo) {
	setElementValue(tipo + ".idAeroporto", data.idAeroporto);
	setElementValue(tipo + ".sgAeroporto", data.sgAeroporto);
	setElementValue(tipo + ".pessoa.nmPessoa", data.nmAeroporto);
}
/** FIM */

/**
 * Rotina acionada quando altera Tipo de Frete
 */
function changeTpFrete() {
	validarFOB();

	copyToResponsavelFrete();
	return true;
}
/** FIM */

/**
 * Rotina para copiar os dados do Responsavel pelo frete
 * 
 * INICIO
 */
function copyToResponsavelFrete() {
	var tpFrete = getElementValue("tpFrete");

	if (tpFrete == "C") {
		tipo = "Solicitou";
	} else if (tpFrete == "F") {
		tipo = "Destino";
	} else {
		return;
	}
	var data = eval("clienteByIdCliente" + tipo + "Data");
	if (!data) {
		validaTipoPessoa();
		return false;
	}
	var clienteOrigem = "clienteByIdCliente" + tipo;
	var clienteDestino = "clienteByIdCliente";

	resetValue(clienteDestino + ".idCliente");

	clienteResponsavelOnPopup(data[0], tipo);
}

function clienteResponsavelOnPopup(data, tipo) {
	if (data) {
		if (tipo && tipo.length == 0) {
			tipo = '';
		}
		var sdo = createServiceDataObject(
				"lms.vendas.gerarCotacoesAction.findClienteResponsavel",
				"clienteResponsavel", {
					tipoOrigem : tipo,
					pessoa : {
						nrIdentificacao : data.pessoa.nrIdentificacao
					}
				});
		xmit({
			serviceDataObjects : [ sdo ]
		});

	} else {
		clearClienteResponsavel();
	}
}

function clienteResponsavel_cb(data) {

	if (data && data.length > 0) {
		var clienteOrigem = "clienteByIdCliente" + data[0].tipoOrigem;
		var clienteDestino = "clienteByIdCliente";

		setElementValue(clienteDestino + ".idCliente", data[0].idCliente);
		setElementValue(clienteDestino + ".pessoa.nrIdentificacao",
				data[0].pessoa.nrIdentificacao);
		setElementValue(clienteDestino + ".pessoa.nmPessoa",
				data[0].pessoa.nmPessoa);
		setElementValue(clienteDestino + ".tpCliente", data[0].tpCliente);
		setElementValue(clienteDestino + ".tpCliente.description",
				data[0].tpCliente.description);
		setElementValue("tpDevedorFrete", data[0].pessoa.tpPessoa);
		setElementValue(clienteDestino + ".tpSituacaoTributaria",
				getElementValue(clienteOrigem + ".tpSituacaoTributaria"));

		cliente_cb(data);

		loadInscricaoEstadual('', data[0].inscricaoEstadual);

		var selectedIe = getElementValue(clienteOrigem + ".idInscricaoEstadual");
		setElementValue(clienteDestino + ".idInscricaoEstadual", selectedIe);

		setDisabled(clienteDestino + ".pessoa.nmPessoa", true);
		setDisabled(clienteDestino + ".tpCliente.description", true);
		validaTipoPessoa();

		if (fromGlobal != undefined) {
			fromGlobal = convertTipo(fromGlobal);
			setFocus(getElement("municipioByIdMunicipio" + fromGlobal
					+ ".nmMunicipio"), true);
			fromGlobal = undefined;
		}

	} else {
		clearClienteResponsavel();
	}
}

function clearClienteResponsavel() {
	var clienteDestino = "clienteByIdCliente";
	resetValue(clienteDestino + ".idCliente");
	resetValue(clienteDestino + ".pessoa.nrIdentificacao");
	resetValue(clienteDestino + ".pessoa.nmPessoa");
	resetValue(clienteDestino + ".tpCliente");
	resetValue(clienteDestino + ".tpCliente.description");
	resetValue("tpDevedorFrete");
	resetValue(clienteDestino + ".tpSituacaoTributaria");
	resetValue(clienteDestino + ".idInscricaoEstadual");
}
/** FIM */

/**
 * Rotinas realizadas apartir do Click do Bot�o Calcula Cota��o INICIO
 */

/*
 * Callback chamada no bot�o da tela, ao executar a primeira fase de valida��es
 * e re-executada na segunda fase de valida��es
 */
function validateCotacao_cb(data, errorMsg, errorKey) {
	validarColetaEntregaEmergencia();
	if (validateErrorCotacao(errorMsg, errorKey) == false) {
		if (data) {
			var objDt = getElement("dtGeracaoCotacao");
			setElementValue(objDt, setFormat(objDt, data.dtGeracaoCotacao));
			objDt = getElement("dtValidade");
			setElementValue(objDt, setFormat(objDt, data.dtValidade));
			setElementValue("nrPpe", data.nrPpe);

			var retornoCalculo = openCalcularCotacao(getElementValue("idCotacao"));

			var tabGroup = getTabGroup(this.document);
			if (tabGroup) {
				tabGroup.setDisabledTab("taxas", true);
				tabGroup.setDisabledTab("gen", true);
			}
			return retornoCalculo
		}
	}
	return false;
}

/*
 * Valida Libera��o Cotacao
 */
function validateErrorCotacao(errorMsg, errorKey) {
	if (!errorMsg) {
		return false;
	}
	var execute = false;

	if ("LMS-04053" == errorKey) {
		/* Valida Cliente Embarque Proibido */
		if (confirm(errorMsg)) {
			execute = openLiberacaoEmbarque("CP"); // LIBERACAO_CLIENTE_EMBARQUE_PROIBIDO
		}
	} else if ("LMS-04054" == errorKey) {
		/* Valida Municipio Embarque Proibido */
		if (confirm(errorMsg)) {
			execute = openLiberacaoEmbarque("MP"); // LIBERACAO_MUNICIPIO_EMBARQUE_PROIBIDO
		}
	} else if ("LMS-04050" == errorKey) {
		/* Valida Calculo Frete Cortesia */
		if (confirm(errorMsg)) {
			execute = openLiberacaoEmbarque("CC"); // LIBERACAO_CALCULO_CORTESIA
		}
	} else {
		alert(errorMsg);
		return true;
	}
	// Continua excu��o
	if (execute == true) {
		executeValidateCotacaoSegundaFase();
		return true;
	}
	return !execute;
}

function executeValidateCotacaoSegundaFase() {
	var sdo = createServiceDataObject(
			"lms.vendas.gerarCotacoesAction.validateCotacaoSegundaFase",
			"validateCotacao");
	xmit({
		serviceDataObjects : [ sdo ]
	});
}

/*
 * Abre a janela de calculo de cota��o apos as valida��es
 */
function openCalcularCotacao(idCotacao) {
	var urlCalculo = 'vendas/gerarCotacoes.do?cmd=popup';
	if (isUpdateMode())
		urlCalculo = urlCalculo + '&idCotacao=' + idCotacao
	var data = showModalDialog(
			urlCalculo,
			window,
			'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:630px;');
	if (!data) {
		return false;
	}
	if (data.idCotacao) {
		newButtonScript(document);
		return true;
	} else if (data.action == "cancelarCalculo") {
		newButtonScript(document, true, {
			name : 'newButton_click'
		});
		return false;
	} else if ((data.action == "calculado") && !isUpdateMode()) {
		calculado = true;
		if (data.tpTipoTabelaPreco != "F") {
			habilitaParametrizacao();
		} else {
			desabilitaParametrizacao();
		}
		setFocus("calcularButton", false);
		return false;
	}
}
/** FIM* */

/**
 * Habilita e Desabilita componentes
 * 
 * INICIO
 */
function habilitaParametrizacao() {
	setDisabledParametros(false);
	habilitaDesabilitaCampos();
	desabilitaIntegrantes(isUpdateMode())
	configureButtons();
}

function desabilitaParametrizacao() {
	setDisabledParametros(true);
	setDisabledSubParametros(true);
	habilitaDesabilitaCampos();
	desabilitaIntegrantes(isUpdateMode());
	configureButtons();
}

function setDisabledParametros(dis) {
	var tabGroup = getTabGroup(this.document);
	if (tabGroup) {
		tabGroup.setDisabledTab("param", dis);
		tabGroup.setDisabledTab("servAd", dis);
	}
}

function setDisabledSubParametros(dis) {
	var tabGroup = getTabGroup(this.document);
	if (tabGroup) {
		tabGroup.setDisabledTab("gen", dis);
		tabGroup.setDisabledTab("taxas", dis);
	}
}

function habilitaDesabilitaCampos() {
	setDisabled(document, false);
	configureButtons();
	setDisabled("nrCotacao", true);
	setDisabled("tpDocumentoCotacao", true);
	setDisabled("clienteByIdClienteSolicitou.tpCliente.description", true);
	setDisabled("clienteByIdClienteDestino.tpCliente.description", true);
	setDisabled("clienteByIdCliente.tpCliente.description", true);
	setDisabled("nrPpe", true);
	setDisabled("dtValidade", true);
	setDisabled("pedidoColeta.nrColeta", true);
	setDisabled("dtGeracaoCotacao", true);
	setDisabled("usuarioByIdUsuarioRealizou.funcionario.nrMatricula", true);
	setDisabled("usuarioByIdUsuarioRealizou.funcionario.nmUsuario", true);
	setDisabled("usuarioByIdUsuarioAprovou.funcionario.nrMatricula", true);
	setDisabled("usuarioByIdUsuarioAprovou.funcionario.nmUsuario", true);
	setDisabled("tpSituacao.description", true);
	setDisabled("dtEfetivacao", true);
	setDisabled("nrDocumentoCotacao", true);
	desabilitaAereo(true, false);
	desabilitaIntegrantes(isUpdateMode())
}

function isUndefined(value) {
	return (value == undefined || value == "" || value == null);
}
function desabilitaIntegrantes(valor) {
	setDisabled("clienteByIdClienteSolicitou.pessoa.nrIdentificacao", valor);
	setDisabled(
			"clienteByIdClienteSolicitou.pessoa.nmPessoa",
			(!isUndefined(getElementValue("clienteByIdClienteSolicitou.idCliente"))));

	setDisabled("clienteByIdClienteSolicitou.tpSituacaoTributaria", valor);
	setDisabled("clienteByIdClienteSolicitou.idInscricaoEstadual", valor);
	setDisabled("municipioByIdMunicipioOrigem.nmMunicipio", valor);
	setDisabled("filialByIdFilialOrigem.sgFilial", valor);

	setDisabled("clienteByIdClienteDestino.pessoa.nrIdentificacao", valor);
	setDisabled(
			"clienteByIdClienteDestino.pessoa.nmPessoa",
			(!isUndefined(getElementValue("clienteByIdClienteDestino.idCliente"))));
	setDisabled("clienteByIdClienteDestino.tpSituacaoTributaria", valor);
	setDisabled("clienteByIdClienteDestino.idInscricaoEstadual", valor);
	setDisabled("municipioByIdMunicipioDestino.nmMunicipio", valor);
	setDisabled("filialByIdFilialDestino.sgFilial", valor);

	setDisabled("clienteByIdCliente.pessoa.nrIdentificacao", valor);
	setDisabled("clienteByIdCliente.pessoa.nmPessoa",
			(!isUndefined(getElementValue("clienteByIdCliente.idCliente"))));
	setDisabled("clienteByIdCliente.tpSituacaoTributaria", valor);
	setDisabled("clienteByIdCliente.idInscricaoEstadual", valor);
	setDisabled("municipioByIdMunicipioResponsavel.nmMunicipio", valor);
	setDisabled("filialByIdFilialResponsavel.sgFilial", valor);

	setDisabled("tpDevedorFrete", valor);
	setDisabled("tpFrete", valor);
}

function habilitaServicoAdicional(servico) {
	var chck = servico.checked;
	var servBt = getElement("servicoButton");
	if (chck == false && servBt.disabled == false)
		removeServicosAdicionais();
	setDisabled(servBt, !chck);
}

function setDisabledParametrosCliente(dis) {
	var tabGroup = getTabGroup(this.document);
	if (tabGroup) {
		tabGroup.setDisabledTab("param", dis);
	}
}

function setDisabledServicosCliente(dis) {
	var tabGroup = getTabGroup(this.document);
	if (tabGroup) {
		tabGroup.setDisabledTab("servAd", dis);
		setDisabled("servicoButton", dis);
	}
}

// desabilita e limpa os campos relacionados ao modal aereo
function desabilitaAereo(dis, reset) {
	setDisabled("aeroportoByIdAeroportoDestino.idAeroporto", dis);
	setDisabled("aeroportoByIdAeroportoOrigem.idAeroporto", dis);
	setDisabled("aeroportoByIdAeroportoDestino.pessoa.nmPessoa", true);
	setDisabled("aeroportoByIdAeroportoOrigem.pessoa.nmPessoa", true);
	setDisabled("blColetaEmergencia", dis);
	setDisabled("blEntregaEmergencia", dis);
	setDisabled("produtoEspecifico.idProdutoEspecifico", dis);
	if (reset == true) {
		resetValue("aeroportoByIdAeroportoDestino.idAeroporto");
		resetValue("aeroportoByIdAeroportoOrigem.idAeroporto");
		resetValue("blColetaEmergencia");
		resetValue("blEntregaEmergencia");
		resetValue("produtoEspecifico.idProdutoEspecifico");
	}
}

function habilitaSituacaoIe() {
	setDisabled("clienteByIdClienteSolicitou.idInscricaoEstadual", false);
	setDisabled("clienteByIdClienteDestino.idInscricaoEstadual", false);
	setDisabled("clienteByIdCliente.idInscricaoEstadual", false);
	setDisabled("clienteByIdClienteSolicitou.tpSituacaoTributaria", false);
	setDisabled("clienteByIdClienteDestino.tpSituacaoTributaria", false);
	setDisabled("clienteByIdCliente.tpSituacaoTributaria", false);
}

function disabledIEByTpCliente(tipo) {
	var tpCliente = "clienteByIdCliente" + tipo + ".tpCliente";
	var inscricaoEstadual = "clienteByIdCliente" + tipo
			+ ".idInscricaoEstadual";
	var isEventual = getElementValue(tpCliente) == 'E';
	setDisabled(inscricaoEstadual, isEventual);
}

function historicoAprovacao() {
	var idPendencia = getElementValue('pendencia_idPendencia');
	showModalDialog(
			'workflow/listarHistoricoPendencia.do?cmd=list&pendencia.idPendencia='
					+ getElementValue('pendencia_idPendencia'),
			window,
			'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');
}

function myOnPageLoad_cb() {
	var url = new URL(parent.location.href);
	if (url.parameters != undefined
			&& url.parameters.idProcessoWorkflow != undefined
			&& url.parameters.idProcessoWorkflow != '') {
		var group = getTabGroup(document);
		if (group) {
			group.getTab("pesq").setDisabled(true);
		}
		onDataLoad(url.parameters.idProcessoWorkflow);
	}
	onPageLoad_cb();
}

/** FIM */
