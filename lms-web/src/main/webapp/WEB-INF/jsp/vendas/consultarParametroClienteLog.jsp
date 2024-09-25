<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarParametroClienteLog" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="list" src="/vendas/consultarParametroClienteLog" cmd="list" />
		<adsm:tab title="parametros" id="cad" src="/vendas/consultarParametroClienteLog" cmd="cad" disabled="true"/>
		<adsm:tab title="generalidades" id="generalidades" src="/vendas/consultarParametroClienteLog" cmd="generalidades" disabled="true"/>
		<adsm:tab title="taxas" id="taxas" src="/vendas/consultarParametroClienteLog" cmd="taxas" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>