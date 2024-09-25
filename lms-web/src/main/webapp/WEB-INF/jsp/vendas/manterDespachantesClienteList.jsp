<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.clienteDespachanteService" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterDespachantesCliente" >
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>
		<adsm:textbox label="cliente" property="cliente.pessoa.nrIdentificacao" dataType="text" size="20" maxLength="20" labelWidth="15%" width="85%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="60" disabled="true" serializable="false"/>
		</adsm:textbox>	

		<adsm:lookup property="despachante" idProperty="idDespachante" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="despachante" action="vendas/manterDespachantes" 
			service="lms.vendas.manterDespachantesClienteAction.findLookupDespachantes" dataType="text" 
			size="20" maxLength="20" width="85%">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="despachante.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="despachante.pessoa.nmPessoa" disabled="true" size="30"/>
		</adsm:lookup>

		<adsm:combobox domain="DM_LOCAL_DESPACHO" label="local" property="tpLocal"/>
		<adsm:combobox domain="DM_STATUS" label="situacao" property="tpSituacao"/>					
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="clienteDespachante"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idClienteDespachante" property="clienteDespachante" gridHeight="200" unique="true" 
				onSelectAll="myOnSelectAll" onSelectRow="myOnSelectRow"
				rows="11"
				>
		<adsm:gridColumn title="despachante" property="nmPessoa" width="35%" />
		<adsm:gridColumn title="local" property="tpLocal" isDomain="true" width="25%" />		
		<adsm:gridColumn title="tipoTelefone" property="tpTelefone" width="10%"/>
		<adsm:gridColumn title="usoTelefone" property="tpUso" width="10%"/>
		<adsm:gridColumn title="ddi" property="nrDdi" width="10%" align="right"/>
		<adsm:gridColumn title="telefone" property="nrTelefone" width="10%" align="right"/>
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
		var sdo = createServiceDataObject("lms.vendas.clienteDespachanteService.validatePermissaoUsuarioLogado", "validatePermissoes", data);
		xmit({serviceDataObjects:[sdo]});
	}

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validatePermissoes_cb(data, error){
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
