<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterEventos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterEventos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterEventos" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
