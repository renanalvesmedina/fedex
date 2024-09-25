<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	onPageLoad="myOnPageLoad"
	service="lms.tabelaprecos.precificarParcelasAction">
	<adsm:form
		action="/tabelaPrecos/precificarParcelas"
		idProperty="idFaixaProgressiva" onDataLoadCallBack="myOnDataLoadCallBack">

		<adsm:hidden
			property="tabelaPrecoParcela.idTabelaPrecoParcela"/>
		<adsm:hidden
			property="versao"/>

		<adsm:hidden
			serializable="false"
			property="tabelaPrecoParcela.tabelaPreco.blEfetivada"/>

		<adsm:hidden
			serializable="false"
			property="tabelaPrecoParcela.tabelaPreco.idPendencia"/>

		<adsm:hidden
			serializable="false"
			property="isVisualizacaoWK"/>

		<adsm:hidden
			serializable="false"
			property="tabelaPrecoParcela.tabelaPreco.tpTipoTabelaPreco"/>

		<adsm:complement
			label="tabela"
			labelWidth="22%"
			required="true"
			width="39%">

			<adsm:textbox
				dataType="text"
				disabled="true"
				property="tabelaPrecoParcela.tabelaPreco.tabelaPrecoString"
				serializable="false"
				size="8"
				maxLength="7" />

			<adsm:textbox
				dataType="text"
				disabled="true"
				property="tabelaPrecoParcela.tabelaPreco.dsDescricao"
				serializable="false"
				size="40"/>
		</adsm:complement>

        <adsm:textbox
        	dataType="text"
        	disabled="true"
        	label="moeda"
			labelWidth="14%"
        	property="tabelaPrecoParcela.tabelaPreco.moeda.sgMoeda"
        	required="true"
        	size="9"
			width="25%"/>

        <adsm:textbox
        	dataType="text"
        	disabled="true"
        	label="parcela"
			labelWidth="22%"
        	property="tabelaPrecoParcela.parcelaPreco.dsParcelaPreco"
        	required="true"
        	size="40"
			width="39%"/>

        <adsm:textbox
        	dataType="text"
        	disabled="true"
        	label="tipoParcela"
			labelWidth="14%"
        	property="tabelaPrecoParcela.parcelaPreco.tpParcelaPreco"
        	required="true"
        	size="20"
			width="25%"/>

        <adsm:textbox
        	dataType="currency"
        	label="valorFaixa"
        	labelWidth="22%"
			mask="###,###,###,###,##0.00"
        	minValue="0.01"
        	property="vlFaixaProgressiva"
        	size="18"
        	width="39%"/>

		<adsm:combobox
			label="produtoEspecifico"
			labelWidth="14%"
			onlyActiveValues="true"
			optionLabelProperty="nrTarifaEspecifica"
			optionProperty="idProdutoEspecifico"
			property="produtoEspecifico.idProdutoEspecifico"
			service="lms.tabelaprecos.produtoEspecificoService.find"
			width="25%"
			boxWidth="160">

			<adsm:propertyMapping
				relatedProperty="produtoEspecifico.nrTarifaEspecifica"
				modelProperty="nrTarifaEspecifica"/>
		</adsm:combobox>

		<adsm:hidden
			property="produtoEspecifico.nrTarifaEspecifica"/>

		<adsm:combobox
			domain="DM_CODIGOS_MIN_PROGRESSIVO"
			label="indicadorMinimoProgressivo"
			labelWidth="22%"
			onlyActiveValues="true"
			property="cdMinimoProgressivo"
			required="true"
			width="39%"
			boxWidth="160"/>

		<adsm:combobox
			domain="DM_INDICADORES_CALCULO"
			label="indicadorCalculo"
			labelWidth="14%"
			onchange="onChangeIndicadorCalculo();"
			onlyActiveValues="true"
			property="tpIndicadorCalculo"
			required="true"
			width="25%"
			boxWidth="160"/>

		<adsm:combobox
			label="unidadeMedida"
			labelWidth="22%"
			onlyActiveValues="true"
			optionLabelProperty="dsUnidadeMedida"
			optionProperty="idUnidadeMedida"
			property="unidadeMedida.idUnidadeMedida"
			service="lms.tabelaprecos.unidadeMedidaService.find"
			width="39%"
			boxWidth="130"/>

		<adsm:combobox
			domain="DM_STATUS"
			label="situacao"
			labelWidth="14%"
			property="tpSituacao"
			required="true"
			width="25%"/>

		<adsm:buttonBar>
			<adsm:storeButton
				caption="salvar"
				id="storeButton"
				service="lms.tabelaprecos.precificarParcelasAction.store"
				callbackProperty="storeCallBack"/>

			<adsm:button
				caption="novo"
				id="newButton"
				buttonType="newButton"
				onclick="newButtonScript(this.document, true, {name:'cleanButton_click'}); onChangeIndicadorCalculo();"/>

			<adsm:removeButton
				id="btnExcluir"/>
		</adsm:buttonBar>

		<adsm:i18nLabels>
			<adsm:include key="LMS-30012"/>
		</adsm:i18nLabels>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
	getElement("tabelaPrecoParcela.tabelaPreco.dsDescricao").required=false;

	function storeCallBack_cb(data, error, key) {
		if (error != undefined && error != "" && error != null) {
			alert(error);
		} else {
			store_cb(data, error);
			if (data.msgAtualizacaoAutomatica != undefined){
				alert(data.msgAtualizacaoAutomatica)
			}
		}
	}

	function initWindow(eventObj) {
		var event = eventObj.name;
		var tabGroup = getTabGroup(this.document);
		var idTab = tabGroup.oldSelectedTab.properties.id;

		if(event != "tab_click" && idTab != "cad"){
			if((event != "storeButton" && event != "gridRow_click")) {
				disableTabPrecif(true);
			} else {
				disableTabPrecif(false);
			}
		} else if(event == "removeButton") {
			disableTabPrecif(true);
		} else if(event == "storeButton") {
			disableTabPrecif(false);
		} else if (event == "tab_click"){
			onChangeIndicadorCalculo();
		}
		controlDisabledButtons();

		/* Controle para autoSearch da Grid de ValorFaixaProgressiva */
		if(event != "tab_click" && event != "cleanButton_click") {
			var innerDocument = getTabGroup(document)._tabs.precif.childTabGroup.getTab("pesq").getDocument();
			innerDocument.getElementById("precificarParcelasResultado.dataTable").gridDefinition.resetGrid();
			resetValue(innerDocument);
		}
		/* Controle para troca de abas entre Precificação >> Detalhamento */
		if(getElementValue("idFaixaProgressiva") != "") {
			setDisabled("btnExcluir", false);
		}
	}

	function disableTabPrecif(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("precif", disabled);
	}

	function controlDisabledButtons(){
		if((getElementValue("tabelaPrecoParcela.tabelaPreco.blEfetivada") == "true" && getElementValue("tabelaPrecoParcela.tabelaPreco.tpTipoTabelaPreco") != "C")
				|| getElementValue("tabelaPrecoParcela.tabelaPreco.idPendencia") != "" || getElementValue("isVisualizacaoWK") == "true"){
			setDisabled('storeButton', true);
		}
	}

	function runValidation1(){
		var valorFaixa = getElementValue("vlFaixaProgressiva");
		var produtoEspecifico = getElementValue("produtoEspecifico.idProdutoEspecifico");

		if((valorFaixa == "" && produtoEspecifico=="") || (valorFaixa!="" && produtoEspecifico!="")){
			alert(i18NLabel.getLabel('LMS-30012'));
			return false;
		}
		return true;
	}

	function validateTab() {
		return validateTabScript(document.forms) && runValidation1();
	}

	function enableUnidadeMedida(flag){
		if(flag){
			setDisabled("unidadeMedida.idUnidadeMedida",false);
			getElement("unidadeMedida.idUnidadeMedida").required = "true";
		}else{
			setElementValue("unidadeMedida.idUnidadeMedida","");
			setDisabled("unidadeMedida.idUnidadeMedida",true);
			getElement("unidadeMedida.idUnidadeMedida").required = "false";
		}
	}

	function onChangeIndicadorCalculo(){
		var indicadorCalculo = getElementValue("tpIndicadorCalculo");
		enableUnidadeMedida(indicadorCalculo == "VU");
	}

	function myOnPageLoad(){
		onPageLoad();
		var u = new URL(parent.location.href);
		setElementValue("tabelaPrecoParcela.tabelaPreco.blEfetivada", u.parameters["tabelaPrecoParcela.tabelaPreco.blEfetivada"]);
		setElementValue("tabelaPrecoParcela.tabelaPreco.idPendencia", u.parameters["tabelaPrecoParcela.tabelaPreco.idPendencia"]);
		setElementValue("tabelaPrecoParcela.tabelaPreco.tpTipoTabelaPreco", u.parameters["tabelaPrecoParcela.tabelaPreco.tpTipoTabelaPreco"]);
		setElementValue("isVisualizacaoWK", u.parameters["isVisualizacaoWK"]);
	}

	function myOnDataLoadCallBack_cb(data, erro){
		onDataLoad_cb(data, erro);
		controlDisabledButtons();
	}
</script>