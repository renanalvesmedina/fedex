<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarMDA" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="list" src="/pendencia/consultarMDA" cmd="list"/>
		<adsm:tab title="mda" id="mda" src="/pendencia/consultarMDA" cmd="mda" disabled="true"/>
		<adsm:tab title="itens" id="itens" src="/pendencia/consultarMDA" cmd="itens" disabled="true" masterTabId="mda" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>