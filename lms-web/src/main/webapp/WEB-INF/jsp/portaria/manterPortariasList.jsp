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
		if (eventObj.name == "cleanButton_click")
			setValues();
	}
//-->
</script>
<adsm:window service="lms.portaria.manterPortariasAction" onPageLoadCallBack="portariaPageLoad" >
	<adsm:form action="/portaria/manterPortarias" idProperty="idPortaria" >
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false" />
		<adsm:lookup service="lms.portaria.manterPortariasAction.findLookupFilial" dataType="text"
				property="terminal.filial"  criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
				width="85%" action="/municipios/manterFiliais" idProperty="idFilial" >
        	<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="terminal.filial.pessoa.nmFantasia"/>
        	<adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="empresa.tpEmpresa"/>
        	<adsm:textbox dataType="text" property="terminal.filial.pessoa.nmFantasia" size="29" disabled="true" serializable="false"/>
        </adsm:lookup>

		<adsm:hidden property="idTerminalTemp" />
		<adsm:hidden property="dsTerminalTemp" />
		<adsm:combobox property="terminal.idTerminal" optionProperty="idTerminal" optionLabelProperty="pessoa.nmPessoa"
				service="lms.portaria.manterPortariasAction.findTerminaisCombo" onDataLoadCallBack="setaTerminal"
				label="terminal" boxWidth="222" >
			<adsm:propertyMapping criteriaProperty="terminal.filial.idFilial" modelProperty="filial.idFilial" />
			<adsm:propertyMapping relatedProperty="terminal.dtVigenciaInicial" modelProperty="dtVigenciaInicial" />
			<adsm:propertyMapping relatedProperty="terminal.dtVigenciaFinal" modelProperty="dtVigenciaFinal" />
		</adsm:combobox>
			
		<adsm:range label="vigenciaTerminal" >
             <adsm:textbox dataType="JTDate" property="terminal.dtVigenciaInicial" disabled="true" picker="false" serializable="false"/>
             <adsm:textbox dataType="JTDate" property="terminal.dtVigenciaFinal" disabled="true" picker="false" serializable="false"/>
        </adsm:range>
        
        <adsm:combobox label="portariaPadrao" property="blPadraoFilial" domain="DM_SIM_NAO" />
        <adsm:textbox dataType="integer" property="nrPortaria" size="4" maxLength="2" label="numero" />
        
        <adsm:textbox dataType="text" property="dsPortaria" size="40" maxLength="60" label="descricao" width="85%" />
        
	   	<adsm:combobox property="tpFuncao" label="funcao" domain="DM_FUNCAO_PORTARIA" width="35%" />

        <adsm:range label="vigencia" width="35%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
        </adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="portaria" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="portaria" idProperty="idPortaria" unique="true" rows="10"
			defaultOrder="terminal_filial_.sgFilial,terminal_pessoa_.nmPessoa,nrPortaria,dtVigenciaInicial" >
		<adsm:gridColumn title="filial" property="terminal.filial.sgFilial" width="5%" />
		<adsm:gridColumn title="terminal" property="terminal.pessoa.nmPessoa" width="20%" />	
		<adsm:gridColumn title="portaria" property="nrPortaria" width="8%" dataType="integer" />
		<adsm:gridColumn title="descricao" property="dsPortaria" />
		<adsm:gridColumn title="funcao" property="tpFuncao" isDomain="true" width="12%"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="13%" dataType="JTDate" />			
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="12%" dataType="JTDate" />
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
