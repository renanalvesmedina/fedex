<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterNaturezaProdutoCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterDivisaoClienteNaturezaProduto" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterDivisaoClienteNaturezaProduto" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
