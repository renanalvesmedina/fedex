<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterEventos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/workflow/manterEventos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/workflow/manterEventos" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>