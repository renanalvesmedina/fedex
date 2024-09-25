<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterJobs" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterJob" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterJob" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
