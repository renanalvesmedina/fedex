<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAjustesTarifa" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" 		id="pesq" 	src="/tabelaPrecos/manterAjustesTarifa" cmd="list"/>
		<adsm:tab title="detalhamento" 	id="cad" 	src="/tabelaPrecos/manterAjustesTarifa" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
