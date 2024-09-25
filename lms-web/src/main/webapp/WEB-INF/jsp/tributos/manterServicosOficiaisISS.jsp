<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterServicosOficiaisISS" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tributos/manterServicosOficiaisISS" cmd="list" />
		<adsm:tab title="detalhamento" id="cad" src="/tributos/manterServicosOficiaisISS" cmd="cad" />
	</adsm:tabGroup>
</adsm:window>
