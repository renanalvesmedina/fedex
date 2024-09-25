<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--

	function docasPageLoad_cb(){
		onPageLoad_cb();
				
		if (getElementValue("idTerminalTemp") != ""){
			notifyElementListeners({e:document.getElementById("terminal.filial.idFilial")});
		
			document.getElementById("terminal.idTerminal").masterLink = "true";
			setDisabled("terminal.idTerminal", true);
		}
	}
	
	function setaMascaraVigenciaTerminal(data) {
		
		var i;
		var vi = document.getElementById("terminal.dtVigenciaInicial");
		var vf = document.getElementById("terminal.dtVigenciaFinal");	
		for (i = 0; i < data.length; i++) {
			setNestedBeanPropertyValue(data, i + ":dtVigenciaInicial", setFormat(vi, getNestedBeanPropertyValue(data, i + ":dtVigenciaInicial")));
			setNestedBeanPropertyValue(data, i + ":dtVigenciaFinal",   setFormat(vf, getNestedBeanPropertyValue(data, i + ":dtVigenciaFinal")));		
		}
	}
	
	function setaTerminal_cb(data, error){

		if (data != undefined) {
			setaMascaraVigenciaTerminal(data);
		}
		
		terminal_idTerminal_cb(data);

		if (getElementValue("idTerminalTemp") != "")
			setElementValue("terminal.idTerminal", getElementValue("idTerminalTemp"));
	}

//-->
</script>
<adsm:window service="lms.portaria.manterDocasAction" onPageLoadCallBack="docasPageLoad">
	<adsm:form action="/portaria/manterDocas" idProperty="idDoca" onDataLoadCallBack="docasDataLoad" service="lms.portaria.manterDocasAction.findByIdDetalhamento">
	
   		<adsm:lookup property="terminal.filial" onchange="return filialOnchange(this)" idProperty="idFilial" required="true" criteriaProperty="sgFilial" maxLength="3"
					service="lms.portaria.manterDocasAction.findFilial" dataType="text" label="filial" size="3"
					action="/municipios/manterFiliais"  width="80%" minLengthForAutoPopUpSearch="3"
					exactMatch="true" style="width:45px" disabled="false" serializable="false">
			<adsm:propertyMapping relatedProperty="terminal.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="terminal.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />			
			<adsm:hidden property="filial.empresa.tpEmpresa" value="M" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
		</adsm:lookup>
		
		<adsm:hidden property="idTerminalTemp" serializable="false"/>
		<adsm:hidden property="terminal.pessoa.nmPessoa" serializable="false"/>		
		<adsm:combobox property="terminal.idTerminal" optionLabelProperty="pessoa.nmPessoa" optionProperty="idTerminal" boxWidth="200"
						service="lms.portaria.manterDocasAction.findTerminal" label="terminal" width="35%"  required="true" onDataLoadCallBack="setaTerminal">
				<adsm:propertyMapping criteriaProperty="terminal.filial.idFilial" modelProperty="idFilial" />
				<adsm:propertyMapping criteriaProperty="idTerminalTemp" modelProperty="idTerminal" />
				<adsm:propertyMapping relatedProperty="terminal.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
				<adsm:propertyMapping relatedProperty="terminal.dtVigenciaInicial" modelProperty="dtVigenciaInicial" />
				<adsm:propertyMapping relatedProperty="terminal.dtVigenciaFinal" modelProperty="dtVigenciaFinal" />
		</adsm:combobox>
		
		<adsm:range label="vigenciaTerminal" width="35%" >
             <adsm:textbox dataType="JTDate" property="terminal.dtVigenciaInicial" disabled="true" picker="false" serializable="false"/>
             <adsm:textbox dataType="JTDate" property="terminal.dtVigenciaFinal" disabled="true" picker="false" serializable="false"/>
        </adsm:range>
        
        <adsm:textbox dataType="integer" property="nrDoca"  required="true" size="4" maxLength="3" label="numero" width="65%"/>
        
        <adsm:textbox dataType="text" property="dsDoca" size="60" maxLength="60" label="descricao" width="65%"/>
        <adsm:hidden property="numeroDescricaoDoca" serializable="false"/>
        
	   	<adsm:combobox property="tpSituacaoDoca" required="true" domain="DM_STATUS_DOCA_BOX" label="situacao" width="35%" />
	   	
	    <adsm:textarea property="obDoca" label="observacao" maxLength="500" rows="5" width="85%" columns="60"/>
	    
		<adsm:range label="vigencia" width="45%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
        
	<adsm:buttonBar>
			<adsm:button id="boxes" caption="boxes" action="portaria/manterBoxes" cmd="main">			
				<adsm:linkProperty src="idDoca" target="idDocaTemp"/>
				<adsm:linkProperty src="dsDoca" target="doca.dsDoca"/>
				<adsm:linkProperty src="terminal.idTerminal" target="idTerminalTemp"/>
				<adsm:linkProperty src="terminal.pessoa.nmPessoa" target="doca.terminal.pessoa.nmPessoa"/>
				<adsm:linkProperty src="terminal.filial.idFilial" target="doca.terminal.filial.idFilial"/>
				<adsm:linkProperty src="terminal.filial.sgFilial" target="doca.terminal.filial.sgFilial"/>
				<adsm:linkProperty src="terminal.dtVigenciaInicial" target="doca.terminal.dtVigenciaInicial"/>
				<adsm:linkProperty src="terminal.dtVigenciaFinal" target="doca.terminal.dtVigenciaFinal"/>
				<adsm:linkProperty src="terminal.filial.pessoa.nmFantasia" target="doca.terminal.filial.pessoa.nmFantasia"/>
			</adsm:button>
			<adsm:storeButton id="store"  callbackProperty="storeDoca" service="lms.portaria.manterDocasAction.storeMap"/>
			<adsm:newButton id="novo"/>
			<adsm:removeButton id="remove"/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>   
<script>
	
	function initWindow(evt){
		if(evt.name != "gridRow_click" && evt.name != 'storeButton'){
			loadUsuarioLogado();
			novo();
			setFocusOnFirstFocusableField();
		}
	}
	
	function storeDoca_cb(data, error, key){
		
		store_cb(data, error, key);

		if (error == undefined && data != undefined){
			comportamentoDetalhe(data);
			setFocusOnNewButton();
		}		
	
	}
	
	function filialOnchange(obj){		
		var retorno = terminal_filial_sgFilialOnChangeHandler();
		
		if (obj.value == '') {
			setElementValue("terminal.filial.pessoa.nmFantasia", "");			
			clearComboTerminal();
		}
		setElementValue("idTerminalTemp", "");
		
		return retorno;		
	}
	
	function novo(){
		setDisabled("terminal.filial.idFilial", document.getElementById("terminal.filial.idFilial").masterLink == "true");
		setDisabled("terminal.idTerminal", document.getElementById("terminal.idTerminal").masterLink == "true");
		if (getElementValue("terminal.filial.idFilial") != "" && document.getElementById("terminal.filial.idFilial").masterLink == "true")
			notifyElementListeners({e:document.getElementById("terminal.filial.idFilial")});
		setDisabled("nrDoca", false);
		setDisabled("dsDoca",false);
		setDisabled("tpSituacaoDoca",false);
		setDisabled("obDoca",false);
		setDisabled("dtVigenciaInicial",false);
		setDisabled("dtVigenciaFinal",false);
		setDisabled("boxes",true);
		setDisabled("store",false);
		setDisabled("novo",false);

	}
	
	function comportamentoDetalhe(data) {
		var acaoVigencia = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");

		if (acaoVigencia == 0){
			novo();
			setFocusOnFirstFocusableField();
		} else if (acaoVigencia == 1){
			setDisabled(document, true);
			setDisabled("dtVigenciaFinal",false);
			setDisabled("obDoca",false);
			setDisabled("tpSituacaoDoca",false);			
			setDisabled("novo",false);
			setDisabled("store",false);
			setFocusOnFirstFocusableField();
		} else if (acaoVigencia == 2) {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
			setFocusOnNewButton();
		}
		
		setDisabled("boxes",false);
	}
	
	function docasDataLoad_cb(data, error){
		onDataLoad_cb(data, error);
		
		comportamentoDetalhe(data);
	}

	
	//Chama o servico que retorna os dados do usuario logado 
	function loadUsuarioLogado(){
		if (document.getElementById("terminal.filial.idFilial").masterLink != 'true'){
			var data = new Array();
			var sdo = createServiceDataObject("lms.portaria.manterDocasAction.findFilialUsuarioLogado",
					"preencheDadosFilialUsuarioLogado",data);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	//Funcao de callback do servico que retorna os dados do usuario logado. 
	function preencheDadosFilialUsuarioLogado_cb(data, exception){
		if (exception == null){
			setElementValue("terminal.filial.idFilial", getNestedBeanPropertyValue(data, "idFilial"));
			setElementValue("terminal.filial.sgFilial", getNestedBeanPropertyValue(data, "sgFilial"));
			setElementValue("terminal.filial.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "nmFantasia"));
			loadComboTerminal(getNestedBeanPropertyValue(data, "idFilial"));
		}
	}


	function loadComboTerminal(idFilial){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		
		var sdo = createServiceDataObject("lms.portaria.manterDocasAction.findTerminal",
					"loadComboTerminal",data);
		
		xmit({serviceDataObjects:[sdo]});		
	}
	
	function loadComboTerminal_cb(data){
		setaMascaraVigenciaTerminal(data);
		terminal_idTerminal_cb(data);
	}
	
	function clearComboTerminal(){
		document.getElementById("terminal.idTerminal").options.length = 1;
	}
	

</script>