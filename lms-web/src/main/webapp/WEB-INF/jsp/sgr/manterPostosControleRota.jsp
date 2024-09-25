<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPostosControleRota" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/sgr/manterPostosControleRota" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/sgr/manterPostosControleRota" cmd="cad"/>
		<adsm:tab title="postosControleTitulo" id="postosControle" src="/sgr/manterPostosControleRota" cmd="postosControle"
		disabled="true" masterTabId="cad" />
	</adsm:tabGroup>
</adsm:window>