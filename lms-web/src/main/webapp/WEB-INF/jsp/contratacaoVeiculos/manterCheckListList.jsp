<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window title="consultarCheckList" service="lms.contratacaoveiculos.manterCheckListAction" onPageLoadCallBack="setaInfFilialUsuarioLogado">
	<adsm:form action="/contratacaoVeiculos/manterCheckList" height="83" idProperty="idChecklistMeioTransporte">
		<adsm:i18nLabels>
           <adsm:include key="LMS-26073"/>
        </adsm:i18nLabels>
         
		<adsm:lookup action="/municipios/manterFiliais" onchange="return changeFilial();"
		dataType="text" 
		property="filial" 
		idProperty="idFilial"
		criteriaProperty="sgFilial"
		service="lms.contratacaoveiculos.manterCheckListAction.findLookupFilial" 
		label="filial" 
		labelWidth="19%" size="3" width="31%" required="true" maxLength="3" disabled="true">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="solicitacaoContratacao.filial.sgFilial" modelProperty="sgFilial"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="29" disabled="true" />
        </adsm:lookup>
        
		<adsm:textbox maxLength="10" dataType="integer" size="10" property="nrChecklist" label="numero" width="31%" labelWidth="19%" mask="0000000000"/>

 
		<adsm:lookup 
		onchange="return sgFilialOnChangeHandler();"
		onDataLoadCallBack="disableNrSolicitacao"
		service="lms.contratacaoveiculos.manterCheckListAction.findLookupFilial" 
		dataType="text" 
		property="solicitacaoContratacao.filial" 
		idProperty="idFilial"
		criteriaProperty="sgFilial" 
		label="solicitacaoContratacao" size="3" 
		action="/municipios/manterFiliais" 
		labelWidth="19%" width="31%" maxLength="3" picker="false" disabled="true">
		<adsm:lookup 
		onchange="return changeSolicitacaoNr();"
		picker="true"
		onPopupSetValue="setaTMT"
		onDataLoadCallBack="setaTipoMeioTransporte"
		service="lms.contratacaoveiculos.manterCheckListAction.findLookupSolicitacaoContratacao" 
		dataType="integer" 
		property="solicitacaoContratacao" 
		idProperty="idSolicitacaoContratacao"
		criteriaProperty="nrSolicitacaoContratacao" 
		size="10" 
		action="/contratacaoVeiculos/manterSolicitacoesContratacao" 
		maxLength="10" mask="0000000000">
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			
			<adsm:propertyMapping relatedProperty="filial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="solicitacaoContratacao.filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
			
			<adsm:propertyMapping relatedProperty="tpSolicitacaoContratacao" modelProperty="tpSolicitacaoContratacao"/>
			<adsm:propertyMapping relatedProperty="dsTipoMeioTransporte" modelProperty="tipoMeioTransporte.dsTipoMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="solicitacaoContratacao.nrIdentificacaoMeioTransp" modelProperty="nrIdentificacaoMeioTransp"/>
			
		    <adsm:propertyMapping relatedProperty="solicitacaoContratacao.idSolicitacaoContratacao" modelProperty="idSolicitacaoContratacao"/>
		    		    
		</adsm:lookup>
			<adsm:propertyMapping relatedProperty="solicitacaoContratacao.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:hidden property="solicitacaoContratacao.filial.pessoa.nmFantasia"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpSolicitacaoContratacao" 
					   domain="DM_TIPO_SOLICITACAO_CONTRATACAO" 
					   label="tipoSolicitacao" labelWidth="19%" width="31%" renderOptions="true"/>

		<adsm:combobox property="tipoMeioTransporte.tpMeioTransporte" label="modalidade" domain="DM_TIPO_MEIO_TRANSPORTE" 
					   labelWidth="19%" width="31%" boxWidth="150" onchange="onChangeTpMeioTransporte();">
		</adsm:combobox>
		
		<adsm:combobox label="tipoMeioTransporte" width="31%" labelWidth="19%"
			optionLabelProperty="dsTipoMeioTransporte"
			optionProperty="idTipoMeioTransporte" boxWidth="150" disabled="false"
			property="tipoMeioTransporte.idTipoMeioTransporte" />
		
		<adsm:textbox
			size="6"
			maxLength="6" 
			label="meioTransporte" 
			property="nrFrotaMT" dataType="text" labelWidth="19%" width="31%" cellStyle="vertical-align:bottom" disabled="true">
		
		<adsm:lookup service="lms.contratacaoveiculos.manterCheckListAction.findLookupMeioTransp" onDataLoadCallBack="meioTransp"
	    	property="solicitacaoContratacao" idProperty="idMeioTransporte" action="f" picker="false" criteriaProperty="nrIdentificacaoMeioTransp"
	    	dataType="text" maxLength="25" size="20" allowInvalidCriteriaValue="true" criteriaSerializable="true">
	    	<adsm:propertyMapping relatedProperty="nrFrotaMT" modelProperty="nrFrota"/>
		</adsm:lookup>   		

		</adsm:textbox>
		
		
		<adsm:range label="periodoRealizacao" labelWidth="19%" width="31%" required="false" maxInterval="30">
  			<adsm:textbox dataType="JTDate" property="dtRealizacaoInicial"/>
  			<adsm:textbox dataType="JTDate" property="dtRealizacaoFinal"/>
		</adsm:range>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="checklistMeioTransporte" />
			<adsm:resetButton/>
		</adsm:buttonBar>
		

 </adsm:form>
	<adsm:grid property="checklistMeioTransporte" idProperty="idChecklistMeioTransporte" gridHeight="200"
		       scrollBars="horizontal" service="lms.contratacaoveiculos.manterCheckListAction.findPaginatedChecklistMeioTransporte" 
		       rowCountService="lms.contratacaoveiculos.manterCheckListAction.getRowCountChecklistMeioTransporte" rows="10">
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filial" property="sgFilial" width="100"/>
			<adsm:gridColumn title="" property="descricaoFilial" width="100"/>
		</adsm:gridColumnGroup>
 
		<adsm:gridColumn title="numero" property="nrChecklist" width="100" align="right" mask="0000000000" dataType="integer"/>
		
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="solicitacaoContratacao" property="sgFilialNrSol" width="50" />
			<adsm:gridColumn title="" property="nrSolicitacaoContratacao" width="150" align="right" mask="0000000000" dataType="integer"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="modalidade" property="tpMeioTransporte.description" width="130"/>
		<adsm:gridColumn title="tipoMeioTransporte" property="dsTipoMeioTransporte" width="160"/>
		<adsm:gridColumn title="meioTransporte" property="nrFrota" width="80"/>
		<adsm:gridColumn title=""               property="nrIdentificacaoMeioTransp" width="80"/>
		<adsm:gridColumn title="dataRealizacao" property="dtRealizacao" width="130" align="center" dataType="JTDate"/>
						
		<adsm:buttonBar freeLayout="false">
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

function meioTransp_cb(data) {
	solicitacaoContratacao_nrIdentificacaoMeioTransp_exactMatch_cb(data);
	if (data == undefined || data.length == 0) {
		setElementValue("nrFrotaMT","");
		setElementValue("solicitacaoContratacao.nrIdentificacaoMeioTransp",getElementValue("solicitacaoContratacao.nrIdentificacaoMeioTransp").toUpperCase());
	}else if (data.length == 1) {
		setElementValue("solicitacaoContratacao.nrIdentificacaoMeioTransp",getNestedBeanPropertyValue(data[0],"nrIdentificador"));
	}
}


function validaPlaca(){
	var placa = document.getElementById("solicitacaoContratacao.nrIdentificacaoMeioTransp");
	placa.value = placa.value.toUpperCase();
}
function validateDados() {
		var ret = validateTabScript(document.forms);
		if(ret == true)
			findButtonScript('checklistMeioTransporte', document.forms[0]);
		return false;
}

	function validateTab() {
		if (validateTabScript(document.forms)) {
			
			if (getElementValue("solicitacaoContratacao.idSolicitacaoContratacao") != "" || getElementValue("nrChecklist") != "" ||
					(getElementValue("dtRealizacaoInicial") != "" && getElementValue("dtRealizacaoFinal") != "")) {
				return true;
			} else {
				alert(i18NLabel.getLabel("LMS-26073")); 
				return false;
               }
            }
		return false;
    }


function initWindow(eventObj){
	if(eventObj.name == 'cleanButton_click'){
 		findInfUsuarioLogado();
 		disableNrSolicitacao(false);
 	}	
}

function setaInfFilialUsuarioLogado_cb(){
	onPageLoad_cb();
	
	findInfUsuarioLogado();
	
	if (document.getElementById("tipoMeioTransporte.tpMeioTransporte").masterLink == "true")
		notifyElementListeners({e:document.getElementById("tipoMeioTransporte.tpMeioTransporte")});
}

function findInfUsuarioLogado(){
 	 	_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.contratacaoveiculos.manterCheckListAction.findInformacoesUsuarioLogado", "setaInformacoesUsuarioLogado", ""));
	  	xmit();
	  	setFocus(document.getElementById("filial.sgFilial"));
 }
 
 function setaInformacoesUsuarioLogado_cb(data,exception){
  		setElementValue("filial.idFilial",getNestedBeanPropertyValue(data,"filial.idFilial"));
 		setElementValue("filial.sgFilial",getNestedBeanPropertyValue(data,"filial.sgFilial"));
 		setElementValue("filial.pessoa.nmFantasia",getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia"));
 		
 		if(document.getElementById("solicitacaoContratacao.filial.idFilial").masterLink == undefined){
 			setElementValue("solicitacaoContratacao.filial.pessoa.nmFantasia",getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia"));
	 		setElementValue("solicitacaoContratacao.filial.idFilial",getNestedBeanPropertyValue(data,"filial.idFilial"));
	 		setElementValue("solicitacaoContratacao.filial.sgFilial",getNestedBeanPropertyValue(data,"filial.sgFilial"));
 		}
 }
 
 function changeFilial(){
	if(getElementValue("filial.sgFilial") == ''){
		setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", true);
	}else{
		setDisabled("solicitacaoContratacao.idSolicitacaoContratacao",false);
	}
	return filial_sgFilialOnChangeHandler();
 }
 
function changeSolicitacaoFilial(){
	if(getElementValue("solicitacaoContratacao.filial.sgFilial") == ''){
		deletaCamposSolicitacaoContratacao();
	}
	return solicitacaoContratacao_filial_sgFilialOnChangeHandler();
}

function changeSolicitacaoNr(){
	if(getElementValue("solicitacaoContratacao.nrSolicitacaoContratacao") == ''){
		deletaCamposSolicitacaoContratacao();
	}
	return solicitacaoContratacao_nrSolicitacaoContratacaoOnChangeHandler();
}

function deletaCamposSolicitacaoContratacao(){
		setElementValue("solicitacaoContratacao.idSolicitacaoContratacao","");
 		setElementValue("solicitacaoContratacao.nrSolicitacaoContratacao","");
 		setElementValue("tpSolicitacaoContratacao","");
 		setElementValue("tipoMeioTransporte.tpMeioTransporte","");
 		setElementValue("tipoMeioTransporte.idTipoMeioTransporte","");
 		setElementValue("solicitacaoContratacao.nrIdentificacaoMeioTransp","");
 		setElementValue("solicitacaoContratacao.idSolicitacaoContratacao","");
 		
}
	
	function setaTMT(data){
		if(data != undefined){
			setElementValue("tipoMeioTransporte.tpMeioTransporte", getNestedBeanPropertyValue(data,"tipoMeioTransporte.tpMeioTransporte.value"));
			setElementValue("idTipoMeioTransporte", getNestedBeanPropertyValue(data,"tipoMeioTransporte.idTipoMeioTransporte"));
			notifyElementListeners({e:document.getElementById("tipoMeioTransporte.tpMeioTransporte")});
		}
	}
	
	function setaTipoMeioTransporte_cb(data){
		solicitacaoContratacao_nrSolicitacaoContratacao_exactMatch_cb(data);
		if(data != undefined){
			
			setElementValue("tipoMeioTransporte.tpMeioTransporte", getNestedBeanPropertyValue(data,":0.tipoMeioTransporte.tpMeioTransporte.value"));
			setElementValue("idTipoMeioTransporte", getNestedBeanPropertyValue(data,":0.tipoMeioTransporte.idTipoMeioTransporte"));
			notifyElementListeners({e:document.getElementById("tipoMeioTransporte.tpMeioTransporte")});
			
			var dataP = new Array();
			var nrIdentificacaoMT = getElementValue("solicitacaoContratacao.nrIdentificacaoMeioTransp");
			setNestedBeanPropertyValue(dataP,"nrIdentificacaoMT",nrIdentificacaoMT);
			setNestedBeanPropertyValue(dataP,"nrIdentificacaoSR","");
			setNestedBeanPropertyValue(dataP,"idSolicitacaoContratacao","");
			_serviceDataObjects = new Array();
			addServiceDataObject(createServiceDataObject("lms.contratacaoveiculos.manterCheckListAction.findRotaByIdSolicitacaoContratacao", "onDataLoadMT", dataP));
			xmit();
		}
	}
	
	function onDataLoadMT_cb(data,exception){
 	if(data!= undefined)
 		if(getNestedBeanPropertyValue(data,"nrFrotaMT")!= "")
 			setElementValue("nrFrotaMT", getNestedBeanPropertyValue(data,"nrFrotaMT"));
 			
 }
	
	function setaTipoMeioTransporteFilial_cb(data){
		solicitacaoContratacao_filial_sgFilial_exactMatch_cb(data);
		if(data != undefined){
			setElementValue("tipoMeioTransporte.tpMeioTransporte", getNestedBeanPropertyValue(data,":0.tipoMeioTransporte.tpMeioTransporte.value"));
			setElementValue("idTipoMeioTransporte", getNestedBeanPropertyValue(data,":0.tipoMeioTransporte.idTipoMeioTransporte"));
			notifyElementListeners({e:document.getElementById("tipoMeioTransporte.tpMeioTransporte")});
		}
	}
	
	function setaValorComboTipoMT_cb(data){
		tipoMeioTransporte_idTipoMeioTransporte_cb(data);
		setElementValue("tipoMeioTransporte.idTipoMeioTransporte",getElementValue("idTipoMeioTransporte"));
		setComboBoxElementValue(document.getElementById("tipoMeioTransporte.idTipoMeioTransporte"), getElementValue("idTipoMeioTransporte"), getElementValue("idTipoMeioTransporte"), getElementValue("dsTipoMeioTransporte"));
	}
	
	/**
	* Onchange do combo TpMeioTransporte
	*/
	function onChangeTpMeioTransporte(){		
		var tpMeioTransporte = getElementValue("tipoMeioTransporte.tpMeioTransporte");
		findTipoMeioTranspByMeio(tpMeioTransporte)
	}
	
	function findTipoMeioTranspByMeio(tpMeioTransporte){
			var data = new Array();	   		
			setNestedBeanPropertyValue(data, "tpMeioTransporte", tpMeioTransporte);
			var sdo = createServiceDataObject("lms.contratacaoveiculos.modeloMeioTransporteService.findTipoMeioTranspByMeio","findTipoMeioTranspByMeioBack", data);
			xmit({serviceDataObjects:[sdo]});			
	}
	
	function findTipoMeioTranspByMeioBack_cb(data, error){
		if (error != undefined) {
			alert(error);
		}
		comboboxLoadOptions({e:document.getElementById("tipoMeioTransporte.idTipoMeioTransporte"), data:data});			
	}
	
	/**************************************************************************************************************************
	 * Funções referentes ao comportamento da lookup de controle de carga
	 **************************************************************************************************************************/	
	function sgFilialOnChangeHandler() {
		if (getElementValue("solicitacaoContratacao.filial.sgFilial")=="") {
			disableNrSolicitacao(true);
			deletaCamposSolicitacaoContratacao();
		} else {
			disableNrSolicitacao(false);
		}
		return lookupChange({e:document.forms[0].elements["solicitacaoContratacao.filial.idFilial"]});
	}

	function disableNrSolicitacao(disable) {
		setDisabled("solicitacaoContratacao.idSolicitacaoContratacao", disable);
	}
	
	function disableNrSolicitacao_cb(data, error) {
		if (data.length==0) disableNrSolicitacao(false);
		return lookupExactMatch({e:document.getElementById("solicitacaoContratacao.filial.idFilial"), data:data});
	}
</script>

