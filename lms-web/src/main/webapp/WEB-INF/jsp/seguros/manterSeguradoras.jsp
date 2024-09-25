<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterSeguradoras" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguros/manterSeguradoras" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguros/manterSeguradoras" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
