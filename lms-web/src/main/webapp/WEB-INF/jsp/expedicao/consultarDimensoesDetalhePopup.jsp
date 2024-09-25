<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.monitoramentoConhecimentosSemAWBAction"
			 onPageLoadCallBack="pageLoad">
	
	<adsm:form action="/expedicao/monitoramentoConhecimentosSemAWB">
		<adsm:section 
			caption="dimensoesCM"  
			width="65%"/>
		<adsm:hidden property="idConhecimento" serializable="false"/>	
	</adsm:form>
	
	<adsm:grid idProperty="idDimensoes" 
			   property="dimensoes" 
			   selectionMode="none" 
			   service="lms.expedicao.monitoramentoConhecimentosSemAWBAction.findDimensoes"
			   showPagging="false"
			   gridHeight="150" 
			   autoSearch="false"
			   unique="false"			
			   scrollBars="vertical"
			   onRowClick="clicarRegistro">

		<adsm:gridColumn title="dimensao1" 
						 property="nrDimensao1"  
						 align="right"/>
						 
		<adsm:gridColumn title="dimensao2" 
						 property="nrDimensao2" 
						 align="right"/>
						 
		<adsm:gridColumn title="dimensao3" 
						 property="nrDimensao3" 
						 align="right"/>
		
		<adsm:buttonBar freeLayout="false" >
			<adsm:button caption="fechar" 
						 id="botaoFechar" 
						 onclick="javascript:window.close();" />
		</adsm:buttonBar>  
	</adsm:grid>
</adsm:window>

<script>
	function pageLoad_cb() {
		onPageLoad_cb();		
		loadGridDimensoes();
		setDisabled('botaoFechar', false);
		setFocus("botaoFechar", false);
	}
	
	function loadGridDimensoes(){
		var url = new URL(parent.location.href);
		setElementValue("idConhecimento", url.parameters["idConhecimento"]);
		
		var data = {idConhecimento : getElementValue("idConhecimento")};
		var service = "lms.expedicao.monitoramentoConhecimentosSemAWBAction.findDimensoes";
		var sdo = createServiceDataObject(service, "retornoConsulta", data);	
		xmit({serviceDataObjects:[sdo]});						
	}
	
	function retornoConsulta_cb(data){
		dimensoesGridDef.resetGrid();
		dimensoesGridDef.populateGrid(data);
	}
	
	function clicarRegistro(id) {
		return false;
	}

</script>