<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction" onPageLoadCallBack="pageLoad">
	<adsm:grid property="ParcelaTabelaCe" onRowClick="onRowClick" idProperty="idParcelaTabelaCe"
				selectionMode="none" showRowIndex="false" autoAddNew="false" gridHeight="120" onValidate="validField"
 	    		showGotoBox="false" showPagging="false" showTotalPageCount="false"
 	    		rowCountService="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.getRowCountGridParcelas"
 	    		service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findGridParcelas">
			<adsm:gridColumn title="tipoParcela" property="tpParcela.description" width="180" />
			<adsm:editColumn property="tpParcela.value"  title=""  field="hidden" width="0"/>
			<adsm:editColumn dataType="currency" property="vlSugerido" title="valorSugerido" field="textbox" align="right"/>
			<adsm:editColumn dataType="currency" property="vlFreteReferencia" title="valorReferencia" field="textbox" align="right"/>
			<adsm:editColumn dataType="currency" property="vlMaximoAprovado" title="valorAutorizadoAte" field="textbox" align="right"/>
			<adsm:editColumn dataType="currency" property="vlNegociado" title="valorNegociado" field="textbox" align="right"/>
			<adsm:editColumn dataType="currency" property="pcSobreValor" title="percentualSobreValor" field="textbox" align="right"/>		
			<adsm:buttonBar>
				<adsm:button  caption="salvarTudo" id="salvaTudo" onclick="getTabGroup(this.document).selectTab(1);getTabGroup(this.document).getTab('cad').getDocument().parentWindow.store()" disabled="false"/>
			</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
<script type="text/javascript">
<!--
	var gridParcela;
	
	function validField(rowIndex, columnName, objCell) {
		return true;	
	}
	function onRowClick() {
		return false;
	}
	
	function onShowTemp() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		
		var idTabelaColetaEntrega = tabDet.getFormProperty("tabelaColetaEntrega.idTabelaColetaEntrega");
		var idTipoMeioTransporte = tabDet.getFormProperty("tipoMeioTransporte.idTipoMeioTransporte");
		var dtVigenciaInicial = dropFormat(tabDet.getDocument().getElementById("tabelaColetaEntrega.dtVigenciaInicial"));
		var idFilial = tabDet.getFormProperty("filial.idFilial");
		var tpSituacaoContratacao = getTabGroup(this.document).getTab("cad").getFormProperty("tpSituacaoContratacao");
		
		if (tabDet.getFormProperty("reconsultarFilho") == "0") {
			behavior();
			return false;
		}
		tabDet.getDocument().getElementById("reconsultarFilho").value = "0";
		loadGrid(idTabelaColetaEntrega,idTipoMeioTransporte,dtVigenciaInicial,idFilial,tpSituacaoContratacao);
	}
	
	function behavior() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		var behavior = tabDet.getFormProperty("behavior");
		var workFlow = tabDet.getFormProperty("idProcessoWorkflow");
		
		var numRows = gridParcela.currentRowCount;
		var tipoParcela = gridParcela.getCellObject(0,"tpParcela.value").value;

   		gridParcela.setDisabledColumn("vlSugerido",true);
   		gridParcela.setDisabledColumn("vlFreteReferencia",true);
   		gridParcela.setDisabledColumn("vlMaximoAprovado",true);
   		gridParcela.setDisabledColumn("vlNegociado",true);
   		gridParcela.setDisabledColumn("pcSobreValor",true);
   		
    	if (tabDet.getFormProperty("warning") == "")  {
	    	setDisabled("salvaTudo",false);
	    	var x = 0;
			if (behavior == "9") {
				gridParcela.setDisabledColumn("vlNegociado",false);
				for (var n = 0; n < gridParcela.currentRowCount; n++){
					if(gridParcela.getCellObject(n,"tpParcela.value").value == "DH"){
						setDisabled("ParcelaTabelaCe:"+n+".pcSobreValor",false);	
					}
					
				}
	    		// deve ter duas chamadas ao método que seta o foco para
	    		// corrigir problema de foco do IE
				setFocus(gridParcela.getCellObject(x,"vlNegociado"),false);
				//setFocus(gridParcela.getCellObject(x,"vlNegociado"),false);
				//setFocus(gridParcela.getCellObject(x,"vlNegociado"),false);
				//setFocus(gridParcela.getCellObject(x,"vlNegociado"),false);
			}else if (behavior == "7" && workFlow != "") {
				gridParcela.setDisabledColumn("vlMaximoAprovado",false);
				for (var n = 0; n < gridParcela.currentRowCount; n++){
					if(gridParcela.getCellObject(n,"tpParcela.value").value == "PF"){
						setDisabled("ParcelaTabelaCe:"+n+".pcSobreValor",false);	
					} else if(gridParcela.getCellObject(n,"tpParcela.value").value == "PV"){
						setDisabled("ParcelaTabelaCe:"+n+".pcSobreValor",false);
					}
				}				
				
			}else if (behavior == "8" && workFlow != "") {
	    		gridParcela.setDisabledColumn("vlMaximoAprovado",false);
	    		for (var n = 0; n < gridParcela.currentRowCount; n++){
					if(gridParcela.getCellObject(n,"tpParcela.value").value == "DH"){
						setDisabled("ParcelaTabelaCe:"+n+".pcSobreValor",false);	
					}
					
					//LMS-4808
					if(gridParcela.getCellObject(n,"tpParcela.value").value == "PF"){
						setDisabled("ParcelaTabelaCe:"+n+".pcSobreValor",false);	
					}
					
					if(gridParcela.getCellObject(n,"tpParcela.value").value == "PV"){
						setDisabled("ParcelaTabelaCe:"+n+".pcSobreValor",false);	
					}
					
				}
	    		// deve ter duas chamadas ao método que seta o foco para
	    		// corrigir problema de foco do IE
				setFocus(gridParcela.getCellObject(x,"vlMaximoAprovado"),false);
				//setFocus(gridParcela.getCellObject(x,"vlMaximoAprovado"),false);
	    	}else if (behavior == "") {
	    		gridParcela.setDisabledColumn("vlSugerido",false);
	    		for (var n = 0; n < gridParcela.currentRowCount; n++){
					if(gridParcela.getCellObject(n,"tpParcela.value").value == "DH"){
						setDisabled("ParcelaTabelaCe:"+n+".pcSobreValor",false);	
					}
					
				}
	    		// deve ter duas chamadas ao método que seta o foco para
	    		// corrigir problema de foco do IE
				setFocus(gridParcela.getCellObject(x,"vlSugerido"),false);
				//setFocus(gridParcela.getCellObject(x,"vlSugerido"),false);
	    	}else
	    		setDisabled("salvaTudo",true);
	    }
    	
		//LMS-3581 - Independente do que foi setado acima, o campo 
		//'pcSobreValor' da Diária será sempre desabilitado.
		setDisabled("ParcelaTabelaCe:0.pcSobreValor",true);

		//LMS-3581 - 'Apaga' da linha 0 o campo '% sobre valor'
		var linha_parcela = document.getElementById("ParcelaTabelaCe:0.pcSobreValor");
		linha_parcela.style.border = "none";
		linha_parcela.style.background="none";

	}
	
	function loadGrid(idTabelaColetaEntrega,idTipoMeioTransporte,dtVigenciaInicial,idFilial,tpSituacaoContratacao) {
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findGridParcelas",
			   "writeRowsGrid",{idTabelaColetaEntrega:idTabelaColetaEntrega,idTipoMeioTransporte:idTipoMeioTransporte,dtVigenciaInicial:dtVigenciaInicial,idFilial:idFilial,tpSituacaoContratacao:tpSituacaoContratacao});
        xmit({serviceDataObjects:[sdo]}); 
	}
	
	function writeRowsGrid_cb(data) {
		writeRowsGridAndBehavior(data, true);
	}
	
	function writeRowsGridAndBehavior(data, useBehavior){
		gridParcela.resetGrid();
		var table = document.getElementById("ParcelaTabelaCe.dataTable");
		var tableBody = null;
	    if (table.tBodies.length > 0) {
            tableBody = table.tBodies[0];
      	}else{
            tableBody = document.createElement("tbody");
            table.appendChild(tableBody);
	    }

		for (var x = 0; x < data.length; x++){
     	    gridParcela.createRow(new Array(), x , data[x], tableBody, undefined);											
		}

		if(useBehavior){
			setTimeout(behavior, 100);		
		}
	}
	
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		gridParcela = document.getElementById("ParcelaTabelaCe.dataTable").gridDefinition;
	}
	
	function initWindow(eventObj) {
		var tabGroup = getTabGroup(this.document);
		if (eventObj.name == "tab_click") {
			var changedCad = tabGroup.getTab("cad").changed;
			if (changedCad) {
				tabGroup.getTab("parc").changed = true;
			}
			onShowTemp();
		}
	}	
//-->
</script>
