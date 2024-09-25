<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterPropostasClienteTaxasAction">
	<adsm:form 
		idProperty="idTaxaCliente"
		action="/vendas/manterPropostasClienteParam"
	 	onDataLoadCallBack="myOnDataLoadCallBack">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-01075"/>
			<adsm:include key="LMS-01076"/>
			<adsm:include key="LMS-01077"/>
		</adsm:i18nLabels>
	
		<adsm:hidden property="simulacao.idSimulacao" />
		<adsm:hidden property="taxaCliente.idTaxaCliente" />
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
		<adsm:hidden property="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" />
		<adsm:hidden property="unidadeFederativaByIdUfDestino.idUnidadeFederativa" />
		<adsm:hidden property="aeroportoByIdAeroportoOrigem.idAeroporto"/> 
		<adsm:hidden property="aeroportoByIdAeroportoDestino.idAeroporto"/>
	
		<%-------------------%>
		<%-- CLIENTE LOOKUP --%>
		<%-------------------%>
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
				size="8"
				maxLength="7"/>
			
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
		
		<%----------------%>
		<%-- ROTAS TEXT --%>
		<%----------------%>
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
			
		<adsm:combobox 
			property="parcelaPreco.idParcelaPreco" 
			label="taxa" 
			onchange="return changeParcelaPreco();"
			optionProperty="idParcelaPreco"
			optionLabelProperty="nmParcelaPreco"
			autoLoad="false"
			required="true" 
			labelWidth="14%" 
			width="45%" />
		
		<adsm:combobox 
			property="tpTaxaIndicador" 
			label="indicador"
			required="true"
			domain="DM_INDICADOR_PARAMETRO_CLIENTE"
			labelWidth="120" 
			width="20%" 
			onchange="return changeTpTaxaIndicador();"/>
			
		<%-----------%>
		<%-- MOEDA --%>
		<%-----------%>
		<adsm:textbox 
			dataType="text" 
			disabled="true"
			property="tabelaPreco.moeda.dsMoeda" 
			serializable="false"
			label="moeda" 
			labelWidth="14%" 
			width="45%" />
			
		<adsm:textbox 
			dataType="decimal" 
			mask="##,###,###,###,##0.00"
			property="vlTaxa" 
			label="valor" 
			maxLength="15" 
			size="15" 
			required="true" 
			labelWidth="120" 
			width="20%" 
			onchange="return changeVlTaxa();"/>
			
		<adsm:textbox 
			dataType="decimal" 
			mask="#,###,###,###,##0.000"
			property="psMinimo" 
			label="minimoKgKm" 
			maxLength="15" 
			size="15" 
			labelWidth="14%" 
			width="45%" 
			unit="kg" 
			onchange="return changePsMinimo();"/>
			
		<adsm:textbox 
			dataType="decimal" 
			mask="##,###,###,###,##0.00"
			property="vlExcedente" 
			label="valorExcedente" 
			maxLength="15" 
			size="15" 
			labelWidth="120" 
			width="20%" 
			onchange="return changeVlExcedente();"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton callbackProperty="afterStore"/>
			<adsm:button 
				id="btnLimpar"
				caption="limpar" 
				onclick="return clickLimpar();" />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid 
		idProperty="idTaxaCliente" 
		property="taxasCliente"
		gridHeight="170" 
		unique="true" 
		rows="7"
		detailFrameName="tax" 
		autoSearch="false">
		
		<adsm:gridColumn 
			dataType="text"
			title="taxa" 
			property="parcelaPreco.nmParcelaPreco" />
			
		<adsm:gridColumn 
			dataType="text"
			title="indicador" 
			property="tpTaxaIndicador" 
			width="15%" 
			isDomain="true" />
			
		<adsm:gridColumn 
			dataType="text"
			title="valorIndicador" 
			property="vlTaxa" 
			align="right" 
			width="15%" />
			
		<adsm:gridColumn 
			dataType="decimal"
			mask="#,###,###,###,##0.000"
			title="pesoMinimoKg" 
			property="psMinimo" 
			align="right" 
			width="15%" />
		
		<adsm:gridColumn 
			dataType="currency"
			title="valorExcedenteReal" 
			property="vlExcedente" 
			align="right" 
			width="20%"/>
			
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
var _data;

var _tabGroup = getTabGroup(this.document);
if(_tabGroup != undefined) {
	var _tab = _tabGroup.getTab("tax");
	_tab.properties.ignoreChangedState = true;
}

function initWindow(eventObj) {
	var frame = parent.document.frames["cad_iframe"];
	_data = frame.getDadosFromRota();
	//oldAlert("event = " + eventObj.name);
	if (eventObj.name == "tab_click") {
		ajustaDados(_data);
		ajustaValoresDefault();
		populaGrid(_data.parametroCliente.idParametroCliente);
		populaTaxas(_data.tabelaPreco.idTabelaPreco);
	} else if (eventObj.name == "newButton_click") {
		ajustaDados(_data);
		ajustaValoresDefault();
	} else if (eventObj.name == "storeButton") {
		populaGrid(_data.parametroCliente.idParametroCliente);
	} else if (eventObj.name == "removeButton_grid") {
		newButtonScript(document, true);
	}
	setDisabled("btnLimpar", false);
	disableWorkflow();
}

function afterStore_cb(data, errorMsg, errorKey, showErrorAlert, eventObj) {
	if (errorMsg != undefined) {
		alert(errorMsg);
		return false;
	}
	store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);
	setElementValue("taxaCliente.idTaxaCliente", data.taxaCliente.idTaxaCliente);
}

function myOnDataLoadCallBack_cb(data, errorMessage, errorCode, eventObj) {
	onDataLoad_cb(data, errorMessage, errorCode, eventObj);
	ajustaDados(_data);
	changeTpTaxaIndicador();
}

function populaGrid(idParametroCliente) {
	var arguments = {
		parametroCliente : {
			idParametroCliente : idParametroCliente
		}
	}

	taxasClienteGridDef.executeSearch(arguments, true);
}

function populaTaxas(idTabelaPreco) {
	var data = {
		tabelaPreco : {
			idTabelaPreco : idTabelaPreco
		}
	}
	var service = "lms.vendas.manterPropostasClienteTaxasAction.findTaxaCliente";
	var sdo = createServiceDataObject(service, "parcelaPreco.idParcelaPreco", data);
	xmit({serviceDataObjects:[sdo]});
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
	setElementValue("servico.idServico", data.servico.idServico);
	setElementValue("tabelaPreco.idTabelaPreco", data.tabelaPreco.idTabelaPreco);
	setElementValue("tabelaPreco.moeda.dsMoeda", data.tabelaPreco.moeda.dsMoeda);
	setElementValue("simulacao.idSimulacao", data.simulacao.idSimulacao);
	setElementValue("parametroCliente.idParametroCliente", data.parametroCliente.idParametroCliente);
	
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
	
	if (_data.disableAll) {
		disableAll();
	} else {
		enableFields();
	}
}

function ajustaValoresDefault() {
	setElementValue("tpTaxaIndicador", "T");
	clearFields();
}

function disableWorkflow() {
	var url = new URL(parent.location.href);

	if (url.parameters != undefined 
			&& url.parameters.idProcessoWorkflow != undefined 
			&& url.parameters.idProcessoWorkflow != '') {

   		disableAll();
   	}
}

function disableAll() {
	setDisabled(document, true);
}

function enableFields() {
	setDisabled(document, false);
	setDisabled("cliente.pessoa.nrIdentificacao", true);
	setDisabled("cliente.pessoa.nmPessoa", true);
	setDisabled("divisaoCliente.dsDivisaoCliente", true);
	setDisabled("tabelaPreco.tabelaPrecoString", true);
	setDisabled("tabelaPreco.dsDescricao", true);
	setDisabled("servico.dsServico", true);
	setDisabled("rotaPreco.origemString", true);
	setDisabled("rotaPreco.destinoString", true);
	setDisabled("tabelaPreco.moeda.dsMoeda", true);
	disableWorkflow();
}

function clearFields() {
	setElementValue("vlTaxa", setFormat("vlTaxa", "0"));
	setElementValue("psMinimo", setFormat("psMinimo", "0"));
	setElementValue("vlExcedente", setFormat("vlExcedente", "0"));
	changeFieldsStatus(true);
}

function changeFieldsStatus(status) {
	setDisabled("vlTaxa", status);
	setDisabled("psMinimo", status);
	setDisabled("vlExcedente", status);
	disableWorkflow();
}

function changeParcelaPreco() {
	setElementValue("taxaCliente.idTaxaCliente", "");
}

function changeTpTaxaIndicador() {
	if (getElementValue("tpTaxaIndicador") == "T") {
		clearFields();
	} else {
		changeFieldsStatus(false);
	}
}

function changeVlTaxa() {
	var indicadores = ["A", "V"];
	return validaValorDesconto(indicadores, "tpTaxaIndicador", "vlTaxa", "LMS-01075");
}

function changePsMinimo() {
	return validaMaiorIgualZero("psMinimo", "LMS-01076");
}

function changeVlExcedente() {
	return validaMaiorIgualZero("vlExcedente", "LMS-01077");
}

function validaValorDesconto(indicadores, campoIndicador, campo, alerta) {
	var maiorIgual = false;
	var value = getElementValue(campoIndicador);
	for (var i = 0; i < indicadores.length; i++) {
		if (value == indicadores[i]) {
			maiorIgual = true;
			break;
		}
	}
	
	if (maiorIgual == true) {
		return validaMaiorIgualZero(campo, alerta);
	} else if (value == "D") {
		return validaDesconto(campo, alerta);
	}
	return true;
}

function validaDesconto(campo, alerta) {
	var valor = stringToNumber(getElementValue(campo));
//	oldAlert("valor = "+valor);
	if (valor < 0.0 || valor > 100.0) {
		alert(i18NLabel.getLabel(alerta));
		setFocus(campo, false);
		return false;
	}
	return true;
}

function validaMaiorIgualZero(campo, alerta) {
	var valor = stringToNumber(getElementValue(campo));
//	oldAlert("valor = "+valor);
	if (valor < 0.0) {
		alert(i18NLabel.getLabel(alerta));
		setFocus(campo, false);
		return false;
	}
	return true;
}

function hide() {
	tab_onHide();
	cleanButtonScript();
	return true;
}

function clickLimpar() {
	var tab = getTab(this.document);
	tab.setChanged(false);
	tab.itemTabChanged = false;
	
	newButtonScript();
}
//-->
</script>
