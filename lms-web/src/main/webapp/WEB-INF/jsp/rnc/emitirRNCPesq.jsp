<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.emitirRNCAction" onPageLoadCallBack="retornoCarregaPagina">
	<adsm:form action="/rnc/emitirRNC" >
		
		<adsm:lookup dataType="text" label="naoConformidade" 
					 property="naoConformidade.filial" criteriaSerializable="true"
					 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.rnc.emitirRNCAction.findLookupFilialByNaoConformidade" 
					 action="/municipios/manterFiliais" popupLabel="pesquisarFilial"
					 onchange="return sgFilialOnChangeHandler();"
					 onDataLoadCallBack="retornoFilialNaoConformidade"
					 size="3" maxLength="3" picker="false" serializable="false" labelWidth="23%" width="77%" >
			<adsm:lookup dataType="integer"
						 property="naoConformidade" 
						 idProperty="idNaoConformidade" 
						 criteriaProperty="nrNaoConformidade"
						 action="/rnc/manterNaoConformidade" criteriaSerializable="true"
						 service="lms.rnc.emitirRNCAction.findLookupNaoConformidade" 
						 popupLabel="pesquisarNaoConformidade"
						 exactMatch="false" required="true"
						 onchange="return nrNaoConformidadeOnChangeHandler();"
						 onPopupSetValue="naoConformidadePopupSetValue"
						 onDataLoadCallBack="retornoNrNaoConformidade"
						 size="10" maxLength="8" serializable="true" mask="00000000">
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="naoConformidade.filial.idFilial" disable="false"/>
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="naoConformidade.filial.sgFilial" disable="true"/>
				<adsm:propertyMapping modelProperty="tpStatusNaoConformidade.description" relatedProperty="tpStatusNaoConformidade"/>
				<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="naoConformidade.filial.idFilial" blankFill="false" />
				<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="naoConformidade.filial.sgFilial" blankFill="false" />
			</adsm:lookup>
		</adsm:lookup>

		<adsm:textbox label="status" dataType="text" property="tpStatusNaoConformidade"
					  labelWidth="23%" width="77%" disabled="true" size="20" serializable="false" />

		<adsm:combobox property="naoConformidade.doctoServico.tpDocumentoServico"
					   label="documentoServico" labelWidth="23%" width="77%" 
					   service="lms.rnc.emitirRNCAction.findTipoDocumentoServico"
					   optionProperty="value" optionLabelProperty="description"
					   serializable="false"
					   onchange="return changeDocumentWidgetType({
						   documentTypeElement:this,
						   filialElement:document.getElementById('naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial'),
						   documentNumberElement:document.getElementById('naoConformidade.doctoServico.idDoctoServico'),
						   parentQualifier:'',
						   documentGroup:'SERVICE',
						   actionService:'lms.rnc.emitirRNCAction'
					   });"> 

			<adsm:lookup dataType="text"
						 property="naoConformidade.doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial"
						 service=""
						 disabled="true" popupLabel="pesquisarFilial"
						 action=""
						 size="3" maxLength="3" picker="false" 
						 onDataLoadCallBack="enableNaoConformidadeDoctoServico"
						 serializable="false"
						 onchange="return changeDocumentWidgetFilial({
							 filialElement:document.getElementById('naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial'),
							 documentNumberElement:document.getElementById('naoConformidade.doctoServico.idDoctoServico')
							 });"
						 />
			
			<adsm:lookup dataType="integer"
						 property="naoConformidade.doctoServico"
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
						 service=""
						 action=""
						 popupLabel="pesquisarDocumentoServico"
						 onDataLoadCallBack="retornoDocumentoServico"
						 afterPopupSetValue="afterPopupDoctoServicoNrDoctoServico"
						 size="10" maxLength="8" serializable="false" disabled="true" mask="00000000" />
		</adsm:combobox>
		
    	<adsm:combobox label="formatoRelatorio" labelWidth="23%" width="77%"
		               domain="DM_FORMATO_RELATORIO" renderOptions="true"
		               property="tpFormatoRelatorio"
		               defaultValue="pdf"
		               required="true"
		/>	
		
		<script>
			var msg = '<adsm:label key="LMS-12009"/>';
		</script>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.rnc.emitirRNCAction" id="btnVisualizar" />
			<adsm:button caption="limpar" id="btnLimpar" buttonType="resetButton" onclick="limpaTela()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

function resetDoctoServico() {
	resetValue("naoConformidade.doctoServico.tpDocumentoServico");
	resetValue("naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial");
	resetValue("naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial");
	resetValue("naoConformidade.doctoServico.idDoctoServico");
	resetValue("naoConformidade.doctoServico.nrDoctoServico");
}

function desabilitaDoctoServico() {
	setDisabled("naoConformidade.doctoServico.tpDocumentoServico", true);
	setDisabled("naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial", true);
	setDisabled("naoConformidade.doctoServico.idDoctoServico", true);
}

function habilitaDoctoServico() {
	setDisabled("naoConformidade.doctoServico.tpDocumentoServico", false);
	setDisabled("naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial", true);
	setDisabled("naoConformidade.doctoServico.idDoctoServico", false);
	setDisabled("naoConformidade.doctoServico.nrDoctoServico", true);
}

function initWindow(eventObj) {
	if (eventObj.name=='tab_load'){
		setDisabled("naoConformidade.nrNaoConformidade", true);
	} else if (eventObj.name=='cleanButton_click') {
		setDisabled("naoConformidade.nrNaoConformidade", true);
		resetDoctoServico();
		habilitaDoctoServico();
	}
}


function retornoCarregaPagina_cb(data, error) {
    onPageLoad_cb(data, error);
    setMasterLink(document, true);
	resetDoctoServico();
	document.getElementById("naoConformidade.filial.sgFilial").required = "true";
	setDisabled("btnLimpar", false);
}


function enableNaoConformidadeDoctoServico_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}	
	var r = naoConformidade_doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
   	if (r == true) {
		setDisabled("naoConformidade.doctoServico.idDoctoServico", false);
      	setFocus(document.getElementById("naoConformidade.doctoServico.nrDoctoServico"));
   	}
}



// ------------------------------------- Nao conformidade ----------------------------------------
function sgFilialOnChangeHandler() {
	var r = naoConformidade_filial_sgFilialOnChangeHandler();
	if (getElementValue("naoConformidade.filial.sgFilial") == "") {
		setDisabled("naoConformidade.nrNaoConformidade", true);
		resetValue("naoConformidade.idNaoConformidade");
		resetDoctoServico();
		habilitaDoctoServico();
	} else {
		setDisabled("naoConformidade.nrNaoConformidade", false);
	}
	return r;
}



function nrNaoConformidadeOnChangeHandler() {
	var r = naoConformidade_nrNaoConformidadeOnChangeHandler();
	if (getElementValue("naoConformidade.nrNaoConformidade") == "") {
		resetDoctoServico();
		habilitaDoctoServico();
	}
	return r;
}


function retornoFilialNaoConformidade_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = naoConformidade_filial_sgFilial_exactMatch_cb(data)
	if (r == true) {
		setDisabled('naoConformidade.idNaoConformidade', false);
		setFocus(document.getElementById("naoConformidade.nrNaoConformidade"));
	}
	return r;
}

function naoConformidadePopupSetValue(data) {
	resetDoctoServico();
	desabilitaDoctoServico();
	setDisabled('naoConformidade.idNaoConformidade', false);
}

function retornoNrNaoConformidade_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = naoConformidade_nrNaoConformidade_exactMatch_cb(data);
	if (r == true) {
		resetDoctoServico();
		desabilitaDoctoServico();
		setFocus("tpFormatoRelatorio");		
	}
	return r;
}
// ------------------------------------- Nao conformidade ----------------------------------------



function afterPopupDoctoServicoNrDoctoServico() {
	buscarNaoConformidade(getElementValue("naoConformidade.doctoServico.idDoctoServico"));
}


/**
 * Quando o "Número do documento" for informado 
 */
function retornoDocumentoServico_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = naoConformidade_doctoServico_nrDoctoServico_exactMatch_cb(data);
	if (r == true) {
		var idDoctoServico = getElementValue("naoConformidade.doctoServico.idDoctoServico");
		if (idDoctoServico != undefined && idDoctoServico != '') {
			buscarNaoConformidade(idDoctoServico);
		}
	}
}

/**
 * Realiza pesquisa em NaoConformidade a fim de verificar se existe algum registro associado ao documento de serviço informado.
 */
function buscarNaoConformidade(idDoctoServico) { 
	var sdo = createServiceDataObject("lms.rnc.emitirRNCAction.findNaoConformidade", "resultado_buscarNaoConformidade", 
			{doctoServico:{idDoctoServico:idDoctoServico}});
	xmit({serviceDataObjects:[sdo]});
}

/**
 * Retorno da pesquisa em NaoConformidade.
 */
function resultado_buscarNaoConformidade_cb(data, error) {
	if (data != undefined && getNestedBeanPropertyValue(data,"nrNaoConformidade") != undefined) {
		setDisabled('naoConformidade.nrNaoConformidade', false);
		setElementValue('naoConformidade.filial.idFilial', getNestedBeanPropertyValue(data,"filial.idFilial"));
		setElementValue('naoConformidade.filial.sgFilial', getNestedBeanPropertyValue(data,"filial.sgFilial"));
		setElementValue('naoConformidade.idNaoConformidade',getNestedBeanPropertyValue(data,"idNaoConformidade"));
		setElementValue('naoConformidade.nrNaoConformidade', getFormattedValue("integer", getNestedBeanPropertyValue(data, "nrNaoConformidade"), "00000000", true));
		setElementValue('tpStatusNaoConformidade',getNestedBeanPropertyValue(data,"tpStatusNaoConformidade.description"));
		desabilitaDoctoServico();
		setFocus("tpFormatoRelatorio");
		
	} else {
		alert(msg);
		setElementValue("naoConformidade.doctoServico.nrDoctoServico", "");
		setFocus(document.getElementById("naoConformidade.doctoServico.nrDoctoServico"));
	}
}

function limpaTela(){
	cleanButtonScript(this.document);
	setDisabled("naoConformidade.doctoServico.idDoctoServico", true);
}
</script>