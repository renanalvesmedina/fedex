<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="onPageLoadCallBack">
	<adsm:form action="/vendas/emitirRelacaoClientesPerdidos" >
		<adsm:hidden property="tpFormatoRelatorio.valor" />
		<adsm:hidden property="tpFormatoRelatorio.descricao" />
		<adsm:hidden property="sgFilial" serializable="true"/>
		
		<adsm:combobox label="regional" 
			property="regional.idRegional"
			required="false"
			optionLabelProperty="siglaDescricao" 
			optionProperty="idRegional" 
			service="lms.vendas.emitirRelacaoClientesPerdidosAction.montaComboRegional" 
			labelWidth="15%" 
			width="35%" 
			boxWidth="170"
		>
			<adsm:propertyMapping relatedProperty="regional.siglaDescricao" modelProperty="siglaDescricao"/>
		</adsm:combobox>

		<adsm:hidden property="regional.siglaDescricao" serializable="true"/>

		<adsm:lookup label="filial"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			action="/municipios/manterRegionalFilial"
			service="lms.vendas.emitirRelacaoClientesPerdidosAction.findLookupFilial" 
			dataType="text"
			exactMatch="true"
			minLengthForAutoPopUpSearch="3"
			labelWidth="15%"
			width="35%"
			size="3"
			maxLength="3"
			onDataLoadCallBack="filialCallBack"
			afterPopupSetValue="filialCallBack"
		>
			<adsm:propertyMapping criteriaProperty="regional.idRegional" modelProperty="regional.idRegional"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" copyPopup="true"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="nmFantasia" />
			<adsm:propertyMapping relatedProperty="sgFilial" modelProperty="filial.sgFilial" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" />
		</adsm:lookup>
		
		
		<adsm:combobox property="ramoAtividade.idRamoAtividade" optionLabelProperty="dsRamoAtividade" optionProperty="idRamoAtividade"
				label="ramoAtividade" service="lms.vendas.emitirRelacaoClientesPerdidosAction.findComboRamoAtividade" labelWidth="15%" width="35%" boxWidth="220" required="false" >
			<adsm:propertyMapping relatedProperty="dsRamoAtividade" modelProperty="dsRamoAtividade"/>
		</adsm:combobox>
		<adsm:hidden property="dsRamoAtividade" serializable="true"/>	
				
		<adsm:combobox
			property="tpAbrangencia" 
			label="abrangencia"
			domain="DM_ABRANGENCIA"
			onlyActiveValues="true"
			disabled="false"
			labelWidth="15%"
			width="35%"
			serializable="true" onchange="abrangenciaChange(this)"/>
			<adsm:hidden property="dsAbrangencia" serializable="true"/>	
		
		<adsm:combobox
			property="tpModal" 
			label="modal"
			domain="DM_MODAL"
			onlyActiveValues="true"
			disabled="false"
			labelWidth="15%"
			width="35%"
			serializable="true" onchange="modalChange(this)"/>
			<adsm:hidden property="dsModal" serializable="true"/>	
			
					
		<adsm:range label="periodoEmissao" labelWidth="15%" width="35%" required="true">
			<adsm:textbox dataType="JTDate" property="dtInicio" required="false" picker="true" />
			<adsm:textbox dataType="JTDate" property="dtFim" picker="true"/>
		</adsm:range>	
		
		<adsm:combobox width="35%" labelWidth="15%" label="formatoRelatorio"
			property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" 
			serializable="false" required="true" onDataLoadCallBack="setFormatoDefault">
			<adsm:propertyMapping relatedProperty="tpFormatoRelatorio.valor" modelProperty="value"/>
			<adsm:propertyMapping relatedProperty="tpFormatoRelatorio.descricao" modelProperty="description"/>
		</adsm:combobox>
		
				
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.vendas.emitirRelacaoClientesPerdidosAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">

	function situacaoChange(obj){
		setElementValue(document.getElementById("dsSituacao"), obj.options[obj.selectedIndex].text);
	}
	
	function abrangenciaChange(obj){
		setElementValue(document.getElementById("dsAbrangencia"), obj.options[obj.selectedIndex].text);
	}
	
	function modalChange(obj){
		setElementValue(document.getElementById("dsModal"), obj.options[obj.selectedIndex].text);
	}
	
	function etapaChange(obj){
		setElementValue(document.getElementById("dsEtapa"), obj.options[obj.selectedIndex].text);
	}
	
	/*DADOS BÁSICOS DA TELA*/
	var msgException = null;
	var teveException = null;
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	var idRegional = null;
	var siglaRegional = null;
	
	function initWindow(eventObj) {
		var event = eventObj.name;
		if(event == "cleanButton_click") {
			setElementValue("tpFormatoRelatorio", "pdf");
			ajustaFormatoRelatorio();
			var data = new Array();	
			var sdo = createServiceDataObject("lms.vendas.emitirPipelineClienteAction.getBasicData", "dataSession", data);
			xmit({serviceDataObjects:[sdo]});
		}
		
	}
	
	function setFormatoDefault_cb(data, error) {
		if(data) {
			tpFormatoRelatorio_cb(data);
		}
		setElementValue("tpFormatoRelatorio", "pdf");
		ajustaFormatoRelatorio();
	}
	
	function ajustaFormatoRelatorio() {
		var combo = document.getElementById("tpFormatoRelatorio");
		setElementValue("tpFormatoRelatorio.descricao", combo.options[combo.selectedIndex].text);
		setElementValue("tpFormatoRelatorio.valor", combo.value);
	}

	function onPageLoadCallBack_cb(data, error) {
		onPageLoad_cb(data, error);
		var data = new Array();	
		var sdo = createServiceDataObject("lms.vendas.emitirPipelineClienteAction.getBasicData", "dataSession", data);
		xmit({serviceDataObjects:[sdo]});
		
	}
	
	function dataSession_cb(data,error) {
		idFilial = getNestedBeanPropertyValue(data, "filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data, "filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia");
		idRegional = getNestedBeanPropertyValue(data, "regional.idRegional");
		siglaRegional = getNestedBeanPropertyValue(data, "regional.siglaDescricao");
		
		setElementValue("filial.idFilial", idFilial);
		setElementValue("filial.sgFilial", sgFilial);
		setElementValue("sgFilial", sgFilial);
		setElementValue("filial.pessoa.nmFantasia", nmFilial);
		setElementValue("regional.idRegional", idRegional);
		setElementValue("regional.siglaDescricao", siglaRegional);
		
		
	}	

	
	function retornoReport_cb(data, error){
		if(error != undefined) {
			alert(error);
			return;
		}
		alertI18nMessage("LMS-30052");
	}

	//EMITIR PDF
	function emitirReportPDF() {
		reportButtonScript('lms.vendas.emitirPipelineClienteAction', 'openPdf', document.Lazy);
	}

	

		/***** Lookup Funcionario *****/
	function funcCallBack_cb(data){
		if(!usuarioByIdUsuario_nrMatricula_exactMatch_cb(data))
			return;
		if(data[0] != undefined) {
			setElementValue("usuarioByIdUsuario.idUsuario", data[0].idUsuario);
			setElementValue("usuarioByIdUsuario.nmUsuario", data[0].nmFuncionario);
			
		}
		return;
	}

	function funcCallBack(data){
		if(data != undefined) {
			setElementValue("usuarioByIdUsuario.idUsuario", data.idUsuario);
			setElementValue("usuarioByIdUsuario.nmUsuario", data.nmUsuario);
			
		}
		return;
	}
	
	//Lookup Filial
	function filialCallBack_cb(data){
		if(!filial_sgFilial_exactMatch_cb(data))
			return;
		if(data[0] != undefined) {
			setElementValue("filial.idFilial", data[0].idFilial);
			setElementValue("filial.sgFilial", data[0].sgFilial);
			setElementValue("sgFilial", data[0].sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data[0].nmFantasia);
			setElementValue("regional.idRegional", data[0].idRegional);
			
		}
		return;
	}
	
	function filialCallBack(data){
		if(data != undefined) {
			setElementValue("filial.idFilial", data.idFilial);
			setElementValue("filial.sgFilial", data.sgFilial);
			setElementValue("sgFilial", data.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data.nmFantasia);
			setElementValue("regional.idRegional", data.idRegional);
			
		}
		return;
	}
	

</script>