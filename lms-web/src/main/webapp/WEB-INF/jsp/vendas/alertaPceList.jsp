<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript"> 

	/**
	 * Usada para fazer o carregamento da tela de alertPceList. 
	 * Captura da tela pai um array de ids de pce e caso existam, faz a chamada 
	 * para a funcao que ira popular a grid desta a partir dos ids capturados.
	 *
	 * @param data
	 */
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		
		//Forca o botao de fechar para o estado de enable...
		setDisabled("btnFechar", false);
		
		var data = new Object();
		data.codigos = dialogArguments.window.getCodigos();
	
		var sdo = createServiceDataObject("lms.vendas.alertaPceAction.findPaginatedPCE", "populateGrid", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Carrega a list de versaoDescritivoPce.
	 *
	 * @param data
	 */
	function populateGrid_cb(data, error){
		if (data._exception!=undefined) {
			alert(error);
		} else {
			//Starta algumas das propriedades necessarias para a grid...
			versaoDescritivoPceGridDef.gridState.stats = new Object();
			versaoDescritivoPceGridDef.gridState.stats.elapsedRenderTime = 0;
			versaoDescritivoPceGridDef.gridState.data = new Array();
			
			var resultSetPage = new Object();
			resultSetPage.list = data.list;
			resultSetPage.hasNextPage = "true";
			resultSetPage.hasPriorPage = "false";
			resultSetPage.currentPage = "1";
	
			versaoDescritivoPceGridDef.populateGrid(resultSetPage);
		}
	}
		
</script>		

<adsm:window onPageLoadCallBack="pageLoad">
	<adsm:grid property="versaoDescritivoPce" idProperty="idVersaoDescritivoPce" 
		   	   selectionMode="none" scrollBars="both" title="alertaPce"
		   	   onRowClick="disableGridClick();" autoSearch="false"
		   	   service="lms.vendas.alertaPceAction.findDataAlertPCE.findPaginatedPCE"
		   	   showTotalPageCount="false" showPagging="false" gridHeight="210">
		   	   
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="cliente" property="versaoPce.cliente.pessoa.nrIdentificacao" width="70"/>
			<adsm:gridColumn title="" property="versaoPce.cliente.pessoa.nmPessoa" width="140" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="descricao" property="dsDescritivoPce" width="500"/>
		<adsm:gridColumn title="contatos" property="versaoContatoPces" width="500"/>
		<adsm:gridColumn title="usuario" property="usuario.nmUsuario" width="140"/>
		
		<adsm:buttonBar> 
			<adsm:button caption="fechar" id="btnFechar" onclick="closeList();"/>
		</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window>   

<script>

	/**
	 
	 * fecha a atual janela
	 */
	function closeList(){
		
		var gridData = versaoDescritivoPceGridDef.gridState.data;
		
		if (gridData.length>0) {
			var data = new Object();
			data.ids = new Array();
			
			for (i=0; gridData.length>i; i++) {
				data.ids[i] = gridData[i].idVersaoDescritivoPce;
			}
			
			var sdo = createServiceDataObject("lms.vendas.alertaPceAction.executeConfirmaRecebimentoDoAlertaList", "closeList", data);
			xmit({serviceDataObjects:[sdo]});
		} else {
			self.close();
		}
	}
	
	/**
	 * Retorna para a tela pai ou exibe um erro caso exista
	 *
	 * @param data
	 * @param error
	 */
	function closeList_cb(data, error){
		if (data._exception==undefined) {
			self.close();
		} else {
			alert(error);
		}
	}
	 
	/**
	 * Desabilita o click na grid
	 */
	function disableGridClick(){
		return false;
	}
	
</script>
