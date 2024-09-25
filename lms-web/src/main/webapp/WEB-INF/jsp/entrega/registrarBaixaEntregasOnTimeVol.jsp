<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="conteudoControleCargasManifestosTitulo" service="lms.entrega.registrarBaixaEntregasOnTimeAction" 
		onPageLoadCallBack="retornoCarregaPagina" >
  	<adsm:form action="/entrega/registrarBaixaEntregasOnTime">

		<adsm:combobox property="doctoServico.tpDocumentoServico" label="documentoServico" labelWidth="19%" optionLabelProperty="description"
					   service="lms.entrega.registrarBaixaEntregasOnTimeAction.findTpDoctoServico" optionProperty="value" width="81%"
					   onchange="" disabled="true">
			<adsm:lookup dataType="text" property="doctoServico.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" action="" size="3" maxLength="3" picker="false" onchange="" disabled="true">
			</adsm:lookup>
			
			<adsm:lookup dataType="integer" property="doctoServico" idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" 
						 service="" action="" size="12" maxLength="8" mask="00000000" serializable="true" disabled="true"/>
			<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
			<adsm:hidden property="idDoctoServico"/>
		</adsm:combobox>
		
		<adsm:textbox dataType="text" property="nrNotaFiscalField" size="10" width="20%" label="notaFiscal" disabled="true" labelWidth="19%" serializable="false"/>
		<adsm:textbox dataType="text" property="nrSequenciaField" size="5" width="10%" label="sequencia" disabled="true" labelWidth="19%" serializable="false"/>
		<adsm:hidden property="idManifestoEntregaVolume"/>
	<adsm:lookup service="lms.entrega.registrarBaixaEntregasOnTimeAction.findLookupOcorrenciaEntrega" dataType="integer" property="ocorrenciaEntrega" 
	 		criteriaProperty="cdOcorrenciaEntrega" idProperty="idOcorrenciaEntrega" label="ocorrencia" labelWidth="19%" width="81%" required="true"
	 		exactMatch="true" minLengthForAutoPopUpSearch="3" size="3" maxLength="3" action="/entrega/manterOcorrenciasEntrega" onDataLoadCallBack="dataLoadOcorrenciaEntrega">
			<adsm:propertyMapping modelProperty="dsOcorrenciaEntrega" relatedProperty="ocorrenciaEntrega.dsOcorrenciaEntrega"/>
								
			<adsm:textbox dataType="text" size="70" maxLength="60" property="ocorrenciaEntrega.dsOcorrenciaEntrega" disabled="true" serializable="false"/>
	</adsm:lookup>
	<adsm:buttonBar freeLayout="true">
		<adsm:button caption="salvar" id="botaoSalvar" onclick="salvaOcorrencia()" disabled="false"/>
	</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idVolumeNotaFiscal" property="volumesDoc" selectionMode="none" scrollBars="none"
			   unique="true" autoSearch="false" showPagging="true" gridHeight="210" rows="12"
			   service="lms.entrega.registrarBaixaEntregasOnTimeAction.findPaginatedVolumes"
			   rowCountService="lms.entrega.registrarBaixaEntregasOnTimeAction.getRowCountVolumes"
			   onRowClick="volumes_OnClick"
	>

		<adsm:gridColumn title="documentoServico" property="tpDoctoServico" isDomain="true" width="30"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" 				property="sgFilialOrigem" width="30" />
			<adsm:gridColumn title="" 				property="nrConhecimento" width="50" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="notaFiscal"		property="nrNotaFiscal" width="80" align="right" />
		<adsm:gridColumn title="sequencia"		property="nrSequencia" width="70" align="right" />
		<adsm:gridColumn title="ocorrencia"	    property="cdOcorEnt" width="40" />
		<adsm:gridColumn title=""				property="dsOcorEnt" width="180" />
	<adsm:buttonBar>
		<adsm:button caption="confirmar" id="botaoFechar" onclick="confirmar();" disabled="false" />
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
	var entregue = false;
	var naoEntregue = false;
	var cdOcorrenciaEntrega = "001";
	var data;
	
	for (i=0; i<volumesDocGridDef.gridState.data.length; i++) {
		if (volumesDocGridDef.gridState.data[i].tpOcorrencia.value=='E'){
			entregue = true;
		}else{
			naoEntregue = true;
			cdOcorrenciaEntrega = volumesDocGridDef.gridState.data[i].cdOcorEnt;
		}
	}

	if(entregue && naoEntregue){
		cdOcorrenciaEntrega = "102";
	}
	//else if(!entregue && naoEntregue)
	//{
		
	//}
		

	
	 var URL = "/entrega/registrarBaixaEntregasOnTime.do?cmd=det" +
		"&manifestoEntrega.filial.idFilial=" + getTabGroup(this.document).getTab("pesq").getElementById("manif_idF").value +
		"&manifestoEntrega.filial.sgFilial=" + getTabGroup(this.document).getTab("pesq").getElementById("manif_sgF").value +
		"&manifestoEntrega.idManifestoEntrega=" + getTabGroup(this.document).getTab("pesq").getElementById("manif_id").value +
		"&manifestoEntrega.nrManifestoEntrega=" + getTabGroup(this.document).getTab("pesq").getElementById("manif_nr").value +
		"&doctoServico.tpDocumentoServico=" + getTabGroup(this.document).getTab("pesq").getElementById("docto_tp").value  +
		"&doctoServico.filialByIdFilialOrigem.idFilial=" + getTabGroup(this.document).getTab("pesq").getElementById("docto_fi_id").value +
		"&doctoServico.filialByIdFilialOrigem.sgFilial=" + getTabGroup(this.document).getTab("pesq").getElementById("docto_fi").value +
		"&controleCarga.filialByIdFilialOrigem.sgFilial=" + getTabGroup(this.document).getTab("pesq").getElementById("cc_sg").value +
		"&controleCarga.nrControleCarga=" + getTabGroup(this.document).getTab("pesq").getElementById("cc_nr").value +
		"&doctoServico.idDoctoServico=" + getTabGroup(this.document).getTab("pesq").getElementById("docto_id").value +
		"&doctoServico.nrDoctoServico=" + getTabGroup(this.document).getTab("pesq").getElementById("docto_nr").value + 
		"&ocorrenciaEntrega.cdOcorrenciaEntrega=" + cdOcorrenciaEntrega + 
		"&ordem=" + getTabGroup(this.document).getTab("pesq").getElementById("ordem").value;
	showModalDialog(URL,window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:250px;');
	voltar();
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
    return false;
}

function volumes_OnClick(id) {
	var data = volumesDocGridDef.findById(id);

	setElementValue("doctoServico.tpDocumentoServico", getNestedBeanPropertyValue(data,"tpDoctoServico.value"));
	setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", getNestedBeanPropertyValue(data,"sgFilialOrigem"));
	setElementValue("doctoServico.nrDoctoServico", getNestedBeanPropertyValue(data,"nrConhecimento"));
	
	setElementValue("nrNotaFiscalField",getNestedBeanPropertyValue(data,"nrNotaFiscal"));
	setElementValue("nrSequenciaField",getNestedBeanPropertyValue(data,"nrSequencia"));
	setElementValue("ocorrenciaEntrega.cdOcorrenciaEntrega",getNestedBeanPropertyValue(data,"cdOcorEnt"));
	setElementValue("ocorrenciaEntrega.dsOcorrenciaEntrega",getNestedBeanPropertyValue(data,"dsOcorEnt"));
	setElementValue("idManifestoEntregaVolume",getNestedBeanPropertyValue(data,"idManifestoEntregaVolume"));
	setDisabled("botaoSalvar",false);
	
	return false;
}
function salvaOcorrencia(){
	var idManifestoEntregaVolume = getElementValue("idManifestoEntregaVolume");
	var idOcorrenciaEntrega = getElementValue("ocorrenciaEntrega.idOcorrenciaEntrega");
	var sdo = createServiceDataObject("lms.entrega.registrarBaixaEntregasOnTimeAction.storeOcorrenciaOnManifestoEntregaVolume",
			"onSaveOcorrencia",{idManifestoEntregaVolume:idManifestoEntregaVolume,
								idOcorrenciaEntrega:idOcorrenciaEntrega});
	xmit({serviceDataObjects:[sdo]});
}
function onSaveOcorrencia_cb(data,error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	populateGrid();
}
function dataLoadOcorrenciaEntrega_cb(data) {
	ocorrenciaEntrega_cdOcorrenciaEntrega_exactMatch_cb(data);
	setFocus("ocorrenciaEntrega.cdOcorrenciaEntrega");
	document.getElementById("ocorrenciaEntrega.idOcorrenciaEntrega").callBack = "ocorrenciaEntrega.cdOcorrenciaEntrega_exactMatch";
}
</script>