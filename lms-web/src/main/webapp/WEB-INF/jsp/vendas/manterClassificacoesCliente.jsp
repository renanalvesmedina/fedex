<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterClassificacoesCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterClassificacoesCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterClassificacoesCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
