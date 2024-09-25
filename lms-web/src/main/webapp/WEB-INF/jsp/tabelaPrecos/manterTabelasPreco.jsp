<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterTabelasPreco" type="main" onPageLoad="setAbas">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tabelaPrecos/manterTabelasPreco" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/tabelaPrecos/manterTabelasPreco" cmd="cad"/>
		<adsm:tab title="parcelas" masterTabId="cad" disabled="true" id="parcela" src="/tabelaPrecos/manterTabelasPreco" cmd="parcela" autoLoad="false"/>
		<adsm:tab title="anexo" masterTabId="cad" disabled="true" id="anexo" src="/tabelaPrecos/manterTabelasPreco" cmd="anexo" autoLoad="false"/>
	</adsm:tabGroup>
</adsm:window>

<script>
	function setAbas(){
		var url = new URL(document.location.href);
		var idProcessoWorkflow = url.parameters.idProcessoWorkflow;
		if (idProcessoWorkflow != undefined && idProcessoWorkflow != ""){
			tabGroup.setDisabledTab("pesq", true);
			tabGroup.setDisabledTab("cad", false);
			tabGroup.setDisabledTab("parcela", false);
			tabGroup.setDisabledTab("anexo", false);
			tabGroup.selectTab('cad',null,true);
		} else {
			tabGroup.setDisabledTab("pesq", false);
			tabGroup.selectTab('pesq');
		}
	}
</script>