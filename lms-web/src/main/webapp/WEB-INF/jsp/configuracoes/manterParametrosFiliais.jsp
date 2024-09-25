<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterParametrosFiliais" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterParametrosFiliais" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterParametrosFiliais" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
