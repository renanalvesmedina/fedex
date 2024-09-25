<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="consultarReciboDemonstrativoDesconto" type="main" onPageLoad="setAbas">
		<adsm:tabGroup selectedTab="0" >
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/consultarReciboDemonstrativoDesconto" cmd="list" disabled="true" />
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/consultarReciboDemonstrativoDesconto" cmd="cad" disabled="true"/>
			<adsm:tab title="documentosServicoTitulo"
			 		  masterTabId="cad" 
			 		  disabled="true"
			 		  copyMasterTabProperties="true"
		   	 		  id="item" 
					  src="/contasReceber/consultarReciboDemonstrativoDesconto" 
					  cmd="item" 
					  onShow="myOnShow"
					  boxWidth="145"/>
		</adsm:tabGroup>
</adsm:window>

<script>

function setAbas(){
	
	var url = new URL(document.location.href);
	
	var idProcessoWorkflow = url.parameters.idProcessoWorkflow;
		
	if ( idProcessoWorkflow != undefined && idProcessoWorkflow != "" ){
		tabGroup.setDisabledTab("cad", false);
		tabGroup.setDisabledTab("item", false);
		tabGroup.setDisabledTab("pesq", true);
		tabGroup.selectTab('cad',null,true);
	}else{
		tabGroup.setDisabledTab("cad", true);
		tabGroup.setDisabledTab("item", true);
		tabGroup.setDisabledTab("pesq", false);
		tabGroup.selectTab('pesq');
		
	}
}

</script>