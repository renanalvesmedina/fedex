<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.digitarDadosNotaNormalCalculoCTRCAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-04051"/>
		<adsm:include key="LMS-04052"/>
		<adsm:include key="LMS-04079"/>
		<adsm:include key="LMS-04080"/>
		<adsm:include key="LMS-04199"/>
	</adsm:i18nLabels>

	<adsm:form action="/expedicao/digitarDadosNotaNormalCalculoCTRC" id="form1" height="20" >
		<adsm:section caption="calculoCTRC" width="60%" />
		<adsm:combobox
			property="divisaoCliente.idDivisaoCliente"
			autoLoad="false"
			label="divisao" 
			optionLabelProperty="dsDivisaoCliente"
			onDataLoadCallBack="divisaoCliente"
			optionProperty="idDivisaoCliente"
			width="89%"
			labelWidth="8%"
			boxWidth="284"
			service="lms.expedicao.digitarDadosNotaNormalCalculoCTRCAction.findDivisaoCliente">
			<adsm:button
				style="FmLbSection"
				id="confirmarDivisaoButton"
				caption="confirmarDivisao" 
				onclick="confirmarDivisao();"
				boxWidth="125"
				disabled="true"/>
		</adsm:combobox>
	</adsm:form>

	<adsm:grid
		property="parcela"
		title="calculoFrete"
		onRowClick="ignore"
		idProperty="idParcela" 
		selectionMode="none"
		showRowIndex="false"
		autoAddNew="false"
		gridHeight="90" 
		onValidate="validaParcelas"
		showGotoBox="false"
		showPagging="false" 
		showTotalPageCount="false"
		scrollBars="vertical">
		<adsm:editColumn
			dataType="text"
			property="dsParcela"
			title="parcela"
			field="textbox"
			width="240"/>
		<adsm:editColumn
			dataType="currency"
			property="vlParcela"
			title="valor"
			field="textbox"
			unit="reais"
			align="right"/>
	</adsm:grid>

	<adsm:form action="/expedicao/digitarDadosNotaNormalCalculoCTRC" id="form2" >
		<adsm:label key="branco" width="30%" />
		<adsm:textbox
			label="desconto"
			property="vlDesconto"
			dataType="decimal"
			size="17"
			labelWidth="16%"
			width="34%"
			disabled="true"/>

		<adsm:label key="branco" width="30%" />
		<adsm:textbox
			label="totalFreteReais"
			property="vlTotalFrete"
			dataType="decimal"
			size="17"
			labelWidth="16%"
			width="34%"
			disabled="true"/>
	</adsm:form>

	<adsm:grid
		property="servico"
		title="calculoServicos"
		onRowClick="ignore"
		idProperty="idServico" 
		selectionMode="none"
		showRowIndex="false"
		autoAddNew="false"
		gridHeight="80"
		onValidate="validaParcelas"
		showGotoBox="false"
		showPagging="false"
		showTotalPageCount="false"
		scrollBars="vertical">
		<adsm:editColumn
			dataType="text"
			property="dsServico"
			title="servicoAdicional"
			field="textbox"
			width="240"/>
		<adsm:editColumn
			dataType="currency"
			property="vlServico"
			title="valor"
			field="textbox"
			unit="reais"
			align="right"/>
	</adsm:grid>

	<adsm:form action="/expedicao/digitarDadosNotaNormalCalculoCTRC" id="form3" height="70">
		<adsm:label key="branco" width="26%" />
		<adsm:textbox
			label="totalServicosReais"
			property="vlTotalServico"
			dataType="decimal" 
			size="17"
			labelWidth="20%"
			width="30%"
			disabled="true"/>

		<adsm:label key="espacoBranco" style="border:none;" width="100%" />
		<adsm:label key="branco" width="28%" />
		<adsm:textbox
			label="totalCTRCReais"
			property="vlTotalCtrc"
			dataType="decimal"
			size="17" 
			labelWidth="18%"
			width="30%"
			disabled="true"/>

		<adsm:buttonBar>
			<adsm:button
				caption="processarCalcManual"
				buttonType="processarCalculo" 
				disabled="true"
				id="processarCalcManualButton"
				onclick="executaCalculoManual();"
				boxWidth="145"/>
			<adsm:button
				caption="cancelarTudo"
				buttonType="cancelarButton"
				disabled="false"
				id="cancelarTudo"
				boxWidth="90"
				onclick="return closeCalculo('cancelarCalculo');"/>
			<adsm:button
				caption="voltarDadosGerais"
				buttonType="closeButton"
				disabled="false"
				id="voltarDadosGerais"
				boxWidth="125"
				onclick="return closeCalculo();"/>
			<adsm:button
				caption="gerarCTRC"
				id="geraDocumentoButton"
				buttonType="gerarCtrcType"
				disabled="true"
				boxWidth="85"
				onclick="gravaCtrc();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript" src="../lib/expedicao.js"></script>
<script language="javascript" type="text/javascript" src="../lib/digitarDadosCTRC.js"></script>
<script language="javascript" type="text/javascript">
	var _valorFrete;
	var tpDocumento = "CTR";
	var manual = "false";

	function myOnPageLoad_cb() {
		onPageLoad_cb();

		var url = new URL(parent.location.href);
		manual = url.parameters["manual"]; 
		if(manual == "false") {
			setDisabled("processarCalcManualButton", true);
			var sdo = createServiceDataObject("lms.expedicao.digitarDadosNotaNormalCalculoCTRCAction.findDivisaoCliente","divisaoCliente");
			xmit({serviceDataObjects:[sdo]});
		} else {
			setDisabled("processarCalcManualButton", false);
			setDisabled("confirmarDivisaoButton", true);
			setDisabled("divisaoCliente.idDivisaoCliente", true);
			var sdo = createServiceDataObject("lms.expedicao.digitarDadosNotaNormalCalculoCTRCAction.findDataGrid", "populaGrids");
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function populaGrids_cb(dados) {
		if(dados) {
			if(dados.parcelaFrete) {
				var gridFrete = getElement("parcela.dataTable").gridDefinition;
				gridFrete.insertRow();
				setElementValue("parcela:0.dsParcela", dados.parcelaFrete.nmParcelaPreco);
				setElementValue("parcela:0.id", dados.parcelaFrete.idParcelaPreco);
				gridFrete.setDisabledColumn("dsParcela", true);
			}
			if(dados.servicosAdicionais) {
				var gridServico = getElement("servico.dataTable").gridDefinition;
				var servicos = dados.servicosAdicionais;
				for(var i = 0; i < servicos.length; i++) {
					gridServico.insertRow();
					setElementValue("servico:" + i + ".dsServico", servicos[i].dsServico);
					setElementValue("servico:" + i + ".id", servicos[i].idServico);
				}
				gridServico.setDisabledColumn("dsServico", true);
			}
			var objParcela = getElement("parcela:0.vlParcela");
			setFocus(objParcela, false);
		}
	}

 	/*
	* Verifica se valor informado para calculo Manual
	* eh diferente do digitado anterior, caso afirmativo 
	* desabilita dotao de Calculo;
	*/
	function verifyChangeValue(rowIndex, columnName, obj) {
		if (manual == "true") {
			var value = getElementValue(obj);
			setDisabled("processarCalcManualButton",_valorFrete == value);
		}
		return true;
	}

	function validaParcelas(rowIndex, columnName, objCell) {
		if(manual != "true") {
			return true;
		}
		setDisabled("geraDocumentoButton", true);
		return verifyChangeValue(rowIndex, columnName, objCell);
	}

	function executaCalculoManual() {
		if(validaCamposPreenchidos()) {
			var vlParcela = getElementValue("parcela:0.vlParcela");
			_valorFrete = vlParcela;

			setDisabled("processarCalcManualButton",true);
			storeEditGridScript('lms.expedicao.digitarDadosNotaNormalCalculoCTRCAction.executaCalculoManual', 'executaCalculoManual', document.forms[1], document.forms[3]);
		}
	}

	function executaCalculoManual_cb(dados, errorMsg) {
		if (errorMsg) {
			alert(errorMsg);
			setFocusOnFirstFocusableField(document);
			return false;
		}
		if(dados) {
			populateTotais(dados);
		}
	}

	function validaCamposPreenchidos() {
		var gridFrete = getElement("parcela.dataTable").gridDefinition;
		var objParcela = getElement("parcela:0.vlParcela");
		if(getElementValue(objParcela) == "") {
			alertI18nMessage("LMS-04080");
			setFocus(objParcela, true);
			return false;
		}
		var gridServico = getElement("servico.dataTable").gridDefinition;
		for(var i = 0; i < gridServico.currentRowCount; i++) {
			var objServico = getElement("servico:" + i + ".vlServico");
			if(getElementValue(objServico) == "") {
				alertI18nMessage("LMS-04080");
				setFocus(objServico, true);
				return false;
			}
		}
		return true;
	}

	function divisaoCliente_cb(data) {
		divisaoCliente_idDivisaoCliente_cb(data);
		if(data && data.length > 1) {
			setDisabled("confirmarDivisaoButton", false);
		} else {
			if(data && data.length == 1) {
				setElementValue("divisaoCliente.idDivisaoCliente", data[0].idDivisaoCliente);
				executeCalculoFretePrimeiraFase(data[0].idDivisaoCliente);
			} else {
				executeCalculoFretePrimeiraFase();
			}
		}
	}

	function confirmarDivisao() {
		var objDiv = getElement("divisaoCliente.idDivisaoCliente");
		var idDivisao = getElementValue(objDiv);
		if(idDivisao != undefined && idDivisao != "") {
			executeCalculoFretePrimeiraFase(idDivisao);
			return true;
		} else {
			alertI18nMessage("LMS-04079");
			setFocus(objDiv, false);
			return false;
		}
	}

	/*
	 * EXECUTA CALCULO FRETE
	 */
	function executeCalculoFretePrimeiraFase(idDivisao) {
		setDisabled("confirmarDivisaoButton", true);
		setDisabled("divisaoCliente.idDivisaoCliente", true);
		var parameters = {idDvivisaoCliente:idDivisao}
		var sdo = createServiceDataObject("lms.expedicao.digitarDadosNotaNormalCalculoCTRCAction.executeCalculoFretePrimeiraFase", "executeCalculoFrete", parameters);
		xmit({serviceDataObjects:[sdo]});
	}

	function populaGridsCtrci(dados){
		if(dados.parcelasFrete) {
			var gridFrete = getElement("parcela.dataTable").gridDefinition;
			var parcelas = dados.parcelasFrete;
			gridFrete.setDisabledColumn("dsParcela", true);
			gridFrete.setDisabledColumn("vlParcela", true);
			for(var i = 0; i < parcelas.length; i++) {
				gridFrete.insertRow();
				var objParc = getElement("parcela:" + i + ".vlParcela");
				setElementValue("parcela:" + i + ".dsParcela", parcelas[i].nmParcelaPreco);
				setElementValue(objParc, setFormat(objParc, parcelas[i].vlParcela));
			}
		}
		if(dados.servicos) {
			var gridServico = getElement("servico.dataTable").gridDefinition;
			var servicos = dados.servicos;
			gridServico.setDisabledColumn("dsServico", true);
			gridServico.setDisabledColumn("vlServico", true);
			for(var i = 0; i < servicos.length; i++) {
				gridServico.insertRow();
				setElementValue("servico:" + i + ".dsServico", servicos[i].dsServico);
				var objParc = getElement("servico:" + i + ".vlServico");
				setElementValue(objParc, setFormat(objParc, servicos[i].vlServico));
			}
		}
		populateTotais(dados);
		return true;
	}

	function populateTotais(dados) {
		var objDesc = getElement("vlDesconto");
		setElementValue(objDesc, setFormat(objDesc, dados.vlDesconto));

		var objFrete = getElement("vlTotalFrete");
		setElementValue(objFrete, setFormat(objFrete, dados.vlTotalFrete));

		var objServico = getElement("vlTotalServico");
		setElementValue(objServico, setFormat(objServico, dados.vlTotalServico));

		var objCtrc = getElement("vlTotalCtrc");
		setElementValue(objCtrc, setFormat(objCtrc, dados.vlTotalCtrc));

		var gerButton = getElement("geraDocumentoButton");
		setDisabled(gerButton, false);
		setFocus(gerButton, false);
	}

	function gravaCtrc() {
		var sdo = createServiceDataObject("lms.expedicao.digitarDadosNotaNormalCalculoCTRCAction.gravaCtrcPrimeiraFase", "gravaCtrcPrimeiraFase");
		xmit({serviceDataObjects:[sdo]});
	}

	function gravaCtrcPrimeiraFase_cb(data, errorMsg, errorKey) {
		if (errorMsg) {
			alert(errorMsg);
			window.close();
			return false;
		}
		if(data.idVersaoDescritivoPce) {
			var ret = showModalDialog('vendas/alertaPce.do?cmd=pop&dVersaoDescritivoPce=' + data.idVersaoDescritivoPce,window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:660px;dialogHeight:160px;');
			var sdo = createServiceDataObject("lms.expedicao.digitarDadosNotaNormalCalculoCTRCAction.gravaCtrcSegundaFase", "gravaCtrcSegundaFase");
			xmit({serviceDataObjects:[sdo]});
			return false;
		} else {
			closeCalculo("calculoOk");
		}
	}

	function gravaCtrcSegundaFase_cb(data, errorMsg, errorKey) {
		if (errorMsg) {
			alert(errorMsg);
			window.close();
			return false;
		}
		closeCalculo("calculoOk");
	}

	function closeCalculo(retorno) {
		window.returnValue = retorno;
		window.close();
	}
</script>