<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTarifasSpot" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tabelaPrecos/manterTarifasSpot" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/tabelaPrecos/manterTarifasSpot" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
