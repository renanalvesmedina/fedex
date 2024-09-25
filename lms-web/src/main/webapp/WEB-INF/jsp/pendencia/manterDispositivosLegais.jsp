<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDispositivosLegais" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/pendencia/manterDispositivosLegais" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/pendencia/manterDispositivosLegais" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
