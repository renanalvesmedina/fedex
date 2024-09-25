<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterLotesSalvados" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/pendencia/manterLotesSalvados" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/pendencia/manterLotesSalvados" cmd="cad"/>
		<adsm:tab title="mercadorias" id="mercadorias" src="/pendencia/manterLotesSalvados" cmd="mercadoria"/>
	</adsm:tabGroup>
</adsm:window>
