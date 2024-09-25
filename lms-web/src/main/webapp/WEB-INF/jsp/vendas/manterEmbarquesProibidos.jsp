<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterEmbarquesProibidos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterEmbarquesProibidos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterEmbarquesProibidos" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
