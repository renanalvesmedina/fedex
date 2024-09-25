<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterSegurosCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterSegurosCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterSegurosCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
