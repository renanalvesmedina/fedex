<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterFrequencias" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterFrequencias" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterFrequencias" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
