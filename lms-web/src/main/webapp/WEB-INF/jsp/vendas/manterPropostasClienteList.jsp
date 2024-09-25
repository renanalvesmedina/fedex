<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterPropostasClienteAction"
			 onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/vendas/manterPropostasClienteParam">

		<adsm:hidden property="_idTabelaDivisaoCliente" serializable="false" />
		<adsm:hidden property="_idDivisaoCliente" serializable="false" />
		<adsm:hidden property="simulacao.idSimulacao" />
		<adsm:hidden property="servico.idServico" />
		<adsm:hidden property="divisaoCliente.idDivisaoCliente" />
		<adsm:hidden property="tabelaPreco.idTabelaPreco" />
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
					maxLength="9" />

			<adsm:textbox
					dataType="text"
					disabled="true"
					property="tabelaPreco.dsDescricao"
					serializable="false"
					size="44" />

		</adsm:complement>

		<%-------------------%>
		<%-- SERVICO COMBO --%>
		<%-------------------%>
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

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton
					id="btnConsultar"
					callbackProperty="parametrosList"/>
			<adsm:button
					caption="voltar"
					id="btnVoltar"
					onclick="back();"
					disabled="false"/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid
			idProperty="idParametroCliente"
			property="parametrosList"
			defaultOrder="nrSimulacao"
			gridHeight="220"
			scrollBars="horizontal"
			unique="true"
			rows="12"
			onRowClick="rowClick();">

		<adsm:gridColumn
				title="zonaOrigem"
				property="zonaByIdZonaOrigem.dsZona"
				width="150" />

		<adsm:gridColumn
				title="paisOrigem"
				property="paisByIdPaisOrigem.nmPais"
				width="150" />

		<adsm:gridColumn
				title="ufOrigem2"
				property="unidadeFederativaByIdUfOrigem.sgUnidadeFederativa"
				width="50" />

		<adsm:gridColumn
				title="filialOrigem"
				property="filialByIdFilialOrigem.sgFilial"
				width="50" />

		<adsm:gridColumn
				title="municipioOrigem"
				property="municipioByIdMunicipioOrigem.nmMunicipio"
				width="200" />

		<adsm:gridColumn
				title="aeroportoOrigem"
				property="aeroportoByIdAeroportoOrigem.sgAeroporto"
				width="80" />

		<adsm:gridColumn
				title="localizacaoOrigem"
				property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.dsTipoLocalizacaoMunicipio"
				width="100" />

		<adsm:gridColumn
				title="grupoRegiaoOrigem"
				property="grupoRegiaoOrigem.dsGrupoRegiao"
				width="110" />

		<adsm:gridColumn
				title="zonaDestino"
				property="zonaByIdZonaDestino.dsZona"
				width="150" />

		<adsm:gridColumn
				title="paisDestino"
				property="paisByIdPaisDestino.nmPais"
				width="150" />

		<adsm:gridColumn
				title="ufDestino2"
				property="unidadeFederativaByIdUfDestino.sgUnidadeFederativa"
				width="50" />

		<adsm:gridColumn
				title="filialDestino"
				property="filialByIdFilialDestino.sgFilial"
				width="50" />

		<adsm:gridColumn
				title="municipioDestino"
				property="municipioByIdMunicipioDestino.nmMunicipio"
				width="200" />

		<adsm:gridColumn
				title="aeroportoDestino"
				property="aeroportoByIdAeroportoDestino.sgAeroporto"
				width="80" />

		<adsm:gridColumn
				title="localizacaoDestino"
				property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.dsTipoLocalizacaoMunicipio"
				width="100" />

		<adsm:gridColumn
				title="grupoRegiaoDestino"
				property="grupoRegiaoDestino.dsGrupoRegiao"
				width="110" />

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>

	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	<!--
	var _disabeAll = false;

	document.getElementById("servico.idServico").masterLink = "true";
	document.getElementById("servico.dsServico").masterLink = "true";
	document.getElementById("divisaoCliente.idDivisaoCliente").masterLink = "true";
	document.getElementById("divisaoCliente.dsDivisaoCliente").masterLink = "true";

	/***************************************/
	/* ON CLICK PICKER TABELA PRECO LOOKUP */
	/***************************************/
	function onclickPickerLookupTabelaPreco() {
		var tabelaPrecoString = getElementValue("tabelaPreco.tabelaPrecoString");
		if(tabelaPrecoString != "")
		{
			setElementValue("tabelaPreco.tabelaPrecoString","");
		}
		lookupClickPicker({e:document.forms[0].elements['tabelaPreco.idTabelaPreco']});

		if(getElementValue("tabelaPreco.tabelaPrecoString")=='' && tabelaPrecoString != "")
		{
			setElementValue("tabelaPreco.tabelaPrecoString",tabelaPrecoString);
		}
	}

	/***************/
	/* INIT WINDOW */
	/***************/
	function initWindow(eventObj) {
//	oldAlert(eventObj.name);
		changeAbasStatus(true);
		if (_disabeAll == true) {
			changeAbaCadStatus(true);
		}

		top._consulta = true;

		setDisabled("btnVoltar", false);
	}

	function changeAbasStatus(status) {
		var tabGroup = getTabGroup(this.document);
		if(tabGroup != undefined) {
			tabGroup.setDisabledTab("param", status);
			tabGroup.setDisabledTab("tax", status);
			tabGroup.setDisabledTab("gen", status);
		}
	}

	function changeAbaCadStatus(status) {
		var tabGroup = getTabGroup(this.document);
		if(tabGroup != undefined) {
			tabGroup.setDisabledTab("cad", status);
		}
	}

	function findDadosDefault() {
		var service = "lms.vendas.manterPropostasClienteAction.findDescricaoDados";
		var data = {
			servico : {
				idServico : getElementValue("servico.idServico")
			},
			divisaoCliente : {
				idDivisaoCliente : getElementValue("divisaoCliente.idDivisaoCliente")
			}
		}
		var sdo = createServiceDataObject(service, "findDadosDefault", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function findDadosDefault_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data.servico != undefined) {
			setElementValue("servico.dsServico", data.servico.dsServico);
		}
		if (data.divisaoCliente != undefined) {
			setElementValue("divisaoCliente.dsDivisaoCliente", data.divisaoCliente.dsDivisaoCliente);
		}
		changeFieldsStatus();
	}

	function myOnPageLoad_cb(data, error) {
		onPageLoad_cb(data, error);
		findDadosDefault();

		if (getElementValue("idProcessoWorkflow") != "") {
			setDisabled(document, true);
			setDisabled("btnConsultar", false);
		}
	}

	function changeFieldsStatus() {
		var service = "lms.vendas.manterPropostasClienteAction.disableFields";
		var sdo = createServiceDataObject(service, "changeFieldsStatus");
		xmit({serviceDataObjects:[sdo]});
	}

	function changeFieldsStatus_cb(data, error) {
		if (data.disableAll == "true") {
			changeAbaCadStatus(true);
			_disabeAll = true;
		} else {
			changeAbaCadStatus(false);
		}
	}

	function back() {
		parent.top.body.backPage();
	}

	function rowClick() {
		changeAbaCadStatus(false);
	}
	-->
</script>
