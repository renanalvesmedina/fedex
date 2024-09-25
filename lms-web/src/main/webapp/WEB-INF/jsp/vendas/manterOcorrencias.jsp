<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterOcorrencias" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterOcorrencias" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterOcorrencias" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
