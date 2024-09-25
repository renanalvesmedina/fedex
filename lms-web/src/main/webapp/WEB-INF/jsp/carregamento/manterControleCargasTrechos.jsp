<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.carregamento.manterControleCargasAction" >

	<adsm:form action="/carregamento/gerarControleCargas">
		<adsm:hidden property="idControleCarga" serializable="false" />

		<adsm:textbox dataType="text" label="controleCargas" property="filialByIdFilialOrigem.sgFilial"
					  size="3" labelWidth="18%" width="82%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>

	</adsm:form>
	
	<adsm:grid property="controleTrechos" idProperty="idControleTrecho" 
			   selectionMode="none" gridHeight="305" showPagging="false" scrollBars="both" 
			   service="lms.carregamento.manterControleCargasAction.findPaginatedControleTrecho"
			   rowCountService=""
			   onRowClick="trechos_OnClick" 
			   onDataLoadCallBack="retornoGrid"
			   autoSearch="false"
			   >
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
			<adsm:button id="botaoIncluirFilial" caption="incluirFilial" onclick="exibeInclusaoTrecho();" />
			<adsm:button id="botaoExcluirFilial" caption="excluirFilial" onclick="exibeExclusaoTrecho();" />
		</adsm:buttonBar>
	</adsm:grid>

	<adsm:form action="/carregamento/manterControleCargas">
	</adsm:form>
</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		var tabCad = getTabGroup(this.document).getTab("cad");
		setElementValue("idControleCarga", tabCad.getFormProperty("idControleCarga"));
		setElementValue("filialByIdFilialOrigem.sgFilial", tabCad.getFormProperty("filialByIdFilialOrigem.sgFilial"));
		setElementValue("nrControleCarga", setFormat(document.getElementById("nrControleCarga"), tabCad.getFormProperty("nrControleCarga")) );
		povoaGrid();
	}
}

function povoaGrid() {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "idControleCarga", getElementValue("idControleCarga"));
    controleTrechosGridDef.executeSearch(filtro);
    return false;
}

function trechos_OnClick(id) {
	return false;
}

function exibirCarregamento(id){
    var tabDet = getTabGroup(this.document).getTab("cad");
    var parametros = '&idControleCarga=' + getElementValue("idControleCarga") +
			'&idFilial=' + controleTrechosGridDef.findById(id).filialByIdFilialOrigem.idFilial +
			'&nrControleCarga=' + tabDet.getFormProperty("nrControleCarga") +
			'&sgFilialControleCarga=' + tabDet.getFormProperty("filialByIdFilialOrigem.sgFilial");
	showModalDialog('carregamento/dadosCarregamento.do?cmd=main' + parametros,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:720px;dialogHeight:490px;');
}

function exibirDescarga(id){
    var tabDet = getTabGroup(this.document).getTab("cad");
    var parametros = '&idControleCarga=' + getElementValue("idControleCarga") + 
    		'&idFilial=' + controleTrechosGridDef.findById(id).filialByIdFilialDestino.idFilial +
			'&nrControleCarga=' + tabDet.getFormProperty("nrControleCarga") +
			'&sgFilialControleCarga=' + tabDet.getFormProperty("filialByIdFilialOrigem.sgFilial");
	showModalDialog('carregamento/dadosDescarga.do?cmd=main' + parametros,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:720px;dialogHeight:490px;');
}

function exibeInclusaoTrecho() {
	showModalDialog('carregamento/manterControleCargas.do?cmd=incluirTrecho',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	povoaGrid();
}

function exibeExclusaoTrecho() {
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.validateExistenciaInsercaoManualTrecho", "retorno_validate",
		{idControleCarga:getElementValue("idControleCarga")});
   	xmit({serviceDataObjects:[sdo]});
}

function retorno_validate_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	showModalDialog('carregamento/manterControleCargas.do?cmd=excluirTrecho',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	povoaGrid();
}



function retornoGrid_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	var tabDet = getTabGroup(this.document).getTab("cad");
    var blPermiteAlteracao = tabDet.getFormProperty("blPermiteAlterar");
    if (blPermiteAlteracao == "true") {
    	setDisabled("botaoIncluirFilial", false);
    	setDisabled("botaoExcluirFilial", false);
    }
    else {
    	setDisabled("botaoIncluirFilial", true);
    	setDisabled("botaoExcluirFilial", true);
	}
}
</script>