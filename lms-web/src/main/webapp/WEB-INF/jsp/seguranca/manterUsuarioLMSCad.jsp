<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterUsuarioLMSAction" >
	<adsm:form action="/seguranca/manterUsuario" idProperty="idUsuarioLMS" onDataLoadCallBack="localOnDataLoad">
	
		<adsm:hidden property="tpCategoriaUsuario" serializable="true"  />
		<adsm:hidden property="jaExisteAdminProCliente" serializable="true"  />
		<adsm:hidden property="excluirAdminProCliente" serializable="true" value="false" />
		<adsm:hidden property="ehAdminFilial" serializable="false" value="false" />
		<adsm:hidden property="idEmpresaPadrao" serializable="true" />
		
		<adsm:lookup size="16" maxLength="60" width="80%" labelWidth="180"
					 property="usuarioADSM" 
					 idProperty="idUsuario"
					 criteriaProperty="login"
					 action="/seguranca/manterUsuarioADSM" 
					 service="lms.seguranca.manterUsuarioLMSAction.findLookupUsuarioAdsm" 
					 dataType="text" 
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 label="loginUsuarioADSM"
					 required="true"					 
					 onDataLoadCallBack="onChangeUsuarioADSM"
					 afterPopupSetValue="afterUsuarioADSMPopup"					 
		>	
			<adsm:propertyMapping relatedProperty="tpCategoriaUsuario" modelProperty="tpCategoriaUsuario.value" />
			<adsm:propertyMapping relatedProperty="usuarioADSM.nmUsuario" modelProperty="nmUsuario" />
 			<adsm:textbox property="usuarioADSM.nmUsuario" size="30"  dataType="text" disabled="true" />
		</adsm:lookup>
		
		<adsm:checkbox property="blIrrestritoCliente" label="acessoIrrestritoClientes" 
					   onclick="onClickAcessoIrrestrito();"  labelWidth="180" width="80%"/>		

		<adsm:listbox size="4" boxWidth="370" width="100%" label="acessoACliente(s)" labelWidth="180" 
	         		property="clienteUsuario" 
	     				optionProperty="idClienteUsuario"
	     				optionLabelProperty="pessoa.nrIdentificacao"
	     				onContentChange="onContentChangeCustom"	     				
	     				>  			
							
						<adsm:hidden property="cnpjClienteUsuario" />
						<adsm:lookup property="cliente" 
									 idProperty="idCliente"
									 criteriaProperty="pessoa.nrIdentificacao"
									 action="/vendas/manterDadosIdentificacao" 
									 service="lms.vendas.clienteService.findLookupClientesAtivos" 
									 dataType="text" 
									 exactMatch="false"
									 minLengthForAutoPopUpSearch="3"
 								   size="20" maxLength="21"
									 required="false">				
						 <adsm:propertyMapping relatedProperty="clienteUsuario_pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
						 <adsm:propertyMapping relatedProperty="clienteUsuario_cliente.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado" />
						 <adsm:propertyMapping relatedProperty="cnpjClienteUsuario" modelProperty="pessoa.nrIdentificacaoFormatado"/>
				 		 <adsm:textbox property="pessoa.nmPessoa" size="30"  dataType="text" disabled="true" />			
				 		 
						</adsm:lookup>
					<BR>
					<adsm:hidden property="remetenteLabel"/>
 					<adsm:checkbox property="remetente" />
					<adsm:label key="remetente"/>
 					<adsm:checkbox property="destinatario" />
					<adsm:label key="destinatario"/>
					<adsm:checkbox property="responsavelFrete" />
					<adsm:label key="responsavelFrete"/>

		</adsm:listbox>

		<adsm:section caption="clienteAssociado"/>
		
		<adsm:lookup size="20" maxLength="21" labelWidth="180" width="80%"
					 property="clienteDoUsuario" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao"
					 action="/vendas/manterDadosIdentificacao" 
					 service="lms.vendas.clienteService.findLookupClientesAtivos" 
					 dataType="text" 
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 label="clienteDoUsuario"
					 required="false"	
		>				
		 <adsm:propertyMapping relatedProperty="clienteDoUsuario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
		 <adsm:propertyMapping relatedProperty="clienteDoUsuario.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado" />
 		 <adsm:textbox property="clienteDoUsuario.pessoa.nmPessoa" size="30"  dataType="text" disabled="true" />			
		</adsm:lookup>
				
		<adsm:checkbox property="blAdminCliente" label="administradorDoCliente" labelWidth="180"/>
		
		<adsm:section caption="administradorDaFilial"/>
		
		<adsm:checkbox onclick="onClickAdmFilial();" property="blAdminFilial" label="administradorDaFilial" labelWidth="180"/>
		
		<adsm:lookup size="4" maxLength="3" labelWidth="180"
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial"
					 action="/municipios/manterFiliais" 
					 service="lms.municipios.filialService.findLookupBySgFilial" 
					 dataType="text" 
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 label="filial"
					 required="false"
					 disabled="false"					 
		>				
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />			
			<adsm:textbox property="filial.pessoa.nmFantasia" size="30"  dataType="text" disabled="true" />
		</adsm:lookup>
		
		<adsm:i18nLabels>
			<adsm:include key="abrevRemetente"/>
			<adsm:include key="abrevDestinatario"/>
			<adsm:include key="abrevResponsavelPeloFrete"/>
			<adsm:include key="tipoClienteObrigatorio"/>
			<adsm:include key="clientesIguais"/>			
			<adsm:include key="clienteObrigatorio"/>
			<adsm:include key="filialObrigatorio"/>
			<adsm:include key="clienteUsuarioObrigatorio"/>
			<adsm:include key="administradorCliente"/>
			<adsm:include key="jaExisteUmUsuarioLMS"/>					
		</adsm:i18nLabels>
		
		<adsm:buttonBar>
			<adsm:button id="vincularEmpresa" caption="vincularEmpresaFiliais" action="/seguranca/manterUsuarioLMSEmpresa" cmd="main" disabled="false">
				<adsm:linkProperty src="idUsuarioLMS"			target="usuarioADSM.idUsuario"/>
				<adsm:linkProperty src="idUsuarioLMS"			target="idUsuarioLMS"/>
				<adsm:linkProperty src="usuarioADSM.login" target="usuarioADSM.login"/>
				<adsm:linkProperty src="usuarioADSM.nmUsuario" target="usuarioADSM.nmUsuario"/>
			</adsm:button>
			<adsm:storeButton />
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

function onClickAdmFilial(){
	 desabilitaLookupFilial();
}

function onClickAcessoIrrestrito(){
	desabilitaLookupUsuarioADSM();
}

function desabilitaLookupFilial(){
	var admFilial = document.getElementById('blAdminFilial');
	var lookupFilial = document.getElementById('filial.idFilial');
	if(admFilial.checked == true){
		setDisabled(admFilial, false);
		setDisabled(lookupFilial, false);
	}else{
		resetValue('filial.idFilial');
		setDisabled(lookupFilial, true);
	}
	return true;
}

function desabilitaLookupUsuarioADSM(){
	var cliente = document.getElementById('blIrrestritoCliente');
	var lookupCliente = document.getElementById("clienteUsuario_cliente.idCliente");
	var listBoxCliente = document.getElementById('clienteUsuario');
	var checkboxRemetente = document.getElementById('remetente');
	var checkboxDestinatario = document.getElementById('destinatario');
	var checkboxResponsavelFrete = document.getElementById('responsavelFrete');		
	
	if(cliente.checked == true){
		setDisabled(lookupCliente, true);
		setDisabled(checkboxRemetente, true);
		setDisabled(checkboxDestinatario, true);
		setDisabled(checkboxResponsavelFrete, true);
		setDisabled(listBoxCliente, true);
		resetValue( lookupCliente );
		//resetListBoxValue(listBoxCliente);
		//listBoxCliente.required = "false";
	}else{
		setDisabled(lookupCliente, false);
		setDisabled(checkboxRemetente, false);
		setDisabled(checkboxDestinatario, false);
		setDisabled(checkboxResponsavelFrete, false);
		setDisabled(listBoxCliente, false);
		//listBoxCliente.required = "true";
	}	

}   

/*function desabilita_cb(data,erro){
	onDataLoad_cb(data,erro);
	setDisabled("vincularEmpresa", false);
	localOnDataLoad_cb
}*/

function habilitaCamposNaEntradaBtnLimpar(){
	setDisabled("usuarioADSM.idUsuario",false);     		
	setDisabled("blIrrestritoCliente",false);
	setDisabled("clienteDoUsuario.idCliente", false);
	setDisabled("blAdminCliente", false);
}

function initWindow(eventObj) {
	/* Casos que desabilita o botao. */
	if (eventObj.name=='tab_load' || eventObj.name=='removeButton' ){
		setDisabled("vincularEmpresa", true);
	}else{
		/* Casos que habilita o botao. */
		if (eventObj.name=='gridRow_click' || eventObj.name=='storeButton' ){
			setDisabled("vincularEmpresa", false);
		} 
	}
	
	/* Casos em que habilita usuário */
	if (eventObj.name == 'tab_click'  ||   eventObj.name == 'removeButton' || 
	    eventObj.name == 'newButton_click' || eventObj.name == 'cleanButton_click') {	    
			habilitaCamposNaEntradaBtnLimpar();
  		desabilitaLookupUsuarioADSM();
			desabilitaLookupFilial();
 			usuarioLogadoEhAdmin();
	} else {
		/* Casos em que desabilita usuário */
		if (eventObj.name == 'storeButton' ||
		    eventObj.name == 'gridRow_click' ) {		    
		 	setDisabled('usuarioADSM.idUsuario',true);		 	
		}	
	}
	
	marcaCheckBox();
  
}

function marcaCheckBox(){
	setElementValue("remetente", true);
	setElementValue("destinatario", true);
	setElementValue("responsavelFrete", true);
}

function onChangeUsuarioADSM_cb( data ){
  
  var r = lookupExactMatch({e:document.getElementById("usuarioADSM.idUsuario"), data:data, callBack:"onChangeUsuarioADSM2"});

	if( r == true ){
		verificaSejaExiste( data );
	}
	
	return r;
}

function onChangeUsuarioADSM2_cb( data ) {
 	usuarioADSM_login_likeEndMatch_cb(data);
  if( data.length == 1 ){
		verificaSejaExiste( data );
	}
	
}

function verificaSejaExiste( data ){
 	data1 = new Array();
  setNestedBeanPropertyValue(data1, "item", data);

	var sdo = createServiceDataObject("lms.seguranca.manterUsuarioLMSAction.findByIdJaExiste", "jaExiste", data1 );
  xmit({serviceDataObjects:[sdo]});
}

function jaExiste_cb( data, error ){
		if (error != undefined) {
			alert(error);
			return false;
		}
	  var tpCategoriaUsuario = getNestedBeanPropertyValue(data, "tpCategoriaUsuario.value");
		if (data != undefined && data.existe != undefined && data.existe == "true" ) {
			alert( i18NLabel.getLabel("jaExisteUmUsuarioLMS") );
			onDataLoad(getNestedBeanPropertyValue(data,"idUsuario"));
			//desabilitaLookupUsuarioADSM();
			alteraCamposConformeCategoria(tpCategoriaUsuario);	
			setDisabled('usuarioADSM.idUsuario',true);
		}else{
		  afterDataLoadUsuarioADSM(tpCategoriaUsuario);			
		}
}

function afterUsuarioADSMPopup( data ) {
 	data1 = new Array();
 	setNestedBeanPropertyValue(data1, "0", data);
  verificaSejaExiste( data1 );
}

function alteraCamposConformeCategoria(tpCategoriaUsuario) {
	var blIrrestritoCliente = document.getElementById('blIrrestritoCliente');
	if( tpCategoriaUsuario!= null && tpCategoriaUsuario!='undefined' ){
		if( tpCategoriaUsuario != 'C' ) {
			setDisabled(blIrrestritoCliente, false); // habilita a checkBox "blIrrestritoCliente" 
			//setElementValue("blIrrestritoCliente", true); // checa Acesso Irrestrito a Clientes
			desabilitaLookupUsuarioADSM(); //Como a checkBox "blIrrestritoCliente" foi checada é necessário desabilitar a lookup usuarioADSM
			modificaSessaoClienteAssociado( true ); // modifica os campos da sessão cliente associado
			setFocus( document.getElementById('blIrrestritoCliente') );	   				
		} else if( tpCategoriaUsuario == 'C' ) {
			//setElementValue("blIrrestritoCliente", false); // tira a checagem da checkBox
			desabilitaLookupUsuarioADSM(); 						//Como a checkBox "blIrrestritoCliente" foi tirada a checagem é necessário habilitar a lookup usuarioADSM		
			setDisabled(blIrrestritoCliente, true);  // desabilita a checkBox "blIrrestritoCliente"
			modificaSessaoClienteAssociado( false );  // chama a função que irá modificar os campos da sessão cliente associado
			setFocus( document.getElementById('clienteUsuario_cliente.pessoa.nrIdentificacao') );	   	
		}
	}   
}

function afterDataLoadUsuarioADSM(tpCategoriaUsuario) {
	var blIrrestritoCliente = document.getElementById('blIrrestritoCliente');
	if( tpCategoriaUsuario!= null && tpCategoriaUsuario!='undefined' ){
		if( tpCategoriaUsuario != 'C' ) {
			setDisabled(blIrrestritoCliente, false); // habilita a checkBox "blIrrestritoCliente" 
			setElementValue("blIrrestritoCliente", true); // checa Acesso Irrestrito a Clientes
			desabilitaLookupUsuarioADSM(); //Como a checkBox "blIrrestritoCliente" foi checada é necessário desabilitar a lookup usuarioADSM
			modificaSessaoClienteAssociado( true ); // modifica os campos da sessão cliente associado
			setFocus( document.getElementById('blIrrestritoCliente') );	  
		} else if( tpCategoriaUsuario == 'C' ) {
			setElementValue("blIrrestritoCliente", false); // tira a checagem da checkBox
			desabilitaLookupUsuarioADSM(); 						//Como a checkBox "blIrrestritoCliente" foi tirada a checagem é necessário habilitar a lookup usuarioADSM		
			setDisabled(blIrrestritoCliente, true);  // desabilita a checkBox "blIrrestritoCliente"
			modificaSessaoClienteAssociado( false );  // chama a função que irá modificar os campos da sessão cliente associado
			setFocus( document.getElementById('clienteUsuario_cliente.pessoa.nrIdentificacao') );	   	
		}
	}   
}

/**
	Função responsável por habilitar ou desabilitar a sessão cliente associado conforme parametro recebido.
*/
function modificaSessaoClienteAssociado( desabilitaLimpa ){
	var checkboxAdmCliente = document.getElementById('blAdminCliente');	
	var lookupClienteAssociado = document.getElementById('clienteDoUsuario.idCliente');	
	if( desabilitaLimpa == true ){
		resetValue('clienteDoUsuario.idCliente'); // limpa a lookup cliente associado
		setDisabled(lookupClienteAssociado, true); // desabilita a lookup
		setDisabled(checkboxAdmCliente, true);		// desabilita a checkBox Administrador do cliente
		lookupClienteAssociado.required = "false";	// tira a obrigatoriedade dela, pois a mesma foi desabilitada
	}	if( desabilitaLimpa == false ){
		setDisabled(lookupClienteAssociado, false); // habilita a lookup cliente associado
		setDisabled(checkboxAdmCliente, false); 		// habilita a checkBox Administrador do cliente
		lookupClienteAssociado.required = "true";	  // seta a obrigatoriedade da mesma para true
	}
}

function onContentChangeCustom(event) {
	var dataListbox = document.getElementById('clienteUsuario');
	if (event.name == "cleanButton_click") {
		dataListbox.selectedIndex = -1;
	}
	if (event.name == "modifyButton_click") {
		if (document.getElementById("clienteUsuario_cliente.idCliente").value == null ||
  			 document.getElementById("clienteUsuario_cliente.idCliente").value == "") {
  		//alert( i18NLabel.getLabel("tipoClienteObrigatorio") );
  		return false;
  	}
  	if( getElementValue("remetente") == false && 
  			getElementValue("destinatario") == false && 
  			getElementValue("responsavelFrete") == false){  			
  		alert( i18NLabel.getLabel("tipoClienteObrigatorio") );
  		var checkboxRemetente = document.getElementById('remetente');
  		checkboxRemetente.focus = "true";
  		return false;
  	}
  	if( verificaDuplicidade( event ) == true ){
  		return false;
  	}
  } else if( (event.name == "modifyButton_afterClick") ){
			var data = event.src.data;
	  	var label = data.cnpjClienteUsuario.pessoa.nrIdentificacao + " - " + data.pessoa.nmPessoa;
			if (data.remetente.remetente==true) {
				label += " - " + i18NLabel.getLabel("abrevRemetente");
			} 
			if (data.destinatario.destinatario==true) {
				label += " - " + i18NLabel.getLabel("abrevDestinatario");
			}
			if (data.responsavelFrete.responsavelFrete==true) {
				label += " - " + i18NLabel.getLabel("abrevResponsavelPeloFrete");
			}
			event.src.text = label;
			marcaCheckBox();
	}

	if( event.name == "cleanButton_afterClick" || event.name == "deleteButton_afterClick" ){
		marcaCheckBox( );
	}
}

function customisaLabelListBox( listBox, data ){

	if ( data != null && data.clienteUsuario!= null ){
		for(var i=0; i < data.clienteUsuario.length; i++) {
	  	var label = data.clienteUsuario[i].cnpjClienteUsuario.pessoa.nrIdentificacao+" - "+data.clienteUsuario[i].pessoa.nmPessoa;
			if ( data.clienteUsuario[i].remetente.remetente == "true" ) {
				label += " - " + i18NLabel.getLabel("abrevRemetente");
			} 
			if ( data.clienteUsuario[i].destinatario.destinatario == "true") {
				label += " - " + i18NLabel.getLabel("abrevDestinatario");
			}
			if ( data.clienteUsuario[i].responsavelFrete.responsavelFrete == "true") {
				label += " - " + i18NLabel.getLabel("abrevResponsavelPeloFrete");
			}
			setNestedBeanPropertyValue(getElement("clienteUsuario").options[i], "text", label);
		}	
	}

	//listBox.src.text = label;
	setElementValue("clienteUsuario", label);
	marcaCheckBox( );
		
}

// Função responsável por verificar se já existe um cliente na listBox. Faz esta verificação
// comparando o cnpj do cliente a ser adicionado com o cnpj dos clientes existentes na lista.
function verificaDuplicidade( event ){
	var data = null;
	var cnpj = "";
	if( event.src != null ){
		data = event.src.data;
	}
	if( event.name == "modifyButton_afterClick" && data != null ){
		cnpj = getNestedBeanPropertyValue(data, "cnpjClienteUsuario.pessoa.nrIdentificacao");
	}else{
  	cnpj = document.getElementById('clienteUsuario_cliente.pessoa.nrIdentificacao').value; //src.data.pessoa.nrIdentificacao;
  }
	var dataListbox = document.getElementById('clienteUsuario');
	var duplicado = false;
	for( var i=0; i < dataListbox.length; i++) {
		var cnpjDaList = dataListbox[i].data.cnpjClienteUsuario.pessoa.nrIdentificacao;
			if( dataListbox.selectedIndex != i ){
				if( cnpj == cnpjDaList ){
					//dataListbox.length = dataListbox.length-1;
					i = dataListbox.length;
					duplicado = true;
					marcaCheckBox();				
				}
			}else if( dataListbox[dataListbox.selectedIndex].data.cnpjClienteUsuario.pessoa.nrIdentificacao != cnpj ){
				if( cnpj == cnpjDaList ){
					i = dataListbox.length;
					duplicado = true;
					marcaCheckBox();
				}
			}
	}
	if( duplicado == true ){
		alert( i18NLabel.getLabel("clientesIguais") );				
		return true;
	}else{
 		return false;
 	}
}

// Configuração personalizada da listbox
clienteUsuarioListboxDef.addRelated({elementId:'remetente',property:'remetente', serializable:true, config:{idProperty:"remetente", labelProperty:"remetenteLabel"}});
clienteUsuarioListboxDef.addRelated({elementId:'destinatario',property:'destinatario', serializable:true, config:{idProperty:"destinatario", labelProperty:"destinatarioLabel"}});
clienteUsuarioListboxDef.addRelated({elementId:'responsavelFrete',property:'responsavelFrete', serializable:true, config:{idProperty:"responsavelFrete", labelProperty:"responsavelFreteLabel"}});
clienteUsuarioListboxDef.addRelated({elementId:'cnpjClienteUsuario',property:'cnpjClienteUsuario', serializable:true, config:{idProperty:"pessoa.nrIdentificacao", labelProperty:"pessoa.nrIdentificacao"}});

// indica que a listbox possui mais de um campo relacionado
clienteUsuarioListboxDef.properties.uniqueRelated = false;

function localOnDataLoad_cb(data, errorMessage, errorCode, eventObj) {    

	onDataLoad_cb( data, errorMessage, errorCode, eventObj);
	 
  setDisabled("vincularEmpresa", false);

  if (errorCode != undefined) {
     alert(errorCode + ' ' + errorMessage);
     return false;
  }	  	

	desabilitaLookupUsuarioADSM();
	
	desabilitaLookupFilial();	
	
	// faz a verificação abaixo pois se exitem registros incorretos, ou seja mesmo o usuário sendo cliente o acesso 
	// irrestrito a cliente esta setado retira a checagem
	var cliente = document.getElementById('blIrrestritoCliente');
	var tpCategoriaUsuario = getNestedBeanPropertyValue(data, "tpCategoriaUsuario.");
	if( cliente.checked == true && tpCategoriaUsuario == 'C' ){	
		setElementValue( document.getElementById("clienteUsuario_cliente.idCliente") , false);
		setElementValue("blIrrestritoCliente", false);
		setDisabled( document.getElementById("clienteUsuario_cliente.idCliente") , false);
		setDisabled( document.getElementById('remetente') , false);
		setDisabled( document.getElementById('destinatario') , false);
		setDisabled( document.getElementById('responsavelFrete') , false);
		setDisabled( document.getElementById('clienteUsuario') , false);
	}
	
	alteraCamposConformeCategoria( tpCategoriaUsuario );
	
	customisaLabelListBox( document.getElementById('clienteUsuario'), data );

  //afterDataLoadUsuarioADSM( tpCategoriaUsuario );
	
	setFocusOnFirstFocusableField(document);
}

function validateTab( event ){
   var tpCategoriaUsuario = getElementValue("tpCategoriaUsuario");
   if(  tpCategoriaUsuario!=null && tpCategoriaUsuario!="" ){
	   if( tpCategoriaUsuario != 'C' ){
	 			var dataListbox = document.getElementById('clienteUsuario');
	 			var cliente = document.getElementById('blIrrestritoCliente');
				if ( (dataListbox.length == null || dataListbox.length == 0 ) && cliente.checked != true ){
	   			alert( i18NLabel.getLabel("clienteObrigatorio") );
	   			setFocus( document.getElementById('clienteUsuario_cliente.pessoa.nrIdentificacao') );	   	
	   			return false;
	   		}
	   }
	   
	   if( tpCategoriaUsuario == 'C' ){
	   		if( getElementValue('clienteDoUsuario.idCliente')==null || getElementValue('clienteDoUsuario.idCliente')==""){
	   			alert( i18NLabel.getLabel("clienteUsuarioObrigatorio") );
	   			setFocus( document.getElementById('clienteDoUsuario.pessoa.nrIdentificacao') );
	   			return false;   		
	   		}
	   }
   }
   
   var adminFilial = document.getElementById('blAdminFilial');
   if( adminFilial != null && adminFilial.checked == true ){
   		if( getElementValue('filial.idFilial')==null || getElementValue('filial.idFilial')=="" ){
	   			alert( i18NLabel.getLabel("filialObrigatorio") );
	   			setFocus( document.getElementById('filial.sgFilial') );
	   			return false;     		
   		}
   }
   
   var adminProCliente = document.getElementById('jaExisteAdminProCliente').value;
   var checkboxAdmCliente = document.getElementById('blAdminCliente');	
   if( (adminProCliente != null && adminProCliente=="true") &&
   			( checkboxAdmCliente != null && checkboxAdmCliente.checked == true ) ){
			if( confirm( getI18nMessage("administradorCliente", getElementValue("usuarioADSM.nmUsuario"),false ) ) ){
				setElementValue("excluirAdminProCliente","true");
			}else{
				checkboxAdmCliente.checked=false;			
			}
   }
   
	var cliente = document.getElementById('blIrrestritoCliente');
	var listBoxCliente = document.getElementById('clienteUsuario');	
	if(cliente.checked == true){
		resetListBoxValue(listBoxCliente);
	}   
	
	if( validateTabScript( document.forms[0] ) == false ){
		return false;
	}
	return true;	
}

function usuarioLogadoEhAdmin(){
 	data = new Array();
  var sdo = createServiceDataObject("lms.seguranca.manterUsuarioLMSAction.findEhAdminFilial", "usuarioLogadoEhAdmin", data);
  xmit({serviceDataObjects:[sdo]});
}

function usuarioLogadoEhAdmin_cb( data, erros ){
	if (erros != undefined) {
    	alert(erros);
        return false;
	}
	
	var ehAdmin = getNestedBeanPropertyValue(data, "_value" );
	document.getElementById('ehAdminFilial').value = ehAdmin;
	permisaoAdminFilial();
		
}

function permisaoAdminFilial(){
	
	var ehAdminFilial = document.getElementById('ehAdminFilial');
	var checkboxAdmCliente = document.getElementById('blAdminCliente');	
	var lookupClienteAssociado = document.getElementById('clienteDoUsuario.idCliente');		
	var lookupCliente = document.getElementById("clienteUsuario_cliente.idCliente");
	var listBoxCliente = document.getElementById('clienteUsuario');
	var adminFilial = document.getElementById('blAdminFilial');
	  	
	if( ehAdminFilial.value == "true" ){
		setDisabled(checkboxAdmCliente, true);
		setDisabled(lookupClienteAssociado, true);
		resetValue('clienteDoUsuario.idCliente');
		setDisabled(lookupCliente, true);
		setDisabled(listBoxCliente, true);
		setDisabled(adminFilial, true);
		resetValue('clienteUsuario_cliente.idCliente');
	}
	setFocusOnFirstFocusableField(document);
	
}
</script>