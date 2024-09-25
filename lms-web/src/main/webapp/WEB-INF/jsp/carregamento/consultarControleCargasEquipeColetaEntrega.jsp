<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="equipeColetaEntregaTitulo">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="equipe" id="equipe" src="/carregamento/consultarControleCargasEquipeColetaEntrega" cmd="equipe"/>
		<adsm:tab title="integrantes" id="integrantes" src="/carregamento/consultarControleCargasEquipeColetaEntrega" cmd="integrantes" disabled="true" />
	</adsm:tabGroup>
</adsm:window>