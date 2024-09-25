<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterServicosAdicionaisClienteAction" onPageLoadCallBack="myPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01050"/>
	</adsm:i18nLabels>
	<adsm:form action="/vendas/manterServicosAdicionaisCliente" idProperty="idServicoAdicionalCliente" 
		newService="lms.vendas.manterServicosAdicionaisClienteAction.newMaster" onDataLoadCallBack="odlc">
		<adsm:hidden property="tabelaDivisaoCliente.idTabelaDivisaoCliente"/>
		<adsm:hidden property="sgMoeda"/>
		<adsm:hidden property="dsSimbolo"/>
		
		<adsm:complement label="cliente" labelWidth="15%" width="40%">
			<adsm:textbox property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao" dataType="text" size="20" disabled="true" serializable="false"/>
			<adsm:textbox property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa" dataType="text" size="28" disabled="true" serializable="false"/>
		</adsm:complement>
		<adsm:textbox labelWidth="15%" width="30%" label="divisao" property="tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente" dataType="text" size="30" disabled="true" serializable="false"/>

		<adsm:complement label="tabelaBase" labelWidth="15%" width="40%">
			<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.idTabelaPreco" value="0" serializable="false"/>
			<adsm:textbox property="tabelaDivisaoCliente.tabelaPreco.tabelaPrecoString" dataType="text" size="10" disabled="true" serializable="false"/>
			<adsm:textbox property="tabelaDivisaoCliente.tabelaPreco.dsDescricao" dataType="text" size="30" disabled="true" serializable="false"/>
		</adsm:complement>
		<adsm:textbox labelWidth="15%" width="30%" label="servico" property="tabelaDivisaoCliente.servico.dsServico" dataType="text" size="30" disabled="true" serializable="false"/>

		<adsm:textbox labelWidth="15%" width="40%" label="moeda" property="tabelaDivisaoCliente.tabelaPreco.moeda.sgMoeda.siglaSimbolo" dataType="text" size="9" disabled="true" serializable="false"/>
		<adsm:hidden property="parcelaPreco.tpParcelaPreco" value="S" serializable="false"/>
		
		<adsm:combobox labelWidth="15%" width="30%" label="servicoAdicional" property="parcelaPreco.idParcelaPreco" service="lms.vendas.manterServicosAdicionaisClienteAction.findTabelaPrecoParcelaComboActiveValues" optionProperty="idParcelaPreco" optionLabelProperty="nmParcelaPreco" boxWidth="200" required="true" onlyActiveValues="true" autoLoad="true" onDataLoadCallBack="ppcombo" onchange="onChangeServicoAdicional();">
			<adsm:propertyMapping criteriaProperty="tabelaDivisaoCliente.tabelaPreco.idTabelaPreco" modelProperty="tabelaPreco.idTabelaPreco"/>
			<adsm:propertyMapping criteriaProperty="parcelaPreco.tpParcelaPreco" modelProperty="parcelaPreco.tpParcelaPreco"/>
		</adsm:combobox>

		<adsm:combobox labelWidth="15%" width="40%" label="indicador" property="tpIndicador" domain="DM_INDICADOR_PARAMETRO_CLIENTE" required="true" onchange="ajustaCampos();"/>
		<adsm:textbox labelWidth="15%" width="30%" label="valor" property="vlValor" dataType="currency" size="10" maxLength="18" required="true" onchange="return onChangeValue('tpIndicador','vlValor');"/>

		<adsm:textbox 
			label="quantidadeDias" 
			property="nrQuantidadeDias" 
			dataType="integer" 
			size="5" 
			maxLength="5" 
			onchange="return onChangeQtdeDias()"
			labelWidth="15%" width="40%"
		/>

		<adsm:textbox 
			dataType="currency" 
			label="valorMinimo" 
			property="vlMinimo" 
			mask="###,###,###,###,##0.00"
			onchange="return onChangeValorMinimo()"
			size="18" 
			labelWidth="15%" width="30%"
		/>
		
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
		
		<adsm:checkbox
			label="pagaParaTodos"  	
			property="blPagaParaTodos"
			labelWidth="15%" 
			width="30%"
			onclick="onBlPagaParaTodosClick()"
		/>
		
		<adsm:combobox labelWidth="15%" width="40%" 
			label="unidadeCalculoServicoAdicional" 
			property="tpUnidMedidaCalcCobr" 
			domain="DM_TIPO_CALCULO_COBRANCA_SERV_AD"/>
			
		<adsm:checkbox
			label="cobrancaCte"  	
			property="blCobrancaCte" 
			labelWidth="15%" 
			width="30%" 
		/>	
	
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton" service="lms.vendas.manterServicosAdicionaisClienteAction.preStore" callbackProperty="storeButton"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript">
function storeButton_cb(data, error, key) {					
	var service = "lms.vendas.manterServicosAdicionaisClienteAction.store";
	
	if(key == 'LMS-04460') {
		if(confirm(error)) {			
			storeButtonScript(service, "storeButton", document.forms[0]);
			return;
		} else {
			return;
		}
	}
	
	if(key == 'LMS-04577') {
		if(confirm(error)) {			
			storeButtonScript(service, "storeButton", document.forms[0]);
			return;
		} else {
			return;
		}
	}
	
	var retorno  =getNestedBeanPropertyValue(data, "validated");
	if(retorno == true || retorno == 'true') {
		storeButtonScript(service, "storeButton", document.forms[0]);
		return;
	}			
	
	store_cb(data, error, key);	
}

function ppcombo_cb(dados, erros) {
	if(erros!=undefined) {
		alert(erros);
		return;
	}
	parcelaPreco_idParcelaPreco_cb(dados,erros);
	var obj = document.getElementById("parcelaPreco.idParcelaPreco");
	obj.masterLink = "true";
}

function str2number(str) {
	if ((str == "")||(str == null)||(str.length == 0)) {
		return 0;
	}
	return parseFloat(str);
}

function validateTab() {
	if(validateTabScript(document.forms) && onChangeValue('tpIndicador','vlValor') && onChangeValorMinimo() && onChangeQtdeDias()) {
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

function onBlPagaParaTodosClick() {
	refreshTabDestinatarios();
}

function refreshTabDestinatarios() {
	var tabGroup = getTabGroup(this.document);	
	
	if(getElementValue("blPagaParaTodos") == false) {
		tabGroup.setDisabledTab("destinatarios", false);
	} else {
		tabGroup.setDisabledTab("destinatarios", true);
	}
}

function onChangeValue(comboName,valueName) {
	var field = getElement(comboName);
	var type = getElementValue(field);
	var value = str2number(getElementValue(valueName));
	if(type=="D") {
		if(value<0 || value>100) {
			alertI18nMessage("LMS-01050", field.label, false);
			return false;
		}
	} else {
		if(value<0) {
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
		
		refreshTabDestinatarios();
		
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
		notifyElementListeners({e:document.getElementById("parcelaPreco.tpParcelaPreco")});
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
			setDisabled(objVlValor, false);
			setDisabled(objVlMinimo, false);
		}		
		
		isParcelaKmExcedenteOrVeiculoDedicado();
		isParcelaSeguroCargaPermanencia();
		
	}

	function onChangeServicoAdicional(){
		isParcelaKmExcedenteOrVeiculoDedicado();
		isParcelaSeguroCargaPermanencia();
		isTaxaPermanenciaCargaOrTaxaFielDepositario();
	}
	
	function isTaxaPermanenciaCargaOrTaxaFielDepositario(defaultValue){
		var id = getElementValue("parcelaPreco.idParcelaPreco");
	    
		var sdo = createServiceDataObject("lms.vendas.manterServicosAdicionaisClienteAction.isTaxaPermanenciaCargaOrTaxaFielDepositario", "isTaxaPermanenciaCargaOrTaxaFielDepositarioInsertNew", {idParcelaPreco:id});
	    xmit({serviceDataObjects:[sdo]});
		
	}
	
	function isTaxaPermanenciaCargaOrTaxaFielDepositarioInsertNew_cb(data,error){
		if (error != undefined) {
			alert(error);
			return;
		}
		
		tpUnidMedidaCalcCobr = document.getElementById("tpUnidMedidaCalcCobr");
		setDisabled(tpUnidMedidaCalcCobr, false);
		tpUnidMedidaCalcCobr.required =  true;
		
		objVlMinimo = document.getElementById("vlMinimo");
		setDisabled(objVlMinimo, false);
		
		if(data._value == "true"){
			setElementValue(tpUnidMedidaCalcCobr, "K");
		} else {
			setElementValue(tpUnidMedidaCalcCobr, "T");
			
			setElementValue(objVlMinimo, setFormat(objVlMinimo, "0"));
			setDisabled(objVlMinimo, true);
		}
	}
	
	function isParcelaKmExcedenteOrVeiculoDedicado(){
		var id = getElementValue("parcelaPreco.idParcelaPreco");
	    
		var sdo = createServiceDataObject("lms.vendas.manterServicosAdicionaisClienteAction.isParcelaKmExcedenteOrVeiculoDedicado", "isParcelaKmExcedenteOrVeiculoDedicado", {idParcelaPreco:id});
	    xmit({serviceDataObjects:[sdo]});
	}

	function isParcelaKmExcedenteOrVeiculoDedicado_cb(data,error){
		if (error != undefined) {
			alert(error);
			return;
		}
		
		objVlMinimo = document.getElementById("vlMinimo");

		if(data._value == "true"){
			setDisabled(objVlMinimo, true);
		} else {
			setDisabled(objVlMinimo, false);
		}
		
	}
	
	function isParcelaSeguroCargaPermanencia(){
		var id = getElementValue("parcelaPreco.idParcelaPreco");
	    
		var sdo = createServiceDataObject("lms.vendas.manterServicosAdicionaisClienteAction.isParcelaSeguroCargaPermanencia", "isParcelaSeguroCargaPermanencia", {idParcelaPreco:id});
	    xmit({serviceDataObjects:[sdo]});
	}

	function isParcelaSeguroCargaPermanencia_cb(data,error){
		if (error != undefined) {
			alert(error);
			return;
		}

		objNrDecursoPrazo= document.getElementById("nrDecursoPrazo");
		objNrQuantidadeDias= document.getElementById("nrQuantidadeDias");
		
		if(data._value == "true"){
			setElementValue(objNrDecursoPrazo, setFormat(objNrDecursoPrazo, ""));
			setElementValue(objNrQuantidadeDias, setFormat(objNrQuantidadeDias, ""));
			setDisabled(objNrDecursoPrazo, true);
			setDisabled(objNrQuantidadeDias, true);
		} else {
			setDisabled(objNrDecursoPrazo, false);
			setDisabled(objNrQuantidadeDias, false);
		}
		
	}


function initWindow(eventObj) {	
	var frame = parent.document.frames["pesq_iframe"];
	var tabGroup = getTabGroup(this.document);	

	if (eventObj.name == "newButton_click" || eventObj.name == "removeButton" || (
			eventObj.name == "tab_click" && tabGroup.oldSelectedTab.properties.id == "pesq")) {	
		setElementValue("parcelaPreco.idParcelaPreco","");
		setElementValue("blCobrancaRetroativa", true);
		setElementValue("blPagaParaTodos", true);
		ajustaCampos();			
	}	
	
	if(eventObj.name == "tab_click"){
		isTaxaPermanenciaCargaOrTaxaFielDepositario();
	}
	
}

</script>
