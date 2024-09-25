<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="adicionarDocumentosPreManifesto" service="lms.carregamento.carregarVeiculoDocumentosManifestoAction">
	
	<adsm:grid idProperty="idPedidoColeta" property="chegadas" gridHeight="162"	
			   selectionMode="none" scrollBars="both" unique="false" autoSearch="false"
			   showGotoBox="false" showPagging="false" showTotalPageCount="false"
			   service="lms.carregamento.carregarVeiculoDocumentosManifestoAction.findPaginatedChegadas"
			   onRowClick="myOnRowClick" onDataLoadCallBack="calculaTotais">	

		<adsm:gridColumnGroup separatorType="PEDIDO_COLETA">
			<adsm:gridColumn title="coleta" property="filialByIdFilialResponsavel.sgFilial" dataType="text" width="30"/>
			<adsm:gridColumn title="" property="nrColeta" dataType="integer" width="60" mask="00000000"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="servico" property="servico.sgServico" width="60" />
		<adsm:gridColumn title="naturezaProduto" property="naturezaProduto.dsNaturezaProduto" width="170" />
		<adsm:gridColumn title="peso" property="psReal" unit="kg" dataType="decimal" mask="###,###,##0.000" width="100" align="right" />
		<adsm:gridColumn title="volumes" property="qtVolumes" width="100" align="right" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" property="moeda.sgMoeda" dataType="text" width="30"/>
			<adsm:gridColumn title="" property="moeda.dsSimbolo" dataType="text" width="30"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" property="vlMercadoria" dataType="currency" width="80"/>
		<adsm:gridColumn title="destino" property="filialDetalhe.pessoa.nmFantasia" width="170" />
		<adsm:gridColumn title="cliente" property="cliente.pessoa.nmPessoa" width="250" />
   		<adsm:gridColumn title="dadosColeta" property="dadosColeta" image="images/popup.gif" width="80" align="center"
   							link="/coleta/consultarColetasDadosColeta.do?cmd=main" linkIdProperty="idPedidoColeta" 
   							popupDimension="780,526" />
	</adsm:grid>

	<adsm:form action="/carregamento/carregarVeiculoDocumentosManifestoAutomatico">
		<adsm:textbox label="totalDocumentos" property="totalDocumentos" dataType="integer" 
					  disabled="true" labelWidth="14%" width="11%" size="10"/>
		<adsm:textbox label="totalDeVolumes" property="totalVolumes" dataType="currency" 
					  disabled="true" labelWidth="12%" width="11%" size="10"/>
		<adsm:textbox label="totalDoPeso" property="totalPeso" dataType="currency" 
					  disabled="true" labelWidth="12%" width="11%" size="10"/>
		<adsm:textbox label="totalMercadoria" property="totalMercadoria" dataType="currency" 
					  disabled="true" labelWidth="14%" width="14%" size="20"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="fechar" id="fechar" onclick="javascript:window.close();"/>			
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>

<script type="text/javascript">
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click" || eventObj.name == "gridRow_click") {
			povoaGrid();
			setDisabled("fechar", false);			
		}
	}	
	
	/**
	 * Função que popula a grid com registros de DoctoServico
	 */
	function povoaGrid() {		
	   	var formPrincipal = buildFormBeanFromForm(window.parent.document.forms[0]);
	   	chegadasGridDef.executeSearch(formPrincipal, true);
		
		return false;
	}	
	
	/**
	 * Função que retorna 'false' caso uma linha da grid tenha sido clicada.
	 */
	function myOnRowClick(id) {
		return false;
	}
	
	/**
	 * Função q calcula os totais de volumes, pesos, mercadoria e documentos
	 */
	function calculaTotais_cb(data, error) {
		var sdo = createServiceDataObject("lms.carregamento.carregarVeiculoDocumentosManifestoAction.getCalculaTotais", "resultadoCalculaTotais", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	 
	/**
	 * Resultado do cálculo dos totais dos registros da grid
	 */
	function resultadoCalculaTotais_cb(data, error) {
		if(!error) {
			setElementValue("totalVolumes", data.totalVolumes);
		 	setElementValue("totalPeso", data.totalPeso);
	 		setElementValue("totalMercadoria", data.totalMercadoria);
		 	setElementValue("totalDocumentos", data.totalDocumentos);
		 } else {
		 	alert(error);
		 }
	}		
	
</script>