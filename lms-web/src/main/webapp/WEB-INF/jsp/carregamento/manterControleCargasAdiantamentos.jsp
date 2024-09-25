<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.carregamento.manterControleCargasAction" >

	<adsm:form action="/carregamento/manterControleCargas" idProperty="idReciboFreteCarreteiro" 
			   service="lms.carregamento.manterControleCargasAction.findByIdAdiantamento" onDataLoadCallBack="retorno_carregaDados">

		<adsm:hidden property="idControleCarga" />
		<adsm:hidden property="filialByIdFilialOrigem.idFilial" />
		<adsm:hidden property="idMeioTransporte" />
		<adsm:hidden property="idMotorista" />
		<adsm:hidden property="idProprietario" />
		
		<adsm:textbox dataType="text" label="controleCargas" property="filialByIdFilialOrigem.sgFilial"
					  size="3" labelWidth="16%" width="84%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:textbox dataType="decimal" property="pcAdiantamentoFrete" label="percentualAdiantamento" 
					  minValue="0.01" maxValue="100" onchange="return calculaVlBruto();"
					  labelWidth="16%" width="44%" size="5" maxLength="5" required="true" />

		<adsm:textbox label="valorAdiantamento" property="moedaPais.moeda.siglaSimbolo" dataType="text" size="6" 
					  labelWidth="15%" width="25%" disabled="true" serializable="false" >
			<adsm:textbox property="vlBruto" dataType="currency" mask="###,###,###,###,##0.00" disabled="true" />
		</adsm:textbox>

		<adsm:textbox property="proprietario.pessoa.nrIdentificacaoFormatado" label="proprietario" dataType="text" size="20" 
					  labelWidth="16%" width="84%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox property="beneficiario.pessoa.nrIdentificacaoFormatado" label="beneficiario" dataType="text" size="20" 
					  labelWidth="16%" width="84%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="beneficiario.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="banco" property="contaBancaria.agenciaBancaria.banco.nrBanco" dataType="integer" size="4" 
					  labelWidth="16%" width="84%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="contaBancaria.agenciaBancaria.banco.nmBanco" size="50" disabled="true" 
						  serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="agencia" property="contaBancaria.agenciaBancaria.nrAgenciaBancaria" dataType="integer" size="4" 
					  labelWidth="16%" width="44%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="contaBancaria.agenciaBancaria.nmAgenciaBancaria" size="50" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="contaCorrente" property="contaBancaria.nrContaBancaria" dataType="integer" labelWidth="15%" width="25%" 
					  disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="contaBancaria.dvContaBancaria" size="2" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textarea label="observacoes" property="obReciboFreteCarreteiro" maxLength="500" rows="2" columns="120" 
					   labelWidth="16%" width="84%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarAdiantamento" id="botaoSalvar" service="lms.carregamento.manterControleCargasAction.storeAdiantamento" />
			<adsm:button caption="limpar" id="botaoLimpar" onclick="limparOnClick(this.form);" />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idReciboFreteCarreteiro" property="recibosFreteCarreteiro" selectionMode="none" rows="7" 
			   autoSearch="false" onRowClick="populaForm" 
			   service="lms.carregamento.manterControleCargasAction.findPaginatedAdiantamentos" 
			   rowCountService="lms.carregamento.manterControleCargasAction.getRowCountAdiantamentos"
			   onDataLoadCallBack="retornoGrid"
			   >
		<adsm:gridColumn title="valor"			property="moedaPais.moeda.siglaSimbolo" width="50" align="left" />
		<adsm:gridColumn title=""				property="vlBruto" dataType="currency" width="90" align="right" />
		<adsm:gridColumn title="beneficiario" 	property="beneficiario.pessoa.nmPessoa" width="" />
		<adsm:gridColumn title="banco" 			property="contaBancaria.agenciaBancaria.banco.nrBanco" width="10%" align="right" />
		<adsm:gridColumn title="agencia" 		property="contaBancaria.agenciaBancaria.nrAgenciaBancaria" width="10%" align="right" />
		<adsm:gridColumn title="contaCorrente" 	property="contaBancaria.nrContaBancaria" width="14%" align="right" />
		<adsm:gridColumn title="emitirRecibo2"  property="emitirRecibo2" image="/images/printer.gif" width="12%" link="javascript:geraRecibo" linkIdProperty="idReciboFreteCarreteiro" align="center" />
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

<script>
var _tabGroup = getTabGroup(this.document);
if (_tabGroup != undefined) {
	_tabGroup.getTab("adiantamento").properties.ignoreChangedState = true;
}

function initWindow(eventObj) {
	if (eventObj.name == "tab_click" || eventObj.name == "tab_load") {
		resetValue(this.document.forms[0]);
		setDisabled("botaoSalvar", false);
		populaDadosMaster();
		povoaGrid();
	}
	else
	if (eventObj.name == "storeButton") {
		desabilitaCampos(true);
		povoaGrid();
	}
	setDisabled("botaoLimpar", false);
	setFocusOnFirstFocusableField();
}

function limparOnClick(form) {
	resetFormValue(form);
	novoAdiantamento();
	setDisabled("botaoSalvar", false);
	setFocus(document.getElementById("pcAdiantamentoFrete"));
}

function retornoGrid_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	findBeneficiario();
}


function novoAdiantamento() {
	populaDadosMaster();
	findBeneficiario();
	desabilitaCampos(false);
}

function desabilitaCampos(valor) {
	setDisabled("pcAdiantamentoFrete", valor);
	setDisabled("botaoSalvar", valor);
	document.getElementById("obReciboFreteCarreteiro").readOnly=valor;
}


function populaDadosMaster() {
	var tabGroup = getTabGroup(this.document);
    var tabDet = tabGroup.getTab("cad");
    setElementValue("idControleCarga", tabDet.getFormProperty("idControleCarga"));
    setElementValue("filialByIdFilialOrigem.sgFilial", tabDet.getFormProperty("filialByIdFilialOrigem.sgFilial"));
    setElementValue("filialByIdFilialOrigem.idFilial", tabDet.getFormProperty("filialByIdFilialOrigem.idFilial"));
    setElementValue("nrControleCarga", tabDet.getFormProperty("nrControleCarga"));
    setElementValue("idMeioTransporte", tabDet.getFormProperty("meioTransporteByIdTransportado.idMeioTransporte"));
    setElementValue("idMotorista", tabDet.getFormProperty("motorista.idMotorista"));
    setElementValue("idProprietario", tabDet.getFormProperty("proprietario.idProprietario"));
}

function limparDadosAbaAdiantamento() {
	resetValue("proprietario.pessoa.nrIdentificacaoFormatado");
	resetValue("proprietario.pessoa.nmPessoa");
	resetValue("beneficiario.pessoa.nrIdentificacaoFormatado");
	resetValue("beneficiario.pessoa.nmPessoa");
	resetValue("contaBancaria.agenciaBancaria.banco.nrBanco");
	resetValue("contaBancaria.agenciaBancaria.banco.nmBanco");
	resetValue("contaBancaria.agenciaBancaria.nrAgenciaBancaria");
	resetValue("contaBancaria.agenciaBancaria.nmAgenciaBancaria");
	resetValue("contaBancaria.nrContaBancaria");
	resetValue("contaBancaria.dvContaBancaria");
}

function calculaVlBruto() {
	if (getElementValue("pcAdiantamentoFrete") != "")
		realizaCalculo();
	else {
		resetValue("moedaPais.moeda.siglaSimbolo");
		resetValue("vlBruto");
	}
}

function realizaCalculo() {
	var tabGroup = getTabGroup(this.document);
    var tabDet = tabGroup.getTab("cad");
    var vlFreteCarreteiro = tabDet.getFormProperty("vlFreteCarreteiro");

	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.getVlAdiantamento", "retornoRealizaCalculo", 
			{pcAdiantamentoFrete:getElementValue('pcAdiantamentoFrete'),
			 vlFreteCarreteiro:prepareStringToNumber(vlFreteCarreteiro),
			 idControleCarga:getElementValue("idControleCarga"),
			 idFilialOrigem:getElementValue("filialByIdFilialOrigem.idFilial")});
	xmit({serviceDataObjects:[sdo]});
}

function retornoRealizaCalculo_cb(data, error) {
	if (error != undefined) {
		resetValue("moedaPais.moeda.siglaSimbolo");
		resetValue("pcAdiantamentoFrete");
		alert(error);
		setFocus(document.getElementById("pcAdiantamentoFrete"));
		return false;
	}
	var vlBruto = getFormattedValue("currency", getNestedBeanPropertyValue(data, "vlAdiantamento"), "", true);
	if (vlBruto != undefined && vlBruto != "") {
		var tabGroup = getTabGroup(this.document);
	    var tabDet = tabGroup.getTab("cad");
	    setElementValue("moedaPais.moeda.siglaSimbolo", tabDet.getFormProperty("moedaVlFreteCarreteiro"));
		setElementValue("vlBruto", vlBruto);
		format(document.getElementById("vlBruto"));
	}
}


function povoaGrid() {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "idControleCarga", getElementValue("idControleCarga"));
    recibosFreteCarreteiroGridDef.executeSearch(filtro);
}


function populaForm(valor) {
	onDataLoad(valor);
	populaDadosMaster();
	return false;
}


function retorno_carregaDados_cb(data, error) {
	onDataLoad_cb(data, error);
	desabilitaCampos(true);
}


function findBeneficiario() {
	var tabGroup = getTabGroup(this.document);
    var tabDet = tabGroup.getTab("cad");

	limparDadosAbaAdiantamento();
	setElementValue("proprietario.pessoa.nrIdentificacaoFormatado", tabDet.getFormProperty("proprietarioViagem.pessoa.nrIdentificacaoFormatado"));
	setElementValue("proprietario.pessoa.nmPessoa", tabDet.getFormProperty("proprietarioViagem.pessoa.nmPessoa"));
	var idProprietario = tabDet.getFormProperty("proprietario.idProprietario");
	if (idProprietario != "" && idProprietario != undefined) {
		var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.findBeneficiario", "retornoFindBeneficiario", 
			{idProprietario:idProprietario});
	    xmit({serviceDataObjects:[sdo]});
	}
}

function retornoFindBeneficiario_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	if (data != undefined) {
		setElementValue("beneficiario.pessoa.nrIdentificacaoFormatado", getNestedBeanPropertyValue(data, "nrIdentificacaoBeneficiario"));
		setElementValue("beneficiario.pessoa.nmPessoa", getNestedBeanPropertyValue(data, "nmPessoaBeneficiario"));
		setElementValue("contaBancaria.agenciaBancaria.banco.nrBanco", getNestedBeanPropertyValue(data, "nrBanco"));
		setElementValue("contaBancaria.agenciaBancaria.banco.nmBanco", getNestedBeanPropertyValue(data, "nmBanco"));
		setElementValue("contaBancaria.agenciaBancaria.nrAgenciaBancaria", getNestedBeanPropertyValue(data, "nrAgenciaBancaria"));
		setElementValue("contaBancaria.agenciaBancaria.nmAgenciaBancaria", getNestedBeanPropertyValue(data, "nmAgenciaBancaria"));
		setElementValue("contaBancaria.nrContaBancaria", getNestedBeanPropertyValue(data, "nrContaBancaria"));
		setElementValue("contaBancaria.dvContaBancaria", getNestedBeanPropertyValue(data, "dvContaBancaria"));
	}
}


function geraRecibo(idReciboFreteCarreteiro) {
	var sdo = createServiceDataObject('lms.fretecarreteiroviagem.manterRecibosAction.execute', 'openPdfRecibo', 
		{idReciboFreteCarreteiro:idReciboFreteCarreteiro}); 
	xmit({serviceDataObjects:[sdo]});
}

function openPdfRecibo_cb(data, error) {
	openReportWithLocator(data, error);
	povoaGrid();
}
</script>