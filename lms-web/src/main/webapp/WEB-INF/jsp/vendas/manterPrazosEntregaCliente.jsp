<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPrazosEntregaCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterPrazosEntregaCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterPrazosEntregaCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
