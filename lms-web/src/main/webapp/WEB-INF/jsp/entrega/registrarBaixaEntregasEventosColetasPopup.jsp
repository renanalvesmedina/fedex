<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="eventosColeta2" service="lms.entrega.registrarBaixaEntregasAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:grid idProperty="idEventoColeta" onRowClick="onRowClick" width="100%"
		property="eventoColeta" gridHeight="200" autoSearch="false" 
		selectionMode="none" scrollBars="horizontal" rows="10"
		service="lms.entrega.registrarBaixaEntregasAction.findPaginatedEventosColetas"
		rowCountService="lms.entrega.registrarBaixaEntregasAction.getRowCountEventosColetas">
		
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="manifestoColeta" property="sgFilialManifesto" width="20"/>
			<adsm:gridColumn title="" property="nrManifesto" dataType="integer" mask="00000000" width="60"/>			
		</adsm:gridColumnGroup>		
				
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="pedidoColeta" property="sgFilialColeta" width="20"/>		
			<adsm:gridColumn title="" property="nrColeta" dataType="integer" width="70" mask="0000000000"/>
		</adsm:gridColumnGroup>		
		
		<adsm:gridColumn title="evento" property="tpEventoColeta.description" width="100"/>
		<adsm:gridColumn title="ocorrencia" property="dsDescricaoResumida" width="100"/>
		<adsm:gridColumn title="dataHora" property="dhEvento" dataType="JTDateTimeZone" width="90" align="center" />
		<adsm:gridColumn title="cliente" property="nmPessoa" width="160"/>		
		
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="endereco" property="dsTipoLogradouro" width="40"/>
			<adsm:gridColumn title="" property="dsEndereco" width="100"/>
			<adsm:gridColumn title="" property="nrEndereco" width="40"/>
			<adsm:gridColumn title="" property="dsComplemento" width="60"/>			
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="municipio" property="nmMunicipio" width="80"/>
		<adsm:gridColumn title="uf" property="sgUnidadeFederativa" width="30"/>
		
	</adsm:grid>
</adsm:window>

<script>
	var idControleCarga = null;
	    
    function myOnPageLoad_cb(){
		onPageLoad_cb();
		
		var u = new URL(parent.location.href);
		idControleCarga = u.parameters["idControleCarga"];
   		
		loadGrid();
    }
    
    function loadGrid() {
    	var param = null;
    	    	
    	if(idControleCarga){ 
    		param = {idControleCarga:idControleCarga};
    		
    		eventoColetaGridDef.executeSearch(param); 
    	}
    }
    
    /**
	 * Desabilita o click na row
	 */
	function onRowClick() {
		return false;
	}
</script>