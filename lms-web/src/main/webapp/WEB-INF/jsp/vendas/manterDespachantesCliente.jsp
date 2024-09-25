<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDespachantesCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterDespachantesCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterDespachantesCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
