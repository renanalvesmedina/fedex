<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.classificacaoClienteService">
	<adsm:form action="/vendas/manterClassificacoesCliente" idProperty="idClassificacaoCliente" onDataLoadCallBack="myDataLoad">
	    <adsm:hidden property="idFilial" serializable="false"/>
	    <adsm:hidden property="cliente.idCliente"/>
	    <adsm:textbox width="85%" dataType="text" property="cliente.pessoa.nrIdentificacao" label="cliente" size="20" maxLength="20" disabled="true" serializable="false" >
	        <adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
	    </adsm:textbox>                    
	    
		<adsm:combobox width="85%" property="descClassificacaoCliente.idDescClassificacaoCliente" boxWidth="300" 
			label="classificacao" 
			optionLabelProperty="dsDescClassificacaoClienteComp" optionProperty="idDescClassificacaoCliente" 
			service="lms.vendas.manterClassificacoesClienteAction.findClassificacoesAtivas"			
			required="true" onlyActiveValues="true"/>
		
		<adsm:buttonBar>
	    	<adsm:storeButton id="storeButton"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		initWindow();
	}
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function initWindow(){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
		}
	}

</script>