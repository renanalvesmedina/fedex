<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="iniciarCarregamento">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="carregamento" id="carregamento" src="/carregamento/iniciarCarregamento" cmd="carregamento"/>
		<adsm:tab title="integrantes" id="integrantes" src="/carregamento/iniciarCarregamento" cmd="integrantes" masterTabId="carregamento" copyMasterTabProperties="true" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>