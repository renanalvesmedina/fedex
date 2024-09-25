<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterOperadorasCartaoPedagio" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/carregamento/manterOperadorasCartaoPedagio" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/carregamento/manterOperadorasCartaoPedagio" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
