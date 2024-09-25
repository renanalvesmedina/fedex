<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="registrarSolicitacaoPriorizacaoEmbarque" type="main"> 
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/sim/registrarSolicitacoesEmbarque" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/sim/registrarSolicitacoesEmbarque" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>