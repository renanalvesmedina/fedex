<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDivisoesCliente" type="main" onPageLoad="setAbas">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterDivisoesCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterDivisoesCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
<script>

function setAbas(){

	var url = new URL(document.location.href);

	var idProcessoWorkflow = url.parameters.idProcessoWorkflow;

	if (idProcessoWorkflow != undefined && idProcessoWorkflow != "" ){
		tabGroup.setDisabledTab("pesq", true);
		tabGroup.setDisabledTab("cad", false);
		tabGroup.selectTab('cad',null,true);
	}else{
		tabGroup.setDisabledTab("pesq", false);
		tabGroup.selectTab('pesq');
	}
}

</script>
