<%@taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.tabelaprecos.parcelaPrecoService" onPageLoadCallBack="inicializaPagina">
	<adsm:form action="/tabelaPrecos/manterParcelasPreco" idProperty="idParcelaPreco" onDataLoadCallBack="inicializaDados">
		<adsm:hidden property="tpSituacaoUnidade" value="A"/>
		<adsm:combobox
			label="tipoParcela"
			property="tpParcelaPreco"
			domain="DM_TIPO_PARCELA"
			labelWidth="17%"
			width="33%"
			required="true"
			onchange="verificaTipoParcelaPreco();"/>
		<adsm:combobox
			label="servicoAdicional"
			property="servicoAdicional.idServicoAdicional"
			service="lms.configuracoes.servicoAdicionalService.find"
			optionProperty="idServicoAdicional"
			optionLabelProperty="dsServicoAdicional"
			labelWidth="17%"
			width="33%"
			boxWidth="200"
			disabled="true"/>

		<adsm:combobox
			label="tipoDePrecificacao"
			property="tpPrecificacao"
			domain="DM_INDICADOR_CALCULO_PARCELA"
			labelWidth="17%"
			width="33%"
			required="true"/>
		<adsm:textbox
			label="parcela"
			dataType="text"
			property="nmParcelaPreco"
			labelWidth="17%"
			width="33%"
			maxLength="60"
			size="33"
			required="true"/>

		<adsm:textbox
			label="descricao"
			dataType="text"
			property="dsParcelaPreco"
			labelWidth="17%"
			width="33%"
			maxLength="60"
			size="33"
			required="true"/>
		<adsm:checkbox
			label="incideICMS"
			property="blIncideIcms"
			labelWidth="17%"
			width="33%"/>

		<adsm:combobox
			label="indicadorCalculo"
			property="tpIndicadorCalculo"
			domain="DM_INDICADORES_CALCULO"
			labelWidth="17%"
			width="33%"
			required="true"
			onchange="verificaTipoIndicadorCalculo();"/>
		<adsm:combobox
			label="unidadeMedida"
			property="unidadeMedida.idUnidadeMedida"
			service="lms.tabelaprecos.unidadeMedidaService.find"
			optionProperty="idUnidadeMedida"
			optionLabelProperty="dsUnidadeMedida"
			labelWidth="17%"
			width="33%"
			boxWidth="200"
			disabled="true"
			onlyActiveValues="true"/>

		<adsm:textbox
			label="codigo"
			dataType="text"
			property="cdParcelaPreco"
			labelWidth="17%"
			width="33%"
			maxLength="30"
			size="33"
			required="true"/>
		<adsm:checkbox
			label="embutirParcela"
			property="blEmbuteParcela"
			labelWidth="17%"
			width="33%" />

		<adsm:combobox
			property="tpSituacao"
			label="situacao"
			domain="DM_STATUS"
			labelWidth="17%"
			width="33%"
			required="true"/>

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:button buttonType="newButton" caption="limpar" id="novo" onclick="myNewButtonScript();"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	function initWindow(eventObj) {
		if(eventObj.name == "tab_click" || eventObj.name == "removeButton") {
			myNewButtonScript();
		}
	}
	function inicializaPagina_cb(data, erro) {
		onPageLoad_cb(data, erro);
		setDisabled("novo", false);
	}
	function inicializaDados_cb(data, erro) {
		onDataLoad_cb(data, erro);
		verificaTipoParcelaPreco();
		verificaTipoIndicadorCalculo();
		setDisabled("cdParcelaPreco", true);
	}
	function myNewButtonScript() {
		newButtonScript(this.document);
		verificaTipoParcelaPreco();
		verificaTipoIndicadorCalculo();
		setElementValue("tpSituacao", "A");
		setDisabled("cdParcelaPreco", false);
	}

	function verificaTipoParcelaPreco() {
		var isServicoAdicional = (getElementValue("tpParcelaPreco") == "S");
		var obj = getElement("servicoAdicional.idServicoAdicional");
		setDisabled(obj, !isServicoAdicional);
		if(!isServicoAdicional) {
			setElementValue(obj, "");
		}
		obj.required = isServicoAdicional ? "true" : "false";
		if (getElementValue("tpParcelaPreco") == "G"){
			setDisabled("blEmbuteParcela", false);
		} else{
			resetValue("blEmbuteParcela");
			setDisabled("blEmbuteParcela", true);
		}
	}

	function verificaTipoIndicadorCalculo() {
		var isUnidadeMedida = (getElementValue("tpIndicadorCalculo") == "VU");
		var obj = getElement("unidadeMedida.idUnidadeMedida");
		setDisabled(obj, !isUnidadeMedida);
		if(!isUnidadeMedida) {
			setElementValue(obj, "");
		}
		obj.required = isUnidadeMedida ? "true" : "false";
	}
</script>