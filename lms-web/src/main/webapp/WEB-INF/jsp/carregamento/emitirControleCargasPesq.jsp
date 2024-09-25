<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.emitirControleCargasAction" onPageLoadCallBack="retornoCarregarPagina" >
	<adsm:i18nLabels>
		<adsm:include key="LMS-05354"/>
	</adsm:i18nLabels>


	<adsm:form action="/carregamento/emitirControleCargas" idProperty="idControleCarga">
		
		<adsm:hidden property="idSolicMonitPreventivo"/>
		<adsm:hidden property="idReciboPostoPassagem"/>
		<adsm:hidden property="controleCargaConcatenado" serializable="false"/>

		<adsm:hidden property="filialByIdFilialAtualizaStatus.idFilial" />		
	
		<adsm:hidden property="idFilial"/>
		
		<adsm:textbox dataType="text" property="sgFilial" label="filial" size="3" 
					  maxLength="3" labelWidth="21%" width="79%" disabled="true">
			<adsm:textbox dataType="text" property="pessoa.nmFantasia" size="50" 
						  maxLength="50" disabled="true"/>
		</adsm:textbox>
		

		<adsm:hidden property="tpControleCarga" value="V" serializable="true" />
		<adsm:hidden property="filialByIdFilialOrigem.pessoa.nmFantasia" />
		<adsm:lookup dataType="text" property="filialByIdFilialOrigem" 
					 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.carregamento.emitirControleCargasAction.findLookupFilialByControleCarga" 
					 action="/municipios/manterFiliais" 
					 onchange="return sgFilialOnChangeHandler();" 
					 onDataLoadCallBack="disableNrControleCarga"
					 label="controleCargas" labelWidth="21%" width="79%" 
					 size="3" maxLength="3" picker="false" popupLabel="pesquisarFilial">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia"
								  relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" />					 
			<adsm:lookup dataType="integer" property="controleCarga" 
						 idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.carregamento.emitirControleCargasAction.findLookupControleCarga" 
						 action="/carregamento/manterControleCargas" cmd="list"
						 onPopupSetValue="loadDataByControleCarga" 
						 onDataLoadCallBack="loadDataByNrControleCarga" 
						 onchange="return checkValueControleCarga(this.value)"
						 maxLength="8" size="8" mask="00000000" popupLabel="pesquisarControleCarga" disabled="true" required="true" >
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" disable="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="filialByIdFilialOrigem.idFilial" disable="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="filialByIdFilialOrigem.sgFilial" disable="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" criteriaProperty="filialByIdFilialOrigem.pessoa.nmFantasia" disable="false" />
 				 <adsm:propertyMapping modelProperty="tpControleCarga" criteriaProperty="tpControleCarga" disable="true" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" relatedProperty="filialByIdFilialOrigem.idFilial" blankFill="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="filialByIdFilialOrigem.sgFilial" blankFill="false" />
 				 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
			</adsm:lookup>
		</adsm:lookup>	
		
		<adsm:hidden property="sgAcao"/>
		<adsm:textbox label="acao" property="dsAcao" dataType="text" size="10" 
					  maxLength="10" labelWidth="21%" width="79%" disabled="true"/>		
		
		<adsm:hidden property="tpStatusControleCarga.value" />
		<adsm:textbox label="status" property="tpStatusControleCarga.description" dataType="text" size="30" 
					  maxLength="30" labelWidth="21%" width="79%" disabled="true"/>	

		<adsm:hidden property="rota.idRota" />
		<adsm:textbox label="rota" property="rota.dsRota" dataType="text" size="36" 
					  labelWidth="21%" width="79%" disabled="true" serializable="false"/>
		
		<adsm:hidden property="motorista.idMotorista" />
		<adsm:hidden property="meioTransporteByIdTransportado.idMeioTransporte" />
		<adsm:textbox label="veiculo" property="meioTransporteByIdTransportado.nrFrota" dataType="text" 
					  maxLength="6" size="6" labelWidth="21%" width="79%" disabled="true">
			<adsm:textbox property="meioTransporteByIdTransportado.nrIdentificador" 
						  dataType="text" maxLength="25" size="25" disabled="true"/>
		</adsm:textbox>
		
		<adsm:hidden property="meioTransporteByIdSemiRebocado.idMeioTransporte" />		
		<adsm:textbox label="semiReboque" property="meioTransporteByIdSemiRebocado.nrFrota" dataType="text" 
					  maxLength="6" size="6" labelWidth="21%" width="79%" disabled="true">
			<adsm:textbox property="meioTransporteByIdSemiRebocado.nrIdentificador" 
						  dataType="text" maxLength="25" size="25" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox label="ociosidadeVisual" property="pcOcupacaoInformado" dataType="decimal"
					  size="6" maxLength="3" labelWidth="21%" width="79%" required="true"
					  onchange="return verificaPercentual()"/>

		<adsm:buttonBar>
			<adsm:button buttonType="button" id="btnMdfe" caption="monitorarMdfe" onclick="openMdfe()" />
			<adsm:button buttonType="reportViewerButton" id="btnVisualizar" caption="emitir" onclick="imprimeRelatorio()" disabled="false" />
			<adsm:button id="cleanButton" caption="limpar" disabled="false" onclick="limpaTela()"/>
		</adsm:buttonBar>
		
		<script>
			var lms_05028 = '<adsm:label key="LMS-05028"/>';
			var LMS_02021 = '<adsm:label key="LMS-02021"/>';
			var LMS_05171 = '<adsm:label key="LMS-05171"/>';
			var labelControleCarga = '<adsm:label key="controleCargas"/>';
		</script>	
		
	</adsm:form>		
</adsm:window>

<script language="javascript" type="text/javascript" src="../lib/mdfe.js"></script>

<script type="text/javascript">
	var mdfe = inicializaMdfe();
	
	function inicializaMdfe() {
		var mdfe = new Mdfe();
		
		var action = "lms.carregamento.emitirControleCargasAction";
		
		mdfe.urlGerarMdfe = action+ ".generateValidateEmissaoControleCarga";
		
		mdfe.gerarMdfeCallId = "retornoGeneratePreEmissaoRelatorio";
		
		mdfe.urVerificaAutorizacaoMdfe = action+ ".verificaAutorizacaoMdfe";
		mdfe.verificaAutorizacaoMdfeCallId = "verificaAutorizacaoMdfe";
		
		mdfe.urlEncerrarMdfesAutorizados = action+ ".encerrarMdfesAutorizados";
		mdfe.encerrarMdfesAutorizadosCallId = "encerrarMdfesAutorizados";
		
		mdfe.urlVerificaEncerramentoMdfe = action+ ".verificaEncerramentoMdfe";
		mdfe.verificaEncerramentoMdfeCallId = "verificaEncerramentoMdfe";

		mdfe.urlImprimirMdfe = action+ ".imprimirMDFe";
		
		mdfe.chaveMensagemFinal = "LMS-05354";
		
		return mdfe;
		
	}

	function initWindow(eventObj) {		
		setDisabled("cleanButton", false);
		setDisabled("btnMdfe", true);
		if (eventObj.name == 'tab_load') {
			inicializaControleCarga();
		}
	}

	function retornoCarregarPagina_cb(data, error) {
		onPageLoad_cb(data, error);
		if (!error) {
			var sdo = createServiceDataObject("lms.carregamento.emitirControleCargasAction.getBasicData", "loadBasicData");
		   	xmit({serviceDataObjects:[sdo]});
		}
	}

	/**
	 * Carrega um array 'dataUsuario' com os dados do usuario em sessao
	 */
	var basicData;
	function loadBasicData_cb(data, error) {
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}
		basicData = data;
		getDadosUrl();
		fillBasicData();
	}


	function getDadosUrl() {
		var url = new URL(parent.location.href);
		var idControleCarga = url.parameters["idControleCarga"];
		if (idControleCarga != undefined && idControleCarga != "") {
			var nrControleCarga = url.parameters["nrControleCarga"];
			var idFilialOrigem = url.parameters["idFilialOrigem"];
			var sgFilialOrigem = url.parameters["sgFilialOrigem"];
			setElementValue("controleCarga.idControleCarga", idControleCarga);
			setElementValue("controleCarga.nrControleCarga", getFormattedValue("integer", nrControleCarga, "00000000", true));
			setElementValue("filialByIdFilialOrigem.idFilial", idFilialOrigem);
			setElementValue("filialByIdFilialOrigem.sgFilial", sgFilialOrigem);
			setDisabled("controleCarga.nrControleCarga", false);
			
			loadDataByControleCarga({idControleCarga:idControleCarga});
		}
	}
	
		/**
	 * Imprime o relatório
	 */
	 function imprimeRelatorio() {
	 	var f = document.forms[0];
	 	if (!validateForm(f)){
	 		return false;
	 	}

	 	var data = new Array(); 
	    setNestedBeanPropertyValue(data, "idFilial", getElementValue("idFilial"));
    	setNestedBeanPropertyValue(data, "idControleCarga", getElementValue("controleCarga.idControleCarga"));
    	setNestedBeanPropertyValue(data, "sgAcao", getElementValue("sgAcao"));
	 	var sdo = createServiceDataObject("lms.carregamento.emitirControleCargasAction.generatePreEmissaoRelatorio", 
	 		"callBackPreEmissaoRelatorio", data);
	 	xmit({serviceDataObjects:[sdo]});
	}

	/**
	 * Faz a chamada para a realizacao do workflow antes do relatorio.
	 */
	function callBackPreEmissaoRelatorio_cb(data, error) {
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}
		setElementValue("sgAcao", data.sgAcao);
		setElementValue("dsAcao", data.dsAcao);
		if(getElementValue("sgAcao") != "EM") {
			setDisabled("pcOcupacaoInformado", true);
		} else {
			setDisabled("pcOcupacaoInformado", false);
		}
		
		generateValidateEmissaoControleCarga();
	}
	 
	function generateValidateEmissaoControleCarga() {
		
		var data = buildFormBeanFromForm(document.forms[0]);
		
		mdfe.gerarMdfe(data, false);
		
		}
	
	/**
	 * Emite o relatorio caso seja possivel.
	 */
	function retornoGeneratePreEmissaoRelatorio_cb(data, error) {
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}
		var msgBloqueio = data.mensagensEnquadramento; 
		
		var msgWorkflow = '';
		if (data.mensagensWorkflowRegras != undefined && data.mensagensWorkflowRegras != '') {
			msgWorkflow += data.mensagensWorkflowRegras;  
		}
		
		if (data.mensagensWorkflowExigencias != undefined && data.mensagensWorkflowExigencias != '') {
			msgWorkflow += data.mensagensWorkflowExigencias;  
		} 
		
		var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:600px;dialogHeight:310px;';
		var idControleCarga = getElementValue("controleCarga.idControleCarga");	
		if (msgBloqueio != undefined && msgBloqueio != '') {
			var url = '/carregamento/emitirControleCargas.do?cmd=bloqueios&msgBloqueio=' + escape(msgBloqueio);
			showModalDialog(url, window, wProperties);
			return;
		}
		
		if (msgWorkflow != undefined && msgWorkflow != '') {
			var url = '/carregamento/emitirControleCargas.do?cmd=workflow&idControleCarga=' + idControleCarga + '&msgWorkflow=' + escape(msgWorkflow);
			showModalDialog(url, window, wProperties);
			return;
		}
		
		setElementValue("idSolicMonitPreventivo", data.idSolicMonitPreventivo);

		if (mdfe.gerarMdfeCallback(data))  {
			finalizaGeneratePreEmissaoRelatorio();
		}
	}
	
	function verificaAutorizacaoMdfe_cb(data, error) {
		
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			mdfe.hideMessageMDFe();
			reloadControleCarga();
			return false;
		}
		
		if (mdfe.verificaAutorizacaoMdfeCallback(data)) {
			finalizaGeneratePreEmissaoRelatorio();
		}
			
			}
		
	function finalizaGeneratePreEmissaoRelatorio() {
		mdfe.imprimirMDFe();
			executeReportWithCallback("lms.carregamento.emitirControleCargasAction.execute", "imprimeRelatorio", document.forms[0]);
		}
	

	/**
	 * Retorno da geração de SMP e inicio do processo de gerar o relatório.
	 */
	function imprimeRelatorio_cb(strFile, error) {
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}
		
		openReportWithLocator(strFile, null, "RELATORIO_CONTROLE_CARGA", true);
		if (getElementValue("sgAcao") == "EM") {
			var registraEmissao = confirm(LMS_02021);
    		if (registraEmissao) {
				var sdo = createServiceDataObject("lms.carregamento.emitirControleCargasAction.generateDataControleCargaByEmissao", 
							"retornoGenerateDataControleCargaByEmissao", 
							{idControleCarga:getElementValue("controleCarga.idControleCarga"),
							idSolicMonitPreventivo:getElementValue("idSolicMonitPreventivo")});
		    	xmit({serviceDataObjects:[sdo]});

				
		    }
	    }
	}


	/**
	 * Função de retorno da geração de evento para o Controle de Carga.
	 */
	function retornoGenerateDataControleCargaByEmissao_cb(data, error) {
    	//recarrega os dados da tela
			reloadControleCarga();

	    if (error) {
	        alert(error);
	        setFocusOnFirstFocusableField();
	        return false;
	    }
		
		getIdReciboPostoPassagem();
	}

	function reloadControleCarga() {
		var dataNew = new Array();
		setNestedBeanPropertyValue(dataNew, "idControleCarga", getElementValue("controleCarga.idControleCarga"));
		loadDataByControleCarga(dataNew);
	}


	function getIdReciboPostoPassagem() {
	    if (getElementValue("sgAcao") == "EM") {
	        var sdo = createServiceDataObject("lms.carregamento.emitirControleCargasAction.getIdReciboPostoPassagem", 
	        	"emiteReciboPostoPassagem", 
	        	{idFilialOrigem:getElementValue("filialByIdFilialOrigem.idFilial"),
	        	idControleCarga:getElementValue("controleCarga.idControleCarga")}
	        );
   		    xmit({serviceDataObjects:[sdo]});
		}
	}
	
	/**
	 * Função que retorna o ID do Recibo de Posto de Passagem caso exista.
	 */
	function emiteReciboPostoPassagem_cb(data, error) {
		if (error) {
	        alert(error);
	        setFocusOnFirstFocusableField();
	        return false;
	    }
	    if (data.idReciboPostoPassagem != undefined) {
	    	setElementValue("idReciboPostoPassagem", data.idReciboPostoPassagem);
    		var dataObject = new Object();
			dataObject.idReciboPostoPassagem = data.idReciboPostoPassagem;

			var sdo = createServiceDataObject("lms.carregamento.emitirReciboPostoPassagemAction",  "openPdfReciboPostoPassagem", dataObject); 
			xmit({serviceDataObjects:[sdo]});
	    }
	}
	
	/**
	 * Função que emite o relatório de Recibo de Posto de Passagem.
	 */
	function openPdfReciboPostoPassagem_cb(data, error) {
	    if (error) {
	        alert(error);
	        setFocusOnFirstFocusableField();
	        return false;
	    }
    	openReport(data._value, "REL_RECIBO_POSTO_PAS", false);			
        var sdo = createServiceDataObject("lms.carregamento.emitirControleCargasAction.generateAtualizacaoPostoPassagem", 
       		"retornoGenerateAtualizacaoPostoPassagem", 
       		{idReciboPostoPassagem:getElementValue("idReciboPostoPassagem")});
		xmit({serviceDataObjects:[sdo]});
	}

	function retornoGenerateAtualizacaoPostoPassagem_cb(data, error) {
	    if (error) {
	        alert(error);
	        setFocusOnFirstFocusableField();
	        return false;
	    }
	}


	/**
	 * Controla o objeto de controle carga
	 */	
	function sgFilialOnChangeHandler() {
		var r = filialByIdFilialOrigem_sgFilialOnChangeHandler();
		if (getElementValue("filialByIdFilialOrigem.sgFilial") == "") {
			resetView();
			inicializaControleCarga();
		}
		else
			setDisabled('controleCarga.idControleCarga', false);

		return r;
	}
		
	function disableNrControleCarga_cb(data, error) {
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}
		var r = filialByIdFilialOrigem_sgFilial_exactMatch_cb(data)
		if (r == true) {
			resetView();
			setDisabled('controleCarga.idControleCarga', false);
			setFocus(document.getElementById("controleCarga.nrControleCarga"));
		}
		return r;
	}

	/**
	 * Consulta da lookup.
	 */
	function loadDataByControleCarga(data) {
		var dataNew = new Array();
		setNestedBeanPropertyValue(dataNew, "idControleCarga", data.idControleCarga);
		var sdo = createServiceDataObject("lms.carregamento.emitirControleCargasAction.findByIdControleCarga", "loadDataByControleCarga", dataNew);
    	xmit({serviceDataObjects:[sdo]});
	}

	function loadDataByControleCarga_cb(data, error) {
		limpaCamposAoBuscarControleCarga();
		if (error) {
			alert(error);
			resetValue('filialByIdFilialOrigem.idFilial');
			resetValue('controleCarga.idControleCarga');
			setDisabled('controleCarga.nrControleCarga', true);
			setFocus('filialByIdFilialOrigem.sgFilial');
			return false;
		}
		setDisabled('controleCarga.idControleCarga', false);
		setElementValue("controleCarga.idControleCarga", data.controleCarga.idControleCarga);
		setElementValue("controleCarga.nrControleCarga", data.controleCarga.nrControleCarga);
		
		setElementValue("filialByIdFilialOrigem.idFilial", data.filialByIdFilialOrigem.idFilial);
		setElementValue("filialByIdFilialOrigem.sgFilial", data.filialByIdFilialOrigem.sgFilial);
		setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", data.filialByIdFilialOrigem.pessoa.nmFantasia);
		setElementValue("filialByIdFilialAtualizaStatus.idFilial", data.filialByIdFilialAtualizaStatus.idFilial);
		
		setElementValue("tpStatusControleCarga.value", data.tpStatusControleCarga.value);
		setElementValue("tpStatusControleCarga.description", data.tpStatusControleCarga.description);
		
		if(data.rota) {
			setElementValue("rota.idRota", data.rota.idRota);
			setElementValue("rota.dsRota", data.rota.dsRota);
		}
		
		if(data.motorista) {
			setElementValue("motorista.idMotorista", data.motorista.idMotorista);
		}			
		
		if(data.meioTransporteByIdTransportado) {
			setElementValue("meioTransporteByIdTransportado.idMeioTransporte", data.meioTransporteByIdTransportado.idMeioTransporte);
			setElementValue("meioTransporteByIdTransportado.nrFrota", data.meioTransporteByIdTransportado.nrFrota);
			setElementValue("meioTransporteByIdTransportado.nrIdentificador", data.meioTransporteByIdTransportado.nrIdentificador);
		}
		
		if(data.meioTransporteByIdSemiRebocado) {
			setElementValue("meioTransporteByIdSemiRebocado.idMeioTransporte", data.meioTransporteByIdSemiRebocado.idMeioTransporte);
			setElementValue("meioTransporteByIdSemiRebocado.nrFrota", data.meioTransporteByIdSemiRebocado.nrFrota);
			setElementValue("meioTransporteByIdSemiRebocado.nrIdentificador", data.meioTransporteByIdSemiRebocado.nrIdentificador);
		}

		setElementValue('pcOcupacaoInformado', setFormat(document.getElementById("pcOcupacaoInformado"), data.pcOcupacaoInformado) );

		// Formata o campo de nrControlecarga
		format(document.getElementById("controleCarga.nrControleCarga"));

		// Verifica se existe registro na tabela de Evento de Controle de Carga e retorna o campo Ação.
		setElementValue("sgAcao", data.sgAcao);
		setElementValue("dsAcao", data.dsAcao);
		if (getElementValue("sgAcao") != "EM") {
			setDisabled("pcOcupacaoInformado", true);
		} else {
			setDisabled("pcOcupacaoInformado", false);
		}
		
		if (data.btnMdfe != "true") {
			setDisabled("btnMdfe", true);
		} else {
			setDisabled("btnMdfe", false);
	}
	}
		
	/**
	 * Carrega os dados da tela de descarregarVeiculos apartir dos dados retornados da 
	 * consulta de 'findCarregamentoDescargaByNrControleCarga'
	 */
	function loadDataByNrControleCarga_cb(data, error){
		limpaCamposAoBuscarControleCarga();
		if (error) {
			alert(error);
			setFocus(document.getElementById("controleCarga.nrControleCarga"));
			return false;
		}
		var r = controleCarga_nrControleCarga_exactMatch_cb(data);
		if (r == true) {
			setElementValue("controleCarga.idControleCarga", data[0].controleCarga.idControleCarga);
			setElementValue("controleCarga.nrControleCarga", data[0].controleCarga.nrControleCarga);
			
			setElementValue("filialByIdFilialOrigem.idFilial", data[0].filialByIdFilialOrigem.idFilial);
			setElementValue("filialByIdFilialOrigem.sgFilial", data[0].filialByIdFilialOrigem.sgFilial);
			setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", data[0].filialByIdFilialOrigem.pessoa.nmFantasia);
			setElementValue("filialByIdFilialAtualizaStatus.idFilial", data[0].filialByIdFilialAtualizaStatus.idFilial);
			
			setElementValue("tpStatusControleCarga.value", data[0].tpStatusControleCarga.value);
			setElementValue("tpStatusControleCarga.description", data[0].tpStatusControleCarga.description);
			
			if(data[0].rota) {
				setElementValue("rota.idRota", data[0].rota.idRota);
				setElementValue("rota.dsRota", data[0].rota.dsRota);
			}
			
			if(data[0].motorista) {
				setElementValue("motorista.idMotorista", data[0].motorista.idMotorista);
			}			
			
			if(data[0].meioTransporteByIdTransportado) {
				setElementValue("meioTransporteByIdTransportado.idMeioTransporte", data[0].meioTransporteByIdTransportado.idMeioTransporte);
				setElementValue("meioTransporteByIdTransportado.nrFrota", data[0].meioTransporteByIdTransportado.nrFrota);
				setElementValue("meioTransporteByIdTransportado.nrIdentificador", data[0].meioTransporteByIdTransportado.nrIdentificador);
			}
			
			if(data[0].meioTransporteByIdSemiRebocado) {
				setElementValue("meioTransporteByIdSemiRebocado.idMeioTransporte", data[0].meioTransporteByIdSemiRebocado.idMeioTransporte);
				setElementValue("meioTransporteByIdSemiRebocado.nrFrota", data[0].meioTransporteByIdSemiRebocado.nrFrota);
				setElementValue("meioTransporteByIdSemiRebocado.nrIdentificador", data[0].meioTransporteByIdSemiRebocado.nrIdentificador);
			}
			
			setElementValue('pcOcupacaoInformado', setFormat(document.getElementById("pcOcupacaoInformado"), data[0].pcOcupacaoInformado) );
			
			// Formata o campo de nrControlecarga
			format(document.getElementById("controleCarga.nrControleCarga"));

			// Verifica se existe registro na tabela de Evento de Controle de Carga e retorna o campo Ação.
			setElementValue("sgAcao", data[0].sgAcao);
			setElementValue("dsAcao", data[0].dsAcao);
			if (getElementValue("sgAcao") != "EM") {
				setDisabled("pcOcupacaoInformado", true);
			} else {
				setDisabled("pcOcupacaoInformado", false);
			}
			var nrControleCarga = getFormattedValue("integer",  getElementValue("controleCarga.nrControleCarga"), "00000000", true);
			setElementValue("controleCargaConcatenado", getElementValue("filialByIdFilialOrigem.sgFilial") + " " + nrControleCarga);
			desabilitaTab("riscos", false);

			if (data[0].btnMdfe != "true") {
				setDisabled("btnMdfe", true);
			} else {
				setDisabled("btnMdfe", false);
		}

		}
		return r;
	}	
	
	
		
	/**
	 * Verifica o atual valor do campo de nrControleCarga
	 */
	function checkValueControleCarga(valor) {		
		var r = controleCarga_nrControleCargaOnChangeHandler();
		if (valor == "") {
            resetView();
		}		
		desabilitaTab("riscos", true);
		return r;
	}	
	
	/**
	 * Responsável por habilitar/desabilitar uma tab
	 */
	function desabilitaTab(aba, disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab(aba, disabled);
	}
	
	/**
	 * Verifica se o percentual da ociosidade visual está entre 0 e 100 %
	 */
	function verificaPercentual() {
		if(getElementValue("pcOcupacaoInformado") >= 0 && getElementValue("pcOcupacaoInformado") <= 100) {
			return true;
		} else {
			alert(lms_05028);
			setFocus("pcOcupacaoInformado");
			return false;
		}
	}


	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * Carrega os dados basicos da tela
	 */
	function fillBasicData() {
		setElementValue("idFilial", basicData.filial.idFilial);
		setElementValue("sgFilial", basicData.filial.sgFilial);
		setElementValue("pessoa.nmFantasia", basicData.filial.pessoa.nmFantasia);
	}
	
	/**
	 * Reseta a tela deixa
	 */
	function resetView() {
		var idFilial = getElementValue("filialByIdFilialOrigem.idFilial");
        var sgFilial = getElementValue("filialByIdFilialOrigem.sgFilial");
        var nmFantasia = getElementValue("filialByIdFilialOrigem.pessoa.nmFantasia");
		resetValue(this.document);
    	setElementValue("filialByIdFilialOrigem.idFilial", idFilial);
        setElementValue("filialByIdFilialOrigem.sgFilial", sgFilial);
        setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", nmFantasia);
		fillBasicData();
	}

	function limpaTela() {
		cleanButtonScript(this.document);
		inicializaControleCarga();
		fillBasicData();
	}

	function inicializaControleCarga() {
		setDisabled("controleCarga.idControleCarga", false);
		setDisabled("controleCarga.nrControleCarga", true);
		document.getElementById("filialByIdFilialOrigem.sgFilial").required = "true";
		document.getElementById("controleCarga.nrControleCarga").label = labelControleCarga;
		desabilitaTab("riscos", true);
	}
	
	function limpaCamposAoBuscarControleCarga(){
		resetValue("sgAcao");
		resetValue("dsAcao");
		resetValue("tpStatusControleCarga.value");
		resetValue("tpStatusControleCarga.description");
		resetValue("rota.idRota");
		resetValue("rota.dsRota");
		resetValue("motorista.idMotorista");
		resetValue("meioTransporteByIdTransportado.idMeioTransporte");
		resetValue("meioTransporteByIdTransportado.nrFrota");
		resetValue("meioTransporteByIdTransportado.nrIdentificador");
		resetValue("meioTransporteByIdSemiRebocado.idMeioTransporte");
		resetValue("meioTransporteByIdSemiRebocado.nrFrota");
		resetValue("meioTransporteByIdSemiRebocado.nrIdentificador");
		resetValue("pcOcupacaoInformado");
	}
	
	function encerrarMdfesAutorizados_cb(data, error) {
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}
		
		mdfe.verificaEncerramentoMdfe(data);
		
	}
	
	function verificaEncerramentoMdfe_cb(data, error) {
		if (error) {
			mdfe.hideMessageMDFe();
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}
		
		if (mdfe.verificaEncerramentoMdfeCallback(data)) {
			generateValidateEmissaoControleCarga();
		}
		
	}
	
	
	function openMdfe(){
		janela_closed = false;
		janela = showModalDialog('/carregamento/mdfeControleCargasPesq.do?cmd=pesq',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:640px;dialogHeight:400px;');
		janela_closed = true;
		setFocusOnFirstFocusableField();
	}
	
	
</script>