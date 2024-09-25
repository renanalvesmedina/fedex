<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:i18nLabels>
	<adsm:include key="LMS-00013"/>
	<adsm:include key="numeroRegistro"/>
	<adsm:include key="controleCarga"/>
	<adsm:include key="meioTransporte"/>
	<adsm:include key="semiReboque"/>
	<adsm:include key="dataRegistro"/>
	<adsm:include key="dataLiberacao"/>
</adsm:i18nLabels>
		
<adsm:window title="manterRegistrosAuditoriasCargas" type="main" onPageLoad="setAbas">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/portaria/manterRegistrosAuditoriasCargas" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/portaria/manterRegistrosAuditoriasCargas" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>

<script>
function setAbas(){
	
	var url = new URL(document.location.href);
	
	var idProcessoWorkflow = url.parameters.idProcessoWorkflow;
		
	if ( idProcessoWorkflow != undefined && idProcessoWorkflow != "" ){
		tabGroup.setDisabledTab("cad", false);
		tabGroup.setDisabledTab("pesq", true);
		tabGroup.selectTab('cad',null,true);
	}else{
		tabGroup.setDisabledTab("cad", false);
		tabGroup.setDisabledTab("pesq", false);
		tabGroup.selectTab('pesq');		
	}
}
</script>