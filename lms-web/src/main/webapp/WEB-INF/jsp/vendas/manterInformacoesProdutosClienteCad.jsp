<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.produtoClienteService">
	<adsm:form action="/vendas/manterInformacoesProdutosCliente" idProperty="idProdutoCliente" onDataLoadCallBack="myDataLoad">

		<adsm:hidden property="idFilial" serializable="false"/>	

		<adsm:hidden property="cliente.idCliente"/>
		<adsm:hidden property="moedaPadrao.idMoeda" serializable="false"/>

		<adsm:textbox labelWidth="26%" width="74%" dataType="text" property="cliente.pessoa.nrIdentificacao" label="cliente" size="20" maxLength="20" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
		</adsm:textbox> 

		<adsm:combobox property="produto.idProduto"
			optionLabelProperty="dsProduto"
			optionProperty="idProduto"
			service="lms.expedicao.produtoService.find"
			onlyActiveValues="true"
			label="produto"
			labelWidth="26%"
			width="24%"
			required="true" boxWidth="100"/>

		<adsm:combobox property="embalagem.idEmbalagem" 
			optionLabelProperty="dsEmbalagem" 
			optionProperty="idEmbalagem" 
			service="lms.expedicao.embalagemService.find" 
			label="tipoEmbalagem" 
			labelWidth="18%" 
			width="32%"
			onlyActiveValues="true" 
			boxWidth="100"
			required="true"
			/>

		<adsm:textbox dataType="text" property="dsTipoIdentificacaoVolumes" label="tipoIdentificadorVolumes" maxLength="30" labelWidth="26%" width="24%" size="20"/>
		
		<adsm:textbox
			label="pesoMedioVolume" 
			property="psMedioVolume" 
			dataType="weight"
			unit="kg"
			size="10"
			maxLength="18"
			labelWidth="18%"
			width="24%"
			required="true"
			/>

		<adsm:textbox
			label="pesoMedioDespacho" 
			property="psMedioDespacho" 
			dataType="weight"
			unit="kg"
			size="10"
			maxLength="18"
			labelWidth="26%"
			width="24%"
			required="true"
			/>


		<adsm:combobox
			label="valorMedioKg" 
			property="moeda.idMoeda" 
			optionLabelProperty="dsSimbolo" 
			optionProperty="idMoeda" 
			service="lms.configuracoes.moedaService.findMoedasAtivasByPaisUsuario" 
			labelWidth="18%" 
			width="13%"
			onDataLoadCallBack="carregaMoedaPadrao"
			onchange="limpaValorMedio()">
		</adsm:combobox>

		<adsm:textbox
			dataType="currency"
			property="vlMedioProdutoKilo"
			size="10"
			maxLength="18"
			mask="###,###,###,###,##0.00"
			minValue="0"
			width="19%"
			required="true"
			/>
		
		<adsm:textbox dataType="integer" property="nrMedioVolumesDespacho" label="numeroMedioVolumePorDespacho" maxLength="8" labelWidth="26%" width="24%" size="20"/>
		
		<adsm:textbox dataType="text" property="dsTipoClassificacao" label="tipoClassificacao" maxLength="30" labelWidth="18%" width="32%" size="20"/>
		<adsm:textbox dataType="weight" property="psAforado" label="pesoCubado" size="10" labelWidth="26%" width="24%" maxLength="18" unit="kg" minValue="0"/>		

		<adsm:textbox
			label="pesoReal" 
			property="psReal" 
			dataType="weight"
			unit="kg"
			size="10"
			maxLength="18"
			labelWidth="18%"
			width="24%"/>

		<adsm:checkbox 
			property="blPesoAforado"
			label="blPesoAforado"
 			labelWidth="26%" 
			width="24%"/>

		<adsm:combobox onlyActiveValues="true" 
			label="situacaoAprovacao" 
			property="situacaoAprovacao" 
			domain="DM_STATUS_WORKFLOW"
			labelWidth="18%" 
			width="32%" 
			disabled="true"
			serializable="true"/>	
			
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton" callbackProperty="afterStore" service="lms.vendas.produtoClienteService.storeMap"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	/*
	* Criada para validar acesso do usuário
	* logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		initWindow();
	}


	/*
	* Call Back do storeMap
	* Irá Atualizar o campo "Situacao da Aprovacao"
	*/
	function afterStore_cb(data, exception, key) {
		store_cb(data,exception,key);
		if (exception == undefined) {
			var store = "true";
		}	
	}	
		
	/**
	* Ao iniciar verifica se veio pelo click na aba Detalhamento,
	* ou pelo botão Novo ou pelo botão Excluir
	*/
	function initWindow(eventObj) {
		verificarMoedaPadrao(eventObj);

		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
		}

		if (eventObj != undefined && 
		   (eventObj.name == 'gridRow_click' || eventObj.name == 'tab_click')) {
			setDisabled('situacaoAprovacao', true);
	}
	}

	/**
	* Função de callback da combo de moedas.
	* Recebe uma lista de Moedas do pais do usuário que está logado, 
	* estas moedas estão ativas (teste feito na tabela MOEDA e na MOEDA_PAIS
	* Após carregar os dados para a combo, o valor da moeda padrão do pais do usuário é setado como selecionado.
	*/
	function carregaMoedaPadrao_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return false;
		}

		if( data != undefined ){
			var dados = getNestedBeanPropertyValue(data[0],"listaMoedas");
			var moedaPadrao = getNestedBeanPropertyValue(data[0],"moedaPadrao");
			
			if( dados != undefined ){
				comboboxLoadOptions({e:document.getElementById("moeda.idMoeda"), data: dados});
				
				if( moedaPadrao != undefined ){
					setElementValue(document.getElementById("moeda.idMoeda"),moedaPadrao);
					setElementValue(document.getElementById("moedaPadrao.idMoeda"),moedaPadrao);							
				}
			}		

		}
	}

	/**
	* Verifica se a moeda padrão está setada, caso não esteja seta a moeda padrão 
	* do pais de acordo com o usuário logado.	
	* Quando a chamada desta função provém do botão salvar, a moeda do pais não é setada
	*/
	function verificarMoedaPadrao(eventObj){	
		var idPadrao = getElementValue("moedaPadrao.idMoeda");	
		if( idPadrao != undefined && idPadrao != '' ){
			if(eventObj != undefined && eventObj.name != undefined && eventObj.name != 'storeButton'){
				setElementValue(document.getElementById("moeda.idMoeda"),idPadrao);					
			}
			setElementValue(document.getElementById("moedaPadrao.idMoeda"),idPadrao);
		}
	}

	/**
	* Função que limpa o campo Valor Médio cada vez que a moeda é mudado
	*/	
	function limpaValorMedio(){
		resetValue("vlMedioProdutoKilo");
	}

	document.getElementById("moedaPadrao.idMoeda").masterLink = "true";
</script>
