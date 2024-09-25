<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.consultarControleCargasAction" >
	<adsm:form action="/carregamento/consultarControleCargas">
	</adsm:form>

	<adsm:grid property="pontos" idProperty="id" 
		   selectionMode="none" showPagging="false" scrollBars="vertical" gridHeight="345" autoSearch="false"
		   service="lms.carregamento.consultarControleCargasAction.findPaginatedPontosParada"
		   rowCountService=""
		   onRowClick="pontos_OnClick"
		   >
		<adsm:gridColumn title="trecho" 	property="trecho" width="10%" />
		<adsm:gridColumn title="rodovia" 	property="rodovia" width="13%" />
		<adsm:gridColumn title="km" 		property="nrKm" width="7%" align="right" />
		<adsm:gridColumn title="municipio" 	property="nmMunicipio" width="20%" />
		<adsm:gridColumn title="uf" 		property="sgUf" width="5%" align="left" />
		<adsm:gridColumn title="motivos" 	property="motivos" image="/images/popup.gif" openPopup="true" link="/carregamento/gerarControleCargas.do?cmd=motivosParada" popupDimension="400,240" width="10%" align="center" linkIdProperty="idPontoParadaTrecho"/>
		<adsm:gridColumn title="tempoParada" property="hrTempoParada" width="15%" align="center"/>
		<adsm:gridColumn title="latitude" 	property="nrLatitude" dataType="decimal" width="10%" align="right"/>
		<adsm:gridColumn title="longitude" 	property="nrLongitude" dataType="decimal" width="10%" align="right"/>
		<adsm:buttonBar/> 
	</adsm:grid>

</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		var tabGroup = getTabGroup(this.document);
	    var tabDet = tabGroup.getTab("cad");
	    var idRotaIdaVolta = tabDet.getFormProperty("rotaIdaVolta.idRotaIdaVolta");
		povoaGrid(idRotaIdaVolta);
	}
}

function povoaGrid(idRotaIdaVolta) {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "rotaIdaVolta.idRotaIdaVolta", idRotaIdaVolta);
    pontosGridDef.executeSearch(filtro);
    return false;
}

function pontos_OnClick(id) {
	return false;
}
</script>