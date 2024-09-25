<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window >
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" idProperty="idDoctoServico">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="manifestoColetaTitle" id="ManifestoColeta" src="/sim/consultarLocalizacoesMercadorias" height="210" cmd="manifestosColeta" boxWidth="120" autoLoad="false" onShow="findManifestoColetaByIdDocto"/>
			<adsm:tab title="manifestoViagemTitle" id="ManifestoViagem" src="/sim/consultarLocalizacoesMercadorias" height="210" cmd="manifestosViagem" boxWidth="120" autoLoad="false" onShow="findManifestoViagemByIdDocto"/>
			<adsm:tab title="manifestoEntregaTitle" id="ManifestoEntrega" src="/sim/consultarLocalizacoesMercadorias" height="210" cmd="manifestosEntrega" boxWidth="120" autoLoad="false" onShow="findManifestoEntregaByIdDocto"/>
		</adsm:tabGroup>
	</adsm:form>	
</adsm:window>  
<script>
function buscaIdDoctoServicoAbaManifesto(){
	var tabGroup = getTabGroup(this.document);
	var tabManifestos = tabGroup.getTab('manifestos');
	tabManifestos.childTabGroup.selectTab('ManifestoColeta',{name:'tab_click'},true);
	
	var idDoctoServico =  parent.document.getElementById("idDoctoServico").value;
	setElementValue("idDoctoServico",idDoctoServico);
}	
</script> 