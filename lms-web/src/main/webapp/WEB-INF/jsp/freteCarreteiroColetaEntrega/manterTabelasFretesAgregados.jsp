<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTabelasFretesAgregados" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="freteCarreteiroColetaEntrega/manterTabelasFretesAgregados" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="freteCarreteiroColetaEntrega/manterTabelasFretesAgregados" cmd="cad"/>
			<adsm:tab title="faixasPeso" id="faixaPeso" src="freteCarreteiroColetaEntrega/manterTabelasFretesAgregados" cmd="faixaPeso" masterTabId="cad" copyMasterTabProperties="true" disabled="true" />
		</adsm:tabGroup>
</adsm:window>