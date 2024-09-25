<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPerfilUsuario" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"     id="pesq" src="/seguranca/manterPerfilUsuario" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad"  src="/seguranca/manterPerfilUsuario" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
