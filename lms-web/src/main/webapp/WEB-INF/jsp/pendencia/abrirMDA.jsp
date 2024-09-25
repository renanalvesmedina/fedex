<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="abrirMDA" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="mda" id="mda" src="/pendencia/abrirMDA" cmd="mda"/>
		<adsm:tab title="itens" id="itens" src="/pendencia/abrirMDA" cmd="itens" masterTabId="mda" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>