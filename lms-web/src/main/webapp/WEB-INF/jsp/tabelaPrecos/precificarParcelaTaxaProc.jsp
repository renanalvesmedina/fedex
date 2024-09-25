<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.precificarParcelaTaxaAction" onPageLoad="carregaDados">
<script language="javascript">
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

	if(getElementValue('tabelaPrecoParcela.tabelaPreco.blEfetivada') == "true" || u.parameters["tabelaPreco.idPendencia"] != "" || u.parameters["isVisualizacaoWK"] == "true"){
		setDisabled('btnSalvar', true);
	}

	//verifica se já existe preço para a parcela
	var filter = [];
	setNestedBeanPropertyValue(filter, "idValorTaxa", idTabelaPrecoParcela);
	var sdo = createServiceDataObject("lms.tabelaprecos.valorTaxaService.find", "carregaDados", filter);
	xmit({serviceDataObjects:[sdo]});
}

function carregaDados_cb(data, error) {

	if(error != undefined) {
		alert(error);
		return;
	}

	var idValorTaxa = getNestedBeanPropertyValue(data,":0.idValorTaxa");
	//se já existe preço para a parcela
	//atribui os valores nos campos (para edição)
	if(idValorTaxa > 0) {
		setElementValue("idValorTaxa", idValorTaxa);
		var obj = document.getElementById("idValorTaxa");
		setElementValue(obj, setFormat(obj, getNestedBeanPropertyValue(data,":0.idValorTaxa")));

		if(getNestedBeanPropertyValue(data,":0.psTaxado") != undefined) {
			obj = document.getElementById("psTaxado");
			setElementValue(obj, setFormat(obj, getNestedBeanPropertyValue(data,":0.psTaxado")));
		}

		if(getNestedBeanPropertyValue(data,":0.vlTaxa") != undefined){
			obj = document.getElementById("vlTaxa");
			setElementValue(obj, setFormat(obj, getNestedBeanPropertyValue(data,":0.vlTaxa")));
		}

		if(getNestedBeanPropertyValue(data,":0.vlExcedente") != undefined){
			obj = document.getElementById("vlExcedente");
			setElementValue(obj, setFormat(obj, getNestedBeanPropertyValue(data,":0.vlExcedente")));
		}
	}
}
</script>
	<adsm:form
		action="/tabelaPrecos/precificarParcelaTaxa"
		idProperty="idValorTaxa">

		<adsm:hidden
			property="tabelaPrecoParcela.idTabelaPrecoParcela"/>

		<adsm:hidden
			property="tabelaPrecoParcela.tabelaPreco.blEfetivada"
			serializable="false"/>

		<adsm:complement
			label="tabela"
			width="55%">

			<adsm:textbox
				dataType="text"
		 		disabled="true"
				property="tabelaPrecoParcela.tabelaPreco.tabelaPrecoString"
		 		serializable="false"
		 		size="8"
				maxLength="7" />

			<adsm:textbox
				dataType="text"
		 		disabled="true"
				property="tabelaPrecoParcela.tabelaPreco.dsDescricao"
		 		serializable="false"
		 		size="60"/>

		</adsm:complement>

		<adsm:textbox
			dataType="text"
			disabled="true"
			label="moeda"
			labelWidth="13%"
			property="tabelaPrecoParcela.tabelaPreco.moeda.dsSimbolo"
			required="true"
			size="9"
			width="17%"/>

		<adsm:textbox
			dataType="text"
			disabled="true"
			label="parcela"
			property="tabelaPrecoParcela.parcelaPreco.nmParcelaPreco"
			required="true"
			size="60"
			width="55%"/>

		<adsm:textbox
			dataType="text"
			disabled="true"
			label="tipoParcela"
			labelWidth="13%"
			property="tabelaPrecoParcela.parcelaPreco.tpParcelaPreco"
			required="true"
			size="18"
			width="17%"/>

		<adsm:textbox
			dataType="weight"
			label="peso"
			minValue="0,001"
			property="psTaxado"
			size="18"
			width="55%"/>

		<adsm:textbox
			dataType="currency"
			label="valor"
			labelWidth="13%"
			mask="###,###,###,###,##0.00"
			minValue="0,01"
			property="vlTaxa"
			required="true"
			size="18"
			width="17%"/>

		<adsm:textbox
			dataType="currency"
			label="valorExcedente"
			mask="###,###,###,###,##0.00"
			minValue="0,01"
			property="vlExcedente"
			size="18"
			width="55%"/>

		<adsm:buttonBar>
			<adsm:storeButton
				id="btnSalvar"
				service="lms.tabelaprecos.precificarParcelaTaxaAction.storeAtualizaTabela"
				callbackProperty="storeCallBack"
				disabled="false"/>
		</adsm:buttonBar>

<script language="javascript">

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

	function validateTab() {
		return validateTabScript(document.forms) && validateToSave();
	}

	function validateToSave() {
		var peso = getElementValue("psTaxado");
		var valor = getElementValue("vlTaxa");
		var valorExcedente = getElementValue("vlExcedente");

		if( peso != "" && peso <= 0 ){
			alert('<adsm:label key="LMS-30005"/>');
			setFocus(document.getElementById("psTaxado"));
			return false;
		}
		if( valor != "" && valor <= 0 )	{
			alert('<adsm:label key="LMS-30005"/>');
			setFocus(document.getElementById("vlTaxa"));
			return false;
		}
		if( valorExcedente != "" && valorExcedente <= 0 ){
			alert('<adsm:label key="LMS-30005"/>');
			setFocus(document.getElementById("vlExcedente"));
			return false;
		}
		if( (peso == "" && valorExcedente != "") || (peso != "" && valorExcedente == "") ){
			alert('<adsm:label key="LMS-30004"/>');
			if (peso == ""){
				setFocus(document.getElementById("psTaxado"));
			} else {
				setFocus(document.getElementById("vlExcedente"));
			}
			return false;
		}
		return true;

	}


</script>

	</adsm:form>
</adsm:window>