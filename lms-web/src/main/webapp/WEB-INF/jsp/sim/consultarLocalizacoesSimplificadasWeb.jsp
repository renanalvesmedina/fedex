<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarLocalizacoesSimplificadaWeb" type="main">
 <adsm:tabGroup selectedTab="0">
	<adsm:tab title="consulta" src="/sim/consultarLocalizacoesSimplificadasWeb" cmd="cons" id="cons"/>	
	<adsm:tab title="listagem" src="/sim/consultarLocalizacoesSimplificadasWeb" cmd="list" id="list"/>
	<adsm:tab title="detalhamento" src="/sim/consultarLocalizacoesSimplificadasWeb" cmd="det" id="det"/>
	
 </adsm:tabGroup>
</adsm:window>