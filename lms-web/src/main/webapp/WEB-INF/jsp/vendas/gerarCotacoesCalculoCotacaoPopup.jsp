<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.gerarCotacoesAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-04079"/>
		<adsm:include key="LMS-01025"/>
		<adsm:include key="LMS-01268"/>
		<adsm:include key="ISSMenu"/>
		<adsm:include key="reais"/>
	</adsm:i18nLabels>
	<script language="javascript" type="text/javascript">
		var idCotacao;
		function myOnPageLoad_cb() {
			onPageLoad_cb();
			hiddenFields();

			setDisabled("gravaCotacaoButton", true);
			setDisabled("cancelarTudo", false);
			setDisabled("voltarDadosGerais", false);

			var u = new URL(parent.location.href);
			idCotacao = u.parameters["idCotacao"]; 
			if(idCotacao && idCotacao != "") {
			}
			var sdo = createServiceDataObject("lms.vendas.gerarCotacoesAction.configureCalculoFrete", "configureCalculoFrete",{idCotacao:idCotacao});
			xmit({serviceDataObjects:[sdo]});
		}
	</script>
	<adsm:form action="/vendas/gerarCotacoes" id="form1" height="60">
		<adsm:section caption="calculoCotacao" width="60%"/>
		<adsm:combobox 
			property="divisaoCliente.idDivisaoCliente" 
			autoLoad="false" 
			label="divisao"
			optionLabelProperty="dsDivisaoCliente" 
			onDataLoadCallBack="divisaoCliente"
			optionProperty="idDivisaoCliente" 
			width="80%" 
			labelWidth="6%" 
			boxWidth="250"
			service="lms.vendas.gerarCotacoesAction.findDivisaoCliente">
			<adsm:button 
				style="FmLbSection" 
				id="confirmarDivisaoButton" 
				caption="confirmarDivisao" 
				onclick="confirmarDivisao();" 
				boxWidth="125" 
				disabled="true"/>
		</adsm:combobox>
		<adsm:textbox 
			label="tabela" 
			property="tabelaCliente" 
			size="45"
			labelWidth="6%" 
			width="34%" 
			disabled="true" 
			dataType="text"/>
	
	</adsm:form>
	<adsm:grid property="parcela" title="calculoFrete" onRowClick="ignore" idProperty="idParcela"
		selectionMode="none" showRowIndex="false" autoAddNew="false" gridHeight="70"
		showGotoBox="false" showPagging="false"
		showTotalPageCount="false" scrollBars="vertical">
		<adsm:gridColumn dataType="text" property="dsParcela" title="parcela" width="240"/>
		<adsm:gridColumn dataType="currency" property="vlParcela" title="valor" unit="reais" align="right"/>
	</adsm:grid>
	
	<adsm:form action="/vendas/gerarCotacoes" id="form2" height="40">
		<adsm:label key="branco" width="30%"/>
		<adsm:textbox label="desconto" property="vlDesconto" dataType="decimal" size="17"
			labelWidth="16%" width="34%" disabled="true"/>
		<adsm:label key="branco" width="30%"/>
		<adsm:textbox label="totalFreteReais" property="vlTotalFrete" dataType="decimal" size="17"
			labelWidth="16%" width="34%" disabled="true"/>
	</adsm:form>
	<adsm:grid property="servico" title="calculoServicos" onRowClick="ignore" idProperty="idServico"
		selectionMode="none" showRowIndex="false" gridHeight="50"
		showGotoBox="false" showPagging="false" showTotalPageCount="false" scrollBars="vertical">
		<adsm:gridColumn dataType="text" property="dsServico" title="servicoAdicional" width="240"/>
		<adsm:gridColumn dataType="currency" property="vlServico" title="valor" unit="reais" align="right"/>
	</adsm:grid>
	<adsm:form action="/vendas/gerarCotacoes" id="form3" height="125">

		<adsm:label key="branco" width="25%"/>
		<adsm:textbox label="totalServicosReais" property="vlTotalServico" dataType="decimal"
			size="17" labelWidth="22%" width="30%" disabled="true"/>

		<adsm:label key="espacoBranco" style="border:none;" width="100%"/>

		<adsm:label key="branco" width="25%"/>
		<adsm:textbox label="totalFreteReais" property="vlTotalCotacao" dataType="decimal" size="17"
			labelWidth="22%" width="30%" disabled="true"/>

		<adsm:label key="branco" width="25%"/>
		<adsm:textbox label="retencaoIcmsSt" property="vlIcmsSubstituicaoTributaria" dataType="decimal" size="17"
			labelWidth="22%" width="30%" disabled="true"/>

		<adsm:label key="branco" width="25%"/>
		<adsm:textbox label="totalCotacao" property="vlLiquido" dataType="decimal" size="17"
			labelWidth="22%" width="30%" disabled="true"/>

		<adsm:label key="branco" width="25%"/>
		<adsm:textbox label="vlIcms" property="vlImposto" dataType="decimal" size="17"
			labelWidth="22%" width="30%" disabled="true"/>			

		<adsm:buttonBar>
			<adsm:button caption="cancelarTudo" buttonType="cancelarButton"
				id="cancelarTudo" boxWidth="90" onclick="cancelarCalculo();"/>
			<adsm:button caption="voltarDadosGerais" buttonType="closeButton"
				id="voltarDadosGerais" boxWidth="125" onclick="voltarDados();"/>
			<adsm:button caption="gravarCotacao" id="gravaCotacaoButton" buttonType="gravaCotacaoType"
				disabled="true" boxWidth="110" onclick="gravaCotacao();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript" src="../lib/expedicao.js"></script>
<script language="javascript" type="text/javascript">
	var toReturn = [];

	function configureCalculoFrete_cb(data, error) {
		if(error) {
			alert(error);
			closeCalculo();
			return false;
		}
		setDivisao(data.divisoesCliente)
		populaGrids_cb(data.calculo);
		if( data.calculo ){
			toReturn.action = "calculado";
		}
	}

	function hiddenFields() {
		setRowVisibility("vlTotalServico", false);
		setRowVisibility("vlTotalCotacao", false);
		setRowVisibility("vlIcmsSubstituicaoTributaria", false);
		setRowVisibility("vlLiquido", false);
		setRowVisibility("vlImposto", false);
	}

	var CONHECIMENTO_NACIONAL = "CTR";
	var NOTA_FISCAL_TRANSPORTE = "NFT";
	function configureDoctoCotacao(tpDocumentoCotacao) {
		setRowVisibility("vlTotalServico", true);
		setRowVisibility("vlTotalCotacao", true);
		setRowVisibility("vlLiquido", true);

		if(tpDocumentoCotacao == CONHECIMENTO_NACIONAL) {
			setRowVisibility("vlIcmsSubstituicaoTributaria", true);
		} else if(tpDocumentoCotacao == NOTA_FISCAL_TRANSPORTE) {
			getElement("spanlbl_vlImposto").innerHTML = getI18nMessage("ISSMenu", null, true)+" ("+getI18nMessage("reais", null, true)+")";
		}
		setRowVisibility("vlImposto", true);
	}

	function divisaoCliente_cb(data, error) {
		if(error) {
			alert(error);
			closeCalculo();
			return false;
		}
		setDivisao(data);
		if(data && data.length == 1) {
			calculaCotacao(data[0].idDivisaoCliente);
		} else if( (!data) || (data && data.length == 0) ) {
			calculaCotacao();
		}
	}

	function setDivisao(data){
		divisaoCliente_idDivisaoCliente_cb(data);
		setDisabled("divisaoCliente.idDivisaoCliente", true);
		setDisabled("confirmarDivisaoButton", true);
		if(data && data.length > 1) {
			setDisabled("confirmarDivisaoButton", false);
			setDisabled("divisaoCliente.idDivisaoCliente", false);
		} else {
			if(data && data.length == 1) {
				setElementValue("divisaoCliente.idDivisaoCliente", data[0].idDivisaoCliente);
				setElementValue("tabelaCliente", data[0].nmTabelaPreco);
			}
		}
	}
	
	function confirmarDivisao() {
		var objDiv = getElement("divisaoCliente.idDivisaoCliente");
		var idDivisao = getElementValue(objDiv);
		if(idDivisao && idDivisao != "") {
			calculaCotacao(idDivisao);
			return true;
		} else {
			alertI18nMessage("LMS-04079");
			setFocus(objDiv, false);
			return false;
		}
	}

	function calculaCotacao(idDivisao) {
		var parameters = {idDvivisaoCliente:idDivisao}
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesAction.calculaCotacao", "calculaCotacao", parameters);
		xmit({serviceDataObjects:[sdo]});
	}

	function calculaCotacao_cb(data, errorMsg, errorKey) {
		if (errorMsg) {
			alert(errorMsg);
			closeCalculo();
			return false;
		}
		if(data) {
			populaGrids_cb(data);
			toReturn.action = "calculado";
			return true;
		}
		return false;
	}

	function populaGrids_cb(data, erros){
		if (erros) {
			alert(erros);
			closeCalculo();
			return false;
		}
		if( data ){ 
			var parcelas = data.parcelas;
			if(parcelas && parcelas.length > 0) {
				var gridFrete = getElement("parcela.dataTable").gridDefinition;
				gridFrete.resetGrid();
				gridFrete.onDataLoad_cb(parcelas, erros);
			}
			var servicos = data.servicos;
			if(servicos && servicos.length > 0) {
				var gridServico = getElement("servico.dataTable").gridDefinition;
				gridServico.resetGrid();
				gridServico.onDataLoad_cb(servicos, erros);
			}
	
			setElementValue("divisaoCliente.idDivisaoCliente", data.idDivisaoCliente);
			setElementValue("tabelaCliente", data.nmTabelaPreco);
	
			populateTotais(data);
			if(idCotacao) {
				setFocus(getElement("voltarDadosGerais"), false);
			}
			var gerButton = getElement("gravaCotacaoButton");
			toReturn.tpTipoTabelaPreco = data.tpTipoTabelaPreco;
			if( data.alterado == "true" ){
				setDisabled(gerButton, false);
			}else{
				setDisabled(gerButton, true);
				setFocus(gerButton, true);
			} 
		}
		return true;
	}

	function populateTotais(data) {
		var objFrete = getElement("vlTotalFrete");
		setElementValue(objFrete, setFormat(objFrete, data.vlTotalFrete));
		var objServico = getElement("vlTotalServico");
		setElementValue(objServico, setFormat(objServico, data.vlTotalServico));
		var objCot = getElement("vlTotalCotacao");
		setElementValue(objCot, setFormat(objCot, data.vlTotalCotacao));
		setElementValue("vlDesconto", setFormat("vlDesconto", data.vlDesconto));
		setElementValue("vlIcmsSubstituicaoTributaria", setFormat("vlIcmsSubstituicaoTributaria", data.vlIcmsSubstituicaoTributaria));
		setElementValue("vlLiquido", setFormat("vlLiquido", data.vlLiquido));
		setElementValue("vlImposto", setFormat("vlImposto", data.vlImposto));

		/* Exibe total de acordo com o Tipo de Cotacao */
		configureDoctoCotacao(data.tpDocumentoCotacao);
	}

	function gravaCotacao() {
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesAction.gravaCotacao", "gravaCotacao");
		xmit({serviceDataObjects:[sdo]});
	}

	function gravaCotacao_cb(data, errorMsg, errorKey) {
		if (errorMsg) {
			alert(errorMsg);
			setFocusOnFirstFocusableField(document);
			return false;
		}
		
		if (data.gerouWorkflow != null) {
			alert("LMS-01268 - "+i18NLabel.getLabel("LMS-01268"));
		}

		if (confirmI18nMessage("LMS-01025", [ data.sgFilial, data.nrCotacao ],
				false)) {
			dialogArguments.window.imprime(data.idCotacao);
		}
		closeCalculo(data);
	}

	function voltarDados() {
		closeCalculo(toReturn);
	}

	function cancelarCalculo() {
		toReturn.action = "cancelarCalculo";
		closeCalculo(toReturn);
	}

	function closeCalculo(retorno) {
		window.returnValue = retorno;
		window.close();
	}
</script>
