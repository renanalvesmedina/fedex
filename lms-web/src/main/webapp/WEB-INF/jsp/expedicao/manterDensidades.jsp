<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDensidades" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/manterDensidades" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/manterDensidades" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
