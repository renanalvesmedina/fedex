<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterUsuarioADSM" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguranca/manterUsuarioADSM" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguranca/manterUsuarioADSM" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
