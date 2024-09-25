<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterCargosOperacionais" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/carregamento/manterCargosOperacionais" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/carregamento/manterCargosOperacionais" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
