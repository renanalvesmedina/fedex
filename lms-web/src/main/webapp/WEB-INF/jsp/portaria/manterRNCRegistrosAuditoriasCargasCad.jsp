<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.portaria.manterRNCRegistrosAuditoriasCargasAction" onPageLoadCallBack="rncRegistroAuditPageLoad">
	<adsm:form action="/portaria/manterRegistrosAuditoriasCargas" idProperty="idNaoConformidadeAuditoria" service="lms.portaria.manterRNCRegistrosAuditoriasCargasAction.findByIdNaoConformidadeAuditoria">
		
   		<adsm:textbox label="filial" labelWidth="20%" width="80%" dataType="text" property="registroAuditoria.filial.sgFilial" disabled="true" required="false" serializable="false" size="3">
   		   		<adsm:textbox dataType="text" property="registroAuditoria.filial.pessoa.nmFantasia" disabled="true" required="false" serializable="false" size="30"/>
   		</adsm:textbox>
		<adsm:hidden property="registroAuditoria.filial.idFilial" serializable="false"/>
                     
        
		<adsm:textbox label="controleCarga" property="registroAuditoria.controleCarga.filialByIdFilialOrigem.sgFilial" dataType="text" size="3" labelWidth="20%" width="30%" disabled="true" serializable="false">
			<adsm:textbox property="registroAuditoria.controleCarga.nrControleCarga" dataType="integer" size="8" disabled="true"  serializable="false"/>
			<adsm:hidden  property="registroAuditoria.controleCarga.idControleCarga" serializable="false" />
		</adsm:textbox>

		<adsm:textbox dataType="integer" property="registroAuditoria.nrRegistroAuditoria" label="numeroRegistro" size="12" maxLength="8" labelWidth="20%" width="30%" required="false" cellStyle="vertical-Align:bottom;" disabled="true"  serializable="false"/>			
		<adsm:hidden property="registroAuditoria.idRegistroAuditoria" serializable="true" />			
		

		<adsm:lookup dataType="text" 
					 property="naoConformidade.filial" 
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.portaria.manterRNCRegistrosAuditoriasCargasAction.findLookupFilial" 
					 action="/municipios/manterFiliais" 
					 onchange="return sgFilialOnChangeHandler();" 
					 onDataLoadCallBack="disableNrNaoConformidade"
					 size="3" maxLength="3" 
					 required="false"
					 picker="false" serializable="true" label="naoConformidade" labelWidth="20%" width="30%">
			<adsm:lookup dataType="integer"
						 property="naoConformidade" 
						 idProperty="idNaoConformidade" 
						 criteriaProperty="nrNaoConformidade"
						 action="/rnc/manterNaoConformidade" 
						 onchange="return onNaoConformidadeChange();"
						 service="lms.portaria.manterRNCRegistrosAuditoriasCargasAction.findLookupNaoConformidade" 
						 onDataLoadCallBack="loadNrNaoConformidade" 
						 onPopupSetValue="naoConformidadePopup"
						 exactMatch="false" required="true"
						 size="10" maxLength="8" serializable="true" mask="00000000">
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="naoConformidade.filial.sgFilial"/>
				<adsm:propertyMapping modelProperty="sgFilialNaoConformidade" relatedProperty="naoConformidade.filial.sgFilial" blankFill="false"/>
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="naoConformidade.filial.idFilial" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="sgFilialDocumentoServico" relatedProperty="sgFilialDocumentoServico"/>
				<adsm:propertyMapping modelProperty="nrDocumentoServico" relatedProperty="nrDocumentoServico"/>				
				<adsm:propertyMapping modelProperty="tpDocumentoServico" relatedProperty="tpDocumentoServico"/> 
				<adsm:propertyMapping modelProperty="ocorrencias" relatedProperty="ocorrencias"/> 
				
			</adsm:lookup>
		</adsm:lookup>

		<adsm:textbox label="documentoServico" labelWidth="20%" width="30%" size="10" dataType="text" property="tpDocumentoServico" disabled="true" serializable="false">
	        <adsm:textbox size="3" dataType="text" property="sgFilialDocumentoServico" disabled="true" serializable="false"/>
			<adsm:textbox size="10" dataType="integer" mask="00000000" property="nrDocumentoServico" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<%--adsm:textarea property="ocorrencia" label="ocorrencia" maxLength="200" labelWidth="20%" width="80%" rows="2" columns="90" disabled="true" required="false" /--%>
        <adsm:listbox 
                   label="ocorrencias" 
                   size="10" 
                   property="ocorrencias" 
				   optionProperty="idOcorrenciaNaoConformidade"
				   optionLabelProperty="dsOcorrenciaNc"
				   labelWidth="20%" 
                   width="80%"  
                   showOrderControls="false" boxWidth="600" showIndex="false" serializable="false" required="false">
       </adsm:listbox>

		<adsm:buttonBar>
			<adsm:button caption="visualizarRNC" action="/rnc/manterNaoConformidade" cmd="main" disabled="false">
				<adsm:linkProperty src="naoConformidade.idNaoConformidade" target="idNaoConformidade" />
				<adsm:linkProperty src="naoConformidade.nrNaoConformidade" target="nrNaoConformidade" />
				<adsm:linkProperty src="naoConformidade.filial.sgFilial" target="filial.sgFilial" />
			</adsm:button>
			<adsm:storeButton service="lms.portaria.manterRNCRegistrosAuditoriasCargasAction.storeNaoConformidadeAuditoria"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   
<script>

	document.getElementById("registroAuditoria.controleCarga.filialByIdFilialOrigem.sgFilial").masterLink = "true";
	document.getElementById("registroAuditoria.controleCarga.nrControleCarga").masterLink = "true";
	document.getElementById("registroAuditoria.controleCarga.idControleCarga").masterLink = "true";
	document.getElementById("registroAuditoria.filial.sgFilial").masterLink = "true";
	document.getElementById("registroAuditoria.filial.pessoa.nmFantasia").masterLink = "true";
	document.getElementById("registroAuditoria.filial.idFilial").masterLink = "true";
	document.getElementById("registroAuditoria.nrRegistroAuditoria").masterLink = "true";
	document.getElementById("registroAuditoria.idRegistroAuditoria").masterLink = "true";
	
	function sgFilialOnChangeHandler() {
		if (getElementValue("naoConformidade.filial.sgFilial")=="") {
			setDisabled("naoConformidade.nrNaoConformidade", true);
			resetValue(document.getElementById("naoConformidade.nrNaoConformidade"), this.document);
			resetValue("ocorrencias");
		} else {
			setDisabled("naoConformidade.nrNaoConformidade", false);
		}
		return lookupChange({e:document.forms[0].elements["naoConformidade.filial.idFilial"]});
	}
	
	function onNaoConformidadeChange() {
		if (document.getElementById("naoConformidade.nrNaoConformidade").value == "") {
			resetValue("ocorrencias");
		}
		return naoConformidade_nrNaoConformidadeOnChangeHandler();	
	}
	
	
	function findListboxValues(idNaoConformidade) {
		var data = new Array();
		setNestedBeanPropertyValue(data, "idNaoConformidade", idNaoConformidade);
		var sdo = createServiceDataObject("lms.portaria.manterRNCRegistrosAuditoriasCargasAction.findListboxValues", "findListboxValues", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findListboxValues_cb(data) {
		if (data != undefined) {
			var formElem = document.getElementById(mainForm);
            fillFormWithFormBeanData(formElem.tabIndex, data);
		}
	}
	
	function disableNrNaoConformidade_cb(data, error) {
		if (data.length==0) {
			setDisabled("naoConformidade.nrNaoConformidade", false);
		}
		return lookupExactMatch({e:document.getElementById("naoConformidade.filial.idFilial"), data:data});
	}
	
	
	function loadNrNaoConformidade_cb(data, error) {
		naoConformidade_nrNaoConformidade_exactMatch_cb(data);
		if (data[0]!=undefined) {
			document.getElementById("naoConformidade.filial.sgFilial").value = data[0].sgFilialNaoConformidade;
			findListboxValues(getNestedBeanPropertyValue(data, ":0.idNaoConformidade"));			
		}
	}

	function naoConformidadePopup(data){
		if (data != undefined){
			data.sgFilialNaoConformidade = getNestedBeanPropertyValue(data, "filial.sgFilial");
			data.sgFilialDocumentoServico = getNestedBeanPropertyValue(data, "doctoServico.filialByIdFilialOrigem.sgFilial");
			data.nrDocumentoServico = getNestedBeanPropertyValue(data, "doctoServico.nrDoctoServico");
			data.tpDocumentoServico = getNestedBeanPropertyValue(data, "doctoServico.tpDocumentoServico.description");
			findListboxValues(getNestedBeanPropertyValue(data, "idNaoConformidade"));
		}
	}

	var idFilial;
	var sgFilial;

	
	function setFilialValues() {		
		if (idFilial != undefined &&
			sgFilial != undefined) {
			setElementValue("naoConformidade.filial.idFilial", idFilial);
			setElementValue("naoConformidade.filial.sgFilial", sgFilial);
		}
	}
	
	function initWindow(evt){
		if(evt.name != "gridRow_click" && evt.name != 'storeButton'){
			setFilialValues();
		}
	}
	
	function rncRegistroAuditPageLoad_cb(){
		onPageLoad_cb();
		findFilialUsuario();
	}
	
	function findFilialUsuario() {
		var data = new Array();
		var sdo = createServiceDataObject("lms.portaria.manterRNCRegistrosAuditoriasCargasAction.findFilialUsuario", "findFilialUsuario", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findFilialUsuario_cb(data, exception) {
		if (exception == null){			
			idFilial = getNestedBeanPropertyValue(data, "idFilial");
			sgFilial = getNestedBeanPropertyValue(data, "sgFilial");
		}
		setFilialValues();
	}
	
</script>