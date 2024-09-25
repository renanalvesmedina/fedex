<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterUsuarioLMSClienteAction" onPageLoadCallBack="onLocalPageLoad">
	<adsm:form action="/seguranca/manterUsuarioLMSCliente" >
									
		<adsm:hidden property="criteriahModoConsultaNoJoin" value="sim" serializable="false" />

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
					 required="false"
		>				
		 <adsm:propertyMapping relatedProperty="clienteDoUsuario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
		 <adsm:propertyMapping relatedProperty="clienteDoUsuario.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado" />
 		 <adsm:textbox property="clienteDoUsuario.pessoa.nmPessoa" size="30"  dataType="text" disabled="true" />			
		</adsm:lookup>
			
		<adsm:textbox property="login" size="15" maxLength="60" label="login" dataType="text" labelWidth="150" width="30%" />		
		
		<adsm:textbox dataType="text" property="nmUsuario" label="nome" width="35%" labelWidth="170" size="35" maxLength="60" />
				
		<adsm:textbox dataType="JTDateTimeZone" property="dhCadastro" label="dataAlteracao" width="30%" size="20" maxLength="20" labelWidth="150"/>		
		
		<adsm:combobox property="blAtivo" label="ativo" boxWidth="100" domain="DM_SIM_NAO" width="35%" labelWidth="170"/>		
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridUsuario" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid 
				property="gridUsuario" 
				idProperty="idUsuario"
				rows="12" 
				service="lms.seguranca.manterUsuarioLMSClienteAction.findPaginatedUsuarioLmsCliente"
 			  rowCountService="lms.seguranca.manterUsuarioLMSClienteAction.getRowCountUsuarioLmsCliente"
  	>
		<adsm:gridColumn property="login" title="usuario" width="80" /> 
		<adsm:gridColumn property="nmUsuario" title="" width="430"/> 
		<adsm:gridColumn property="dhCadastro" title="dataDeCadastro" dataType="JTDateTimeZone" width="150"/>
		<adsm:gridColumn property="blAtivo" title="ativo" renderMode="image-check" />		
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

function onLocalPageLoad_cb(){
	verificaClienteAssociado();
	
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

</script>