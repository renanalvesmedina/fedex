<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="gerarParametrosProposta" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="detalhamento" id="cad" src="/vendas/gerarParametrosPropostasCliente" cmd="cad"/>
		<adsm:tab title="destinos" id="dest" src="/vendas/gerarParametrosPropostasCliente" cmd="dest" disabled="true"/>
		<adsm:tab title="percentual"   id="grup" src="/vendas/gerarParametrosPropostasCliente" cmd="grup" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>
