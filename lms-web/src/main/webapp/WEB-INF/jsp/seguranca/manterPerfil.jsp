<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPerfil" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguranca/manterPerfil" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguranca/manterPerfil" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
