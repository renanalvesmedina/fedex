<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterInformacoesProdutosCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterInformacoesProdutosCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterInformacoesProdutosCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
