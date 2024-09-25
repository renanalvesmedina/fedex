<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--

	function docasPageLoad_cb(){
		onPageLoad_cb();
				
		if (getElementValue("idTerminalTemp") != ""){
			notifyElementListeners({e:document.getElementById("terminal.filial.idFilial")});
			
			document.getElementById("terminal.idTerminal").masterLink = "true";
			setDisabled("terminal.idTerminal", true);
		}else
			loadUsuarioLogado();
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
	<adsm:form action="/portaria/manterDocas" height="106" >
       
   		<adsm:lookup property="terminal.filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
					service="lms.portaria.manterDocasAction.findFilial" dataType="text" label="filial" size="3"
					action="/municipios/manterFiliais"  width="80%" minLengthForAutoPopUpSearch="3"
					exactMatch="false" style="width:45px" disabled="false">
			<adsm:propertyMapping relatedProperty="terminal.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="terminal.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />			
			
			<adsm:hidden property="filial.empresa.tpEmpresa" value="M" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
		</adsm:lookup>
		
		<adsm:hidden property="idTerminalTemp" serializable="false"/>
		<adsm:combobox property="terminal.idTerminal" optionLabelProperty="pessoa.nmPessoa" optionProperty="idTerminal" boxWidth="200" 
						service="lms.portaria.manterDocasAction.findTerminalTodos" label="terminal" width="35%" onDataLoadCallBack="setaTerminal">
				<adsm:propertyMapping criteriaProperty="terminal.filial.idFilial" modelProperty="filial.idFilial" />
				<adsm:propertyMapping relatedProperty="terminal.dtVigenciaInicial" modelProperty="dtVigenciaInicial" />
				<adsm:propertyMapping relatedProperty="terminal.dtVigenciaFinal" modelProperty="dtVigenciaFinal" />
		</adsm:combobox>
		
		<adsm:range label="vigenciaTerminal" width="35%" >
             <adsm:textbox dataType="JTDate" property="terminal.dtVigenciaInicial" disabled="true" picker="false" serializable="false"/>
             <adsm:textbox dataType="JTDate" property="terminal.dtVigenciaFinal" disabled="true" picker="false" serializable="false"/>
        </adsm:range>
		
        <adsm:textbox dataType="integer" property="nrDoca" size="4" maxLength="3" label="numero" width="65%"/>
        
        <adsm:textbox dataType="text" property="dsDoca" size="60" maxLength="60" label="descricao" width="65%"/>
        
	   	<adsm:combobox property="tpSituacaoDoca" domain="DM_STATUS_DOCA_BOX" label="situacao" width="35%" />
	   	
		<adsm:range label="vigencia" width="35%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
        
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="doca"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
 	
	<adsm:grid idProperty="idDoca" defaultOrder="terminal_filial_.sgFilial,terminal_pessoa_.nmPessoa,nrDoca,dtVigenciaInicial" 
			property="doca" unique="true" rows="9">
		<adsm:gridColumn title="filial" property="terminal.filial.sgFilial" width="8%" />
		<adsm:gridColumn title="terminal" property="terminal.pessoa.nmPessoa" width="22%" />
		<adsm:gridColumn title="doca" dataType="integer" property="nrDoca" width="5%"/>
		<adsm:gridColumn title="descricao" property="dsDoca" width="27%" />
		<adsm:gridColumn title="situacao" property="tpSituacaoDoca" isDomain="true" width="12%"/>
		<adsm:gridColumn title="vigenciaInicial" dataType="JTDate" property="dtVigenciaInicial" width="13%" />
		<adsm:gridColumn title="vigenciaFinal" dataType="JTDate" property="dtVigenciaFinal" width="13%" />			
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
<script type="text/javascript">
<!--

	function initWindow(evt){
		if ((evt.name == "cleanButton_click" || evt.name == "tab_load")) {
			writeDataSession()
			setFocusOnFirstFocusableField();
		}
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
	
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	
	//Funcao de callback do servico que retorna os dados do usuario logado. 
	function preencheDadosFilialUsuarioLogado_cb(data, exception){
		if (exception == null && document.getElementById("terminal.filial.idFilial").masterLink != "true" && getElementValue("idTerminalTemp") == ""){
			idFilial = getNestedBeanPropertyValue(data, "idFilial");
			sgFilial = getNestedBeanPropertyValue(data, "sgFilial");
			nmFilial = getNestedBeanPropertyValue(data, "nmFantasia");
			writeDataSession();
		}
	}
	
	function writeDataSession() {
		if (idFilial != null &&
			sgFilial != null &&
			nmFilial != null) {
			setElementValue("terminal.filial.idFilial", idFilial);
			setElementValue("terminal.filial.sgFilial", sgFilial);
			setElementValue("terminal.filial.pessoa.nmFantasia", nmFilial);
			loadComboTerminal(idFilial);
		}
	}
	
	
	function loadComboTerminal(idFilial){
		var data = new Array();
		setNestedBeanPropertyValue(data, "filial.idFilial", idFilial);
		
		var sdo = createServiceDataObject("lms.portaria.manterDocasAction.findTerminalTodos",
					"setaTerminal",data);
		
		xmit({serviceDataObjects:[sdo]});		
	}
	//-->
</script>

