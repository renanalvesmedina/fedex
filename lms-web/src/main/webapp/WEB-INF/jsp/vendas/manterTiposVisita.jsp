<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposVisita" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterTiposVisita" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterTiposVisita" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
