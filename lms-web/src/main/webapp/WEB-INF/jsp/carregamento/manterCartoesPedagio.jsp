<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterCartoesPedagio" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/carregamento/manterCartoesPedagio" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/carregamento/manterCartoesPedagio" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
