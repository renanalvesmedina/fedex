<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterParametrosCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterParametrosCliente" cmd="list" onShow="onShow"/>
		<adsm:tab title="rota" id="cad" src="/vendas/manterParametrosCliente" cmd="cad" />
		<adsm:tab title="parametros" id="param" src="/vendas/manterParametrosCliente" cmd="param" disabled="true" masterTabId="cad"/>
		<adsm:tab title="taxas" id="tax" src="/vendas/manterParametrosCliente" cmd="tax" disabled="true" onHide="hide" masterTabId="cad"/>
		<adsm:tab title="generalidades" id="gen" src="/vendas/manterParametrosCliente" cmd="gen" disabled="true" onHide="hide" masterTabId="cad"/>
	</adsm:tabGroup>
</adsm:window>
