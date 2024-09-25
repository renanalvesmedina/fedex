<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function loadData() {
		var url = new URL(parent.location.href);
		var idOcorrenciaNaoConformidadeLocMerc = url.parameters["idOcorrenciaNaoConformidadeLocMerc"];
	
		var data = new Array();
		setNestedBeanPropertyValue(data, "idOcorrenciaNaoConformidade", idOcorrenciaNaoConformidadeLocMerc);
	
		if (idOcorrenciaNaoConformidadeLocMerc!=null && idOcorrenciaNaoConformidadeLocMerc!="") {
			var sdo = createServiceDataObject("lms.rnc.manterOcorrenciasNaoConformidadeAction.findById", "loadData", data);
			xmit({serviceDataObjects:[sdo]});			
		} else {
			var url = new URL(parent.location.href);
		var idProcessoWorkflow = url.parameters["idProcessoWorkflow"];
		
		if (idProcessoWorkflow!=null) {
			var data = new Object();				
			data.idProcessoWorkflow = idProcessoWorkflow;
		    var sdo = createServiceDataObject("lms.rnc.manterOcorrenciasNaoConformidadeAction.findByIdProcessoWorkflow", "loadData", data);
		    xmit({serviceDataObjects:[sdo]});
		} else {
			onPageLoad();
		}
	}
	}
	
	function loadData_cb(data, error) {
		setElementValue("naoConformidade.filial.sgFilial", data.naoConformidade.filial.sgFilial);
		setElementValue("naoConformidade.nrNaoConformidade", setFormat(document.getElementById("naoConformidade.nrNaoConformidade"), data.naoConformidade.nrNaoConformidade));

		if (data.naoConformidade.doctoServico!=undefined) {
			setElementValue("naoConformidade.doctoServico.idDoctoServico", data.naoConformidade.doctoServico.idDoctoServico);
			setElementValue("naoConformidade.doctoServico.tpDocumentoServico.description", data.naoConformidade.doctoServico.tpDocumentoServico.description);
			setElementValue("naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial", data.naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial);
			setElementValue("naoConformidade.doctoServico.nrDoctoServico", setFormat(document.getElementById("naoConformidade.doctoServico.nrDoctoServico"), data.naoConformidade.doctoServico.nrDoctoServico));
		}
		setElementValue("naoConformidade.idNaoConformidade", data.naoConformidade.idNaoConformidade);
		setElementValue("idOcorrenciaNaoConformidade", data.idOcorrenciaNaoConformidade);
		
		onPageLoad();
	}

</script>

<adsm:window service="lms.rnc.manterOcorrenciasNaoConformidadeAction" onPageLoad="loadData" onPageLoadCallBack="retornoPageLoad">
	<adsm:form action="/rnc/manterOcorrenciasNaoConformidade" idProperty="idOcorrenciaNaoConformidade" >
		
		<adsm:hidden property="idOcorrenciaNaoConformidadeLocMerc"/>	

		<adsm:hidden property="blPermiteAlterar" serializable="false" />

		<adsm:textbox dataType="text" property="naoConformidade.filial.sgFilial" size="3" maxLength="3" label="naoConformidade" labelWidth="23%" width="77%" disabled="true" serializable="false" >
			<adsm:textbox dataType="integer" property="naoConformidade.nrNaoConformidade" mask="00000000" size="9" maxLength="8" disabled="true" serializable="false" />
		</adsm:textbox>
		<adsm:hidden property="naoConformidade.idNaoConformidade" serializable="true" />
		<adsm:hidden property="naoConformidade.tpStatusNaoConformidade" serializable="true" />


		<adsm:textbox dataType="text" property="naoConformidade.doctoServico.tpDocumentoServico.description" 
					  label="documentoServico" size="10" labelWidth="23%" width="77%" disabled="true" serializable="false" >

			<adsm:textbox dataType="text" property="naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial"
						  size="3" maxLength="3" serializable="false" disabled="true"/>

			<adsm:textbox dataType="integer" property="naoConformidade.doctoServico.nrDoctoServico"
						  size="11" maxLength="8" serializable="false" disabled="true" mask="00000000"/>
		</adsm:textbox>
		<adsm:hidden property="naoConformidade.doctoServico.idDoctoServico" serializable="false" />
		<adsm:hidden property="naoConformidade.doctoServico.moeda.idMoeda" serializable="false" />

		<adsm:buttonBar freeLayout="true">
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="ocorrencias" idProperty="idOcorrenciaNaoConformidade" 
			   selectionMode="none" autoSearch="false" rows="12"
			   defaultOrder="nrOcorrenciaNc:asc"
			   onRowClick="ocorrenciasNc_OnClick"
			   unique="true" gridHeight="240" scrollBars="horizontal" >

		<adsm:gridColumn title="ocorrencia" 			property="nrOcorrenciaNc" width="70" align="right" />
		<adsm:gridColumn title="motivoNaoConformidade" 	property="motivoAberturaNc.dsMotivoAbertura" width="170" />
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filialResponsavel" 		property="filialByIdFilialResponsavel.sgFilial" width="50" />
			<adsm:gridColumn title="" 						property="filialByIdFilialResponsavel.pessoa.nmFantasia" width="160" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="descricao" 				property="dsOcorrenciaNc" width="280" />
		<adsm:gridColumnGroup separatorType="RNC">	   
			<adsm:gridColumn title="rncLegado" 			property="filialByIdFilialLegado.sgFilial" width="30" />
			<adsm:gridColumn title="" 				 	property="nrRncLegado" width="80" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>	
		<adsm:gridColumn title="volumes" 				property="qtVolumes" align="right" width="70" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" 					property="moeda.sgMoeda" width="30" />
			<adsm:gridColumn title="" 						property="moeda.dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 						property="vlOcorrenciaNc" dataType="currency" align="right" width="100" />
		<adsm:gridColumn title="status" 				property="tpStatusOcorrenciaNc" isDomain="true" width="100" />

		<adsm:buttonBar/>
	</adsm:grid>
</adsm:window>


<script>
function initWindow(eventObj) {
	setElementValue("blPermiteAlterar", "true");
}


function pageLoad() {
	//Preparando tela para receber idProcessoWorkflow.
	var url = new URL(parent.location.href);
	var idProcessoWorkflow = url.parameters["idProcessoWorkflow"];
	
	if (idProcessoWorkflow==undefined) {
		onPageLoad();
	} else {
		var data = new Object();			
		data.idProcessoWorkflow = idProcessoWorkflow;
		var sdo = createServiceDataObject("lms.rnc.manterOcorrenciasNaoConformidadeAction.findById", "dataLoad", data);
		xmit({serviceDataObjects:[sdo]});
		
		var tabGroup = getTabGroup(this.document);
		tabGroup._tabsIndex[0].setDisabled(true);
		
		setDisabled(document, true);
	}
}

	function dataLoad_cb(data, error) {	
		onDataLoad_cb(data, error);
		if (error){
			alert(error);
			return false;
		}
	}


function retornoPageLoad_cb(data) {
	onPageLoad_cb();
	povoaGrid();
}

function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		desabilitaTab("caracteristicas", true);
		desabilitaTab("fotos", true);
		desabilitaTab("item", true);
		povoaGrid();
	}
}

function povoaGrid() {
	var fb = buildFormBeanFromForm(this.document.forms[0], 'LIKE_END'); 
	ocorrencias_cb(fb);
	if (getElementValue('naoConformidade.doctoServico.idDoctoServico') == '') {
		desabilitaTab("cad", true);
	}
}

/**
 * Responsável por habilitar/desabilitar uma determinada aba
 */
function desabilitaTab(aba, disabled) {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab(aba, disabled);
}

function ocorrenciasNc_OnClick(id) {
	desabilitaTab("cad", false);
	return true;
}
</script>