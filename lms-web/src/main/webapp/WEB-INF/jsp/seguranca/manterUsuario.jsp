<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterUsuarios" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguranca/manterUsuario" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguranca/manterUsuario" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
