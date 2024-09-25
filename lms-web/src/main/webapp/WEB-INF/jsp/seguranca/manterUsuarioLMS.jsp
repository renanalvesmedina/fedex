<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterUsuarios" type="main">
	<adsm:tabGroup selectedTab="0">	
		<adsm:tab title="listagem"     id="pesq" src="/seguranca/manterUsuarioLMS" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad"  src="/seguranca/manterUsuarioLMS" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
