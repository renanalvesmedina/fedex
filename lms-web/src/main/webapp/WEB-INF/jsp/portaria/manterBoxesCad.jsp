<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--

	function boxesPageLoad_cb(){
		onPageLoad_cb();

		if (getElementValue("idTerminalTemp") != ""){
			notifyElementListeners({e:document.getElementById("doca.terminal.filial.idFilial")});
			document.getElementById("doca.terminal.filial.siglaNomeFilial").masterLink = "true";
			document.getElementById("doca.terminal.idTerminal").masterLink = "true";
			document.getElementById("doca.terminal.pessoa.nmPessoa").masterLink = "true";
						
			setDisabled("doca.terminal.idTerminal", true);
		} else {
			loadUsuarioLogado();
		}
		
		if (getElementValue("idDocaTemp") != ""){
				
			document.getElementById("doca.idDoca").masterLink = "true";
			document.getElementById("doca.numeroDescricaoDoca").masterLink = "true";
			setDisabled("doca.idDoca", true);
		}
				
	}
	
	function setaMascaraVigenciaTerminal(data) {
		
		var i;
		var vi = document.getElementById("doca.terminal.dtVigenciaInicial");
		var vf = document.getElementById("doca.terminal.dtVigenciaFinal");	
		for (i = 0; i < data.length; i++) {
			setNestedBeanPropertyValue(data, i + ":dtVigenciaInicial", setFormat(vi, getNestedBeanPropertyValue(data, i + ":dtVigenciaInicial")));
			setNestedBeanPropertyValue(data, i + ":dtVigenciaFinal",   setFormat(vf, getNestedBeanPropertyValue(data, i + ":dtVigenciaFinal")));		
		}
	}
	
	function setaTerminal_cb(data, error){
		if (data != undefined) {
			setaMascaraVigenciaTerminal(data);
		}
		
		doca_terminal_idTerminal_cb(data);
		
		if (getElementValue("idTerminalTemp") != "")
			setElementValue("doca.terminal.idTerminal", getElementValue("idTerminalTemp"));
		
		if (getElementValue("idDocaTemp") != ""){
			notifyElementListeners({e:document.getElementById("doca.terminal.idTerminal")});	
		}
	}
	
	function setaDoca_cb(data, error){
		doca_idDoca_cb(data);
		
		if (getElementValue("idDocaTemp") != "")
			setElementValue("doca.idDoca", getElementValue("idDocaTemp"));
	}

//-->
</script>
<adsm:window onPageLoadCallBack="boxesPageLoad" service="lms.portaria.manterBoxesAction">

	<adsm:form action="/portaria/manterBoxes" idProperty="idBox" service="lms.portaria.manterBoxesAction.findById" onDataLoadCallBack="boxesDataLoad">
	
		<adsm:hidden property="doca.terminal.filial.siglaNomeFilial" serializable="false"/>
		
		<adsm:lookup property="doca.terminal.filial" idProperty="idFilial" required="true" onchange="return filialOnchange(this);" criteriaProperty="sgFilial" maxLength="3"
					service="lms.portaria.manterBoxesAction.findFilial" dataType="text" label="filial" size="3"
					action="/municipios/manterFiliais"  width="80%" minLengthForAutoPopUpSearch="3"
					exactMatch="true" style="width:45px" disabled="false" serializable="false">
			<adsm:propertyMapping relatedProperty="doca.terminal.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="doca.terminal.filial.siglaNomeFilial" modelProperty="siglaNomeFilial"/>
			<adsm:textbox dataType="text" property="doca.terminal.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />			
			
			<adsm:hidden property="filial.empresa.tpEmpresa" value="M" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
		</adsm:lookup>
		 
		<adsm:hidden property="idTerminalTemp" serializable="false"/>
		<adsm:hidden property="doca.terminal.pessoa.nmPessoa" serializable="false"/>
		<adsm:combobox property="doca.terminal.idTerminal" optionLabelProperty="pessoa.nmPessoa" optionProperty="idTerminal"  boxWidth="200"
						service="lms.portaria.manterBoxesAction.findTerminal" label="terminal" width="35%"  required="true" onDataLoadCallBack="setaTerminal">
				<adsm:propertyMapping criteriaProperty="doca.terminal.filial.idFilial" modelProperty="idFilial" />
				<adsm:propertyMapping criteriaProperty="idTerminalTemp" modelProperty="idTerminal" />
				<adsm:propertyMapping relatedProperty="doca.terminal.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
				<adsm:propertyMapping relatedProperty="doca.terminal.dtVigenciaInicial" modelProperty="dtVigenciaInicial" />
				<adsm:propertyMapping relatedProperty="doca.terminal.dtVigenciaFinal" modelProperty="dtVigenciaFinal" />
		</adsm:combobox>
		
		<adsm:range label="vigenciaTerminal" width="35%" >
             <adsm:textbox dataType="JTDate" property="doca.terminal.dtVigenciaInicial" disabled="true" picker="false" serializable="false"/>
             <adsm:textbox dataType="JTDate" property="doca.terminal.dtVigenciaFinal" disabled="true" picker="false" serializable="false"/>
        </adsm:range>
        
		<adsm:hidden property="idDocaTemp" serializable="false"/>
		<adsm:hidden property="doca.numeroDescricaoDoca" serializable="false"/>
		<adsm:combobox property="doca.idDoca" optionLabelProperty="numeroDescricaoDoca" optionProperty="idDoca" required="true" boxWidth="200"
						service="lms.portaria.manterBoxesAction.findDoca" label="doca" width="35%" onDataLoadCallBack="setaDoca">
			<adsm:propertyMapping criteriaProperty="doca.terminal.idTerminal" modelProperty="idTerminal" />
			<adsm:propertyMapping criteriaProperty="idDocaTemp" modelProperty="idDoca" />
			<adsm:propertyMapping relatedProperty="doca.numeroDescricaoDoca" modelProperty="numeroDescricaoDoca" />
		</adsm:combobox>
						
        <adsm:textbox dataType="integer" property="nrBox" size="4" maxLength="3" label="numero" required="true"/>
        
        <adsm:textbox dataType="text" property="dsBox" size="40" maxLength="60" label="descricao" />
        
	   	<adsm:combobox property="tpSituacaoBox" domain="DM_STATUS_DOCA_BOX" label="situacao" width="35%" required="true" />
	   	
		<adsm:combobox property="modulo.idModulo" optionLabelProperty="nrModulo" optionProperty="idModulo" 
						service="lms.portaria.manterBoxesAction.findModulo" label="modulo" width="35%" >
			<adsm:propertyMapping criteriaProperty="doca.terminal.idTerminal" modelProperty="terminal.idTerminal" />
		</adsm:combobox>
		
	    <adsm:textarea property="obBox" label="observacao" maxLength="500" rows="5" width="85%" columns="60"/>
	    
		<adsm:range label="vigencia" width="45%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
        
	<adsm:buttonBar>
			<adsm:button id="meiosTransporte" caption="meiosTransporte" action="portaria/manterMeiosTransporteBox" cmd="main">
				<adsm:linkProperty src="doca.terminal.filial.idFilial" target="box.doca.terminal.filial.idFilial"/>
				<adsm:linkProperty src="doca.terminal.filial.sgFilial" target="box.doca.terminal.filial.sgFilial"/>
				<adsm:linkProperty src="doca.terminal.filial.pessoa.nmFantasia" target="box.doca.terminal.filial.pessoa.nmFantasia"/>
				<adsm:linkProperty src="doca.terminal.pessoa.nmPessoa" target="box.doca.terminal.pessoa.nmPessoa"/>
				<adsm:linkProperty src="doca.numeroDescricaoDoca" target="box.doca.numeroDescricaoDoca"/>
				<adsm:linkProperty src="idBox" target="box.idBox"/>
				<adsm:linkProperty src="nrBox" target="box.nrBox"/>
			</adsm:button>
			<adsm:button id="finalidades" caption="finalidades" action="portaria/manterFinalidadesBoxes" cmd="main">
				<adsm:linkProperty src="doca.terminal.filial.idFilial" target="box.doca.terminal.filial.idFilial"/>
				<adsm:linkProperty src="doca.terminal.filial.sgFilial" target="box.doca.terminal.filial.sgFilial"/>
				<adsm:linkProperty src="doca.terminal.filial.pessoa.nmFantasia" target="box.doca.terminal.filial.pessoa.nmFantasia" />
				<adsm:linkProperty src="doca.terminal.pessoa.nmPessoa" target="box.doca.terminal.pessoa.nmPessoa"/>
				<adsm:linkProperty src="doca.numeroDescricaoDoca" target="box.doca.numeroDescricaoDoca"/>
				<adsm:linkProperty src="idBox" target="box.idBox"/>
				<adsm:linkProperty src="nrBox" target="box.nrBox"/>
			</adsm:button>
			<adsm:storeButton id="store" callbackProperty="storeBox" service="lms.portaria.manterBoxesAction.storeMap"/>
			<adsm:newButton id="novo"/>
			<adsm:removeButton id="remove"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   
<script>

	function initWindow(evt){

		if(evt.name != "gridRow_click" && evt.name != 'storeButton' && evt.name != 'tab_load'){
			loadUsuarioLogado();
			novo();
			setFocusOnFirstFocusableField();
		}
	}
	
	//Chama o servico que retorna os dados do usuario logado 
	function loadUsuarioLogado(){
		if (document.getElementById("doca.terminal.filial.idFilial").masterLink != 'true'){
			var data = new Array();
			var sdo = createServiceDataObject("lms.portaria.manterBoxesAction.findFilialUsuarioLogado",
					"preencheDadosFilialUsuarioLogado",data);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	//Funcao de callback do servico que retorna os dados do usuario logado. 
	function preencheDadosFilialUsuarioLogado_cb(data, exception){
		if (exception == null){
			setElementValue("doca.terminal.filial.idFilial", getNestedBeanPropertyValue(data, "idFilial"));
			setElementValue("doca.terminal.filial.sgFilial", getNestedBeanPropertyValue(data, "sgFilial"));			
			setElementValue("doca.terminal.filial.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "nmFantasia"));
			setElementValue("doca.terminal.filial.siglaNomeFilial",
							getNestedBeanPropertyValue(data, "sgFilial") + " - " + getNestedBeanPropertyValue(data, "nmFantasia"));
					
			loadComboTerminal(getNestedBeanPropertyValue(data, "idFilial"));
		}
	}
	
	function loadComboTerminal(idFilial){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		
		var sdo = createServiceDataObject("lms.portaria.manterBoxesAction.findTerminal",
					"loadComboTerminal",data);
		
		xmit({serviceDataObjects:[sdo]});		
	}
	
	function loadComboTerminal_cb(data){
		setaMascaraVigenciaTerminal(data);
		doca_terminal_idTerminal_cb(data);
	}
	
	function filialOnchange(obj){		
		var retorno = doca_terminal_filial_sgFilialOnChangeHandler();
		
		if (obj.value == '') {
			setElementValue("doca.terminal.filial.pessoa.nmFantasia", "");
			clearComboTerminal();
		}
		setElementValue("idTerminalTemp", "");
		
		return retorno;		
	}
	
	function clearComboTerminal(){
		document.getElementById("doca.terminal.idTerminal").options.length = 1;
	}
	
	function storeBox_cb(data, error, key){		
		store_cb(data, error, key);

		if (error == undefined && data != undefined){
			comportamentoDetalhe(data);
			setFocusOnNewButton();
			
   		    var siglaNomeFilial = getElementValue("doca.terminal.filial.sgFilial")
						   		    + " - " +
						          getElementValue("doca.terminal.filial.pessoa.nmFantasia");

	        var nmTerminal = document.getElementById("doca.terminal.idTerminal").options[document.getElementById("doca.terminal.idTerminal").selectedIndex].text;
	        
	        var numeroDescDoca = document.getElementById("doca.idDoca").options[document.getElementById("doca.idDoca").selectedIndex].text;
			
			setElementValue("doca.terminal.filial.siglaNomeFilial", siglaNomeFilial);
			setElementValue("doca.terminal.pessoa.nmPessoa", nmTerminal);
			setElementValue("doca.numeroDescricaoDoca", numeroDescDoca);			
		}		
	
	}

	function novo(){
		setDisabled("doca.terminal.filial.idFilial", document.getElementById("doca.terminal.filial.idFilial").masterLink == "true");
		setDisabled("doca.terminal.idTerminal", document.getElementById("doca.terminal.idTerminal").masterLink == "true");
		setDisabled("doca.idDoca", false);
		setDisabled("nrBox",false);
		setDisabled("dsBox",false);
		setDisabled("tpSituacaoBox",false);
		setDisabled("modulo.idModulo",false);
		setDisabled("obBox",false);
		setDisabled("dtVigenciaInicial",false);
		setDisabled("dtVigenciaFinal",false);
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
			setDisabled("novo",false);
			setDisabled("store",false);
			setDisabled("obBox",false);
			setDisabled("tpSituacaoBox",false);
			setFocusOnFirstFocusableField();
		} else if (acaoVigencia == 2) {
			setDisabled(document, true);
			setDisabled("novo", false);			
			setFocusOnNewButton();
		}
		
		setDisabled("meiosTransporte",false);
		setDisabled("finalidades",false);
	}
	
	function boxesDataLoad_cb(data, error){
		onDataLoad_cb(data, error);
						
		comportamentoDetalhe(data);
	}


</script>