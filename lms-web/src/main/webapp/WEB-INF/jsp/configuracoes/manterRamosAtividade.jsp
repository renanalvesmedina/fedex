<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterRamosAtividade" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterRamosAtividade" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterRamosAtividade" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
