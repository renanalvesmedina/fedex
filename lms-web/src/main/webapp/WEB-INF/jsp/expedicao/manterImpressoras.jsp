<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterImpressoras" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/manterImpressoras" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/manterImpressoras" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
