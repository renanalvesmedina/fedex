<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script>
function carregaPagina(){
	onPageLoad();
}
</script>
<adsm:window service="lms.recepcaodescarga.descarregarVeiculoAction" onPageLoad="carregaPagina" onPageLoadCallBack="retornoCarregaPagina">
	<adsm:form idProperty="idDoctoServico" action="/recepcaoDescarga/descarregarVeiculo"
			   service="lms.recepcaodescarga.descarregarVeiculoAction.findControleCargaConhScanByIdDoctoServico">
		
		<adsm:hidden property="idDoctoServico" serializable="false"/>
		<adsm:hidden property="tpDocumentoServico" serializable="false"/>
		<adsm:hidden property="sgFilialOrigem" serializable="false"/>
		<adsm:hidden property="nrDoctoServico" serializable="false"/>


		<adsm:hidden property="idControleCarga" />
		<adsm:textbox label="controleCarga" property="sgControleCarga" dataType="text"  
					  disabled="true" size="3" maxLength="3" labelWidth="18%" width="82%">
			<adsm:textbox property="nrControleCarga" dataType="text" disabled="true" 
						  size="8" maxLength="8" />
		</adsm:textbox>
		
		<adsm:textbox label="codigoBarras" labelWidth="18%" width="82%"  property="nrCodigoBarras" dataType="text" disabled="false" 
						  size="50" maxLength="44" serializable="true" required="true" />
							
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton  callbackProperty="afterStore" caption="salvar" service="lms.recepcaodescarga.descarregarVeiculoAction.storeControleCargaConhecimento"/>
			<adsm:resetButton caption="limpar"/>	
			<adsm:button id="exportar" caption="exportar" onclick="emitirReport()" />
		</adsm:buttonBar>

	</adsm:form>
	
	<adsm:grid idProperty="idControleCargaConhScan" property="doctoServico" selectionMode="none" onRowClick="onRowClick"	   
			   gridHeight="250" unique="true" autoSearch="false" scrollBars="both"	   
   			   service="lms.recepcaodescarga.descarregarVeiculoAction.findPaginatedDocumento" 
			   rowCountService="lms.recepcaodescarga.descarregarVeiculoAction.getRowCountDocumento">	
		<adsm:gridColumn title="documentoServico" property="doctoServico.tpDocumento" isDomain="true" width="30"/>
        <adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="" property="sgFilialOrigem" width="30" />
            <adsm:gridColumn title="" property="nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="destino" property="sgFilialDestino" width="60" />
		<adsm:gridColumn title="dpe" property="dtPrevEntrega" dataType="JTDate" width="100" align="center" />
		<adsm:gridColumn title="servico" property="sgServico" width="90" />
		<adsm:gridColumn title="situacaoDocumento" property="situacaoDocumento" width="200" />		
		<adsm:gridColumn title="remetente" property="clienteRemetente" width="215" />
		<adsm:gridColumn title="destinatario" property="clienteDestinatario" width="215" />
		<adsm:gridColumn title="volumes" property="qtVolumes" width="100" align="right" />
		<adsm:gridColumn title="peso" property="psReal" width="100" unit="kg" dataType="decimal" mask="###,###,##0.000"/>
        <adsm:gridColumnGroup customSeparator=" ">
        	<adsm:gridColumn title="valorMercadoria" property="sgMoeda" dataType="text" width="30"/>
            <adsm:gridColumn title="" property="dsSimboloMoeda" width="30"/>
        </adsm:gridColumnGroup>
        <adsm:gridColumn title="" property="vlMercadoria" dataType="currency" width="70"/>		
        <adsm:gridColumnGroup customSeparator=" ">
        	<adsm:gridColumn title="valorFrete" property="sgMoeda2" dataType="text" width="30"/>
         	<adsm:gridColumn title="" property="dsSimboloMoeda2" width="30"/>
        </adsm:gridColumnGroup>
        <adsm:gridColumn title="" property="vlTotalDocServico" dataType="currency" width="70"/>
	</adsm:grid>

</adsm:window>

<script type="text/javascript">
	var adicionouEvento = false;

	function initWindow(eventObj) {		
		if(eventObj.name == "tab_click") {
			resetValue(this.document);
			doctoServicoGridDef.resetGrid();
		}
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("recepcaoDescarga");
		var idControleCarga = tabDet.getFormProperty("controleCarga.idControleCarga");
		var sgControleCarga = tabDet.getFormProperty("controleCarga.filialByIdFilialOrigem.sgFilial");
		var nrControleCarga = tabDet.getFormProperty("controleCarga.nrControleCarga");
		
		setElementValue("idControleCarga" , idControleCarga);
		setElementValue("sgControleCarga" , sgControleCarga);
		setElementValue("nrControleCarga" , nrControleCarga);
		populaGrid();
		setFocus("nrCodigoBarras");
		if(!adicionouEvento){
			adicionouEvento = true;
			addEvent(document.getElementById("nrCodigoBarras"), "keyup", 
				function(e){
					if(e.keyCode == 13){
						storeButtonScript('lms.recepcaodescarga.descarregarVeiculoAction.storeControleCargaConhecimento', 'afterStore', document.getElementById("form_idDoctoServico"));
					}
				});
		}
		setDisabled("exportar", false);
	}
	
	function afterStore_cb(data,erro) {
		if(erro != undefined) {
			alert(erro);
			setFocus("nrCodigoBarras");
			document.getElementById("nrCodigoBarras").select();
			return false;
		} else {
			document.getElementById("nrCodigoBarras").value = "";
		}
		setFocus("nrCodigoBarras");
		store_cb(data,erro);
	}
	
    function addEvent( obj, type, fn ){ 
        if (obj.addEventListener){ 
            obj.addEventListener( type, fn, false );
        }
        else if (obj.attachEvent){ 
            obj["e"+type+fn] = fn; 
            obj[type+fn] = function(){ obj["e"+type+fn]( window.event ); } 
            obj.attachEvent( "on"+type, obj[type+fn] ); 
        } 
    } 
 
	
	function onRowClick() {
		return false;
	}
	
	function populaGrid() {		
	   	var data = new Object();
		data.idControleCarga = getElementValue("idControleCarga");
		doctoServicoGridDef.executeSearch(data, true);
	}	

	function retornoCarregaPagina_cb(data, error){
		onPageLoad_cb(data,error);
	}
	function emitirReport() {
		var sdo = createServiceDataObject('lms.recepcaodescarga.emitirRelatorioDocumentosServicosExcelAction.execute', 'openPdf', {idControleCarga:getElementValue("idControleCarga")}); 
		executeReportWindowed(sdo,'xls');
	}

</script>