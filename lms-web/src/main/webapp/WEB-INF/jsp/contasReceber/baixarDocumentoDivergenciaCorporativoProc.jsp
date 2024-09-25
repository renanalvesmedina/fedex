<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myPageLoadCallBack">

	<adsm:form action="/contasReceber/baixarDocumentoDivergenciaCorporativo">
		
		<adsm:hidden property="documento.filial.pessoa.nmFantasia"/>
		<adsm:combobox property="documento.tpDocumento"
					   label="documento" 
					   width="30%"
					   labelWidth="20%"
					   service="lms.contasreceber.baixarDocumentoDivergenciaCorporativoAction.findTipoDocumentoServico"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   serializable="true"			   
					   onchange="return tpDocumentoServicoOnChange();"> 			

			<adsm:lookup dataType="text"
						 property="documento.filial"
					 	 idProperty="idFilial" 
					 	 criteriaProperty="sgFilial"
						 service="lms.contasreceber.baixarDocumentoDivergenciaCorporativoAction.findFilial"
						 disabled="true"
						 action=""
						 size="3" 
						 maxLength="3"
						 picker="false" 
						 onchange="return filialOnChange();"
						 onDataLoadCallBack="filialOnChange"						 
						 exactMatch="true">		
					<adsm:propertyMapping relatedProperty="documento.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>							 
			
			</adsm:lookup>
			
			<adsm:lookup dataType="integer"
						 property="documento"
						 idProperty="idDocumento" 
						 criteriaProperty="nrDocumento"
						 service=""
						 action=""
						 cmd="pesq"
						 size="11" 
						 maxLength="10" 
						 serializable="true" 
						 disabled="true" 
						 exactMatch="true"
						 onPopupSetValue="myOnPopupSetValue"
						 onDataLoadCallBack="myOnDataLoadDocumento"
						 onchange="return onChangeDocumento();"
						 mask="0000000000" required="true">
			</adsm:lookup>	
					 
		</adsm:combobox>
		
		<adsm:textbox dataType="JTDate"
					  property="dtLiquidacao" 
					  label="dataLiquidacao" 
					  serializable="true"
					  disabled="true"
					  required="true"
					  labelWidth="20%" 
					  width="30%"/>
		
		
		<adsm:textbox dataType="text"
					  property="sgFilialRelacaoCobranca" 
					  label="relacaoCobranca" 
					  serializable="true"
					  disabled="true"
					  required="true"
					  size="3"
					  labelWidth="20%" 
					  width="80%">
					  <adsm:textbox dataType="integer"
					  				size="10"
					  				serializable="true"
					  				mask="0000000000"
								    property="nrRelacaoCobranca" 
								    disabled="true"/>
					  
		</adsm:textbox>
				  
		<adsm:buttonBar>
			<adsm:button disabled="false" buttonType="storeButton" service="lms.contasreceber.baixarDocumentoDivergenciaCorporativoAction.executeBaixaDocumento" callbackProperty="baixarDoc" caption="baixar"/>
			<adsm:button disabled="false" id="limpar" buttonType="resetButton" onclick="limpaTela()" caption="limpar"/>
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>

<script>

	document.getElementById("documento.tpDocumento").required = true;
	document.getElementById("documento.filial.sgFilial").required = true;

/**
  * Função de callBack da página 
  */
function myPageLoadCallBack_cb(data, error){
	onPageLoad_cb(data, error);
	//setDisabled("relacaoCobranca.idRelacaoCobranca", false);
}
/*
 * On change da combo de Tipo de Documento de Serivo.<BR>
 * Altera lookup de conhecimento
 */
function tpDocumentoServicoOnChange(){
	resetValue("documento.filial.idFilial");
	resetDevedor();		
	setMaskNrDocumento();					
		
	if (getElementValue("documento.tpDocumento") == "FAT"){				

		document.getElementById("documento.idDocumento").service = "lms.contasreceber.baixarDocumentoDivergenciaCorporativoAction.findFatura";
		document.getElementById("documento.idDocumento").cmd = "list";
		document.getElementById("documento.idDocumento").url = contextRoot+"/contasReceber/manterFaturas.do";
		
		document.getElementById("documento.idDocumento").propertyMappings = [  
		{ modelProperty:"nrFatura", criteriaProperty:"documento.nrDocumento", inlineQuery:true, disable:false }, 
		{ modelProperty:"nrFatura", relatedProperty:"documento.nrDocumento", blankFill:true }, 
		{ modelProperty:"filialByIdFilial.idFilial", criteriaProperty:"documento.filial.idFilial", inlineQuery:true, disable:true }, 
		{ modelProperty:"filialByIdFilial.sgFilial", criteriaProperty:"documento.filial.sgFilial", inlineQuery:true, disable:true },
		{ modelProperty:"filialByIdFilial.idFilial", relatedProperty:"documento.filial.idFilial", blankFill:false },
		{ modelProperty:"filialByIdFilial.sgFilial", relatedProperty:"documento.filial.sgFilial", blankFill:false },
		{ modelProperty:"filialByIdFilial.pessoa.nmFantasia", criteriaProperty:"documento.filial.pessoa.nmFantasia", inlineQuery:true, disable:true },
		{ modelProperty:"filialByIdFilial.pessoa.nmFantasia", relatedProperty:"documento.filial.pessoa.nmFantasia", blankFill:false }			
		];			
	} else {
		document.getElementById("documento.idDocumento").service = "lms.contasreceber.baixarDocumentoDivergenciaCorporativoAction.findDevedorServDocFat";
		document.getElementById("documento.idDocumento").cmd = "pesq";
		document.getElementById("documento.idDocumento").url = contextRoot+"/contasReceber/pesquisarDevedorDocServFatLookUp.do";
		
		document.getElementById("documento.idDocumento").propertyMappings = [  
		{ modelProperty:"doctoServico.nrDoctoServico", criteriaProperty:"documento.nrDocumento", inlineQuery:true, disable:false }, 
		{ modelProperty:"doctoServico.nrDoctoServico", relatedProperty:"documento.nrDocumento", blankFill:true }, 
		{ modelProperty:"doctoServico.tpDocumentoServico", criteriaProperty:"documento.tpDocumento", inlineQuery:true, disable:true }, 
		{ modelProperty:"doctoServico.filialByIdFilialOrigem.idFilial", criteriaProperty:"documento.filial.idFilial", inlineQuery:true, disable:true }, 
		{ modelProperty:"doctoServico.filialByIdFilialOrigem.sgFilial", criteriaProperty:"documento.filial.sgFilial", inlineQuery:true, disable:true },
		{ modelProperty:"doctoServico.filialByIdFilialOrigem.sgFilial", relatedProperty:"documento.filial.sgFilial", blankFill:false },			
		{ modelProperty:"doctoServico.filialByIdFilialOrigem.idFilial", relatedProperty:"documento.filial.idFilial", blankFill:false },
		{ modelProperty:"doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", relatedProperty:"documento.filial.pessoa.nmFantasia", blankFill:false }						
		];
	}
	filialOnChange();		
	resetValue("documento.filial.idFilial");
}



/*
 * On Change da lookup de Filial
 */
function filialOnChange(){
	resetDevedor();
	return documento_filial_sgFilialOnChangeHandler();
}	

/**
 * On Change da lookup de Filial
 */
function resetDevedor(){
	resetValue("documento.idDocumento");
	resetValue("documento.nrDocumento");
	if (getElementValue("documento.tpDocumento") == "") {
		setDisabled("documento.filial.idFilial",true);
		setDisabled("documento.idDocumento",true);	
	} else {
		setDisabled("documento.filial.idFilial",false);
		habilitaLupa();
	}
	
	if (getElementValue("documento.filial.sgFilial") != ""){
		setDisabled("documento.idDocumento",false);		
	}
}			

/**
  * OnPopSetValue da lookup de documento
  */
function myOnPopupSetValue(data, dialogWindow){
	
	if (data.idFatura != null) {
		validateDocumento(data.idFatura);
		
		setElementValue("documento.idDocumento",data.idFatura);
		setElementValue("documento.nrDocumento",setFormat(getElement("documento.nrDocumento"),data.nrFatura));
		setElementValue("documento.filial.idFilial", data.filialByIdFilial.idFilial);
		setElementValue("documento.filial.sgFilial", data.filialByIdFilial.sgFilial);
		setElementValue("documento.filial.pessoa.nmFantasia", data.filialByIdFilial.pessoa.nmFantasia);
		findDadosRelacaoCobranca(data.idFatura, data.filialByIdFilial.sgFilial, "FAT");
		
		setDisabled("documento.idDocumento", false);
	} else if (data.idDevedorDocServFat != null) {	
		validateDocumento(data.idDevedorDocServFat);
			
		setElementValue("documento.idDocumento",data.idDevedorDocServFat);
		setElementValue("documento.nrDocumento",setFormat(getElement("documento.nrDocumento"),data.nrDoctoServico));
		setElementValue("documento.filial.idFilial",data.idFilialOrigem);
		setElementValue("documento.filial.sgFilial",data.sgFilialOrigem);
		findDadosRelacaoCobranca(data.idDevedorDocServFat, data.sgFilialOrigem, "DEV");
		
		setDisabled("documento.idDocumento", false);
	} else {
		habilitaLupa();	
	}
	
	return false;
}

/**
  * CallBack da lookup de documento
  */
function myOnDataLoadDocumento_cb(d,e){
	
	if (e == undefined && documento_nrDocumento_exactMatch_cb(d,e)) {
		if ((d.length == 1)){
			myOnPopupSetValue(d[0], undefined);
		}			
	}else{
		resetValue("dtLiquidacao");
		resetDocumento();
	}
}

/** 
  * Valida regras da fatura ou devedorDocServFat 
  */
function validateDocumento(idDocumento){
	var sdo;
	if (getElementValue("documento.tpDocumento") == "FAT"){	
		sdo = createServiceDataObject("lms.contasreceber.baixarDocumentoDivergenciaCorporativoAction.validateFatura",
			"validateDocumento", { idDocumento:idDocumento } );
	}else{
		sdo = createServiceDataObject("lms.contasreceber.baixarDocumentoDivergenciaCorporativoAction.validateDoctoServico",
			"validateDocumento", { idDocumento:idDocumento } );	
	}
	
	xmit({serviceDataObjects:[sdo]});
}

/**
  * Busca os dados da RelacaoCobranca
  */
function findDadosRelacaoCobranca(idDocumento, sgFilial, tpDoc){
	var dados = new Array();
	
	setNestedBeanPropertyValue(dados, "idDocumento",idDocumento);
	setNestedBeanPropertyValue(dados, "sgFilial",sgFilial);
	setNestedBeanPropertyValue(dados, "tpDoc",tpDoc);
	
	var remoteCall = {serviceDataObjects:new Array()};
    var dtLiquidacao = createServiceDataObject("lms.contasreceber.baixarDocumentoDivergenciaCorporativoAction.findDadosRelacaoCobranca", "findDadosRelacaoCobranca", dados);
    remoteCall.serviceDataObjects.push(dtLiquidacao);
    xmit(remoteCall);
	
}

/**
  * Função de callback findDadosRelacaoCobranca
  */
function findDadosRelacaoCobranca_cb(data, error){
	if(error == undefined){
		setElementValue("dtLiquidacao", setFormat("dtLiquidacao", data.dtLiquidacao));
		setElementValue("nrRelacaoCobranca", setFormat("nrRelacaoCobranca", data.nrRelacaoCobranca));
		setElementValue("sgFilialRelacaoCobranca", data.sgFilialRelacaoCobranca);
	}else{
		resetDocumento();
		resetValue("dtLiquidacao");
		resetValue("nrRelacaoCobranca");
		resetValue("sgFilialRelacaoCobranca");
		alert(error);
	}
	
	return false;
}

/** 
  * CallBack validateDocumento
  */
function validateDocumento_cb(data, error){
	if(error != undefined){
		alert(error);
		resetDocumento();
	}
}

function resetDocumento(){
	resetValue("documento.idDocumento");
	resetValue("documento.nrDocumento");
	resetValue("dtLiquidacao");
	resetValue("sgFilialRelacaoCobranca");
	resetValue("nrRelacaoCobranca");
	
	setFocus("documento.nrDocumento");
}

function onChangeDocumento(){
	documento_nrDocumentoOnChangeHandler();
}	

/**
  * Coloca mascara no nrDocumento
  */
function setMaskNrDocumento(){
	var obj = getElement("documento.nrDocumento");
	
	switch(getElementValue("documento.tpDocumento")) {
	case "CRT":
		obj.maxLength = 6;
		obj.mask = "000000";
		break;
	case "CTR":
		obj.maxLength = 8;
		obj.mask = "00000000";
		break;
	case "NDN":
		obj.maxLength = 10;
		obj.mask = "0000000000";
		break;
	case "NFS":
		obj.maxLength = 6;
		obj.mask = "000000";
		break;		
	case "NFT":
		obj.maxLength = 8;
		obj.mask = "00000000";			
		break;
	case "FAT":
		obj.maxLength = 10;
		obj.mask = "0000000000";			
		break;
	case "NDI":
		obj.maxLength = 10;
		obj.mask = "0000000000";			
		break;					
	case "ROF":
		obj.maxLength = 10;
		obj.mask = "0000000000";			
		break;			
	}
}

/*
 *	Habilita a lupa de documento de serviço mesmo com o número do documento desabilitado
 */
function habilitaLupa() {
	setDisabled("documento.idDocumento", false);
	setDisabled("documento.nrDocumento", true);
}	 

/*
 * On Change callBack da lookup de Filial
 */	
function filialOnChange_cb(data,e,c,x){
	resetDevedor();
	if (data.length == 1) {			
		setDisabled("documento.idDocumento",false);

		__lookupSetValue({e:getElement("documento.filial.idFilial"), data:data[0]});
		return true;
	} else {
		alert(lookup_noRecordFound);
	}			
}

/**
  * Função responsável por limpar a tela 
  */
function limpaTela(){

	cleanButtonScript(document.form);
	
	setDisabled("documento.idDocumento", true);
	setDisabled("documento.nrDocumento", true);
	setDisabled("documento.filial.idFilial", true);
	
	
}

function baixarDoc_cb(data, error){
	if(error != undefined){
		alert(error);

	}else{
		limpaTela();
		showSuccessMessage();		
		setFocusOnFirstFocusableField(document)
	}
}

</script>
