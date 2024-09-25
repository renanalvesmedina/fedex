<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterManifestosViagem" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/manterManifestosViagem" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/manterManifestosViagem" cmd="cad"/>
		<adsm:tab title="conhecimentos" id="conhecimento" src="/expedicao/manterManifestosViagem" cmd="conhecimento"/>
	</adsm:tabGroup>
</adsm:window>
