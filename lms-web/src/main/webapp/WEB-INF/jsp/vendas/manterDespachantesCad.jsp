<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function myOnPageLoad(){
		onPageLoad();

	     initPessoaWidget({ tpTipoElement:document.getElementById("pessoa.tpPessoa")
	     , tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
	       , numberElement:document.getElementById("pessoa.idPessoa") });		
	}
	
	
</script>
<adsm:window service="lms.vendas.despachanteService" onPageLoad="myOnPageLoad" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/vendas/manterDespachantes" id="formDespachante" idProperty="idDespachante" onDataLoadCallBack="myDataLoadCallBack">
        <adsm:hidden property="labelDespachante"  serializable="false"/>
     
        <adsm:combobox definition="TIPO_PESSOA.cad" labelWidth="15%" width="40%"
        	label="tipoPessoa" />
		<adsm:complement label="identificacao" labelWidth="14%" width="31%" required="false" >
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" 
				required="false"/>
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" 
					required="false"
					onDataLoadCallBack="pessoa_nrIdentificacao"
					service="lms.vendas.manterDespachantesAction.findPessoa"/>
		</adsm:complement>
		
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" 
				label="nome" maxLength="50" size="50" 
				required="true" labelWidth="15%" width="40%" />

		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="14%" width="31%" required="true" defaultValue="A"/>

		<adsm:textbox property="pessoa.dhInclusao" label="dataInclusao" labelWidth="15%" width="40%" dataType="JTDateTimeZone" picker="false" disabled="true"/>
		<adsm:textbox property="pessoa.dsEmail" label="email" dataType="email" maxLength="60" size="40" labelWidth="14%" width="30%"/>

		<adsm:textbox property="pessoa.nrRg" label="rg" dataType="text" labelWidth="15%" width="40%"  maxLength="20"/>
		<adsm:textbox property="pessoa.dsOrgaoEmissorRg" label="orgaoEmissor" dataType="text" maxLength="10" labelWidth="14%" width="30%"/>
                                       
		<adsm:textbox property="pessoa.dtEmissaoRg" label="dataEmissao" labelWidth="15%" width="40%" dataType="JTDate" picker="true" />
                                       
		<adsm:buttonBar>
			
			<adsm:button caption="enderecos" action="configuracoes/manterEnderecoPessoa" cmd="main" >
		        <adsm:linkProperty src="idDespachante" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelDespachante" target="labelPessoaTemp" />
			</adsm:button>
	
	        <adsm:button caption="contatos" action="configuracoes/manterContatos" cmd="main" >
		        <adsm:linkProperty src="idDespachante" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelDespachante" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="telefones" action="configuracoes/manterTelefonesPessoa" cmd="main">
				<adsm:linkProperty src="labelDespachante" target="labelPessoaTemp"/>			
				<adsm:linkProperty src="idDespachante" target="pessoa.idPessoa"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>												
			</adsm:button>	
			<adsm:button caption="inscricoesEstaduais" action="configuracoes/manterInscricoesEstaduais" id="inscricoesEstaduais" cmd="main" >
		        <adsm:linkProperty src="idDespachante" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelDespachante" target="labelPessoaTemp" />
			</adsm:button>		
			<adsm:storeButton callbackProperty="despachante"/>
			<adsm:newButton/>
			<adsm:removeButton service="lms.vendas.despachanteService.removeDespachanteById"/>
		</adsm:buttonBar>
<script><!--
    document.getElementById("labelDespachante").masterLink = "true";
    
    function myOnPageLoad_cb(data, errorMessage, errorMsg, eventObj){
    	onPageLoad_cb(data, errorMessage, errorMsg, eventObj);
   	   	var labelTemp = '<adsm:label key="despachante"/>';
  	  	setElementValue("labelDespachante",labelTemp);  
  	}

	function disabledPessoaFields(value) {
		setDisabled("pessoa.tpPessoa", value);
		setDisabled("pessoa.tpIdentificacao", true);
		setDisabled("pessoa.nrIdentificacao", true);
		if (!value && getElement("pessoa.tpPessoa").selectedIndex > 0) {
			setDisabled("pessoa.tpIdentificacao", value);
			setDisabled("pessoa.nrIdentificacao", value);
		}
	}
	/**
	 * Habilita / desabilita os campos de RG quando é uma pessoa jurídica
	*/
	function disabledRgFields() {
		var desabilitar = false;
		if (getElementValue("pessoa.tpPessoa") == "J"){
			resetValue("pessoa.nrRg");
			resetValue("pessoa.dsOrgaoEmissorRg");
			resetValue("pessoa.dtEmissaoRg");
			desabilitar = true;
		}
		
		setDisabled("pessoa.nrRg", desabilitar);
		setDisabled("pessoa.dsOrgaoEmissorRg", desabilitar);
		setDisabled("pessoa.dtEmissaoRg", desabilitar);	

		if (getElementValue("idDespachante") != ""){			
			setDisabled("inscricoesEstaduais", false);		
		} else {
			setDisabled("inscricoesEstaduais", true);
		}
	}
   
    /**
	 * Setar label dinamico
	 */
    function myDataLoadCallBack_cb(data, errorMessage, errorMsg, eventObj){
    	var dlEventObj = eventObj;
		if (eventObj == undefined) {
			dlEventObj = {name:"onDataLoad"};
		}  	   
		if (errorMessage != undefined) {
  	    	alert(errorMessage);
  	   	}

		onDataLoad_cb(data, errorMessage, errorMsg, eventObj);
		setEnableDisableButtons(document, dlEventObj);		
		disabledPessoaFields(true);
		disabledRgFields();
		setFocusOnFirstFocusableField();

    	// carrega os dados da tag de identificacao apropriadamente
    	onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), numberElement:document.getElementById("pessoa.nrIdentificacao")});			
    }   

	function despachante_cb(data, error) {
		store_cb(data, error);
		if (error != undefined) {
			return;
		}
		disabledPessoaFields(true);
	}

    /**
	 * Quando detalhar item, desabilita campos de Identificacao da Pessoa
	 */
	function initWindow(eventObj) {
		disabledPessoaFields(false);
	    disabledRgFields();
	    if(eventObj.name != "storeButton") {
	    	setFocusOnFirstFocusableField();
	    }
    }

	/**
	 * Popula dados de Pessoa no formulário.
	 */
	function pessoa_nrIdentificacao_cb(data, erro){
		//Erro do Service
		if (erro != undefined){
			alert(erro);
			cleanButtonScript(this.document);
			return false;
		}

		// Se Pessoa cadastrada
		if (data != undefined && data.length == 1){
			if(data[0].idPessoa != undefined && data[0].idPessoa != ""){
				setElementValue("pessoa.idPessoa",data[0].idPessoa);
			}else{
				setElementValue("pessoa.idPessoa","");
			}
			
			if(data[0].nmPessoa != undefined && data[0].nmPessoa != ""){
				setElementValue("pessoa.nmPessoa",data[0].nmPessoa);
			}else{
				setElementValue("pessoa.nmPessoa","");
			}
			
			if(data[0].dsEmail != undefined && data[0].dsEmail != ""){
				setElementValue("pessoa.dsEmail",data[0].dsEmail);
			}else{
				setElementValue("pessoa.dsEmail","");
			}
			
			if(data[0].nrRg != undefined && data[0].nrRg != ""){
				setElementValue("pessoa.nrRg",data[0].nrRg);
			}else{
				setElementValue("pessoa.nrRg","");
			}
			
			if(data[0].dsOrgaoEmissorRg != undefined && data[0].dsOrgaoEmissorRg != ""){
				setElementValue("pessoa.dsOrgaoEmissorRg",data[0].dsOrgaoEmissorRg);
			}else{
				setElementValue("pessoa.dsOrgaoEmissorRg","");
			}
			
			if(data[0].dtEmissaoRg != undefined && data[0].dtEmissaoRg != ""){
				setElementValue("pessoa.dtEmissaoRg",setFormat(document.getElementById("pessoa.dtEmissaoRg"),data[0].dtEmissaoRg));
			}else{
				setElementValue("pessoa.dtEmissaoRg",setFormat(document.getElementById("pessoa.dtEmissaoRg"),""));
			}
		}
	}

	function pessoa_tpPessoa_onChange(){
		var retorno = pessoa_tpPessoaOnChangeHandler();	
		disabledRgFields();
		return retorno;
	}
--></script>
	</adsm:form>
</adsm:window>	

