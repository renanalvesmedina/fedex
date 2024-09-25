<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposTabelaPrecoCiaAerea" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tabelaPrecos/manterTiposTabelaPrecoCiaAerea" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/tabelaPrecos/manterTiposTabelaPrecoCiaAerea" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
