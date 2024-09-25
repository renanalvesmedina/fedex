<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterRegioesCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterRegioesCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterRegioesCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
