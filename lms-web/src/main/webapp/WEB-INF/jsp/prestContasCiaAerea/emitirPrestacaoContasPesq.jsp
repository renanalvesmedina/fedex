<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.prestcontasciaaerea.emitirPrestacaoContasAction" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form action="/prestContasCiaAerea/emitirPrestacaoContas">

		<adsm:i18nLabels>
			<adsm:include key="LMS-37001" />
		</adsm:i18nLabels>

		<adsm:combobox label="companhiaAerea" 
			property="empresa.idEmpresa" 
			service="lms.prestcontasciaaerea.emitirPrestacaoContasAction.findComboCompanhiaAerea" 
			optionLabelProperty="pessoa.nmPessoa" 
			optionProperty="idEmpresa" 
			required="true"
			labelWidth="22%" width="78%"
			onchange="enablePeriodoVenda()" />

		<adsm:hidden property="filial.idFilial"/>
		<adsm:textbox labelWidth="22%" width="78%" dataType="text" label="filial" property="filial.sgFilial" size="3" disabled="true">
		<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="41" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox dataType="JTDate" labelWidth="22%" label="periodoVendas" property="dtInicial" size="10" width="12%" onchange="dtInicialOnChange(this)" disabled="true"/> 
		<adsm:label key="ate" width="4%" style="border:none;"/>
	   	<adsm:textbox dataType="JTDate" property="dtFinal" size="10" width="27%" disabled="true"/>




		<adsm:textbox label="numeroCT" property="nrPrestacaoConta" dataType="integer"  labelWidth="22%" width="78%" maxLength="15" onchange="nrPrestacaoContaOnChange()"/>
			
		<adsm:combobox property="tpFormatoRelatorio" label="formatoRelatorio" domain="DM_FORMATO_RELATORIO" required="true" defaultValue="pdf" labelWidth="22%" width="78%" />
		
		<script>
			var lms37001 = i18NLabel.getLabel("LMS-37001");
		</script>
		
		<adsm:buttonBar>
			<adsm:button caption="visualizar" disabled="false" onclick="visualizarPrestacaoContas(this);" buttonType="reportViewerButton"/>
			<adsm:button caption="limpar" onclick="limpar()" disabled="false" id="btnLimpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function enablePeriodoVenda(){
		var documento = getCurrentDocument("dtInicial", null);
        var obj = getElement("dtInicial", documento);

		resetValue("dtInicial");
		resetValue("dtFinal");
        
		if(getElementValue("empresa.idEmpresa")){
	        setDisabledCalendar(obj, false, documento);
		} else {
			setDisabledCalendar(obj, true, documento);
			setDisabled("nrPrestacaoConta", false);
		}
	}

	function initWindow(eventObj){
		setDisabled('btnLimpar', false);

		if(eventObj.name == 'tab_load' || eventObj.name == 'cleanButton_click') {
			_serviceDataObjects = new Array();
				
			addServiceDataObject(createServiceDataObject("lms.prestcontasciaaerea.emitirPrestacaoContasAction.findFilialUsuarioLogado",
				"setFilialUsuario", 
				new Array()));
	
	        xmit(false);
	
			_serviceDataObjects = new Array();
	   }
	}
	
	/** Limpa os campos da tela e desabilita o calendario */ 
	function limpar(){
		cleanButtonScript();
		
		var documento = getCurrentDocument("dtInicial", null);
        var obj = getElement("dtInicial", documento);
         setDisabledCalendar(obj, true, documento);
         setDisabled("nrPrestacaoConta", false);
	}
	
	

	function validateConsultar(eThis){
	
		//Deve informar o nrPrestacaoConta ou o periodo de vendas.
	
		if ( getElementValue("nrPrestacaoConta") == "" && getElementValue("dtInicial") != "" ){
			return true;
		}
		
		if ( getElementValue("nrPrestacaoConta") != "" && getElementValue("dtInicial") == "" ){
			return true;
		}

		if(document.getElementById("dtInicial").isDisabled == true){
			setFocus("nrPrestacaoConta");
		} else {
			setFocus("dtInicial");
		}

		alert(''+lms37001);
		
		return false;
	}
	
	function visualizarPrestacaoContas(eThis){
		if (validateConsultar(eThis) == false) return false;
		reportButtonScript('lms.prestcontasciaaerea.emitirPrestacaoContasAction', 'openPdf', eThis.form);
	}

	function dtInicialOnChange(eThis){
		if (getElementValue("dtInicial") == ""){
			resetValue("dtInicial");
			resetValue("dtFinal");

			setDisabled("nrPrestacaoConta", false);
			
			return true;
		}
		
		setDisabled("nrPrestacaoConta", true);

		var data = new Array();
		setNestedBeanPropertyValue(data, "dtInicial", getElementValue("dtInicial"));
		setNestedBeanPropertyValue(data, "empresa.idEmpresa", getElementValue("empresa.idEmpresa"));
		setNestedBeanPropertyValue(data, "filial.idFilial", getElementValue("filial.idFilial"));

			//Data final, do periodo.
			var sdo = createServiceDataObject("lms.prestcontasciaaerea.emitirPrestacaoContasAction.findDataFinal",
				"dtInicial",data);
			xmit({serviceDataObjects:[sdo]});
		}
		
	function dtInicial_cb(data, error){
		if (error != undefined){
			alert(''+error);
			return;
		}
		
		if (data == undefined) return;
		if (data._value == undefined) return;
		
		setElementValue("dtFinal", data._value);
		
		return true;
		
	}

	/**
	 * Ao alterar a prestação de contas, zera o periodo.
	 */
	function nrPrestacaoContaOnChange(){
		if(getElementValue("nrPrestacaoConta")){
		var	documento = getCurrentDocument("dtInicial", null);
		var obj = getElement("dtInicial", documento);
			setDisabledCalendar(obj, true, documento);
		
		resetValue("dtInicial");
		resetValue("dtFinal");
		} else {
			enablePeriodoVenda();
	}
	}
	
	
  /**
   *  Método que seta o formato PDF como padrão para geração do relatório
   *
   */
   function myOnPageLoadCallBack_cb(data,erro){
	
		onPageLoad_cb(data,erro);
		setElementValue("tpFormatoRelatorio","pdf");
	
   }
   
	// seta a filial
	function setFilialUsuario_cb(data, error) {
		if (data != null) {
			setElementValue("filial.idFilial", data.idFilial);
			setElementValue("filial.sgFilial", data.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data.pessoa.nmFantasia);
		}
	}

</script>