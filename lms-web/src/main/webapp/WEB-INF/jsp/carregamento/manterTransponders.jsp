<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTransponder" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"     id="pesq" src="/carregamento/manterTransponders" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/carregamento/manterTransponders" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
