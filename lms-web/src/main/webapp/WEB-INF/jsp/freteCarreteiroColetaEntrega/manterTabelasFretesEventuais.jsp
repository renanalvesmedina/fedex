<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTabelasFretesEventuais" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="freteCarreteiroColetaEntrega/manterTabelasFretesEventuais" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="freteCarreteiroColetaEntrega/manterTabelasFretesEventuais" disabled="true" cmd="cad"/>
		</adsm:tabGroup>
		<adsm:i18nLabels>
			<adsm:include key="LMS-25012" />
		</adsm:i18nLabels>
</adsm:window>

