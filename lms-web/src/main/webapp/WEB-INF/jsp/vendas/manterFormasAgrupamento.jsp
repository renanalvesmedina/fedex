<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterFormasAgrupamento" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterFormasAgrupamento" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterFormasAgrupamento" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
