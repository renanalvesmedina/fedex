<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="servicosAdicionaisProposta" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterPropostasServicosAdicionais" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterPropostasServicosAdicionais" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>