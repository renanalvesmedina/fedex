<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="finalizarCarregamentoPreManifestos" >
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="dispositivosSemIdentificacaoTitulo" id="list" src="/carregamento/finalizarCarregamentoPreManifestos" cmd="list"/>
		<adsm:tab title="dispositivosComIdentificacaoTitulo" id="identificacao" src="/carregamento/finalizarCarregamentoPreManifestos" cmd="identificacao"/>
	</adsm:tabGroup>
</adsm:window>
