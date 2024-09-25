<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script type="text/javascript">
	function carregaPagina() {
		setMasterLink(document, true);
		var u = new URL(parent.location.href);
		setElementValue("origem", u.parameters["origem"]);
		if (getElementValue("origem") == "programacaoColetasVeiculos" || getElementValue("origem") == "executarRetornarColetasEntregas") {
			setElementValue("filial.idFilial", u.parameters["filial.idFilial"]);
			lookupChange({e:document.getElementById("filial.idFilial"), forceChange:true});
			loadControleCarga(u.parameters["meioTransporte2.idMeioTransporte"], u.parameters["filial.idFilial"]);
		}
		onPageLoad();
	}

	function validateTab() {
		var flag = validateTabScript(document.forms[0]);
		if (flag && getElementValue("tpSituacaoMeioTransporte") != "EMCE") {
			alert(i18NLabel.getLabel("LMS-09035"));
			return false;
		}
		return flag;
	}

	//FUNÇÔES RELACIONADAS AO DOCTO SERVICO
	function changeTpDoctoServico(field) {
		resetValue('idDoctoServico');
		var flag = changeDocumentWidgetType(
			{
				documentTypeElement:field, 
				filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
				documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
				parentQualifier:'doctoServico',
				documentGroup:'DOCTOSERVICE',
				actionService:'lms.entrega.registrarBaixaEntregasOnTimeAction'
			}
		);
		document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial").service = "lms.entrega.registrarBaixaEntregasOnTimeAction.findLookupFilial";
		var pms = document.getElementById("doctoServico.idDoctoServico").propertyMappings;
		pms[pms.length] = {modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico"};
		pms[pms.length] = {modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia", relatedProperty:"doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", blankFill:false};

		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.idFilial", criteriaProperty:"filial.idFilial", inlineQuery:true};
		if (getElementValue("doctoServico.tpDocumentoServico") == "RRE") {
			pms[pms.length] = {modelProperty:"filialDestino.sgFilial", criteriaProperty:"filial.sgFilial"};
			pms[pms.length] = {modelProperty:"filialDestino.pessoa.nmFantasia", criteriaProperty:"filial.pessoa.nmFantasia"};
			pms[pms.length] = {modelProperty:"filialDestino.idFilial", criteriaProperty:"filial.idFilial"};	
		} else {
			pms[pms.length] = {modelProperty:"filialByIdFilialDestino.sgFilial", criteriaProperty:"filial.sgFilial"};
			pms[pms.length] = {modelProperty:"filialByIdFilialDestino.pessoa.nmFantasia", criteriaProperty:"filial.pessoa.nmFantasia"};
		}

		addElementChangeListener({e:document.getElementById("filial.idFilial"), changeListener:document.getElementById("doctoServico.idDoctoServico")});

		return flag;
	}

	function changeFilialDoctoServico() {
		resetValue('idDoctoServico');
		return changeDocumentWidgetFilial(
			{
				filialElement:document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial"), 
				documentNumberElement:document.getElementById("doctoServico.idDoctoServico")
			}
		);
	}

	//ADICIONAR CONTROLE CARGA APOS BUSCAR MEIO DE TRANSPORTE
	function callBackMeioTransp2_cb(data) {
		meioTransporte2_nrFrota_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			loadControleCarga(getNestedBeanPropertyValue(data[0],"idMeioTransporte"),getElementValue("filial.idFilial"));
	}

	function callBackMeioTransp_cb(data) {
		meioTransporte_nrIdentificador_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			loadControleCarga(getNestedBeanPropertyValue(data[0],"idMeioTransporte"),getElementValue("filial.idFilial"));
	}

	function callPopUpSetMeioTransporte(data) {
		loadControleCarga(getNestedBeanPropertyValue(data,"idMeioTransporte"),getElementValue("filial.idFilial"));
		return true;
	}

	function changeCriteriaMeioTransporte2() {
		var flag = meioTransporte2_nrFrotaOnChangeHandler();
		if (getElementValue("meioTransporte.idMeioTransporte") == "") {
			resetValue("tpSituacaoMeioTransporte");
			resetValue("controleCarga.sgFilial");
			resetValue("controleCarga.nrControle");
		}
		return flag;
	}

	function changeCriteriaMeioTransporte() {
		var flag = meioTransporte_nrIdentificadorOnChangeHandler();
		if (getElementValue("meioTransporte.idMeioTransporte") == "") {
			resetValue("tpSituacaoMeioTransporte");
			resetValue("controleCarga.sgFilial");
			resetValue("controleCarga.nrControle");
		}
		return flag;
	}

	//FILIAL
	function callBackFilial_cb(data) {
		filial_sgFilial_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			loadControleCarga(getElementValue("meioTransporte.idMeioTransporte"),getNestedBeanPropertyValue(data[0],"idFilial"));
	}

	function callPopUpSetFilial(data) {
		loadControleCarga(getElementValue("meioTransporte.idMeioTransporte"),getNestedBeanPropertyValue(data,"idFilial"));
		return true;
	}
	function changeCriteriaMeioTransporte() {
		var flag = meioTransporte_nrIdentificadorOnChangeHandler();
		if (getElementValue("meioTransporte.idMeioTransporte") == "") {
			resetValue("tpSituacaoMeioTransporte");
			resetValue("controleCarga.sgFilial");
			resetValue("controleCarga.nrControle");
		}
		return flag;
	}

	function changeCriteriaFilial() {
		var flag = filial_sgFilialOnChangeHandler();
		if (getElementValue("filial.idFilial") == "") {
			resetValue("tpSituacaoMeioTransporte");
			resetValue("controleCarga.sgFilial");
			resetValue("controleCarga.nrControle");
		}
		return flag;
	}

	function loadControleCarga(idMeioTransporte,idFilial) {
		var sdo = createServiceDataObject("lms.entrega.registrarBaixaEntregasOnTimeAction.findControleCarga","onDataLoadSamples",{idMeioTransporte:idMeioTransporte,idFilial:idFilial});
		xmit({serviceDataObjects:[sdo]});
	}

	function onDataLoadSamples_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		var formElem = document.getElementById(mainForm);
		fillFormWithFormBeanData(formElem.tabIndex, data);
	}

	//SETANDO A FILIAL COM A FILIAL DO USUARIO LOGADO
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.entrega.registrarBaixaEntregasOnTimeAction.findDataSession","dataSession",null);
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
		tpOrdemDoc = getNestedBeanPropertyValue(data,"filial.tpOrdemDoc");
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
			setElementValue("filial.tpOrdemDoc",tpOrdemDoc);
		}
	}

	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			writeDataSession();
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial",true);
			setDisabled("doctoServico.idDoctoServico",true);
		}
		setFocus("meioTransporte2.nrFrota");
	}

	function baixasEfetuadas() {
		if (getElementValue("meioTransporte.idMeioTransporte") == "") {
			alert(i18NLabel.getLabel("LMS-09038"));
			return;
		}
		if (getElementValue("tpSituacaoMeioTransporte") != "EMCE") {
			alert(i18NLabel.getLabel("LMS-09035"));
			return false;
		}
		showModalDialog("/entrega/registrarBaixaEntregasOnTime.do?cmd=baixas",window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:320px;');
	}

	function dataLoadVoid_cb(data,error) {
		if (error)
			alert(error);
	}

	function rowClick(id) {
		var data = ManifestoEntregaDocumentoGridDef.findById(id);
		if (getNestedBeanPropertyValue(data,"docto_tp.value") == "MAV") {
			if (confirm(i18NLabel.getLabel("LMS-09045"))) {
				var sdo = createServiceDataObject("lms.entrega.registrarBaixaEntregasOnTimeAction.createEventoManifesto","dataLoadVoid",{sgFilial:getNestedBeanPropertyValue(data,"manif_sgF"),nrManifesto:getNestedBeanPropertyValue(data,"manif_nr")});
				xmit({serviceDataObjects:[sdo]});
			}
		} else {
			var URL = "/entrega/registrarBaixaEntregasOnTime.do?cmd=det" +
						"&manifestoEntrega.filial.idFilial=" + getNestedBeanPropertyValue(data,"manif_idF") +
						"&manifestoEntrega.filial.sgFilial=" + getNestedBeanPropertyValue(data,"manif_sgF") +
						"&manifestoEntrega.idManifestoEntrega=" + getNestedBeanPropertyValue(data,"manif_id") +
						"&manifestoEntrega.nrManifestoEntrega=" + getNestedBeanPropertyValue(data,"manif_nr") +
						"&doctoServico.tpDocumentoServico=" + getNestedBeanPropertyValue(data,"docto_tp.value") +
						"&doctoServico.filialByIdFilialOrigem.idFilial=" + getNestedBeanPropertyValue(data,"docto_fi_id") +
						"&doctoServico.filialByIdFilialOrigem.sgFilial=" + getNestedBeanPropertyValue(data,"docto_fi") +
						"&controleCarga.filialByIdFilialOrigem.sgFilial=" + getNestedBeanPropertyValue(data,"cc_sg") +
						"&controleCarga.nrControleCarga=" + getNestedBeanPropertyValue(data,"cc_nr") +
						"&doctoServico.idDoctoServico=" + getNestedBeanPropertyValue(data,"docto_id") +
						"&doctoServico.nrDoctoServico=" + getNestedBeanPropertyValue(data,"docto_nr");
			showModalDialog(URL,window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:270px;');
		}
		return false;
	}

</script>
<adsm:window service="lms.entrega.registrarBaixaEntregasOnTimeAction" onPageLoad="carregaPagina" onPageLoadCallBack="pageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-09035"/>
		<adsm:include key="LMS-09045"/>
		<adsm:include key="LMS-09039"/>
		<adsm:include key="LMS-09038"/>
	</adsm:i18nLabels>

	<adsm:form action="/entrega/registrarBaixaEntregasOnTime">		
		<adsm:hidden property="origem"/>
<%--
		<adsm:hidden property="manif_idF"/>
		<adsm:hidden property="manif_sgF"/>
		<adsm:hidden property="manif_id"/>
		<adsm:hidden property="manif_nr"/>
		<adsm:hidden property="docto_tp"/>
		<adsm:hidden property="docto_fi_id"/>
		<adsm:hidden property="docto_fi"/>
		<adsm:hidden property="cc_sg"/>
		<adsm:hidden property="cc_nr"/>
		<adsm:hidden property="docto_id"/>
		<adsm:hidden property="docto_nr"/>
		<adsm:hidden property="ordem"/>
--%>
		<adsm:hidden property="filial.empresa.tpEmpresa" value="M" serializable="false"/>

		<adsm:lookup
			label="filial"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.entrega.registrarBaixaEntregasOnTimeAction.findLookupFilial"
			action="/municipios/manterFiliais"
			onchange="return changeCriteriaFilial();"
			onDataLoadCallBack="callBackFilial"
			onPopupSetValue="callPopUpSetFilial"
			labelWidth="19%"
			dataType="text"
			size="3"
			maxLength="3"
			width="41%"
			required="true" disabled="true"
		>
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="filial.empresa.tpEmpresa"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="35" disabled="true"/> 
		</adsm:lookup>
		<adsm:hidden property="actionBaixas" value="lms.entrega.registrarBaixaEntregasOnTimeAction"/>


		<adsm:textbox
			label="controleCargas"
			property="controleCarga.sgFilial"
			dataType="text"
			size="3"
			serializable="false"
			disabled="true"
			labelWidth="19%"
			width="21%"
		>
			<adsm:textbox property="controleCarga.nrControle" dataType="text" size="9" serializable="false" disabled="true" />
		</adsm:textbox>

		<adsm:lookup
			label="meioTransporte"
			property="meioTransporte2"
			idProperty="idMeioTransporte"
			criteriaProperty="nrFrota"
			service="lms.entrega.registrarBaixaEntregasOnTimeAction.findLookupMeioTransp"
			action="/contratacaoVeiculos/manterMeiosTransporte"
			onDataLoadCallBack="callBackMeioTransp2" 
			onchange="return changeCriteriaMeioTransporte2()"
			dataType="text"
			labelWidth="19%"
			serializable="false"
			required="true"
			maxLength="6"
			picker="false"
			cmd="list"
			width="41%"
			size="7"
		>
			<adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador"/>
			<adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte"/>		
			<adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador"/>
			<adsm:lookup
				property="meioTransporte"
				idProperty="idMeioTransporte"
				criteriaProperty="nrIdentificador"
				service="lms.entrega.registrarBaixaEntregasOnTimeAction.findLookupMeioTransp"
				action="/contratacaoVeiculos/manterMeiosTransporte"
				onchange="return changeCriteriaMeioTransporte()"
				onDataLoadCallBack="callBackMeioTransp"
				onPopupSetValue="callPopUpSetMeioTransporte"
				dataType="text"
				maxLength="25"
				cmd="list"
				size="20"
			>
				<adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota" modelProperty="nrFrota" disable="false"/>
				<adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte" modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporte2.nrFrota" modelProperty="nrFrota" />
			</adsm:lookup>
		</adsm:lookup>

		<adsm:combobox property="filial.tpOrdemDoc" domain="DM_TP_ORDEM_DOC"
				label="ordemDocumentos" labelWidth="19%" width="21%" renderOptions="true"/>

		<adsm:combobox
			label="documentoServico"
			property="doctoServico.tpDocumentoServico"
			optionLabelProperty="description"
			service="lms.entrega.registrarBaixaEntregasOnTimeAction.findTpDoctoServico"
			onchange="return changeTpDoctoServico(this);"
			labelWidth="19%"
			optionProperty="value"
			width="41%"
		>
			<adsm:lookup
				property="doctoServico.filialByIdFilialOrigem"
				idProperty="idFilial"
				criteriaProperty="sgFilial" 
				service=""
				action=""
				onchange="return changeFilialDoctoServico();"
				disabled="true"
				dataType="text"
				size="3"
				maxLength="3"
				picker="false" 
				popupLabel="pesquisarFilial"
			>
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
			/>
			<adsm:hidden property="idDoctoServico"/>
		</adsm:combobox>
		
		<adsm:textbox label="numeroPreAWB" property="idAwb" dataType="integer" width="15%" labelWidth="19%" maxLength="15"/>

		<adsm:hidden property="tpSituacaoMeioTransporte" serializable="false"/>
		<adsm:hidden property="idControleCarga"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ManifestoEntregaDocumento"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		idProperty="idManifestoEntregaDocumento"
		property="ManifestoEntregaDocumento"
		onRowClick="rowClick"
		selectionMode="none"
		scrollBars="horizontal"
		rows="10"
		gridHeight="200"
	>

	
		<adsm:gridColumn title="documentoServico" property="docto_tp" width="55" isDomain="true"/>
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="" property="docto_fi" width="50" />
			<adsm:gridColumn title="" property="docto_nr" dataType="integer" mask="00000000" width="100"/>
		</adsm:gridColumnGroup>		
		<%--<adsm:gridColumn width="60" title="identificacao" property="dest_tpIdent" isDomain="true" align="left"/>
		<adsm:gridColumn width="120" title="" property="dest_nrIdent" align="right"/>--%>
		<adsm:gridColumn width="150" title="destinatario" property="dest_nome" align="left"/>
		<adsm:gridColumn width="90" title="dpe" property="dpe" align="center"/>
		<adsm:gridColumn width="300" title="endereco" property="dsEndereco" align="left"/>
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="manifestoEntrega" property="manif_sgF" width="80"/>
			<adsm:gridColumn title="" dataType="integer" mask="00000000" property="manif_nr" width="70" />
		</adsm:gridColumnGroup>		
		<adsm:gridColumn width="200" title="ciaAerea" property="cia_aerea" align="left"/>
		<adsm:gridColumn width="90" title="preAwb" property="id_awb" align="left"/>
		<%--<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="controleCarga" property="cc_sg" width="80"/>
			<adsm:gridColumn title="" dataType="integer" mask="00000000" property="cc_nr" width="70"/>
		</adsm:gridColumnGroup>--%>		
	</adsm:grid>

	<adsm:buttonBar>
		<adsm:button caption="baixasEfetuadas" disabled="false" onclick="baixasEfetuadas()"/>
	</adsm:buttonBar>

</adsm:window>
