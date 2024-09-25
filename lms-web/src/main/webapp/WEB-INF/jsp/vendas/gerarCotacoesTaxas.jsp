<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.gerarCotacoesParametroClienteAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01050"/>
	</adsm:i18nLabels>	
	<adsm:form action="/vendas/gerarCotacoes" idProperty="idTaxaCliente" onDataLoadCallBack="myOnDataLoad"
		service="lms.vendas.gerarCotacoesParametroClienteAction.findTaxaById">
		<adsm:combobox property="parcelaPreco.idParcelaPreco" 
			label="taxa" optionLabelProperty="nmParcelaPreco" 
			optionProperty="idParcelaPreco" required="true" autoLoad="false"
			service="lms.vendas.gerarCotacoesParametroClienteAction.findParcelaPrecoTaxa" 
			labelWidth="18%" width="43%" >
			<adsm:propertyMapping relatedProperty="parcelaPreco.cdParcelaPreco" 
				modelProperty="cdParcelaPreco"/>
			<adsm:propertyMapping relatedProperty="parcelaPreco.nmParcelaPreco" 
				modelProperty="nmParcelaPreco"/>
		</adsm:combobox>	
		<adsm:hidden property="parcelaPreco.cdParcelaPreco"/>
		<adsm:hidden property="parcelaPreco.nmParcelaPreco"/>
		<adsm:combobox property="tpTaxaIndicador" label="indicador" 
			domain="DM_INDICADOR_PARAMETRO_CLIENTE" onlyActiveValues="true"
			required="true" labelWidth="13%" width="26%" onchange="changeIndicador(this);">
			<adsm:propertyMapping relatedProperty="tpTaxaIndicadorDesc" 
				modelProperty="description"/>
		</adsm:combobox>
		<adsm:hidden property="tpTaxaIndicadorDesc"/>
		<adsm:textbox dataType="text" disabled="true"
			property="dsSimbolo"
			label="moeda" maxLength="10" size="8" 
			required="true" 
			labelWidth="18%" width="43%"/>	
		<adsm:textbox dataType="decimal" property="vlTaxa" 
			label="valor" required="true" 
			mask="##,###,###,###,##0.00" size="18" 
			labelWidth="13%" width="21%" />
		<adsm:textbox dataType="weight" property="psMinimo" 
			label="pesoMinimo" 
			mask="#,###,###,###,##0.000"
			labelWidth="18%" width="43%" unit="kg" />
		<adsm:textbox dataType="decimal" property="vlExcedente" 
			label="valorExcedente" mask="##,###,###,###,##0.00" size="18" 
			labelWidth="13%" width="26%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton service="lms.vendas.gerarCotacoesParametroClienteAction.storeTaxaCliente" 
				caption="salvar"/>
			<adsm:newButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid
		autoSearch="false"
		detailFrameName="taxas"
		property="taxaCliente"
		gridHeight="170"
		unique="true"
		idProperty="idTaxaCliente"
		showGotoBox="false"
		showPagging="false"
		onRowClick="populaForm"
		service="lms.vendas.gerarCotacoesParametroClienteAction.findTaxasCliente"
		showTotalPageCount="false"
		scrollBars="vertical">

		<adsm:gridColumn
			title="taxa"
			property="parcelaPreco.nmParcelaPreco"
			width="184"/>

		<adsm:gridColumn
			title="indicador"
			property="tpTaxaIndicador"
			width="136"
			isDomain="true"/>

		<adsm:gridColumn
			title="valorIndicador"
			property="valorIndicador"
			align="right"
			width="122"/>

		<adsm:gridColumn
			title="pesoMinimoKg"
			dataType="decimal"
			property="psMinimo"
			align="right"
			width="102"
			mask="#,###,###,###,##0.000"/>

		<adsm:gridColumn
			title="valorExcedenteReal"
			property="vlExcedente"
			mask="##,###,###,###,##0.00"
			dataType="decimal"
			align="right"
			width="120"/>

		<adsm:buttonBar>
			<adsm:removeButton 
				caption="excluir"
				service="lms.vendas.gerarCotacoesParametroClienteAction.removeTaxaByIds"/>
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
		if(idPar)
			return false; 
		onDataLoad(valor);
		return true;
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
				findTaxasByParametro(idPar);
				setDisabled(document, true);
			} else {
				idPar = null;
				setDisabled(document, false);
				setDisabled("dsSimbolo", true);
				carregaParcelas();
				carregaTaxas();
			}
			copyDsSimbolo();
		} else if(event == "storeButton")
			carregaTaxas();
	}

	function findTaxasByParametro(idParametro){
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesParametroClienteAction.findTaxasByParametro", 
			"carregaTaxas", {idParametro:idParametro});
		xmit({serviceDataObjects:[sdo]});
	}

	function carregaParcelas() {
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesParametroClienteAction.findParcelaPrecoTaxa", "parcelaPreco.idParcelaPreco");
		xmit({serviceDataObjects:[sdo]});
	}

	function carregaTaxas() {
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesParametroClienteAction.findTaxasCliente", "carregaTaxas");
		xmit({serviceDataObjects:[sdo]});
	}

	function carregaTaxas_cb(data, erros){
		var gridTaxa = document.getElementById("taxaCliente.dataTable").gridDefinition;
		gridTaxa.resetGrid();
 		gridTaxa.onDataLoad_cb(data, erros);
	}

	function changeIndicador(combo) {
		comboboxChange({e:combo});
		configIndicador();
	}

	function configIndicador() {
		var ind = getElementValue("tpTaxaIndicador");
		if(ind == "T") {
			setElementValue("vlTaxa", setFormat("vlTaxa", "0"));
			setElementValue("psMinimo", setFormat("psMinimo", "0"));
			setElementValue("vlExcedente", setFormat("vlExcedente", "0"));
			setDisabled("vlTaxa", true);
			setDisabled("psMinimo", true);
			setDisabled("vlExcedente", true);
		} else {
			setDisabled("vlTaxa", false);
			setDisabled("psMinimo", false);
			setDisabled("vlExcedente", false);
		}
	}

	function validateTab() {
		return validateTabScript(document.forms) && validaTaxa();
	}

	function validaTaxa() {
		var field = getElement("tpTaxaIndicador");
		var tpTaxaIndicador = getElementValue(field);
		var vlObj = document.getElementById("vlTaxa");
		var val = getElementValue(vlObj);
		var num = stringToNumber(val);
		if(tpTaxaIndicador == "V") {
			if(val == "" || num < 0) {
				alertI18nMessage("LMS-01050", field.label, false);
				setFocus(vlObj);
				return false;
			}
		} else if(tpTaxaIndicador == "D" || tpTaxaIndicador == "A") {
			if(val == "" || num < 0 || (num > 100 && tpTaxaIndicador == "D")) {
				alertI18nMessage("LMS-01050", field.label, false);
				setFocus(vlObj);
				return false;
			}
		}
		var psObj = document.getElementById("psMinimo");
		var peso = stringToNumber(getElementValue(psObj));
		if(peso < 0) {
			alertI18nMessage("LMS-01050", psObj.label, false);
			setFocus(psObj);
			return false;
		}
		var vlExObj = document.getElementById("vlExcedente");
		var vlEx = stringToNumber(getElementValue(vlExObj));
		if(vlEx < 0) {
			alertI18nMessage("LMS-01050", vlExObj.label, false);
			setFocus(vlExObj);
			return false;
		}
		return true;
	}

	function myOnDataLoad_cb(data, errorMessage, errorCode, eventObj) {
		onDataLoad_cb(data, errorMessage, errorCode, eventObj);
		configIndicador();
	}
</script>
