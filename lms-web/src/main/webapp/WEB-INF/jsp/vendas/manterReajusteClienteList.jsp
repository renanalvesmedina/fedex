<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterReajusteClienteAction" onPageLoad="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-00055"/>
		<adsm:include key="LMS-01033"/>
		<adsm:include key="LMS-01034"/>
		<adsm:include key="LMS-01162"/>
	</adsm:i18nLabels>
	<adsm:form action="/vendas/manterReajusteCliente" id="formList">

		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:lookup
				label="filial"
				labelWidth="16%"
				width="45%"
				property="filial"
				idProperty="idFilial"
				criteriaProperty="sgFilial"
				action="/municipios/manterFiliais"
				service="lms.vendas.manterReajusteClienteAction.findFilialLookup"
				dataType="text"
				size="3"
				maxLength="3"
				exactMatch="true"
				minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping
					relatedProperty="filial.pessoa.nmFantasia"
					modelProperty="pessoa.nmFantasia" />

			<adsm:textbox
					dataType="text"
					property="filial.pessoa.nmFantasia"
					serializable="false"
					size="30"
					maxLength="30"
					disabled="true"/>
		</adsm:lookup>

		<%----------------------%>
		<%-- NR REAJUSTE TEXT --%>
		<%----------------------%>
		<adsm:textbox
				label="numeroReajuste"
				property="nrReajuste"
				dataType="integer"
				size="10"
				maxLength="10"
				labelWidth="10%"
				width="29%"/>

		<%--------------------%>
		<%-- CLIENTE LOOKUP --%>
		<%--------------------%>
		<adsm:lookup
				action="/vendas/manterDadosIdentificacao"
				dataType="text"
				criteriaProperty="pessoa.nrIdentificacao"
				exactMatch="true"
				idProperty="idCliente"
				property="cliente"
				label="cliente"
				size="20"
				maxLength="20"
				width="45%"
				labelWidth="16%"
				service="lms.vendas.manterReajusteClienteAction.findClienteLookup"
				onDataLoadCallBack="lookupCliente"
				afterPopupSetValue="aferPopupCliente">
			<adsm:propertyMapping
					modelProperty="pessoa.nmPessoa"
					relatedProperty="cliente.pessoa.nmPessoa" />

			<adsm:textbox
					dataType="text"
					disabled="true"
					serializable="false"
					property="cliente.pessoa.nmPessoa"
					size="30" />
		</adsm:lookup>

		<%-------------------%>
		<%-- DIVISAO COMBO --%>
		<%-------------------%>
		<adsm:combobox
				optionLabelProperty="dsDivisaoCliente"
				width="29%"
				optionProperty="idDivisaoCliente"
				labelWidth="10%"
				boxWidth="140"
				property="divisaoCliente.idDivisaoCliente"
				service="lms.vendas.manterReajusteClienteAction.findDivisaoClienteCombo"
				label="divisao">
			<adsm:propertyMapping
					modelProperty="cliente.idCliente"
					criteriaProperty="cliente.idCliente" />
		</adsm:combobox>

		<%---------------------%>
		<%-- TABELA COMBOBOX --%>
		<%---------------------%>
		<adsm:combobox
				label="tabela"
				labelWidth="16%"
				optionLabelProperty="tabelaPreco.tabelaPrecoStringDescricao"
				optionProperty="idTabelaDivisaoCliente"
				property="tabelaDivisaoCliente.idTabelaDivisaoCliente"
				service="lms.vendas.manterReajusteClienteAction.findTabelaDivisaoClienteCombo"
				width="45%"
				boxWidth="330"
				onchange="return onChangeTabelaPreco();"
				onDataLoadCallBack="tabelaDivisaoClienteDataLoad">
			<adsm:propertyMapping
					criteriaProperty="divisaoCliente.idDivisaoCliente"
					modelProperty="divisaoCliente.idDivisaoCliente"/>
		</adsm:combobox>

		<%-------------------%>
		<%-- TABELA LOOKUP --%>
		<%-------------------%>
		<adsm:lookup
				label="tabela"
				property="tabelaPreco"
				service="lms.vendas.manterReajusteClienteAction.findTabelaPrecoLookup"
				action="/tabelaPrecos/manterTabelasPreco"
				idProperty="idTabelaPreco"
				criteriaProperty="tabelaPrecoString"
				onclickPicker="onclickPickerLookupTabelaPreco()"
				onDataLoadCallBack="dataLoadTabelaPreco"
				onPopupSetValue="validaTabelaPreco"
				dataType="text"
				size="10"
				maxLength="9"
				width="45%"
				labelWidth="16%">
			<adsm:propertyMapping
					modelProperty="dsDescricao"
					relatedProperty="tabelaPreco.dsDescricao"/>

			<adsm:textbox
					dataType="text"
					property="tabelaPreco.dsDescricao"
					size="30"
					maxLength="30"
					disabled="true"/>
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:button
					id="btnConsultar"
					caption="consultar"
					onclick="return validateSearch();"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
			idProperty="idReajusteCliente"
			property="reajusteCliente"
			defaultOrder="nrReajuste"
			gridHeight="170"
			unique="true"
			rows="11">

		<adsm:gridColumn
				dataType="JTDate"
				title="data"
				property="dtInicioVigencia"
				width="15%" />

		<adsm:gridColumn
				title="numeroReajuste"
				property="nrReajuste"
				width="15%" />

		<adsm:gridColumn
				title="cliente"
				property="cliente.pessoa.nmPessoa"
				width="20%" />

		<adsm:gridColumn
				title="tabela"
				property="tabelaPrecoString"
				width="10%" />

		<adsm:gridColumn
				dataType="currency"
				title="percentualReajusteTratado"
				property="pcReajusteAcordado"
				width="15%" />

		<adsm:gridColumn
				isDomain="true"
				title="situacao"
				property="tpSituacaoAprovacao"
				width="10%" />

		<adsm:gridColumn
				title="efetivado"
				property="blEfetivado"
				renderMode="image-check"
				width="10%" />

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	function initWindow(eventObj) {
		setDisabled("btnConsultar", false);
		if (eventObj.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			if(tabGroup != undefined) {
				var telaCad = tabGroup.getTab("cad").tabOwnerFrame;
			}
		}
	}

	function myOnPageLoad() {
		var url = new URL(parent.location.href);
		if (url.parameters != undefined
				&& url.parameters.idProcessoWorkflow != undefined
				&& url.parameters.idProcessoWorkflow != '') {
			setDisabled("formList", true);
			getTabGroup(document).selectTab("cad", "tudoMenosNulo", true);
		} else {
			onPageLoad();
			validateSession();
		}
	}

	/** Somente Matriz pode consultar reajustes de outras Filiais */
	function validateSession() {
		var sdo = createServiceDataObject("lms.vendas.manterReajusteClienteAction.validateSession","validateSession");
		xmit({serviceDataObjects:[sdo]});
	}
	function validateSession_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return;
		}
		if(data.isMatriz != "true") {
			setElementValue("filial.idFilial", data.idFilial);
			setElementValue("filial.sgFilial", data.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data.pessoa.nmFantasia);
			getElement("filial.idFilial").masterLink = "true";
			getElement("filial.sgFilial").masterLink = "true";
			getElement("filial.pessoa.nmFantasia").masterLink = "true";

			setDisabled("filial.idFilial", true);
			setFocusOnFirstFocusableField();
		}
	}

	function onChangeTabelaPreco() {
		setElementValue("idTabelaDivisaoCliente", getElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente"));
		return true;
	}

	function tabelaDivisaoClienteDataLoad_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return;
		}
		tabelaDivisaoCliente_idTabelaDivisaoCliente_cb(data);
		if (data.length == 1) {
			setElementValue("tabelaDivisaoCliente.idTabelaDivisaoCliente", data[0].idTabelaDivisaoCliente);
		}
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

	function onclickPickerLookupTabelaPreco() {
		var tabelaPrecoString = getElementValue("tabelaPreco.tabelaPrecoString");
		if(tabelaPrecoString != "")	{
			setElementValue("tabelaPreco.tabelaPrecoString","");
		}
		lookupClickPicker({e:document.forms[0].elements['tabelaPreco.idTabelaPreco']});

		if(getElementValue("tabelaPreco.tabelaPrecoString")=='' && tabelaPrecoString != "")	{
			setElementValue("tabelaPreco.tabelaPrecoString",tabelaPrecoString);
		}
	}

	function dataLoadTabelaPreco_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		if (data != undefined && data.length == 1) {
			if (!validaTabelaPreco(data[0])) {
				return false;
			}
		}
		return tabelaPreco_tabelaPrecoString_exactMatch_cb(data);
	}

	/* Valida Tipo e Subtipo das tabelas de Pre�o */
	function validaTabelaPreco(data) {
		if(data == undefined) {
			return false;
		}

		var errorType = 0;
		var tpTipoTabelaPreco = data.tipoTabelaPreco.tpTipoTabelaPreco.value;
		if (tpTipoTabelaPreco != "R"
				&& tpTipoTabelaPreco != "A"
				&& tpTipoTabelaPreco != "M"
				&& tpTipoTabelaPreco != "T") {
			errorType = 1;
		}

		var tpSubtipoTabelaPreco = data.subtipoTabelaPreco.tpSubtipoTabelaPreco;
		if (tpSubtipoTabelaPreco == "F" && errorType == 0) {
			errorType = 2;
		}
		if(errorType > 0) {
			resetValue("tabelaPreco.idTabelaPreco");
			resetValue("tabelaPreco.tabelaPrecoString");
			resetValue("tabelaPreco.dsDescricao");
			(errorType == 1) ? alertI18nMessage("LMS-01034") : alertI18nMessage("LMS-01162");;
			setFocus("tabelaPreco.tabelaPrecoString", false);
			return false;
		}
		return true;
	}

	function validateSearch() {
		if(!hasValue(getElementValue("filial.idFilial"))
				&& !hasValue(getElementValue("nrReajuste"))
				&& !hasValue(getElementValue("cliente.idCliente"))
				&& !hasValue(getElementValue("tabelaPreco.idTabelaPreco"))) {
			alertI18nMessage("LMS-00055");
			setFocusOnFirstFocusableField();
			return false;
		}
		reajusteClienteGridDef.executeSearch(buildFormBeanFromForm(document.forms[0]));
		return true;
	}
</script>