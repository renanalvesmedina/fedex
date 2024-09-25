<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.carregamento.consultarControleCargasAction" >
	
	<adsm:grid property="controleTrechos" idProperty="idControleTrecho" 
			   selectionMode="none" gridHeight="355" showPagging="false" scrollBars="both" autoSearch="false"
			   service="lms.carregamento.consultarControleCargasAction.findPaginatedControleTrecho"
			   rowCountService=""
			   onRowClick="trechos_OnClick" >
		<adsm:gridColumn title="origem" 				property="filialByIdFilialOrigem.sgFilial" width="70" />
		<adsm:gridColumn title="carregamento" 			property="carregamento" image="/images/popup.gif" openPopup="true" link="javascript:exibirCarregamento" width="110" align="center" linkIdProperty="filialByIdFilialOrigem.idFilial" />
		<adsm:gridColumn title="destino" 				property="filialByIdFilialDestino.sgFilial" width="70" align="left" />
		<adsm:gridColumn title="descarga" 				property="descarga" image="/images/popup.gif" openPopup="true" link="javascript:exibirDescarga" width="80" align="center" linkIdProperty="filialByIdFilialDestino.idFilial" />
		<adsm:gridColumn title="dataHoraSaidaPrevista"	property="dhPrevisaoSaida" dataType="JTDateTimeZone" width="120" align="center"/>
		<adsm:gridColumn title="distancia" 				property="nrDistancia" width="100" align="right" unit="km" />
		<adsm:gridColumn title="tempoViagem" 			property="hrTempoViagem" dataType="JTTime" width="90" align="center"/>
		<adsm:gridColumn title="tempoOperacaoPrevisto" 	property="hrTempoOperacao" dataType="JTTime" width="125" align="center"/>
		<adsm:gridColumn title="dataHoraSaida" 			property="dhSaida" dataType="JTDateTimeZone" width="120" align="center"/>
		<adsm:gridColumn title="dataHoraChegada" 		property="dhChegada" dataType="JTDateTimeZone" width="120" align="center"/>
		<adsm:gridColumn title="tempoOperacaoRealizado"	property="hrTempoOperacaoRealizado" dataType="JTTime" width="125" align="center"/>

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
    controleTrechosGridDef.executeSearch(filtro);
    return false;
}

function trechos_OnClick(id) {
	return false;
}

function exibirCarregamento(id){
	var tabGroup = getTabGroup(this.document);
    var tabDet = tabGroup.getTab("cad");
    var parametros = '&idControleCarga=' + tabDet.getFormProperty("idControleCarga") +
    	 	'&idFilial=' + controleTrechosGridDef.findById(id).filialByIdFilialOrigem.idFilial +
		 	'&nrControleCarga=' + tabDet.getFormProperty("nrControleCarga") +
			'&sgFilialControleCarga=' + tabDet.getFormProperty("filialByIdFilialOrigem.sgFilial");
	showModalDialog('carregamento/dadosCarregamento.do?cmd=main' + parametros,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:720px;dialogHeight:490px;');
}

function exibirDescarga(id){
	var tabGroup = getTabGroup(this.document);
    var tabDet = tabGroup.getTab("cad");
    var parametros = '&idControleCarga=' + tabDet.getFormProperty("idControleCarga") +
			'&idFilial=' + controleTrechosGridDef.findById(id).filialByIdFilialDestino.idFilial +
			'&nrControleCarga=' + tabDet.getFormProperty("nrControleCarga") + 
			'&sgFilialControleCarga=' + tabDet.getFormProperty("filialByIdFilialOrigem.sgFilial");
	showModalDialog('carregamento/dadosDescarga.do?cmd=main' + parametros,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:720px;dialogHeight:490px;');
}

function getIdControleCarga() {
    var tabDet = getTabGroup(this.document).getTab("cad");
    return tabDet.getFormProperty("idControleCarga");
}
</script>