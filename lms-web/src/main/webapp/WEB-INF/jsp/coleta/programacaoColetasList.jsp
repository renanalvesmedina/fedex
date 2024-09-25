<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="programacaoColetas" service="lms.coleta.programacaoColetasAction" >
	<div style="height:271px">
	<adsm:grid property="pedidosColeta" idProperty="idPedidoColeta" selectionMode="none" gridHeight="224" 
			   scrollBars="both" unique="false" title="coletasNaoProgramadas"
			   autoSearch="false" showPagging="false"
			   onRowClick="povoaGridServicosAdicionais"
			   onRowDblClick="pedidosColeta_OnDblClick"
			   service="lms.coleta.programacaoColetasAction.findPaginatedColetasNaoProgramadas" 
			   rowCountService="" minimumHeight="true"
			   onPopulateRow="povoaLinhas" onDataLoadCallBack="retornoGridPedidoColeta">
		<adsm:gridColumnGroup separatorType="PEDIDO_COLETA">
			<adsm:gridColumn title="coleta" 			property="sgFilial" width="35" />
			<adsm:gridColumn title="" 					property="nrColeta" width="60" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="tipo" 				property="tpModoPedidoColeta" width="140" />
		<adsm:gridColumn title="rota" 				property="dsRota" width="140" />
		<adsm:gridColumn title="dadosColeta" 		property="dadosColeta" image="/images/popup.gif" openPopup="true" link="javascript:exibirDetalheColeta" width="100" align="center" />
		<adsm:gridColumn title="cliente" 			property="nrIdentificacaoFormatado" width="120" align="right" />
		<adsm:gridColumn title="" 					property="nmPessoa" width="250" />
		<adsm:gridColumn title="alterarValores"		property="alterarValores" image="/images/popup.gif" openPopup="true" link="javascript:alterarValores" width="100" align="center" />
		<adsm:gridColumn title="horarioSaidaViagem" property="horarioSaidaViagem" image="/images/popup.gif" openPopup="true" link="javascript:exibirHorarioSaidaViagem" width="150" align="center" />
		
		<adsm:buttonBar> 
			<adsm:button caption="atualizar" id="botaoAtualiza" onclick="povoaGrid();" disabled="false" />
			<adsm:button caption="programacaoVeiculos" id="botaoProgramacaoVeiculos" action="/coleta/programacaoColetasVeiculos" cmd="main" disabled="false" />
		</adsm:buttonBar>

	</adsm:grid>
	</div>

	<adsm:grid property="servicosAdicionais" idProperty="idServicoAdicional" selectionMode="none" gridHeight="55" 
			   scrollBars="vertical" unique="false" title="servicosAdicionais" 
			   autoSearch="false" showPagging="false" defaultOrder="servicoAdicional_.dsServicoAdicional"
			   onRowClick="servicosAdicionais_OnClick" 
			   service="lms.coleta.programacaoColetasAction.findPaginatedServicosAdicionais"
			   rowCountService="" >
		<adsm:gridColumn property="servicoAdicional.dsServicoAdicional" width="100%" title="descricao" />
	</adsm:grid>
		
	<adsm:form action="/coleta/programacaoColetas" idProperty="idPedidoColeta" >
	</adsm:form>
	
</adsm:window>

<script>
function initWindow(eventObj) {
	povoaGrid();
}

function povoaGrid() {
	pedidosColetaGridDef.executeSearch();
}

function povoaGridServicosAdicionais(idPedidoColeta) {
      var filtro = new Array();
      setNestedBeanPropertyValue(filtro, "pedidoColeta.idPedidoColeta", idPedidoColeta);
      servicosAdicionaisGridDef.executeSearch(filtro);
      return false;
}

function pedidosColeta_OnDblClick(idPedidoColeta) {
	var parametros = '&idPedidoColetaMaster=' + idPedidoColeta;
	parent.parent.redirectPage("coleta/programacaoColetasVeiculos.do?cmd=main" + parametros);
	return false;
}

function alterarValores(id){
	showModalDialog('coleta/alteracaoValoresColeta.do?cmd=main&idPedidoColeta='+id ,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:770px;dialogHeight:200px;');
	povoaGrid();
}


function exibirHorarioSaidaViagem(id){
	showModalDialog('coleta/rotasDestinosColeta.do?cmd=main&idPedidoColeta='+id ,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:600px;dialogHeight:240px;');
	povoaGrid();
}


function exibirDetalheColeta(id){
	showModalDialog('coleta/consultarColetasDadosColeta.do?cmd=main&idPedidoColeta='+id ,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	povoaGrid();
}


function servicosAdicionais_OnClick(idServicoAdicional) {
	return false;
}

function povoaLinhas(tr, data) {
	if (data["blPriorizar"] == "true")
		tr.style.backgroundColor = "#8FBFD6";
	if (data["blPerigoso"] == "true")
		tr.style.backgroundColor = "#D6BF8F";
}

function retornoGridPedidoColeta_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
}
</script>