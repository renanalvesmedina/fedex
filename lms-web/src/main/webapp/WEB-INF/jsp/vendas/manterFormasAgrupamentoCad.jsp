<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.formaAgrupamentoService">
	<adsm:form action="/vendas/manterFormasAgrupamento" idProperty="idFormaAgrupamento" onDataLoadCallBack="formasAgrupamentoFormLoad"
		service="lms.vendas.formaAgrupamentoService.findByIdMap">
		<adsm:i18nLabels>
			<adsm:include key="LMS-01144"/>
		</adsm:i18nLabels>

		<adsm:lookup
			service="lms.vendas.clienteService.findLookup" 
			 action="/vendas/manterDadosIdentificacao"
			 dataType="text" 
			 property="cliente" 
			 idProperty="idCliente" 
			 criteriaProperty="pessoa.nrIdentificacao" 
			 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			 onPopupSetValue="dadosClientePopupSetValue"
			 label="cliente" 
			 size="20" 
			 maxLength="20" 
			 labelWidth="15%" 
			 width="50%" 
			 exactMatch="false"
			 required="true" 
			 onchange="return changeCliente();"
			 onDataLoadCallBack="buscaDadosCliente">
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.nmPessoa"
				modelProperty="pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				property="cliente.pessoa.nmPessoa"
				size="30"
				maxLength="50"
				disabled="true"
				required="true"/>
		</adsm:lookup>

		<adsm:combobox
			property="tpSituacao"
			label="situacao"
			domain="DM_STATUS"
			width="15%"
			labelWidth="15%"
			required="true"/>
		<adsm:textbox
			label="descricao"
			property="dsFormaAgrupamento"
			maxLength="60"
			dataType="text"
			size="63"
			required="true"
			labelWidth="15%"
			width="50%"/>
		<adsm:checkbox
			property="blAutomatico"
			label="automatico"
			width="15%"
			labelWidth="15%"/>

		<adsm:textbox
			label="prioridade"
			property="nrOrdemPrioridade"
			maxLength="2"
			dataType="integer"
			size="3"
			required="true"
			labelWidth="15%"
			width="50%"/>


		<adsm:textbox
			label="seqModFat"
			property="sqCorporativo"
			maxLength="10"
			dataType="integer"
			size="11"
			labelWidth="15%"
			width="20%"/>
		

		<adsm:pairedListbox
			property="aux_source" 
			service="lms.vendas.formaAgrupamentoService.findFormasAgrupamento"
			size="20" boxWidth="200" label="campo" width="85%"
			optionLabelProperty="descricao" 
			optionProperty="idComposto" 
			orderProperty="nrOrdemPrioridade" 
			showOrderControls="true"
			listOptionProperty="formaAgrupamentoListBoxElement">
		</adsm:pairedListbox>
				
		<adsm:buttonBar>
			<adsm:button
				id="btnSalvar"
				caption="salvar"
				buttonType="newButton"
				onclick="validarFormasAgrupamentos()"
				disabled="false"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>

		<script>var labelAuxSource = '<adsm:label key="campo"/>';</script>
	</adsm:form>
</adsm:window>
<script>

	/**
	* Ao iniciar verifica se veio pelo click na aba Detalhamento,
	* ou pelo botão Novo ou pelo botão Excluir, entao
	* limpa informacoes da tabela INFORMACAO_DOCTO_CLIENTE da listagem da esquerda
	*/
	function initWindow(eventObj) {	
		if (eventObj.name == "tab_click" || eventObj.name == "newButton_click" || eventObj.name == "removeButton") {   					
	   		deletaPairedListBox(document.getElementById("_aux_source_source"),"info_dcto_cliente");   				
		} 
    }
    
    /**
    * Função chamada no retorno da carga do form    
    * Verifica se o cliente está setado e estando deleta todas as informações da listagem
    * da esquerda e refaz a pesquisa também com os dados da tabela INFORMACAO_DOCTO_CLIENTE
    */
	function formasAgrupamentoFormLoad_cb(data, error){
		var idCliente = getNestedBeanPropertyValue(data, "cliente.idCliente");		
		if( idCliente != undefined ){		    
			deletaPairedListBox(document.getElementById("_aux_source_source"));   		
			pesquisaFormasAgrupamentoByCliente(idCliente);
		}
		onDataLoad_cb(data, error);		
	}
	
	/**
	* Função de retorno da busca pelas formas de Agrupamento
	* Compara os dados da lista da direita (selecionados) e desabilita
	* os itens correspondentes na lista da esquerda.
	*/
	function myReturn_cb(data){	
		aux_source_source_cb(data);
		var campoEsq = document.getElementById("_aux_source_source");
		var campoDir = document.getElementById("aux_source");
		
		for(i=(campoDir.options.length-1); i>=0; i--) {			
			for(j=(campoEsq.options.length-1); j>=0; j--) {				
				if( campoDir.options[i].value == campoEsq.options[j].value ){
					campoEsq.options[j].disabled = true;					
					campoEsq.options[j].style.color = 'gray';
					break;
				}				
			}				
		}
	}
	
	/**
	* Função da pesquisa de Formas de Agrupamento propriamente dita.
	* Verifica se o cliente está setado e se estiver adiciona o filtro por cliente
	* na pesquisa.
	*/
	function pesquisaFormasAgrupamentoByCliente(idCliente){
		var data = new Array();	   
		if (idCliente != undefined){
			setNestedBeanPropertyValue(data, "idCliente", idCliente);
		}
		var sdo = createServiceDataObject("lms.vendas.formaAgrupamentoService.findFormasAgrupamento", "myReturn", data);
		xmit({serviceDataObjects:[sdo]}); 		
		return true;
	}
	
	/**
	* Valida seleção de pelo menos 1 item na lista da direita
	* Caso positivo chama o método store da FormaAgrupamentoService
	
	* [Diego Umpierre] - So valida o campo 'CAMPO' se o blAutomatico estiver marcado.
	* Alteração solicitada por Joelson em 12/07/2005 14:30
	*/
	function validarFormasAgrupamentos(){
		var campo = document.getElementById("aux_source");
		var blAuto = getElementValue("blAutomatico");
		if( campo.options.length == 0  &&  blAuto == true){
			alert(getMessage(erRequired, new Array(labelAuxSource)));					
			setFocus(campo);
			return false;
		} else {
			storeButtonScript('lms.vendas.formaAgrupamentoService.store', 
						  'myDataLoad', 
						  document.getElementById('form_idFormaAgrupamento'));
			return true;
		}		
	}	

	/**
	* Função de callback da lookup de clientes.
	* Após selecionar o cliente, deleta os dados da listagem da esquerda e refaz
	* a pesquisa com o filtro de cliente.
	*/	         
	function buscaDadosCliente_cb(data,exception) {
	   if (exception != undefined) {
	   	 alert(exception);
		 return;
	   }
	   
	   if (data != undefined && data.length > 0) {
	   		if (data[0].tpCliente.value == "F") {
	   			setDisabled("btnSalvar", true);
	   		} else {
	   			setDisabled("btnSalvar", false);
	   		}
	   }

  	   var retorno = cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
	   if( retorno == true ){
		   var idCliente = getNestedBeanPropertyValue(data[0], "idCliente");
		   deletaPairedListBox(document.getElementById("_aux_source_source"));
		   deletaPairedListBox(document.getElementById("aux_source"),"info_dcto_cliente");
	   	   pesquisaFormasAgrupamentoByCliente(idCliente);
	   } else {
			resetValue(document.getElementById("cliente.idCliente"));
			resetValue(document.getElementById("cliente.pessoa.nmPessoa"));
			setFocus(document.getElementById("cliente.pessoa.nrIdentificacao"));
	   }
	   return retorno;
	}

	function dadosClientePopupSetValue(data) {
		deletaPairedListBox(document.getElementById("_aux_source_source"));
		deletaPairedListBox(document.getElementById("aux_source"),"info_dcto_cliente");
		pesquisaFormasAgrupamentoByCliente(data.idCliente);
	}

	/**
	* Deleta os dados da listagem passada por parâmetro que
	* tenha o tipo igual ao tipo também informado por parâmetro.
	* Caso o tipo seja omitido deleta todos os dados da lista informada
	*/	
	function deletaPairedListBox(paired, tipoForma){
		for(i=(paired.options.length-1); i>=0; i--) {
			if( tipoForma != undefined ){
				var tmp = paired.options[i].value.split(';');
				if(tmp[3] == tipoForma){
					paired.options.remove(i);
				} 
			} else {
				paired.options.remove(i);				
			}
		}
	}	

	/**
	* Meu callback da busca dos dados. Utilizado para corrigir o problema de não lançar algum erro na tela se este acontecer.
	*/
	function myDataLoad_cb(data,erro){
		onDataLoad_cb(data,erro);
		if( erro != undefined ){
			alert(erro);
			setFocus(document.getElementById('btnSalvar'));
		}
		showSuccessMessage();
		setFocusOnFirstFocusableField(document);
	}
	
	function changeCliente() {
		if (getElementValue("cliente.pessoa.nrIdentificacao") == "") {
			setDisabled("btnSalvar", false);
		}
		return cliente_pessoa_nrIdentificacaoOnChangeHandler();
	}
</script>