<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.coleta.programacaoColetasAction">

	<adsm:grid property="coletasProgramadas" idProperty="idPedidoColeta" selectionMode="check" autoSearch="false"
			   scrollBars="horizontal" unique="true" gridHeight="310" title="coletasProgramadas" rows="15"
			   onRowClick="coletasProgramadas_OnClick"
			   service="lms.coleta.programacaoColetasAction.findPaginatedColetasProgramadas"
			   rowCountService="lms.coleta.programacaoColetasAction.getRowCountColetasProgramadas" >
		<adsm:gridColumnGroup separatorType="PEDIDO_COLETA">
			<adsm:gridColumn title="coleta" 		property="sgFilialResponsavel" width="45" />
			<adsm:gridColumn title="" 				property="nrColeta" width="80" align="right" dataType="integer" mask="0000000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="meioTransporte"	property="nrFrotaByIdTransportado" width="60" />
		<adsm:gridColumn title=""				property="nrIdentificadorByIdTransportado" width="90" />
		<adsm:gridColumn title="dadosColeta" 	property="dadosColeta" image="/images/popup.gif" openPopup="true" link="/coleta/consultarColetasDadosColeta.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idPedidoColeta"/>
		<adsm:gridColumn title="cliente" 		property="nrIdentificacaoFormatadoCliente" width="120" align="right" />
		<adsm:gridColumn title="" 				property="nmPessoaCliente" width="200" />
		<adsm:gridColumn title="endereco" 		property="enderecoComComplemento" width="250" />
		<adsm:gridColumn title="volumes" 		property="qtTotalVolumesVerificado" width="100" align="right" />
		<adsm:gridColumn title="peso" 			property="psTotalVerificado" width="100" align="right" unit="kg" dataType="decimal" mask="###,###,###,##0.000" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="sgMoeda" title="valor" width="30"/>
			<adsm:gridColumn title="" property="dsSimboloMoeda" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 				property="vlTotalVerificado" dataType="currency" width="100" align="right" />
		<adsm:gridColumn title="horarioColeta" 	property="strHorarioColeta" width="130" align="center"/>

		<adsm:buttonBar freeLayout="false"> 
			<adsm:removeButton service="lms.coleta.programacaoColetasAction.removeByIdsColetasProgramadas" />
		</adsm:buttonBar>
	</adsm:grid>
	
	<adsm:form action="/coleta/programacaoColetas" idProperty="idPedidoColeta" >
	</adsm:form>
</adsm:window>

<script>
function initWindow(eventObj) {
	povoaGrid();
}

function povoaGrid() {
	coletasProgramadasGridDef.executeSearch();
}

function coletasProgramadas_OnClick(idPedidoColeta) {
	return false;
}
</script>