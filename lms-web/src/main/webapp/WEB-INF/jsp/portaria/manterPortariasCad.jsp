<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function portariaPageLoad_cb(data){
		onPageLoad_cb(data);
		if (getElementValue("idTerminalTemp") != ""){
			notifyElementListeners({e:document.getElementById("terminal.filial.idFilial")});
			document.getElementById("terminal.idTerminal").masterLink = "true";
			setDisabled("terminal.idTerminal", true);
		}
		if (document.getElementById("terminal.filial.idFilial").masterLink != "true") {
			var sdo = createServiceDataObject("lms.portaria.manterTerminaisAction.findFilialUsuarioLogado","findFilialCallBack",null);
			xmit({serviceDataObjects:[sdo]});
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
		terminal_idTerminal_cb(data);
		
		if (data != undefined) {
			setaMascaraVigenciaTerminal(data);
		}
		
		if (getElementValue("idTerminalTemp") != "")
			setComboBoxElementValue(document.getElementById("terminal.idTerminal"), getElementValue("idTerminalTemp"),
									getElementValue("idTerminalTemp"), getElementValue("dsTerminalTemp"));
	}
		
//-->
</script>
<adsm:window service="lms.portaria.manterPortariasAction" onPageLoadCallBack="portariaPageLoad" >
	<adsm:form action="/portaria/manterPortarias" idProperty="idPortaria" onDataLoadCallBack="portariaLoad" >
		<adsm:hidden property="empresa.tpEmpresa" value="M" />
		<adsm:lookup service="lms.portaria.manterPortariasAction.findLookupFilial" dataType="text"
				property="terminal.filial"  criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
				width="85%" action="/municipios/manterFiliais" idProperty="idFilial" required="true" >
        	<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="terminal.filial.pessoa.nmFantasia"/>
        	<adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="empresa.tpEmpresa"/>
        	<adsm:textbox dataType="text" property="terminal.filial.pessoa.nmFantasia" size="29" disabled="true" serializable="false"/>
        </adsm:lookup>

		<adsm:hidden property="idTerminalTemp"/>
		<adsm:hidden property="dsTerminalTemp" serializable="false"/>
		<adsm:combobox property="terminal.idTerminal" optionProperty="idTerminal" optionLabelProperty="pessoa.nmPessoa"
				service="lms.portaria.manterPortariasAction.findTerminaisComboVigentes" onDataLoadCallBack="setaTerminal"
				label="terminal" boxWidth="222" required="true" >
			<adsm:propertyMapping criteriaProperty="terminal.filial.idFilial" modelProperty="filial.idFilial" />
			<adsm:propertyMapping criteriaProperty="idTerminalTemp" modelProperty="idTerminal" />
			<adsm:propertyMapping relatedProperty="terminal.dtVigenciaInicial" modelProperty="dtVigenciaInicial" />
			<adsm:propertyMapping relatedProperty="terminal.dtVigenciaFinal" modelProperty="dtVigenciaFinal" />
		</adsm:combobox>
			
		<adsm:range label="vigenciaTerminal" >
             <adsm:textbox dataType="JTDate" property="terminal.dtVigenciaInicial" disabled="true" picker="false" serializable="false"/>
             <adsm:textbox dataType="JTDate" property="terminal.dtVigenciaFinal" disabled="true" picker="false" serializable="false"/>
        </adsm:range>		
		
		<adsm:checkbox label="portariaPadrao" property="blPadraoFilial" />
        <adsm:textbox dataType="integer" property="nrPortaria" size="4" maxLength="2" label="numero" required="true" />
        
        <adsm:textbox dataType="text" property="dsPortaria" size="40" maxLength="60" label="descricao" required="true" width="85%" />
        
	   	<adsm:combobox property="tpFuncao" label="funcao" required="true" domain="DM_FUNCAO_PORTARIA" width="35%" />
	   	
		<adsm:range label="vigencia" width="35%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
        </adsm:range>
        
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton" callbackProperty="afterStore" />
			<adsm:newButton id="newButton" />
			<adsm:removeButton id="removeButton" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--

	var idFilialLogado;
	var sgFilialLogado;
	var nmFilialLogado;
	
	function findFilialCallBack_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data != undefined) {
			idFilialLogado = getNestedBeanPropertyValue(data,"idFilial");
			sgFilialLogado = getNestedBeanPropertyValue(data,"sgFilial");
			nmFilialLogado = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
			setValues();
		}
	}

	function setValues() {
		if (idFilialLogado != undefined &&
				sgFilialLogado != undefined &&
				nmFilialLogado != undefined) {
			setElementValue("terminal.filial.idFilial",idFilialLogado);
			setElementValue("terminal.filial.sgFilial",sgFilialLogado);
			setElementValue("terminal.filial.pessoa.nmFantasia",nmFilialLogado);
			notifyElementListeners({e:document.getElementById("terminal.filial.idFilial")});
		}
	}
	
	function initWindow(eventObj) {
		if ((eventObj.name != "storeButton") && (eventObj.name != "gridRow_click")) {
			setValues();
			estadoNovo("newButton");
		}
	}
	
	function portariaLoad_cb(data,error) {
		onDataLoad_cb(data,error);
		estadoTelaAcaoVigencia(data);
	}

	function habilitarCampos() {
		setDisabled("dtVigenciaFinal",false);
		setDisabled("newButton",false);
		setDisabled("storeButton",false);
		setDisabled("removeButton",false);
	}
	
	function afterStore_cb(data,error) {
		store_cb(data,error);
		if (error == undefined) {
			estadoTelaAcaoVigencia(data);
			setFocusOnNewButton();
		}
	}
	
	function estadoNovo(event) {
		setDisabled(document,false);
		
		setDisabled("terminal.idTerminal", document.getElementById("terminal.idTerminal").masterLink == "true");
		setDisabled("terminal.filial.pessoa.nmFantasia",true);
		setDisabled("terminal.dtVigenciaInicial",true);
		setDisabled("terminal.dtVigenciaFinal",true);
		setDisabled("removeButton",true);
		
		if (event != undefined && event == "newButton")
			setFocusOnFirstFocusableField();
	}

	function estadoTelaAcaoVigencia(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data,"acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			setDisabled("removeButton",false);
			setFocusOnFirstFocusableField();
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document,true);
			habilitarCampos();
			setDisabled("removeButton",true);
			setFocusOnFirstFocusableField();
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document,true);
			setDisabled("newButton",false);
			setDisabled("storeButton",true);
			setDisabled("removeButton",true);
			setFocusOnNewButton();
		}
	}
	
//	
--></script>