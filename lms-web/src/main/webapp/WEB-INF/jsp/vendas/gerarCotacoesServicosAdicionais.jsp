<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.gerarCotacoesParametroClienteAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01050"/>
	</adsm:i18nLabels>	
	<adsm:form action="/vendas/gerarCotacoes" idProperty="idServicoAdicionalCliente"
		onDataLoadCallBack="myOnDataLoad"
		service="lms.vendas.gerarCotacoesParametroClienteAction.findServicoAdicionalById">
		<%-------------------%>
		<%-- GENERALIDADE COMBO --%>
		<%-------------------%>
		<adsm:combobox
			boxWidth="230"
			label="servicoAdicional"
			optionLabelProperty="nmParcelaPreco"
			optionProperty="idParcelaPreco"
			property="parcelaPreco.idParcelaPreco"
			required="true" autoLoad="false"
			service="lms.vendas.gerarCotacoesParametroClienteAction.findParcelaPrecoServico"
			labelWidth="18%"
			width="43%">
			<adsm:propertyMapping relatedProperty="parcelaPreco.cdParcelaPreco"
				modelProperty="cdParcelaPreco"/>
			<adsm:propertyMapping relatedProperty="parcelaPreco.nmParcelaPreco"
				modelProperty="nmParcelaPreco"/>
		</adsm:combobox>
		<adsm:hidden property="parcelaPreco.cdParcelaPreco"/>
		<adsm:hidden property="parcelaPreco.nmParcelaPreco"/>
		<%-------------------%>
		<%-- INDICADOR COMBO --%>
		<%-------------------%>
			<adsm:combobox
			domain="DM_INDICADOR_PARAMETRO_CLIENTE"
			label="indicador"
			labelWidth="13%"
			onchange="changeIndicador(this);"
			property="tpIndicador"
			required="true"
			width="26%">
			<adsm:propertyMapping relatedProperty="tpIndicadorDesc"
				modelProperty="description"/>
		</adsm:combobox>
		<adsm:hidden property="tpIndicadorDesc"/>
		<%-------------------%>
		<%-- VALOR TEXT --%>
		<%-------------------%>
		<adsm:textbox
			dataType="decimal"
			property="vlValor"
			label="valor"
			labelWidth="18%"
			width="43%"
			mask="###,###,###,###,##0.00"
			size="18"
			required="true"
		/>
		<%-------------------%>
		<%-- VALOR MINIMO TEXT --%>
		<%-------------------%>
		<adsm:textbox
			dataType="decimal"
			property="vlMinimo"
			label="valorMinimo"
			mask="###,###,###,###,##0.00"
			size="18"
			labelWidth="13%"
			width="26%"
		/>
		<%-------------------%>
		<%-- MOEDA TEXT --%>
		<%-------------------%>
		<adsm:textbox
			dataType="text"
			disabled="true"
			property="dsSimbolo"
			label="moeda"
			size="8"
			labelWidth="18%"
			width="43%"
			required="true"
		/>
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton service="lms.vendas.gerarCotacoesParametroClienteAction.storeServicoCliente"/>
			<adsm:newButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid
		autoSearch="false"
		detailFrameName="servAd"
		idProperty="idServicoAdicionalCliente"
		gridHeight="170" service="lms.vendas.gerarCotacoesParametroClienteAction.findServicosCliente"
		property="servicoAdicionalCliente"
		unique="true" onRowClick="populaForm"
		showGotoBox="false" showPagging="false"
		showTotalPageCount="false" scrollBars="vertical">

		<adsm:gridColumn
			title="servicoAdicional"
			property="parcelaPreco.nmParcelaPreco"
			width="272"/>
		<adsm:gridColumn
			title="indicador"
			property="tpIndicador"
			width="203"
			isDomain="true"/>
		<adsm:gridColumn
			align="right"
			title="valorIndicador"
			property="valorIndicador"
			width="204"/>

		<adsm:buttonBar>
			<adsm:removeButton service="lms.vendas.gerarCotacoesParametroClienteAction.removeServicoByIds"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script language="javascript" type="text/javascript">

	function myOnPageLoad_cb() {
		var eventObj = new Object();
		eventObj.name = "tab_click";
		initWindow(eventObj);
	}

	function populaForm(valor) {
		if(idCot) {
			return false; 
		}
		onDataLoad(valor);
		return true;
	}

	function findDsSimboloMoeda() {
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesParametroClienteAction.findDsSimboloMoeda", "findDsSimboloMoeda");
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findDsSimboloMoeda_cb(data) {
		var obj = document.getElementById("dsSimbolo");
		setElementValue(obj, data._value);
		obj.masterLink = "true";
	}
	
	var idCot;
	function initWindow(eventObj) {
		var event = eventObj.name;
		if(event == "tab_click") {
			newButtonScript(document);
			var tabGroup = getTabGroup(this.document);
			var tabCad = tabGroup.getTab("cad");
			idCot = getElementValue(tabCad.getElementById("idCotacao"))
			if(idCot && idCot != "") {
				findServicosByCotacao(idCot);
				setDisabled(document, true);
			} else { 
				idCot = null;
				setDisabled(document, false);
				setDisabled("dsSimbolo", true);
				findDsSimboloMoeda();
				carregaParcelas();
				carregaServicos();
			}
		} else if(event == "storeButton"){
			carregaServicos();
		} 
	}
	
	function findServicosByCotacao(idCotacao){
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesParametroClienteAction.findServicosByCotacao", 
			"carregaServicos", {idCotacao:idCotacao});
		xmit({serviceDataObjects:[sdo]});
	}


	function carregaParcelas() {
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesParametroClienteAction.findParcelaPrecoServico", "parcelaPreco.idParcelaPreco");
		xmit({serviceDataObjects:[sdo]});
	}
	
	function carregaServicos() {
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesParametroClienteAction.findServicosCliente", "carregaServicos");
		xmit({serviceDataObjects:[sdo]});
	}

	function carregaServicos_cb(data, erros){
		var gridGen = document.getElementById("servicoAdicionalCliente.dataTable").gridDefinition;
		gridGen.resetGrid();
 		gridGen.onDataLoad_cb(data, erros);
	}

	function changeIndicador(combo) {
		comboboxChange({e:combo});
		configIndicador();
	}

	function configIndicador() {
		var ind = getElementValue("tpIndicador");
		if(ind == "T") {
			setElementValue("vlValor", setFormat("vlValor", "0"));
			setDisabled("vlValor", true);
		} else 
			setDisabled("vlValor", false);
	}

	function validateTab() {
		return validateTabScript(document.forms) && validateValorLimite();
	}

	function validateValorLimite() {
		var field = getElement("tpIndicador");
		var tp = getElementValue(field);
		var vlObj = document.getElementById("vlValor");
		var val = getElementValue(vlObj);
		var num = stringToNumber(val);
		if(tp == "V") {
			if(val == "" || num < 0) {
				alertI18nMessage("LMS-01050", field.label, false);
				setFocus(vlObj);
				return false;
			}
		} else if(tp == "D" || tp == "A") {
			if(val == "" || num < 0 || (num > 100 && tp == "D")) {
				alertI18nMessage("LMS-01050", field.label, false);
				setFocus(vlObj);
				return false;
			}
		}
		return true;
	}

	function myOnDataLoad_cb(data, errorMessage, errorCode, eventObj) {
		onDataLoad_cb(data, errorMessage, errorCode, eventObj);
		configIndicador();
	}

</script>
