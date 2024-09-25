<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarAWBs" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/consultarAWBs" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/consultarAWBs" cmd="cad"/>
		<adsm:tab title="historico" id="hist" src="/expedicao/consultarAWBs" cmd="hist" onHide="hide"/>
		<adsm:tab title="rnc" id="rnc" src="/expedicao/consultarAWBs" cmd="rnc"/>
	</adsm:tabGroup>
</adsm:window>
