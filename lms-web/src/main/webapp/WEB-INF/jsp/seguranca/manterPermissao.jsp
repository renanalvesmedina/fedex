<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPermissoes" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguranca/manterPermissao" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguranca/manterPermissao" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
