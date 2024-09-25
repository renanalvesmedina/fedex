<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction" >
	<adsm:i18nLabels>
		<adsm:include key="LMS-26100"/>
	</adsm:i18nLabels>
	<adsm:grid
		property="FluxoContratacao"
		onRowClick="onRowClick"
		idProperty="idFluxoContratacao"
		selectionMode="none"
		showRowIndex="false"
		autoAddNew="false"
		gridHeight="120"
		onValidate="validField"
		service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findGridFluxoContratacao"
		showPagging="false"
		showGotoBox="false"
		showTotalPageCount="false"
		onDataLoadCallBack="FluxoContratacao_load"
	>
			<adsm:gridColumn property="dsFluxoFilial" title="fluxos" width="15%"/>
			<adsm:editColumn dataType="currency" property="pcValorFrete" title="percentualFrete" field="textbox" align="right" width="18%"/>
			<adsm:editColumn dataType="currency" property="valorFrete" title="valorFrete" field="textbox" align="right" width="18%" disabled="true"/>
			<adsm:editColumn dataType="text" property="tpAbrangenciaDescription" title="abrangencia" field="textbox" align="left" width="18%" disabled="true"/>
			<adsm:gridColumn property="nrChaveLiberacao" title="numeroChaveLiberacao" align="right" width="31%"/>
			<adsm:editColumn property="dsFluxoFilialEdit" title="" field="hidden" width="0%"/>
			<adsm:editColumn property="filialOrigem.idFilial" title="" field="hidden" width="0%"/>
			<adsm:editColumn property="filialDestino.idFilial" title="" field="hidden" width="0%"/>
			<adsm:editColumn property="nrChaveLiberacaoEdit" title="" field="hidden" width="0%"/>
			<adsm:editColumn property="tpAbrangencia" title="" field="hidden" width="0%"/>
			<adsm:buttonBar>
				<adsm:button caption="salvarTudo" id="salvaTudo" onclick="saveAll();" disabled="true"/>
			</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
<!--


	function validField(rowIndex, columnName, objCell) {
		if (columnName == "pcValorFrete") {
			var zero = parseFloat(0);
			var cem = parseFloat(100);
			var valor = parseFloat(prepareStringToNumber(objCell.value));
			if (valor < zero) {
				alert(getMessage(erForaLimites, null));
				setFocus(objCell);
				return false;
			}
			if (valor > cem) {
				alert(getMessage(erForaLimites, null));
				setFocus(objCell);
				return false;
			}
			var soma = parseFloat(0);
			var parcial;
			for (var i = 0; i < FluxoContratacaoGridDef.getRowCount(); i++) {
				parcial = prepareStringToNumber(FluxoContratacaoGridDef.getCellObject(i,"pcValorFrete").value);
				soma += parseFloat(parcial);
			}
			//Arredonda por que o javascript pode deixar "sujeira"
			soma = parseInt(soma * 100);
			if (soma > parseInt(10000)) {
				alert(i18NLabel.getLabel("LMS-26100"));
				setFocus(objCell);
				return false;
			}
			/*var tabGroup = getTabGroup(this.document);
			var tabDet = tabGroup.getTab("cad");
			tabDet.getDocument().getElementById("reconsultarFluxoContratacao").value = "-1";
			onShowTemp();*/
			loadValorFrete();
		}
		return true;
	}

	function onRowClick() {
		return false;
	}

	function loadValorFrete() {
		for (var i = 0; i < FluxoContratacaoGridDef.getRowCount(); i++) {
			var id = "FluxoContratacao:"+i+".valorFrete";
			document.getElementById(id).value = "";
		}
		var dataToSubmit = getDataToSubmit();
		var sdo = createServiceDataObject(FluxoContratacaoGridDef.service, "loadValorFrete",dataToSubmit);
		xmit({serviceDataObjects:[sdo]}); 
	}

	function loadValorFrete_cb(data, exception) {
		if (exception != undefined) {
			alert(exception);
			return;
		}
		for (var i = 0; i < data.length; i++) {
			var id = "FluxoContratacao:"+i+".valorFrete";
			setElementValue(id, 
				setFormat(document.getElementById(id),
					getNestedBeanPropertyValue(data[i],"valorFrete")));
		}
	}

	function onShowTemp() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");

		if (tabDet.getFormProperty("reconsultarFluxoContratacao") == "0") {
			behavior();
			return false;
		}
		loadGrid();
	}

	function getDataToSubmit() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		var idSolicitacaoContratacao = tabDet.getFormProperty("idSolicitacaoContratacao");
		var tpFluxoContratacao = tabDet.getFormProperty("tpFluxoContratacao");
		var rotas = getElementValue(tabDet.getDocument().getElementById("rotas"));
		var idRotaIdaVolta = getElementValue(tabDet.getDocument().getElementById("rotaIdaVolta.idRotaIdaVolta"));
		var vlFreteSugerido = getElementValue(tabDet.getDocument().getElementById("vlFreteSugerido"));
		var vlFreteMaximoAutorizado = getElementValue(tabDet.getDocument().getElementById("vlFreteMaximoAutorizado"));
		var tpAbrangencia = getElementValue(tabDet.getDocument().getElementById("tpAbrangencia"));

		var dataToSubmit;
		if (idRotaIdaVolta != undefined && idRotaIdaVolta != "") {
			dataToSubmit = {idSolicitacaoContratacao:idSolicitacaoContratacao,tpFluxoContratacao:tpFluxoContratacao,idRotaIdaVolta:idRotaIdaVolta,vlFreteSugerido:vlFreteSugerido,vlFreteMaximoAutorizado:vlFreteMaximoAutorizado,tpAbrangencia:tpAbrangencia};
		} else {
			dataToSubmit = {idSolicitacaoContratacao:idSolicitacaoContratacao,tpFluxoContratacao:tpFluxoContratacao,rotas:rotas,vlFreteSugerido:vlFreteSugerido,vlFreteMaximoAutorizado:vlFreteMaximoAutorizado,tpAbrangencia:tpAbrangencia};
		}
		var gridFormBean = buildFormBeanFromForm(document.forms[0]);
		for (var gridProperty in gridFormBean) 
			dataToSubmit[gridProperty] = gridFormBean[gridProperty];
		return dataToSubmit;
	}
	
	function behavior() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");

		if (tabDet.getDocument().getElementById("idSolicitacaoContratacao").value != "" && tabDet.getDocument().getElementById("vlFreteMaximoAutorizado").required != "true") {
			FluxoContratacaoGridDef.setDisabledColumn("pcValorFrete",true);
		}
		FluxoContratacaoGridDef.setDisabledColumn("valorFrete",true);
		setDisabled("salvaTudo", tabGroup.getTab("cad").getElementById("salvar").disabled == true);
		FluxoContratacaoGridDef.setDisabledColumn("tpAbrangenciaDescription", true);
	}

	function loadGrid() {
		var dataToSubmit = getDataToSubmit();
		FluxoContratacaoGridDef.executeSearch(dataToSubmit, true);
	}

	function initWindow(eventObj) {
		var tabGroup = getTabGroup(this.document);
		if (eventObj.name == "tab_click") {
			var changedCad = tabGroup.getTab("cad").changed;
			if (changedCad) {
				tabGroup.getTab("fluxoContratacao").changed = true;
			}
			setFocusOnFirstFocusableField();
			onShowTemp();
		}
	}

	function FluxoContratacao_load_cb(data, exception) {
		if (exception != undefined) {
			alert(exception);
			return false;
		}

		// quest  27230
		if(data.length == 0){
			FluxoContratacaoGridDef.resetGrid();
			FluxoContratacaoGridDef._refreshPageControls();
			FluxoContratacaoGridDef.refreshRowsBackground();
		}
		
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		tabDet.getDocument().getElementById("reconsultarFluxoContratacao").value = "0";
		behavior();

		setFocusOnFirstFocusableField();
	}

	function saveAll() {
		getTabGroup(this.document).selectTab(1);
		getTabGroup(this.document).getTab('cad').getDocument().parentWindow.store();
	}
//-->
</script>
