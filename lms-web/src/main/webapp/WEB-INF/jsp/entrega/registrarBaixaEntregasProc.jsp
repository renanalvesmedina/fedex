<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.entrega.registrarBaixaEntregasAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/entrega/registrarBaixaEntregas">
		<adsm:hidden property="filial.empresa.tpEmpresa" serializable="false" value="M"/>

		<adsm:hidden property="manif_idF"/>
		<adsm:hidden property="manif_sgF"/>
		<adsm:hidden property="manif_id"/>
		<adsm:hidden property="manif_nr"/>
		<adsm:hidden property="tpManifesto"/>
		<adsm:hidden property="docto_tp"/>
		<adsm:hidden property="docto_fi_id"/>
		<adsm:hidden property="docto_fi"/>
		<adsm:hidden property="cc_sg"/>
		<adsm:hidden property="cc_nr"/>
		<adsm:hidden property="docto_id"/>
		<adsm:hidden property="docto_nr"/>
		<adsm:hidden property="ordem"/>

		<adsm:lookup
			label="filial"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			action="/municipios/manterFiliais"
			service="lms.entrega.registrarBaixaEntregasAction.findLookupFilial" 
			dataType="text"
			size="5"
			maxLength="3"
			width="77%"
			labelWidth="16%"
			required="true"
			onchange="return changeCriteriaFilial();"
			onDataLoadCallBack="callBackFilial"
			onPopupSetValue="callPopUpSetFilial"
		>
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="filial.empresa.tpEmpresa"/>
			<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" modelProperty="idFilial"/>
			<adsm:propertyMapping relatedProperty="manifesto.filial.sgFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping relatedProperty="manifesto.filial.idFilial" modelProperty="idFilial"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/> 
		</adsm:lookup>
		<adsm:hidden property="actionBaixas" value="lms.entrega.registrarBaixaEntregasAction"/>

		<adsm:combobox
			label="documentoServico"
			property="doctoServico.tpDocumentoServico"
			optionLabelProperty="description"
			service="lms.entrega.registrarBaixaEntregasAction.findTpDoctoServico"
			onchange="return changeTpDoctoServico(this);"
			labelWidth="16%"
			optionProperty="value"
			width="34%"
		>
			<adsm:lookup
				property="doctoServico.filialByIdFilialOrigem"
				idProperty="idFilial"
				criteriaProperty="sgFilial"
				service=""
				action=""
				disabled="true"
				dataType="text"
				size="3"
				maxLength="3"
				picker="false"
				popupLabel="pesquisarFilial"
				onchange="return changeFilialDoctoServico();"
				
			>
				<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="idFilial"/>
				<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
			</adsm:lookup>

			<adsm:lookup
				property="doctoServico"
				idProperty="idDoctoServico"
				criteriaProperty="nrDoctoServico" 
				service=""
				action=""
				dataType="integer"
				size="12"
				maxLength="8"
				mask="00000000"
				serializable="true"
				disabled="true"
				popupLabel="pesquisarDocumentoServico"
				onDataLoadCallBack="dataLoadDocto"
				onPopupSetValue="popUpSetDocto"
			/>

			<adsm:hidden property="idDoctoServico"/>
		</adsm:combobox>
		
		<adsm:textbox label="numeroPreAWB" property="idAwb" dataType="integer" width="34%" labelWidth="16%" maxLength="15"/>

		<adsm:lookup
			label="manifestoEntrega"
			property="manifesto.filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			action="/municipios/manterFiliais"
			service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findLookupFilial"
			disabled="true"
			dataType="text"
			picker="false"
			size="3"
			maxLength="3"
			labelWidth="16%"
			width="34%"
			exactMatch="true"
			serializable="false"
		>
			<adsm:lookup
				property="manifestoEntrega"
				idProperty="idManifestoEntrega"
				criteriaProperty="nrManifestoEntrega"
				service="lms.contasreceber.emitirReciboFreteAction.findManifestosEntregaByFilial"
				action="entrega/consultarManifestosEntrega"
				dataType="integer"
				size="20"
				maxLength="16"
				mask="00000000"
				cmd="lookup"
				popupLabel="pesquisarManifestoEntrega"
				onDataLoadCallBack="dataLoadManifesto"
				onPopupSetValue="popUpSetManifesto"
				serializable="true"
			>
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filial.sgFilial" blankFill="false" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filial.pessoa.nmFantasia" blankFill="false" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filial.idFilial" blankFill="false"/>
			</adsm:lookup>
		</adsm:lookup>

		<%--LOOKUP MANIFESTO DE VIAGEM--%>
		<adsm:hidden property="manifesto.tpManifestoViagem" value="ED"/>
		<adsm:combobox
			label="manifestoViagem"
			labelWidth="16%"
			width="34%"
			serializable="true"
			property="manifesto.tpManifesto"
			disabled="true"
			service="lms.sim.consultarLocalizacoesMercadoriasAction.findTipoManifesto"
			optionProperty="value"
			optionLabelProperty="description"
		>
			<adsm:lookup
				dataType="text"
				property="manifesto.filialByIdFilialOrigem" popupLabel="pesquisarFilial"
				idProperty="idFilial"
				criteriaProperty="sgFilial"
				service=""
				action=""
				size="3"
				maxLength="3"
				picker="false"
				serializable="false"
				onchange="onChangeFilialMVN();"
			/>

			<adsm:lookup
				property="manifesto.manifestoViagemNacional"
				idProperty="idManifestoViagemNacional"
				criteriaProperty="nrManifestoOrigem"
				service=""
				action=""
				dataType="integer"
				size="10"
				maxLength="8"
				mask="00000000"
				disabled="true"
				serializable="true"
				popupLabel="pesquisarManifestoViagem"
				onDataLoadCallBack="manifestoViagem"
				onPopupSetValue="manifestoViagemPopup"
		/>
		</adsm:combobox>

		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" />
		<adsm:lookup
			dataType="text"
			property="controleCarga.filialByIdFilialOrigem"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			labelWidth="16%"
			service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupFilial"
			action="/municipios/manterFiliais"
			disabled="true"
			width="34%"
			popupLabel="pesquisarFilial"
			label="controleCargas"
			size="3"
			maxLength="3"
			picker="false"
			serializable="false"
			criteriaSerializable="true"
		>
			<adsm:lookup
				dataType="integer"
				property="controleCarga"
				idProperty="idControleCarga"
				criteriaProperty="nrControleCarga"
				disabled="true"
				service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupControleCarga"
				action="/carregamento/manterControleCargas"
				cmd="list"
				popupLabel="pesquisarControleCarga"
				maxLength="8"
				size="8"
				mask="00000000"
				criteriaSerializable="true"
			>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="filial.sgFilial" blankFill="false" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" criteriaProperty="filial.pessoa.nmFantasia" blankFill="false" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="filial.idFilial" blankFill="false"/>
			</adsm:lookup>
		</adsm:lookup>		

		<adsm:combobox 
				property="filialByIdFilialOrigem.tpOrdemDoc" 
				domain="DM_TP_ORDEM_DOC"
				label="ordemDocumentos" 
				labelWidth="16%" 
				width="34%" 
				renderOptions="true"
		/>
				
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ManifestoEntregaDocumento" />
			<adsm:resetButton/>
		</adsm:buttonBar>
		<adsm:i18nLabels>
			<adsm:include key="LMS-09035"/>
			<adsm:include key="LMS-09045"/>
			<adsm:include key="LMS-09039"/>
			<adsm:include key="required2FieldsOr"/>
			<adsm:include key="LMS-09090"/>
		</adsm:i18nLabels> 
	</adsm:form>
	<adsm:grid
		idProperty="idManifestoEntregaDocumento"
		property="ManifestoEntregaDocumento"
		onRowClick="rowClick"
		selectionMode="radio"
		scrollBars="horizontal"
		rows="10"
		gridHeight="200"
		onPopulateRow="populateRow"
		onSelectRow="myOnSelectRow"
	>
		<adsm:gridColumn title="documentoServico" property="docto_tp" width="55" isDomain="true"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="" property="docto_fi" width="50" />
			<adsm:gridColumn title="" property="docto_nr" dataType="integer" mask="00000000" width="100"/>
		</adsm:gridColumnGroup>		
		<adsm:gridColumn width="60" title="identificacao" property="dest_tpIdent" isDomain="true" align="left"/>
		<adsm:gridColumn width="120" title="" property="dest_nrIdent" align="right"/>
		<adsm:gridColumn width="150" title="destinatario" property="dest_nome" align="left"/>
		<adsm:gridColumn width="90" title="dpe" property="dpe" align="center" />
		<adsm:gridColumn width="500" title="endereco" property="dsEndereco" align="left"/>
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="manifestoEntrega" property="manif_sgF" width="80"/>
			<adsm:gridColumn title="" dataType="integer" mask="00000000" property="manif_nr" width="70" />
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="manifestoViagem" property="manifestoViagemNacional.sgFilial" width="80"/>
			<adsm:gridColumn title="" dataType="integer" mask="00000000" property="manifestoViagemNacional.nrManifesto" width="70" />
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="controleCarga" property="cc_sg" width="80"/>
			<adsm:gridColumn title="" dataType="integer" mask="00000000" property="cc_nr" width="70"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn width="200" title="ciaAerea" property="cia_aerea" align="left"/>
		<adsm:gridColumn width="90" title="preAwb" property="id_awb" align="right"/>
	<adsm:gridColumn title="volumes" property="volumes" image="/images/popup.gif" openPopup="false" link="javascript:exibirVolumes" align="center" width="85" />
	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="coletasPendentes" disabled="false" onclick="listarEventosColetas()"/>		
		<adsm:button id="btnEmitirEntregasNaoRealizadas" caption="emitirEntregasNaoRealizadas" boxWidth="180"
					onclick="exibeEmitirEntregasNaoRealizadas();" disabled="false"/>
		<adsm:button caption="confirmar" id="botaoFechar" onclick="confirmar();" disabled="false" />
		<adsm:button caption="baixasEfetuadas" disabled="false" onclick="baixasEfetuadas()"/>
	</adsm:buttonBar>
</adsm:window>
<script type="text/javascript">
<!--
function confirmar(){
	 var URL = "/entrega/registrarBaixaEntregas.do?cmd=det" +
		"&manifestoEntrega.filial.idFilial=" + document.getElementById("manif_idF").value +
		"&manifestoEntrega.filial.sgFilial=" + document.getElementById("manif_sgF").value +
		"&manifestoEntrega.idManifestoEntrega=" + document.getElementById("manif_id").value +
		"&manifestoEntrega.nrManifestoEntrega=" + document.getElementById("manif_nr").value +	
		"&manifestoViagem.filial.idFilial=" + document.getElementById("manif_idF").value +
		"&manifestoViagem.filial.sgFilial=" + document.getElementById("manif_sgF").value +
		"&manifestoViagem.idManifestoViagem=" + document.getElementById("manif_id").value +
		"&manifestoViagem.nrManifestoViagem=" + document.getElementById("manif_nr").value +
		"&doctoServico.tpDocumentoServico=" + document.getElementById("docto_tp").value  +
		"&doctoServico.filialByIdFilialOrigem.idFilial=" + document.getElementById("docto_fi_id").value +
		"&doctoServico.filialByIdFilialOrigem.sgFilial=" + document.getElementById("docto_fi").value +
		"&controleCarga.filialByIdFilialOrigem.sgFilial=" + document.getElementById("cc_sg").value +
		"&controleCarga.nrControleCarga=" + document.getElementById("cc_nr").value +
		"&doctoServico.idDoctoServico=" + document.getElementById("docto_id").value +
		"&doctoServico.nrDoctoServico=" + document.getElementById("docto_nr").value + 
		"&tpManifesto=" + document.getElementById("tpManifesto").value + 
		"&ordem=" + document.getElementById("ordem").value;
	showModalDialog(URL,window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:270px;');
	ManifestoEntregaDocumentoGridDef.resetGrid();
}

	function myOnSelectRow(rowRef){
		setDisabled("botaoFechar", false);
		var data = ManifestoEntregaDocumentoGridDef.gridState.data[rowRef.rowIndex];
		setElementValue("manif_idF",getNestedBeanPropertyValue(data,"manif_idF"));
		setElementValue("manif_sgF",getNestedBeanPropertyValue(data,"manif_sgF"));
		setElementValue("manif_id",getNestedBeanPropertyValue(data,"manif_id"));
		setElementValue("manif_nr",getNestedBeanPropertyValue(data,"manif_nr"));
		setElementValue("tpManifesto",getNestedBeanPropertyValue(data,"tpManifesto"));
		setElementValue("docto_tp",getNestedBeanPropertyValue(data,"docto_tp.value"));
		setElementValue("docto_fi_id",getNestedBeanPropertyValue(data,"docto_fi_id"));
		setElementValue("docto_fi",getNestedBeanPropertyValue(data,"docto_fi"));
		setElementValue("cc_sg",getNestedBeanPropertyValue(data,"cc_sg"));
		setElementValue("cc_nr",getNestedBeanPropertyValue(data,"cc_nr"));
		setElementValue("docto_id",getNestedBeanPropertyValue(data,"docto_id"));
		setElementValue("docto_nr",getNestedBeanPropertyValue(data,"docto_nr"));
		setElementValue("ordem",getNestedBeanPropertyValue(data,"ordem"));
	}
	//FUNÇÔES RELACIONADAS AO DOCTO SERVICO
	
	function populateRow(tr,data) {
		setDisabled("botaoFechar", true);
        var ordem = getNestedBeanPropertyValue(data,"ordem");

        if(ordem < 0){
			tr.style.backgroundColor = '#ff6655';
        }
   
   }

	var volumeClick = false;
	
	function exibirVolumes(id){
		var data = ManifestoEntregaDocumentoGridDef.getDataRowById(id);
		volumeClick = true;
		setElementValue("idDoctoServico", getNestedBeanPropertyValue(data,"docto_id"));
		showModalDialog("/expedicao/consultarDocumentoServicoVolumes.do?cmd=main" , window, "unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;");
	}

	function changeTpDoctoServico(field) {
		resetValue('idDoctoServico');
		var flag = changeDocumentWidgetType({
			documentTypeElement:field, 
			filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
			documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
			parentQualifier:'doctoServico',
			documentGroup:'DOCTOSERVICE',
			actionService:'lms.entrega.registrarBaixaEntregasAction'
		});
		document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial").service = "lms.entrega.registrarBaixaEntregasAction.findLookupFilial";
		var pms = document.getElementById("doctoServico.idDoctoServico").propertyMappings;
		pms[pms.length] = {modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico"};
		
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.idFilial", criteriaProperty:"filial.idFilial", inlineQuery:true};
		if (getElementValue("doctoServico.tpDocumentoServico") == "RRE") {
			pms[pms.length] = {modelProperty:"filialDestino.sgFilial", criteriaProperty:"filial.sgFilial"};
			pms[pms.length] = {modelProperty:"filialDestino.pessoa.nmFantasia", criteriaProperty:"filial.pessoa.nmFantasia"};
			pms[pms.length] = {modelProperty:"filialDestino.idFilial", criteriaProperty:"filial.idFilial"};	
		} else {
			pms[pms.length] = {modelProperty:"filialByIdFilialDestino.sgFilial", criteriaProperty:"filial.sgFilial"};
			pms[pms.length] = {modelProperty:"filialByIdFilialDestino.pessoa.nmFantasia", criteriaProperty:"filial.pessoa.nmFantasia"};
		}
		if (getElementValue("doctoServico.tpDocumentoServico") != "MDA")
			pms[pms.length] = {modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia", criteriaProperty:"doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia"};
			
		pms[pms.length] = {modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia", relatedProperty:"doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia"};

		return flag;
	}

	function changeFilialDoctoServico() {
		resetValue('idDoctoServico');
		return changeDocumentWidgetFilial({
			filialElement:document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial"), 
			documentNumberElement:document.getElementById("doctoServico.idDoctoServico")
		});
	}

	//VALIDATE DADOS PARA CONSULTA!!
	function validateTab() {
		var flag = validateTabScript(document.forms);
		if (flag && getElementValue("doctoServico.idDoctoServico") == "" && 
				getElementValue("controleCarga.idControleCarga") == "" &&
				getElementValue("manifestoEntrega.idManifestoEntrega") == "" &&
				getElementValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional") == "") {
			alert(i18NLabel.getLabel("LMS-09090"));
			setFocus("doctoServico.tpDocumentoServico");
			return false;
		} 
		return flag;
	}
	
	//ADICIONAR CONTROLE CARGA APOS BUSCAR MEIO DE TRANSPORTE
	function loadControleCarga(id,isDoctoServico) {
		var sdo = createServiceDataObject("lms.entrega.registrarBaixaEntregasAction.findControleCarga","onDataLoadSamples",{id:id,isDocto:isDoctoServico});
		xmit({serviceDataObjects:[sdo]});
	}

	function onDataLoadSamples_cb(data,exception) {
		if (exception != undefined){
			alert(exception);
		}else{
			var formElem = document.getElementById(mainForm);
			fillFormWithFormBeanData(formElem.tabIndex, data);
		}
	}

	//SETANDO A FILIAL COM A FILIAL DO USUARIO LOGADO
	function pageLoad_cb(data) {
		setDisabled("botaoFechar", true);
		onPageLoad_cb(data);
		resetAndDisableManifestoViagem();
		var sdo = createServiceDataObject("lms.entrega.registrarBaixaEntregasAction.findDataSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}

	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	var tpOrdemDoc = null;
	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
		tpOrdemDoc = getNestedBeanPropertyValue(data,"filialByIdFilialOrigem.tpOrdemDoc");
		writeDataSession();
	}

	function writeDataSession() {
		if (idFilial != null &&
			sgFilial != null &&
			nmFilial != null &&
			tpOrdemDoc != null) {
			setElementValue("filial.idFilial",idFilial);
			setElementValue("filial.sgFilial",sgFilial);
			setElementValue("filial.pessoa.nmFantasia",nmFilial);
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial",idFilial);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial",sgFilial);
			setElementValue("manifesto.filial.idFilial",idFilial);
			setElementValue("manifesto.filial.sgFilial",sgFilial);
			setElementValue("filialByIdFilialOrigem.tpOrdemDoc",tpOrdemDoc);
			enabledChildren(false);
		}
	}

	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			writeDataSession();
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial",true);
			setDisabled("doctoServico.idDoctoServico",true);
			
			resetAndDisableManifestoViagem();
		}
	}

	function resetAndDisableManifestoViagem() {
		setElementValue("manifesto.tpManifesto","VN");
		changeDocumentWidgetType({
				documentTypeElement:document.getElementById("manifesto.tpManifesto"), 
				filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
				documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional'), 
				documentGroup:'MANIFESTO',
				actionService:'lms.sim.consultarLocalizacoesMercadoriasAction'
		});
		var pms = document.getElementById("manifesto.manifestoViagemNacional.idManifestoViagemNacional").propertyMappings;
		pms[pms.length] = {modelProperty:"manifesto.tpManifestoViagem", criteriaProperty:"manifesto.tpManifestoViagem", inlineQuery:true};
	}

	function baixasEfetuadas() {
		var controleCarga = getElement("controleCarga.idControleCarga");
		var manifestoViagemNacional = getElement("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
		if( (getElementValue(controleCarga) == "") && (getElementValue(manifestoViagemNacional) == "") ) {
			alertI18nMessage("required2FieldsOr", new Array(controleCarga.label, manifestoViagemNacional.label), false);
			return;
		}
		showModalDialog("/entrega/registrarBaixaEntregasOnTime.do?cmd=baixas",window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:320px;');
	}

	function dataLoadVoid_cb(data,error) {
		if (error)
			alert(error);
	}

	function rowClick(id) {
		if(!volumeClick){
		var data = ManifestoEntregaDocumentoGridDef.findById(id);

		if (getNestedBeanPropertyValue(data,"docto_tp.value") == "MAV") {
			if (confirm(i18NLabel.getLabel("LMS-09045"))) {
				var sdo = createServiceDataObject("lms.entrega.registrarBaixaEntregasAction.createEventoManifesto","dataLoadVoid",{sgFilial:getNestedBeanPropertyValue(data,"manif_sgF"),nrManifesto:getNestedBeanPropertyValue(data,"manif_nr")});
				xmit({serviceDataObjects:[sdo]});
			}
		} else {
				var tabGroup = getTabGroup(this.document);
				tabGroup._tabsIndex[0].setDisabled(true);
				tabGroup._tabsIndex[1].setDisabled(false);
				setElementValue("manif_idF",getNestedBeanPropertyValue(data,"manif_idF"));
				setElementValue("manif_sgF",getNestedBeanPropertyValue(data,"manif_sgF"));
				setElementValue("manif_id",getNestedBeanPropertyValue(data,"manif_id"));
				setElementValue("manif_nr",getNestedBeanPropertyValue(data,"manif_nr"));
				setElementValue("tpManifesto",getNestedBeanPropertyValue(data,"tpManifesto"));
				setElementValue("docto_tp",getNestedBeanPropertyValue(data,"docto_tp.value"));
				setElementValue("docto_fi_id",getNestedBeanPropertyValue(data,"docto_fi_id"));
				setElementValue("docto_fi",getNestedBeanPropertyValue(data,"docto_fi"));
				setElementValue("cc_sg",getNestedBeanPropertyValue(data,"cc_sg"));
				setElementValue("cc_nr",getNestedBeanPropertyValue(data,"cc_nr"));
				setElementValue("docto_id",getNestedBeanPropertyValue(data,"docto_id"));
				setElementValue("docto_nr",getNestedBeanPropertyValue(data,"docto_nr"));
				setElementValue("ordem",getNestedBeanPropertyValue(data,"ordem"));
				tabGroup.selectTab('vol', {name:'tab_click'});
		}
		return false;
	}
		volumeClick = false;
		return false;
	}

	//FILIAL
	function callBackFilial_cb(data) {
		filial_sgFilial_exactMatch_cb(data);
		if (data != undefined && data.length == 1) {
			enabledChildren(false);
		}
	}

	function callPopUpSetFilial(data) {
		enabledChildren(false);
		return true;
	}

	function changeCriteriaFilial() {
		var flag = filial_sgFilialOnChangeHandler();
		if (getElementValue("filial.sgFilial") == "")
			enabledChildren(true);	
		resetAndDisableManifestoViagem();
		return flag;
	}

	function enabledChildren(filialIsEmpty) {
		if (filialIsEmpty) {
			resetValue("controleCarga.filialByIdFilialOrigem.idFilial");
			setDisabled("controleCarga.idControleCarga", true);
			resetValue("manifesto.filial.idFilial");
			setDisabled("manifestoEntrega.idManifestoEntrega", true);
		} else {
			setDisabled("controleCarga.idControleCarga", false);
			setDisabled("manifestoEntrega.idManifestoEntrega", false);
		}
		resetValue("doctoServico.tpDocumentoServico");
		resetValue("doctoServico.idDoctoServico");
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		setDisabled("doctoServico.idDoctoServico", true);
	}

	function dataLoadDocto_cb(data) {
		doctoServico_nrDoctoServico_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			loadControleCarga(getNestedBeanPropertyValue(data[0], "idDoctoServico"), true);
	}

	function popUpSetDocto(data) {
		loadControleCarga(getNestedBeanPropertyValue(data, "idDoctoServico"), true);
		return true;
	}

	function dataLoadManifesto_cb(data) {
		manifestoEntrega_nrManifestoEntrega_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			loadControleCarga(getNestedBeanPropertyValue(data[0], "idManifestoEntrega"), false);
	}

	function popUpSetManifesto(data) {
		loadControleCarga(getNestedBeanPropertyValue(data, "idManifestoEntrega"), false);
		return true;
	}

	function manifestoViagem_cb(data) {
		var r = manifesto_manifestoViagemNacional_nrManifestoOrigem_exactMatch_cb(data);
		if (r == true) {
			var idManifesto = getElementValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
			validateManifestoViagem(idManifesto);
		}
	}

	function manifestoViagemPopup(data) {
		var idManifesto = getNestedBeanPropertyValue(data, "idManifestoViagemNacional");
		validateManifestoViagem(idManifesto);
	}

	function validateManifestoViagem(idManifestoViagem) {
		var sdo = createServiceDataObject(
			"lms.entrega.registrarBaixaEntregasAction.validateFilialManifestoViagem",
			"validateManifestoViagem",
			{idManifesto:idManifestoViagem, idFilial:getElementValue("filial.idFilial")}
		);
		xmit({serviceDataObjects:[sdo]});
	}

	function validateManifestoViagem_cb(data,error) {
		if (error != undefined) {
			resetValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
			alert(error);
			setFocus("manifesto.manifestoViagemNacional.nrManifestoOrigem");
		} else {
			setFocus("controleCarga.idControleCarga");
		}
	}

	function onChangeFilialMVN() {
		resetValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
		return changeDocumentWidgetFilial({
			filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'),
			documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional')
		});
	}
	
	function exibeEmitirEntregasNaoRealizadas() {	
		parent.parent.redirectPage("entrega/emitirEntregasNaoRealizadas.do?cmd=main");	
	}
	
	function listarEventosColetas() {
		var controleCarga = getElement("controleCarga.idControleCarga");
		var manifestoEntrega = getElement("manifestoEntrega.idManifestoEntrega");
		
		if( (getElementValue(controleCarga) == "") && (getElementValue(manifestoEntrega) == "") ) {
			alertI18nMessage("required2FieldsOr", new Array(controleCarga.label, manifestoEntrega.label), false);
			return;
		}
		
		showModalDialog("/entrega/registrarBaixaEntregas.do?cmd=eventoColetaPopup&idControleCarga="+getElementValue(controleCarga),window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:360px;');
	}
	//-->
</script>
