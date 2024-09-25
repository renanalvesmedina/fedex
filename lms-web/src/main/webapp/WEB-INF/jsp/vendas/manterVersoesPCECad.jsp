<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function pageLoadVigencia_cb(data,error) {
		onDataLoad_cb(data,error);
		acaoVigencia(data);		
		setDisabled("emitirButton",false);
		setDisabled("cliente.idCliente",true);
		document.getElementById("cliente.pessoa.nrIdentificacao").masterLink = "true";
		clienteNotFound(false);
		setFocusOnFirstFocusableField();
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
		      setDisabled("dtVigenciaFinal");
		      setFocusOnFirstFocusableField();
		}else if (acaoVigenciaAtual == 2) {
		      setDisabled(document,true);
		      setDisabled("__buttonBar:0.newButton",false);
		      setFocusOnNewButton();
		}
	}

	function enabledFields() {
		var flag = document.getElementById("cliente.pessoa.nrIdentificacao").masterLink == "true";
		setDisabled(document,false);
		setDisabled("nrVersaoPce",true);
		setDisabled("cliente.pessoa.nmPessoa",true);
		setDisabled("cliente.idCliente",flag);
		setDisabled("emitirButton",getElementValue("idVersaoPce") == "");
		setDisabled("removeButton",getElementValue("acaoVigenciaAtual") != "0");
		setDisabled("dtVigenciaInicial",(getElementValue("acaoVigenciaAtual") == "1" || getElementValue("acaoVigenciaAtual") == "2"));
		setDisabled("dtVigenciaFinal",getElementValue("acaoVigenciaAtual") == "2");
		setDisabled("storeButton",getElementValue("acaoVigenciaAtual") == "2");
		setFocusOnFirstFocusableField();
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "newButton_click" || eventObj.name == "removeButton") {
			document.getElementById("cliente.pessoa.nrIdentificacao").masterLink = "false";
			resetValue("cliente.pessoa.nrIdentificacao");
		}

		if ((eventObj.name == "newButton_click") || eventObj.name == "removeButton" || (eventObj.name == "tab_click")) {
			if (getElementValue("idVersaoPce") == "")
				clienteNotFound((getElementValue("cliente.idCliente") == ""));
			else
				clienteNotFound(false);
			enabledFields();
			if (document.getElementById("cliente.pessoa.nrIdentificacao").masterLink == "true") {
				var tabGroup = getTabGroup(this.document);
				if (tabGroup.getTab("descritivos").getElementById("VersaoDescritivoPce.dataTable").rows.length == 0) {
					setDisabled("cliente.idCliente",false);				
				}
			}
			setDisabled("removeButton",!((getElementValue("idVersaoPce") != "") && (getElementValue("acaoVigenciaAtual") == "" || getElementValue("acaoVigenciaAtual") == "0")));
			setFocusOnFirstFocusableField();
		}
	}
	function afterStore_cb(data,exception,key) {
    	store_cb(data,exception,key);
    	if (key == "LMS-29101") {
    		var tabGroup = getTabGroup(this.document);	
				tabGroup.selectTab('descritivos',{name:'tab_click'});
			return;
    	}
		if (exception == undefined) {
			acaoVigencia(data);
			setFocusOnNewButton();
			var tabGroup = getTabGroup(this.document);
			var tabD = tabGroup.getTab("descritivos");
			tabD.getElementById("_nrversaoPce").value = getNestedBeanPropertyValue(data, "nrVersaoPce");
			clienteNotFound(false);
		}
    }  
    
	function popUpSetC(data) {
		clienteNotFound(false);
		return true;
	}
	function dataLoadC_cb(data,exception) {
		if (exception) {
			alert(exception);
			clienteNotFound(true);
			return;
		}
		if (data == undefined || data.length > 1)
			clienteNotFound(true);
		else
			clienteNotFound(false);
			
		return cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
	}
	function onChangeC() {
		if (getElementValue("cliente.pessoa.nrIdentificacao") == "")
			clienteNotFound(true);
		return cliente_pessoa_nrIdentificacaoOnChangeHandler();
	}
	
	function clienteNotFound(ok) {
		var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("descritivos",ok);
	}
//-->
</script>
<adsm:window service="lms.vendas.manterVersoesPCEAction">
	<adsm:form action="/vendas/manterVersoesPCE" idProperty="idVersaoPce" onDataLoadCallBack="pageLoadVigencia" newService="lms.vendas.manterVersoesPCEAction.newMaster">
		<adsm:lookup service="lms.vendas.manterVersoesPCEAction.findLookupCliente" dataType="text" property="cliente" idProperty="idCliente"
					criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" label="cliente" size="20" maxLength="20" labelWidth="15%" 
					width="85%" action="/vendas/manterDadosIdentificacao" exactMatch="true" required="true" onPopupSetValue="popUpSetC" onDataLoadCallBack="dataLoadC" onchange="return onChangeC()">			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="situacao" modelProperty="tpSituacao"/>
			<adsm:propertyMapping criteriaProperty="tpCliente" modelProperty="tpCliente"/>
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="35" disabled="true" serializable="false"/>
		</adsm:lookup>
		<adsm:hidden property="tpCliente" value="S" serializable="false"/>
		<adsm:hidden property="acaoVigenciaAtual" serializable="false"/>
		<adsm:hidden property="situacao" value="A" serializable="false"/>
		<adsm:textbox maxLength="8" size="8" dataType="integer" property="nrVersaoPce" label="versao" disabled="true"/>
		<adsm:range label="vigencia" width="85%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.vendas.emitirPCEAction" caption="emitir" id="emitirButton"/>
			<adsm:storeButton id="storeButton" callbackProperty="afterStore"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>