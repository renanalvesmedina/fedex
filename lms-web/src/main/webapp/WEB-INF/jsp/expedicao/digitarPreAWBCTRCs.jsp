<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	title="digitarPreAWBIncluirExcluirCTRCs"
	service="lms.expedicao.digitarPreAWBCTRCsAction"
	onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-04082"/>
		<adsm:include key="LMS-04164"/>
		<adsm:include key="LMS-04441"/>
		<adsm:include key="LMS-04165"/>
		<adsm:include key="LMS-04166"/>
		<adsm:include key="LMS-04109"/>
		<adsm:include key="LMS-04505"/>
		<adsm:include key="LMS-00061"/>
		<adsm:include key="LMS-04533"/>
		<adsm:include key="doctoServico"/>
	</adsm:i18nLabels>

	<adsm:form
		action="/expedicao/digitarPreAWBCTRCs">

		<adsm:section
			caption="incluirExcluirCTRCTitulo"
			width="90%"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%" />

		<adsm:hidden
			serializable="false"
			property="doctoServicoOriginal.filialByIdFilialOrigem.pessoa.nmFantasia"/>
		<adsm:hidden
			property="tpSituacaoConhecimento"
			serializable="false"
			value="E"/>

		<adsm:combobox
			optionLabelProperty="description"
			optionProperty="value"
			property="tpDocumentoServico"
			width="35%"
			boxWidth="10%"
			service="lms.expedicao.digitarPreAWBCTRCsAction.findTipoDocumento"
			label="doctoServico"
			labelWidth="15%"
			onchange="return tipoDocumentoChange();">
			<adsm:lookup
				dataType="text"
				property="doctoServicoOriginal.filialByIdFilialOrigem"
				idProperty="idFilial"
				criteriaProperty="sgFilial"
				service="lms.expedicao.digitarPreAWBCTRCsAction.findFilialConhecimento"
				onDataLoadCallBack="filialConhecimento"
				onchange="return filialConhecimentoChange();"
				action="/municipios/manterFiliais"
				serializable="false"
				size="3"
				maxLength="3"
				picker="false"
				disabled="true">
				
				<adsm:hidden
					property="filialByIdFilialOrigem.idFilial"/>
				<adsm:propertyMapping
				 	relatedProperty="filialByIdFilialOrigem.idFilial" 
				 	modelProperty="idFilial"/>
	
				<adsm:propertyMapping
					modelProperty="pessoa.nmFantasia" 
					relatedProperty="doctoServicoOriginal.filialByIdFilialOrigem.pessoa.nmFantasia"/>
					
				<adsm:textbox
					dataType="integer"
					size="4"
					maxLength="8"
					mask="00000000"
				onchange="return nrConhecimentoChange(this);"
					property="doctoServicoOriginal.nrConhecimento"
					serializable="false"
					disabled="true">
				
					<adsm:label key="hifen"/>
					<adsm:lookup
						dataType="integer"
						property="doctoServicoOriginal"
						popupLabel="pesquisarNumeroCTRC"
						idProperty="idDoctoServico"
						criteriaProperty="dvConhecimento" 
						service="lms.expedicao.digitarPreAWBCTRCsAction.findDoctoServico"
						action="/expedicao/pesquisarConhecimento"
						onchange="return doctoServicoOriginalChange()"
						onDataLoadCallBack="numCtrcOnDataLoadCallBack"
						onPopupSetValue="popupSetValueDoctoServicoOriginal"
						afterPopupSetValue="afterPopupDoctoServicoOriginal"
						size="1"
						disabled="true"
						maxLength="1">
						
						<adsm:propertyMapping 
							criteriaProperty="tpDocumentoServico" 
							modelProperty="tpDocumentoServico" 
						/>
						
						<adsm:propertyMapping
							criteriaProperty="tpSituacaoConhecimento" 
						 	modelProperty="tpSituacaoConhecimento"
						 	inlineQuery="false"/>
	
						<adsm:propertyMapping
						 	criteriaProperty="doctoServicoOriginal.nrConhecimento" 
						 	modelProperty="nrConhecimento"
						 	addChangeListener="false"/>
	
						<adsm:propertyMapping
						 	criteriaProperty="doctoServicoOriginal.filialByIdFilialOrigem.idFilial"
						 	modelProperty="filialByIdFilialOrigem.idFilial"/>
	
						<adsm:propertyMapping
							criteriaProperty="doctoServicoOriginal.filialByIdFilialOrigem.sgFilial"
						 	modelProperty="filialByIdFilialOrigem.sgFilial"
						 	inlineQuery="false"/> 	 
	
						<adsm:propertyMapping
						 	criteriaProperty="doctoServicoOriginal.filialByIdFilialOrigem.pessoa.nmFantasia"
						 	modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia"
						 	inlineQuery="false"/>	 
	
						<adsm:propertyMapping
						 	relatedProperty="doctoServicoOriginal.nrConhecimento" 
						 	modelProperty="nrConhecimento"/>
	
						<adsm:propertyMapping
						 	relatedProperty="nrConhecimento" 
						 	modelProperty="nrConhecimento"/>
	
						<adsm:propertyMapping
						 	relatedProperty="sgAeroporto" 
						 	modelProperty="sgAeroporto"/>
	
					 	<adsm:propertyMapping
						 	relatedProperty="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
						 	modelProperty="nmPessoa"/>
					</adsm:lookup>
				</adsm:textbox>
			</adsm:lookup>
		</adsm:combobox>
		<adsm:complement
			label="aeroportoDeDestino"
			labelWidth="15%"
			width="34%">
			<adsm:hidden
				property="nrConhecimento"/>
			<adsm:textbox
				dataType="text"
				property="sgAeroporto"
				size="3"
				disabled="true"
				maxLength="3"/>
			<adsm:textbox
				dataType="text"
	            property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
	           	serializable="false"
	           	size="20"
	           	maxLength="45"
	           	disabled="true"/>
		</adsm:complement>
	
		<adsm:label
			key="branco" 
 			style="border:none"
 			width="1%" />
		
		<adsm:textbox 
			label="codigoBarras" 
			labelWidth="15%" 
			width="84%"
			property="nrCodigoBarras" 
			dataType="text" 
			disabled="false" 
			size="52"
			maxLength="44"
			serializable="true" />
	
		
		<adsm:label
			key="branco" 
 			style="border:none"
 			width="1%" />
			
		<adsm:hidden property="filialPesquisa.pessoa.nmFantasia" serializable="false"/>		
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		<adsm:hidden property="tpAcesso" value="F"/>	
    	<adsm:lookup label="coleta" labelWidth="15%" width="84%"
    		              dataType="text" 
    		              property="filialPesquisa"  
    		              idProperty="idFilial" 
    		              criteriaProperty="sgFilial" 
					      service="lms.coleta.cancelarColetaAction.findFilialBySgFilial" 
					      action="/municipios/manterFiliais" 
					      onchange="return sgFilialOnChangeHandler()"
					      size="3" maxLength="3" picker="false" serializable="false" disabled="true">
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>					      
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping relatedProperty="filialPesquisa.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			
			<adsm:lookup size="8" maxLength="8" dataType="integer"

						 onDataLoadCallBack="carregaDadosPedidoColeta"
						 idProperty="idPedidoColeta"
						 property="pedidoColeta"
						 action="/coleta/consultarColetas" cmd="pesq"
						 service="lms.coleta.cancelarColetaAction.findLookupComEnderecoCompleto"
						 criteriaProperty="nrColeta"
						 mask="00000000"
						 onPopupSetValue="buscaPedidoColeta"
						 onchange="return coletaOnChangeHandler();"
						 >
    
						 <adsm:propertyMapping criteriaProperty="filialPesquisa.idFilial" 	 	   modelProperty="filialByIdFilialResponsavel.idFilial" disable="true" />
						 <adsm:propertyMapping criteriaProperty="filialPesquisa.sgFilial" 	 	   modelProperty="filialByIdFilialResponsavel.sgFilial" disable="true"/>
						 <adsm:propertyMapping criteriaProperty="filialPesquisa.pessoa.nmFantasia" modelProperty="filialByIdFilialResponsavel.pessoa.nmFantasia" disable="true"/>						 
						 <adsm:propertyMapping relatedProperty="filialPesquisa.sgFilial" blankFill="false" modelProperty="filialByIdFilialResponsavel.sgFilial"/>
						 
						 <adsm:propertyMapping relatedProperty="pedidoColeta.nrColeta" 	 	 modelProperty="nrColeta"/>
						 <adsm:propertyMapping relatedProperty="pedidoColeta.idPedidoColeta" modelProperty="idPedidoColeta"/>
						 
			</adsm:lookup>
		</adsm:lookup>		

	
		<adsm:buttonBar freeLayout="true">

			<adsm:button
				caption="salvar"
				id="storeButton"
				disabled="false"
				onclick="salvar();"/>

			<adsm:button 
				caption="novo"
				id="newButton"
				buttonType="reset"
				disabled="false"
				onclick="limparCampos();"/>

			<adsm:button
				caption="fechar"
				buttonType="closeButton"
				id="closeButton"
				disabled="false"
				onclick="self.close();"/>

		</adsm:buttonBar>
					
	</adsm:form>
	
	<adsm:grid
		idProperty="idCtoAwb"
		property="ctoAwb"
		showGotoBox="false"
		onRowClick="populaForm"
		rows="7"
		gridHeight="115"
		unique="true"
		mode="main"
		onDataLoadCallBack="gridOnDataLoadCallBack"
		showTotalPageCount="false">

		<adsm:gridColumn
			title="documento"
			property="nrConhecimentoFormatado"
			width="14%" />

		<adsm:gridColumn 
			title="aeroportoDeDestino" 
			property="sgAeroportoDestino" 
			width="20%"/>

		<adsm:gridColumn 
			title="peso" 
			property="psReal" 
			width="16%" 
			align="right" 
			unit="kg"
			mask="#,###,###,###,##0.000" 
			dataType="decimal"/>

		<adsm:gridColumn 
			title="pesoCubado" 
			property="psAforado" 
			width="17%" 
			align="right" 
			unit="kg"
			mask="#,###,###,###,##0.000" 
			dataType="decimal"/>

		<adsm:gridColumn 
			dataType="integer"
			title="qtdeVolumes" 
			property="qtVolumes" 
			width="17%" 
			align="right" />

		<adsm:gridColumn 
			title="valorMerc" 
			property="vlMercadoria" 
			width="16%" 
			align="right" 
			dataType="currency" 
			unit="reais"/>

		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>

	</adsm:grid>
</adsm:window>

<script language="javascript" type="text/javascript" src="../lib/expedicao.js"></script>
<script language="javascript" type="text/javascript">
<!--
	function salvar(){
		var idFilial = getElementValue("doctoServicoOriginal.filialByIdFilialOrigem.idFilial");
		var nrConhecimento = getElementValue("doctoServicoOriginal.nrConhecimento");
		var nrCodigoBarras = getElementValue("nrCodigoBarras");
		var tpDoctoServico = getElementValue("tpDocumentoServico");
		var hasCodigoBarras = (nrCodigoBarras != null && nrCodigoBarras != "");
		var idPedidoColeta = getElementValue("pedidoColeta.idPedidoColeta");
		var hasIdPedidoColeta = (idPedidoColeta != null && idPedidoColeta != "");
		
		if (tpDoctoServico == "" && idFilial == "" && nrConhecimento == "" && !hasCodigoBarras && !hasIdPedidoColeta) {
			alert("LMS-04505 - " + i18NLabel.getLabel('LMS-04505'));
		} else {
			if (!hasCodigoBarras && !hasIdPedidoColeta) {
				if (idFilial == "") {
					alertI18nMessage("LMS-04109", "doctoServico", true);
					setFocus("doctoServicoOriginal.filialByIdFilialOrigem.sgFilial");
					return;
				}
				if (nrConhecimento == "") {
					alertI18nMessage("LMS-04109", "doctoServico", true);
					setFocus("doctoServicoOriginal.nrConhecimento");
					return;
				}
			}

			if (validateTabScript(document.forms[0])) {
				var nrF = getElement("doctoServicoOriginal.filialByIdFilialOrigem.idFilial");
				var nrC = getElement("doctoServicoOriginal.nrConhecimento");
				var nrD = getElement("doctoServicoOriginal.dvConhecimento");
				
				var isSalvar;

				if (hasCodigoBarras) {
					isSalvar = true;
				} else {
					if (tpDoctoServico == 'CTE') {
						isSalvar = (nrF.value != '' && nrC.value != '');
					} else {
						isSalvar = (nrF.value != '' && nrC.value != '' && nrD.value != '');
					}
				}

				if (isSalvar) {
					storeButtonScript(
							'lms.expedicao.digitarPreAWBCTRCsAction.storeInSession',
							'storeSession', document.forms[0]);
				} else if(hasIdPedidoColeta) {
					storeButtonScript(
							'lms.expedicao.digitarPreAWBCTRCsAction.storeInSessionByPedidoColeta',
							'storeSessionPedidoColeta', document.forms[0]);					
				} else {
					if (!hasCodigoBarras && !hasIdPedidoColeta) {
						alertI18nMessage("LMS-04109", "doctoServico", true);
					}
				}
			}
		}
	}
	/************************************************************\
	 *
	\************************************************************/
	function myOnPageLoad_cb() {
		onPageLoad_cb();
		setDisabled("newButton", false);
		populaGrid();
		codigoBarras();
		carregaDadosUsuarioLogado();
	}
	/************************************************************\
	 *
	\************************************************************/
	function filialConhecimento_cb(data) {
		var noData = (data == undefined || data.length == 0);
		if (noData) {
			resetValue("doctoServicoOriginal.nrConhecimento");
		}

		return doctoServicoOriginal_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	}
	/************************************************************\
	 *
	\************************************************************/
	function filialConhecimentoChange() {
		var toDisable = getElementValue("doctoServicoOriginal.filialByIdFilialOrigem.sgFilial") == "";
		validateFieldsCTRC(toDisable);
		if (toDisable) {
			setFocus("storeButton", false);
		}
		return doctoServicoOriginal_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}
	/************************************************************\
	 *
	\************************************************************/
	function doctoServicoOriginalChange() {
		var dvConhecimento = getElementValue("doctoServicoOriginal.dvConhecimento");
		var isNotEmpty = dvConhecimento != "";

		setFocus("storeButton");

		if (isNotEmpty) {
			var nrConhecimento = getElementValue("doctoServicoOriginal.nrConhecimento");
			if (!validaDigitoVerifCTRC(nrConhecimento, dvConhecimento)) {
				alert(i18NLabel.getLabel('LMS-04082'));
				return false;
			}
		}
		return doctoServicoOriginal_dvConhecimentoOnChangeHandler();
	}

	function getDigitoVerifCTRC(nrConhecimento) {
		nrConhecimento = nrConhecimento.substring(nrConhecimento.length - 6,
				nrConhecimento.length);
		var nr = parseInt(stringToNumber(nrConhecimento), 10);
		dvConhecimento = nr % 11;
		return (dvConhecimento == 10) ? 0 : dvConhecimento;
	}

	function validaDigitoVerifCTRC(nrConhecimento, dvConhecimento) {
		var dv = getDigitoVerifCTRC(nrConhecimento);
		return (dv == dvConhecimento);
	}

	/************************************************************\
	 *
	\************************************************************/
	function limparCampos() {
		var nrCodigoBarras = getElementValue("nrCodigoBarras");

		newButtonScript();
		setDisabled("newButton", false);
		setDisabled(
				getElement('doctoServicoOriginal.filialByIdFilialOrigem.idFilial'),
				true);

		validateFieldsCTRC(true);

		if (nrCodigoBarras != null && nrCodigoBarras != '') {
			setFocus("nrCodigoBarras");
		}
	}
	function validateFieldsCTRC(toDisable) {
		var tpDoctoServico = getElementValue('tpDocumentoServico');

		setDisabled("doctoServicoOriginal.idDoctoServico", toDisable);

		if (tpDoctoServico == 'CTE' || toDisable) {
			setDisabled("doctoServicoOriginal.dvConhecimento", true);
		} else {
			setDisabled("doctoServicoOriginal.dvConhecimento", false);
		}

		return true;
	}
	/************************************************************\
	 *
	\************************************************************/
	function populaGrid() {
		ctoAwbGridDef.executeSearch({}, true);
	}
	/************************************************************\
	 *
	\************************************************************/
	function storeSessionPedidoColeta_cb(data, erros) {
		if (erros != undefined) {
			alert(erros);
			return false;
		}
		
		if (data.length > 0) {
			for(var i = 0; i < data.length; i++) {
				validateRetornoStoreSession(data[i]);
			}
		}
		
	}

	function storeSession_cb(data, erros) {
		if (erros != undefined) {
			alert(erros);
			return false;
		}
		validateRetornoStoreSession(data);
	}
	
	function validateRetornoStoreSession(data) {
		if (data.confirm == "true") {
			if (window.confirm(i18NLabel.getLabel("LMS-04165"), "")) {
				var dados = {
					nrConhecimento : data.nrConhecimento,
					filialByIdFilialOrigem : {
						idFilial : data.idFilial
					}
				}
				var service = "lms.expedicao.digitarPreAWBCTRCsAction.storeInSessionNaoAereo";
				var sdo = createServiceDataObject(service,
						"storeSessionNaoAereo", dados);
				xmit({
					serviceDataObjects : [ sdo ]
				});
			} else {
				return executeErrorProcedings();
			}
		} else if (data.error != "") {
			if (data.error == "LMS-00061") {
				var msgError = getI18nMessage('LMS-00061');
				limparCampos();
				setFocus("nrCodigoBarras");
				alert(msgError);
				return true;
			}

			if (data.errorParam != undefined) {
				var msgError = getI18nMessage(data.error, new Array(
						data.coringa1, data.coringa2, data.coringa3), false);
				return executeErrorProcedings(data.error + " - " + msgError);
			} else {
				return executeErrorProcedings(data.error + " - " + i18NLabel.getLabel(data.error));
			}
		} else {
			return executeSuccessProcedings();
		}

	}
	/************************************************************\
	 *
	\************************************************************/
	function storeSessionNaoAereo_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		} else if (data.error != "") {
			return executeErrorProcedings(i18NLabel.getLabel(data.error));
		} else {
			return executeSuccessProcedings();
		}
	}

	function executeErrorProcedings(message) {
		if (message != undefined) {
			alert(message);
		}
		setFocusCodigoBarras("doctoServicoOriginal.filialByIdFilialOrigem.sgFilial");
		limparCampos();

		return true;
	}

	function executeSuccessProcedings() {
		var nrCodigoBarras = getElementValue("nrCodigoBarras");
		setFocusCodigoBarras("closeButton");
		limparCampos();
		populaGrid();

		return true;
	}

	function setFocusCodigoBarras(idElement) {
		var nrCodigoBarras = getElementValue("nrCodigoBarras");
		if (nrCodigoBarras != null && nrCodigoBarras != '') {
			setFocus("nrCodigoBarras");
		} else {
			setFocus(document.getElementById(idElement), false);
		}
	}

	/************************************************************\
	 *
	\************************************************************/
	function populaForm(valor) {
		return false;
	}
	/************************************************************\
	 *
	\************************************************************/
	function numCtrcOnDataLoadCallBack_cb(data) {
		doctoServicoOriginal_dvConhecimento_exactMatch_cb(data);
		updateStoreButton(data);
		if (data == undefined || data.length == 0) {
			limparCampos();
		}
		return;
	}

	/************************************************************\
	 *
	\************************************************************/
	function popupSetValueDoctoServicoOriginal(data) {

		if (data && data != undefined) {
			setElementValue(
					'doctoServicoOriginal.filialByIdFilialOrigem.sgFilial',
					data.sgFilialOrigem);
			setElementValue(
					'doctoServicoOriginal.filialByIdFilialOrigem.idFilial',
					data.idFilialOrigem);
			setElementValue('filialByIdFilialOrigem.idFilial',
					data.idFilialOrigem);
			setElementValue('sgAeroporto', data.sgAeroporto);
			setElementValue('aeroportoByIdAeroportoDestino.pessoa.nmPessoa',
					data.nmPessoa);

			if (data.dvConhecimento) {
				setElementValue('doctoServicoOriginal.dvConhecimento',
						data.dvConhecimento);
			}
		}

		updateStoreButton(data);
		return;
	}

	/************************************************************\
	 *
	\************************************************************/
	function afterPopupDoctoServicoOriginal(data) {
		var tpDoctoServico = getElementValue('tpDocumentoServico');
		var idValid = data != undefined;

		if (idValid) {
			setElementValue(
					"doctoServicoOriginal.filialByIdFilialOrigem.idFilial",
					data.filialByIdFilialOrigem.idFilial);
			setElementValue("filialByIdFilialOrigem.idFilial",
					data.filialByIdFilialOrigem.idFilial);
			setElementValue(
					"doctoServicoOriginal.filialByIdFilialOrigem.sgFilial",
					data.filialByIdFilialOrigem.sgFilial);

			if (tpDoctoServico == 'CTR') {
				setElementValue('doctoServicoOriginal.dvConhecimento',
						data.dvConhecimento);
			} else {
				setElementValue('doctoServicoOriginal.dvConhecimento', '');
			}
		}
		validateFieldsCTRC(!idValid);
	}

	function tipoDocumentoChange() {
		var tpDoctoServico = getElementValue('tpDocumentoServico');
		var isDisable = (tpDoctoServico == '');

		resetValue('doctoServicoOriginal.nrConhecimento');
		resetValue('doctoServicoOriginal.filialByIdFilialOrigem.sgFilial');
		resetValue('doctoServicoOriginal.dvConhecimento');
		setDisabled(getElement('doctoServicoOriginal.idDoctoServico'),
				isDisable);
		setDisabled(
				getElement('doctoServicoOriginal.filialByIdFilialOrigem.idFilial'),
				isDisable);
		setDisabled('doctoServicoOriginal.nrConhecimento', isDisable);

		if (tpDoctoServico == 'CTE' || isDisable) {
			setDisabled('doctoServicoOriginal.dvConhecimento', true);
		} else {
			setDisabled('doctoServicoOriginal.dvConhecimento', false);
		}
	}

	/************************************************************\
	 *
	\************************************************************/
	function updateStoreButton(data) {
		if (data != undefined) {
			setDisabled("storeButton", false);
			setFocus("storeButton", false);
		}
	}
	/************************************************************\
	 *
	\************************************************************/
	function gridOnDataLoadCallBack_cb() {
		setDisabled("doctoServicoOriginal.idDoctoServico", true);
		setDisabled(
				getElement('doctoServicoOriginal.filialByIdFilialOrigem.idFilial'),
				true);
		setDisabled("storeButton", false);
		setFocus("closeButton");
	}

	function initWindow(eventObj) {
		setDisabled("storeButton", false);
		carregaDadosUsuarioLogado();
	}

	function nrConhecimentoChange(data) {
		var nrConhecimento = getElementValue("doctoServicoOriginal.nrConhecimento");
		var filter = new Array();

		if (nrConhecimento != "") {
			setNestedBeanPropertyValue(filter, "tpDocumentoServico",
					getElementValue("tpDocumentoServico"));
			setNestedBeanPropertyValue(filter,
					"filialByIdFilialOrigem.idFilial",
					getElementValue("filialByIdFilialOrigem.idFilial"));
			setNestedBeanPropertyValue(filter, "nrConhecimento", nrConhecimento);

			var sdo = createServiceDataObject(
					"lms.expedicao.digitarPreAWBCTRCsAction.findDoctoServico",
					"loadRelated", filter);
			xmit({
				serviceDataObjects : [ sdo ]
			});
		} else {
			resetValue('doctoServicoOriginal.dvConhecimento');
			resetValue('sgAeroporto');
			resetValue('aeroportoByIdAeroportoDestino.pessoa.nmPessoa');
		}
	}

	function loadRelated_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}

		if (data && data != undefined) {
			setElementValue('doctoServicoOriginal.idDoctoServico',
					data[0].idDoctoServico);
			setElementValue('nrConhecimento', data[0].nrConhecimento);
			setElementValue('sgAeroporto', data[0].sgAeroporto);
			setElementValue('aeroportoByIdAeroportoDestino.pessoa.nmPessoa',
					data[0].nmPessoa);
		}
	}

	function addEvent(obj, type, fn) {
		if (obj.addEventListener) {
			obj.addEventListener(type, fn, false);
		} else if (obj.attachEvent) {
			obj["e" + type + fn] = fn;
			obj[type + fn] = function() {
				obj["e" + type + fn](window.event);
			}
			obj.attachEvent("on" + type, obj[type + fn]);
		}
	}

	var adicionouEvento = false;
	function codigoBarras() {
		setFocus("nrCodigoBarras");
		if (!adicionouEvento) {
			adicionouEvento = true;
			addEvent(
					document.getElementById("nrCodigoBarras"),
					"keydown",
					function(e) {
						if (e.keyCode == 13) {
							storeButtonScript(
									'lms.expedicao.digitarPreAWBCTRCsAction.storeInSession',
									'storeSession', document.forms[0]);
						}
					});
		}
	}
	
	/**
	 * Controla o objeto coleta
	 */	
	 function carregaDadosUsuarioLogado() {
		   	var data = new Array();
			var sdo = createServiceDataObject("lms.coleta.cancelarColetaAction.getDadosUsuarioLogado", "carregaDadosUsuarioLogado", data);
		   	xmit({serviceDataObjects:[sdo]});

		}
			
		function carregaDadosUsuarioLogado_cb(data, error) {
			if (error!=undefined) {
				alert(error);
				return false;
			}
			setElementValue("filialPesquisa.idFilial", data.filial.idFilial);
			setElementValue("filialPesquisa.sgFilial", data.filial.sgFilial);
			setElementValue("filialPesquisa.pessoa.nmFantasia", data.filial.pessoa.nmFantasia);
			
			disableNrPedidoColeta(false);		
			setFocus("tpDocumentoServico");
		}	 
	 
	 
	function sgFilialOnChangeHandler() {
		if (getElementValue("filialPesquisa.sgFilial")=="") {
			disableNrPedidoColeta(true);
			resetValue("filialPesquisa.idFilial");
			limpaGrid();
		} else {
			disableNrPedidoColeta(false);
		}
		return lookupChange({e:document.forms[0].elements["filialPesquisa.idFilial"]});
	}
	
	function disableNrPedidoColeta(disable) {
		setDisabled(document.getElementById("pedidoColeta.nrColeta"), disable);
	}
	
	function buscaPedidoColeta(data) {
		var criteria = new Array();
	    // Monta um map
	    setNestedBeanPropertyValue(criteria, "idPedidoColeta", data.idPedidoColeta);
	
	    var sdo = createServiceDataObject("lms.coleta.cancelarColetaAction.findLookupComEnderecoCompleto", "carregaDadosPedidoColeta", criteria);
	    xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Função que ao informar o numero da coleta, carrega a grid
	 * de detalhe coleta
	 */
	function carregaDadosPedidoColeta_cb(data, error) {
		var retornoLookup = pedidoColeta_nrColeta_exactMatch_cb(data);
		
		if (!retornoLookup)
		   return false;
		
		return retornoLookup;
	}
	
	
	function coletaOnChangeHandler() {
		if (getElementValue("pedidoColeta.nrColeta")=="") {
			resetValue("pedidoColeta.idPedidoColeta");
// 			limpaGrid();
		}
		
		return pedidoColeta_nrColetaOnChangeHandler();
	}
</script>