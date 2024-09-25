<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

	<adsm:window title="manterContatosRecebemInformaçõesEventos"
				service="lms.sim.manterContatosRecebemInformacoesEventosAction"
				onPageLoadCallBack="contatosCallBack"
				>
	
	<adsm:form 	idProperty="idContatoComunicacao"
				action="/sim/manterContatosRecebemInformacoesEventos"
				service="lms.sim.manterContatosRecebemInformacoesEventosAction.findByIdDetalhamento"
				onDataLoadCallBack="contatosDataLoadCallBack">
				
		<adsm:hidden property="cliente.idCliente" />
		<adsm:hidden property="tpIdentificacao" />
		<adsm:hidden property="idConfiguracaoComunicacao" />
		
		<adsm:textbox dataType="text" property="pessoa.nrIdentificacao"  label="cliente" labelWidth="18%" width="18%" size="23" disabled="true"/>
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" width="64%" size="60" required="true" disabled="true"/>
	
		<adsm:combobox property="servico" optionLabelProperty="dsServico" optionProperty="idServico"  labelWidth="18%" width="82%" disabled="true"
			service="lms.configuracoes.servicoService.find" label="servico" boxWidth="300">
		</adsm:combobox>
		
		<adsm:combobox property="meioTransmissao"  domain="DM_MEIO_COMUNICACAO" label="meioTransmissao" labelWidth="18%" width="82%"  disabled="true"/>

		<adsm:combobox 	property="contato" service="lms.sim.manterContatosRecebemInformacoesEventosAction.findComboContatosCliente" 
						optionProperty="idContato" optionLabelProperty="nmContato"  label="contato" required="true" 
						labelWidth="18%" width="82%">
				<adsm:propertyMapping criteriaProperty="cliente.idCliente"  modelProperty="cliente.idCliente"/>
				<adsm:propertyMapping relatedProperty="email"  modelProperty="dsEmail"/>
		</adsm:combobox>
		
		<adsm:textbox dataType="email" property="email"  label="email" labelWidth="18%" width="82%" disabled="true" size="35"/>

		<adsm:combobox 	property="telefone" service="lms.sim.manterContatosRecebemInformacoesEventosAction.findComboTelefonesContato" 
						optionProperty="idTelefoneContato" optionLabelProperty="dsTelefoneContato"  label="telefone" 
						labelWidth="18%" width="30%">
				<adsm:propertyMapping criteriaProperty="contato"  modelProperty="contato.idContato"/>
				<adsm:propertyMapping criteriaProperty="cliente.idCliente"  modelProperty="cliente.idCliente"/>
		</adsm:combobox>

		<adsm:range label="vigencia" labelWidth="18%" width="82%">
			<adsm:textbox dataType="JTDate" property="dataInicio" size="20" required="true"/>
			<adsm:textbox dataType="JTDate" property="dataFim" size="20"/>
		</adsm:range>
	
		<adsm:buttonBar>
			
			<adsm:button id="botaoContatos" caption="contatos" action="/configuracoes/manterContatos" cmd="main" disabled="true">
                <adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" disabled="true" />
                <adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" disabled="true" />
                <adsm:linkProperty src="cliente.idCliente" target="cliente.idCliente" disabled="true" />
                <adsm:linkProperty src="tpIdentificacao" target="tpIdentificacao" disabled="true" />
             </adsm:button>
			<adsm:button id="botaoSalvar" caption="salvar" service="lms.sim.manterContatosRecebemInformacoesEventosAction.storeCustom" callbackProperty="afterStore"/>
			<adsm:button id="botaoLimpar" caption="limpar" onclick="limpar_OnClick();" disabled="false" buttonType="newButton"/>
			<adsm:removeButton id="botaoExcluir" disabled="true"/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
<script>
	function limpar_OnClick(){
		 
		//setDisabled(document,false);

		// Resetar valores dos campos
		resetValue("contato");
		resetValue("email");
		resetValue("telefone");
		resetValue("dataInicio");
		resetValue("dataFim");

		// Campos da tela
		document.getElementById("contato").masterLink = "false";
		setDisabled("contato",false);
		setDisabled("email",false);
		setDisabled("telefone",false);
		setDisabled("dataInicio",false);
		setDisabled("dataFim",false);
		 
		// Botoes		
		setDisabled("botaoSalvar",false);
		setDisabled("botaoExcluir",true);
		setDisabled("botaoContatos",true);

		// xmit direto
		document.getElementById("contato").masterLink = "true";
		notifyElementListeners({e:document.getElementById("cliente.idCliente")});
		
		setFocusOnFirstFocusableField(document);
		 
	}	
	
	function afterStore_cb(data,error,key) {
		//window.alert("afterStore_cb 1");
		store_cb(data,error,key); 
		//setElementValue("tpIdentificacao",getNestedBeanPropertyValue(data, "tpIdentificacao"));
		setDisabled("botaoExcluir",true);
		setDisabled("botaoContatos",true);
		desabilitaCampos(data, error);
		 
	}
	
	function contatosDataLoadCallBack_cb(data, error) {
		//window.alert("Data Load Call Back 1");
		document.getElementById("contato").masterLink = "false";
		notifyElementListeners({e:document.getElementById("contato")});

		document.getElementById("cliente.idCliente").masterLink = "false";
		onDataLoad_cb(data,error);	

		setElementValue("telefone",getNestedBeanPropertyValue(data, "telefone"));
		setDisabled("botaoExcluir",false);
		setDisabled("botaoContatos",false);
		document.getElementById("contato").masterLink = "true";
		document.getElementById("cliente.idCliente").masterLink = "true";
		desabilitaCampos(data, error)
	}
	
	function contatosCallBack_cb(){
		//window.alert("Call Back 1");
	
		
		onPageLoad_cb();
		// xmit direto
		document.getElementById("contato").masterLink = "true";
		notifyElementListeners({e:document.getElementById("cliente.idCliente")});
		//document.getElementById("cliente.idCliente").masterLink = "true";
		document.getElementById("idConfiguracaoComunicacao").masterLink = "true";
		
		setDisabled("botaoExcluir",true);
		setDisabled("botaoContatos",false);
	}
	function initWindow(evento){
		 
		// Desabilita documento 
		setDisabled(document,false);

		document.getElementById("contato").masterLink = "false";
		setDisabled("contato",false);		
		document.getElementById("contato").masterLink = "true";
		
		// Botoes 
		setDisabled("botaoSalvar",false);
		setDisabled("botaoExcluir",true);
		setDisabled("botaoContatos",true);

		// Estes campos sao sempre desabilitados em tela.
		setDisabled("servico",true);
		setDisabled("cliente.idCliente",true);
		setDisabled("meioTransmissao",true);
		document.getElementById("cliente.idCliente").masterLink = "true";
		 
		
	}

	function desabilitaCampos(data, error){
		//window.alert("Desab");
	
		var dtVigenciaInicialDetalhe = getNestedBeanPropertyValue(data, "dataInicio");
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		//window.alert("Resultado: " + acaoVigenciaAtual);
		if (acaoVigenciaAtual == 0) {
				// xmit direto
		
		 	//setDisabled(document,true);
			setDisabled("botaoLimpar",false);
			setDisabled("botaoSalvar",false);
			setDisabled("botaoExcluir",false);
			document.getElementById("contato").masterLink = "false";
			setDisabled("contato",false);		
			document.getElementById("contato").masterLink = "true";
			setDisabled("telefone",false);		
			setDisabled("email",false);		
			setDisabled("dataInicio",false);		
			setDisabled("dataFim",false);
		} else if (acaoVigenciaAtual == 1) {
		 	setDisabled(document,true);
			setDisabled("botaoLimpar",false);
			setDisabled("botaoSalvar",false);
			setDisabled("dataInicio",true);		
			setDisabled("dataFim",false);		
			setFocus("dataFim");
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document,true);
			setDisabled("botaoLimpar",false);
			setDisabled("dataInicio",true);		
			setDisabled("dataFim",true);		
			setFocus("botaoLimpar",false);
		}	
	}
	
		
</script>