
function populateDadosCalculados(data) {
	setElementValue("idDoctoServico", data.idDoctoServico); 
	setElementValue("idFilialDestino", data.idFilialDestino);
	setElementValue("sgFilialDestino", data.sgFilialDestino);
}

function openDimensoes(idDocumento) {
	var url = "expedicao/digitarDimensoes.do?cmd=main";
	if(idDocumento) {
		url += "&idDocumento=" + idDocumento;
	}
	return openModalDialog(url, 450, 340);
}

function openDimensoesTpDocumento(idDocumento) {
	var url = "expedicao/digitarDimensoes.do?cmd=main";
	var newUrl = new URL(document.location.href);
	var idProcessoWorkflow = newUrl.parameters.idProcessoWorkflow;
	if(idDocumento) {
		url += "&idDocumento=" + idDocumento;
	}
	if( tpDocumento ){
		url += "&tpDocumento=" + tpDocumento;
	}
	if (idProcessoWorkflow != undefined && idProcessoWorkflow != "") {
		url += "&idProcessoWorkflow=" + idProcessoWorkflow;
	}
	return openModalDialog(url, 450, 340);
}

function openServicosAdicionais(idDocumento) {
	var url = "expedicao/digitarServicosAdicionais.do?cmd=main";
	if(idDocumento) {
		url += "&idDocumento=" + idDocumento;
	}
	return openModalDialog(url, 685, 385);
}

function openServicosAdicionaisTpDocumento(idDocumento) {
	var url = "expedicao/digitarServicosAdicionais.do?cmd=main";
	if(idDocumento) {
		url += "&idDocumento=" + idDocumento;
	}
	if( tpDocumento ){
		url += "&tpDocumento=" + tpDocumento;
	}
	return openModalDialog(url, 685, 385);
}


function openLiberacaoEmbarque(tpBloqueioLiberacao) {
	var url = 'expedicao/liberacaoEmbarque.do?cmd=main';
	if(tpBloqueioLiberacao) url += '&tpBloqueioLiberacao=' + tpBloqueioLiberacao;

	var data = openModalDialog(url, 660, 200);
	return data;
}

function validateCtrcOriginal(){
	var tpConhecimento = getElementValue("tpConhecimento");
	if(tpConhecimento == "RF" || tpConhecimento == "DP") {
		var idCtrc = getElementValue("doctoServicoOriginal.idDoctoServico");
		if(idCtrc == null || idCtrc == "") {
			alertI18nMessage("LMS-04067");
			setFocus(document.getElementById("doctoServicoOriginal.filialByIdFilialOrigem.sgFilial"), true);
			return false;	
		}
	} 
	return true;
}

function validateCotacao() {
	if( (getElementValue("tpCalculoPreco") == "C") && (getElementValue("cotacao.idCotacao") == "") ) {
		alertI18nMessage("LMS-04072");
		return false;
	}
	return true;	
}

function validateDensidade() {
	if( (getElementValue("densidade.idDensidade") != idDensidadePadrao) && (getElementValue("psAforado") != "") ) {
		alertI18nMessage("LMS-04066");
		return false;
	}
	return true;
}

function validateCliente() {
	if(getElementValue("clienteByIdClienteRemetente.idCliente") == "") {
		setFocus(document.getElementById("clienteByIdClienteRemetente.pessoa.nrIdentificacao"), false);
		return false;
	}
	if(getElementValue("clienteByIdClienteDestinatario.idCliente") == ""){
		setFocus(document.getElementById("clienteByIdClienteDestinatario.pessoa.nrIdentificacao"), false);
		return false;
	}
	return true;
}

function myPageLoad_cb() {
	setMasterLink(document, true);
	inicializaTela();
}

function filialConhecimento_cb(data) {
	if(data == undefined || data.length == 0){
		resetValue("doctoServicoOriginal.nrConhecimento");
		setDisabled("doctoServicoOriginal.nrConhecimento", true);
		setDisabled("doctoServicoOriginal.idDoctoServico", true);
	} else {
		setDisabled("doctoServicoOriginal.nrConhecimento", false);
		setDisabled("doctoServicoOriginal.idDoctoServico", false);
	}
	return doctoServicoOriginal_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
}

function filialConhecimentoChange() {
	var sgFilial = getElementValue("doctoServicoOriginal.filialByIdFilialOrigem.sgFilial");
	if(sgFilial == "") {
		resetValue("doctoServicoOriginal.nrConhecimento");
		setDisabled("doctoServicoOriginal.nrConhecimento", true);
		setDisabled("doctoServicoOriginal.idDoctoServico", true);
	}
	return doctoServicoOriginal_filialByIdFilialOrigem_sgFilialOnChangeHandler();
}

function doctoServicoOriginalChange() {
	var dvConhecimento = getElementValue("doctoServicoOriginal.dvConhecimento");
	if(dvConhecimento != "") {
		var nrConhecimento = getElementValue("doctoServicoOriginal.nrConhecimento");
		if(!validaDigitoVerificadorCTRC(nrConhecimento, dvConhecimento)) {
			alertI18nMessage("LMS-04082");
			return false;
		}
	}
	return doctoServicoOriginal_dvConhecimentoOnChangeHandler();
}

function changePsCubado(obj) {
	var psCubado = getElementValue(obj);
	if( (psCubado != "") && (stringToNumber(psCubado) <= 0) ) {
		alertI18nMessage("LMS-04021", new Array(obj.label), false);
		return false;
	}
	return true;
}

function notasFiscais_cb(data, error) {
	if(error) {
		var nrObj = document.getElementById("quantidadeNotasFiscais");
		resetValue(nrObj);
		setFocus(nrObj, false);
		disabledNotasFiscais(true);
		alert(error);
		return false;	
	}
	disabledNotasFiscais(false);	
	var tabGroup = getTabGroup(this.document);
	tabGroup.selectNextTab();
}

function changeTpFrete() {
	var tpDevedor = getElementValue("tpDevedorFrete");
	if(tpDevedor != "O") {
		var tpFrete = getElementValue("tpFrete");
		if(tpFrete == "C")
			setElementValue("tpDevedorFrete", "R");
		else if(tpFrete == "F")
			setElementValue("tpDevedorFrete", "D");	
	}
	return true;
}

function changeTpCalculoPreco() {
	var tpCalculo = getElementValue("tpCalculoPreco");
	if(tpCalculo == "G") {	
		var tpConhecimento = getElementValue("tpConhecimento");
		if(tpConhecimento == "RF" || tpConhecimento == "DP") {	
			alertI18nMessage("LMS-04055");
			resetValue("tpCalculoPreco");
			return false;
		}
	}
	if(tpCalculo != "C")
		setElementValue("cotacao.idCotacao", "");
	return true;
}

function changeNrCotacao(combo) {
	comboboxChange({e:combo});
	if(getElementValue("cotacao.idCotacao"))
		setElementValue("tpCalculoPreco", "C");
	return true;
}

var idDensidadePadrao;
function densidade_cb(data, error) {
	densidade_idDensidade_cb(data);
	if(data != undefined) {
		for(var i = 0; i < data.length; i++) {
			if(data[i].tpDensidade.description == 'A') {
				idDensidadePadrao = data[i].idDensidade;
				setElementValue("densidade.idDensidade", idDensidadePadrao);
				comboboxChange({e:document.getElementById("densidade.idDensidade")});
				return;
			}
		}
	}
}

function setDensidadePadrao() {
	setElementValue("densidade.idDensidade", idDensidadePadrao);
	comboboxChange({e:getElement("densidade.idDensidade")});
}

function servicoOnChange(combo) {
	comboboxChange({e:combo});
	var tpModal = getElementValue("servico.tpModal");
	if(tpModal == 'A') {
		desabilitaAereo(false);
		if(getElementValue("clienteByIdClienteRemetente.idCliente") != "") {
			findAeroportoOrigem();
		}

		var idCliente = getElementValue("clienteByIdClienteDestinatario.idCliente");
		if(idCliente != "") {
			var idMunicipio = getElementValue("clienteByIdClienteDestinatario.endereco.idMunicipio");
			var nrCep = getElementValue("clienteByIdClienteDestinatario.endereco.nrCep");
			findAeroportoDestino(idMunicipio, nrCep, idCliente);
		}
	} else {
		desabilitaAereo(true);
	}
}

var idServicoPadrao;
function servico_cb(data) {
	if(data) {
		servico_idServico_cb(data.servicos);
		idServicoPadrao = data.idServicoPadrao;
		setServicoPadrao();
	}
}

function setServicoPadrao() {
	if(idServicoPadrao) {
		setElementValue("servico.idServico", idServicoPadrao);
		servicoOnChange(document.getElementById("servico.idServico"));
	}
}

function desabilitaAereo(dis) {
	setDisabled("aeroportoByIdAeroportoDestino.idAeroporto", dis);
	setDisabled("aeroportoByIdAeroportoOrigem.idAeroporto", dis);
	setDisabled("blColetaEmergencia", dis);
	setDisabled("blEntregaEmergencia", dis);
	setDisabled("produtoEspecifico.idProdutoEspecifico", dis);
	if(dis == true) {
		resetValue("aeroportoByIdAeroportoDestino.idAeroporto");
		resetValue("aeroportoByIdAeroportoOrigem.idAeroporto");
		resetValue("blColetaEmergencia");
		resetValue("blEntregaEmergencia");
		resetValue("produtoEspecifico.idProdutoEspecifico");
	}
}

function findFilialDestino_cb(dados) {
	setElementValue("filialDestino.idFilial", dados.idFilial);
	setElementValue("filialDestino.sgFilial", dados.sgFilial);
}

function findAeroportoOrigem_cb(data, error) {
	setAeroporto(data, "aeroportoByIdAeroportoOrigem");
}

function findAeroportoDestino_cb(data, error) {
	setAeroporto(data, "aeroportoByIdAeroportoDestino");
}

function setAeroporto(data, tipo) {
	if(data) {
		setElementValue(tipo + ".idAeroporto", data.idAeroporto);
		setElementValue(tipo + ".sgAeroporto", data.sgAeroporto);
		setElementValue(tipo + ".pessoa.nmPessoa", data.nmAeroporto);
	}
}

function resetAeroporto(tipo) {
	var tpModal = getElementValue("servico.tpModal");
	if(tpModal == "A") {
		if(tipo == "Remetente")
			resetValue("aeroportoByIdAeroportoOrigem.idAeroporto");
		else
			resetValue("aeroportoByIdAeroportoDestino.idAeroporto");
	}
}

function cadastrarCliente(tipo) {
	var data = openModalDialog("expedicao/cadastrarClientes.do?cmd=main&origem=exp", 750, 300);
	if(data) {
		disabledCamposAdicionais(true);
		cadastrarCliente_cb(data, tipo);
	}
}

function cadastrarCliente_cb(dados, tipo) {
	setElementValue(tipo + ".idCliente", dados.idCliente);
	setElementValue(tipo + ".pessoa.nrIdentificacao", dados.pessoa.nrIdentificacao);
	setElementValue(tipo + ".nrIdentificacao", dados.pessoa.nrIdentificacao);
	setElementValue(tipo + ".tpCliente", "E");
	setElementValue(tipo + ".pessoa.nmPessoa", dados.pessoa.nmPessoa);
	setElementValue(tipo + ".ie.id", dados.inscricaoEstadual.idInscricaoEstadual);
	if(tipo == "clienteByIdClienteDestinario") {
		limpaLocalEntregaDestinatario();
	}
	setEndereco(dados, tipo);
	setDisabled(tipo + "Button", true);
	notifyElementListeners({e:document.getElementById(tipo + ".idCliente")});
}

function remetenteChange() {
	objCliente = getElement("clienteByIdClienteRemetente.idCliente");
	setInscricaoEstadual("Remetente");
	setPedidoColeta();
	setCotacao();
	return clienteChange("Remetente");
}

function destinatarioChange() {
	setInscricaoEstadual("Destinatario");
	return clienteChange("Destinatario");
}

function redespachoChange() {
	setInscricaoEstadual("Redespacho");
	return clienteChange("Redespacho");
}

function consignatarioChange() {
	setInscricaoEstadual("Consignatario");
	return clienteChange("Consignatario");
}

function devedorFreteChange() {
	setInscricaoEstadual("ResponsavelFrete");
	return clienteChange("ResponsavelFrete");
}

function clienteChange(tipo) {
	var cliente = "clienteByIdCliente" + tipo;
	var nrIdentificacao = getElementValue(cliente + ".pessoa.nrIdentificacao");
	if(nrIdentificacao == getElementValue(cliente + ".nrIdentificacao")) {
		return true;
	}
	if(nrIdentificacao == "") {
		limpaDadosCliente(tipo);
	}
	if(tipo == "Remetente") {
		if(nrIdentificacao == "") {
			setDisabled("quantidadeNotasFiscais", true);
		}
	}
	return eval(cliente + "_pessoa_nrIdentificacaoOnChangeHandler();");
}

function limpaDadosCliente(tipo) {
	var cliente = "clienteByIdCliente" + tipo;
	setEndereco(undefined, cliente);
	resetValue(cliente + ".idCliente");
	resetValue(cliente + ".idInscricaoEstadual");
	setDisabled(cliente + ".idInscricaoEstadual", true);
	if(tipo == "Remetente") {
		disabledCamposAdicionais(true);
	} else if(tipo == "Destinatario") {
		limpaLocalEntregaDestinatario();
	}
}

function remetentePopup(data) {
	return clientePopup(data, "Remetente");
}

function destinatarioPopup(data) {
	return clientePopup(data, "Destinatario");
}

function redespachoPopup(data) {
	return clientePopup(data, "Redespacho");
}

function consignatarioPopup(data) {
	return clientePopup(data, "Consignatario");
}

function responsavelFretePopup(data) {
	return clientePopup(data, "ResponsavelFrete");
}

function clientePopup(data, tipo) {
	var cliente = "clienteByIdCliente" + tipo;
	if(data == undefined) {
		var nrIdentificacao = getElementValue(cliente + ".pessoa.nrIdentificacao");
		resetValue(cliente + ".idCliente");
		setElementValue(cliente + ".pessoa.nrIdentificacao", nrIdentificacao);
		setDisabled(cliente + "Button", false);
		setFocus(document.getElementById(cliente + "Button"), false);
		if(tipo == "Remetente") {
			disabledCamposAdicionais(true);
		}
	} else {
		setDisabled(cliente + "Button", true);
		findDadosCliente(data, tipo);
		if(tipo == "Remetente") {
			var objNotas = document.getElementById("quantidadeNotasFiscais");
			if(objNotas.disabled == true || objNotas.disabled == "true") {
				verificaRemetenteClienteEspecial();
			}
		} else if(tipo == "Destinatario") {
			limpaLocalEntregaDestinatario();
		}
	}
	resetValue(cliente + ".ie.id");
	setDisabled(cliente + ".idInscricaoEstadual", true);
	return true;
}

function remetente_cb(data, error) {
	if(error) {
		alert(error);
		return;
	}
	return setCliente(data, "Remetente");
}

function destinatario_cb(data, error) {
	if(error) {
		alert(error);
		return;
	}
	return setCliente(data, "Destinatario");
}

function redespacho_cb(data, error) {
	if(error) {
		alert(error);
		return;
	}
	return setCliente(data, "Redespacho");
}

function consignatario_cb(data, error) {
	if(error) {
		alert(error);
		return;
	}
	return setCliente(data, "Consignatario");
}

function responsavelFrete_cb(data, error) {
	if(error) {
		alert(error);
		return;
	}
	return setCliente(data, "ResponsavelFrete");
}

function setCliente(data, tipo) {
	var cliente = "clienteByIdCliente" + tipo;
	if(data == undefined || data.length == 0) {
		var nrIdentificacao = getElementValue(cliente + ".pessoa.nrIdentificacao");
		limpaDadosCliente(tipo);
		setElementValue(cliente + ".pessoa.nrIdentificacao", nrIdentificacao);
		setDisabled(cliente + "Button", false);
		setFocus(document.getElementById(cliente + "Button"), false);
		if(tipo == "Remetente") {
			disabledCamposAdicionais(true);
			nrRowCountInformacaoDoctoCliente = 0;
			setDisabled("quantidadeNotasFiscais", true);
		}
		setInscricaoEstadual(tipo);
		if(tipo == "Remetente" || tipo == "Destinatario") {
			resetAeroporto(tipo);
		}
	} else {
		setDisabled(cliente + "Button", true);
		if(data[0].inscricaoEstadual) {
			setInscricaoEstadual(tipo, data[0].inscricaoEstadual);
		}
		setEndereco(data[0], cliente);
		if(tipo == "Remetente") {
			setPedidoColeta(data[0].pedidoColeta);
			setCotacao(data[0].cotacao);
			setAeroporto(data[0].aeroportoOrigem, "aeroportoByIdAeroportoOrigem");
			nrRowCountInformacaoDoctoCliente = data[0].nrRowCountInformacaoDoctoCliente;
			var objNotas = document.getElementById("quantidadeNotasFiscais");
			if(objNotas.disabled == true || objNotas.disabled == "true") {
				verificaRemetenteClienteEspecial();
			}
			setDisabled("quantidadeNotasFiscais", false);
		} else if(tipo == "Destinatario") {
			setAeroporto(data[0].aeroportoDestino, "aeroportoByIdAeroportoDestino");
			limpaLocalEntregaDestinatario();
		}
	}
	return eval(cliente + "_pessoa_nrIdentificacao_exactMatch_cb(data);");
}

function setEndereco(dados, tipo) {
	var endereco = null;
	if(dados && dados.pessoa.endereco) {
		endereco = dados.pessoa.endereco;
	}
	if(dados && dados.endereco) {
		endereco = dados.endereco;
	}

	if(endereco != null) {
		setElementValue(tipo + ".endereco.nrCep", endereco.nrCep);
		setElementValue(tipo + ".endereco.nmMunicipio", endereco.nmMunicipio);
		setElementValue(tipo + ".endereco.idMunicipio", endereco.idMunicipio);
		setElementValue(tipo + ".endereco.idUnidadeFederativa", endereco.idUnidadeFederativa);
		setElementValue(tipo + ".endereco.siglaDescricaoUf", endereco.sgUnidadeFederativa);
		setElementValue(tipo + ".endereco.dsComplemento", endereco.dsComplemento);
		setElementValue(tipo + ".endereco.nrEndereco", endereco.nrEndereco);
		var dsEndereco = endereco.dsTipoLogradouro + " " + endereco.dsEndereco;
		setElementValue(tipo + ".endereco.dsEndereco", dsEndereco);
	} else {
		setElementValue(tipo + ".endereco.nrCep", "");
		setElementValue(tipo + ".endereco.nmMunicipio", "");
		setElementValue(tipo + ".endereco.idMunicipio", "");
		setElementValue(tipo + ".endereco.idUnidadeFederativa", "");
		setElementValue(tipo + ".endereco.siglaDescricaoUf", "");
		setElementValue(tipo + ".endereco.dsComplemento", "");
		setElementValue(tipo + ".endereco.nrEndereco", "");
		setElementValue(tipo + ".endereco.dsEndereco", "");
	}
}

function setInscricaoEstadual(tipo, data) {
	loadInscricaoEstadual(tipo, data);
	var cliente = "clienteByIdCliente" + tipo;
	if(data) {
		resetValue("clienteByIdCliente" + tipo + ".ie.id");
		if(data.length == 1) {
			if(tipo == "Destinatario") {
				setFocus(document.getElementById("pedidoColeta.idPedidoColeta"), false);
			} else if(tipo == "Redespacho"
			       || tipo == "Consignatario"
			       || tipo == "ResponsavelFrete") {
				setElementValue(cliente + ".nrInscricaoEstadual", data[0].inscricaoEstadual.nrInscricaoEstadual);
			}
			return true;
		} else if(data.length > 1) {
			var idIe = getElementValue(cliente + ".ie.id");
			if(idIe)
				setElementValue(cliente + ".idInscricaoEstadual", idIe);
			return true;
		}
	}
}

function loadInscricaoEstadual(tipo, data) {
	var cliente = "clienteByIdCliente" + tipo;
	if(data) {
		window[cliente + "_idInscricaoEstadual_cb"](data);
		if(data.length == 1) {
			setElementValue(cliente + ".idInscricaoEstadual", data[0].inscricaoEstadual.idInscricaoEstadual)
			setDisabled(cliente + ".idInscricaoEstadual", true);
		} else if(data.length > 1) {
			setDisabled(cliente + ".idInscricaoEstadual", false);
			setFocus(document.getElementById(cliente + ".idInscricaoEstadual"), true);
		}
		return true;
	}
	clearElement(cliente + ".idInscricaoEstadual");
	return false;
}

function setPedidoColeta(data) {
	if(data) {
		pedidoColeta_idPedidoColeta_cb(data);
	} else {
		clearElement("pedidoColeta.idPedidoColeta");
	}
}

function setCotacao(data) {
	if(data) {
		cotacao_idCotacao_cb(data);
	} else {
		clearElement("cotacao.idCotacao");
	}
}

var nrRowCountInformacaoDoctoCliente = 0;
function verificaRemetenteClienteEspecial() {
	if(nrRowCountInformacaoDoctoCliente != 0) {
		disabledCamposAdicionais(false);
	} else {
		disabledCamposAdicionais(true);
	}
	var blAdic = getElementValue("blServicosAdicionais");
	if(blAdic == true)
		setFocus(document.getElementById("servicosAdicionaisButton"), false);
	else
		setFocus(document.getElementById("calcularCTRCButton"), false);
}

function disabledCamposAdicionais(dis) {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("camposAdicionais", dis);
}

function disabledNotasFiscais(dis) {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("notasFiscais", dis);
}

function filial_cb(data) {
	if(data == undefined || data.length == 0)
		setDisabled("ctoCtoCooperada.nrCtoCooperada", true);
	else
		setDisabled("ctoCtoCooperada.nrCtoCooperada", false);
	return ctoCtoCooperada_filialByIdFilial_sgFilial_exactMatch_cb(data);
}

function filialPopup(data) {
	if(data == undefined)
		setDisabled("ctoCtoCooperada.nrCtoCooperada", true);
	else
		setDisabled("ctoCtoCooperada.nrCtoCooperada", false);
	return true;
}

function filialChange() {
	var sg = getElementValue("ctoCtoCooperada.filialByIdFilial.sgFilial");
	if(sg == "") {
		resetValue("ctoCtoCooperada.nrCtoCooperada");
		setDisabled("ctoCtoCooperada.nrCtoCooperada", true)
	}
	return ctoCtoCooperada_filialByIdFilial_sgFilialOnChangeHandler();
}

function empresaChange(combo) {
	comboboxChange({e:combo});
	if(getElementValue(combo) != "") {
		setDisabled("ctoCtoCooperada.filialByIdFilial.idFilial", false);
	} else {
		setDisabled("ctoCtoCooperada.filialByIdFilial.idFilial", true);
	}
	resetValue("ctoCtoCooperada.nrCtoCooperada");
	setDisabled("ctoCtoCooperada.nrCtoCooperada", true);	
}
