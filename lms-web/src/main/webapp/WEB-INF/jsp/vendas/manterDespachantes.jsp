<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDespachantes" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterDespachantes" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterDespachantes" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
