<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterParametrosISS" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tributos/manterParametrosISS" cmd="list" />
		<adsm:tab title="detalhamento" id="cad" src="/tributos/manterParametrosISS" cmd="cad" />
	</adsm:tabGroup>
</adsm:window>
