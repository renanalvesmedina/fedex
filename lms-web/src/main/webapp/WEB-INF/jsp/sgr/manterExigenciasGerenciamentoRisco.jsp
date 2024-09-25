<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterExigenciasGerenciamentoRisco" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/sgr/manterExigenciasGerenciamentoRisco" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/sgr/manterExigenciasGerenciamentoRisco" cmd="cad"/>
		<adsm:tab title="ordem" id="ordem" src="/sgr/manterExigenciasGerenciamentoRisco" cmd="ordem"/>
	</adsm:tabGroup>
</adsm:window>
