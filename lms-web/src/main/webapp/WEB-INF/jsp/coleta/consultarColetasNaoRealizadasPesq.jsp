<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function coletasOnDataLoad_cb(data, error){
		resetCombo();
	}
	
	function resetCombo() {
		var obj = document.getElementById("tpOpcaoColeta");
		obj.selectedIndex = 1;
	}
</script>

<adsm:window service="lms.coleta.consultarColetasNaoRealizadasAction" onPageLoadCallBack="coletasOnDataLoad">
	<adsm:form action="/coleta/consultarColetasNaoRealizadas" idProperty="idPedidoColeta" >

		<adsm:combobox property="tpOpcaoColeta" label="opcao" domain="DM_OPCAO_COLETA" required="true" renderOptions="true"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="pedidoColeta"/>
			<adsm:button caption="limpar" onclick="newItem(this.document);" buttonType="resetButton" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="pedidoColeta" idProperty="idPedidoColeta" selectionMode="none" scrollBars="horizontal" rows="13"
			   unique="true" gridHeight="260" onRowClick="populaForm" defaultOrder="nrColeta" 
			   service="lms.coleta.pedidoColetaService.findPaginatedColetasNaoRealizadas"
			   rowCountService="lms.coleta.pedidoColetaService.getRowCountColetasNaoRealizadas"> 
		<adsm:gridColumnGroup separatorType="PEDIDO_COLETA">
			<adsm:gridColumn title="coleta" 		property="sgFilial" dataType="text" width="30"/>
			<adsm:gridColumn title="" 				property="nrColeta" dataType="integer" align="right" width="60" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="cliente" 		property="nmPessoa" width="150" />
		<adsm:gridColumn title="tipo" 			property="tpPedidoColeta" isDomain="true" width="150" />
		<adsm:gridColumn title="solicitacao" 	property="dhPedidoColeta" dataType="JTDateTimeZone" align="center" width="150"/>
		<adsm:gridColumn title="peso" 			property="psTotalInformado" dataType="decimal" mask="###,###,##0.000" unit="kg" width="90" align="right" />
		<adsm:gridColumn title="volumes" 		property="qtTotalVolumesInformado" width="90" align="right" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" 			property="sgMoeda" dataType="text" width="30" />
			<adsm:gridColumn title="" 				property="dsSimbolo" dataType="text" width="30"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 				property="vlTotalInformado" unit="reais" dataType="currency" width="70"/>
		<adsm:gridColumn title="status" 		property="tpStatusColeta" isDomain="true" width="150" />
		<adsm:gridColumn title="dadosColeta" 	property="dadosColeta" image="/images/popup.gif" width="80" link="/coleta/consultarColetasDadosColeta.do?cmd=main" popupDimension="790,520" linkIdProperty="idPedidoColeta" align="center"/>
		<adsm:gridColumn title="dadosCliente" 	property="dadosCliente" image="/images/popup.gif" width="100" link="/coleta/consultarColetasDadosCliente.do?cmd=main" linkIdProperty="idCliente" align="center"/>
		<adsm:gridColumn title="eventosColeta" 	property="eventosColeta" image="/images/popup.gif" width="100" link="/coleta/consultarEventosColeta.do?cmd=pesq&popUp=true" linkIdProperty="idPedidoColeta" align="center"/>		
		<adsm:buttonBar> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click"){
			pedidoColetaGridDef.resetGrid();
		}
	}
	
	function populaForm(valor) {
		return false;
	}
	
	function newItem(doc) {
		cleanButtonScript(doc);
		resetCombo();
	}	
</script>