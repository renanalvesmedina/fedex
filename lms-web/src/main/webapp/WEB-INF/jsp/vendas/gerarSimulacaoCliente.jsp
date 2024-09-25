<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="gerarSimulacaoCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/gerarSimulacaoCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/gerarSimulacaoCliente" cmd="cad"/>
		<adsm:tab title="proposta" id="prop" src="/vendas/gerarSimulacaoCliente" cmd="prop"/>
	</adsm:tabGroup>
</adsm:window>
