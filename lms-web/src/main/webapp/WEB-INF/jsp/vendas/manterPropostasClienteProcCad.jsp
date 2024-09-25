<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vendas.manterPropostasClienteProcAction"
			 onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-30054" />
		<adsm:include key="LMS-30057" />
		<adsm:include key="LMS-01163" />
		<adsm:include key="LMS-01162" />
		<adsm:include key="LMS-01033" />
		<adsm:include key="required2FieldsOr" />
		<adsm:include key="tabela" />
		<adsm:include key="tabelaFob" />
		<adsm:include key="LMS-01168"/>
		<adsm:include key="LMS-01220"/>
	</adsm:i18nLabels>
	<adsm:form action="/vendas/manterPropostasCliente"
			   idProperty="idSimulacao" onDataLoadCallBack="formLoad">


		<adsm:hidden property="tpDiferencaAdvalorem" />
		<adsm:hidden property="idProcessoWorkflow" />
		<adsm:hidden property="simulacao.idSimulacao" />
		<adsm:hidden property="filial.idFilial" />
		<adsm:hidden property="usuario.idFilial" />
		<adsm:hidden property="simulacao.tpSituacaoAprovacao.value" />
		<adsm:hidden property="tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" />
		<adsm:hidden property="blEfetivada" />
		<adsm:hidden property="tpSituacaoAprovacao.value" />
		<adsm:hidden property="tpProposta" value="P" serializable="false" />
		<adsm:hidden property="divisaoCliente.dsDivisaoCliente" />
		<adsm:hidden property="servico.dsServico" />
		<adsm:hidden property="servico.tpModal" />
		<adsm:hidden property="save.mode" value="parcial" />
		<adsm:hidden
				property="tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco"
				serializable="false" />
		<adsm:hidden
				property="tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco"
				serializable="false" />
		<adsm:hidden property="blTabelaFob" value="N" serializable="false" />

		<%----------------------%>
		<%-- NR PROPOSTA TEXT --%>
		<%----------------------%>
		<adsm:textbox dataType="text" label="numeroProposta"
					  property="nrProposta" size="10" maxLength="10" labelWidth="16%"
					  disabled="true" />

		<%---------------------%>
		<%-- PROMOTOR LOOKUP --%>
		<%---------------------%>

		<adsm:lookup action="/configuracoes/consultarFuncionariosView" dataType="text"
					 criteriaProperty="nrMatricula" exactMatch="true" serializable="true"
					 idProperty="idUsuario" property="usuario" label="promotorVendas" size="20"
					 maxLength="20" width="45%" labelWidth="16%" required="true" service="lms.vendas.manterPropostasClienteAction.findFuncionarioPromotorLookup">
			<adsm:propertyMapping modelProperty="nmUsuario" relatedProperty="usuario.nmUsuario" />
			<adsm:textbox dataType="text" disabled="true" serializable="true" property="usuario.nmUsuario" size="30" />
		</adsm:lookup>

		<%--------------------%>
		<%-- CLIENTE LOOKUP --%>
		<%--------------------%>
		<adsm:lookup action="/vendas/manterDadosIdentificacao" dataType="text"
					 criteriaProperty="pessoa.nrIdentificacao" exactMatch="true"
					 idProperty="idCliente" property="cliente" label="cliente" size="20"
					 maxLength="20" width="45%" labelWidth="16%"
					 service="lms.vendas.manterPropostasClienteAction.findClienteLookup"
					 required="true" onDataLoadCallBack="lookupCliente"
					 afterPopupSetValue="aferPopupCliente">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa"
								  relatedProperty="cliente.pessoa.nmPessoa" />

			<adsm:textbox dataType="text" disabled="true" serializable="false"
						  property="cliente.pessoa.nmPessoa" size="30" />
		</adsm:lookup>

		<%-------------------%>
		<%-- DIVISAO COMBO --%>
		<%-------------------%>
		<adsm:combobox optionLabelProperty="dsDivisaoCliente" width="29%"
					   optionProperty="idDivisaoCliente" labelWidth="10%" boxWidth="140"
					   property="divisaoCliente.idDivisaoCliente"
					   service="lms.vendas.manterPropostasClienteAction.findDivisaoCombo"
					   label="divisao"
					   required="true">
			<adsm:propertyMapping criteriaProperty="cliente.idCliente"
								  modelProperty="cliente.idCliente" />

			<adsm:propertyMapping
					relatedProperty="divisaoCliente.dsDivisaoCliente"
					modelProperty="dsDivisaoCliente" />
		</adsm:combobox>

		<%-------------------%>
		<%-- TABELA LOOKUP --%>
		<%-------------------%>
		<adsm:lookup label="tabela" property="tabelaPreco"
					 service="lms.vendas.manterPropostasClienteAction.findLookupTabelaPreco"
					 action="/tabelaPrecos/manterTabelasPreco" idProperty="idTabelaPreco"
					 criteriaProperty="tabelaPrecoString"
					 onclickPicker="onclickPickerLookupTabelaPreco()"
					 onDataLoadCallBack="dataLoadTabelaPreco"
					 afterPopupSetValue="aferPopupTabelaPreco"
					 onPopupSetValue="validateSubtipoTabelaPreco"
					 onchange="return onChangeTabelaPreco();" dataType="text" size="10"
					 maxLength="9" width="45%" labelWidth="16%" >

			<adsm:propertyMapping criteriaProperty="tpProposta"
								  relatedProperty="tpProposta" />
			<adsm:propertyMapping modelProperty="dsDescricao"
								  relatedProperty="tabelaPreco.dsDescricao" />
			<adsm:propertyMapping modelProperty="moeda.siglaSimbolo"
								  relatedProperty="tabelaPreco.moeda.siglaSimbolo" />
			<adsm:propertyMapping modelProperty="moeda.sgMoeda"
								  relatedProperty="tabelaPreco.moeda.sgMoeda" />
			<adsm:propertyMapping modelProperty="moeda.dsSimbolo"
								  relatedProperty="tabelaPreco.moeda.dsSimbolo" />
			<adsm:propertyMapping criteriaProperty="blTabelaFob"
								  modelProperty="blTabelaFob" />
			<adsm:propertyMapping
					relatedProperty="tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco"
					modelProperty="subtipoTabelaPreco.idSubtipoTabelaPreco" />
			<adsm:propertyMapping
					relatedProperty="tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco"
					modelProperty="subtipoTabelaPreco.tpSubtipoTabelaPreco" />

			<adsm:textbox dataType="text" property="tabelaPreco.dsDescricao"
						  size="30" maxLength="30" disabled="true" />
		</adsm:lookup>
		<adsm:hidden property="tabelaPreco.moeda.siglaSimbolo"
					 serializable="false" />
		<adsm:hidden property="tabelaPreco.moeda.sgMoeda" serializable="false" />
		<adsm:hidden property="tabelaPreco.moeda.dsSimbolo"
					 serializable="false" />

		<%-------------------%>
		<%-- SERVICO COMBO --%>
		<%-------------------%>
		<adsm:combobox boxWidth="200" label="servico" onlyActiveValues="true"
					   optionLabelProperty="dsServico" optionProperty="idServico"
					   property="servico.idServico"
					   service="lms.vendas.manterPropostasClienteAction.findServicoCombo"
					   onchange="changeServico(this)" required="true" width="29%"
					   labelWidth="10%" autoLoad="false">
			<adsm:propertyMapping relatedProperty="servico.dsServico"
								  modelProperty="dsServico" />
			<adsm:propertyMapping relatedProperty="servico.tpModal"
								  modelProperty="tpModal.value" />
		</adsm:combobox>

		<adsm:checkbox label="pagaFreteTonelada"
					   property="blPagaFreteTonelada" labelWidth="16%" width="29%" />

		<adsm:checkbox label="emiteCargaCompleta"
					   property="blEmiteCargaCompleta" labelWidth="26%" width="29%" />

		<%-----------------------%>
		<%-- TABELA FOB LOOKUP --%>
		<%-----------------------%>
		<adsm:lookup
				action="/tabelaPrecos/manterTabelasPreco"
				criteriaProperty="tabelaPrecoString"
				dataType="text"
				exactMatch="true"
				idProperty="idTabelaPreco"
				label="tabelaFob"
				maxLength="9"
				property="tabelaPrecoFob"
				service="lms.vendas.manterPropostasClienteAction.findLookupTabelaPreco"
				onclickPicker="onclickPickerLookupTabelaPrecoFob()"
				onPopupSetValue="validateSubtipoTabelaPrecoFob"
				afterPopupSetValue="afterPopupTabelaPrecoFob"
				onDataLoadCallBack="lookupTabelaPrecoFob"
				onchange="return changeTabelaPrecoFob();"
				size="5"
				labelWidth="16%"
				width="40%">

			<adsm:propertyMapping relatedProperty="tabelaPrecoFob.dsDescricao"
								  modelProperty="dsDescricao" />

			<adsm:textbox dataType="text" disabled="true"
						  property="tabelaPrecoFob.dsDescricao" size="30" />
		</adsm:lookup>




		<adsm:combobox
				label="tipoGeracao"
				property="tpGeracaoProposta"
				onlyActiveValues="true"
				optionProperty="value"
				optionLabelProperty="description"
				service="lms.vendas.manterPropostasClienteProcAction.findComboTipoGeracao"
				labelWidth="15%"
				width="29%"
				boxWidth="200"
				autoLoad="false"
				renderOptions="true"
				onchange="return verifyGeracaoProposta();"
				required="true"/>

		<adsm:textbox
				label="fatorCubagem"
				property="nrFatorCubagem"
				dataType="decimal"
				size="10" maxValue="999"
				maxLength="6"
				labelWidth="16%"
				width="40%"/>

		<adsm:textbox
				label="fatorDensidade"
				property="nrFatorDensidade"
				dataType="decimal"
				size="10"
				maxValue="999.99"
				maxLength="6"
				minValue="0"
				labelWidth="16%"
				onchange="return validateFatorDensidade();"
				width="32%"/>

		<adsm:section caption="limitadores" />

		<adsm:textbox label="nrLimiteMetragemCubica" minValue="0"
					  mask="###,##0.000"
					  maxValue="999999.999"
					  dataType="currency" labelWidth="16%"
					  property="nrLimiteMetragemCubica" width="35%"
					  size="10"
					  maxLength="10"/>

		<adsm:textbox
				dataType="integer"
				property="nrLimiteQuantVolume"
				label="nrLimiteQuantVolume"
				labelWidth="20%"
				width="29%"
				maxLength="5"
				size="10"/>


		<adsm:buttonBar>
			<adsm:button
					id="btnServicosAdicionais"
					caption="servicosAdicionais"
					onclick="clickServicosAdicionais();"
					disabled="false"/>
			<adsm:button
					id="btnGerarPropostas"
					caption="gerarParametros"
					onclick="clickGerarParametros();"
					disabled="false"/>
			<adsm:button
					id="btnParametros"
					caption="manterParam"
					onclick="clickManterParametros();"
					disabled="false"/>
			<adsm:button id="btnSalvar" caption="salvar"
						 onclick="return onClickSalvar();" />
			<adsm:button caption="limpar" onclick="return cleanTabs();"
						 id="btnLimpar" />
			<adsm:removeButton id="btnExcluir" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	var _disableAll = false;
	var _changedParametros = true;
	var _idServico;
	var servicoPadrao;

	function initWindow(eventObj) {
		if (eventObj.name == "gridRow_click") {
			_changedParametros = false;
		} else if (eventObj.name == "removeButton") {
			_changedParametros = true;
		}
		if (_disableAll == true) {
			disableAll();
		} else {
			changeFieldsStatus();
			changeParametrosStatus();
		}
		setDisabled("btnLimpar", false);
		disableWorkflow();
	}

	function formLoad_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		onDataLoad_cb(data, error);

		_idServico = data.servico.idServico;
		loadComboBoxServico([data.servico]);

		if (data.disableAll == "true") {
			_disableAll = true;
			disableAll();
		} else {
			_disableAll = false;
			changeFieldsStatus();
		}

		var idProcessoWorkflow = getElementValue("idProcessoWorkflow");
		/* Se a proposta foi aberta a partir da tela de workflow. */
		if (idProcessoWorkflow != "") {
			getTabGroup(document).getTab("pesq").setDisabled(true);
			showSuccessMessage();
			disableWorkflow();
		}
		verifyGeracaoProposta();
	}

	function changeFieldsStatus() {
		var idSimulacao = getElementValue("simulacao.idSimulacao");
		if (idSimulacao != "") {
			enableFieldsEdit();

		} else {
			enableFieldsInsert();
		}
	}

	function disableAll() {
		if (getElementValue("idProcessoWorkflow") == "") {
			setDisabled(document, true);
			if (getElementValue("tabelaPreco.idTabelaPreco") != ""
					&& getElementValue("tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco") != "P") {
				setDisabled("btnServicosAdicionais", false);
				setDisabled("btnParametros", false);
				verifyGeracaoProposta();
			}
			setDisabled("btnLimpar", false);
			changeAbasStatus(false);
		}
	}

	function enableFieldsInsert() {
		if (getElementValue("idProcessoWorkflow") == "") {
			enableFields();
			setDisabled("btnExcluir", true);
			setDisabled("btnServicosAdicionais", true);
			setDisabled("btnGerarPropostas", true);
			setDisabled("btnParametros", true);
			setElementValue("tpGeracaoProposta", "T");//Tarifada;
			changeAbasStatus(true);
		}
	}

	function enableFieldsEdit() {
		enableFields();
		changeAbasStatus(false);
		disableFieldsTabelaFob();
		setDisabled("btnGerarPropostas", true);
	}

	function enableFields() {
		if (getElementValue("idProcessoWorkflow") == "") {
			setDisabled(document, false);
			setDisabled("cliente.pessoa.nmPessoa", true);
			setDisabled("tabelaPreco.dsDescricao", true);
			setDisabled("tabelaPrecoFob.dsDescricao", true);
			setDisabled("nrProposta", true);
			setDisabled("usuario.nmUsuario", true);

			refreshTabelaPrecoFobStatus();
		}
	}

	function changeAbasStatus(status) {
		var tabGroup = getTabGroup(document);
		if(tabGroup != undefined) {
			// LMS-6172 - desabilita abas para modo consulta
			if (getElementValue("simulacao.idSimulacao") == "") {
				status = true;
			}

			if (!status
					&& getElementValue("tabelaPreco.idTabelaPreco") != ""
					&& getElementValue("tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco") != "P") {

				tabGroup.setDisabledTab("form", status);
				tabGroup.setDisabledTab("resumo", status);
			} else {
				tabGroup.setDisabledTab("form", status);
				tabGroup.setDisabledTab("resumo", status);
			}

			// LMS-6172
			tabGroup.setDisabledTab("anexo", status);

			// LMS-6191 - habilita somente M(em efetiva��o), F(efetivada) ou H(efetiva��o reprovada)
			var tpSituacaoAprovacao = getElementValue("simulacao.tpSituacaoAprovacao.value");
			if (tpSituacaoAprovacao != "M" && tpSituacaoAprovacao != "F" && tpSituacaoAprovacao != "H") {
				status = true;
			}
			tabGroup.setDisabledTab("historico", status);
		}
	}

	function onClickSalvar() {
		var tabGroup = getTabGroup(document);
		var tabForm = tabGroup.getTab("form");
		var formsForm = tabForm.getDocument().forms;

		if (getElementValue("tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco") == "P" &&
				getElementValue("tpGeracaoProposta")=="C"){
			alertI18nMessage("LMS-01168");
			return false;
		}

		if (validateTabScript(document.forms) && validateTabScript(formsForm)) {
			if (getElementValue("tabelaPreco.idTabelaPreco") != ""
					|| getElementValue("tabelaPrecoFob.idTabelaPreco") != "") {
				var data = buildFormBeanFromForm(formsForm[0]);
				merge(data, buildFormBeanFromForm(document.forms[0]));
				data.usuario.nrMatricula = getElementValue("usuario.nrMatricula");

				var service = "lms.vendas.manterPropostasClienteProcAction.store";
				var sdo = createServiceDataObject(service, "afterStore", data);
				xmit({serviceDataObjects:[sdo]});
			} else {
				alertI18nMessage("required2FieldsOr", ["tabela", "tabelaFob"], true);
			}
		}
	}

	function afterStore_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		store_cb(data, error);
		changeAbasStatus(false);
		setElementValue("nrProposta", data.nrProposta);
		setElementValue("simulacao.idSimulacao", data.simulacao.idSimulacao);
		setElementValue("idSimulacao", data.simulacao.idSimulacao);
		changeParametrosStatus(false);
		disableFieldsTabelaFob();
		setFocus("btnLimpar", false);
	}

	function cleanTabs() {
		_disableAll = false;
		newButtonScript(document);
		getElement("servico.idServico").options.length = 1;
		var tabGroup = getTabGroup(document);
		var tab = tabGroup.getTab("form");
		newButtonScript(tab.getDocument());
		var tabResumo = tabGroup.getTab("resumo");
		newButtonScript(tabResumo.getDocument());
		changeParametrosStatus(true);
		top._consultaFormalidades = true;
	}

	/***************************************/
	/* ON CLICK PICKER TABELA PRECO LOOKUP */
	/***************************************/
	function onclickPickerLookupTabelaPreco() {
		var tabelaPrecoString = getElementValue("tabelaPreco.tabelaPrecoString");
		if (tabelaPrecoString != "") {
			setElementValue("tabelaPreco.tabelaPrecoString", "");
		}
		setElementValue("tpProposta", "P");
		lookupClickPicker({e:document.forms[0].elements['tabelaPreco.idTabelaPreco']});

		if (getElementValue("tabelaPreco.tabelaPrecoString") == "" && tabelaPrecoString != "") {
			setElementValue("tabelaPreco.tabelaPrecoString", tabelaPrecoString);
		}

		if (getElementValue("tabelaPreco.tabelaPrecoString") != tabelaPrecoString) {
			if (getElementValue("simulacao.idSimulacao") != "") {
				changeParametrosStatus(true);
			}
		}
	}

	function onclickPickerLookupTabelaPrecoFob() {
		var tabelaPrecoString = getElementValue("tabelaPrecoFob.tabelaPrecoString");
		if(tabelaPrecoString != "") {
			setElementValue("tabelaPrecoFob.tabelaPrecoString","");
		}
		lookupClickPicker({e:getElement("tabelaPrecoFob.idTabelaPreco")});

		if(getElementValue("tabelaPrecoFob.tabelaPrecoString") == "" && tabelaPrecoString != "") {
			setElementValue("tabelaPrecoFob.tabelaPrecoString", tabelaPrecoString);
		}
	}

	function onChangeTabelaPreco() {
		var result = tabelaPreco_tabelaPrecoStringOnChangeHandler();
		if (getElementValue("simulacao.idSimulacao") != "") {
			changeParametrosStatus(true);
		}
		if (getElementValue("tabelaPreco.idTabelaPreco") == "") {
			setDisabled("tabelaPrecoFob.idTabelaPreco", false);
			getElement("servico.idServico").options.length = 1;
			getElement("servico.idServico").selectedIndex = 0;
			changeAbasStatus(true);
		} else {
			changeAbasStatus(false);
		}
		if (getElementValue("tabelaPrecoFob.idTabelaPreco") != "") {
			// seta o campo servico para o padr�o e desabilita
			carregaServicoPadraoApenasTabelaFob();
		}
		return result;
	}

	function changeParametrosStatus(status) {
		if (getElementValue("idProcessoWorkflow") == "") {
			if (getElementValue("tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco") != "P") {
				if (status != undefined) {
					_changedParametros = status;
					setDisabled("btnServicosAdicionais", status);
					setDisabled("btnParametros", status);
				} else {
					setDisabled("btnServicosAdicionais", _changedParametros);
					setDisabled("btnParametros", _changedParametros);
				}
			}
		}
		verifyGeracaoProposta();
	}

	function verifyGeracaoProposta() {
		if(hasValue(getElementValue("idSimulacao"))) {
			setDisabled("btnGerarPropostas", ((getElementValue("tpGeracaoProposta") != "C" && getElementValue("tpGeracaoProposta") != "P") || getElementValue("servico.tpModal") != "R"));
		} else if(hasValue(getElementValue("servico.tpModal")) && getElementValue("servico.tpModal") != "R"){
			setElementValue("tpGeracaoProposta", "T");
		}
		setDisabled("servico.idServico", hasValue(getElementValue("idSimulacao")));
		setDisabled("tpGeracaoProposta", hasValue(getElementValue("idSimulacao")));
		setDisabled("nrFatorDensidade", hasValue(getElementValue("idSimulacao")));
	}

	function lookupCliente_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		/** valida se eh cliente Especial */
		if (data.length > 0) {
			if (!validateClienteEspecial(data[0])) {
				return;
			}
		}

		/** Popula dados e formata nrIdentificacao */
		var retorno = cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
		if (data.length > 0) {
			setElementValue("cliente.pessoa.nrIdentificacao", data[0].pessoa.nrIdentificacaoFormatado);
		}
		return retorno;
	}

	function aferPopupCliente(data) {
		if (validateClienteEspecial(data)) {
			setElementValue("cliente.pessoa.nrIdentificacao", data.pessoa.nrIdentificacaoFormatado);
		}
	}


	function validateClienteEspecial(cliente) {
		if(cliente != undefined && cliente.tpCliente.value != "S") {
			alertI18nMessage("LMS-01033");
			resetValue("cliente.idCliente");
			resetValue("cliente.pessoa.nmPessoa");
			setFocus("cliente.pessoa.nrIdentificacao", false);
			return false;
		}
		return true;
	}

	function aferPopupTabelaPreco(data, dialogWindow) {
		loadComboBoxServico([data.tipoTabelaPreco.servico]);
	}

	function dataLoadTabelaPreco_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}

		if (data.length == 1) {
			if (!validateSubtipoTabelaPreco(data[0])) {
				resetValue("tabelaPreco.idTabelaPreco");
				setFocus("tabelaPreco.tabelaPrecoString", false);
				return false;
			}
			resetValue("tabelaPrecoFob.idTabelaPreco");
			setDisabled("tabelaPrecoFob.idTabelaPreco", true);
		}

		var servicos = [];
		if (data != undefined && data.length == 1) {
			if (!validaTabelaPreco(data[0].tipoTabelaPreco.tpTipoTabelaPreco.value)) {
				return false;
			}
			servicos = [data[0].tipoTabelaPreco.servico];
		}
		loadComboBoxServico(servicos);
		return tabelaPreco_tabelaPrecoString_exactMatch_cb(data);
	}

	function loadComboBoxServico(data) {
		if(data.length < 1) {
			loadComboBoxServico_cb(data);
			return;
		}
		var situacao = "A";
		var sdo = createServiceDataObject(
				"lms.vendas.manterPropostasClienteAction.findServicoCombo",
				"loadComboBoxServico",
				{tpModal:data[0].tpModal.value, tpAbrangencia:data[0].tpAbrangencia.value, tpSituacao:situacao});
		xmit({serviceDataObjects:[sdo]});
	}

	function loadComboBoxServico_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		servico_idServico_cb(data);
		if(_idServico != undefined) {
			setElementValue("servico.idServico", _idServico);
			_idServico = undefined;
		}
		if (getElementValue("tabelaPreco.idTabelaPreco") != ""
				&& getElementValue("tabelaPrecoFob.idTabelaPreco") != "") {
			if (data.length > 1) {
				var blPossuiServicoPadrao = false;
				for (var i = 0; i < data.length; i++) {
					if (data[i].idServico == servicoPadrao.idServico) {
						blPossuiServicoPadrao = true;
						break;
					}
				}
				if (!blPossuiServicoPadrao) {
					resetValue("tabelaPreco.idTabelaPreco");
					resetValue("servico.idServico");
					getElement("servico.idServico").options.length = 1;
					getElement("servico.idServico").selectedIndex = 0;
					alertI18nMessage("LMS-30057", [getElementValue("tabelaPreco.tabelaPrecoString"), getElementValue("tabelaPrecoFob.tabelaPrecoString")], false);
				} else {
					setElementValue("servico.idServico", servicoPadrao.idServico);
					if (_disableAll == undefined || _disableAll == false) {
						setDisabled("servico.idServico", false);
					}
				}
			} else if (data.length == 1) {
				if (data.idServico != servicoPadrao.idServico) {
					alertI18nMessage("LMS-30057", [getElementValue("tabelaPreco.tabelaPrecoString"), getElementValue("tabelaPrecoFob.tabelaPrecoString")], false);
				}
			}
		}
	}

	function validaTabelaPreco(tpTipoTabelaPreco) {
		if (tpTipoTabelaPreco == "C") {
			setElementValue("tabelaPreco.idTabelaPreco", "");
			setElementValue("tabelaPreco.tabelaPrecoString", "");
			setElementValue("tabelaPreco.dsDescricao", "");
			alertI18nMessage("LMS-30054");
			setFocus("tabelaPreco.tabelaPrecoString", false);
			return false;
		}
		return true;
	}

	function setSaveMode(mode) {
		setElementValue("save.mode", mode);
	}

	function myOnPageLoad_cb() {
		var url = new URL(parent.location.href);
		if (url.parameters != undefined
				&& url.parameters.idProcessoWorkflow != undefined
				&& url.parameters.idProcessoWorkflow != '') {
			onDataLoad(url.parameters.idProcessoWorkflow);
		}
		findDadosSessao();
		onPageLoad_cb();
	}

	function disableWorkflow() {
		if (getElementValue("idProcessoWorkflow") != "") {
			setDisabled(document, true);
			setDisabled("btnParametros", false);
			setDisabled("btnServicosAdicionais", false);
			changeAbasStatus(false);
		}
	}

	function validateSubtipoTabelaPrecoFob(data, dialogWindow) {
		if (data != undefined) {
			if (data.subtipoTabelaPreco.tpSubtipoTabelaPreco == "F") {
				return true;
			} else {
				alertI18nMessage("LMS-01163");
				return false;
			}
		}
		return true;
	}

	function validateSubtipoTabelaPreco(data, dialogWindow) {
		if (data != undefined) {
			if (data.subtipoTabelaPreco.tpSubtipoTabelaPreco != "F") {
				return true;
			} else {
				alertI18nMessage("LMS-01162");
				return false;
			}
		}
		return true;
	}

	function validateFatorDensidade(data, dialogWindow) {
		if (getElement("nrFatorDensidade").value != ""){
			var vlFator = getElement("nrFatorDensidade").value.toString().replace(",", ".").valueOf();
			if (vlFator > 1.009) {	//1.009 para que ap�s a aplica��o da m�scara, a parte decimal n�o seja arredondada e ignorada pela compara��o
				return true;
			} else {
				alertI18nMessage("LMS-01220");
				resetValue("nrFatorDensidade");
				setFocus("nrFatorDensidade", false);
				return false;
			}
		}
		return true;
	}

	function lookupTabelaPrecoFob_cb(data, error) {
		var result = tabelaPrecoFob_tabelaPrecoString_exactMatch_cb(data);
		if (data.length == 1 && !validateSubtipoTabelaPrecoFob(data[0])) {
			resetValue("tabelaPrecoFob.idTabelaPreco");
			setFocus("tabelaPrecoFob.tabelaPrecoString", false);
		}
		atualizaTabelaPrecoFob();
		return result;
	}

	function changeServico(element) {
		comboboxChange({e:element});
		verifyGeracaoProposta();
		refreshTabelaPrecoFobStatus();
		verifyTpServicoLimitadores();
	}

	function verifyTpServicoLimitadores() {
		if(getElementValue("servico.tpModal") != "R"){
			limpaLimitadores();
			desabilitaLimitadores();
		}else{
			habilitaLimitadores();
		}
	}

	function desabilitaLimitadores(){
		setDisabled("nrLimiteMetragemCubica",true);
		setDisabled("nrLimiteQuantVolume",true);
	}

	function habilitaLimitadores(){
		setDisabled("nrLimiteMetragemCubica",false);
		setDisabled("nrLimiteQuantVolume",false);
	}

	function limpaLimitadores(){
		setElementValue("nrLimiteMetragemCubica", "");
		setElementValue("nrLimiteQuantVolume", "");
	}

	function findDadosSessao() {
		var sdo = createServiceDataObject("lms.vendas.manterPropostasClienteProcAction.findDadosSessao", "findDadosSessao");
		xmit({serviceDataObjects:[sdo]});
	}

	function findDadosSessao_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		servicoPadrao = {
			idServico : data.idServicoPadrao,
			dsServico : data.dsServicoPadrao
		};
	}

	function changeTabelaPrecoFob() {
		var result = tabelaPrecoFob_tabelaPrecoStringOnChangeHandler();
		if (getElementValue("tabelaPrecoFob.idTabelaPreco") == "") {
			setDisabled("servico.idServico", false);
		}
		return result;
	}

	function carregaServicoPadraoApenasTabelaFob() {
		servico_idServico_cb([servicoPadrao]);
		setElementValue("servico.idServico", servicoPadrao.idServico);
		setDisabled("servico.idServico", true);
	}

	function atualizaTabelaPrecoFob() {
		if (getElementValue("tabelaPreco.idTabelaPreco") != "") {
			// verifica se a tabela de preco possui o servico padrao, em caso
			// positivo seleciona-o e desabilita o campo, em caso negativo
			// exibe mensagem e limpa a tabela de preco fob
			var options = getElement("servico.idServico").options;
			var blPossuiServicoPadrao = false;
			for (var i = 0; i < options.length; i++) {
				var option = options[i];
				if (option.value == servicoPadrao.idServico) {
					blPossuiServicoPadrao = true;
					break;
				}
			}
			if (blPossuiServicoPadrao) {
				setElementValue("servico.idServico", servicoPadrao.idServico);
				setDisabled("servico.idServico", false);
			} else {
				alertI18nMessage("LMS-30057", [getElementValue("tabelaPrecoFob.tabelaPrecoString"), getElementValue("tabelaPreco.tabelaPrecoString")], false);
				resetValue("tabelaPrecoFob.idTabelaPreco");
				setFocus("tabelaPrecoFob.tabelaPrecoString", false);
			}
		} else {
			// carrega a combo de servicos apenas com o servico padrao
			carregaServicoPadraoApenasTabelaFob();
		}
	}

	function afterPopupTabelaPrecoFob(data, dialogWindow) {
		atualizaTabelaPrecoFob();
	}

	function disableFieldsTabelaFob() {
		if (getElementValue("tabelaPreco.idTabelaPreco") == "" ||
				getElementValue("tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco") == "P") {

			setDisabled("btnServicosAdicionais", true);
			setDisabled("btnGerarPropostas", true);
			setDisabled("btnParametros", true);
		}

		if (getElementValue("tabelaPreco.idTabelaPreco") == "") {
			setDisabled("servico.idServico", true);
		}
	}

	function clickGerarParametros() {
		var url = "/vendas/gerarParametrosPropostasCliente.do?cmd=main";
		url += "&idProcessoWorkflow=" + document.getElementById("idProcessoWorkflow").value;
		url += "&simulacao.idSimulacao=" + document.getElementById("simulacao.idSimulacao").value;
		url += "&tpGeracaoProposta=" + document.getElementById("tpGeracaoProposta").value;
		url += "&idSimulacao=" + document.getElementById("simulacao.idSimulacao").value;
		url += "&tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco=" + document.getElementById("tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco").value;
		url += "&tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco=" + document.getElementById("tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco").value;
		url += "&disableAll=" + _disableAll;
		clickRedirectButton(url);
	}

	function clickManterParametros() {
		var url = "/vendas/manterPropostasClienteParam.do?cmd=main";
		url += "&idProcessoWorkflow=" + document.getElementById("idProcessoWorkflow").value;
		url += "&simulacao.idSimulacao=" + document.getElementById("simulacao.idSimulacao").value;
		url += "&cliente.pessoa.nmPessoa=" + encodeString(document.getElementById("cliente.pessoa.nmPessoa").value);
		url += "&cliente.pessoa.nrIdentificacao=" + document.getElementById("cliente.pessoa.nrIdentificacao").value;
		url += "&cliente.idCliente=" + document.getElementById("cliente.idCliente").value;
		url += "&divisaoCliente.idDivisaoCliente=" + document.getElementById("divisaoCliente.idDivisaoCliente").value;
		url += "&tabelaPreco.idTabelaPreco=" + document.getElementById("tabelaPreco.idTabelaPreco").value;
		url += "&tabelaPreco.tabelaPrecoString=" + encodeString(document.getElementById("tabelaPreco.tabelaPrecoString").value);
		url += "&tabelaPreco.dsDescricao=" + encodeString(document.getElementById("tabelaPreco.dsDescricao").value);
		url += "&servico.idServico=" + document.getElementById("servico.idServico").value;
		url += "&tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco=" + document.getElementById("tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco").value;
		url += "&tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco=" + document.getElementById("tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco").value;
		url += "&simulacao.tpGeracaoProposta=" + getElementValue("tpGeracaoProposta");
		clickRedirectButton(url);
	}

	function clickServicosAdicionais() {
		var url = '/vendas/manterPropostasServicosAdicionais.do?cmd=main';
		url += "&idProcessoWorkflow=" + document.getElementById("idProcessoWorkflow").value;
		url += "&simulacao.idSimulacao=" + document.getElementById("simulacao.idSimulacao").value;
		url += "&tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco=" + escape(document.getElementById("tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco").value);
		url += "&tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao=" + document.getElementById("cliente.pessoa.nrIdentificacao").value;
		url += "&tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa=" + escape(document.getElementById("cliente.pessoa.nmPessoa").value);
		url += "&tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente=" + escape(document.getElementById("divisaoCliente.dsDivisaoCliente").value);
		url += "&tabelaDivisaoCliente.tabelaPreco.idTabelaPreco=" + document.getElementById("tabelaPreco.idTabelaPreco").value;
		url += "&tabelaDivisaoCliente.tabelaPreco.tabelaPrecoString=" + document.getElementById("tabelaPreco.tabelaPrecoString").value;
		url += "&tabelaDivisaoCliente.tabelaPreco.dsDescricao=" + escape(document.getElementById("tabelaPreco.dsDescricao").value);
		url += "&tabelaDivisaoCliente.servico.dsServico=" + document.getElementById("servico.dsServico").value;
		url += "&tabelaDivisaoCliente.tabelaPreco.moeda.sgMoeda.siglaSimbolo=" + document.getElementById("tabelaPreco.moeda.siglaSimbolo").value;
		url += "&sgMoeda=" + document.getElementById("tabelaPreco.moeda.sgMoeda").value;
		url += "&dsSimbolo=" + document.getElementById("tabelaPreco.moeda.dsSimbolo").value;
		clickRedirectButton(url);
	}

	function clickRedirectButton(url) {
		if (getElementValue("idProcessoWorkflow") == "") {
			parent.parent.redirectPage(contextRoot + url);
		} else {
			showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:500px;');
		}
	}

	function refreshTabelaPrecoFobStatus() {
		if (servicoPadrao != undefined) {
			if (getElementValue("tabelaPreco.idTabelaPreco") != "") {
				if (getElementValue("servico.idServico") == servicoPadrao.idServico) {
					setDisabled("tabelaPrecoFob.idTabelaPreco", false);
				} else {
					resetValue("tabelaPrecoFob.idTabelaPreco");
					setDisabled("tabelaPrecoFob.idTabelaPreco", true);
				}
			}
		}
	}
</script>