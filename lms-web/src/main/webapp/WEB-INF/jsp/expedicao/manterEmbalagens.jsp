<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterEmbalagens" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/manterEmbalagens" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/manterEmbalagens" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
