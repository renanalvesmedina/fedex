<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterClientesOperadoresLogisticos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterClienteOperadorLogistico" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterClienteOperadorLogistico" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
