<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterRecibosReembolso" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"                id="pesq"             src="/seguros/manterRecibosReembolso" cmd="list" onShow="onTabShow"/>
		<adsm:tab title="detalhamento"            id="cad"              src="/seguros/manterRecibosReembolso" cmd="cad"/>
		<adsm:tab title="documentosServicoTitulo" id="documentosServico" src="/seguros/manterRecibosReembolso" cmd="documentosServico" masterTabId="cad" copyMasterTabProperties="true" onShow="onTabShow"/>
	</adsm:tabGroup>
</adsm:window>
