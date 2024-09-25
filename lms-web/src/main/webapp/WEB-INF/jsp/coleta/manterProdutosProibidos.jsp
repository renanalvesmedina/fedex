<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterProdutosProibidos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/coleta/manterProdutosProibidos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/coleta/manterProdutosProibidos" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
