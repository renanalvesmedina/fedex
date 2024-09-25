<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirFaturasNacionais">

		<adsm:i18nLabels>
			<adsm:include key="LMS-36241"/>
			<adsm:include key="LMS-36247"/>
			<adsm:include key="LMS-36267"/>
		</adsm:i18nLabels>

		<adsm:hidden property="filialSessao.idFilial" serializable="false"/>
	
		<adsm:lookup action="/municipios/manterFiliais" 
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label = "filialFaturamento" 
					 size="3" 
					 maxLength="3" 
					 width="82%"
					 required="true"
					 labelWidth="20%"
					 onDataLoadCallBack="verificaFilial"
					 exactMatch="true">
					 
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="50" maxLength="50" disabled="true" serializable="false"/>
			
		</adsm:lookup>

		
		<adsm:textbox label="fatura" labelWidth="20%" width="10%" dataType="integer" property="faturaInicial" size="7" maxLength="10" smallerThan="faturaFinal" onchange="validateRange(document.getElementById('faturaInicial'))"/>
		<adsm:label key="ate" width="5%"/>	    
	    <adsm:textbox width="15%" dataType="integer" property="faturaFinal" size="7" maxLength="10" biggerThan="faturaInicial" onchange="validateRange(document.getElementById('faturaFinal'))"/>
		
		<adsm:combobox property="modal" domain="DM_MODAL" label="modal" labelWidth="20%" width="30%"/>
		<adsm:combobox property="abrangencia" domain="DM_ABRANGENCIA" label="abrangencia" labelWidth="20%" width="30%"/>

		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio" 
    				   labelWidth="20%"
    				   width="30%"
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:button buttonType="reportViewerButton" caption="visualizar" onclick="validateFormulario()" disabled="false"/>
			<adsm:button caption="limpar" id="btlimpar" onclick="limpar()" />
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script>

	
	function validateRange(element){
		var faturaInicial = getElementValue("faturaInicial");
		var faturaFinal = getElementValue("faturaFinal");
		if (faturaInicial != "" && faturaFinal != ""){
			if ((faturaFinal - faturaInicial) >1000){
				alert(i18NLabel.getLabel("LMS-36247"));
				setElementValue(element,"");
				setFocus(element);
			}	
		}		
	}

	function validateFormulario(){
		if (validateForm(document.forms[0])) {
			
			if (getElementValue("faturaInicial") != "" || getElementValue("faturaFinal") != "") {
				
				// Caso apenas um dos dois campos esteja preenchido, deve lan�ar o alerta.
				if (getElementValue("faturaInicial") == "" || getElementValue("faturaFinal") == "") {	
					alert(i18NLabel.getLabel("LMS-36241"));
					return;
				}
			}
			
			if (confirm(i18NLabel.getLabel("LMS-36267"))){
				executeReportWithCallback('lms.contasreceber.emitirFaturasNacionaisAction.executeEmitirCTEWithLocator', 'imprimeCte', document.forms[0]);
			}else{
				reportButtonScript('lms.contasreceber.emitirFaturasNacionaisAction.execute', 'openPdf', document.forms[0]);
			}


		}
	}
	
	function imprimeCte_cb(strFile,error){
		if (error == null) {
			if (strFile != null && strFile._value != "") {
				openReportWithLocator(strFile, null, "IMPRESSAO_CTE", true);
			}
		} else {
			alert(error+'');
			return;
		}
		executeReportWithCallback('lms.contasreceber.emitirFaturasNacionaisAction.executeEmitirNTEWithLocator', 'imprimeNte', document.forms[0]);
	}
	
	function imprimeNte_cb(strFile,error){
		if (error == null) {
			if (strFile != null && strFile._value != "") {
				openReportWithLocator(strFile, null, "IMPRESSAO_NTE", true);
			}
		} else {
			alert(error+'');
		}
		executeReportWithCallback('lms.contasreceber.emitirFaturasNacionaisAction.executeEmitirNSEWithLocator', 'imprimeNse', document.forms[0]);
	}
	
	function imprimeNse_cb(strFile,error){
		if (error == null) {
			if (strFile != null && strFile._value != "") {
				openReportWithLocator(strFile, null, "IMPRESSAO_NSE", true);
			}
		} else {
			alert(error+'');
		}
		reportButtonScript('lms.contasreceber.emitirFaturasNacionaisAction.execute', 'openPdf', document.forms[0]);
	}
	
    function myOnPageLoadCallBack_cb(data, erro){
		onPageLoad_cb(data, erro);
		buscaFilialUsuario();
	}

	/** chamado ao iniciar a tela */
	function initWindow(){
		setDisabled("btLimpar",false);
	}

	/** Fun��o que limpa os campos e busca a filial do usuario */
	function limpar(){
		cleanButtonScript();
		buscaFilialUsuario();
	}

	/** Busca a Filial padrao do usuario */
	function buscaFilialUsuario(){
		_serviceDataObjects = new Array();
		
		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirFaturasNacionaisAction.findFilialUsuario",
			"setFilialUsuario", 
			new Array()));

		xmit(false);	
		
	}

	function setFilialUsuario_cb(data, error) {
		setElementValue('filialSessao.idFilial', data.idFilial);
		setElementValue('filial.idFilial', data.idFilial);
		setElementValue('filial.sgFilial', data.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.pessoa.nmFantasia);
		
		document.getElementById("faturaInicial").required = false;
		document.getElementById("faturaFinal").required = false;
		
	}
	
	function verificaFilial_cb(data, error) {
		filial_sgFilial_exactMatch_cb(data);
		
		if (data != null && data[0] != undefined ) {
			var filialSessao = getElementValue("filialSessao.idFilial");
			var filial = data[0].idFilial;
			
			// required tem q ser string
			var req = ""+(filialSessao != filial);

			document.getElementById("faturaInicial").required = req;
			document.getElementById("faturaFinal").required = req;
		}
	}
</script>