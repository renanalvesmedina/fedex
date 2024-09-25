<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterMotivosCancelamentoControleCargas" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/carregamento/manterMotivosCancelamentoControleCargas" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/carregamento/manterMotivosCancelamentoControleCargas" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
