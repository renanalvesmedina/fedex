<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
/**
  * Carrega página.
  */   
function carregaPagina_cb() {
      onPageLoad_cb();
      setMasterLink(document, true);
      document.getElementById("ocorrenciaDoctoServico.comunicadoApreensao.vlMulta").label = document.getElementById("ocorrenciaDoctoServico.comunicadoApreensao.moeda.idMoeda").label;
      if(getElementValue("origem") == "regOcorrDoctoServico") {        
            document.getElementById("ocorrenciaDoctoServico.doctoServico.tpDocumentoServico").onchange();
            lookupChange({e:document.getElementById("ocorrenciaDoctoServico.doctoServico.idDoctoServico"), forceChange:true});
      }
}
</script>
<adsm:window service="lms.pendencia.emitirComunicadoApreensaoClienteAction" onPageLoadCallBack="carregaPagina">
	<adsm:form action="/pendencia/emitirComunicadoApreensaoCliente">
		<adsm:hidden property="origem"/>
		<adsm:combobox 
			label="documentoServico"
			labelWidth="21%"
			width="79%"
			property="ocorrenciaDoctoServico.doctoServico.tpDocumentoServico"
			service="lms.pendencia.emitirComunicadoApreensaoClienteAction.findTipoDocumentoServico"
			optionProperty="value" 
			optionLabelProperty="description"
			onchange="return tpDoctoServicoOnChangeHandler()">

			<adsm:lookup 
				dataType="text"
				property="ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem"
				idProperty="idFilial" criteriaProperty="sgFilial"
				service=""
				disabled="true"
				action=""
				size="3" 
				maxLength="3" popupLabel="pesquisarFilial"
				picker="false" 
				onDataLoadCallBack="enableDoctoServico"
				onchange="return sgFilialDoctoServicoOnChangeHandler()"
						 />
			<adsm:lookup popupLabel="pesquisarDocumentoServico"
						 dataType="integer"
						 property="ocorrenciaDoctoServico.doctoServico"
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico"
						 service=""
						 action=""
						 onDataLoadCallBack="retornoDocumentoServico"
						 size="10" 
						 maxLength="8" 
						 required="true"
						 serializable="true" 
						 disabled="true" 
						 mask="00000000"
						 onchange="return nrDoctoServicoOnChangeHandler();"
						 onPopupSetValue="retornoPopup"/>
		</adsm:combobox>
		
		<adsm:hidden property="ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.idPessoa" serializable="true" />
		<adsm:textbox 
			label="remetente"
			labelWidth="21%" 
			width="79%" 
			property="ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacao"
			dataType="text" 
			size="18" 
			maxLength="18" 
			disabled="true" 
			serializable="true">
			<adsm:textbox 
				property="ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" 
				dataType="text" 
				size="50" 
				maxLength="50" 
				disabled="true" 
				serializable="true"/>
		</adsm:textbox>
		
		<adsm:textbox
			label="emailRemetente"
			labelWidth="21%" 
			width="79%" 
			property="ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.dsEmail"
			dataType="text" 
			size="110" 
			serializable="true"
			maxLength="110"/>

		<adsm:hidden property="ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.idPessoa" serializable="true" />
		<adsm:textbox 
			label="destinatario"
			labelWidth="21%" 
			width="79%" 
			property="ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao"  
			dataType="text" 
			size="18" 
			maxLength="18" 
			disabled="true" 
			serializable="true">
			<adsm:textbox 
				property="ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa"
				dataType="text" 
				size="50" 
				maxLength="50" 
				disabled="true" 
				serializable="true"/>
		</adsm:textbox>

		<adsm:textbox 
			label="emailDestinatario"
			labelWidth="21%" 
			width="79%" 
			dataType="text"
			size="110" 
			maxLength="110"
			property="ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.dsEmail"
			serializable="true"/>


		<adsm:textbox label="ocorrencia" property="ocorrenciaDoctoServico.ocorrenciaPendenciaByIdOcorBloqueio.cdOcorrencia"	labelWidth="21%" 
			width="79%" size="3" dataType="text" disabled="true">
			<adsm:textbox 
				property="ocorrenciaDoctoServico.ocorrenciaPendenciaByIdOcorBloqueio.dsOcorrencia"
				dataType="text"
				size="50"
				disabled="true" 
				serializable="true"/>
		</adsm:textbox>
			
		<adsm:textbox 
			label="dataOcorrencia" 
			property="ocorrenciaDoctoServico.dhBloqueio"
			dataType="JTDateTimeZone" 
			labelWidth="21%" 
			width="79%" 
			disabled="true" 
			picker="false"
			serializable="true"/>

		<adsm:textbox 
			label="numeroTermoApreensao" 
			dataType="text" 
			property="ocorrenciaDoctoServico.comunicadoApreensao.nrTermoApreensao"
			labelWidth="21%" 
			width="29%"
			required="true"
			disabled="false" 
			serializable="true"
			size="22" 
			maxLength="20" />
			
		<adsm:textbox 
			dataType="JTDate" 
			property="ocorrenciaDoctoServico.comunicadoApreensao.dtOcorrencia"
			label="dataTermoApreensao" 
			labelWidth="23%" 
			width="27%"
			required="true"
			disabled="false" 
			picker="true"
			serializable="true"/>


		<adsm:hidden property="ocorrenciaDoctoServico.comunicadoApreensao.moeda.siglaSimbolo" />
		<adsm:hidden property="ocorrenciaDoctoServico.comunicadoApreensao.idComunicadoApreensao" serializable="true" />

		<adsm:combobox property="ocorrenciaDoctoServico.comunicadoApreensao.moeda.idMoeda" 
					   service="lms.pendencia.emitirComunicadoApreensaoClienteAction.findComboMoeda" 
					   optionProperty="idMoeda" 
					   optionLabelProperty="siglaSimbolo" 
					   label="valorMulta" 
					   width="79%" 
					   labelWidth="21%"
					   onchange="onChangeForSetHiddenValue(this, 'ocorrenciaDoctoServico.comunicadoApreensao.moeda.siglaSimbolo')">
			<adsm:textbox 
				property="ocorrenciaDoctoServico.comunicadoApreensao.vlMulta"
				dataType="currency"
				disabled="true" 
				required="true"
				serializable="true"/>
		</adsm:combobox>
				
		<adsm:textarea 
			label="motivoAlegado" 
			property="ocorrenciaDoctoServico.comunicadoApreensao.dsMotivoAlegado"
			maxLength="200" 
			columns="90" 
			rows="4" 
			disabled="false"
			required="true"
			labelWidth="21%" 
			width="79%"
			serializable="true"/>

		<adsm:section caption="responsavel" />

		<adsm:textbox 
			property="filial.sgFilial" 
			label="filial"
			dataType="text" 
			size="3" 
			maxLength="3" 
			disabled="true"
			labelWidth="21%" 
			width="79%"
			serializable="true">
			<adsm:textbox 
				dataType="text" 
				property="filial.pessoa.nmFantasia" 
				size="50"
				maxLength="50" 
				disabled="true"
				serializable="true" />
		</adsm:textbox>

		<adsm:textbox 
			label="responsavel"
			labelWidth="21%" 
			width="79%" 
			property="usuario.nrMatricula"
			dataType="integer" 
			size="18" 
			maxLength="18" 
			disabled="true" 
			serializable="true">
			<adsm:textbox 
				property="usuario.nmUsuario"
				dataType="text" 
				size="50" 
				maxLength="50" 
				disabled="true" 
				serializable="true"/>
		</adsm:textbox>

		<adsm:textbox 
					 dataType="email" 
					 size="40" 
					 maxLength="40"
					 labelWidth="21%" 
					 width="79%" 
					 property="usuario.dsEmail"
					 label="emailResponsavel" 
					 serializable="true"/>

		<adsm:textbox 
					 property="telefone" 
					 label="telefone" 
					 dataType="text"
					 size="15%" 
					 maxLength="15" 
					 labelWidth="21%" 
					 width="29%" 
					 serializable="true"/>
			
		<adsm:textbox 
					 property="ramal" 
					 label="ramal" 
					 dataType="text" 
					 size="5"
					 maxLength="5" 
					 labelWidth="23%" 
					 width="27%"
					 serializable="true"/>
		
		<adsm:textbox
					 property="fax" 
					 label="fax" 
					 dataType="text" 
					 size="15%"
					 maxLength="15" 
					 labelWidth="21%" 
					 width="79%"
					 serializable="true"/>

		<adsm:i18nLabels>
			<adsm:include key="LMS-17007"/>
		</adsm:i18nLabels>

		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="enviarPorEmail" disabled="true" id="enviarPorEmail" onclick="validatePCE('email')"/>
			<adsm:button caption="emitirCarta" id="emitirCarta" disabled="true" onclick="validatePCE('carta')"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form> 
</adsm:window>

<script type="text/javascript">

function initWindow(eventObj) {
	document.getElementById("ocorrenciaDoctoServico.doctoServico.tpDocumentoServico").required = "true";
	document.getElementById("ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.idFilial").required = "true";
	document.getElementById("ocorrenciaDoctoServico.comunicadoApreensao.moeda.idMoeda").required = "true";
	
	if (eventObj.name == "cleanButton_click") {
		setDisabled("ocorrenciaDoctoServico.doctoServico.idDoctoServico", true);
		setDisabled("ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.idFilial", true);		
		setDisabled("ocorrenciaDoctoServico.comunicadoApreensao.vlMulta", true);
	}
	setFocusOnFirstFocusableField();	
}

function onChangeForSetHiddenValue(combo, strHidden) {
	setElementValue(strHidden, combo.options[combo.selectedIndex].text);
	if (combo.selectedIndex == 0){
		setDisabled("ocorrenciaDoctoServico.comunicadoApreensao.vlMulta", true);
		resetValue("ocorrenciaDoctoServico.comunicadoApreensao.vlMulta");
	} else {
		setDisabled("ocorrenciaDoctoServico.comunicadoApreensao.vlMulta", false);
	}	
}

	//#####################################################
	// Inicio da validacao do pce
	//#####################################################

	var caller; //Distingue qual e o botao que fez o chamado, evita "redundancia"
	
	/**
	 * Faz o mining de ids de pedidoColeta para iniciar a validacao dos mesmos
	 *
	 * @param methodComplement
	 */
	function validatePCE(callerButton) {

		if (callerButton=='email'){
			document.getElementById("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.dsEmail").required = "true";
			document.getElementById("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.dsEmail").required = "true";
		} else {
			document.getElementById("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.dsEmail").required = "false";
			document.getElementById("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.dsEmail").required = "false";
		}
	
		var valid = validateForm(document.forms[0]);
		if (!valid){
			return valid;
		}
		caller = callerButton;		
		
		var data = new Object();
		data.idDoctoServico = getElementValue("ocorrenciaDoctoServico.doctoServico.idDoctoServico");
		var sdo = createServiceDataObject("lms.pendencia.emitirComunicadoApreensaoClienteAction.validatePCE", "validatePCE", data);
		xmit({serviceDataObjects:[sdo]});
	}
		
	/**
	 * Callback da chamada de validacao do PCE, chama a popUp de alert com os dados do
	 * PCE caso necessario.
	 *
	 * @param data
	 * @param error
	 */
	function validatePCE_cb(data, error) {
		if (data._exception==undefined) {
			if (data.destinatario.codigo!=undefined) {
				showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + data.destinatario.codigo + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
			}
			if (data.remetente.codigo!=undefined) {
				showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + data.remetente.codigo + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
			}
		} else {
			alert(error);
		}
		
		//Redireciona para o metodo de acordo com o botao selecionado.
		if (caller=='email') {
			enviarPorEmailOnclick();
		} else {
			emitirCartaOnClick();
		}
	}
	
	/**
	 * Este callback existe decorrente de uma necessidade da popUp de alert.
	 */
	function alertPCE_cb() {
		//Empty...
	}	
	
	//#####################################################
	// Fim da validacao do pce
	//#####################################################

function enviarPorEmailOnclick() {
	var map = buildFormBeanFromForm(this.document.forms[0]);
	var idDoctoServico = getElementValue("ocorrenciaDoctoServico.doctoServico.idDoctoServico");
	var nrDoctoServico = getElementValue("ocorrenciaDoctoServico.doctoServico.nrDoctoServico");
	var sgFilial = getElementValue("ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.sgFilial");
	
	setNestedBeanPropertyValue(map, "ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.sgFilial", sgFilial);
	setNestedBeanPropertyValue(map, "ocorrenciaDoctoServico.doctoServico.nrDoctoServico", nrDoctoServico);
	setNestedBeanPropertyValue(map, "ocorrenciaDoctoServico.doctoServico.idDoctoServico", idDoctoServico);

	var sdo = createServiceDataObject("lms.pendencia.emitirComunicadoApreensaoClienteAction.executeEnviarPorEmail", "resultadoEnviarPorEmail", map);
	xmit({serviceDataObjects:[sdo]});		
}

function resultadoEnviarPorEmail_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		return false;
	} else {
		showSuccessMessage();
	}	
}

function emitirCartaOnClick() {
	var map = buildFormBeanFromForm(this.document.forms[0]);
	var sgFilial = getElementValue("ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.sgFilial");
	var nrDoctoServico = getElementValue("ocorrenciaDoctoServico.doctoServico.nrDoctoServico");
	var idDoctoServico = getElementValue("ocorrenciaDoctoServico.doctoServico.idDoctoServico");
	
	setNestedBeanPropertyValue(map, "ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.sgFilial", sgFilial);
	setNestedBeanPropertyValue(map, "ocorrenciaDoctoServico.doctoServico.nrDoctoServico", nrDoctoServico);
	setNestedBeanPropertyValue(map, "ocorrenciaDoctoServico.doctoServico.idDoctoServico", idDoctoServico);

//	reportButtonScript("lms.pendencia.emitirComunicadoApreensaoClienteAction", "openPdf", this.document.forms[0]);
	
	var sdo = createServiceDataObject("lms.pendencia.emitirComunicadoApreensaoClienteAction.execute", "openPdf", map);
	executeReportWindowed(sdo, 'pdf');
	
}

// INICIO JS da tag de doctoServico ********************************************

function enableDoctoServico_cb(data) {
   var r = ocorrenciaDoctoServico_doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
   if (r == true) {
      setDisabled("ocorrenciaDoctoServico.doctoServico.idDoctoServico", false);
      setFocus(document.getElementById("ocorrenciaDoctoServico.doctoServico.nrDoctoServico"));
   }
}

function retornoDocumentoServico_cb(data) {
	ocorrenciaDoctoServico_doctoServico_nrDoctoServico_exactMatch_cb(data);

	if (data != undefined && data.length > 0) {
		var idDoctoServico = getElementValue("ocorrenciaDoctoServico.doctoServico.idDoctoServico");
	
		if (idDoctoServico != undefined && idDoctoServico != '') {
			verificaComunicadoApreensao(idDoctoServico);
		}
	}
}

function verificaComunicadoApreensao(idDoctoServico) { 
	var map = new Array();
	setNestedBeanPropertyValue(map, "idDoctoServico", idDoctoServico);
	
	var sdo = createServiceDataObject("lms.pendencia.emitirComunicadoApreensaoClienteAction.executeVerificaComunicadoApreensao", "resultadoVerificaComunicadoApreensao", map);
	xmit({serviceDataObjects:[sdo]});
}

function resultadoVerificaComunicadoApreensao_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		resetValue("ocorrenciaDoctoServico.doctoServico.nrDoctoServico");
		setFocus("ocorrenciaDoctoServico.doctoServico.nrDoctoServico");
		return false;
	} else {
		//var idDoctoServico = getElementValue("ocorrenciaDoctoServico.doctoServico.idDoctoServico");
		var idOcorrenciaDoctoServico = data.idOcorrenciaDoctoServico;
		buscaDadosTela(idOcorrenciaDoctoServico)
	}
}

/**
 * Realiza pesquisa para os dados necessarios da tela.
 * Esta função não é obrigatória.
 */
function buscaDadosTela(idOcorrenciaDoctoServico) { 
	var map = new Array();
	setNestedBeanPropertyValue(map, "idOcorrenciaDoctoServico", idOcorrenciaDoctoServico);

	var sdo = createServiceDataObject("lms.pendencia.emitirComunicadoApreensaoClienteAction.findByIdToComunicadoApreensaoAoCliente", "resultado_buscaDadosTela", map);
	xmit({serviceDataObjects:[sdo]});
}

/**
 * Retorno da pesquisa dos dados da tela.
 * Esta função não é obrigatória. 
 */
function resultado_buscaDadosTela_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		resetValue("ocorrenciaDoctoServico.doctoServico.nrDoctoServico");
		return false;
	} else {
		setElementValue('ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.idPessoa', setFormat(document.getElementById("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.idPessoa"), data.doctoServico.clienteByIdClienteRemetente.pessoa.idPessoa));
		setElementValue('ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacao', setFormat(document.getElementById("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacao"), data.doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacao));
		setElementValue('ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa', setFormat(document.getElementById("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa"), data.doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa));
		setElementValue('ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.dsEmail', setFormat(document.getElementById("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.dsEmail"), data.doctoServico.clienteByIdClienteRemetente.pessoa.dsEmail));
		setElementValue('ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.idPessoa', setFormat(document.getElementById("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.idPessoa"), data.doctoServico.clienteByIdClienteDestinatario.pessoa.idPessoa));
		
		setElementValue('ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao', setFormat(document.getElementById("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao"), data.doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao));
		setElementValue('ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa', setFormat(document.getElementById("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa"), data.doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa));
		setElementValue('ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.dsEmail', setFormat(document.getElementById("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.dsEmail"), data.doctoServico.clienteByIdClienteDestinatario.pessoa.dsEmail));
		setElementValue('ocorrenciaDoctoServico.ocorrenciaPendenciaByIdOcorBloqueio.cdOcorrencia', data.ocorrenciaPendenciaByIdOcorBloqueio.cdOcorrencia);
		setElementValue('ocorrenciaDoctoServico.ocorrenciaPendenciaByIdOcorBloqueio.dsOcorrencia', setFormat(document.getElementById("ocorrenciaDoctoServico.ocorrenciaPendenciaByIdOcorBloqueio.dsOcorrencia"), data.ocorrenciaPendenciaByIdOcorBloqueio.dsOcorrencia));
		setElementValue('ocorrenciaDoctoServico.dhBloqueio', setFormat(document.getElementById("ocorrenciaDoctoServico.dhBloqueio"), data.dhBloqueio));

		if (getNestedBeanPropertyValue(data, "comunicadoApreensao.nrTermoApreensao") != undefined) {
			setElementValue('ocorrenciaDoctoServico.comunicadoApreensao.nrTermoApreensao', setFormat(document.getElementById("ocorrenciaDoctoServico.comunicadoApreensao.nrTermoApreensao"), data.comunicadoApreensao.nrTermoApreensao));
			setElementValue('ocorrenciaDoctoServico.comunicadoApreensao.dtOcorrencia', setFormat(document.getElementById("ocorrenciaDoctoServico.comunicadoApreensao.dtOcorrencia"), data.comunicadoApreensao.dtOcorrencia));
			setElementValue('ocorrenciaDoctoServico.comunicadoApreensao.dsMotivoAlegado', setFormat(document.getElementById("ocorrenciaDoctoServico.comunicadoApreensao.dsMotivoAlegado"), data.comunicadoApreensao.dsMotivoAlegado));
			setElementValue('ocorrenciaDoctoServico.comunicadoApreensao.idComunicadoApreensao', data.comunicadoApreensao.idComunicadoApreensao);
			if (data.comunicadoApreensao.moeda.idMoeda!=""){
				setElementValue('ocorrenciaDoctoServico.comunicadoApreensao.moeda.idMoeda', data.comunicadoApreensao.moeda.idMoeda);
				setElementValue('ocorrenciaDoctoServico.comunicadoApreensao.vlMulta', setFormat(document.getElementById("ocorrenciaDoctoServico.comunicadoApreensao.vlMulta"), data.comunicadoApreensao.vlMulta));
				setDisabled('ocorrenciaDoctoServico.comunicadoApreensao.vlMulta', false);
			}
		}
		else {
			resetValue("ocorrenciaDoctoServico.comunicadoApreensao.idComunicadoApreensao");
		}

		setElementValue('filial.sgFilial', setFormat(document.getElementById("filial.sgFilial"), data.filial.sgFilial));
		setElementValue('filial.pessoa.nmFantasia', setFormat(document.getElementById("filial.pessoa.nmFantasia"), data.filial.pessoa.nmFantasia));
		setElementValue('usuario.nrMatricula', setFormat(document.getElementById("usuario.nrMatricula"), data.usuario.nrMatricula));
		setElementValue('usuario.nmUsuario', setFormat(document.getElementById("usuario.nmUsuario"), data.usuario.nmUsuario));
		setElementValue('usuario.dsEmail', setFormat(document.getElementById("usuario.dsEmail"), data.usuario.dsEmail));
		
		setDisabled("enviarPorEmail", false);
		setDisabled("emitirCarta", false);
	}
}
// FIM JS da tag de doctoServico ***********************************************
function retornoPopup(data) {
	verificaComunicadoApreensao(data.idDoctoServico);
}

function nrDoctoServicoOnChangeHandler() {
	var x = ocorrenciaDoctoServico_doctoServico_nrDoctoServicoOnChangeHandler();
	if (getElementValue("ocorrenciaDoctoServico.doctoServico.nrDoctoServico")=="") {
		var tpDoctoServico = getElementValue("ocorrenciaDoctoServico.doctoServico.tpDocumentoServico");
		var idFilialDoctoServico = getElementValue("ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.idFilial");
		var sgFilialDoctoServico = getElementValue("ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.sgFilial");
		cleanButtonScript(this.document);
		setElementValue("ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.idFilial", idFilialDoctoServico);
		setElementValue("ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.sgFilial", sgFilialDoctoServico);		
		setElementValue("ocorrenciaDoctoServico.doctoServico.tpDocumentoServico", tpDoctoServico);
		setDisabled("ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.idFilial", false);
		setDisabled("ocorrenciaDoctoServico.doctoServico.idDoctoServico", false);
	}
	return x;
}

function tpDoctoServicoOnChangeHandler(){
	var idDoctoServico = getElementValue("ocorrenciaDoctoServico.doctoServico.idDoctoServico");
	var x = changeDocumentWidgetType({
		documentTypeElement:document.getElementById('ocorrenciaDoctoServico.doctoServico.tpDocumentoServico'),
		filialElement:document.getElementById('ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.idFilial'),
		documentNumberElement:document.getElementById('ocorrenciaDoctoServico.doctoServico.idDoctoServico'),
		parentQualifier:'ocorrenciaDoctoServico',
		documentGroup:'SERVICE',
		actionService:'lms.pendencia.emitirComunicadoApreensaoClienteAction'});
	var tpDoctoServico = getElementValue("ocorrenciaDoctoServico.doctoServico.tpDocumentoServico");
	if (idDoctoServico!=""){
		cleanButtonScript(this.document);
	}
	
	if (tpDoctoServico!=""){
		setElementValue("ocorrenciaDoctoServico.doctoServico.tpDocumentoServico", tpDoctoServico);
		setDisabled("ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.idFilial", false);
		setDisabled("ocorrenciaDoctoServico.doctoServico.idDoctoServico", false);
		setDisabled("ocorrenciaDoctoServico.doctoServico.nrDoctoServico", true);
	}
	return x;
}

function sgFilialDoctoServicoOnChangeHandler(){
	var idDoctoServico = getElementValue("ocorrenciaDoctoServico.doctoServico.idDoctoServico");
	var x = changeDocumentWidgetFilial({
		 filialElement:document.getElementById('ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.idFilial'),
		 documentNumberElement:document.getElementById('ocorrenciaDoctoServico.doctoServico.idDoctoServico')
		 });
	if (getElementValue("ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.sgFilial")==""
		&& idDoctoServico!=""){
		tpDoctoServicoOnChangeHandler();
	}
	return x;
}
</script>