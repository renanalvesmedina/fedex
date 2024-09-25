<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	setMasterLink(document, true);
	onPageLoad();
}
</script>
<adsm:window title="documentos" service="lms.carregamento.consultarControleCargasJanelasAction" onPageLoad="carregaPagina" 
			 onPageLoadCallBack="retornoCarregaPagina" >

	<adsm:form action="/carregamento/consultarControleCargasDocumentosServico">
		<adsm:section caption="documentos" />
		
		<adsm:hidden property="idDoctoServico" />		

		<adsm:hidden property="idManifesto" />
		<adsm:hidden property="manifesto.tpManifesto.value" />
		<adsm:hidden property="dhEmissaoManifesto" />

		<adsm:hidden property="controleCarga.idControleCarga" serializable="false" />
		<adsm:textbox dataType="text" label="controleCargas" property="controleCarga.filialByIdFilialOrigem.sgFilial"
					  size="3" labelWidth="18%" width="82%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="controleCarga.nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox dataType="text" property="manifesto.tpManifesto.description" label="manifesto" size="20" 
					  labelWidth="18%" width="37%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="manifesto.filialByIdFilialOrigem.sgFilial" size="3" disabled="true" serializable="false" />
			<adsm:textbox dataType="integer" property="manifesto.nrManifesto" size="8" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox dataType="text" property="meioTransporteByIdTransportado.nrFrota" 
					  label="meioTransporte" width="30%" size="6" serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporteByIdTransportado.nrIdentificador" 
						  size="24" serializable="false" disabled="true" />
		</adsm:textbox>

		<adsm:combobox property="doctoServico.tpDocumentoServico"
					   label="documentoServico" labelWidth="18%" width="82%" serializable="true" 
					   service="lms.carregamento.consultarControleCargasJanelasAction.findTipoDocumentoServico"
					   optionProperty="value" optionLabelProperty="description"
					   onchange="return changeDocumentWidgetType({
						   documentTypeElement:this,
						   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
						   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
						   parentQualifier:'',
						   documentGroup:'SERVICE',
						   actionService:'lms.carregamento.consultarControleCargasJanelasAction'
					   });"> 
			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial"
						 service=""
						 disabled="true" popupLabel="pesquisarFilial"
						 action=""
						 size="3" maxLength="3" picker="false" onDataLoadCallBack="enableDoctoServico" serializable="true" 
						 onchange="return changeDocumentWidgetFilial({
							 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
							 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
							 });"
						 />
			<adsm:lookup dataType="integer"
						 property="doctoServico"
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
						 service=""
						 action=""
						 onDataLoadCallBack="retornoDocumentoServico"
						 popupLabel="pesquisarDocumentoServico"
						 size="10" maxLength="8" serializable="true" disabled="true" mask="00000000" />
		</adsm:combobox>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton id="botaoFind" callbackProperty="documentos"/>
			<adsm:button caption="limpar" id="botaoLimpar" onclick="botaoLimpar_OnClick();" />
		</adsm:buttonBar>
	</adsm:form>


	<adsm:grid idProperty="idDoctoServico" property="documentos" selectionMode="none" 
			   unique="true" autoSearch="false" scrollBars="horizontal" gridHeight="252" rows="12"
			   service="lms.carregamento.consultarControleCargasJanelasAction.findPaginatedDocumentos"
			   rowCountService="lms.carregamento.consultarControleCargasJanelasAction.getRowCountDocumentos"
			   onRowClick="documentos_OnClick"
	>
		<adsm:gridColumn title="documentoServico" 	property="tpDoctoServico" isDomain="true" width="45"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" 					property="sgFilial" width="40" />
			<adsm:gridColumn title="" 					property="nrDoctoServico" width="55" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="destino" 			property="sgFilialDestino" width="80" />
		<adsm:gridColumn title="dpe" 				property="dtPrevEntrega" width="80" dataType="JTDate" align="center" />
		<adsm:gridColumn title="emissao" 			property="dhEmissao" width="120" dataType="JTDateTimeZone" align="center" />
		<adsm:gridColumn title="servico" 			property="sgServico" width="70" />
		<adsm:gridColumn title="remetente" 			property="nmPessoaRemetente" width="250"/>
		<adsm:gridColumn title="destinatario" 		property="nmPessoaDestinatario" width="250" />
		<adsm:gridColumn title="volumes" 			property="qtVolumes" width="80" align="right" />
		<adsm:gridColumn title="peso" 				property="psReal" width="120" dataType="decimal" align="right" unit="kg" mask="###,###,###,##0.000" />
		<adsm:gridColumn title="pesoAforado"		property="psAforado" width="120" dataType="decimal" align="right" unit="kg" mask="###,###,###,##0.000" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" 					property="sgMoeda" width="28" />
			<adsm:gridColumn title="" 						property="dsSimbolo" width="28" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 						property="vlMercadoria" dataType="currency" align="right" width="100" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valorFrete"				property="sgMoedaVlTotal" width="28" />
			<adsm:gridColumn title="" 						property="dsSimboloVlTotal" width="28" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 						property="vlTotalDocServico" dataType="currency" align="right" width="100" />
		<adsm:gridColumn title="ocorrenciaEntrega"	property="dsOcorrenciaEntrega" width="200" />
		<adsm:gridColumn title="volumes" 			property="volumes" image="/images/popup.gif" openPopup="true" link="javascript:exibirVolumes" align="center" width="85" />

		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		setDisabled("botaoFind", false);
		setDisabled("botaoLimpar", false);
		setDisabled("botaoFechar", false);
		povoaDadosMaster();
	}
}

function povoaDadosMaster() {
	setElementValue("controleCarga.idControleCarga", dialogArguments.window.document.getElementById('controleCarga.idControleCarga').value);
	setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", dialogArguments.window.document.getElementById('controleCarga.filialByIdFilialOrigem.sgFilial').value);
	setElementValue("controleCarga.nrControleCarga", dialogArguments.window.document.getElementById('controleCarga.nrControleCarga').value);
	setElementValue("meioTransporteByIdTransportado.nrFrota", dialogArguments.window.document.getElementById('meioTransporteByIdTransportado.nrFrota').value);
	setElementValue("meioTransporteByIdTransportado.nrIdentificador", dialogArguments.window.document.getElementById('meioTransporteByIdTransportado.nrIdentificador').value);
	var sdo = createServiceDataObject("lms.carregamento.consultarControleCargasJanelasAction.findDadosManifesto", 
			"retornoFindDadosManifesto", 
			{idManifesto:getElementValue("idManifesto")});
    xmit({serviceDataObjects:[sdo]});
}


function retornoFindDadosManifesto_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue("manifesto.tpManifesto.value", getNestedBeanPropertyValue(data,"tpManifesto.value"));
	setElementValue("manifesto.tpManifesto.description", getNestedBeanPropertyValue(data,"tpManifesto.description"));
	setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", getNestedBeanPropertyValue(data,"filialByIdFilialOrigem.sgFilial"));
	setElementValue("manifesto.nrManifesto", getFormattedValue("integer", getNestedBeanPropertyValue(data,"nrManifesto"), "00000000", true));
	setElementValue("dhEmissaoManifesto", getNestedBeanPropertyValue(data,"dhEmissaoManifesto"));
	povoaGrid();
}


function documentos_OnClick(id) {
	return false;
}

function povoaGrid() {
	var filtro = new Array();
	setNestedBeanPropertyValue(filtro, "idDoctoServico", getElementValue("doctoServico.idDoctoServico"));
    setNestedBeanPropertyValue(filtro, "idManifesto", getElementValue("idManifesto"));
    setNestedBeanPropertyValue(filtro, "manifesto.tpManifesto.value", getElementValue("manifesto.tpManifesto.value"));
    setNestedBeanPropertyValue(filtro, "dhEmissaoManifesto", getElementValue("dhEmissaoManifesto"));
    documentosGridDef.executeSearch(filtro, true);
}

function botaoLimpar_OnClick() {
	resetValue("doctoServico.tpDocumentoServico");
	resetValue("doctoServico.filialByIdFilialOrigem.idFilial");
	resetValue("doctoServico.idDoctoServico");
	setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
	setDisabled("doctoServico.idDoctoServico", true);
	povoaGrid();
	setFocusOnFirstFocusableField();
}

function enableDoctoServico_cb(data) {
   var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
   if (r == true) {
      setDisabled("doctoServico.idDoctoServico", false);
      setFocus(document.getElementById("doctoServico.nrDoctoServico"));
   }
}

function retornoDocumentoServico_cb(data) {
	doctoServico_nrDoctoServico_exactMatch_cb(data);
}

function exibirVolumes(id){
	setElementValue("idDoctoServico", id);
	showModalDialog("/expedicao/consultarDocumentoServicoVolumes.do?cmd=main" , window, "unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;");
}
</script>