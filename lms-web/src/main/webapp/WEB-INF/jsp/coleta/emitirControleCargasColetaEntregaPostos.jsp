<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.emitirControleCargasColetaEntregaAction" >

	<adsm:form action="/coleta/emitirControleCargasColetaEntrega" >
		<adsm:masterLink idProperty="idControleCarga" showSaveAll="true">
			<adsm:masterLinkItem property="controleCargaConcatenado" label="controleCargas" itemWidth="100" />
			<adsm:hidden property="_meioTransporteByIdTransportado.idMeioTransporte" />
			<adsm:hidden property="_meioTransporteByIdSemiRebocado.idMeioTransporte" />
			<adsm:hidden property="_rotaColetaEntrega.idRotaColetaEntrega" />
		</adsm:masterLink>
		
		<adsm:hidden property="blEmissao" serializable="false" />
		
		<script>
			var lms_00061 = '<adsm:label key="LMS-00061"/>';
			var labelFormaPagamento = '<adsm:label key="formaPagamento"/>';
			var labelOperadora = '<adsm:label key="operadora"/>';
			var labelNumeroCartao = '<adsm:label key="numeroCartao"/>';
		</script>
	</adsm:form>


	<adsm:grid property="pagamentos" idProperty="idPagtoPedagioCc" 
			   selectionMode="none" gridHeight="120" showPagging="false" title="pagamento" scrollBars="vertical"
			   service="lms.coleta.emitirControleCargasColetaEntregaAction.findPaginatedPagtoPedagioCc"
			   rowCountService="lms.coleta.emitirControleCargasColetaEntregaAction.getRowCountPagtoPedagioCc"
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
			   service="lms.coleta.emitirControleCargasColetaEntregaAction.findPaginatedPostoPassagemCc"
			   rowCountService="lms.coleta.emitirControleCargasColetaEntregaAction.getRowCountPostoPassagemCc" 
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
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("emissao");
		setElementValue("blEmissao", tabDet.getFormProperty("blEmissao"));
		setFocusOnFirstFocusableField();
	}
}



/********************** INICIO - GRID PAGAMENTO **********************/
var idSelecionadoPagamento;

function initGridPagamentos() {
	var gridDef = document.getElementById("pagamentos.dataTable").gridDefinition;
	for(var i = 0; i < gridDef.currentRowCount; i++) {
		if (getElementValue("blEmissao") == "true" && getElementValue("pagamentos:" + i + ".tipoPagamPostoPassagem_blCartaoPedagio") == "true") {
			setDisabled("pagamentos:" + i + ".operadoraCartaoPedagio_idOperadoraCartaoPedagio", false);
			setDisabled("pagamentos:" + i + ".cartaoPedagio_nrCartao", false);
			document.getElementById("pagamentos:" + i + ".operadoraCartaoPedagio_idOperadoraCartaoPedagio").required = "true";
			document.getElementById("pagamentos:" + i + ".operadoraCartaoPedagio_idOperadoraCartaoPedagio").label = labelOperadora;
			document.getElementById("pagamentos:" + i + ".cartaoPedagio_nrCartao").required = "true";
			document.getElementById("pagamentos:" + i + ".cartaoPedagio_nrCartao").label = labelNumeroCartao;
		}
		else {
			setDisabled("pagamentos:" + i + ".operadoraCartaoPedagio_idOperadoraCartaoPedagio", true);
			setDisabled("pagamentos:" + i + ".cartaoPedagio_nrCartao", true);
			document.getElementById("pagamentos:" + i + ".operadoraCartaoPedagio_idOperadoraCartaoPedagio").required = "false";
			document.getElementById("pagamentos:" + i + ".cartaoPedagio_nrCartao").required = "false";
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
	var sdo = createServiceDataObject("lms.coleta.emitirControleCargasColetaEntregaAction.findCartaoPedagio", "resultadoFindCartaoPedagio", 
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

	var tabGroup = getTabGroup(this.document);
    var tabDet = tabGroup.getTab("emissao");
    var idControleCarga = tabDet.getFormProperty("idControleCarga");

	var filtro = new Array();
	setNestedBeanPropertyValue(filtro, "_meioTransporteByIdTransportado_idMeioTransporte", getElementValue('_meioTransporteByIdTransportado.idMeioTransporte'));
	setNestedBeanPropertyValue(filtro, "_rotaColetaEntrega_idRotaColetaEntrega", getElementValue('_rotaColetaEntrega.idRotaColetaEntrega'));
	setNestedBeanPropertyValue(filtro, "idControleCarga", idControleCarga);
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
		if (getElementValue("blEmissao") == "false") {
			setDisabled("postos:" + i + ".tipoPagamPostoPassagem_idTipoPagamPostoPassagem", true);
		}
	}
	return true;
}

/********************** FIM - GRID POSTOS **********************/

function atualizaGridPagto() {
	var tabGroup = getTabGroup(this.document);

    var tabDetEmissao = tabGroup.getTab("emissao");
    var form = tabDetEmissao.tabOwnerFrame.window.document.forms[0];

    var formPagamentos = document.forms[1];
    var formPostos = document.forms[2];

	var data = editGridFormBean(form, formPostos);
	var pagamentos = getNestedBeanPropertyValue(editGridFormBean(form, formPagamentos), "pagamentos");
	if (pagamentos != undefined) {
		setNestedBeanPropertyValue(data, "pagamentos", pagamentos);
	}

	var sdo = createServiceDataObject("lms.coleta.emitirControleCargasColetaEntregaAction.generatePagtoPedagioCcByPostosPassagem", 
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


function limpaGrids() {
	postosGridDef.resetGrid();
	pagamentosGridDef.resetGrid();
}

pagamentosGridDef.ignoreItemGridRefresh=true;
</script>