<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
    /**
	 * Carrrega em um campo HIDDEN o valor do label, para ser passado por linkProperty (para outra página)
	 */	 
	function setLabelFabricanteHidden(){
		if (i18NLabel.getLabel("fabricante") != ""){
			setElementValue(document.getElementById("labelFabricante"), i18NLabel.getLabel("fabricante"));
		}
	}

	function fabricanteOnLoad(){
		onPageLoad();

		setLabelFabricanteHidden();
		initPessoaWidget({ tpTipoElement:document.getElementById("pessoa.tpPessoa")
	     , tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
	       , numberElement:document.getElementById("pessoa.idPessoa") });
	    changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'cad'});
	}
	
	
</script>	
<adsm:window service="lms.vol.manterFabricantesAction" onPageLoad="fabricanteOnLoad" >
	<adsm:form action="/vol/manterFabricantes" id="formFabricante" idProperty="idFabricante" onDataLoadCallBack="myDataLoad">
        <adsm:hidden property="labelFabricante"  serializable="false"/>
	
	    <adsm:i18nLabels>
			<adsm:include key="fabricante"/>
		</adsm:i18nLabels>
		
	    <adsm:hidden property="labelFabricante" serializable="false"/>
	
        <adsm:complement label="identificacao" required="true" labelWidth="18%" width="82%">
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad"/>
			<adsm:lookup definition="IDENTIFICACAO_PESSOA"  service="lms.vol.manterFabricantesAction.findPessoa"
			onDataLoadCallBack="pessoa_nrIdentificacao" onchange="return pessoa_nrIdentificacao_onChange();" />					
		</adsm:complement>
		
				<!-- Atributos para salvar/atualizar em Pessoa -->
		<adsm:hidden property="pessoa.tpPessoa" value="J"/>
		
		<adsm:textbox dataType="text" size="95%" labelWidth="18%" width="82%"
			property="pessoa.nmPessoa" label="nome" maxLength="50" required="true"/>
		
	 	<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="18%" width="32%" required="true" defaultValue="A"/>
		<adsm:textbox dataType="email" property="pessoa.dsEmail" label="email" 	size="70" maxLength="60" labelWidth="18%" width="82%" />		
		
	<%--	<adsm:lookup 
		      labelWidth="18%" width="82%"
		      dataType="text" 
		      property="contato" 
			  idProperty="idContato" 
			  criteriaProperty="nmContato" 
			  label="contato" 
			  size="40" 
			  maxLength="40" 
			  required="false"
			  action="/configuracoes/manterContatos" 
			  exactMatch="true" >
		</adsm:lookup>	--%>
						
		<adsm:buttonBar>
			<adsm:button caption="contatos" action="/configuracoes/manterContatos" cmd="main">
				<adsm:linkProperty src="idFabricante" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="labelFabricante" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="enderecos" action="/configuracoes/manterEnderecoPessoa" cmd="main">
				<adsm:linkProperty src="labelFabricante" target="labelPessoaTemp" />
				<adsm:linkProperty src="idFabricante" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
			</adsm:button>	
			<adsm:button caption="telefones" action="configuracoes/manterTelefonesPessoa" cmd="main">
				<adsm:linkProperty src="labelFabricante" target="labelPessoaTemp" />
				<adsm:linkProperty src="idFabricante" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
			</adsm:button>	
				
			<adsm:button caption="modelosEqp" action="/vol/manterModelos" cmd="main">
				<adsm:linkProperty src="labelFabricante" target="labelPessoaTemp" />
				<adsm:linkProperty src="idFabricante" target="volFabricante.idFabricante" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="nmPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="volFabricante.pessoa.nrIdentificacao" />
			</adsm:button>		
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	/**
	 * Após inclusão atribui o idPessoa inserido.
	 * Atribui o valor do label para o campo hidden do form.
	 */
	function myDataLoad_cb(data, errors){
		var idPessoa = getNestedBeanPropertyValue(data, "_value");
		
		if (idPessoa == undefined && data != undefined){
			idPessoa = getNestedBeanPropertyValue(data, "pessoa.idPessoa");
		}
	
		var temPessoa = false;
			
		if (idPessoa != undefined){
			setElementValue("pessoa.idPessoa", idPessoa);
			setDisabled("pessoa.tpIdentificacao", true );
			setDisabled("pessoa.nrIdentificacao", true );
			temPessoa = true;
		}
		onDataLoad_cb(data,errors);
		
		onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), numberElement:document.getElementById("pessoa.nrIdentificacao")});
		
		setLabelFabricanteHidden();
	
		setDisabled("pessoa.tpIdentificacao", true );
		setDisabled("pessoa.nrIdentificacao", true );

		if (temPessoa == true) {
			setFocus(document.getElementById("pessoa.nmPessoa"));		
		}
	}
	
	/**
	 * Quando detalhar item, desabilita campos de Identificacao da Pessoa
	 */
	function initWindow(eventObj) {
		if (eventObj.name == "newButton_click" || eventObj.name == "tab_click"){
		    setElementValue("tpSituacao","A");
		}
		if (eventObj.name != "tab_click" && eventObj.name != "storeButton" && eventObj.name != "gridRow_click") {
	    	resetValue(document);
	    }
	    if (eventObj.name == "storeButton"){
	    	setFocus(document.getElementById("__buttonBar:0.newButton"));
	    }
	    
		setDisabled("pessoa.tpIdentificacao", (eventObj.name == "gridRow_click") );
		setDisabled("pessoa.nrIdentificacao", true );
		setLabelFabricanteHidden();
		if (eventObj.name == "gridRow_click") {
			setFocus(document.getElementById("pessoa.nmPessoa"));
		}
	}

	
	/**
	 *
	 */
	function reiniciaValores(){
		resetValue(document.getElementById("pessoa.nrIdentificacao"));
		resetValue(document.getElementById("pessoa.tpIdentificacao"));
	}


	function pessoa_nrIdentificacao_cb(d,e){
		pessoa_nrIdentificacao_exactMatch_cb(d,e);
		
		if (e != undefined) {
			alert(e);
		} else {
			if (d != undefined && d.length == 1) {
				setElementValue("pessoa.dhInclusao", setFormat("pessoa.dhInclusao",d[0].dhInclusao));
			}
		}
	}
	
	function pessoa_nrIdentificacao_onChange(){
		pessoa_nrIdentificacaoOnChangeHandler();
		if (getElementValue("pessoa.nrIdentificacao")==""){
			resetValue(document.getElementById("pessoa.dhInclusao"));
		}
	}
	
	/**
	 * Popula dados de Pessoa no formulário.
	 * 
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
	
		}
	}
	
</script>
