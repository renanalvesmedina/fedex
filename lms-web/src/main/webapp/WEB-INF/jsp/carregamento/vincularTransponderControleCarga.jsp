<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="vincularTransponderControleCarga" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"     id="pesq" src="/carregamento/vincularTransponderControleCarga" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/carregamento/vincularTransponderControleCarga" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
