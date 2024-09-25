<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterHorariosColetaAutomaticaCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterHorariosColetaAutomaticaCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterHorariosColetaAutomaticaCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
