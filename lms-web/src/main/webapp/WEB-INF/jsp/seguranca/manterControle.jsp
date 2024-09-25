<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterControle" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguranca/manterControle" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguranca/manterControle" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
