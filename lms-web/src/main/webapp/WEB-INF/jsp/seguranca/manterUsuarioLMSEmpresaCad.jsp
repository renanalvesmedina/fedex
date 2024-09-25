<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterUsuarioLMSEmpresaAction" onPageLoadCallBack="controlaEmpresaPadrao" >
	<adsm:form action="/seguranca/manterUsuarioLMSEmpresa" idProperty="idUsuarioEmpresa" onDataLoadCallBack="onDataLoadLocal">
	
	<adsm:i18nLabels>
	    <adsm:include key="LMS_SEG_EMPRESA_PADRAO"/>
	    <adsm:include key="LMS_SEG_FILIAL_REGIONAL_OBG"/>
	    <adsm:include key="LMS_SEG_FILIAL_FILIAL_PADRAO_OBG"/>	    
		  <adsm:include key="filiais"/>
		  <adsm:include key="filialPadrao"/>
		  <adsm:include key="regionais"/>	  
		  <adsm:include key="empresaJaPadrao"/>		      
		  <adsm:include key="acessoIrrestritoFiliais"/>
		  <adsm:include key="exclusaoEmpresaPadrao"/>		       	      
	</adsm:i18nLabels>		

		<!-- Esses dados vem da tela de manterUsuarioLMSCad.jsp! nmFantasia é utilizado como controle(flag). -->
		<adsm:hidden  property="empresaPadraoAux"  serializable="true" />		
		<adsm:hidden  property="nmFantasia"      serializable="true" />		
		<adsm:hidden property="flag" value="" serializable="false"/>
		<adsm:hidden  property="tpEmpresa"  value=""  serializable="false"/>
		<adsm:hidden  property="idUsuarioLMS"    serializable="true" />
		<adsm:hidden property="tpSituacao" serializable="false" value="A"/>
		<adsm:hidden property="edicao" serializable="false" value="false"/>
		<!-- adsm:textbox property="loginUsuarioLMS" serializable="true" label="usuario" disabled="false" dataType="text" size="20" width="20" /-->
		 
		<adsm:lookup size="20" maxLength="60" width="60%" labelWidth="180"
					 property="usuarioADSM" 
					 idProperty="idUsuario"
					 criteriaProperty="login"
					 action="/seguranca/manterUsuarioADSM" 
					 service="lms.seguranca.manterUsuarioLMSAction.findLookupUsuarioAdsm" 
					 dataType="text" 
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 label="loginUsuarioADSM"
					 disabled="true"
		>	
			<adsm:propertyMapping relatedProperty="tpCategoriaUsuario" modelProperty="tpCategoriaUsuario.value" />
			<adsm:propertyMapping relatedProperty="usuarioADSM.nmUsuario" modelProperty="nmUsuario" />
 			<adsm:textbox property="usuarioADSM.nmUsuario" size="30" dataType="text" disabled="true" />
		</adsm:lookup>
				 
		<adsm:lookup 
						action="/seguranca/manterUsuarioLMSEmpresa" 
						cmd="empresa"
						dataType="text" 
						exactMatch="true" 
						property="empresaByIdEmpresaCadastrada" 
						idProperty="idEmpresa" 
						criteriaProperty="pessoa.nrIdentificacao"
						relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
						labelWidth="180" 
						label="empresa" 
						maxLength="20" 
						service="lms.seguranca.manterUsuarioLMSEmpresaAction.findLookupEmpresa" 
						size="20" 
						width="85%"
 					  onDataLoadCallBack="onChangeUsuarioLMSEmpresa"
 					  afterPopupSetValue="onChangeUsuarioLMSEmpresa"
						required="true"		
						onchange="return onChangeEmpresa( this )"				
			>
				<adsm:propertyMapping  relatedProperty="empresaByIdEmpresaCadastrada.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" 		/>
				<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="tpEmpresa" relatedProperty="tpEmpresa"/>
				<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>
		    <adsm:textbox dataType="text" disabled="true" property="empresaByIdEmpresaCadastrada.pessoa.nmPessoa"	size="30"			/>
		</adsm:lookup> 		 

	 	<adsm:checkbox property="empresaPadrao" label="empresaPadrao"	labelWidth="180" width="20%" onclick="return controlaEmpresaPadrao();" />

	 	<adsm:checkbox property="blIrrestritoFilial" 	label="acessoIrrestritoFiliais" labelWidth="180" width="80%" onclick="onChangeAcessoFilial();"  />  
	 	
		<adsm:listbox label="regional(is)" size="4" boxWidth="170" width="80%" labelWidth="180" 
	         		property="regionalUsuario" 
	     			  optionProperty="idRegionalUsuario"
	     			  optionLabelProperty="siglaDescricao"	
	     			  onContentChange="onContentChangeCustomRegional"     			  
	     			 >
	     				<adsm:combobox
                 	property="regional.idRegional" 
                  optionLabelProperty="siglaDescricao" optionProperty="idRegional" 
                  service="lms.municipios.manterRegionaisAction.findRegionaisVigentes"
                  required="false" boxWidth="170">
                  <adsm:propertyMapping criteriaProperty="empresaByIdEmpresaCadastrada.idEmpresa"	modelProperty="idEmpresa"  />
              </adsm:combobox>                            		  	                  
        </adsm:listbox>
		<adsm:hidden property="regionaisSelecionadas" value="" />
		
		<adsm:listbox label="filiais" size="4" boxWidth="170" width="40%" labelWidth="180"
					  property="filiaisUsuario" 
					  optionProperty="idFilialUsuario"
					  optionLabelProperty="sgFilial"
					  onContentChange="onContentChangeCustom"	 >
				<adsm:lookup property="filial"
		  				 idProperty="idFilial"
		  	       criteriaProperty="sgFilial"
		  				 action="/seguranca/manterUsuarioLMSEmpresa"
		  				 cmd="filial"
		  				 service="lms.seguranca.manterUsuarioLMSEmpresaAction.findLookupFilial"
		  				 dataType="text"
							 exactMatch="false"
							 minLengthForAutoPopUpSearch="3"
		  				 size="30" maxLength="3"
		  				 serializable="true"
		  				 required="false"
		  				>
		 			<adsm:propertyMapping criteriaProperty="empresaByIdEmpresaCadastrada.idEmpresa"  modelProperty="empresa.idEmpresa"  />
		 			<adsm:propertyMapping criteriaProperty="empresaByIdEmpresaCadastrada.pessoa.nrIdentificacao"  modelProperty="empresa.pessoa.nrIdentificacao"  />
					<adsm:propertyMapping criteriaProperty="empresaByIdEmpresaCadastrada.pessoa.nmPessoa"   	  modelProperty="empresa.pessoa.nmPessoa" />
  				</adsm:lookup>
		</adsm:listbox>		
		<adsm:hidden property="filiaisSelecionadas" value="" />
		
		<adsm:lookup property="filial"
 					 idProperty="idFilial"
           criteriaProperty="sgFilial"
 					 action="/seguranca/manterUsuarioLMSEmpresa"
 					 cmd="filial"
 					 service="lms.seguranca.manterUsuarioLMSEmpresaAction.findLookupFilial"
 					 dataType="text"
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
 					 size="10" maxLength="3"
 					 serializable="true"
 					 label="filialPadrao"
 					 labelWidth="180"
 					 width="70%"
 					 required="true"
  		>
			<adsm:propertyMapping criteriaProperty="empresaByIdEmpresaCadastrada.pessoa.nrIdentificacao"  modelProperty="empresa.pessoa.nrIdentificacao"  />
 			<adsm:propertyMapping criteriaProperty="empresaByIdEmpresaCadastrada.idEmpresa"  modelProperty="empresa.idEmpresa"  />			
			<adsm:propertyMapping criteriaProperty="empresaByIdEmpresaCadastrada.pessoa.nmPessoa"   modelProperty="empresa.pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="filiaisSelecionadas"   	  modelProperty="filiaisSelecionadas" />
			<adsm:propertyMapping criteriaProperty="regionaisSelecionadas"    modelProperty="regionaisSelecionadas" />
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" 	modelProperty="pessoa.nmFantasia"/>
   			<adsm:textbox dataType="text" serializable="false" property="filial.pessoa.nmFantasia" size="30" disabled="true"/> 
		</adsm:lookup>

		<adsm:buttonBar>
			<adsm:button id="detalhamentoFiliais" caption="detalhamentoFiliaisRegionais" action="/seguranca/manterUsuarioLMSEmpresaFilial" cmd="main" disabled="true" >
				<adsm:linkProperty src="idUsuarioEmpresa" target="idEmpresaUsuario"/>
			</adsm:button>	
			<adsm:button  id="btnSalvar" onclick="return validaObrigatoriedade();" caption="salvar" />
			<adsm:newButton/>
			<!--adsm:removeButton /-->
			<adsm:button id="btnRemover" onclick="return findEmpresaPadrao();" caption="excluir" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

function onChangeEmpresa( data ){	
	if( data != undefined && data.value == "" ){
		desabilitaCampos();
		var listBoxRegional 		= document.getElementById('regionalUsuario');
		var listBoxFilial 			= document.getElementById('filiaisUsuario');
		var blIrrestritoFilial = document.getElementById('blIrrestritoFilial');
		//var idUsuarioEmpresa = document.getElementById('idUsuarioEmpresa');		
		resetListBoxValue(listBoxRegional);
		resetListBoxValue(listBoxFilial);
		resetValue( blIrrestritoFilial );
		//resetValue( idUsuarioEmpresa );
		setElementValue("empresaPadraoAux", false);
	}
	return lookupChange({e:getElement("empresaByIdEmpresaCadastrada.idEmpresa")});
}

function alteraIdsDasFiliaisSelecionadas(){
	var dataListbox = document.getElementById('filiaisUsuario');
	var blIrrestritoFilial = document.getElementById('blIrrestritoFilial');
	setElementValue("filiaisSelecionadas", "");
	var filiaisSelecionadas = document.getElementById('filiaisSelecionadas');
	if( blIrrestritoFilial.checked == false ){
		for( i=0; i < dataListbox.length; i++ ){
			var data = dataListbox[i].data;
			if( filiaisSelecionadas == null || filiaisSelecionadas.value=="" ){
				filiaisSelecionadas.value = data.idFilial;
			}else{
				filiaisSelecionadas.value += ","+ data.idFilial;
			}
		}
		setElementValue("filiaisSelecionadas", filiaisSelecionadas.value);
	}else{
		setElementValue("filiaisSelecionadas", "");
	}
	
}

function onContentChangeCustomRegional( event ){ 
	var acessoIrrestritoFiliais 	= document.getElementById('blIrrestritoFilial');
  var filialPadrao = document.getElementById('filial.idFilial');	
  var dataListbox = document.getElementById('regionalUsuario'); 
  var dataListboxFilial = document.getElementById('filiaisUsuario');   
	if( (event.name == "modifyButton_afterClick") || (event.name == "deleteButton_afterClick") ){
		if( event.src != null ){
			var data = event.src.data;
			if ( dataListbox != null || dataListboxFilial != null){
				if ( dataListbox.length > 0 || dataListboxFilial.length > 0 || acessoIrrestritoFiliais.checked == true) {	
					setDisabled( filialPadrao, false);					
				}else{
					resetValue( filialPadrao );				
					setDisabled( filialPadrao, true);
				}
			}
		}
		alteraIdsDasRegionaisSelecionadas();
		verificaSeFilialPadraoEhValida();
	}

}

/*
 * Quando holver um ou mais elementos na listbox habilita a lookup "Filial padrão", caso contrário desabilita e limpa.
*/
function onContentChangeCustom( event ){ 
	var acessoIrrestritoFiliais 	= document.getElementById('blIrrestritoFilial');
  var filialPadrao = document.getElementById('filial.idFilial');	
  var dataListbox = document.getElementById('filiaisUsuario');  
  var dataListboxRegional = document.getElementById('regionalUsuario'); 
	if( (event.name == "modifyButton_afterClick") || (event.name == "deleteButton_afterClick") ){
		if( event.src != null ){
			var data = event.src.data;
			var habilitar = false;
			if ( dataListbox != null || dataListboxRegional != null ){
				if ( dataListbox.length > 0 || dataListboxRegional.length > 0 || acessoIrrestritoFiliais.checked == true) {	
					setDisabled( filialPadrao, false);					
				}else{
					resetValue( filialPadrao );				
					setDisabled( filialPadrao, true);
				}
			}
		}
		alteraIdsDasFiliaisSelecionadas();
		verificaSeFilialPadraoEhValida();
	}

}

function verificaSeFilialPadraoEhValida(){
	var nrIdentificacao = document.getElementById('empresaByIdEmpresaCadastrada.pessoa.nrIdentificacao');
	var sgFilial =  document.getElementById('filial.sgFilial');
	var nmPessoa = document.getElementById('empresaByIdEmpresaCadastrada.pessoa.nmPessoa');
	var idEmpresa = document.getElementById('empresaByIdEmpresaCadastrada.idEmpresa');
	var	filiaisSelecionadas = document.getElementById('filiaisSelecionadas');
	var regionaisSelecionadas = document.getElementById('regionaisSelecionadas');
	var filialPadrao = document.getElementById('filial.idFilial');
	if( filialPadrao.value != null && filialPadrao.value != "" ){
   data = new Array();
   setNestedBeanPropertyValue(data, "sgFilial", sgFilial.value);
   setNestedBeanPropertyValue(data, "empresa.pessoa.nrIdentificacao", nrIdentificacao.value);
   setNestedBeanPropertyValue(data, "empresa.pessoa.nmPessoa", nmPessoa.value);
   setNestedBeanPropertyValue(data, "empresa.idEmpresa", idEmpresa.value);
   setNestedBeanPropertyValue(data, "filiaisSelecionadas", filiaisSelecionadas.value);
   setNestedBeanPropertyValue(data, "regionaisSelecionadas", regionaisSelecionadas.value);
                  
   var sdo = createServiceDataObject("lms.seguranca.manterUsuarioLMSEmpresaAction.findLookupFilial", "verificaSeFilialPadraoEhValida", data);
   xmit({serviceDataObjects:[sdo]});
  }
}

function verificaSeFilialPadraoEhValida_cb( data,errorMessage, errorCode, eventObj ){
	var filialPadrao = document.getElementById('filial.idFilial');
	if( data == null || data.length == 0 ){
			resetValue( filialPadrao );
	}
}

function alteraIdsDasRegionaisSelecionadas(){
	var dataListbox = document.getElementById('regionalUsuario');
	var blIrrestritoFilial = document.getElementById('blIrrestritoFilial');
	setElementValue("regionaisSelecionadas", "");
	var regionaisSelecionadas = document.getElementById('regionaisSelecionadas');
	if( blIrrestritoFilial.checked == false ){
		for( i=0; i < dataListbox.length; i++ ){
			var data = dataListbox[i].data;
			if( regionaisSelecionadas == null || regionaisSelecionadas.value=="" ){
				regionaisSelecionadas.value = data.idRegional;
			}else{
				regionaisSelecionadas.value += ","+ data.idRegional;
			}
		}
		setElementValue("regionaisSelecionadas", regionaisSelecionadas.value);
	}else{
		setElementValue("regionaisSelecionadas", "");
	}
	
}

getElement('nmFantasia').masterLink = true;

/*
 * Função chamada na edição de um registro no load dos dados.
*/
function onDataLoadLocal_cb(data,errorMessage, errorCode, eventObj){
	localOnDataLoad_cb( data,errorMessage, errorCode, eventObj );

	//Esta passando um objeto vasio no parametro data porque os campos da tela são preenchidos
	// na função localOnDataLoad_cb().
	onDataLoad_cb( new Object(), errorMessage, errorCode, eventObj );	
	
	habilitaAcessoFiliais();
	alteraIdsDasFiliaisSelecionadas();
	alteraIdsDasRegionaisSelecionadas();
	acessoIrrestritoFiliais();
}

/*
 *	Função chamada na inicialização da janela.
*/
function initWindow(event) {	
	setDisabled('btnSalvar',false);
	setDisabled('btnRemover',false);

	if ( event.name == 'tab_click' || event.name == 'removeButton' ) {
		controlaEmpresaPadrao();
		desabilitaControles();
		alteraIdsDasFiliaisSelecionadas();
		alteraIdsDasRegionaisSelecionadas();
	}else if ( event.name == 'newButton_click' ) {
		controlaEmpresaPadrao();
		desabilitaControles();
		alteraIdsDasFiliaisSelecionadas();
		alteraIdsDasRegionaisSelecionadas();
	}else	if( event.name == 'gridRow_click' || event.name == 'storeButton' ){
		var empresaPadrao = document.getElementById('empresaPadrao');
		setDisabled( empresaPadrao, false);		
		setElementValue("edicao", "true");
		habilitaAcessoFiliais();
	}
	
}

function habilitaAcessoFiliais(){
	var acessoIrrestritoFiliais 	= document.getElementById('blIrrestritoFilial');	
	var detalhamentoFiliais 	= document.getElementById('detalhamentoFiliais');	
	if(acessoIrrestritoFiliais.checked == true){		
		setDisabled( detalhamentoFiliais, true);	
	}else{
		setDisabled( detalhamentoFiliais, false);
	}
}

function onChangeUsuarioLMSEmpresa(){
  afterDataLoadUsuarioLMSEmpresa();	
}

function onChangeUsuarioLMSEmpresa_cb( data ){
	empresaByIdEmpresaCadastrada_pessoa_nrIdentificacao_exactMatch_cb(data);
 	if( data.length == 1 ){	  	
		onChangeEmpresa();
 		afterDataLoadUsuarioLMSEmpresa();	
 	}
}

/*
 *	Função chamada após o carregamentos dos dados, irá setar os controles no seu estado padrão
*/
function afterDataLoadUsuarioLMSEmpresa() {
	var blIrrestritoFilial = document.getElementById('blIrrestritoFilial');
	var regionais = document.getElementById('regionalUsuario');
	var comboRegional = document.getElementById('regionalUsuario_regional.idRegional');	
	var filiais = document.getElementById('filiaisUsuario');
	var lookupFilialUsuario = document.getElementById('filiaisUsuario_filial.idFilial');
	var idEmpresa = getElementValue("empresaByIdEmpresaCadastrada.idEmpresa");
	var filialPadrao = document.getElementById('filial.idFilial');
	var empresaPadrao = document.getElementById('empresaPadrao');
	
	if ( idEmpresa != null && idEmpresa != "") {
		// Verificar se o usuário logado tem acesso a todas as filiais
		acessoIrrestritoFiliais();
		resetValue( regionais );
		setDisabled( regionais, false);
		setDisabled( comboRegional, false);
		setDisabled( lookupFilialUsuario, false);
		setDisabled( filiais, false);
		resetValue( filiais );
	  setDisabled( filialPadrao, true);
 		resetValue( filialPadrao );
 		setDisabled( empresaPadrao, false);
	}
	buscaIdEmpresaUsuario();
	setFocus(empresaPadrao);
}

function buscaIdEmpresaUsuario(){
	  var idEmpresa = getElementValue("empresaByIdEmpresaCadastrada.idEmpresa");
    var idUsuario = getElementValue("usuarioADSM.idUsuario");
    
   	data = new Array();
    setNestedBeanPropertyValue(data, "idEmpresa", idEmpresa);
    setNestedBeanPropertyValue(data, "idUsuario", idUsuario);
    var sdo = createServiceDataObject("lms.seguranca.manterUsuarioLMSEmpresaAction.findIdEmpresaUsuario", "idEmpresaUsuario", data);
    xmit({serviceDataObjects:[sdo]});
    
}

function idEmpresaUsuario_cb( data, erros ){

	if (erros != undefined) {
    	alert(erros);
      return false;
	}
	
	if ( getNestedBeanPropertyValue(data, "_value" ) != undefined  && getNestedBeanPropertyValue(data, "_value" )!=0 ) {	
		if( getNestedBeanPropertyValue(data, "_value" ) == getElementValue("idUsuarioEmpresa") ){
			setElementValue("idUsuarioEmpresa", getNestedBeanPropertyValue(data, "_value" ));
		}else if( getElementValue("edicao") == false ) {
			setElementValue("idUsuarioEmpresa", "");
		}
	}
}

function acessoIrrestritoFiliais(){	
  var idEmpresa = getElementValue("empresaByIdEmpresaCadastrada.idEmpresa");
  var blIrrestritoFilial = document.getElementById('blIrrestritoFilial');
 	if ( idEmpresa != null && idEmpresa != "") {
   	data = new Array();
    setNestedBeanPropertyValue(data, "idEmpresa", idEmpresa);
    var sdo = createServiceDataObject("lms.seguranca.manterUsuarioLMSEmpresaAction.temAcessoIrrestritoFilial", "acessoIrestrito", data);
    xmit({serviceDataObjects:[sdo]});
 	} else {
		setDisabled( blIrrestritoFilial, true);
 	}  	
}

function acessoIrestrito_cb(data, erros){
	var blIrrestritoFilial = document.getElementById('blIrrestritoFilial');
	if (erros != undefined) {
    	alert(erros);
      return false;
	}
	if ( getNestedBeanPropertyValue(data, "acessoIrestrito" ) != undefined  ) {	
		if( getNestedBeanPropertyValue(data, "acessoIrestrito" ) != "" ){
			var temAcesso = getNestedBeanPropertyValue(data, "acessoIrestrito" );
			if( temAcesso == "true" ){
				setDisabled( blIrrestritoFilial, false);
				return true;
			}else{
				setDisabled( blIrrestritoFilial, true);
				resetValue( blIrrestritoFilial );
				return true;
			}
		}
	}
	setDisabled( blIrrestritoFilial, true);
	return true;
}

/*
 * Desabilita os campos:
 *	- Checkbox "Acesso Irrestrito a Filiais"
 *	- Todo o campo "Regional(is)
 *	- Todo o campo "Filial(is)
 *	- Campo "Filial padrão"
*/
function desabilitaControles(){
	var blIrrestritoFilial = document.getElementById('blIrrestritoFilial');
	var empresaPadrao = document.getElementById('empresaPadrao');
	var regionais = document.getElementById('regionalUsuario');
	var comboRegional = document.getElementById('regionalUsuario_regional.idRegional');	
	var filiais = document.getElementById('filiaisUsuario');
	var lookupFilialUsuario = document.getElementById('filiaisUsuario_filial.idFilial');
	var filialPadrao = document.getElementById('filial.idFilial');
	
	setDisabled( blIrrestritoFilial, true);
	setDisabled( comboRegional, true);
	setDisabled( regionais, true);
	setDisabled( lookupFilialUsuario, true);
	setDisabled( filiais, true);
  setDisabled( filialPadrao, true);
  setDisabled( empresaPadrao, true);
	setDisabled('btnRemover',true);		
}

function desabilitaCampos(){
	var blIrrestritoFilial = document.getElementById('blIrrestritoFilial');
	var empresaPadrao = document.getElementById('empresaPadrao');
	var regionais = document.getElementById('regionalUsuario');
	var comboRegional = document.getElementById('regionalUsuario_regional.idRegional');	
	var filiais = document.getElementById('filiaisUsuario');
	var lookupFilialUsuario = document.getElementById('filiaisUsuario_filial.idFilial');
	var filialPadrao = document.getElementById('filial.idFilial');
	
	setDisabled( blIrrestritoFilial, true);
	setDisabled( comboRegional, true);
	setDisabled( regionais, true);
	setDisabled( lookupFilialUsuario, true);
	setDisabled( filiais, true);
  setDisabled( filialPadrao, true);
  setDisabled( empresaPadrao, true);
  
}

/*
 * Verifica se o usuário LMS possui uma empresa padrão setada. Faz uma chamada AJAX ao servidor para verificar isso.
*/
function controlaEmpresaPadrao(){	
 	if (getElementValue('empresaPadraoAux') != 'true') {
    var idUsuarioLMS = getElementValue("idUsuarioLMS");
   	data = new Array();
    setNestedBeanPropertyValue(data, "idUsuarioLMS",idUsuarioLMS);
    var sdo = createServiceDataObject("lms.seguranca.manterUsuarioLMSEmpresaAction.getEmpresaPadrao", "empresaPadrao", data);
    xmit({serviceDataObjects:[sdo]});
 	} else {
		setElementValue("empresaPadrao", true);
 	}  	
}

/*
 *	Função call back invocada no retorno de uma chamada ao servidor.
 *	Verifica se já existe uma empresa padrão cadastrado no banco, se existe dá a opção do usuário
 *	substituir a existente pela atual.
*/
function empresaPadrao_cb(data, erros) {

	if (erros != undefined) {
    	alert(erros);
        return false;
	}       
		
	if ( getNestedBeanPropertyValue(data, "idEmpresa" ) != undefined  ) {	
		if ( ( getNestedBeanPropertyValue(data, "idEmpresa" ) != getElementValue("empresaByIdEmpresaCadastrada.idEmpresa") ) && 
			( getElementValue("empresaByIdEmpresaCadastrada.idEmpresa") != "" && 
			getElementValue("empresaByIdEmpresaCadastrada.idEmpresa") != undefined )&& 
			getNestedBeanPropertyValue(data, "idEmpresa" ) != ""  ) {	
		
			if ( confirm(i18NLabel.getLabel('LMS_SEG_EMPRESA_PADRAO').replace('{1}', getNestedBeanPropertyValue(data, "pessoa.nmPessoa" )))==true) {
				setElementValue("empresaPadrao", true);
				return true;
			} else {
				setElementValue("empresaPadrao", false);
				return true;
			}
			
		} else if( getElementValue("empresaByIdEmpresaCadastrada.idEmpresa") != "" ){
			alert( i18NLabel.getLabel('empresaJaPadrao') );
			setElementValue("empresaPadrao", true);
			return true;
		}else{
			setElementValue("empresaPadrao", false);
			return true;		
		}		
	} else {
		setElementValue("empresaPadrao", true);
		return true;
	}
	setElementValue("empresaPadrao", false);
} 

/*
 * Verifica se o usuário LMS possui uma empresa padrão setada. Faz uma chamada AJAX ao servidor para verificar isso.
*/
function findEmpresaPadrao(){	
  var idUsuario = document.getElementById('usuarioADSM.idUsuario').value;
 	data = new Array();
  setNestedBeanPropertyValue(data, "idUsuario",idUsuario);
  var sdo = createServiceDataObject("lms.seguranca.manterUsuarioLMSEmpresaAction.findEmpresaPadrao", "excluirEmpresaPadrao", data);
  xmit({serviceDataObjects:[sdo]});
}

/*
 *	Função call back invocada no retorno de uma chamada ao servidor.
 *	Verifica se a empresa a ser excluida é a empresa padrão do usuário não deixa
 * 	usuário excluir o registro.
*/
function excluirEmpresaPadrao_cb(data, erros) {

	if (erros != undefined) {
    	alert(erros);
        return false;
	}
	
	var idEmpresa = document.getElementById('empresaByIdEmpresaCadastrada.idEmpresa').value;
	var idUsuarioEmpresa = document.getElementById('idUsuarioEmpresa').value;	
	var excluir = true;
	if ( getNestedBeanPropertyValue(data, "_value" ) != undefined  && getNestedBeanPropertyValue(data, "_value" )!=0 ) {	
		if ( getNestedBeanPropertyValue(data, "_value" ) == idUsuarioEmpresa ) {	
		 	//gridUsuarioGridDef.getSelectedIds().ids.pop(i);
		 	excluir = false;
		 	alert(i18NLabel.getLabel('exclusaoEmpresaPadrao') );
		}
	}
	
	if( excluir == true ){
		removeButtonScript('lms.seguranca.manterUsuarioLMSEmpresaAction.removeById','removeById', 'idUsuarioEmpresa' );;
	}				
	
} 

function controlaEmpresaPadrao_cb(){
	onPageLoad_cb();	
}

function onChangeAcessoFilial(){
	var filialPadrao = document.getElementById('filial.idFilial');
	var acessoIrrestritoFiliais 	= document.getElementById('blIrrestritoFilial');	
	var detalhamentoFiliais 	= document.getElementById('detalhamentoFiliais');	

	controlaAcesso();
 	alteraIdsDasFiliaisSelecionadas();
 	alteraIdsDasRegionaisSelecionadas()
	if(acessoIrrestritoFiliais.checked == false){
		resetValue( filialPadrao );
		//setDisabled( detalhamentoFiliais, false);		
	}else{
		//setDisabled( detalhamentoFiliais, true);
	}
}

/*
 * Checkbox marcado:
 *		Desabilitar todos os componentes do campo "Regional(is)", do campo "Filial(is)" sem limpar o conteúdo.
 * Checkbox desmarcado:
 *		Habilitar novamente os campos
*/
function controlaAcesso(){

	var acessoIrrestritoFiliais 	= document.getElementById('blIrrestritoFilial');
	var comboRegional 				= document.getElementById('regionalUsuario_regional.idRegional');
	var listBoxRegional 			= document.getElementById('regionalUsuario');
	var lookupFilial 					= document.getElementById('filiaisUsuario_filial.idFilial');	
	var listBoxFilial 				= document.getElementById('filiaisUsuario');
	var filialPadrao					= document.getElementById('filial.idFilial');
		
	if(acessoIrrestritoFiliais.checked == true){	
		setDisabled(comboRegional, true);
		setDisabled(listBoxRegional, true);
		setDisabled(lookupFilial, true);			
		setDisabled(listBoxFilial, true);
		
		setDisabled(filialPadrao, false);
		
		lookupFilial.required = false;
		listBoxFilial.required = false;
		listBoxRegional.required = "false";
		listBoxFilial.required = "false";
	}else{	
		setDisabled(comboRegional, false);
		setDisabled(listBoxRegional, false);
		setDisabled(lookupFilial, false);			
		setDisabled(listBoxFilial, false); 
		if( (listBoxFilial != null && listBoxFilial.length == 0 ) && 
				( listBoxRegional != null && listBoxRegional.length == 0 ) ){
			setDisabled(filialPadrao, true);
		}else{
			setDisabled(filialPadrao, false);
		}
		
		listBoxRegional.required = "true";
		listBoxFilial.required = "true";		
		listBoxRegional.required = "false";
		listBoxFilial.required = "false"; 
	}  
	
}

function localOnDataLoad_cb(data, errorMessage, errorCode, eventObj) {    
	if (errorCode != undefined) {
        alert(errorCode + ' ' + errorMessage);
        return false;
    }	
	
	setElementValue("idUsuarioEmpresa",getNestedBeanPropertyValue(data, "idUsuarioEmpresa" ));   	
	setElementValue("empresaByIdEmpresaCadastrada.idEmpresa",getNestedBeanPropertyValue(data, "empresaByIdEmpresaCadastrada.idEmpresa" ));   	
	setElementValue("empresaByIdEmpresaCadastrada.pessoa.nrIdentificacao",getNestedBeanPropertyValue(data, "empresaByIdEmpresaCadastrada.pessoa.nrIdentificacao" ));   	
	setElementValue("empresaByIdEmpresaCadastrada.pessoa.nmPessoa",getNestedBeanPropertyValue(data, "empresaByIdEmpresaCadastrada.pessoa.nmPessoa" ));   	

	setElementValue("empresaPadrao",   getNestedBeanPropertyValue(data, "empresaPadrao" ));   	
	setElementValue("empresaPadraoAux",getNestedBeanPropertyValue(data, "empresaPadrao" ));    
	
	setElementValue("blIrrestritoFilial",getNestedBeanPropertyValue(data, "blIrrestritoFilial" ));   	

	setElementValue("filial.idFilial",getNestedBeanPropertyValue(data, "filial_idFilial" ));   	
	setElementValue("filial.sgFilial",getNestedBeanPropertyValue(data, "filial_sgFilial" ));   	
	setElementValue("filial.pessoa.nmFantasia",getNestedBeanPropertyValue(data, "filial_pessoa_nmFantasia" ));   	

	fillListBoxLookup(data,'regionais','idRegional','sgRegional','regionalUsuario');
	fillListBoxLookup(data,'filiais','idFilial','sgFilial','filiaisUsuario');	
	
	controlaAcesso();	
}   	

function montaMsgValidaObrigatoriedade(){
    var msgFiliais   =  i18NLabel.getLabel('filiais');
		var msgFilialPadrao =	i18NLabel.getLabel('filialPadrao');
    var msgCompleta  =  i18NLabel.getLabel('LMS_SEG_FILIAL_FILIAL_PADRAO_OBG').replace('{1}',msgFiliais);
    msgCompleta      =  msgCompleta.replace('{2}',msgFilialPadrao);
    return msgCompleta; 
}
 
function validaObrigatoriedadeControles(){
	/* recupera os valores dos campso. */
    var acessoIrrestritoCli = getElement('blIrrestritoFilial');
    var filiais             = getElementValue('filiaisUsuario');
    var filial           = getElementValue('filial.idFilial');        
    
    if ( filial!=undefined && filial!="" ) return 1;
    
    return 0;
}


function validaObrigatoriedade(){    
  storeButtonScript('lms.seguranca.manterUsuarioLMSEmpresaAction.store', 'store', document.forms[0]);
  limparList();
	return true;        
    
}

function limparList(){
	var acessoIrrestritoCli = getElement('blIrrestritoFilial');
	var listBoxRegional 		= document.getElementById('regionalUsuario');
	var listBoxFilial 			= document.getElementById('filiaisUsuario');
	if (acessoIrrestritoCli.checked==true){
		resetListBoxValue(listBoxRegional);
		resetListBoxValue(listBoxFilial);
	}
	
}

/**
 *  Função JavaScript que preenche qualquer ListBoxLookup conforme os parrametros:
 *  data 	    --> dados.
 *  chaveData   --> Chave que será a base de procura dos dados dentro de 'data'(parametro a cima). Qual eh a propriedade de 'data' que tem os dados da listbiox.
 *  idData      --> Chave da lookup. ou seja idProperty da lookup.
 *  dsData      --> Criteria da lookup. ou seja criteriaProperty da lookup.
 *  objHTML     --> Property da listbox ou sejá o nome do objeto no HTML.  
 *
 * @Author Diego Pachecp - LMS - GT5
 * @verion 1.1
 *
 **/
function fillListBoxLookup(data,chaveData,idData,dsData,objHTML){
   try{   
	   	var dataListbox = getNestedBeanPropertyValue(data, chaveData);
	   	if (dataListbox==null) 		return; 
	   	if (dataListbox==undefined) return;
	   	
		for(var i=0; i<dataListbox.length; i++) {
			var label = getNestedBeanPropertyValue(dataListbox[i], dsData);
			var option = new Option(label, getNestedBeanPropertyValue(dataListbox[i], idData));

			option.data = new Object();
			option.data[dsData] = label;
			option.data[idData]= option.value;
			option.data["_uniqueId"] = (i+1)*-1;
			setNestedBeanPropertyValue(option.data,dsData, label);
			getElement(objHTML).options.add(option);
		}	
	}	catch(e){
		alert('Houve um erro ao preencher a ListBox: ' + objHTML + '\n' + e.message );
	}
}
//***************************************************************************************************

</script>