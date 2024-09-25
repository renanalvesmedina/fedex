<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:i18nLabels>
	<adsm:include key="LMS-05024"/>
</adsm:i18nLabels>

<adsm:window title="descarregarVeiculo">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="equipe" id="equipe" src="/recepcaoDescarga/descarregarVeiculo" cmd="trocarEquipe"/>
		<adsm:tab title="integrantes" id="cad" src="/recepcaoDescarga/descarregarVeiculo" cmd="trocarIntegrantes" masterTabId="equipe" copyMasterTabProperties="true" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>