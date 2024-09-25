<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposAgrupamento" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterTiposAgrupamento" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterTiposAgrupamento" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
