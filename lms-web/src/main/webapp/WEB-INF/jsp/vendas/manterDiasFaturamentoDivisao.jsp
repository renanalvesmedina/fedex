<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDiasFaturamentoDivisao" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterDiasFaturamentoDivisao" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterDiasFaturamentoDivisao" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
