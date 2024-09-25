<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDocumentosCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterDocumentosCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterDocumentosCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
