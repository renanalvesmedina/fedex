<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterEstoqueDispositivosUnitizacao" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/carregamento/manterEstoqueDispositivosUnitizacao" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/carregamento/manterEstoqueDispositivosUnitizacao" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
