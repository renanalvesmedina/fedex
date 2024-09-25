<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="registrarNegociacoes" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/rnc/registrarNegociacoes" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/rnc/registrarNegociacoes" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
