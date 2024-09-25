<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterAutoridadeListaAction" onPageLoadCallBack="localOnPageLoad">
	<adsm:form action="/seguranca/manterAutoridadeLista" >
		<adsm:range width="12%">						  
			<adsm:combobox property="tpAutoridade" domain="DM_TIPO_AUTORIDADE" 
						   label="autoridadeParaMn"  width="20%" labelWidth="15%" onchange="controlaLookup();" />
		</adsm:range>						   

		<adsm:hidden property="hIdUsuarioADSM" serializable="true" />

		<adsm:range width="60%">	
			<adsm:lookup dataType="text" disabled="true" maxLength="60" 
					     property="autoridadePara" idProperty="id" criteriaProperty="criteria"
						 action="" service="" 
 					     exactMatch="false"				 
						 minLengthForAutoPopUpSearch="3" onDataLoadCallBack="permissaoPara" />
 		</adsm:range>				 								 

		<adsm:lookup dataType="text" label="listaPermissao"  maxLength="60"
		     	 	 property="listaPermissao" idProperty="idListaPermissao" criteriaProperty="nmListaPermissao"
	 				 action="/seguranca/manterListaPermissao"
	 				 service="lms.seguranca.manterAutoridadeListaAction.findLookupListaPermissao"
			 	     width="100%"  size="25%"
	    />			

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridAutoridade" />
			<adsm:resetButton/>
		</adsm:buttonBar> 
		
	</adsm:form>
	<adsm:grid idProperty="idAutoridadeLista" property="gridAutoridade"  rows="12"
			   service="lms.seguranca.manterAutoridadeListaAction.findPaginatedAutoridadeLista" 
			   rowCountService="lms.seguranca.manterAutoridadeListaAction.findAutoridadeListaRowCount">
		<adsm:gridColumn property="nmAutoridade"				  title="autoridadePara"    width="30%"/>
		<adsm:gridColumn property="tpAutoridade" 				  title="tpAutoridadetm" 	width="30%"/>
		<adsm:gridColumn property="nmListaPermissao"	 		  title="listaPermissao"    width="40%"/>
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
        
    if (tpAutoridade == 'Usuário')
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
		   document.getElementById("autoridadePara.id").service = "lms.seguranca.manterAutoridadeListaAction.findLookupUsuarioAdsm";
		   document.getElementById("autoridadePara.id").url = contextRoot+"/seguranca/manterUsuarioADSM.do";
		   document.getElementById("autoridadePara.id").propertyMappings = 
		   [  
		     { modelProperty:"login", criteriaProperty:"usuario.nmUsuario", inlineQuery:true, disable:true },
		     { modelProperty:"login", relatedProperty:"usuario.nmUsuario", blankFill:true },
   		     { modelProperty:"usuario.idUsuario", relatedProperty:"hIdUsuarioADSM", blankFill:true }		     
		   ];
    }
    
    if (tpAutoridade == 'Perfil')
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
}    


function localOnPageLoad_cb() {
	onPageLoad_cb();		
}
</script>