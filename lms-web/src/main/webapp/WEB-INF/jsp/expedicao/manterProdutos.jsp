<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterProdutos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/manterProdutos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/manterProdutos" cmd="cad"/>
		<adsm:tab title="produtos" id="prod" src="/expedicao/manterProdutos" cmd="prod" masterTabId="cad" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>
