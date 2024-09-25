<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirReciboFrete">
	
		<adsm:hidden property="filial.pessoa.nmFantasia" serializable="true"/>
		
		<adsm:hidden property="nrRecibo" serializable="true"/>
		
		<adsm:lookup label="recibo"
					 serializable="true"
					 action="/municipios/manterFiliais" 
					 service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findLookupFilial" 
					 dataType="text" 
					 property="filialByIdFilialEmissora" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 picker="false"
					 size="3" 
					 maxLength="3" 
					 labelWidth="20%"
					 width="5%"
					 exactMatch="true">
					 
			<adsm:lookup serializable="true"
   					 	 service="lms.contasreceber.emitirReciboFreteAction.findRecibosByFilial" 
   					 	 dataType="integer" 
   					 	 property="recibo" 
   	 					 idProperty="idRecibo"
   	 					 mask="0000000000"
   						 criteriaProperty="nrRecibo" 
   						 size="10"
   						 maxLength="10"
   					 	 width="75%"
   					  	 action="/contasReceber/manterReciboBrasil">
   					 
   				<adsm:propertyMapping criteriaProperty="filialByIdFilialEmissora.idFilial" modelProperty="filialByIdFilialEmissora.idFilial"/> 
   				<adsm:propertyMapping criteriaProperty="filialByIdFilialEmissora.sgFilial" modelProperty="filialByIdFilialEmissora.sgFilial" inlineQuery="false"/> 
   				<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filialByIdFilialEmissora.pessoa.nmFantasia" inlineQuery="false"/> 
   				
   				<adsm:propertyMapping relatedProperty="nrRecibo" modelProperty="nrRecibo"/>
   				
	       </adsm:lookup>
    
        </adsm:lookup>
        
        <adsm:lookup label="manifestoEntrega" 
        			 serializable="true" 
        			 action="/municipios/manterFiliais" 
        			 disabled="false"
					 service="lms.contasreceber.emitirReciboFreteAction.findLookupFilial" 
					 dataType="text" 
					 property="manifesto.filial" 
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 picker="false" 
					 size="3" 
					 maxLength="3" 
					 labelWidth="20%" 
					 width="80%" 
					 exactMatch="true">
			
			<adsm:lookup serializable="true" 
						 service="lms.contasreceber.emitirReciboFreteAction.findManifestosEntregaByFilial" 
   					 	 dataType="integer" 
   					 	 popupLabel="pesquisarManifestoEntrega"	
   					 	 property="manifestoEntrega" 
   					 	 idProperty="idManifestoEntrega" 
   					 	 criteriaProperty="nrManifestoEntrega" 
   						 size="20" 
   						 maxLength="16" 
   						 mask="00000000" 
   						 action="entrega/consultarManifestosEntrega" 
   						 cmd="lookup">
				<adsm:propertyMapping modelProperty="filial.sgFilial"          criteriaProperty="filialByIdFilialEmissora.sgFilial" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filial.pessoa.nmFantasia" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="filial.idFilial"          criteriaProperty="filialByIdFilialEmissora.idFilial" />
   		   </adsm:lookup>
    
        </adsm:lookup>
		
		<adsm:combobox property="tpFormatoRelatorio" 
			required="true"
			label="formatoRelatorio" 
			domain="DM_FORMATO_RELATORIO"
			defaultValue="pdf"
			labelWidth="20%" width="80%"/>
		
		<adsm:buttonBar>
		
			<adsm:button id="emitirRecibo" caption="visualizar" onclick="emiteRecibo();" buttonType="reportButton" disabled="false"/>
			<adsm:resetButton/>
		
		</adsm:buttonBar>
	
	</adsm:form>

</adsm:window>

<script>

function emiteRecibo() {
	executeReportWithCallback("lms.contasreceber.emitirReciboFreteAction.execute", "openReportWithLocator", document.forms[0]);
}

function myPageLoadCallBack_cb(data, erro){
	onPageLoad_cb(data,erro);
	findFilialUsuario();
	setElementValue("tpFormatoRelatorio", "pdf");
}

function reciboPopUpSetValue(data){
	
	setElementValue("filialByIdFilialEmissora.sgFilial", data.filialByIdFilialEmissora.sgFilial);
	setElementValue("filialByIdFilialEmissora.idFilial", data.filialByIdFilialEmissora.idFilial);
	
	return true;
}

function manifestoPopUpSetValue(data) {
	
	setElementValue("manifesto.filial.sgFilial", data.filial.sgFilial);
	setElementValue("manifesto.filial.idFilial", data.filial.idFilial);
	
	return true;
}

/**
  * callBack da lookup de recibo
  */
function lookupRecibo_cb(data, error){
	
	if(error != undefined)
		alert(error);
		
	if(recibo_nrRecibo_exactMatch_cb(data))
		reciboPopUpSetValue(data);
		
}

/**
  * callBack da lookup de manifesto
  */
function lookupManifesto_cb(data, error){

	if(error != undefined)
		alert(error);
		
	if(manifestoEntrega_nrManifestoEntrega_exactMatch_cb(data))
		reciboPopUpSetValue(data);
		
}

/** 
  * Busca a filial do usuario da sessão 
  */
function findFilialUsuario(){
	
	_serviceDataObjects = new Array();

	addServiceDataObject(createServiceDataObject("lms.configuracoes.manterVinculoFormulariosImpressorasAction.findFilialUsuario",
		"findFilialUsuario", 
		new Array()));
	
	xmit(false);	
}
	
/** 
  * CallBack da faunção findFilialUsuario 
  */
function findFilialUsuario_cb(data, error) {
	setFilial(data);
}

/**
  * Seta os campos da filial de acordo com os dados que retornam 
  */
function setFilial(data){

	var sgFilial = data.sgFilial;
	var idFilial = data.idFilial;
	
	// Seta nmFantasia das lookups
	setElementValue("filial.pessoa.nmFantasia", data.pessoa.nmFantasia);
	
	// Filial Recibo
	setElementValue("filialByIdFilialEmissora.sgFilial", sgFilial);
	setElementValue("filialByIdFilialEmissora.idFilial", idFilial);
	
	getElement("filialByIdFilialEmissora.idFilial").masterLink = "true";
	getElement("filialByIdFilialEmissora.sgFilial").masterLink = "true";
	setDisabled("filialByIdFilialEmissora.idFilial", true);
	
	// Filial Manifesto
	setElementValue("manifesto.filial.sgFilial", sgFilial);
	setElementValue("manifesto.filial.idFilial", idFilial);
	setDisabled("manifesto.filial.idFilial", true);
	
	// Seta as filiais como masterLink
	getElement("manifesto.filial.idFilial").masterLink = "true";
	getElement("manifesto.filial.sgFilial").masterLink = "true";
	getElement("filial.pessoa.nmFantasia").masterLink = "true";
	
}

function initWindow(evento){
	setFocus("recibo.nrRecibo");
	//setFocusOnFirstFocusableField();
}

</script>