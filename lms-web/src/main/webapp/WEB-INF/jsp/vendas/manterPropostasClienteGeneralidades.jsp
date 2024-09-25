<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterPropostasClienteGeneralidadesAction">
	<adsm:form 
		action="/vendas/manterPropostasClienteParam"
		idProperty="idGeneralidadeCliente"
		onDataLoadCallBack="myOnDataLoadCallBack">
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-01075"/>
		</adsm:i18nLabels>
		
		<adsm:hidden property="simulacao.idSimulacao" />
		<adsm:hidden property="generalidadeCliente.idGeneralidadeCliente" />
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

		<adsm:hidden property="parcelaPreco.cdParcelaPreco"/>
		<adsm:combobox 
			property="parcelaPreco.idParcelaPreco" 
			label="generalidade" 
			optionLabelProperty="nmParcelaPreco" 
			optionProperty="idParcelaPreco" 
			onchange="changeParcelaPreco(); changeDomainIndicador();"
			autoLoad="false"
			required="true"
			labelWidth="14%" 
			width="45%">
			<adsm:propertyMapping 
				relatedProperty="parcelaPreco.cdParcelaPreco" 
				modelProperty="cdParcelaPreco"/>
		</adsm:combobox>
			
		<adsm:combobox 
			property="tpIndicador" 
			label="indicador" 
			domain="DM_INDICADOR_PARAMETRO_CLIENTE"
			required="true" 
			labelWidth="100" 
			width="20%" 
			onchange="return changeTpIndicador();"/>
	
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
			dataType="currency" 
			property="vlGeneralidade" 
			label="valor" 
			maxLength="18" 
			size="18" 
			required="true" 
			labelWidth="100" 
			width="20%" 
			onchange="return changeVlGeneralidade();"/>

		<adsm:combobox 
			property="tpIndicadorMinimo" 
			label="indicadorDoMinimo" 
			domain="DM_INDICADOR_PARAMETRO_CLIENTE"
			required="true" 
			labelWidth="14%" 
			width="45%" 
			onchange="return validateVlMinimo();"/>
			
		<adsm:textbox 
			dataType="currency" 
			property="vlMinimo" 
			label="valorDoMinimo" 
			maxLength="18" 
			size="18" 
			required="true" 
			labelWidth="100" 
			width="20%" 
			onchange="return validateVlMinimo();"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton callbackProperty="afterStore"/>
			<adsm:button 
				id="btnLimpar"
				caption="limpar" 
				onclick="return clickLimpar();" />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid 
		idProperty="idGeneralidadeCliente"
		property="generalidadesList"
		autoSearch="false"
		gridHeight="170" 
		unique="true" 
		rows="8" 
		detailFrameName="gen">
		
		<adsm:gridColumn 
			title="generalidade" 
			property="parcelaPreco.nmParcelaPreco" 
			width="40%" />
			
		<adsm:gridColumn 
			title="indicador" 
			property="tpIndicador" 
			isDomain="true"
			width="30%" />
			
		<adsm:gridColumn 
			title="valorIndicador" 
			property="vlGeneralidade" 
			align="right" 
			width="30%" />
			
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--

var _data;
var tpIndicadorValue;



function validateVlMinimo(){
	var tpIndicador = getElementValue("tpIndicadorMinimo");
	var vlMinimo = stringToNumber(getElementValue("vlMinimo"));

	if (tpIndicador == "T" || tpIndicador == ""){
		setElementValue("vlMinimo",setFormat("vlMinimo","0"));
		setDisabled("vlMinimo",true);
	}else{
		setDisabled("vlMinimo",false);		

		if (tpIndicador == "V" || tpIndicador == "A"){
			if (vlMinimo < 0){
				alert(i18NLabel.getLabel("LMS-01075"));
				return false;
			}
		}else if (tpIndicador == "D"){
			if (vlMinimo < 0 || vlMinimo > 100){
				alert(i18NLabel.getLabel("LMS-01075"));
				return false;
			}
		}
	}
	return true;
}

function initWindow(eventObj) {
	var frame = parent.document.frames["cad_iframe"];
	_data = frame.getDadosFromRota();
	if (eventObj.name == "tab_click") {
		ajustaDados(_data);
		ajustaValoresDefault();
		populaGrid(_data.parametroCliente.idParametroCliente);
		populaGeneralidades(_data.tabelaPreco.idTabelaPreco);
	} else if (eventObj.name == "newButton_click") {
		ajustaDados(_data);
		ajustaValoresDefault();
	} else if (eventObj.name == "storeButton") {
		populaGrid(_data.parametroCliente.idParametroCliente);
	} else if (eventObj.name == "removeButton_grid") {
		newButtonScript();
	}
	disableWorkflow();
}

function myOnDataLoadCallBack_cb(data, errorMessage, errorCode, eventObj) {
	tpIndicadorValue = data.tpIndicador.value;
	changeDomainIndicador(data.parcelaPreco.cdParcelaPreco);

	onDataLoad_cb(data, errorMessage, errorCode, eventObj);
	ajustaDados(_data);
	changeTpIndicador();
}

function changeDomainIndicador(cdParcelaPreco) {
	if(cdParcelaPreco == undefined) {
		comboboxChange({e:getElement("parcelaPreco.idParcelaPreco")});
		cdParcelaPreco = getElementValue("parcelaPreco.cdParcelaPreco");
		tpIndicadorValue = undefined;
	}
	var isTad = (cdParcelaPreco == "IDTad");
	var domain = isTad ? "DM_INDICADOR_TAD" : "DM_INDICADOR_PARAMETRO_CLIENTE";

	var tpIndicador = getElement("tpIndicador");
	if(tpIndicador.domain != domain) {
		var sdo = createServiceDataObject("lms.vendas.manterParametrosClienteGeneralidadesAction.findDomainValues", "tpIndicador", {e:domain});
		xmit({serviceDataObjects:[sdo]});
		tpIndicador.domain = domain;
	} else setDefaultValueIndicador();
}

function tpIndicador_cb(data) {
	comboboxLoadOptions({e:document.getElementById("tpIndicador"), data:data});
	setDefaultValueIndicador();
}

function setDefaultValueIndicador() {
	if(tpIndicadorValue != undefined) {
		setElementValue("tpIndicador", tpIndicadorValue);
	} else setInitialValues();
}

/**
* Ajusta o valor inicial do indicador de generalidade e de seu valor
*/
function setInitialValues() {
	setElementValue("tpIndicador","T");
	setElementValue("tpIndicadorMinimo","T");
	setElementValue("vlGeneralidade","0,00");
	setElementValue("vlMinimo","0,00");
	setDisabled("vlGeneralidade",true);
	setDisabled("vlMinimo",true);
}

function afterStore_cb(data, errorMsg, errorKey, showErrorAlert, eventObj) {
	if (errorMsg != undefined) {
		alert(errorMsg);
		return false;
	}
	
	store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);
	setElementValue("generalidadeCliente.idGeneralidadeCliente", data.generalidadeCliente.idGeneralidadeCliente);
}

function populaGrid(idParametroCliente) {
	var arguments = {
		parametroCliente : {
			idParametroCliente : idParametroCliente
		}
	}

	generalidadesListGridDef.executeSearch(arguments, true);
}

function populaGeneralidades(idTabelaPreco) {
	var data = {
		tabelaPreco : {
			idTabelaPreco : idTabelaPreco
		}
	}
	var service = "lms.vendas.manterPropostasClienteGeneralidadesAction.findGeneralidadesCliente";
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

function disableAll() {
	setDisabled(document, true);
}

function disableWorkflow() {
	var url = new URL(parent.location.href);

	if (url.parameters != undefined 
			&& url.parameters.idProcessoWorkflow != undefined 
			&& url.parameters.idProcessoWorkflow != '') {

   		disableAll();
   	}
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

function ajustaValoresDefault() {
	setElementValue("tpIndicador", "T");
	setElementValue("tpIndicadorMinimo", "T");
	validateVlMinimo();
	clearFields();
}

function clearFields() {
	setElementValue("vlGeneralidade", setFormat("vlGeneralidade", "0"));
	changeFieldsStatus(true);
}

function changeFieldsStatus(status) {
	setDisabled("vlGeneralidade", status);
	disableWorkflow();
}

function changeParcelaPreco() {
	setElementValue("generalidadeCliente.idGeneralidadeCliente", "");
}

function changeTpIndicador() {
	var indicador = getElementValue("tpIndicador");
	if (indicador == "" || indicador == "T") {
		clearFields();
	} else {
		changeFieldsStatus(false);
	}
}

function changeVlGeneralidade() {
	var indicadores = ["A", "V", "Q"];
	return validaValorDesconto(indicadores, "tpIndicador", "vlGeneralidade", "LMS-01075");
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
	if (valor < 0.0 || valor > 100.0) {
		alert(i18NLabel.getLabel(alerta));
		setFocus(campo, false);
		return false;
	}
	return true;
}

function validaMaiorIgualZero(campo, alerta) {
	var valor = stringToNumber(getElementValue(campo));
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
