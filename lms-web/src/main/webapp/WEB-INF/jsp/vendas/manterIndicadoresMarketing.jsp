<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterIndicadoresMarketing" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterIndicadoresMarketing" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterIndicadoresMarketing" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
