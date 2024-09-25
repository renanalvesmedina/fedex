<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myPageLoadCallBack">
	<adsm:form action="/contasReceber/emitirExtratoCliente">
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-36146"/>
		</adsm:i18nLabels>
		
		<adsm:hidden property="sgFilial"/>
		<adsm:lookup property="filialByIdFilial" 
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.contasreceber.emitirExtratoClienteAction.findLookupFilial" 
					 dataType="text"  
					 label="filialCobranca" 
					 size="3" 
					 action="/municipios/manterFiliais" 
					 labelWidth="20%"
					 width="30%" 
					 minLengthForAutoPopUpSearch="3" 
					 exactMatch="false" 
					 style="width:45px" 
					 maxLength="3">
					 
			<adsm:propertyMapping relatedProperty="filialByIdFilial.pessoa.nmFantasia" 
								  modelProperty="pessoa.nmFantasia" />
								  
			<adsm:propertyMapping relatedProperty="sgFilial" 
								  modelProperty="sgFilial" />					  
					
			<adsm:textbox dataType="text" 
						  property="filialByIdFilial.pessoa.nmFantasia" 
						  size="30" 
						  serializable="true" 
						  disabled="true"/>
						  
		</adsm:lookup>
		
		<adsm:combobox property="tpFrete" 
					   label="tipoFrete" 
					   labelWidth="20%"
					   width="30%" 
					   domain="DM_TIPO_FRETE"/>	
		
		<adsm:range label="periodo" labelWidth="20%" width="30%" required="true" >
			<adsm:textbox property="dtInicial" dataType="JTDate"/>
			<adsm:textbox property="dtFinal" dataType="JTDate"/>
		</adsm:range>
		
		<adsm:hidden property="dsServico"/>
		<adsm:combobox property="servico.idServico" 
					   label="servico"  
					   service="lms.contasreceber.emitirExtratoClienteAction.findComboServico" 
					   labelWidth="20%"
					   width="30%"
					   optionLabelProperty="dsServico" 
					   optionProperty="idServico" 
					   boxWidth="150">
					   
					   <adsm:propertyMapping relatedProperty="dsServico" 
								  modelProperty="dsServico" />	
								  
		</adsm:combobox>
					   
		<adsm:combobox property="modal" 
					   label="modal" 
					   labelWidth="20%"
					   width="30%" 
					   domain="DM_MODAL"/>	
					   
		<adsm:combobox property="abrangencia" 
					   label="abrangencia" 
					   labelWidth="20%"
					   width="30%" 
					   domain="DM_ABRANGENCIA"/>
					   
		<adsm:combobox property="estadoCobranca" 
					   label="estadoCobranca" 
					   labelWidth="20%"
  					   width="30%" 
					   domain="DM_ESTADO_COBRANCA_EXTRATO"/>	
		
		<adsm:hidden property="sgSimbolo"/>			   
		<adsm:combobox label="moeda"
					   property="moeda.idMoeda"
					   optionProperty="idMoeda"
					   required="true"
					   optionLabelProperty="siglaSimbolo"
					   labelWidth="20%"
					   width="30%"
					   boxWidth="130"
					   service="lms.contasreceber.emitirExtratoClienteAction.findMoedasByCombo">

					   <adsm:propertyMapping relatedProperty="sgSimbolo" modelProperty="siglaSimbolo" />	
		</adsm:combobox>			   
					   
		<adsm:checkbox label="totalPorFilial" 
					   property="totalPorFilial" 
					   width="30%" 
					   labelWidth="20%"/>

		<adsm:checkbox label="soTotais" 
					   property="soTotais" 
					   width="30%" 
					   labelWidth="20%"/>
					   
		<%-- ###################    MULTIPLA ESCOLHA DE CLIENTES    ################# --%>
		
		<adsm:section caption="multiplaEscolhaClientes"/>

		<adsm:listbox size="2"
					  property="clientes"
					  optionProperty="idCliente"
					  width="80%"
					  onchange="myOnChange(this)"
					  onContentChange="myOnContentChange"
					  showOrderControls="false"
					  boxWidth="505"
					  showIndex="false"
					  serializable="true"
					  label="cliente"
					  labelWidth="20%"
					  required="false"
					  allowMultiple="false">
			
			<adsm:lookup 
				action="/vendas/manterDadosIdentificacao" 
				criteriaProperty="pessoa.nrIdentificacao" 
				relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				dataType="text" 
				exactMatch="true" 
				idProperty="idCliente" 
				onDataLoadCallBack="cliente"
				onPopupSetValue="popUpSetValueCliente"
				onchange="return onChangeCliente();"
				maxLength="20" 
				property="cliente" 
				service="lms.contasreceber.emitirExtratoClienteAction.findLookupCliente" 
				size="20" >
				
				<adsm:propertyMapping relatedProperty="clientes_pessoa.nmPessoa" 
			        			  	  modelProperty="pessoa.nmPessoa"/>
			        			  	  
				<adsm:textbox property="pessoa.nmPessoa" size="58" dataType="text" disabled="true" 
							  serializable="false"/>
			
				
					 
			</adsm:lookup>
			
		</adsm:listbox>	
		
		<adsm:textbox label="filialCobrancaCliente" 
					  property="filialByIdFilialCliente" 
					  dataType="text" 
					  labelWidth="20%" 
					  width="30%" 
					  size="50" 
					  disabled="true"/>

		<adsm:textbox label="tipoCliente" 
					  property="tpClienteEscolhaMultipla" 
					  dataType="text" 
					  size="15" 
					  labelWidth="20%" 
					  width="30%" 
					  disabled="true"/>
					  
		<adsm:hidden property="descricaoDivisaoCliente"/>	
		<adsm:combobox property="divisaoCliente.idDivisaoCliente" label="divisao" 
					   autoLoad="false"
					   disabled="true"
        			   service="lms.contasreceber.emitirExtratoClienteAction.findComboDivisaoCliente" 
					   optionLabelProperty="dsDivisaoCliente"				
					   labelWidth="20%"
					   boxWidth="150"
					   width="30%"
					   optionProperty="idDivisaoCliente">
					   
					  <adsm:propertyMapping 
							relatedProperty="descricaoDivisaoCliente" 
							modelProperty="dsDivisaoCliente"/>
							
					  <adsm:propertyMapping 
					  		criteriaProperty="cliente.idCliente"
							modelProperty="cliente.idCliente"/>
		</adsm:combobox>	   
		
		<%-- ###################    FILTROS COMPLEMENTARES CLIENTE    ################# --%>
				
        <adsm:section caption="filtrosComplementaresCliente"/>

		<adsm:textbox 
					property="identificacaoParcial" 
					maxLength="8"
					minLength="8"
					onchange="validateCNPJ(this)"
					dataType="integer" 
					label="identificacaoParcial"
					labelWidth="20%"
					width="30%"/>
					
		<adsm:textbox label="tipoCliente" 
					  property="tpClienteComplementar" 
					  dataType="text" 
					  labelWidth="20%" 
					  width="30%" 
					  size="30" 
					  disabled="true"/>
		
		<adsm:textbox label="nome" 
					  property="nmCliente" 
					  dataType="text" 
					  labelWidth="20%" 
					  width="30%" 
					  size="50" 
					  disabled="true"/>

		<adsm:textbox label="apelido" 
					  property="apelido" 
					  dataType="text" 
					  size="15" 
					  labelWidth="20%" 
					  width="30%" 
					  disabled="true"/>

		<adsm:hidden property="dsGrupoEconomico"/>
        <adsm:combobox property="grupoEconomico.idGrupoEconomico" 
					   onlyActiveValues="false" 
					   optionLabelProperty="dsGrupoEconomico" 
					   optionProperty="idGrupoEconomico" 
					   service="lms.contasreceber.emitirExtratoClienteAction.findComboGrupoEconomico" 
					   label="grupoEconomico" 
					   labelWidth="20%" 
					   width="30%" 
					   boxWidth="260">

					   <adsm:propertyMapping relatedProperty="dsGrupoEconomico" 
								  modelProperty="dsGrupoEconomico" />	
								  
		</adsm:combobox>
		
		<adsm:combobox property="tpClienteFiltroComp" 
			   label="tipoCliente" 
			   labelWidth="20%"
			   width="30%" 
			   domain="DM_TIPO_CLIENTE_EXTRATO"
			   defaultValue="RF"
			   required="true"/>
		
		
		<%-- ###################    QUE TENHAM COMO CLIENTE    ################# --%>
		
		
		
				
		<adsm:section caption="queTenhaComoCliente"/>

		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" 
			exactMatch="true" 
			idProperty="idCliente" 
			label="cliente" 
			maxLength="20" 
			property="cliente" 
			onchange="return clienteQuetenhaComoClienteOnChange();"
			service="lms.contasreceber.emitirExtratoClienteAction.findLookupCliente" 
			size="20" 
			labelWidth="20%" 
			width="80%">
			
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" 
			        			  modelProperty="pessoa.nmPessoa"/>
			
			<adsm:textbox dataType="text" 
						  disabled="true" 
						  property="cliente.pessoa.nmPessoa" 
						  serializable="true"
						  size="58"/>
		</adsm:lookup>
		
		
		<adsm:combobox property="tpClienteWithCliente" 
					   label="tipoCliente" 
					   labelWidth="20%"
					   onchange="return tpClienteQueTenhamComoClienteOnChange(this);"
					   width="30%" 
					   domain="DM_TIPO_CLIENTE_EXTRATO"/>	

		<adsm:combobox property="tpFormatoRelatorio" 
					   label="formatoRelatorio" 
					   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"
					   labelWidth="20%" width="30%"
					   required="true" />

		<adsm:buttonBar>
			<adsm:button buttonType="reportViewerButton" onclick="validateSubmit();" caption="visualizar" disabled="false"/>
			<adsm:button caption="limpar" onclick="limpar(this);" buttonType="resetButton"/>
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>

<script>

	/**
	 * Função responsável por validar o número parcial do CNPJ
	 */
	function validateCNPJ(cnpj){

		if(cnpj.value != ""){

			// executa somente se possuir 8 caracteres
			if(cnpj.value.length == 8){
				_serviceDataObjects = new Array(); 
				var data = buildFormBeanFromForm(document.forms[0]);
				
				addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirExtratoClienteAction.findNrCNPJParcialEqualNrIdentificacaoPessoa", 
				"validateCNPJ", { nrCNPJParcial:cnpj.value })); 
				xmit(false);
			}
			
		}else{
			clearFieldsIdentificacaoParcial();
		}

	}

	/**
	 * Função callBack da função validateCNPJ()
	 */
	function validateCNPJ_cb(data, errors){
		if(errors != undefined && errors != ""){
			setElementValue("identificacaoParcial", "");
			clearFieldsIdentificacaoParcial();
			setFocus("identificacaoParcial");
			alert( errors );
		}else{
			populateFormWithDataOfCallBack(data);
		}
	}
	
	/**
	  * Função responsável por limpar os campos relacionados ao nrIdentificacaoParcial
	  */
	function clearFieldsIdentificacaoParcial(){
		setElementValue("tpClienteComplementar", "");
		setElementValue("nmCliente", "");
		setElementValue("apelido", "");
	}
	
	/**
	 * Função de validação do submit
	 */
	function validateSubmit(){
	
		var clientes = getElement("clientes").length > 0;
		var idParcial = getElementValue("identificacaoParcial") != '';
		var gpEconomico = getElement("grupoEconomico.idGrupoEconomico").selectedIndex > 0;
	
		// Caso seja preenchida apenas uma informação, gera o relatório
		if ( clientes && !idParcial && !gpEconomico 
				|| (!clientes && (idParcial ^ gpEconomico) ) ){
			
			validatePeriodo();
		
		// Caso mais de uma informação for preenchida, exibe o alerta.
		}else{
			
			/** Valida primeiramente os campos obrigatórios, depois a regra da tela */
			if(validateForm(document.forms[0])){
				alert(i18NLabel.getLabel('LMS-36146'));		
			}
		
		}
		
	}
	 
	function validatePeriodo(){
		
		if(getElementValue("dtInicial") != "" && getElementValue("dtFinal")!= ""){
						
			var e = getElement("estadoCobranca");
			var estadoCobrancaSelecionado = e.options[e.selectedIndex].value;
			
			var dados = new Array();
			setNestedBeanPropertyValue(dados, "estadoCobrancaSelecionado", estadoCobrancaSelecionado);
			setNestedBeanPropertyValue(dados, "dtInicial", getElementValue("dtInicial"));
			setNestedBeanPropertyValue(dados, "dtFinal", getElementValue("dtFinal"));
			
			_serviceDataObjects = new Array();
		
			addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirExtratoClienteAction.validatePeriodo",
				"validatePeriodo", 
				dados));
		
	        xmit(false);
	        
		}
		
	}
	
	function validatePeriodo_cb(dados, error) {
		if ( error != undefined ) {
			alert(error);
			setFocus("dtInicial");
			return false;
		}
		
		reportButtonScript('lms.contasreceber.emitirExtratoClienteAction', 'openPdf', document.forms[0]);
		return true;
	}
	
	/**
	  * Função chamada no callBack da lookup de cliente da listBox de clientes 
	  */
	function cliente_cb(data, errors){
		
		getElement("clientes").selectedIndex = -1;
		
		if(errors != undefined){
			alert(errors);
		}
		
		var retorno = clientes_cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
		
		if(retorno){
			findDadosClienteByExtrato(data[0].idCliente, "2");
		}else{
			setElementValue("filialByIdFilialCliente", "");
			setElementValue("tpClienteEscolhaMultipla", "");
			getElement("divisaoCliente.idDivisaoCliente").options.length = 1;
			getElement("divisaoCliente.idDivisaoCliente").options[0].selected = true;
			setDisabled("divisaoCliente.idDivisaoCliente", true);
		}
		
		return retorno;
	}
	
	/**
	  * Função popUpSetValue da lookup de cliente 
	  */
	function popUpSetValueCliente(data, error){
		getElement("clientes").selectedIndex = -1;
		findDadosClienteByExtrato(data.idCliente, "2");
	}
	
	/** 
	  * Busca os dados complementares do cliente após a busca da lookup 
	  * metodo = 1 -> onChnge da listBox
	  * metodo = 2 -> callBack e popUpSetValue da lokup de cliente
	  */
	function findDadosClienteByExtrato(idCliente, metodo){
		var sdo = createServiceDataObject("lms.contasreceber.emitirExtratoClienteAction.findDadosClienteByExtrato",
			"findDadosClienteByExtrato", {idCliente:idCliente});
		xmit({serviceDataObjects:[sdo]});
		
		/** Caso tenha um único registro busca as divisões do cliente */
		if( (metodo == "1" && getElement("clientes").length == 1) ||  (metodo == "2" && getElement("clientes").length == 0)){
			findComboDivisaoCliente(idCliente);
		}
	}
	
	/**
	  * Função chamada no callBack da função findDadosClienteByExtrato 
	  */
	function findDadosClienteByExtrato_cb(data, errors){
		populateFormWithDataOfCallBack(data);
	}
	
	/**
	  * Reseta os campos tpCliente e filialByIdFilialCliente quando adicionado um cliente a listBox 
	  */
	function myOnContentChange(event){
		
		/** Caso tenha mais de um cliente na escolha multipla e seja o evento salvar cliente ( imagem de um sinal de adição (+) ) ou seja o evento novo cliente */
		if( (event.name == "modifyButton_click" || event.name == "cleanButton_click") && getElement("clientes").length > 1 ){
			setElementValue("filialByIdFilialCliente", "");
			setElementValue("tpClienteEscolhaMultipla", "");
			getElement("divisaoCliente.idDivisaoCliente").options.length = 1;
			getElement("divisaoCliente.idDivisaoCliente").options[0].selected = true;
			setDisabled("divisaoCliente.idDivisaoCliente", true);
		}
		
		/** Após salvar o registro na listBox, popula a divisão */
		if(event.name == "modifyButton_afterClick" || event.name == "deleteButton_afterClick"){
			
			/** Concatena o nrIdentificacao com o nmPessoa para ser exibido na listBox */
			//event.src.text = event.src.text + " - " + getElementValue("clientes_pessoa.nmPessoa");
			
			/** Caso tenha um único registro */
			if(getElement("clientes").length == 1){
				if (event.name == "deleteButton_afterClick"){
					//getElement("clientes").selectedIndex = 0;
					 (getElement("clientes"));
				}
				
				/** Busca as divisões do cliente */
				var sdo = createServiceDataObject("lms.contasreceber.emitirExtratoClienteAction.findComboDivisaoCliente",
					"findComboDivisaoCliente", {idCliente:getElement("clientes")[0].data.cliente.idCliente});
				xmit({serviceDataObjects:[sdo]});
				
			/** Caso tenha mais de um registro na listBox desabilita a combo de divisão */
			}else{
				setElementValue("filialByIdFilialCliente", "");
				setElementValue("tpClienteEscolhaMultipla", "");
				setDisabled("divisaoCliente.idDivisaoCliente", true);
				getElement("divisaoCliente.idDivisaoCliente").options[0].selected = true;
				setElementValue("descricaoDivisaoCliente", "");				
			}
			
			
		}
		
		return true;
	}
	
	/** 
	  * Onchange da listBox, é chamado no click de um option do select 
	  */
	function myOnChange(element){
		idCliente = element.options[element.selectedIndex].data.cliente.idCliente;
		findDadosClienteByExtrato(idCliente, "1");
	}
	
	/** 
	  * Popula o form com os dados de retorno 
	  */
	function populateFormWithDataOfCallBack(data){
		fillFormWithFormBeanData(0, data);
	}
	
	/** 
	  * Busca a moeda do usuário que está na sessão 
	  */
	function findMoedaUsuario(){
	
		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirExtratoClienteAction.findMoedaSessao", "findMoedaSessao", new Array())); 
		
	}
	
	/** 
	  * CallBack da função findMoedaUsuario 
	  */
	function findMoedaSessao_cb(data, error){
	
		if(error != undefined){
			alert(error);
		}
		
		setElementValue("moeda.idMoeda", data.idMoeda);
		
		document.getElementById("moeda.idMoeda").onchange();
	}
	
	/** 
	  * CallBack da Page 
	  */
	function myPageLoadCallBack_cb(data, error){
		onPageLoad_cb(data, error);
	}
	
	/** 
	  * Função invocada no início da window
	  */
	
	function initWindow(event){
		initPage();
	}
	
	/**
	  * Função invocada no callBack da page 
	  */
	function initPage(){
		
		_serviceDataObjects = new Array();
		
		findMoedaUsuario();
		findPeriodoByMonth();
		
		getElement("divisaoCliente.idDivisaoCliente").options.length = 1;
		getElement("divisaoCliente.idDivisaoCliente").options[0].selected = true;
		setDisabled("divisaoCliente.idDivisaoCliente", true);
		
		xmit(false);
	}
	
	/** 
	  * CallBack da função findComboDivisaoCliente 
	  */
	function findComboDivisaoCliente_cb(data, error){
		
		if(error != undefined){
			alert(error);
		}
		
		if (data != undefined && data.length <= 0){
			setDisabled("divisaoCliente.idDivisaoCliente", true);
		} else {
			setDisabled("divisaoCliente.idDivisaoCliente", false);
		}
		divisaoCliente_idDivisaoCliente_cb(data);
	}
	
	/**
	  * Busca o periodo inicial e final
	  */
	function findPeriodoByMonth(){
		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirExtratoClienteAction.findPeriodoByMonth", "findPeriodoByMonth", new Array())); 
	}
	
	/**
	  * CallBack da função findPeriodoByMonth
	  */
	function findPeriodoByMonth_cb(data, error){
		
		if(error != undefined){
			alert("error");
		}
		
		fillFormWithFormBeanData(0, data);
	}
	
	/** Busca as divisões do cliente */
	function findComboDivisaoCliente(idCliente){
	
		var data = new Array();	   
		
		setNestedBeanPropertyValue(data, "idCliente", idCliente);
		
		var sdo = createServiceDataObject("lms.contasreceber.emitirExtratoClienteAction.findComboDivisaoCliente","findComboDivisaoCliente", data);
		xmit({serviceDataObjects:[sdo]});

	}
	
	function onChangeCliente(){
		var cliente = document.getElementById("clientes_cliente.pessoa.nrIdentificacao");
		if (getElementValue("cliente.pessoa.nrIdentificacao") == ""){
			getElement("divisaoCliente.idDivisaoCliente").options.length = 1;
			getElement("divisaoCliente.idDivisaoCliente").options[0].selected = true;
			setDisabled("divisaoCliente.idDivisaoCliente", true);
			
			getElement("filialByIdFilialCliente").value = "";
			getElement("tpClienteEscolhaMultipla").value = "";
		}
		
		return clientes_cliente_pessoa_nrIdentificacaoOnChangeHandler();
	}

	function trataRequiredQueTenhaComoCliente(){
		var preenchidoId 	= false;
		var preenchidoTipo 	= false;

		if (getElementValue("cliente.pessoa.nrIdentificacao") != "") preenchidoId = true;
		if (getElement("tpClienteWithCliente").selectedIndex != 0) preenchidoTipo = true;

		/* inverte aqui, pois se um está preenchido, o outro é obrigatório */
		getElement("cliente.pessoa.nrIdentificacao").required = preenchidoTipo;
		getElement("tpClienteWithCliente").required = preenchidoId;
	}

	function clienteQuetenhaComoClienteOnChange(){
		trataRequiredQueTenhaComoCliente();
		return cliente_pessoa_nrIdentificacaoOnChangeHandler();
	}
	
	function tpClienteQueTenhamComoClienteOnChange(arg){
		trataRequiredQueTenhaComoCliente();	
		return comboboxChange({e:arg});
	}
	
	function limpar(arg){
		getElement("tpClienteWithCliente").required = "false";
		getElement("cliente.pessoa.nrIdentificacao").required = "false";
		cleanButtonScript(arg.document);
	}
</script>
