<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.portaria.manterRegistrosAuditoriasCargasAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/portaria/manterRegistrosAuditoriasCargas" height="380" idProperty="idRegistroAuditoria" onDataLoadCallBack="dataLoad">

		<adsm:i18nLabels>
			<adsm:include key="LMS-00065"/>
			<adsm:include key="requiredField"/>
		</adsm:i18nLabels>

		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>

		<adsm:hidden property="usuario.idUsuario" serializable="true"/>

		<adsm:lookup service="lms.portaria.manterRegistrosAuditoriasCargasAction.findLookupFilial" dataType="text"
			property="filial" criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
			width="87%" labelWidth="23%" action="/municipios/manterFiliais" idProperty="idFilial" 
			onchange="return filialOnChange(this)" exactMatch="true" required="true"
			onDataLoadCallBack="filialDataLoad"
			onPopupSetValue="filialPopup" disabled="true"
		>
			<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
			<adsm:hidden property="controleCarga.filialByIdFilialOrigem.empresa.tpEmpresa" value="M" serializable="false"/>
			<adsm:hidden property="filial.siglaNomeFilial" serializable="false"/>
		</adsm:lookup>

		<adsm:textbox dataType="integer" mask="00000000" property="nrRegistroAuditoria" label="numeroRegistro" size="12" maxLength="8" labelWidth="23%" width="30%" required="false" cellStyle="vertical-Align:bottom;" disabled="true" />
		<adsm:textbox dataType="JTDateTimeZone" picker="false" property="dhRegistroAuditoria" label="dataHoraRegistro" size="12" labelWidth="20%" width="27%" required="false" disabled="true" />

		<adsm:section caption="controleCarga"/>

		<adsm:lookup
			dataType="text"
			property="controleCarga.filialByIdFilialOrigem"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.portaria.manterRegistrosAuditoriasCargasAction.findLookupFilial"
			action="/municipios/manterFiliais"
			label="controleCargas"
			labelWidth="23%"
			width="30%"
			size="3"
			maxLength="3"
			picker="false"
			popupLabel="pesquisarFilial"
			exactMatch="true"
			onchange="return sgFilialOnChangeHandler(this)"
			onDataLoadCallBack="filialCCDataLoad"
			onPopupSetValue="filialCCPopup"
			serializable="false"
		>
			<adsm:lookup
				property="controleCarga"
				idProperty="idControleCarga"
				criteriaProperty="nrControleCarga"
				service="lms.portaria.manterRegistrosAuditoriasCargasAction.findLookupControleCarga"
				action="/carregamento/manterControleCargas"
				cmd="list"
				mask="00000000"
				onPopupSetValue="loadDataByNrControleCargaPopup"
				onDataLoadCallBack="loadDataByNrControleCarga"
				onchange="return checkValueControleCarga(this.value)"
				maxLength="8"
				size="8"
				disabled="true"
				dataType="integer"
				required="true"
				popupLabel="pesquisarControleCarga"
			>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" inlineQuery="false"/>

				<adsm:propertyMapping criteriaProperty="tpStatusControleCarga" modelProperty="tpStatusControleCarga"/>
				<adsm:hidden property="tpStatusControleCarga" value="EA" serializable="false"/>

				<adsm:propertyMapping relatedProperty="controleCarga.eventoControleCarga.dhEvento" modelProperty="eventoControleCarga.dhEvento"/>
				<adsm:propertyMapping relatedProperty="idEquipeOperacao" modelProperty="idEquipeOperacao"/>
				<adsm:propertyMapping relatedProperty="versao" modelProperty="versao"/>

				<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialDestino.idFilial" modelProperty="filialByIdFilialDestino.idFilial"/>
				<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialDestino.sgFilial" modelProperty="filialByIdFilialDestino.sgFilial"/>

				<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialDestino.siglaNomeFilial" modelProperty="filialByIdFilialDestino.siglaNomeFilial"/>
				<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>
				<adsm:propertyMapping relatedProperty="controleCarga.meioTransporteByIdTransportado.nrFrota" modelProperty="meioTransporteByIdTransportado.nrFrota"/>
				<adsm:propertyMapping relatedProperty="controleCarga.meioTransporteByIdTransportado.nrIdentificador" modelProperty="meioTransporteByIdTransportado.nrIdentificador"/>
				<adsm:propertyMapping relatedProperty="controleCarga.meioTransporteByIdTransportado.idMeioTransporte" modelProperty="meioTransporteByIdTransportado.idMeioTransporte"/>
				<adsm:propertyMapping relatedProperty="controleCarga.meioTransporteByIdSemiRebocado.nrFrota" modelProperty="meioTransporteByIdSemiRebocado.nrFrota"/>
				<adsm:propertyMapping relatedProperty="controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador" modelProperty="meioTransporteByIdSemiRebocado.nrIdentificador"/>
				<adsm:propertyMapping relatedProperty="controleCarga.meioTransporteByIdSemiRebocado.idMeioTransporte" modelProperty="meioTransporteByIdSemiRebocado.idMeioTransporte"/>

				<adsm:propertyMapping relatedProperty="controleCarga.motorista.pessoa.nrIdentificacaoFormatado" modelProperty="motorista.pessoa.nrIdentificacaoFormatado"/>
				<adsm:propertyMapping relatedProperty="controleCarga.motorista.pessoa.nmPessoa" modelProperty="motorista.pessoa.nmPessoa"/>
				<adsm:propertyMapping relatedProperty="controleCarga.motorista.idMotorista" modelProperty="motorista.idMotorista"/>

				<adsm:propertyMapping relatedProperty="tpControleCarga" modelProperty="tpControleCarga.description"/>
				<adsm:propertyMapping relatedProperty="tpControleCargaValue" modelProperty="tpControleCarga.value"/>
			</adsm:lookup>

			<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:hidden property="controleCarga.filialByIdFilialOrigem.empresa.tpEmpresa" value="M" serializable="false"/>
		</adsm:lookup>

		<adsm:hidden property="tpControleCargaValue" serializable="false"/>
		<adsm:textbox dataType="text" property="tpControleCarga" label="tipo" disabled="true" width="27%" required="false" serializable="false" labelWidth="20%"/>

		<adsm:textbox dataType="JTDateTimeZone" property="controleCarga.eventoControleCarga.dhEvento" label="dataEmissao" labelWidth="23%" width="30%" disabled="true" required="false" serializable="false" picker="false"/>

		<adsm:hidden property="controleCarga.filialByIdFilialDestino.idFilial" serializable="true"/>
		<adsm:hidden property="controleCarga.filialByIdFilialDestino.siglaNomeFilial" serializable="true"/>
		<adsm:textbox property="controleCarga.filialByIdFilialDestino.sgFilial" label="filialDestino" size="3" dataType="text" disabled="true" required="false" labelWidth="20%" width="27%" serializable="false">
			<adsm:textbox property="controleCarga.filialByIdFilialDestino.pessoa.nmFantasia" size="25" dataType="text" disabled="true" required="false" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox property="controleCarga.meioTransporteByIdTransportado.nrFrota" label="meioTransporte" labelWidth="23%" width="30%" size="9" maxLength="20" dataType="text" disabled="true" serializable="true">
			<adsm:textbox property="controleCarga.meioTransporteByIdTransportado.nrIdentificador" size="15" dataType="text" disabled="true" required="false" serializable="true"/>
			<adsm:hidden property="controleCarga.meioTransporteByIdTransportado.idMeioTransporte" serializable="true"/>
		</adsm:textbox>

		<adsm:textbox property="controleCarga.meioTransporteByIdSemiRebocado.nrFrota" label="semiReboque" labelWidth="20%" width="27%" size="9" maxLength="20" dataType="text" disabled="true" serializable="true">
			<adsm:textbox property="controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador" size="15" dataType="text" disabled="true" serializable="true"/>
			<adsm:hidden property="controleCarga.meioTransporteByIdSemiRebocado.idMeioTransporte" serializable="true"/>
		</adsm:textbox>

		<adsm:textbox property="controleCarga.motorista.pessoa.nrIdentificacaoFormatado" label="motorista" labelWidth="23%" width="77%" size="20" dataType="text" disabled="true" serializable="false">
			<adsm:textbox property="controleCarga.motorista.pessoa.nmPessoa" size="35" dataType="text" disabled="true" serializable="false"/>
			<adsm:hidden property="controleCarga.motorista.idMotorista" serializable="false"/>
		</adsm:textbox>

		<adsm:hidden property="idEquipeOperacao"/>
		<adsm:hidden property="versao"/>
		<adsm:listbox 
			label="equipe" 
			size="3" 
			property="equipe" 
			optionProperty="idChapa"
			optionLabelProperty="usuario.funcionario.codPessoa.nome"
			labelWidth="23%"
			width="77%" 
			showOrderControls="false" boxWidth="180" showIndex="false" serializable="false" required="false">
		</adsm:listbox>

		<adsm:listbox 
			label="lacres" 
			size="3" 
			property="lacres" 
			optionProperty="idLacre"
			optionLabelProperty="nrLacres" 
			labelWidth="23%"
			width="27%" 
			showOrderControls="false" boxWidth="180" showIndex="false" serializable="false" required="false">
		</adsm:listbox>

		<adsm:section caption="informacoesComplementares"/>
		<adsm:textarea property="obComentarios" label="comentario" maxLength="500" labelWidth="23%" width="77%" rows="2" columns="90" required="true" />

		<adsm:listbox 
			label="equipeAuditoria" 
			size="3" 
			property="equipeAuditoria" 
			optionProperty="idEquipe"
			labelWidth="23%"
			width="77%"
			showOrderControls="false" 
			boxWidth="320" showIndex="false" serializable="true" required="true">
			<adsm:lookup
				property="usuario"
				idProperty="idUsuario" 
				criteriaProperty="nrMatricula"
				service="lms.portaria.manterRegistrosAuditoriasCargasAction.findLookupFuncionario" 
				dataType="text" size="16" maxLength="14" action="/configuracoes/consultarFuncionariosView" serializable="false"
			>
				<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
				<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
				<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
				<adsm:propertyMapping relatedProperty="equipeAuditoria_usuario.funcionario.codPessoa.nome" modelProperty="nmUsuario"/>

				<adsm:textbox dataType="text" property="usuario.funcionario.codPessoa.nome" size="45" disabled="true" serializable="false"/>
			</adsm:lookup>
		</adsm:listbox>

		<adsm:listbox 
			label="lacresAtuais" 
			size="3" 
			property="lacresAtuais" 
			optionProperty="idLacre"
			optionLabelProperty="nrLacres"
			labelWidth="23%" 
			width="77%" onContentChange="onLacresAtuaisContentChange" 
			showOrderControls="false" boxWidth="180" showIndex="false" serializable="true" required="false">
			<adsm:textbox property="nrLacres" dataType="text" maxLength="8" />
		</adsm:listbox>

		<adsm:combobox property="tpResultado" domain="DM_RESULTADO_AUDITORIA" label="resultado" labelWidth="23%" width="27%" required="true" onchange="onTpResultadoChange(this);"/>

		<adsm:section caption="liberacao"/>

		<adsm:checkbox property="meioTransporteLiberado" label="meioTransporteLiberado" labelWidth="23%" width="27%" disabled="true" />

		<adsm:textbox property="dhLiberacao" dataType="JTDateTimeZone" label="dataHoraLiberacao" labelWidth="23%" size="15" width="27%" disabled="true" picker="false"/> 
		<adsm:textarea property="obMotivoLiberacao" label="motivoLiberacao" labelWidth="23%" maxLength="800" width="77%" rows="2" columns="90" disabled="true" />

		<adsm:hidden property="usuarioLiberacao.idUsuario"/>
		<adsm:textbox property="usuarioLiberacao.nmUsuario" dataType="text" label="usuarioLiberacao" size="30" maxLength="50" labelWidth="23%" width="27%" disabled="true" serializable="false"/>

		<adsm:buttonBar>
			<adsm:button id="emitirResultadosButton" caption="emitir" onclick="emitirResultados()" />
			<adsm:button id="controleCargaButton" caption="emitirControleCargas" boxWidth="140" onclick="emitirControleCarga()"/>
			<adsm:button id="storeButton" caption="salvar" onclick="onStoreButtonClick(this);" />
			<adsm:newButton id="botaoNovo"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	document.getElementById("idProcessoWorkflow").masterLink = "true";

	function filialOnChange(obj) {
		var retorno = filial_sgFilialOnChangeHandler();
		if (obj.value == '') {
			resetControleCargaRelateds();
		}
		return retorno;
	}

	function resetControleCargaRelateds() {
		resetValue("controleCarga.idControleCarga");
		resetValue("equipe");
		resetValue("lacres");
		resetValue("lacresAtuais");
	}

	function filialDataLoad_cb(data) {
		return filial_sgFilial_exactMatch_cb(data);
	}

	function filialPopup(data) {
		return true;
	}

	function emitirResultados() {
		var data = new Array();

		data.idRegistroAuditoria = getElementValue("idRegistroAuditoria");
		var sdo = createServiceDataObject("lms.portaria.manterRegistrosAuditoriasCargasAction.execute", "openPdf", data);
		executeReportWindowed(sdo, 'pdf');
	}

	// lista de lacres que foram violados 
	// e serão e que serao atualizados
	var lacresViolados = new Array();
	var removeLacre = false;

	function onLacresAtuaisContentChange(e) {
		if (e.name == "deleteButton_click") {
			var lacresAtuais = document.getElementById("lacresAtuais");
			if (lacresAtuais.options[lacresAtuais.selectedIndex].value > 0) {
				openConferenciaLacre();
				if (removeLacre == true) {
					removeLacre = false;
					return true;
				}
				return false;
			}
		}
	}

	function onStoreButtonClick(element) {
		var strService = "lms.portaria.manterRegistrosAuditoriasCargasAction.store";
		var strCallBack = "storeCallback";
		var form = element.form;
		var tab = getTab(form.document);

		var controleCarga = getElement("controleCarga.idControleCarga");
		if(getElementValue("controleCarga.filialByIdFilialOrigem.idFilial") == "") {
			alertI18nMessage("requiredField", controleCarga.label, false);
			setFocus("controleCarga.filialByIdFilialOrigem.sgFilial");
			return false;
		}
		if(tab.validate({name:"storeButton_click"}) == false) {
			return false;
		}

		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false}; 
		var	formBean = buildFormBeanFromForm(form);

		// adidciona o array de lacres violados ao form bean
		setNestedBeanPropertyValue(formBean, "lacresViolados", lacresViolados);
		setNestedBeanPropertyValue(formBean, "lacresVioladosSize", lacresViolados.length);
		setNestedBeanPropertyValue(formBean, "lacresSize", document.getElementById("lacres").length);
		setNestedBeanPropertyValue(formBean, "lacresAtuaisSize", document.getElementById("lacresAtuais").length);

		var storeSDO = createServiceDataObject(strService, strCallBack, formBean);

		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);

		return true;
	}

	function storeCallback_cb(data, error) {
		store_cb(data, error);
		if (error == undefined) {
			// zera o array
			lacresViolados = new Array();

			// apresenta mensagem ao usuario
			var msg = getNestedBeanPropertyValue(data, "mensagem");
			if (msg != undefined) {
				alert(msg);
			}
			// se agora possui data de liberacao, entao meio transporte liberado
			if (getNestedBeanPropertyValue(data, "dhLiberacao") != undefined) {
				setElementValue("meioTransporteLiberado", true);
			}
			bloqueiaTela();

			if (error == undefined && data != undefined &&
				getNestedBeanPropertyValue(data,"isWorkflow") == "true")
					alert(i18NLabel.getLabel("LMS-00065"));

			setFocusOnNewButton();
		}
	}

	function dataLoad_cb(data, error) {
		onDataLoad_cb(data, error); 

		// marca ou desmarca a liberacao do meio de transporte
		onTpResultadoChange(document.getElementById("tpResultado"));

		if (getElementValue("idProcessoWorkflow") != "") {
			setDisabled(document, true);
		} else {
			bloqueiaTela();
			setFocusOnNewButton();
		}
	}

	function bloqueiaTela() {
		setDisabled(document, true);
		setDisabled("emitirResultadosButton", false);
		setDisabled("botaoNovo", false);

		if (getElementValue("tpResultado") == "A" || getElementValue("dhLiberacao") != "") {
			setDisabled("controleCargaButton", false);
		}
	}

	function pageLoad_cb() {
		onPageLoad_cb();
		lacresAtuaisElement.onclick = "";

		if (getElementValue("idProcessoWorkflow") != "") {
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
			xmit({serviceDataObjects:[sdo]});
		} else {
			findFilialUsuario();
		}
	}

	function onControleCargaChange() {
		if (document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").value == "") {
			resetValue("equipe");
			resetValue("lacres");
			resetValue("lacresAtuais");
		}
		return controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}

	function onControleCargaCallback_cb(data) {
		controleCarga_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
		findListboxesValues();
	}

	function findListboxesValues(idControleCarga) {
		var data = new Array();
		setNestedBeanPropertyValue(data, "idControleCarga", idControleCarga);
		var sdo = createServiceDataObject("lms.portaria.manterRegistrosAuditoriasCargasAction.findListboxesValues", "findListboxesValues", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function findListboxesValues_cb(data) {
		if (data != undefined) {
			var formElem = document.getElementById(mainForm);
			fillFormWithFormBeanData(formElem.tabIndex, data);

			setElementValue("controleCarga.eventoControleCarga.dhEvento", setFormat("controleCarga.eventoControleCarga.dhEvento", getNestedBeanPropertyValue(data, "eventoControleCarga.dhEvento")));
		}
	}

	function openConferenciaLacre() { 
		showModalDialog('portaria/manterRegistrosAuditoriasCargasLacres.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:250px;'); 
	}

	var idFilial;
	var sgFilial;
	var nmFilial;
	function setFilialValues() {
		if (idFilial != undefined &&
			sgFilial != undefined &&
			nmFilial != undefined) {
			setElementValue("filial.idFilial", idFilial);
			setElementValue("filial.sgFilial", sgFilial);
			setElementValue("filial.pessoa.nmFantasia", nmFilial);
		}
	}

	function findFilialUsuario() {
		var data = new Array();
		var sdo = createServiceDataObject("lms.portaria.manterRegistrosAuditoriasCargasAction.findFilialUsuario", "findFilialUsuario", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function findFilialUsuario_cb(data, exception) {
		if (exception == null) {
			idFilial = getNestedBeanPropertyValue(data, "filial.idFilial");
			sgFilial = getNestedBeanPropertyValue(data, "filial.sgFilial");
			nmFilial = getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia");
		}
		setFilialValues();
	}

	function initWindow(event) {
		if (event.name == 'newButton_click' || event.name == 'tab_click') {
			setDisabled("storeButton", false);
			setDisabled("emitirResultadosButton", true);
			setDisabled("controleCargaButton", true);
			desbloqueiaTela();
			lacresViolados = new Array();
			removeLacre = false;
			setFilialValues();
			setFocusOnFirstFocusableField();
		} 
	}

	function desbloqueiaTela() {
		//setDisabled("filial.idFilial", false);
		//setDisabled("controleCarga.filialByIdFilialOrigem.idFilial", false);
		setDisabled("controleCarga.idControleCarga", true);
		setDisabled("obComentarios", false);
		setDisabled("tpResultado", false);
		setDisabled("equipeAuditoria", false);
		setDisabled("equipeAuditoria_usuario.idUsuario", false);
		setDisabled("lacresAtuais", false);
		setDisabled("lacresAtuais_nrLacres", false);
	}

	function onTpResultadoChange(e) {
		var dhLiberacao = document.getElementById("dhLiberacao");
		if ((e.options[e.selectedIndex].value == 'A') || (e.options[e.selectedIndex].value == 'R' && dhLiberacao.value != "" ))
			setElementValue("meioTransporteLiberado", true);
		else 
			setElementValue("meioTransporteLiberado", false);
	}

	/**********************************************************************
	* Funções referentes ao comportamento da lookup de controle de carga
	***********************************************************************/	
		function sgFilialOnChangeHandler(obj) {
		if (obj.value=="") {
			disableNrControleCarga(true);
			resetValue("controleCarga.idControleCarga");
			resetValue("equipe");
			resetValue("lacres");
			resetValue("lacresAtuais");
		} else {
			disableNrControleCarga(false);
		}
		return lookupChange({e:document.forms[0].elements["controleCarga.filialByIdFilialOrigem.idFilial"]});
	}

	function filialCCDataLoad_cb(data) {
		if (data != undefined && data.length == 1) {
			resetControleCargaRelateds();
			disableNrControleCarga(false);
		}
		return controleCarga_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	}

	function filialCCPopup(data) {
		if (data != undefined) {
			resetControleCargaRelateds();
			disableNrControleCarga(false);
		}
	}

	function disableNrControleCarga(disable) {
		setDisabled("controleCarga.idControleCarga", disable);
	}

	function disableNrControleCarga_cb(data, error) {
		if (data.length==0) disableNrControleCarga(false);
		return lookupExactMatch({e:document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"), data:data});
	}

	
	function loadDataByNrControleCargaPopup(data) {
		if (data!=undefined && data.idControleCarga != undefined) {
			findListboxesValues(data.idControleCarga);
		}
		return true;
	}

	/**
	 * Carrega os dados da tela de carregarVeiculos apartir dos dados retornados da 
	 * consulta de 'findCarregamentoDescargaByNrControleCarga'
	 */
	function loadDataByNrControleCarga_cb(data, error) {
		controleCarga_nrControleCarga_exactMatch_cb(data);
		//Verifica se este objeto e nulo
		if (data[0]!=undefined) {
			findListboxesValues(data[0].idControleCarga);
		}
	}

	/**
	 * Verifica o atual valor do campo de nrControleCarga
	 */
	function checkValueControleCarga(valor) {
		var retorno = controleCarga_nrControleCargaOnChangeHandler();
		if (valor=="") {
			resetValue("equipe");
			resetValue("lacres");
			resetValue("lacresAtuais");;
		}
		return retorno;
	}

	function equipeAuditoriaDataLoad_cb(data) {
		equipeAuditoria_usuario_idChapa_exactMatch_cb(data);
		if (data != undefined && data.length >= 1) {
			document.getElementById("equipeAuditoria_usuario.idUsuario").value = getNestedBeanPropertyValue(data, ":0.usuario.idUsuario");
		}
	}

	function equipeAuditoriaPopup(data) {
		if (data == undefined)
			return;
		setNestedBeanPropertyValue(data, "idUsuario", getNestedBeanPropertyValue(data, "usuario.idUsuario"));
	}

	//Chama emissão do controle de carga, V para Viagem e C para Coleta/entrega
	function emitirControleCarga() {
		var data = new Array();

		data.tpControleCarga = getElementValue("tpControleCargaValue");
		data._reportCall = true;
		data.tpFormatoRelatorio = "pdf";
		setNestedBeanPropertyValue(data, "controleCarga.idControleCarga", getElementValue("controleCarga.idControleCarga"));

		var service = null;
		if (data.tpControleCarga == "V") {
			data.blRegistroAuditoria = true;
			data.sgAcao = "RE";
			service = "lms.carregamento.emitirControleCargasAction.execute";
		} else {
			data.blEmissao = false;
			service = "lms.coleta.emitirControleColetaEntregaAction.execute";
		}
		var sdo = createServiceDataObject(service, "openReportWithLocator", data);
		var remoteCall = {serviceDataObjects:[sdo]};
		xmit(remoteCall);
	}
</script>