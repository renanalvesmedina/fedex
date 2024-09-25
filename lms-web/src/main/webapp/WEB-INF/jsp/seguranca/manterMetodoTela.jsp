<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMetodoTela" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguranca/manterMetodoTela" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguranca/manterMetodoTela" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
