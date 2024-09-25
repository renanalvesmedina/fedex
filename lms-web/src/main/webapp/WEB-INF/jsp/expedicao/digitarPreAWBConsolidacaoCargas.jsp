<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.digitarPreAWBAction"
			 onPageLoad="myPageLoad" onPageLoadCallBack="pageLoad" >
	<adsm:form
		id="filter.form"
		action="/expedicao/digitarPreAWB"
		service="lms.expedicao.digitarPreAWBAction">
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-00055"/>
			<adsm:include key="LMS-04168"/>
			<adsm:include key="LMS-04159"/>
		</adsm:i18nLabels>
		
		<adsm:hidden property="tela" serializable="false"/>
		<adsm:hidden property="dados" serializable="false"/>
		
		<%----------------------%>
		<%-- AEROPORTO LOOKUP --%>
		<%----------------------%>
		<adsm:lookup
			action="/municipios/manterAeroportos"
			service="lms.expedicao.digitarPreAWBAction.findAeroporto"
			dataType="text"
			property="aeroporto"
			idProperty="idAeroporto"
			criteriaProperty="sgAeroporto"
			label="aeroportoDeDestino"
			size="3"
			maxLength="3"
			labelWidth="18%"
			width="32%"
			onDataLoadCallBack="aeroporto"
			afterPopupSetValue="clearByAeroporto();">

			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="aeroporto.pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				property="aeroporto.pessoa.nmPessoa" 
				serializable="false"
				size="26"
				maxLength="50"
				disabled="true"/>

		</adsm:lookup>

		<%-------------------%>
		<%-- FILIAL LOOKUP --%>
		<%-------------------%>
		<adsm:lookup
			action="/municipios/manterFiliais"
			service="lms.expedicao.digitarPreAWBAction.findLookupFilial"
			dataType="text"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			label="filialDeDestino"
			size="3"
			maxLength="3"
			labelWidth="15%"
			width="35%"
			onDataLoadCallBack="filial"
			afterPopupSetValue="clearByFilial();">

			<adsm:propertyMapping
				modelProperty="pessoa.nmFantasia"
				relatedProperty="filial.pessoa.nmFantasia"/>

			<adsm:textbox
				dataType="text"
				property="filial.pessoa.nmFantasia"
				serializable="false"
				size="26"
				maxLength="60"
				disabled="true"/>

		</adsm:lookup>

		<%----------------------%>
		<%-- MUNICIPIO LOOKUP --%>
		<%----------------------%>
		<adsm:lookup
			action="/municipios/manterMunicipios"
			service="lms.expedicao.digitarPreAWBAction.findLookupMunicipio"
			property="municipio"
			criteriaProperty="nmMunicipio"
			idProperty="idMunicipio"
			label="municipioDestino"
			dataType="text"
			maxLength="60"
			labelWidth="18%"
			width="32%"
			size="38"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"
			onDataLoadCallBack="municipio"
			afterPopupSetValue="clearByMunicipio();"/>

		<%----------------------%>
		<%-- MANIFESTO LOOKUP --%>
		<%----------------------%>
		<adsm:hidden property="manifestoViagem.filial.pessoa.nmFamtasia"/>
		<adsm:hidden property="manifestoLookup.idFilial"/>
		<adsm:lookup 
			dataType="text" 
			property="manifestoViagem.filial" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial"		
			service="lms.expedicao.digitarPreAWBAction.findLookupFilial"
			onchange="return resetFilialManifestoViagem();"
			action="/municipios/manterFiliais"
			label="numeroManifesto2"
			size="3"
			maxLength="3" 
			disabled="false"
			labelWidth="15%" 
			width="35%" 
			exactMatch="true" 
			popupLabel="pesquisarFilial"
			picker="false">			
			<adsm:propertyMapping relatedProperty="manifestoViagem.filial.pessoa.nmFamtasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="manifestoLookup.idFilial" modelProperty="idFilial"/>
			<adsm:lookup 
				dataType="integer" 
				action="/expedicao/manterManifestosViagem" 
				service="lms.expedicao.digitarPreAWBAction.findLookupManifestoViagem"
				exactMatch="false" 
				maxLength="8"
				property="manifestoViagem" 
				idProperty="idManifestoViagemNacional" 
				criteriaProperty="nrManifesto" 
				size="7" 
				disabled="false"
				onchange="return resetManifestoViagem();"
				onDataLoadCallBack="manifestoViagem"
				afterPopupSetValue="clearByManifestoViagem();"
				mask="00000000">				
				<adsm:propertyMapping relatedProperty="manifestoViagem.filial.sgFilial" modelProperty="filial.sgFilial" />
				<adsm:propertyMapping relatedProperty="manifestoViagem.filial.idFilial" modelProperty="filial.idFilial" />
				<adsm:propertyMapping criteriaProperty="manifestoViagem.filial.sgFilial" modelProperty="filialOrigem.sgFilial" />
				<adsm:propertyMapping criteriaProperty="manifestoLookup.idFilial" modelProperty="filialOrigem.idFilial" />						
				<adsm:propertyMapping criteriaProperty="manifestoViagem.filial.pessoa.nmFamtasia" modelProperty="filial.nmFantasiaOrigem" />					
			</adsm:lookup>  			
   		</adsm:lookup>   		

		<adsm:buttonBar freeLayout="true">
			<adsm:button
				id="gerarSugestao"
				caption="gerarSugestao"
				disabled="false"
				onclick="clickGerarSugestao();" />
			<adsm:resetButton id="limpar" />
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid
		idProperty="idConhecimento"
		property="ctrcList"
		service="lms.expedicao.digitarPreAWBAction.findCtrc"
		showPagging="false"
		gridHeight="120"
		autoSearch="false"
		unique="false"
		scrollBars="vertical"
		onRowClick="ctrcRowClick"
		onSelectRow="ctrcSelectRow"
		onSelectAll="ctrcSelectAll">

		<adsm:gridColumn
			title="documento"
			property="nrCtrc" />

		<adsm:gridColumn
			title="aeroportoDestino"
			property="aeroportoDestino.sgAeroporto"/>

		<adsm:gridColumn
			title="peso"
			property="doctoServico.psReal"
			align="right"
			unit="kg"
			dataType="weight"/>

		<adsm:gridColumn
			title="pesoCubado"
			property="doctoServico.psAforado"
			align="right"
			unit="kg"
			dataType="weight"/>

		<adsm:gridColumn
			title="qtdeVolumes"
			property="doctoServico.qtVolumes"
			align="right"
			dataType="integer"/>

		<adsm:gridColumn
			title="valorMerc"
			property="doctoServico.vlMercadoria"
			dataType="currency"
			align="right"
			unit="reais"/>

		<adsm:gridColumn
			title="dimensoes"
			property="dimensaoString"
			align="right"
			width="23%"
			unit="cmCmCm"/>

	</adsm:grid>

	<adsm:form 
		action="/expedicao/digitarPreAWB"
		service="lms.expedicao.digitarPreAWBAction">

		<adsm:hidden property="_qtCtrc" />
		<adsm:hidden property="_psRealTotal" />
		<adsm:hidden property="_vlMercadoriaTotal" />
		<adsm:hidden property="_qtVolumesTotal" />
		<adsm:hidden property="_psCubadoTotal" />

		<adsm:section caption="dadosCarga" />

		<%-----------------%>
		<%-- QTCTRC TEXT --%>
		<%-----------------%>
		<adsm:textbox 
			dataType="integer" 
			label="qtdDocumentos" 
			property="qtCtrc"
			disabled="true" 
			labelWidth="18%" 
			width="11%"
			maxLength="5" 
			size="6" />

		<adsm:textbox 
			dataType="currency"
			label="valorTotalDaMercadoriaRs" 
			property="vlMercadoriaTotal"
			labelWidth="21%" 
			width="21%" 
			disabled="true"
			maxLength="9"
			size="9" />

		<adsm:textbox 
			dataType="decimal" 
			mask="#,###,###,###,##0.000" 
			label="pesoRealTotal" 
			property="psRealTotal" 
			disabled="true" 
			unit="kg" 
			labelWidth="16%"
			width="13%" 
			maxLength="18"
			size="8" />

		<adsm:label 
			key="branco" 
			style="border:none;" 
			width="29%" />

		<adsm:textbox 
			dataType="integer" 
			label="qtdeTotalDeVolumes" 
			property="qtVolumesTotal" 
			disabled="true" 
			size="9" 
			labelWidth="21%" 
			maxLength="5"
			width="21%"/>

		<adsm:textbox 
			dataType="decimal" 
			mask="#,###,###,###,##0.000" 
			label="pesoCubadoTotal" 
			property="psCubadoTotal" 
			disabled="true" 
			unit="kg" 
			labelWidth="16%" 
			width="13%" 
			maxLength="18"
			size="8"/>

		<adsm:buttonBar freeLayout="false">
			<adsm:button id="consolidarCarga" 
				caption="consolidarCarga"
				disabled="false"
				onclick="clickConsolidarCarga();"/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
/**********************/
/* CALLBACK AEROPORTO */
/**********************/
function aeroporto_cb(data) {
	aeroporto_sgAeroporto_likeEndMatch_cb(data);
	clearByAeroporto();
}
function clearByAeroporto() {
	clearFilial();
	clearMunicipio();
	clearManifestoViagem();
}

/*******************/
/* CALLBACK FILIAL */
/*******************/
function filial_cb(data) {
	filial_sgFilial_likeEndMatch_cb(data);
	clearByFilial();
}
function clearByFilial() {
	clearAeroporto();
	clearMunicipio();
	clearManifestoViagem();
}

/**********************/
/* CALLBACK MUNICIPIO */
/**********************/
function municipio_cb(data) {
	municipio_nmMunicipio_exactMatch_cb(data);
	clearByMunicipio();
}
function clearByMunicipio() {
	clearAeroporto();
	clearFilial();
	clearManifestoViagem();
}

/**********************/
/* CALLBACK MANIFESTO */
/**********************/
function manifestoViagem_cb(data){
	manifestoViagem_nrManifesto_exactMatch_cb(data);
	clearByManifestoViagem();
}
function clearByManifestoViagem(){
	clearAeroporto();
	clearFilial();
	clearMunicipio();
}

/**************************/
/* ONCLICK GERAR SUGESTAO */
/**************************/
function clickGerarSugestao() {
	var idAeroporto = getElementValue("aeroporto.idAeroporto");
	var idFilial = getElementValue("filial.idFilial");
	var idMunicipio = getElementValue("municipio.idMunicipio");
	var idManifestoViagem = getElementValue("manifestoViagem.idManifestoViagemNacional");

	// indicam se os campos acima estao vazios
	// true = vazio, false = preenchido
	var blAeroporto = (idAeroporto == null || idAeroporto == "");
	var blFilial = (idFilial == null || idFilial == "");
	var blMunicipio = (idMunicipio == null || idMunicipio == "");
	var blManifestoViagem = (idManifestoViagem == null || idManifestoViagem == "");

	// indica se deve executar ou nao a pesquisa apos a verificacao dos
	// parametros
	var blExecute = true;

	// se os quatro estiverem vazios emite o alerta
	if(blAeroporto && blFilial && blMunicipio && blManifestoViagem) {
		alert(i18NLabel.getLabel('LMS-00055'));
		blExecute = false;
	}

	// se mais de um estiver preenchido emitir o alerta
	if(!blAeroporto) {
		if(!blFilial || !blMunicipio || !blManifestoViagem) {
			alert(i18NLabel.getLabel('LMS-04168'));
			blExecute = false;
		}
	}

	if(!blFilial) {
		if(!blAeroporto || !blMunicipio || !blManifestoViagem) {
			alert(i18NLabel.getLabel('LMS-04168'));
			blExecute = false;
		}
	}

	if(!blMunicipio) {
		if(!blFilial || !blAeroporto || !blManifestoViagem) {
			alert(i18NLabel.getLabel('LMS-04168'));
			blExecute = false;
		}
	}

	if(!blManifestoViagem) {
		if(!blFilial || !blAeroporto || !blMunicipio) {
			alert(i18NLabel.getLabel('LMS-04168'));
			blExecute = false;
		}
	}

	if(blExecute) {
		clearContadores();
		setElementValue("qtCtrc", "");
		findButtonScript('ctrcList', getElement("filter.form"));
	}
}

/**************************/
/* ONCLICK GERAR SUGESTAO */
/**************************/
function clickConsolidarCarga() {
	var rowCount = ctrcListGridDef.getRowCount();
	var execute = false;

	for(i = 0;i<rowCount;i++) {
		var check = document.getElementById("ctrcList:"+i+".idConhecimento");
		if(check.checked) {
			execute = true;
			break;
		}
	}

	if(!execute) {
		alert(i18NLabel.getLabel("LMS-04159"));
	} else {
		var dados = new Array();
		var gridData = ctrcListGridDef.getSelectedIds();
		for(i=0;i<gridData.ids.length;i++) {
			var lineData = ctrcListGridDef.findById(gridData.ids[i]);
			dados.push(lineData.idConhecimento);
		}
		var data = {
			conhecimentos : dados,
			qtCtrc : getElementValue("_qtCtrc"),
			psRealTotal : getElementValue("_psRealTotal"),
			vlMercadoriaTotal : getElementValue("_vlMercadoriaTotal"),
			qtVolumesTotal : getElementValue("_qtVolumesTotal"),
			psCubadoTotal : getElementValue("_psCubadoTotal")
		}
		var service = "lms.expedicao.digitarPreAWBCTRCsAction.consolidarCarga";
		var sdo = createServiceDataObject(service, "consolidarCarga", data);
		xmit({serviceDataObjects:[sdo]});
	}
}

function consolidarCarga_cb(data, error) {
	if(error) {
		alert(error);
		return false;
	} else {
		var tabGroup = getTabGroup(this.document);
		
		var dados = {
			qtCtrc : getElementValue("_qtCtrc"),
			psRealTotal : getElementValue("_psRealTotal"),
			vlMercadoriaTotal : getElementValue("_vlMercadoriaTotal"),
			qtVolumesTotal : getElementValue("_qtVolumesTotal"),
			psCubadoTotal : getElementValue("_psCubadoTotal"),
			aeroporto : {
				idAeroporto : getElementValue("aeroporto.idAeroporto"),
				sgAeroporto : getElementValue("aeroporto.sgAeroporto"),
				pessoa : {
					nmPessoa : getElementValue("aeroporto.pessoa.nmPessoa")
				}
			}
		}
		var tabGeracao = tabGroup.getTab("geracaoManutencao");
		cleanButtonScript(tabGeracao.getDocument());
		
		var frame = parent.document.frames["geracaoManutencao_iframe"];
		frame.saveDataFromConsolidacaoCargas(dados);
		tabGroup.setDisabledTab("consolidacaoCargas", true);
		tabGroup.selectNextTab();
	}
}

/***************/
/* INIT WINDOW */
/***************/
function initWindow(event) {
	setDisabled("gerarSugestao", false);
	setDisabled("consolidarCarga", false);
}

/************************/
/* ONROWCLICK CTRC GRID */
/************************/
function ctrcRowClick(valor) {
	return false;
}

/*************************/
/* ONSELECTROW CTRC GRID */
/*************************/
function ctrcSelectRow(rowRef) {
	// check = ctrcList:0.idConhecimento
	var check = document.getElementById("ctrcList:"+rowRef.rowIndex+".idConhecimento");
	handleRowClick(rowRef.rowIndex, check.checked);
}

/*************************/
/* ONSELECTALL CTRC GRID */
/*************************/
function ctrcSelectAll(estado) {
	clearContadores();
	if(estado) {
		var rowCount = ctrcListGridDef.getRowCount();
		for(i = 0;i<rowCount;i++) {
			handleRowClick(i, estado);
		}
	}
}

/*********************************/
/* ONHIDE TAB CONSOLIDACAOCARGAS */
/*********************************/
function hide() {
	cleanButtonScript();
	clearContadores();
	ctrcListGridDef.resetGrid();
	tab_onHide();
	return true;
}

/**********************/
/* FUNCOES AUXILIARES */
/**********************/
function clearAeroporto() {
	setElementValue("aeroporto.idAeroporto", "");
	setElementValue("aeroporto.sgAeroporto", "");
	setElementValue("aeroporto.pessoa.nmPessoa", "");
}

function clearFilial() {
	setElementValue("filial.idFilial", "");
	setElementValue("filial.sgFilial", "");
	setElementValue("filial.pessoa.nmFantasia", "");
}

function clearMunicipio() {
	setElementValue("municipio.idMunicipio", "");
	setElementValue("municipio.nmMunicipio", "");
}

function clearManifestoViagem(){
	setElementValue("manifestoViagem.filial.pessoa.nmFamtasia", "");
	setElementValue("manifestoLookup.idFilial", "");
	setElementValue("manifestoViagem.filial.idFilial","");
	setElementValue("manifestoViagem.filial.sgFilial","");
	setElementValue("manifestoViagem.nrManifesto","");
	setElementValue("manifestoViagem.idManifestoViagemNacional","");
}

function clearContadores() {
	setElementValue("_qtCtrc", "");
	setElementValue("_psRealTotal", "");
	setElementValue("_vlMercadoriaTotal", "");
	setElementValue("_qtVolumesTotal", "");
	setElementValue("_psCubadoTotal", "");
	setElementValue("qtCtrc", "");
	setElementValue("psRealTotal", "");
	setElementValue("vlMercadoriaTotal", "");
	setElementValue("qtVolumesTotal", "");
	setElementValue("psCubadoTotal", "");
}

/**
 * Realiza as somas ou subtracoes dependendo do estado do checkbox da linha
 * representada pelo rowIndex.
 */
function handleRowClick(rowIndex, checked) {
	// recupera as informacoes da linha
	var data = ctrcListGridDef.gridState.data[rowIndex];
	var qtCtrc = getNumberVar("_qtCtrc", "int");
	var psRealTotal = getNumberVar("_psRealTotal", "float");
	var vlMercadoriaTotal = getNumberVar("_vlMercadoriaTotal", "float");
	var qtVolumesTotal = getNumberVar("_qtVolumesTotal", "int");
	var psCubadoTotal = getNumberVar("_psCubadoTotal", "float");
	var psReal = getNumberValue(data.doctoServico.psReal, "float");
	var vlMercadoria = getNumberValue(data.doctoServico.vlMercadoria, "float");
	var qtVolumes = getNumberValue(data.doctoServico.qtVolumes, "int");
	var psAforado = getNumberValue(data.doctoServico.psAforado, "float");

	Math.round((1000.01 - 1000) * Math.pow(10, 3)) / Math.pow(10, 3)

	if(checked) {
		// soma dados
		qtCtrc += 1;
		psRealTotal = Math.round((psRealTotal + psReal) * Math.pow(10, 3)) / Math.pow(10, 3);
		vlMercadoriaTotal = Math.round((vlMercadoriaTotal + vlMercadoria) * Math.pow(10, 2)) / Math.pow(10, 2);
		qtVolumesTotal += qtVolumes;
		psCubadoTotal = Math.round((psCubadoTotal + psAforado) * Math.pow(10, 3)) / Math.pow(10, 3);
	} else {
		// subtrai dados
		qtCtrc -= 1;
		psRealTotal = Math.round((psRealTotal - psReal) * Math.pow(10, 3)) / Math.pow(10, 3);
		vlMercadoriaTotal = Math.round((vlMercadoriaTotal - vlMercadoria) * Math.pow(10, 2)) / Math.pow(10, 2);
		qtVolumesTotal -= qtVolumes;
		psCubadoTotal = Math.round((psCubadoTotal - psAforado) * Math.pow(10, 3)) / Math.pow(10, 3);
	}

	setNumberVar("_qtCtrc", qtCtrc, false);
	setNumberVar("_psRealTotal", psRealTotal, false);
	setNumberVar("_vlMercadoriaTotal", vlMercadoriaTotal, false);
	setNumberVar("_qtVolumesTotal", qtVolumesTotal, false);
	setNumberVar("_psCubadoTotal", psCubadoTotal, false);
	setNumberVar("qtCtrc", qtCtrc, true);
	setNumberVar("psRealTotal", psRealTotal, true);
	setNumberVar("vlMercadoriaTotal", vlMercadoriaTotal, true);
	setNumberVar("qtVolumesTotal", qtVolumesTotal, true);
	setNumberVar("psCubadoTotal", psCubadoTotal, true);
}

function getNumberVar(element, type) {
	var result = getElementValue(element);
	if(result == null || result == "") {
		result = 0;
	} else {
		if(type == "int") {
			result = parseInt(result);
		} else if(type == "float") {
			result = parseFloat(result);
		}
	}
	return result;
}

function setNumberVar(element, value, formato) {
	if(value == 0) {
		value = "";
	}
	if(formato == true) {
		setElementValue(element, setFormat(element, ""+value));
	} else {
		setElementValue(element, value);
	}
}

function getNumberValue(number, type) {
	var result = 0.0;
	if(!isNaN(number)) {
		if(type == "int") {
			result = parseInt(number);
		} else if(type == "float") {
			result = parseFloat(number);
		}
	}
	return result;
}

function resetFilialManifestoViagem() {
	if (getElementValue("manifestoViagem.filial.sgFilial") == "" ) {
		setElementValue("manifestoViagem.filial.pessoa.nmFamtasia", "");
		setElementValue("manifestoLookup.idFilial", "");
		setElementValue("manifestoViagem.filial.idFilial","");
		setElementValue("manifestoViagem.nrManifesto","");
		setElementValue("manifestoViagem.idManifestoViagemNacional","");
	}
	return manifestoViagem_filial_sgFilialOnChangeHandler();
}

function resetManifestoViagem() {
	if (getElementValue("manifestoViagem.nrManifesto") == "" ) {
		if (getElementValue("manifestoViagem.filial.sgFilial") == "" ) {
			setElementValue("manifestoViagem.filial.pessoa.nmFamtasia", "");
			setElementValue("manifestoViagem.filial.idFilial","");
			setElementValue("manifestoLookup.idFilial", "");
			setElementValue("manifestoViagem.filial.sgFilial","");
		}
		setElementValue("manifestoViagem.idManifestoViagemNacional","");		
	}
	return manifestoViagem_nrManifestoOnChangeHandler();
}

	function myPageLoad() {
		onPageLoad();
	}

	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		
		var url = new URL(parent.location.href);	
	   	setElementValue("tela", url.parameters["tela"]);
	   	setElementValue("dados", url.parameters["dados"]);
	   	
		if (getElementValue("tela") != "") {
			getTabGroup(this.document).setDisabledTab("consolidacaoCargas", false);
			getTabGroup(this.document).selectTab(1, null, true);
			
			setDisabled("gerarSugestao", true);		
			consultarConsolidacaoCarga(getElementValue("dados"));
		}
	}
	
	function consultarConsolidacaoCarga(conhecimentos){
		var dados = new Array();
		var data = {conhecimentos : conhecimentos};
		var service = "lms.expedicao.digitarPreAWBAction.findCtrc";
		var sdo = createServiceDataObject(service, "retornoConsulta", data);	
		xmit({serviceDataObjects:[sdo]});		
	}
	
	function retornoConsulta_cb(data){
		ctrcListGridDef.resetGrid();
		ctrcListGridDef.populateGrid(data);
		
		var checkAll = getElement('ctrcList.chkSelectAll');
		checkAll.checked = true;
		ADSM.selectAll(ctrcListGridDef, checkAll);
		setDisabled('limpar', true);
		setFocus('consolidarCarga');
	}

-->
</script>
