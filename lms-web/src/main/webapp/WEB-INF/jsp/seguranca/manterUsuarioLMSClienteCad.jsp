<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterUsuarioLMSClienteAction" >
	<adsm:form action="/seguranca/manterUsuarioLMSCliente" idProperty="idUsuario" onDataLoadCallBack="localOnDataLoad">

		<adsm:hidden property="criteriaFindSemUsuario" value="sim" serializable="false" />
		
		<adsm:hidden property="dsSenha" value="" serializable="true" />
		
		<adsm:hidden property="urlSistema" value="" serializable="true" />

		<adsm:lookup size="20" maxLength="21" labelWidth="150" width="80%"
					 property="clienteDoUsuario" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao"
					 action="/seguranca/manterUsuarioLMSCliente"
					 cmd="cliente"
					 service="lms.seguranca.manterUsuarioLMSClienteAction.findLookupClientesAtivos" 
					 dataType="text" 
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 label="clienteDoUsuario"
					 required="true"
		>				
		 <adsm:propertyMapping relatedProperty="clienteDoUsuario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
		 <adsm:propertyMapping relatedProperty="clienteDoUsuario.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado" />
 		 <adsm:textbox property="clienteDoUsuario.pessoa.nmPessoa" size="30"  dataType="text" disabled="true" />			
		</adsm:lookup>	
				
		<adsm:textbox dataType="text" property="login" label="login" width="30%" labelWidth="150" size="15" maxLength="60" required="true"/>
						
		<adsm:textbox dataType="text" property="nmUsuario" label="nome" width="30%" labelWidth="170" size="35" maxLength="60" required="true"/>
				
		<adsm:complement label="telefone" labelWidth="150" width="30%" >
			<adsm:textbox dataType="text" property="nrDdd"  size="5" maxLength="5" />
			<adsm:textbox dataType="text" property="nrFone" size="10" maxLength="10"/>
		</adsm:complement>		
		
		<adsm:textbox dataType="email" property="dsEmail" label="email" width="30%" size="30" maxLength="60" labelWidth="170"/>
				
		<adsm:checkbox property="blAtivo"  label="ativo" width="30%" labelWidth="150" /> 
		<adsm:hidden property="blAtivoVerify" serializable="true"/>	
				
		<adsm:combobox property="locale" label="idioma" domain="TP_LINGUAGEM" onlyActiveValues="true" boxWidth="200" width="30%" labelWidth="170" required="true"/>

		<adsm:textbox dataType="text" property="dsSenhaTemporaria" label="senhaTemporaria" width="30%" size="25" maxLength="20" labelWidth="150"/>
		
		<adsm:hidden property="dsSenhaVerify" serializable="true"/>		
		
		<adsm:textbox dataType="JTDate" property="dtUltimaTrocaSenha" label="dataUltimaTrocaSenha" width="30%" size="20" maxLength="20" labelWidth="170" disabled="true"/>
		
		<adsm:textbox dataType="JTDateTimeZone" property="dhCadastro" label="dataAlteracao" width="100%" size="20" maxLength="20" labelWidth="150" disabled="true"/>		

		<adsm:complement label="alteradoPor" labelWidth="150" width="100%" >
			<adsm:textbox dataType="text" property="cadastradoPorLogin" size="10" maxLength="20" serializable="false" disabled="true"/>
			<adsm:textbox dataType="text" property="cadastradoPorNmUsuario"  size="50" maxLength="50" serializable="false" disabled="true"/>
		</adsm:complement>
		<BR>
		<adsm:listbox size="4" boxWidth="370" width="100%" label="acessoACliente(s)" labelWidth="150" 
	         		property="clienteUsuario" 
	     				optionProperty="idClienteUsuario"
	     				optionLabelProperty="pessoa.nrIdentificacao"
	     				orderProperty="pessoa.nrIdentificacao"	     				
	     				onContentChange="onContentChangeCustom"	     				
	     				>  			
							
						<adsm:hidden property="cnpjClienteUsuario" />
						<adsm:lookup property="cliente" 
									 idProperty="idCliente"
									 criteriaProperty="pessoa.nrIdentificacao"									 
									 action="/seguranca/manterUsuarioLMSCliente"
									 cmd="cliente"
									 service="lms.seguranca.manterUsuarioLMSClienteAction.findLookupClientesAtivos" 
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
		
		<adsm:listbox label="acessoPerfil(s)" size="4" boxWidth="170" width="100%" 
						property="acessoPerfil" 												
						optionProperty="idPerfilUsuario"
						optionLabelProperty="dsPerfil"
						orderProperty="dsPerfil"
						>
						
				<adsm:lookup property="perfil" 
							 idProperty="idPerfil"
							 criteriaProperty="dsPerfil"
							 action="/seguranca/manterPerfil"
							 service="lms.seguranca.manterUsuarioLMSClienteAction.findLookupPerfil" 
							 dataType="text"
							 exactMatch="false"
							 size="30" maxLength="60">
				 </adsm:lookup>
		</adsm:listbox>		
				
		<adsm:i18nLabels>
			<adsm:include key="senhaTemporariaObrigatoria"/>
			<adsm:include key="ADSM_SAME_PASSWD_EXCEPTION_KEY"/>
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
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
function buscaLocale(){
  var sdo = createServiceDataObject("lms.seguranca.manterUsuarioLMSClienteAction.findLocaleByUsuarioLogado", "localeUsuario");
  xmit({serviceDataObjects:[sdo]});	
}

function localeUsuario_cb( data, erros ){
	if (erros != undefined) {
    	alert(erros);
        return false;
	}       
	if( data != null && data != "undefined" ){	
		setElementValue("locale", getNestedBeanPropertyValue(data, "_value" ) );
	}
	
	return true;
}

function verificaClienteAssociado(){
  var sdo = createServiceDataObject("lms.seguranca.manterUsuarioLMSClienteAction.findClienteAssociadoByUsuarioSessao", "clienteAssociado");
  xmit({serviceDataObjects:[sdo]});	
}

function clienteAssociado_cb( data, erros ){
	if (erros != undefined) {
    	alert(erros);
        return false;
	}       
	if( data != null && (data.pessoa != null && data.pessoa != "undefined" )){	
		setElementValue("clienteDoUsuario.pessoa.nrIdentificacao", data.pessoa.nrIdentificacaoFormatado);
		setElementValue("clienteDoUsuario.pessoa.nmPessoa", data.pessoa.nmPessoa );
		setElementValue("clienteDoUsuario.idCliente", data.idCliente );
		desabilitaLookupClienteUsuario();
	}
	
	return true;
}

function desabilitaLookupClienteUsuario(){
	setDisabled("clienteDoUsuario.idCliente", true);
	setDisabled("clienteDoUsuario.pessoa.nmPessoa", true);	
  document.getElementById("clienteDoUsuario.idCliente").masterLink = "true";
  document.getElementById("clienteDoUsuario.pessoa.nrIdentificacao").masterLink = "true";  
  document.getElementById("clienteDoUsuario.pessoa.nmPessoa").masterLink = "true";
  		
}

function localOnDataLoad_cb( data, errorMessage, errorCode, eventObj ){
	onDataLoad_cb( data, errorMessage, errorCode, eventObj);
	carregaDadosSessao();
	
	customisaLabelListBox( document.getElementById('clienteUsuario'), data );

	setFocusOnFirstFocusableField(document);	
}

// Carrega da sessão do usuário as informações de login e o nome.
function carregaDadosSessao() {	

 	 data = new Array();
 	 setNestedBeanPropertyValue(data, "idUsuario", document.getElementById("idUsuario").value );
 	     
   document.getElementById("cadastradoPorLogin").masterLink = "true";
   document.getElementById("cadastradoPorNmUsuario").masterLink = "true";
   
   var sdo = createServiceDataObject("lms.seguranca.manterUsuarioLMSClienteAction.carregaDadosSessao", "carregaDadosSessao", data);
   xmit({serviceDataObjects:[sdo]});
}

// função callback responsável por setar nos campos os falores recebidos da camada de controle da aplicação.	
function carregaDadosSessao_cb(data, error) {

	setElementValue("cadastradoPorLogin", getNestedBeanPropertyValue(data, "cadastradoPorLogin"));
	setElementValue("cadastradoPorNmUsuario", getNestedBeanPropertyValue(data, "cadastradoPorNmUsuario"));
	setElementValue("dhCadastro", setFormat("dhCadastro", getNestedBeanPropertyValue(data, "dhCadastro")));
	setElementValue("blAtivoVerify", getNestedBeanPropertyValue(data, "blAtivoVerify") );	
	setElementValue("dsSenha", getNestedBeanPropertyValue(data, "dsSenha") );
	setElementValue("dsSenhaVerify", getNestedBeanPropertyValue(data, "dsSenhaTemporaria") );
}

var senhaTempObrigatoria = false;

function resetaCamposHiddenAndDisable(){
 	document.getElementById("dsSenha").value ="";	
 	document.getElementById("idUsuario").value ="";	
 	document.getElementById("cadastradoPorLogin").value ="";	
 	document.getElementById("cadastradoPorNmUsuario").value ="";
}

// funçao chamada quando a página é carregada.
function initWindow(event) {
	verificaClienteAssociado();
	
	if ((event.name=="newButton_click") || (event.name=="tab_click")  ||
			event.name == 'removeButton' ||  event.name == 'cleanButton_click' ) {
		var form = document.getElementById("form_idUsuario");
		form.blAtivo.checked = true;
		resetaCamposHiddenAndDisable();
		buscaLocale();
		carregaDadosSessao();
	}
	
	if( event.name=='gridRow_click' || event.name=="tab_click" || event.name=='tab_load' ){
		document.getElementById("urlSistema").value = getCurrentBaseURL();	
		tamanhoMinimoSenha();
		carregaDadosSessao();
	}
	
	if (event.name == 'tab_click'  ){
		var form = document.getElementById("form_idUsuario");
		if( document.getElementById("login").value == null ||
				document.getElementById("login").value == "" ){
			senhaTempObrigatoria = true;			
		}
	}else{
			senhaTempObrigatoria = false;
	}
	
	if( event.name == 'storeButton' ){
		carregaDadosSessao();
	}

	marcaCheckBox();	
	
}

function validateTab( event ){
	if( validateTabScript( document.forms[0] ) == false ){
		return false;
	}
	document.getElementById("urlSistema").value = getCurrentBaseURL();	
	if( senhaTempObrigatoria == true || 
		(document.getElementById("blAtivoVerify").value == "false" && document.getElementById("blAtivo").checked == true)
	 	|| ( document.getElementById("dsSenha").value == "" && document.getElementById("blAtivo").checked == true ) ){
		if( document.getElementById("dsSenhaTemporaria").value == null ||
				document.getElementById("dsSenhaTemporaria").value == "" ){
				alert( i18NLabel.getLabel("senhaTemporariaObrigatoria") );
				setFocus(document.getElementById("dsSenhaTemporaria"));
	   		return false;
		}
	}
	if( document.getElementById("blAtivo").checked == true && document.getElementById("blAtivoVerify").value == "false" ){
		if( document.getElementById("dsSenhaTemporaria").value == document.getElementById("dsSenhaVerify").value ){
				alert( i18NLabel.getLabel("ADSM_SAME_PASSWD_EXCEPTION_KEY") );
				setFocus(document.getElementById("dsSenhaTemporaria"));
	   		return false;			
		}
	}
		
}	

function tamanhoMinimoSenha(){
  var sdo = createServiceDataObject("lms.seguranca.manterUsuarioLMSClienteAction.findTamanoMinimoSenhaUsuarioADSM", "tamanhoSenha");
  xmit({serviceDataObjects:[sdo]});	
}

function tamanhoSenha_cb( data, erros ){
	if (erros != undefined) {
    	alert(erros);
        return false;
	}       
	
	var tamanho = getNestedBeanPropertyValue(data, "_value" );
	document.getElementById('dsSenhaTemporaria').minLength = tamanho;
	
}

//*********************************************************************************//
function marcaCheckBox(){
	setElementValue("remetente", true);
	setElementValue("destinatario", true);
	setElementValue("responsavelFrete", true);
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


</script>