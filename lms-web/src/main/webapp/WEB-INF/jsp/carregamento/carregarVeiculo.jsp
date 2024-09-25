<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="carregarVeiculo" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="carregamento" id="carregamento" src="/carregamento/carregarVeiculo" cmd="carregamento"/>
		<adsm:tab title="gerenciamentoRiscosTitulo" id="gerenciamentoRiscos" src="/carregamento/carregarVeiculo" cmd="gerenciamentoRiscos" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>
