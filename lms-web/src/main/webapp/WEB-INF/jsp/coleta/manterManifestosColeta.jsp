<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterManifestosColeta" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/coleta/manterManifestosColeta" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/coleta/manterManifestosColeta" cmd="cad" disabled="true"/>
		<adsm:tab title="coletas" id="coletas" src="/coleta/manterManifestosColeta" cmd="coletas" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>
