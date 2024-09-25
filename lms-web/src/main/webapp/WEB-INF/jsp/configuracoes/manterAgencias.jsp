<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterAgencias" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterAgencias" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterAgencias" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
