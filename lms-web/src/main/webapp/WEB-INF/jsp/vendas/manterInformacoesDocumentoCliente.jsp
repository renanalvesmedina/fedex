<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterInformacoesDocumentoCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterInformacoesDocumentoCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterInformacoesDocumentoCliente" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
