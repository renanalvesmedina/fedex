<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterSegmentosMercado" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/vendas/manterSegmentosMercado" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/vendas/manterSegmentosMercado" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
