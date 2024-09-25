<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterEscoltaUtilizadaTrechoViagem" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/sgr/manterEscoltaUtilizadaTrechoViagem" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/sgr/manterEscoltaUtilizadaTrechoViagem" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
