<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.processarFaturamentoManualAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/processarFaturamentoManual">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-36208"/>
			<adsm:include key="LMS-36125"/>
		</adsm:i18nLabels>

		<adsm:section caption="filtrosSelecaoDocumentosServico"/>
        <adsm:lookup label="filialCobranca" property="filial" 
        	service="lms.municipios.filialService.findLookup" 
        	action="/municipios/manterFiliais" idProperty="idFilial" 
        	criteriaProperty="sgFilial" dataType="text" size="3" 
        	labelWidth="20%"
        	maxLength="3" width="80%">
            <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia"
	            modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" 
				size="30" disabled="true"/>
        </adsm:lookup>		
		
		<adsm:hidden property="cliente.tpSituacao" value="A"/>
		
		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" 
			exactMatch="true" 
			idProperty="idCliente" 
			label="clienteResponsavel" 
			maxLength="20" 
			property="cliente" 
			service="lms.contasreceber.processarFaturamentoManualAction.findLookupCliente" 
			size="20" 
			width="80%"
			onDataLoadCallBack="lookupClienteCallBack"
			onPopupSetValue="lookupClientePopUpSetValue"
        	labelWidth="20%"			
			required="true">
			
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
				
			<adsm:propertyMapping 
				criteriaProperty="cliente.tpSituacao" 
				modelProperty="tpSituacao"/>				
			
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				serializable="false"
				size="58"/>
				
		</adsm:lookup>		

        <adsm:combobox property="divisaoCliente.idDivisaoCliente" label="divisao" 
        		service="lms.contasreceber.processarFaturamentoManualAction.findComboDivisaoCliente" 
				optionLabelProperty="dsDivisaoCliente"				
				boxWidth="150"
	        	labelWidth="20%"				
	        	width="30%"
	        	onchange="validateDivisao(); comboboxChange({e:this});"
	        	onDataLoadCallBack="comboDivisaoCliente"
				optionProperty="idDivisaoCliente">
				<adsm:propertyMapping 
					criteriaProperty="cliente.idCliente" 
					modelProperty="idCliente"/>	
		</adsm:combobox>        
        
        <adsm:combobox property="tpFrete" label="tipoFrete" domain="DM_TIPO_FRETE"/>

		<adsm:combobox 
        	labelWidth="20%"				
        	width="30%"		
			property="tpModal" label="modal" domain="DM_MODAL"
			onchange="getDataVencimento();"
			required="false"/>
		
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA"
			required="false" onchange="onChangeTpAbrangencia(); getDataVencimento();"/>	        
        
        <adsm:hidden property="formaAgrupamento.blAutomatico" serializable="true" value="N"/>
        <adsm:hidden property="formaAgrupamento.tpSituacao" serializable="true" value="A"/>
        

				
		<adsm:range labelWidth="20%" label="emissao" required="false">	
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal"/>
		</adsm:range>
		
		<adsm:section caption="dadosFatura"/>

		<adsm:textbox labelWidth="20%"				
	        	width="30%"
	        	label="emissao" dataType="JTDate" property="dtEmissao" required="true"
  	        	onchange="getDataVencimento();"/>

		<adsm:textbox labelWidth="15%"				
	        	width="35%"
	        	label="vencimento" dataType="JTDate" property="dtVencimento"
	        	onchange="return validateDtVencimento();"/>

        <adsm:checkbox property="blGerarBoleto" label="gerarBoleto" width="30%" labelWidth="20%"/>
		
		<adsm:combobox 
			service="lms.contasreceber.processarFaturamentoManualAction.findComboCedentesActive" 
			optionLabelProperty="comboText" 
			optionProperty="idCedente" 
			property="cedente.idCedente" 
			onlyActiveValues="true"
			label="banco"
			boxWidth="150"> 
		</adsm:combobox>
		
        <adsm:combobox property="agrupamentoCliente.idAgrupamentoCliente" 
				label="formaAgrupamento" 
	        	labelWidth="20%"				
	        	width="30%"				
				boxWidth="150"
				service="lms.contasreceber.processarFaturamentoManualAction.findAgrupamentoCliente" 
				optionLabelProperty="formaAgrupamento.dsFormaAgrupamento" 
				optionProperty="idAgrupamentoCliente">
			<adsm:propertyMapping 
				criteriaProperty="divisaoCliente.idDivisaoCliente" 
				modelProperty="divisaoCliente.idDivisaoCliente" 
			/>				
			<adsm:propertyMapping 
				criteriaProperty="formaAgrupamento.blAutomatico" 
				modelProperty="formaAgrupamento.blAutomatico"
			/>				
			<adsm:propertyMapping 
				criteriaProperty="formaAgrupamento.tpSituacao" 
				modelProperty="formaAgrupamento.tpSituacao"
			/>				
		</adsm:combobox>

		<adsm:combobox property="tipoAgrupamento.idTipoAgrupamento" 
			label="tipoAgrupamento" service="lms.contasreceber.processarFaturamentoManualAction.findComboTipoAgrupamento" 
			optionLabelProperty="dsTipoAgrupamento" optionProperty="idTipoAgrupamento"
			boxWidth="150">
			<adsm:propertyMapping criteriaProperty="agrupamentoCliente.idAgrupamentoCliente" modelProperty="agrupamentoCliente.idAgrupamentoCliente"/>
		</adsm:combobox>
		
		
		<adsm:textbox label="cotacao" 
	        	labelWidth="20%"						
			width="8%"
			size="8"
			dataType="text"
			property="simboloMoedaPais" 
			serializable="true"
			disabled="true" />							
			
		<adsm:textbox property="dtCotacaoMoeda" 
			dataType="JTDate" 
			disabled="true" 
			picker="false"
			size="8"
			serializable="true" width="9%"/>
		
		<adsm:lookup property="cotacaoMoeda" idProperty="idCotacaoMoeda" 
			criteriaProperty="vlCotacaoMoeda" 
			action="/configuracoes/manterCotacoesMoedas" dataType="currency"
			onchange="vlCotacaoMoedaOnChange(this);" onPopupSetValue="cotacaoMoedaSetValue"
			service="lms.contasreceber.processarFaturamentoManualAction.findLookupCotacaoMoeda" 
			size="8"
			disabled="true"
			serializable="true"
			width="13%">
			<adsm:propertyMapping  criteriaProperty="idPaisCotacao" modelProperty="moedaPais.pais.idPais" />
			<adsm:propertyMapping  criteriaProperty="nmPaisCotacao" modelProperty="moedaPais.pais.nmPais" />			
			<adsm:propertyMapping  relatedProperty="dtCotacaoMoeda" modelProperty="dtCotacaoMoeda" />
			<adsm:propertyMapping  relatedProperty="vlCotacaoMoedaTmp" modelProperty="vlCotacaoMoeda" />
		</adsm:lookup>		
        
		<adsm:hidden property="vlCotacaoMoeda" serializable="true"/>
		<adsm:hidden property="vlCotacaoMoedaTmp" serializable="false"/>	

		<adsm:hidden property="idPaisCotacao" serializable="false"/>
		<adsm:hidden property="nmPaisCotacao" serializable="false"/>

		<adsm:buttonBar>
			<adsm:button caption="processar" onclick="return processarOnClick();" />
			<adsm:button caption="limpar" id="newButton" onclick="limpar()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

    var blProcessado = false;
    
    function limpar(){
        if (blProcessado){
            cleanButtonScript(this.document, true, {name:'newButton_click'});
        }else{
            newButtonScript(this.document, true, {name:'newButton_click'});
        }
        blProcessado = false;
    }

	function processarOnClick(){
	
		var aviso = undefined;
	
		if (document.getElementById("agrupamentoCliente.idAgrupamentoCliente").options.length > 1 &&
		    document.getElementById("agrupamentoCliente.idAgrupamentoCliente").selectedIndex == 0){
			aviso = i18NLabel.getLabel('LMS-36208');
		}
		
		if (document.getElementById("tipoAgrupamento.idTipoAgrupamento").options.length > 1){
			document.getElementById("tipoAgrupamento.idTipoAgrupamento").required = "true";
		} else {
			document.getElementById("tipoAgrupamento.idTipoAgrupamento").required = "false";
		}		
		
		var retorno = validateForm(document.forms[0]);
		var executaSemAgrupamento = true;
		
		if (retorno == true){
		
			if( aviso != undefined ){
				executaSemAgrupamento = window.confirm(aviso);
			}
			
			if( executaSemAgrupamento == true ) {
			
				var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false, showSuccessMessage:false};
				var dataCall = createServiceDataObject("lms.contasreceber.processarFaturamentoManualAction.executeFaturamento", "processarOnClick", buildFormBeanFromForm(document.forms[0]));
				remoteCall.serviceDataObjects.push(dataCall);
				xmit(remoteCall);
				
			} else {
				setFocus("agrupamentoCliente.idAgrupamentoCliente");
			}
		}
	}
	
	
	function processarOnClick_cb(d,e,c,x){
		//onDataLoad_cb(d,e,c,x);
		if (e == undefined){
			showSuccessMessage();
			document.getElementById("newButton").focus();
			document.getElementById("newButton").focus;
			blProcessado = true;
		} else {
			alert(e+'');
		}
		if(c =="LMS-36099"){
			document.getElementById("dtEmissao").focus();
			document.getElementById("dtEmissao").select();
	}
	}
	
	function processarEspecialOnClick(){
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:true, showSuccessMessage:true};
	    var dataCall = createServiceDataObject("lms.contasreceber.processarFaturamentoManualAction.executeFaturamentoEspecialAutomatico", "onDataLoad");
	    remoteCall.serviceDataObjects.push(dataCall);
		xmit(remoteCall);
	}	

	function processarNormalOnClick(){
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:true, showSuccessMessage:true};
	    var dataCall = createServiceDataObject("lms.contasreceber.processarFaturamentoManualAction.executeFaturamentoNormalAutomatico", "onDataLoad");
	    remoteCall.serviceDataObjects.push(dataCall);
		xmit(remoteCall);
	}	
	
	function initWindow(eventObj){
		if (eventObj.name == "newButton_click"){
			initPage();
		}
	}

	function myOnPageLoad_cb(d,e,o,x){
		onPageLoad_cb(d,e,o,x);
		initPage();
	}
	
	function initPage(){
		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject("lms.contasreceber.processarFaturamentoManualAction.findInitialValue", "initPage", null)); 
		xmit(false);
		document.getElementById("blGerarBoleto").checked = true;
	}
	
	/*
	 * Monta as duas constantes que tem a lista de situação de fatura	
	 */
	function initPage_cb(d,e,o,x){
		if (e == undefined) {	
			onDataLoad_cb(d,e,o,x);	

			if (d.blMatriz == "true") {
				setDisabled("filial.idFilial", false);
			} else {
				setDisabled("filial.idFilial", true);			
			}
			onChangeTpAbrangencia();
			getDataVencimento();
			getDataEmissaoFinal();
			setFocusOnFirstFocusableField();
		}
	}
	
	/*
	 * Ao clicar na listagem da popUp (lookup) atribui o valor do simboloMoedaPais
	 */
	function cotacaoMoedaSetValue(data){
		if (data == undefined) return;
		setElementValue( "simboloMoedaPais", getSimboloMoedaPais(data.moedaPais) );
	}	
	
	/*
	 * Concatena o simbolo da moeda com a sigla do país
	 */
	function getSimboloMoedaPais(moedaPais){
		if (moedaPais == undefined) return "";
		if (moedaPais.moeda != undefined && moedaPais.pais != undefined){
			return moedaPais.moeda.dsSimbolo + " - " + moedaPais.pais.sgPais;
		}
		return "";
	}
	
	/*
	 * Não faz a busca quando alterar o valor, deixando apenas no clique da picker.
	 */
	function vlCotacaoMoedaOnChange(eThis){
		if (getElementValue("vlCotacaoMoedaTmp") == getElementValue("cotacaoMoeda.vlCotacaoMoeda")){
			setElementValue("vlCotacaoMoeda","");
		} else {
			setElementValue("vlCotacaoMoeda",getElementValue("cotacaoMoeda.vlCotacaoMoeda"));
			format(document.getElementById("vlCotacaoMoeda"), event);
		}
		return true;
	}
	
	function onChangeTpAbrangencia(){
		if (getElementValue("tpAbrangencia") == "I"){
			setDisabled("cotacaoMoeda.idCotacaoMoeda",false);
			setDisabled("cotacaoMoeda.vlCotacaoMoeda",true);
			setDisabled("vlCotacaoMoeda",false);
			setDisabled("vlCotacaoMoedaTmp",false);		
		} else {
			resetValue("simboloMoedaPais");
			resetValue("dtCotacaoMoeda");
			resetValue("cotacaoMoeda.idCotacaoMoeda");
			resetValue("cotacaoMoeda.vlCotacaoMoeda");			
			resetValue("vlCotacaoMoeda");
			resetValue("vlCotacaoMoedaTmp");				
					
			setDisabled("cotacaoMoeda.idCotacaoMoeda",true);
			setDisabled("cotacaoMoeda.vlCotacaoMoeda",true);			
			setDisabled("vlCotacaoMoeda",true);
			setDisabled("vlCotacaoMoedaTmp",true);
		}		
	}


	function validateDtVencimento(){
		var retorno = true;

		getElement("dtVencimento").biggerThan = "dtEmissao";

		if (!compareRange(getElement("dtVencimento"))){
			alertI18nMessage("LMS-36125");
			retorno = false;
		}

		getElement("dtVencimento").biggerThan = undefined;

		return retorno;
	}
	
	
	function getDataVencimento(){
		_serviceDataObjects = new Array();	
		addServiceDataObject(createServiceDataObject("lms.contasreceber.processarFaturamentoManualAction.findDataVencimento", "getDataVencimento", buildFormBeanFromForm(this.document.forms[0])));
		xmit(false);		
	}
	
	function getDataVencimento_cb(d,e,c,x){
		if (e != undefined) {
			alert(e+'');
		} else {
			setElementValue("dtVencimento", setFormat("dtVencimento", d._value));
		}
	}		
	
	function getDataEmissaoFinal(){
	    _serviceDataObjects = new Array();  
        addServiceDataObject(createServiceDataObject("lms.contasreceber.processarFaturamentoManualAction.findDtEmissaoFinal", "getDataEmissaoFinal"));
        xmit(false);
	}
	
	function getDataEmissaoFinal_cb(data,error){
	   if (error != undefined){
	       alert(error);
	   }else{
	       setElementValue("dtEmissaoFinal", setFormat("dtEmissaoFinal", data._value));
	   }
	}
	
	/** Callback da lookupd e cliente */
	function lookupClienteCallBack_cb(data, error){
		
		var retorno = true;
		
		if(error != undefined){
			alert(error);
			retorno = false;
		}			
		
		cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
		
		return retorno;
	}
	
	/** PopUpSetValue da lookup de cliente */
	function lookupClientePopUpSetValue(data, error){
		
		setElementValue("cliente.idCliente", data.idCliente);
		setElementValue("cliente.pessoa.nrIdentificacao", data.pessoa.nrIdentificacaoFormatado);
		setElementValue("cliente.pessoa.nmPessoa", data.pessoa.nmPessoa);
		
		return true;
	}
	
	/** CallBack da combo de divisao de cliente */ 
	function comboDivisaoCliente_cb(data, error){

		// Caso encontre divisão, limpa a data de vencimento
		if(data.length != 0){
			setElementValue("dtVencimento", ""); 
		// Caso não encontre,  seta a data de vencimento
		}else{
			getDataVencimento();
		}
		
		divisaoCliente_idDivisaoCliente_cb(data); 
	}
	
	
	/** OnChange da combo de divisão */
	function validateDivisao(){
		
		// Caso não esteja na opção selecione, busca a data de vencimento
		if(getElement("divisaoCliente.idDivisaoCliente").selectedIndex != 0)
			getDataVencimento();
		// Caso esteja na opção selecione, limpa a data de vencimento
		else
			setElementValue("dtVencimento", ""); 
	}
	
</script>
