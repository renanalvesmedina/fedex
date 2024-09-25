<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterIntegrantesComite" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/workflow/manterIntegrantesComite" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/workflow/manterIntegrantesComite" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>