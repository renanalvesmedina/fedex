<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script language="javascript" type="text/javascript">

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click" || eventObj.name == "removeButton"){
			carregaDados();
		}
    }

	function carregaDados() {
		var u = new URL(parent.location.href);
		var idTabelaPrecoParcela = u.parameters["idTabelaPrecoParcela"];
		setElementValue("tabelaPrecoParcela.idTabelaPrecoParcela", idTabelaPrecoParcela);
		setElementValue("tabelaPrecoParcela.tabelaPreco.dsDescricao", u.parameters["tabelaPreco.dsDescricao"]);
		setElementValue("tabelaPrecoParcela.tabelaPreco.blEfetivada", u.parameters["tabelaPreco.blEfetivada"]);
		setElementValue("tabelaPrecoParcela.tabelaPreco.tabelaPrecoString", u.parameters["tabelaPreco.tabelaPrecoString"]);
		setElementValue("tabelaPrecoParcela.tabelaPreco.moeda.dsSimbolo", u.parameters["tabelaPreco.moeda.dsSimbolo"]);
		setElementValue("tabelaPrecoParcela.parcelaPreco.nmParcelaPreco", u.parameters["parcelaPreco.nmParcela"]);
		setElementValue("tabelaPrecoParcela.parcelaPreco.tpParcelaPreco", u.parameters["parcelaPreco.tpParcelaPreco.description"]);
		setElementValue("tabelaPrecoParcela.parcelaPreco.tpIndicadorCalculo", u.parameters["parcelaPreco.tpIndicadorCalculo.description"]);

		if(getElementValue('tabelaPrecoParcela.tabelaPreco.blEfetivada') == "true" || u.parameters["tabelaPreco.idPendencia"] != "" || u.parameters["isVisualizacaoWK"] == "true"){
			setDisabled('btnSalvar', true);
			setDisabled('btnNovo', true);
		} else {
			var tpTipoTabelaPreco = u.parameters["tabelaPreco.tpTipoTabelaPreco"];
			if(tpTipoTabelaPreco == "D") {
				setDisabled("pesoMinimo", false);
			}
		}
	}

	function preenche_cb(dados, erros){
		onDataLoad_cb(dados, erros);
		carregaDados();
	}
</script>

<adsm:window
	service="lms.tabelaprecos.manterPrecosFretesAction">
	<adsm:form
		action="/tabelaPrecos/manterPrecosFretes"
		idProperty="idPrecoFrete"
		onDataLoadCallBack="preenche">

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
			required="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			property="tabelaPrecoParcela.parcelaPreco.nmParcelaPreco"
			label="parcela"
			size="41"
			maxLength="60"
			disabled="true"
			required="true"
			serializable="false"/>

		<adsm:textbox
			dataType="text"
			property="tabelaPrecoParcela.parcelaPreco.tpParcelaPreco"
			label="tipoParcela"
			size="15"
			maxLength="15"
			disabled="true"
			required="true"
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
			label="tarifa"
			maxLength="5"
			property="tarifaPreco"
			service="lms.tabelaprecos.tarifaPrecoService.findLookup"
			size="5">

			<adsm:propertyMapping
				criteriaProperty="tarifa.tpSituacao"
				modelProperty="tpSituacao"/>
		</adsm:lookup>

		<adsm:textbox
			dataType="text"
			property="tabelaPrecoParcela.parcelaPreco.tpIndicadorCalculo"
			label="indicadorCalculo"
			size="15"
			maxLength="1"
			disabled="true"
			required="true"/>

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
			disabled="true"
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

		<adsm:textbox
			dataType="currency"
			property="vlPrecoFrete"
			label="valor"
			width="65%"
			size="18"
			minValue="0.00000"
			required="true"
			mask="###,###,###,##0.00000"/>

		<adsm:textbox
			label="pesoMinimo"
			property="pesoMinimo"
			dataType="weight"
			minValue="0.00"
			width="65%"
			unit="kg"
			maxLength="18"
			size="18"
			disabled="true"
			mask="###,###,##0.00" />

		<adsm:buttonBar>
			<adsm:storeButton
				id="btnSalvar"
				service="lms.tabelaprecos.manterPrecosFretesAction.storeAtualizaTabela"
				callbackProperty="storeCallBack"/>

 			<adsm:button
 				caption="novo"
 				onclick="newButtonScript(this.document); carregaDados();"
 				disabled="false"
 				buttonType="newButton"
 				id="btnNovo"/>

			<adsm:removeButton
				id="btnExcluir"/>

		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	setDisabled("rotaPreco.idRotaPreco",false);
	setDisabled("rotaPreco.origemString",true);
	setDisabled("rotaPreco.destinoString",true);

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

</script>