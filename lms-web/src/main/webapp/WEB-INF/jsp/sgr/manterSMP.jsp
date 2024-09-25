<%@ taglib prefix="adsm" uri="/WEB-INF/adsm.tld" %>
<adsm:window title="manterSMP" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab cmd="list" id="pesq" src="/sgr/manterSMP" title="listagem" />
		<adsm:tab cmd="cad" disabled="true" id="cad" src="/sgr/manterSMP" title="detalhamento" />
		<adsm:tab cmd="providencias" disabled="true" id="providencias" masterTabId="cad" src="/sgr/manterSMP" title="providencias" />
		<adsm:tab cmd="eventos" disabled="true" id="eventos" masterTabId="cad" src="/sgr/manterSMP" title="eventos" />
	</adsm:tabGroup>
</adsm:window>
