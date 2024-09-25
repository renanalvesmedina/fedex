<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterFiliaisEmbarcadoras" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterFiliaisEmbarcadoras" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterFiliaisEmbarcadoras" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
