<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.configuracoes.manterParametrosBoletoFilialAction">

	<adsm:form action="/configuracoes/manterParametrosBoletoFilial" idProperty="idParametroBoletoFilial" onDataLoadCallBack="myDataLoadCallBack">
	
		
		<adsm:hidden property="historicoFiliais.vigenteEm"/>
		<adsm:hidden  property="dataAtual" serializable="false"/>
	
	
        <adsm:lookup property="filial" 
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.configuracoes.manterParametrosBoletoFilialAction.findLookupFilial" 
					 dataType="text"  
					 label="filial" 
					 required="true"
					 size="3" 
					 action="/municipios/manterFiliais" 
					 labelWidth="15%"
					 width="35%" 
					 minLengthForAutoPopUpSearch="3" 
					 exactMatch="false" 
					 maxLength="3">
					 
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" 
								  modelProperty="pessoa.nmFantasia" />
								  
			<adsm:propertyMapping  modelProperty="historicoFiliais.vigenteEm"
				criteriaProperty="dataAtual" />								  
								  
			<adsm:textbox dataType="text" 
						  property="filial.pessoa.nmFantasia" 
						  size="30" 
						  serializable="true" 
						  disabled="true"/>
						  
		</adsm:lookup>
        
		<adsm:range label="vigencia" labelWidth="15%" width="35%">
			<adsm:textbox property="dtVigenciaInicial" dataType="JTDate" required="true" />
			<adsm:textbox property="dtVigenciaFinal" dataType="JTDate"/>
		</adsm:range>
		
		<adsm:checkbox property="blValorLiquido" label="blValorLiquido" labelWidth="15%" width="35%"/>
		
		<adsm:checkbox property="blWorkflowCancelamento" label="aprovarCancelamento" width="35%"/>

		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="myStore"/>
			<adsm:button caption="limpar" id="btnLimpar" onclick="myNewButton();" buttonType="newButton" disabled="false"/>
			<adsm:removeButton/>	
		</adsm:buttonBar>
		
	</adsm:form>

</adsm:window>
<script>

//	busca a data atual para as lookups de filial
	function findDataAtual(data, error){
	
		var dados = new Array();
    
        var sdo = createServiceDataObject("lms.configuracoes.manterParametrosBoletoFilialAction.findDataAtual",
                                          "setaDataAtual",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
		
	}
	
	//seta a data atual
	function setaDataAtual_cb(data,error){

		setElementValue("dataAtual",data._value);
		setFocusOnFirstFocusableField(document);
	
	}
	
	
/** Store personalizado */
function myStore_cb(data, erro){
	store_cb(data, erro);
	
	if( erro == undefined ){
		validatePermissionEdit('ST');
	}

	
}


function myDataLoadCallBack_cb(data, errors){
	onDataLoad_cb(data, errors);
	validatePermissionEdit();
}

/**
 * 
 */
function validatePermissionEdit(daOnde){
	
	var _daOnde = null;
	
	if( daOnde == undefined ){
		_daOnde = 'DL';//Veio do data load call back 
	} else {
		_daOnde = daOnde;
	}

	var sdo = createServiceDataObject("lms.configuracoes.manterParametrosBoletoFilialAction.validatePermissionEdit",
			"validatePermissionEdit", { 
			dtVigenciaInicial:getElementValue("dtVigenciaInicial"), 
			dtVigenciaFinal:getElementValue("dtVigenciaFinal"),
			daOnde:_daOnde } );
	xmit({serviceDataObjects:[sdo]});
}


/**
 *
 */
function validatePermissionEdit_cb(data, errors){
	
	if (data.result == "M") {
		enabledDisabledFilds(true);		
		setDisabled("dtVigenciaFinal", true); 
	} 
	
	// Caso a dtVigenciaInicial seja menor do que a data atual, só é permitido edtitar a dtVigenciaFinal
	if(data.result == "T"){
		enabledDisabledFilds(true);		
		setDisabled("dtVigenciaFinal", false);
	}
 	
	if( data.daOnde == 'ST' ){
		setFocus('btnLimpar',true,true);
	} else if( data.daOnde == 'DL' ){
		setFocusOnFirstFocusableField(document);
	}

}




function initWindow(eventObj){
	if(eventObj.name == "gridRow_click"){
		enabledDisabledFilds(false);
	}

	if(eventObj.name == "tab_click" || eventObj.name == 'newButton_click' || eventObj.name == 'removeButton' ){
		enabledDisabledFilds(false);
		setDisabled("dtVigenciaFinal", false);
	}	

	if( eventObj.name != 'storeButton' ){		
			findDataAtual();
	}
	
	setFocusOnFirstFocusableField(document);

}
/*
	Função para o retorno do excluir
*/
function removeById_cb(map, errorMsg) {
	if (errorMsg == null){
		enabledDisabledFilds(false);
		newButtonScript(document, true, {name:"removeButton"});
	} else {
		alert(errorMsg);
	}
	setFocusOnFirstFocusableField(document);
}


function myNewButton(){
	enabledDisabledFilds(false);
	newButtonScript(document, true, {name:"newButton_click"});
}

function enabledDisabledFilds(boolean){
	setDisabled("filial.idFilial", boolean);
	setDisabled("dtVigenciaInicial", boolean);
	setDisabled("blValorLiquido", boolean);
	setDisabled("blWorkflowCancelamento", boolean);
}
</script>