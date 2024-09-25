<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.entrega.informarEntradasRegiaoEntregaAction" onPageLoadCallBack="findFilialAcessoUsuario">
	<adsm:form action="/entrega/informarEntradasRegiaoEntrega" idProperty="idDoctoServico" >
			
		<adsm:hidden property="tpEmpresa" value="M"/>
		<adsm:lookup 
	        action="/municipios/manterFiliais" 
	        dataType="text" 
	        property="filial" 
	        idProperty="idFilial" 
	        criteriaProperty="sgFilial" 
	        service="lms.entrega.informarEntradasRegiaoEntregaAction.findLookupFilial" 
	        label="filial" labelWidth="18%" size="3" width="82%" required="true" maxLength="3" onchange="return onFilialChange(this);" disabled="true">
	        <adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="doctoServico.tpDocumentoServico" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.idFilial" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.idFilial" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.sgFilial" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="doctoServico.idDoctoServico" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="doctoServico.nrDoctoServico" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="nrRota" modelProperty=""/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
        
         <adsm:combobox property="doctoServico.tpDocumentoServico" 
					   label="documentoServico" labelWidth="18%" width="82%" 
					   service="lms.entrega.informarEntradasRegiaoEntregaAction.findTipoDocumentoServico" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="return changeComboDocumentoServicoType(this);" >

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 disabled="true"
						 action="" popupLabel="pesquisarDocumentoServico"
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
						 size="20" maxLength="10" mask="0000000000" required="true" serializable="true" disabled="true" popupLabel="pesquisarDocumentoServico">
			</adsm:lookup>
			
			<adsm:hidden property="filialByIdFilialOrigem.pessoa.nmFantasia"/>
			<adsm:hidden property="idFilialOper"/>
			<adsm:hidden property="idFilialDest"/>
			<adsm:hidden property="tpDocumentoServico"/>
			
			
		</adsm:combobox> 
        
        <adsm:textbox dataType="text" property="nrRota" label="rotaEntrega" width="82%" labelWidth="18%" disabled="true" />
		
		<adsm:buttonBar>
			<adsm:button caption="confirmarEntradaDocumento" disabled="false" buttonType="storeButton" service="lms.entrega.informarEntradasRegiaoEntregaAction.confirmaEntradaDoc" callbackProperty="retornoConfirmar" id="botaoConfirmar"/>
			
			<adsm:button id="botaoAlterar"  caption="alteracaoRotaEntrega" action="entrega/manterRotaEntregaDocumentoServico" cmd="main" disabled="true" >
				
				<adsm:linkProperty src="filial.idFilial" target="filial.idFilial"/>
				<adsm:linkProperty src="filial.sgFilial" target="filial.sgFilial"/>
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="filial.pessoa.nmFantasia"/>
								
				<adsm:linkProperty src="doctoServico.tpDocumentoServico" target="doctoServico.tpDocumentoServico"/>			
				<adsm:linkProperty src="doctoServico.nrDoctoServico" target="doctoServico.nrDoctoServico" disabled="true"/>
				<adsm:linkProperty src="idDoctoServico" target="idDoctoServico"/>
				<adsm:linkProperty src="doctoServico.filialByIdFilialOrigem.sgFilial" target="doctoServico.filialByIdFilialOrigem.sgFilial"/>
				<adsm:linkProperty src="nrRota" target="nrRota"/>
				<adsm:linkProperty src="idFilialOper" target="idFilialOper"/>
				<adsm:linkProperty src="idFilialDest" target="idFilialDest"/>
			</adsm:button>
				
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function onFilialChange(elem) {
		setDisabled("botaoAlterar", true);
		setDisabled("botaoConfirmar", false);
		return filial_sgFilialOnChangeHandler();
	}

	function onDoctoServicoChange(elem) {
		setDisabled("botaoAlterar", true);
		setDisabled("botaoConfirmar", false);
		return doctoServico_nrDoctoServicoOnChangeHandler();
	}
	

//DOCTO SERVICO
	
	function changeComboDocumentoServicoType(field) {
		resetValue('doctoServico.idDoctoServico');
		setDisabled("botaoAlterar", true);
		setDisabled("botaoConfirmar", false);
		setElementValue("tpDocumentoServico", getElementValue("doctoServico.tpDocumentoServico"));
		var flag = changeDocumentWidgetType({documentTypeElement:field,
                             filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
                             documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
                             documentGroup:'DOCTOSERVICE',actionService:'lms.entrega.informarEntradasRegiaoEntregaAction'});
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
		setDisabled("botaoAlterar", true);
		setDisabled("botaoConfirmar", false);
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
	   		addServiceDataObject(createServiceDataObject("lms.entrega.informarEntradasRegiaoEntregaAction.findRetornoPopPupDoctoServico", "onDataLoadDoctoServico", criteria));
	  		xmit();
	  		
	  		
  	}
  	
}	
function onDataLoadDoctoServico_cb(data,exception){
	onDataLoad_cb(data,exception);
	setDisabled("botaoAlterar", true);
	setDisabled("botaoConfirmar", false);
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
		findFilialAcessoUsuario();
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial",true);
		setDisabled("doctoServico.idDoctoServico",true);
		setDisabled("botaoAlterar",true);
		setDisabled("botaoConfirmar", false);
	}
}

//*************busca a filial do usuario logado*****************************************
function findFilialAcessoUsuario_cb(data,exception){
		onPageLoad_cb(data,exception);
		findFilialAcessoUsuario();
		
}
function findFilialAcessoUsuario(){
		_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.entrega.informarEntradasRegiaoEntregaAction.findFilialUsuarioLogado", "setaInformacoesFilial", new Array()));
	  	xmit();
}
	
	function setaInformacoesFilial_cb(data, exception){
		if(data != undefined){
			setElementValue("filial.sgFilial", getNestedBeanPropertyValue(data, "filial.sgFilial"));
			setElementValue("filial.idFilial", getNestedBeanPropertyValue(data, "filial.idFilial"));
			setElementValue("filial.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia"));
		}
		
	}
	
//********************************funcoes do botao Confirmar*******************************************
function retornoConfirmar_cb(data, error, errorKey){
	 if(error != undefined) {
	 	alert(error);
	 	if(errorKey == "LMS-09001"){
			setDisabled("botaoAlterar",false);
			setDisabled("botaoConfirmar", true);
			setFocus(document.getElementById("botaoAlterar"), false);
		}else{
			setDisabled("botaoAlterar",true);
			setDisabled("botaoConfirmar", false);
		}
		
		return false;
	}
	if(errorKey == undefined)
		store_cb(data);
	else if(errorKey != undefined){
		
		if(errorKey == "LMS-09001"){
			setDisabled("botaoAlterar",false);
			setFocus(document.getElementById("botaoAlterar"), false);
		}else{
			setDisabled("botaoAlterar",true);
			
		}
		return false;
	}
	setDisabled("botaoAlterar",false);
}



</script>