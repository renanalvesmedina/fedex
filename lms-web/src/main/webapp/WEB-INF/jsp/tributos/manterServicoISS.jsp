<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterServicoISS" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tributos/manterServicoISS" cmd="list" />
		<adsm:tab title="detalhamento" id="cad" src="/tributos/manterServicoISS" cmd="cad" />
	</adsm:tabGroup>
</adsm:window>
