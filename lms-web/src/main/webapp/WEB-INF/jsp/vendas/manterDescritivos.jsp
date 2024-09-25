<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDescritivos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterDescritivos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterDescritivos" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
