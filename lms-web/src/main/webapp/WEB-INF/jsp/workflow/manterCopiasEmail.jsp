<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterCopiasEmail" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/workflow/manterCopiasEmail" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/workflow/manterCopiasEmail" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
