<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposSinistro" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguros/manterTiposSinistro" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguros/manterTiposSinistro" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
