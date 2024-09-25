<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script>
	/*
	 * Remove as criteriaProperty da lookup.
	 */
	function myOnPageLoad(){
		onPageLoad();
		removePropertyMapByCriteria(document.getElementById("cotacaoMoeda.idCotacaoMoeda"), "cotacaoMoeda.vlCotacaoMoeda");
		
		_serviceDataObjects = new Array();
		
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterNotasDebitoInternacionaisAction.findInitialValue", "initPage", null)); 
		xmit(false);					
	}	
</script>

<adsm:window service="lms.contasreceber.manterNotasDebitoInternacionaisAction" onPageLoad="myOnPageLoad" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterNotasDebitoInternacionais" idProperty="idFatura"
		newService="lms.contasreceber.manterNotasDebitoInternacionaisAction.newMaster" onDataLoadCallBack="myOnDataLoad">

		<adsm:hidden property="nrFaturaFormatada"/>		
		<adsm:hidden property="filialByIdFilial.idFilial"/>
		<adsm:textbox dataType="text" property="filialByIdFilial.sgFilial" size="3" width="5%" label="filialFaturamento" labelWidth="20%" serializable="false" disabled="true">				
			<adsm:textbox dataType="text" property="filialByIdFilial.pessoa.nmFantasia" width="25%" size="30" serializable="false" disabled="true"/>		
		</adsm:textbox>
		
		<adsm:textbox dataType="integer" label="numero" property="nrFatura" size="10" maxLength="10" disabled="true" labelWidth="17%" width="33%"/>

		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			dataType="text" 
			exactMatch="true" 
			idProperty="idCliente" 
			label="clienteResponsavel" 
			maxLength="20" 
			property="cliente" 
			service="lms.contasreceber.manterNotasDebitoInternacionaisAction.findLookupCliente" 
			size="20" 
			labelWidth="20%"
			width="80%"
			onPopupSetValue="popupCliente"
			required="true">
			
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				serializable="false"
				size="58"/>
				
		</adsm:lookup>
		
		<adsm:hidden property="cliente.filialByIdFilialCobranca.idFilial"/>
		<adsm:textbox dataType="text" property="cliente.filialByIdFilialCobranca.sgFilial" size="3" width="5%" label="filialCobrancaCliente" labelWidth="20%" serializable="false" disabled="true">				
			<adsm:textbox dataType="text" property="cliente.filialByIdFilialCobranca.pessoa.nmFantasia" width="25%" size="30" serializable="false" disabled="true"/>		
		</adsm:textbox>	
		
		
		<adsm:hidden property="filialByIdFilialCobradora.idFilial"/>
		<adsm:textbox dataType="text" property="filialByIdFilialCobradora.sgFilial" size="3" width="5%" label="filialCobranca" labelWidth="17%" serializable="false" disabled="true">				
			<adsm:textbox dataType="text" property="filialByIdFilialCobradora.pessoa.nmFantasia" width="28%" size="30" serializable="false" disabled="true"/>		
		</adsm:textbox>			

        <adsm:combobox property="divisaoCliente.idDivisaoCliente" label="divisao" 
        		service="lms.contasreceber.manterNotasDebitoInternacionaisAction.findComboDivisaoCliente" 
				optionLabelProperty="dsDivisaoCliente"				
				labelWidth="17%"
				width="33%"
				optionProperty="idDivisaoCliente" autoLoad="false">
			<adsm:propertyMapping 
				criteriaProperty="cliente.idCliente" 
				modelProperty="cliente.idCliente"
			/>				
		</adsm:combobox>

		<adsm:combobox property="tpSituacaoFatura" label="situacaoFatura" domain="DM_STATUS_ROMANEIO"
			labelWidth="17%"
			width="33%"
			autoLoad="false"
			onchange="return;"
			required="true">
		</adsm:combobox>
		
		<adsm:combobox property="tpSituacaoAprovacao" label="situacaoAprovacao" domain="DM_STATUS_WORKFLOW" labelWidth="20%"
			width="30%"/>			

		<adsm:textbox label="cotacao" 
			width="8%"
			labelWidth="17%"
			dataType="text"
			property="simboloMoedaPais" 
			size="8"
			serializable="false"
			disabled="true" />
			
		<adsm:textbox property="dtCotacaoMoeda" 
			dataType="JTDate" 
			style="width:83px;"
			disabled="true" 
			picker="false"
			serializable="false" width="9%"/>
		
		<adsm:lookup property="cotacaoMoeda" idProperty="idCotacaoMoeda" 
			criteriaProperty="vlCotacaoMoeda" 
			action="/configuracoes/manterCotacoesMoedas" dataType="currency"
			onchange="vlCotacaoMoedaOnChange(this);" onPopupSetValue="cotacaoMoedaSetValue"
			service="lms.contasreceber.manterNotasDebitoInternacionaisAction.findLookupCotacaoMoeda" 
			serializable="true"
			size="12" width="14%">
			<adsm:propertyMapping  relatedProperty="dtCotacaoMoeda" modelProperty="dtCotacaoMoeda" />
			<adsm:propertyMapping  relatedProperty="vlCotacaoMoedaTmp" modelProperty="vlCotacaoMoeda" />			
		</adsm:lookup>

		<adsm:hidden property="vlCotacaoMoeda" serializable="true"/>
		<adsm:hidden property="vlCotacaoMoedaTmp" serializable="false"/>		

		<adsm:textbox dataType="JTDate" label="dataEmissao" property="dtEmissao" size="8" maxLength="20" required="true" 
			labelWidth="20%"
			width="30%"
			required="true"/>
		<adsm:textbox dataType="JTDate" label="dataVencimento" property="dtVencimento" size="8" maxLength="20" required="true" 
			labelWidth="17%"
			width="33%"
			required="true"/>
			

		<adsm:textarea label="descricao" property="obFatura" labelWidth="22%"
			width="78%" maxLength="80" columns="100" rows="3" required="true" />

		<adsm:section caption="informacoesComplementares" />

		<adsm:hidden property="redeco.idRedeco"/>
		<adsm:complement label="redeco" labelWidth="18%" width="15%">
			<adsm:textbox dataType="text" property="redeco.filial.sgFilial" size="3" width="4%" disabled="true" maxLength="3"/>
			<adsm:textbox dataType="text" property="redeco.nrRedeco" size="6" width="9%" disabled="true" maxLength="10"/>
		</adsm:complement>

		<adsm:textbox labelWidth="20%" dataType="JTDate" label="dataLiquidacao" property="dtLiquidacao" size="10" 
		maxLength="20" width="13%" disabled="true" picker="false"/>
		
		<adsm:hidden property="recibo.idRecibo"/>
		<adsm:complement label="recibo" labelWidth="19%" width="14%">
			<adsm:textbox dataType="text" property="recibo.filial.sgFilial" size="3" width="4%" disabled="true" maxLength="3"/>
			<adsm:textbox dataType="text" property="recibo.nrRecibo" size="6" width="9%" disabled="true" maxLength="10"/>
		</adsm:complement>
		
		
		<adsm:hidden property="relacaoCobranca.idRelacaoCobranca"/>
		<adsm:complement label="relacaoCobranca" labelWidth="18%" width="15%">
			<adsm:textbox dataType="text" property="relacaoCobranca.nrRelacaoCobrancaFilial" size="3" width="4%" disabled="true" maxLength="3"/>
			<adsm:textbox dataType="text" property="relacaoCobranca.filial.sgFilial" size="6" width="9%" disabled="true" maxLength="10"/>
		</adsm:complement>

		<adsm:section caption="totaisImpostos" />

		<adsm:textbox labelWidth="20%" dataType="integer" label="qtdeTotalDocumentos" property="qtDocumentos" size="10" 
			maxLength="6" width="13%" disabled="true"/>

		<adsm:textbox labelWidth="19%" dataType="currency" label="valorIVA" property="vlIva" size="10" maxLength="20" width="14%"/>
		<adsm:textbox labelWidth="18%" dataType="currency" label="valorTotalNota" property="vlTotal" size="10" maxLength="18" width="13%" disabled="true"/>
		<adsm:textbox labelWidth="19%" dataType="currency" label="valorTitulo" property="vlTotalRecebido" size="10" maxLength="18" width="14%" disabled="true"/>

		<adsm:buttonBar >
			<adsm:button boxWidth="90" caption="complemento" action="/contasReceber/consultarComplementosRomaneios" cmd="main">
				<adsm:linkProperty src="idFatura" target="idFatura"/>
			</adsm:button>
			<adsm:button boxWidth="45" caption="emitir"/>
			<adsm:button boxWidth="120" caption="relacaoCobranca" id="relacaoCobrancaButton" action="/contasReceber/pesquisarRelacoesCobranca" cmd="main">
				<adsm:linkProperty src="relacaoCobranca.idRelacaoCobranca" target="idRelacaoCobranca"/>
				<adsm:linkProperty src="relacaoCobranca.nrRelacaoCobrancaFilial" target="nrRelacaoCobrancaFilial"/>
			</adsm:button>
			<adsm:button boxWidth="55" caption="redeco" id="redecoButton" action="/contasReceber/manterRedeco" cmd="main">
				<adsm:linkProperty src="redeco.idRedeco" target="idRedeco"/>
				<adsm:linkProperty src="redeco.nrRedeco" target="nrRedeco"/>			
			</adsm:button>
			<adsm:button boxWidth="90" caption="recibo" id="recibo" onclick="return onClickRecibo();">
				<adsm:linkProperty src="recibo.idRecibo" target="idRecibo"/>
				<adsm:linkProperty src="recibo.nrRecibo" target="nrRecibo"/>			
			</adsm:button>
            <adsm:button boxWidth="55" caption="cancelar" onclick="return cancelFatura();"/>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
		
	<adsm:i18nLabels>
		<adsm:include key="LMS-36070"/>
	</adsm:i18nLabels>
	</adsm:form>	
</adsm:window>
<script>

	document.getElementById("cliente.idCliente").callBack = "clienteExactMatch";
	
	
	function popupCliente(data){
		if (data == undefined) {
			return;
		}
		mountFilialCliente(data.idCliente);
		mountDivisaoCliente(data.idCliente);
	}

	function mountFilialCliente(varIdCliente){	
		_serviceDataObjects = new Array();	
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterNotasDebitoInternacionaisAction.findFilialCliente", "mountFilialCliente", {idCliente:varIdCliente}));
		xmit(false);		
	}
	
	function mountDivisaoCliente(varIdCliente){	
		_serviceDataObjects = new Array();	
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterNotasDebitoInternacionaisAction.findComboDivisaoCliente", "divisaoCliente_idDivisaoCliente", {cliente:{idCliente:varIdCliente}}));
		xmit(false);						
	}	
	
	function cancelFatura(){	
		if (confirm(i18NLabel.getLabel("LMS-36070"))==true){
			_serviceDataObjects = new Array();	
			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterNotasDebitoInternacionaisAction.cancelFatura", "myOnDataLoad", buildFormBeanFromForm(this.document.forms[0])));
			xmit(false);		
		}
	}	
	
	
	function mountFilialCliente_cb(d,e,o,x){
		cleanFilialCliente();
		if (d != undefined) {
			setElementValue("cliente.filialByIdFilialCobranca.idFilial",d.idFilial);		
			setElementValue("cliente.filialByIdFilialCobranca.sgFilial",d.sgFilial);
			setElementValue("cliente.filialByIdFilialCobranca.pessoa.nmFantasia",d.nmFantasia);
		}
	}

	
	function cleanFilialCliente(){
		setElementValue("cliente.filialByIdFilialCobranca.idFilial","");		
		setElementValue("cliente.filialByIdFilialCobranca.sgFilial","");
		setElementValue("cliente.filialByIdFilialCobranca.pessoa.nmFantasia","");	
	}

	function clienteExactMatch_cb(data) {
		cleanFilialCliente();
		var retorno = lookupExactMatch({e:document.getElementById("cliente.idCliente"), data:data});
		if (data != undefined && data.length > 0) {
			if (data.length == 1) {				
				mountFilialCliente(data[0].idCliente);
				mountDivisaoCliente(data[0].idCliente);
			}
		}
		return retorno;
	}	
	
	function myOnPageLoad_cb(d,e,o,x){
		onPageLoad_cb(d,e,o,x);
		initPage();
	}
	
	var constStatusFaturaFull = new Array();
	var constStatusFatura = new Array(2);
	var constFilial	= new Object();
	
	function myOnDataLoad_cb(d,e,o,x){		
		loadSituacaoFatura();
		
		if (e != null){
			alert(e+'');
			return false;
		}
				
		onDataLoad_cb(d,e,o,x);
		setElementValue("vlCotacaoMoedaTmp",getElementValue("cotacaoMoeda.vlCotacaoMoeda"));
		format("cotacaoMoeda.vlCotacaoMoeda");
		format("vlCotacaoMoedaTmp");
		format("vlCotacaoMoeda");		
		if (getElementValue("vlCotacaoMoeda") != ""){
			setElementValue("cotacaoMoeda.vlCotacaoMoeda",getElementValue("vlCotacaoMoeda"));
		}
		setDisabled("tpSituacaoFatura",true);
		
		setElementValue("nrFaturaFormatada", getElementValue("filialByIdFilial.sgFilial") + " " + getElementValue("nrFatura"));
		
		disabledButtons();
	}
	
	function initWindow(eventObj) {
    	if (eventObj.name != "tab_click" && eventObj.name != "storeButton"&& eventObj.name != "gridRow_click") {
	    	initPage();
	    }
	    
	    if (eventObj.name != "gridRow_click" && eventObj.name != "newButton_click") {
	    	disabledButtons(false);	
	    }
	}

	function initPage(){
		loadSituacaoFaturaNovo();
		loadFilialCobranca();
		
		setElementValue("tpSituacaoFatura","DI");
		
		setDisabled("tpSituacaoFatura",false);
	}
	
	/*
	 * Monta as duas constantes que tem a lista de situação de fatura	
	 */
	function initPage_cb(d,e,o,x){
		if (d != null) {	
			constFilial.idFilial = d.idFilial;
			constFilial.sgFilial = d.sgFilial;
			constFilial.nmFantasia = d.nmFantasia;				
		
			constStatusFaturaFull = d.tpSituacaoFatura;
			
			for (var i = 0; i < constStatusFaturaFull.length; i++) {
				if (constStatusFaturaFull[i].value == "DI"){
					constStatusFatura[0] = constStatusFaturaFull[i];
				} else if (constStatusFaturaFull[i].value == "IN"){
					constStatusFatura[1] = constStatusFaturaFull[i];
				}
			}			
		}
	}	

	function loadSituacaoFatura(){
		if (constStatusFaturaFull[0] != undefined){
			document.getElementById("tpSituacaoFatura").length = 0;
				
			for (var i = 0; i < constStatusFaturaFull.length; i++) {
				document.getElementById("tpSituacaoFatura")[i] = new Option(constStatusFaturaFull[i].description,constStatusFaturaFull[i].value);  		
			}
		} else {	
			setTimeout("loadSituacaoFatura()",1000);
		}			
	}

	function loadSituacaoFaturaNovo(){
		if (constStatusFatura[0] != undefined){
			document.getElementById("tpSituacaoFatura").length = 0;
			
			document.getElementById("tpSituacaoFatura")[0] = new Option(constStatusFatura[0].description,constStatusFatura[0].value);  
			document.getElementById("tpSituacaoFatura")[1] = new Option(constStatusFatura[1].description,constStatusFatura[1].value);  		
		} else {	
			setTimeout("loadSituacaoFaturaNovo()",1000);
		}			
	}
	
	function loadFilialCobranca(){
		if (constFilial.idFilial != undefined){
			setElementValue("filialByIdFilialCobradora.idFilial", constFilial.idFilial);
			setElementValue("filialByIdFilialCobradora.sgFilial", constFilial.sgFilial);
			setElementValue("filialByIdFilialCobradora.pessoa.nmFantasia", constFilial.nmFantasia);	
		} else {	
			setTimeout("loadFilialCobranca()",1000);
		}		
	}
	
	function onClickRecibo(){
		parent.parent.redirectPage('contasReceber/manterReciboOficial.do?cmd=main' + buildLinkPropertiesQueryString_reciboOficial());	
	}
	
	function disabledButtons(){
		var blDisabledButtons = false;
		var varTpSituacaoFatura = getElementValue("tpSituacaoFatura");
		if (varTpSituacaoFatura != "DI" && varTpSituacaoFatura != "EM" && varTpSituacaoFatura != "BL"){
			blDisabledButtons = true;
		}
	
		setDisabled("storeButton",blDisabledButtons);
		setDisabled("removeButton",blDisabledButtons);
		
		if (getElementValue("relacaoCobranca.idRelacaoCobranca") == ""){
			setDisabled("relacaoCobrancaButton",true);
		} else {
			setDisabled("relacaoCobrancaButton",false);
		}
		
		if (getElementValue("redeco.idRedeco") == ""){
			setDisabled("redecoButton",true);
		} else {
			setDisabled("redecoButton",false);
		}
		
		if (getElementValue("recibo.idRecibo") == ""){
			setDisabled("recibo",true);
		} else {
			setDisabled("recibo",false);		
		}				
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
	 * Ao clicar na listagem da popUp (lookup) atribui o valor do simboloMoedaPais
	 */
	function cotacaoMoedaSetValue(data){
		if (data == undefined) return;
		setElementValue( "simboloMoedaPais", getSimboloMoedaPais(data.moedaPais) );
	}
	
	
	/*
	 * Remove propertyMapping
	 */
	function removePropertyMapByCriteria(e, criteriaName){

		var props = e.propertyMappings;
		var newProps = new Array();
		for (var i = 0; i < props.length; i++){
			var map = props[i];
			 // Nao copia a criteria informada
			if (map.criteriaProperty != criteriaName){
				newProps[newProps.length] = props[i];
			}
		}
		e.propertyMappings = newProps;
	}
	
	function myOnShow(eventObj) {
		if (eventObj.tabGroup.oldSelectedTab.properties.id == "pesq") {
			newButtonScript();
		} else {
		
		}		
		return false;
	}	
</script>
