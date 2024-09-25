<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="parametrosProposta" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterPropostasClienteParam" cmd="list"/>
		<adsm:tab title="rota" id="cad" src="/vendas/manterPropostasClienteParam" cmd="cad"/>
		<adsm:tab title="parametros" id="param" src="/vendas/manterPropostasClienteParam" cmd="param"/>
		<adsm:tab title="taxas" id="tax" src="/vendas/manterPropostasClienteParam" cmd="tax" onHide="hide"/>
		<adsm:tab title="generalidades" id="gen" src="/vendas/manterPropostasClienteParam" cmd="gen" onHide="hide"/>
	</adsm:tabGroup>
</adsm:window>
