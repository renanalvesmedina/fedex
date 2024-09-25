<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDadosComplementares" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterDadosComplementares" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterDadosComplementares" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
