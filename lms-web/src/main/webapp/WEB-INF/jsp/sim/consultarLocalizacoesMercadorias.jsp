<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarLocalizacoesMercadorias" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="consulta" id="doc" src="/sim/consultarLocalizacoesMercadorias" cmd="cons"/>
		<adsm:tab title="listagem" id="list" src="/sim/consultarLocalizacoesMercadorias" cmd="list" disabled="true" onShow="executaConsulta"/>
		<adsm:tab title="detalhamento" id="cad" src="/sim/consultarLocalizacoesMercadorias" cmd="det" disabled="true" autoLoad="false" onShow="buscaIdDoctoServico"/>
	</adsm:tabGroup>
</adsm:window>