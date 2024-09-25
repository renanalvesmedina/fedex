<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoad="myOnPageLoad" onPageLoadCallBack="findDadosSessao">
	<adsm:i18nLabels>
		<adsm:include key="LMS-30050"/>
	</adsm:i18nLabels>

	<adsm:form action="/vendas/clientesAtualizacaoManualTabela">
		<adsm:hidden property="tpFormatoRelatorio.valor" />
		<adsm:hidden property="tpFormatoRelatorio.descricao" />
		<adsm:hidden property="regional.siglaDescricao"/>		
		<adsm:hidden property="tpAcesso" value="F"/>

		<!-- Combo de regionais -->
		<adsm:combobox
			label="regional"
			property="regional.idRegional"
			optionLabelProperty="siglaDescricao"
			optionProperty="idRegional"
			service="lms.vendas.clientesAtualizacaoManualTabelaAction.findRegional"
			boxWidth="240">

			<adsm:propertyMapping
				relatedProperty="regional.siglaDescricao"
				modelProperty="siglaDescricao"/>
		</adsm:combobox>

		<!-- Lookup de filiais -->
		<adsm:lookup
			label="filial"
			labelWidth="15%"
			width="8%"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			action="/municipios/manterFiliais"
			service="lms.vendas.clientesAtualizacaoManualTabelaAction.findLookupFilial"
			dataType="text"
			size="3"
			maxLength="3"
			exactMatch="true"
			minLengthForAutoPopUpSearch="3"
			criteriaSerializable="true"
		>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="filial.sgFilial" modelProperty="sgFilial"/>

			<adsm:propertyMapping criteriaProperty="regional.idRegional" modelProperty="regionalFiliais.regional.idRegional"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial"modelProperty="sgFilial"/>
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>

			<adsm:textbox
				dataType="text"
				property="filial.pessoa.nmFantasia"
				serializable="false"
				size="30" width="27%"
				maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:combobox property="modal" label="modal" domain="DM_MODAL" serializable="false">
			<adsm:hidden property="modal.valor"/>
			<adsm:hidden property="modal.descricao"/>
			<adsm:propertyMapping relatedProperty="modal.valor" modelProperty="value"/>
			<adsm:propertyMapping relatedProperty="modal.descricao" modelProperty="description"/>
		</adsm:combobox>
		
		<adsm:combobox property="abrangencia" label="abrangencia" domain="DM_ABRANGENCIA" serializable="false">
			<adsm:hidden property="abrangencia.valor"/>
			<adsm:hidden property="abrangencia.descricao"/>
			<adsm:propertyMapping relatedProperty="abrangencia.valor" modelProperty="value"/>
			<adsm:propertyMapping relatedProperty="abrangencia.descricao" modelProperty="description"/>
		</adsm:combobox>

		<adsm:textbox dataType="JTDate" property="dataReferencia" label="dataReferencia"/>

		<adsm:combobox width="35%" label="formatoRelatorio"
			property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" 
			serializable="false" required="true" onDataLoadCallBack="setFormatoDefault">
			<adsm:propertyMapping relatedProperty="tpFormatoRelatorio.valor" modelProperty="value"/>
			<adsm:propertyMapping relatedProperty="tpFormatoRelatorio.descricao" modelProperty="description"/>
		</adsm:combobox>

		<adsm:buttonBar>
			<!-- vendas/clientesAtualizacaoManualTabela.jasper -->
			<adsm:reportViewerButton service="lms.vendas.clientesAtualizacaoManualTabelaAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">

function initWindow(eventObj) {
//alert("eventObj.name="+eventObj.name);
	if (eventObj.name == "cleanButton_click"){
		findDadosSessao_cb(null, null);
		setFormatoDefault_cb(null, null);
	}
}

function setFormatoDefault_cb(data, error) {
	if(data) {
		tpFormatoRelatorio_cb(data);
	}
	setElementValue("tpFormatoRelatorio", "pdf");
	ajustaFormatoRelatorio();
}

function ajustaFormatoRelatorio() {
	var combo = document.getElementById("tpFormatoRelatorio");
	setElementValue("tpFormatoRelatorio.descricao", combo.options[combo.selectedIndex].text);
	setElementValue("tpFormatoRelatorio.valor", combo.value);
}

function myOnPageLoad() {
	onPageLoad();
	var pms = document.getElementById("filial.idFilial").propertyMappings;
	var pmsn = new Array();
	for (var i = 2; i < pms.length; i++)
		pmsn[i - 2] = pms[i];
	document.getElementById("filial.idFilial").propertyMappings = pmsn;
}

function findDadosSessao_cb(data, error) {
	if(error != undefined) {
		alert(error);
	} else {
		var sdo = createServiceDataObject("lms.vendas.clientesAtualizacaoManualTabelaAction.findDadosSessao", "ajustaDadosSessao");
		xmit({serviceDataObjects:[sdo]});
	}
}

function ajustaDadosSessao_cb(data, errorMsg, errorKey) {
	if(errorMsg) {
		alert(errorMsg);
		return;
	}
	var dataAtual = "";
	var idFilialSessao = "";
	var nmFantasiaSessao = "";
	var sgFilialSessao = "";
	var idRegionalFilial = "";
	if(data) {
		dataAtual = data.dataAtual;
		idFilialSessao = data.idFilialSessao;
		nmFantasiaSessao = data.nmFantasiaSessao;
		sgFilialSessao = data.sgFilialSessao;
		idRegionalFilial = data.idRegionalFilial;
	}
	setElementValue("regional.idRegional", idRegionalFilial);
	setElementValue("filial.idFilial", idFilialSessao);
	setElementValue("filial.pessoa.nmFantasia", nmFantasiaSessao);
	setElementValue("filial.sgFilial", sgFilialSessao);
	setElementValue("dataReferencia", dataAtual);
	ajustaRegionalSiglaDescricao();
}

function ajustaRegionalSiglaDescricao() {
	var comboRegional = getElement("regional.idRegional");
	if((comboRegional.options.length > 0) && (comboRegional.selectedIndex > 0) ) {
		setElementValue("regional.siglaDescricao", comboRegional.options[comboRegional.selectedIndex].text);
	} else {
		setElementValue("regional.siglaDescricao", "");
		setElementValue("regional.idRegional", 0);
	}
}

function validateTab() {
	if(getElementValue("regional.idRegional") == "" && getElementValue("filial.sgFilial") == "") {
		alertI18nMessage("LMS-30050");
		setFocusOnFirstFocusableField();
		return false;
	}
	return validateTabScript(document.forms);
}




</script>

