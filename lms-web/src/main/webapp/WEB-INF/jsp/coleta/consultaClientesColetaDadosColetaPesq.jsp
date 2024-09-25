<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>
function carregaClientePageLoad() {
	onPageLoad();
	setMasterLink(this.document, true);
	mostraEscondeBotaoFechar();
	carregaDadosCliente();
}

function returnFalse(){
	return false;
}
</script>

<adsm:window title="coletasCliente" service="lms.coleta.consultaClientesColetaAction" onPageLoad="carregaClientePageLoad">
	<adsm:form action="/coleta/consultaClientesColetaDadosColeta" height="115">	
		<adsm:section caption="coletas"/>
		
		<adsm:textbox label="cliente" width="85%" disabled="true" serializable="false"
					  property="cliente.pessoa.nrIdentificacao"
					  dataType="text" 
					  size="20" maxLength="20">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="50" disabled="true" serializable="false"/>
		</adsm:textbox>
		
		<adsm:hidden property="cliente.idCliente" serializable="true"/>
		<adsm:hidden property="idCliente" />
		
		<adsm:range label="periodo" width="85%" >
			<adsm:textbox dataType="JTDate" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDate" property="dataFinal" picker="true"/>
		</adsm:range>

		<adsm:multicheckbox label="status" property="tpStatusColetaAberta|tpStatusColetaTransmitida|tpStatusColetaManifestada|tpStatusColetaExecutada|tpStatusColetaCancelada|"  texts="aberta|transmitida|manifestada|executada|cancelada" width="85%" />
		<adsm:combobox label="tipoColeta" property="tpPedidoColeta" domain="DM_TIPO_PEDIDO_COLETA" width="85%" renderOptions="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="coletas"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="coletas" idProperty="idPedidoColeta" scrollBars="horizontal" selectionMode="none"
				service="lms.coleta.consultaClientesColetaAction.findPaginatedPedidosColetaByCliente" gridHeight="202"
				rowCountService="lms.coleta.consultaClientesColetaAction.getRowCountPedidosColetaByCliente"
				onRowClick="returnFalse">
		<adsm:gridColumnGroup separatorType="PEDIDO_COLETA">
			<adsm:gridColumn title="numero" property="sgFilialColeta" width="40"/>
			<adsm:gridColumn title="" property="nrColeta" width="60" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="tipo" property="tpPedidoColeta" width="105" isDomain="true"/>
		<adsm:gridColumn title="solicitacao" property="dhPedidoColeta" width="140" align="center" dataType="JTDateTimeZone"/>
		<adsm:gridColumn title="peso" property="psTotalVerificado" width="70" align="right" unit="kg" dataType="decimal" mask="###,###,##0.000"/>
		<adsm:gridColumn title="volumes" property="qtTotalVolumesVerificado" width="70" align="right"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" property="sgMoeda" width="30" />
		    <adsm:gridColumn title="" property="dsSimbolo" width="30"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" property="vlTotalVerificado" width="70" align="right" dataType="currency"/>
		<adsm:gridColumn title="status" property="tpStatusColeta" width="105" isDomain="true"/>
		<adsm:gridColumn title="dadosColeta" property="dadosColeta" 	image="/images/popup.gif" width="80" link="/coleta/consultarColetasDadosColeta.do?cmd=main" popupDimension="790, 520" linkIdProperty="idPedidoColeta" align="center"/>
		<adsm:gridColumn title="eventosColeta" property="eventosColeta" image="/images/popup.gif" width="100" link="/coleta/consultarEventosColeta.do?cmd=pesq&popUp=true" popupDimension="790, 470" linkIdProperty="idPedidoColeta" align="center"/>
		<adsm:gridColumn title="imprimir" property="imprimir" 		image="/images/printer.gif" width="70" reportName="lms.coleta.relatorioPedidoColetaService" linkIdProperty="idPedidoColeta" align="center" />
	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />	
	</adsm:buttonBar>
</adsm:window>
<script>
function carregaDadosCliente() {
	var idCliente = getElementValue("idCliente");
	var map = new Array();
	setNestedBeanPropertyValue(map, "idCliente", idCliente);
    var sdo = createServiceDataObject("lms.coleta.consultaClientesColetaAction.findCliente", "resultadoCarregaDadosCliente", map);
    xmit({serviceDataObjects:[sdo]});
}

/**
 * Mostra ou esconde o botão Fechar caso seja uma lookup ou nao.
 */
function mostraEscondeBotaoFechar(){
	var isLookup = window.dialogArguments && window.dialogArguments.window;
	if (isLookup) {
		setDisabled('btnFechar',false);
	} else {
		setVisibility('btnFechar', false);
	}	
}

/**
 * Carrega o cliente que foi selecionado na tela anterior
 */
 function resultadoCarregaDadosCliente_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		return false;
	}
	setElementValue('cliente.pessoa.nrIdentificacao', data.pessoa.nrIdentificacao);
	setElementValue('cliente.pessoa.nmPessoa',data.pessoa.nmPessoa);
	setElementValue('cliente.idCliente', data.idCliente);
	
	document.getElementById("cliente.pessoa.nrIdentificacao").masterLink=true;		
	document.getElementById("cliente.pessoa.nmPessoa").masterLink=true;		
	document.getElementById("cliente.idCliente").masterLink=true;		
}

/**
 * Função para não chamar nada no onclick da row da grid
 */
function disableRowClick() {
	return false;
}
</script>
