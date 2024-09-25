<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterFilialResponsavelMonitoramento" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/sgr/manterFilialResponsavelMonitoramento" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/sgr/manterFilialResponsavelMonitoramento" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
