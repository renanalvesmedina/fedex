<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterAutoridadeListaAction" onPageLoadCallBack="localOnPageLoad" >
	<adsm:i18nLabels>
	               <adsm:include key="campoAutorPar"/>
    </adsm:i18nLabels>	
	<adsm:form action="/seguranca/manterAutoridadeLista" idProperty="idAutoridadeLista" onDataLoadCallBack="localOnDataLoad" >

		<adsm:hidden property="hIdUsuarioADSM" serializable="true" />

		<adsm:range width="12%">						  
			<adsm:combobox property="tpAutoridade" domain="DM_TIPO_AUTORIDADE" 
						   label="autoridadeParaMn"  width="20%" labelWidth="15%" onchange="controlaLookup();" required="false" />
		</adsm:range>						   

		<adsm:range width="60%">	
			<adsm:lookup dataType="text" disabled="true" maxLength="60"
					     property="autoridadePara" idProperty="id" criteriaProperty="criteria"
						 action="" service="" required="true" 
		 				 exactMatch="false"
						 minLengthForAutoPopUpSearch="3" onDataLoadCallBack="permissaoPara"
						 />
 		</adsm:range>				 								 

		<adsm:lookup dataType="text" label="listaPermissao" maxLength="60"
		     	 	 property="listaPermissao" idProperty="idListaPermissao" criteriaProperty="nmListaPermissao"
	 				 action="/seguranca/manterListaPermissao"
	 				 service="lms.seguranca.manterAutoridadeListaAction.findLookupListaPermissao"
			 	     width="100%" required="true" size="25%"
	    />			
	
	   <adsm:buttonBar>
		   <adsm:storeButton />
		   <adsm:newButton />
		   <adsm:removeButton />
	   </adsm:buttonBar>
	
	</adsm:form>
</adsm:window>
<script>

function initWindow(eventObj) {
	//alert('Evento: ' + eventObj.name);
	if (eventObj.name=='newButton_click')
	{		  
	  limpar();
	}
}

function limpar()
{	
    resetValue("autoridadePara.id");
    resetValue("listaPermissao.idListaPermissao");
    setDisabled("autoridadePara.id",true);	    
}

function permissaoPara_cb(data) {

	return lookupExactMatch({e:document.getElementById("autoridadePara.id"), data:data, callBack:'autoridadePara_criteria_likeEndMatch' });
}

/* guarda o ultimo ponteiro para o picker da lookup. */
var ultPicker   = 'autoridadePara_picker';

function controlaLookup()
{   
    var tpAutoridade = getElementValue("tpAutoridade");   
    resetValue("autoridadePara.criteria");
    resetValue("autoridadePara.id");
    setDisabled("autoridadePara.id", true); 
          
    if (tpAutoridade == 'Usuário') criaLookupUsuario();    
    if (tpAutoridade == 'Perfil') criaLookupPerfil();	
}    

function criaLookupPerfil()
{
   /* LookUp de Perfil */
   setDisabled("autoridadePara.id", false);  
	       
   document.getElementById("autoridadePara.id").id = "perfil.idPerfil";
   document.getElementById("autoridadePara.id").name = "perfil.idPerfil";
   
   document.getElementById("autoridadePara.criteria").id = "perfil.dsPerfil";
   document.getElementById("autoridadePara.criteria").name = "perfil.dsPerfil";
   
   document.getElementById(ultPicker).id = "perfil_picker";
   ultPicker = "perfil_picker";				   
   
   document.getElementById("autoridadePara.id").property = "perfil";
   document.getElementById("autoridadePara.id").idProperty = "idPerfil";
   document.getElementById("autoridadePara.id").criteriaProperty = "dsPerfil";
   document.getElementById("autoridadePara.criteria").mainElement = "perfil.idPerfil";
   document.getElementById("autoridadePara.criteria").property = "perfil";					  
   document.getElementById("autoridadePara.id").service = "lms.seguranca.manterPermissaoAction.findLookupPerfil";
   document.getElementById("autoridadePara.id").url = contextRoot+"/seguranca/manterPerfil.do";
   document.getElementById("autoridadePara.id").propertyMappings = 
   [  
     { modelProperty:"dsPerfil", criteriaProperty:"perfil.dsPerfil", inlineQuery:true, disable:true },
     { modelProperty:"dsPerfil", relatedProperty:"perfil.dsPerfil", blankFill:true }
   ];    
}

function criaLookupUsuario()
{
  /* LookUp de Usuario */
  setDisabled("autoridadePara.id", false);  
       
  document.getElementById("autoridadePara.id").id = "usuario.idUsuario";
  document.getElementById("autoridadePara.id").name = "usuario.idUsuario";
  
  document.getElementById("autoridadePara.criteria").id = "usuario.nmUsuario";
  document.getElementById("autoridadePara.criteria").name = "usuario.nmUsuario";
  
  document.getElementById(ultPicker).id = "usuario_picker";
  ultPicker = "usuario_picker";	
  
  document.getElementById("autoridadePara.id").property = "usuario";
  document.getElementById("autoridadePara.id").idProperty = "idUsuario";
  document.getElementById("autoridadePara.id").criteriaProperty = "nmUsuario";
  document.getElementById("autoridadePara.criteria").mainElement = "usuario.idUsuario";
  document.getElementById("autoridadePara.criteria").property = "usuario";					  
  document.getElementById("autoridadePara.id").service = "lms.seguranca.manterUsuarioLMSAction.findLookupUsuarioAdsm";
  document.getElementById("autoridadePara.id").url = contextRoot+"/seguranca/manterUsuarioADSM.do";  
  document.getElementById("autoridadePara.id").propertyMappings = 
  [  
     { modelProperty:"login", criteriaProperty:"usuario.nmUsuario", inlineQuery:true, disable:true },
     { modelProperty:"login", relatedProperty:"usuario.nmUsuario", blankFill:true },
 	 { modelProperty:"usuario.idUsuario", relatedProperty:"hIdUsuarioADSM", blankFill:true }		     
  ];
}

function localOnPageLoad_cb() {
	onPageLoad_cb();		
}

function localOnDataLoad_cb(data, errorMessage, errorCode, eventObj) 
{    
   if (errorCode != undefined) {
        alert(errorCode + ' ' + errorMessage);
        return false;
    }	
    
    setElementValue("idAutoridadeLista",getNestedBeanPropertyValue(data, "idAutoridadeLista" ));        
    setElementValue("listaPermissao.idListaPermissao",getNestedBeanPropertyValue(data, "listaPermissao.idListaPermissao" ));        
    setElementValue("listaPermissao.nmListaPermissao",getNestedBeanPropertyValue(data, "listaPermissao.nmListaPermissao" ));        
    	
    setElementValue("tpAutoridade",getNestedBeanPropertyValue(data, "tpAutoridade" ));
    var tpPermissao = getElementValue("tpAutoridade");       

   if (tpPermissao == 'Usuário') 
   {   
      criaLookupUsuario();       
      setElementValue("usuario.idUsuario",getNestedBeanPropertyValue(data, "usuario.idUsuario" ));
      setElementValue("usuario.nmUsuario",getNestedBeanPropertyValue(data, "usuario.mnUsuario" ));
   }
   else
   {
      if (tpPermissao == 'Perfil')  
      {
	    criaLookupPerfil();	
        setElementValue("perfil.idPerfil",getNestedBeanPropertyValue(data, "perfil.idPerfil" ));
        setElementValue("perfil.dsPerfil",getNestedBeanPropertyValue(data, "perfil.dsPerfil" ));
      }       
  }  
  	
   onDataLoad_cb(new Object(), errorMessage, errorCode, eventObj);    
}

function validateTab(eventObj) {     

    // adicionar validações personalizadas            
    
    if (getElementValue("tpAutoridade") == "") {
        alert(i18NLabel.getLabel("campoAutorPar"));
        setFocus(document.getElementById("tpAutoridade"));
        return false;
    }        
 
    // script padrao de validacao da tela
    return validateTabScript(document.forms); 
 
}

</script>