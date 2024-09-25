<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">

	<adsm:form action="/sim/consultarLocalizacoesMercadorias" id="idDevedorDocServFat" height="165">
	
		
		<adsm:hidden property="idDoctoServico"/>
		
		<adsm:hidden property="idFatura"/>
		
		<adsm:hidden property="idBdm"/>
		
		<adsm:hidden property="idRelCob"/>
		
		<adsm:textbox dataType="text" disabled="true" property="identificacao" label="responsavel" 
		 			  labelWidth="15%" size="20" width="15%">
					<adsm:textbox dataType="text" disabled="true" property="nmPessoa"
								  size="50" width="65%"/>
		
		</adsm:textbox>
		
		<adsm:textbox 
					 dataType="text"
					 disabled="true"
					 property="dsEndereco" 
					 label="endereco" 
					 size="60"
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
	
		<adsm:textbox dataType="text" disabled="true" property="fatura" label="fatura" labelWidth="15%" width="18%" />
					 
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
					 dataType="integer" 
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
					 width="35%"/>
		
		    
    </adsm:form>
    		
	<adsm:grid property="devedorDocServFat" 
			   idProperty="idDevedorDocServFat" 
			   scrollBars="vertical" 
			   service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedDevedorDocServFatByDoctoServico"
			   selectionMode="none" detailFrameName="cobranca" onRowClick="populateDetaiDevedorDocServFat" showRowIndex="false" showGotoBox="false" 
			   showPagging="false" showTotalPageCount="false" gridHeight="30" unique="true" rows="2" onDataLoadCallBack="myOnDataLoadGrid">	
		<adsm:gridColumn width="400" title="responsavel" property="identificacao" dataType="text"/>
		<adsm:gridColumnGroup customSeparator=" " >
				<adsm:gridColumn title="valorDevido" property="sgMoeda" width="50" />
				<adsm:gridColumn title="" property="dsSimbolo" dataType="text" width="0" />
	    </adsm:gridColumnGroup>
		<adsm:gridColumn width="250" title="" property="vlDevido" dataType="currency"/>
	</adsm:grid>
	
	
</adsm:window>


<script>
function findPaginatedDevedor(){
	var idDoctoServico =  parent.document.getElementById("idDoctoServico").value;
	var data = new Array();
	setNestedBeanPropertyValue(data,"idDoctoServico", idDoctoServico);
	devedorDocServFatGridDef.executeSearch(data);
}

function populateDetaiDevedorDocServFat(id){

	cleanButtonScript(document);

	setElementValue("idDevedorDocServFat", id);
	
	var dados = new Array();
	setNestedBeanPropertyValue(dados, "idDevedorDocServFat", id);
    var sdo = createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findDevedorDocServFatDetail",
                                         "onDataLoad",
                                         dados);
   xmit({serviceDataObjects:[sdo]});

	return false;
}

function myOnDataLoadGrid_cb(data, errorMessage){
	if(data.length > 0){
		var id = getNestedBeanPropertyValue(data,":0.idDevedorDocServFat");
		populateDetaiDevedorDocServFat(id);
	}
}







</script>