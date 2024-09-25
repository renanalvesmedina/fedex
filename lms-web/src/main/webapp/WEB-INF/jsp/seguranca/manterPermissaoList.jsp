<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterPermissaoAction" onPageLoadCallBack="localOnPageLoad">
	<adsm:form action="/seguranca/manterPermissao" >
	<adsm:i18nLabels>
	               <adsm:include key="campoPermiParObrigatorio"/>
	               <adsm:include key="campoTpRecursoObrigatorio"/>
    </adsm:i18nLabels>			
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
						 minLengthForAutoPopUpSearch="3" onDataLoadCallBack="permissaoPara" 
						 maxLength="60" />
 		</adsm:range>				
 								 
		<adsm:combobox property="recurso" domain="DM_TIPO_RECURSO" label="tipoRecurso" width="100%" onchange="return mudaRecurso();" />					 
		
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
			 	     width="100%" disabled="true" maxLength="60" minLengthForAutoPopUpSearch="3"
  	                 exactMatch="false"		 	     
	    >				
	    		    <adsm:propertyMapping modelProperty="tpMetodoTela" criteriaProperty="criteriaLookupMetodo" />	  
	    		    
    		        <adsm:propertyMapping criteriaProperty="modulo.idModuloSistema" 		modelProperty="modulo.idModuloSistema" /> 	
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
				     labelWidth="15%" width="20%" disabled="true" maxLength="60" 
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
					 labelWidth="10%" width="20%" disabled="true" maxLength="60" 
  	                 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"					 
		>									 					 					 
  			 <adsm:propertyMapping criteriaProperty="tela.idRecurso" modelProperty="tela.idRecurso" />
 			 <adsm:propertyMapping criteriaProperty="tela.nmRecurso" modelProperty="tela.nmRecurso" /> 			 
 			 <adsm:propertyMapping relatedProperty="tela.idRecurso"  modelProperty="tela_recurso_idRecurso" blankFill="false" />
  			 <adsm:propertyMapping relatedProperty="tela.nmRecurso"  modelProperty="tela_recurso_nmRecurso" blankFill="false" />
  			 
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
					 
		<adsm:combobox property="tpPermissao" domain="DM_NIVEL_ACESSO" label="tipoPermissao"  />		
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridPermissao" />
			<adsm:resetButton />
		</adsm:buttonBar>
		
	</adsm:form>
	<adsm:grid idProperty="idPermissao" property="gridPermissao"  rows="10" 
			   service="lms.seguranca.manterPermissaoAction.findPaginetedPermissao"
			   rowCountService="lms.seguranca.manterPermissaoAction.findPermissaoRowCount">
		<adsm:gridColumn property="autoridade" 					  title="permissaoPara" width="20%"/>
		<adsm:gridColumn property="tpPermissao" 				  title="tipoPermissao" width="30%"/>
		<adsm:gridColumn property="nmRecurso" 			 		  title="nmRecurso" />
		<adsm:gridColumn property="tpRecurso" 			  		  title="tipoRecurso" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
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
   
   switch(tr)
   {
		case 'M': setDisabled("metodo.idRecurso", false);     setDisabled("modulo.idModuloSistema", false);    break;
		case 'C': setDisabled("controle.idRecurso", false);   setDisabled("modulo.idModuloSistema", false);    
		case 'A': setDisabled("aba.idAba", false);   		  setDisabled("modulo.idModuloSistema", false); 	  
   		case 'T': setDisabled("tela.idRecurso", false); 	  setDisabled("modulo.idModuloSistema", false);	  
   }   
}

/* guarda o ultimo ponteiro para o picker da lookup. */
var ultPicker   = 'permissaoPara_picker';
		   
function permissaoPara_cb(data) {

	return lookupExactMatch({e:document.getElementById("permissaoPara.id"), data:data, callBack:'permissaoPara_criteria_likeEndMatch' });
}

function controlaLookup()
{
   
   var tpPermissao = getElementValue("tpAutoridade");   

   resetValue("permissaoPara.criteria");
   resetValue("permissaoPara.id");
   setDisabled("permissaoPara.id",true);	
   
   if (tpPermissao == 'Lista')
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
    
    if (tpPermissao == 'Usuário')
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
    
    if (tpPermissao == 'Perfil')
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
}    


function localOnPageLoad_cb() {
	onPageLoad_cb();
}	

function validateTab(eventObj) {     

    // adicionar validações personalizadas            
    
    if (getElementValue("tpAutoridade") == "") {
        alert(i18NLabel.getLabel("campoPermiParObrigatorio"));
        setFocus(document.getElementById("tpAutoridade"));
        return false;
    }        
 
    // script padrao de validacao da tela
    return validateTabScript(document.forms); 
 
}

</script>