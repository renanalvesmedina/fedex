<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPerifericosRastreador" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"     id="pesq" src="/sgr/manterPerifericosRastreador" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/sgr/manterPerifericosRastreador" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
