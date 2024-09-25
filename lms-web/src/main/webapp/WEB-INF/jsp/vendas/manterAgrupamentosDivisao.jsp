<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAgrupamentosDivisao" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterAgrupamentosDivisao" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterAgrupamentosDivisao" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
