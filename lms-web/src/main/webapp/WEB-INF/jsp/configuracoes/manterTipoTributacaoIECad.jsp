<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.configuracoes.manterTipoTributacaoIEAction" onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/configuracoes/manterTipoTributacaoIE" idProperty="idTipoTributacaoIE" onDataLoadCallBack="myDataLoadCallBack">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-27096"/>
		</adsm:i18nLabels>
		
  	    <adsm:hidden property="inscricaoEstadual.pessoa.idPessoa" serializable="true"/>
  	    <adsm:hidden property="labelPessoaTemp" serializable="false"/>
  	    <adsm:hidden property="confirmacao" serializable="true"/>
  	    <adsm:label key="branco" style="width:0"/>
		<td colspan="20" id="labelPessoa" class="FmLbRequerido"></td>
		<adsm:textbox 
					dataType="text" 
		            property="inscricaoEstadual.pessoa.nrIdentificacao" 
		            size="20" 
		            serializable="false"
		            required="true"
		            maxLength="20" 
		            width="80%">
		              
		            <adsm:textbox 
		            			dataType="text" 
		              			property="inscricaoEstadual.pessoa.nmPessoa" 
		              			serializable="false"
		              			size="60" 
		              			maxLength="20"/>
		</adsm:textbox>
		              
		<adsm:hidden property="inscricaoEstadual.idInscricaoEstadual" serializable="true"/>
		<adsm:textbox label="ie"  
		              dataType="text" 
		              property="inscricaoEstadual.nrInscricaoEstadual" 
		              size="20" 
		              serializable="true"
		              required="true"
		              maxLength="50" 
		              labelWidth="20%" 
		              width="70%"/>
		              
		<adsm:combobox 
					property="tpSituacaoTributaria" 
					label="tpSituacaoTributaria" 
					required="true"
					domain="DM_SIT_TRIBUTARIA_CLIENTE"
					onchange="habilitarBlIncentivada();"
					labelWidth="20%" 
					width="70%"
					boxWidth="350"/>
					
		<adsm:combobox 
					label="tipoTributacao" 
					property="tipoTributacaoIcms.idTipoTributacaoIcms" 
					service="lms.configuracoes.manterTipoTributacaoIEAction.findComboTipoTributacaoIcmsOnlyActivaValues" 
					onlyActiveValues="true" 
					optionLabelProperty="dsTipoTributacaoIcms" 
					optionProperty="idTipoTributacaoIcms" 
					labelWidth="20%" 
					width="30%" 
					boxWidth="200"/>	  	
					
					
		<adsm:checkbox 
					property="blIsencaoExportacoes" 
					disabled="false" 
					label="blIsencaoExportacoes" 
					labelWidth="20%"  
					width="30%" />
		
		<adsm:checkbox 
					property="blAceitaSubstituicao" 
					disabled="false" 
					label="blAceitaSubstituicao" 
					labelWidth="20%"  
					width="30%" />						              
		<adsm:checkbox 
					property="blIncentivada" 
					disabled="true" 
					label="blIncentivada" 
					labelWidth="20%"  
					width="30%" />
					
		<adsm:range label="vigencia" labelWidth="20%" width="30%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
					
		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="myStore"/>
			<adsm:button caption="limpar" id="btnLimpar" onclick="myNewButton();" buttonType="newButton" disabled="false"/>
			<adsm:removeButton/>
		</adsm:buttonBar>

	</adsm:form>
	
</adsm:window>

<script><!--

function habilitarBlIncentivada(){
	if("CI" == getElementValue("tpSituacaoTributaria")){		
		document.getElementById("blIncentivada").checked = true;
	}else{		
		document.getElementById("blIncentivada").checked = false;
	}	
}

function myStore_cb(data, erro, key){
	if (key == "LMS-27095") {
		if (confirm(erro)) {
			setElementValue("confirmacao", "true");
			storeButtonScript("lms.configuracoes.manterTipoTributacaoIEAction.store", "myStore", document.forms[0]);
		} else {
			setFocus("tpSituacaoTributaria");
		}
		return;
	}

	store_cb(data, erro);
	
	if( erro == undefined ){
		validatePermissionEdit("true");
	}

	setElementValue("confirmacao", "false");
}

function myOnPageLoad_cb(data, erro){
	onPageLoad_cb();
	document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
}

function myDataLoadCallBack_cb(data, errors){
	onDataLoad_cb(data, errors);
	validatePermissionEdit();
	setElementValue("confirmacao", "false");

	var st = data["tpSituacaoTributaria"]["value"];
	if(st != undefined && st == "CI"){
		document.getElementById("blIncentivada").checked = true;		
	}else{
		document.getElementById("blIncentivada").checked = false;
}

}

/**
 * Valida a regra 1.2
 */
function validatePermissionEdit(store){
var retorno = "validatePermissionEdit";
if ( store == "true" ) retorno = "validatePermissionEdit1"

	var sdo = createServiceDataObject("lms.configuracoes.manterTipoTributacaoIEAction.validatePermissionEdit",
			retorno, { dtVigenciaInicial:getElementValue("dtVigenciaInicial") } );
	xmit({serviceDataObjects:[sdo]});
}

function initWindow(eventObj){
	if(eventObj.name == "gridRow_click"){
		enabledDisabledFilds(false);

		/*Campo deve sempre esta desabilitado*/
		setDisabled("blIncentivada", true);
	}

	if(eventObj.name == "tab_click" || eventObj.name == 'newButton_click' || eventObj.name == 'removeButton' ){
		enabledDisabledFilds(false);
		setElementValue('blAceitaSubstituicao',true);
	}	

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


/**
 * Valida a regra 1.2
 */
function validatePermissionEdit_cb(data, errors){
	/**
	 * Caso a dtVigenciaInicial seja menor do que a data atual, só é permitido edtitar a dtVigenciaFinal
	 */
	if(data.result == "T"){
		enabledDisabledFilds(true);
	}
}
/**
 * Valida a regra 1.2 //depois do store seta o focus para limpar
 */
function validatePermissionEdit1_cb(data, errors){
	/**
	 * Caso a dtVigenciaInicial seja menor do que a data atual, só é permitido edtitar a dtVigenciaFinal
	 */
	if(data.result == "T"){
		enabledDisabledFilds(true);
	}
	setFocus('btnLimpar',true,true);
}


function myNewButton(){
	enabledDisabledFilds(false);
	validateBlAtualizacaoCountasse();
}

function validateBlAtualizacaoCountasse(){
	var sdo = createServiceDataObject("lms.configuracoes.manterTipoTributacaoIEAction.validateBlAtualizacaoCountasse", 
	                                  "validateBlAtualizacaoCountasse", 
	                                  {idPessoa:getElementValue("inscricaoEstadual.pessoa.idPessoa")});
	xmit({serviceDataObjects:[sdo]}); 		
}

function validateBlAtualizacaoCountasse_cb(data, error){
	if (error != undefined) {
		alert(i18NLabel.getLabel("LMS-27096"));
	}
	newButtonScript();
}
	
function enabledDisabledFilds(boolean){
	setDisabled("tpSituacaoTributaria", boolean);
	setDisabled("tipoTributacaoIcms.idTipoTributacaoIcms", boolean);
	setDisabled("blIsencaoExportacoes", boolean);
	setDisabled("blAceitaSubstituicao", boolean);
	setDisabled("dtVigenciaInicial", boolean);
	
	setFocusOnFirstFocusableField(document);

}
--></script>