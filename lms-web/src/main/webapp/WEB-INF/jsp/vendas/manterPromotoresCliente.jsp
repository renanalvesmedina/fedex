<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPromotoresCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterPromotoresCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterPromotoresCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
