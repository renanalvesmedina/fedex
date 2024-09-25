<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAnaliseCreditoCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterAnaliseCreditoCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterAnaliseCreditoCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>