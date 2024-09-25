<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.gerarCotacoesParametroClienteAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01067"/>
		<adsm:include key="LMS-01068"/>
		<adsm:include key="LMS-01075"/>
	</adsm:i18nLabels>	
	<adsm:form action="/vendas/gerarCotacoes" idProperty="idGeneralidadeCliente"
		onDataLoadCallBack="myOnDataLoad"
		service="lms.vendas.gerarCotacoesParametroClienteAction.findGeneralidadeById">
		<%-------------------%>
		<%-- GENERALIDADE COMBO --%>
		<%-------------------%>
		<adsm:combobox 
			boxWidth="230"
			label="generalidade" 
			labelWidth="18%"
			optionLabelProperty="nmParcelaPreco" 
			optionProperty="idParcelaPreco" 
			property="parcelaPreco.idParcelaPreco" 
			required="true" autoLoad="false"
			onchange="changeParcela(this); changeDomainIndicador();"
			service="lms.vendas.gerarCotacoesParametroClienteAction.findParcelaPrecoGeneralidade" 
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
		<%-------------------%>
		<%-- VALOR TEXT --%>
		<%-------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="vlGeneralidade" 
			label="valor" 
			labelWidth="13%" 
			mask="###,###,###,###,##0.00"
			size="18" 
			required="true" 
			width="26%" 
		/>
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton service="lms.vendas.gerarCotacoesParametroClienteAction.storeGeneralidadeCliente"/>
			<adsm:newButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid
		service="lms.vendas.gerarCotacoesParametroClienteAction.findGeneralidadesCliente"
		property="generalidadeCliente"
		idProperty="idGeneralidadeCliente"
		gridHeight="170"
		unique="true"
		detailFrameName="gen"
		autoSearch="false"
		onRowClick="populaForm"
		showGotoBox="false"
		showPagging="false"
		showTotalPageCount="false"
		scrollBars="vertical">

		<adsm:gridColumn
			title="generalidade"
			dataType="text"
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
			<adsm:removeButton
				service="lms.vendas.gerarCotacoesParametroClienteAction.removeGeneralidadeByIds"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	var tpIndicadorValue;

	function myOnPageLoad_cb() {
		var eventObj = new Object();
		eventObj.name = "tab_click";
		initWindow(eventObj);
	}

	function myOnDataLoad_cb(data, errorMessage, errorCode, eventObj) {
		tpIndicadorValue = data.tpIndicador;
		changeDomainIndicador(data.parcelaPreco.cdParcelaPreco);

		onDataLoad_cb(data, errorMessage, errorCode, eventObj);
		configIndicador();
		findLimiteDesconto();
	}

	function populaForm(valor) {
		if(idPar)
			return false; 
		onDataLoad(valor);
		return true;
	}

	function changeParcela(combo){
		findLimiteDesconto();
		return true;
	}

	/**
	* Obtém o percentual de limite de desconto referente à generalidade
	*/
	function findLimiteDesconto() {
		var idParcelaPreco = getElementValue("parcelaPreco.idParcelaPreco");
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesParametroClienteAction.findLimiteDescontoParcela", "findLimiteDesconto", {idParcelaPreco:idParcelaPreco});
		xmit({serviceDataObjects:[sdo]});
	}

	var limiteDesconto;
	function findLimiteDesconto_cb(data, erros) {
		if(erros) {
			alert(erros);
			return false;
		}
		limiteDesconto = stringToNumber(data._value);
	}

	function copyDsSimbolo() {
		var frame = parent.document.frames["param_iframe"];
		var obj = document.getElementById("dsSimbolo");
		setElementValue(obj, frame.getDsSimbolo());
		obj.masterLink = "true";
	}

	var idPar;
	function initWindow(eventObj) {
		var event = eventObj.name;
		if(event == "tab_click") {
			newButtonScript(document);
			var tabGroup = getTabGroup(this.document);
			var tabCad = tabGroup.getTab("param");
			idPar = getElementValue(tabCad.getElementById("idParametroCliente"))
			if(idPar && idPar != "") {
				findGeneralidadesByParametro(idPar);
				setDisabled(document, true);
			} else {
				idPar = null;
				setDisabled(document, false);
				setDisabled("dsSimbolo", true);
				carregaParcelas();
				carregaGeneralidades();
			}
			copyDsSimbolo();
		} else if(event == "newButton_click") {
			setElementValue("tpIndicador", "T");
			configIndicador();
		} else if(event == "storeButton")
			carregaGeneralidades();
	}
	
	function findGeneralidadesByParametro(idParametro){
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesParametroClienteAction.findGeneralidadesByParametro", 
			"carregaGeneralidades", {idParametro:idParametro});
		xmit({serviceDataObjects:[sdo]});
	}
	
	
	function carregaParcelas() {
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesParametroClienteAction.findParcelaPrecoGeneralidade",
		   "parcelaPreco.idParcelaPreco");
       	xmit({serviceDataObjects:[sdo]});
	}
	
	function carregaGeneralidades() {
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesParametroClienteAction.findGeneralidadesCliente",
		   "carregaGeneralidades");
       	xmit({serviceDataObjects:[sdo]});
	}

	function carregaGeneralidades_cb(data, erros){
		var gridGen = document.getElementById("generalidadeCliente.dataTable").gridDefinition;
		gridGen.resetGrid();
 		gridGen.onDataLoad_cb(data, erros);
	}

	function changeDomainIndicador(cdParcelaPreco) {
		if(cdParcelaPreco == undefined) {
			comboboxChange({e:getElement("tpIndicador")});
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
		}
		configIndicador();
	}

	function changeIndicador(combo) {
		comboboxChange({e:combo});
		configIndicador();
	}

	function configIndicador() {
		var ind = getElementValue("tpIndicador");
		if(ind == "T") {
			setElementValue("vlGeneralidade", setFormat("vlGeneralidade", "0"));
			setDisabled("vlGeneralidade", true);
		} else 
			setDisabled("vlGeneralidade", false);
	}
	
	function validateTab() {
		return validateTabScript(document.forms) && validateGeneralidade();
	}

	function validateIndicadorLimiteDesconto(){
		if(limiteDesconto < 100) {
			var obj = document.getElementById("tpIndicador");
			var tp = getElementValue(obj);
			if(tp != "D" && tp != "T" && tp != "A") {
				alert(i18NLabel.getLabel("LMS-01067"));
				setFocus(obj, true);
				return false;
			}
		}
		return true;
	}

	function validateValorLimite() {
		var tp = getElementValue("tpIndicador");
		var vlObj = document.getElementById("vlGeneralidade");
		var val = getElementValue(vlObj);
		var num = stringToNumber(val);
		if(tp == "V" || tp == "Q") {
			if(val == "" || num < 0) {
				alert(i18NLabel.getLabel("LMS-01075"));
				setFocus(vlObj);
				return false;
			}
		} else if(tp == "D" || tp == "A") {
			if(val == "" || num < 0 || (num > 100 && tp == "D")) {
				alert(i18NLabel.getLabel("LMS-01075"));
				setFocus(vlObj);
				return false;
			}
		}
		return true;
	}

	function validateGeneralidade() {
		return validateIndicadorLimiteDesconto()
			&& validateValorLimite()
			&& validateIndicadorDesconto();
	}
	
	function validateIndicadorDesconto() {
		var tp = getElementValue("tpIndicador");
		if(tp == "D") {
			var obj = document.getElementById("vlGeneralidade");
			if(stringToNumber(getElementValue(obj)) >  limiteDesconto){
				alert(i18NLabel.getLabel("LMS-01068"));
				setFocus(obj, true);
				return false;
			}
		}
		return true;
	}
</script>