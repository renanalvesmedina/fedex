<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.consultarDadosCobrancaDocumentoServicoAction" onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/contasReceber/consultarDadosCobrancaDocumentoServico">

		<adsm:hidden property="idDevedorDocServFat"/>
		
		<adsm:textbox 
					 dataType="text"
					 disabled="true"
					 property="filialOrigem" 
					 label="filialOrigem" 
					 labelWidth="20%" 
					 size="4"
					 width="3%">
					 
					 <adsm:textbox 
								 dataType="text"
								 disabled="true"
								 property="filialOrigemNome" 
								 width="27%"/>
					 
		</adsm:textbox>

		<adsm:textbox label="numeroTransferencia" dataType="integer" property="nrTransferencia" labelWidth="20%" width="30%" disabled="true"  />

		<adsm:textbox 
					 dataType="text"
					 disabled="true"
					 property="filialDestino" 
					 label="filialDestino" 
					 labelWidth="20%" 
					 size="4"
					 width="3%">
					 
					 <adsm:textbox 
								 dataType="text"
								 disabled="true"
								 property="filialDestinoNome" 
								 width="27%"/>
					 
		</adsm:textbox>
		
		<adsm:textbox 
					 dataType="text"
					 disabled="true"
					 property="tpSituacaoTransferencia" 
					 label="situacaoTransferencia" 
					 labelWidth="20%" 
					 width="30%">
					 
		</adsm:textbox>
		
		<adsm:textbox 
					 dataType="JTDate"
					 disabled="true"
					 picker="false"
					 property="dtEmissao" 
					 label="dataEmissao" 
					 labelWidth="20%" 
					 width="80%">
					 
		</adsm:textbox>

      	<adsm:textbox 
					 dataType="text"
					 disabled="true"
					 property="identificacaoAntiga" 
					 label="responsavelAnterior" 
					 size="20"
					 labelWidth="20%" 
					 width="10%">
					 
					<adsm:textbox 
								 dataType="text" 
								 disabled="true"
								 property="nmPessoaAntiga"
								 size="50" 
								 width="70%"/>
		
		</adsm:textbox>
		
		<adsm:textbox 
					 dataType="text"
					 disabled="true"
					 property="identificacaoNova" 
					 size="20"
					 label="novoResponsavel" 
					 labelWidth="20%" 
					 width="10%">
					 
					<adsm:textbox 
								 dataType="text" 
								 disabled="true"
								 property="nmPessoaNova"
								 size="50" 
								 width="70%"/>
		
		</adsm:textbox>
		
		<adsm:textbox 
					 dataType="text"
					 disabled="true"
					 property="dsMotivoTransferencia" 
					 label="motivo" 
					 labelWidth="20%" 
					 width="80%">
					 
		</adsm:textbox>
		
		<adsm:textarea 
					 maxLength="500"
					 disabled="true"
					 property="obItemTransferencia" 
					 label="observacao" 
					 columns="70"
					 rows="3"
					 labelWidth="20%" 
					 width="80%">
					 
		</adsm:textarea>
      	
    </adsm:form>
    
	<adsm:grid 
			  property="itemTransferencia"
			  idProperty="idItemTransferencia"
			  service="lms.contasreceber.consultarDadosCobrancaDocumentoServicoAction.findByIdDevedorDocServFat"
			  rowCountService="lms.contasreceber.consultarDadosCobrancaDocumentoServicoAction.getRowCountByDevedorDocServFat"
			  selectionMode="none" 
			  onRowClick="populateDetaiTransferencia"
			  showRowIndex="false"
			  showGotoBox="false" 
			  showPagging="false"
			  showTotalPageCount="false"
			  unique="true" 
			  scrollBars="vertical"
			  gridHeight="235"
			 >	
			  
		<adsm:gridColumn width="25%" title="filialOrigem" property="filialOrigem"/>
		<adsm:gridColumn width="25%" title="numeroTransferencia" align="right" property="nrTransferencia" />
		<adsm:gridColumn width="25%" title="filialDestino" property="filialDestino"/>
		<adsm:gridColumn width="25%" title="novoResponsavel" property="identificacao"/>
		

	
	<adsm:buttonBar>
		<adsm:button caption="fechar" id="btFechar" disabled="false" onclick="javascript:window.close();"/>
	</adsm:buttonBar>

	</adsm:grid>
	
</adsm:window>

<script>

	function populateDetaiTransferencia(id){
	
		var dados = new Array();
		setNestedBeanPropertyValue(dados, "idItemTransferencia", id);
	    var sdo = createServiceDataObject("lms.contasreceber.consultarDadosCobrancaDocumentoServicoAction.findDadosTransferencia",
	                                         "populateDetaiTransferencia",
	                                         dados);
	   xmit({serviceDataObjects:[sdo]});
	
	   return false;
	
	}
	
	function populateDetaiTransferencia_cb(data){
		newButtonScript(document);
		onDataLoad_cb(data);
		setDisabled("btFechar",false);
	
	}
	
	function myOnPageLoadCallBack_cb(data, error, o){
		onPageLoad_cb(data);
		itemTransferencia_cb(
			{
				idDevedorDocServFat:getElementValue("idDevedorDocServFat")
			}
		);
	
	}
	
	setMasterLink(document, true);
</script>
