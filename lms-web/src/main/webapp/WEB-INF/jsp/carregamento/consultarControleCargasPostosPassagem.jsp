<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.consultarControleCargasAction" >

	<adsm:grid property="pagamentos" idProperty="idPagtoPedagioCc" 
			   selectionMode="none" gridHeight="120" showPagging="false" title="pagamento" scrollBars="vertical"
			   service="lms.carregamento.consultarControleCargasAction.findPaginatedPagtoPedagioCc" 
			   rowCountService=""
			   onRowClick="pagamentos_OnClick" autoSearch="false" >
		<adsm:gridColumn title="formaPagamento" property="tipoPagamPostoPassagem.dsTipoPagamPostoPassagem" width="220" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" 			property="moeda.sgMoeda" width="30" />
			<adsm:gridColumn title="" 				property="moeda.dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 				property="vlPedagio" dataType="currency" width="100" align="right" />
		<adsm:gridColumn title="operadora" 		property="operadoraCartaoPedagio.pessoa.nmPessoa" width="220" />
		<adsm:gridColumn title="numeroCartao" 	property="cartaoPedagio.nrCartao" dataType="text" width="" align="right" />
	</adsm:grid>



	<adsm:grid property="postos" idProperty="idPostoPassagemCc" 
			   selectionMode="none" gridHeight="140" showPagging="false" title="postosPassagem" scrollBars="vertical"
			   service="lms.carregamento.consultarControleCargasAction.findPaginatedPostoPassagemCc"
			   rowCountService="" 
			   onRowClick="postos_OnClick" autoSearch="false" >
		<adsm:gridColumn title="tipo" 			property="postoPassagem.tpPostoPassagem" isDomain="true" width="120" />
		<adsm:gridColumn title="municipio" 		property="postoPassagem.municipio.nmMunicipio" width="170"/>
		<adsm:gridColumn title="rodovia" 		property="postoPassagem.rodovia.sgRodovia" width="70" />
		<adsm:gridColumn title="km" 			property="postoPassagem.nrKm" width="50" align="right"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" 			property="moeda.sgMoeda" width="30" />
			<adsm:gridColumn title="" 				property="moeda.dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 				property="vlPagar" dataType="currency" width="100" align="right" />
		<adsm:gridColumn title="formaPagamento" property="tipoPagamPostoPassagem.dsTipoPagamPostoPassagem" width="" />
	</adsm:grid>

	<adsm:form action="/carregamento/consultarControleCargas">
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:form>

</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
	   	var tabDet = getTabGroup(this.document).getTab("cad");
	    var idControleCarga = tabDet.getFormProperty("idControleCarga");
	    var filtro = new Array();
		setNestedBeanPropertyValue(filtro, "idControleCarga", idControleCarga);
		pagamentosGridDef.executeSearch(filtro);
		postosGridDef.executeSearch(filtro);
	}
}

function pagamentos_OnClick(idPagtoPedagioCc) {
	return false;
}

function postos_OnClick(idPostoPassagemCc) {
	return false;
}

</script>