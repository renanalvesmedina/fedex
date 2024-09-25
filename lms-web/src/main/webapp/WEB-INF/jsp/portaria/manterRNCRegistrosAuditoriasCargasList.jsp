<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterRegistrosAuditoriasCargas" service="lms.portaria.manterRNCRegistrosAuditoriasCargasAction" onPageLoadCallBack="rncRegistroAuditPageLoad">
	<adsm:form action="/portaria/manterRegistrosAuditoriasCargas" idProperty="idNaoConformidadeAuditoria" >

		<adsm:textbox label="filial" labelWidth="20%" width="80%" dataType="text" property="registroAuditoria.filial.sgFilial" disabled="true" required="false" serializable="false" size="3">
   		   		<adsm:textbox dataType="text" property="registroAuditoria.filial.pessoa.nmFantasia" disabled="true" required="false" serializable="false" size="30"/>
   		</adsm:textbox>   		
		<adsm:hidden property="registroAuditoria.filial.idFilial" serializable="false" />

		<adsm:textbox label="controleCarga" property="registroAuditoria.controleCarga.filialByIdFilialOrigem.sgFilial" dataType="text" size="3" labelWidth="20%" width="30%" disabled="true" serializable="false">
			<adsm:textbox property="registroAuditoria.controleCarga.nrControleCarga" dataType="integer" size="8" disabled="true" serializable="false"/>
			<adsm:hidden  property="registroAuditoria.controleCarga.idControleCarga" serializable="true"/>
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
					 picker="false" serializable="true" label="naoConformidade" labelWidth="20%" width="30%">
			<adsm:lookup dataType="integer"
						 property="naoConformidade" 
						 idProperty="idNaoConformidade" 
						 criteriaProperty="nrNaoConformidade"
						 action="/rnc/manterNaoConformidade" 
						 service="lms.portaria.manterRNCRegistrosAuditoriasCargasAction.findLookupNaoConformidade" 
						 onDataLoadCallBack="loadNrNaoConformidade" 
						 onPopupSetValue="naoConformidadePopup"
						 exactMatch="false" required="false"
						 size="10" maxLength="8" serializable="true" mask="00000000">
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="naoConformidade.filial.sgFilial"/>
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="naoConformidade.filial.idFilial" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="sgFilialNaoConformidade" relatedProperty="naoConformidade.filial.sgFilial" blankFill="false"/>
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="naoConformidadeAuditoria"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="naoConformidadeAuditoria" idProperty="idNaoConformidadeAuditoria" selectionMode="check" unique="true" rows="12" service="lms.portaria.manterRNCRegistrosAuditoriasCargasAction.findPaginatedNaoConformidadeAuditoria" rowCountService="lms.portaria.manterRNCRegistrosAuditoriasCargasAction.getRowCountNaoConformidadeAuditoria">
		
		<adsm:gridColumnGroup separatorType="RNC"> 
			<adsm:gridColumn title="naoConformidade" property="sgFilialNaoConformidade" width="70" />
			<adsm:gridColumn title="" property="nrNaoConformidade" width="60" align="right" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="documentoServico" property="tpDocumentoServico" width="40"/>
		
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="sgFilialDocumentoServico" width="70" />
			<adsm:gridColumn title="" property="nrDocumentoServico" width="60" align="right" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="ocorrencias" property="ocorrencias" />
		
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">

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
			//resetValue(this.document);
			resetValue(document.getElementById("naoConformidade.nrNaoConformidade"), this.document);
		} else {
			setDisabled("naoConformidade.nrNaoConformidade", false);
		}
		return lookupChange({e:document.forms[0].elements["naoConformidade.filial.idFilial"]});
	}
	
	
	function disableNrNaoConformidade_cb(data, error) {
		if (data.length==0) {
			setDisabled("naoConformidade.nrNaoConformidade", false);
		}
		return lookupExactMatch({e:document.getElementById("naoConformidade.filial.idFilial"), data:data});
	}
	
	
	function loadNrNaoConformidade_cb(data, error) {
		naoConformidade_nrNaoConformidade_exactMatch_cb(data);
	}
	
	function naoConformidadePopup(data){
		if (data != undefined){
			data.sgFilialNaoConformidade = getNestedBeanPropertyValue(data, "filial.sgFilial");
		}
	}

	function initWindow(evt){
		if (evt.name == "cleanButton_click" || evt.name == "tab_load"){
			setFilialValues();
			setFocusOnFirstFocusableField();
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
