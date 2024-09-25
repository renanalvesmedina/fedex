<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposClassificacaoCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterTiposClassificacaoCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterTiposClassificacaoCliente" cmd="cad"/>
		<adsm:tab title="classificacoes" masterTabId="cad" copyMasterTabProperties="true" id="classificacoes" src="/vendas/manterTiposClassificacaoCliente" cmd="classif"/>
	</adsm:tabGroup>
</adsm:window>
