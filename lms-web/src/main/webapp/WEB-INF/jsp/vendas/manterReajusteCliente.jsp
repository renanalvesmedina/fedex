<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="gerarReajusteCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterReajusteCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterReajusteCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>