<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterRedecoAction">
	<adsm:form action="/contasReceber/manterRedeco" idProperty="idItemRedeco"
		service="lms.contasreceber.manterRedecoAction.findByIdItemRedeco"
		onDataLoadCallBack="myOnDataLoad">

		<adsm:masterLink idProperty="idRedeco" showSaveAll="true">
			<adsm:masterLinkItem label="numeroRedeco" property="nrRedeco" itemWidth="30" boxWidth="80"/>
			<adsm:masterLinkItem label="situacao" property="tpSituacaoRedeco" itemWidth="70"  boxWidth="80"/>
			<adsm:masterLinkItem label="dataEmissao" property="dtEmissao" itemWidth="50" boxWidth="80"/>			
		</adsm:masterLink>
		
		<adsm:hidden property="tipoFinalidade" serializable="true"/>
		<adsm:hidden property="dtRecebimento" serializable="true"/>

		<adsm:combobox property="documento.tpDocumento"
			   label="documentoServico" 
			   width="33%"
			   labelWidth="20%"
			   service="lms.contasreceber.manterRedecoAction.findComboTpDocumento"
			   optionProperty="value" 
			   optionLabelProperty="description"
			   serializable="true"		   
			   onchange="return tpDocumentoServicoOnChange();"> 			

			<adsm:lookup dataType="text"
						 property="documento.filial"
					 	 idProperty="idFilial" 
					 	 criteriaProperty="sgFilial"
						 service="lms.contasreceber.manterRedecoAction.findFilial"
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
						 popupLabel="pesquisarDocumentoServico"
						 mask="0000000000" required="true">
			</adsm:lookup>			 
		</adsm:combobox>
		
		<adsm:hidden property="documento.filial.pessoa.nmFantasia"/>

		<adsm:textbox dataType="currency" property="vlRecebido" label="totalRecebido" disabled="true" labelWidth="20%" width="27%"/>
		<adsm:textbox dataType="currency" property="vlJuros" label="jurosRecebidos" labelWidth="20%" width="33%" onchange="onChangeVlJuros();" minValue="0"/>
		<adsm:textbox dataType="currency" property="vlDesconto" label="totalDescontos" disabled="true" labelWidth="20%" width="27%" minValue="0"/>
		<adsm:textbox dataType="currency" property="vlTarifa" label="totalTarifas" labelWidth="20%" width="33%" onchange="calcularValorTotalRecebido();" minValue="0"/>
        <adsm:textbox dataType="currency" property="vlDocumento" label="valorDocumento" disabled="true" labelWidth="20%" width="27%" minValue="0"/>
		<adsm:textbox dataType="currency" property="vlJurosCalculados" label="jurosCalculado" labelWidth="20%" width="33%" disabled="true" minValue="0"/>
		
		<adsm:textbox dataType="currency" property="vlSaldo" label="valorSaldo" disabled="true" labelWidth="20%" width="27%" minValue="0"/>
		
		<adsm:textbox dataType="currency" property="vlDiferencaCambialCotacao" label="diferencaCambialCotacao" labelWidth="20%" width="33%" disabled="true"/>
		<adsm:textbox dataType="currency" property="vlDiferencaJuros" label="diferencaJuros" labelWidth="20%" width="27%" disabled="true"/>
        	
    	<adsm:textbox dataType="text" property="moeda.siglaSimbolo" label="moeda" disabled="true" labelWidth="20%" width="33%"/>			
		<adsm:textarea property="obItemRedeco" label="observacao" maxLength="500" columns="46" rows="1" labelWidth="20%" width="80%"/>


        <adsm:section caption="totaisRedeco" />
		<adsm:textbox dataType="currency" property="qtTotalDocumentos" label="quantidadeDocumentos" labelWidth="23%" width="30%" disabled="true"/>
		<adsm:textbox dataType="currency" property="vlTotalRecebido" label="valorTotalRecebido" labelWidth="20%" width="27%" disabled="true"/>

		<adsm:hidden property="dtEmissao"/>
		<adsm:hidden property="tpAbrangencia"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button id="fatura" caption="fatura" action="/contasReceber/manterFaturas" cmd="main" boxWidth="70" disabled="true">
				<adsm:linkProperty src="documento.filial.sgFilial" target="filialByIdFilial.sgFilial"/>
				<adsm:linkProperty src="documento.filial.idFilial" target="filialByIdFilial.idFilial"/>				
				<adsm:linkProperty src="documento.filial.pessoa.nmFantasia" target="filialByIdFilial.pessoa.nmFantasia"/>
				<adsm:linkProperty src="documento.nrDocumento" target="nrFatura"/>				
			</adsm:button>
			<adsm:button buttonType="storeButton" caption="salvarDocumento" onclick="myStoreButton();" id="storeButton"/>
			<adsm:button buttonType="newButton" caption="novoDocumento" onclick="newButtonScript();" />			
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idItemRedeco" property="itemRedeco"
		unique="true" autoSearch="false" rows="2" showGotoBox="true"
		showPagging="true"
		detailFrameName="itens"
		onDataLoadCallBack="myItemRedeco"
		service="lms.contasreceber.manterRedecoAction.findPaginatedItemRedeco"
		rowCountService="lms.contasreceber.manterRedecoAction.getRowCountItemRedeco">	
		
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="documentoServico" property="documento.tpDocumento" width="67" />
			<adsm:gridColumn title="" property="documento.filial.sgFilial" width="34" />
			<adsm:gridColumn title="" property="documento.nrDocumento" width="33" dataType="integer" mask="0000000000"/>
		</adsm:gridColumnGroup>		
		<adsm:gridColumn width="14%" title="valorDocumento" property="vlDocumento" dataType="currency"/>
		<adsm:gridColumn width="12%" title="valorSaldo" property="vlSaldo" dataType="currency"/>
		<adsm:gridColumn width="12%" title="jurosCalculado" property="vlJurosCalculados" dataType="currency"/>
		<adsm:gridColumn width="12%" title="jurosRecebidos" property="vlJuros" dataType="currency"/>
		<adsm:gridColumn width="12%" title="totalDescontos" property="vlDesconto" dataType="currency"/>
		<adsm:gridColumn width="12%" title="totalRecebido" property="vlRecebido" dataType="currency"/>		
		<adsm:gridColumn width="" title="moeda" property="moeda.siglaSimbolo" dataType="text"/>		
		<adsm:buttonBar> 
			<adsm:removeButton caption="excluirDocumento" id="removeButton"
					service="lms.contasreceber.manterRedecoAction.removeByIdsItemRedeco" />
		</adsm:buttonBar>		
	</adsm:grid>

</adsm:window>

<script>

var tpFinalidade;

document.getElementById("qtTotalDocumentos").masterLink = "true";	
document.getElementById("vlTotalRecebido").masterLink = "true";
document.getElementById("dtEmissao").masterLink = "true";	
document.getElementById("dtRecebimento").masterLink = "true";	
document.getElementById("tpAbrangencia").masterLink = "true";

getElement("documento.tpDocumento").required = "true";
getElement("documento.filial.sgFilial").required = "true";

/**
 *
 *
 *
 * FUNCTIONS QUE CARREGA A TELA
 *
 *
 *
 *
 */

function myOnDataLoad_cb(d,e,c,x){
	onDataLoad_cb(d,e,c,x);
	disableFields();
	setMaskNrDocumento();
	setElementValue("documento.nrDocumento", setFormat("documento.nrDocumento", new Number(getElementValue("documento.nrDocumento")).toString()));	
	setDisabled("documento.tpDocumento", true);	
	setDisabled("documento.filial.idFilial", true);	
	setDisabled("documento.idDocumento", true);	
	setFocusOnFirstFocusableField();
}

function setaTipoDocumentoPadrao(){
	setDisabled("documento.tpDocumento", false);	
	setElementValue('documento.tpDocumento','FAT');
	tpDocumentoServicoOnChange();
}

function initWindow(eventObj){
	var frameCad = getTabGroup(this.document).getTab("cad").tabOwnerFrame;

	setElementValue('tipoFinalidade',frameCad.getElementValue('tpFinalidade'));
	
	if (eventObj.name == "tab_click"){
	
		if (frameCad.getElementValue("blMatriz") == "true"){
			setDisabled("vlTarifa", false);
		} else {
			setDisabled("vlTarifa", true);
		}
		
		if (eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq") {
			newButtonScript();
			frameCad.newButtonScript();
			setDisabled("documento.tpDocumento", false);
			setDisabled("documento.filial.idFilial", false);
			habilitaLupa();
		} else {
			setElementValue("tpAbrangencia", frameCad.getElementValue("tpAbrangencia"));
			setElementValue("dtEmissao", frameCad.getElementValue("dtEmissao"));				
			setElementValue("dtRecebimento", frameCad.getElementValue("dtRecebimento"));			
			setaTipoDocumentoPadrao();
		}
	}	

	if (eventObj.name == "newButton_click" || eventObj.name == "removeButton_grid" || eventObj.name == "storeItemButton"){
		setaTipoDocumentoPadrao();
		setFocusOnFirstFocusableField();
		setDisabled("documento.tpDocumento", false);
		setDisabled("documento.filial.idFilial", false);
		habilitaLupa();
	}
	
	if( getElementValue('documento.idDocumento') != ''){
		setDisabled('fatura',false);
	} else {
		setDisabled('fatura',true);	
	}	
}

function myStoreButton(){
	var tabGroup = getTabGroup(this.document);
	var tabCad = tabGroup.getTab("cad");
	var telaCad = tabCad.tabOwnerFrame;	
	var elemTab = getTab(telaCad.document, false);	
	var elements = new Array(telaCad.document.getElementById("dtEmissao"), telaCad.document.getElementById("tpAbrangencia"), telaCad.document.getElementById('dtRecebimento'));
		
	for (var i = 0; i < elements.length; i++){	
		var currentElem = elements[i];
		if (currentElem.required != null) {
			if (currentElem.required == 'true' && trim(currentElem.value.toString()).length == 0) {
				if(currentElem == '[object]') {				
					if ((currentElem.label != undefined)) {
						alert(getMessage(erRequired, new Array(currentElem.label)));
					}
					if (elemTab != null) {
						// se a aba do elemento n?o estiver selecionada, muda a aba
						// para a correta.
						if (elemTab.properties.id != tabGroup.selectedTab.properties.id) {
							var eventObj = {name:"tab_click", src:elemTab}; 	 // simula um clique na aba, para garantir funcionamento
							tabGroup.selectTab(elemTab.properties.id, eventObj); // uniforme das rotinas de habilita??o de botoes.
						}
					}
					setFocus(currentElem);
					return false;
				}
			}
		}
	}
	setElementValue('dtRecebimento',telaCad.document.getElementById('dtRecebimento'));
	storeButtonScript('lms.contasreceber.manterRedecoAction.storeItemRedeco', 'myStoreItem', document.forms[0]);
}

function myStoreItem_cb(data, errorMsg, errorKey, showErrorAlert) {
	var eventObj = {name:"storeItemButton"};		
	store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);		
}

function onChangeVlJuros(){
	setElementValue("vlDiferencaJuros", setFormat("vlDiferencaJuros", (getElementValue("vlJuros") - getElementValue("vlJurosCalculados")) + ""));
	calcularValorTotalRecebido();
}

function myItemRedeco_cb(searchFilters) {
	populaSomatorios();	
}



/**
 *
 *
 * CÓDIGO PARA A LOOKUP DE DOCUMENTO DE SERVICO/FATURA/NDI/ROF
 *
 *
 *
 */

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

/*
 *	Habilita a lupa de documento de serviço mesmo com o número do documento desabilitado
 */
function habilitaLupa() {
	setDisabled("documento.idDocumento", false);
	setDisabled("documento.nrDocumento", true);
}	 


/*
 * On change da combo de Tipo de Documento de Serivo.<BR>
 * Altera lookup de conhecimento
 */
function tpDocumentoServicoOnChange(){
	resetValue("documento.filial.idFilial");
	resetDevedor();		
	resetDadosDocumento();
	disableFields();
	setMaskNrDocumento();					
		
	if (getElementValue("documento.tpDocumento") == "FAT"){				

		document.getElementById("documento.idDocumento").service = "lms.contasreceber.manterRedecoAction.findFatura";
		document.getElementById("documento.idDocumento").cmd = "list";
		document.getElementById("documento.idDocumento").url = contextRoot+"/contasReceber/manterFaturas.do";
		
		document.getElementById("documento.idDocumento").propertyMappings = [  
		{ modelProperty:"nrFatura", criteriaProperty:"documento.nrDocumento", inlineQuery:true, disable:false}, 
		{ modelProperty:"nrFatura", relatedProperty:"documento.nrDocumento", blankFill:true }, 
		{ modelProperty:"filialByIdFilial.idFilial", criteriaProperty:"documento.filial.idFilial", inlineQuery:true, disable:true }, 
		{ modelProperty:"filialByIdFilial.sgFilial", criteriaProperty:"documento.filial.sgFilial", inlineQuery:true, disable:true },
		{ modelProperty:"filialByIdFilial.idFilial", relatedProperty:"documento.filial.idFilial", blankFill:false },
		{ modelProperty:"filialByIdFilial.sgFilial", relatedProperty:"documento.filial.sgFilial", blankFill:false },
		{ modelProperty:"filialByIdFilial.pessoa.nmFantasia", criteriaProperty:"documento.filial.pessoa.nmFantasia", inlineQuery:true, disable:true },
		{ modelProperty:"filialByIdFilial.pessoa.nmFantasia", relatedProperty:"documento.filial.pessoa.nmFantasia", blankFill:false }			
		];			
	} else if (getElementValue("documento.tpDocumento") == "NDI"){
	
		document.getElementById("documento.idDocumento").service = "lms.contasreceber.manterRedecoAction.findNotaDebitoInternacional";
		document.getElementById("documento.idDocumento").cmd = "list";
		document.getElementById("documento.idDocumento").url = contextRoot+"/contasReceber/manterNotasDebitoInternacionais.do";
		
		document.getElementById("documento.idDocumento").propertyMappings = [  
		{ modelProperty:"nrFatura", criteriaProperty:"documento.nrDocumento", inlineQuery:true, disable:false}, 
		{ modelProperty:"nrFatura", relatedProperty:"documento.nrDocumento", blankFill:true }, 
		{ modelProperty:"filialByIdFilial.idFilial", criteriaProperty:"documento.filial.idFilial", inlineQuery:true, disable:true }, 
		{ modelProperty:"filialByIdFilial.sgFilial", criteriaProperty:"documento.filial.sgFilial", inlineQuery:true, disable:true },
		{ modelProperty:"filialByIdFilial.idFilial", relatedProperty:"documento.filial.idFilial", blankFill:false },
		{ modelProperty:"filialByIdFilial.sgFilial", relatedProperty:"documento.filial.sgFilial", blankFill:false },
		{ modelProperty:"filialByIdFilial.pessoa.nmFantasia", criteriaProperty:"documento.filial.pessoa.nmFantasia", inlineQuery:true, disable:true },
		{ modelProperty:"filialByIdFilial.pessoa.nmFantasia", relatedProperty:"documento.filial.pessoa.nmFantasia", blankFill:false }		
		];					
	} else if (getElementValue("documento.tpDocumento") == "ROF"){
		document.getElementById("documento.idDocumento").service = "lms.contasreceber.manterRedecoAction.findRecibo";
		document.getElementById("documento.idDocumento").cmd = "list";
		document.getElementById("documento.idDocumento").url = contextRoot+"/contasReceber/manterReciboOficial.do";
		
		document.getElementById("documento.idDocumento").propertyMappings = [  
		{ modelProperty:"nrRecibo", criteriaProperty:"documento.nrDocumento", inlineQuery:true, disable:false}, 
		{ modelProperty:"nrRecibo", relatedProperty:"documento.nrDocumento", blankFill:true }, 
		{ modelProperty:"filialByIdFilial.idFilial", criteriaProperty:"documento.filial.idFilial", inlineQuery:true, disable:true }, 
		{ modelProperty:"filialByIdFilial.sgFilial", criteriaProperty:"documento.filial.sgFilial", inlineQuery:true, disable:true },
		{ modelProperty:"filialByIdFilial.idFilial", relatedProperty:"documento.filial.idFilial", blankFill:false },
		{ modelProperty:"filialByIdFilial.sgFilial", relatedProperty:"documento.filial.sgFilial", blankFill:false },
		{ modelProperty:"filialByIdFilial.pessoa.nmFantasia", criteriaProperty:"documento.filial.pessoa.nmFantasia", inlineQuery:true, disable:true },
		{ modelProperty:"filialByIdFilial.pessoa.nmFantasia", relatedProperty:"documento.filial.pessoa.nmFantasia", blankFill:false }			
		];					
	} else {
		document.getElementById("documento.idDocumento").service = "lms.contasreceber.manterRedecoAction.findDevedorServDocFat";
		document.getElementById("documento.idDocumento").cmd = "pesq";
		document.getElementById("documento.idDocumento").url = contextRoot+"/contasReceber/pesquisarDevedorDocServFatLookUp.do";
		
		document.getElementById("documento.idDocumento").propertyMappings = [  
		{ modelProperty:"doctoServico.nrDoctoServico", criteriaProperty:"documento.nrDocumento", inlineQuery:true, disable:false}, 
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
	resetDadosDocumento();
	return documento_filial_sgFilialOnChangeHandler();
}	

/*
 * On Change callBack da lookup de Filial
 */	
function filialOnChange_cb(data,e,c,x){
	resetDevedor();
	if (data.length == 1) {			
		setDisabled("documento.idDocumento",false);

		__lookupSetValue({e:getElement("documento.filial.idFilial"), data:data[0]});
		//setElementValue("documento.filial.pessoa.nmFantasia",data[0].pessoa.nmFantasia);
		return true;
	} else {
		habilitaLupa();
		alert(lookup_noRecordFound);
	}			
}


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
		obj.maxLength = 8;
		obj.mask = "00000000";
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


function onChangeDocumento(){
	if (getElementValue("documento.nrDocumento") == ""){
		resetDadosDocumento();
	}
	return documento_nrDocumentoOnChangeHandler();
}	

function validateTpSituacaoFatura(idFatura) {
	var remoteCall = {serviceDataObjects:new Array()};
    var dataCall = createServiceDataObject("lms.contasreceber.manterRedecoAction.validateTpSituacaoFatura", 
                                           "validateTpSituacaoFatura", 
                                           {idFatura:idFatura,
                                            tipoFinalidade:getElementValue("tipoFinalidade")});
    remoteCall.serviceDataObjects.push(dataCall);
	xmit(remoteCall);	
}

function validateTpSituacaoFatura_cb(data, error) {
	if (error != undefined) {
		setElementValue("documento.idDocumento", "");
		setElementValue("documento.nrDocumento", "");
		setElementValue("documento.filial.idFilial", "");
		setElementValue("documento.filial.sgFilial", "");
		setElementValue("documento.filial.pessoa.nmFantasia", "");	
		setElementValue("vlJuros", "");
		setElementValue("vlDesconto", "");
		setElementValue("vlDocumento", "");
		setElementValue("vlJurosCalculados", "");
		setElementValue("vlDiferencaJuros", "");
		setElementValue("moeda.siglaSimbolo", "");
		setElementValue("vlDiferencaCambialCotacao", "");
		setElementValue("vlRecebido", "");
		setElementValue("vlTarifa", "");
		alert(error);
	}
}

function myOnPopupSetValue(data, dialogWindow) {
	var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false};
	var storeSDO = createServiceDataObject("lms.contasreceber.manterRedecoAction.validateMonitoramentoEletronicoAutorizado", "myOnPopupSetValue", {data:data});
	remoteCall.serviceDataObjects.push(storeSDO);
	xmit(remoteCall);
	
	return false;
}
	
function myOnPopupSetValue_cb(data, error){
	if (error) {
		alert(error);
	} else {
		if (data.data.idFatura != null) {
			setElementValue("documento.idDocumento",data.data.idFatura);
			setElementValue("documento.nrDocumento",setFormat(getElement("documento.nrDocumento"),data.data.nrFatura));
			setElementValue("documento.filial.idFilial",data.data.filialByIdFilial.idFilial);
			setElementValue("documento.filial.sgFilial",data.data.filialByIdFilial.sgFilial);
			setElementValue("documento.filial.pessoa.nmFantasia",data.data.filialByIdFilial.pessoa.nmFantasia);		
			setDisabled("documento.idDocumento", false);
			populateDocumentValue();
			validateTpSituacaoFatura(data.data.idFatura);
		} else if (data.data.idDevedorDocServFat != null) {			
			setElementValue("documento.idDocumento",data.data.idDevedorDocServFat);
			setElementValue("documento.nrDocumento",setFormat(getElement("documento.nrDocumento"),data.data.nrDoctoServico));
			setElementValue("documento.filial.idFilial",data.data.idFilialOrigem);
			setElementValue("documento.filial.sgFilial",data.data.sgFilialOrigem);
			setElementValue("documento.filial.pessoa.nmFantasia",data.data.nmFantasia);		
			
			setDisabled("documento.idDocumento", false);
			populateDocumentValue();
		} else if (data.data.idRecibo != null) {
			setElementValue("documento.idDocumento",data.data.idRecibo);
			setElementValue("documento.nrDocumento",setFormat(getElement("documento.nrDocumento"),data.data.nrRecibo));
			setElementValue("documento.filial.idFilial",data.data.filialByIdFilial.idFilial);
			setElementValue("documento.filial.sgFilial",data.data.filialByIdFilial.sgFilial);
			setElementValue("documento.filial.pessoa.nmFantasia",data.data.filialByIdFilial.pessoa.nmFantasia);		
	
			setDisabled("documento.idDocumento", false);
			populateDocumentValue();
		} else {
			habilitaLupa();	
		}
	}
	
	return false;
}

function myOnDataLoadDocumento_cb(d,e){
	
	var retorno = documento_nrDocumento_exactMatch_cb(d);
	
	if( retorno == false ){
		setFocus('documento.nrDocumento');
		return retorno;
	}
	              
	if (e == undefined) {
		if ((d.length == 1)){
			myOnPopupSetValue(d[0], undefined);
		}			
	}
	
	return retorno;	
	
}

function disableFields(){
	if (getElementValue("documento.tpDocumento") == "FAT"){				
		//setDisabled("vlJuros", false);
		setDisabled("fatura", false);
	} else {
		//setDisabled("vlJuros", true);
		setDisabled("fatura", true);
	}
}

/**
 *
 * Faz a chamada para buscar os dados (valores) do documento
 *
 * chamado por: myOnPopupSetValue
 */
function populateDocumentValue(){
	var remoteCall = {serviceDataObjects:new Array()};
    var dataCall = createServiceDataObject("lms.contasreceber.manterRedecoAction.findDocumentValue", 
                                           "myOnDataLoadDocumentValue", 
                                           {idDocumento:getElementValue("documento.idDocumento"), 
                                            tpDocumento:getElementValue("documento.tpDocumento"), 
                                            tipoFinalidade:getElementValue("tipoFinalidade")});
    remoteCall.serviceDataObjects.push(dataCall);
	xmit(remoteCall);	
}

/**
 *	Método de retorno da busca dos valores do documento de serviço selecionado
 *  chamado por: populateDocumentValue (no retorno)
 */
function myOnDataLoadDocumentValue_cb(data,error){
	onDataLoad_cb(data,error);
	calcularValorTotalRecebido();
}

/** 
 * 
 * Função para buscar os somatórios dos itens de fatura 
 *
 * chamado por: 
 */
function populaSomatorios(){
      var remoteCall = {serviceDataObjects:new Array()};
      var dataCall = createServiceDataObject("lms.contasreceber.manterRedecoAction.findSomatorios", "populaSomatorios", 
            {
                  idRedeco:getElementValue("masterId")
            }
      );
      remoteCall.serviceDataObjects.push(dataCall);
      xmit(remoteCall);  

}

/** 
 * Função (callBack) para popular os dados dos somatórios 
 */
function populaSomatorios_cb(data, error, erromsg){
	if (error != undefined) {
		alert(error+'');
		return;
	}
	
	setElementValue(getElement("vlTotalRecebido"), setFormat(getElement("vlTotalRecebido"), data.vlTotalRecebido));		
    setElementValue("qtTotalDocumentos",data.qtTotalDocumentos);
}	

/**
 *
 * Reinicia os valors dos campos de valors do documento
 *
 * chamado por: tpDocumentoServicoOnChange, filialOnChange, onChangeDocumento
 */
function resetDadosDocumento(){
	resetValue("vlRecebido");
	resetValue("vlJuros");
	resetValue("vlDesconto");
	resetValue("vlTarifa");
	resetValue("vlDocumento");
	resetValue("vlJurosCalculados");
	resetValue("vlDiferencaJuros");
	resetValue("vlDiferencaCambialCotacao");
}

/**
 * É chamado no callback do populate do documento, no onChange do juros recebidos e no onChange do total de tarifas
 */
function calcularValorTotalRecebido(){
	var vlTotal = new Number(getElementValue("vlSaldo")) + new Number(getElementValue("vlJuros")) - new Number(getElementValue("vlTarifa"));
	setElementValue("vlRecebido", setFormat("vlRecebido", vlTotal.toString()));
	enableDisableFatura();
}

function enableDisableFatura(){
	// Desabilita o botão de fatura caso o documento não seja fatura
	if(getElementValue("documento.tpDocumento") != 'FAT'){
		setDisabled("fatura", true);
	}else{
		setDisabled("fatura", false);
	}
}
</script>
