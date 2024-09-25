<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterPermissaoAction" onPageLoadCallBack="localOnPageLoad">
	<adsm:i18nLabels>
                <adsm:include key="campoPermiParObrigatorio"/>
                <adsm:include key="campoTpRecursoObrigatorio"/>
    </adsm:i18nLabels>
	<adsm:form action="/seguranca/manterPermissao" idProperty="idPermissao" onDataLoadCallBack="localOnDataLoad" >
	
		<adsm:range width="17%">						  
			<adsm:combobox property="tpAutoridade" domain="DM_SUBJECT_PERMISSAO" 
						   label="permissaoPara" required="false" width="20%" labelWidth="15%" onchange="controlaLookup();" />
		</adsm:range>						   
		
		<adsm:hidden property="hIdUsuarioADSM" serializable="true" />

		<adsm:range width="20%">	
			<adsm:lookup dataType="text" 
					     property="permissaoPara" idProperty="id" criteriaProperty="criteria"
						 action="/seguranca/manterRecurso"
						 service="" required="true" 
						 exactMatch="false"
						 minLengthForAutoPopUpSearch="3" onDataLoadCallBack="permissaoPara"/>
 		</adsm:range>				
 								 
		<adsm:combobox property="recurso" domain="DM_TIPO_RECURSO" label="tipoRecurso" width="100%" onchange="return mudaRecurso();" required="true" />					 
		
		<adsm:hidden serializable="false"
					 property="criteriaLookupMetodo" 
					 value="metodo" 
		/>			 	 			
		
		 <adsm:lookup dataType="text" label="modulo" 
		     	 	 property="modulo" idProperty="idModuloSistema" criteriaProperty="nmModuloSistema"
	 				 minLengthForAutoPopUpSearch="3"
	  			 	 maxLength="60"
	 				 action="/seguranca/manterModulo" 
	 				 service="lms.seguranca.manterPermissaoAction.findLookupModuloSistema"
		             width="100%" required="false" disabled="true"
  	                 exactMatch="false"
		/>

		<adsm:lookup dataType="text" label="metodo" 
		     	 	 property="metodo" idProperty="idRecurso" criteriaProperty="nmRecurso"
	 				 action="/seguranca/manterMetodoTela"
	 				 service="lms.seguranca.manterPermissaoAction.findLookupMetodo"
			 	     width="100%" disabled="true" maxLength="60"
  	                 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"			 	     
	    >				
	    		    <adsm:propertyMapping modelProperty="tpMetodoTela" criteriaProperty="criteriaLookupMetodo" />	    				 					 				 
	    		    
	    		    <adsm:propertyMapping criteriaProperty="modulo.idModuloSistema"  		modelProperty="modulo.idModuloSistema" /> 	
				    <adsm:propertyMapping criteriaProperty="modulo.nmModuloSistema" 		modelProperty="modulo.nmModuloSistema" /> 	
		
				    <adsm:propertyMapping relatedProperty="modulo.idModuloSistema"  		modelProperty="idModuloSistema"	    blankFill="false" />
		  			<adsm:propertyMapping relatedProperty="modulo.nmModuloSistema"  		modelProperty="nmModuloSistema" 	blankFill="false" />
	    		    
	    </adsm:lookup>


		<adsm:hidden serializable="false"
					 property="criteriaLookupTela" 
					 value="tela" 
		/>			 	 							 		

		<adsm:lookup dataType="text" label="tela"
			     	 property="tela" idProperty="idRecurso" criteriaProperty="nmRecurso"
	  			 	 action="/seguranca/manterMetodoTela"	     
	 				 service="lms.seguranca.manterPermissaoAction.findLookupTela"	  			 	 
				     labelWidth="15%" width="20%" disabled="true"  maxLength="60"
  	                 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"						 			     
	    >
		    <adsm:propertyMapping modelProperty="tpMetodoTela" criteriaProperty="criteriaLookupTela" />	 
		    
		    <adsm:propertyMapping criteriaProperty="modulo.idModuloSistema" 		modelProperty="modulo.idModuloSistema" /> 	
		    <adsm:propertyMapping criteriaProperty="modulo.nmModuloSistema" 		modelProperty="modulo.nmModuloSistema" /> 	

		    <adsm:propertyMapping relatedProperty="modulo.idModuloSistema"  		modelProperty="idModuloSistema"	    blankFill="false" />
  			<adsm:propertyMapping relatedProperty="modulo.nmModuloSistema"  		modelProperty="nmModuloSistema" 	blankFill="false" />			    
	    </adsm:lookup>				

		<adsm:lookup dataType="text" label="aba"
			     	 property="aba" idProperty="idAba" criteriaProperty="recurso_nmRecurso"
					 action="/seguranca/manterAba"
	 				 service="lms.seguranca.manterPermissaoAction.findLookupAba"					 
					 labelWidth="10%" width="20%" disabled="true"  maxLength="60"
  	                 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"					 
		>									 					 					 
  			 <adsm:propertyMapping criteriaProperty="tela.idRecurso" modelProperty="tela.idRecurso" />
 			 <adsm:propertyMapping criteriaProperty="tela.nmRecurso" modelProperty="tela.nmRecurso" /> 			 
 			 <adsm:propertyMapping relatedProperty="tela.idRecurso"  modelProperty="tela_recurso_idRecurso" blankFill="false"  />
  			 <adsm:propertyMapping relatedProperty="tela.nmRecurso"  modelProperty="tela_recurso_nmRecurso" blankFill="false"  />
  			 
   	         <adsm:propertyMapping relatedProperty="modulo.idModuloSistema"  		modelProperty="idModuloSistema"	    blankFill="false" />
  			 <adsm:propertyMapping relatedProperty="modulo.nmModuloSistema"  		modelProperty="nmModuloSistema" 	blankFill="false" />	
  			 
		</adsm:lookup>
				 
		<adsm:lookup dataType="text" label="controle"
				     property="controle" idProperty="idRecurso" criteriaProperty="nmRecurso"
					 action="/seguranca/manterControle"
 	 				 service="lms.seguranca.manterPermissaoAction.findLookupControle"
 					 labelWidth="10%" width="20%" disabled="true"  maxLength="60"
  	                 exactMatch="false"
					 minLengthForAutoPopUpSearch="3" 					 
		> 					 
			<adsm:propertyMapping criteriaProperty="tela.idRecurso" 		modelProperty="tela.idRecurso" /> 	
		    <adsm:propertyMapping criteriaProperty="tela.nmRecurso" 		modelProperty="tela.nmRecurso" /> 			 				  
			<adsm:propertyMapping criteriaProperty="aba.idAba"  			modelProperty="aba.idAba" /> 					  
			<adsm:propertyMapping criteriaProperty="aba.recurso_nmRecurso"  modelProperty="aba.recurso_nmRecurso" /> 
			
			<adsm:propertyMapping relatedProperty="tela.idRecurso"  		modelProperty="idTela"	blankFill="false" />
  			<adsm:propertyMapping relatedProperty="tela.nmRecurso"  		modelProperty="nmTela" 	blankFill="false" />
 			<adsm:propertyMapping relatedProperty="aba.idAba"  				modelProperty="idAba" 	blankFill="false" />
  			<adsm:propertyMapping relatedProperty="aba.recurso_nmRecurso"   modelProperty="nmAba"  	blankFill="false" />					  			
  			
		    <adsm:propertyMapping relatedProperty="modulo.idModuloSistema"  		modelProperty="idModuloSistema"	    blankFill="false" />
  			<adsm:propertyMapping relatedProperty="modulo.nmModuloSistema"  		modelProperty="nmModuloSistema" 	blankFill="false" />	  			
		</adsm:lookup>  

					 
		<%-- <adsm:combobox property="tpPermissao" domain="DM_TIPO_PERMISSAO" label="tipoPermissao" required="true" /> --%>
		<adsm:combobox property="tpPermissao" domain="DM_NIVEL_ACESSO" label="tipoPermissao" required="true" />		
	
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

/* variavel de controle para saber se esta no modo de insert.  */
var ModoInsert = false;

function initWindow(eventObj) {
	
	ModoInsert = false;
	
	if (eventObj.name=="newButton_click" || eventObj.name=="tab_click")
	{	
	  limpar();
	  ModoInsert = true;
	}			
}

function limpar()
{
    setDisabled("tela.idRecurso", true);
    setDisabled("aba.idAba", true);
    setDisabled("controle.idRecurso", true);      
	setDisabled("metodo.idRecurso", true);    	 
	setDisabled("modulo.idModuloSistema", true);  
    setDisabled("permissaoPara.id",true);
}

function mudaRecurso() {

   var tr = getElementValue("recurso");
   
    resetValue("tela.idRecurso");
    resetValue("aba.idAba");
    resetValue("controle.idRecurso");      
    resetValue("metodo.idRecurso");
    resetValue("modulo.idModuloSistema");    

    setDisabled("tela.idRecurso", true);
    setDisabled("aba.idAba", true);
    setDisabled("controle.idRecurso", true);      
	setDisabled("metodo.idRecurso", true);    	
	setDisabled("modulo.idModuloSistema", true);    	   
	
	document.getElementById("metodo.nmRecurso").required = "false";
	document.getElementById("tela.nmRecurso").required = "false";
	document.getElementById("aba.recurso_nmRecurso").required = "false";
	document.getElementById("controle.nmRecurso").required = "false";	
    
   switch(tr)
   {
		case 'M': setDisabled("metodo.idRecurso", false); 	 document.getElementById("metodo.nmRecurso").required = "true";       setDisabled("modulo.idModuloSistema", false);  break;
		case 'C': setDisabled("controle.idRecurso", false);  document.getElementById("controle.nmRecurso").required = "true";     setDisabled("modulo.idModuloSistema", false);      
		case 'A': setDisabled("aba.idAba", false);  	 	 document.getElementById("aba.recurso_nmRecurso").required = "true";  setDisabled("modulo.idModuloSistema", false); 	 	
   		case 'T': setDisabled("tela.idRecurso", false);	   	 document.getElementById("tela.nmRecurso").required = "true";         setDisabled("modulo.idModuloSistema", false);    
   }   
   
   /* controle de nivel de acesso. */	   
   switch(tr)
   {
		case 'M': aplicaNivelAcesso("1");	break;
   		case 'T': aplicaNivelAcesso("1"); break;
		case 'A': aplicaNivelAcesso("0"); break;
		case 'C': aplicaNivelAcesso("0"); break;
   }      
   
}

function aplicaNivelAcesso(valor)
{
   if (ModoInsert==true) setElementValue("tpPermissao",valor);
}

/* guarda o ultimo ponteiro para o picker da lookup. */
var ultPicker   = 'permissaoPara_picker';

function permissaoPara_cb(data) {
	return lookupExactMatch({e:document.getElementById("permissaoPara.id"), data:data, callBack:'permissaoPara_criteria_likeEndMatch' });
}

function criaLookUpLista()
{
  /* LookUpde ListaPermissoes */
  
  setDisabled("permissaoPara.id",false);	
 
  document.getElementById("permissaoPara.id").id = "listaPermissao.idListaPermissao";
  document.getElementById("permissaoPara.id").name = "listaPermissao.idListaPermissao";
  
  document.getElementById("permissaoPara.criteria").id = "listaPermissao.nmListaPermissao";
  document.getElementById("permissaoPara.criteria").name = "listaPermissao.nmListaPermissao"; 
  
  document.getElementById(ultPicker).id = "listaPermissao_picker";
  ultPicker = "listaPermissao_picker";   

  document.getElementById("permissaoPara.id").property = "listaPermissao";
  document.getElementById("permissaoPara.id").idProperty = "idListaPermissao";
  document.getElementById("permissaoPara.id").criteriaProperty = "nmListaPermissao";
  document.getElementById("permissaoPara.criteria").mainElement = "listaPermissao.idListaPermissao";
  document.getElementById("permissaoPara.criteria").property = "listaPermissao";
  document.getElementById("permissaoPara.id").service = "lms.seguranca.manterPermissaoAction.findLookupListaPermissao";
  document.getElementById("permissaoPara.id").url = contextRoot+"/seguranca/manterListaPermissao.do";
  document.getElementById("permissaoPara.id").propertyMappings = 
  [  
    { modelProperty:"nmListaPermissao", criteriaProperty:"listaPermissao.nmListaPermissao", inlineQuery:true, disable:true },
    { modelProperty:"nmListaPermissao", relatedProperty:"listaPermissao.nmListaPermissao", blankFill:true }
  ];
}

function criaLookupUsuario()
{
   /* LookUp de Usuario */
  	       
  setDisabled("permissaoPara.id",false);	
       
  document.getElementById("permissaoPara.id").id = "usuario.idUsuario";
  document.getElementById("permissaoPara.id").name = "usuario.idUsuario";
  
  document.getElementById("permissaoPara.criteria").id = "usuario.nmUsuario";
  document.getElementById("permissaoPara.criteria").name = "usuario.nmUsuario";
  
  document.getElementById(ultPicker).id = "usuario_picker";
  ultPicker = "usuario_picker";	
  
  document.getElementById("permissaoPara.id").property = "usuario";
  document.getElementById("permissaoPara.id").idProperty = "idUsuario";
  document.getElementById("permissaoPara.id").criteriaProperty = "nmUsuario";
  document.getElementById("permissaoPara.criteria").mainElement = "usuario.idUsuario";
  document.getElementById("permissaoPara.criteria").property = "usuario";					  		   
  document.getElementById("permissaoPara.id").service = "lms.seguranca.manterUsuarioLMSAction.findLookupUsuarioAdsm";
  document.getElementById("permissaoPara.id").url = contextRoot+"/seguranca/manterUsuarioADSM.do";  		   		   
  document.getElementById("permissaoPara.id").propertyMappings = 
  [  
    { modelProperty:"login", criteriaProperty:"usuario.nmUsuario", inlineQuery:true, disable:true },
    { modelProperty:"login", relatedProperty:"usuario.nmUsuario", blankFill:true },
	{ modelProperty:"usuario.idUsuario", relatedProperty:"hIdUsuarioADSM", blankFill:true }		         
  ];  
  
}

function criaLookupPerfil()
{
  /* LookUp de Perfil */  	       
  
  setDisabled("permissaoPara.id",false);	
  
  document.getElementById("permissaoPara.id").id = "perfil.idPerfil";
  document.getElementById("permissaoPara.id").name = "perfil.idPerfil";
  
  document.getElementById("permissaoPara.criteria").id = "perfil.dsPerfil";
  document.getElementById("permissaoPara.criteria").name = "perfil.dsPerfil";
  
  document.getElementById(ultPicker).id = "perfil_picker";
  ultPicker = "perfil_picker";				   
  
  document.getElementById("permissaoPara.id").property = "perfil";
  document.getElementById("permissaoPara.id").idProperty = "idPerfil";
  document.getElementById("permissaoPara.id").criteriaProperty = "dsPerfil";
  document.getElementById("permissaoPara.criteria").mainElement = "perfil.idPerfil";
  document.getElementById("permissaoPara.criteria").property = "perfil";					  
  document.getElementById("permissaoPara.id").service = "lms.seguranca.manterPermissaoAction.findLookupPerfil";
  document.getElementById("permissaoPara.id").url = contextRoot+"/seguranca/manterPerfil.do";
  document.getElementById("permissaoPara.id").propertyMappings = 
  [  
    { modelProperty:"dsPerfil", criteriaProperty:"perfil.dsPerfil", inlineQuery:true, disable:true },
    { modelProperty:"dsPerfil", relatedProperty:"perfil.dsPerfil", blankFill:true }
  ];
}

function controlaLookup()
{   
   var tpPermissao = getElementValue("tpAutoridade");   
   resetValue("permissaoPara.criteria");
   resetValue("permissaoPara.id");
   setDisabled("permissaoPara.id",true);	
   
   if (tpPermissao == 'Lista')   criaLookUpLista();    
   if (tpPermissao == 'Usuário') criaLookupUsuario();
   if (tpPermissao == 'Perfil')  criaLookupPerfil();
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
    
	setElementValue("recurso",getNestedBeanPropertyValue(data, "recurso" ));
    var recurso = getElementValue("recurso");       
   	mudaRecurso();
    
    setElementValue("idPermissao",getNestedBeanPropertyValue(data, "idPermissao" ));        
    setElementValue("tpPermissao",getNestedBeanPropertyValue(data, "tpPermissao" ));
    
    setElementValue("modulo.idModuloSistema",getNestedBeanPropertyValue(data, "idModuloSistema" ));
    setElementValue("modulo.nmModuloSistema",getNestedBeanPropertyValue(data, "nmModuloSistema" ));    
    	
    setElementValue("tpAutoridade",getNestedBeanPropertyValue(data, "tpAutoridade" ));
    var tpPermissao = getElementValue("tpAutoridade");       

    if (tpPermissao == 'Lista')   
    {
	    criaLookUpLista();   
	    setElementValue("listaPermissao.idListaPermissao",getNestedBeanPropertyValue(data, "listaPermissao.idListaPermissao" ));
	    setElementValue("listaPermissao.nmListaPermissao",getNestedBeanPropertyValue(data, "listaPermissao.nmListaPermissao" ));
	}
	else
	{
	   if (tpPermissao == 'Usuário') 
       {   
          criaLookupUsuario();       
	      setElementValue("usuario.idUsuario",getNestedBeanPropertyValue(data, "usuario.idUsuario" ));
          setElementValue("usuario.nmUsuario",getNestedBeanPropertyValue(data, "usuario.login" ));
          setElementValue("hIdUsuarioADSM",getNestedBeanPropertyValue(data, "hIdUsuarioADSM" ));          
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
    }    
   

	switch(recurso)
    {
		case 'M':
			      setElementValue("metodo.idRecurso",getNestedBeanPropertyValue(data, "metodo.idRecurso" ));
				  setElementValue("metodo.nmRecurso",getNestedBeanPropertyValue(data, "metodo.nmRecurso" ));			          
		break;	     	
     	case 'C':
				  setElementValue("controle.idRecurso",getNestedBeanPropertyValue(data, "controle.idRecurso" ));
		          setElementValue("controle.nmRecurso",getNestedBeanPropertyValue(data, "controle.nmRecurso" ));
     	case 'A':			          
     		      if (recurso=='C')
     		      {     		      
	     		      setElementValue("aba.idAba",getNestedBeanPropertyValue(data, "recursoControleAbaRecursoId" ));
   			          setElementValue("aba.recurso_nmRecurso",getNestedBeanPropertyValue(data, "recursoControleAbaRecursoNm" ));
   			      }
   			      else
   			      {
   			      	  setElementValue("aba.idAba",getNestedBeanPropertyValue(data, "aba.idAba" ));
   			          setElementValue("aba.recurso_nmRecurso",getNestedBeanPropertyValue(data, "aba.nmRecurso" ));
   			      }
		case 'T':
  				 
  				switch(recurso)
				 {
			
					case 'C':
			  				  setElementValue("tela.idRecurso",getNestedBeanPropertyValue(data, "recursoControleAbaTelaRecursoId" ));		    			
		    			      setElementValue("tela.nmRecurso",getNestedBeanPropertyValue(data, "recursoControleAbaTelaRecursoNm" ));     		          
					break;		    			      
					case 'A':
			  				  setElementValue("tela.idRecurso",getNestedBeanPropertyValue(data, "recursoTelaAbaRecursoId" ));		    			
		    			      setElementValue("tela.nmRecurso",getNestedBeanPropertyValue(data, "recursoTelaAbaRecursoNm" ));     		          
					break;
					case 'T':
			  				  setElementValue("tela.idRecurso",getNestedBeanPropertyValue(data, "tela.idRecurso" ));		    			
		    			      setElementValue("tela.nmRecurso",getNestedBeanPropertyValue(data, "tela.nmRecurso" ));     		          
					break;	
				 }									
		break;					  	
	}   
	
	onDataLoad_cb(new Object(), errorMessage, errorCode, eventObj);
}

function validateTab(eventObj) {     

    // adicionar validações personalizadas            
    
    if (getElementValue("tpAutoridade") == "") {
        alert(i18NLabel.getLabel("campoPermiParObrigatorio"));
        setFocus(document.getElementById("tpAutoridade"));
        return false;
    }        

    if (getElementValue("recurso") == "") {
        alert(i18NLabel.getLabel("campoTpRecursoObrigatorio"));
        setFocus(document.getElementById("recurso"));
        return false;
    }   
 
    // script padrao de validacao da tela
    return validateTabScript(document.forms); 
 
}


</script>
