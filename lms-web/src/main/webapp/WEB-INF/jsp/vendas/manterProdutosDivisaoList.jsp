<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.divisaoProdutoService" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterProdutosDivisao" idProperty="idProdutoDivisao">
		<adsm:complement label="cliente" labelWidth="17%" 
			required="true" width="83%"	>
			<adsm:textbox dataType="text" disabled="true" 
				property="divisaoCliente.cliente.pessoa.nrIdentificacao"
				serializable="false" size="18"/>
			<adsm:textbox dataType="text" disabled="true" 
				property="divisaoCliente.cliente.pessoa.nmPessoa"
				serializable="false" size="30"/>
		</adsm:complement>
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="divisaoCliente.idDivisaoCliente"/>
		<adsm:textbox dataType="text" disabled="true" label="divisao" 
			labelWidth="17%" property="divisaoCliente.dsDivisaoCliente"
			serializable="false" size="51" width="83%"/>
			
		<adsm:lookup action="/expedicao/manterProdutos" size="25" 
			criteriaProperty="dsProduto" dataType="text" width="33%"
			exactMatch="false"	idProperty="idProduto" labelWidth="17%"
			label="produto" onPopupSetValue="configuraNatureza" 
			minLengthForAutoPopUpSearch="1" property="produto"  
			maxLength="80" onDataLoadCallBack="lookup_produto" 
			service="lms.expedicao.produtoService.findLookup">
			<adsm:propertyMapping criteriaProperty="naturezaProduto.idNaturezaProduto" 
				modelProperty="naturezaProduto.idNaturezaProduto"/>
		</adsm:lookup>	
		<adsm:combobox label="naturezaProduto" labelWidth="17%" 
			optionLabelProperty="dsNaturezaProduto" boxWidth="160"
			optionProperty="idNaturezaProduto" width="33%" 
			property="naturezaProduto.idNaturezaProduto" 
			service="lms.expedicao.naturezaProdutoService.find" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="produtosDivisao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idDivisaoProduto" onSelectRow="permissaoExcluir" onSelectAll="permissaoExcluir" property="produtosDivisao" gridHeight="200" unique="true" defaultOrder="produto_.dsProduto" rows="12">
		<adsm:gridColumn property="produto.dsProduto" title="produto" width="45%"/>
		<adsm:gridColumn property="naturezaProduto.dsNaturezaProduto" title="naturezaProduto" width="35%"/>
		<adsm:gridColumnGroup customSeparator=" ">
                  <adsm:gridColumn property="moeda.sgMoeda" dataType="text" title="valorMedioQuilograma" width="30"/>
                  <adsm:gridColumn property="moeda.dsSimbolo" dataType="text" title="" width="30"/>
        </adsm:gridColumnGroup>
		<adsm:gridColumn title="" dataType="currency" property="vlMedioKg" width="80" align="right"/>
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script language="javascript">

	function lookup_produto_cb(dados, erros) {
   		configuraNatureza(dados[0]);
   	 	return lookupExactMatch({e:document.getElementById("produto.idProduto"), callBack:"lookup_produto_like", data:dados});
   	}
    
   	function lookup_produto_like_cb(dados, erros) {
   		configuraNatureza(dados[0]);
   		return lookupLikeEndMatch({e:document.getElementById("produto.idProduto"), data:dados});
   	}
    

	function configuraNatureza(dados){
   		if(dados) {
    		var v = getNestedBeanPropertyValue(dados, "naturezaProduto.idNaturezaProduto");
	    	if(v != undefined){
	    		var np = getElementValue("naturezaProduto.idNaturezaProduto");
	    		if(v != np)
	    			setElementValue("naturezaProduto.idNaturezaProduto", v);
	    	}
	    } 
   	}

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
	var sdd = createServiceDataObject("lms.vendas.manterProdutosDivisaoAction.validatePermissao", "validarPermissoes", data);
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
