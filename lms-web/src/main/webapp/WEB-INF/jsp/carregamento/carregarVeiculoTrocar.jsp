<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:i18nLabels>
	<adsm:include key="LMS-05024"/>
</adsm:i18nLabels>

<adsm:window title="carregarVeiculo">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="equipe" id="equipe" src="/carregamento/carregarVeiculo" cmd="trocarEquipe"/>
		<adsm:tab title="integrantes" id="cad" src="/carregamento/carregarVeiculo" cmd="trocarIntegrantes" masterTabId="equipe" copyMasterTabProperties="true" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>