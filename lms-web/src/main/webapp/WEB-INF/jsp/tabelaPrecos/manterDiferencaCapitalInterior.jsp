<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDiferencaCapitalInterior" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" 		id="pesq" 	src="/tabelaPrecos/manterDiferencaCapitalInterior" cmd="list"/>
		<adsm:tab title="detalhamento" 	id="cad" 	src="/tabelaPrecos/manterDiferencaCapitalInterior" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>