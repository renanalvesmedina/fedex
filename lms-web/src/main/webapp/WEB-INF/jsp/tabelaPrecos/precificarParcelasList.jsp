<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	onPageLoad="myOnLoadPage"
	service="lms.tabelaprecos.precificarParcelasAction">

	<adsm:form
		action="/tabelaPrecos/precificarParcelas">

		<adsm:hidden 
			property="tabelaPrecoParcela.idTabelaPrecoParcela"/>

		<adsm:hidden
			serializable="false"
			property="tabelaPrecoParcela.tabelaPreco.blEfetivada"/>
			
		<adsm:hidden
			serializable="false"
			property="tabelaPrecoParcela.tabelaPreco.tpTipoTabelaPreco"/>

		<adsm:complement
			label="tabela"
			labelWidth="22%"
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
        	serializable="false"
        	size="9"
			width="25%"/>

        <adsm:textbox
        	dataType="text"
        	disabled="true"
        	label="parcela"
			labelWidth="22%"
        	property="tabelaPrecoParcela.parcelaPreco.dsParcelaPreco"
        	serializable="false"
        	size="40"
			width="39%"/>

        <adsm:textbox
        	dataType="text"
        	disabled="true"
        	label="tipoParcela"
			labelWidth="14%"
        	property="tabelaPrecoParcela.parcelaPreco.tpParcelaPreco"
        	serializable="false"
        	size="20"
			width="25%"/>

		<adsm:combobox
			domain="DM_CODIGOS_MIN_PROGRESSIVO"
			label="indicadorMinimoProgressivo"
			labelWidth="22%"
			property="cdMinimoProgressivo"
			width="39%"
			boxWidth="160"/>

		<adsm:combobox
			domain="DM_STATUS"
			label="situacao"
			labelWidth="14%"
			property="tpSituacao"
			width="25%"/>

		<adsm:buttonBar
			freeLayout="true">
			<adsm:findButton
				callbackProperty="precificarParcelasResultado"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		idProperty="idFaixaProgressiva"
		gridHeight="200"
		property="precificarParcelasResultado"
		unique="true"
		rows="11"
		showPagging="true"
		showTotalPageCount="true"
		service="lms.tabelaprecos.precificarParcelasAction.findFaixaProgressivaPaginated"
		rowCountService="lms.tabelaprecos.precificarParcelasAction.getRowCount">

		<adsm:gridColumn
			dataType="decimal"
			property="vlFaixaProgressiva"
			title="valorFaixa"
			width="16%"/>

		<adsm:gridColumn
			align="right"
			property="produtoEspecifico.nrTarifaEspecifica"
			title="produto"
			width="15%"/>

		<adsm:gridColumn
			property="cdMinimoProgressivo"
			title="indicadorMinimoProgressivo"
			width="22%"
			isDomain="true"/>

		<adsm:gridColumn
			property="tpIndicadorCalculo"
			title="indicadorCalculo"
			width="20%"
			isDomain="true"/>

		<adsm:gridColumn
			property="unidadeMedida.dsUnidadeMedida"
			title="unidadeMedida"
			width="15%"/>

		<adsm:gridColumn
			title="situacao"
			property="tpSituacao"
			width="12%"
			isDomain="true"/>

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	function initWindow(eventObj){
		var event = eventObj.name;
		if(event == "tab_click"){
			disableTabPrecif(true);
		}
	}
	function disableTabPrecif(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("precif", disabled);
	}

	function myOnLoadPage() {
		onPageLoad();
		var u = new URL(parent.location.href);
		setElementValue("tabelaPrecoParcela.tabelaPreco.blEfetivada", u.parameters["tabelaPrecoParcela.tabelaPreco.blEfetivada"]);
		setElementValue("tabelaPrecoParcela.tabelaPreco.tpTipoTabelaPreco", u.parameters["tabelaPrecoParcela.tabelaPreco.tpTipoTabelaPreco"]);
	}
</script>
