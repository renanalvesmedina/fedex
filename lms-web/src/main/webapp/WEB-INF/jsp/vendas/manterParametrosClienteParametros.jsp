<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.vendas.manterParametrosClienteAction"
>
	<adsm:i18nLabels>
		<adsm:include key="LMS-01030"/>
		<adsm:include key="LMS-01040"/>
		<adsm:include key="LMS-01045"/>
		<adsm:include key="LMS-01050"/>
		<adsm:include key="LMS-01052"/>
		<adsm:include key="LMS-01060"/>
		<adsm:include key="LMS-01069"/>
		<adsm:include key="LMS-01070"/>
		<adsm:include key="LMS-01073"/>
		<adsm:include key="LMS-01074"/>
		<adsm:include key="LMS-01078"/>
		<adsm:include key="LMS-01079"/>
		<adsm:include key="LMS-01080"/>
		<adsm:include key="LMS-01155"/>
		<adsm:include key="pagaPesoExcedente"/>
	</adsm:i18nLabels>

	<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
	<adsm:hidden property="tpAcesso" value="A"/>

	<adsm:form
		action="/vendas/manterParametrosCliente"
		height="370"
		idProperty="idParametroCliente"
		onDataLoadCallBack="dlc">

		<%-- CLIENTE LOOKUP --%>
		<adsm:complement
			label="cliente"
			labelWidth="18%"
			required="true"
			width="43%"
			separator="branco">
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao"
				serializable="false"
				size="20"/>
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa"
				serializable="false"
				size="30"/>
		</adsm:complement>

		<%-- DIVISAO COMBO --%>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="divisao"
			labelWidth="13%"
			property="tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"
			required="true"
			serializable="false"
			size="20"
			width="20%"	/>

		<%-- TABELA LOOKUP --%>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.idTabelaPreco"/>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco" serializable="false"/>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" serializable="false"/>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="tabela"
			labelWidth="18%"
			property="tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao"
			required="true"
			serializable="false"
			size="60"
			width="82%"/>

		<%-- ROTAS TEXT--%>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="origem"
			labelWidth="18%"
			property="rotaPreco.origemString"
			serializable="false"
			size="80"
			width="78%"/>
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="destino"
			labelWidth="18%"
			property="rotaPreco.destinoString"
			serializable="false"
			size="80"
			width="78%"/>

		<%-- ESPECIFICACAO ROTA TEXT AREA --%>
		<adsm:textarea
			columns="81"
			label="especificacaoRota"
			labelWidth="18%"
			maxLength="500"
			property="dsEspecificacaoRota"
			rows="2"
			width="78%"/>

		<%-- PARCEIRA DE REDESPACHO LOOKUP --%>
		<adsm:lookup
			label="parceiraRedespacho"
			property="clienteByIdClienteRedespacho"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.vendas.manterParametrosClienteAction.findClienteLookup"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			exactMatch="true"
			labelWidth="18%"
			maxLength="20"
			size="20"
			width="78%">
			<adsm:propertyMapping
				criteriaProperty="tpSituacao"
				modelProperty="tpSituacao"/>
			<adsm:propertyMapping
				relatedProperty	="clienteByIdClienteRedespacho.pessoa.nmPessoa"
				modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="clienteByIdClienteRedespacho.pessoa.nmPessoa"
				serializable="false"
				size="30"/>
		</adsm:lookup>
		
		<%-- filialByIdFilialMercurioRedespacho.pessoa.nmFantasia DE REDESPACHO LOOKUP --%>
		<adsm:lookup
			action="/municipios/manterFiliais"
			criteriaProperty="sgFilial"
			dataType="text"
			exactMatch="false"
			idProperty="idFilial"
			label="filialRedespacho"
			labelWidth="18%"
			maxLength="3"
			minLengthForAutoPopUpSearch="3"
			property="filialByIdFilialMercurioRedespacho"
			service="lms.municipios.filialService.findLookupBySgFilial"
			size="5"
			width="78%">
			<adsm:propertyMapping modelProperty="tpAcesso" criteriaProperty="tpAcesso"/>
			<adsm:propertyMapping
				relatedProperty="filialByIdFilialMercurioRedespacho.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="filialByIdFilialMercurioRedespacho.pessoa.nmFantasia"
				serializable="false"
				size="30"/>
		</adsm:lookup>

		<%-- SITUACAO COMBO --%>
		<adsm:combobox
			domain="DM_STATUS"
			label="situacao"
			labelWidth="18%"
			property="tpSituacaoParametro"
			required="true"
			width="43%"/>

		<%-- MOEDA --%>
		<adsm:textbox
			dataType="text"
			disabled="true"
			property="tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo"
			serializable="false"
			label="moeda"
			size="9"
			labelWidth="13%"
			width="26%"
			required="true"/>

		<%-- VIGENCIA --%>
		<adsm:range
			label="vigencia"
			labelWidth="18%"
			width="78%">
			<adsm:textbox
				dataType="JTDate"
				property="dtVigenciaInicial"
				required="true"
				smallerThan="dtVigenciaFinal"/>
			<adsm:textbox
				biggerThan="dtVigenciaInicial"
				dataType="JTDate"
				property="dtVigenciaFinal"/>
		</adsm:range>

		<%-- Include do JSP que contem os campos dos parametros do cliente --%>
		<%@ include file="parametroCliente.jsp" %>

		<adsm:hidden property="tabelaDivisaoCliente.idTabelaDivisaoCliente" />
		<adsm:hidden property="zonaByIdZonaOrigem.idZona" />
		<adsm:hidden property="paisByIdPaisOrigem.idPais" />
		<adsm:hidden property="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" />
		<adsm:hidden property="filialByIdFilialOrigem.idFilial" />
		<adsm:hidden property="municipioByIdMunicipioOrigem.idMunicipio"/>
		<adsm:hidden property="aeroportoByIdAeroportoOrigem.idAeroporto"/>
		<adsm:hidden property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio"/>
		<adsm:hidden property="zonaByIdZonaDestino.idZona"/>
		<adsm:hidden property="paisByIdPaisDestino.idPais"/>
		<adsm:hidden property="unidadeFederativaByIdUfDestino.idUnidadeFederativa"/>
		<adsm:hidden property="filialByIdFilialDestino.idFilial"/>
		<adsm:hidden property="municipioByIdMunicipioDestino.idMunicipio"/>
		<adsm:hidden property="aeroportoByIdAeroportoDestino.idAeroporto"/>
		<adsm:hidden property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio"/>
		<adsm:hidden property="grupoRegiaoOrigem.idGrupoRegiao"/>
		<adsm:hidden property="grupoRegiaoDestino.idGrupoRegiao"/>
		<adsm:buttonBar>			
			<adsm:storeButton caption="salvarParametros"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript" src="../lib/parametroCliente.js"></script>
<script language="javascript" type="text/javascript">
	function limpa() {
		setElementValue("idParametroCliente","");
		unsetMasterLink("tabelaDivisaoCliente.idTabelaDivisaoCliente");
		unsetMasterLink("zonaByIdZonaOrigem.idZona");
		unsetMasterLink("paisByIdPaisOrigem.idPais");
		unsetMasterLink("unidadeFederativaByIdUfOrigem.idUnidadeFederativa");
		unsetMasterLink("filialByIdFilialOrigem.idFilial");
		unsetMasterLink("municipioByIdMunicipioOrigem.idMunicipio");
		unsetMasterLink("aeroportoByIdAeroportoOrigem.idAeroporto");
		unsetMasterLink("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio");
		unsetMasterLink("grupoRegiaoOrigem.idGrupoRegiao");

		unsetMasterLink("zonaByIdZonaDestino.idZona");
		unsetMasterLink("paisByIdPaisDestino.idPais");
		unsetMasterLink("unidadeFederativaByIdUfDestino.idUnidadeFederativa");
		unsetMasterLink("filialByIdFilialDestino.idFilial");
		unsetMasterLink("municipioByIdMunicipioDestino.idMunicipio");
		unsetMasterLink("aeroportoByIdAeroportoDestino.idAeroporto");
		unsetMasterLink("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio");
		unsetMasterLink("grupoRegiaoDestino.idGrupoRegiao");

		unsetMasterLink("idParametroCliente");
		unsetMasterLink("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao");
		unsetMasterLink("tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa");
		unsetMasterLink("tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente");
		unsetMasterLink("tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco");
		unsetMasterLink("tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco");

		unsetMasterLink("tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao");
		unsetMasterLink("tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo");

		resetValue(this.document);
	}

	function unsetMasterLink(nome) {
		var obj = document.getElementById(nome);
		obj.masterLink = "false";
	}

	/**
	* Valida os parametros do cliente e os dados de tab CAD. Se os dados de CAD não são válidos, coloca em CAD em foco.
	*/
	function validaParametrosCliente() {
		var frame = parent.document.frames["cad_iframe"];
		if(frame.validateParametrosCad(this.document) == true) {
			return validateParametroCliente();
		}
		var tabGroup = getTabGroup(this.document);
		tabGroup.selectTab("cad",{name:"tab_click"});
		return false;
	}

	/**
	* Se os dados são válidos, habilita as abas de taxas e de generalidades.
	*/
	function validateTab() {
		return (validateTabScript(document.forms) && validaParametrosCliente());
	}

	/**
	* Habilita as abas de taxas e generalidades
	*/
	function habilitarTaxasGeneralidades() {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("gen", false);
		tabGroup.setDisabledTab("tax", false);
	}

	function initWindow(eventObj) {
		if (eventObj.name == "continuar") {
			var idParametroCliente = getElementValue("idParametroCliente");
			if(idParametroCliente=="") {
				setInitialValuesInsertMode();
			}
		}
		if( eventObj.name == "continuar" || eventObj.name == "tab_click" ) {
			copyValuesFromCad();

			var frame = parent.document.frames["cad_iframe"];
			if(validateForm(frame.getForm()) != true) {
				var tabGroup = getTabGroup(this.document);
				tabGroup.selectTab("cad",{name:"tab_click"});
			}
			findLimitesDescontos();
		} else if(eventObj.name == "storeButton" ) {
			var tabGroup = getTabGroup(this.document);
			tabGroup.getTab('cad').setChanged(false); 

			var frame = parent.document.frames["cad_iframe"];
			var idParametroCliente = getElementValue("idParametroCliente");
			if(idParametroCliente != "") {
				frame.setIdParametroCliente(idParametroCliente);
				habilitarTaxasGeneralidades();
			}
		}
	}

	function setInitialValuesInsertMode() {
		setElementValue("tpSituacaoParametro", "A");
		setDefaultValues();

		var sdos = new Array();
		sdos.push(createServiceDataObject("lms.vendas.manterParametrosClienteAction.getDataHoje", "setDataAtual", {}));
		sdos.push(createServiceDataObject("lms.vendas.manterParametrosClienteAction.findPercentualTotalFrete", "pcCobrancaReentrega",{param:"reentrega"}));
		sdos.push(createServiceDataObject("lms.vendas.manterParametrosClienteAction.findPercentualTotalFrete", "pcCobrancaDevolucoes",{param:"devolucoes"}));
		xmit({serviceDataObjects:sdos});
	}

	function setDataAtual_cb(data,error) {
		if(error != undefined) {
			alert(error);
			return;
		}
		setElementValue("dtVigenciaInicial", getNestedBeanPropertyValue(data, "dataAtual"));
	}

	function pcCobrancaReentrega_cb(data, errMsg) {
		var value = getNestedBeanPropertyValue(data, "result");
		setFormattedValue("pcCobrancaReentrega", value);
	}

	function pcCobrancaDevolucoes_cb(data, errMsg) {
		var value = getNestedBeanPropertyValue(data, "result");
		setFormattedValue("pcCobrancaDevolucoes", value);
	}

	function copyValuesFromCad() {
		var frame = parent.document.frames["cad_iframe"];
		var dados = frame.getValuesFromCad();
		copyValues(dados);
	}

	function copyValuesAndSetMasterLink(dados, nome) {
		var obj = document.getElementById(nome);
		var value = getNestedBeanPropertyValue(dados, nome);
		setElementValue(obj, value);
		obj.masterLink = "true";
	}

	function copyValues(dados) {
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.idTabelaDivisaoCliente");
		copyValuesAndSetMasterLink(dados,"zonaByIdZonaOrigem.idZona");
		copyValuesAndSetMasterLink(dados,"paisByIdPaisOrigem.idPais");
		copyValuesAndSetMasterLink(dados,"unidadeFederativaByIdUfOrigem.idUnidadeFederativa");
		copyValuesAndSetMasterLink(dados,"filialByIdFilialOrigem.idFilial");
		copyValuesAndSetMasterLink(dados,"municipioByIdMunicipioOrigem.idMunicipio");
		copyValuesAndSetMasterLink(dados,"aeroportoByIdAeroportoOrigem.idAeroporto");
		copyValuesAndSetMasterLink(dados,"tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio");
		copyValuesAndSetMasterLink(dados,"grupoRegiaoOrigem.idGrupoRegiao");

		copyValuesAndSetMasterLink(dados,"zonaByIdZonaDestino.idZona");
		copyValuesAndSetMasterLink(dados,"paisByIdPaisDestino.idPais");
		copyValuesAndSetMasterLink(dados,"unidadeFederativaByIdUfDestino.idUnidadeFederativa");
		copyValuesAndSetMasterLink(dados,"filialByIdFilialDestino.idFilial");
		copyValuesAndSetMasterLink(dados,"municipioByIdMunicipioDestino.idMunicipio");
		copyValuesAndSetMasterLink(dados,"aeroportoByIdAeroportoDestino.idAeroporto");
		copyValuesAndSetMasterLink(dados,"tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio");
		copyValuesAndSetMasterLink(dados,"grupoRegiaoDestino.idGrupoRegiao");		

		copyValuesAndSetMasterLink(dados,"idParametroCliente");
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao");
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa");
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente");
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.tabelaPreco.idTabelaPreco");
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco");
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco");

		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao");
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo");

		setElementValue("rotaPreco.origemString", "");
		var rota = getDescricaoRota(dados, "Origem");
		if(rota != "") {
			var obj = getElement("rotaPreco.origemString");
			setElementValue("rotaPreco.origemString", rota);
			obj.masterLink = "true";
		}

		setElementValue("rotaPreco.destinoString", "");
		rota = getDescricaoRota(dados, "Destino");
		if(rota != "") {
			var obj = document.getElementById("rotaPreco.destinoString");
			setElementValue("rotaPreco.destinoString", rota);
			obj.masterLink = "true";
		}
	}

	function getDescricaoRota(dados, tipo) {
		var rota = "";
		rota += getParteRota(dados, "zonaByIdZona" + tipo + ".dsZona");
		rota += getParteRota(dados, "paisByIdPais" + tipo + ".nmPais");
		rota += getParteRota(dados, "unidadeFederativaByIdUf" + tipo + ".siglaDescricao");
		rota += getParteRota(dados, "filialByIdFilial" + tipo + ".sgFilial");
		rota += getParteRota(dados, "municipioByIdMunicipio" + tipo + ".municipio.nmMunicipio");
		rota += getParteRota(dados, "aeroportoByIdAeroporto" + tipo + ".sgAeroporto");
		rota += getParteRota(dados, "tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".dsTipoLocalizacaoMunicipio");
		rota += getParteRota(dados, "grupoRegiao" + tipo + ".dsGrupoRegiao");		
		
		if(rota != "") {
			var i = rota.lastIndexOf("-");
			rota = rota.substring(0, i);
		}
		return rota;
	}

	function getParteRota(dados, parte) {
		value = getNestedBeanPropertyValue(dados, parte);
		return ((value == "") ? "" : value + "-");
	}
</script>