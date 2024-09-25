<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="finalizarDescargas">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="recepcaoDescarga" id="recepcaoDescarga" src="/recepcaoDescarga/finalizarDescarga" cmd="recepcaoDescarga"/>
		<adsm:tab title="dispositivosSemIdentificacaoTitulo" id="dispositivosSemIdentificacao" src="/recepcaoDescarga/finalizarDescarga" cmd="dispositivosSemIdentificacao" masterTabId="recepcaoDescarga" copyMasterTabProperties="true"/>
		<adsm:tab title="dispositivosComIdentificacaoTitulo" id="dispositivosComIdentificacao" src="/recepcaoDescarga/finalizarDescarga" cmd="dispositivosComIdentificacao" masterTabId="recepcaoDescarga" copyMasterTabProperties="true"/>
		<adsm:tab title="volumeManifestoEntregaTitulo" id="volumeManifestoEntrega" src="/recepcaoDescarga/finalizarDescarga" cmd="volumeManifestoEntrega" masterTabId="recepcaoDescarga" disabled="true" copyMasterTabProperties="true"/>
		<adsm:tab title="volumeManifestoNacionalTitulo" id="volumeManifestoNacional" src="/recepcaoDescarga/finalizarDescarga" cmd="volumeManifestoNacional" masterTabId="recepcaoDescarga" disabled="true" copyMasterTabProperties="true"/>
	</adsm:tabGroup>
</adsm:window>
