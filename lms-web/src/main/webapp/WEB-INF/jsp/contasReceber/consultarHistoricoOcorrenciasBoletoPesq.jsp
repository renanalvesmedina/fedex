<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window  service="lms.contasreceber.consultarHistoricoOcorrenciasBoletoAction" onPageLoadCallBack="myOnPageLoad">
	
<adsm:form id="histOcorrBole.form" idProperty="idBoleto" action="/contasReceber/consultarHistoricoOcorrenciasBoleto">
	

	    <adsm:textbox label="numeroBoleto" property="nrBoleto" dataType="integer"  size="13" width="35%" disabled="true" mask="000000000000"/>
        
        <adsm:textbox label="fatura" property="sgFilial" dataType="text"  size="3"  maxLength="3" width="5%" disabled="true"/>
        <adsm:textbox property="nrFatura" dataType="integer"  size="10"  maxLength="10" width="15%" disabled="true" mask="0000000000"/>

</adsm:form>


        <adsm:grid  idProperty="idHistoricoBoleto"  property="historicoBoleto" unique="false" 
        	service="lms.contasreceber.consultarHistoricoOcorrenciasBoletoAction.findPaginatedHistMov"
	        rowCountService="lms.contasreceber.consultarHistoricoOcorrenciasBoletoAction.getRowCountHistMov"
			selectionMode="none"	    
			disableMarkAll="true"
			onRowClick="loadMotMov"
			onRowDblClick="loadMotMov"

			gridHeight="45"
			title="historicoMovimentacao"
			
			rows="3">
		    <adsm:gridColumn dataType="text" 	title="tipo" property="tipoOcorrencia" isDomain="true" />
     		<adsm:gridColumn dataType="integer" title="numero" property="nrOcorrenciaBanco" width="7%"/>
	    	<adsm:gridColumn dataType="text" 	title="descricao" property="dsOcorrenciaBanco" width="10%"/>
		    <adsm:gridColumn dataType="text" 	title="situacao" property="tpSituacaoHistoricoBoleto" isDomain="true" width="10%" />
            <adsm:gridColumn dataType="text"  	title="usuario" property="nmUsuario" width="10%" />
    		<adsm:gridColumn dataType="JTDateTimeZone" title="dataHoraInclusao"  property="dhOcorrencia" width="18%" />
    		<adsm:gridColumn dataType="text" 	title="fluxoAprovacao"  
	    		image="/images/popup.gif" 
	    		openPopup="true" 
	    		link="javascript:showHistoricoAprovacao" 
	    		align="center" 
				width="20%" 
				property="idPendencia" />
    		<adsm:gridColumn dataType="text" 	title="observacao"  property="dsHistoricoBoleto" width="18%"/>
	    </adsm:grid>

        <adsm:grid  idProperty="idHistoricoMotMov"  property="historicoBoletoMotMov" unique="false" 
        	service="lms.contasreceber.consultarHistoricoOcorrenciasBoletoAction.findPaginatedMotMov"
	        rowCountService="lms.contasreceber.consultarHistoricoOcorrenciasBoletoAction.getRowCountMotMov"
			selectionMode="none"	    
			disableMarkAll="true"
			onRowClick="doNothing"
			onRowDblClick="doNothing"

			gridHeight="25"
			title="motivoDaMovimentacao"
			
        	rows="2">
	        
		    <adsm:gridColumn dataType="integer"  title="numero" property="nrMotivoOcorrenciaBanco" />
     		<adsm:gridColumn dataType="text" title="descricao" property="dsMotivoOcorrencia" />
	    	
	    </adsm:grid>




        <adsm:grid  idProperty="idTarifaBoleto"  property="tarifaBoleto" unique="false" 
        	service="lms.contasreceber.consultarHistoricoOcorrenciasBoletoAction.findPaginatedTarBol"
	        rowCountService="lms.contasreceber.consultarHistoricoOcorrenciasBoletoAction.getRowCountTarBol"
			selectionMode="none"	    
			disableMarkAll="true"
			onRowClick="doNothing"
			onRowDblClick="doNothing"


			gridHeight="25"
			title="tarifas"


        	rows="2">
	        
		    <adsm:gridColumn dataType="text" isDomain="true" title="tipo" property="tipoOcorrencia" />
     		<adsm:gridColumn dataType="integer" title="numero" property="nrOcorrenciaBanco" />
	    	<adsm:gridColumn dataType="text" title="descricao" property="dsOcorrenciaBanco" width="30%" />
		    <adsm:gridColumn dataType="integer" title="valor" property="vlTarifaBoleto" />
    		<adsm:gridColumn dataType="JTDateTimeZone" title="dataHoraInclusao"  property="dhInclusao" />
	    </adsm:grid>



 	<adsm:buttonBar />

</adsm:window>

<script>
//metodo que vai chamar a grid e mandar ela executar

function myOnPageLoad_cb(data, errorMessage, errorCode, eventObj){

	if (errorCode != undefined) {
        alert(errorCode + ' ' + errorMessage);
        return false;
    }	
    
    setMasterLink(document, true);
 

	var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false }; // n?o deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.
	var storeSDO = createServiceDataObject("lms.contasreceber.consultarHistoricoOcorrenciasBoletoAction.findHistoricoOcorBoleto", "populaGrid",{idBoleto:getElementValue("idBoleto")});
	remoteCall.serviceDataObjects.push(storeSDO);
	xmit(remoteCall);	

}
//populando as grid
function populaGrid_cb(data, errorMessage, errorCode, eventObj){

	if (errorCode != undefined) {
        alert(errorCode + ' ' + errorMessage);
        return false;
    }	
    
    
	onDataLoad_cb(data, errorMessage, errorCode, eventObj);
	
	findButtonScript("tarifaBoleto", document.getElementById("histOcorrBole.form"));

	findButtonScript("historicoBoleto", document.getElementById("histOcorrBole.form"));

}

//metodo para que nada seja executado no clic da grid
function doNothing(){
return false;
}


//recarregando a grid Motivos da Movimentacao 
function loadMotMov(rowRef){
	
	historicoBoletoMotMovGridDef.executeSearch({idHistoricoBoleto:rowRef}, true);

	return false;
}

function showHistoricoAprovacao( id ){
	var data = historicoBoletoGridDef.getDataRowById(id);
	if(data.pendencia){
		var idPendencia = data.pendencia.idPendencia;
		var tpDocumento = data.tpDoctoServico.value;
		if(tpDocumento == "NFS" || tpDocumento == "NFT" ){
			showModalDialog('workflow/listarHistoricoPendencia.do?cmd=list&pendencia.idPendencia='+idPendencia,
					window,
					'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');
		} else {
			showModalDialog('questionamentoFaturas/consultarHistoricoQuestionamentoFaturas.do?cmd=list&idQuestionamentoFatura='+idPendencia,
							window,
							'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');
		}
	}

	
}

</script>