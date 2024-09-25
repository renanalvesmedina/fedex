<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterProdutosDivisao" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterProdutosDivisao" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterProdutosDivisao" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
