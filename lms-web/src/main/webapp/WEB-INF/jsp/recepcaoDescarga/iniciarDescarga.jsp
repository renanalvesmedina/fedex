<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="iniciarDescarga">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="recepcaoDescarga" id="recepcaoDescarga" src="/recepcaoDescarga/iniciarDescarga" cmd="recepcaoDescarga"/>
		<adsm:tab title="integrantes" id="integrantes" src="/recepcaoDescarga/iniciarDescarga" cmd="integrantes" masterTabId="recepcaoDescarga" copyMasterTabProperties="true" disabled="true"/>
		<adsm:tab title="lacres" id="lacres" src="/recepcaoDescarga/iniciarDescarga" cmd="lacres" masterTabId="recepcaoDescarga" copyMasterTabProperties="true"/>
		<adsm:tab title="fotos" id="fotos" src="/recepcaoDescarga/iniciarDescarga" cmd="fotos" masterTabId="recepcaoDescarga" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>