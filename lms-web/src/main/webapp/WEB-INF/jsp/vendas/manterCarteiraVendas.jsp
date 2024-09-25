<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterCarteiraVendas" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab src="/vendas/manterCarteiraVendas" id="pesq" cmd="list" title="listagem"/>
		<adsm:tab src="/vendas/manterCarteiraVendas" id="cad" cmd="cad" title="detalhamento"/>
		<adsm:tab src="/vendas/manterCarteiraVendas" id="clientes" cmd="clientes" title="clientes" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>