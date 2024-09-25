<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.simularNovosPrecosAction">
	<adsm:form action="/tabelaPrecos/simularNovosPrecos">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-00055" />
		</adsm:i18nLabels>
	
		<adsm:combobox 
			label="tipoTabela"
			property="tipoTabelaPreco.tpTipoTabelaPreco"
			optionProperty="value"
			optionLabelProperty="description"
			service="lms.tabelaprecos.simularNovosPrecosAction.findTipoTabelaPreco" 
			width="19%"
			labelWidth="11%"/>
		
		<adsm:textbox 
			property="tipoTabelaPreco.nrVersao"
			dataType="integer"
			minValue="0"
			label="versao"
			size="7"
			maxLength="6"
			labelWidth="6%"
			width="7%"/>

		<adsm:combobox 
			label="subtipoTabela"
			property="subtipoTabelaPreco.idSubtipoTabelaPreco"
			optionProperty="idSubtipoTabelaPreco"
			optionLabelProperty="tpSubtipoTabelaPreco"
			service="lms.tabelaprecos.simularNovosPrecosAction.findSubtipoTabelaPreco" 
			width="13%"
			labelWidth="13%"/>

		<adsm:textbox 
			label="dataReferencia" 
			dataType="JTDate" 
			property="dtReferencia" 
			width="12%"
			labelWidth="14%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button 
				id="btnConsultar"
				caption="consultar" 
				onclick="return onClickConsultar();"
				disabled="false"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid 
		idProperty="idTabelaPreco" 
		property="tabelaPrecoList" 
		detailFrameName="reaj"
		gridHeight="200" 
		rows="14"
		unique="true">

		<adsm:gridColumn 
			title="tabela" 
			property="tabelaPrecoString" 
			width="20%" />

		<adsm:gridColumn 
			dataType="percent"
			title="reajuste" 
			property="pcReajuste" 
			width="20%" 
			unit="percent" />

		<adsm:gridColumn 
			dataType="JTDate"
			title="vigenciaInicial" 
			property="dtVigenciaInicial" 
			width="20%" />

		<adsm:gridColumn 
			dataType="JTDate"
			title="vigenciaFinal" 
			property="dtVigenciaFinal" 
			width="20%" />

		<adsm:gridColumn 
			title="efetivada" 
			property="blEfetivada" 
			renderMode="image-check"
			width="20%" />

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>

	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
function initWindow(eventObj) {
//	oldAlert("event = "+eventObj.name);
	changeConsultarButtonStatus(false);
	changeAbasStatus(true);
	cleanReajuste();
	reconfiguraSessao();
}

function onClickConsultar() {
	if (!validateSearch()) {
		alert(i18NLabel.getLabel("LMS-00055"));
		return false;
	} else {
		findButtonScript("tabelaPrecoList", document.forms[0]);
	}
}

function changeConsultarButtonStatus(status) {
	setDisabled("btnConsultar", status);
}

function changeAbasStatus(status) {
	var tabGroup = getTabGroup(this.document);
	if(tabGroup != undefined) {
		tabGroup.setDisabledTab("taxas", status);
		tabGroup.setDisabledTab("gen", status);
	}
}

function validateSearch() {
	var search = true;
	if (getElementValue("tipoTabelaPreco.tpTipoTabelaPreco")       == "" &&
		getElementValue("tipoTabelaPreco.nrVersao")                == "" &&
		getElementValue("subtipoTabelaPreco.idSubtipoTabelaPreco") == "" &&
		getElementValue("dtReferencia")                            == "") {
		search = false;
	}
	return search;
}

function reconfiguraSessao() {
	var service = "lms.tabelaprecos.simularNovosPrecosAction.reconfiguraSessao";
	var sdo = createServiceDataObject(service);
	xmit({serviceDataObjects:[sdo]});
}

function cleanReajuste() {
	var tabGroup = getTabGroup(this.document);
	var tab = tabGroup.getTab("reaj");
	newButtonScript(tab.getDocument(), true);
}
//-->
</script>
