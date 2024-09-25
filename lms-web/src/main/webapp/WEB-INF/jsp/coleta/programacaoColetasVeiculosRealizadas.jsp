<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.coleta.programacaoColetasVeiculosAction" >

	<adsm:grid property="coletasVeiculosRealizadas" idProperty="idPedidoColeta" 
			   selectionMode="none" gridHeight="295" scrollBars="both" showPagging="false" unique="true" autoSearch="false" 
			   service="lms.coleta.programacaoColetasVeiculosAction.findPaginatedColetasVeiculosRealizadas"
			   rowCountService=""
			   onRowClick="coletasVeiculosRealizadas_OnClick">
		<adsm:gridColumnGroup separatorType="PEDIDO_COLETA">
			<adsm:gridColumn title="coleta" 		property="filialByIdFilialResponsavel.sgFilial" width="50" />
			<adsm:gridColumn title="" 				property="nrColeta" width="100" align="right" dataType="integer" mask="0000000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="dadosColeta" 	property="dadosColeta" image="/images/popup.gif" openPopup="true" link="/coleta/consultarColetasDadosColeta.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idPedidoColeta"/>
		<adsm:gridColumn title="cliente" 		property="cliente.pessoa.nrIdentificacaoFormatado" width="120" align="right" />
		<adsm:gridColumn title="" 				property="cliente.pessoa.nmPessoa" width="250" />
		<adsm:gridColumn title="endereco" 		property="enderecoComComplemento" width="280" />
		<adsm:gridColumn title="volumes" 		property="qtTotalVolumesVerificado" width="80" align="right"/>
		<adsm:gridColumn title="peso" 			property="psTotalVerificado" width="100" align="right" unit="kg" dataType="decimal" mask="###,###,###,##0.000" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" 		property="moeda.sgMoeda" width="30" />
			<adsm:gridColumn title="" 			property="moeda.dsSimbolo" width="40" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 				property="vlTotalVerificado" dataType="currency" width="100" align="right" />
		<adsm:gridColumn title="alterarValores" property="alterarValores" image="/images/popup.gif" openPopup="true" link="javascript:alterarValores" width="100" align="center" />
		<adsm:gridColumn title="veiculo" 		property="dadosVeiculo" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosDadosVeiculo.do?cmd=main" popupDimension="790,260" width="100" align="center" linkIdProperty="idControleCarga"/>
		<adsm:gridColumn title="equipe" 		property="dadosEquipe" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosDadosEquipe.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idControleCarga"/>
		<adsm:buttonBar> 
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "gridRow_click" || eventObj.name == "tab_click") {
		povoaGrid();
	}
}

function povoaGrid() {
    var tabDetCad = getTabGroup(this.document).parentTab.tabGroup.getTab("cad");
    var idControleCarga = tabDetCad.getFormProperty("idControleCarga");
	if (idControleCarga != undefined && idControleCarga != ""){
	    var filtro = new Array();
    	setNestedBeanPropertyValue(filtro, "idControleCarga", idControleCarga);
    	coletasVeiculosRealizadasGridDef.executeSearch(filtro);
    	return false;
	}
}

/**
 * Re-carrega a grid apos se clicar em 'alterar valores'
 */
function alterarValores(id){
	showModalDialog('coleta/alteracaoValoresColeta.do?cmd=main&idPedidoColeta='+id ,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:770px;dialogHeight:200px;');
	povoaGrid();
}

function coletasVeiculosRealizadas_OnClick(id) {
	return false;
}
</script>