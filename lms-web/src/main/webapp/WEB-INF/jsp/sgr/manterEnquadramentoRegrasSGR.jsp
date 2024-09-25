<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterEnquadramentoRegrasSGR" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/sgr/manterEnquadramentoRegrasSGR" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/sgr/manterEnquadramentoRegrasSGR" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
