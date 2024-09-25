<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myOnPageLoad">
	
	<adsm:form action="/contasReceber/carregarArquivoRecebido">
		<adsm:hidden property="tpSituacao" value="A"/>
		<adsm:lookup label="cliente"
					 service="lms.contasreceber.carregarArquivoRecebidoAction.findLookupCliente" 
					 dataType="text"
					 property="cliente" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
		 			 onPopupSetValue="popupCliente"
					 exactMatch="true" 
					 size="20"
					 maxLength="20" 
					 width="100%" 
					 action="/vendas/manterDadosIdentificacao"
					 required="true"
					 onchange="return onChangeCliente()">
				<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="cliente.pessoa.nmPessoa" />
				<adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" formProperty="nrIdentificacao" />
				<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />				
				<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" disabled="true" size="61" />	
		</adsm:lookup>
		<adsm:hidden property="nrIdentificacao" serializable="true"/>
	
        <adsm:combobox property="divisaoCliente.idDivisaoCliente" label="divisao" 
        		service="lms.contasreceber.carregarArquivoRecebidoAction.findComboDivisaoCliente" 
				optionLabelProperty="dsDivisaoCliente"				
				width="35%"
				boxWidth="240"
				required="true"
				optionProperty="idDivisaoCliente" 
				autoLoad="false"
				onchange="return onChangeDivisao(this)">
			<adsm:propertyMapping 
				criteriaProperty="cliente.idCliente" 
				modelProperty="cliente.idCliente"
			/>				
		</adsm:combobox>
		
		<adsm:combobox 
			service="lms.contasreceber.carregarArquivoRecebidoAction.findComboCedentes" 
			optionLabelProperty="comboText" 
			optionProperty="idCedente" 
			property="cedente.idCedente" 
			label="banco"
			labelWidth="18%"
			width="22%"
			boxWidth="150"
			required="true"> 
		</adsm:combobox>    
    
		<adsm:hidden property="dataAtual"/>
	
        <adsm:textbox width="35%" dataType="JTDate" label="dataEmissao" property="dtEmissao" size="8" maxLength="20" required="true" onchange="calculaVencimento();"/>
	
		<adsm:textbox labelWidth="18%" width="22%" dataType="JTDate" label="dataVencimento" property="dtVencimento" size="8" maxLength="20" required="true"/>

        <adsm:checkbox property="gerarBoleto" label="gerarBoleto"/>
	
		<adsm:textbox label="arquivo" property="arquivo" dataType="file" width="85%" size="72" required="true" serializable="true" />

		<adsm:buttonBar>
		
			<adsm:button caption="relatorioOcorrencias" id="btnOcorrencia" action="/contasReceber/emitirOcorrenciasPreFaturasImportadas.do" cmd="main" disabled="false">

				<adsm:linkProperty src="cliente.idCliente" target="cliente.idCliente"/>
				<adsm:linkProperty src="cliente.pessoa.nrIdentificacao" target="cliente.pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="cliente.pessoa.nmPessoa" target="cliente.pessoa.nmPessoa"/>
				<adsm:linkProperty src="dataAtual" target="periodoInicial"/>
				<adsm:linkProperty src="dataAtual" target="periodoFinal"/>
			</adsm:button>		
		
			<adsm:button service="lms.contasreceber.carregarArquivoRecebidoAction.executeImportacao" 
					callbackProperty="retornoImportacao"
					buttonType="storeButton"
					caption="importarArquivo"
					id="btnImportar"
					disabled="false"
			/>
		
			<adsm:button caption="limpar" onclick="limpar()" disabled="false" id="btnLimpar"/>
			
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>

<script>
	//preservando o valor quando limpar
	document.getElementById('dataAtual').masterLink = 'true';

	/** Executado ao entrar na tela  */
	function initWindow(){
		setDisabled('btnImportar', false);
		setDisabled('btnOcorrencia', false);
		setDisabled('btnLimpar', false);
		
	}

	/** Limpa os campos da tela */ 
	function limpar(){
		cleanButtonScript();
		setElementValue("dtEmissao", setFormat(getElement("dtEmissao"), getElementValue("dataAtual")));
		setElementValue('gerarBoleto',true);
		calculaVencimento();
	}



	function calculaVencimento() {
		var dtEmissao = getElement("dtEmissao");
		
		if ( dtEmissao.value != "" && isDate(dtEmissao.value, dtEmissao.mask) && 
		     getElementValue("divisaoCliente.idDivisaoCliente") != '') {
		     
			_serviceDataObjects = new Array();
			
			addServiceDataObject(
				createServiceDataObject("lms.contasreceber.carregarArquivoRecebidoAction.findDataVencimento",
					"retornoCalculaVencimento", 
					{
					  idDivisaoCliente:getElementValue("divisaoCliente.idDivisaoCliente"),
					  dtEmissao:getElementValue("dtEmissao")
					}
				)
			);
	
	        xmit(false);
		} else {
			resetValue('dtVencimento');
		}
	}

	function retornoCalculaVencimento_cb(data, error) {
		setElementValue("dtVencimento", setFormat(getElement("dtVencimento"), data._value));
	}

	function retornoImportacao_cb(data, error,c) {

		if (error != null || (data.LMS_36112 != undefined && data.LMS_36112 != '' )) { 
		
			if( error != null ){		
				alert(error);
			} else if(data.LMS_36112 != undefined && data.LMS_36112 != '' ){
				alert(data.LMS_36112);
			}			
			
			if(c == "LMS-36099"){
				var dtEmissao = getElement("dtEmissao");
				dtEmissao.focus();
				dtEmissao.select();
			}else{
			setFocusOnFirstFocusableField(document);
			}
			
		} else {
			showSuccessMessage();			
			openReportWithLocator(data.reportLocator, error);
		}
	}

	function myOnPageLoad_cb(data, erro){
		onPageLoad_cb(data,erro);

		resetFileElementValue(document.getElementById('arquivo'));
		setElementValue('gerarBoleto',true);

		_serviceDataObjects = new Array();	
		addServiceDataObject(createServiceDataObject("lms.contasreceber.carregarArquivoRecebidoAction.findDataAtual", "setDataAtual", new Array()));
		xmit(false);
	}	
	
	function setDataAtual_cb(data, error) {
		setElementValue('dataAtual', setFormat(getElement("dataAtual"), data._value));
		setElementValue('dtEmissao', setFormat(getElement("dtEmissao"), data._value));
		calculaVencimento();		
	}
	
	document.getElementById("cliente.idCliente").callBack = "clienteExactMatch";
	
	function popupCliente(data){
		if (data == undefined) {
			return;
		}
		 
		_serviceDataObjects = new Array();	
		
		mountDivisaoCliente(data.idCliente);
		findCedenteByCliente(data.idCliente);
	
		xmit(false);	
	}
	
	function mountDivisaoCliente(varIdCliente){	
		addServiceDataObject(createServiceDataObject("lms.contasreceber.carregarArquivoRecebidoAction.findComboDivisaoCliente", "myRetornoDivisao", {cliente:{idCliente:varIdCliente}}));
	}	
	
	function myRetornoDivisao_cb(data,error){
		divisaoCliente_idDivisaoCliente_cb(data);
		if( data.length == 1 ){
			var divisao = getElement('divisaoCliente.idDivisaoCliente');
			divisao.selectedIndex = 1;
			calculaVencimento();
		}
		verificaDivisaoClientePossuiAgrupamento();
	}
	
	function clienteExactMatch_cb(data) {
		var retorno = lookupExactMatch({e:document.getElementById("cliente.idCliente"), data:data});
		if (data != undefined && data.length > 0) {
			if (data.length == 1) {		
				_serviceDataObjects = new Array();	
		
				mountDivisaoCliente(data[0].idCliente);
				findCedenteByCliente(data[0].idCliente);
			
				xmit(false);			
			}
		}
		return retorno;
	}
	
	function findCedenteByCliente(idCliente){

		addServiceDataObject(createServiceDataObject("lms.contasreceber.carregarArquivoRecebidoAction.findCedenteByCliente", 
		                                             "retornoCedenteByCliente", 
		                                             {cliente:{idCliente:idCliente}}));
	}
	
	function retornoCedenteByCliente_cb(data,error){
	
		if( error != undefined ){
			alert(error);
			setFocusOnFirstFocusableField(document);
			return false;		
		}
	
		if( data != undefined ){
			fillFormWithFormBeanData(0, data);		
			setFocusOnFirstFocusableField(document);
		}	
	}
	
	function onChangeDivisao(elem){
		var retorno = comboboxChange({e:elem});
		resetValue('dtVencimento');
		calculaVencimento();
		verificaDivisaoClientePossuiAgrupamento();
		return retorno;
	}
	
	function verificaDivisaoClientePossuiAgrupamento(){			
		_serviceDataObjects = new Array();	
		addServiceDataObject(createServiceDataObject("lms.contasreceber.carregarArquivoRecebidoAction.verifDivisaoClientePossuiAgrup", 
						"verifDivisaoClientePossuiAgrup",
						{ idDivisaoCliente:getElementValue("divisaoCliente.idDivisaoCliente")}));
		xmit(false);						
	}

	function verifDivisaoClientePossuiAgrup_cb(data) {		
		if (data._value == "true") {
			setDisabled("gerarBoleto", true);
			setElementValue("gerarBoleto", false);
		} else {
			setDisabled("gerarBoleto", false);
		}
	}

	function onChangeCliente(){
		var retorno = cliente_pessoa_nrIdentificacaoOnChangeHandler();			
		
		var cliente = getElement("cliente.pessoa.nrIdentificacao");
		
		if (getElementValue("cliente.pessoa.nrIdentificacao") == "" || isElementChanged(cliente) ){
			document.getElementById("divisaoCliente.idDivisaoCliente").options.length = 1;
			document.getElementById("divisaoCliente.idDivisaoCliente").selectedIndex = 0;
			resetValue('dtVencimento');
		}
		verificaDivisaoClientePossuiAgrupamento();
		
		return retorno;	
	}
		
</script>