<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterSolicitacoesRetirada" type="main"  onPageLoad="setAbas">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="sim/registrarSolicitacoesRetirada" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="sim/registrarSolicitacoesRetirada" cmd="cad"/>
			<adsm:tab title="documentos" id="doc" src="sim/registrarSolicitacoesRetirada" cmd="doc" 
		     		  copyMasterTabProperties="true" masterTabId="cad" disabled="true"/>
		</adsm:tabGroup>
</adsm:window>

<script>
function setAbas(){
	
	var url = new URL(document.location.href);
	
	var idProcessoWorkflow = url.parameters.idProcessoWorkflow;
		
	if ( idProcessoWorkflow != undefined && idProcessoWorkflow != "" ){
		tabGroup.setDisabledTab("cad", false);
		tabGroup.setDisabledTab("doc", false);
		tabGroup.setDisabledTab("pesq", true);
		tabGroup.selectTab('cad',null,true);
	}else{
		tabGroup.setDisabledTab("cad", false);
		tabGroup.setDisabledTab("doc", true);
		tabGroup.setDisabledTab("pesq", false);
		tabGroup.selectTab('pesq');
		
	}
}
</script>