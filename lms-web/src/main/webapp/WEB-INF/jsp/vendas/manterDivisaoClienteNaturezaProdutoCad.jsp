<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterDivisaoClienteNaturezaProdutoAction">
	<adsm:form action="/vendas/manterDivisaoClienteNaturezaProduto" onDataLoadCallBack="myOnDataLoad" idProperty="idDivisaoClienteNaturezaProduto">
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="divisaoCliente.idDivisaoCliente"/>

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
			serializable="false"
			required="true"/>

		<adsm:combobox
			label="naturezaProduto"
			labelWidth="22%" 
			boxWidth="160"
			width="78%"
			optionProperty="idNaturezaProduto"
			optionLabelProperty="dsNaturezaProduto"
			property="naturezaProduto.idNaturezaProduto" 
			service="lms.expedicao.naturezaProdutoService.find"
			onlyActiveValues="true"
			required="true"/>

		<adsm:textbox
			dataType="text"
			label="naturezaProdutoCliente" 
			labelWidth="22%"
			property="dsNaturezaProdutoCliente"
			size="51"
			maxLength="60"
			width="78%"
			required="true"/>

		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window> 

<script language="javascript" type="text/javascript">
	function initWindow(eventObj) {
		var event = eventObj.name;
		if (event == "tab_click" || event == "gridRow_click"){
			validaPermissao();
			setElementValue("idFilial",getTabGroup(document).getTab("pesq").getElementById("idFilial").value)
		}
	}

	function myOnDataLoad_cb(dados, erros) {
		onDataLoad_cb(dados,erros);
		validaPermissao();
	}

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validaPermissao(){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
		}
	}
</script>