<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterTarifaPostoPassagemAction" onPageLoadCallBack="pageLoadReal">
	<adsm:form action="/municipios/manterTarifaPostoPassagem" idProperty="idTarifaPostoPassagem" onDataLoadCallBack="pageLoad" newService="lms.municipios.manterTarifaPostoPassagemAction.newMaster">
 
		<adsm:textbox dataType="text" property="tpPosto" label="tipo" maxLength="10" size="35" disabled="true" labelWidth="17%" width="33%" serializable="false" />
		<adsm:textbox dataType="text" property="localizacao" label="localizacaoMunicipio" maxLength="10" size="35" disabled="true" labelWidth="17%" width="33%"  serializable="false"/>
		
		<adsm:textbox dataType="text" property="rodovia" label="rodovia" maxLength="10" size="35" disabled="true" labelWidth="17%" width="33%" serializable="false"/>
		
		<adsm:textbox dataType="text" property="sentido" label="sentidoCobranca" maxLength="10" size="35" disabled="true" labelWidth="17%" width="33%" serializable="false"/>
		<adsm:hidden property="pais.idPais" serializable="false"/>
	
		<adsm:combobox property="tpFormaCobranca" label="formaCobranca" onchange="tpForma_change(this);"  domain="DM_FORMA_COBRANCA_POSTO_PASSAGEM" width="73%" labelWidth="17%" required="true"/>
		
		<adsm:combobox property="valorTarifaPostoPassagem.moedaPais.idMoedaPais" disabled="true" optionProperty="idMoedaPais"  optionLabelProperty="moeda.siglaSimbolo" service="lms.configuracoes.moedaPaisService.findMoedaByPais" label="moeda" width="33%" labelWidth="17%" onDataLoadCallBack="verificaMoedaPais"/>
		
		<adsm:hidden property="postoPassagem.idPostoPassagem"/>
		<adsm:hidden property="acaoVigenciaAtual" serializable="false"/>
				
		<adsm:textbox dataType="decimal" property="valorTarifaPostoPassagem.vlTarifa" disabled="true" label="valor" labelWidth="17%" width="33%" mask="#,###,###,###,###,##0.00" minValue="0.01"/>
		<adsm:hidden property="valorTarifaPostoPassagem.idValorTarifaPostoPassagem" serializable="true"/>
		<adsm:range label="vigencia" labelWidth="17%" width="83%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:button id="__buttonBar:0_1" caption="tiposPagamentoPostos" action="/municipios/manterTipoPagamentoPosto" cmd="main">
				<adsm:linkProperty src="postoPassagem.idPostoPassagem" target="postoPassagem.idPostoPassagem"/>
				<adsm:linkProperty src="tpPosto" target="tpPosto"/>
				<adsm:linkProperty src="localizacao" target="localizacao"/>
				<adsm:linkProperty src="rodovia" target="rodovia"/>
				<adsm:linkProperty src="sentido" target="sentido"/>
				<adsm:linkProperty src="pais.idPais" target="pais.idPais"/>
			</adsm:button>
			<adsm:storeButton caption="salvar" id="storeButton" service="lms.municipios.manterTarifaPostoPassagemAction.store" callbackProperty="storeAdvanced"/>
			<adsm:newButton id="__buttonBar:0.newButton"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<Script>
<!--
	var moedasPadraoId = null;
	function tpForma_change(field) {
	    var option = true;
		if (field.value == "FI" || field.value == "EI")
			option = false;
		
		if (field.value == "") {
			disabledALL();
			return;
		}
		var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("val", !option);

		setElementValue("valorTarifaPostoPassagem.moedaPais.idMoedaPais","");
		setElementValue("valorTarifaPostoPassagem.vlTarifa","");
			
		setDisabled("valorTarifaPostoPassagem.moedaPais.idMoedaPais",option);
		setDisabled("valorTarifaPostoPassagem.vlTarifa",option);
		document.getElementById("valorTarifaPostoPassagem.moedaPais.idMoedaPais").required = new String(!option);
		document.getElementById("valorTarifaPostoPassagem.vlTarifa").required = new String(!option);
		if ((field.value == "FI" || field.value == "EI") && moedasPadraoId != undefined && moedasPadraoId != null)
			document.getElementById("valorTarifaPostoPassagem.moedaPais.idMoedaPais").value = moedasPadraoId;
	}

	function disabledALL() {
		var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("val",true);

		setElementValue("valorTarifaPostoPassagem.moedaPais.idMoedaPais","");
		setElementValue("valorTarifaPostoPassagem.vlTarifa","");
			
		setDisabled("valorTarifaPostoPassagem.moedaPais.idMoedaPais",true);
		setDisabled("valorTarifaPostoPassagem.vlTarifa",true);
		document.getElementById("valorTarifaPostoPassagem.moedaPais.idMoedaPais").required = "false";
		document.getElementById("valorTarifaPostoPassagem.vlTarifa").required = "false";
	}


	function pageLoad_cb(data,error) {
		onDataLoad_cb(data,error);
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		acaoVigencia(data);
		if (getNestedBeanPropertyValue(data,"valorTarifaPostoPassagem") == undefined) {
			var tabGroup = getTabGroup(this.document);
				tabGroup.setDisabledTab("val",false);

			document.getElementById("valorTarifaPostoPassagem.moedaPais.idMoedaPais").required = "false";
			document.getElementById("valorTarifaPostoPassagem.vlTarifa").required = "false";
			if (acaoVigenciaAtual == 0)
				setFocus(document.getElementById("dtVigenciaInicial"));
		}else{
			if (acaoVigenciaAtual == 0) {
				setDisabled("valorTarifaPostoPassagem.moedaPais.idMoedaPais",false);
				setDisabled("valorTarifaPostoPassagem.vlTarifa",false);
				document.getElementById("valorTarifaPostoPassagem.moedaPais.idMoedaPais").required = "true";
				document.getElementById("valorTarifaPostoPassagem.vlTarifa").required = "true";
				setFocus("valorTarifaPostoPassagem.moedaPais.idMoedaPais");
			}else{
				document.getElementById("valorTarifaPostoPassagem.moedaPais.idMoedaPais").required = "false";
				document.getElementById("valorTarifaPostoPassagem.vlTarifa").required = "false";
			}
		}
		setDisabled("__buttonBar:0_1",false);
	}
	
	function acaoVigencia(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			  enabledFields();
		}else if (acaoVigenciaAtual == 1) {
		      setDisabled(document,true);
		      setDisabled("dtVigenciaFinal",false);
		      setDisabled("storeButton",false);
		      setDisabled("__buttonBar:0.newButton",false);
		      setFocusOnFirstFocusableField();
		}else if (acaoVigenciaAtual == 2) {
		   	 setDisabled(document,true);
		     setDisabled("__buttonBar:0.newButton",false);
		     setFocusOnNewButton();
		}
		setDisabled("tpFormaCobranca",true);
		setDisabled("__buttonBar:0_1",false);
	}
	function enabledFields() {
	    setDisabled("tpFormaCobranca",false);
		setDisabled("dtVigenciaInicial",false);
		setDisabled("dtVigenciaFinal",false);
		setDisabled("__buttonBar:0_1",false);

		var option = true;
		if (getElementValue("tpFormaCobranca") == "FI" || getElementValue("tpFormaCobranca") == "EI")
			option = false;

		setDisabled("valorTarifaPostoPassagem.moedaPais.idMoedaPais",option);
		setDisabled("valorTarifaPostoPassagem.vlTarifa",option);
		
		setFocusOnFirstFocusableField();
	}
	
	function initWindow(eventObj) {
		if ((eventObj.name == "removeButton") || (eventObj.name == "newButton_click") || (eventObj.name == "tab_click" && getElementValue("idTarifaPostoPassagem") == "")) {
			setDisabled("__buttonBar:0_1",false);
			enabledFields();
			if (eventObj.name != "tab_click") {
				var tabGroup = getTabGroup(this.document);
					tabGroup.setDisabledTab("val",true);
				setFocus("tpFormaCobranca",false);
			}
			setDisabled("__buttonBar:0.removeButton",getElementValue("acaoVigenciaAtual") != "0");
			setDisabled("storeButton",getElementValue("acaoVigenciaAtual") == "2");
		}

		if (eventObj.name == "tab_click") {
			setDisabled("__buttonBar:0.removeButton",getElementValue("acaoVigenciaAtual") != "0");
			setDisabled("storeButton",getElementValue("acaoVigenciaAtual") == "2");
		}
	}
	
	function pageLoadReal_cb(data,excetpion) {
		onPageLoad_cb(data,excetpion);
		//notifyElementListeners({e:document.getElementById("pais.idPais")});
		if (getElementValue("pais.idPais") != undefined) {
			var remoteCall = {serviceDataObjects:new Array()};
				remoteCall.serviceDataObjects.push(createServiceDataObject("lms.municipios.manterTarifaPostoPassagemAction.findComboMoedaPais", "verificaMoedaPais", { moedaPais:{pais:{idPais:getElementValue("pais.idPais") }}}));
			xmit(remoteCall);
		}  
	}
	
	function storeAdvanced_cb(data,errorMsg, errorKey) {
		store_cb(data, errorMsg, errorKey);
		if (errorMsg == undefined) {
			setDisabled("tpFormaCobranca",true);
	    	acaoVigencia(data);
			setFocusOnNewButton();
		}
		if (errorKey == "LMS-29035") {
			var tabGroup = getTabGroup(this.document);	
				tabGroup.selectTab('val',{name:'tab_click'});
		}
	}
	
	function verificaMoedaPais_cb(dados) {
		valorTarifaPostoPassagem_moedaPais_idMoedaPais_cb(dados);
		if(dados) {
			for(var i = 0; i < dados.length; i++) {
				var indUtil = getNestedBeanPropertyValue(dados[i], "blIndicadorMaisUtilizada");
				if(indUtil == true || indUtil == 'true'){
					moedasPadraoId = getNestedBeanPropertyValue(dados[i], "idMoedaPais");
					return;
				}
			}
		}
	}
//-->
</Script>


