<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.gerarControleCargasAction" >

	<adsm:form action="/carregamento/gerarControleCargas">
		<adsm:hidden property="idControleCarga" serializable="false" />

		<adsm:masterLink idProperty="idControleCarga" showSaveAll="true">
			<adsm:masterLinkItem property="controleCargaConcatenado" label="controleCargas" itemWidth="100" />
			<adsm:hidden property="_meioTransporteByIdTransportado.idMeioTransporte" />
			<adsm:hidden property="_meioTransporteByIdSemiRebocado.idMeioTransporte" />
			<adsm:hidden property="_rotaColetaEntrega.idRotaColetaEntrega" />
			<adsm:hidden property="_rotaIdaVolta.idRotaIdaVolta" />
			<adsm:hidden property="_rota.idRota" />
			<adsm:hidden property="_tpControleCargaValor" />
		</adsm:masterLink>

		<script>
			var labelNumeroCartao = '<adsm:label key="numeroCartao"/>';
			var lms_00061 = '<adsm:label key="LMS-00061"/>';
			var labelFormaPagamento = '<adsm:label key="formaPagamento"/>';
		</script>
	</adsm:form>


	<adsm:grid property="pagamentos" idProperty="idPagtoPedagioCc" 
			   selectionMode="none" gridHeight="120" showPagging="false" title="pagamento" scrollBars="vertical"
			   service="lms.carregamento.gerarControleCargasAction.findPaginatedPagtoPedagioCc"
			   rowCountService="lms.carregamento.gerarControleCargasAction.getRowCountPagtoPedagioCc"
			   onRowClick="pagamentos_OnClick" autoSearch="false"
			   onValidate="validatePagamentos"
			   onDataLoadCallBack="retornoGridPagamentos"
			   >
		<adsm:editColumn title="" 				property="pagtoPedagioCc_idPagtoPedagioCc" dataType="integer" field="hidden" width="" />
		<adsm:editColumn title="" 				property="cartaoPedagio_idCartaoPedagio" dataType="integer" field="hidden" width="" />
		<adsm:editColumn title="" 				property="tipoPagamPostoPassagem_blCartaoPedagio" dataType="text" field="hidden" width="" />
		<adsm:gridColumn title="formaPagamento" property="tipoPagamPostoPassagem.dsTipoPagamPostoPassagem" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" 			property="moeda.sgMoeda" width="30" />
			<adsm:gridColumn title="" 				property="moeda.dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 				property="vlPedagio" dataType="currency" width="100" align="right" />
		<adsm:editColumn title="operadora" 		property="operadoraCartaoPedagio_idOperadoraCartaoPedagio" field="combobox" value="valores" description="descricoes" width="160" />
		<adsm:editColumn title="numeroCartao" 	property="cartaoPedagio_nrCartao" mask="################" dataType="integer" maxLength="16" field="textbox" width="160" />
	</adsm:grid>



	<adsm:grid property="postos" idProperty="idPostoPassagemCc" 
			   selectionMode="none" gridHeight="110" showPagging="false" title="postosPassagem" scrollBars="vertical"
			   service="lms.carregamento.gerarControleCargasAction.findPaginatedPostoPassagemCc"
			   rowCountService="lms.carregamento.gerarControleCargasAction.getRowCountPostoPassagemCc" 
			   onRowClick="postos_OnClick" autoSearch="false"
			   onValidate="validatePostos"
			   onDataLoadCallBack="retornoGridPostos"
			   >
		<adsm:editColumn title="" 				property="postoPassagemCc_idPostoPassagemCc" dataType="integer" field="hidden" width="" />
		<adsm:gridColumn title="tipo" 			property="postoPassagem.tpPostoPassagem" isDomain="true" width="120" />
		<adsm:gridColumn title="municipio" 		property="postoPassagem.municipio.nmMunicipio" />
		<adsm:gridColumn title="rodovia" 		property="postoPassagem.rodovia.sgRodovia" width="70" />
		<adsm:gridColumn title="km" 			property="postoPassagem.nrKm" width="50" align="right"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" 			property="moeda.sgMoeda" width="30" />
			<adsm:gridColumn title="" 				property="moeda.dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 				property="vlPagar" dataType="currency" width="100" align="right" />
		<adsm:editColumn title="formaPagamento" property="tipoPagamPostoPassagem_idTipoPagamPostoPassagem" field="combobox" value="valores" description="descricoes" width="160" />
	</adsm:grid>

</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		var idControleCarga = getTabGroup(this.document).getTab("cad").getFormProperty("idControleCarga");
		setElementValue("idControleCarga", idControleCarga);
		setFocusOnFirstFocusableField();
	}
}


/********************** INICIO - GRID PAGAMENTO **********************/
var idSelecionadoPagamento;

function initGridPagamentos() {
	var gridDef = document.getElementById("pagamentos.dataTable").gridDefinition;
	for(var i = 0; i < gridDef.currentRowCount; i++) {
		if ((getElementValue("idControleCarga") == "" || getElementValue("idControleCarga") == "-1") && getElementValue("pagamentos:" + i + ".tipoPagamPostoPassagem_blCartaoPedagio") == "true") {
			setDisabled("pagamentos:" + i + ".operadoraCartaoPedagio_idOperadoraCartaoPedagio", false);
			setDisabled("pagamentos:" + i + ".cartaoPedagio_nrCartao", false);
		}
		else {
			setDisabled("pagamentos:" + i + ".operadoraCartaoPedagio_idOperadoraCartaoPedagio", true);
			setDisabled("pagamentos:" + i + ".cartaoPedagio_nrCartao", true);
		}
	}
}

function pagamentos_OnClick(idPagtoPedagioCc) {
	return false;
}

function validatePagamentos(rowIndex, columnName, objCell) {
	var gridDef = document.getElementById("pagamentos.dataTable").gridDefinition;
	if (columnName == "operadoraCartaoPedagio_idOperadoraCartaoPedagio") {
		if (objCell.value == "") {
			setDisabled("pagamentos:" + rowIndex + ".cartaoPedagio_nrCartao", true);
			setElementValue("pagamentos:" + rowIndex + ".cartaoPedagio_nrCartao",  "");
			setElementValue("pagamentos:" + rowIndex + ".cartaoPedagio_idCartaoPedagio",  "");
		}
		else
			setDisabled("pagamentos:" + rowIndex + ".cartaoPedagio_nrCartao", false);
	}
	else
	if (columnName == "cartaoPedagio_nrCartao") {
		if (objCell.value != "") {
			var idOperadora = getElementValue("pagamentos:" + rowIndex + ".operadoraCartaoPedagio_idOperadoraCartaoPedagio");
			idSelecionadoPagamento = rowIndex;
			findCartaoPedagio(idOperadora, objCell.value);
		}
		else
			setElementValue("pagamentos:" + rowIndex + ".cartaoPedagio_idCartaoPedagio",  "");
	}
	return true;
}

function findCartaoPedagio(idOperadora, nrCartao) {
	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.findCartaoPedagio", "resultadoFindCartaoPedagio", 
		{idOperadoraCartaoPedagio:idOperadora, 
		nrCartao:nrCartao} );
   	xmit({serviceDataObjects:[sdo]});
}

function resultadoFindCartaoPedagio_cb(data, error) {
	if (error != undefined) {
		alert(error);
		setElementValue("pagamentos:" + idSelecionadoPagamento + ".cartaoPedagio_nrCartao", "");
		setFocus(document.getElementById("pagamentos:" + idSelecionadoPagamento + ".cartaoPedagio_nrCartao"));
		return;
	}
	var idCartaoPedagio = getNestedBeanPropertyValue(data, "idCartaoPedagio");
	if (idCartaoPedagio == undefined || idCartaoPedagio == "") {
		alert(lms_00061);
		setElementValue("pagamentos:" + idSelecionadoPagamento + ".cartaoPedagio_nrCartao", "");
		setFocus(document.getElementById("pagamentos:" + idSelecionadoPagamento + ".cartaoPedagio_nrCartao"));
	}
	else
		setElementValue("pagamentos:" + idSelecionadoPagamento + ".cartaoPedagio_idCartaoPedagio", idCartaoPedagio);
}

function retornoGridPagamentos_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	initGridPagamentos();
}

/********************** FIM - GRID PAGAMENTO **********************/






/********************** INICIO - GRID POSTOS **********************/
function postos_OnClick(idPostoPassagemCc) {
	return false;
}

function retornoGridPostos_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	validaCamposGridPostos();

	var filtro = new Array();
	setNestedBeanPropertyValue(filtro, "_meioTransporteByIdTransportado_idMeioTransporte", getElementValue('_meioTransporteByIdTransportado.idMeioTransporte'));
	setNestedBeanPropertyValue(filtro, "_rotaColetaEntrega_idRotaColetaEntrega", getElementValue('_rotaColetaEntrega.idRotaColetaEntrega'));
	setNestedBeanPropertyValue(filtro, "_rotaIdaVolta.idRotaIdaVolta", getElementValue('_rotaIdaVolta.idRotaIdaVolta'));
	setNestedBeanPropertyValue(filtro, "_tpControleCargaValor", getElementValue('_tpControleCargaValor'));
	setNestedBeanPropertyValue(filtro, "idControleCarga", getElementValue("idControleCarga"));
	pagamentosGridDef.executeSearch(filtro);
}


function validatePostos(rowIndex, columnName, objCell) {
	var gridDef = document.getElementById("postos.dataTable").gridDefinition;
	if (columnName == "tipoPagamPostoPassagem_idTipoPagamPostoPassagem") {
		var data = gridDef.gridState.data[rowIndex];
		if (objCell.value != "") {
			if (data.tipoPagamPostoPassagem_idTipoPagamPostoPassagem != objCell.value) {
				atualizaGridPagto();
			}
		}
	}
	return true;
}


function validaCamposGridPostos() {
	var gridDef = document.getElementById("postos.dataTable").gridDefinition;
	for(var i = 0; i < gridDef.currentRowCount; i++) {
		document.getElementById("postos:" + i + ".tipoPagamPostoPassagem_idTipoPagamPostoPassagem").required="true";
		document.getElementById("postos:" + i + ".tipoPagamPostoPassagem_idTipoPagamPostoPassagem").label=labelFormaPagamento;
		if (getElementValue("idControleCarga") != "" && getElementValue("idControleCarga") != "-1")
			setDisabled("postos:" + i + ".tipoPagamPostoPassagem_idTipoPagamPostoPassagem", true);
		else
			setDisabled("postos:" + i + ".tipoPagamPostoPassagem_idTipoPagamPostoPassagem", false);
	}
	return true;
}
/********************** FIM - GRID POSTOS **********************/


function atualizaGridPagto() {
	var tabGroup = getTabGroup(this.document);

    var tabDetCad = tabGroup.getTab("cad");
    var form = tabDetCad.tabOwnerFrame.window.document.forms[0];

    var formPagamentos = document.forms[1];
    var formPostos = document.forms[2];

	var data = editGridFormBean(form, formPostos);
	var pagamentos = getNestedBeanPropertyValue(editGridFormBean(form, formPagamentos), "pagamentos");
	if (pagamentos != undefined) {
		setNestedBeanPropertyValue(data, "pagamentos", pagamentos);
	}
	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.generatePagtoPedagioCcByPostosPassagem", 
		"retornoAtualizaGridPagto", data);
   	xmit({serviceDataObjects:[sdo]});
}


function retornoAtualizaGridPagto_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	postosGridDef.executeLastSearch();
}


pagamentosGridDef.ignoreItemGridRefresh=true;
</script>