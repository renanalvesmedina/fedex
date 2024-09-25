<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterPropostasClienteFormalidadesAction">
	<adsm:form action="/vendas/manterPropostasCliente" height="370">
	
		<adsm:hidden property="simulacao.idSimulacao" />
		<adsm:hidden property="simulacao.dtTabelaVigenciaInicial" />
		<adsm:hidden property="tpSituacaoAprovacao.value" />
		<adsm:hidden property="pendencia.idPendencia" />
		<adsm:hidden property="cliente.idCliente" />
		<adsm:hidden property="divisaoCliente.idDivisaoCliente" />
		<adsm:hidden property="filial.idFilial" />

		<adsm:hidden property="municipioByIdMunicipioOrigem.idMunicipio" />
		<adsm:hidden property="municipioByIdMunicipioDestino.idMunicipio" />
		<adsm:hidden property="filialByIdFilialOrigem.idFilial" />
		<adsm:hidden property="filialByIdFilialDestino.idFilial" />
		<adsm:hidden property="zonaByIdZonaOrigem.idZona" />
		<adsm:hidden property="zonaByIdZonaDestino.idZona" />
		<adsm:hidden property="paisByIdPaisOrigem.idPais" />
		<adsm:hidden property="paisByIdPaisDestino.idPais" />
		<adsm:hidden property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio" />
		<adsm:hidden property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio" />
		<adsm:hidden property="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" />
		<adsm:hidden property="unidadeFederativaByIdUfDestino.idUnidadeFederativa" />
		<adsm:hidden property="aeroportoByIdAeroportoOrigem.idAeroporto" />
		<adsm:hidden property="aeroportoByIdAeroportoDestino.idAeroporto" />

		<!-- LMS-6168 -->
		<adsm:hidden property="blConfirmaEfetivarProposta" value="false" />

		<adsm:combobox
			property="tpPeriodicidadeFaturamento"
			labelWidth="25%"
			width="30%"
			domain="DM_PERIODICIDADE_FATURAMENTO"
			label="periodicidadeFaturamento"
			disabled="true"/>

		<adsm:textbox
			dataType="integer"
			property="nrDiasPrazoPagamento"
			label="prazoPagamento"
			minValue="0"
			size="3"
			labelWidth="20%"
			width="25%"
			unit="dias"
			disabled="true"/>

		<adsm:textbox
			dataType="JTDate"
			property="dtValidadeProposta"
			label="dataValidadeProposta"
			labelWidth="25%"
			width="30%"
			picker="true" />

		<adsm:textbox
			dataType="JTDate"
			property="dtVigenciaInicial"
			label="dataInicioVigencia"
			labelWidth="20%"
			width="25%"
			picker="true"
			onchange="return onChangeDtVigenciaInicial();"/>

		<adsm:textbox
			dataType="JTDate"
			property="dtAceiteCliente"
			label="dataAceitacaoCliente"
			labelWidth="25%"
			width="30%"
			picker="true" />

		<adsm:textbox
			dataType="JTDate"
			property="dtAprovacao"
			label="dataAprovacao"
			labelWidth="20%"
			width="25%"
			disabled="true"
			picker="false" />

		<adsm:textbox
			dataType="text"
			property="usuarioByIdUsuarioAprovou.dsDescricao"
			label="funcionarioAprovador"
			size="60"
			labelWidth="25%"
			width="75%"
			disabled="true" />

		<adsm:textbox
			dataType="JTDate"
			property="dtEmissaoTabela"
			label="dataEmissaoTabela"
			labelWidth="25%"
			width="30%"
			disabled="true"
			picker="false" />

		<adsm:textbox
			dataType="text"
			property="tpSituacaoAprovacao"
			labelWidth="20%"
			width="25%"
			label="situacaoAprovacao"
			disabled="true"/>
		
		<adsm:textbox
			dataType="text"
			property="usuarioByIdUsuarioEfetivou.dsDescricao"
			label="funcionarioEfetivador"
			size="60"
			labelWidth="25%"
			width="75%"
			disabled="true" />
		
		<adsm:textarea
			label="observacao"
			maxLength="500"
			property="obProposta"
			labelWidth="25%"
			width="75%"
			rows="5"
			columns="80" />

		<adsm:checkbox
			property="blEfetivada"
			labelWidth="25%"
			width="30%"
			label="efetivada"
			disabled="true"/>

		<adsm:textbox 
			dataType="JTDate" 
			property="dtEfetivacao" 
			label="dataEfetivacao" 
			labelWidth="25%" 
			width="30%" 
			disabled="true" 
			picker="false" />

		<adsm:textbox
			dataType="JTDateTimeZone"
			property="dhSolicitacao"
			label="dataSolicitacaoEfetivacao"
			labelWidth="25%"
			width="75%"
			picker="false" />

		<adsm:textbox
			dataType="JTDateTimeZone"
			property="dhReprovacao"
			label="dataUltimaReprovacao"
			labelWidth="25%"
			width="75%"
			picker="false" />
 			
 		<adsm:textarea
			label="motivoUltReprovacao"
			maxLength="500"
			property="dsMotivo"
			labelWidth="25%"
			width="75%"
			rows="5"
			disabled="true" 
			columns="80"/>	

		<adsm:buttonBar lines="2">
			<adsm:button
				id="btnHistoricoAprovacao"
				caption="historicoAprovacao"
				disabled="true"
				onclick="return showHistoricoAprovacao();" />
			<adsm:button
				id="btnSalvar"
				caption="salvar"
				onclick="return onClickSalvar();"/>
			<adsm:button
				id="btnAprovacao"
				caption="aprovacao"
				onclick="return aprovacaoProposta();" />
			<adsm:reportViewerButton
				id="btnImprimirProposta"
				caption="imprimirProposta" service="lms.vendas.emitirTabelaPropostaAction" />
				<%-- reportName="vendas/imprimirTabelaProposta.jasper" --%>
 			<adsm:button
				id="btnSolicitarEfetivacao"
				caption="solicitarEfetivacao"
				onclick="return onClickSolicitarEfetivacao();" />
			<adsm:button
				id="btnReprovarEfetivacao"
				caption="reprovarEfetivacao"
				onclick="onClickReprovarEfetivacao();" /> 
			<adsm:button
				id="btnEfetivarProposta"
				caption="efetivarProposta"
				onclick="return onClickEfetivarProposta();" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
var _disableAll = false;

function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		findDadosSessao();
		var frame = parent.document.frames["cad_iframe"];		
		var data = buildFormBeanFromForm(frame.document.forms[0]);
		onDataLoad_cb(data);
		disableWorkflow();
	}
}

function findDadosSessao() {
	var service = "lms.vendas.manterPropostasClienteFormalidadesAction.findDadosSessao";
	var sdo = createServiceDataObject(service, "findDadosSessao");
	xmit({serviceDataObjects:[sdo]});
}

function findDadosSessao_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	var tabCad = getTabGroup(document).getTab("cad");
	tabCad.tabOwnerFrame.setSaveMode("complete");
	ajustaDadosSessao(data);
}


function aprovacaoProposta() {
	var tabGroup = getTabGroup(document);
	var tabRota = tabGroup.getTab("cad");
	var formsRota = tabRota.getDocument().forms;

	if (validateTabScript(formsRota) && validateTabScript(document.forms)) {
	
	var service = "lms.vendas.manterPropostasClienteFormalidadesAction.aprovacaoProposta";
	var sdo = createServiceDataObject(service, "aprovacaoProposta");
	xmit({serviceDataObjects:[sdo]});
	}
}

function aprovacaoProposta_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}

	setTpSituacaoAprovacao(data);
	setUsuario(data);
	changeButtons();

	setElementValue("pendencia.idPendencia", data.pendencia.idPendencia);
	setElementValue("dtAprovacao", setFormat("dtAprovacao", data.dtAprovacao));
}

function ajustaDadosSessao(data) {
	if (data.tpPeriodicidadeFaturamento != undefined) {
		setElementValue("tpPeriodicidadeFaturamento", data.tpPeriodicidadeFaturamento);
	} else {
		setElementValue("tpPeriodicidadeFaturamento", "");
	}
	setElementValue("filial.idFilial", getNestedBeanPropertyValue(data, "filial.idFilial"));
	setElementValue("nrDiasPrazoPagamento", getNestedBeanPropertyValue(data, "nrDiasPrazoPagamento"));
	setElementValue("dtValidadeProposta", setFormat("dtValidadeProposta", getNestedBeanPropertyValue(data, "dtValidadeProposta")));
	setElementValue("dtAceiteCliente", setFormat("dtAceiteCliente", getNestedBeanPropertyValue(data, "dtAceiteCliente")));
	setElementValue("dtVigenciaInicial", setFormat("dtVigenciaInicial", getNestedBeanPropertyValue(data, "dtTabelaVigenciaInicial")));
	setElementValue("simulacao.dtTabelaVigenciaInicial", getNestedBeanPropertyValue(data, "dtTabelaVigenciaInicial"));
	setElementValue("simulacao.idSimulacao", getNestedBeanPropertyValue(data, "simulacao.idSimulacao"));
	setElementValue("dtAprovacao", setFormat("dtAprovacao", getNestedBeanPropertyValue(data, "dtAprovacao")));
	setElementValue("dtEmissaoTabela", setFormat("dtEmissaoTabela", getNestedBeanPropertyValue(data, "dtEmissaoTabela")));
	setElementValue("pendencia.idPendencia", getNestedBeanPropertyValue(data, "pendencia.idPendencia"));
	setElementValue("obProposta", getNestedBeanPropertyValue(data, "obProposta"));
	setElementValue("dtEfetivacao", setFormat("dtEfetivacao", getNestedBeanPropertyValue(data, "dtEfetivacao")));
	setElementValue("dhSolicitacao", setFormat("dhSolicitacao", getNestedBeanPropertyValue(data, "dhSolicitacao")));
	setElementValue("dhReprovacao", setFormat("dhReprovacao", getNestedBeanPropertyValue(data, "dhReprovacao")));
	setElementValue("dsMotivo", getNestedBeanPropertyValue(data, "dsMotivo"));

	setUsuario(data);
	setTpSituacaoAprovacao(data);
	setBlEfetivada(data);

	if (data.blEfetivada == "S" || ((data.filialSessao.idFilial != data.filial.idFilial) && data.isFilialMatriz == "N")) {
		_disableAll = true;
		disableAll();
	} else {
		_disableAll = false;
		enableFields();
	}

	changeButtons();
}

function changeButtons() {
	var url = new URL(parent.location.href);
	if (url.parameters != undefined 
			&& (url.parameters.idProcessoWorkflow == undefined 
				|| url.parameters.idProcessoWorkflow == "")) {
		var tpSituacaoAprovacao = getElementValue("tpSituacaoAprovacao.value");
		var tabGroup = getTabGroup(document);
		
		if (tpSituacaoAprovacao == "A") {
			setDisabled("btnHistoricoAprovacao", false);
			setDisabled("btnImprimirProposta", false);
			setDisabled("btnSolicitarEfetivacao", false);
			if (_disableAll) {
				setDisabled("btnEfetivarProposta", true);
				setDisabled("btnSalvar", true);
			} else {
				setDisabled("btnEfetivarProposta", false);
			}
			setDisabled("btnAprovacao", true);
			setDisabled("btnReprovarEfetivacao", true);
			setDisabled("btnEfetivarProposta", true);
		} else if (tpSituacaoAprovacao == "C") {
			setDisabled("btnHistoricoAprovacao", false);
			if (_disableAll) {
				setDisabled("btnAprovacao", true);
				setDisabled("btnSalvar", true);
			} else {
				setDisabled("btnAprovacao", false);
			}
			setDisabled("btnSolicitarEfetivacao", true);
			setDisabled("btnReprovarEfetivacao", true);
			setDisabled("btnImprimirProposta", true);
			setDisabled("btnEfetivarProposta", true);
		} else if (tpSituacaoAprovacao == "E" || tpSituacaoAprovacao == "R") {
			setDisabled("btnHistoricoAprovacao", false);
			setDisabled("btnImprimirProposta", (tpSituacaoAprovacao == "R"));
			setDisabled("btnSolicitarEfetivacao", true);
			setDisabled("btnReprovarEfetivacao", true);
			setDisabled("btnAprovacao", true);
			setDisabled("btnEfetivarProposta", true);
		} else if (tpSituacaoAprovacao == "" || tpSituacaoAprovacao == "I") {
			if (_disableAll) {
				setDisabled("btnAprovacao", true);
				setDisabled("btnSalvar", true);
			} else {
				setDisabled("btnAprovacao", false);
			}
			setDisabled("btnAprovacao", false);
			
			setDisabled("btnHistoricoAprovacao", true);
			setDisabled("btnImprimirProposta", true);
			setDisabled("btnSolicitarEfetivacao", true);
			setDisabled("btnReprovarEfetivacao", true);
			setDisabled("btnEfetivarProposta", true);
		} else if (tpSituacaoAprovacao == "M") {
			setDisabled("btnHistoricoAprovacao", false);
			setDisabled("btnReprovarEfetivacao", false);
			setDisabled("btnImprimirProposta", false);
			setDisabled("btnEfetivarProposta", false);
			
			setDisabled("btnSolicitarEfetivacao", true);
			setDisabled("btnAprovacao", true);
			
			//LMS-6191
			tabGroup.setDisabledTab("historico", false);
		} else if(tpSituacaoAprovacao == "H"){
			setDisabled("btnHistoricoAprovacao", false);
			setDisabled("btnImprimirProposta", false);
			setDisabled("btnSolicitarEfetivacao", false);
			setDisabled("btnAprovacao", false);
			
			setDisabled("btnReprovarEfetivacao", true);
			setDisabled("btnEfetivarProposta", true);
			
			//LMS-6191
			tabGroup.setDisabledTab("historico", false);
		} else if(tpSituacaoAprovacao == "F"){
			setDisabled("btnHistoricoAprovacao", false);
			setDisabled("btnImprimirProposta", false);
			
			setDisabled("btnSolicitarEfetivacao", true);
			setDisabled("btnReprovarEfetivacao", true);
			setDisabled("btnAprovacao", true);
			setDisabled("btnEfetivarProposta", true);
			
			//LMS-6191
			tabGroup.setDisabledTab("historico", false);
		}
		
		if(_disableAll == true){
			setDisabled("btnHistoricoAprovacao", false);
			setDisabled("btnImprimirProposta", false);
			setDisabled("btnSolicitarEfetivacao", true);
			setDisabled("btnReprovarEfetivacao", true);
			setDisabled("btnAprovacao", true);
			setDisabled("btnEfetivarProposta", true);
		} else {
			setDisabled("btnSalvar", false);
		}
	}
}

function onClickSalvar() {
	var tabGroup = getTabGroup(document);
	var tabRota = tabGroup.getTab("cad");
	var formsRota = tabRota.getDocument().forms;

	if (validateTabScript(formsRota) && validateTabScript(document.forms)) {
		var data = buildFormBeanFromForm(formsRota[0]);
		merge(data, buildFormBeanFromForm(document.forms[0]));

		var service = "lms.vendas.manterPropostasClienteProcAction.store";
		var sdo = createServiceDataObject(service, "afterStore", data);
		xmit({serviceDataObjects:[sdo]});
	}
}

function afterStore_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	store_cb(data, error);
	setTpSituacaoAprovacao(data);
	setUsuario(data);
	setElementValue("dtEfetivacao", setFormat("dtEfetivacao", getNestedBeanPropertyValue(data, "dtEfetivacao")));
	setElementValue("dtAprovacao", setFormat("dtAprovacao", data.dtAprovacao));
	
	changeButtons();
	setFocus("btnAprovacao", false);
}

function onChangeDtVigenciaInicial(){
	setElementValue("simulacao.dtTabelaVigenciaInicial", getElementValue("dtVigenciaInicial"));
}

function onClickSolicitarEfetivacao(){
	var id = getElementValue("simulacao.idSimulacao");
    
	var sdo = createServiceDataObject("lms.vendas.manterPropostasClienteFormalidadesAction.solicitarEfetivacaoProposta", "solicitarEfetivacao", {idSimulacao:id});
    xmit({serviceDataObjects:[sdo]});
}

function solicitarEfetivacao_cb(data,error){
	if (error != undefined) {
		alert(error);
		return;
	}
	
	setTpSituacaoAprovacao(data);
	setElementValue("dhSolicitacao", setFormat("dhSolicitacao", getNestedBeanPropertyValue(data, "dhSolicitacao")));

	changeButtons();

	showSuccessMessage();
}

function onClickEfetivarProposta() {
	var tabGroup = getTabGroup(document);
	var tabRota = tabGroup.getTab("cad");
	var idDivisaoCliente = tabRota.getDocument().getElementById("divisaoCliente.idDivisaoCliente").value;

	var data = {
		idSimulacao : getElementValue("simulacao.idSimulacao"),
		idDivisaoCliente : idDivisaoCliente,
		dtInicioVigencia : getElementValue("simulacao.dtTabelaVigenciaInicial"),
		// LMS-6168
		blConfirmaEfetivarProposta : getElementValue("blConfirmaEfetivarProposta")
	};

	var service = "lms.vendas.manterPropostasClienteFormalidadesAction.storeEfetivacaoProposta";
	var sdo = createServiceDataObject(service, "efetivarProposta", data);
	xmit({serviceDataObjects:[sdo]});
}


function efetivarProposta_cb(data, error) {
	// LMS-6168 - reseta confirmação para LMS-01233
	setElementValue("blConfirmaEfetivarProposta", false);

	if (error != undefined) {
		// LMS-6168 - verifica situação para confirmação LMS-01233
		if (error.startsWith('LMS-01233')) {
			if (confirm(error)) {
				// repete processo após confirmação do usuário
				setElementValue("blConfirmaEfetivarProposta", true);
				onClickEfetivarProposta();
			}
			return;
		}
		
		alert(error);
		return;
	}
	store_cb(data, error);

	findDadosSessao();
	showSuccessMessage();
	
	_disableAll = true;
	disableAll();
	// desabilita os campos da aba de detalhamento
	var telaCad = getTabGroup(document).getTab("cad").tabOwnerFrame;
	telaCad._disableAll = true;
	telaCad.disableAll();
}

function disableAll() {
	var url = new URL(parent.location.href);
	if (url.parameters != undefined 
			&& (url.parameters.idProcessoWorkflow == undefined 
				|| url.parameters.idProcessoWorkflow == "")) {
		setDisabled(document, true);
		setDisabled("btnAprovacao", false);
		setDisabled("btnImprimirProposta", false);
	}
}

function enableFields() {
	var url = new URL(parent.location.href);
	if (url.parameters != undefined 
			&& (url.parameters.idProcessoWorkflow == undefined 
				|| url.parameters.idProcessoWorkflow == "")) {
		setDisabled(document, false);

		setDisabled("tpPeriodicidadeFaturamento", true);
		setDisabled("nrDiasPrazoPagamento", true);
		setDisabled("usuarioByIdUsuarioAprovou.dsDescricao", true);
		setDisabled("dtAprovacao", true);
		setDisabled("dtEmissaoTabela", true);
		setDisabled("tpSituacaoAprovacao", true);
		setDisabled("blEfetivada", true);
		setDisabled("dtEfetivacao", true);
		setDisabled("dhSolicitacao", true);
		setDisabled("dhReprovacao", true);
		setDisabled("dsMotivo", true);
	}
}

function setUsuario(data) {
	if (data.usuarioByIdUsuarioAprovou != undefined) {
		var dsDescricao = "";
		var first = true;
		if (data.usuarioByIdUsuarioAprovou.nrMatricula != "" && 
			data.usuarioByIdUsuarioAprovou.nrMatricula != undefined) {
			first = false;
			dsDescricao += data.usuarioByIdUsuarioAprovou.nrMatricula;
		}
		if (data.usuarioByIdUsuarioAprovou.nmUsuario != "" &&
			data.usuarioByIdUsuarioAprovou.nmUsuario != undefined) {
			if (first) {
				dsDescricao += " ";
			}
			dsDescricao += " - " + data.usuarioByIdUsuarioAprovou.nmUsuario;
		}
		setElementValue("usuarioByIdUsuarioAprovou.dsDescricao", dsDescricao);
	} else {
		setElementValue("usuarioByIdUsuarioAprovou.dsDescricao", "");
	}
	
	if (data.usuarioByIdUsuarioEfetivou != undefined) {
		var dsDescricao = "";
		var first = true;
		if (data.usuarioByIdUsuarioEfetivou.nrMatricula != "" && 
			data.usuarioByIdUsuarioEfetivou.nrMatricula != undefined) {
			first = false;
			dsDescricao += data.usuarioByIdUsuarioEfetivou.nrMatricula;
		}
		if (data.usuarioByIdUsuarioEfetivou.nmUsuario != "" &&
			data.usuarioByIdUsuarioEfetivou.nmUsuario != undefined) {
			if (first) {
				dsDescricao += " ";
			}
			dsDescricao += " - " + data.usuarioByIdUsuarioEfetivou.nmUsuario;
		}
		setElementValue("usuarioByIdUsuarioEfetivou.dsDescricao", dsDescricao);
	} else {
		setElementValue("usuarioByIdUsuarioEfetivou.dsDescricao", "");
	}
}



function setTpSituacaoAprovacao(data) {
	if (data.tpSituacaoAprovacao != undefined) {
		setElementValue("tpSituacaoAprovacao", data.tpSituacaoAprovacao.description);
		setElementValue("tpSituacaoAprovacao.value", data.tpSituacaoAprovacao.value);
		
		var tabCad = getTabGroup(document).getTab("cad");
		setFormPropertyValue(tabCad.tabOwnerFrame,"tpSituacaoAprovacao.value",data.tpSituacaoAprovacao.value);
	} else {
		setElementValue("tpSituacaoAprovacao", "");
		setElementValue("tpSituacaoAprovacao.value", "");
	}
}

function setBlEfetivada(data) {
	if (data.blEfetivada == "S") {
		setElementValue("blEfetivada", true);
	} else {
		setElementValue("blEfetivada", false);
	}
	var tabCad = getTabGroup(document).getTab("cad");
	setFormPropertyValue(tabCad.tabOwnerFrame, "blEfetivada", data.blEfetivada);	
}

function showHistoricoAprovacao() {
	var idPendencia = getElementValue("pendencia.idPendencia");
	if (idPendencia == "") {
		idPendencia = 0;
	}
	showModalDialog('workflow/listarHistoricoPendencia.do?cmd=list&pendencia.idPendencia='+idPendencia,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');
}

function disableWorkflow() {
	var url = new URL(parent.location.href);
	if (url.parameters != undefined 
			&& url.parameters.idProcessoWorkflow != undefined 
			&& url.parameters.idProcessoWorkflow != '') {
		setDisabled(document, true);
		setDisabled("btnImprimirProposta", false);
	}
}
function onClickReprovarEfetivacao() {
	var idSimulacao = getElementValue("simulacao.idSimulacao");
	var url = '/vendas/manterPropostasCliente.do?cmd=reprovar&idSimulacao=' + idSimulacao;
	var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:600px;dialogHeight:200px;';				
	showModalDialog(url, window, wProperties);
	findDadosSessao();
	
	showSuccessMessage();
}

</script>