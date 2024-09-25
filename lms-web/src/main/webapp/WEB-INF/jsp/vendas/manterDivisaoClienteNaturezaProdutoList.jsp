<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterDivisaoClienteNaturezaProdutoAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterDivisaoClienteNaturezaProduto">
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="divisaoCliente.idDivisaoCliente"/>
		<adsm:hidden property="permissao" serializable="false"/>

		<adsm:complement
			label="cliente"
			labelWidth="22%" 
			required="true"
			width="78%"
			separator="branco">
			<adsm:textbox
				dataType="text"
				disabled="true" 
				property="divisaoCliente.cliente.pessoa.nrIdentificacao"
				serializable="false"
				size="18"/>
			<adsm:textbox
				dataType="text"
				disabled="true" 
				property="divisaoCliente.cliente.pessoa.nmPessoa"
				serializable="false"
				size="30"/>
		</adsm:complement>

		<adsm:textbox
			dataType="text"
			disabled="true"
			label="divisao" 
			labelWidth="22%"
			width="78%"
			size="51"
			property="divisaoCliente.dsDivisaoCliente"
			serializable="false"/>

		<adsm:combobox
			label="naturezaProduto"
			labelWidth="22%" 
			boxWidth="160"
			width="78%"
			optionProperty="idNaturezaProduto"
			optionLabelProperty="dsNaturezaProduto"
			property="naturezaProduto.idNaturezaProduto" 
			service="lms.expedicao.naturezaProdutoService.find"/>

		<adsm:textbox
			dataType="text"
			label="naturezaProdutoCliente" 
			labelWidth="22%"
			property="dsNaturezaProdutoCliente"
			size="51"
			maxLength="60"
			width="78%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="divisaoClienteNaturezaProduto"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid
		idProperty="idDivisaoClienteNaturezaProduto"
		onSelectRow="permissaoExcluir"
		onSelectAll="permissaoExcluir"
		property="divisaoClienteNaturezaProduto"
		rowCountService="lms.vendas.manterDivisaoClienteNaturezaProdutoAction.getRowCountNaturezaProduto"
		service="lms.vendas.manterDivisaoClienteNaturezaProdutoAction.findPaginatedNaturezaProduto"
		gridHeight="200"
		unique="true"
		rows="11">
		<adsm:gridColumn
			property="naturezaProduto.dsNaturezaProduto"
			title="naturezaProduto"
			width="35%"/>

		<adsm:gridColumn
			property="dsNaturezaProdutoCliente"
			title="naturezaProdutoCliente"
			width="65%"/>

		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script language="javascript">
	function permissaoExcluir(data){
		if (document.getElementById("permissao").value!="true") {
			setDisabled("removeButton", true);
			return false;
		}
	}
	
	function myPageLoad_cb(){
		onPageLoad_cb();
		var idFilial = getElementValue("idFilial");
		var data = new Array();	
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdd = createServiceDataObject("lms.vendas.manterDivisaoClienteNaturezaProdutoAction.validatePermissao", "validarPermissoes", data);
		xmit({serviceDataObjects:[sdd]});
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
</script>
