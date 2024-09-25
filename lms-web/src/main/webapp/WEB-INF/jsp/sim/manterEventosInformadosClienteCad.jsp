<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>

//função que popula a pairedListBox
function pairedListbox_cb() {
   onPageLoad_cb();
   clearList();
   _serviceDataObjects = new Array();
   addServiceDataObject(createServiceDataObject("lms.sim.manterEventosInformadosClienteAction.findEventosByTpDocumento", "eventos_source", {tpDocumento:getElementValue("tpDocumento")}));
   xmit();
}
</script>

	<adsm:window 	title="manterEventosqueoClienteDesejaserInformado"
					service="lms.sim.manterEventosInformadosClienteAction"	
					 onPageLoadCallBack="pairedListbox"
					>
					

	<adsm:form 	idProperty="idConfiguracaoComunicacao" 
				action="/sim/manterEventosInformadosCliente"
				service="lms.sim.manterEventosInformadosClienteAction.findByIdDetalhamento"
				onDataLoadCallBack="pageLoad"
				>
				
		<adsm:hidden property="idConfiguracaoComunicacao" />		
		<adsm:hidden property="tpIdentificacao" />		

		<adsm:combobox property="tpCliente" label="tipoCliente" domain="DM_TIPO_CLIENTE" labelWidth="21%" width="79%"  required="true"/>
		
		<adsm:lookup 	property="cliente" 
						idProperty="idCliente" 
						criteriaProperty="pessoa.nrIdentificacao"    
						relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.sim.manterPedidosComprasAction.findLookupCliente" 
					 action="/coleta/cadastrarPedidoColeta" cmd="consultarClientes" dataType="text" 
					 label="cliente"  size="20" maxLength="20" width="75%" labelWidth="21%" required="true">	
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa"  size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>


		<adsm:combobox property="tpDocumento" label="documento" domain="DM_TIPO_DOCUMENTO_CLIENTE"  labelWidth="21%" onchange="pairedListbox_cb()"  
		width="75%"	/>

		<adsm:pairedListbox property="eventos" label="eventos"  
            service=""  
            size="6" boxWidth="200" labelWidth="21%" width="79%" 
			showOrderControls="true" optionLabelProperty="dsEvento" 
			optionProperty="idEvento" required="true" >
			
			</adsm:pairedListbox>


	    <adsm:combobox property="tipoAcesso" domain="DM_TIPO_ACESSO_EVENTO" label="tipoAcesso" labelWidth="21%" width="29%" required="true" />
	    <adsm:combobox property="formaComunicacao" domain="DM_MEIO_COMUNICACAO" label="formaComunicacao" labelWidth="21%" width="29%" />
   	
		<adsm:combobox property="servico" optionLabelProperty="dsServico" optionProperty="idServico"  
			labelWidth="21%" width="29%"
			service="lms.configuracoes.servicoService.find" label="servico" />	
			
		<adsm:range label="vigencia" labelWidth="21%" width="79%">
			<adsm:textbox dataType="JTDate" property="dataInicio" size="20" required="true"/>
			<adsm:textbox dataType="JTDate" property="dataFim" size="20"/>
		</adsm:range>
		
	    <adsm:section caption="comunicacao" />	
	
		<adsm:textbox dataType="integer" property="intervaloComunicacao" label="intervaloComunicacao" unit="dias" labelWidth="21%" width="79%" size="4" maxLength="2" onchange="limpaComunicarCadaEvento()"/>	
		<adsm:textbox dataType="JTTime" property="horario" label="horarioDeterminado" labelWidth="21%" width="79%" size="20" onchange="limpaComunicarCadaEvento()"/>	
	
		<adsm:checkbox property="comunicarCadaEvento" label="comunicarCadaEvento"  labelWidth="21%" width="29%" onclick="limpaCamposComunicacao()"/>
		<adsm:checkbox property="somenteDiasUteis" label="somenteDiasUteis" labelWidth="21%" width="29%" onclick="limpaComunicarCadaEvento()"/>

		<adsm:buttonBar>
			<adsm:button id="botaoContatosCliente" caption="contatosCliente" action="/sim/manterContatosRecebemInformacoesEventos" cmd="main">
                <adsm:linkProperty src="cliente.idCliente" target="cliente.idCliente" disabled="true" />
                <adsm:linkProperty src="cliente.pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" disabled="true" />
                <adsm:linkProperty src="cliente.pessoa.nmPessoa" target="pessoa.nmPessoa" disabled="true" />
                <adsm:linkProperty src="servico" target="servico" disabled="true" />
                <adsm:linkProperty src="formaComunicacao" target="meioTransmissao" disabled="true" />
                <adsm:linkProperty src="idConfiguracaoComunicacao" target="idConfiguracaoComunicacao" disabled="true" />
                <adsm:linkProperty src="tpIdentificacao" target="tpIdentificacao" disabled="true" />

                
             </adsm:button>
		
			<adsm:button id="botaoSalvar" caption="salvar" service="lms.sim.manterEventosInformadosClienteAction.storeCustom" callbackProperty="afterStore" disabled="false"/>
			<adsm:button id="botaoLimpar" caption="limpar" onclick="limpar_OnClick();" disabled="false" buttonType="newButton"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function limpar_OnClick(){
		resetValue("tpCliente");
		resetValue("cliente.pessoa.nrIdentificacao");
		resetValue("cliente.pessoa.nmPessoa");
		resetValue("tpDocumento");
		resetValue("tipoAcesso");
		resetValue("formaComunicacao");
		resetValue("servico");
		resetValue("dataInicio");
		resetValue("dataFim");
		resetValue("intervaloComunicacao");
		resetValue("horario");
		resetValue("comunicarCadaEvento");
		resetValue("somenteDiasUteis");
		resetValue("eventos");

	 	setDisabled(document,false);
		setDisabled("__buttonBar:0.removeButton",true);
		setDisabled("cliente.pessoa.nmPessoa",true);
		setFocus("tpCliente");
		
		//resetValue(document.getElementById("eventos_source"));
 		clearList();
	}	

	
	
	function initWindow(evento){
		
		setDisabled("botaoSalvar",false);
		setDisabled("botaoContatosCliente",false);
		setDisabled(document,false);
		if (evento.name == "tab_click" || evento.name == "newButton_click") {
			setDisabled(document,false); 
			clearList();
		}
		setDisabled("cliente.pessoa.nmPessoa",true);
		
	}
	function reportTemp(){
		
	}
	function afterStore_cb(data,error,key) {
		store_cb(data,error,key);

		setElementValue("idConfiguracaoComunicacao",getNestedBeanPropertyValue(data, "idConfiguracaoComunicacao"));
		setElementValue("tpIdentificacao",getNestedBeanPropertyValue(data, "tpIdentificacao"));
	}
	
	
	///DAQUI PRA BAIXO É SO PARA A LIST
		function loadList() {
			// Este metodo deve estar retornando a lista dos valores que o cliente já possui
			var sdo = createServiceDataObject("lms.sim.manterEventosInformadosClienteAction.findEventosCliente","resultLoad",{idCliente:getElementValue("cliente.idCliente")});
			xmit({serviceDataObjects:[sdo]});
		}
		function clearList() {
			// Limpa a lista de itens escolhidos
			resetValue("eventos");
			//resetValue(document.getElementById("eventos_source"));
			// Limpa a lista de itens disponíveis
			eventosListboxDef.clearSourceOptions();

		}
		function popula_cb(data,exception){
		}
		function resultLoad_cb(data,exception) {
		
		}
		
		//change lookup
		function changeLookup() {
			// Funcao é chamada para limpar a lista antes de ser populada novamente por valores novos ? (1)
			if (getElementValue("cliente.pessoa.nrIdentificacao") == "")
				clearList();
			return cliente_pessoa_nrIdentificacaoOnChangeHandler();
			
		}
		//CallBack
		function dataLoadLookup_cb(data,exception) {
			loadList();
		}
		//PopUp
		function popUpLookup(data) {
				loadList();			
		}
	
		function pageLoad_cb(data, error) {
			onDataLoad_cb(data,error);
			var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
			setElementValue("idConfiguracaoComunicacao",getNestedBeanPropertyValue(data, "idConfiguracaoComunicacao"));
		 	setDisabled(document,false);
			if (acaoVigenciaAtual == 0) {
				setDisabled("__buttonBar:0.removeButton",false);
			} else if (acaoVigenciaAtual == 1) {
			 	setDisabled(document,true);
				setDisabled("botaoLimpar",false);
				setDisabled("dataInicio",true);		
				setDisabled("dataFim",true);		
				setFocus("botaoLimpar",false);
			} else if (acaoVigenciaAtual == 2) {
				setDisabled(document,true);
				setDisabled("botaoLimpar",false);
				setDisabled("dataInicio",true);		
				setDisabled("dataFim",true);		
				setFocus("botaoLimpar",false);
			 }
		setDisabled("cliente.pessoa.nmPessoa",true);
		
		}
		function limpaComunicarCadaEvento(){
			setElementValue("comunicarCadaEvento","");
		}
		function limpaCamposComunicacao(){
			setElementValue("intervaloComunicacao","");
			setElementValue("horario","");
			setElementValue("somenteDiasUteis","");
		
		}

</script>
