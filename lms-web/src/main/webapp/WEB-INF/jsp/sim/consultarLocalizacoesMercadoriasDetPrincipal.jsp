<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="255" idProperty="idDoctoServico">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="informacoesBasicas" id="infBas" src="/sim/consultarLocalizacoesMercadorias" cmd="principalInfBas" height="230" onShow="findByIdDetalhamento" autoLoad="false"/>
		<adsm:tab title="notasFiscais" id="nf" src="/sim/consultarLocalizacoesMercadorias" cmd="principalNf" height="230" autoLoad="false" onShow="findPaginatedPrincipalNf"/>
	</adsm:tabGroup>
	</adsm:form>
</adsm:window>   
<script>
function buscaIdDoctoServicoAbaPrincipal(){
	var tabGroup = getTabGroup(this.document);
	var tabPrincipal = tabGroup.getTab('principal');
	tabPrincipal.childTabGroup.selectTab('infBas',{name:'tab_click'},true);
}
</script>