<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="geraFluxosCargaFiliaisNovas" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="processo" id="proc" src="/municipios/gerarFluxoCargaFilialNova" cmd="proc"/>
			<adsm:tab title="filiais"  id="filiais" src="/municipios/gerarFluxoCargaFilialNova" cmd="filiais" onShow="carregaGridPrincipal" masterTabId="" onHide="resetForm" disabled="true"/>
		</adsm:tabGroup>
</adsm:window>
