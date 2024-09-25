<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterEventosCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterEventosCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterEventosCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
