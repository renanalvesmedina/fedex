<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterPropostasClienteFormalidadesAction">
	<adsm:form action="/vendas/manterPropostasCliente" height="370">
	
		<adsm:hidden property="cliente.idCliente" />
		<adsm:hidden property="divisaoCliente.idDivisaoCliente" />
		<adsm:hidden property="servico.idServico" />
		<adsm:hidden property="tabelaPreco.idTabelaPreco" />
		<adsm:hidden property="simulacao.idSimulacao" />
		<adsm:hidden property="parametroCliente.idParametroCliente" /> 
		<adsm:hidden property="simulacao.dtTabelaVigenciaInicial" />
		<adsm:hidden property="tpSituacaoAprovacao.value" />
		
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
		
		<adsm:combobox 
			property="tpPeriodicidadeFaturamento" 
			labelWidth="30%" 
			width="20%" 
			domain="DM_PERIODICIDADE_FATURAMENTO"
			label="periodicidadeFaturamento"/>
		
		<adsm:textbox 
			dataType="integer" 
			property="nrDiasPrazoPagamento" 
			label="prazoPagamento" 
			size="3" 
			labelWidth="30%" 
			width="20%" 
			unit="dias" />

		<adsm:textbox 
			dataType="JTDate" 
			property="dtValidadeProposta" 
			label="dataValidadeProposta" 
			labelWidth="30%" 
			width="20%" 
			picker="true" />
			
		<adsm:textbox 
			dataType="JTDate" 
			property="dtVigenciaInicial" 
			label="dataInicioVigencia" 
			labelWidth="30%" 
			width="20%" 
			picker="true" 
			onchange="return onChangeDtVigenciaInicial();"/>

		<adsm:textbox 
			dataType="JTDate" 
			property="dtAceiteCliente" 
			label="dataAceitacaoCliente" 
			labelWidth="30%" 
			width="20%" 
			picker="true" />
			
		<adsm:textbox 
			dataType="JTDate" 
			property="dtAprovacao" 
			label="dataAprovacao" 
			labelWidth="30%" 
			width="20%" 
			disabled="true" 
			picker="false" />

		<adsm:textbox 
			dataType="text" 
			property="usuarioByIdUsuarioAprovou.dsDescricao" 
			label="funcionarioAprovador" 
			size="60"
			labelWidth="30%" 
			width="70%" 
			disabled="true" />

		<adsm:textbox 
			dataType="JTDate" 
			property="dtEmissaoTabela" 
			label="dataEmissaoTabela" 
			labelWidth="30%" 
			width="20%" 
			disabled="true" 
			picker="false" />
		
		<adsm:textbox 
			dataType="text"
			property="tpSituacaoAprovacao" 
			labelWidth="30%" 
			width="20%" 
			label="situacaoAprovacao" 
			disabled="true"/>

    	<adsm:buttonBar>	
			<adsm:button 
				id="btnHistoricoAprovacao"
				caption="historicoAprovacao" 
				onclick="showModalDialog('vendas/consultarHistoricoAprovacao.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:400px;');"/>
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
				caption="imprimirProposta" /> <%-- reportName="vendas/imprimirTabelaProposta.jasper" --%>
			<adsm:button 
				id="btnEfetivarProposta"
				caption="efetivarProposta" 
				onclick="return onClickEfetivarProposta();" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
top._consultaFormalidades = true;

var _disableAll = false;

function initWindow(eventObj) {
	if (eventObj.name = "tab_click") {
		if (top._consultaFormalidades) {
			top._consultaFormalidades = false;
			findDadosSessao();
		} else {
			cleanButtonScript(document);
			atualizaStatusAprovacao();
		}
		
		var frame = parent.document.frames["cad_iframe"];
		var dados = frame.getDadosFromRota();
		ajustaDados(dados);
		
		if(top._consulta) {			
			top._consulta = false;
			
			var param = parent.document.frames["param_iframe"];
			param.populaParametros(dados.parametroCliente.idParametroCliente);
		}
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
	ajustaDadosSessao(data);
}

function atualizaStatusAprovacao() {
	var service = "lms.vendas.manterPropostasClienteFormalidadesAction.findDadosSessao";
	var sdo = createServiceDataObject(service, "atualizaStatusAprovacao");
	xmit({serviceDataObjects:[sdo]});
}

function atualizaStatusAprovacao_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	
	setUsuario(data);
	setElementValue("dtAprovacao", setFormat("dtAprovacao", data.dtAprovacao));
	setElementValue("dtEmissaoTabela", setFormat("dtEmissaoTabela", data.dtEmissaoTabela));
	setTpSituacaoAprovacao(data);
	changeButtons();
}

function aprovacaoProposta() {
	var service = "lms.vendas.manterPropostasClienteFormalidadesAction.aprovacaoProposta";
	var sdo = createServiceDataObject(service, "aprovacaoProposta");
	xmit({serviceDataObjects:[sdo]});
}

function aprovacaoProposta_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setDisabled("btnAprovacao", true);
	setDisabled("btnHistoricoAprovacao", false);
	
	setTpSituacaoAprovacao(data);
	setUsuario(data);
	
	if (data.tpSituacaoAprovacao != undefined) {
		if (data.tpSituacaoAprovacao.value == "A") {
			setDisabled("btnEfetivarProposta", false);
		}
	}
	
	setElementValue("dtAprovacao", setFormat("dtAprovacao", data.dtAprovacao));
}

function ajustaDadosSessao(data) {
	if (data.tpPeriodicidadeFaturamento != undefined) {
		setElementValue("tpPeriodicidadeFaturamento", data.tpPeriodicidadeFaturamento);
	} else {
		setElementValue("tpPeriodicidadeFaturamento", "");
	}
	setElementValue("nrDiasPrazoPagamento", data.nrDiasPrazoPagamento);
	setElementValue("dtValidadeProposta", setFormat("dtValidadeProposta", data.dtValidadeProposta));
	setElementValue("dtAceiteCliente", setFormat("dtAceiteCliente", data.dtAceiteCliente));
	setElementValue("dtVigenciaInicial", setFormat("dtVigenciaInicial", data.dtTabelaVigenciaInicial));
	setElementValue("simulacao.dtTabelaVigenciaInicial", data.dtTabelaVigenciaInicial);
	
	setUsuario(data);
	
	setElementValue("dtAprovacao", setFormat("dtAprovacao", data.dtAprovacao));
	setElementValue("dtEmissaoTabela", setFormat("dtEmissaoTabela", data.dtEmissaoTabela));
	setTpSituacaoAprovacao(data);
	changeButtons();
}

function changeButtons() {
	var tpSituacaoAprovacao = getElementValue("tpSituacaoAprovacao.value");
	if (tpSituacaoAprovacao == "A") {
		setDisabled("btnHistoricoAprovacao", false);
		setDisabled("btnImprimirProposta", false);
		if (_disableAll) {
			setDisabled("btnEfetivarProposta", true);
		} else {
			setDisabled("btnEfetivarProposta", false);
		}
		setDisabled("btnAprovacao", true);
	} else if (tpSituacaoAprovacao == "C") {
		setDisabled("btnHistoricoAprovacao", false);
		if (_disableAll) {
			setDisabled("btnAprovacao", true);
		} else {
			setDisabled("btnAprovacao", false);
		}
		setDisabled("btnImprimirProposta", false);
		setDisabled("btnEfetivarProposta", true);
	} else if (tpSituacaoAprovacao == "E" || tpSituacaoAprovacao == "R") {
		setDisabled("btnHistoricoAprovacao", false);
		setDisabled("btnImprimirProposta", false);		
		setDisabled("btnAprovacao", true);
		setDisabled("btnEfetivarProposta", true);
	} else if (tpSituacaoAprovacao == "") {
		if (_disableAll) {
			setDisabled("btnAprovacao", true);
		} else {
			setDisabled("btnAprovacao", false);
		}
		setDisabled("btnImprimirProposta", false);		
		setDisabled("btnHistoricoAprovacao", true);
		setDisabled("btnEfetivarProposta", true);
	}
}

function onClickSalvar() {
	var tabGroup = getTabGroup(document);
	var tabRota = tabGroup.getTab("cad");
	var tabParametro = tabGroup.getTab("param");
	var formsRota = tabRota.getDocument().forms;
	var formsParametro = tabParametro.getDocument().forms;
	//oldAlert(formsParametro[0].elements["cliente.pessoa.nrIdentificacao"].value);	
	
	if (validateTabScript(formsRota) && validateTabScript(formsParametro)) {
		var frame = parent.document.frames["param_iframe"];
		var data = buildFormBeanFromForm(formsParametro[0]);
		merge(data, buildFormBeanFromForm(document.forms[0]));
		
		var service = "lms.vendas.manterPropostasClienteAction.store";
		var sdo = createServiceDataObject(service, "afterStore", data);
		xmit({serviceDataObjects:[sdo]});
	}
}

function afterStore_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	
	setTpSituacaoAprovacao(data);
	setUsuario(data);
	setElementValue("dtAprovacao", setFormat("dtAprovacao", data.dtAprovacao));
	changeButtons();
	showSuccessMessage();
}

function onChangeDtVigenciaInicial(){
	setElementValue("simulacao.dtTabelaVigenciaInicial", getElementValue("dtVigenciaInicial"));
}

function onClickEfetivarProposta() {
	var data = {
		idSimulacao : getElementValue("simulacao.idSimulacao"),
		idParametroCliente : getElementValue("parametroCliente.idParametroCliente"),
		idDivisaoCliente : getElementValue("divisaoCliente.idDivisaoCliente"),
		dtInicioVigencia : getElementValue("simulacao.dtTabelaVigenciaInicial")
	}

	var service = "lms.vendas.manterPropostasClienteFormalidadesAction.storeEfetivacaoProposta";
	var sdo = createServiceDataObject(service, "efetivarProposta", data);
	xmit({serviceDataObjects:[sdo]});
}

function efetivarProposta_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	showSuccessMessage();
}

function ajustaDados(data) {
	setElementValue("cliente.idCliente", data.cliente.idCliente);
	setElementValue("divisaoCliente.idDivisaoCliente", data.divisaoCliente.idDivisaoCliente);
	setElementValue("servico.idServico", data.servico.idServico);
	setElementValue("tabelaPreco.idTabelaPreco", data.tabelaPreco.idTabelaPreco);
	setElementValue("simulacao.idSimulacao", data.simulacao.idSimulacao);
	setElementValue("parametroCliente.idParametroCliente", data.parametroCliente.idParametroCliente);
	setElementValue("simulacao.dtTabelaVigenciaInicial", data.simulacao.dtTabelaVigenciaInicial);
	
	setElementValue("municipioByIdMunicipioOrigem.idMunicipio", data.municipioByIdMunicipioOrigem.idMunicipio);
	setElementValue("municipioByIdMunicipioDestino.idMunicipio", data.municipioByIdMunicipioDestino.idMunicipio);
	setElementValue("filialByIdFilialOrigem.idFilial", data.filialByIdFilialOrigem.idFilial);
	setElementValue("filialByIdFilialDestino.idFilial", data.filialByIdFilialDestino.idFilial);
	setElementValue("zonaByIdZonaOrigem.idZona", data.zonaByIdZonaOrigem.idZona);
	setElementValue("zonaByIdZonaDestino.idZona", data.zonaByIdZonaDestino.idZona);
	setElementValue("paisByIdPaisOrigem.idPais", data.paisByIdPaisOrigem.idPais);
	setElementValue("paisByIdPaisDestino.idPais", data.paisByIdPaisDestino.idPais);
	setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio", data.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio);
	setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio", data.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio);
	setElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa", data.unidadeFederativaByIdUfOrigem.idUnidadeFederativa);
	setElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa", data.unidadeFederativaByIdUfDestino.idUnidadeFederativa);
	setElementValue("aeroportoByIdAeroportoOrigem.idAeroporto", data.aeroportoByIdAeroportoOrigem.idAeroporto);
	setElementValue("aeroportoByIdAeroportoDestino.idAeroporto", data.aeroportoByIdAeroportoDestino.idAeroporto);
	
	_disableAll = data.disableAll;
	
	if (data.disableAll) {
		disableAll();
		changeButtons();
	} else {
		enableFields();
		changeButtons();
	}
}

function disableAll() {
	setDisabled(document, true);
}

function enableFields() {
	setDisabled(document, false);
	setDisabled("usuarioByIdUsuarioAprovou.dsDescricao", true);
	setDisabled("dtAprovacao", true);
	setDisabled("dtEmissaoTabela", true);
	setDisabled("tpSituacaoAprovacao", true);
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
			dsDescricao += data.usuarioByIdUsuarioAprovou.nmUsuario;
		}
		setElementValue("usuarioByIdUsuarioAprovou.dsDescricao", dsDescricao);
	} else {
		setElementValue("usuarioByIdUsuarioAprovou.dsDescricao", "");
	}
}

function setTpSituacaoAprovacao(data) {
	if (data.tpSituacaoAprovacao != undefined) {
		setElementValue("tpSituacaoAprovacao", data.tpSituacaoAprovacao.description);
		setElementValue("tpSituacaoAprovacao.value", data.tpSituacaoAprovacao.value);
	} else {
		setElementValue("tpSituacaoAprovacao", "");
		setElementValue("tpSituacaoAprovacao.value", "");
	}
}

//-->
</script>