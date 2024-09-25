<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.pesquisarRelacoesCobrancaAction" >

	<adsm:form action="/contasReceber/pesquisarRelacoesCobranca" idProperty="idDoctoServico">

		<adsm:masterLink showSaveAll="false" idProperty="idRelacaoCobranca" >
			<adsm:masterLinkItem label="filialCobranca" property="sgFilialNmFantasiaFilial"/>
			<adsm:masterLinkItem label="relacaoCobranca" property="nrRelacaoCobrancaFilial"/>
			<adsm:masterLinkItem label="moeda" property="siglaMoeda"/>
			
		</adsm:masterLink>
		

		<adsm:textbox property="nrIdentificacao" labelWidth="20%" width="16%" dataType="text" label="clienteResponsavel" disabled="true">
		<adsm:textbox property="nmPessoa" dataType="text" width="64%" size="50" disabled="true" />
		</adsm:textbox>
		

		
		<adsm:textbox label="documentoServico" property="tpDocumentoServico" dataType="text" serializable="false" 
			disabled="true" size="10" labelWidth="20%" width="80%">
			<adsm:textbox property="sgFilial" dataType="text" serializable="false" disabled="true" size="5"/>
			<adsm:textbox property="nrDoctoServico" dataType="integer" serializable="false" disabled="true" size="10"/>
		</adsm:textbox>
		
		
		
		
		<adsm:textbox dataType="currency"  property="vlDevido" label="valorDocumento" labelWidth="20%" width="30%" disabled="true"/>
		<adsm:textbox dataType="currency" property="vlDesconto" label="valorDesconto" labelWidth="20%" width="30%" disabled="true"/>
		<adsm:textbox property="dsMotivoDesconto" labelWidth="20%" width="30%" size="40" dataType="text" label="motivoDesconto" disabled="true" />


	</adsm:form>
	
	
	<adsm:grid idProperty="idDoctoServico" property="doctoServico" 
			   service="lms.contasreceber.pesquisarRelacoesCobrancaAction.findPaginatedMapGridById" 
			   rowCountService="lms.contasreceber.pesquisarRelacoesCobrancaAction.getRowCountMapGrid"
			   selectionMode="none"
			   rows="9"
			   onRowClick="onClickGrid"
			   onRowDblClick="onClickGrid"
			   detailFrameName="documentosServico">
	
	
	
	<adsm:gridColumn width="30" title="documentoServico" property="tpDocumentoServico"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="0" title="" property="sgFilial" dataType="text"/>	
			<adsm:gridColumn width="100" title="" property="nrDoctoServico" dataType="integer"/>
		</adsm:gridColumnGroup>	
	
		<adsm:gridColumn width="220" title="clienteResponsavel" property="clienteResponsavel" dataType="text"/>
		<adsm:gridColumn  title="valorDocumento" property="vlDevido" dataType="currency"/>
		<adsm:gridColumn width="120" title="valorDesconto" property="vlDesconto" dataType="currency"/>	
		<adsm:gridColumn width="200" title="motivoDesconto" dataType="text"  property="dsMotivoDesconto"/>
		
		<adsm:buttonBar />
		
	</adsm:grid>
	
</adsm:window>

<script>

function initWindow(eventObj) {
	var event = eventObj.name;
	if(event == "tab_click"){
	  cleanButtonScript();
	}
 }

/* Funcao chamada quando se clica em uma linha da grid*/
	function onClickGrid(id){
		
		  var remoteCall = {serviceDataObjects:new Array()};
	      var dataCall = createServiceDataObject("lms.contasreceber.pesquisarRelacoesCobrancaAction.findMapGridById", "populaDados", 
	            {
	                  idDoctoServico:id
	            }
	      );
	      remoteCall.serviceDataObjects.push(dataCall);
	      xmit(remoteCall);  
	
		return false;
	}

/* funcao executada depois do clic em uma das linhas da grid */
	function populaDados_cb(data, errorMessage, errorCode,eventObj){
		if (errorMessage != undefined) {
			alert(errorMessage+'');
			return;
		}
		
		/* limpa os dados e depois os seta */
		cleanButtonScript();
		onDataLoad_cb(data, errorMessage, errorCode, eventObj);
 		 
 
	    	    
	}

	function myOnShow(){
		return false;
	}
	
	

</script>