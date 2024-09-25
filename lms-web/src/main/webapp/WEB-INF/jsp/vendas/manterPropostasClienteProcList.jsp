<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterPropostasClienteProcAction" onPageLoad="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-30054" />
		<adsm:include key="LMS-01163" />
		<adsm:include key="LMS-01162" />
		<adsm:include key="LMS-01033" />
	</adsm:i18nLabels>
	<adsm:form action="/vendas/manterPropostasCliente" id="formList">

		<adsm:hidden property="idProcessoWorkflow"/>
		<adsm:hidden property="_idTabelaDivisaoCliente" serializable="false" />
		<adsm:hidden property="_idDivisaoCliente" serializable="false" />
		<adsm:hidden property="tpProposta" value="P" serializable="false" />
		<adsm:hidden property="blTabelaFob" value="N" serializable="false" />

		<adsm:lookup
				label="filial"
				labelWidth="16%"
				width="45%"
				property="filial"
				idProperty="idFilial"
				criteriaProperty="sgFilial"
				action="/municipios/manterFiliais"
				service="lms.vendas.manterPropostasClienteProcAction.findLookupFilial"
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
		<%-- NR PROPOSTA TEXT --%>
		<%----------------------%>
		<adsm:textbox
				label="numeroProposta"
				property="nrSimulacao"
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
				service="lms.vendas.manterPropostasClienteAction.findClienteLookup"
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
				service="lms.vendas.manterPropostasClienteAction.findDivisaoCombo"
				label="divisao">

			<adsm:propertyMapping
					modelProperty="cliente.idCliente"
					criteriaProperty="cliente.idCliente" />
		</adsm:combobox>

		<%-------------------%>
		<%-- TABELA LOOKUP --%>
		<%-------------------%>
		<adsm:lookup
				label="tabela"
				property="tabelaPreco"
				service="lms.vendas.manterPropostasClienteAction.findLookupTabelaPreco"
				action="/tabelaPrecos/manterTabelasPreco"
				idProperty="idTabelaPreco"
				criteriaProperty="tabelaPrecoString"
				onclickPicker="onclickPickerLookupTabelaPreco()"
				onDataLoadCallBack="dataLoadTabelaPreco"
				onPopupSetValue="validateSubtipoTabelaPreco"
				dataType="text"
				size="10"
				maxLength="9"
				width="45%"
				labelWidth="16%">

			<adsm:propertyMapping
					criteriaProperty="tpProposta" relatedProperty="tpProposta"/>
			<adsm:propertyMapping
					modelProperty="dsDescricao"
					relatedProperty="tabelaPreco.dsDescricao"/>
			<adsm:propertyMapping
					criteriaProperty="blTabelaFob"
					modelProperty="blTabelaFob"/>

			<adsm:textbox dataType="text" property="tabelaPreco.dsDescricao"
						  size="30" maxLength="30" disabled="true"/>
		</adsm:lookup>

		<%-------------------%>
		<%-- SERVICO COMBO --%>
		<%-------------------%>
		<adsm:combobox
				boxWidth="200"
				label="servico"
				onlyActiveValues="false"
				optionLabelProperty="dsServico"
				optionProperty="idServico"
				property="servico.idServico"
				service="lms.vendas.manterPropostasClienteAction.findServicoCombo"
				width="25%"
				labelWidth="10%"/>

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
				maxLength="7"
				property="tabelaPrecoFob"
				service="lms.vendas.manterPropostasClienteAction.findLookupTabelaPreco"
				onclickPicker="onclickPickerLookupTabelaPrecoFob()"
				onPopupSetValue="validateSubtipoTabelaPrecoFob"
				onDataLoadCallBack="lookupTabelaPrecoFob"
				size="10"
				labelWidth="16%"
				width="45%">

			<adsm:propertyMapping
					criteriaProperty="tpProposta"
					relatedProperty="tpProposta"/>
			<adsm:propertyMapping
					relatedProperty="tabelaPrecoFob.dsDescricao"
					modelProperty="dsDescricao" />

			<adsm:textbox
					dataType="text"
					disabled="true"
					property="tabelaPrecoFob.dsDescricao"
					size="30" />
		</adsm:lookup>

		<%-------------------%>
		<%-- SITUACAO COMBO --%>
		<%-------------------%>
		<adsm:combobox
				boxWidth="200"
				property="tpSituacaoAprovacao"
				domain="DM_SITUACAO_SIMULACAO"
				renderOptions="true"
				label="situacao"
				width="25%"
				labelWidth="10%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="simulacoesList"/>
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid
			idProperty="idSimulacao"
			property="simulacoesList"
			defaultOrder="nrSimulacao"
			gridHeight="170"
			unique="true"
			rows="11">

		<adsm:gridColumn
				dataType="JTDate"
				title="data"
				property="dtSimulacao"
				width="15%" />
		<adsm:gridColumn
				title="numeroProposta"
				property="nrProposta"
				width="15%" />
		<adsm:gridColumn
				title="cliente"
				property="clienteByIdCliente.pessoa.nmPessoa"
				width="30%" />
		<adsm:gridColumn
				title="tabela"
				property="tabelaPrecoString"
				width="10%" />
		<adsm:gridColumn
				title="tabelaFob"
				property="tabelaPrecoFob.tabelaPrecoString"
				width="10%" />
		<adsm:gridColumn
				isDomain="true"
				title="situacao"
				property="tpSituacaoAprovacao"
				width="10%" />
		<adsm:gridColumn
				title="efetivada"
				property="blEfetivada"
				renderMode="image-check"
				width="10%" />

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>

	</adsm:grid>
</adsm:window>
<script type="text/javascript">

	<!--
	/***************************************/
	/* ON CLICK PICKER TABELA PRECO LOOKUP */
	/***************************************/
	function onclickPickerLookupTabelaPreco() {
		var tabelaPrecoString = getElementValue("tabelaPreco.tabelaPrecoString");
		if(tabelaPrecoString != "")	{
			setElementValue("tabelaPreco.tabelaPrecoString","");
		}
		setElementValue("tpProposta", "P");
		lookupClickPicker({e:document.forms[0].elements['tabelaPreco.idTabelaPreco']});

		if(getElementValue("tabelaPreco.tabelaPrecoString")=='' && tabelaPrecoString != "")	{
			setElementValue("tabelaPreco.tabelaPrecoString",tabelaPrecoString);
		}
	}

	/***************/
	/* INIT WINDOW */
	/***************/
	function initWindow(eventObj) {
		changeAbasStatus(true);

		if (eventObj.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			if(tabGroup != undefined) {
				var telaCad = tabGroup.getTab("cad").tabOwnerFrame;
				telaCad.cleanTabs();
			}
		}

		var service = "lms.vendas.manterPropostasClienteAction.reconfiguraSessao";
		var sdo = createServiceDataObject(service);
		xmit({serviceDataObjects:[sdo]});

		top._consulta = true;
		top._consultaFormalidades = true;
	}

	function changeAbasStatus(status) {
		var tabGroup = getTabGroup(this.document);
		if(tabGroup != undefined) {
			tabGroup.setDisabledTab("form", status);
			tabGroup.setDisabledTab("resumo", status);
			// LMS-6172
			tabGroup.setDisabledTab("anexo", status);
			// LMS-6191
			tabGroup.setDisabledTab("historico", status);
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

	function dataLoadTabelaPreco_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		if (data != undefined && data.length == 1) {
			if (validaTabelaPreco(data[0].tipoTabelaPreco.tpTipoTabelaPreco.value)) {
				return tabelaPreco_tabelaPrecoString_exactMatch_cb(data);
			}
			if (!validateSubtipoTabelaPreco(data[0])) {
				resetValue("tabelaPreco.idTabelaPreco");
				setFocus("tabelaPreco.tabelaPrecoString", false);
				return false;
			}
			return false;
		}
		return tabelaPreco_tabelaPrecoString_exactMatch_cb(data);
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

	function validaTabelaPreco(tpTipoTabelaPreco) {
		if (tpTipoTabelaPreco == "C" || tpTipoTabelaPreco == "D") {
			setElementValue("tabelaPreco.idTabelaPreco", "");
			setElementValue("tabelaPreco.tabelaPrecoString", "");
			setElementValue("tabelaPreco.dsDescricao", "");
			alertI18nMessage("LMS-30054");
			setFocus("tabelaPreco.tabelaPrecoString", false);
			return false;
		}
		return true;
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


	function lookupTabelaPrecoFob_cb(data, error) {
		var result = tabelaPrecoFob_tabelaPrecoString_exactMatch_cb(data);
		if (data.length == 1 && !validateSubtipoTabelaPrecoFob(data[0])) {
			resetValue("tabelaPrecoFob.idTabelaPreco");
			setFocus("tabelaPrecoFob.tabelaPrecoString", false);
		}
		return result;
	}
	-->
</script>
