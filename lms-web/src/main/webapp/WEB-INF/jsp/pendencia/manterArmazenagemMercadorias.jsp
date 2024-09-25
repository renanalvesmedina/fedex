<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterArmazenagemMercadorias" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="listagem" src="/pendencia/manterArmazenagemMercadorias" cmd="list"/>
		<adsm:tab title="detalhamento" id="detalhamento" src="/pendencia/manterArmazenagemMercadorias" cmd="cad"/>
		<adsm:tab title="mercadorias" id="mercadorias" src="/pendencia/manterArmazenagemMercadorias" cmd="merc"/>
	</adsm:tabGroup>
</adsm:window>
