<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>
	
	/**
	  * Função chamada no início da página
	  */
	function myOnPageLoad() {
		onPageLoad();
		setDisabled("controleFormulario.controleForm",true);
		// Seta a sigla da filial do usário para masterLink
		//getElement("sgFilialUsuario").masterLink = "true";
	}
</script>

<adsm:window service="lms.configuracoes.controleFormularioService" onPageLoad="myOnPageLoad" onPageLoadCallBack="myPageLoadCallBack">
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-27059"/>
		<adsm:include key="LMS-27060"/>
		<adsm:include key="LMS-27061"/>
		<adsm:include key="LMS-27074"/>
	</adsm:i18nLabels>
	
	<adsm:form action="/configuracoes/manterControleFormularios" idProperty="idControleFormulario" onDataLoadCallBack="controleFormulario">

		<adsm:hidden property="sgFilialUsuario" serializable="true"/>
		<adsm:hidden property="gridRow_click" value="false"/>
		
		<adsm:lookup label="estoqueOrigem" 
					 property="controleFormulario" 
					 service="lms.configuracoes.controleFormularioService.findLookup"
					 idProperty="idControleFormulario" 
					 criteriaProperty="controleForm" 
					 onPopupSetValue="estoqueOrigemPopUp"
				     dataType="text" 
				     action="/configuracoes/manterControleFormularios"
					 width="27%">
			<adsm:propertyMapping relatedProperty="tpFormulario" modelProperty="tpFormulario"/>
			<adsm:propertyMapping criteriaProperty="tpSituacaoAtivo" modelProperty="tpSituacaoFormulario"/>
		</adsm:lookup>

		<adsm:hidden property="tpSituacaoAtivo" 
					 value="A" 
					 serializable="false"/>

		<adsm:lookup label="empresa" 
					 property="empresa" 
					 dataType="text" 
					 service="lms.municipios.empresaService.findLookup" 
					 maxLength="20"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
					 idProperty="idEmpresa" 
					 action="/municipios/manterEmpresas" 
					 labelWidth="18%" 
					 width="40%" 
					 required="true">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="empresa.pessoa.nmPessoa"/>
			<adsm:propertyMapping criteriaProperty="tpSituacaoAtivo" modelProperty="tpSituacao"/>
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" size="20" disabled="true" serializable="false"/>
		</adsm:lookup>
 
		<adsm:lookup label="filial"
				property="filial"
				dataType="text"
				service="lms.municipios.filialService.findLookupBySgFilial"
				exactMatch="false"
				idProperty="idFilial"
				criteriaProperty="sgFilial"
				size="5"
				required="true"
				maxLength="3"
				width="10%"
				action="/municipios/manterFiliais"
				onPopupSetValue="filialOnPopUpSetValue"				
				onDataLoadCallBack="verificaPermissaoDataLoadCallBack">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="14" disabled="true" width="17%" serializable="false"/>
			
		</adsm:lookup>

		

		<adsm:textbox label="dataRecebimento" property="dtRecebimento" dataType="JTDate" labelWidth="18%" width="32%" required="true"/>

		<adsm:combobox label="tipoDocumento" property="tpFormulario" domain="DM_TIPO_FORMULARIO" width="27%" required="true"/>

		<adsm:range label="formularios" labelWidth="18%" width="32%" required="true">
			<adsm:textbox dataType="integer" property="nrFormularioInicial" size="10" maxLength="10" />
			<adsm:textbox dataType="integer" property="nrFormularioFinal" size="10" maxLength="10"/>
		</adsm:range>

		<adsm:combobox label="situacao" property="tpSituacaoFormulario" domain="DM_SITUACAO_FORMULARIO" required="true" width="27%"/>		

		<adsm:range label="selosFiscais" labelWidth="18%" width="32%">
			<adsm:textbox property="nrSeloFiscalInicial" dataType="integer" maxLength="15" size="15" width="16%"/>
			<adsm:textbox property="nrSeloFiscalFinal" dataType="integer" maxLength="15" size="15" width="16%"/>
		</adsm:range>

		<adsm:textbox label="serie" property="cdSerie" dataType="text" maxLength="5" width="27%"/>

		<adsm:textbox label="aidf" property="nrAidf" dataType="text" maxLength="20" labelWidth="18%" width="32%" size="20"/>

		<adsm:hidden property="controleForm"/>

		<adsm:buttonBar>
			<adsm:button caption="impressoras" id="impressorasButton" action="/configuracoes/manterVinculoFormulariosImpressoras" cmd="main">
				<adsm:linkProperty src="idControleFormulario" target="controleFormulario.idControleFormulario"/>
				<adsm:linkProperty src="filial.idFilial" target="controleFormulario.filial.idFilial"/>
				<adsm:linkProperty src="controleForm" target="controleFormulario.controleForm"/>
				<adsm:linkProperty src="nrFormularioInicial" target="controleFormulario.nrFormularioInicial"/>
				<adsm:linkProperty src="nrFormularioFinal" target="controleFormulario.nrFormularioFinal"/>
				<adsm:linkProperty src="nrSeloFiscalInicial" target="nrSeloFiscalInicialControleFormulario"/>
				<adsm:linkProperty src="nrSeloFiscalFinal" target="nrSeloFiscalFinalControleFormulario"/>
				<adsm:linkProperty src="cdSerie" target="cdSerie"/>
			</adsm:button>

			<adsm:button caption="salvar" buttonType="storeButton" id="storeButton" onclick="myStore();"/>
			<adsm:button id="newButton" caption="limpar" disabled="false" onclick="novoControleFormular(this);" buttonType="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	
	/**
	  * Função chamada no clic do botão salvar
	  */
	function myStore(){
		if( validateTabScript(this.form) && validaIntervaloSelos() 
			&& validaAIDF()){
			
			storeButtonScript('lms.configuracoes.controleFormularioService.store', 'storeCF', document.forms[0]);
		}
		
	}
	
	/**
	  * Valida o intervalo de boletos de acordo com as  regras da especificação
	  */
	function validaIntervaloSelos(){
		var retorno = true;
		
		// Caso seja informado selo inicial e não seja informado o final
		if(getElementValue("nrSeloFiscalInicial") != '' && getElementValue("nrSeloFiscalFinal") == ''){
			alert(i18NLabel.getLabel("LMS-27059"));
			retorno = false;
			
		// Caso não seja informado selo inicial e seja informado o selo final
		}else if(getElementValue("nrSeloFiscalInicial") == '' && getElementValue("nrSeloFiscalFinal") != ''){
			alert(i18NLabel.getLabel("LMS-27060"));
			retorno = false;
		
		// Caso tenha sido informado um intervalo de selos, valida se o intervalo de selos e o de formulários formulários são iguais
		}else if( getElementValue("nrSeloFiscalInicial") != '' ){
			var countNrformularios = parseInt(getElementValue("nrFormularioFinal"),10) - parseInt(getElementValue("nrFormularioInicial"),10) + 1;
			var countNrSelos = parseInt(getElementValue("nrSeloFiscalFinal"),10) - parseInt(getElementValue("nrSeloFiscalInicial"),10) + 1;
			
			// Caso os intervalos não tenham o mesmo tamanho, lança a exceção
			if(parseInt(countNrformularios,10) != parseInt(countNrSelos,10)){
				alert(i18NLabel.getLabel("LMS-27074"));
				retorno = false;
			}
		}
		
		return retorno;
	}
	
	/** 
	  * Valida as permissões dos botões no callBack do store, e no callBack do form
	  */
	function validaTpSituacaoControleFormulario() {
		var permissao = (getElementValue("idControleFormulario") != "" && getElementValue("tpSituacaoFormulario") == "E");
		if(permissao){
			setDisabled("storeButton" , permissao);
			setDisabled("removeButton" , permissao);
		}
	}
	
	/**
	  * Coloca o foco na lupa da lookup de controle de formulário
	  */
	function focoLupa() {
		setFocus(document.getElementById("controleFormulario_lupa"), false);
	}
	
	/**
	  * CallBack do store
	  */
	function storeCF_cb(data, erro) {
		store_cb(data, erro);
		setControleFormulario();
		validaTpSituacaoControleFormulario();
		
		if( erro != undefined ){		
			focoLupa();
		} else {
			setFocus('newButton',true,true);
		}
		
	}

	/**
	  * Seta o controle de formulário no hidden que será passado para a tela de vinculo com impressoras
	  */
	function setControleFormulario(){
		setElementValue("controleForm", getElementValue("filial.sgFilial") + " - " + getElementValue("nrFormularioInicial") + " - " + getElementValue("nrFormularioFinal"));
	}
	
	/**
	  * Função chamada no início da tela
	  */
	function initWindow(eventObj) {
		
		// Caso o evento não seja o click da grid, habilita o tpFormulario
		if(eventObj.name != "gridRow_click"){
			setDisabled("tpFormulario", false);
		}
		
		// Caso o evento seja clic da tab, removeButton ou newButtonClick, seta a filial da sessão
		if(eventObj.name == "tab_click" || eventObj.name == "removeButton" || eventObj.name == "newButton_click"){
			setElementValue("gridRow_click", "false");
			findFilialSessao();
		}
		
		// No storeButton o foco não é colocado na lupa, pois o padrão é o foco no botão limpar
		if(eventObj.name != "storeButton"){
			setElementValue("tpSituacaoFormulario","A");
			focoLupa();
		}
		
		return true;
	}

	/**
	* Ao carregar os dados verifica se o usuário tem permissão de edição para a filial especificada
	*
	*/
	function controleFormulario_cb(data, erro, errors, eventObj) {
		onDataLoad_cb(data,erro);

		setElementValue("gridRow_click","true");
		
		// Caso exista úm estoque de origem, desabilita o tpFormulario
		if(getElementValue("controleFormulario.idControleFormulario") != ''){
			setDisabled("tpFormulario", true);
		}
		
		// Chamada no callBack do form(gridRow_click), valida a filial do usuário
		validaFilialUsuario();
		
		validaTpSituacaoControleFormulario();
		
		// Coloca o foco na lupa do estoque de origem
		focoLupa();
		
	}

	/**
	* Método para verificação das permissões do usuário sobre a filial especificada.
	* Método chamado pela função de retorno da lookup de filiais
	*/
	function verificaPermissaoDataLoadCallBack_cb(data, exception) {
		if( exception != undefined ) {
			alert(exception);
			return false;
		}
		
		// Caso a função exactMatch retorne true, valida a filial da sessão do usuário com a do controle de formulário
		if(filial_sgFilial_exactMatch_cb(data)) {
			validaFilialUsuario();
		}
		
		return true;
	}

	/** 
	  * Função chamada no show da tab
	  */
	function myOnShow() {
		tab_onShow();
		//setDisabled("tpFormulario", false);
	}

	/**
	  * Função chamada no clic do botão limpar
	  */
	function novoControleFormular(elem) {
		newButtonScript(elem.document, true, {name:'newButton_click'});
		setDisabled("tpFormulario", false);
		setElementValue("tpSituacaoFormulario","A");
		focoLupa();
	}

	/** 
	  * OnPopUpSetValue da lookup de controle de formulário
	  */
	function estoqueOrigemPopUp(data) {
		setDisabled("tpFormulario", true);
		return true;
	}

	/**
	  * Caso seja selecionado CTR ou NFS na combo de documento, o campo AIDF se torna obrigatório
	  */
	function validaAIDF(){
		var retorno = true;
		
		var tpDocumento = getElement("tpFormulario").options[getElement("tpFormulario").selectedIndex].value
		
		if((tpDocumento == 'NFS' || tpDocumento =='CTR') && getElementValue("nrAidf") == ""){
			alert(i18NLabel.getLabel("LMS-27061"));
			retorno = false;
		}
		
		return retorno;
	}
	
	/** 
	  * Valida se a filial do usuario e a filial do controle de formulario são iguais 
	  */
	function validaFilialUsuario(){
		var retorno = true;

		// Caso a sigla da filial da sessão do usuário seja diferente da filial do controle de formulário, desabilita os botões
		if( getElementValue("sgFilialUsuario") != getElementValue("filial.sgFilial") ){
			setDisabled("storeButton", true);
			setDisabled("impressorasButton", true);
			setDisabled("removeButton", true);
			
			retorno = false;
		
		// Caso a sigla da filial da sessão do usuário seja igual a da filial do controle de formulário, habilita os botões
		}else{
			
			if(getElementValue("gridRow_click") == "false"){
				
				setDisabled("storeButton", false);
				
				// Habilita os botões caso o controle de formuário já tenha sido salvo
				if(getElementValue("idControleFormulario") != ""){
					setDisabled("impressorasButton", false);
					setDisabled("removeButton", false);
				}
				
			}
				
		}
		
		return retorno;
	}
	
	/**
	  * Seta os campos da filial de acordo com os dados que retornam 
	  */
	function setFilial_cb(data){
		setElementValue("filial.pessoa.nmFantasia", data.pessoa.nmFantasia);
		setElementValue("filial.sgFilial", data.sgFilial);
		setElementValue("filial.idFilial", data.idFilial);
	}
	
	/**
	  * Seta os campos da filial de acordo com os dados que retornam no pageLoadCallBack
	  */
	function setFilialPageLoadCallBack_cb(data){
		setElementValue("sgFilialUsuario", data.sgFilial);
		getElement("sgFilialUsuario").masterLink = "true";
		
		setElementValue("filial.pessoa.nmFantasia", data.pessoa.nmFantasia);
		setElementValue("filial.sgFilial", data.sgFilial);
		setElementValue("filial.idFilial", data.idFilial);
	}
	
	function myPageLoadCallBack_cb(data, error){
		onPageLoad_cb(data, error);
		
		_serviceDataObjects = new Array(); 
		
		addServiceDataObject(createServiceDataObject("lms.configuracoes.manterVinculoFormulariosImpressorasAction.findFilialUsuario",
			"setFilialPageLoadCallBack", 
			new Array()));
			
		xmit(false);
	}
	
	
	function findFilialSessao(){
		_serviceDataObjects = new Array(); 
		
		addServiceDataObject(createServiceDataObject("lms.configuracoes.manterVinculoFormulariosImpressorasAction.findFilialUsuario",
			"setFilial", 
			new Array()));
			
		xmit(false);	
	}
	
	function filialOnPopUpSetValue(data, error){
		
		setFilial_cb(data);
		validaFilialUsuario();
		
	}
	
</script>