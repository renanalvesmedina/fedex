<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.entrega.manterRotaEntregaDocumentoServicoAction" onPageLoadCallBack="findFilialAcessoUsuario" >
	<adsm:form action="/entrega/manterRotaEntregaDocumentoServico" idProperty="idDoctoServico">
		
		<adsm:hidden property="tpEmpresa" value="M"/>
		<adsm:lookup 
	        action="/municipios/manterFiliais" 
	        dataType="text" 
	        property="filial" 
	        idProperty="idFilial" 
	        criteriaProperty="sgFilial" 
	        service="lms.entrega.manterRotaEntregaDocumentoServicoAction.findLookupFilial" 
	        label="filial" labelWidth="18%" size="3" width="82%" required="true" maxLength="3" disabled="true">
	        <adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			
			<adsm:propertyMapping relatedProperty="doctoServico.tpDocumentoServico" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.idFilial" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.idFilial" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.sgFilial" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="doctoServico.idDoctoServico" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="doctoServico.nrDoctoServico" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.idRotaColetaEntrega" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.nrRota" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="nrRota" modelProperty=""/>
			
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
        
        <adsm:combobox property="doctoServico.tpDocumentoServico" 
					   label="documentoServico" labelWidth="18%" width="82%" 
					   service="lms.entrega.manterRotaEntregaDocumentoServicoAction.findTipoDocumentoServico" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="return changeComboDocumentoServicoType(this);" >

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 disabled="true" popupLabel="pesquisarDocumentoServico"
						 action="" 
						 size="3" maxLength="3" picker="false" 
						 onchange="return changeComboDocumentoServicoFilial();">
			</adsm:lookup>				  
			
			<adsm:lookup dataType="integer" 
						 property="doctoServico" 
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" 
						 service="" 
						 action="" 
						 onPopupSetValue="retornoPopPupDoctoServico"
						 onchange="return onDoctoServicoChange(this);"
						 size="20" maxLength="10" mask="0000000000" serializable="true" disabled="true" popupLabel="pesquisarDocumentoServico" required="true">
			</adsm:lookup>
			
			<adsm:hidden property="filialByIdFilialOrigem.pessoa.nmFantasia"/>
			<adsm:hidden property="idFilialOper"/>
			<adsm:hidden property="idFilialDest"/>
			<adsm:hidden property="tpDocumentoServico"/>
		</adsm:combobox> 	
       
        <adsm:textbox label="rotaEntrega" labelWidth="18%" width="32%" size="22" dataType="text" property="nrRota" disabled="true" />
		
		<adsm:lookup 
	        action="/municipios/manterRotaColetaEntrega" 
	        dataType="integer" 
	        property="rotaColetaEntrega" 
	        idProperty="idRotaColetaEntrega" 
	        criteriaProperty="nrRota" 
	        service="lms.entrega.manterRotaEntregaDocumentoServicoAction.findLookupRotaEntrega" 
	        label="novaRotaEntrega" labelWidth="18%" size="3" width="82%" required="true" maxLength="3" exactMatch="true">
	        <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
   	        <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false"/>
   	        <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" inlineQuery="false"/>
	        <adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota"/>
			<adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true"/>
        </adsm:lookup>
		
		<adsm:buttonBar>
			<adsm:button caption="confirmarAlteracaoRota" buttonType="storeButton" id="botaoConfirmar" service="lms.entrega.manterRotaEntregaDocumentoServicoAction.confirmarAlteracaoRota" callbackProperty="confirmarAlteracaoRota"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
//DOCTO SERVICO
    function onDoctoServicoChange(elem) {
		resetValue('rotaColetaEntrega.idRotaColetaEntrega');
		resetValue('rotaColetaEntrega.nrRota');
		return doctoServico_nrDoctoServicoOnChangeHandler();
	}
	
	function changeComboDocumentoServicoType(field) {
		resetValue('doctoServico.idDoctoServico');
		resetValue('rotaColetaEntrega.idRotaColetaEntrega');
		resetValue('rotaColetaEntrega.nrRota');
		setElementValue("tpDocumentoServico", getElementValue("doctoServico.tpDocumentoServico"));
		var flag = changeDocumentWidgetType({documentTypeElement:field,
                             filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
                             documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
                             documentGroup:'DOCTOSERVICE',actionService:'lms.entrega.manterRotaEntregaDocumentoServicoAction'});
        var pms = document.getElementById("doctoServico.idDoctoServico").propertyMappings;
        
		pms[pms.length] = { modelProperty:"tpDocumentoServico", criteriaProperty:"tpDocumentoServico", inlineQuery:true };
		pms[pms.length] = { modelProperty:"idFilialDoctoSer", criteriaProperty:"doctoServico.filialByIdFilialOrigem.idFilial", inlineQuery:true };
		
		pms[pms.length] = { modelProperty:"doctoServico.idDoctoServico", relatedProperty:"idDoctoServico" };
		pms[pms.length] = { modelProperty:"idFilialOper", relatedProperty:"idFilialOper" };
		pms[pms.length] = { modelProperty:"idFilialDest", relatedProperty:"idFilialDest" };
		pms[pms.length] = { modelProperty:"nrRota", relatedProperty:"nrRota" };
		

		return flag;
	}
	
	function changeComboDocumentoServicoFilial() {
		resetValue('doctoServico.idDoctoServico');
		resetValue('rotaColetaEntrega.idRotaColetaEntrega');
		resetValue('rotaColetaEntrega.nrRota');
		var flagF = changeDocumentWidgetFilial({
                         filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
                         documentNumberElement:document.getElementById('doctoServico.idDoctoServico')});
       
       var pmsf = document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial").propertyMappings;
       pmsf[pmsf.length] = { modelProperty:"empresa.tpEmpresa", criteriaProperty:"tpEmpresa", inlineQuery:true }; 
        
       return flagF; 
	}
	
function retornoPopPupDoctoServico(data){
	if(data != undefined){
	   		var idDoctoServico = getNestedBeanPropertyValue(data,"idDoctoServico");
			
			var criteria = new Array();
			setNestedBeanPropertyValue(criteria,"idDoctoServico",idDoctoServico);
		
			_serviceDataObjects = new Array();
	   		addServiceDataObject(createServiceDataObject("lms.entrega.manterRotaEntregaDocumentoServicoAction.findRetornoPopPupDoctoServico", "onDataLoad", criteria));
	  		xmit();
  		
	}
}	



function resetaNrRota(){
	resetValue("nrRota");
}

function setaPropertysMappingFilial() {
		var pmsf = document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial").propertyMappings;
		pmsf[pmsf.length] = { modelProperty:"empresa.tpEmpresa", criteriaProperty:"empresa.tpEmpresa", inlineQuery:true};
}


function initWindow(eventObj){
	if(eventObj.name=='cleanButton_click'){
		setMasterLink(document, true); 
		if(document.getElementById("filial.idFilial").masterLink == undefined)
			findFilialAcessoUsuario();
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial",true);
		setDisabled("doctoServico.idDoctoServico",true);
	}
}

//*************busca a filial do usuario logado*****************************************
function findFilialAcessoUsuario_cb(data,exception){
		onPageLoad_cb(data,exception);
		setMasterLink(document, true); 
		if(document.getElementById("filial.idFilial").masterLink == undefined)
			findFilialAcessoUsuario();
		setDisabled("botaoConfirmar", false);
		
}

function findFilialAcessoUsuario(){
		_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.entrega.manterRotaEntregaDocumentoServicoAction.findFilialUsuarioLogado", "setaInformacoesFilial", new Array()));
	  	xmit();
}
	
function setaInformacoesFilial_cb(data, exception){
		if(data != undefined){
			setElementValue("filial.sgFilial", getNestedBeanPropertyValue(data, "filial.sgFilial"));
			setElementValue("filial.idFilial", getNestedBeanPropertyValue(data, "filial.idFilial"));
			setElementValue("filial.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia"));
		}
}
	
//funcao chamada no callback do botao confirmarAlteracaoRota
function confirmarAlteracaoRota_cb(data, error, errorKey){
	 if(error != undefined) {
		alert(error);
		return false;
	}
	if(errorKey == undefined)
		store_cb(data);
	else if(errorKey != undefined)
		return false;
	
}

</script>

