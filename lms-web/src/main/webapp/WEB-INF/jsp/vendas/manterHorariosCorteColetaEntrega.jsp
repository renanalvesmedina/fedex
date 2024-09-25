<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterHorariosCorteColetaEntrega" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterHorariosCorteColetaEntrega" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterHorariosCorteColetaEntrega" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
