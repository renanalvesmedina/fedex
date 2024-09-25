<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterPropostasClienteAction">
	<adsm:form
			action="/vendas/manterPropostasClienteParam"
			height="370">

		<adsm:i18nLabels>
			<adsm:include key="LMS-01040"/>
			<adsm:include key="LMS-01045"/>
			<adsm:include key="LMS-01050"/>
			<adsm:include key="LMS-01052"/>
			<adsm:include key="LMS-01060"/>
			<adsm:include key="LMS-01070"/>
			<adsm:include key="LMS-01155"/>
			<adsm:include key="pagaPesoExcedente"/>
		</adsm:i18nLabels>

		<adsm:hidden property="simulacao.idSimulacao" />
		<adsm:hidden property="simulacao.tpGeracaoProposta" />
		<adsm:hidden property="parametroCliente.idParametroCliente" />
		<adsm:hidden property="cliente.idCliente" />
		<adsm:hidden property="divisaoCliente.idDivisaoCliente" />
		<adsm:hidden property="servico.idServico" />
		<adsm:hidden property="tabelaPreco.idTabelaPreco" />
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
		<adsm:hidden property="grupoRegiaoOrigem.idGrupoRegiao" />
		<adsm:hidden property="grupoRegiaoDestino.idGrupoRegiao" />
		<adsm:hidden property="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" />
		<adsm:hidden property="unidadeFederativaByIdUfDestino.idUnidadeFederativa" />
		<adsm:hidden property="aeroportoByIdAeroportoOrigem.idAeroporto"/>
		<adsm:hidden property="aeroportoByIdAeroportoDestino.idAeroporto"/>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco" serializable="false"/>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" serializable="false"/>
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>

		<%--------------------%>
		<%-- CLIENTE LOOKUP --%>
		<%--------------------%>
		<adsm:complement
				label="cliente"
				labelWidth="14%"
				width="45%"
				required="true">
			<adsm:textbox
					dataType="text"
					disabled="true"
					property="cliente.pessoa.nrIdentificacao"
					serializable="false"
					size="20"
					maxLength="11" />
			<adsm:textbox
					dataType="text"
					disabled="true"
					property="cliente.pessoa.nmPessoa"
					serializable="false"
					size="34" />
		</adsm:complement>

		<%-------------------%>
		<%-- DIVISAO COMBO --%>
		<%-------------------%>
		<adsm:textbox
				dataType="text"
				disabled="true"
				label="divisao"
				labelWidth="100"
				property="divisaoCliente.dsDivisaoCliente"
				serializable="false"
				width="18%" />

		<%-------------------%>
		<%-- TABELA LOOKUP --%>
		<%-------------------%>
		<adsm:complement
				label="tabela"
				labelWidth="14%"
				width="45%"
				required="true">
			<adsm:textbox
					dataType="text"
					disabled="true"
					property="tabelaPreco.tabelaPrecoString"
					serializable="false"
					size="10"
					maxLength="9"/>
			<adsm:textbox
					dataType="text"
					disabled="true"
					property="tabelaPreco.dsDescricao"
					serializable="false"
					size="44" />
		</adsm:complement>

		<%-- SERVICO COMBO --%>
		<adsm:textbox
				dataType="text"
				disabled="true"
				label="servico"
				labelWidth="100"
				size="30"
				property="servico.dsServico"
				serializable="false"
				width="25%"
				required="true"/>

		<%-- ROTAS TEXT --%>
		<adsm:textbox
				dataType="text"
				disabled="true"
				label="origem"
				labelWidth="14%"
				property="rotaPreco.origemString"
				serializable="false"
				size="75"
				width="55%" />

		<adsm:textbox
				dataType="text"
				disabled="true"
				label="destino"
				labelWidth="14%"
				property="rotaPreco.destinoString"
				serializable="false"
				size="75"
				width="55%" />

		<%-- ESPECIFICACAO ROTA TEXT AREA --%>
		<adsm:textarea
				columns="75"
				label="especificacaoRota"
				labelWidth="14%"
				maxLength="500"
				property="dsEspecificacaoRota"
				rows="2"
				width="55%" />

		<%-- MOEDA --%>
		<adsm:textbox
				dataType="text"
				disabled="true"
				property="tabelaPreco.moeda.dsMoeda"
				serializable="false"
				label="moeda"
				labelWidth="14%"
				width="18%" />

		<%-- Include do JSP que contem os campos dos parametros do cliente --%>
		<%@ include file="parametroCliente.jsp" %>

		<adsm:buttonBar>
			<adsm:storeButton
					id="btnSalvar"
					callbackProperty="afterStore"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript" src="../lib/parametroCliente.js"></script>
<script type="text/javascript">
	<!--
	top._consulta = true;
	telaOrigem = "proposta";

	var _disableAll = false;

	var _tabGroup = getTabGroup(this.document);
	if(_tabGroup != undefined) {
		var _tab = _tabGroup.getTab("param");
		_tab.properties.ignoreChangedState = true;
	}

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			var frame = parent.document.frames["cad_iframe"];
			var data = frame.getDadosFromRota();
			ajustaDados(data);

			/* Valida Tipo Proposta */
			validateTipoGeracaoProposta();

			if (data.simulacao.idSimulacao == "") {
				ajustaValoresDefault();
			} else if (top._consulta) {
				top._consulta = false;
				if (data.parametroCliente.idParametroCliente != "") {
					populaParametros(data.parametroCliente.idParametroCliente);
				} else {
					ajustaValoresDefault();
				}
			}
		}
		disableWorkflow();
	}

	function validateTipoGeracaoProposta() {
		if("C" == getElementValue("simulacao.tpGeracaoProposta")) {
			isProposta = true;
			disableGrupoFretePeso(true);
			disableGrupoFreteValor(true);
			disableGrupoFretePercentual(true);
			return false;
		}
		return true;
	}

	function populaParametros(idParametroCliente) {
		var service = "lms.vendas.manterPropostasClienteAction.findParametroCliente";
		var sdo = createServiceDataObject(service, "findParametroCliente", {id:idParametroCliente});
		xmit({serviceDataObjects:[sdo]});
	}

	function validateTab() {
		return (validateTabScript(document.forms) && validateParametroCliente());
	}

	function afterStore_cb(data, errorMsg, errorKey, showErrorAlert, eventObj) {
		store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);
		if (errorMsg == undefined) {
			changeAbasStatus(false);
			var frame = parent.document.frames["cad_iframe"];
			frame.setParametroCliente(data.parametroCliente.idParametroCliente);
			frame.setSimulacao(data.simulacao.idSimulacao);
			frame.setMoeda(data.tabelaPreco.moeda.dsMoeda);

			setElementValue("tabelaPreco.moeda.dsMoeda", data.tabelaPreco.moeda.dsMoeda);
		}
	}

	function findParametroCliente_cb(data, error) {
		onDataLoad_cb(data, error);
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (_disableAll) {
			disableAll();
		} else {
			enableFields();
		}
	}

	function ajustaDados(data) {
		setElementValue("cliente.pessoa.nrIdentificacao", data.cliente.pessoa.nrIdentificacao);
		setElementValue("cliente.pessoa.nmPessoa", data.cliente.pessoa.nmPessoa);
		setElementValue("divisaoCliente.dsDivisaoCliente", data.divisaoCliente.dsDivisaoCliente);
		setElementValue("tabelaPreco.tabelaPrecoString", data.tabelaPreco.tabelaPrecoString);
		setElementValue("tabelaPreco.dsDescricao", data.tabelaPreco.dsDescricao);
		setElementValue("servico.dsServico", data.servico.dsServico);
		setElementValue("rotaPreco.origemString", data.rotaPreco.origemString);
		setElementValue("rotaPreco.destinoString", data.rotaPreco.destinoString);
		setElementValue("cliente.idCliente", data.cliente.idCliente);
		setElementValue("divisaoCliente.idDivisaoCliente", data.divisaoCliente.idDivisaoCliente);
		setElementValue("tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco", data.tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco);
		setElementValue("tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco",data.tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco);
		setElementValue("servico.idServico", data.servico.idServico);
		setElementValue("tabelaPreco.idTabelaPreco", data.tabelaPreco.idTabelaPreco);
		setElementValue("tabelaPreco.moeda.dsMoeda", data.tabelaPreco.moeda.dsMoeda);
		setElementValue("simulacao.idSimulacao", data.simulacao.idSimulacao);
		setElementValue("simulacao.tpGeracaoProposta", data.simulacao.tpGeracaoProposta);
		setElementValue("parametroCliente.idParametroCliente", data.parametroCliente.idParametroCliente);

		setElementValue("municipioByIdMunicipioOrigem.idMunicipio", data.municipioByIdMunicipioOrigem.idMunicipio);
		setElementValue("municipioByIdMunicipioDestino.idMunicipio", data.municipioByIdMunicipioDestino.idMunicipio);
		setElementValue("filialByIdFilialOrigem.idFilial", data.filialByIdFilialOrigem.idFilial);
		setElementValue("filialByIdFilialDestino.idFilial", data.filialByIdFilialDestino.idFilial);
		setElementValue("grupoRegiaoOrigem.idGrupoRegiao", data.grupoRegiaoOrigem.idGrupoRegiaoOrigem);
		setElementValue("grupoRegiaoDestino.idGrupoRegiao", data.grupoRegiaoDestino.idGrupoRegiaoDestino);
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

		if (data.disableAll) {
			disableAll();
			_disableAll = true;
		} else {
			enableFields();
			_disableAll = false;
		}
	}

	function disableAll() {
		setDisabled(document, true);
	}

	function enableFields() {
		if(validateTipoGeracaoProposta()) {
			setDisabled(document, false);
		}
		setDisabled("cliente.pessoa.nrIdentificacao", true);
		setDisabled("cliente.pessoa.nmPessoa", true);
		setDisabled("divisaoCliente.dsDivisaoCliente", true);
		setDisabled("tabelaPreco.tabelaPrecoString", true);
		setDisabled("tabelaPreco.dsDescricao", true);
		setDisabled("servico.dsServico", true);
		setDisabled("rotaPreco.origemString", true);
		setDisabled("rotaPreco.destinoString", true);
		setDisabled("tabelaPreco.moeda.dsMoeda", true);
		disableIndicadoresTabela();
	}

	function ajustaValoresDefault() {
		setDefaultValues();
		setElementValue("dsEspecificacaoRota", "");
		findPercentuaisTotalFrete();
	}

	function disableIndicadoresTabela() {
		if(validateTipoGeracaoProposta()) {
			setDisabled("vlMinFretePeso", isTabela(getElementValue("tpIndicadorMinFretePeso")));
			setDisabled("vlFretePeso", isTabela(getElementValue("tpIndicadorFretePeso")));
			setDisabled("vlTarifaMinima", isTabela(getElementValue("tpTarifaMinima")));
			setDisabled("vlTblEspecifica", isTabela(getElementValue("tpIndicVlrTblEspecifica")));
			setDisabled("vlAdvalorem", isTabela(getElementValue("tpIndicadorAdvalorem")));
			setDisabled("vlAdvalorem2", isTabela(getElementValue("tpIndicadorAdvalorem2")));
			setDisabled("vlValorReferencia", isTabela(getElementValue("tpIndicadorValorReferencia")));
			setDisabled("vlPercentualGris", isTabela(getElementValue("tpIndicadorPercentualGris")));
			setDisabled("vlMinimoGris", isTabela(getElementValue("tpIndicadorMinimoGris")));
			setDisabled("vlPedagio", isTabela(getElementValue("tpIndicadorPedagio")));
			setDisabled("vlPercentualTde", isTabela(getElementValue("tpIndicadorPercentualTde")));
			setDisabled("vlMinimoTde", isTabela(getElementValue("tpIndicadorMinimoTde")));
		}
		disableWorkflow();
	}

	function findPercentuaisTotalFrete() {
		var service = "lms.vendas.manterPropostasClienteAction.findPercentuaisTotalFrete";
		var sdo = createServiceDataObject(service, "findPercentuaisTotalFrete");
		xmit({serviceDataObjects:[sdo]});
	}

	function findPercentuaisTotalFrete_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		setElementValue("pcCobrancaReentrega", setFormat("pcCobrancaReentrega", data.pcCobrancaReentrega));
		setElementValue("pcCobrancaDevolucoes", setFormat("pcCobrancaDevolucoes", data.pcCobrancaDevolucoes));
	}

	function changeAbasStatus(status) {
		var tabGroup = getTabGroup(this.document);
		if(tabGroup != undefined) {
			tabGroup.setDisabledTab("tax", status);
			tabGroup.setDisabledTab("gen", status);
		}
	}

	//-->
</script>