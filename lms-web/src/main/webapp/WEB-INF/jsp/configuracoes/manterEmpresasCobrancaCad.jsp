<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
    /**
	 * Carrrega em um campo HIDDEN o valor do label, para ser passado por linkProperty (para outra página)
	 */	 
	function setLabelEmpresaCobrancaHidden(){
		if (i18NLabel.getLabel("empresaCobranca") != ""){
			setElementValue(document.getElementById("labelEmpresaCobranca"), i18NLabel.getLabel("empresaCobranca"));
		}
	}

	function empresasCobrancaOnLoad(){
		setLabelEmpresaCobrancaHidden();
		onPageLoad();
		setDisabled("concluirCadastroButton", true );
		
		initPessoaWidget({ tpTipoElement:document.getElementById("pessoa.tpPessoa")
	     , tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
	       , numberElement:document.getElementById("pessoa.idPessoa") });
	    changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'cad'});
	}
	
	
</script>
<adsm:window service="lms.configuracoes.manterEmpresasCobrancaAction" onPageLoad="empresasCobrancaOnLoad">

	<adsm:i18nLabels>
		<adsm:include key="empresaCobranca"/>
	</adsm:i18nLabels>
		
	<adsm:form action="/configuracoes/manterEmpresasCobranca" idProperty="idEmpresaCobranca"  onDataLoadCallBack="myDataLoad">

	
		<adsm:complement label="identificacao" required="true" labelWidth="18%" width="82%">
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" service="lms.configuracoes.manterEmpresasCobrancaAction.findTpIdentificacaoPessoa"/>
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" 
					service="lms.configuracoes.manterEmpresasCobrancaAction.validateExistenciaEmpresaCobranca"
					onDataLoadCallBack="pessoa_nrIdentificacao" onchange="return pessoa_nrIdentificacao_onChange();"/>					
		</adsm:complement>
		
		<adsm:hidden property="pessoa.nmFantasia" serializable="true"/>
		
				<!-- Atributos para salvar/atualizar em Pessoa -->
		<adsm:hidden property="pessoa.tpPessoa" value="J"/>
		
		<adsm:textbox dataType="text" size="95%" labelWidth="18%" width="82%"
			property="pessoa.nmPessoa" label="nome" maxLength="50" required="true"/>
		
		<adsm:combobox required="true" property="tpSituacao" domain="DM_STATUS_PESSOA" 
			label="situacao" labelWidth="18%" width="32%"/>
			
			
		<adsm:textbox dataType="JTDateTimeZone" disabled="true" picker="false"
			property="pessoa.dhInclusao" label="dataInclusao" />
			
		<adsm:textbox dataType="email" property="pessoa.dsEmail" label="email" 
			size="70" maxLength="60" labelWidth="18%" width="82%" />

		<adsm:hidden property="labelEmpresaCobranca" serializable="false"/>

		<adsm:buttonBar>
			<adsm:button caption="contatos" action="/configuracoes/manterContatos" cmd="main">
				<adsm:linkProperty src="idEmpresaCobranca" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelEmpresaCobranca" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="enderecos" action="/configuracoes/manterEnderecoPessoa" cmd="main">
				<adsm:linkProperty src="idEmpresaCobranca" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelEmpresaCobranca" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="inscricoesEstaduais" action="/configuracoes/manterInscricoesEstaduais" cmd="main"
				style="width:135px">
				<adsm:linkProperty src="idEmpresaCobranca" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelEmpresaCobranca" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="dadosBancarios" action="/configuracoes/manterDadosBancariosPessoa" cmd="main"
				style="width:125px">
				<adsm:linkProperty src="idEmpresaCobranca" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelEmpresaCobranca" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="concluirCadastro" service="lms.configuracoes.empresaCobrancaService.validateConcluirCadastro" 
				callbackProperty="concluirCadastro" disabled="false" id="concluirCadastroButton"/>
			<adsm:storeButton id="storeID"/>
			<adsm:button id="newButtonID" onclick="initPage();" caption="limpar" buttonType="newButton" disabled="false"/>
			<adsm:button caption="excluir" buttonType="removeButton" onclick="removeEC();"/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>

<script>

	/**
	 * Após inclusão atribui o idPessoa inserido.
	 * Atribui o valor do label para o campo hidden do form.
	 */
	function myDataLoad_cb(data, errors){
		//changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.idPessoa'), tabCmd:'cad'});
		var idPessoa = getNestedBeanPropertyValue(data, "_value");
		
		if (idPessoa == undefined && data != undefined){
			idPessoa = getNestedBeanPropertyValue(data, "pessoa.idPessoa");
		}
		
		if (idPessoa != undefined){
			setElementValue("pessoa.idPessoa", idPessoa);
			setDisabled("pessoa.tpIdentificacao", true );
			setDisabled("pessoa.nrIdentificacao", true );
		}
		onDataLoad_cb(data,errors);
		
		onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), numberElement:document.getElementById("pessoa.nrIdentificacao")});
		
		setLabelEmpresaCobrancaHidden();
		setFocusOnFirstFocusableField(document);
		var oSituacao = document.getElementById("tpSituacao");
		if (oSituacao.value != "N"){
			removeIncompletoOption(oSituacao);
			setDisabled("concluirCadastroButton", true );
			setDisabled("tpSituacao", false );
		}else {
			setDisabled("concluirCadastroButton", false );
			setDisabled("tpSituacao", true );
		}
	}

    
	function removeEC(){
		removeButtonScript('lms.configuracoes.empresaCobrancaService.removeEmpresaCobrancaById', 'removeEC', 'idEmpresaCobranca', document);
		
	}
	
	function removeEC_cb(data){
		removeById_cb(data);
		setFocus(document.getElementById("pessoa.tpIdentificacao"), false);
	}

	var OPTION_INCOMPLETO = undefined;
	function setOptionIncompleto(oIncompleto){
		if (OPTION_INCOMPLETO == undefined){
			OPTION_INCOMPLETO = new Option(oIncompleto.text, oIncompleto.value);
		}
	}
	function concluirCadastro_cb(data,erro){
		if (erro != undefined){
			alert(erro);
		}else{
			document.getElementById("tpSituacao").value = "A";
			setDisabled("tpSituacao", true);
			storeButtonScript('lms.configuracoes.empresaCobrancaService.store', 'store', idEmpresaCobrancaFormElement);
		}
	}
	function getIndexByValue(oSelect, _value){
		var size = oSelect.options.length;
		for (var i = 0; i < size; i++){
			if (oSelect.options[i].value == _value){
				return i;
			}
		}
	}
	function removeIncompletoOption(oSituacao){
		var incompleto = getIndexByValue(oSituacao ,"N");
		if (incompleto != undefined){
			setOptionIncompleto(oSituacao.options[incompleto]);
			oSituacao.options[incompleto] = null;
		}
	}
	
	function habilitarDesabilitarBotaoConcluir(){
		if (getElementValue("tpSituacao")=="N") {
			setDisabled("tpSituacao",true);
			setDisabled("concluirCadastroButton",false);					
		} else {   
			setDisabled("concluirCadastroButton",true);		
			setDisabled("tpSituacao",false);
		}
	}
	
	
	
	/* Setando os valores padroes */
	function initPage() {
		newButtonScript(document, true, {name:'newButton_click'});
		
		setDisabled("pessoa.tpIdentificacao", false);

		// Necessário carregar novamente o tpSituacao, pois a option "INCOMPLETO" é retirado das opções
		// disponiveis quando vem da grid.
	    var sdo = createServiceDataObject("adsm.configuration.domainValueService.find", "tpSituacaoAfter", {domain:{name:"DM_STATUS_PESSOA"}});
        xmit({serviceDataObjects:[sdo]});
	
	
	}

	/* setando o padrao para o tpSituacao */
    function tpSituacaoAfter_cb(data,erro){
	    tpSituacao_cb(data,erro);
	    setElementValue("tpSituacao","N");
	    setDisabled("tpSituacao",getElementValue("tpSituacao")=="N");    
	    setFocusOnFirstFocusableField();	
    }	
	
	
	/**
	 * Quando detalhar item, desabilita campos de Identificacao da Pessoa
	 */
	function initWindow(eventObj) {
		if (eventObj.name == "newButton_click" || eventObj.name == "tab_click"){
			setDisabled("pessoa.tpIdentificacao", false);
		    setElementValue("tpSituacao","N");
		}

		if (eventObj.name != "tab_click" && eventObj.name != "storeButton" && eventObj.name != "gridRow_click" && eventObj.name != "newButton_click") {
	    	initPage();
	    }
		

			
		var oSituacao = document.getElementById("tpSituacao");;
		var indexN = getIndexByValue(oSituacao,"N");
	
		if (eventObj.name == "storeButton"){ 
			if (getElementValue(document.getElementById("tpSituacao")) == "N"){
				setDisabled("tpSituacao", true);
				setDisabled("concluirCadastroButton", false );
			}else{
				setDisabled("concluirCadastroButton", true );
				removeIncompletoOption(oSituacao);
				setDisabled("tpSituacao", false);
				setDisabled("concluirCadastroButton", true );
			}

			idPessoa = getElementValue('idEmpresaCobranca');
			
			if (idPessoa != undefined){
				setElementValue("pessoa.idPessoa", idPessoa);
			//	setDisabled("pessoa.tpIdentificacao", true );
			//	setDisabled("pessoa.nrIdentificacao", true );
			}
			
		}else if (eventObj.name != "gridRow_click"){
			document.getElementById("tpSituacao").value = "N";
			setDisabled("tpSituacao", true);
		}

		setDisabled("pessoa.tpIdentificacao", (eventObj.name == "gridRow_click") );
		setDisabled("pessoa.nrIdentificacao", true );
		setLabelEmpresaCobrancaHidden();

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
		var retorno = pessoa_nrIdentificacaoOnChangeHandler();
		if (getElementValue("pessoa.nrIdentificacao")==""){
			resetValue(document.getElementById("pessoa.dhInclusao"));
		}
		return retorno;
	}
	
</script>
