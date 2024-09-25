<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="inserirParametros" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/inserirParametros" cmd="list"/>
		<adsm:tab title="rota" id="cad" src="/vendas/inserirParametros" cmd="cad"/>
		<adsm:tab title="parametros" id="param" src="/vendas/inserirParametros" cmd="param"/>
		<adsm:tab title="taxas" id="tax" src="/vendas/inserirParametros" cmd="tax"/>
		<adsm:tab title="generalidades" id="gen" src="/vendas/inserirParametros" cmd="gen"/>
		<adsm:tab title="servicosAdicionaisTitulo" id="form" src="/vendas/inserirParametros" cmd="form" boxWidth="120"/>
	</adsm:tabGroup>
</adsm:window>
