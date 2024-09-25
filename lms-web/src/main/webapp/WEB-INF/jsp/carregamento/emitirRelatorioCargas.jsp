<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultaRelatorioCargasTerminal" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="relatorio" id="pesq" src="/carregamento/emitirRelatorioCargas" cmd="pesq"/>
	</adsm:tabGroup>
</adsm:window>
