<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterSeguradorasReguladora" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguros/manterSeguradorasReguladora" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguros/manterSeguradorasReguladora" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
