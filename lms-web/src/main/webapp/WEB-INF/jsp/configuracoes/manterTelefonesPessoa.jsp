<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterTelefones" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterTelefonesPessoa" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterTelefonesPessoa" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
