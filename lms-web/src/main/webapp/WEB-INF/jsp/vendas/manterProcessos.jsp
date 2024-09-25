<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterProcessos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterProcessos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterProcessos" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
