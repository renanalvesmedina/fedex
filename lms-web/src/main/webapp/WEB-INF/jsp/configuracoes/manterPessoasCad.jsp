<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function myOnPageLoad(){
		onPageLoad();
		//setDisabled("pessoa.nrIdentificacao", true );
	     initPessoaWidget({ tpTipoElement:document.getElementById("pessoa.tpPessoa")
	     , tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
	       , numberElement:document.getElementById("pessoa.idPessoa") });		
	}
	
	
</script>
<adsm:window service="lms.configuracoes.manterPessoasAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-27053" />
		<adsm:include key="pessoa" />
	</adsm:i18nLabels>
	<adsm:form action="/configuracoes/manterPessoas" idProperty="idPessoa" onDataLoadCallBack="myOnDataLoad">	
		<adsm:hidden property="pessoa.tpPessoa"/>	
		<adsm:textbox dataType="text" label="tipoPessoa" property="pessoa.tpPessoaTmp" maxLength="50" disabled="true"/>        	
		<adsm:complement label="identificacao" required="true" >
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" onchange="return tpIdentificacaoOnChange();"/>
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" 
					service="lms.configuracoes.manterPessoasAction.findPessoa"
					onDataLoadCallBack="pessoaCallback"
					onchange="return nrIdentificacaoOnChange();"/>
		</adsm:complement>		
		
		<adsm:hidden property="labelPessoa"/>		 
		
		<adsm:textbox dataType="text" size="95%" labelWidth="15%" width="82%" property="pessoa.nmPessoa" label="nome" maxLength="50" disabled="true"/>
		<adsm:textbox dataType="JTDateTimeZone" picker="false" serializable="false" labelWidth="15%" width="82%" property="pessoa.dhInclusao" label="dataInclusao" disabled="true"/>
		<adsm:textbox dataType="text" property="pessoa.dsEmail" label="email" size="60" maxLength="60" labelWidth="15%" width="85%" disabled="true"/>
		<adsm:textbox dataType="text" labelWidth="15%" width="18%" property="pessoa.nrRg" label="numeroRG" maxLength="20" disabled="true"/>
		<adsm:textbox dataType="text" property="pessoa.dsOrgaoEmissorRg" label="orgaoEmissorRG" size="10" maxLength="10" labelWidth="18%" width="12%" disabled="true"/>
		<adsm:textbox dataType="JTDate" picker="false" property="pessoa.dtEmissaoRg" label="dataEmissaoRG" labelWidth="19%" width="15%" disabled="true"/>
		<adsm:checkbox property="pessoa.blAtualizacaoCountasse" label="atualizacaoCountasse" disabled="true" labelWidth="15%"/>
		<adsm:buttonBar>
			<adsm:button caption="telefones" action="/configuracoes/manterTelefonesPessoa" cmd="main">
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>
			</adsm:button>
			<adsm:button caption="contatos" action="/configuracoes/manterContatos" cmd="main">
				<adsm:linkProperty src="idPessoa" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>
			</adsm:button>
			<adsm:button caption="enderecos" action="/configuracoes/manterEnderecoPessoa" cmd="main">
				<adsm:linkProperty src="idPessoa" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>							
			</adsm:button>			
			<adsm:button caption="inscricoesEstaduais" id="buttonInscricaoEstadual" action="/configuracoes/manterInscricoesEstaduais" cmd="main">
				<adsm:linkProperty src="idPessoa" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>						
			</adsm:button>			
			<adsm:button caption="dadosBancarios" action="/configuracoes/manterDadosBancariosPessoa" cmd="main">
				<adsm:linkProperty src="idPessoa" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>						
			</adsm:button>						
			<adsm:storeButton/>
			<adsm:button caption="excluir" onclick="myRemoveButtonScript();"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
    
    function myOnPageLoad_cb(data,erro){
		onPageLoad_cb(data,erro);
	    setElementValue(document.getElementById("labelPessoa"),i18NLabel.getLabel("pessoa"));
    }
    
    function myOnDataLoad_cb(d,e){  
    	
    	if (e == undefined){
	 		setElementValue("pessoa.tpPessoa", d.pessoa.tpPessoa);
	 		setElementValue("pessoa.tpIdentificacao", d.pessoa.tpIdentificacao);
	 		tpIdentificacaoOnChange();
 		}		

    	changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'cad'});	            	
    	onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), numberElement:document.getElementById("pessoa.nrIdentificacao")});
		
    	onDataLoad_cb(d,e);

	    //habilitaDesabilitaRg();
	    setDisabled("pessoa.nrIdentificacao", false);		    	    
    }
	
	function myRemoveButtonScript() {
		removeButtonScript("lms.configuracoes.pessoaService.removeById", "myRemoveButtonScript", 'idPessoa', this.document);
	}
	
	function myRemoveButtonScript_cb(map, errorMsg) {
		if (errorMsg == null){
			newButtonScript(document, true, {name:"removeButton"});
			//ref para tabgroup    	
			var tabGroup = getTabGroup(this.document);
			
			tabGroup.selectTab("pesq");
		} else {
			alert(errorMsg);
		}
	}
    
    document.getElementById("labelPessoa").masterLink = "true";

	function pessoaCallback_cb(data,erro) {
		if (data == undefined || erro != undefined) {
			return pessoa_nrIdentificacao_exactMatch_cb(data);				
		}

		// Se Pessoa cadastrada
		if (data.idPessoa != undefined){

			setElementValue("pessoa.tpIdentificacao",data.tpIdentificacao);
			
			tpIdentificacaoOnChange();
			setElementValue("pessoa.idPessoa",data.idPessoa);
			setElementValue("pessoa.nmPessoa",data.nmPessoa);
			setElementValue("pessoa.tpPessoa",data.tpPessoa);
			setElementValue("pessoa.dsEmail",data.dsEmail);
			setElementValue("pessoa.nrRg",data.nrRg);
			setElementValue("pessoa.dsOrgaoEmissorRg",data.dsOrgaoEmissorRg);
			setElementValue("pessoa.nrIdentificacao",data.nrIdentificacaoFormatado);
			setElementValue("pessoa.dtEmissaoRg",setFormat(document.getElementById("pessoa.dtEmissaoRg"), data.dtEmissaoRg));
		}

	}
	
	function tpIdentificacaoOnChange(){
		var tpIdentificacaoElement = document.getElementById("pessoa.tpIdentificacao");
		var numberElement = document.getElementById("pessoa.nrIdentificacao");
		
		var tpIdentificacao = getElementValue(tpIdentificacaoElement);
		
		setElementValue("pessoa.nrIdentificacao", "");
		
		setDisabled(numberElement, tpIdentificacao == "");
		
		if (numberElement.disabled == false) {
		   var numberInputElement;
		   if ("lookup" == numberElement.widgetType)
		      numberInputElement = document.getElementById(numberElement.property + "." + numberElement.criteriaProperty);
		   else
		      numberInputElement = numberElement;
			
		   if (tpIdentificacao == "")
		      numberInputElement.dataType = "text";
		   else
		      numberInputElement.dataType = tpIdentificacao;
		}
		
		return true;	
	}
	
	function nrIdentificacaoOnChange(){
		if (getElementValue("pessoa.nrIdentificacao") != ""){
			return pessoa_nrIdentificacaoOnChangeHandler();
		}
	}	
	
	
	
	/**
	 * Habilita / desabilita os campos de RG quando é uma pessoa jurídica
	
	function habilitaDesabilitaRg() {
		var desabilitar = false;
		if (getElementValue("pessoa.tpPessoa") == "J"){
			desabilitar = true;
		}
		
		setDisabled("pessoa.nrRg", desabilitar);
		setDisabled("pessoa.dsOrgaoEmissorRg", desabilitar);
		setDisabled("pessoa.dtEmissaoRg", desabilitar);						
	}	*/
</script>