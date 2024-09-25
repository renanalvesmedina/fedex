<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterGerenciadorasRisco" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/sgr/manterGerenciadorasRisco" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/sgr/manterGerenciadorasRisco" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
