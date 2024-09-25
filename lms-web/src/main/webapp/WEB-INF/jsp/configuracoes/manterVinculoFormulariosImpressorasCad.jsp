<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script>
	
	/**  
	  * Função chamada no load da  página
	  */
	function myOnPageLoad(){
		onPageLoad(); 
		setDisabled("controleFormulario.controleForm", true);
	}
</script>
<adsm:window service="lms.configuracoes.impressoraFormularioService" onPageLoadCallBack="impressoraFormularioLoad" onPageLoad="myOnPageLoad">
	<adsm:form action="/configuracoes/manterVinculoFormulariosImpressoras" idProperty="idImpressoraFormulario" onDataLoadCallBack="formVinculoImp">

		<adsm:hidden property="sgFilialUsuario" serializable="true"/>
		
		<%-- Para controle da Impressora quando vier da listagem (veja JS) --%>
		<adsm:hidden property="_idImpressora" serializable="false"/>
		<adsm:hidden property="tpSituacaoAtivo" value="A" serializable="false"/>

		<adsm:hidden property="nrSeloFiscalInicialControleFormulario" serializable="false"/>
		<adsm:hidden property="nrSeloFiscalFinalControleFormulario" serializable="false"/>
		
	   	<adsm:lookup label="controleFormulario" property="controleFormulario" service=""
	   		width="27%" labelWidth="18%"
			idProperty="idControleFormulario" criteriaProperty="controleForm"
			dataType="text" action="/configuracoes/manterControleFormularios" 
			onPopupSetValue="findImpressoraByControleForm" required="true">
				<adsm:propertyMapping modelProperty="cdSerie" relatedProperty="cdSerie"/>
				<adsm:propertyMapping modelProperty="nrFormularioInicial" relatedProperty="controleFormulario.nrFormularioInicial"/>
				<adsm:propertyMapping modelProperty="nrFormularioFinal" relatedProperty="controleFormulario.nrFormularioFinal"/>
				<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="controleFormulario.filial.idFilial"/>
				<adsm:propertyMapping criteriaProperty="tpSituacaoAtivo" modelProperty="tpSituacaoFormulario"/>
		</adsm:lookup>
		<adsm:hidden property="controleFormulario.filial.idFilial" serializable="false"/>
		<adsm:hidden property="controleFormulario.nrFormularioInicial" serializable="false"/>
		<adsm:hidden property="controleFormulario.nrFormularioFinal" serializable="false"/>
		
		<adsm:combobox label="impressora" property="impressora.idImpressora" onlyActiveValues="true"
			width="35%" labelWidth="20%" autoLoad="false"
			service="lms.expedicao.impressoraService.findCombo" required="true" style="width:240px"
			optionProperty="idImpressora" optionLabelProperty="checkinLocalizacao" >
		</adsm:combobox>
		
		<adsm:range label="formularios" required="true" width="27%" labelWidth="18%">
			<adsm:textbox property="nrFormularioInicial" dataType="integer" 
				size="10" maxLength="10" required="true" />
			<adsm:textbox property="nrFormularioFinal" dataType="integer" 
				size="10" maxLength="10" required="true" />
		</adsm:range>

		<adsm:textbox label="ultimoFormularioImpresso" property="nrUltimoFormulario"
			width="35%" labelWidth="20%"
			dataType="integer" maxLength="15" required="true" />
		
		<adsm:range label="selosFiscais" required="false" width="27%" labelWidth="18%">
			<adsm:textbox property="nrSeloFiscalInicial" dataType="integer" 
				size="10" maxLength="10" required="false" />
			<adsm:textbox property="nrSeloFiscalFinal" dataType="integer" 
				size="10" maxLength="10" required="false" />
		</adsm:range>
		
		<adsm:textbox label="ultimoSeloImpresso" property="nrUltimoSeloFiscal"
			width="35%" labelWidth="20%"
			dataType="integer" maxLength="15"/>
			
		<adsm:textbox label="serie" property="cdSerie" dataType="text" disabled="true" maxLength="5" width="27%" labelWidth="18%"/>	
		
		<adsm:buttonBar>
			<adsm:button caption="salvar" onclick="vinculoFormularioSalvar(this);" buttonType="storeButton" disabled="false" id="store"/>
			<adsm:newButton id="btnLimpar"/>
			<adsm:removeButton id="remove"/>
		</adsm:buttonBar>

	</adsm:form>
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-27025"/>
		<adsm:include key="LMS-27026"/>
		<adsm:include key="LMS-27027"/>
		<adsm:include key="LMS-27057"/>
		<adsm:include key="LMS-27076"/>
		<adsm:include key="LMS-27059"/>
		<adsm:include key="LMS-27060"/>
		<adsm:include key="LMS-27074"/>
	</adsm:i18nLabels>
	
</adsm:window>

<script>

	/**
	 *   Verifica se o intervalo dos Formulários informados está contido no intervalo de Formulários do 
	 *   Controle de Estoque selecionado.
	 *   @author Robson Edemar Gehl
	 */
	function validarIntervaloFormulario(){
		var nrFormularioInicial 	= Number(getElementValue("nrFormularioInicial"));
		var nrFormularioFinal 		= Number(getElementValue("nrFormularioFinal"));
		var nrCrtlFormInicial 		= Number(getElementValue("controleFormulario.nrFormularioInicial"));
		var nrCrtlFormFinal 		= Number(getElementValue("controleFormulario.nrFormularioFinal"));

		var validacao =(nrFormularioInicial >= nrCrtlFormInicial) && (nrFormularioFinal <= nrCrtlFormFinal);

		if ( !validacao ){
			alert(i18NLabel.getLabel("LMS-27025"));
			setFocus("nrFormularioInicial", false);
			return false;
		}
		
		return true;
	}
	
	/**
	  * Valida o intervalo de boletos de acordo com as  regras da especificação
	  */
	function validaIntervaloSelos(){
		var retorno = true;
		
		// Caso seja informado selo inicial e não seja informado o final
		if(getElementValue("nrSeloFiscalInicial") != '' && getElementValue("nrSeloFiscalFinal") == ''){
			alert(i18NLabel.getLabel("LMS-27059"));
			setFocus("nrSeloFiscalInicial", false);
			retorno = false;
			
		// Caso não seja informado selo inicial e seja informado o selo final
		}else if(getElementValue("nrSeloFiscalInicial") == '' && getElementValue("nrSeloFiscalFinal") != ''){
			alert(i18NLabel.getLabel("LMS-27060"));
			setFocus("nrSeloFiscalInicial", false);
			retorno = false;
		
		// Caso tenha sido informado um intervalo de selos, valida se o intervalo de selos e o de formulários formulários são iguais
		}else if( getElementValue("nrSeloFiscalInicial") != '' ){
			var countNrformularios = parseInt(getElementValue("nrFormularioFinal"),10) - parseInt(getElementValue("nrFormularioInicial"),10) + 1;
			var countNrSelos = parseInt(getElementValue("nrSeloFiscalFinal"),10) - parseInt(getElementValue("nrSeloFiscalInicial"),10) + 1;
			
			// Caso os intervalos não tenham o mesmo tamanho, lança a exceção
			if(parseInt(countNrformularios,10) != parseInt(countNrSelos,10)){
				alert(i18NLabel.getLabel("LMS-27074"));
				setFocus("nrSeloFiscalInicial", false);
				retorno = false;
			}
		}
		
		return retorno;
	}
	
	
	/**
	 *   Verifica se o Selo Fiscal está contido no intervalo de Selos Fiscais do 
	 *   Controle de Estoque selecionado.<BR>
	 *   Se o Selo fiscal não for informado, retorna true, não fazendo a validação.
	 *   @author Robson Edemar Gehl
	 */
	function validarSeloFiscal(){
		var retorno = true;
		
		var nrSeloFiscalInicial 	= parseInt(getElementValue("nrSeloFiscalInicial"),10);
		var nrSeloFiscalFinal 		= parseInt(getElementValue("nrSeloFiscalFinal"),10);
		
		var nrSeloInicialFormulario = parseInt(getElementValue("nrSeloFiscalInicialControleFormulario"),10);
		var nrSeloFinalFormulario = parseInt(getElementValue("nrSeloFiscalFinalControleFormulario"),10);

	    // Caso seja preenchido o intervalo de selos no contrlole de formulário, o usuário deve informar um intervalo de selos para a impressora.
		if (getElementValue("nrSeloFiscalInicialControleFormulario").trim() != "" &&  (getElementValue("nrSeloFiscalInicial").trim() == "" || getElementValue("nrSeloFiscalFinal").trim() == "" || getElementValue("nrUltimoSeloFiscal").trim() == "")){
			alert(i18NLabel.getLabel("LMS-27076"));
			setFocus("nrSeloFiscalInicial", false);
			retorno = false;
		
		// Após todas validações, valida os intervalo de selos
		}else if(getElementValue("nrSeloFiscalInicialControleFormulario").trim() != ""){
		
			// Caso o intervalo de selos da impressora esteja fora do intervalo de selos do controle de formulário
			if ( nrSeloFiscalInicial < nrSeloInicialFormulario || nrSeloFiscalFinal > nrSeloFinalFormulario ){
				alert(i18NLabel.getLabel("LMS-27026"));
				setFocus("nrSeloFiscalInicial", false);
				retorno = false;
			}
			
		}
		
		return retorno;
	}
	
	/**
	 *   Verifica se o Número da Última Impressão está contido no intervalo de 
	 *   Formulários informado.
	 *   @author Robson Edemar Gehl
	 */
	function validarUltimaImpressao(){
		var nrUltimaImpressao 		= getElementValue("nrUltimoFormulario");
		var nrFormularioInicial 	= getElementValue("nrFormularioInicial");
		var nrFormularioFinal 		= getElementValue("nrFormularioFinal");

		var validacao = ( ((Number(nrUltimaImpressao) >= Number(nrFormularioInicial)) && (Number(nrUltimaImpressao) <= Number(nrFormularioFinal))) || ((Number(nrFormularioInicial)-1) == Number(nrUltimaImpressao)) );
		if ( !validacao ){
			alert(i18NLabel.getLabel("LMS-27027"));
			setFocus("nrUltimoFormulario", false);
			return false;
		}
		return true;
	}
	
	/**
	 *	Verifica se o Número do último selo impresso está contido no intervalo de 
	 *  selos informado.
	 *  @author Robson Edemar Gehl
	 */
	function validarUltimoSeloImpresso(){
		var nrUltimoSeloImpresso 		= getElementValue("nrUltimoSeloFiscal");
		var nrSeloFiscalInicial 	= getElementValue("nrSeloFiscalInicial");
		var nrSeloFiscalFinal 		= getElementValue("nrSeloFiscalFinal");

		var validacao = ( ((Number(nrUltimoSeloImpresso) >= Number(nrSeloFiscalInicial)) && (Number(nrUltimoSeloImpresso) <= Number(nrSeloFiscalFinal))) || ((Number(nrSeloFiscalInicial)-1) == Number(nrUltimoSeloImpresso)) );
		if ( !validacao ){
			alert(i18NLabel.getLabel("LMS-27057"));
			setFocus("nrUltimoSeloFiscal", false);
			return false;
		}
		return true;
	}
	
	/**
	  * CallBack do store
	  */
	function salvarVinculo_cb(data,erro){
		store_cb(data,erro);
		
		if( erro != undefined ){		
			setFocusLupaByLookupControleFormulario();
		} else {
			setFocus('btnLimpar',true,true);
		}
	}
	
	/**
	 *	Ao salvar Vínculo dos Formulários
	 *	@author Robson Edemar Gehl
	 */
	function vinculoFormularioSalvar(elem){
	
		var eForm = elem.document;
		
		if(validateTabScript(eForm.forms[0])){
			
			if (!validarIntervaloFormulario()) return false;
			
			if (!validarSeloFiscal()) return false;
			
			if(!validaIntervaloSelos()) return false;
			
			if (!validarUltimaImpressao()) return false;
			
			if(!validarUltimoSeloImpresso()) return false;
		
			storeButtonScript('lms.configuracoes.impressoraFormularioService.store', 'salvarVinculo', eForm.forms[0]);
			
		}
	}


	/**
	 * Função de callBack da página
	 */
	function impressoraFormularioLoad_cb(data,errors){
		onPageLoad_cb();
		
		//Carrega as Impressoras
		if (getElementValue("idImpressoraFormulario") == ""){
			//carregaImpressorasFilial();
		}

	}
	
	/** 
	  * (OnPopSetValue lookuk controleFormulario) Busca impressoras de acordo com a filial do controle de formulário
	  */
	function findImpressoraByControleForm(data) {
	
   	   // Seta nos hidens o intervalo de selos fiscais do controle de formulário
	   setNrSeloFiscalControleFormulario(data.nrSeloFiscalInicial, data.nrSeloFiscalFinal);

	   validaFilialUsuario(getElementValue("sgFilialUsuario"), data.controleForm);
	   
	   carregaImpressorasFilial(data);
	   return true;
	}
	
	/**
	  * Seta nos hidens o intervalo de selos fiscais do controle de formulário
	  */
	function setNrSeloFiscalControleFormulario(nrSeloFiscalInicialControleFormulario, nrSeloFiscalFinalControleFormulario){
		setElementValue("nrSeloFiscalInicialControleFormulario", nrSeloFiscalInicialControleFormulario);
	    setElementValue("nrSeloFiscalFinalControleFormulario", nrSeloFiscalFinalControleFormulario);
	}
	
	/**
	  * CallBack da função carregaImpressorasFilial() 
	  */
	function impressora_cb(data,erro){
		impressora_idImpressora_cb(data,erro);
		var idImpressora = getElementValue(document.getElementById("_idImpressora"));
		if (idImpressora != undefined){
			setElementValue( document.getElementById("impressora.idImpressora"), idImpressora );
		}
	}
	
	/**
	  * Busca as impressoras de acordo com a filial do controle de formulário para popular a comboBox
	  */
	function carregaImpressorasFilial(data) {
	    var idFilial;
		if (data == undefined){
			idFilial = getElementValue("controleFormulario.filial.idFilial");
		}else{
			//Vem da popUp de controle de formulário?
			idFilial = getNestedBeanPropertyValue(data, "filial.idFilial");
			if (idFilial == undefined){
				//Não, vem da listagem! A raiz do XML é impressora.
				idFilial = getNestedBeanPropertyValue(data, "controleFormulario.filial.idFilial");
				//Para controle de seleção -- filtra por filial, selecionando id correto no CB da Impressora.
				idImpressora = getNestedBeanPropertyValue(data, "impressora.idImpressora");
				if (idImpressora != undefined){
					setElementValue(document.getElementById("_idImpressora"), idImpressora);
				}
			}

		}
		
		var dataCombo = new Array();
		setNestedBeanPropertyValue(dataCombo, "filial.idFilial", idFilial);
		var sdo = createServiceDataObject(document.getElementById("impressora.idImpressora").service,
			"impressora", dataCombo);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	  * Função de callBack do formulário
	  */
	function formVinculoImp_cb(data,errors){
		onDataLoad_cb(data,errors);
		
		// Seta nos hidens o intervalo de selos fiscais do controle de formulário
	    setNrSeloFiscalControleFormulario(data.controleFormulario.nrSeloFiscalInicial, data.controleFormulario.nrSeloFiscalFinal);
	   
		// Busca a filial do usuário e valida se a filial do usuario e a filial do controle de formulario são iguais 
		findFilialUsuario();
		
		//Carrega as Impressoras
		if (getElementValue("idImpressoraFormulario") != ""){
			carregaImpressorasFilial(data);
		}
		
		setFocusLupaByLookupControleFormulario();
	}

	/** 
	  *	Busca a filial do usuario da sessão 
	  */
	function findFilialUsuario(){
		_serviceDataObjects = new Array();
	
		addServiceDataObject(createServiceDataObject("lms.configuracoes.manterVinculoFormulariosImpressorasAction.findFilialUsuario",
			"findFilialUsuario", 
			new Array()));
	
		xmit(false);	
	}
	
	/** 
	  *	CallBack da faunção findFilialUsuario 
	  */
	function findFilialUsuario_cb(data, error) {
		setElementValue("sgFilialUsuario", data.sgFilial);
		validaFilialUsuario(data.sgFilial, getElementValue("controleFormulario.controleForm"));
	}
	
	/** 
	  * Valida se a filial do usuario e a filial do controle de formulario são iguais 
	  */
	function validaFilialUsuario(sgFilialUsuario, sgFilialControleFormulario){
		if( sgFilialControleFormulario != "" && sgFilialUsuario != sgFilialControleFormulario.substring(0, 3) ){
			setDisabled("remove", true);
			setDisabled("store", true);
		}else{
			if(sgFilialControleFormulario != "" && getElementValue("idImpressoraFormulario") != "")
				setDisabled("remove", false);
			setDisabled("store", false);
		}
	}
	
	/**
	  * Função chamada no início da página
	  */
	function initWindow(eventObj){
		// Busca a filial do usuário e valida se a filial do usuario e a filial do controle de formulario são iguais 
		findFilialUsuario();
		
		// Caso o evento não seja store, coloca o foco na lupa do controle de formulario
		if(eventObj.name != "storeButton"){
			setFocusLupaByLookupControleFormulario();
		}
		
		// Caso o evento não seja store, e não seja gridRowClick, busca todas impressoras
		if(eventObj.name != "gridRow_click" && eventObj.name != "storeButton"){
			_serviceDataObjects = new Array();
			
			
			var criteria = new Array();
			
			/** Quando existe uma filial do controlede formulário, busca as impressoras desta filial */
			if(getElementValue("controleFormulario.filial.idFilial") != ""){
				setNestedBeanPropertyValue(criteria, "filial.idFilial", getElementValue("controleFormulario.filial.idFilial"));
			}
			
			addServiceDataObject(createServiceDataObject("lms.expedicao.impressoraService.findCombo",
				"impressora_idImpressora", 
				criteria));
		
			xmit(false);	
		}
		
		return true;
	}
	
	function setFocusLupaByLookupControleFormulario(){
		if( getElement('controleFormulario.idControleFormulario').masterLink != 'true' ){
			setFocus("controleFormulario_lupa", false);
		} else {
			setFocusOnFirstFocusableField(document);
		}
	}
	
	
</script>
