<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script type="text/javascript">
<!--
	
	function changeComboDocumentoServico(field) {
		//Indice inválido para forcar o onChange do componente
		document.getElementById("registroPriorizacaoDocto").selectedIndex = -1;
	
		if(document.getElementById("registroPriorizacaoDocto_doctoServico.idDoctoServico").masterLink=="true"){
			document.getElementById("registroPriorizacaoDocto_doctoServico.idDoctoServico").masterLink="false";
			document.getElementById("registroPriorizacaoDocto_doctoServico.nrDoctoServico").masterLink="false";
			document.getElementById("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.sgFilial").masterLink="false";
			document.getElementById("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial").masterLink="false";
		}	
		var flag = changeDocumentWidgetType(
					{documentTypeElement:field, 
					 filialElement:document.getElementById('registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial'), 
					 documentNumberElement:document.getElementById('registroPriorizacaoDocto_doctoServico.idDoctoServico'), 
					 parentQualifier:'registroPriorizacaoDocto_doctoServico',
					 documentGroup:'DOCTOSERVICE',
					 actionService:'lms.sim.registrarSolicitacoesEmbarqueAction'}
				  );
				  
        var pms = document.getElementById("registroPriorizacaoDocto_doctoServico.idDoctoServico").propertyMappings;
		
		pms[pms.length] = { modelProperty:"clienteByIdClienteDestinatario.idCliente", criteriaProperty:"destinatario.idCliente", inlineQuery:true};
		pms[pms.length] = { modelProperty:"clienteByIdClienteRemetente.idCliente", criteriaProperty:"remetente.idCliente", inlineQuery:true };
		pms[pms.length] = { modelProperty:"clienteByIdClienteDestinatario.pessoa.nrIdentificacao", criteriaProperty:"destinatario.pessoa.nrIdentificacao", inlineQuery:false};
		pms[pms.length] = { modelProperty:"clienteByIdClienteRemetente.pessoa.nrIdentificacao", criteriaProperty:"remetente.pessoa.nrIdentificacao", inlineQuery:false };
		pms[pms.length] = { modelProperty:"clienteByIdClienteDestinatario.pessoa.nmPessoa", criteriaProperty:"destinatario.pessoa.nmPessoa", inlineQuery:false};
		pms[pms.length] = { modelProperty:"clienteByIdClienteRemetente.pessoa.nmPessoa", criteriaProperty:"remetente.pessoa.nmPessoa", inlineQuery:false };
		pms[pms.length] = { modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia", relatedProperty:"registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", blankFill:false};
 		pms[pms.length] = { modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico" };

		addElementChangeListener({e:document.getElementById("destinatario.idCliente"), changeListener:document.getElementById("registroPriorizacaoDocto_doctoServico.idDoctoServico")});
		addElementChangeListener({e:document.getElementById("remetente.idCliente"), changeListener:document.getElementById("registroPriorizacaoDocto_doctoServico.idDoctoServico")});
		return flag;
	}
	
	function changeFilialDoctoServico() {
		return changeDocumentWidgetFilial({
                         filialElement:document.getElementById('registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial'),
                         documentNumberElement:document.getElementById('registroPriorizacaoDocto_doctoServico.idDoctoServico')});
	}

	//Implementação da filial do usuário logado como padrão
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	var dhRegistro = null;
	var idUsuario = null;
	var nmUsuario = null;
	
	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
		idUsuario = getNestedBeanPropertyValue(data,"usuario.idUsuario");
		nmUsuario = getNestedBeanPropertyValue(data,"usuario.nmUsuario");
		dhRegistro = getNestedBeanPropertyValue(data,"dhRegistro");
		writeDataSession();
	}
	
	function writeDataSession() {
		if (idFilial != null &&
			sgFilial != null &&
			nmFilial != null) {
			setElementValue("filial.idFilial",idFilial);
			setElementValue("filial.sgFilial",sgFilial);
			setElementValue("filial.pessoa.nmFantasia",nmFilial);
		}
		if (idUsuario != null && 
			nmUsuario != null) {
			setElementValue("usuarioCriacao.idUsuario",idUsuario);
			setElementValue("usuarioCriacao.nmUsuario",nmUsuario);
		}
		if (dhRegistro != null)
			setElementValue("dhRegistro",setFormat(document.getElementById("dhRegistro"),dhRegistro));
	}
	
	function setaMasterLink(){
		var url = new URL(parent.location.href);
		
		if(url.parameters["remetenteIdCliente"]!= undefined){
	 		setElementValue("remetente.idCliente",url.parameters["remetenteIdCliente"]);
	 		setElementValue("remetente.pessoa.nrIdentificacao",url.parameters["remetentePessoaNrIdentificacao"]);
	 	    setElementValue("remetente.pessoa.nmPessoa",url.parameters["remetentePessoaNmPessoa"]);
	 	    document.getElementById("remetente.idCliente").masterLink= "true";
	 	    setDisabled("remetente.idCliente",true);
	 	    setDisabled("remetente.pessoa.nrIdentificacao",true);
	 	    
	 	}
	 	if(url.parameters["destinatarioIdCliente"]!= undefined){
	 		setElementValue("destinatario.idCliente",url.parameters["destinatarioIdCliente"]);
	 		setElementValue("destinatario.pessoa.nrIdentificacao",url.parameters["destinatarioPessoaNrIdentificacao"]);
	 	    setElementValue("destinatario.pessoa.nmPessoa",url.parameters["destinatarioPessoaNmPessoa"]);
	 	    document.getElementById("destinatario.idCliente").masterLink= "true";
	 	    setDisabled("destinatario.idCliente", true);
	 	    setDisabled("destinatario.pessoa.nrIdentificacao", true);
	 	    
	 	}
	 	
	 	if(url.parameters["idDoctoServicoLocMerc"]!= undefined){
	 		setDisabled("filial.idFilial",true);
	 	}
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "newButton_click" || eventObj.name == "tab_click") {
		 	writeDataSession();
			behaviorNotCanceled();
			setDisabled("rotaColeta",true);
			setDisabled("cancel",true);
			setDisabled("rotaViagem",true);
			setDisabled("removeButton",true);
			if(document.getElementById("idDoctoServicoLocMerc").masterLink=="true"){
				 setElementValue("registroPriorizacaoDocto_doctoServico.idDoctoServico",getElementValue("idDoctoServicoLocMerc"));
 				 setElementValue("registroPriorizacaoDocto_doctoServico.nrDoctoServico",getElementValue("nrDoctoServicoLocMerc"));
 				 setElementValue("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.sgFilial",getElementValue("sgFilialDoctoServLocMerc"));
 				 setElementValue("registroPriorizacaoDocto_doctoServico.tpDocumentoServico",getElementValue("tpDocumentoServicoLocMerc"));
 				 addDoctoServico(true);
			}
			setaMasterLink();
			
											
		}else if (eventObj.name == "removeButton") {
			writeDataSession();
		}
		setDisabled("rotaViagem",false);
		setDisabled("rotaColeta",false);
		document.getElementById("notaFiscais_Delete").style.visibility = "hidden";
	}

	function changeRegistroPriorizacaoDocto(field) {
		registroPriorizacaoDoctoListboxDef.updateRelateds();

		var idDoctoServicoAux = getElementValue("registroPriorizacaoDocto_doctoServico.idDoctoServico");
		var nrDoctoServicoAux = getElementValue("registroPriorizacaoDocto_doctoServico.nrDoctoServico");
		var idFilialAux = getElementValue("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial");
		var sgFilialAux = getElementValue("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.sgFilial");
		var nmFantasiaAux = getElementValue("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia");
		var tpDocumentoServicoAux = getElementValue("registroPriorizacaoDocto_doctoServico.tpDocumentoServico");

		getElement("registroPriorizacaoDocto_doctoServico.tpDocumentoServico").onchange();

		setElementValue("registroPriorizacaoDocto_doctoServico.idDoctoServico",idDoctoServicoAux);
 		setElementValue("registroPriorizacaoDocto_doctoServico.nrDoctoServico",nrDoctoServicoAux);
 		setElementValue("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial",idFilialAux);
 		setElementValue("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.sgFilial",sgFilialAux);
 		setElementValue("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia",nmFantasiaAux);
 		setElementValue("registroPriorizacaoDocto_doctoServico.tpDocumentoServico",tpDocumentoServicoAux);

		enableRegistroPriorizacaoDocto();
		return true;
	}
	
	function enableRegistroPriorizacaoDocto() {
		var eTpDocumentoServico = document.getElementById("registroPriorizacaoDocto_doctoServico.tpDocumentoServico");
		if (eTpDocumentoServico != undefined && eTpDocumentoServico.disabled != undefined && eTpDocumentoServico.disabled == false) {
			//setDisabled("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial", false);
			setDisabled("registroPriorizacaoDocto_doctoServico.idDoctoServico", false);
		}
	}
	
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.sim.registrarSolicitacoesEmbarqueAction.findDataSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}
	function contextChangeDoctoServico(eventObj) {
		if ("cleanButton_click" == eventObj.name) {
			//Indice inválido para forcar o onChange do componente
			document.getElementById("registroPriorizacaoDocto").selectedIndex = -1;
			setDisabled("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial", true);
			setDisabled("registroPriorizacaoDocto_doctoServico.idDoctoServico", true);
		} else if ("modifyButton_click" == eventObj.name) {
			
			if(document.getElementById("idDoctoServicoLocMerc").masterLink != "true"){
				if (getElementValue("registroPriorizacaoDocto_doctoServico.idDoctoServico") != "")
					addDoctoServico(true);
				return false;
			}else{
				if(getElementValue("registroPriorizacaoDocto_doctoServico.idDoctoServico")!= ""){	
					if(getElementValue("idDoctoServicoLocMerc") == getElementValue("registroPriorizacaoDocto_doctoServico.idDoctoServico"))
					  return false;
					 else
				     	return true;
				}else{
					return false;
				}
			}
		} else if("deleteButton_click" == eventObj.name){
			if(document.getElementById("idDoctoServicoLocMerc").masterLink == "true"){
				if(getElementValue("idDoctoServicoLocMerc") == getElementValue("registroPriorizacaoDocto_doctoServico.idDoctoServico"))
					return false;
			}else
				return true;
		} else if ("deleteButton_afterClick" == eventObj.name) {
				addNotasFiscais();
				if(getElementValue("registroPriorizacaoDocto_doctoServico.idDoctoServico")== null){
					setDisabled("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial",true);
					setDisabled("registroPriorizacaoDocto_doctoServico.idDoctoServico",true);
				}
				return true;
		}
	}
	function contextChangeNotaFiscal(eventObj) {
		if ("modifyButton_click" == eventObj.name && getElementValue("notaFiscal.idFilialOrigem") != "") {
			var nrDocumento = document.getElementById("registroPriorizacaoDocto_doctoServico.nrDoctoServico");
			setElementValue("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial",getElementValue("notaFiscal.idFilialOrigem"));
			setElementValue("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.sgFilial",getElementValue("notaFiscal.sgFilialOrigem"));
			setElementValue("registroPriorizacaoDocto_doctoServico.idDoctoServico",getElementValue("notaFiscal.idDoctoServico"));
			setElementValue(nrDocumento,setFormat(nrDocumento,getElementValue("notaFiscal.nrConhecimento")));
			setElementValue("registroPriorizacaoDocto_doctoServico.tpDocumentoServico",getElementValue("notaFiscal.tpDocumentoServico.value"));
			notaFiscaisListboxDef.cleanRelateds();
			addDoctoServico(false);
			return false;
		}
		if ("deleteButton_click" == eventObj.name)
			return false;
	}
	
	function addDoctoServico(isClicked) {
		var index = document.getElementById("registroPriorizacaoDocto").selectedIndex;
		
		var sdo = createServiceDataObject("lms.sim.registrarSolicitacoesEmbarqueAction.validateAddDoctoServico","addDoctoServico",{idDoctoServico:getElementValue("registroPriorizacaoDocto_doctoServico.idDoctoServico"),id:getElementValue("idRegistroPriorizacaoEmbarq")});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function limpaValoresDocumentoServico(){
		resetValue("registroPriorizacaoDocto_doctoServico.idDoctoServico");
		resetValue("registroPriorizacaoDocto_doctoServico.tpDocumentoServico");
		setDisabled("registroPriorizacaoDocto_doctoServico.idDoctoServico", true);
		resetValue("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial");
		setDisabled("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial", true);
	}
	
	function addDoctoServico_cb(data,execption) {
		if (execption != undefined) {
			alert(execption);		
			limpaValoresDocumentoServico();
			return;
		}
		
		var dataListBox = registroPriorizacaoDoctoListboxDef.getData();
		var idDoctoServico = getNestedBeanPropertyValue(data,"idDocto");
		if (dataListBox != undefined && dataListBox.length > 0) {
			for(var x = 0; x < dataListBox.length; x++) {
				if (getNestedBeanPropertyValue(dataListBox[x],"doctoServico.idDoctoServico") == idDoctoServico) {
					alert(i18NLabel.getLabel("LMS-10031"));
					return;	
				}
			}
		}

		registroPriorizacaoDoctoListboxDef.cleanRelateds();
		setDisabled("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial",true);
		setDisabled("registroPriorizacaoDocto_doctoServico.idDoctoServico",true);
		
		var nrDocumento = document.getElementById("registroPriorizacaoDocto_doctoServico.nrDoctoServico");
		setElementValue("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial",getNestedBeanPropertyValue(data,"idFilial"));
		setElementValue("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.sgFilial",getNestedBeanPropertyValue(data,"sgFilial"));
		setElementValue("registroPriorizacaoDocto_doctoServico.idDoctoServico",getNestedBeanPropertyValue(data,"idDocto"));
		setElementValue(nrDocumento,setFormat(nrDocumento,getNestedBeanPropertyValue(data,"nrDocto")));
		setElementValue("registroPriorizacaoDocto_doctoServico.tpDocumentoServico",getNestedBeanPropertyValue(data,"tpDocto"));

		registroPriorizacaoDoctoListboxDef.onContentChange = function(e) {
																return true;
															 };
		registroPriorizacaoDoctoListboxDef.insertOrUpdateOption();
		registroPriorizacaoDoctoListboxDef.onContentChange = Listbox_onContentChange;
		addNotasFiscais();
	}
	function addNotasFiscais() {
		var sdo = createServiceDataObject("lms.sim.registrarSolicitacoesEmbarqueAction.findNotasFiscais","addNotasFiscais",buildFormBeanFromForm(document.Lazy));
		xmit({serviceDataObjects:[sdo]});
	}
	
	function addNotasFiscais_cb(data) {
		if (data != undefined)
			fillFormWithFormBeanData(document.Lazy.tabIndex, {notaFiscais:data});
		
	}
	
	function remetente_cb(data) {
		var flag = remetente_pessoa_nrIdentificacao_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			removeDoctos();
		return flag;
	}

	function destinatario_cb(data) {
		
		var flag = destinatario_pessoa_nrIdentificacao_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			removeDoctos();
		return flag;
	}

	function remetente_popUp(data) {
		removeDoctos();
		return true;
	}
	function destinatario_popUp(data) {
		removeDoctos();
		return true;
	}
	
	function removeDoctos() {
		var obj = document.getElementById("registroPriorizacaoDocto");
		for(i=obj.options.length; i>=0; i--) {
			obj.options.remove(i);
		}
		
		obj = document.getElementById("notaFiscais");
		for(i=obj.options.length; i>=0; i--) {
			obj.options.remove(i);
		}
		
		setElementValue("registroPriorizacaoDocto_doctoServico.tpDocumentoServico","");
		setElementValue("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.sgFilial","");
		setElementValue("registroPriorizacaoDocto_doctoServico.nrDoctoServico","");
		setElementValue("registroPriorizacaoDocto_doctoServico.idDoctoServico","");
		
		
		comboboxChange({e:document.getElementById("registroPriorizacaoDocto_doctoServico.tpDocumentoServico")});
		setDisabled("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial",true);
		setDisabled("registroPriorizacaoDocto_doctoServico.idDoctoServico",true);
	}

	function dataLoad_cb(data) {
		onDataLoad_cb(data);
		addNotasFiscais();
		if (getNestedBeanPropertyValue(data,"dhCancelamento") != undefined)
			behaviorCanceled();
		else
			behaviorNotCanceled();
		
		var tab = getTab(document);
		tab.setChanged(false);
	}
	
	function behaviorCanceled() {
		setDisabled(document,true);
		setDisabled("newButton",false);
		setFocus("newButton",false);
		var tab = getTab(document);
		tab.setChanged(false);
	}
	function behaviorNotCanceled() {
		//setDisabled(document,false);
		setDisabled("dhCancelamento",true);
		setDisabled("usuarioCancelamento.idUsuario",true);
		setDisabled("usuarioCancelamento.nmUsuario",true);
		setDisabled("usuarioCriacao.idUsuario",true);
		setDisabled("usuarioCriacao.nmUsuario",true);
		setDisabled("dhRegistro",true);
		setDisabled("filial.pessoa.nmfantasia",true);
		setDisabled("remetente.pessoa.nmPessoa",true);
		setDisabled("destinatario.pessoa.nmPessoa",true);
		setDisabled("registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.idFilial",true);
		setDisabled("registroPriorizacaoDocto_doctoServico.idDoctoServico",true);

		setFocusOnFirstFocusableField();
	}
	
	function cancelaRecibo() {
		strCallBack = "cancelaRecibo";
		strService = "lms.sim.registrarSolicitacoesEmbarqueAction.cancelaRegistro";
		var storeSDO = createServiceDataObject(strService, strCallBack, buildFormBeanFromForm(document.Lazy));
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false};
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
	}
	
	function cancelaRecibo_cb(data,exception) {
		if (exception != undefined)
			alert(exception);
		else
			behaviorCanceled();
			
		if (data != undefined)
			fillFormWithFormBeanData(document.Lazy.tabIndex, data);
	} 
	/*function storeCustom_cb(data,exception,key) {
		if (exception != undefined) {
			//REGRA CANCELADA
			if (key == "LMS-10035") {
				if (confirm(exception)) {
					setElementValue("confirmation","true");
					storeButtonScript('lms.sim.registrarSolicitacoesEmbarqueAction.store', 'storeCustom', document.Lazy)
					setElementValue("confirmation","");
				}
			}else
				alert(exception);
		} else {
			store_cb(data);
		}
	}*/
	
	function remetenteOnChange(){
		removeDoctos();
		return remetente_pessoa_nrIdentificacaoOnChangeHandler();
	}
	
	function destinatarioOnChange(){
		removeDoctos();
		return destinatario_pessoa_nrIdentificacaoOnChangeHandler();
	}
	
//-->
</script>
<adsm:window service="lms.sim.registrarSolicitacoesEmbarqueAction" onPageLoadCallBack="pageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-10031"/>
	</adsm:i18nLabels>
	<adsm:form action="/sim/registrarSolicitacoesEmbarque" idProperty="idRegistroPriorizacaoEmbarq" onDataLoadCallBack="dataLoad" id="Lazy">
				
		<adsm:hidden property="idDoctoServicoLocMerc"/>
		<adsm:hidden property="tpDocumentoServicoLocMerc"/>
		<adsm:hidden property="nrDoctoServicoLocMerc"/>
		<adsm:hidden property="sgFilialDoctoServLocMerc"/>
		<adsm:hidden property="idFilialDoctoServLocMerc"/>
		
		<adsm:lookup label="filialSolicitante" dataType="text" size="3" maxLength="3" width="32%" property="filial"
				     service="lms.sim.registrarSolicitacoesEmbarqueAction.findLookupFilial" action="municipios/manterFiliais"
				      idProperty="idFilial" criteriaProperty="sgFilial" required="true" labelWidth="22%" disabled="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/> 
		</adsm:lookup>
		<adsm:hidden property="confirmation"/>
		
		<adsm:hidden property="cliente.tpSituacao" value="A" serializable="false"/>
		
		<adsm:lookup dataType="text" property="remetente" idProperty="idCliente" width="78%" action="/vendas/manterDadosIdentificacao" onDataLoadCallBack="remetente"
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" maxLength="20" labelWidth="22%" onPopupSetValue="remetente_popUp"
				service="lms.sim.registrarSolicitacoesEmbarqueAction.findLookupCliente" label="remetente" size="17" exactMatch="true" onchange="return remetenteOnChange();">
			<adsm:propertyMapping criteriaProperty="cliente.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="remetente.pessoa.nmPessoa" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="destinatario" idProperty="idCliente" action="/vendas/manterDadosIdentificacao" width="78%" onDataLoadCallBack="destinatario"
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" maxLength="20" labelWidth="22%"
				service="lms.sim.registrarSolicitacoesEmbarqueAction.findLookupCliente" label="destinatario" size="17" exactMatch="true" onPopupSetValue="destinatario_popUp" onchange="return destinatarioOnChange();">
			<adsm:propertyMapping criteriaProperty="cliente.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="destinatario.pessoa.nmPessoa" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:textbox dataType="text" property="nmSolicitante" size="27" width="78%" label="nomeSolicitante" labelWidth="22%" required="true" maxLength="60"/>

		<adsm:listbox
			size="6"
					  labelWidth="22%"
					  property="registroPriorizacaoDocto"
					  optionProperty="idRegistroPriorizacaoDocto"
					  optionLabelProperty="doctoServico.nmDoctoServico"
					  width="35%" 
					  showOrderControls="false" 
					  boxWidth="185"
					  showIndex="false"
					  serializable="true"
					  required="true"
					  onContentChange="contextChangeDoctoServico"
					  label="documentoServico"
			onchange="return changeRegistroPriorizacaoDocto(this);">

			<adsm:combobox
				property="doctoServico.tpDocumentoServico"
				optionLabelProperty="description"
				service="lms.sim.registrarSolicitacoesEmbarqueAction.findTipoDocumentoServico"
				optionProperty="value"
					   onchange="return changeComboDocumentoServico(this);">

				<adsm:lookup 
					action="/expedicao/pesquisarConhecimento"
					dataType="text" property="doctoServico.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
					service="" disabled="true" size="3" maxLength="3" picker="false"  popupLabel="pesquisarFilial"
						 onchange="return changeFilialDoctoServico();">
					<adsm:propertyMapping 
						relatedProperty="registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" 
						modelProperty="pessoa.nmFantasia"/>
				</adsm:lookup>
						 <adsm:hidden property="registroPriorizacaoDocto_doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
			
			<adsm:lookup dataType="integer" property="doctoServico" idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" 
						 service="" action="" size="12" maxLength="8" mask="00000000" serializable="true" disabled="true" popupLabel="pesquisarDocumentoServico"/>
			<adsm:hidden property="idDoctoServico"/>
		</adsm:combobox>
	</adsm:listbox>

		<adsm:listbox size="9"
					  property="notaFiscais"
					  optionProperty="idNotaFiscais"
					  optionLabelProperty="nmNotaFiscais"
					  width="27%" 
					  showOrderControls="false" 
					  boxWidth="120"
					  showIndex="false"
					  serializable="true"
					  onContentChange="contextChangeNotaFiscal"					  
					  label="notaFiscal">

       	<adsm:lookup action="/expedicao/consultarNotaFiscalCliente" mask="000000000" maxLength="9" 
					 service="lms.entrega.registrarEntregasParceirosAction.findLookupConhecimento" criteriaProperty="nrNotaFiscal" 
					 dataType="integer" property="notaFiscalConhecimento" popupLabel="pesquisarNotaFiscal"
					 idProperty="idDoctoServico" exactMatch="false" minLengthForAutoPopUpSearch="3">

				<adsm:propertyMapping modelProperty="destinatario.idCliente" criteriaProperty="destinatario.idCliente"/>
				<adsm:propertyMapping modelProperty="remetente.idCliente"    criteriaProperty="remetente.idCliente"/>
				<adsm:propertyMapping modelProperty="destinatario.pessoa.nrIdentificacao" criteriaProperty="destinatario.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="remetente.pessoa.nrIdentificacao" criteriaProperty="remetente.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="destinatario.pessoa.nmPessoa" criteriaProperty="destinatario.pessoa.nmPessoa" inlineQuery="false"/>
				<adsm:propertyMapping modelProperty="remetente.pessoa.nmPessoa" criteriaProperty="remetente.pessoa.nmPessoa" inlineQuery="false"/>


				<adsm:propertyMapping modelProperty="tpDocumentoServico.value" relatedProperty="notaFiscal.tpDocumentoServico.value"/>
				<adsm:propertyMapping modelProperty="nrDoctoServico" relatedProperty="notaFiscal.nrConhecimento"/>
				<adsm:propertyMapping modelProperty="idDoctoServico" relatedProperty="notaFiscal.idDoctoServico"/>
				<adsm:propertyMapping modelProperty="idFilialOrigem" relatedProperty="notaFiscal.idFilialOrigem"/>
				<adsm:propertyMapping modelProperty="sgFilialOrigem" relatedProperty="notaFiscal.sgFilialOrigem"/>
				<adsm:hidden property="notaFiscal.tpDocumentoServico.value" serializable="false"/>
				<adsm:hidden property="notaFiscal.nrConhecimento" serializable="false"/>
				<adsm:hidden property="notaFiscal.idDoctoServico" serializable="false"/>
				<adsm:hidden property="notaFiscal.idFilialOrigem" serializable="false"/>
				<adsm:hidden property="notaFiscal.sgFilialOrigem" serializable="false"/>
				
			</adsm:lookup>
						 
		</adsm:listbox>
			  

		<adsm:textarea label="observacao" property="obPriorizacao" maxLength="600" columns="85" labelWidth="22%" width="78%" required="true"/>
		
		<adsm:textbox dataType="JTDateTimeZone" property="dhRegistroEmbarque"  width="78%" label="dataHoraLimiteEmbarque" labelWidth="22%" required="true"/>
		<adsm:textbox dataType="JTDateTimeZone" property="dhRegistro"  width="28%" label="dataHoraRegistro" labelWidth="22%" required="true" disabled="true"/>
		
		<adsm:textbox dataType="text" property="usuarioCriacao.nmUsuario" size="19" width="28%" label="usuario" required="true" labelWidth="22%" disabled="true"/>
		<adsm:hidden property="usuarioCriacao.idUsuario"/>
		<adsm:section caption="informacoesCancelamento"/>
		
		<adsm:textbox dataType="JTDateTimeZone" property="dhCancelamento"  width="78%" label="dataHoraCancelamento" labelWidth="22%" disabled="true"/>
		<adsm:textbox dataType="text" property="usuarioCancelamento.nmUsuario" size="19" width="28%" label="usuario" labelWidth="22%" disabled="true"/>
		<adsm:hidden property="usuarioCancelamento.idUsuario"/>
		<adsm:textarea label="observacao" property="obCancelamanto" maxLength="600" columns="85" labelWidth="22%" width="78%"/>

		<adsm:buttonBar>
			<adsm:button caption="rotasViagem" action="municipios/consultarRotas" cmd="main" id="rotaViagem"/>
			<adsm:button caption="rotasColetaEntrega" action="municipios/manterRotaColetaEntrega" cmd="main" id="rotaColeta" />
			<adsm:button id="cancel" caption="cancelar" onclick="cancelaRecibo();"/>
			<adsm:storeButton id="storeButtom"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
