<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="incluirReciboIndenizacao" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="RIM"                id="rim"      src="/indenizacoes/incluirReciboIndenizacao" cmd="rim" />
		<adsm:tab title="documentosRNC"      id="rnc"      src="/indenizacoes/incluirReciboIndenizacao" cmd="rnc" boxWidth="105"      disabled="true" masterTabId="rim" copyMasterTabProperties="true" onShow="onTabShow"/>
		<adsm:tab title="documentosProcesso" id="processo" src="/indenizacoes/incluirReciboIndenizacao" cmd="processo" boxWidth="160" disabled="true" masterTabId="rim" copyMasterTabProperties="true" onShow="onTabShow"/>
		<adsm:tab title="MDA"                id="mda"      src="/indenizacoes/incluirReciboIndenizacao" cmd="mda"                     disabled="true" masterTabId="rim" copyMasterTabProperties="true" onShow="onTabShow"/>
		<adsm:tab title="parcelas"           id="parcelas" src="/indenizacoes/incluirReciboIndenizacao" cmd="parcelas"                disabled="true" masterTabId="rim" copyMasterTabProperties="true" onShow="onTabShow"/>
		<adsm:tab title="anexo"              id="anexo"    src="/indenizacoes/incluirReciboIndenizacao" cmd="anexo"                   disabled="false" masterTabId="rim" copyMasterTabProperties="true" onShow="onTabShow"/>
	</adsm:tabGroup>
</adsm:window>
