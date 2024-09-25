<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="incluirExcluirCRT" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/incluirExcluirCRT" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/incluirExcluirCRT" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
