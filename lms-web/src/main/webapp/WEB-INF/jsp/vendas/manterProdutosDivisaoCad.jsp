<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.divisaoProdutoService">
	<adsm:form action="/vendas/manterProdutosDivisao" onDataLoadCallBack="myOnDataLoad" idProperty="idDivisaoProduto">
		<adsm:complement label="cliente" labelWidth="17%" 
			required="true" width="83%">
			<adsm:textbox dataType="text" disabled="true" 
				property="divisaoCliente.cliente.pessoa.nrIdentificacao"
				serializable="false"size="18"/>
			<adsm:textbox dataType="text" disabled="true" 
				property="divisaoCliente.cliente.pessoa.nmPessoa"
				serializable="false" size="30"/>
		</adsm:complement>
		<adsm:hidden property="divisaoCliente.idDivisaoCliente"/>
		<adsm:hidden property="idFilial" serializable="false"/>

		<adsm:textbox dataType="text" disabled="true" label="divisao" 
			labelWidth="17%" property="divisaoCliente.dsDivisaoCliente"
			required="true"	serializable="false" size="51" width="83%"/>

		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>

		<adsm:lookup
			label="produto"
			property="produto"
			idProperty="idProduto"
			criteriaProperty="dsProduto"
			service="lms.expedicao.produtoService.findLookup"
			action="/expedicao/manterProdutos"
			size="25"
			dataType="text"
			width="33%"
			exactMatch="false"
			labelWidth="17%"
			onPopupSetValue="configuraNatureza"
			minLengthForAutoPopUpSearch="1"
			maxLength="80"
			onDataLoadCallBack="lookup_produto"
		>
			<adsm:propertyMapping criteriaProperty="naturezaProduto.idNaturezaProduto" 
				modelProperty="naturezaProduto.idNaturezaProduto"/>
			<adsm:propertyMapping criteriaProperty="tpSituacao" 
				modelProperty="tpSituacao"/>	
		</adsm:lookup>

		<adsm:combobox
			label="naturezaProduto"
			property="naturezaProduto.idNaturezaProduto"
			optionProperty="idNaturezaProduto"
			optionLabelProperty="dsNaturezaProduto"
			service="lms.expedicao.naturezaProdutoService.findAllAtivo"
			labelWidth="17%"
			required="true"
			width="33%"
			boxWidth="160"
		/>

		<adsm:combobox
			label="valorMedioQuilograma"
			property="moeda.idMoeda"
			optionProperty="idMoeda"
			optionLabelProperty="dsSimbolo"
			service="lms.configuracoes.moedaPaisService.findMoedaByPaisUsuarioLogado"
			labelWidth="17%"
			boxWidth="80"
			width="83%"
			onDataLoadCallBack="verificaMoedaPais"
		>
			<adsm:textbox
				property="vlMedioKg"
				minValue="0.01"
				dataType="currency"
				size="10"
				mask="##,###,###,###,##0.00"
				onchange="return validaMoeda(this);"
			/>
		</adsm:combobox>
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window> 

<script language="javascript" type="text/javascript">

	function lookup_produto_cb(dados, erros) {
		configuraNatureza(dados[0]);
		return lookupExactMatch({e:document.getElementById("produto.idProduto"), callBack:"lookup_produto_like", data:dados});
	}

	function lookup_produto_like_cb(dados, erros) {
		configuraNatureza(dados[0]);
		return lookupLikeEndMatch({e:document.getElementById("produto.idProduto"), data:dados});
	}

	function configuraNatureza(dados) {
		if(dados) {
			var v = getNestedBeanPropertyValue(dados, "naturezaProduto.idNaturezaProduto");
			if(v != undefined){
				var np = getElementValue("naturezaProduto.idNaturezaProduto");
				if(v != np)
					setElementValue("naturezaProduto.idNaturezaProduto", v);
			}
		} 
	}

	function validaMoeda(obj) {
		if(obj.value.length > 0) {
			document.getElementById("moeda.idMoeda").required = "true";
		} else {
			document.getElementById("moeda.idMoeda").required = "false";
		}
	}

	var moedas = undefined;
	var moedaDefault = undefined;
	function initWindow(eventObj) {
		var event = eventObj.name;
		if(event != "storeButton" && event != "gridRow_click") {
			if(moedas) {
				moeda_idMoeda_cb(moedas);
				if(moedaDefault)
					setElementValue("moeda.idMoeda", moedaDefault);
			}
			document.getElementById("moeda.idMoeda").required = "false";
		}
		if (event == "tab_click" || event == "gridRow_click"){
			validaPermissao();
			setElementValue("idFilial",getTabGroup(document).getTab("pesq").getElementById("idFilial").value)
		}
	}

	function verificaMoedaPais_cb(dados) {
		moeda_idMoeda_cb(dados);
		moedas = dados;
		if(dados) {
			for(var i = 0; i < dados.length; i++) {
				var indUtil = getNestedBeanPropertyValue(dados[i], "blIndicadorMaisUtilizada");
				if(indUtil == true || indUtil == 'true'){
					moedaDefault = getNestedBeanPropertyValue(dados[i], "idMoeda");
					setElementValue("moeda.idMoeda", moedaDefault);
					return;
				}
			}
		}
	}

	function myOnDataLoad_cb(dados, erros) {
		onDataLoad_cb(dados,erros);
		validaMoeda(document.getElementById("vlMedioKg"));
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