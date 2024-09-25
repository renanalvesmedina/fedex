<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.vendas.cotacaoFreteViaWebAction" onPageLoadCallBack="myPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01050"/>
		<adsm:include key="LMS-01092"/>
		<adsm:include key="LMS-01027"/>
	</adsm:i18nLabels>
	<adsm:form action="/vendas/cotacaoFreteViaWeb">
		<adsm:hidden property="dimensoesOk" value="false"/>

		<adsm:lookup
			label="remetente"
			property="clienteByIdClienteRemetente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			criteriaSerializable="true"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			allowInvalidCriteriaValue="true"
			service="lms.vendas.cotacaoFreteViaWebAction.findDadosCliente"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			required="true"
			size="20"
			maxLength="20"
			labelWidth="18%"
			width="82%"
			exactMatch="true"
			onchange="return changeCliente('Remetente');"
			onDataLoadCallBack="remetente"
			onPopupSetValue="popupRemetente">
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteRemetente.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="pessoa.tpPessoa.value"
				relatedProperty="clienteByIdClienteRemetente.pessoa.tpPessoa"/>
			<adsm:propertyMapping
				modelProperty="tpCliente.value"
				relatedProperty="clienteByIdClienteRemetente.tpCliente"/>

			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteRemetente.pessoa.nmPessoa"
				size="50"
				required="true"/>
		</adsm:lookup>

		<adsm:combobox 
			property="clienteByIdClienteRemetente.pessoa.tpPessoa" 
			labelWidth="18%" 
			width="16%" 
			label="tipoPessoa" 
			domain="DM_TIPO_PESSOA"
			onchange="validateICMS('Remetente');"
			renderOptions="true"
			disabled="true"
			required="true"/>
		<adsm:hidden property="clienteByIdClienteRemetente.tpCliente"/>

		<adsm:combobox
			label="ie"
			property="clienteByIdClienteRemetente.idInscricaoEstadual"
			optionProperty="inscricaoEstadual.idInscricaoEstadual"
			optionLabelProperty="inscricaoEstadual.nrInscricaoEstadual"
			service=""
			onchange="changeInscricaoEstadual('Remetente');"
			boxWidth="110"
			labelWidth="3%"
			width="63%"
			disabled="true">
			<adsm:propertyMapping
				relatedProperty="clienteByIdClienteRemetente.tpSituacaoTributaria"
				modelProperty="tpSituacaoTributaria"/>
		</adsm:combobox>
		<adsm:hidden property="clienteByIdClienteRemetente.tpSituacaoTributaria"/>
		<adsm:hidden property="clienteByIdClienteRemetente.dsInscricaoEstadual"/>

		<adsm:lookup
			label="destinatario"
			property="clienteByIdClienteDestinatario"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			criteriaSerializable="true"
			allowInvalidCriteriaValue="true"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.vendas.cotacaoFreteViaWebAction.findDadosCliente"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			required="true"
			size="20"
			maxLength="20"
			labelWidth="18%"
			width="82%"
			exactMatch="true"
			onchange="return changeCliente('Destinatario');"
			onDataLoadCallBack="destinatario"
			onPopupSetValue="popupDestinatario">
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="pessoa.tpPessoa.value"
				relatedProperty="clienteByIdClienteDestinatario.pessoa.tpPessoa"/>
			<adsm:propertyMapping
				modelProperty="tpCliente.value"
				relatedProperty="clienteByIdClienteDestinatario.tpCliente"/>

			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteDestinatario.pessoa.nmPessoa"
				size="50"
				required="true"/>
		</adsm:lookup>

		<adsm:combobox 
			property="clienteByIdClienteDestinatario.pessoa.tpPessoa" 
			labelWidth="18%" 
			width="16%" 
			label="tipoPessoa" 
			domain="DM_TIPO_PESSOA"
			onchange="validateICMS('Destinatario');"
			renderOptions="true"
			disabled="true"
			required="true"/>
		<adsm:hidden property="clienteByIdClienteDestinatario.tpCliente"/>

		<adsm:combobox
			label="ie"
			property="clienteByIdClienteDestinatario.idInscricaoEstadual"
			optionProperty="inscricaoEstadual.idInscricaoEstadual"
			optionLabelProperty="inscricaoEstadual.nrInscricaoEstadual"
			service=""
			onchange="changeInscricaoEstadual('Destinatario');"
			boxWidth="110"
			labelWidth="3%"
			width="63%"
			disabled="true">
			<adsm:propertyMapping
				relatedProperty="clienteByIdClienteDestinatario.tpSituacaoTributaria"
				modelProperty="tpSituacaoTributaria"/>
		</adsm:combobox>
		<adsm:hidden property="clienteByIdClienteDestinatario.tpSituacaoTributaria"/>
		<adsm:hidden property="clienteByIdClienteDestinatario.dsInscricaoEstadual"/>

		<adsm:combobox
			property="tpFrete"
			domain="DM_TIPO_FRETE"
			serializable="true"
			onchange="return validateDivisaoCliente();"
			label="tipoFrete"
			labelWidth="18%"
			width="18%"
			required="true"
			defaultValue="C"
			boxWidth="90">
			<adsm:propertyMapping
				modelProperty="description"
				relatedProperty="dsTpFrete"/>
		</adsm:combobox>
		<adsm:hidden property="dsTpFrete"/>

		<adsm:combobox
			label="servico"
			property="servico.idServico"
			optionLabelProperty="dsServico" 
			onchange="return changeServico();"
			optionProperty="idServico" 
			service="lms.vendas.cotacaoFreteViaWebAction.findServicos"
			labelWidth="7%"
			width="35%" 
			required="true"
			boxWidth="220">
			<adsm:propertyMapping
				modelProperty="dsServico"
				relatedProperty="servico.dsServico"/>
			<adsm:propertyMapping
				modelProperty="tpModal"
				relatedProperty="tpModal"/>
		</adsm:combobox>
		<adsm:hidden property="servico.dsServico"/>

		<adsm:combobox
			label="modal"
			property="tpModal"
			domain="DM_MODAL"
			serializable="true"
			labelWidth="6%"
			width="16%"
			required="true"
			disabled="true"
			boxWidth="90">
			<adsm:propertyMapping
				modelProperty="description"
				relatedProperty="dsTpModal"/>
		</adsm:combobox>
		<adsm:hidden property="dsTpModal"/>

		<adsm:combobox
			label="divisao"
			property="divisaoCliente.idDivisaoCliente" 
			service="lms.vendas.cotacaoFreteViaWebAction.findDivisaoCliente"
			disabled="true"
			autoLoad="false"
			optionProperty="idDivisaoCliente"
			optionLabelProperty="dsDivisaoCliente" 
			labelWidth="18%"
			width="82%"
			onDataLoadCallBack="carregaDivisoesCliente" 
			boxWidth="120">
			<adsm:propertyMapping
				relatedProperty="divisaoCliente.dsDivisaoCliente" 
				modelProperty="dsDivisaoCliente"/>
		</adsm:combobox>
		<adsm:hidden property="divisaoCliente.dsDivisaoCliente"/>

		<adsm:lookup 
			label="cepOrigem"
			property="nrCepLookupOrigem" 
			dataType="integer"
			idProperty="nrCep" 
			labelWidth="18%"
			width="20%"
			criteriaProperty="cepCriteria"
			exactMatch="false"
			service="lms.vendas.cotacaoFreteViaWebAction.findCepLookup" 
			size="12"
			maxLength="8"
			serializable="false" 
			required="true"
			action="/configuracoes/pesquisarCEP" 
			onDataLoadCallBack="nrCepLookupOrigem"
			onPopupSetValue="popupNrCepOrigem"
			onchange="return cepChange('Origem');"
			disabled="true">
			<adsm:propertyMapping
				modelProperty="municipio.idMunicipio"
				relatedProperty="idMunicipioOrigem"/>
			<adsm:propertyMapping
				modelProperty="municipio.nmMunicipio"
				relatedProperty="nmMunicipioOrigem"/>
			<adsm:propertyMapping
				modelProperty="municipio.unidadeFederativa.pais.sgPais"
				relatedProperty="sgPaisOrigem"/>
		</adsm:lookup>
		<adsm:hidden property="idMunicipioOrigem"/>
		<adsm:hidden property="nmMunicipioOrigem"/>
		<adsm:hidden property="sgPaisOrigem"/>
		<adsm:hidden property="nrCepOrigem"/>
		<adsm:hidden property="idFilialOrigem"/>

		<adsm:textbox
			dataType="text"
			property="sgFilialOrigem"
			label="filial"
			size="5"
			maxLength="5"
			labelWidth="5%"
			width="33%"
			disabled="true">
			<adsm:textbox
				dataType="text"
				property="nmFilialOrigem"
				size="30"
				maxLength="45"
				disabled="true"/>
		</adsm:textbox>

		<adsm:textbox
			dataType="text"
			size="14"
			property="tlFilialOrigem"
			label="telefone"
			maxLength="15"
			labelWidth="8%"
			width="13%"
			disabled="true"/>		

		<adsm:lookup 
			label="cepDestino"
			property="nrCepLookupDestino" 
			dataType="integer"
			idProperty="nrCep" 
			labelWidth="18%"
			width="20%"
			criteriaProperty="cepCriteria"
			exactMatch="false"
			service="lms.vendas.cotacaoFreteViaWebAction.findCepLookup" 
			size="12"
			maxLength="8"
			serializable="false" 
			required="true"
			action="/configuracoes/pesquisarCEP" 
			onDataLoadCallBack="nrCepLookupDestino"
			onPopupSetValue="popupNrCepDestino"
			onchange="return cepChange('Destino');"
			disabled="true">
			<adsm:propertyMapping
				modelProperty="municipio.idMunicipio"
				relatedProperty="idMunicipioDestino"/>
			<adsm:propertyMapping
				modelProperty="municipio.unidadeFederativa.pais.sgPais"
				relatedProperty="sgPaisDestino" />
		</adsm:lookup>

		<adsm:hidden property="idMunicipioDestino"/>
		<adsm:hidden property="sgPaisDestino"/>
		<adsm:hidden property="nrCepDestino"/>
		<adsm:hidden property="idFilialDestino"/>
		<adsm:textbox
			dataType="text"
			property="sgFilialDestino"
			label="filial"
			size="5"
			maxLength="5"
			labelWidth="5%"
			width="33%"
			disabled="true">
			<adsm:textbox
				dataType="text"
				property="nmFilialDestino"
				size="30"
				maxLength="45"
				disabled="true"/>
		</adsm:textbox>

		<adsm:textbox
			dataType="text"
			size="14"
			property="tlFilialDestino"
			label="telefone"
			maxLength="15"
			labelWidth="8%"
			width="13%"
			disabled="true"/>

		<adsm:combobox
			property="moeda.idMoeda"
			label="valorMercadoria"
			optionProperty="idMoeda"
			optionLabelProperty="dsSimbolo"
			serializable="true"
			service="lms.vendas.cotacaoFreteViaWebAction.findMoedasPaisEnderecoPadrao"
			onDataLoadCallBack="carregaMoedas"
			autoLoad="false"
			labelWidth="18%"
			width="25%"
			boxWidth="75">
			<adsm:propertyMapping
				relatedProperty="dsMoeda"
				modelProperty="dsSimbolo"/>
			<adsm:textbox
				property="vlMercadoria"
				dataType="currency"
				onchange="return validaValor(this);"
				required="true"
				size="9"/>
		</adsm:combobox>

		<adsm:hidden property="dsMoeda"/>
		<adsm:textbox
			property="psReal"
			label="pesoMercadoria"
			dataType="weight"
			inlineMask="false"
			onchange="return validaValor(this);"
			unit="kg"
			mask="###,##0.000"
			size="10"
			labelWidth="14%"
			width="17%"
			required="true"/>

		<adsm:textbox
			property="psCubado"
			label="pesoCubado"
			dataType="weight"
			inlineMask="false"
			unit="kg"
			mask="###,##0.000"
			size="10"
			labelWidth="10%"
			width="15%"
			disabled="true"/>

		<adsm:checkbox
			property="blRemetenteContribuinte"
			label="remetenteContribuinteICMS"
			labelWidth="22%"
			width="78%"
			disabled="true"/>

		<adsm:checkbox
			property="blDestinatarioContribuinte"
			label="destinatarioContribuinteICMS"
			labelWidth="22%"
			width="78%"
			disabled="true"/>

		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="obsCotacaoFreteWebTemp" width="100%" style="border:none;"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/> 
		<adsm:label key="empMaxCotacaoFreteWebTemp" width="100%" style="border:none;"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="frase00CotacaoFreteWebTemp" width="100%" style="border:none;"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="frase01CotacaoFreteWebTemp" width="100%" style="border:none;"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="frase02CotacaoFreteWebTemp" width="100%" style="border:none;"/>
		<adsm:buttonBar>
			<adsm:button
				caption="dimensoes"
				id="dimensoesButton"
				disabled="true"
				boxWidth="80" 
				onclick="openDimensoesWeb();"/>
			<adsm:button
				caption="calcularFrete"
				id="calcularButton"
				buttonType="storeButton"
				onclick="return openCalcularFrete();"
				disabled="true"/>
			<adsm:button
				caption="limpar"
				id="cancelarButton"
				disabled="false"
				buttonType="cancelarType"
				boxWidth="90"
				onclick="cancelarCotacao();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript" src="../lib/digitarDadosCTRC.js"></script>
<script type="text/javascript" src="../lib/expedicao.js"></script>
<script language="javascript" type="text/javascript">
	/* SCRIPTS de inicialização da tela */
	getElement("moeda.idMoeda").required = "true";
	getElement("vlMercadoria").label = getElement("moeda.idMoeda").label;

	var remetente = getElement("clienteByIdClienteRemetente.pessoa.nrIdentificacao");
	var destinatario = getElement("clienteByIdClienteDestinatario.pessoa.nrIdentificacao");
	getElement("clienteByIdClienteRemetente.pessoa.nmPessoa").label = remetente.label;
	getElement("clienteByIdClienteDestinatario.pessoa.nmPessoa").label = destinatario.label;
	remetente.required = "false";
	destinatario.required = "false";

	function myPageLoad_cb() {
		initWindow();
	}
	function initWindow() {
		setDisabled("dimensoesButton", true);
		setDisabled("cancelarButton", false);
		setDisabled("calcularButton", true);
		setElementValue("dsTpFrete", getTextFromCombo("tpFrete"));
		createCotacaoInSession();
	}
	function createCotacaoInSession() {
		var sdo = createServiceDataObject("lms.vendas.cotacaoFreteViaWebAction.createCotacao");
		xmit({serviceDataObjects:[sdo]});
	}

	function clienteCallback(data, tipo) {
		var cliente = "clienteByIdCliente" + tipo;
		if(data == undefined || data.length == 0) {
			var nrId = getElementValue(cliente + ".pessoa.nrIdentificacao");
			resetValue(cliente + ".idCliente");
			setElementValue(cliente + ".pessoa.nrIdentificacao", nrId);
			setDisabled(cliente + ".pessoa.nmPessoa", false);
			setDisabled(cliente + ".pessoa.tpPessoa", false);
			setFocus(cliente + ".pessoa.nmPessoa", true);
			/* IE */
			configuraInscricaoEstadual(undefined, tipo);
		} else {
			setDisabled(cliente + ".pessoa.nmPessoa", true);
			setDisabled(cliente + ".pessoa.tpPessoa", true);
			/* IE */
			configuraInscricaoEstadual(data[0].inscricaoEstadual, tipo);
		}
	}

	function remetente_cb(data, error) {
		clienteCallback(data, "Remetente");
		clienteByIdClienteRemetente_pessoa_nrIdentificacao_exactMatch_cb(data);
		validateICMS("Remetente");
		validateDivisaoCliente();

		if(error != undefined) {
			validateTipoFrete("Remetente");
			setFocus("clienteByIdClienteRemetente.pessoa.nrIdentificacao");
			alert(error);
		}
	}

	function destinatario_cb(data, error) {
		clienteCallback(data, "Destinatario");
		clienteByIdClienteDestinatario_pessoa_nrIdentificacao_exactMatch_cb(data);
		validateICMS("Destinatario");
		validateDivisaoCliente();

		if(error != undefined) {
			validateTipoFrete("Destinatario");
			setFocus("clienteByIdClienteDestinatario.pessoa.nrIdentificacao");
			alert(error);
		}
	}

	function changeCliente(tipo) {
		var cliente = "clienteByIdCliente" + tipo;
		var nrIdentificacao = getElementValue(cliente+".pessoa.nrIdentificacao");
		if (getElementValue(cliente+".pessoa.nrIdentificacao") == "") {
			validateTipoFrete(tipo);
		}
		eval(cliente + "_pessoa_nrIdentificacaoOnChangeHandler();");
		if(nrIdentificacao.length < 5) {
			getElement(cliente+".pessoa.nrIdentificacao").previousValue = undefined;
			setFocus(cliente+".pessoa.nrIdentificacao");
		}
	}

	function configuraInscricaoEstadual(data, tipo) {
		loadInscricaoEstadual(data, tipo);

		var cliente = "clienteByIdCliente" + tipo;
		resetValue(cliente + ".tpSituacaoTributaria");
		resetValue("bl"+ tipo +"Contribuinte");
		if(!data) {
			return false;
		}
		if(data.length == 1) {
			var tpSituacaoTributaria = data[0].tpSituacaoTributaria.value;
			setElementValue(cliente + ".tpSituacaoTributaria", tpSituacaoTributaria);
			setElementValue("bl"+ tipo +"Contribuinte", tpSituacaoTributaria == "CO");
		}
		setDisabled(cliente + ".idInscricaoEstadual", (data.length == 1));
		return true;
	}
	function loadInscricaoEstadual(data, tipo) {
		var cliente = "clienteByIdCliente" + tipo;
		if(data) {
			getElement(cliente+".idInscricaoEstadual").required = "true";
			window[cliente + "_idInscricaoEstadual_cb"](data);
			if(data.length == 1) {
				setElementValue(cliente + ".idInscricaoEstadual", data[0].inscricaoEstadual.idInscricaoEstadual)
				setDisabled(cliente + ".idInscricaoEstadual", true);
			} else if(data.length > 1) {
				setDisabled(cliente + ".idInscricaoEstadual", false);
				setFocus(cliente + ".idInscricaoEstadual", true);
			}
			return true;
		}
		getElement(cliente+".idInscricaoEstadual").required = "false";
		setDisabled(cliente + ".idInscricaoEstadual", true);
		clearElement(cliente + ".idInscricaoEstadual");
		return false;
	}
	function changeInscricaoEstadual(tipo){
		comboboxChange({e:getElement("clienteByIdCliente" + tipo + ".idInscricaoEstadual")});
		validateICMS(tipo);
	}

	function getTextFromCombo(field) {
		var element = getElement(field);
		return element.options[element.selectedIndex].text;
	}

	function changeServico() {
		var servico = getElementValue("servico.idServico");
		comboboxChange({e:getElement("servico.idServico")});
		if(servico != "") {
			setDisabled("nrCepLookupOrigem.nrCep", false);
			setDisabled("nrCepLookupDestino.nrCep", false);
			setElementValue("dsTpModal", getTextFromCombo("tpModal"));
		} else {
			resetValue("nrCepLookupOrigem.nrCep");
			resetValue("idMunicipioOrigem");
			resetValue("nrCepOrigem");
			resetValue("idFilialOrigem");
			resetValue("sgFilialOrigem");
			resetValue("nmFilialOrigem");
			resetValue("tlFilialOrigem");
			setDisabled("nrCepLookupOrigem.nrCep",true);

			resetValue("nrCepLookupDestino.nrCep");
			resetValue("idMunicipioDestino");
			resetValue("nrCepDestino");
			resetValue("idFilialDestino");
			resetValue("sgFilialDestino");
			resetValue("nmFilialDestino");
			resetValue("tlFilialDestino");
			setDisabled("nrCepLookupDestino.nrCep",true);
		}
		/* Divisao Cliente */
		validateDivisaoCliente();
		/* Tipo Modal */
		var tpModal = getElementValue("tpModal");
		if(tpModal != "") {
			setDisabled("dimensoesButton", false);
		} else {
			setDisabled("dimensoesButton", true);
			resetValue("psCubado");
		}
		/* Peso Cubado */
		if(getElementValue("dimensoesOk") == "true") {
			calculaPesoCubado();
		}
		return true;
	}

	function habilitaCalcularFrete() {
		if( (getElementValue("idFilialOrigem") != "")
			&& (getElementValue("idFilialDestino") != "")
		) {
			setDisabled("calcularButton", false);
		} else {
			setDisabled("calcularButton", true);
		}
	}

	function nrCepLookupDestino_cb(data) {
		var result = true;
		if(data.length == 0) {
			resetFilialCep("Destino");
		}
		if (data.length > 1) {
			lookupClickPicker({e:document.forms[0].elements['nrCepLookupDestino.nrCep']});
		} else {
			result = nrCepLookupDestino_cepCriteria_exactMatch_cb(data);
			populateCep(data[0],"Destino");
		}
		habilitaCalcularFrete();
		return result;
	}

	function nrCepLookupOrigem_cb(data) {
		var result = true;
		if(data.length == 0) {
			resetFilialCep("Origem");
		}
		if (data.length > 1) {
			lookupClickPicker({e:document.forms[0].elements['nrCepLookupOrigem.nrCep']});
		} else {
			result = nrCepLookupOrigem_cepCriteria_exactMatch_cb(data);
			populateCep(data[0],"Origem");
		}
		return result;
	}

	/* Popup Remetente */
	function popupRemetente(data) {
		var sdo = createServiceDataObject("lms.vendas.cotacaoFreteViaWebAction.findDadosCliente", "remetente", {pessoa:{nrIdentificacao:data.pessoa.nrIdentificacao}});
		xmit({serviceDataObjects:[sdo]});
	}

	/* Popup Destinatario */
	function popupDestinatario(data) {
		var sdo = createServiceDataObject("lms.vendas.cotacaoFreteViaWebAction.findDadosCliente", "destinatario", {pessoa:{nrIdentificacao:data.pessoa.nrIdentificacao}});
		xmit({serviceDataObjects:[sdo]});
	}

	function popupNrCepDestino(data) {
		populateCep(data,"Destino");
		return true;
	}

	function popupNrCepOrigem(data) {
		populateCep(data,"Origem");
		return true;
	}

	function populateCep(data, tipoCep) {
		if(data == undefined) {
			setDisabled("calcularButton", true);
			return;
		}
		var idMunicipio = data.municipio.idMunicipio;
		var nrCep = data.nrCep;
		setElementValue("nrCep" + tipoCep, nrCep);
		var idServico = getElementValue("servico.idServico");
		var params;
		if(idMunicipio && nrCep) {
			params = {idMunicipio:idMunicipio, nrCep:nrCep, idServico:idServico};
		}
		var sdo;
		if(tipoCep == "Origem") {
			sdo = createServiceDataObject("lms.vendas.cotacaoFreteViaWebAction.findFilialDadosByMunicipioNrCepServico", "carregaFilialOrigem", params);
		} else if (tipoCep == "Destino") {
			sdo = createServiceDataObject("lms.vendas.cotacaoFreteViaWebAction.findFilialDadosByMunicipioNrCepServico", "carregaFilialDestino", params);
		} else {
			//FIXME
			alert("Erro nao esperado: Tipo Cep");
			return false;
		}
		xmit({serviceDataObjects:[sdo]});
	}

	function carregaFilialOrigem_cb(dados,erros) {
		if(erros) {
			alert(erros);
			setDisabled("calcularButton", true);

			var objCritCep = getElement("nrCepLookupOrigem.cepCriteria");
			setFocus(objCritCep, true);
			return false;
		}
		if(dados != undefined) {
			setElementValue("idFilialOrigem",dados.idFilial);
			setElementValue("sgFilialOrigem",dados.sgFilial);
			setElementValue("nmFilialOrigem",dados.nmFilial);
			setElementValue("tlFilialOrigem",dados.nrTelefone);
		}
		habilitaCalcularFrete();
	}

	function carregaFilialDestino_cb(dados,erros) {
		if(erros) {
			alert(erros);
			setDisabled("calcularButton", true);
			var objCritCep = getElement("nrCepLookupDestino.cepCriteria");
			setFocus(objCritCep, true);
			return false;
		}
		if (dados != undefined){
			setElementValue("idFilialDestino",dados.idFilial);
			setElementValue("sgFilialDestino",dados.sgFilial);
			setElementValue("nmFilialDestino",dados.nmFilial);
			setElementValue("tlFilialDestino",dados.nrTelefone);
		}
		habilitaCalcularFrete();
	}

	function cepChange(tipoCep){
		var cep = getElementValue("nrCepLookup"+tipoCep +".cepCriteria");
		if (cep == "") {
			resetEnderecoCep(tipoCep);
			setDisabled("calcularButton", true);
			return true;
		} else {
			setElementValue("nrCep"+tipoCep,cep);
			return lookupChange({e:getElement("nrCepLookup" + tipoCep + ".nrCep"), forceChange:true});
		}
	}

	function resetEnderecoCep(tipoCep) {
		resetValue("nrCepLookup" + tipoCep + ".nrCep");
		resetValue("nrCepLookup" + tipoCep + ".cepCriteria");
		resetFilialCep(tipoCep);	
	}

	function resetFilialCep(tipoCep) {
		resetValue("idMunicipio" + tipoCep);
		resetValue("nrCep" + tipoCep);
		resetValue("sgFilial" + tipoCep);
		resetValue("nmFilial" + tipoCep);
		resetValue("tlFilial" + tipoCep);	
	}

	function carregaDivisoesCliente_cb(dados,erros) {
		if (erros != undefined){
			alert(erros);
			return false;
		}
		getElement("divisaoCliente.idDivisaoCliente").required = (dados != undefined && dados.length > 0) ? "true" : "false";
		if (dados != undefined) {
			divisaoCliente_idDivisaoCliente_cb(dados);
			if (dados.length == 1){
				setElementValue("divisaoCliente.idDivisaoCliente",dados[0].idDivisaoCliente);
			}
			if (dados.length <= 1){
				setDisabled("divisaoCliente.idDivisaoCliente", true);
			} else {
				setDisabled("divisaoCliente.idDivisaoCliente", false);
			}
		}
		return true;
	}

	function carregaServicos_cb(dados,erros){
		if (erros != undefined){
			alert(erros);
			return false;
		}
		if (dados!=undefined){
			servico_idServico_cb(dados);
		}
	}

	function openDimensoesWeb() {
		var returnModalValue = openDimensoes();
		if(returnModalValue == true) {
			setElementValue("dimensoesOk", returnModalValue);
			calculaPesoCubado();
		}
	}

	function validateDivisaoCliente() {
		/* Valida se deve carregar as divisoes */
		if( (getElement("tpFrete").selectedIndex < 1)
			|| (getElement("servico.idServico").selectedIndex < 1)
		) {
			return false;
		}

		var idCliente, tpCliente;
		var idClienteRemetente = getElementValue("clienteByIdClienteRemetente.idCliente");
		var idClienteDestinatario = getElementValue("clienteByIdClienteDestinatario.idCliente");
		var blReturn = true;

		var value = getTextFromCombo("tpFrete");
		if(value == "CIF" && idClienteRemetente != "") {
			idCliente = idClienteRemetente;
			tpCliente = getElementValue("clienteByIdClienteRemetente.tpCliente");
		} else if (value == "FOB" && idClienteDestinatario != "") {
			idCliente = idClienteDestinatario;
			tpCliente = getElementValue("clienteByIdClienteDestinatario.tpCliente");
		} else {
			blReturn = false;
		}
		setElementValue("dsTpFrete", getTextFromCombo("tpFrete"));
		/* Carrega Moedas do Devedor */
		validateMoedas(idCliente);

		/* Busca Divisão Cliente, caso o mesmo seja cadastrado */
		if(blReturn) {
			/* Carrega Divisoes de acordo com o Cliente Devedor + Servico*/
			var idServico = getElementValue("servico.idServico");
			var sdo = createServiceDataObject("lms.vendas.cotacaoFreteViaWebAction.findDivisaoCliente", "carregaDivisoesCliente",{idCliente:idCliente, tpCliente:tpCliente, idServico:idServico});
			xmit({serviceDataObjects:[sdo]});
		} else {
			clearElement("divisaoCliente.idDivisaoCliente");
			setDisabled("divisaoCliente.idDivisaoCliente", true);
			clearElement("moeda.idMoeda");
		}
		return blReturn;
	}

	function validateICMS(tipo) {
		var cliente = "clienteByIdCliente"+tipo;
		var blContribuinte = getElement("bl"+tipo+"Contribuinte");

		if(getElementValue(cliente+".idCliente") == "") {
			if (getElementValue(cliente+".pessoa.tpPessoa") != "J") {
				setElementValue(blContribuinte, false);
				setDisabled(blContribuinte, true);
			} else {
				setDisabled(blContribuinte, false);
			}
		} else {
			setDisabled(blContribuinte, true);
			setElementValue(blContribuinte, getElementValue(cliente + ".tpSituacaoTributaria") == "CO");
		}
	}

	function validateTipoFrete(tipo) {
		var cliente = "clienteByIdCliente" + tipo;
		var value = getTextFromCombo("tpFrete");
		if ((tipo == "Remetente" && value == "CIF") || (tipo == "Destinatario" && value == "FOB")) {
			clearElement("divisaoCliente.idDivisaoCliente");
			setDisabled("divisaoCliente.idDivisaoCliente", true);
			clearElement("moeda.idMoeda");
		}
		getElement(cliente+".idInscricaoEstadual").required = "false";
		clearElement(cliente+".idInscricaoEstadual");
		setDisabled(cliente+".pessoa.nmPessoa", false);
		setDisabled(cliente+".pessoa.tpPessoa", true);
		setDisabled(cliente+".idInscricaoEstadual", true);
	}

	function validateMoedas(idCliente) {
		/* Carrega Moedas de acordo com o Cliente Devedor */
		var sdo = createServiceDataObject("lms.vendas.cotacaoFreteViaWebAction.findMoedasPaisEnderecoPadrao", "carregaMoedas",{idCliente:idCliente});
		xmit({serviceDataObjects:[sdo]});
	}
	function carregaMoedas_cb(dados,erros){
		if (erros != undefined){
			alert(erros);
			return false;
		}
		if (dados != undefined){
			var index = null;
			var count = 0;
			moeda_idMoeda_cb(dados);
			for (var i = 0; i<dados.length; i++){
				if (dados[i].blIndicadorMaisUtilizada == "true"){
					setElementValue("moeda.idMoeda",dados[i].idMoeda);
					setElementValue("dsMoeda",dados[i].dsSimbolo);
					if(index == null) {
						index = dados[i].idMoeda;
					}
				}
			}
			if(dados.length == 1) {
				setElementValue("moeda.idMoeda",dados[0].idMoeda);
			} else if (index != null){
				setElementValue("moeda.idMoeda",index);
			}
		}
	}

	function validateClientes() {
		if(getElementValue("clienteByIdClienteRemetente.idCliente") == "" 
		&& getElementValue("clienteByIdClienteDestinatario.idCliente") == "") {
			alertI18nMessage("LMS-01027");
			setFocus("clienteByIdClienteRemetente.pessoa.nrIdentificacao");
			return false;
		}
		return true;
	}

	function validateDimensoes() {
		if(getElementValue("dimensoesOk") == false) {
			alertI18nMessage("LMS-01092");
			return false;
		}
		return true;
	}

	function validaValor(obj) {
		var value = getElementValue(obj);
		if(value != "") {
			var nrValue = stringToNumber(value);
			if(nrValue <= 0) {
				alertI18nMessage("LMS-01050", obj.label, false);
				return false;
			}
		}
		return true;
	}

	function validateTab() {
		return validateTabScript(document.forms) && validateClientes() && validateDimensoes();
	}

	function openCalcularFrete() {
		/* Carregas IE Fields */
		resetValue("clienteByIdClienteRemetente.dsInscricaoEstadual");
		resetValue("clienteByIdClienteDestinatario.dsInscricaoEstadual");

		if(getElementValue("clienteByIdClienteRemetente.idInscricaoEstadual") > 0) {
			setElementValue("clienteByIdClienteRemetente.dsInscricaoEstadual", getTextFromCombo("clienteByIdClienteRemetente.idInscricaoEstadual"));
		}
		if(getElementValue("clienteByIdClienteDestinatario.idInscricaoEstadual") > 0) {
			setElementValue("clienteByIdClienteDestinatario.dsInscricaoEstadual", getTextFromCombo("clienteByIdClienteDestinatario.idInscricaoEstadual"));
		}
		if(!validateTab()) {
			return false;
		}
		/* Grava dados na Sessao e chama Calculo do Frete */
		var sdo = createServiceDataObject("lms.vendas.cotacaoFreteViaWebAction.storeInSession", "openCalcularFrete", buildFormBeanFromForm(document.forms[0]));
		xmit({serviceDataObjects:[sdo]});
		return true;
	}
	function openCalcularFrete_cb(data,erros) {
		if (erros != undefined) {
			alert(erros);
			return false;
		}
		showModalDialog('vendas/cotacaoFreteViaWebCalculoFrete.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:410px;dialogHeight:380px;');
	}

	function calculaPesoCubado(){
		var value = getElementValue("tpModal");
		if (value && value != ""){
			var sdo = createServiceDataObject("lms.vendas.cotacaoFreteViaWebAction.calculaPesoCubado", "carregaPesoCubado", {tpModal:value});
			xmit({serviceDataObjects:[sdo]});
		} else {
			resetValue("psCubado");
		}
	}
	function carregaPesoCubado_cb(dados,erros) {
		if (erros != undefined) {
			alert(erros);
			return false;
		}
		if(dados!=undefined) {
			var obj = getElement("psCubado");
			setElementValue(obj, setFormat(obj, dados.pesoCubado));
		}
	}

	function cancelarCotacao(){
		var sdo = createServiceDataObject("lms.vendas.cotacaoFreteViaWebAction.cancelarCotacao", "cancelarCotacao");
		xmit({serviceDataObjects:[sdo]});
		initWindow();
		clearElement("divisaoCliente.idDivisaoCliente");
		clearElement("moeda.idMoeda");
	}
	function cancelarCotacao_cb(dados,erros){
		if (erros != undefined){
			alert(erros);
			return false;
		}
		resetValue(document);
		setDisabled("divisaoCliente.idDivisaoCliente", true);
		setDisabled("nrCepLookupOrigem.nrCep", true);
		setDisabled("nrCepLookupDestino.nrCep", true);
		setFocusOnFirstFocusableField();
	}
</script> 