<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterListaPermissao" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguranca/manterListaPermissao" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguranca/manterListaPermissao" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>

