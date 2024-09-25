<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterTiposExigenciasGerenciamentoRisco" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/sgr/manterTiposExigenciasGerenciamentoRisco" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/sgr/manterTiposExigenciasGerenciamentoRisco" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
