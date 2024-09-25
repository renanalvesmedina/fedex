<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/contasReceber/emitirBoletos">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-36267"/>
		</adsm:i18nLabels>
		
		<adsm:combobox service="lms.contasreceber.emitirBoletosAction.findCedentes" 
					  optionLabelProperty="comboText" 
					  labelWidth="15%"
					  width="35%"
					  boxWidth="209"
					  required="false"
					  optionProperty="idCedente" 
					  property="cedente.idCedente" 
					  autoLoad="true"
					  label="banco"/> 
					  
		
		<adsm:hidden property="siglaFilial" serializable="true"/>
		
		<adsm:hidden property="nrFatura" serializable="true"/>
		
		<adsm:hidden property="filial.pessoa.nmPessoa" serializable="false"/>
		
		<adsm:lookup label="fatura"
				popupLabel="pesquisarFilial"
				action="/municipios/manterFiliais" 
				service="lms.contasreceber.emitirBoletosAction.findLookupFilial" 
				dataType="text" 
				property="filialByIdFilial" 
				idProperty="idFilial"
				criteriaProperty="sgFilial" 
				picker="false"
				size="3" 
				maxLength="3" 
				onchange="return changeLookupFilial();"
				labelWidth="15%"
				width="35%"
				exactMatch="true">

			<adsm:propertyMapping relatedProperty="siglaFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmPessoa" modelProperty="pessoa.nmFantasia"/>
			
			<adsm:lookup serializable="true"
					service="lms.contasreceber.emitirBoletosAction.findLookupFatura" 
					popupLabel="pesquisarFatura"
					dataType="integer" 
					mask="0000000000"
					property="fatura" 
					idProperty="idFatura"
					criteriaProperty="nrFatura" 
					onPopupSetValue="setaFilialFatura"
					onDataLoadCallBack="setaFilialFatura"
					size="20"
					maxLength="16"
					action="/contasReceber/manterFaturas">
   		 		
   		 		<adsm:propertyMapping formProperty="nrFatura" modelProperty="nrFatura"/>
   				
   				<adsm:propertyMapping criteriaProperty="filialByIdFilial.idFilial" modelProperty="filialByIdFilial.idFilial"/> 
   				<adsm:propertyMapping criteriaProperty="filialByIdFilial.sgFilial" modelProperty="filialByIdFilial.sgFilial" inlineQuery="true"/> 
   				<adsm:propertyMapping criteriaProperty="filial.pessoa.nmPessoa" modelProperty="filialByIdFilial.pessoa.nmFantasia" inlineQuery="true"/> 
   				
   			 </adsm:lookup>
    
        </adsm:lookup>
     
       <adsm:hidden property="filial.idFilial" serializable="true"/> 
       
       <adsm:textbox labelWidth="15%" width="35%" label="filialFaturamento" dataType="text" 
         			  property="filial.sgFilial" 
         			  serializable="false" 
         			  size="3" 
         			  maxLength="3" disabled="true" required="true">
         			  
	       <adsm:textbox dataType="text" 
	        			  property="filial.pessoa.nmFantasia" 
	        			  serializable="false" 
	        			  size="30" 
	        			  maxLength="30" disabled="true"/>
        
		</adsm:textbox>

        <adsm:range label="intervaloBoletos" labelWidth="15%" width="35%" >
			<adsm:textbox dataType="integer" size="13" maxLength="13" property="boletoInicial" smallerThan="boletoFinal" />
			<adsm:textbox dataType="integer" size="13" maxLength="13" property="boletoFinal" biggerThan="boletoInicial" />
		</adsm:range>
		
	   <adsm:combobox property="tpFormatoRelatorio" 
					  required="true"
					  label="formatoRelatorio" 
					  defaultValue="pdf"
					  disabled="true"
					  domain="DM_FORMATO_RELATORIO"
					  labelWidth="15%" width="35%"/>
					  
		<adsm:hidden property="reemissao" serializable="true" value="false"/>					  
			
		<adsm:buttonBar>
			<adsm:button buttonType="reportViewerButton" caption="visualizar" onclick="validateFormulario()" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>


<script>


	
function validateFormulario(){
		if (validateForm(document.forms[0])) {
			if (confirm(i18NLabel.getLabel("LMS-36267"))){
			executeReportWithCallback('lms.contasreceber.emitirBoletosAction.executeEmitirCTEWithLocator', 'imprimeCte', document.forms[0]);
			}else{
				reportButtonScript('lms.contasreceber.emitirBoletosAction', 'openPdf', document.forms[0]);
			}
		}
}


function imprimeCte_cb(strFile,error){
	if (error == null) {
		if (strFile != null && strFile._value != "") {
		openReportWithLocator(strFile, null, "IMPRESSAO_CTE", true);
		}
		executeReportWithCallback('lms.contasreceber.emitirBoletosAction.executeEmitirNTEWithLocator', 'imprimeNte', document.forms[0]);
	} else {
		alert(error+'');
	}
}


function imprimeNte_cb(strFile,error){
	if (error == null) {
		if (strFile != null && strFile._value != "") {
			openReportWithLocator(strFile, null, "IMPRESSAO_NTE", true);
		}
	} else {
		alert(error+'');
	}
	executeReportWithCallback('lms.contasreceber.emitirBoletosAction.executeEmitirNSEWithLocator', 'imprimeNse', document.forms[0]);
}

function imprimeNse_cb(strFile,error){
	if (error == null) {
		if (strFile != null && strFile._value != "") {
			openReportWithLocator(strFile, null, "IMPRESSAO_NSE", true);
		}
	} else {
		alert(error+'');
	}
	reportButtonScript('lms.contasreceber.emitirBoletosAction', 'openPdf', document.forms[0]);
}

function changeLookupFilial() {
	var valor = getElementValue("filialByIdFilial.sgFilial");
	if (valor != "") {
		setDisabled("fatura.idFatura", false);
	} else {
		setDisabled("fatura.idFatura", true, undefined, false);
	}
	return filialByIdFilial_sgFilialOnChangeHandler();
}

/**
 * Função de retorno da popUp da lookup de fatura
 */
function setaFilialFatura(data, errors){
	setElementValue("filialByIdFilial.sgFilial", data.filialByIdFilial.sgFilial);
	setElementValue("filialByIdFilial.idFilial", data.filialByIdFilial.idFilial);
	setElementValue("filial.pessoa.nmPessoa", data.filialByIdFilial.pessoa.nmFantasia);
	setFocus("filial.sgFilial");
	
	setDisabled("fatura.idFatura", false);
	
	// Busca o cedente de acordo com a fatura
	findCedenteByFatura(data.filialByIdFilial.idFilial, data.nrFatura); 
}

/**
 * Função de retorno da service da lookup de fatura
 */
function setaFilialFatura_cb(data, errors){
	if(fatura_nrFatura_exactMatch_cb(data)){
		setElementValue("filialByIdFilial.sgFilial", data[0].filialByIdFilial.sgFilial);
		setElementValue("filialByIdFilial.idFilial", data[0].filialByIdFilial.idFilial);
		
		if(data[0].cedente != undefined){
			setElementValue("cedente.idCedente", data[0].cedente.idCedente);
		}
		
		setFocus("filial.sgFilial");
	}
	
	return true;
}

/**
  * CallBack da página
  */
function myOnPageLoad_cb(d, e){
	onPageLoad_cb(d, e);
	initPage();
}

/** 
  * Função responsável por carregar dados ao carregar a pagina
  */
function initPage(){
	_serviceDataObjects = new Array();
	
	findFilialSessao();
	
	xmit(false);
}

/**
  * Busca a filial da sessão
  */
function findFilialSessao(){
	addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirBoletosAction.findFilialUsuarioLogado",
		"setFilialUsuario", 
		 new Array()));

}

/**
  * Busca o cedente de acordo com a filial
  */
function findCedenteByFatura(idFilial, nrFatura){
	
	var data = new Array();
	
	setNestedBeanPropertyValue(data, "nrFatura", nrFatura);
	setNestedBeanPropertyValue(data, "filialByIdFilial.idFilial", idFilial);
	
	_serviceDataObjects = new Array();
	
	addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirBoletosAction.findCedenteByFatura",
		"setCedenteByFatura", data));
		 
	xmit(false);

}

/**
  * CallBack da função findCedenteByFatura
  */
function setCedenteByFatura_cb(data, error){
	
	if(error != undefined){
		alert(error);
	}
	
	if(data.idCedente != null)
		setElementValue("cedente.idCedente", data.idCedente);
	
	return true;
}

/**
  * Função chamada no inicio da página
  */
function initWindow(ev){
	if (ev.name == "cleanButton_click") {
		initPage();
	}
	
	if (ev.name == "tab_load"
			|| ev.name == "tab_click"
			|| ev.name == "cleanButton_click") {
		setDisabled("fatura.idFatura", true, undefined, false);
	}
}

/**
  * CallBack da função findFilialSessao
  */
function setFilialUsuario_cb(data, error) {
	if (data != null) {
		setElementValue("filial.idFilial", data.idFilial);
		setElementValue("filial.sgFilial", data.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", data.pessoa.nmFantasia);
	}
}

</script>