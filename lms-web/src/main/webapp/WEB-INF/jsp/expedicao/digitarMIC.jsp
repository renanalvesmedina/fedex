<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMIC" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/digitarMIC" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/digitarMIC" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
