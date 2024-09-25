<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterGruposEconomicos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterGruposEconomicos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterGruposEconomicos" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
