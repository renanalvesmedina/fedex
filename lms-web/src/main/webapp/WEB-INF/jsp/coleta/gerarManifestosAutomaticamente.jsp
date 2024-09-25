<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="gerarManifestosAutomaticamente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="gerar" id="proc" src="/coleta/gerarManifestosAutomaticamente" cmd="proc"/>
	</adsm:tabGroup>
</adsm:window>