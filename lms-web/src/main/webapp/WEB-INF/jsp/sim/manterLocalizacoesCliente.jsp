<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDescricaoEventos" type="main">
 <adsm:tabGroup selectedTab="0">
	<adsm:tab title="listagem" src="/sim/manterLocalizacoesCliente" cmd="list" id="pesq"/>
	<adsm:tab title="detalhamento" src="/sim/manterLocalizacoesCliente" cmd="cad" id="cad"/>
 </adsm:tabGroup>
</adsm:window>


