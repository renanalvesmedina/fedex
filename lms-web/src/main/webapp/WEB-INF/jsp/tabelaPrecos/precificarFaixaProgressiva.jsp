<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/tabelaPrecos/precificarParcelas">

		<adsm:i18nLabels>
			<adsm:include key="LMS-30014"/>
			<adsm:include key="LMS-30018"/>
			<adsm:include key="LMS-30020"/>
			<adsm:include key="LMS-30021"/>
			<adsm:include key="requiredField"/>
		</adsm:i18nLabels>

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
			labelWidth="6%"
			width="32%">
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
				size="30"/>
		</adsm:complement>

		<adsm:textbox
			label="moeda"
			property="tabelaPrecoParcela.tabelaPreco.moeda.sgMoeda"
			dataType="text"
			labelWidth="12%"
			width="20%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			label="valorFaixa"
			property="vlFaixaProgressiva"
			dataType="currency"
			labelWidth="14%"
			width="16%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			label="parcela"
			property="tabelaPrecoParcela.parcelaPreco.dsParcelaPreco"
			dataType="text"
			labelWidth="6%"
			width="32%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			label="tipoParcela"
			property="tabelaPrecoParcela.parcelaPreco.tpParcelaPreco"
			dataType="text"
			labelWidth="12%"
			width="20%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			label="produtoEspecifico"
			property="produtoEspecifico.nrTarifaEspecifica"
			dataType="text"
			labelWidth="14%"
			width="16%"
			disabled="true"
			serializable="false"/>
	</adsm:form>

	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tabelaPrecos/precificarParcelas" cmd="precifList" height="348"/>
		<adsm:tab title="detalhamento" id="cad" src="/tabelaPrecos/precificarParcelas" cmd="precifCad" height="348"/>
	</adsm:tabGroup>
</adsm:window>
<script type="text/javascript">
	/**
	* Captura o evento de um clique na tab com o objetivo de ajustar a habilitação de
	* vigência promocional.
	*/
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click"
			|| eventObj.name == "storeItemButton"
			|| eventObj.name == "removeButton_grid"){
			estadoNovo();
		}
	}

	/**
	* Restaura o estado novo da tela
	*/
	function estadoNovo(){
		var tabGroup = getTabGroup(this.document);
		var cadDocument = tabGroup.getTab("cad").getDocument();

		setElementValue("tabelaPrecoParcela.tabelaPreco.tabelaPrecoString", getElement("tabelaPrecoParcela.tabelaPreco.tabelaPrecoString", cadDocument).value);
		setElementValue("tabelaPrecoParcela.tabelaPreco.dsDescricao", getElement("tabelaPrecoParcela.tabelaPreco.dsDescricao", cadDocument).value);
		setElementValue("tabelaPrecoParcela.tabelaPreco.moeda.sgMoeda", getElement("tabelaPrecoParcela.tabelaPreco.moeda.sgMoeda", cadDocument).value);
		setElementValue("vlFaixaProgressiva", getElement("vlFaixaProgressiva", cadDocument).value);
		setElementValue("tabelaPrecoParcela.parcelaPreco.dsParcelaPreco", getElement("tabelaPrecoParcela.parcelaPreco.dsParcelaPreco", cadDocument).value);
		setElementValue("tabelaPrecoParcela.parcelaPreco.tpParcelaPreco", getElement("tabelaPrecoParcela.parcelaPreco.tpParcelaPreco", cadDocument).value);
		setElementValue("produtoEspecifico.nrTarifaEspecifica", getElement("produtoEspecifico.nrTarifaEspecifica", cadDocument).value);

		setElementValue("tabelaPrecoParcela.tabelaPreco.blEfetivada", getElement("tabelaPrecoParcela.tabelaPreco.blEfetivada", cadDocument).value);
		setElementValue("tabelaPrecoParcela.tabelaPreco.idPendencia", getElement("tabelaPrecoParcela.tabelaPreco.idPendencia", cadDocument).value);
		setElementValue("tabelaPrecoParcela.tabelaPreco.tpTipoTabelaPreco", getElement("tabelaPrecoParcela.tabelaPreco.tpTipoTabelaPreco", cadDocument).value);
		setElementValue("isVisualizacaoWK", getElement("isVisualizacaoWK", cadDocument).value);
	}

	function getParametrosTabelaPreco() {
		var data = {tabelaPrecoParcela: {tabelaPreco: {}}};
		data.tabelaPrecoParcela.tabelaPreco.blEfetivada = getElementValue("tabelaPrecoParcela.tabelaPreco.blEfetivada");
		data.tabelaPrecoParcela.tabelaPreco.idPendencia = getElementValue("tabelaPrecoParcela.tabelaPreco.idPendencia");
		data.tabelaPrecoParcela.tabelaPreco.tpTipoTabelaPreco = getElementValue("tabelaPrecoParcela.tabelaPreco.tpTipoTabelaPreco");
		data.isVisualizacaoWK = getElementValue("isVisualizacaoWK");
		return data;
	}
</script>