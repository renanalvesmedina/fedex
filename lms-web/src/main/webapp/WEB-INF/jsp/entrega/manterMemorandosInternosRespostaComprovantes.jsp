<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.manterMemorandosInternosRespostaAction" >
	<adsm:form action="/entrega/manterMemorandosInternosResposta" id="form_comp" idProperty="idDocumentoMir"
			service="lms.entrega.manterMemorandosInternosRespostaAction.findByIdComp"
			onDataLoadCallBack="compDataLoad" >
		<adsm:masterLink idProperty="idMir" >
			<adsm:masterLinkItem property="filialByIdFilialOrigem.siglaNomeFilial" label="filialOrigem" itemWidth="50" boxWidth="200" />
			<adsm:masterLinkItem property="filialByIdFilialDestino.siglaNomeFilial" label="filialDestino" itemWidth="50" boxWidth="200" />
			<adsm:masterLinkItem property="nrMir" label="numeroMIR" itemWidth="50" boxWidth="200" />
			<adsm:masterLinkItem property="tpMir.description" label="tipoMir" itemWidth="50" boxWidth="200" />
		</adsm:masterLink>
		<adsm:hidden property="versao" />
		
		<adsm:hidden property="idFilialOrigem" />
		<adsm:hidden property="sgFilialOrigem" />
		<adsm:hidden property="nmFilialOrigem" />
		
		<adsm:hidden property="idFilialDestino" />
		<adsm:hidden property="sgFilialDestino" />
		<adsm:hidden property="nmFilialDestino" />
		
		<adsm:hidden property="tpMir.value" />
				
		<adsm:hidden property="registroDocumentoEntrega.idRegistroDocumentoEntrega" />
		<adsm:hidden property="nrDoctoServicoHidden" />
		<adsm:hidden property="tpDoctoServicoHidden" />
		<adsm:hidden property="idDoctoServico" />
		
		
		<adsm:combobox property="registroDocumentoEntrega.doctoServico.tpDocumentoServico"
				label="documentoServico" labelWidth="16%" width="84%"
				service="lms.entrega.manterMemorandosInternosRespostaAction.findTipoDocumentoServico"
				optionProperty="value" optionLabelProperty="description"
				onchange="return changeTpDoctoServico(this);" > 

			<adsm:lookup dataType="text"
					property="registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial"
					service="" action="" disabled="true"
					exactMatch="true"
					size="3" maxLength="3" picker="false" onDataLoadCallBack="enableRegistroDocumentoEntregaDoctoServico"
					onchange="return changeFilialDoctoServico();" />
			
			<adsm:lookup dataType="integer"
					property="registroDocumentoEntrega.doctoServico" idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
					service="" action=""
					onDataLoadCallBack="documentoServicoDataLoad" onPopupSetValue="documentoServicoPopup" 
					onchange="return onChangeNrDocumentoServico();" popupLabel="pesquisarDocumentoServico"
					size="10" maxLength="8" serializable="true" required="true" disabled="true" mask="00000000" />
		</adsm:combobox>
		<adsm:hidden property="registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.idCliente" serializable="false" />
		<adsm:hidden property="registroDocumentoEntrega.doctoServico.clienteByIdClienteConsignatario.idCliente" serializable="false" />
		<adsm:hidden property="registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.idCliente" serializable="false" />
		<adsm:hidden property="registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" />


		<adsm:textbox dataType="text"
				property="registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado"
				label="remetente" size="18" labelWidth="16%" width="34%"
				disabled="true" serializable="false">
			<adsm:textbox dataType="text" 
					property="registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" 
					size="22" disabled="true" />
			<adsm:hidden property="registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.tpIdentificacao.value" />
			<adsm:hidden property="registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacao" />
		</adsm:textbox>
	
		<adsm:textbox dataType="text"
				property="registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado"
				label="destinatario" size="18" labelWidth="16%" width="34%"
				disabled="true" serializable="false">
			<adsm:textbox dataType="text" 
					property="registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" 
					size="22" disabled="true" />
			<adsm:hidden property="registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.tpIdentificacao.value" />
			<adsm:hidden property="registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao" />
		</adsm:textbox>
		
		<adsm:hidden property="registroDocumentoEntrega.tipoDocumentoEntrega.idTipoDocumentoEntrega" />
		<adsm:textbox dataType="text" property="registroDocumentoEntrega.tipoDocumentoEntrega.dsTipoDocumentoEntrega"
				label="tipoComprovante" size="40" labelWidth="16%" width="34%" disabled="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="comprovantesPendentes" id="btnPendentes"
					onclick="showModalDialog('entrega/manterMemorandosInternosResposta.do?cmd=comprovantesPendentes',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:510px;');" />
			<adsm:storeButton caption="salvarDocumento"
					service="lms.entrega.manterMemorandosInternosRespostaAction.saveComp" />
			<adsm:newButton /> 
		</adsm:buttonBar>
		
	</adsm:form>
	<adsm:grid property="documentoMir" idProperty="idDocumentoMir" 
			service="lms.entrega.manterMemorandosInternosRespostaAction.findPaginatedComp"
			rowCountService="lms.entrega.manterMemorandosInternosRespostaAction.getRowCountComp"
			detailFrameName="comp" rows="7" scrollBars="horizontal" gridHeight="152">
	
		<adsm:gridColumn title="documentoServico" 
					property="registroDocumentoEntrega.doctoServico.tpDocumentoServico" width="40"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" 
					property="registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.sgFilial" width="60" />
			<adsm:gridColumn title="" 
					property="registroDocumentoEntrega.doctoServico.nrDoctoServico" 
					width="60" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup> 
		
		<adsm:gridColumn title="identificacao" 
				property="registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.tpIdentificacao" 
				width="50" />
		<adsm:gridColumn title="" 
				property="registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado" 
				align="right" width="120"/>
		<adsm:gridColumn title="remetente" 
				property="registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" 
				width="140" />
		
		<adsm:gridColumn title="identificacao"
				property="registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.tpIdentificacao" 
				width="50" />
		<adsm:gridColumn title="" 
				property="registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado" 
				align="right" width="120"/>
		<adsm:gridColumn title="destinatario" 
				property="registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" 
				width="140" />

		<adsm:gridColumn width="150" title="tipoComprovante" property="registroDocumentoEntrega.tipoDocumentoEntrega.dsTipoDocumentoEntrega" />
		
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirDocumento" id="btnExcluir"
					service="lms.entrega.manterMemorandosInternosRespostaAction.removeByIdsComp" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	document.getElementById('registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.sgFilial').serializable = true;
	
	document.getElementById("idFilialOrigem").masterLink = true;
	document.getElementById("sgFilialOrigem").masterLink = true;
	document.getElementById("nmFilialOrigem").masterLink = true;
	
	document.getElementById("idFilialDestino").masterLink = true;
	document.getElementById("sgFilialDestino").masterLink = true;
	document.getElementById("nmFilialDestino").masterLink = true;
	
	document.getElementById("tpMir.value").masterLink = true;
		
	function initWindow(eventObj) {
		var blBloqueiaFilhos = getElementValueFromCad("blBloqueiaFilhos");
		if (blBloqueiaFilhos == "true") {
			setDisabled(document,true);
		} else {
			setDisabled(document,false);
			//setDisabled("registroDocumentoEntrega.doctoServico.idDoctoServico",true);
			//setDisabled("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.sgFilial",true);
			setDisabledRelatedPropertiesDoctoServico(true);
			setDisabled("btnExcluir",true);
			setEstadoFilialAcordoTpMir();
			setDisabled("registroDocumentoEntrega.doctoServico.idDoctoServico",true);
		}
	}
	
	function enableRegistroDocumentoEntregaDoctoServico_cb(data) {
	   var blRetorno = registroDocumentoEntrega_doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (blRetorno) {
	      setDisabled("registroDocumentoEntrega.doctoServico.idDoctoServico", false);
	      setFocus(document.getElementById("registroDocumentoEntrega.doctoServico.nrDoctoServico"));
	   }
	   return blRetorno;
	}
	
	
	/**
	 * Quando o "Número do documento" for informado 
	 */
	function documentoServicoDataLoad_cb(data) {
		registroDocumentoEntrega_doctoServico_nrDoctoServico_exactMatch_cb(data);
		
		if (data != undefined && data.length > 0) {
			var idDoctoServico = getNestedBeanPropertyValue(data,":0.idDoctoServico");
			validateDoctoServico(idDoctoServico);
		}
	}
	
	function documentoServicoPopup(data) {
		if (data != undefined) {
			var idDoctoServico = getNestedBeanPropertyValue(data,":idDoctoServico");
			validateDoctoServico(idDoctoServico);
		}
		setDisabled("registroDocumentoEntrega.doctoServico.nrDoctoServico", false);
	}
	
	function validateDoctoServico(idDoctoServico) {
		var data = new Array();
		setNestedBeanPropertyValue(data,'idDoctoServico',idDoctoServico);
		setNestedBeanPropertyValue(data,'idFilialOrigem',getElementValue("idFilialOrigem"));
		setNestedBeanPropertyValue(data,'idFilialDestino',getElementValue("idFilialDestino"));
		setNestedBeanPropertyValue(data,'tpMir',getElementValue("tpMir.value"));
		setNestedBeanPropertyValue(data,'idMir',getElementValueFromCad("idMir"));
		setNestedBeanPropertyValue(data,'idDocumentoMir',getElementValue("idDocumentoMir"));
	
		var sdo = createServiceDataObject("lms.entrega.manterMemorandosInternosRespostaAction.validateDoctoServicoInComprovantes",
				"validateDoctoServicoInComprovantes",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validateDoctoServicoInComprovantes_cb(data,error) {
		if (error != undefined) {
			alert(error);
			resetValue("registroDocumentoEntrega.doctoServico.idDoctoServico");
			return false;
		}
		
		onDataLoad_cb(data,error);
	}
	
	function setDisabledRelatedPropertiesDoctoServico() {
		setDisabled("registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado",true);
		setDisabled("registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa",true);
		setDisabled("registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado",true);
		setDisabled("registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa",true);
		setDisabled("registroDocumentoEntrega.tipoDocumentoEntrega.dsTipoDocumentoEntrega",true);
	}
	
	function resetRelatedPropertiesDoctoServico() {
		resetValue("registroDocumentoEntrega.idRegistroDocumentoEntrega");
		resetValue("registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado");
		resetValue("registroDocumentoEntrega.doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa");
		resetValue("registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado");
		resetValue("registroDocumentoEntrega.doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa");
		resetValue("registroDocumentoEntrega.tipoDocumentoEntrega.dsTipoDocumentoEntrega");
	}	
			
	function onChangetpDocumentoServico() {
		var comboTpDocumentoServico = document.getElementById("registroDocumentoEntrega.doctoServico.tpDocumentoServico");
		setElementValue('tpDoctoServicoHidden',
				comboTpDocumentoServico.options[comboTpDocumentoServico.selectedIndex].text);
		
		resetRelatedPropertiesDoctoServico();
	}
	
	function onChangeFilialDocumentoServico() {
		resetRelatedPropertiesDoctoServico();
		//return registroDocumentoEntrega_doctoServico_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}
	
	function onChangeNrDocumentoServico() {
		resetRelatedPropertiesDoctoServico();
		return registroDocumentoEntrega_doctoServico_nrDoctoServicoOnChangeHandler();
	}
	
	function tabShowCustom() {
		setElementValue("idFilialOrigem",getElementValueFromCad("filialByIdFilialOrigem.idFilial"));
		setElementValue("sgFilialOrigem",getElementValueFromCad("filialByIdFilialOrigem.sgFilial"));
		setElementValue("nmFilialOrigem",getElementValueFromCad("filialByIdFilialOrigem.pessoa.nmFantasia"));
	
		setElementValue("idFilialDestino",getElementValueFromCad("filialByIdFilialDestino.idFilial"));
		setElementValue("sgFilialDestino",getElementValueFromCad("filialByIdFilialDestino.sgFilial"));
		setElementValue("nmFilialDestino",getElementValueFromCad("filialByIdFilialDestino.pessoa.nmFantasia"));
		
		var tpMir = getElementValueFromCad("tpMir");
		setElementValue("tpMir.value",tpMir);
		
		newButtonScript(this.document, true, {name:'newItemButton_click'});
		setDisabled("registroDocumentoEntrega.doctoServico.idDoctoServico",true);
	}
	
	function setEstadoFilialAcordoTpMir(isDetalhamento) {
		var tpMir = getElementValueFromCad("tpMir");
		
		var idFilialDestino = getElementValueFromCad("filialByIdFilialDestino.idFilial");
		setElementValue("idFilialDestino",idFilialDestino);

		if (tpMir == "EA") {
			if (isDetalhamento == undefined || isDetalhamento != "true") {
				setElementValue("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.idFilial","");
				setElementValue("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.sgFilial","");
				setElementValue("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia","");
			}
			
			document.getElementById("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.idFilial").masterLink = false;
			setDisabled("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.sgFilial",
					getElementValue("registroDocumentoEntrega.doctoServico.tpDocumentoServico") == "");
			
			setDisabled("registroDocumentoEntrega.doctoServico.nrDoctoServico",
					getElementValue("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.idFilial") == "");
		} 
		
		else if (tpMir == "DO") {			
			setElementValue("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.idFilial",
					getElementValueFromCad("filialByIdFilialDestino.idFilial"));
			setElementValue("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.sgFilial",
					getElementValueFromCad("filialByIdFilialDestino.sgFilial"));
			setElementValue("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia",
					getElementValueFromCad("filialByIdFilialDestino.pessoa.nmFantasia"));
					
			document.getElementById("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.idFilial").masterLink = true;
			setDisabled("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.sgFilial",true);
			
			setDisabled("registroDocumentoEntrega.doctoServico.nrDoctoServico",false);
		} 
		
		else if (tpMir == "AE") {			
			setElementValue("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.idFilial",
					getElementValueFromCad("filialByIdFilialOrigem.idFilial"));
			setElementValue("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.sgFilial",
					getElementValueFromCad("filialByIdFilialOrigem.sgFilial"));
			setElementValue("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia",
					getElementValueFromCad("filialByIdFilialOrigem.pessoa.nmFantasia"));
					
			document.getElementById("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.idFilial").masterLink = true;
			setDisabled("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.sgFilial",true);
			
			setDisabled("registroDocumentoEntrega.doctoServico.nrDoctoServico",false);
		}
	}
	
	// Como é chamado o método changeTpDoctoServico que realiza a chamada de outro método
	// que é o setEstadoFilialAcordoTpMir, temos que criar uma flag que indicará ao método que o mesmo
	// foi chamado no DETALHAMENTO. Muito cuidado com isto. É uma má prática, mas foi necessário para não
	// mexer em toda estrutura.
	var blDetalhamentoGlobal = "false";
	function compDataLoad_cb(data,error) {
		setElementValue("registroDocumentoEntrega.doctoServico.tpDocumentoServico",
				getNestedBeanPropertyValue(data,"registroDocumentoEntrega.doctoServico.tpDocumentoServico.value"));
		blDetalhamentoGlobal = "true";
		changeTpDoctoServico(document.getElementById("registroDocumentoEntrega.doctoServico.tpDocumentoServico"));
		onDataLoad_cb(data,error);
		
		var blBloqueiaFilhos = getElementValueFromCad("blBloqueiaFilhos");
		if (blBloqueiaFilhos == "true")
			setDisabled(document,true);
		else {
			setDisabled(document,false);
			//setDisabled("registroDocumentoEntrega.doctoServico.idDoctoServico",false);
			//setDisabled("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.sgFilial",false);
			setDisabledRelatedPropertiesDoctoServico();
			setDisabled("btnExcluir",true);
		}
		
		setElementValue("nrDoctoServicoHidden",getNestedBeanPropertyValue(data,"registroDocumentoEntrega.doctoServico.nrDoctoServico"));
		setElementValue("tpDoctoServicoHidden",
				getNestedBeanPropertyValue(data,"registroDocumentoEntrega.doctoServico.tpDocumentoServico.description"));
	}
	
	
	function getElementValueFromCad(property) {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		return tabDet.getFormProperty(property);
	}
	
	
	function changeTpDoctoServico(field) {
		resetValue('idDoctoServico');
		onChangetpDocumentoServico();
		
		var args = {documentTypeElement:field,
					filialElement:document.getElementById('registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.idFilial'),
					documentNumberElement:document.getElementById('registroDocumentoEntrega.doctoServico.idDoctoServico'),
					parentQualifier:'registroDocumentoEntrega.doctoServico',
					documentGroup:'DOCTOSERVICE',
					actionService:'lms.entrega.manterMemorandosInternosRespostaAction'
					};
		var flag = changeDocumentWidgetType(args);
			
		if (field.value != '') {		
			changeDocumentWidgetFilial({
					filialElement:document.getElementById('registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.idFilial'),
					documentNumberElement:document.getElementById('registroDocumentoEntrega.doctoServico.idDoctoServico')
			});
		}
		
		setaPropertysMappingNrDocumento();
				
		document.getElementById("registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.idFilial").service =
				"lms.entrega.manterMemorandosInternosRespostaAction.findLookupFilial";
			
		if (field.value == "") {	
			setDisabled("registroDocumentoEntrega.doctoServico.idDoctoServico",true);
		}
		
		return flag;
	}
	
	/**
	*	Setter manual das properties mappings adicionais aos documentos de serviço
	*/
	function setaPropertysMappingNrDocumento() {
		var properties = document.getElementById("registroDocumentoEntrega.doctoServico.idDoctoServico").propertyMappings;
		// id do Documento de servico
		properties[properties.length] = {modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico"};

		var tpMir = getElementValueFromCad("tpMir");
		if (tpMir == "EA" || tpMir == "DO") {
			//Filial Destino
			properties[properties.length] = {modelProperty:"filialByIdFilialDestino.idFilial",
					criteriaProperty:"idFilialOrigem", inlineQuery:true};
			properties[properties.length] = {modelProperty:"filialByIdFilialDestino.sgFilial",
					criteriaProperty:"sgFilialOrigem", inlineQuery:false};
			properties[properties.length] = {modelProperty:"filialByIdFilialDestino.pessoa.nmFantasia",
					criteriaProperty:"nmFilialOrigem", inlineQuery:false};
		}
		
		setEstadoFilialAcordoTpMir(blDetalhamentoGlobal);
		blDetalhamentoGlobal = "false";
	}
	
	function changeFilialDoctoServico() {
		resetValue('idDoctoServico');
		
		onChangeFilialDocumentoServico();
		
		return changeDocumentWidgetFilial({
					filialElement:document.getElementById('registroDocumentoEntrega.doctoServico.filialByIdFilialOrigem.idFilial'),
					documentNumberElement:document.getElementById('registroDocumentoEntrega.doctoServico.idDoctoServico')
			});
	}
	
</script>