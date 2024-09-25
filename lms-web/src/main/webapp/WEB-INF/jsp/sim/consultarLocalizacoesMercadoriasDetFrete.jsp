<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="255" idProperty="idDoctoServico">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="dadosFrete" id="freteDados" src="/sim/consultarLocalizacoesMercadorias" cmd="fretePrincipal" height="230" onShow="findPaginatedParcelas" autoLoad="false"/>
		<adsm:tab title="impostos" id="freteImpostos" src="/sim/consultarLocalizacoesMercadorias" cmd="freteImpostos" height="230" onShow="findPaginatedImpostos" autoLoad="false"/>
		<adsm:tab title="dadosCalculo" id="freteCalculo" src="/sim/consultarLocalizacoesMercadorias" cmd="freteCalculo" height="230" boxWidth="100" autoLoad="false" onShow="findPaginatedDadosCalculo"/>
		<adsm:tab title="dadosServicosAdicionais" id="servicosAdicionais" src="/sim/consultarLocalizacoesMercadorias" cmd="freteServicosAdicionais" height="230" boxWidth="100" autoLoad="false" onShow="findServicosAdicionaisFrete"/>
	</adsm:tabGroup>
	</adsm:form>
</adsm:window>   
<script>
function buscaIdDoctoServicoAbaFrete(){
	var tabGroup = getTabGroup(this.document);
	var tabFretes = tabGroup.getTab('frete');
	tabFretes.childTabGroup.selectTab('freteDados',{name:'tab_click'},true);
	
	var idDoctoServico =  parent.document.getElementById("idDoctoServico").value;
	setElementValue("idDoctoServico",idDoctoServico);
}
</script>