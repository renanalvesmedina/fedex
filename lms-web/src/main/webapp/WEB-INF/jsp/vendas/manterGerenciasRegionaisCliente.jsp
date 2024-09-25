<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterGerenciasRegionaisCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterGerenciasRegionaisCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterGerenciasRegionaisCliente" cmd="cad"/>		
	</adsm:tabGroup>
</adsm:window>