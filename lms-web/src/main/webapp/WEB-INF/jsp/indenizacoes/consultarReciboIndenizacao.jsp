<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarReciboIndenizacao" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"     id="pesq"       src="/indenizacoes/consultarReciboIndenizacao" cmd="list"       onShow="onTabShow"/>
		<adsm:tab title="RIM"          id="cad"        src="/indenizacoes/consultarReciboIndenizacao" cmd="rim"        onShow="onTabShow" disabled="true"/>
		<adsm:tab title="documentos"   id="documentos" src="/indenizacoes/consultarReciboIndenizacao" cmd="documentos" masterTabId="cad" copyMasterTabProperties="true" onShow="onTabShow"  boxWidth="90" />
		<adsm:tab title="MDA"          id="mda"        src="/indenizacoes/consultarReciboIndenizacao" cmd="mda"        onShow="onTabShow"/>
		<adsm:tab title="parcelas"     id="parcelas"   src="/indenizacoes/consultarReciboIndenizacao" cmd="parcelas"   onShow="onTabShow"/>
		<adsm:tab title="anexo"        id="anexo"      src="/indenizacoes/consultarReciboIndenizacao" cmd="anexo"      masterTabId="cad" copyMasterTabProperties="true" onShow="onTabShow"/>
		<adsm:tab title="eventos"      id="eventos"    src="/indenizacoes/consultarReciboIndenizacao" cmd="eventos"    masterTabId="cad" copyMasterTabProperties="true" onShow="onTabShow"/>
	</adsm:tabGroup>
</adsm:window>
