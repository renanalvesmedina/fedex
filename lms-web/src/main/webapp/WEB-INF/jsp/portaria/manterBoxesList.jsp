<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--

	function boxesPageLoad_cb(){
		onPageLoad_cb();
		
		if (getElementValue("idTerminalTemp") != ""){
			notifyElementListeners({e:document.getElementById("doca.terminal.filial.idFilial")});		
			document.getElementById("doca.terminal.idTerminal").masterLink = "true";
			document.getElementById("doca.terminal.filial.pessoa.nmFantasia").masterLink = "true";		
			
			setDisabled("doca.terminal.idTerminal", true);
		} else {
			loadUsuarioLogado();
		}
		
		if (getElementValue("idDocaTemp") != ""){
	
			document.getElementById("doca.idDoca").masterLink = "true";
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
<adsm:window service="lms.portaria.manterBoxesAction" onPageLoadCallBack="boxesPageLoad">
	<adsm:form action="/portaria/manterBoxes" height="106" >
	
		<adsm:lookup property="doca.terminal.filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
					service="lms.portaria.manterBoxesAction.findFilial" dataType="text" label="filial" size="3"
					action="/municipios/manterFiliais"  width="80%" minLengthForAutoPopUpSearch="3"
					exactMatch="true" style="width:45px" disabled="false" >
			<adsm:propertyMapping relatedProperty="doca.terminal.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="doca.terminal.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />			

			<adsm:hidden property="filial.empresa.tpEmpresa" value="M" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
		</adsm:lookup> 
		
		<adsm:hidden property="idTerminalTemp" serializable="false"/>
		<adsm:combobox property="doca.terminal.idTerminal" optionLabelProperty="pessoa.nmPessoa" optionProperty="idTerminal"  boxWidth="200"
						service="lms.portaria.manterBoxesAction.findTerminalTodos" label="terminal" width="35%" onDataLoadCallBack="setaTerminal">
				<adsm:propertyMapping criteriaProperty="doca.terminal.filial.idFilial" modelProperty="filial.idFilial" />
				<adsm:propertyMapping relatedProperty="doca.terminal.dtVigenciaInicial" modelProperty="dtVigenciaInicial" />
				<adsm:propertyMapping relatedProperty="doca.terminal.dtVigenciaFinal" modelProperty="dtVigenciaFinal" />
		</adsm:combobox>
		
		<adsm:range label="vigenciaTerminal" width="35%" >
             <adsm:textbox dataType="JTDate" property="doca.terminal.dtVigenciaInicial" disabled="true" picker="false" serializable="false"/>
             <adsm:textbox dataType="JTDate" property="doca.terminal.dtVigenciaFinal" disabled="true" picker="false" serializable="false"/>
        </adsm:range>
        
		<adsm:hidden property="idDocaTemp" serializable="false"/>
		<adsm:combobox property="doca.idDoca" optionLabelProperty="numeroDescricaoDoca" optionProperty="idDoca"  boxWidth="200"
						service="lms.portaria.manterBoxesAction.findDocaTodos" label="doca" width="35%" onDataLoadCallBack="setaDoca">
			<adsm:propertyMapping criteriaProperty="doca.terminal.idTerminal" modelProperty="terminal.idTerminal" />
		</adsm:combobox>
		
        <adsm:textbox dataType="integer" property="nrBox" size="4" maxLength="3" label="numero" />
        
        <adsm:textbox dataType="text" property="dsBox" size="40" maxLength="60" label="descricao" />
        
		<adsm:combobox property="tpSituacaoBox" domain="DM_STATUS_DOCA_BOX" label="situacao" width="35%" />
		
		<adsm:range label="vigencia" width="65%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
        
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="box"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idBox" defaultOrder="doca_terminal_filial_.sgFilial,doca_terminal_pessoa_.nmPessoa,doca_.nrDoca,nrBox,dtVigenciaInicial" property="box" unique="true" rows="9">
		<adsm:gridColumn title="filial" property="doca.terminal.filial.sgFilial" width="8%" />
		<adsm:gridColumn title="terminal" property="doca.terminal.pessoa.nmPessoa" width="15%" />	
		<adsm:gridColumn title="doca" property="doca.numeroDescricaoDoca" width="8%" />
		<adsm:gridColumn title="box" dataType="integer" property="nrBox" width="8%" />
		<adsm:gridColumn title="descricao" property="dsBox" width="23%"/>
		<adsm:gridColumn title="situacao" property="tpSituacaoBox" isDomain="true" width="12%"/>
		<adsm:gridColumn title="vigenciaInicial" dataType="JTDate" property="dtVigenciaInicial" align="center" width="13%" />
		<adsm:gridColumn title="vigenciaFinal" dataType="JTDate" property="dtVigenciaFinal" align="center" width="13%" />		
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--

	function initWindow(evt){

		if(evt.name == 'cleanButton_click'){
			loadUsuarioLogado();
			setFocusOnFirstFocusableField();
		}
	}
	
	//Chama o servico que retorna os dados do usuario logado 
	function loadUsuarioLogado(){

		if (document.getElementById("doca.terminal.idTerminal").masterLink != "true"){

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
			notifyElementListeners({e:document.getElementById("doca.terminal.filial.idFilial")});		
			//loadComboTerminal(getNestedBeanPropertyValue(data, "idFilial"));
		}
	}
	
	function loadComboTerminal(idFilial){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		
		var sdo = createServiceDataObject("lms.portaria.manterBoxesAction.findTerminalTodos",
					"loadComboTerminal",data);
		
		xmit({serviceDataObjects:[sdo]});		
	}

	function loadComboTerminal_cb(data){
		setaMascaraVigenciaTerminal(data);
		doca_terminal_idTerminal_cb(data);
	}
	

//-->
</script>
