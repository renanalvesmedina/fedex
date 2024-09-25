<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="registrarVendasLotesSalvados" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/pendencia/registrarVendasLotesSalvados" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/pendencia/registrarVendasLotesSalvados" cmd="cad"/>
		<adsm:tab title="mercadorias" id="mercadorias" src="/pendencia/registrarVendasLotesSalvados" cmd="merc"/>
		<adsm:tab title="cheques" id="cheques" src="/pendencia/registrarVendasLotesSalvados" cmd="cheques"/>
	</adsm:tabGroup>
</adsm:window>
