<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript">

	/**
	 * Carrega as informacoes da pagina...
	 */
	function loadObjectsAndData() {
		setDisabled("retornar", false);
		
		var url = new URL(parent.location.href);
		var data = new Array();
		
		data.executionGroup = url.parameters["grupo"];
		data.serviceName = url.parameters["metodo"];
		data.tipoGrupo = url.parameters["tipoGrupo"];
		
		var sdo = createServiceDataObject("lms.configuracoes.consultarJobsAction.getJobParameters", "loadObjectsAndData", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	function loadObjectsAndData_cb(data, error) {
		
		if (data._exception!=undefined) {
			alert(error);
		} else {
			populateParameterGrid(data.parameters);	
		}
		
		onPageLoad();
		return true;
	}
	
	/**
	 * Carrega a list de parametrosJob.
	 *
	 * @param
	 */
	function populateParameterGrid(list){
		
		//Starta algumas das propriedades necessarias para a grid...
		parametrosJobGridDef.gridState.stats = new Object();
		parametrosJobGridDef.gridState.stats.elapsedRenderTime = 0;
		parametrosJobGridDef.gridState.data = new Array();
		
		var resultSetPage = new Object();
		resultSetPage.list = list;
		resultSetPage.hasNextPage = "true";
		resultSetPage.hasPriorPage = "false";
		resultSetPage.currentPage = "1";

		parametrosJobGridDef.populateGrid(resultSetPage);
	}

</script>

<adsm:window service="lms.configuracoes.consultarJobsAction" onPageLoad="loadObjectsAndData">	
	<adsm:grid property="parametrosJob" idProperty="idParametro" title="parametros"
			   service="lms.configuracoes.consultarJobsAction.findPaginatedPametrosJobs"
			   rowCountService="lms.configuracoes.consultarJobsAction.getRowCountParametrosJobs"
			   scrollBars="vertical" selectionMode="none" gridHeight="273" showPagging="false" 
			   onRowClick="disableGridClick();">
		<adsm:gridColumn property="nome" title="nome" dataType="text" width="50%"/>
		<adsm:gridColumn property="valor" title="valor" dataType="text" width="50%"/>
		<adsm:buttonBar> 
			<adsm:button id="retornar" caption="retornar" onclick="returnToParent()"/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">

	/**
	 * fecha a atual janela
	 */
	function returnToParent(){
		self.close();
	}
	 
	/**
	 * Desabilita o click na grid
	 */
	function disableGridClick(){
		return false;
	}

</script>

