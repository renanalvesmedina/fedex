<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contratacaoveiculos.manterBeneficiariosFreteCarreteiroAction"  onPageLoadCallBack="beneficiario_pageLoad" onPageLoad="pageLoadBeneficiario">

	<adsm:form action="/contratacaoVeiculos/manterBeneficiariosFreteCarreteiro" idProperty="idBeneficiarioProprietario" 
				service="lms.contratacaoveiculos.manterBeneficiariosFreteCarreteiroAction.findById" onDataLoadCallBack="beneficiarioDataLoad">
	
		<adsm:hidden property="beneficiario.idBeneficiario" />
		<adsm:hidden property="proprietario.idProprietario" />
		<adsm:hidden property="labelPessoa"/>
			
		<adsm:complement width="77%" labelWidth="17%" required="true" label="proprietario">
	        <adsm:textbox label="proprietario" dataType="text" property="proprietario.pessoa.nrIdentificacao" size="20" disabled="true" width="20%"/>
    		<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="50" width="40%" disabled="true" />
        </adsm:complement>

								
		<adsm:combobox definition="TIPO_PESSOA.cad" property="beneficiario.pessoa.tpPessoa" labelWidth="17%" width="33%" label="tipoPessoa" onchange="return changeTypePessoaWidget({tpTipoElement:this, tpIdentificacaoElement:document.getElementById('beneficiario.pessoa.tpIdentificacao'), numberElement:document.getElementById('beneficiario.pessoa.idPessoa'), tabCmd:'cad'});"/>		
		
	    <adsm:complement label="identificacao" labelWidth="17%" width="33%">
		            <adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" property="beneficiario.pessoa.tpIdentificacao" 
		            onchange="return changeIdentificationTypePessoaWidget({tpIdentificacaoElement:this, numberElement:document.getElementById('beneficiario.pessoa.idPessoa'), tabCmd:'cad'});"/>		
		            <adsm:lookup definition="IDENTIFICACAO_PESSOA" property="beneficiario.pessoa" onDataLoadCallBack="onDataLoadIdentificacao">
		            	<adsm:propertyMapping criteriaProperty="beneficiario.pessoa.tpPessoa" modelProperty="tpPessoa"/>
		            	<adsm:propertyMapping criteriaProperty="beneficiario.pessoa.tpIdentificacao" modelProperty="tpIdentificacao"/>
		            	<adsm:propertyMapping relatedProperty="beneficiario.pessoa.nmPessoa" modelProperty="nmPessoa"/>
		            	<adsm:propertyMapping relatedProperty="beneficiario.pessoa.dsEmail" modelProperty="dsEmail"/>
		            </adsm:lookup>		
		            
		</adsm:complement>	
        
		<adsm:textbox dataType="text" labelWidth="17%" width="33%" label="nome" property="beneficiario.pessoa.nmPessoa" required="true" size="40" maxLength="50"/>

		<adsm:textbox dataType="email" labelWidth="17%" width="33%" property="beneficiario.pessoa.dsEmail" label="email" maxLength="60"  size="40"/>

		<adsm:range label="vigencia" labelWidth="17%" width="33%">
			<adsm:textbox dataType="JTDate" required="true" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
<script>
	function LMS_00049(){
		alert(parent.i18NLabel.getLabel("LMS-00049"));
	}
		
	var labelPessoaTemp = parent.i18NLabel.getLabel("beneficiario");
</script>
		<adsm:buttonBar>
			<adsm:button caption="enderecos" action="configuracoes/manterEnderecoPessoa.do" cmd="main" >
				<adsm:linkProperty src="beneficiario.idBeneficiario" target="pessoa.idPessoa"/>
				<adsm:linkProperty src="beneficiario.pessoa.tpIdentificacao" target="pessoa.tpIdentificacao"/>
				<adsm:linkProperty src="beneficiario.pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="beneficiario.pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="contatos" action="configuracoes/manterContatos.do" cmd="main" >
				<adsm:linkProperty src="beneficiario.idBeneficiario" target="pessoa.idPessoa"/>
				<adsm:linkProperty src="beneficiario.pessoa.tpIdentificacao" target="pessoa.tpIdentificacao"/>
				<adsm:linkProperty src="beneficiario.pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="beneficiario.pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="dadosBancarios" action="configuracoes/manterDadosBancariosPessoa.do" cmd="main" >			
				<adsm:linkProperty src="beneficiario.idBeneficiario" target="pessoa.idPessoa"/>
				<adsm:linkProperty src="beneficiario.pessoa.tpIdentificacao" target="pessoa.tpIdentificacao"/>
				<adsm:linkProperty src="beneficiario.pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="beneficiario.pessoa.nmPessoa" target="pessoa.nmPessoa"/>	
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp" />			
			</adsm:button>
		<adsm:storeButton callbackProperty="storeBeneficiario" service="lms.contratacaoveiculos.manterBeneficiariosFreteCarreteiroAction.storeMap"/>
		<adsm:newButton />
		<adsm:removeButton/>		
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>   

<script>
	document.getElementById("labelPessoa").masterLink = "true";
	
	function onDataLoadIdentificacao_cb(data,exception) {
		beneficiario_pessoa_nrIdentificacao_exactMatch_cb(data);
		if (exception != undefined) {
			alert(exception);			
			resetValue(document.getElementById("beneficiario.pessoa.nrIdentificacao"));
			setFocus(document.getElementById("beneficiario.pessoa.nrIdentificacao"));
		} else 
			setElementValue("beneficiario.pessoa.nmPessoa",getNestedBeanPropertyValue(data,"0.nmPessoa"));	
	}

	function storeBeneficiario_cb(data, error, key){
		
		store_cb(data, error, key);

		if (error == undefined && data != undefined){
			var acaoVigencia = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
			var store = "true";
			validaAcaoVigencia(acaoVigencia, store);
			setDisabled("beneficiario.pessoa.tpPessoa", true);
			setDisabled("beneficiario.pessoa.tpIdentificacao", true);
			setDisabled("beneficiario.pessoa.nrIdentificacao", true);
			setElementValue("beneficiario.pessoa.idPessoa",
					getNestedBeanPropertyValue(data, "beneficiario.pessoa.idPessoa"));
		}
		
		var msg = getNestedBeanPropertyValue(data, "msgContaBancaria");
		if (msg != undefined){
			alert(msg);
		} 
	}
	
	
	function beneficiario_pageLoad_cb(data){
		onPageLoad_cb(data);
		setElementValue(document.getElementById("labelPessoa"),labelPessoaTemp);	
		document.getElementById("beneficiario.pessoa.nrIdentificacao").serializable = "true";
	}

	function novo(){
		setDisabled("beneficiario.pessoa.nmPessoa", false);
		setDisabled("beneficiario.pessoa.dsEmail", false);
		setDisabled("dtVigenciaInicial", false);
		setDisabled("dtVigenciaFinal", false);
		setFocusOnFirstFocusableField();
	}


	function beneficiarioDataLoad_cb(data, error){
		onDataLoad_cb(data, error);

		onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("beneficiario.pessoa.tpIdentificacao"), 
					numberElement:document.getElementById("beneficiario.pessoa.nrIdentificacao")});
	
		setDisabled("beneficiario.pessoa.tpPessoa", true);
		setDisabled("beneficiario.pessoa.tpIdentificacao", true);
		setDisabled("beneficiario.pessoa.nrIdentificacao", true);

		var acaoVigencia = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		validaAcaoVigencia(acaoVigencia, null);
	}
	
	function validaAcaoVigencia(acaoVigencia, tipoEvento){
		if (acaoVigencia == 0){
			novo();
			if(tipoEvento == "" ||  tipoEvento == null) 
		     	setFocusOnFirstFocusableField(document);
		    else
		       setFocusOnNewButton(document);
		} else if (acaoVigencia == 1){
			setDisabled("dtVigenciaInicial", true);		
			setDisabled("__buttonBar:0.removeButton",true);	
			if(tipoEvento == "" ||  tipoEvento == null)
		     	setFocusOnFirstFocusableField(document);
		    else
		       setFocusOnNewButton(document);
		} else if (acaoVigencia == 2) {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
			setFocusOnNewButton(document);
		}
	}

	function initWindow(event){
		if (event.name != "gridRow_click" && event.name != "storeButton"){
			setDisabled("beneficiario.pessoa.tpPessoa", false);
			setDisabled("beneficiario.pessoa.tpIdentificacao", true);
			setDisabled("beneficiario.pessoa.nrIdentificacao", true);
			novo();
		}
	}

	/**
	 * Valida a pessoa de acordo com o retorno do método na service.
	 */
	function validaIdPessoa_cb(data,exception) {
		if (exception != undefined) {
			alert(exception);
			reiniciaValores();
			resetValue(document.getElementById("beneficiario.pessoa.nrIdentificacao"));
			setFocus(document.getElementById("beneficiario.pessoa.nrIdentificacao"));
			return false;
		}
		
		if (data.idPessoa != undefined){
			setElementValue("beneficiario.pessoa.idPessoa",data.idPessoa);
			setElementValue("beneficiario.idBeneficiario",data.idBeneficiario);
			setElementValue("beneficiario.pessoa.nmPessoa",data.nmPessoa);
			setElementValue("beneficiario.pessoa.tpIdentificacao",data.tpIdentificacao);
			setElementValue("beneficiario.pessoa.tpPessoa",data.tpPessoa);
			setElementValue("beneficiario.pessoa.dsEmail",data.dsEmail);
			return true;
		}
	
		//Pessoa não existe
		//Então reiniciar valores
		reiniciaValores();
		
		return true;
	}
	
	function reiniciaValores(){
		resetValue(document.getElementById("beneficiario.pessoa.idPessoa"));
		resetValue(document.getElementById("beneficiario.pessoa.nmPessoa"));
		resetValue(document.getElementById("beneficiario.pessoa.dsEmail"));
	}
	
	function pageLoadBeneficiario() {
   		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("beneficiario.pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("beneficiario.pessoa.tpIdentificacao")
      			   ,numberElement:document.getElementById("beneficiario.pessoa.idPessoa")});
	}
	 
	
	
</script>