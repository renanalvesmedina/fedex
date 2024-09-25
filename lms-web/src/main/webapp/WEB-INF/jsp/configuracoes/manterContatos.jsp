<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterContatos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterContatos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterContatos" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
