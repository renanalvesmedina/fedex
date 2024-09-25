<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.rnc.manterNaoConformidadeAction" onPageLoadCallBack="carregaPagina">
	<adsm:form action="/rnc/manterNaoConformidade" idProperty="idNaoConformidade" >

		<adsm:range label="periodo" labelWidth="20%" width="80%" >
			<adsm:textbox property="ocorrenciaNaoConformidade.dataInclusaoInicial" dataType="JTDate" size="10" maxLength="10" picker="true" />
			<adsm:textbox property="ocorrenciaNaoConformidade.dataInclusaoFinal" dataType="JTDate" size="10" maxLength="10" picker="true"/>
		</adsm:range>


		<adsm:lookup label="naoConformidade" dataType="text" 
					 property="filial" idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.rnc.manterNaoConformidadeAction.findLookupFilial" 
					 action="/municipios/manterFiliais" popupLabel="pesquisarFilial"
					 size="3" maxLength="3" labelWidth="20%" width="30%" 
					 onchange="return sgFilial_OnChange();"
					 picker="false" serializable="true" >
			<adsm:textbox dataType="integer" property="nrNaoConformidade" mask="00000000" size="8" maxLength="8" onchange="nrNaoConformidade_OnChange();" />
		</adsm:lookup>

		<adsm:lookup label="rncLegado" dataType="text" 
					 property="filialLegado" idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.rnc.manterNaoConformidadeAction.findLookupFilial" 
					 action="/municipios/manterFiliais" popupLabel="pesquisarFilial"
					 size="3" maxLength="3" labelWidth="15%" width="35%"
					 onchange="return sgFilialLegado_OnChange();"
					 picker="false" serializable="true" >
			<adsm:textbox dataType="integer" property="nrRncLegado" mask="00000000" size="8" maxLength="8" onchange="nrRncLegado_OnChange();"/>
		</adsm:lookup>

		<adsm:combobox property="doctoServico.tpDocumentoServico" 
					   service="lms.rnc.manterNaoConformidadeAction.findTipoDocumentoServico"
					   optionProperty="value" optionLabelProperty="description"
					   label="documentoServico" labelWidth="20%" width="30%" serializable="true"
					   onchange="return changeDocumentWidgetType({
					   		documentTypeElement:this, 
					   		filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
					   		documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
					   		documentGroup:'SERVICE',
					   		actionService:'lms.rnc.manterNaoConformidadeAction'
					   	});">

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" popupLabel="pesquisarFilial"
						 action="" 
						 size="3" maxLength="3" picker="false" disabled="true" serializable="true"
 						 onchange="return changeDocumentWidgetFilial({
							 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
							 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
						  }); "/>

			<adsm:lookup dataType="integer"
						 property="doctoServico" 
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 service="" 
						 action="" 
						 popupLabel="pesquisarDocumentoServico"
						 onDataLoadCallBack="retornoDocumentoServico"
						 size="10" serializable="true" disabled="true" />
		</adsm:combobox>
		
		<adsm:combobox label="awb" property="tpStatusAwb"
					   domain="DM_LOOKUP_AWB" 
					   labelWidth="15%" width="35%"
					   defaultValue="E" renderOptions="true" disabled="true">
        
			<adsm:lookup property="ciaFilialMercurio.empresa" 
						 idProperty="idEmpresa"
						 dataType="text"
						 criteriaProperty="sgEmpresa"
				 		 criteriaSerializable="true"
						 service="lms.rnc.manterNaoConformidadeAction.findLookupSgCiaAereaAwb"
						 action="" 	
						 size="3" maxLength="3"						 
					 	picker="false">
			</adsm:lookup>

	        <adsm:lookup dataType="integer" size="13" maxLength="11" 
	        	property="awb"
	        	idProperty="idAwb"
	        	criteriaProperty="nrAwb"
	        	criteriaSerializable="true"
	 			service="lms.rnc.manterNaoConformidadeAction.findLookupAwb"
				action="expedicao/consultarAWBs"
				onDataLoadCallBack="awbOnDataLoadCallBack"
				onPopupSetValue="findAwb_cb">
				
				<adsm:propertyMapping modelProperty="tpStatusAwb" criteriaProperty="tpStatusAwb" disable="true" />
				<adsm:propertyMapping modelProperty="ciaFilialMercurio.empresa.idEmpresa" criteriaProperty="ciaFilialMercurio.empresa.idEmpresa" disable="true" />
	        </adsm:lookup>
	    </adsm:combobox>


		<adsm:combobox property="tpStatusNaoConformidade" label="status" domain="DM_STATUS_NAO_CONFORMIDADE" labelWidth="20%" width="30%" renderOptions="true"/>
		
		<adsm:combobox property="naoConformidade.tpModal" label="modal" domain="DM_MODAL"
				labelWidth="15%" width="35%" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="botaoConsultar" onclick="consultar_OnClick(this.form);" />
			<adsm:button caption="limpar" id="botaoLimpar" onclick="limpar_OnClick()" disabled="false" buttonType="newButton" />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="naoConformidades" idProperty="idNaoConformidade" selectionMode="none" unique="true" rows="11"
			   service="lms.rnc.manterNaoConformidadeAction.findPaginatedGridNaoConformidade"
			   rowCountService="lms.rnc.manterNaoConformidadeAction.getRowCountGridNaoConformidade" >
		<adsm:gridColumnGroup separatorType="RNC">	   
			<adsm:gridColumn title="naoConformidade" 	property="filial.sgFilial" width="50" />
			<adsm:gridColumn title="" 				 	property="nrNaoConformidade" width="80" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>	
		<adsm:gridColumn title="documentoServico" 	property="doctoServico.tpDocumentoServico" isDomain="true" width="45"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" 					property="doctoServico.filialByIdFilialOrigem.sgFilial" width="45" />
			<adsm:gridColumn title="" 					property="doctoServico.nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="status" 			property="tpStatusNaoConformidade" isDomain="true" />
	</adsm:grid>

	<adsm:buttonBar> 
		<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />		
	</adsm:buttonBar>
</adsm:window>

<script>

// seta seta como visivel o botão fechar, em caso seja uma lookup
var isLookup = window.dialogArguments && window.dialogArguments.window;
if (isLookup) {
	setDisabled('btnFechar',false);
} else {
	setVisibility('btnFechar', false);
}	

function initWindow(eventObj) {
	setDisabled("botaoConsultar", false);
	setElementValue("tpStatusNaoConformidade", "RNC");
	if (eventObj.name=="tab_click"){
		if(getElementValue("doctoServico.filialByIdFilialOrigem.idFilial")!=""){
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", false);
		}
		if(getElementValue("doctoServico.idDoctoServico")!=""){
			setDisabled("doctoServico.idDoctoServico", false);
		}
	}
	setFocusOnFirstFocusableField();
}

function limpar_OnClick(){
	resetValue('ocorrenciaNaoConformidade.dataInclusaoInicial');
	resetValue('ocorrenciaNaoConformidade.dataInclusaoFinal');
	resetValue('filial.idFilial');
	setElementValue('nrNaoConformidade', '');
	resetValue('tpStatusNaoConformidade');
	resetValue('doctoServico.idDoctoServico');
	resetValue('doctoServico.tpDocumentoServico');
	resetValue('doctoServico.filialByIdFilialOrigem.idFilial');
	setDisabled('doctoServico.filialByIdFilialOrigem.idFilial', true);
	setDisabled('doctoServico.idDoctoServico', true);
	setElementValue("tpStatusNaoConformidade", "RNC");
	
	resetValue('naoConformidade.tpModal');
	resetValue('ciaFilialMercurio.empresa.idEmpresa');
	resetValue('awb.idAwb');
	
	resetValue('filialLegado.idFilial');
	resetValue('nrRncLegado');
	loadDataUsuario();
	setFocusOnFirstFocusableField();
}

function carregaPagina_cb() {
	onPageLoad_cb();
	setElementValue("tpStatusNaoConformidade", "RNC");
	loadDataUsuario();
	setDisabled("botaoConsultar", false);
}


/**
 * Quando o "Número do documento" for informado
 */
function retornoDocumentoServico_cb(data) {
	doctoServico_nrDoctoServico_exactMatch_cb(data);
}


function loadDataUsuario() {
	var sdo = createServiceDataObject("lms.rnc.manterNaoConformidadeAction.getDataUsuario", "resultado_loadDataUsuario");
   	xmit({serviceDataObjects:[sdo]});
}


function resultado_loadDataUsuario_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue("ocorrenciaNaoConformidade.dataInclusaoInicial", setFormat(document.getElementById("ocorrenciaNaoConformidade.dataInclusaoInicial"), getNestedBeanPropertyValue(data, "dtInicial")));
	setElementValue("ocorrenciaNaoConformidade.dataInclusaoFinal", setFormat(document.getElementById("ocorrenciaNaoConformidade.dataInclusaoFinal"), getNestedBeanPropertyValue(data, "dtAtual")));
}


function sgFilial_OnChange() {
	var r = filial_sgFilialOnChangeHandler();
	if (getElementValue("filial.idFilial") != "" && getElementValue("nrNaoConformidade") != "") {
		resetValue("ocorrenciaNaoConformidade.dataInclusaoInicial");
		resetValue("ocorrenciaNaoConformidade.dataInclusaoFinal");
	} 
	return r;
}

function nrNaoConformidade_OnChange() {
	if (getElementValue("filial.idFilial") != "" && getElementValue("nrNaoConformidade") != "") {
		resetValue("ocorrenciaNaoConformidade.dataInclusaoInicial");
		resetValue("ocorrenciaNaoConformidade.dataInclusaoFinal");
	} 
}

function sgFilialLegado_OnChange() {
	var r = filialLegado_sgFilialOnChangeHandler();
	if (getElementValue("filialLegado.idFilial") != "" && getElementValue("nrRncLegado") != "") {
		resetValue("ocorrenciaNaoConformidade.dataInclusaoInicial");
		resetValue("ocorrenciaNaoConformidade.dataInclusaoFinal");
	} 
	return r;
}

function nrRncLegado_OnChange() {
	if (getElementValue("filialLegado.idFilial") != "" && getElementValue("nrRncLegado") != "") {
		resetValue("ocorrenciaNaoConformidade.dataInclusaoInicial");
		resetValue("ocorrenciaNaoConformidade.dataInclusaoFinal");
	} 
}


function consultar_OnClick(form) {
	naoConformidadesGridDef.resetGrid();
	var sdo = createServiceDataObject("lms.rnc.manterNaoConformidadeAction.validatePaginatedRnc", 
		"retornoConsultar", buildFormBeanFromForm(form, 'LIKE_END'));
	xmit({serviceDataObjects:[sdo]});
}

function retornoConsultar_cb(data, error) {
	if (error != undefined) {
		alert(error);
		if (getElementValue("ocorrenciaNaoConformidade.dataInclusaoInicial") == "" || getElementValue("ocorrenciaNaoConformidade.dataInclusaoFinal") == "") {
			if (getElementValue("ocorrenciaNaoConformidade.dataInclusaoInicial") == "")
				setFocus(document.getElementById("ocorrenciaNaoConformidade.dataInclusaoInicial"));
			else
				setFocus(document.getElementById("ocorrenciaNaoConformidade.dataInclusaoFinal"));
		}
		else
			setFocus(document.getElementById("filial.sgFilial"));

		return false;
	}
	findButtonScript('naoConformidades', document.forms[0]);
}

function awbOnDataLoadCallBack_cb(data) {
	awb_nrAwb_exactMatch_cb(data);
	if(data != null && data.length > 0){
		data = data[0] != null ? data[0] : data;			
	}
}

function findAwb_cb(data, error){
	if(data != null){
		data = data[0] != null ? data[0] : data;
		setElementValue("awb.idAwb", data.idAwb);
		setElementValue("ciaFilialMercurio.empresa.idEmpresa", data.ciaFilialMercurio.empresa.idEmpresa);
		setElementValue("ciaFilialMercurio.empresa.sgEmpresa", data.ciaFilialMercurio.empresa.sgEmpresa);
	}
}

</script>