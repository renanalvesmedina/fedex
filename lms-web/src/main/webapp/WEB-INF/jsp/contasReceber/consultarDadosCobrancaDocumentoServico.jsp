<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="consultarDadosCobrancaDocumentoServico" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/consultarDadosCobrancaDocumentoServico" cmd="list" />
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/consultarDadosCobrancaDocumentoServico" disabled="true" cmd="cad"/>
			<adsm:tab title="devedores" id="devedores" src="/contasReceber/consultarDadosCobrancaDocumentoServico" disabled="true" cmd="devedores" 
			copyMasterTabProperties="true" masterTabId="cad"/>
		</adsm:tabGroup>
</adsm:window>
