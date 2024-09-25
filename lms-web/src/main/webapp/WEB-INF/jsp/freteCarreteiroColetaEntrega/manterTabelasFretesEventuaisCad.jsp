<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	var gridParcela;
	var lineEV = 1;
	var lineFP = 1;
	
	function validateGrid(row) {
		if (row >= 4 ) {
			if (getElementValue(gridParcela.getCellObject(row,"pcSobreValor")) != "" ||
				getElementValue(gridParcela.getCellObject(row,"vlDefinido")) != "") {
				setElementValue(gridParcela.getCellObject(0,"vlDefinido"),"");
				setElementValue(gridParcela.getCellObject(1,"vlDefinido"),"");
				setElementValue(gridParcela.getCellObject(2,"vlDefinido"),"");
				setElementValue(gridParcela.getCellObject(3,"vlDefinido"),"");
				setDisabled(gridParcela.getCellObject(0,"vlDefinido"),true);
				setDisabled(gridParcela.getCellObject(1,"vlDefinido"),true);
				setDisabled(gridParcela.getCellObject(2,"vlDefinido"),true);
				setDisabled(gridParcela.getCellObject(3,"vlDefinido"),true);
				gridParcela.getCellObject(0,"vlDefinido").required = "false";
				gridParcela.getCellObject(1,"vlDefinido").required = "false";
				gridParcela.getCellObject(2,"vlDefinido").required = "false";
				gridParcela.getCellObject(3,"vlDefinido").required = "false";

				if (row == 4) {
					setElementValue(gridParcela.getCellObject(5,"vlDefinido"),"");
					setDisabled(gridParcela.getCellObject(5,"vlDefinido"),true);
					setElementValue(gridParcela.getCellObject(5,"pcSobreValor"),"");
					setDisabled(gridParcela.getCellObject(5,"pcSobreValor"),true);
				}else{
					setElementValue(gridParcela.getCellObject(4,"vlDefinido"),"");
					setDisabled(gridParcela.getCellObject(4,"vlDefinido"),true);
					setElementValue(gridParcela.getCellObject(4,"pcSobreValor"),"");
					setDisabled(gridParcela.getCellObject(4,"pcSobreValor"),true);
				}
				
				gridParcela.getCellObject(row,"vlDefinido").required = "true";
				gridParcela.getCellObject(row,"pcSobreValor").required = "true";
			}else{
				setDisabled(gridParcela.getCellObject(0,"vlDefinido"),false);
				setDisabled(gridParcela.getCellObject(1,"vlDefinido"),false);
				setDisabled(gridParcela.getCellObject(2,"vlDefinido"),false);
				setDisabled(gridParcela.getCellObject(3,"vlDefinido"),false);
				setDisabled(gridParcela.getCellObject(4,"vlDefinido"),false);
				setDisabled(gridParcela.getCellObject(5,"vlDefinido"),false);
				setDisabled(gridParcela.getCellObject(4,"pcSobreValor"),false);
				setDisabled(gridParcela.getCellObject(5,"pcSobreValor"),false);
				gridParcela.getCellObject(4,"vlDefinido").required = "false";
				gridParcela.getCellObject(4,"pcSobreValor").required = "false";
				gridParcela.getCellObject(5,"vlDefinido").required = "false";
				gridParcela.getCellObject(5,"pcSobreValor").required = "false";

				gridParcela.getCellObject(0,"vlDefinido").required = "true";
				gridParcela.getCellObject(1,"vlDefinido").required = "true";
				gridParcela.getCellObject(2,"vlDefinido").required = "true";
				gridParcela.getCellObject(3,"vlDefinido").required = "true";
				
			}
		}else{
			if (getElementValue(gridParcela.getCellObject(0,"vlDefinido")) == "" &&
				getElementValue(gridParcela.getCellObject(1,"vlDefinido")) == "" &&
				getElementValue(gridParcela.getCellObject(2,"vlDefinido")) == "" &&
				getElementValue(gridParcela.getCellObject(3,"vlDefinido")) == "") {
				
				setDisabled(gridParcela.getCellObject(4,"vlDefinido"),false);
				setDisabled(gridParcela.getCellObject(5,"vlDefinido"),false);
					
				setDisabled(gridParcela.getCellObject(4,"pcSobreValor"),false);
				setDisabled(gridParcela.getCellObject(5,"pcSobreValor"),false);
				
				for( x=0 ; x<4 ; x++){
					var vlSugerido 			= getElementValue(gridParcela.getCellObject(x,"vlSugerido"));
					var vlMaximoAprovado 	= getElementValue(gridParcela.getCellObject(x,"vlMaximoAprovado"));
					var vlNegociado 		= getElementValue(gridParcela.getCellObject(x,"vlNegociado"));
					if(vlSugerido != "" || vlMaximoAprovado != "" || vlNegociado != ""){
						setDisabled(gridParcela.getCellObject(x,"vlDefinido"),false);
						gridParcela.getCellObject(x,"vlDefinido").required = "true";
					}else{
						setDisabled(gridParcela.getCellObject(x,"vlDefinido"),true);
						gridParcela.getCellObject(x,"vlDefinido").required = "false";
					}
				}
				
			}else{
				
				setDisabled(gridParcela.getCellObject(4,"vlDefinido"),true);
				setDisabled(gridParcela.getCellObject(5,"vlDefinido"),true);
				setDisabled(gridParcela.getCellObject(4,"pcSobreValor"),true);
				setDisabled(gridParcela.getCellObject(5,"pcSobreValor"),true);
				
				setElementValue(gridParcela.getCellObject(4,"vlDefinido"),"");
				setElementValue(gridParcela.getCellObject(5,"vlDefinido"),"");
				setElementValue(gridParcela.getCellObject(4,"pcSobreValor"),"");
				setElementValue(gridParcela.getCellObject(5,"pcSobreValor"),"");
				
				for( x=0 ; x<4 ; x++){
					var vlSugerido 			= getElementValue(gridParcela.getCellObject(x,"vlSugerido"));
					var vlMaximoAprovado 	= getElementValue(gridParcela.getCellObject(x,"vlMaximoAprovado"));
					var vlNegociado 		= getElementValue(gridParcela.getCellObject(x,"vlNegociado"));
					if(vlSugerido != "" || vlMaximoAprovado != "" || vlNegociado != ""){
						gridParcela.getCellObject(x,"vlDefinido").required = "true";
					}else{
						gridParcela.getCellObject(x,"vlDefinido").required = "false";
					}
				}
			}
		}
		
		//LMS-3581 - Mantém pcSobreValor da linha 0 sempre desabilitada.
		setDisabled(gridParcela.getCellObject(0,"pcSobreValor"),true);
	}

	function validateTab() {
		var flag = validateTabScript(document.forms);
		if (flag == true) 
			if (getElementValue("tpSituacaoAprovacao") == "S")
				alert(lms25002);
		return flag;
	}
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		gridParcela = document.getElementById("ParcelaTabelaCe.dataTable").gridDefinition;

		if (getElementValue("disableTabListagem") == "true") {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("pesq", true);
		}

		if (getElementValue("idProcessoWorkflow") != "") {
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
			xmit({serviceDataObjects:[sdo]});
			getTabGroup(this.document).setDisabledTab("cad",false);
			getTabGroup(this.document).selectTab(1);
			getTabGroup(this.document).setDisabledTab("pesq",true);
		}
	}
	function dataLoad_cb(data,exception) {
		onDataLoad_cb(data,exception);

		loadGrid(getElementValue("idTabelaColetaEntrega"));
		var behaivor = getElementValue("behaivor");
		setDisabled(document,true);
		setDisabled("storeButtom",false);
		if (behaivor == "1") {
			setDisabled("dtVigenciaFinal",false);
		}else if (behaivor == "2") {
			setDisabled("dtVigenciaInicial",false);
			setDisabled("dtVigenciaFinal",false);
		}else if (behaivor == "3") {
			setDisabled("dtVigenciaFinal",false);
		}else
			setDisabled("storeButtom",true);

		if (getElementValue("idProcessoWorkflow") == "")
			setDisabled("solicitacao",false);
		else
			setDisabled("solicitacao",true);

		setFocusOnFirstFocusableField();
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			gridParcela.resetGrid();
			setDisabled(document,true);
		}
	}
	function loadGrid(idTabelaColetaEntrega) {
		var data = new Array();
		setNestedBeanPropertyValue(data, "idTabelaColetaEntrega", idTabelaColetaEntrega);
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction.findGridEditabled",
			   "writeRowsGrid",data);
        xmit({serviceDataObjects:[sdo]}); 
	}
	function writeRowsGrid_cb(data) {
		gridParcela.resetGrid();
		var table = document.getElementById("ParcelaTabelaCe.dataTable");
	    if (table.tBodies.length > 0) {
            var tableBody = table.tBodies[0];
      	}else{
            var tableBody = document.createElement("tbody");
            table.appendChild(tableBody);
	    }
		var behaivor = getElementValue("behaivor");	    
		for (var x = 0; x < data.length; x++) {
			var tpParcela = getNestedBeanPropertyValue(data[x],"tpParcela.value");
			var tpParcelaDescritpion = getNestedBeanPropertyValue(data[x],"tpParcela.description");

			if (tpParcela == "EV")
				lineEV = x;
			else if (tpParcela == "FP")
				lineFP = x;
			
     	    gridParcela.createRow(new Array(), x , data[x], tableBody, undefined);
    		if (tpParcela != "PV" && tpParcela != "PF")
	    		setDisabled(gridParcela.getCellObject(x,"pcSobreValor"),true);
		}
		
		gridParcela.setDisabledColumn("pcSobreValor",true);
		gridParcela.setDisabledColumn("vlSugerido",true);
		gridParcela.setDisabledColumn("vlReferencia",true);
		gridParcela.setDisabledColumn("vlMaximoAprovado",true);
		gridParcela.setDisabledColumn("vlNegociado",true);
		gridParcela.setDisabledColumn("vlDefinido",true);

		//LMS-3581 - Mantém pcSobreValor da linha 0 sempre desabilitada.
		var linha_parcela = document.getElementById("ParcelaTabelaCe:0.pcSobreValor");
		linha_parcela.style.border = "none";
		linha_parcela.style.background="none";

		
		document.getElementById("storeButtom").tabIndex = 999;
		
		if (behaivor == "1" || behaivor == "2") {
			gridParcela.setDisabledColumn("vlDefinido",false);
			if (getElementValue(gridParcela.getCellObject(4,"vlDefinido")) != "" || getElementValue(gridParcela.getCellObject(5,"vlDefinido"))) {
				if (getElementValue(gridParcela.getCellObject(4,"vlDefinido")) != ""){
					validateGrid(4);					
				}else{
					validateGrid(5);					
				}
			}else{
				validateGrid(0);
			}
		}else
			gridParcela.setDisabledColumn("pcSobreValor",true);
	}
	function store() {
		storeEditGridScript('lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction.store', 'storeCustom', document.forms[0], document.forms[1]);
	}
	function storeCustom_cb(data,exception) {
		store_cb(data,exception);
		if (getNestedBeanPropertyValue(data,"ParcelaTabelaCe") != undefined) {
			var parcelas = getNestedBeanPropertyValue(data,"ParcelaTabelaCe");
			for (var xx = 0; xx < parcelas.length; xx++){
				for (var xy = 0; xy <= 5 ; xy++){
					if( getElementValue("ParcelaTabelaCe:" + (xy) + ".tpParcela.value") == parcelas[xx].tpParcela ){
	    				setElementValue("ParcelaTabelaCe:" + (xy) + ".id",getNestedBeanPropertyValue(parcelas[xx],"id"));
	    				xy = 6;
					}
				}
			}
	    }
	    
	    if (getElementValue("idProcessoWorkflow") == "")
			setDisabled("solicitacao",false);
		else
			setDisabled("solicitacao",true);
	    
	}
	function onRowClick() {
		return false;
	}
//-->
</script>
<adsm:window service="lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction" onPageLoadCallBack="pageLoad">
	<adsm:form id="Lazy" action="/freteCarreteiroColetaEntrega/manterTabelasFretesEventuais" idProperty="idTabelaColetaEntrega" onDataLoadCallBack="dataLoad">		
		<adsm:hidden property="behaivor" serializable="false"/>
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:hidden property="disableTabListagem" />

		<adsm:lookup label="filial" labelWidth="19%" dataType="text" size="3" maxLength="3" width="31%" action="municipios/manterFiliais"
				service="lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction.findLookupFilial" property="filial" idProperty="idFilial"
				picker="false" criteriaProperty="sgFilial" required="true" disabled="true"  cellStyle="vertical-align:bottom">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="filial.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmPessoa" size="32" disabled="true" serializable="false" cellStyle="vertical-align:bottom" /> 
		</adsm:lookup>
		
		<adsm:lookup picker="false" width="5%" maxLength="3" action="/contratacaoVeiculos/manterSolicitacoesContratacao"
				service="lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction.findLookupSolicitacaoContratacao" 
				dataType="text" property="solicitacaoContratacao" idProperty="idSolicitacaoContratacao" disabled="true"
				criteriaProperty="filial.sgFilial" label="solicitacaoContratacao" size="3" labelWidth="12%" cellStyle="vertical-align:bottom" >
		</adsm:lookup>
		
		<adsm:lookup width="33%" maxLength="10" picker="false" disabled="true"
				service="lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction.findLookupSolicitacaoContratacao" 
				dataType="integer" property="solicitacaoContratacao2" idProperty="idSolicitacaoContratacao" mask="0000000000"
				criteriaProperty="nrSolicitacaoContratacao" size="15" action="/contratacaoVeiculos/manterSolicitacoesContratacao"
				cellStyle="vertical-align:bottom">
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte" 
				service="lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction.findLookupMeioTransporteRodoviario" 
				picker="false" disabled="true" cellStyle="vertical-align:bottom" 
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota" 
				label="meioTransporte" labelWidth="19%" width="31%" size="8" maxLength="6">
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador"/>
			<adsm:textbox property="meioTransporteRodoviario.meioTransporte.nrIdentificador" size="20" dataType="text" disabled="true"/>
		</adsm:lookup>
		

		<adsm:lookup dataType="text" property="proprietario" idProperty="idProprietario" maxLength="20" serializable="false"
				service="lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction.findLookupProprietario" width="38%"
				action="/contratacaoVeiculos/manterProprietarios" criteriaProperty="pessoa.nrIdentificacaoFormatado" labelWidth="12%"
				label="proprietario" disabled="true" picker="false" size="19" >
			<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" serializable="false" cellStyle="vertical-align:bottom" size="26" disabled="true"/>
		</adsm:lookup>
        

		<adsm:multicheckbox property="blDomingo|blSegunda|blTerca|blQuarta|blQuinta|blSexta|blSabado|"
				labelWidth="19%" width="81%" label="frequenciaValidade" texts="dom|seg|ter|qua|qui|sex|sab" align="top"/>
 
		<adsm:textbox dataType="JTTime" labelWidth="19%" width="31%" cellStyle="vertical-align:bottom" 
				 label="horarioInicio" property="hrDiariaInicial"/>

		<adsm:textbox dataType="text" property="moedaPais" label="moeda" labelWidth="12%" width="38%" disabled="true"/>

		<adsm:range label="vigencia" labelWidth="19%" width="31%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		
		<adsm:combobox property="tpSituacaoAprovacao" domain="DM_SITUACAO_APROVACAO_TABELA_FRETE_EVENT" label="situacao"
						labelWidth="12%" width="38%" onlyActiveValues="true" boxWidth="160" disabled="true"/>
		<adsm:buttonBar>
			<adsm:button caption="salvar" onclick="store()" id="storeButtom"/>
			<adsm:button caption="solicitacaoContratacao" action="contratacaoVeiculos/manterSolicitacoesContratacao" cmd="main" id="solicitacao">
				<adsm:linkProperty src="solicitacaoContratacao2.idSolicitacaoContratacao" target="idProcessoWorkflow"/>
			</adsm:button>
		</adsm:buttonBar>
		
		<script type="text/javascript">
		<!--
			var lms25002 = "<adsm:label key="LMS-25002"/>";
		//-->
		</script>
		
	</adsm:form>
		<adsm:grid property="ParcelaTabelaCe" onRowClick="onRowClick" idProperty="idParcelaTabelaCe" onValidate="validateGrid"
				selectionMode="none" showRowIndex="false" gridHeight="170" autoSearch="false" title="parcelasFrete"
 	    		showGotoBox="false" showPagging="false" showTotalPageCount="false" scrollBars="horizontal" service="">
 	    	<adsm:gridColumn width="190" title="tipoParcela" property="tpParcela.description"  />
			<adsm:editColumn property="tpParcela.value"  title=""  field="hidden" width="0"/>
			<adsm:editColumn dataType="currency" property="vlSugerido" title="valorSugerido" field="textbox" align="right" width="120"/>
			<adsm:editColumn dataType="currency" property="vlReferencia" title="valorReferencia" field="textbox" align="right" width="120"/>
			<adsm:editColumn dataType="currency" property="vlMaximoAprovado" title="valorAutorizadoAte" field="textbox" align="right" width="150"/>
			<adsm:editColumn dataType="currency" property="vlNegociado" title="valorNegociado" field="textbox" align="right" width="120"/>
			<adsm:editColumn dataType="currency" property="vlDefinido" title="valorDefinido" field="textbox" align="right" width="120"/>
			<adsm:editColumn dataType="percent" property="pcSobreValor" title="percentualSobreValor" field="textbox" align="right" width="120"/>
	</adsm:grid>
</adsm:window>
