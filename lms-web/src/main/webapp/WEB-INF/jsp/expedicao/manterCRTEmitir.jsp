<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.manterCRTEmitirAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-04145"/>
		<adsm:include key="LMS-04146"/>
		<adsm:include key="LMS-04147"/>
		<adsm:include key="LMS-04148"/>
		<adsm:include key="LMS-04149"/>
		<adsm:include key="LMS-04150"/>
		<adsm:include key="numeroVias"/>
		<adsm:include key="requiredField"/>
	</adsm:i18nLabels>

	<adsm:form action="/expedicao/manterCRTEmitir">

		<adsm:hidden property="nrVia" serializable="true"/>
		
		<adsm:section caption="emitir" width="54"/>

		<adsm:label key="branco" style="border:none;" width="1%" />
		<adsm:combobox property="tpEmissao" label="tipoEmissao" domain="DM_TIPO_EMISSAO_CRT" onchange="return changeCombo(this)" labelWidth="20%" width="79%" required="true" />

		<adsm:label key="branco" style="border:none;" width="1%" />
		<adsm:checkbox property="primeiraVia" label="primeiraVia" labelWidth="20%" width="79%" />

		<adsm:label key="branco" style="border:none;" width="1%" />
		<adsm:checkbox property="segundaVia" label="segundaVia" labelWidth="20%" width="79%" />

		<adsm:label key="branco" style="border:none;" width="1%" />
		<adsm:checkbox property="terceiraVia" label="terceiraVia" labelWidth="20%" width="79%" />

		<adsm:label key="branco" style="border:none;" width="1%" />
		<adsm:textbox dataType="integer" property="numeroVias" label="numeroVias" minValue="1" width="50%" labelWidth="20%" size="10" maxLength="10" required="true"/>


		<adsm:buttonBar freeLayout="true">
			<adsm:button id="emitir" caption="emitir" onclick="return validateRelatorio()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript" src="../lib/expedicao.js"></script>
<script type="text/javascript">

	function myOnPageLoad_cb(data,erro){
		if(erro) {
			return;
		}
		ajustaDadosDefault();
	}

	function ajustaDadosDefault(){
		setElementValue("tpEmissao","O");
		setElementValue("numeroVias","1");
		setDisabled("emitir",false);
		changeCombo(document.getElementById("tpEmissao"));
	}

	function changeCombo(obj){
		comboboxChange({e:obj});

		var vlCombo = getElementValue("tpEmissao");

		if(vlCombo == "R") {
			getElement("primeiraVia").checked = false;
			getElement("segundaVia").checked = false;
			getElement("terceiraVia").checked = false;
			
			setDisabled("primeiraVia",true);
			setDisabled("segundaVia",true);
			setDisabled("terceiraVia",true);
			setElementValue("nrVia","");
			setElementValue("numeroVias","1");
			setDisabled("numeroVias",true);
		}
		if(vlCombo == "C" || vlCombo == "V") {
			getElement("primeiraVia").checked = false;
			getElement("segundaVia").checked = false;
			getElement("terceiraVia").checked = false;
			setElementValue("nrVia","");

			setDisabled("primeiraVia",true);
			setDisabled("segundaVia",true);
			setDisabled("terceiraVia",true);
			setDisabled("numeroVias",false);
		}
		if(vlCombo == "O") {
			setDisabled("primeiraVia",false);
			setDisabled("segundaVia",false);
			setDisabled("terceiraVia",false);
			setElementValue("numeroVias","1");
			setDisabled("numeroVias",true);
		}
	}
	

	function validateRelatorio(){
		if(getElementValue("tpEmissao") == "") {
			alertRequiredField("tpEmissao");
			setFocus("tpEmissao", false);
			return false;
		}
		if(getElementValue("numeroVias") == "") {
			alertRequiredField("numeroVias");
			setFocus("numeroVias", false);
			return false;
		}

		if(getElementValue("tpEmissao") == "O") {
			var sdo = createServiceDataObject("lms.expedicao.manterCRTEmitirAction.validateRelatorioOriginal", "validateRelatorioOriginal", buildFormBeanFromForm(document.forms[0]));
			xmit({serviceDataObjects:[sdo]});
		}
		if(getElementValue("tpEmissao") == "R") {
			emitirRelatorio(); 
		}
		if(getElementValue("tpEmissao") == "C") {
			emitirRelatorio(); 
		}
		if(getElementValue("tpEmissao") == "V") {
			validateDescontoAprovadoWorkflow();
		}
		
	}

	function validateRelatorioOriginal_cb(data, erro) {
	    if (erro!=undefined) {
	    	alert(erro);
	    	return;
	    }

		if(data.tpSituacaoCrt.value == "D" || data.tpSituacaoCrt.value == "R") {
			storeEvento(data.idDoctoServico);
		}
		if(data.tpSituacaoCrt.value == "E" ) {
			validatePendenciaWorkflow(data.idDoctoServico);
		}
	}

	function storeEvento(idDoctoServico){
		var sdo = createServiceDataObject("lms.expedicao.manterCRTEmitirAction.storeEvento", "storeEvento", {idDoctoServico:idDoctoServico});
		xmit({serviceDataObjects:[sdo]});
	}

	function storeEvento_cb(data, erro){
	    if (erro!=undefined) {
	    	alert(erro);
	    	return;
	    }

		if(confirmI18nMessage("LMS-04145")){
//			oldAlert('Sim');
		}else{
			emitirRelatorio();
			
		}
		
	    /*
	    * Não concluído.
	    */
	}
	
	function validatePendenciaWorkflow(idDoctoServico){
		var sdo = createServiceDataObject("lms.expedicao.manterCRTEmitirAction.executeValidacaoPendenciaWorkflow", "validatePendenciaWorkflow", {idDoctoServico:idDoctoServico});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validatePendenciaWorkflow_cb(data, erro){
	    if (erro!=undefined) {
	    	alert(erro);
	    	return;
	    }

	    if(data.pendenciaReemissao == null || data.pendenciaReemissao.tpSituacaoPendencia.value == "C") {
			if(confirmI18nMessage("LMS-04149")){
				storePendenciaWorkflow(data.idDoctoServico);		
			}
	    }
	}


	function validateDescontoAprovadoWorkflow(){
		var sdo = createServiceDataObject("lms.expedicao.manterCRTEmitirAction.executeValidacaoDescontoAprovadoWorkflow", "validateDescontoAprovadoWorkflow", buildFormBeanFromForm(document.forms[0]));
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validateDescontoAprovadoWorkflow_cb(data, erro){
	    if (erro!=undefined) {
	    	alert(erro);
	    	return;
	    }
		emitirRelatorio();
	}
	
	
	
	

	function storePendenciaWorkflow(idDoctoServico){
		var sdo = createServiceDataObject("lms.expedicao.manterCRTEmitirAction.storePendenciaWorkflow", "storePendenciaWorkflow", {idDoctoServico:idDoctoServico});
		xmit({serviceDataObjects:[sdo]});
	}

	function storePendenciaWorkflow_cb(data, erro){
	    if (erro!=undefined) {
	    	alert(erro);
	    	return;
	    }
	    alertI18nMessage("LMS-04150");
	    self.close();
	}
	
	
	
	

	/**************************
	* Imprime a frente do CRT.*
	**************************/
	function emitirRelatorio() {
		var callBack = "emitirRelatorioDireto";

		if(getElementValue("tpEmissao") == "R") {
			callBack = "emitirRelatorio";
		}

		if(getElementValue("tpEmissao") == "O") {
			if(getElementValue("primeiraVia")) {
				setElementValue("nrVia","primeiraVia");
			}else
				if(getElementValue("segundaVia")) {
					setElementValue("nrVia","segundaVia");
				}else
					if(getElementValue("terceiraVia")) {
						setElementValue("nrVia","terceiraVia");
					}else {
						return;
					}
		}
		executeReportWithCallback("lms.expedicao.manterCRTEmitirAction", callBack, document.forms[0]);
	}

	/*************************
	* Imprime o verso do CRT.*
	*************************/
	function emitirRelatorioBack() {
	    alertI18nMessage("LMS-04146");
		executeReportWithCallback("lms.expedicao.manterCRTEmitirAction.executeReportBack", "emitirRelatorioBackDireto", document.forms[0]);
	}

	function emitirRelatorioDireto_cb(strFile, erro) {
	    if (erro!=undefined) {
	    	alert(erro);
	    	return;
	    }
	    var quantidade = getElementValue("numeroVias");
   		printPdf_cb(strFile, erro, quantidade, dialogArguments.w.document);

		if(getElementValue("tpEmissao") == "O") {
			emitirRelatorioBack();
		}else{
		    self.close();
		}
	}

	function emitirRelatorioBackDireto_cb(strFile, erro) {
	    if (erro!=undefined) {
	    	alert(erro);
	    	return;
	    }
   		printPdf_cb(strFile, erro, null);
   		setElementValue(getElementValue("nrVia"),false);
   		emitirRelatorio();
	}


	function emitirRelatorio_cb(strFile, erro) {
	    if (erro!=undefined) {
	    	alert(erro);
	    	return;
	    }
   		openReport(strFile._value);
	    self.close();   		
	}

	
	
</script>
