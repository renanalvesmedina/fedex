<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="descarregarVeiculo" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="recepcaoDescarga" id="recepcaoDescarga" src="/recepcaoDescarga/descarregarVeiculo" cmd="recepcaoDescarga"/>
		<adsm:tab title="documentos" id="abaDocumentos" src="/recepcaoDescarga/descarregarVeiculo" cmd="abaDocumentos" disabled="true"/>
		<adsm:tab title="documentosServico" id="abaDocumentoServico" src="/recepcaoDescarga/descarregarVeiculo" cmd="abaDocumentoServico" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>
