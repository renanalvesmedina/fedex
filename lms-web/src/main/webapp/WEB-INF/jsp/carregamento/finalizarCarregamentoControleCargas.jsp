<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="finalizarCarregamentoControleCargas" >
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="carregamento" id="list" src="/carregamento/finalizarCarregamentoControleCargas" cmd="list"/>
		<adsm:tab title="fotos" id="fotos" src="/carregamento/finalizarCarregamentoControleCargas" cmd="fotos" masterTabId="list" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>
