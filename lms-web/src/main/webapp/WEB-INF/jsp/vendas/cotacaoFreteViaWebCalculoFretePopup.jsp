<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.cotacaoFreteViaWebCalculoFreteAction" onPageLoad="myOnPageLoad">
		<adsm:grid
			property="parcela"
			idProperty="idParcela"
			showPagging="false"
			showTotalPageCount="false"
			gridHeight="80"
			selectionMode="none"
			unique="true"
			scrollBars="vertical"
			title="calculoFrete"
			onRowClick="ignore">
			<adsm:gridColumn
				property="nmParcela"
				title="parcela"/>
			<adsm:gridColumn
				property="vlParcela"
				dataType="currency"
				title="valor"
				unit="reais"/>
		</adsm:grid>

		<adsm:form action="/vendas/cotacaoFreteViaWebCalculoFrete">
		<adsm:label key="espacoBranco" style="border:none;" width="100%"/>

		<adsm:label key="branco" width="16%"/>
		<adsm:textbox
			label="desconto"
			property="vlDesconto"
			dataType="decimal"
			size="15"
			labelWidth="20%"
			width="64%"
			disabled="true"/>
		<adsm:label key="branco" width="16%"/>
		<adsm:textbox
			label="totalFreteReais"
			property="vlTotalFrete"
			dataType="decimal"
			size="15"
			labelWidth="20%"
			width="64%"
			disabled="true"/>
		<adsm:label key="branco" width="16%"/>
		<adsm:textbox
			label="retencaoIcmsSt"
			property="vlRetencaoICMS"
			dataType="decimal"
			size="15"
			labelWidth="20%"
			width="64%"
			disabled="true"/>
		<adsm:label key="branco" width="16%"/>
		<adsm:textbox
			label="totalCotacao"
			property="vlTotalLiquido"
			dataType="decimal"
			size="15"
			labelWidth="20%"
			width="64%"
			disabled="true"/>
		<adsm:label key="branco" width="16%"/>
		<adsm:textbox
			label="vlIcms"
			property="vlTotalImposto"
			dataType="decimal"
			size="15"
			labelWidth="20%"
			width="64%"
			disabled="true"/>
		<adsm:label key="branco" width="16%"/>
		<adsm:textbox
			label="prazoEntrega"
			property="prazoEntrega"
			dataType="integer"
			size="15"
			unit="dias"
			labelWidth="20%"
			width="64%"
			disabled="true"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton
				id="imprimir"
				caption="imprimir"
				service="lms.vendas.emitirCotacaoFreteWebService"/>
			<adsm:button
				caption="fechar"
				id="fechar"
				disabled="false"
				onclick="self.close();"/>
		</adsm:buttonBar>
 	</adsm:form>
</adsm:window>

<script language="javascript">
	function ignore() {
		return false;
	}

	function myOnPageLoad() {
		onPageLoad();
		var sdo = createServiceDataObject("lms.vendas.cotacaoFreteViaWebCalculoFreteAction.findCalculoFrete", "findCalculoFrete");
		xmit({serviceDataObjects:[sdo]});
	}

	function findPrazoEntrega_cb(data, error) {
		if (error != undefined || error == "") {
			alert(error);
			self.close();
			return false;
		}
		if (data!=undefined) {
			var objFrete = getElement("prazoEntrega");
			setElementValue(objFrete, setFormat(objFrete, data.prazoEntrega));
		}
		return true;
	}

	function findCalculoFrete_cb(data, error) {
		if (error != undefined || error == "") {
			alert(error);
			self.close();
			return false;
		}
		if (data != undefined) {
			parcelaGridDef.resetGrid();
			parcelaGridDef.onDataLoad_cb(data.parcelas, error);

			//*** Formata Valores
			var vlDesconto = getElement("vlDesconto");
			setElementValue(vlDesconto, setFormat(vlDesconto, data.vlDesconto));
			var vlTotalFrete = getElement("vlTotalFrete");
			setElementValue(vlTotalFrete, setFormat(vlTotalFrete, data.vlTotalFrete));
			var vlRetencaoICMS = getElement("vlRetencaoICMS");
			setElementValue(vlRetencaoICMS, setFormat(vlRetencaoICMS, data.vlRetencaoICMS));
			var vlTotalLiquido = getElement("vlTotalLiquido");
			setElementValue(vlTotalLiquido, setFormat(vlTotalLiquido, data.vlTotalLiquido));
			var vlTotalImposto = getElement("vlTotalImposto");
			setElementValue(vlTotalImposto, setFormat(vlTotalImposto, data.vlTotalImposto));

			setDisabled("imprimir", false);

			//*** Busca Prazo Entrega
			var sdo2 = createServiceDataObject("lms.vendas.cotacaoFreteViaWebCalculoFreteAction.findPrazoEntrega", "findPrazoEntrega");
			xmit({serviceDataObjects:[sdo2]});
		}
		return true;
	}
</script>