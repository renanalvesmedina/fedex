<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterServicosAdicionaisCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterServicosAdicionaisCliente" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterServicosAdicionaisCliente" cmd="cad"/>			
		<adsm:tab title="destinatariosComServicoPago" id="destinatarios" src="/vendas/manterServicosAdicionaisCliente" cmd="destinatarios" 
			disabled="true" masterTabId="cad" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>
