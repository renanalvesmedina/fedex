<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	function loadPage() {
		onPageLoad();
		findButtonScript('faixaValor', this.document.forms[0]);
	}
</script>
<adsm:window onPageLoad="loadPage" service="lms.sgr.faixaDeValorService">
	<adsm:form action="/sgr/manterProcedimentosFaixasValores">
		<adsm:hidden property="enquadramentoRegra.idEnquadramentoRegra" />
		<adsm:hidden property="enquadramentoRegra.moeda.dsSimbolo" serializable="false" />
		<adsm:hidden  property="clienteEnquadramentosLength" />

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

		<adsm:buttonBar freeLayout="true" />
	</adsm:form>

	<adsm:grid
		defaultOrder="vlLimiteMinimo:asc"
		gridHeight="280"
		idProperty="idFaixaDeValor"
		property="faixaValor"
		rows="14"
		selectionMode="check"
		unique="true"
	>
		<adsm:gridColumn dataType="text" property="enquadramentoRegra.moeda.siglaSimbolo" title="moeda" width="12%" />
		<adsm:gridColumn align="right" dataType="currency" property="vlLimiteMinimo" title="limiteInicial" width="20%" />
		<adsm:gridColumn align="right" dataType="currency" property="vlLimiteMaximo" title="limiteFinal" width="20%" />
		<adsm:gridColumn align="center" property="blRequerLiberacaoCemop" renderMode="image-check" title="requerLiberacaoCMOP" width="16%" />
		<adsm:gridColumn property="blExclusivaAeroporto" renderMode="image-check" title="exclusivaAeroporto" width="16%" />
		<adsm:gridColumn property="blExclusivaCliente" renderMode="image-check" title="cargaExclusiva" width="16%" />

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			desabilitaTabDescricaoPadrao(true);
		}
	}

	function desabilitaTabDescricaoPadrao(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("exigencias", disabled);
		tabGroup.setDisabledTab("naturezas", disabled);
	}
</script>
