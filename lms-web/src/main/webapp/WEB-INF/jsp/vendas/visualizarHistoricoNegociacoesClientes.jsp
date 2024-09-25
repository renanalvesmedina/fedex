<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="historicoNegociacoesCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/visualizarHistoricoNegociacoesClientes" cmd="list" />
		<adsm:tab title="parametros" id="cad" src="/vendas/visualizarHistoricoNegociacoesClientes" cmd="param" disabled="true" onHide="hide"/>
		<adsm:tab title="taxas" id="tax" src="/vendas/visualizarHistoricoNegociacoesClientes" cmd="tax" disabled="true" onHide="hide"/>
		<adsm:tab title="generalidades" id="gen" src="/vendas/visualizarHistoricoNegociacoesClientes" cmd="gen" disabled="true" onHide="hide"/>
	</adsm:tabGroup>
</adsm:window>
