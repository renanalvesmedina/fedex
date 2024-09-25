<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.vendas.manterPropostasServicosAdicionaisAction" 
	onPageLoadCallBack="myPageLoad">
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-01050"/>
	</adsm:i18nLabels>

	<adsm:form action="/vendas/manterServicosAdicionaisCliente" idProperty="idServicoAdicionalCliente" onDataLoadCallBack="odlc">
		<adsm:hidden property="simulacao.idSimulacao"/>
		<adsm:hidden property="sgMoeda"/>
		<adsm:hidden property="dsSimbolo"/>
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>

		<adsm:complement label="cliente" labelWidth="15%" width="40%">
			<adsm:textbox
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao"
				dataType="text"
				size="20"
				disabled="true"
				serializable="false"/>
			<adsm:textbox
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa"
				dataType="text"
				size="28"
				disabled="true"
				serializable="false"/>
		</adsm:complement>

		<adsm:textbox
			labelWidth="15%"
			width="30%"
			label="divisao"
			property="tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"
			dataType="text"
			size="30"
			disabled="true"
			serializable="false"/>

		<adsm:complement label="tabelaBase" labelWidth="15%" width="40%">
			<adsm:hidden
				property="tabelaDivisaoCliente.tabelaPreco.idTabelaPreco"
				value="0"
				serializable="false"/>
			<adsm:textbox
				property="tabelaDivisaoCliente.tabelaPreco.tabelaPrecoString"
				dataType="text"
				size="10"
				disabled="true"
				serializable="false"/>
			<adsm:textbox
				property="tabelaDivisaoCliente.tabelaPreco.dsDescricao"
				dataType="text"
				size="30"
				disabled="true"
				serializable="false"/>
		</adsm:complement>

		<adsm:textbox
			labelWidth="15%"
			width="30%"
			label="servico"
			property="tabelaDivisaoCliente.servico.dsServico"
			dataType="text"
			size="30"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			labelWidth="15%"
			width="40%"
			label="moeda"
			property="tabelaDivisaoCliente.tabelaPreco.moeda.sgMoeda.siglaSimbolo"
			dataType="text"
			size="9"
			disabled="true"
			serializable="false"/>

		<adsm:hidden property="parcelaPreco.tpParcelaPreco" value="S" serializable="false"/>
		<adsm:combobox
			labelWidth="15%"
			width="30%"
			label="servicoAdicional"
			property="parcelaPreco.idParcelaPreco"
			service="lms.vendas.manterPropostasServicosAdicionaisAction.findTabelaPrecoParcelaCombo"
			optionProperty="idParcelaPreco"
			optionLabelProperty="nmParcelaPreco"
			boxWidth="200"
			required="true"
			onlyActiveValues="true"
			autoLoad="false"
			onDataLoadCallBack="ppcombo"
			onchange="onChangeServicoAdicional();">
		</adsm:combobox>

		<adsm:combobox
			labelWidth="15%"
			width="40%"
			label="indicador"
			property="tpIndicador"
			domain="DM_INDICADOR_PARAMETRO_CLIENTE"
			required="true"
			onchange="ajustaCampos();"/>

		<adsm:textbox
			labelWidth="15%"
			width="30%"
			label="valor"
			property="vlValor"
			dataType="currency"
			size="10"
			maxLength="18"
			required="true"
			disabled="true"
			onchange="return onChangeTpIndicador();"/>

		<adsm:textbox 
			label="quantidadeDias" 
			property="nrQuantidadeDias" 
			dataType="integer" 
			size="5" 
			maxLength="5" 
			onchange="return onChangeQtdeDias()"
			labelWidth="15%" width="40%"/>

		<adsm:textbox 
			dataType="currency" 
			label="valorMinimo" 
			property="vlMinimo" 
			mask="###,###,###,###,##0.00"
			onchange="return onChangeValorMinimo()"
			size="18" 
			labelWidth="15%" width="30%"/>

		<adsm:combobox
			label="formaCobranca"
			property="tpFormaCobranca"
			required="true"
			labelWidth="15%"
			width="40%"
			defaultValue="P"
			domain="DM_FORMA_COBRANCA_SERVICO"
		/>

		<adsm:checkbox
			label="cobrancaRetroativa"
			property="blCobrancaRetroativa"
			labelWidth="15%"
			width="30%"
		/>

		<adsm:textbox
			label="decursoPrazo"
			property="nrDecursoPrazo"
			dataType="integer"
			size="5"
			maxLength="3"
			minValue="1" maxValue="999"
			labelWidth="15%" width="40%"
		/>
		
		<adsm:combobox labelWidth="15%" width="40%" 
			label="unidadeCalculoServicoAdicional" 
			property="tpUnidMedidaCalcCobr" 
			domain="DM_TIPO_CALCULO_COBRANCA_SERV_AD"/>

	<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript">
	function onChangeServicoAdicional(){
		isTaxaPermanenciaCargaOrTaxaFielDepositario();
	}

	function ppcombo_cb(dados, erros) {
		if(erros!=undefined) {
			alert(erros);
			return;
		}
		parcelaPreco_idParcelaPreco_cb(dados,erros);
		getElement("parcelaPreco.idParcelaPreco").masterLink = "true";
	}

	function str2number(str) {
		if ((str == "")||(str == null)||(str.length == 0)) {
			return 0;
		}
		return parseFloat(str);
	}

	function validateTab() {
		if(validateTabScript(document.forms) && onChangeTpIndicador() && onChangeValorMinimo() && onChangeQtdeDias()) {
			return true;
		}
		return false;
	}

	function onChangeValorMinimo() {
		var field = getElement("vlMinimo");
		var value = str2number(getElementValue(field));
		if(value<0) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
		return true;
	}

	function onChangeQtdeDias() {
		var field = getElement("nrQuantidadeDias");
		var value = str2number(getElementValue(field));
		if(value<0) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
		return true;
	}

	function onChangeTpIndicador() {
		var field = getElement("tpIndicador");
		var type = getElementValue(field);
		var value = str2number(getElementValue("vlValor"));
		if(type=="D") {
			if( (value < 0) || (value > 100) ) {
				alertI18nMessage("LMS-01050", field.label, false);
				return false;
			}
		} else {
			if(value < 0) {
				alertI18nMessage("LMS-01050", field.label, false);
				return false;
			}
		}
		return true;
	}

	function odlc_cb(data,errMsg) {
		if(errMsg!=undefined){
			alert(errMsg);
			return;
		}
		onDataLoad_cb(data,errMsg);
		ajustaCampos();
		
		tpUnidMedidaCalcCobr = document.getElementById("tpUnidMedidaCalcCobr");
		if(getElementValue(tpUnidMedidaCalcCobr) == ""){
			setDisabled(tpUnidMedidaCalcCobr, true);
			tpUnidMedidaCalcCobr.required =  false;
			setElementValue(tpUnidMedidaCalcCobr, "");
		}else{
			setDisabled(tpUnidMedidaCalcCobr, false);
			tpUnidMedidaCalcCobr.required =  true;
		}
		
	}

	function myPageLoad_cb(data,errMsg) {
		if(errMsg!=undefined){
			alert(errMsg);
			return;
		}
		onPageLoad_cb(data,errMsg);
		findComboParcelas();
		notifyElementListeners({e:document.getElementById("parcelaPreco.idParcelaPreco")});
		disableWorkflow();
	}

	function ajustaCampos() {
		tpIndicador = getElementValue("tpIndicador");
		objVlValor = document.getElementById("vlValor");
		objVlMinimo = document.getElementById("vlMinimo");
		if(tpIndicador == "T") {
			setElementValue(objVlValor, setFormat(objVlValor, "0"));
			setElementValue(objVlMinimo, setFormat(objVlMinimo, "0"));
			setDisabled(objVlValor, true);
			setDisabled(objVlMinimo, true);
		} else {
			if (getElementValue("idProcessoWorkflow") == "") {
				setDisabled(objVlValor, false);
				setDisabled(objVlMinimo, false);
			}
		}
	}

	function initWindow(eventObj) {
		if (eventObj.name == "newButton_click" || eventObj.name == "tab_click" || eventObj.name == "removeButton") {
			setElementValue("parcelaPreco.idParcelaPreco","");
			setElementValue("blCobrancaRetroativa", true);
			ajustaCampos();
		}
		disableWorkflow();
		
		if(eventObj.name == "tab_click"){
			isTaxaPermanenciaCargaOrTaxaFielDepositario();
		}
		
	}

	function findComboParcelas(){
		var params = [];
		params.tpSituacao = "S";
		params.tabelaPreco = {idTabelaPreco: getElementValue("tabelaDivisaoCliente.tabelaPreco.idTabelaPreco")};
		params.parcelaPreco= {tpParcelaPreco: getElementValue("parcelaPreco.tpParcelaPreco")};

		var service = "lms.vendas.manterPropostasServicosAdicionaisAction.findTabelaPrecoParcelaCombo";
		var sdo = createServiceDataObject(service, "ppcombo", params);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Funcao que é chamada quando a tela vem pelo workflow, visto que
	 * nesse caso ela será somente de consulta.
	 */
	function disableWorkflow() {
		if (getElementValue("idProcessoWorkflow") != "") {
	   		setDisabled(document, true);
	   	}
	}
	
	function isTaxaPermanenciaCargaOrTaxaFielDepositario(){
		var id = getElementValue("parcelaPreco.idParcelaPreco");
	    
		var sdo = createServiceDataObject("lms.vendas.manterPropostasServicosAdicionaisAction.isTaxaPermanenciaCargaOrTaxaFielDepositario", "isTaxaPermanenciaCargaOrTaxaFielDepositario", {idParcelaPreco:id});
	    xmit({serviceDataObjects:[sdo]});
	}
	
	function isTaxaPermanenciaCargaOrTaxaFielDepositario_cb(data,error){
		if (error != undefined) {
			alert(error);
			return;
		}
		
		tpUnidMedidaCalcCobr = document.getElementById("tpUnidMedidaCalcCobr");

		if(data._value == "true"){
			setDisabled(tpUnidMedidaCalcCobr, false);
			tpUnidMedidaCalcCobr.required =  true;
			setElementValue(tpUnidMedidaCalcCobr, "T");
		} else {
			setDisabled(tpUnidMedidaCalcCobr, true);
			tpUnidMedidaCalcCobr.required =  false;
			setElementValue(tpUnidMedidaCalcCobr, "");
		}
		
	}
	
	
</script>