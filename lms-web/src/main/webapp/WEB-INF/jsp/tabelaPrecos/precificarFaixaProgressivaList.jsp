<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<%-- Parametro utilizado por rotaPreco.jsp --%>
<% Boolean blActiveValues = false; %>
<adsm:window service="lms.tabelaprecos.precificarParcelasAction">
	<adsm:form
		action="/tabelaPrecos/precificarParcelas"
		height="100">

		<adsm:i18nLabels>
			<adsm:include key="LMS-30017"/>
			<adsm:include key="LMS-00055"/>
			<adsm:include key="2filtrosObrigatorios"/>
			<adsm:include key="zonaOrigem"/>
			<adsm:include key="zonaDestino"/>
		</adsm:i18nLabels>

		<adsm:hidden property="executeSearch" value="false" serializable="false"/>
		<adsm:hidden property="idFaixaProgressiva"/>
		<adsm:hidden property="geral.tpSituacao" value="A" serializable="false"/>
		<adsm:hidden property="tpAcesso" value="A" serializable="false" />
		<!-- Campo hidden para carregar dados da popup de uma lookup -->
		<adsm:hidden property="origemString" serializable="false"/>
		<adsm:hidden property="destinoString" serializable="false"/>

		<adsm:lookup
			label="tarifa"
			property="tarifaPreco"
			idProperty="idTarifaPreco"
			criteriaProperty="cdTarifaPreco"
			action="/tabelaPrecos/manterTarifasPreco"
			service="lms.tabelaprecos.tarifaPrecoService.findLookup"
			exactMatch="true"
			dataType="text"
			labelWidth="16%"
			maxLength="5"
			size="5"
			width="84%">
			<adsm:propertyMapping
				criteriaProperty="geral.tpSituacao"
				modelProperty="tpSituacao"/>
		</adsm:lookup>

		<%-- Include do JSP que contem os campos das rotas de origem e destino --%>
		<%@ include file="rotaPreco.jsp" %>
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" labelWidth="16%" label="situacao"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton
				onclick="setPropertyValues();"
				callbackProperty="precificarParcelasResultado"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		idProperty="idValorFaixaProgressiva"
		property="precificarParcelasResultado"
		detailFrameName="cad"
		autoSearch="false"
		unique="false"
		service="lms.tabelaprecos.precificarParcelasAction.findPaginatedValorFaixaProgressiva"
		rowCountService="lms.tabelaprecos.precificarParcelasAction.getRowCountValorFaixaProgressiva"
		scrollBars="horizontal"
		gridHeight="87"
		rows="4">

		<adsm:gridColumn
			title="rotaOrigem"
			property="rotaPreco.origemString"
			width="150"/>

		<adsm:gridColumn title="rotaDestino"
			property="rotaPreco.destinoString"
			width="150"/>

		<adsm:gridColumn
			align="right"
			dataType="decimal"
			mask="##,##0.0000"
			property="nrFatorMultiplicacao"
			title="fatorMultiplicacao"
			width="150"/>

		<adsm:gridColumn
			align="right"
			dataType="decimal"
			mask="###,###,###,###,##0.0000"
			property="vlFixo"
			title="valorFixo"
			width="70"/>

		<adsm:gridColumn
			dataType="decimal"
			mask="##0.00"
			property="pcTaxa"
			title="taxas"
			unit="percent"
			width="80"/>

		<adsm:gridColumn
			dataType="decimal"
			mask="##0.00"
			property="pcDesconto"
			title="desconto"
			unit="percent"
			width="100"/>

		<adsm:gridColumn
			dataType="decimal"
			mask="###,###,###,###,##0.00"
			property="vlAcrescimo"
			title="valorAcrescimo"
			width="120"/>

		<adsm:gridColumn
			property="blPromocional"
			renderMode="image-check"
			title="promocional"
			width="100"/>

		<adsm:gridColumn
			dataType="JTDate"
			property="dtVigenciaPromocaoInicial"
			title="vigenciaInicial"
			width="100"/>

		<adsm:gridColumn
			dataType="JTDate"
			property="dtVigenciaPromocaoFinal"
			title="vigenciaFinal"
			width="100"/>
		<adsm:buttonBar>
			<adsm:removeButton
				service="lms.tabelaprecos.precificarParcelasAction.removeByIdsValorFaixaProgressiva"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript" src="../lib/rotaPreco.js"></script>
<script language="javascript" type="text/javascript">
	function initWindow(eventObj) {
		setPropertyValues();
		var event = eventObj.name;
		if(event == "tab_click"
			|| event == "cleanButton_click"
			|| event == "removeButton_grid") {
			setFocus("tarifaPreco.cdTarifaPreco", false);
		}
		/* Controle para autoSearch da Grid */
		if(getElementValue("executeSearch") == "true"
			&& getElementValue("tarifaPreco.idTarifaPreco") != ""
			|| (hasValue(getElementValue("zonaByIdZonaOrigem.idZona")) && hasValue(getElementValue("zonaByIdZonaDestino.idZona")))) {
			findButtonScript("precificarParcelasResultado", document.forms[0]);
		}
		setElementValue("executeSearch", "false");
	}

	function setPropertyValues() {
		var tabGroup = getTabGroup(this.document);
		var idFaixaProgressiva = tabGroup.parentTabGroup.getTab("cad").getDocument().getElementById("idFaixaProgressiva").value;
		setElementValue("idFaixaProgressiva", idFaixaProgressiva);
	}

</script>