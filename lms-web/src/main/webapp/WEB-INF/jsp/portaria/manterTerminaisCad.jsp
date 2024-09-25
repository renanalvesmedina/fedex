<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.portaria.manterTerminaisAction">
	<adsm:form action="/portaria/manterTerminais" idProperty="idTerminal" onDataLoadCallBack="pageLoad">
		<adsm:hidden property="tpEmpresa" serializable="false" value="M"/>
		<adsm:lookup service="lms.portaria.manterTerminaisAction.findLookupFilial" dataType="text"
					 property="filial"  criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
					 width="85%" action="/municipios/manterFiliais" idProperty="idFilial" required="true">
        	<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
        	<adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
        	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="40" disabled="true" serializable="false"/>
        </adsm:lookup> 
		<adsm:hidden property="pessoa.idPessoa"/>
        <adsm:textbox dataType="text" property="pessoa.nmPessoa" size="52" maxLength="50" label="descricao" width="65%" required="true"/>
	   	<adsm:textarea property="obTerminal" label="observacao" maxLength="500" rows="5" width="85%" columns="60"/>
		
		<adsm:range label="vigencia" width="65%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
        
        
		<adsm:section caption="espacoFisicoTerminal" />
		 
        <adsm:textbox dataType="decimal" mask="#,###,###,###,###,##0.00" property="nrAreaTotal" size="24" label="areaTotal"  labelWidth="15%" width="35%" unit="m2" />
        <adsm:textbox dataType="decimal" mask="#,###,###,###,###,##0.00" property="nrAreaArmazenagem" size="24"  label="areaArmazenagem"  labelWidth="20%" width="30%" unit="m2"/>
		<adsm:textbox dataType="integer" property="nrDocas" size="24" label="numeroDocas" labelWidth="15%" width="35%" disabled="true"/>
        <adsm:textbox dataType="integer" property="nrBoxes" size="24" label="numeroBoxes" labelWidth="20%" width="30%" disabled="true"/>
		<Script>
			<!--
				var labelPessoa = "<adsm:label key="terminal"/>";
		    //-->
		</Script>

	<adsm:buttonBar>
			<adsm:button caption="portarias" id="portariasButton" action="portaria/manterPortarias" cmd="main">
				<adsm:linkProperty src="filial.idFilial" target="terminal.filial.idFilial"/>
				<adsm:linkProperty src="filial.sgFilial" target="terminal.filial.sgFilial"/>
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="terminal.filial.pessoa.nmFantasia"/>
				<adsm:linkProperty src="idTerminal" target="idTerminalTemp"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="dsTerminalTemp"/>
				<adsm:linkProperty src="dtVigenciaInicial" target="terminal.dtVigenciaInicial"/>
				<adsm:linkProperty src="dtVigenciaFinal" target="terminal.dtVigenciaFinal"/>
			</adsm:button>
			<adsm:button caption="docas" id="docasButton" action="portaria/manterDocas" cmd="main">
				<adsm:linkProperty src="filial.idFilial" target="terminal.filial.idFilial"/>
				<adsm:linkProperty src="filial.sgFilial" target="terminal.filial.sgFilial"/>
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="terminal.filial.pessoa.nmFantasia"/>
				<adsm:linkProperty src="idTerminal" target="idTerminalTemp"/>
				<adsm:linkProperty src="dtVigenciaInicial" target="terminal.dtVigenciaInicial"/>
				<adsm:linkProperty src="dtVigenciaFinal" target="terminal.dtVigenciaFinal"/>
			</adsm:button>
			<adsm:button id="contatosButton" caption="contatos" onclick="parent.parent.redirectPage('configuracoes/manterContatos.do?cmd=main' + buildLinkPropertiesQueryString_default());"/>
			<adsm:button id="enderecosButton" onclick="parent.parent.redirectPage('configuracoes/manterEnderecoPessoa.do?cmd=main' + buildLinkPropertiesQueryString_default());" caption="enderecos"/>
			<adsm:storeButton id="storeButton" callbackProperty="afterStore"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
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

	var sdo = createServiceDataObject("lms.portaria.manterTerminaisAction.findFilialUsuarioLogado","findFilialCallBack",null);
	xmit({serviceDataObjects:[sdo]});
	
	function setValues() {
		if (idFilialLogado != undefined &&
			sgFilialLogado != undefined &&
			nmFilialLogado != undefined &&
			document.getElementById("filial.idFilial").masterLink != "true") {
			setElementValue("filial.idFilial",idFilialLogado);
			setElementValue("filial.sgFilial",sgFilialLogado);
			setElementValue("filial.pessoa.nmFantasia",nmFilialLogado);
		}
		
	}
	function initWindow(eventObj) {
		if ((eventObj.name == "newButton_click") || (eventObj.name == "removeButton") || (eventObj.name == "tab_click")) {
			setValues();
			enabledFields();
		}
	}
	
	function buildLinkPropertiesQueryString_default() {
		var	qs  = "&pessoa.idPessoa=" + document.getElementById("idTerminal").value;
			qs += "&pessoa.nmPessoa=" + document.getElementById("pessoa.nmPessoa").value;
			qs += "&labelPessoaTemp=" + labelPessoa;
		return qs;
		/*
			qs += "&pessoa.nrIdentificacao=" + document.getElementById("pessoa.nrIdentificacao").value;
		*/
	}
	
	function afterStore_cb(data,exception,key) {
    	store_cb(data,exception,key);
		if (exception == undefined) {
			acaoVigencia(data);
			setFocusOnNewButton();
			habilitaBotoes();
		}
    }  
    
    function habilitaBotoes(){
    	setDisabled("portariasButton", false);
    	setDisabled("docasButton", false);
    	setDisabled("contatosButton", false);
    	setDisabled("enderecosButton", false);
    }

	function acaoVigencia(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			  enabledFields();
			  setDisabled("removeButton",false);
			  setFocusOnFirstFocusableField();
		}else if (acaoVigenciaAtual == 1) {
		      setDisabled(document,true);
		      setDisabled("storeButton",false);
		      setDisabled("newButton",false);
		      setDisabled("dtVigenciaFinal",false);
		      setDisabled("nrAreaTotal",false);
		      setDisabled("nrAreaArmazenagem",false);
		      setDisabled("obTerminal",false);
		      setFocusOnFirstFocusableField();
		}else if (acaoVigenciaAtual == 2) {
		      setDisabled(document,true);
		      setDisabled("newButton",false);
		      setFocusOnNewButton();
		}
	}
    
    /*
     *	Padrão de vigencias
     */
    function pageLoad_cb(data,error) {
		onDataLoad_cb(data,error);
		acaoVigencia(data);

		setDisabled("enderecosButton",false);
		setDisabled("contatosButton",false);
		setDisabled("docasButton",false);
		setDisabled("portariasButton",false);	
	}
	function enabledFields() {
		setDisabled(document,false);
		setDisabled("filial.pessoa.nmFantasia",true);
		setDisabled("nrDocas",true);
		setDisabled("nrBoxes",true);
		//Botões
		setDisabled("removeButton",true);
		setDisabled("enderecosButton",true);
		setDisabled("contatosButton",true);
		setDisabled("docasButton",true);
		setDisabled("portariasButton",true);	
		setFocusOnFirstFocusableField();
	}

//-->
</script>
