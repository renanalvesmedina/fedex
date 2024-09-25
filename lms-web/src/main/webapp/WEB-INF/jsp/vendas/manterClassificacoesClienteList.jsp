<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterClassificacoesClienteAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterClassificacoesCliente" >
	    <adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>
	    <adsm:textbox width="85%" dataType="text" property="cliente.pessoa.nrIdentificacao" label="cliente" size="20" maxLength="20" disabled="true" serializable="false" >
	        <adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
	    </adsm:textbox>            
	            
		<adsm:combobox width="85%" 
		               boxWidth="300"
					   property="descClassificacaoCliente.idDescClassificacaoCliente" 
					   label="classificacao" 
					   optionLabelProperty="dsDescClassificacaoClienteComp" 
					   optionProperty="idDescClassificacaoCliente" 
					   service="lms.vendas.manterClassificacoesClienteAction.find"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="classificacoes"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idClassificacaoCliente" property="classificacoes" 
			   defaultOrder="descClassificacaoCliente_tipoClassificacaoCliente_.dsTipoClassificacaoCliente, descClassificacaoCliente_.dsDescClassificacaoCliente" 
			   selectionMode="check" rows="13" unique="true" onSelectAll="myOnSelectAll" onSelectRow="myOnSelectRow">
		<adsm:gridColumn title="classificacao" property="dsDescClassificacaoClienteComp" width="100%" />
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
	    </adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myPageLoad_cb(data, error) {
		onPageLoad_cb();
		var idFilial = getElementValue("idFilial");
		var data = new Array();	   
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdo = createServiceDataObject("lms.vendas.classificacaoClienteService.validatePermissao","validarPermissoes",data);
		xmit({serviceDataObjects:[sdo]});
	}                                      
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validarPermissoes_cb(data, error){		
		setElementValue("permissao", data._value);
		if(data._value!="true") {
			setDisabled("removeButton", true);
		}
	}

	// Seta a propriedade masterLink para true para que o campo não seja limpo com a funcionalidade "Limpar"
	document.getElementById("permissao").masterLink = "true";

	/**
	* Esta função é utilizada para verificar a permissão de acesso pelo usuário
	* em relação a filial responsável operacional pelo cliente, desabilitando
	* o botão de excluir da listagem caso não tenha permissão.
	*/
	function myOnSelectRow(){

		var permissao = document.getElementById("permissao");
		
		if( permissao.value != "true" ){
			setDisabled("removeButton",true);
			return false;
		}
	}
	
	/**
	* Esta função deve executar exatamente a mesma tarefa que a função myOnSelectRow.
	*/
	function myOnSelectAll(){
		return myOnSelectRow();
	}

</script>