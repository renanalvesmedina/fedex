<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDispositivosUnitizacao" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/recepcaoDescarga/manterDispositivosUnitizacao" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/recepcaoDescarga/manterDispositivosUnitizacao" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
