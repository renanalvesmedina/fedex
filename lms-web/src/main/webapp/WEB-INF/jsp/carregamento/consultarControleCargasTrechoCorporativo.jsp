<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.carregamento.consultarControleCargasAction" >
	
	<adsm:grid property="trechoCorporativo" idProperty="idTrechoCorportativo" 
			   selectionMode="none" gridHeight="365" showPagging="false" scrollBars="vertical" autoSearch="false"
			   service="lms.carregamento.consultarControleCargasAction.findPaginatedTrechoCorporativo"
			   rowCountService=""
			   onRowClick="trechosCorporativo_OnClick" >
		<adsm:gridColumn title="origem" 		property="sgFilialOrigem" width="10%" />
		<adsm:gridColumn title="destino" 		property="sgFilialDestino" width="10%" />
		<adsm:gridColumn title="saida" 			property="hrSaida" dataType="JTTime" width="10%" align="center"/>
		<adsm:gridColumn title="previsao"		property="hrPrevisao" dataType="JTTime" width="13%" align="center"/>
		<adsm:gridColumn title="freteAte10T" 	property="vlFaixa1" dataType="currency" width="19%" align="right" />
		<adsm:gridColumn title="freteAte15T" 	property="vlFaixa2" dataType="currency" width="19%" align="right"/>
		<adsm:gridColumn title="freteAcima" 	property="vlFaixa3" dataType="currency" width="19%" align="right"/>
		<adsm:buttonBar> 
		</adsm:buttonBar>
	</adsm:grid>

	<adsm:form action="/carregamento/consultarControleCargas">
	</adsm:form>
</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		povoaGrid();
	}
}

function povoaGrid() {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "idControleCarga", getIdControleCarga());
    trechoCorporativoGridDef.executeSearch(filtro);
    return false;
}

function trechosCorporativo_OnClick(id) {
	return false;
}

function getIdControleCarga() {
    var tabDet = getTabGroup(this.document).getTab("cad");
    return tabDet.getFormProperty("idControleCarga");
}
</script>