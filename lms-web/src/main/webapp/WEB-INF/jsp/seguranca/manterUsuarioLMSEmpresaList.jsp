<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterUsuarioLMSEmpresaAction">
	<adsm:form action="/seguranca/manterUsuarioLMSEmpresa" idProperty="idUsuario">

		<adsm:i18nLabels>
			  <adsm:include key="exclusaoEmpresaPadrao"/>	      	      
		</adsm:i18nLabels>	
			
		<adsm:hidden  property="nmFantasia"      serializable="true" />		
		<adsm:hidden  property="idUsuarioLMS"      serializable="true" />
		
		<!-- adsm:textbox property="loginUsuarioLMS" serializable="true" label="usuario" disabled="false" dataType="text" size="20" width="40" /-->
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
					 onDataLoadCallBack="onChangeUsuarioADSM"
					 afterPopupSetValue="afterUsuarioADSMPopup"
		>	
			<adsm:propertyMapping relatedProperty="tpCategoriaUsuario" modelProperty="tpCategoriaUsuario.value" />
			<adsm:propertyMapping relatedProperty="usuarioADSM.nmUsuario" modelProperty="nmUsuario" />
 			<adsm:textbox property="usuarioADSM.nmUsuario" size="30"  dataType="text" disabled="true" />
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
			>
		<adsm:propertyMapping  relatedProperty="empresaByIdEmpresaCadastrada.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" 		/>
  			    <adsm:textbox dataType="text" disabled="true" property="empresaByIdEmpresaCadastrada.pessoa.nmPessoa"	size="30"			/>
		</adsm:lookup>			 
				 		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridUsuario" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idEmpresaUsuario" property="gridUsuario" rows="13"
			   service="lms.seguranca.manterUsuarioLMSEmpresaAction.findPaginated"
			   rowCountService="lms.seguranca.manterUsuarioLMSEmpresaAction.getRowCount" 
			   onSelectAll="habilitaBtnExcluir"
			   onSelectRow="habilitaBtnExcluir">
			<adsm:gridColumn title="tipoEmpresa" property="tpEmpresa" isDomain="true" width="100" />
			<adsm:gridColumn title="empresa" property="nmPessoa" width="50%"/>
			<adsm:gridColumn title="filialPadrao" property="nmFilialPadrao" width="40%"/>		
		<adsm:buttonBar>
			<!-- adsm:removeButton onclick="return controlaEmpresaPadrao();"/-->			
			<adsm:button  id="btnRemover" onclick="return findEmpresaPadrao();" caption="excluir" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

function initWindow(event) {

}

function habilitaBtnExcluir( event ){
	var btnRemover = document.getElementById('btnRemover');
	if( gridUsuarioGridDef.getSelectedIds().ids.length >0 && (event != true && event != false) ) {
		setDisabled(btnRemover, false);		
	}else if(event == true){
			setDisabled(btnRemover, false);		
	}else{
		setDisabled(btnRemover, true);		
	}
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
	var excluir = true;
	for( i=0; i < gridUsuarioGridDef.getSelectedIds().ids.length; i++ ){	
		if ( getNestedBeanPropertyValue(data, "_value" ) != undefined  && getNestedBeanPropertyValue(data, "_value" )!=0 ) {	
			if ( ( getNestedBeanPropertyValue(data, "_value" ) == gridUsuarioGridDef.getSelectedIds().ids[i]) ) {	
			 	//gridUsuarioGridDef.getSelectedIds().ids.pop(i);
			 	excluir = false;
			 	alert(i18NLabel.getLabel('exclusaoEmpresaPadrao') );
			}
		}
	}	

	if( excluir == true ){
		gridUsuarioGridDef.removeByIds('lms.seguranca.manterUsuarioLMSEmpresaAction.removeByIds');
	}				
	
} 

</script>