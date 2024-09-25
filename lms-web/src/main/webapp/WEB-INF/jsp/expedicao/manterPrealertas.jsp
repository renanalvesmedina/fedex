<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPrealertas" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/manterPrealertas" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/manterPrealertas" cmd="cad" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>
