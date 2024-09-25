<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/carregamento/gerarControleCargas">

		<adsm:masterLink idProperty="idControleCarga" showSaveAll="true">
			<adsm:masterLinkItem property="controleCargaConcatenado" label="controleCargas" />
		</adsm:masterLink>

		<adsm:textbox dataType="decimal" property="pcAdiantamentoFrete" label="percentualAdiantamento" 
					  minValue="0.01" maxValue="100" onchange="return calculaVlBruto();"
					  labelWidth="18%" width="32%" size="5" maxLength="5" required="true" />

		<adsm:textbox label="valorAdiantamento" property="moedaVlAdiantamento" dataType="text" size="6" labelWidth="18%" width="32%" 
					  disabled="true" serializable="false" >
			<adsm:textbox property="vlBruto" dataType="currency" mask="###,###,###,###,##0.00" disabled="true" />
		</adsm:textbox>

		<adsm:textbox property="nrIdentificacaoProprietario" label="proprietario" dataType="text" size="20" 
					  labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="nomeProprietario" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox property="nrIdentificacaoBeneficiario" label="beneficiario" dataType="text" size="20" 
					  labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="nomeBeneficiario" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="banco" property="nrBanco" dataType="integer" size="4" width="82%" labelWidth="18%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="nmBanco" size="50" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="agencia" property="nrAgenciaBancaria" dataType="integer" size="4" width="82%" 
					  labelWidth="18%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="nmAgenciaBancaria" size="50" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="contaCorrente" property="nrContaBancaria" dataType="integer" labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="dvContaBancaria" size="2" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textarea label="observacoes" property="obReciboFreteCarreteiro" maxLength="500" rows="4" columns="80" 
					   labelWidth="18%" width="82%" onchange="return observacoes_OnChange()" />
		
		<adsm:buttonBar/>
	</adsm:form>
</adsm:window>

<script>
function initWindow(eventObj) {
	verificaPcAdiantamentoFreteObrigatorio();
	if (eventObj.name == "tab_click") {
		var idControleCarga = getTabGroup(this.document).getTab("cad").getFormProperty("idControleCarga");
		if (idControleCarga == undefined || idControleCarga == "" || idControleCarga == "-1") {
			setDisabled("pcAdiantamentoFrete", false);
			setDisabled("obReciboFreteCarreteiro", false);
		}
	}
	setFocusOnFirstFocusableField();
}

function verificaPcAdiantamentoFreteObrigatorio() {
	if (getElementValue("obReciboFreteCarreteiro") != "")
		document.getElementById("pcAdiantamentoFrete").required="true";
	else
		document.getElementById("pcAdiantamentoFrete").required="false";
}

function observacoes_OnChange() {
	verificaPcAdiantamentoFreteObrigatorio();
	return true;
}

function calculaVlBruto() {
	if (getElementValue("pcAdiantamentoFrete") != "")
		realizaCalculo();
	else {
		resetValue("moedaVlAdiantamento");
		resetValue("vlBruto");
	}
}

function realizaCalculo() {
	var tabGroup = getTabGroup(this.document);
    var tabDet = tabGroup.getTab("cad");
    var vlFreteCarreteiro = tabDet.getFormProperty("vlFreteCarreteiro");
    
	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.getVlAdiantamento", "retorno", 
			{pcAdiantamentoFrete:getElementValue('pcAdiantamentoFrete'),
			 vlFreteCarreteiro:prepareStringToNumber(vlFreteCarreteiro)});
	xmit({serviceDataObjects:[sdo]});
}

function retorno_cb(data, error) {
	if (error != undefined) {
		resetValue("moedaVlAdiantamento");
		resetValue("pcAdiantamentoFrete");
		alert(error);
		setFocus(document.getElementById("pcAdiantamentoFrete"));
		return false;
	}
	var vlBruto = getFormattedValue("currency", getNestedBeanPropertyValue(data, "vlAdiantamento"), "", true);
	if (vlBruto != undefined && vlBruto != "") {
		var tabGroup = getTabGroup(this.document);
	    var tabDet = tabGroup.getTab("cad");
	    setElementValue("moedaVlAdiantamento", tabDet.getFormProperty("moedaVlFreteCarreteiro"));
		setElementValue("vlBruto", vlBruto);
		format(document.getElementById("vlBruto"));
	}
}
</script>