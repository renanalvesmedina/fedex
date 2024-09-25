<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	//Carrega dados do usuario
	function loadDataObjectsCad() {	
		onPageLoad(); 
		var data = new Array();	
		var sdo = createServiceDataObject("lms.vendas.manterPipelineClienteAction.getBasicData", "dataSession", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function inicializaTelaCad_cb(){
		onPageLoad_cb();
	}
	
	function validaMesAnoFechamentoAtualizado() {
		
		var strDtFechamento = getElementValue("mesAnoFechamento");
		var strDtFechamentoAtualizado = getElementValue("mesAnoFechamentoAtualizado");
		
		if(strDtFechamento == '' || strDtFechamentoAtualizado == '') {
			return false;			
		}
		
		var dtFechamento = mockToDate(stringToDate(strDtFechamento,"yyyy-MM-dd"));
		var dtFechamentoAtualizado = mockToDate(stringToDate(strDtFechamentoAtualizado,"yyyy-MM-dd"));
		
		if (dtFechamento >= dtFechamentoAtualizado){
			alertI18nMessage('LMS-01210');
			setFocusDateField('mesAnoFechamentoAtualizado');
		}
		
	}
	
	function validaMesAnoFechamento() {
		var strDtFechamento = getElementValue("mesAnoFechamento");
		if(strDtFechamento == '') {
			return false;
		}
		var today = zeraData(new Date());
		var mockDate = mockToDate(stringToDate(strDtFechamento, "yyyy-MM-dd"));
		
		if (today > mockDate){
			alertI18nMessage('LMS-01209');
			setFocusDateField('mesAnoFechamento');
		}
	}
	
	function zeraData(d){
		d.setDate(1);
		d.setHours(0);
		d.setMinutes(0);
		d.setSeconds(0);
		d.setMilliseconds(0);
		return d;
	}
	
	function mockToDate(mockDate) {
		var result = zeraData(new Date());
		var year = mockDate.getFullYear();
		var month = mockDate.getMonth();
		result.setFullYear(year);
		result.setMonth(month)
		return result;
	}
	
	function setFocusDateField(fieldId) {
		var fieldElement = document.getElementById(fieldId);
		if(fieldElement.disabled || fieldElement.readOnly) {
			return false;
		}
		setTimeout(function(){fieldElement.focus()}, 100);
	}
	
</script>
<adsm:window service="lms.vendas.manterPipelineClienteAction" onPageLoad="loadDataObjectsCad" >

<adsm:form action="/vendas/manterPipelineCliente" idProperty="idPipelineCliente" service="lms.vendas.manterPipelineClienteAction.findByIdCustom" onDataLoadCallBack="pipelineClienteDataLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01210"/>
		<adsm:include key="LMS-01209"/>
		<adsm:include key="LMS-01219"/>
	</adsm:i18nLabels>

	<adsm:hidden property="idGerente" serializable="false"/>
	<adsm:hidden property="filial.idFilial"/>
	<adsm:hidden property="dtAtual" serializable="false"/>
	
	<adsm:textbox
		label="filial"
		dataType="text"
		property="filial.sgFilial"
		size="3"
		maxLength="3"
		required="true"
		disabled="true"
		serializable="false"
		labelWidth="10%"
		width="45%">
		<adsm:textbox
			dataType="text"
			property="filial.pessoa.nmFantasia"
			size="25"
			maxLength="60"
			disabled="true"
			serializable="false"
		/>
	</adsm:textbox>

	<adsm:hidden property="idRegional" serializable="false" />
	<adsm:textbox
		dataType="text" 
		label="regional"
		property="siglaDescricao" 
		disabled="true"
		size="35"
		maxLength="60"
		width="30%"
		labelWidth="8%"
		serializable="false"
		required="true"
	/>

	<adsm:lookup
		action="/configuracoes/consultarFuncionariosView"
		service="lms.vendas.manterPipelineClienteAction.findLookupFuncionario"
		dataType="text"
		required="true"
		disabled="true"
		property="usuarioByIdUsuario"
		idProperty="idUsuario"
		criteriaProperty="nrMatricula"
		label="vendedor"
		size="17"
		maxLength="10"
		exactMatch="true"
		width="45%"
		labelWidth="10%"
		onDataLoadCallBack="funcCallBack"
		onPopupSetValue="funcCallBack"
	>
		<adsm:propertyMapping
			relatedProperty="usuarioByIdUsuario.nmUsuario"
			modelProperty="nmUsuario"/>
		<adsm:propertyMapping
			relatedProperty="usuarioByIdUsuario.vfuncionario.dsFuncao"
			modelProperty="dsFuncao"/>
		<adsm:textbox
			dataType="text" 
			property="usuarioByIdUsuario.nmUsuario" 
			size="35" 
			maxLength="50" 
			disabled="true"
			serializable="false"/>
		</adsm:lookup>

		<adsm:textbox
			dataType="text" 
			required="true"
			label="cargo"
			property="usuarioByIdUsuario.vfuncionario.dsFuncao" 
			maxLength="30" 
			size="35"
			disabled="true" 
			width="30%"
			labelWidth="8%"
			serializable="false"/>

		<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			service="lms.vendas.manterPipelineClienteAction.findLookupCliente" 
			required="false"
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="cliente" 
			size="17" 
			maxLength="18"
			dataType="text"
			width="45%"
			labelWidth="10%" onDataLoadCallBack="clienteCallBack" afterPopupSetValue="clienteCallBack" onchange="return changeCliente();">
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping
				relatedProperty="cliente.tpCliente" 
				modelProperty="tpCliente"/>
			
			<adsm:propertyMapping
				relatedProperty="clienteByIdClienteRespnsavel.nrIdenticacao" 
				modelProperty="cliente.cliente.pessoa.nrIdentificacao"/>
				
			<adsm:propertyMapping
				relatedProperty="clienteByIdClienteRespnsavel.nmPessoa" 
				modelProperty="cliente.cliente.pessoa.nmPessoa"/>
				
			<adsm:textbox
				dataType="text" 
				property="cliente.pessoa.nmPessoa" 
				size="35" 
				maxLength="50"
				disabled="false" 
				serializable="true" required="true"/>
		</adsm:lookup>
		
		<adsm:textbox label="responsavel" property="clienteByIdClienteRespnsavel.nrIdenticacao" dataType="text" 
					  labelWidth="10%" width="45%" disabled="true">
			<adsm:textbox property="clienteByIdClienteRespnsavel.nmPessoa"
						  dataType="text" maxLength="25" size="25" disabled="true"/>
		</adsm:textbox>
		
		<adsm:combobox
			property="cliente.tpCliente" 
			label="tipoCliente"
			domain="DM_TIPO_CLIENTE"
			onlyActiveValues="true"
			required="true"
			disabled="true"
			labelWidth="12%"
			width="26%"
			serializable="true"/>
			
		<adsm:combobox
			property="tpNegociacao" 
			label="tipoDeNegociacao"
			domain="DM_NEGOCIACAO_PIPELINE"
			onlyActiveValues="true"
			disabled="true"
			labelWidth="10%"
			width="43%"
			required="true"/>

	   	<adsm:combobox property="moeda.idMoeda" 
			label="receitaMensalAtual"
			optionLabelProperty="siglaSimbolo"
			optionProperty="idMoeda" 
			service="lms.vendas.manterPipelineClienteAction.findComboMoeda"
			labelWidth="10%" 
			width="31%" cellStyle="vertical-align:bottom">
			<adsm:textbox dataType="currency" mask="###,###,###,##0.00" required="false" property="vlReceitaAtual" size="15" disabled="false" cellStyle="vertical-align:bottom" serializable="true" />
		</adsm:combobox>
		
		<adsm:combobox property="segmentoMercado.idSegmentoMercado" optionLabelProperty="dsSegmentoMercado" optionProperty="idSegmentoMercado"
				label="ramoAtuacao" service="lms.vendas.segmentoMercadoService.find" labelWidth="10%" width="85%" boxWidth="172" required="true" />
		
		<adsm:combobox
			property="tpProbabilidade" 
			label="probabilidade"
			domain="DM_PROBABILIDADE_PIPELINE"
			onlyActiveValues="true"
			disabled="false"
			labelWidth="10%"
			required="true"
			width="80%"/>
		
		<adsm:textbox label="nrMesFechamentoNegocio" width="15%" labelWidth="30%" 
			dataType="monthYear" property="mesAnoFechamento" onchange="validaMesAnoFechamento();"/>
    	<adsm:textbox label="nrMesAtualizadoFechamentoNegocio" width="15%" labelWidth="30%" 
    		dataType="monthYear" property="mesAnoFechamentoAtualizado" onchange="validaMesAnoFechamentoAtualizado();"/>
		
		<adsm:combobox
			property="tpSituacao" 
			label="situacao"
			domain="DM_SITUACAO_PIPELINE"
			disabled="true"
			labelWidth="8%"
			width="31%"
			serializable="true" required="true"/>
			
		 <adsm:section caption="potencialReceita"/>
		 	<adsm:label key="abrangencia" width="13%" style="border:none;"/>
		 	<adsm:label key="modal" width="12%" style="border:none;"/>
		 	<adsm:label key="valorPotencialReceita" width="25%" style="border:none;"/>
		 	<adsm:label key="concorrente1" width="20%" style="border:none;"/>
		 	<adsm:label key="concorrente2" width="20%" style="border:none;"/>
		 	
		 	<adsm:hidden property="idPipelineReceita1" serializable="true"/>
			<adsm:combobox property="tpAbrangencia1"  domain="DM_ABRANGENCIA" disabled="false"  width="13%" serializable="true"/>
			<adsm:combobox property="tpModal1"  domain="DM_MODAL" disabled="false"  width="12%" serializable="true"/>
			<adsm:combobox property="moeda1.idMoeda"  optionLabelProperty="siglaSimbolo" optionProperty="idMoeda" service="lms.vendas.manterPipelineClienteAction.findComboMoeda"  width="25%">
				<adsm:textbox dataType="currency" mask="###,###,###,##0.00" property="vlReceitaAtual1" size="15" disabled="false" serializable="true" onchange="return setaVlReceitaTotal()" />
			</adsm:combobox>
		    <adsm:textbox   width="20%" dataType="text" property="dsConcorrente11" size="30" disabled="false" serializable="true" maxLength="50"/>
		    <adsm:textbox   width="30%" dataType="text" property="dsConcorrente21" size="30" disabled="false" serializable="true" maxLength="50"/>
		 
		  <adsm:label key="branco" width="13%" style="border:none;"/>
		  <adsm:combobox label="receitaTotal" labelWidth="12%" property="moeda5.idMoeda"  optionLabelProperty="siglaSimbolo" optionProperty="idMoeda" service="lms.vendas.manterPipelineClienteAction.findComboMoeda" width="25%">
			<adsm:textbox dataType="currency" mask="###,###,###,###0.00" property="vlReceitaPrevista" size="15" disabled="true" serializable="true"/>
		  </adsm:combobox>
		  
		  
		  <adsm:buttonBar>
			<adsm:storeButton id="storeButton" callbackProperty="afterStore" />
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>		
	</adsm:form>
</adsm:window>

<script type="text/javascript">

		
	/*DADOS BÁSICOS DA TELA*/
	var msgException = null;
	var teveException = null;

	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	var idRegional = null;
	var siglaRegional = null;

	var idUsuario = null;
	var nrMatricula = null;
	var nmUsuario = null;
	var dsFuncaoUsuario = null;

	var idUsuarioVisto = null;
	var nmUsuarioVisto = null;	
	
	var idMoedaUsuario = null;

	var isGerente = null;
	var changeFilial = null;	
	var dtAtualSession = null;	

	function dataSession_cb(data,error) {
		teveException = getNestedBeanPropertyValue(data, "teveException");

		idFilial = getNestedBeanPropertyValue(data, "filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data, "filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia");

		idRegional = getNestedBeanPropertyValue(data, "regional.idRegional");
		siglaRegional = getNestedBeanPropertyValue(data, "regional.siglaDescricao");

		idUsuario = getNestedBeanPropertyValue(data, "usuario.idUsuario");
		nmUsuario = getNestedBeanPropertyValue(data, "usuario.nmUsuario");
		nrMatricula = getNestedBeanPropertyValue(data, "usuario.nrMatricula");
		dsFuncaoUsuario = getNestedBeanPropertyValue(data, "usuario.dsFuncao");		

		idMoedaUsuario = getNestedBeanPropertyValue(data, "idMoedaUsuario");	

		idUsuarioVisto = getNestedBeanPropertyValue(data, "usuarioByIdUsuarioVisto.idUsuario");
		nmUsuarioVisto = getNestedBeanPropertyValue(data, "usuarioByIdUsuarioVisto.nmUsuario");

		isGerente = getNestedBeanPropertyValue(data, "usuarioByIdUsuarioVisto.isGerente");
		changeFilial = getNestedBeanPropertyValue(data, "usuarioByIdUsuarioVisto.changeFilial");
		
		dtAtualSession = getNestedBeanPropertyValue(data, "dtAtual");
	}
	
	function setaVlReceitaTotal() {
		var total = stringToNumber("0");
		if(getElementValue("vlReceitaAtual1")!= "")
			total = total + stringToNumber(getElementValue("vlReceitaAtual1"));

		setFormattedValue("vlReceitaPrevista", total);

		return true;
	}
	
		
	function habilitaNmCliente(){
		setDisabled("cliente.pessoa.nmPessoa", false);
		return true;
	}

	function habilitaDatasFechamento(dtFechamento, dtFechamentoAtualizado) {
		setDisabled("mesAnoFechamento", dtFechamento);
		setDisabled("mesAnoFechamentoAtualizado", dtFechamentoAtualizado);
	}

	function setFormattedValue(field, value) {
		setElementValue(field, setFormat(field, value.toString()));
	}

	function initWindow(eventObj) {
		if(eventObj.name == "tab_click"){
				if(getElementValue("idPipelineCliente") == "" || getElementValue("idPipelineCliente") == null){
					writeDataSession();
					habilitaDesabilitaCampos();
					setDisabled("removeButton", true);
				habilitaDatasFechamento(false, true);
				document.getElementById("mesAnoFechamento").required='true';
				} else{
					var tabGroup = getTabGroup(this.document);	
					var tpSituacao = tabGroup.getTab("etapas").getElementById("tpSituacao").value;
				habilitaDatasFechamento(true, false);
				document.getElementById("mesAnoFechamento").required='false';
					if(tpSituacao == 'P' || tpSituacao == 'F'){
						setElementValue("tpSituacao", tpSituacao);
						setDisabled(document, true);
					}
					setDisabled("removeButton", false);
					setDisabled("newButton", false);
				
				}
	   }else if (eventObj.name == "removeButton" || eventObj.name == "newButton_click") {
			writeDataSession();
			habilitaDesabilitaCampos();
			habilitaDatasFechamento(false, true);
			
			var tabGroup = getTabGroup(this.document);
			tabGroup.getTab("etapas").getElementById("consultou").value = '';
			tabGroup.setDisabledTab("etapas", true);
			tabGroup.setDisabledTab("propostasTabelas", true);
			
			setDisabled("removeButton", true);
		}
	}
	
	
	function verificaCamposReceitaObrigatorios(){
		for(var i=1; i <= 1; i++) {
			if(getElementValue("dsConcorrente1"+i) != '' || getElementValue("dsConcorrente2"+i)!= ''){
				if(getElementValue("tpAbrangencia"+i) == '' || getElementValue("tpModal"+i)== '' || getElementValue("vlReceitaAtual"+i)== ''){
					alertI18nMessage("LMS-01053");
					break;
				}
			}
		}
	}
	
	/***** SALVAR *****/
	function afterStore_cb(data, error, errorKey) {
		store_cb(data,error);
		if(error == undefined){
			setElementValue("idPipelineCliente", data.idPipelineCliente);
			configuraEtapas(false);
			configuraPropostasTabelas(false);
			setDisabled("removeButton", false);
			habilitaDatasFechamento(true, false);
			setFocusOnFirstFocusableField();
			
			var tabGroup = getTabGroup(this.document).selectTab("etapas", "etapas", true);
			alertI18nMessage('LMS-01219');
		}else{
			if(errorKey == 'LMS-01209') {
				setFocusDateField('mesAnoFechamento');
			} else if(errorKey == 'LMS-01210') {
				setFocusDateField('mesAnoFechamentoAtualizado');
			} else if(errorKey != 'LMS-01055'){
				setFocus(document.getElementById("tpAbrangencia1"));
			}else{
				setFocus(document.getElementById("percProbabilidade"));	
			}	
		}
	}
	
	function pipelineClienteDataLoad_cb(data, error){
		onDataLoad_cb(data, error);
		setElementValue("cliente.pessoa.nrIdentificacao", getNestedBeanPropertyValue(data, "cliente.pessoa.nrIdentificacao" ));
		setElementValue("dtAtual", getNestedBeanPropertyValue(data, "dtAtual" ));
		configuraEtapas(false);
		configuraPropostasTabelas(false);
		var situacao = getNestedBeanPropertyValue(data, "tpSituacao" );
		if(situacao == 'F' || situacao == 'P' || situacao == 'C'){
			setDisabled(document, true);
			configuraPropostasTabelas(true);
		}else{
			habilitaDesabilitaCampos();
			habilitaDatasFechamento(true, false);
		}
	}
	
	function habilitaDesabilitaCampos(){
		setDisabled(document, false);
		desabilitaCampos();
	}
	
		
	//copia valores para a aba etapas
	function configuraEtapas(disabled){
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("etapas", disabled);
	}	
	
	function configuraPropostasTabelas(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("propostasTabelas", disabled);
	}
	
	/**
	 * Preenche os dados basicos da tela
	 */
	function writeDataSession() {
		setElementValue("filial.idFilial", idFilial);
		setElementValue("filial.sgFilial", sgFilial);
		setElementValue("filial.pessoa.nmFantasia", nmFilial);
		setElementValue("dtAtual", dtAtualSession);
		setElementValue("idRegional", idRegional);
		setElementValue("siglaDescricao", siglaRegional);
		setElementValue("tpSituacao", 'E');
		setElementValue("moeda.idMoeda", idMoedaUsuario);
		setElementValue("moeda1.idMoeda", idMoedaUsuario);
		setElementValue("moeda5.idMoeda", idMoedaUsuario);
		desabilitaCampos();
		
		//se o usuario nao é o gerente comercial da filial
		if(idUsuario != idUsuarioVisto) {
			writeDataUsuario();
		}
	}
	
	function desabilitaCampos(){
		setDisabled("moeda.idMoeda", true);
		setDisabled("moeda1.idMoeda", true);
		setDisabled("moeda5.idMoeda", true);
		setDisabled("filial.idFilial", true);
		setDisabled("filial.sgFilial", true);
		setDisabled("filial.pessoa.nmFantasia", true);
		setDisabled("usuarioByIdUsuario.idUsuario", true);
		setDisabled("siglaDescricao", true);
		setDisabled("cliente.tpCliente", true);
		setDisabled("clienteByIdClienteRespnsavel.nrIdenticacao", true);
		setDisabled("clienteByIdClienteRespnsavel.nmPessoa", true);
		if(getElementValue("cliente.idCliente")== ''){
			setDisabled("cliente.pessoa.nmPessoa", false);
			
		}else{	
			setDisabled("cliente.pessoa.nmPessoa", true);
		}	
			
		setDisabled("usuarioByIdUsuario.nmUsuario", true);
		setDisabled("usuarioByIdUsuario.vfuncionario.dsFuncao", true);
		setDisabled("tpSituacao", true);
		setDisabled("vlReceitaPrevista", true);
	}

	/**
	 * Preenche os dados do usuário
	 */
	function writeDataUsuario(){
		setElementValue("usuarioByIdUsuario.idUsuario", idUsuario);
		setElementValue("usuarioByIdUsuario.nrMatricula", nrMatricula);
		setElementValue("usuarioByIdUsuario.nmUsuario", nmUsuario);
		setElementValue("usuarioByIdUsuario.vfuncionario.dsFuncao", dsFuncaoUsuario);
	}	
	function clienteCallBack_cb(data){
		if(!cliente_pessoa_nrIdentificacao_exactMatch_cb(data))
			return;
		if(data[0] != undefined){	
			setDisabled("cliente.pessoa.nmPessoa", "true");
		}
		return;
	}
	
	function clienteCallBack(data){
		if(data!= undefined){	
			var sdo = createServiceDataObject("lms.vendas.manterPipelineClienteAction.getDadosReponsavelCliente", "getDadosReponsavelCliente", {idCliente:data.idCliente});
			xmit({serviceDataObjects:[sdo]});
			setDisabled("cliente.pessoa.nmPessoa", "true");
		}
		return;
	}
	
	function getDadosReponsavelCliente_cb(data, error) {
		setElementValue("clienteByIdClienteRespnsavel.nrIdenticacao", getNestedBeanPropertyValue(data, "clienteByIdClienteRespnsavel.nrIdentificacao"));
		setElementValue("clienteByIdClienteRespnsavel.nmPessoa", getNestedBeanPropertyValue(data, "clienteByIdClienteRespnsavel.nmPessoa"));
	}
	
	function changeCliente() {
		if (getElementValue("cliente.pessoa.nrIdentificacao") == "") {
			setDisabled("cliente.pessoa.nmPessoa", false);
		}
		return cliente_pessoa_nrIdentificacaoOnChangeHandler();
	}

	/***** Lookup Funcionario *****/
	function funcCallBack_cb(data){
		if(!usuarioByIdUsuario_nrMatricula_exactMatch_cb(data))
			return;
		if(data[0] != undefined) {
			setElementValue("usuarioByIdUsuario.idUsuario", data[0].idUsuario);
			setElementValue("usuarioByIdUsuario.nmUsuario", data[0].nmFuncionario);
			setElementValue("usuarioByIdUsuario.vfuncionario.dsFuncao", data[0].dsFuncao);
		}
		return;
	}

	function funcCallBack(data){
		if(data != undefined) {
			setElementValue("usuarioByIdUsuario.idUsuario", data.idUsuario);
			setElementValue("usuarioByIdUsuario.nmUsuario", data.nmUsuario);
			setElementValue("usuarioByIdUsuario.vfuncionario.dsFuncao", data.dsFuncao);
		}
		return;
	}
</script>	
