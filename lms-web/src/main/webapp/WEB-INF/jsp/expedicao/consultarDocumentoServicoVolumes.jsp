<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="conteudoControleCargasManifestosTitulo" service="lms.expedicao.consultarDocumentoServicoVolumesAction" 
			 onPageLoadCallBack="retornoCarregaPagina" >

	<adsm:form action="/expedicao/consultarDocumentoServicoVolumes">
		<adsm:section caption="volumes" />
	</adsm:form>


	<adsm:grid idProperty="idVolumeNotaFiscal" property="volumes" selectionMode="none" scrollBars="horizontal"
			   unique="true" autoSearch="false" showPagging="true" gridHeight="310" rows="18"
			   service="lms.expedicao.consultarDocumentoServicoVolumesAction.findPaginatedVolumes"
			   rowCountService="lms.expedicao.consultarDocumentoServicoVolumesAction.getRowCountVolumes"
			   onRowClick="volumes_OnClick"
	>

		<adsm:gridColumn title="documentoServico" property="tpDoctoServico" isDomain="true" width="30"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" 				property="sgFilialOrigem" width="30" />
			<adsm:gridColumn title="" 				property="nrConhecimento" width="50" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="notaFiscal"		property="nrNotaFiscal" width="80" align="right" />
		<adsm:gridColumn title="sequencia"		property="nrSequencia" width="70" align="right" />
		<adsm:gridColumn title="localizacao"	property="dsLocalizacaoMercadoria" width="180" />
		<adsm:gridColumn title=""				property="sgLocalizacaoFilial" width="40" />
		<adsm:gridColumn title="unitizado"		property="dsTipoDispositivoUnitizacao" width="150" />
		<adsm:gridColumn title=""				property="nrIdentificacaoDispositivoUnitizacao" width="80" align="right" />
		<adsm:gridColumn title="macroZona" 		property="dsMacroZona" width="200" />
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>
function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		populateGrid();
	}
}

function populateGrid() {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "idDoctoServico", dialogArguments.window.document.getElementById('idDoctoServico').value);
    volumesGridDef.executeSearch(filtro, true);
    return false;
}

function volumes_OnClick(id) {
	return false;
}
</script>