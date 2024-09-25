<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.expedicao.digitarPreNotaFiscalServicoAction"
	onPageLoadCallBack="myOnPageLoad">

	<adsm:section
		caption="calculoNotaFiscal"
		width="58%"/>

	<adsm:grid
		autoAddNew="false"
		onRowClick="ignore"
		idProperty="idServicoAdicional"
		gridHeight="10"
		property="servicosAdicionais"
		unique="false"
		selectionMode="none"
		showPagging="false"
		width="418"
		disableMarkAll="true"
		showTotalPageCount="false"
		onValidate="verifyChangeValue(0, 'valor', 'servicosAdicionais:0.valor');">

		<adsm:editColumn
			dataType="text"
			property="dsServicoAdicional"
			title="servicoAdicional"
			field="textbox"
			width="300"
			disabled="true"/>

		<adsm:editColumn
			dataType="currency"
			property="valor"
			title="valor"
			field="textbox"
			width="118"
			unit="reais"
			minValue="0"
			required="true"
			disabled="true"/>

	</adsm:grid>

	<adsm:grid
		autoAddNew="false"
		idProperty="idTributo"
		onRowClick="ignore"
		gridHeight="70"
		property="tributos"
		unique="false"
		selectionMode="none"
		showPagging="false"
		width="418"
		disableMarkAll="true"
		showTotalPageCount="false"
		scrollBars="vertical">

		<adsm:editColumn
			dataType="text"
			property="nome"
			title="tributo"
			field="textbox"
			width="300"
			disabled="true"/>

		<adsm:editColumn
			dataType="currency"
			property="valor"
			title="valor"
			field="textbox"
			required="true"
			width="118"
			unit="reais"
			disabled="true" />

	</adsm:grid>

	<adsm:form
		action="/expedicao/digitarPreNotaFiscalServicoCalculoPreco"
		idProperty="idNotaFiscalServico">

		<adsm:hidden
			property="userip"/>

		<adsm:label
			key="espacoBranco"
			style="border:none;"
			width="15%"/>

		<adsm:textbox
			property="totalTributos"
			label="totalTributosReais"
			dataType="currency"
			size="18"
			labelWidth="25%"
			width="60%"
			disabled="true"/>

		<adsm:label
			key="espacoBranco"
			style="border:none;"
			width="100%"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="15%"/>

		<adsm:textbox
			property="valorTotal"
			label="valorTotalNFReais"
			dataType="currency"
			size="18"
			labelWidth="25%"
			width="60%"
			disabled="true"/>

		<adsm:label
			key="espacoBranco"
			style="border:none;"
			width="100%"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="15%"/>

		<adsm:textbox
			property="numFormulario"
			label="numeroProximoFormulario"
			dataType="integer"
			size="18"
			labelWidth="25%"
			width="60%"
			maxLength="6"/>

		<adsm:buttonBar
			lines="2">

			<adsm:button
				disabled="false"
				id="processarCalcManualId"
				buttonType="processarCalcManualBT"
				caption="processarCalcManual"
				onclick="processarCalcManual()"/>

			<adsm:button
				disabled="false"
				id="cancelarTudoID"
				buttonType="cancelarTudoBT"
				caption="cancelarTudo"
				boxWidth="95"
				onclick="cancelarTudo()"/>

			<adsm:button
				disabled="false"
				id="voltarDadosGeraisID"
				buttonType="voltarDadosGeraisBT"
				caption="voltarDadosGerais"
				boxWidth="125"
				onclick="voltarDadosGerais()"/>

			<adsm:button
				disabled="true"
				id="salvarEmitirID"
				buttonType="salvarEmitirBT"
				caption="salvarEmitir"
				boxWidth="90"
				onclick="salvarEmitir()"/>

		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	var _idParcelaPreco;
	var _tpCalculo;
	var _valorFrete;

	var TP_CALCULO_NORMAL = "N";
	var TP_CALCULO_MANUAL = "M";
	var TP_CALCULO_COTACAO = "C";

	function myOnPageLoad_cb(dados,erro) {
		onPageLoad_cb();
		var u = new URL(parent.location.href);
		var idNF = u.parameters["idNF"];

		setElementValue("idNotaFiscalServico",idNF);
		servicosAdicionaisGridDef.insertRow();
		
		if (idNF=="") {
			_idParcelaPreco = u.parameters["idPP"];
			_tpCalculo = u.parameters["tpCalc"];

			var idServicoAdicional = u.parameters["idServAd"];
			var dsServicoAdicional = u.parameters["dsServAd"];
			setElementValue("servicosAdicionais:0.id", idServicoAdicional);
			setElementValue("servicosAdicionais:0.dsServicoAdicional", dsServicoAdicional);

			if(_tpCalculo == TP_CALCULO_NORMAL || _tpCalculo == TP_CALCULO_COTACAO) {
				setDisabled("processarCalcManualId",true);
				servicosAdicionaisGridDef.setDisabledColumn("valor", true);
				calculaNFServico();
			} else if(_tpCalculo == TP_CALCULO_MANUAL) {
				setDisabled("processarCalcManualId",false);
				servicosAdicionaisGridDef.setDisabledColumn("valor", false);
				setFocus("servicosAdicionais:0.valor");
			}
		} else {
			setDisabled(this.document,true);
			obtemDadosPopupModoConsulta();
		}

		/* funcao que manipula o foco apos sair do campo do numero de formulario */
		getElement("numFormulario").onblur = function() {
			var objButton = document.getElementById("salvarEmitirID");
			if(!objButton.disabled) {
				objButton.focus();
			}
		}
	}

	function calculaNFServico() {
		var sdo = createServiceDataObject("lms.expedicao.digitarPreNotaFiscalServicoAction.findNFServico", "calculaNFServico", {});
		xmit({serviceDataObjects:[sdo]});
	}

	function obtemDadosPopupModoConsulta() {
		var idNotaFiscalServico = getElementValue("idNotaFiscalServico");
		var sdo = createServiceDataObject("lms.expedicao.digitarPreNotaFiscalServicoAction.obtemDadosPopupModoConsulta", "obtemDadosPopupModoConsulta", {idNotaFiscalServico:idNotaFiscalServico});
		xmit({serviceDataObjects:[sdo]});
	}

	function ignore() {
		return false;
	}

	function cancelarTudo() {
		var retorno = new Object();
		retorno.cancelarTudo = "true";
		closePopup(retorno);
	}

	function voltarDadosGerais() {
		closePopup();
	}

	function salvarEmitir_cb(data, error, errorCode) {
		if(error != undefined) {
			alert(error);
			if( (errorCode != "LMS-04113")
				&& (errorCode != "LMS-04114")
			) {
				closePopup(undefined);
			}
			return;
		}
		closePopup(data);
	}

	function validateSalvarEmitir() {
		var obj = document.getElementById("numFormulario");
		obj.required="true";
		return validateTabScript(this.document.forms);//TODO: usar msg especifica
	}

	function salvarEmitir() {
		if(validateSalvarEmitir()) {
			var numFormulario = getElementValue("numFormulario");
			var userip = getElementValue("userip");
			var service = 'lms.expedicao.digitarPreNotaFiscalServicoAction.storeAndPressNF';

			var args = {
				numFormulario : numFormulario
				,userip : userip
			}

			var sdo = createServiceDataObject(service, "salvarEmitir", args);
			xmit({serviceDataObjects:[sdo]});

			var obj = document.getElementById("numFormulario");
			obj.required="false";
		}
	}
	
	function obtemDadosPopupModoConsulta_cb(dados,erro) {
		if(erro!=undefined) {
			alert(erro);
			return;
		}
		setDisabled("voltarDadosGeraisID",false);
		calculaNFServico_cb(dados,erro);
	
		setElementValue("servicosAdicionais:0.dsServicoAdicional", getNestedBeanPropertyValue(dados, "servicosAdicionais.dsServicoAdicional"));
		setElementValue("numFormulario", getNestedBeanPropertyValue(dados, "numFormulario"));
	}
	
	function processarCalcManual() {
		if(validateTabScript(this.document.forms)) {
			var valor = getElementValue("servicosAdicionais:0.valor");
			var idServicoAdicional= getElementValue("servicosAdicionais:0.id");
			/* Armazena Valor do Ultimo Calculo para Validacoes. */
			_valorFrete = valor;

			var sdo = createServiceDataObject("lms.expedicao.digitarPreNotaFiscalServicoAction.findNFServico", "calculaNFServico", {valor:valor,idServicoAdicional:idServicoAdicional,idParcelaPreco:_idParcelaPreco});
			xmit({serviceDataObjects:[sdo]});
		}
		return true;
	}

	function calculaNFServico_cb(dados,erro) {

		if(erro!=undefined)	{
			setDisabled('salvarEmitirID', true);
			alert(erro);
			return;
		}

		var valor = getNestedBeanPropertyValue(dados, "servicosAdicionais.valor");
		setElementValue("servicosAdicionais:0.valor", setFormat(document.getElementById("servicosAdicionais:0.valor"), valor));

		var valorTotal = getNestedBeanPropertyValue(dados, "valorTotal");
		setElementValue("valorTotal", setFormat(document.getElementById("valorTotal"), valorTotal));

		var totalTributos = getNestedBeanPropertyValue(dados, "totalTributos");
		setElementValue("totalTributos", setFormat(document.getElementById("totalTributos"), totalTributos));

		tributosGridDef.resetEditGrid();

		for(var i = 0; i < dados.tributos.length ; i++) {
			tributosGridDef.insertRow();
			var nmTarget = "tributos:" + i;
			setElementValue(nmTarget + ".nome", dados.tributos[i].nome);
			setElementValue(nmTarget + ".valor", setFormat(document.getElementById(nmTarget + ".valor"), dados.tributos[i].valor));
		}

		if(dados && dados != undefined && getElementValue("idNotaFiscalServico") == ''){
			setDisabled('salvarEmitirID', false);
		}
		setDisabled("processarCalcManualId",true);
	}

	function closePopup(retorno) {
		window.returnValue = retorno;
		self.close();
	}

	/*
	* Verifica se valor informado para calculo Manual
	* eh diferente do digitado anterior, caso afirmativo 
	* desabilita dotao de Calculo;
	*/
	function verifyChangeValue(rowIndex, columnName, obj) {
		if (_tpCalculo == TP_CALCULO_MANUAL) {
			var value = getElementValue(obj);
			setDisabled("processarCalcManualId",_valorFrete == value);
		}
		return true;
	}

	
	
</script>