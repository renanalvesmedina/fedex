<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="conteudoControleCargasManifestosTitulo" service="lms.entrega.registrarBaixaEntregasAction" 
		onPageLoadCallBack="retornoCarregaPagina" >	
	<adsm:grid idProperty="idManifestoEntregaVolume" property="volumesDoc" selectionMode="check" scrollBars="none"
			   unique="true" autoSearch="false" showPagging="true" gridHeight="210" rows="12"
			   service="lms.entrega.registrarBaixaEntregasAction.findPaginatedVolumes"
			   rowCountService="lms.entrega.registrarBaixaEntregasAction.getRowCountVolumes"
			   onRowClick="volumes_OnClick" onSelectRow="myOnSelectRow" onSelectAll="myOnSelectAll" onDataLoadCallBack="onGridLoad"
	>

		<adsm:gridColumn title="documentoServico" property="tpDoctoServico" isDomain="true" width="30"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" 				property="sgFilialOrigem" width="30" />
			<adsm:gridColumn title="" 				property="nrConhecimento" width="50" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="notaFiscal"		property="nrNotaFiscal" width="30" align="right" />
		<adsm:gridColumn title="sequencia"		property="nrSequencia" width="45" align="right" />
		<adsm:gridColumn title="nrVolumeColeta"	    property="nrVolumeColeta" width="130" />
		<adsm:gridColumn title="nrVolumeEmbarque"		property="nrVolumeEmbarque" width="180" />
	<adsm:buttonBar>
		<adsm:button caption="confirmar" id="botaoConfirmarVolume" onclick="confirmar();" disabled="true" />
		<adsm:button caption="fechar" id="botaoFechar" onclick="voltar();" disabled="false" />
	</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window>

<script>
function retornoCarregaPagina_cb(data, error) {
	//onPageLoad_cb(data, error);
	if (error == undefined) {
		//populateGrid();
	}
}
function confirmar(){
	var nrSequencias = "";
	for(var i = 0; i < volumesDocGridDef.getSelectedIds().ids.length; i++){
		nrSequencias = nrSequencias + volumesDocGridDef.findById(volumesDocGridDef.getSelectedIds().ids[i]).nrSequencia;
		if(i < volumesDocGridDef.getSelectedIds().ids.length -1){
			nrSequencias = nrSequencias + ',';
		}
	}
	
	 var URL = "/entrega/registrarBaixaEntregas.do?cmd=det" +
		"&manifestoEntrega.filial.idFilial=" + getTabGroup(this.document).getTab("pesq").getElementById("manif_idF").value +
		"&manifestoEntrega.filial.sgFilial=" + getTabGroup(this.document).getTab("pesq").getElementById("manif_sgF").value +
		"&manifestoEntrega.idManifestoEntrega=" + getTabGroup(this.document).getTab("pesq").getElementById("manif_id").value +
		"&manifestoEntrega.nrManifestoEntrega=" + getTabGroup(this.document).getTab("pesq").getElementById("manif_nr").value +
		"&manifestoViagem.filial.idFilial=" + getTabGroup(this.document).getTab("pesq").getElementById("manif_idF").value +
		"&manifestoViagem.filial.sgFilial=" + getTabGroup(this.document).getTab("pesq").getElementById("manif_sgF").value +
		"&manifestoViagem.idManifestoViagem=" + getTabGroup(this.document).getTab("pesq").getElementById("manif_id").value +
		"&manifestoViagem.nrManifestoViagem=" + getTabGroup(this.document).getTab("pesq").getElementById("manif_nr").value +
		"&doctoServico.tpDocumentoServico=" + getTabGroup(this.document).getTab("pesq").getElementById("docto_tp").value  +
		"&doctoServico.filialByIdFilialOrigem.idFilial=" + getTabGroup(this.document).getTab("pesq").getElementById("docto_fi_id").value +
		"&doctoServico.filialByIdFilialOrigem.sgFilial=" + getTabGroup(this.document).getTab("pesq").getElementById("docto_fi").value +
		"&controleCarga.filialByIdFilialOrigem.sgFilial=" + getTabGroup(this.document).getTab("pesq").getElementById("cc_sg").value +
		"&controleCarga.nrControleCarga=" + getTabGroup(this.document).getTab("pesq").getElementById("cc_nr").value +
		"&doctoServico.idDoctoServico=" + getTabGroup(this.document).getTab("pesq").getElementById("docto_id").value +
		"&doctoServico.nrDoctoServico=" + getTabGroup(this.document).getTab("pesq").getElementById("docto_nr").value + 
		"&idsManifestoEntregaDocumento=" + volumesDocGridDef.getSelectedIds().ids +
		"&nrSequencias=" + nrSequencias +  
		"&tpManifesto=" + getTabGroup(this.document).getTab("pesq").getElementById("tpManifesto").value + 
		"&ordem=" + getTabGroup(this.document).getTab("pesq").getElementById("ordem").value;
	showModalDialog(URL,window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:250px;');
	populateGrid();
}
function voltar(){
	var tabGroup = getTabGroup(this.document);
	tabGroup._tabsIndex[0].setDisabled(false);
	tabGroup._tabsIndex[1].setDisabled(true);
	tabGroup.selectTab('pesq', {name:'tab_click'});
	return false;
}

function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		populateGrid();
	}
}

function populateGrid() {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "idDoctoServico", getTabGroup(this.document).getTab("pesq").getElementById("docto_id").value);
    setNestedBeanPropertyValue(filtro, "idManifestoEntrega", getTabGroup(this.document).getTab("pesq").getElementById("manif_id").value);
    volumesDocGridDef.executeSearch(filtro, true);
    habilitaConfirmar()
    return false;
}
function onGridLoad_cb(){
	habilitaConfirmar();
}
function myOnSelectRow(rowRef){
	habilitaConfirmar();
}
function myOnSelectAll(checked){
	setDisabled("botaoConfirmarVolume",!checked);
}
function habilitaConfirmar(){
	//alert(volumesDocGridDef.getSelectedIds().ids.length);
	if(volumesDocGridDef.getSelectedIds().ids.length < 1){
		setDisabled("botaoConfirmarVolume",true);
	}
	else{
		setDisabled("botaoConfirmarVolume",false);
	}
}

function volumes_OnClick(id) {	
	return false;
}
</script>