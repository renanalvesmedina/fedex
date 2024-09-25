<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.documentoClienteService" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterDocumentosCliente" >

		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>

		<adsm:textbox label="cliente" property="cliente.pessoa.nrIdentificacao" dataType="text" size="20" maxLength="20" labelWidth="15%" width="85%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="60" disabled="true" serializable="false"/>
		</adsm:textbox>
		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL"/>
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA"/>		

		<adsm:combobox
			label="tipoDocumento"
			property="tipoDocumentoEntrega.idTipoDocumentoEntrega"
			optionProperty="idTipoDocumentoEntrega"
			optionLabelProperty="dsTipoDocumentoEntregaTpDocumentoCobranca"
			service="lms.entrega.tipoDocumentoEntregaService.find"
			style="width:250"
		/>

		<adsm:combobox
			label="faturaVinculada"
			property="blFaturaVinculada"
			domain="DM_SIM_NAO"
		/>

		<adsm:textbox
			label="vigencia"
			property="dtVigencia"
			dataType="JTDate"
		/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="documentoCliente"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid
		property="documentoCliente"
		idProperty="idDocumentoCliente"
		gridHeight="200"
		unique="true"
		onSelectAll="myOnSelectAll"
		onSelectRow="myOnSelectRow"
		service="lms.vendas.documentoClienteService.findPaginatedTyped"
		rowCountService="lms.vendas.documentoClienteService.getRowCountTyped"
		rows="11"
	>
		<adsm:gridColumn title="modal" property="tpModal" isDomain="true" width="15%" />
		<adsm:gridColumn title="abrangencia" property="tpAbrangencia" isDomain="true" width="15%" />
		<adsm:gridColumn title="tipoDocumento" property="tipoDocumentoEntrega.dsTipoDocumentoEntregaTpDocumentoCobranca" width="30%" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="20%"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="20%"/>
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<Script>
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myPageLoad_cb(data, error) {
		onPageLoad_cb();
		var idFilial = getElementValue("idFilial");
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdo = createServiceDataObject("lms.vendas.documentoClienteService.validatePermissaoUsuarioLogado", "validatePermissoes", data);
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
