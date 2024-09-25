<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="vincularVersaoDescritivos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/vincularVersaoDescritivos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/vincularVersaoDescritivos" cmd="cad"/>
		<adsm:tab title="contatos" id="contatos" src="/vendas/vincularVersaoDescritivos" cmd="contatos"/>
	</adsm:tabGroup>
</adsm:window>
