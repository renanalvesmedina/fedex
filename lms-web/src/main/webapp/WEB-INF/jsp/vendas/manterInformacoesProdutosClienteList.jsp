<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vendas.produtoClienteService" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterInformacoesProdutosCliente">
	
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
	
		<adsm:hidden property="cliente.idCliente"/>
		
	    <adsm:textbox labelWidth="26%" width="74%" dataType="text" property="cliente.pessoa.nrIdentificacao" label="cliente" size="20" maxLength="20" disabled="true" serializable="false" >
	        <adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
	    </adsm:textbox> 
		
		<adsm:combobox property="produto.idProduto"  
					   optionLabelProperty="dsProduto" 
					   optionProperty="idProduto" 
					   service="lms.expedicao.produtoService.find" 
					   label="produto" 
					   labelWidth="26%" 
					   width="24%" boxWidth="100"/>
		
		<adsm:combobox property="embalagem.idEmbalagem" 
					   optionLabelProperty="dsEmbalagem" 
					   optionProperty="idEmbalagem" 
					   service="lms.expedicao.embalagemService.find" 
					   label="tipoEmbalagem" 
					   labelWidth="18%" 
					   width="32%"
					   boxWidth="100"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="produtoCliente"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid unique="true" gridHeight="200" idProperty="idProdutoCliente" property="produtoCliente" rows="13"
	           defaultOrder="produto_.dsProduto, embalagem_.dsEmbalagem, dsTipoClassificacao" onSelectAll="myOnSelectAll" onSelectRow="myOnSelectRow">
		<adsm:gridColumn title="produto" property="produto.dsProduto" width="28%"/>
		<adsm:gridColumn title="tipoEmbalagem" property="embalagem.dsEmbalagem" width="20%"/>
		<adsm:gridColumn title="classificacao" property="dsTipoClassificacao" width="12%"/>
		<adsm:gridColumn title="pesoCubado" mask="###,###,###,###,##0.000" property="psAforado" width="14%" align="right" unit="kg" dataType="decimal"/>
		<adsm:gridColumn title="pesoReal" mask="###,###,###,###,##0.000" property="psReal" width="13%" align="right" unit="kg" dataType="decimal"/>
		<adsm:gridColumn title="valorMedio" property="valorMedioFormatado" width="13%" align="right"/>
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
<script>
	/**
	* Criada para validar o acesso do usuário 
	* logado à filial do cliente
	*/
	function myPageLoad_cb(data, error) {
		onPageLoad_cb();
		var idFilial = getElementValue("idFilial");
		var data = new Array();	   
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdo = createServiceDataObject("lms.vendas.produtoClienteService.validatePermissao", "validarPermissoes", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	* Criada para validar o acesso do usuário 
	* logado à filial do cliente
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
