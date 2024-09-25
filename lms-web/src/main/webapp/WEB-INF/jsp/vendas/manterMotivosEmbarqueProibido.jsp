<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMotivosEmbarqueProibido" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterMotivosEmbarqueProibido" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterMotivosEmbarqueProibido" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
