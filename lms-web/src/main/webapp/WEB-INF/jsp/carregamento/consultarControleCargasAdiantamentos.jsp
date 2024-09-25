<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.carregamento.consultarControleCargasAction" >

	<adsm:form action="/carregamento/consultarControleCargas" idProperty="idReciboFreteCarreteiro" 
			   service="lms.carregamento.consultarControleCargasAction.findByIdAdiantamento" >

		<adsm:hidden property="idControleCarga" />
		
		<adsm:textbox dataType="text" label="controleCargas" property="filialByIdFilialOrigem.sgFilial"
					  size="3" labelWidth="16%" width="84%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:textbox dataType="decimal" property="pcAdiantamentoFrete" label="percentualAdiantamento" 
					  labelWidth="16%" width="44%" size="5" maxLength="5" disabled="true" />

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
					   labelWidth="16%" width="84%" disabled="true" />

	</adsm:form>

	<adsm:grid idProperty="idReciboFreteCarreteiro" property="recibosFreteCarreteiro" selectionMode="none" rows="8" 
			   autoSearch="false" onRowClick="populaForm" 
			   service="lms.carregamento.consultarControleCargasAction.findPaginatedAdiantamentos" 
			   rowCountService="lms.carregamento.consultarControleCargasAction.getRowCountAdiantamentos"
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

function initWindow(eventObj) {
	if (eventObj.name == "tab_click" || eventObj.name == "tab_load") {
		resetValue(this.document.forms[0]);
		setDisabled("obReciboFreteCarreteiro", false);
		document.getElementById("obReciboFreteCarreteiro").readOnly=true;
		populaDadosMaster();
		povoaGrid();
	}
	setFocusOnFirstFocusableField();
}


function retornoGrid_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
}


function populaDadosMaster() {
	var tabGroup = getTabGroup(this.document);
    var tabDet = tabGroup.getTab("cad");
    setElementValue("idControleCarga", tabDet.getFormProperty("idControleCarga"));
    setElementValue("filialByIdFilialOrigem.sgFilial", tabDet.getFormProperty("filialByIdFilialOrigem.sgFilial"));
    setElementValue("nrControleCarga", tabDet.getFormProperty("nrControleCarga"));
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

function geraRecibo(idReciboFreteCarreteiro) {
	var sdo = createServiceDataObject('lms.fretecarreteirocoletaentrega.emitirReciboAction.execute', 'openReportWithLocator', 
		{reciboFreteCarreteiro:{idReciboFreteCarreteiro:idReciboFreteCarreteiro}}); 
	
	xmit({serviceDataObjects:[sdo]});
}

</script>