<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
<!--
	var idMoedaPais = null;
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		setMasterLink(document,true);
		var parameters = new Array();
		setNestedBeanPropertyValue(parameters,"idParamSimulacaoHistorica",getElementValue("idParamSimulacaoHistorica"))
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.aplicarReajustesTabelasFreteTabelasAction.findById", "xmitPageLoad", parameters);
      	xmit({serviceDataObjects:[sdo]});
	}
	
	function xmitPageLoad_cb(data) {
		if (getNestedBeanPropertyValue(data,"disabledEfetivar") != "true") {
			fillFormWithFormBeanData(document.form.tabIndex, data);
			var blPercentual = getNestedBeanPropertyValue(data,"blPercentual");
			if (blPercentual != undefined) 
				for (var x = 0; x < 4; x++)
					if (getElementValue("ncParcelaSimulacao:" + x + ".pcReajuste") != "")
						document.getElementById("blPercentual" + x).value = blPercentual;
			idMoedaPais = getNestedBeanPropertyValue(data,"moedaPais.idMoedaPais");
	      	carregaMoedas();
	      	resetValue("filiaisF_filial.idFilial");
	      	setDisabled("efetivarReajuste",false);
	    }else
	    	setDisabled("efetivarReajuste",true);
	    setDisabled("limpar",false);
	}
	
	function efetivarReajusteOnClick() {
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.aplicarReajustesTabelasFreteTabelasAction.efetivarReajuste", "dataLoadEfetivaReajuste", buildFormBeanFromForm(document.form));
		xmit({serviceDataObjects:[sdo]});
	}
	function dataLoadEfetivaReajuste_cb(data,exception,key) { 
		if (exception) {
			alert(exception);
			setFocus(document.getElementById("limpar"), false);
			return;
		}
		showSuccessMessage();
		setFocus(document.getElementById("limpar"), false);
	}
	
	function storeCustom_cb(data,exception,key) {
		if (exception != undefined) {
			if  (key == "LMS-25018") {
				if (confirm(exception)) {
					setElementValue("blAprovado","true");
					storeButtonScript('lms.fretecarreteirocoletaentrega.aplicarReajustesTabelasFreteTabelasAction.store', 'storeCustom', document.form);
					resetValue("blAprovado");
				}
			}else{
				alert(exception);
				setFocusOnFirstFocusableField();
			}	
			return;
		}else{
			setDisabled("efetivarReajuste",false);
			fillFormWithFormBeanData(document.form.tabIndex, data);
			reportButtonScript('lms.fretecarreteirocoletaentrega.aplicarReajustesTabelasFreteTabelasAction', 'openPdf', document.form);
		}
	}
	
	function initWindow(eventObj) {
		setDisabled("limpar",false);
	}
	
	
	
	function changeP() {
		var flag = moedaPais_pais_nmPaisOnChangeHandler();
		if (flag == true && getElementValue("moedaPais.pais.nmPais") == "")
		   resetValue("moedaPais.idMoedaPais");
		return flag;
	}
	
	function dataLoadP_cb(data) {
		return dataLoadP_default(lookupExactMatch({e:document.getElementById("moedaPais.pais.idPais"), data:data, callBack:"dataLoadP_LikeEnd"}));
	}
	
	function dataLoadP_LikeEnd_cb(data,exception) {
		return dataLoadP_default(moedaPais_pais_nmPais_likeEndMatch_cb(data));
	}
	
	function dataLoadP_default(flag) {
		if (flag == true)
			carregaMoedas();
		return flag;
	}
	
	function popUpP(data) {
		setElementValue("moedaPais.pais.idPais",getNestedBeanPropertyValue(data,"idPais"));
		carregaMoedas();
		return true;
	}
	
	
	function carregaMoedas() {
	    var e = document.getElementById("moedaPais.idMoedaPais");
	    var sdo = createServiceDataObject(e.service, e.callBack, {pais:{idPais:getElementValue("moedaPais.pais.idPais")}});
	    xmit({serviceDataObjects:[sdo]});
	    notifyElementListeners({e:document.getElementById("moedaPais.pais.idPais")});
	}
	
	function dataLoadMoeda_cb(data) {
		moedaPais_idMoedaPais_cb(data);
		for (var x = 0; x < data.length; x++) {
			if ((idMoedaPais != null && getNestedBeanPropertyValue(data[x],"idMoedaPais") == idMoedaPais)
				||(idMoedaPais == null && getNestedBeanPropertyValue(data[x],"blIndicadorMaisUtilizada") == "true")) {
				document.getElementById("moedaPais.idMoedaPais").selectedIndex = x + 1;
				idMoedaPais = null;
				break;
			}
		}
		comboboxChange({e:document.getElementById("moedaPais.idMoedaPais")});
	}
	function contentChangeF(eventObj) {
		resetValue("filiaisT");
		resetValue("filiaisT_filial.idFilial");
		resetValue("filiaisT_nmFilial");
		resetValue("filiaisT_sgFilial");
	}
	function contentChangeT(eventObj) {
		resetValue("filiaisF");
		resetValue("filiaisF_filial.idFilial");
		resetValue("filiaisF_nmFilial");	
		resetValue("filiaisF_sgFilial");
	}
	function remove() {
		var tabGroup = getTabGroup(this.document);
		var tab = tabGroup.getTab("proc");
		tab.setChanged(false);
		if (window.confirm(i18NLabel.getLabel("LMS-25038"))) { 
			var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.aplicarReajustesTabelasFreteTabelasAction.removeById", "removeById", {e:0}); 
			xmit({serviceDataObjects:[sdo]});
		}
	}
//-->
</script>
<adsm:window service="lms.fretecarreteirocoletaentrega.aplicarReajustesTabelasFreteTabelasAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/freteCarreteiroColetaEntrega/aplicarReajustesTabelasFreteTabelas" id="form" idProperty="idSimulacaoReajusteFreteCe">
		<adsm:hidden property="idParamSimulacaoHistorica" serializable="false"/>
		<adsm:hidden property="tpSituacao" serializable="false" value="A"/>
		<adsm:hidden property="blAprovado"/>
		<adsm:i18nLabels>
			<adsm:include key="LMS-25038"/>
		</adsm:i18nLabels>
		<adsm:combobox
			property="tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega" label="tipoTabela" labelWidth="19%" boxWidth="180" onlyActiveValues="true"
			service="lms.fretecarreteirocoletaentrega.aplicarReajustesTabelasFreteTabelasAction.findComboTipoTabelaColetaEntrega"
			optionProperty="idTipoTabelaColetaEntrega" optionLabelProperty="dsTipoTabelaColetaEntrega" width="80%" required="true"/>

		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" boxWidth="180" cellStyle="vertical-align:bottom;" labelWidth="19%"
			optionProperty="idTipoMeioTransporte" optionLabelProperty="dsTipoMeioTransporte" label="tipoMeioTransporte" width="80%" onlyActiveValues="true"
			service="lms.fretecarreteirocoletaentrega.aplicarReajustesTabelasFreteTabelasAction.findComboTipoMeioTransporte" required="true"/>


		<adsm:lookup property="moedaPais.pais"
					idProperty="idPais"
					service="lms.municipios.emitirPostoPassagemAction.findLookupPais"
					dataType="text"
					criteriaProperty="nmPais"
					label="pais"
					size="35"
					maxLength="60"
					action="/municipios/manterPaises"
					exactMatch="false"
					minLengthForAutoPopUpSearch="3"
					onDataLoadCallBack="dataLoadP"
					onPopupSetValue="popUpP"
					labelWidth="19%"
					width="83%"
					onchange="return changeP();"
					required="true">
					<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
		</adsm:lookup>
		
		<adsm:combobox property="moedaPais.idMoedaPais" service="lms.fretecarreteirocoletaentrega.aplicarReajustesTabelasFreteTabelasAction.findComboMoeda"
				 optionLabelProperty="moeda.siglaSimbolo" optionProperty="idMoedaPais" label="moeda" 
				 onDataLoadCallBack="dataLoadMoeda" labelWidth="19%" width="33%" autoLoad="false" required="true">
			<adsm:propertyMapping criteriaProperty="moedaPais.pais.idPais" modelProperty="pais.idPais"/>
			<adsm:propertyMapping relatedProperty="descricaoMoeda" modelProperty="moeda.siglaSimbolo"/>
			
		</adsm:combobox>
		<adsm:hidden property="descricaoMoeda" serializable="true"/>
				
		<adsm:range label="vigenciaReajuste" labelWidth="19%" width="81%">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal"/>
		</adsm:range>

		<adsm:listbox property="filiaisF" optionProperty="sgFilial" optionLabelProperty="dsRota" label="reajusteNaoSeAplicaPara" size="4"
				 onContentChange="contentChangeF"
				 labelWidth="19%" width="31%" showOrderControls="false" boxWidth="180" showIndex="false" serializable="true">
			<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" exactMatch="false"
					dataType="text" size="3" maxLength="3"  minLengthForAutoPopUpSearch="3" action="/municipios/manterFiliais"
					service="lms.fretecarreteirocoletaentrega.aplicarReajustesTabelasFreteTabelasAction.findLookupFilial">
				<adsm:propertyMapping relatedProperty="filiaisF_nmFilial" modelProperty="pessoa.nmFantasia"/>
				<adsm:textbox dataType="text" property="nmFilial" disabled="true" serializable="false"/>
			</adsm:lookup>   
		</adsm:listbox>
		
		<adsm:listbox property="filiaisT" optionProperty="sgFilial" optionLabelProperty="dsRota" label="reajusteSeAplicaPara" size="4"
				 labelWidth="19%" width="31%" showOrderControls="false" boxWidth="180" showIndex="false" serializable="true"
				 onContentChange="contentChangeT">
			<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" exactMatch="false"
					dataType="text" size="3" maxLength="3"  minLengthForAutoPopUpSearch="3" action="/municipios/manterFiliais"
					service="lms.fretecarreteirocoletaentrega.aplicarReajustesTabelasFreteTabelasAction.findLookupFilial">
				<adsm:propertyMapping relatedProperty="filiaisT_nmFilial" modelProperty="pessoa.nmFantasia"/>
				<adsm:textbox dataType="text" property="nmFilial" disabled="true" serializable="false"/>
			</adsm:lookup>   
		</adsm:listbox>
		
		<adsm:label key="branco" width="100%" />
				<adsm:section caption="parcelas"/>
				<adsm:label key="parcela" width="20%" style="height:23px;"/>
				<adsm:label key="tipoReajuste" width="20%" style="height:23px;"/>
				<adsm:label key="valorReajuste" width="20%" style="height:23px;"/>
				<adsm:label key="branco" width="40%" style="height:23px;border:none;"/>
				
				<adsm:label key="diaria" width="20%" style="height:23px"/>
				<adsm:combobox property="blPercentual0" domain="DT_TIPO_SIMULACAO_REAJUSTE" width="20%"/>
				<adsm:textbox dataType="currency" property="ncParcelaSimulacao:0.pcReajuste" size="10" width="60%"/>

				<adsm:label key="evento" width="20%" style="height:23px"/>
				<adsm:combobox property="blPercentual1" domain="DT_TIPO_SIMULACAO_REAJUSTE" width="20%"/>
				<adsm:textbox dataType="currency" property="ncParcelaSimulacao:1.pcReajuste" size="10" width="60%"/>

				<adsm:label key="fracaoPeso" width="20%" style="height:23px"/>			
				<adsm:combobox property="blPercentual2" domain="DT_TIPO_SIMULACAO_REAJUSTE" width="20%"/>
				<adsm:textbox dataType="currency" property="ncParcelaSimulacao:2.pcReajuste" size="10" width="60%"/>
				
				<adsm:label key="kmExcedente" width="20%" style="height:24px"/>
				<adsm:combobox property="blPercentual3" domain="DT_TIPO_SIMULACAO_REAJUSTE" width="20%"/>
				<adsm:textbox dataType="currency" property="ncParcelaSimulacao:3.pcReajuste" size="10" width="60%"/>
		<adsm:buttonBar>
 			<adsm:storeButton caption="emitir" callbackProperty="storeCustom"/>
			<adsm:button caption="efetivarReajuste" id="efetivarReajuste" onclick="efetivarReajusteOnClick();" disabled="false"/>
			<adsm:button onclick="remove()" disabled="false"  caption="limpar" id="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
	document.getElementById("moedaPais.pais.nmPais").serializable="true";
//-->
</script>
