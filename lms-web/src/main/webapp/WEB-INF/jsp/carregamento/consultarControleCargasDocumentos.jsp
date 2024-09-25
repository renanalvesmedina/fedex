<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="conteudoControleCargasManifestosTitulo" service="lms.carregamento.consultarControleCargasJanelasAction" 
			 onPageLoadCallBack="retornoCarregaPagina" >

	<adsm:form action="/carregamento/consultarControleCargasDocumentos">

		<adsm:hidden property="tpFormatoRelatorio" value="xls" serializable="true"/>
		
		<adsm:section caption="conteudoControleCargasManifestos" />

		<adsm:hidden property="tpControleCarga" serializable="true" />
		<adsm:hidden property="controleCarga.idControleCarga" serializable="true" />
		
		<adsm:hidden property="tpOperacao" serializable="true" />
		<adsm:hidden property="idManifestoReport" serializable="true" />

		<adsm:textbox dataType="text" label="controleCargas" property="controleCarga.filialByIdFilialOrigem.sgFilial"
					  size="3" labelWidth="20%" width="80%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="controleCarga.nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:textbox dataType="text" label="valorControleCargas" property="moeda.siglaSimbolo" size="10" 
					  labelWidth="20%" width="80%" disabled="true" serializable="false" >
			<adsm:textbox property="vlTotalFrota" dataType="currency" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" label="valorTotalFrete" property="moeda.siglaSimboloFrete" size="10" 
					  labelWidth="20%" width="80%" disabled="true" serializable="false" >
			<adsm:textbox property="vlTotalFrete" dataType="currency" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:textbox dataType="text" property="meioTransporteByIdTransportado.nrFrota" 
					  label="meioTransporte" labelWidth="20%" width="80%" size="6" serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporteByIdTransportado.nrIdentificador" 
						  size="24" serializable="false" disabled="true" />
		</adsm:textbox>
	</adsm:form>


	<adsm:grid idProperty="idManifesto" property="manifestos" selectionMode="none" 
			   unique="true" autoSearch="false" showPagging="false" scrollBars="both" gridHeight="303"
			   service="lms.carregamento.consultarControleCargasJanelasAction.findPaginatedManifestos"
			   rowCountService=""
			   onRowClick="manifestos_OnClick"
	>
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">	   
			<adsm:gridColumn title="manifesto" 			property="sgFilial" width="30" />
			<adsm:gridColumn title="" 					property="nrManifesto" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>	
		<adsm:gridColumn title="filialDestino" 		property="sgFilialDestino" width="100" />
		<adsm:gridColumn title="tipoOperacao" 		property="tpOperacao" isDomain="true" width="130" />
		<adsm:gridColumn title="modal" 			property="tpModal" isDomain="true" width="130" />
		<adsm:gridColumn title="tipoManifesto" 		property="tpManifesto" isDomain="true" width="140" />
		<adsm:gridColumn title="status" 			property="status" isDomain="true" width="160" />
		<adsm:gridColumn title="peso" 				property="psTotal" width="120" dataType="decimal" align="right" unit="kg" mask="###,###,###,##0.000" />
		<adsm:gridColumn title="pesoAforado"		property="psTotalAforado" width="120" dataType="decimal" align="right" unit="kg" mask="###,###,###,##0.000" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" 			property="sgMoeda" width="30" />
			<adsm:gridColumn title="" 				property="dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 					property="vlMercadoria" dataType="currency" width="110" align="right" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valorFrete" 			property="sgMoedaFrete" width="30" />
			<adsm:gridColumn title="" 				property="dsSimboloFrete" width="30" />			
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 					property="vlFrete" dataType="currency" width="110" align="right" />				
		<adsm:gridColumn title="documentosServico" 	property="documentosServico" image="/images/popup.gif" openPopup="true" link="javascript:exibirDocumentos" align="center" width="120" />
		<adsm:gridColumn title="visualizar" 		property="relatorioControleCarga" image="/images/popup.gif" openPopup="true" link="javascript:imprimeRelatorioControleCarga" linkIdProperty="idManifesto" align="center" width="120" />
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>
function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		povoaDadosMaster();
	}
}

function povoaGrid() {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "controleCarga.idControleCarga", getElementValue("controleCarga.idControleCarga"));
    manifestosGridDef.executeSearch(filtro, true);
    return false;
}


function povoaDadosMaster() {
	setElementValue("controleCarga.idControleCarga", dialogArguments.window.document.getElementById('idControleCarga').value);
	setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", dialogArguments.window.document.getElementById('filialByIdFilialOrigem.sgFilial').value);
	setElementValue("controleCarga.nrControleCarga", dialogArguments.window.document.getElementById('nrControleCarga').value);
	var sdo = createServiceDataObject("lms.carregamento.consultarControleCargasJanelasAction.findDadosControleCargaByManifesto", 
			"retornoFindDadosControleCargaByManifesto", 
			{idControleCarga:getElementValue("controleCarga.idControleCarga")});
    xmit({serviceDataObjects:[sdo]});
}

function retornoFindDadosControleCargaByManifesto_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setElementValue("moeda.siglaSimbolo", getNestedBeanPropertyValue(data, "moeda.siglaSimbolo"));
	setElementValue("vlTotalFrota", setFormat(document.getElementById("vlTotalFrota"), getNestedBeanPropertyValue(data,"vlTotalFrota")));
	setElementValue("moeda.siglaSimboloFrete", getNestedBeanPropertyValue(data, "moeda.siglaSimbolo"));
	setElementValue("vlTotalFrete", setFormat(document.getElementById("vlTotalFrete"), getNestedBeanPropertyValue(data,"vlTotalFrete")));
	setElementValue("meioTransporteByIdTransportado.nrFrota", getNestedBeanPropertyValue(data,"meioTransporteByIdTransportado.nrFrota"));
	setElementValue("meioTransporteByIdTransportado.nrIdentificador", getNestedBeanPropertyValue(data, "meioTransporteByIdTransportado.nrIdentificador"));
	setElementValue("tpControleCarga", getNestedBeanPropertyValue(data, "tpControleCarga"));
	setFocus("botaoFechar");
	povoaGrid();
}

function manifestos_OnClick(id) {
	return false;
}

function exibirDocumentos(id){
	var data = manifestosGridDef.getDataRowById(id);
	var tpOperacao = data.tpOperacao.value;

	if (tpOperacao == "V" || tpOperacao == "E")
		showModalDialog('carregamento/consultarControleCargasDocumentosServico.do?cmd=main&idManifesto=' + id ,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:522px;');
	else
	if (tpOperacao == "C") {
		var dadosURL = "&idManifesto=" + data.idManifesto + "&nrManifesto=" + data.nrManifesto + "&sgFilial=" + data.sgFilial;
		showModalDialog("/recepcaoDescarga/descarregarVeiculo.do?cmd=documentosColetas" + dadosURL , window, "unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;");
	}
}

function imprimeRelatorioControleCarga(idManifesto) {
    if (!validateForm(this.document.forms[0])) {
		return false;
	}
    
    if(manifestosGridDef == null || manifestosGridDef == undefined || manifestosGridDef.getDataRowById(idManifesto) == null 
    		|| manifestosGridDef.getDataRowById(idManifesto) == undefined || (manifestosGridDef.getDataRowById(idManifesto)).tpOperacao == null
    		|| (manifestosGridDef.getDataRowById(idManifesto)).tpOperacao == undefined || ( (manifestosGridDef.getDataRowById(idManifesto)).tpOperacao).value == null
    		|| ((manifestosGridDef.getDataRowById(idManifesto)).tpOperacao).value == undefined ){
    	
    	return false;
    }
    
    setElementValue("tpOperacao", ((manifestosGridDef.getDataRowById(idManifesto)).tpOperacao).value);
    setElementValue("idManifestoReport", idManifesto);
	reportButtonScript('lms.carregamento.consultarControleCargasJanelasAction.executeRelatorioDocumentosControleCarga', 'openPdf', this.document.forms[0]);
}

</script>