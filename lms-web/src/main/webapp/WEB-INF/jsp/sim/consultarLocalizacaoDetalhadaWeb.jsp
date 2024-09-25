<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarLocalizacaoDetalhadaWeb" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="consulta" id="doc" src="/sim/consultarLocalizacaoDetalhadaWeb" cmd="cons"/>
		<adsm:tab title="listagem" id="list" src="/sim/consultarLocalizacaoDetalhadaWeb" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/sim/consultarLocalizacaoDetalhadaWeb" cmd="det"/>
	</adsm:tabGroup>
</adsm:window>