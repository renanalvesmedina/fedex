<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterSetores" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"     id="pesq" src="/carregamento/manterSetor" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/carregamento/manterSetor" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
