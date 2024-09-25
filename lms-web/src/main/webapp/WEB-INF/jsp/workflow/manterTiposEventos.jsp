<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposEventos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/workflow/manterTiposEventos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/workflow/manterTiposEventos" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
