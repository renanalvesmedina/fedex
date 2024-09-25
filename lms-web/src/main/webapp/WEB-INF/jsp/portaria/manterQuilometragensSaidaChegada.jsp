<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterQuilometragensSaidaChegada" type="main" onPageLoad="setAbas">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/portaria/manterQuilometragensSaidaChegada" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/portaria/manterQuilometragensSaidaChegada" cmd="cad" disabled="true" />
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