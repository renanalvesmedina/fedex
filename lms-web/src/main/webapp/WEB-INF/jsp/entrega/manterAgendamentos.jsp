<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAgendamentos" type="main" onPageLoad="setAbas">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/entrega/manterAgendamentos" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/entrega/manterAgendamentos" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>

<script>


var dataEmpresaSessao;
function loadTipoEmpresa_cb(data, errorKey) {
	 dataEmpresaSessao = data;	
	  
     var url = new URL(document.location.href);
	 if(dataEmpresaSessao.tipoEmpresaSessao == 'P'){			
 			tabGroup.getTab("cad").properties.tipoEmpresaSessao= url.parameters['tipoEmpresaSessao'];			
			tabGroup.getTab("cad").properties.idDoctoServico= url.parameters['idDoctoServico'];	
			tabGroup.getTab("cad").properties.idManifestoEntregaDocumento= url.parameters['idManifestoEntregaDocumento'];			
	 }
	 
}


function setAbas(){

	var url = new URL(document.location.href);
	var idProcessoWorkflow = url.parameters.idProcessoWorkflow;
	var idsMonitoramentoCCT = url.parameters.idsMonitoramentoCCT;

	if ( idProcessoWorkflow != undefined && idProcessoWorkflow != "" ){
		tabGroup.setDisabledTab("cad", false);
		tabGroup.setDisabledTab("pesq", true);
		tabGroup.selectTab('cad',null,true);
	} else if ( idsMonitoramentoCCT != undefined && idsMonitoramentoCCT != "" ){
		tabGroup.setDisabledTab("cad", false);
		tabGroup.setDisabledTab("pesq", true);
		tabGroup.selectTab('cad',null,true);
	} else{
		tabGroup.setDisabledTab("cad", false);
		tabGroup.setDisabledTab("pesq", false);
		tabGroup.selectTab('pesq');			
	}
	
	var data = new Array();
	var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.buscaTipoEmpresaSessao", "loadTipoEmpresa", data);
	xmit({serviceDataObjects:[sdo]});
	
	
}




 


</script>  