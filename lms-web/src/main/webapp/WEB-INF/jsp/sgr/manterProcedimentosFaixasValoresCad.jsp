<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.faixaDeValorService">
	<adsm:form action="/sgr/manterProcedimentosFaixasValores" height="250" idProperty="idFaixaDeValor">
		<adsm:textbox
			dataType="text"
			disabled="true"
			label="enquadramentoRegra"
			labelWidth="20%"
			maxLength="100"
			property="enquadramentoRegra.dsEnquadramentoRegra"
			serializable="false"
			size="100"
			width="80%"
		/>

		<adsm:hidden property="enquadramentoRegra.idEnquadramentoRegra" />
		<adsm:hidden property="enquadramentoRegra.moeda.idMoeda" />

		<adsm:textbox
			dataType="text"
			disabled="true"
			label="limites"
			labelWidth="20%"
			property="enquadramentoRegra.moeda.siglaSimbolo"
			serializable="false"
			size="8"
			width="8%"
		/>
		<adsm:range width="72%">
			<adsm:textbox dataType="currency" mask="#,###,###,##0.00" property="vlLimiteMinimo" required="true" size="15" />
			<adsm:textbox dataType="currency" mask="#,###,###,##0.00" property="vlLimiteMaximo" size="15" />
		</adsm:range>
		<adsm:checkbox label="requerLiberacaoCMOP" labelWidth="20%" property="blRequerLiberacaoCemop" width="80%" />
		<adsm:checkbox label="exclusivaAeroporto" labelWidth="20%" property="blExclusivaAeroporto" width="80%" />
		<adsm:checkbox label="cargaExclusiva" labelWidth="20%" property="blExclusivaCliente" width="80%" />

		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	function initWindow(eventObj) {
		var event = eventObj.name;
		if (event == "removeButton" || event == "newButton_click") {
			desabilitaTabDescricaoPadrao(true);
		} else if (event == "gridRow_click" || eventObj.name == "storeButton") {
			desabilitaTabDescricaoPadrao(false);
		}

		getElement("vlLimiteMinimo").label = getElement("enquadramentoRegra.moeda.siglaSimbolo").label;

		var tab = getTabGroup(this.document).getTab("pesq");
		var prop = parseInt(tab.getFormProperty("clienteEnquadramentosLength"));
		setDisabled("blExclusivaCliente", !prop);
	}

	function desabilitaTabDescricaoPadrao(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("exigencias", disabled);
		tabGroup.setDisabledTab("naturezas", disabled);
	}

	function verificaDescricaoPadrao() {
		var idMotivoAberturaNc = getElementValue("idFaixaDeValor");
		if (idMotivoAberturaNc != "" && idMotivoAberturaNc != undefined) {
			desabilitaTabDescricaoPadrao(false);
		} else {
			desabilitaTabDescricaoPadrao(true);
		}
	}
</script>
