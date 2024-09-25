<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterValorCpt" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterValorCpt" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterValorCpt" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
