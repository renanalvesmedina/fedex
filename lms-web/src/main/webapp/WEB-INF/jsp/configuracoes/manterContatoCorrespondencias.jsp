<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterContatoCorrespondencias" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterContatoCorrespondencias" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterContatoCorrespondencias" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
