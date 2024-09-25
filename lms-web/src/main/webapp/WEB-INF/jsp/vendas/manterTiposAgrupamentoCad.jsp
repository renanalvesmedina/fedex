<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.tipoAgrupamentoService">
	<adsm:form action="/vendas/manterTiposAgrupamento" idProperty="idTipoAgrupamento" onDataLoadCallBack="myDataLoad">

		<adsm:hidden property="agrupamentoCliente.idAgrupamentoCliente"/>
		
		<adsm:complement 
			label="cliente"
			width="80%"
			labelWidth="20%"
			>
			<adsm:textbox 
				dataType="text"
				property="agrupamentoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao"
				size="20" 
				maxLength="20"
				disabled="true"
				serializable="false"
			/>
			<adsm:textbox 
				dataType="text" 
				maxLength="50" 
				property="agrupamentoCliente.divisaoCliente.cliente.pessoa.nmPessoa"
				size="30" 
				disabled="true"
				required="true"
				serializable="false"
			/>
		</adsm:complement>

		<adsm:textbox 
			label="divisao"
			dataType="text" 
			maxLength="50" 
			property="agrupamentoCliente.divisaoCliente.dsDivisaoCliente"
			size="30" 
			width="30%"
			labelWidth="20%"
			disabled="true"
			required="true"
			serializable="false"
		/>

		<adsm:textbox 
			label="formaAgrupamento"
			dataType="text" 
			maxLength="50" 
			labelWidth="20%" 
			width="30%"
			size="30" 
			property="agrupamentoCliente.formaAgrupamento.dsFormaAgrupamento"
			disabled="true"
			required="true"
			serializable="false"
		/>
		
		<adsm:textbox 
			label="codigo"
			maxLength="10" 
			labelWidth="20%" 
			width="30%"		
			dataType="text" 
			size="10" 
			property="cdTipoAgrupamento"
			required="true"
		/>

		<adsm:textbox 
			label="descricao"
			maxLength="60" 
			labelWidth="20%" 
			width="30%"		
			dataType="text" 
			size="30" 
			property="dsTipoAgrupamento"
			required="true"
		/>

		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window> 
<script language="javascript">

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
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
	
	function initWindow(obj){
		if (obj.name == "tab_click" || obj.name == "gridRow_click"){
			validaPermissao();
		}
	}
	

</script>