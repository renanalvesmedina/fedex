<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterRotaMasterGM" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/manterRotaMasterGM" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/manterRotaMasterGM" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
