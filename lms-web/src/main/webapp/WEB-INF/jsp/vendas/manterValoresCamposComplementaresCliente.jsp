<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterValoresCamposComplementaresCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterValoresCamposComplementaresCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterValoresCamposComplementaresCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
