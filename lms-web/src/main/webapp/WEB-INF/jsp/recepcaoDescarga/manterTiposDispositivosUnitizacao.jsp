<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposDispositivosUnitizacao" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/recepcaoDescarga/manterTiposDispositivosUnitizacao" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/recepcaoDescarga/manterTiposDispositivosUnitizacao" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
