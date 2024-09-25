<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.consultarDadosCobrancaDocumentoServicoAction">

	<adsm:form action="/contasReceber/consultarDadosCobrancaDocumentoServico" id="idDevedorDocServFat">
	
		<adsm:masterLink showSaveAll="false" idProperty="idDoctoServico">
		
			<adsm:masterLinkItem label="documentoServico" property="doctoServico" />
			<adsm:masterLinkItem label="moeda" property="siglaSimbolo"/>			
			
		</adsm:masterLink>
		
		<adsm:hidden property="idFatura"/>
		
		<adsm:hidden property="idBdm"/>
		
		<adsm:hidden property="idRelCob"/>
		
		<adsm:textbox dataType="text" disabled="true" property="identificacao" label="responsavel" labelWidth="15%" 
					  size="20" width="75%">
			<adsm:textbox dataType="text" disabled="true" property="nmPessoa" size="50" />
		</adsm:textbox>
		
		<adsm:textbox 
					 dataType="text"
					 disabled="true"
					 property="dsDivisaoCliente" 
					 label="divisao" 
					 size="60"
					 labelWidth="15%" 
					 width="85%"/>		
					 
		<adsm:textbox 
					 dataType="text"
					 disabled="true"
					 property="dsEndereco" 
					 label="endereco" 
					 size="100"
					 labelWidth="15%" 
					 width="85%"/>
					 
		<adsm:textbox 
					 dataType="text" 
					 property="dsBairro" 
					 disabled="true"
					 label="bairro"
					 labelWidth="15%"
					 size="30"
					 width="35%"/>
					 
		<adsm:textbox 
					 dataType="text" 
					 disabled="true"
					 property="nmMunicipio" 
					 label="municipio"
					 labelWidth="15%"
					 size="35"
					 width="30%"/>
								 
		<adsm:textbox 
					 dataType="text" 
					 disabled="true"
					 property="sgUnidadeFederativa" 
					 label="uf"
					 labelWidth="15%"
					 size="30"
					 width="35%"/>
					 
		<adsm:textbox 
					 dataType="text" 
					 disabled="true"
					 property="nmPais" 
					 label="pais"
					 labelWidth="15%"
					 size="30"
					 width="35%"/>
					 
		<adsm:textbox 
					 dataType="text" 
					 property="nrCep" 
					 disabled="true"
					 label="cep"
					 labelWidth="15%"
					 size="30"
					 width="35%"/>
					 
		<adsm:textbox 
					 dataType="text" 
					 disabled="true"
					 property="telefoneEndereco" 
					 label="telefone"
					 labelWidth="15%"
					 size="30"
					 width="35%"/>
					 
		<adsm:textbox 
					 dataType="JTDate" 
					 disabled="true"
					 picker="false"
					 property="dtVencimento" 
					 label="dataVencimento"
					 labelWidth="15%"
					 size="30"
					 width="35%"/>
		
		<adsm:textbox 
					 dataType="JTDate" 
					 disabled="true"
					 picker="false"
					 property="dtLiquidacao" 
					 label="dataLiquidacao"
					 labelWidth="15%"
					 size="30"
					 width="35%"/>
					 
		<adsm:textbox 
					 dataType="text" 
					 disabled="true"
					 property="filialCobranca" 
					 label="filialCobranca"
					 labelWidth="15%"
					 width="18%"/>
					 
		<adsm:textbox 
					 dataType="text" 
					 disabled="true"
					 property="tpSituacaoCobranca" 
					 label="situacaoCobranca"
					 labelWidth="15%"
					 width="18%"/>
	
		<adsm:textbox 
					 dataType="text" 
					 disabled="true"
					 property="fatura" 
					 label="fatura"
					 labelWidth="15%"
					 width="18%"/>
					 
		<adsm:textbox 
					 dataType="text" 
					 property="redeco" 
					 disabled="true"
					 label="redeco"
					 labelWidth="15%"
					 width="18%"/>

		<adsm:textbox 
					 dataType="text" 
					 property="finalidadeRedeco" 
					 disabled="true"
					 label="finalidadeRedeco"
					 labelWidth="15%"
					 width="18%"/>
					 
		<adsm:textbox 
					 dataType="text" 
					 disabled="true"
					 property="boleto" 
					 label="boleto"
					 labelWidth="15%"
					 width="18%"/>
					 
		<adsm:textbox 
					 dataType="text" 
					 disabled="true"
					 property="recibo" 
					 label="recibo"
					 labelWidth="15%"
					 width="18%"/>
					 
		<adsm:textbox 
					 dataType="text" 
					 property="relacaoCobranca" 
					 disabled="true"
					 label="relacaoCobranca"
					 labelWidth="15%"
					 width="18%"/>
				 
		<adsm:textbox 
					 dataType="currency" 
					 property="vlDevido" 
					 disabled="true"
					 label="valorDevido"
					 labelWidth="15%"
					 width="18%"/>
				 
		<adsm:textbox 
					 dataType="text" 
					 property="dsMotivoDesconto" 
					 disabled="true"
					 label="motivoDesconto"
					 labelWidth="15%"
					 width="18%"/>
					 
		<adsm:textbox 
					 dataType="currency" 
					 disabled="true"
					 property="vlDesconto" 
					 label="valorDesconto"
					 labelWidth="15%"
					 width="18%"/>					 
				 
		<adsm:textbox 
					 dataType="currency" 
					 property="valorPago" 
					 disabled="true"
					 label="valorPago"
					 labelWidth="15%"
					 width="18%"/>
				 
		<adsm:textbox 
					 dataType="text" 
					 property="tpMotivoDesconto" 
					 disabled="true"
					 label="tipoDesconto"
					 labelWidth="15%"
					 width="18%"/>
				 
		<adsm:textbox 
					 dataType="text" 
					 property="tpSituacaoAprovacao" 
					 label="situacaoAprovacaoDesconto"
					 disabled="true"
					 labelWidth="15%"
					 width="18%"/>
				 
		<adsm:textbox 
					 dataType="text" 
					 property="nrNotaDebitoNac" 
					 disabled="true"
					 label="notaDebito"
					 labelWidth="15%"
					 width="18%"/>
					 
		<adsm:textbox 
					 dataType="text"
					 property="doctoDesconto" 
					 disabled="true"
					 label="doctoDesconto" 
					 labelWidth="15%" 
					 width="18%"/>					 
				 
		<adsm:textbox 
					 dataType="text"
					 property="bdm" 
					 disabled="true"
					 label="BDM" 
					 labelWidth="15%" 
					 width="18%"/>
					 
		<adsm:textbox 
					 dataType="currency"
					 property="recebimentosParciais" 
					 disabled="true"
					 label="recebimentosParciais" 
					 labelWidth="15%" 
					 width="18%"/>
					 
		<adsm:textbox 
					 dataType="currency"
					 property="saldoDevedor" 
					 disabled="true"
					 label="valorSaldoDevedor" 
					 labelWidth="15%" 
					 width="18%"/>
					 
					 
		<adsm:hidden property="nrFat"/>
		<adsm:hidden property="idFilialFat"/>
		<adsm:hidden property="sgFilialFat"/>
		<adsm:hidden property="nmFantasiaFat"/>
		<adsm:hidden property="nrRc"/>
		<adsm:hidden property="idFilialRc"/>
		<adsm:hidden property="sgFilialRc"/>
		<adsm:hidden property="nmFantasiaRc"/>
		<adsm:hidden property="nrBdm"/>
		<adsm:hidden property="idFilialBdm"/>
		<adsm:hidden property="sgFilialBdm"/>
		<adsm:hidden property="nmFantasiaBdm"/>
		
		<adsm:hidden property="tpSituacaoCobrancaHidden"/>
		
		<adsm:buttonBar freeLayout="true">
		
	        <adsm:button caption="transferencias" id="tranferencias" onclick="openTransferencia();"/>
            
            
            <adsm:button caption="documentosCobranca" id="documentosCobranca" onclick="navegarDocumentosServico();"/>
	           
	   	</adsm:buttonBar>
    
    </adsm:form>
    		
	<adsm:grid 
			  property="devedorDocServFat"
			  idProperty="idDevedorDocServFat"
			  scrollBars="vertical" 
			  service="lms.contasreceber.consultarDadosCobrancaDocumentoServicoAction.findDevedorDocServFatByDoctoServico"
			  rowCountService="lms.contasreceber.consultarDadosCobrancaDocumentoServicoAction.getRowCountByDevedorDocServFat"
			  selectionMode="none" 
			  detailFrameName="devedores"
			  onRowClick="populateDetaiDevedorDocServFat"
			  showRowIndex="false"
			  showGotoBox="false" 
			  showPagging="false"
			  showTotalPageCount="false"
			  gridHeight="60" 
			  unique="true" 
			  rows="2"
			  onDataLoadCallBack="populateDetaiDevedorDocServFatOnStart">
	
		<adsm:gridColumn width="80%" title="responsavel" property="identificacao" dataType="text"/>
		
		<adsm:gridColumn width="20%" title="valorDevido" property="vlDevido" dataType="currency"/>
		
	</adsm:grid>
	
	
</adsm:window>


<script>

function initWindow(eventObj){
	if(eventObj.name == "tab_click"){
		var nome = document.getElementById("nmPessoa");
		var tpSituacao = document.getElementById("tpSituacaoCobrancaHidden").value;

		if (nome.value.length == 0){			
			setDisabled("tranferencias", true);
			setDisabled("documentosCobranca", true);
		}else{
            if (tpSituacao != "F" && tpSituacao !="L"){
                setDisabled("documentosCobranca", true);
            }
		}
					
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		var telaCad = tabCad.tabOwnerFrame;	
	
		
		devedorDocServFat_cb( {	idDoctoServico:telaCad.document.getElementById("idDoctoServico").value	 } );
	}
}

function populateDetaiDevedorDocServFat(id){

	cleanButtonScript(document);

	setElementValue("idDevedorDocServFat", id);
	
	var dados = new Array();
	setNestedBeanPropertyValue(dados, "idDevedorDocServFat", id);
    var sdo = createServiceDataObject("lms.contasreceber.consultarDadosCobrancaDocumentoServicoAction.findDevedorDocServFatDetail",
                                         "populateDetaiDevedorDocServFat",
                                         dados);
   xmit({serviceDataObjects:[sdo]});
	
	return false;
}

function populateDetaiDevedorDocServFatOnStart_cb(data , error){
	if (data[0]!=undefined){
		cleanButtonScript(document);

		setElementValue("idDevedorDocServFat", data[0].idDevedorDocServFat);
		
		var dados = new Array();
		setNestedBeanPropertyValue(dados, "idDevedorDocServFat", data[0].idDevedorDocServFat);
	    var sdo = createServiceDataObject("lms.contasreceber.consultarDadosCobrancaDocumentoServicoAction.findDevedorDocServFatDetail",
	                                         "populateDetaiDevedorDocServFat", dados);
	    
	    xmit({serviceDataObjects:[sdo]});
		
		return false;
	}
}

function navegarDocumentosServico(){
	
	if(getElementValue("tpSituacaoCobrancaHidden") == "L"){
		
		if(getElementValue("idBdm") != ""){
			
				var url = 'contasReceber/manterBDM.do?cmd=main';
				url = url + '&idBdm=' + getElementValue("idBdm");
				url = url + '&nrBdm=' + getElementValue("nrBdm");
				url = url + '&filialEmissora.idFilial=' + getElementValue("idFilialBdm");
				url = url + '&filialEmissora.sgFilial=' + getElementValue("sgFilialBdm");
				url = url + '&filialEmissora.pessoa.nmFantasia=' + getElementValue("nmFantasiaBdm");

				parent.parent.redirectPage(url);
		}else if(getElementValue("idRelCob") != ""){
			
				var url = 'contasReceber/pesquisarRelacoesCobranca.do?cmd=main';
				url = url + '&idRelacaoCobranca=' + getElementValue("idRelCob");
				url = url + '&nrRelacaoCobrancaFilial=' + getElementValue("nrRc");				
				url = url + '&filial.idFilial=' + getElementValue("idFilialRc");
				url = url + '&filial.sgFilial=' + getElementValue("sgFilialRc");
				url = url + '&filial.pessoa.nmFantasia=' + getElementValue("nmFantasiaRc");

				parent.parent.redirectPage(url);
		}
		
	}else if(getElementValue("tpSituacaoCobrancaHidden") == "F"){
		if (getElementValue("idFatura") != "") {
				
				var url = 'contasReceber/manterFaturas.do?cmd=main';
				url = url + '&idFatura=' + getElementValue("idFatura");
				
				url = url + '&nrFatura=' + getElementValue("nrFat");				
				url = url + '&filialByIdFilialCobradora.idFilial=' + getElementValue("idFilialFat");
				url = url + '&filialByIdFilialCobradora.sgFilial=' + getElementValue("sgFilialFat");
				url = url + '&filialByIdFilialCobradora.pessoa.nmFantasia=' + getElementValue("nmFantasiaFat");				
				url = url + '&filialByIdFilial.idFilial=' + getElementValue("idFilialFat");
				url = url + '&filialByIdFilial.sgFilial=' + getElementValue("sgFilialFat");
				url = url + '&filialByIdFilial.pessoa.nmFantasia=' + getElementValue("nmFantasiaFat");				

				parent.parent.redirectPage(url);
		}
	}
}

function populateDetaiDevedorDocServFat_cb(data){
	onDataLoad_cb(data);
	
	if(data.tpSituacaoCobrancaHidden != "F" && data.tpSituacaoCobrancaHidden != "L"){
		setDisabled("documentosCobranca", true);
	} else {
		setDisabled("documentosCobranca", false);
	}
	
	

}

function openTransferencia(){
  	var url = '/contasReceber/consultarDadosCobrancaDocumentoServico.do?cmd=transferencia&idDevedorDocServFat=' + getElementValue('idDevedorDocServFat');
	showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');
}



</script>