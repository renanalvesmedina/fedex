<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPedidosCompras" type="main">
 <adsm:tabGroup selectedTab="0">
	<adsm:tab title="listagem" src="/sim/manterPedidosCompras" cmd="list" id="pesq"/>
	<adsm:tab title="detalhamento" src="/sim/manterPedidosCompras" cmd="cad" id="cad"/>
 </adsm:tabGroup>
</adsm:window>
