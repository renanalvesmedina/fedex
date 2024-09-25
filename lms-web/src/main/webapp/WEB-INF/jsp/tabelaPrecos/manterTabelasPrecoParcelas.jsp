<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	function pageLoad(){
		onPageLoad();
		prepareParcelas();
	}
</script>
<adsm:window
	service="lms.tabelaprecos.tabelaPrecoParcelaService"
	onPageLoad="pageLoad">

	<adsm:form
		action="/tabelaPrecos/manterTabelasPreco"
		onDataLoadCallBack="formCallback"
		idProperty="idTabelaPrecoParcela"
		height="70">

		<adsm:hidden
			property="tabelaPreco.idTabelaPreco"/>

		<adsm:complement
			label="tabelaBase"
			labelWidth="15%"
			width="35%">

			<adsm:textbox
				dataType="text"
				property="tabelaPreco.tabelaPrecoString"
				size="8"
				maxLength="7"
				disabled="true"
				serializable="false"/>

			<adsm:textbox
				dataType="text"
				maxLength="60"
				property="tabelaPreco.dsDescricao"
			 	size="30"
			 	disabled="true"
			 	serializable="false"/>

		</adsm:complement>

		<adsm:textbox
			label="moeda"
			labelWidth="15%"
			width="35%"
			serializable="false"
			property="tabelaPreco.moeda.dsSimbolo"
			dataType="text"
			disabled="true"
			size="10"
			maxLength="60"/>

		<adsm:combobox
			labelWidth="15%"
			width="35%"
			boxWidth="160"
			property="parcelaPreco.idParcelaPreco"
			label="parcela"
			optionLabelProperty="nmParcelaPreco"
			optionProperty="idParcelaPreco"
			service="lms.tabelaprecos.parcelaPrecoService.findParcelaPreco"
			required="true">

			<adsm:propertyMapping
				relatedProperty="parcelaPreco.tpParcelaPreco"
				modelProperty="tpParcelaPreco" />

			<adsm:propertyMapping
				relatedProperty="parcelaPreco.tpParcelaPreco.description"
				modelProperty="tpParcelaPreco.description" />

			<adsm:propertyMapping
				relatedProperty="parcelaPreco.tpIndicadorCalculo"
				modelProperty="tpIndicadorCalculo" />

			<adsm:propertyMapping
				relatedProperty="parcelaPreco.tpIndicadorCalculo.description"
				modelProperty="tpIndicadorCalculo.description" />

			<adsm:propertyMapping
				relatedProperty="parcelaPreco.tpPrecificacao"
				modelProperty="tpPrecificacao" />
		</adsm:combobox>

		<adsm:textbox
			labelWidth="15%"
			width="35%"
			label="tipoParcela"
			serializable="false"
			property="parcelaPreco.tpParcelaPreco.description"
			dataType="text"
			disabled="true"
			size="15"
			maxLength="15"/>

		<adsm:hidden
			property="parcelaPreco.tpPrecificacao" />

		<adsm:hidden
			property="parcelaPreco.tpParcelaPreco" />

		<adsm:hidden
			property="parcelaPreco.tpIndicadorCalculo" />

		<adsm:hidden
			property="parcelaPreco.tpIndicadorCalculo.description" />

		<adsm:buttonBar
			freeLayout="true">
			<adsm:button
				caption="precificarParcela"
				onclick="precificar();"
				id="precificarParcela"/>

			<adsm:button
				caption="novaParcela"
				onclick="novaParcela();"
				buttonType="newButton"
				disabled="false"
				id="novaParc" />

			<adsm:storeButton
				id="salvarParcela"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		autoSearch="false"
		onRowClick="populaForm"
		idProperty="idTabelaPrecoParcela"
		defaultOrder="parcelaPreco_.nmParcelaPreco"
		property="parcelaPreco"
		unique="true"
		rows="11"
		gridHeight="143">

		<adsm:gridColumn
			title="parcela"
			property="parcelaPreco.nmParcelaPreco"
			width="50%" />

		<adsm:gridColumn
			title="tipoParcela"
			property="parcelaPreco.tpParcelaPreco"
			isDomain="true"
			width="50%" />

		<adsm:buttonBar>
			<adsm:removeButton
				caption="excluirParcela"
				id="excluirParcela"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	var tabelaPreco = [];

	function prepareParcelas() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		var idTabelaPreco = tabDet.getFormProperty("idTabelaPreco");
		if(idTabelaPreco != undefined && idTabelaPreco != '') {
			var frame = parent.document.frames["cad_iframe"];

			var data = frame.getDadosTabela();
			setNestedBeanPropertyValue(tabelaPreco, "tabelaPrecoString", data.sgTabelaPreco);
			setElementValue("tabelaPreco.tabelaPrecoString", data.sgTabelaPreco);

			setElementValue("tabelaPreco.dsDescricao", data.dsTabelaPreco);
			setNestedBeanPropertyValue(tabelaPreco, "dsDescricao", data.dsTabelaPreco);

			setElementValue("tabelaPreco.idTabelaPreco", idTabelaPreco);
			setNestedBeanPropertyValue(tabelaPreco, "idTabelaPreco", idTabelaPreco);

			setElementValue("tabelaPreco.moeda.dsSimbolo", data.moeda.sgMoeda);
			setNestedBeanPropertyValue(tabelaPreco, "moeda.dsSimbolo", data.moeda.sgMoeda);

			setNestedBeanPropertyValue(tabelaPreco, "blEfetivada", data.blEfetivada);
			setNestedBeanPropertyValue(tabelaPreco, "idPendencia", data.idPendencia);
			setNestedBeanPropertyValue(tabelaPreco, "tpTipoTabelaPreco", data.tpTipoTabelaPreco);

			populaGrid();
			novaParcela();
		}
	}

	function populaGrid() {
		parcelaPrecoGridDef.executeSearch({tabelaPreco:{idTabelaPreco:getElementValue("tabelaPreco.idTabelaPreco")}}, true);
	}

	function populaForm(valor) {
		onDataLoad(valor);

		setElementValue("tabelaPreco.tabelaPrecoString", getNestedBeanPropertyValue(tabelaPreco, "tabelaPrecoString"));
		setElementValue("tabelaPreco.moeda.dsSimbolo", getNestedBeanPropertyValue(tabelaPreco, "moeda.dsSimbolo"));
		setElementValue("tabelaPreco.dsDescricao", getNestedBeanPropertyValue(tabelaPreco, "dsDescricao"));
		setElementValue("tabelaPreco.idTabelaPreco", getNestedBeanPropertyValue(tabelaPreco, "idTabelaPreco"));
		return false;
	}

	function formCallback_cb(dados, erros){
		onDataLoad_cb(dados, erros);
		disableControls(true);
	}

	function disableControls(disabled) {
		var blEfetivada = getNestedBeanPropertyValue(tabelaPreco, "blEfetivada");
		var idPendencia = getNestedBeanPropertyValue(tabelaPreco, "idPendencia");
		var tpTipoTabelaPreco = getNestedBeanPropertyValue(tabelaPreco, "tpTipoTabelaPreco");
		if((blEfetivada && tpTipoTabelaPreco != "C") || idPendencia != "" || (getIdProcessoWorkflow() != undefined && getIdProcessoWorkflow() != '')) {
			setDisabled("salvarParcela", true);
			setDisabled("novaParc", true);
		} else {
			setDisabled("salvarParcela", disabled);
		}
		setDisabled("parcelaPreco.idParcelaPreco", disabled);
		setDisabled("precificarParcela", !disabled);
	}

	function novaParcela() {
		resetValue("idTabelaPrecoParcela");
		resetValue("parcelaPreco.idParcelaPreco");
		disableControls(false);
		setFocusOnFirstFocusableField(document);
	}

	function initWindow(eventObj) {
		var event = eventObj.name;
		if(event == "removeButton_grid" || event == "tab_click"){
			prepareParcelas();
		} else if(event == "storeButton"){
			disableControls(true)
			populaGrid();
		}
    }

	function precificar() {
		var tpPrecificacao = getElementValue("parcelaPreco.tpPrecificacao");
		var url = '';
		switch(tpPrecificacao) {
			case "G":
				//Generalidade
				url += "/tabelaPrecos/precificarParcelaGeneralidade.do";
				break;
			case "T":
				//Taxa
				url += "/tabelaPrecos/precificarParcelaTaxa.do";
				break;
			case "S":
				//Serviço Adicional
				url += "/tabelaPrecos/precificarParcelaServicoAdicional.do";
				break;
			case "P":
				//Preço Frete
				url += "/tabelaPrecos/manterPrecosFretes.do";
				break;
			case "M":
				//Mínimo Progressivo
				url += "/tabelaPrecos/precificarParcelas.do" + getQuery();
				redirectPrec(url, '');
				return;
			default:
				return false;
		}
		var qs = "?cmd=main";
		qs += "&idTabelaPrecoParcela=" + getElementValue("idTabelaPrecoParcela");
		qs += "&tabelaPreco.tabelaPrecoString=" + getNestedBeanPropertyValue(tabelaPreco, "tabelaPrecoString");
		qs += "&tabelaPreco.tpTipoTabelaPreco=" + getNestedBeanPropertyValue(tabelaPreco, "tpTipoTabelaPreco");
		qs += "&tabelaPreco.dsDescricao=" + getNestedBeanPropertyValue(tabelaPreco, "dsDescricao");
		qs += "&tabelaPreco.blEfetivada=" + getNestedBeanPropertyValue(tabelaPreco, "blEfetivada");
		qs += "&tabelaPreco.idPendencia=" + getNestedBeanPropertyValue(tabelaPreco, "idPendencia");
		qs += "&tabelaPreco.moeda.dsSimbolo=" + getNestedBeanPropertyValue(tabelaPreco, "moeda.dsSimbolo");
		var parcelaPreco = document.getElementById("parcelaPreco.idParcelaPreco")
		qs += "&parcelaPreco.nmParcela=" + parcelaPreco.options[parcelaPreco.selectedIndex].text;
		qs += "&parcelaPreco.tpParcelaPreco.value=" + getElementValue("parcelaPreco.tpParcelaPreco");
		qs += "&parcelaPreco.tpParcelaPreco.description=" + getElementValue("parcelaPreco.tpParcelaPreco.description");
		qs += "&parcelaPreco.tpIndicadorCalculo.value=" + getElementValue("parcelaPreco.tpIndicadorCalculo");
		qs += "&parcelaPreco.tpIndicadorCalculo.description=" + getElementValue("parcelaPreco.tpIndicadorCalculo.description");
		qs += "&isVisualizacaoWK=" + isVisualizacaoWK();

		redirectPrec(url, qs);
	}

	function getQuery(){
		var qs = "?cmd=main";
		qs += "&tabelaPrecoParcela.idTabelaPrecoParcela=" + getElementValue("idTabelaPrecoParcela");
		qs += "&tabelaPrecoParcela.tabelaPreco.tabelaPrecoString=" + getNestedBeanPropertyValue(tabelaPreco, "tabelaPrecoString");
		qs += "&tabelaPrecoParcela.tabelaPreco.dsDescricao=" + getNestedBeanPropertyValue(tabelaPreco, "dsDescricao");
		qs += "&tabelaPrecoParcela.tabelaPreco.blEfetivada=" + getNestedBeanPropertyValue(tabelaPreco, "blEfetivada");
		qs += "&tabelaPrecoParcela.tabelaPreco.idPendencia=" + getNestedBeanPropertyValue(tabelaPreco, "idPendencia");
		qs += "&tabelaPrecoParcela.tabelaPreco.moeda.sgMoeda=" + getNestedBeanPropertyValue(tabelaPreco, "moeda.dsSimbolo");
		qs += "&tabelaPrecoParcela.tabelaPreco.tpTipoTabelaPreco=" + getNestedBeanPropertyValue(tabelaPreco, "tpTipoTabelaPreco");
		var parcelaPreco = document.getElementById("parcelaPreco.idParcelaPreco")
		qs += "&tabelaPrecoParcela.parcelaPreco.dsParcelaPreco=" + parcelaPreco.options[parcelaPreco.selectedIndex].text;
		qs += "&tabelaPrecoParcela.parcelaPreco.tpParcelaPreco=" + getElementValue("parcelaPreco.tpParcelaPreco.description");
		qs += "&isVisualizacaoWK=" + isVisualizacaoWK();
		return qs;
	}

	function redirectPrec(url, qs) {
	 	if (getIdProcessoWorkflow() != undefined && getIdProcessoWorkflow() != '') {
			showModalDialog(url + qs, window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
		} else {
			var urlBase = getCurrentBaseURL();
			parent.parent.redirectPage(urlBase + url + qs);
		}
	}

	function isVisualizacaoWK(){
		return getIdProcessoWorkflow() != undefined && getIdProcessoWorkflow() != '';
	}

	function getIdProcessoWorkflow() {
		var url = new URL(parent.location.href);
		return url.parameters["idProcessoWorkflow"];
	}

	function disableButtonsWK() {
		if (getIdProcessoWorkflow() != undefined && getIdProcessoWorkflow() != '') {
			setDisabled("excluirParcela", true);
			setDisabled("novaParc", true);
			setDisabled("salvarParcela", true);
		}
	}
</script>