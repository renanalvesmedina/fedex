<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script language="javascript" type="text/javascript">

	function carregaDados() {
		onPageLoad();
		var u = new URL(parent.location.href);
		var idTabelaPrecoParcela = u.parameters["idTabelaPrecoParcela"];
		setElementValue("tabelaPrecoParcela.idTabelaPrecoParcela", idTabelaPrecoParcela);
		setElementValue("tabelaPrecoParcela.tabelaPreco.dsDescricao", u.parameters["tabelaPreco.dsDescricao"]);
		setElementValue("tabelaPrecoParcela.tabelaPreco.tabelaPrecoString", u.parameters["tabelaPreco.tabelaPrecoString"]);
		setElementValue("tabelaPrecoParcela.tabelaPreco.blEfetivada", u.parameters["tabelaPreco.blEfetivada"]);
		setElementValue("tabelaPrecoParcela.tabelaPreco.moeda.dsSimbolo", u.parameters["tabelaPreco.moeda.dsSimbolo"]);
		setElementValue("tabelaPrecoParcela.parcelaPreco.nmParcelaPreco", u.parameters["parcelaPreco.nmParcela"]);
		setElementValue("tabelaPrecoParcela.parcelaPreco.tpParcelaPreco", u.parameters["parcelaPreco.tpParcelaPreco.description"]);

	}

	function isTabelaPrecoEfetivada(){
		return getElementValue('tabelaPrecoParcela.tabelaPreco.blEfetivada') == 'true';
	}

	function limpar(){
		setElementValue("tarifaPreco.idTarifaPreco", "");
		setElementValue("tarifaPreco.cdTarifaPreco", "");
		setElementValue("rotaPreco.idRotaPreco", "");
		setElementValue("rotaPreco.origemString", "");
		setElementValue("rotaPreco.destinoString", "");

		setFocus(document.getElementById("tarifaPreco.cdTarifaPreco"));
	}
</script>
<adsm:window
	service="lms.tabelaprecos.precoFreteService"
	onPageLoad="carregaDados">

	<adsm:form
		action="/tabelaPrecos/manterPrecosFretes"
		idProperty="idPrecoFrete">

		<adsm:hidden
			property="tabelaPrecoParcela.idTabelaPrecoParcela"/>

		<adsm:hidden
			property="tabelaPrecoParcela.tabelaPreco.blEfetivada"
			serializable="false"/>

		<adsm:complement
			label="tabela"
			width="35%">

			<adsm:textbox
				dataType="text"
				property="tabelaPrecoParcela.tabelaPreco.tabelaPrecoString"
				size="8" 
				maxLength="7" 
				disabled="true"
				serializable="false"/>

			<adsm:textbox 
				dataType="text" 
				maxLength="60" 
				property="tabelaPrecoParcela.tabelaPreco.dsDescricao"
				size="30" 
				disabled="true"
				serializable="false"/>
		</adsm:complement>

		<adsm:textbox
			dataType="text"
			property="tabelaPrecoParcela.tabelaPreco.moeda.dsSimbolo"
			label="moeda"
			size="9"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			property="tabelaPrecoParcela.parcelaPreco.nmParcelaPreco"
			label="parcela"
			size="41"
			maxLength="60"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			property="tabelaPrecoParcela.parcelaPreco.tpParcelaPreco"
			label="tipoParcela"
			size="15"
			maxLength="15"
			disabled="true"
			serializable="false"/>	

		<adsm:hidden
			property="tarifa.tpSituacao"
			serializable="false"
			value="A"/>

		<adsm:lookup
			action="/tabelaPrecos/manterTarifasPreco"
			criteriaProperty="cdTarifaPreco"
			dataType="text"
			exactMatch="true"
			idProperty="idTarifaPreco"
			labelWidth="15%"
			label="tarifa"
			maxLength="5"
			property="tarifaPreco"
			service="lms.tabelaprecos.tarifaPrecoService.findLookup"
			size="5"
			width="85%">

			<adsm:propertyMapping
				criteriaProperty="tarifa.tpSituacao"
				modelProperty="tpSituacao"/>
		</adsm:lookup>

		<adsm:lookup 
			action="/tabelaPrecos/manterRotas" 
			criteriaProperty="origemString" 
			dataType="text" 
			exactMatch="false" 
			idProperty="idRotaPreco" 
			labelWidth="15%" 
			label="origem" 
			property="rotaPreco" 
			required="false"
			service="lms.tabelaprecos.rotaPrecoService.findLookup" 
			size="80" 
			width="85%">

			<adsm:propertyMapping
				relatedProperty="rotaPreco.destinoString"
				modelProperty="destinoString"/>
		</adsm:lookup>

		<adsm:textbox
			dataType="text"
			disabled="true"
			label="destino"
			property="rotaPreco.destinoString"
			required="false"
			serializable="false"
			size="80"
			width="85%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="precosFrete"/>
			<adsm:button caption="limpar" onclick="limpar();" disabled="false" buttonType="resetButton"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		selectionMode="check"
		idProperty="idPrecoFrete"
		property="precosFrete"
		gridHeight="200"
		unique="true"
		defaultOrder="tarifaPreco_.cdTarifaPreco, vlPrecoFrete">

		<adsm:gridColumn
			title="tarifa"
			property="tarifaPreco.cdTarifaPreco"
			width="8%"/>

		<adsm:gridColumn
			title="origem"
			property="rotaPreco.origemString"
			width="38%"/>

		<adsm:gridColumn
			title="destino"
			property="rotaPreco.destinoString"
			width="38%"/>

		<adsm:gridColumn
			title="valor"
			property="vlPrecoFrete"
			width="16%"
			align="right"
			dataType="currency"
			mask="###,###,###,##0.00000"/>

		<adsm:buttonBar>
			<adsm:removeButton
				id="btnExcluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
	setDisabled("rotaPreco.idRotaPreco",false);
	setDisabled("rotaPreco.origemString",true);
	setDisabled("rotaPreco.destinoString",true);
</script>
