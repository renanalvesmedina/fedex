<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposRegistroComplementoDocumentoServico" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/manterTiposRegistroComplementoDocumentoServico" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/manterTiposRegistroComplementoDocumentoServico" cmd="cad"/>
		<adsm:tab title="registrosComplementoTitulo" masterTabId="cad" copyMasterTabProperties="true" id="registrosComplemento" src="/expedicao/manterTiposRegistroComplementoDocumentoServico" cmd="registrosComplemento"/>
	</adsm:tabGroup>
</adsm:window>
