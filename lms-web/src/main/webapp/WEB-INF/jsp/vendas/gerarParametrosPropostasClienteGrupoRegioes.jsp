<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.gerarParametrosPropostaAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01040"/>
		<adsm:include key="LMS-01050"/>
		<adsm:include key="LMS-01051"/>
		<adsm:include key="LMS-01060"/>
		<adsm:include key="LMS-01070"/>
		<adsm:include key="LMS-01155"/>
		<adsm:include key="pagaPesoExcedente"/>
	</adsm:i18nLabels>

	<adsm:form action="/vendas/gerarParametrosPropostasCliente">
		<adsm:hidden property="idProposta"/>
		<adsm:hidden property="simulacao.idSimulacao"/>
		<adsm:hidden property="unidadeFederativaByIdUfOrigem.idUnidadeFederativa"/>
		<adsm:hidden property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio"/>
		<adsm:hidden property="disableAll"/>

		<adsm:hidden property="pcFretePercentual"/>
		<adsm:hidden property="vlMinimoFretePercentual"/>
		<adsm:hidden property="vlToneladaFretePercentual"/>
		<adsm:hidden property="psFretePercentual"/>
				
		<adsm:hidden property="tpIndicadorMinFretePeso"/>
		<adsm:hidden property="vlMinFretePeso"/>
		<adsm:hidden property="tpIndicadorFreteMinimo"/>
		<adsm:hidden property="vlFreteMinimo"/>
		<adsm:hidden property="tpIndicadorFretePeso"/>
		<adsm:hidden property="vlFretePeso"/>
		<adsm:hidden property="blPagaPesoExcedente"/>
		<adsm:hidden property="pcDiferencaFretePeso"/>
		<adsm:hidden property="tpIndicadorAdvalorem"/>
		<adsm:hidden property="tpDiferencaAdvalorem"/>
		<adsm:hidden property="vlAdvalorem"/>
		<adsm:hidden property="pcDiferencaAdvalorem"/>
		<adsm:hidden property="blFreteExpedido"/>
		<adsm:hidden property="blFreteRecebido"/>		

		<adsm:hidden property="isGenerate" value="false"/>
	</adsm:form>
	<adsm:grid
		property="destinoProposta"
		idProperty="idDestinoProposta"
		selectionMode="1"
		scrollBars="vertical"
		gridHeight="310"
		showGotoBox="false"
		showPagging="false"
		showTotalPageCount="false"
		autoSearch="false"
		service="lms.vendas.gerarParametrosPropostaAction.generatePercentualProposta"
		onDataLoadCallBack="onDataLoadCallBack"
		onPopulateRow="onPopulateRow"
		onValidate="validateField"
		onchange="onChangeField"
		onSelectRow="onSelectRow"
		onSelectAll="onSelectAll"
		onRowClick="onRowClick">
		
		<adsm:editColumn 
			property="siglaDescricao" 
			title="destino"
			field="textbox" 
			width="140" 
			disabled="true"/>

		<adsm:editColumn
			property="pcFretePercentual" 
			dataType="decimal"
			mask="##0.00"
			minValue="0"
			maxValue="100"
			title="pcFretePercentual" 
			field="textbox"
			width="120"
			maxLength="18"
			disabled="true"/>
			
		<adsm:editColumn
			property="vlMinimoFretePercentual" 
			dataType="decimal"
			mask="###,###,###,###,##0.00"
			minValue="0"
			title="vlMinimoFretePercentual" 
			field="textbox"
			width="100"
			maxLength="18"
			disabled="true"/>
			
		<adsm:editColumn
			property="vlToneladaFretePercentual" 
			dataType="decimal"
			mask="###,###,###,###,##0.00000"
			minValue="0"
			title="vlToneladaFretePercentual" 
			field="textbox"
			width="100"
			maxLength="18"
			disabled="true"/>
			
		<adsm:editColumn
			property="psFretePercentual" 
			dataType="decimal"
			mask="###,###,###,###,##0.00"
			minValue="0"
			title="psFretePercentual" 
			field="textbox"
			width="100"
			maxLength="18"
			disabled="true"/>

		<adsm:editColumn
			property="isSelected"
			field="hidden"
			dataType="text"
			title=""
			width="1"/>
		<adsm:editColumn
			property="idUnidadeFederativaDestino"
			field="hidden"
			dataType="text"
			title=""
			width="1"/>
		<adsm:editColumn
			property="idTipoLocalizacaoMunicipioDestino"
			field="hidden"
			dataType="text"
			title=""
			width="1"/>	
		<adsm:editColumn
			property="idGrupoRegiao"
			field="hidden"
			dataType="text"
			title=""
			width="1"/>				

		<adsm:buttonBar>
			<adsm:button caption="gerarDestinos" id="btnGerarDestinos" onclick="populateGrid(true);" disabled="true"/>
			<adsm:button caption="salvar" id="btnSalvar" onclick="storePercentualProposta();" disabled="true"/>
			<adsm:button caption="limpar" id="btnLimpar" onclick="populateGrid(false);" disabled="true"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript" src="../lib/parametroClienteProposta.js"></script>
<script language="javascript" type="text/javascript">

	function initWindow(eventObj) {
		if (eventObj.name == "continuar") {
			copyValuesFromCad();
			populateGrid(false);
		}
	}

	function populateGrid(isGenerate) {
		setElementValue("isGenerate", isGenerate);
		var formPrincipal = buildFormBeanFromForm(document.forms[0]);
	   	destinoPropostaGridDef.executeSearch(formPrincipal, true);
	}

	function onDataLoadCallBack_cb(data, error) {
		var rowCountChecked = 0;
		for (rowIndex=0; rowIndex < data.length; rowIndex++) {
			if(!hasValue(data[rowIndex].idDestinoProposta)) {
				populateRowDefaultValues(rowIndex);
				disableRows(rowIndex, true);
			} else {
				document.getElementById("destinoProposta:"+rowIndex+".idDestinoProposta").checked = true;
				disableRows(rowIndex, false);
				rowCountChecked++;
			}
	    }
		/* valida checkBox principal */
	    document.getElementById("destinoProposta.chkSelectAll").checked = (destinoPropostaGridDef.currentRowCount == rowCountChecked);
	    validateButtons();
	}

	function onPopulateRow(tr, data) {
		tr.style.backgroundColor = "#E7E7E7";
	}

	function storePercentualProposta() {
		/* Executa STORE quando existir alguma linha marcada, ou se ele está realizando um UPDATE,
		 * nesse ultimo, caso nao tenha nenhuma linha selecionada 
		 * ele irá EXCLUIR todas referencias vinculadas anteiormente a essa PROPOSTA.
		 */
		var mapIds = destinoPropostaGridDef.getSelectedIds();
	   	if (mapIds.ids.length > 0 || hasValue(getElementValue("idProposta"))) {
	   		
			var data = new Array();	
			merge(data, buildFormBeanFromForm(document.forms[0]));
			merge(data, buildFormBeanFromForm(document.forms[1]));
				
			var sdo = createServiceDataObject("lms.vendas.gerarParametrosPropostaAction.storePercentualProposta", "storePercentualProposta", data);
			xmit({serviceDataObjects:[sdo]});
			
		}
	}
	function storePercentualProposta_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return;
		}
		/* Informa o ID da Proposta na tela CAD */
		var frame = parent.document.frames["cad_iframe"];
		frame.setIdPropertyValue(data._value);
		setElementValue("idProposta", data._value);
		populateGrid(false);
	}

	/*function validateStore() {
		var dataTable = document.getElementById("destinoProposta.dataTable");
    	for (rowIndex=0; rowIndex < dataTable.rows.length; rowIndex++) {
    		if(getElementValueByGrid(rowIndex,"isSelected") == "true") {
    			if (!validateParametroClienteDestinoProposta(rowIndex)) {
					return false;
				}
    		}
    	}
		return true;
	}*/
	

	function validateField(rowIndex, columnName, objCell) {
		/*if(isIn(columnName, ["tpIndicadorFreteMinimo", "vlFreteMinimo"])) {
			return validateVlPercentualMinimo(rowIndex);
		}
		if(isIn(columnName, ["tpIndicadorFretePeso", "vlFretePeso"])) {
			return validateTpFretePeso(rowIndex);
		}
		if(isIn(columnName, ["vlAdvalorem"])) {
			return validateVlAdvalorem1(rowIndex);
		}*/
		return true;
	}

	function onChangeField(columnName, rowIndex) {
		/*if(isIn(columnName, ["tpIndicadorAdvalorem"])) {
			return validateTpAdvalorem1(rowIndex-1);
		}*/
		return true;
	}

	function populateRowDefaultValues(rowIndex) {
		/*setElementValue(destinoPropostaGridDef.getCellObject(rowIndex,"tpIndicadorFreteMinimo"),  getElementValue("tpIndicadorFreteMinimo"));

		var objFreteMinimo = destinoPropostaGridDef.getCellObject(rowIndex,"vlFreteMinimo");
	    setElementValue(objFreteMinimo, setFormat(objFreteMinimo, getElementValue("vlFreteMinimo")));
	    setElementValue(destinoPropostaGridDef.getCellObject(rowIndex,"tpIndicadorFretePeso"), getElementValue("tpIndicadorFretePeso"));

	    var objFretePeso = destinoPropostaGridDef.getCellObject(rowIndex,"vlFretePeso");
	    setElementValue(objFretePeso, setFormat(objFretePeso, getElementValue("vlFretePeso")));

		//var objDiferencaFretePeso = destinoPropostaGridDef.getCellObject(rowIndex,"pcDiferencaFretePeso");
	    //setElementValue(objDiferencaFretePeso, setFormat(objDiferencaFretePeso, getElementValue("pcDiferencaFretePeso")));
	    setElementValue(destinoPropostaGridDef.getCellObject(rowIndex,"tpIndicadorAdvalorem"), getElementValue("tpIndicadorAdvalorem"));

	    var objAdvalorem = destinoPropostaGridDef.getCellObject(rowIndex,"vlAdvalorem");
	    setElementValue(objAdvalorem, setFormat(objAdvalorem, getElementValue("vlAdvalorem")));

		var objDiferencaAdvalorem = destinoPropostaGridDef.getCellObject(rowIndex,"pcDiferencaAdvalorem");
	    setElementValue(objDiferencaAdvalorem, setFormat(objDiferencaAdvalorem, getElementValue("pcDiferencaAdvalorem")));*/
	}

	function disableRows(rowIndex, disabled) {
		
		setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"siglaDescricao"), true);//deve ficar sempre desabilitada 
	    setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"pcFretePercentual"), disabled);
	    setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"vlMinimoFretePercentual"), disabled);
	    setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"vlToneladaFretePercentual"), disabled);
	    setDisabled(destinoPropostaGridDef.getCellObject(rowIndex,"psFretePercentual"), disabled);
	    
		/** campo auxiliar */
	    setElementValue(destinoPropostaGridDef.getCellObject(rowIndex,"isSelected"), !disabled);
	    /** validações da linha 
	    if(!disabled) {
	    	verifyIndicadoresTabela(rowIndex);
	    }
	    */
	}

	function onSelectRow(rowRef) {
		var rowIndex = destinoPropostaGridDef.navigate.currentRow;
		if(rowRef.firstChild.firstChild.checked) {
			disableRows(rowIndex, false);
		} else {
			populateRowDefaultValues(rowIndex);
			disableRows(rowIndex, true);
		}
		validateButtons();
	}

	function onSelectAll(checkAll) {
		for (rowIndex=0; rowIndex < destinoPropostaGridDef.currentRowCount; rowIndex++) {
			if(!checkAll) {
				populateRowDefaultValues(rowIndex);
			}
			disableRows(rowIndex, !checkAll);
	    }
	    validateButtons(checkAll);
	}

	function onRowClick() {
		return false;
	}

	function validateButtons(checkAll) {
		setDisabled("btnGerarDestinos", true);
		setDisabled("btnSalvar", true);
		setDisabled("btnLimpar", true);
		/* Verifica se pode desabilitar os campos */
		if(!validateDisabledFields()) {
			return;
		}
		var ids = destinoPropostaGridDef.getSelectedIds();
	   	if ((ids.ids.length > 0 && checkAll == undefined) || checkAll) {
	   		setDisabled("btnSalvar", false);
	   		if(!hasValue(getElementValue("idProposta"))) {
	   			setDisabled("btnLimpar", false);
	   		}
	   	}
	   	if(hasValue(getElementValue("idProposta"))) {
			setDisabled("btnGerarDestinos", false);
			setDisabled("btnSalvar", false);
		}
	}

	function validateDisabledFields() {
		if(getElementValue("disableAll") == "true") {
			setDisabled(document, true);
			document.getElementById("destinoProposta.chkSelectAll").disabled = true;
			return false;
		}
		return true;
	}

	/* Funções utilizadas pela parametroClienteProposta.js */
	function getElementByGrid(rowIndex, property) {
		return destinoPropostaGridDef.getCellObject(rowIndex, property);
	}
	function getElementValueByGrid(rowIndex, property) {
		return getElementValue(getElementByGrid(rowIndex,property));
	}

	function copyValuesFromCad() {
		var frame = parent.document.frames["cad_iframe"];
		var data = frame.getValuesFromCad();
		copyValues(data);
	}

	function copyValuesAndSetMasterLink(data, property) {
		var obj = document.getElementById(property);
		var value = getNestedBeanPropertyValue(data, property);
		setElementValue(obj, value);
		obj.masterLink = "true";
	}

	function copyValues(data) {
		
		copyValuesAndSetMasterLink(data, "idProposta");
		copyValuesAndSetMasterLink(data, "simulacao.idSimulacao");
		copyValuesAndSetMasterLink(data, "unidadeFederativaByIdUfOrigem.idUnidadeFederativa");
		copyValuesAndSetMasterLink(data, "tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio");
		copyValuesAndSetMasterLink(data, "disableAll");

		copyValuesAndSetMasterLink(data, "pcFretePercentual");
		copyValuesAndSetMasterLink(data, "vlMinimoFretePercentual");
		copyValuesAndSetMasterLink(data, "vlToneladaFretePercentual");
		copyValuesAndSetMasterLink(data, "psFretePercentual");
		copyValuesAndSetMasterLink(data, "blFreteExpedido");
		copyValuesAndSetMasterLink(data, "blFreteRecebido");

		copyValuesAndSetMasterLink(data, "tpIndicadorMinFretePeso");
		copyValuesAndSetMasterLink(data, "vlMinFretePeso");
		copyValuesAndSetMasterLink(data, "tpIndicadorFreteMinimo");
		copyValuesAndSetMasterLink(data, "vlFreteMinimo");
		copyValuesAndSetMasterLink(data, "tpIndicadorFretePeso");
		copyValuesAndSetMasterLink(data, "vlFretePeso");
		copyValuesAndSetMasterLink(data, "blPagaPesoExcedente");
		copyValuesAndSetMasterLink(data, "pcDiferencaFretePeso");
		copyValuesAndSetMasterLink(data, "tpIndicadorAdvalorem");
		copyValuesAndSetMasterLink(data, "vlAdvalorem");
		copyValuesAndSetMasterLink(data, "pcDiferencaAdvalorem");
		copyValuesAndSetMasterLink(data, "tpDiferencaAdvalorem");
		copyValuesAndSetMasterLink(data, "blFreteExpedido");
		copyValuesAndSetMasterLink(data, "blFreteRecebido");		

	}
</script>