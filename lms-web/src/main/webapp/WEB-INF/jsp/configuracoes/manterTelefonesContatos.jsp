<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterTelefonesContatos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterTelefonesContatos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterTelefonesContatos" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
