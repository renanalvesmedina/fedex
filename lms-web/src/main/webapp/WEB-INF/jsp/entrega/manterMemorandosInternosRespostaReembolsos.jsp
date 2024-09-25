<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window onPageLoadCallBack="pageLoadCustom"
		service="lms.entrega.manterMemorandosInternosRespostaAction">
	<adsm:form action="/entrega/manterMemorandosInternosResposta" id="form_reem" idProperty="idDocumentoMir"
			service="lms.entrega.manterMemorandosInternosRespostaAction.findByIdReemb"
			onDataLoadCallBack="reemDataLoad" >
		<adsm:masterLink idProperty="idMir" >
			<adsm:masterLinkItem property="filialByIdFilialOrigem.siglaNomeFilial" label="filialOrigem" itemWidth="50" boxWidth="200" />
			<adsm:masterLinkItem property="filialByIdFilialDestino.siglaNomeFilial" label="filialDestino" itemWidth="50" boxWidth="200" />
			<adsm:masterLinkItem property="nrMir" label="numeroMIR" itemWidth="50" boxWidth="200" />
			<adsm:masterLinkItem property="tpMir.description" label="tipoMir" itemWidth="50" boxWidth="200" />
		</adsm:masterLink>
		<adsm:hidden property="versao" />
		<adsm:hidden property="idFilialOrigem" />
		<adsm:hidden property="idFilialDestino" />
		<adsm:hidden property="idFilialDestinoLookup" />
		<adsm:hidden property="sgFilialDestinoLookup" />
		<adsm:hidden property="nmFilialDestinoLookup" />
		<adsm:hidden property="tpMir.value" />
		
		<adsm:hidden property="reciboReembolso.tpDocumentoServico.value" />
		<adsm:hidden property="reciboReembolso.doctoServicoByIdDoctoServReembolsado.tpDocumentoServico.value" />

		
		<adsm:textbox dataType="text"
				property="reciboReembolso.tpDocumentoServico.description"
				label="numeroRecibo" labelWidth="16%" width="84%" size="5" disabled="true">
 
			<adsm:lookup dataType="text"
					property="reciboReembolso.filial"
					idProperty="idFilial" criteriaProperty="sgFilial"
					service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupFilial"
					action="/municipios/manterFiliais" size="3" maxLength="3"
					picker="false" onDataLoadCallBack="doctoServicoDataLoad"
					onPopupSetValue="filialDoctoServicoPopup"
					onchange="return onChangeFilialDocumentoServico(this);" >
				<adsm:propertyMapping relatedProperty="reciboReembolso.filial.pessoa.nmFantasia"
						modelProperty="pessoa.nmFantasia" />
			</adsm:lookup>

			<adsm:lookup dataType="integer" property="reciboReembolso"
					idProperty="idDoctoServico" criteriaProperty="nrReciboReembolso"
					service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupServiceDocumentNumberRRE" 
					action="/entrega/manterReembolsos" size="10"
					onDataLoadCallBack="nrReciboReembolsoDataLoad"
					onPopupSetValue="nrReciboReembolsoPopup"
					onchange="return onChangeNrDocumentoServico();"
					maxLength="8" serializable="true" mask="00000000" required="true">
				<adsm:propertyMapping criteriaProperty="reciboReembolso.filial.idFilial"
						modelProperty="filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping criteriaProperty="reciboReembolso.filial.sgFilial"
						modelProperty="filialByIdFilialOrigem.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="reciboReembolso.filial.pessoa.nmFantasia"
						modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" inlineQuery="false" />
				
				<adsm:propertyMapping criteriaProperty="idFilialDestinoLookup"
						modelProperty="filialDestino.idFilial" />
				<adsm:propertyMapping criteriaProperty="sgFilialDestinoLookup"
						modelProperty="filialDestino.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="nmFilialDestinoLookup"
						modelProperty="filialDestino.pessoa.nmFantasia" inlineQuery="false" />
				
				<adsm:propertyMapping relatedProperty="reciboReembolso.filial.idFilial"
						modelProperty="filialByIdFilialOrigem.idFilial" blankFill="false" />
				<adsm:propertyMapping relatedProperty="reciboReembolso.filial.sgFilial"
						modelProperty="filialByIdFilialOrigem.sgFilial" blankFill="false" />
				<adsm:propertyMapping relatedProperty="reciboReembolso.filial.pessoa.nmFantasia"
						modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
			</adsm:lookup>
			<adsm:hidden property="reciboReembolso.filial.pessoa.nmFantasia" />
		</adsm:textbox>


		<adsm:textbox dataType="text"
				property="reciboReembolso.doctoServicoByIdDoctoServReembolsado.tpDocumentoServico.description"
				label="documentoServico" labelWidth="16%" width="34%" size="5" disabled="true" >
			<adsm:textbox dataType="text"
					property="reciboReembolso.doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.sgFilial"
					size="3" disabled="true" />
			<adsm:textbox dataType="integer"
					property="reciboReembolso.doctoServicoByIdDoctoServReembolsado.nrDoctoServico"
					size="10" disabled="true" mask="00000000"/>
		</adsm:textbox>

		<adsm:textbox dataType="text" property="reciboReembolso.vlReembolsoCalculadoView"
				label="valorReembolso" size="20" labelWidth="16%" width="34%"
				style="text-align:right"
				disabled="true" serializable="true" />
		<adsm:hidden property="reciboReembolso.vlReembolso" />
    	<adsm:hidden property="reciboReembolso.vlAplicado" />
    	<adsm:hidden property="reciboReembolso.tpValorAtribuidoRecibo" />
    	<adsm:hidden property="reciboReembolso.vlReembolsoCalculado" />
    	<adsm:hidden property="reciboReembolso.dsSimbolo" />

		<adsm:textbox dataType="text"
				property="reciboReembolso.clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado"
				label="remetente" size="18" labelWidth="16%" width="34%"
				disabled="true" serializable="false">
			<adsm:textbox dataType="text" 
					property="reciboReembolso.clienteByIdClienteRemetente.pessoa.nmPessoa" 
					size="22" disabled="true" />
			<adsm:hidden property="reciboReembolso.clienteByIdClienteRemetente.pessoa.tpIdentificacao" />
			<adsm:hidden property="reciboReembolso.clienteByIdClienteRemetente.pessoa.nrIdentificacao" />
		</adsm:textbox>
	
		<adsm:textbox dataType="text"
				property="reciboReembolso.clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado"
				label="destinatario" size="18" labelWidth="16%" width="34%"
				disabled="true" serializable="false">
			<adsm:textbox dataType="text" 
					property="reciboReembolso.clienteByIdClienteDestinatario.pessoa.nmPessoa" 
					size="22" disabled="true" />
			<adsm:hidden property="reciboReembolso.clienteByIdClienteDestinatario.pessoa.tpIdentificacao" />
			<adsm:hidden property="reciboReembolso.clienteByIdClienteDestinatario.pessoa.nrIdentificacao" />
		</adsm:textbox>


		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="reembolsosPendentes" id="btnPendentes"
					onclick="showModalDialog('entrega/manterMemorandosInternosResposta.do?cmd=reembolsosPendentes',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:510px;');" />
			<adsm:storeButton caption="salvarDocumento" id="btnSalvar"
					service="lms.entrega.manterMemorandosInternosRespostaAction.saveReemb" />
			<adsm:newButton id="btnLimpar" />
		</adsm:buttonBar> 

	</adsm:form>
	<adsm:grid property="documentoMir" idProperty="idDocumentoMir"
			service="lms.entrega.manterMemorandosInternosRespostaAction.findPaginatedReemb"
			rowCountService="lms.entrega.manterMemorandosInternosRespostaAction.getRowCountReemb"
			detailFrameName="reem" rows="7" scrollBars="horizontal" gridHeight="152">
			
		<%--adsm:gridColumn title="documentoServico" 
					property="reciboReembolso.tpDocumentoServico" width="50" /--%>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">			
			<adsm:gridColumn title="numeroRecibo" property="reciboReembolso.filial.sgFilial" width="80" />
			<adsm:gridColumn title="" property="reciboReembolso.nrDoctoServico"
					width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup> 

		<adsm:gridColumn title="documentoServico"
					property="reciboReembolso.doctoServicoByIdDoctoServReembolsado.tpDocumentoServico" width="40" />
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="reciboReembolso.doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.sgFilial"
					width="60" />
			<adsm:gridColumn title="" property="reciboReembolso.doctoServicoByIdDoctoServReembolsado.nrDoctoServico"
					width="60" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="identificacao" property="reciboReembolso.clienteByIdClienteRemetente.pessoa.tpIdentificacao" 
				width="50" />
		<adsm:gridColumn title="" property="reciboReembolso.clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado" 
				align="right" width="120" />
		<adsm:gridColumn title="remetente" property="reciboReembolso.clienteByIdClienteRemetente.pessoa.nmPessoa" width="140" />

		<adsm:gridColumn title="identificacao" property="reciboReembolso.clienteByIdClienteDestinatario.pessoa.tpIdentificacao" 
				width="50" />
		<adsm:gridColumn title="" property="reciboReembolso.clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado" 
				align="right" width="120" />
		<adsm:gridColumn title="destinatario" property="reciboReembolso.clienteByIdClienteDestinatario.pessoa.nmPessoa" width="140" />

		<adsm:buttonBar>
			<adsm:removeButton caption="excluirDocumento" id="btnExcluir"
					service="lms.entrega.manterMemorandosInternosRespostaAction.removeByIdsReemb" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	
	document.getElementById("reciboReembolso.tpDocumentoServico.description").masterLink = true;
	document.getElementById("reciboReembolso.tpDocumentoServico.value").masterLink = true;
	document.getElementById('reciboReembolso.filial.sgFilial').serializable = true;
	document.getElementById('reciboReembolso.nrReciboReembolso').serializable = true;
	
	document.getElementById("idFilialOrigem").masterLink = true;
	document.getElementById("idFilialDestino").masterLink = true;
	document.getElementById("idFilialDestinoLookup").masterLink = true;
	document.getElementById("sgFilialDestinoLookup").masterLink = true;
	document.getElementById("nmFilialDestinoLookup").masterLink = true;
	document.getElementById("tpMir.value").masterLink = true;
	
	function initWindow(eventObj) {
		var blBloqueiaFilhos = getElementValueFromCad("blBloqueiaFilhos");
		if (blBloqueiaFilhos == "true") {
			setDisabled(document,true);
		} else {
			setDisabled(document,false);
			setDisabled("reciboReembolso.tpDocumentoServico.description",true);
			setDisabledRelatedPropertiesDoctoServico(true);
			setDisabled("btnExcluir",true);
			setEstadoFilialAcordoTpMir(undefined, eventObj.name);
		}
	}
	
	function pageLoadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		carregaTpDocumentoServico();
		//setDisabled("reciboReembolso.nrReciboReembolso", true);
	}
	
	function carregaTpDocumentoServico() {
		setElementValue("reciboReembolso.tpDocumentoServico.description", "RRE");
		setElementValue("reciboReembolso.tpDocumentoServico.value", "RRE");
	}
	
	function nrReciboReembolsoDataLoad_cb(data) {
		reciboReembolso_nrReciboReembolso_exactMatch_cb(data);
	
		if (data != undefined && data.length > 0) {
			var idDoctoServico = getNestedBeanPropertyValue(data,":0.idDoctoServico");
			validateDoctoServico(idDoctoServico);
		}
	}
	
	function nrReciboReembolsoPopup(data) {
		if (data != undefined) {
			var idDoctoServico = getNestedBeanPropertyValue(data,":idDoctoServico");
			validateDoctoServico(idDoctoServico);
		}
		setDisabled("reciboReembolso.nrReciboReembolso",false);
	}
	
	function validateDoctoServico(idDoctoServico) {
		var data = new Array();
		setNestedBeanPropertyValue(data,'idDoctoServico',idDoctoServico);+
		setNestedBeanPropertyValue(data,'idFilialOrigem',getElementValue("idFilialOrigem"));
		setNestedBeanPropertyValue(data,'idFilialDestino',getElementValue("idFilialDestino"));
		setNestedBeanPropertyValue(data,'tpMir',getElementValue("tpMir.value"));
		setNestedBeanPropertyValue(data,'idMir',getElementValueFromCad("idMir"));
		setNestedBeanPropertyValue(data,'idDocumentoMir',getElementValue("idDocumentoMir"));
		
		var sdo = createServiceDataObject("lms.entrega.manterMemorandosInternosRespostaAction.validateDoctoServicoInReembolsos",
				"validateDoctoServicoInReembolsos",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validateDoctoServicoInReembolsos_cb(data,error) {
		if (error != undefined) {
			alert(error);
			resetValue("reciboReembolso.nrReciboReembolso");
			return false;
		}
		
		onDataLoad_cb(data,error);
	}
	
	function onChangeFilialDocumentoServico(elem) {
		var retorno = reciboReembolso_filial_sgFilialOnChangeHandler();
		if (!retorno || elem.value == "") {
			resetRelatedPropertiesDoctoServico();
			setDisabled("reciboReembolso.nrReciboReembolso",elem.value == "");
		}
		return retorno;
	}
	
	function onChangeNrDocumentoServico(elem) {
		resetRelatedPropertiesDoctoServico();
		return reciboReembolso_nrReciboReembolsoOnChangeHandler();
	}
	
	function setDisabledRelatedPropertiesDoctoServico(value) {
		setDisabled("reciboReembolso.doctoServicoByIdDoctoServReembolsado.tpDocumentoServico.description",value);
		setDisabled("reciboReembolso.doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.sgFilial",value);
		setDisabled("reciboReembolso.doctoServicoByIdDoctoServReembolsado.nrDoctoServico",value);
		setDisabled("reciboReembolso.vlReembolsoCalculadoView",value);
		setDisabled("reciboReembolso.clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado",value);
		setDisabled("reciboReembolso.clienteByIdClienteRemetente.pessoa.nmPessoa",value);
		setDisabled("reciboReembolso.clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado",value);
		setDisabled("reciboReembolso.clienteByIdClienteDestinatario.pessoa.nmPessoa",value);
	}
	
	function resetRelatedPropertiesDoctoServico() {
		resetValue("reciboReembolso.doctoServicoByIdDoctoServReembolsado.tpDocumentoServico.description");
		resetValue("reciboReembolso.doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.sgFilial");
		resetValue("reciboReembolso.doctoServicoByIdDoctoServReembolsado.nrDoctoServico");
		resetValue("reciboReembolso.vlReembolsoCalculadoView");
		resetValue("reciboReembolso.clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado");
		resetValue("reciboReembolso.clienteByIdClienteRemetente.pessoa.nmPessoa");
		resetValue("reciboReembolso.clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado");
		resetValue("reciboReembolso.clienteByIdClienteDestinatario.pessoa.nmPessoa");
	}
	
	function reemDataLoad_cb(data,error) {
		onDataLoad_cb(data,error);

		var blBloqueiaFilhos = getElementValueFromCad("blBloqueiaFilhos");
		if (blBloqueiaFilhos == "true") {
			setDisabled(document,true);
		} else {
			setDisabled(document,false);
			setDisabled("reciboReembolso.tpDocumentoServico.description",true);
			setDisabledRelatedPropertiesDoctoServico(true);
			setDisabled("btnExcluir",true);
			setEstadoFilialAcordoTpMir("true",undefined);
		}		
	}
		
	function tabShowCustom() {
		var tpMir = getElementValueFromCad("tpMir");
		setElementValue("tpMir.value",tpMir);
		newButtonScript(this.document, true, {name:'newItemButton_click'});
	}
	
	function setEstadoFilialAcordoTpMir(isDetalhamento,eventName) {
		var tpMir = getElementValueFromCad("tpMir");
		
		var idFilialDestino = getElementValueFromCad("filialByIdFilialOrigem.idFilial");
		setElementValue("idFilialOrigem",idFilialDestino);
		var idFilialDestino = getElementValueFromCad("filialByIdFilialDestino.idFilial");
		setElementValue("idFilialDestino",idFilialDestino);

		// saber se grid está vazia, para sugerir número de recibo quando masterlink.
		var blGridVazia = documentoMirGridDef.gridState["rowCount"] <= 0;

		if (tpMir == "EA") {
			setElementValue("idFilialDestinoLookup",getElementValueFromCad("filialByIdFilialOrigem.idFilial"));
			setElementValue("sgFilialDestinoLookup",getElementValueFromCad("filialByIdFilialOrigem.sgFilial"));
			setElementValue("nmFilialDestinoLookup",getElementValueFromCad("filialByIdFilialOrigem.pessoa.nmFantasia"));
			
			if (isDetalhamento == undefined || isDetalhamento != "true") {
				setElementValue("reciboReembolso.filial.idFilial","");
				setElementValue("reciboReembolso.filial.sgFilial","");
				setElementValue("reciboReembolso.filial.pessoa.nmFantasia","");
			}
			
			var idFilialReciboCad = getElementValueFromCad("reciboReembolso.filial.idFilial");
			if (blGridVazia &&
					idFilialReciboCad != undefined &&
					idFilialReciboCad != "" &&
					(eventName == "newItemButton_click" || eventName == "tab_click")) {
				setElementValue("reciboReembolso.filial.idFilial",idFilialReciboCad);
				setElementValue("reciboReembolso.filial.sgfilial",getElementValueFromCad("reciboReembolso.filial.sgFilial"));
				setElementValue("reciboReembolso.filial.pessoa.nmFantasia",getElementValueFromCad("reciboReembolso.filial.pessoa.nmFantasia"));
				setElementValue("reciboReembolso.idDoctoServico",getElementValueFromCad("reciboReembolso.idDoctoServico"));
				setElementValue("reciboReembolso.nrReciboReembolso",getElementValueFromCad("reciboReembolso.nrReciboReembolso"));
				setFocus("reciboReembolso.nrReciboReembolso");
				document.getElementById("reciboReembolso.nrReciboReembolso").previousValue = "";
			}
			
			document.getElementById("reciboReembolso.filial.idFilial").masterLink = false;
			setDisabled("reciboReembolso.filial.sgFilial",false);
			
			setDisabled("reciboReembolso.nrReciboReembolso",getElementValue("reciboReembolso.filial.idFilial") == "");
		} else {
			if (tpMir == "DO") {
				setElementValue("idFilialDestinoLookup",getElementValueFromCad("filialByIdFilialOrigem.idFilial"));
				setElementValue("sgFilialDestinoLookup",getElementValueFromCad("filialByIdFilialOrigem.sgFilial"));
				setElementValue("nmFilialDestinoLookup",getElementValueFromCad("filialByIdFilialOrigem.pessoa.nmFantasia"));
				
				setElementValue("reciboReembolso.filial.idFilial",getElementValueFromCad("filialByIdFilialDestino.idFilial"));
				setElementValue("reciboReembolso.filial.sgFilial",getElementValueFromCad("filialByIdFilialDestino.sgFilial"));
				setElementValue("reciboReembolso.filial.pessoa.nmFantasia",
						getElementValueFromCad("filialByIdFilialDestino.pessoa.nmFantasia"));
				document.getElementById("reciboReembolso.filial.idFilial").masterLink = true;
				setDisabled("reciboReembolso.filial.sgFilial",true);
				
				setDisabled("reciboReembolso.nrReciboReembolso",false);
			} else if (tpMir == "AE") {
				setElementValue("idFilialDestinoLookup","");
				setElementValue("sgFilialDestinoLookup","");
				setElementValue("nmFilialDestinoLookup","");
				
				setElementValue("reciboReembolso.filial.idFilial",getElementValueFromCad("filialByIdFilialOrigem.idFilial"));
				setElementValue("reciboReembolso.filial.sgFilial",getElementValueFromCad("filialByIdFilialOrigem.sgFilial"));
				setElementValue("reciboReembolso.filial.pessoa.nmFantasia",
						getElementValueFromCad("filialByIdFilialOrigem.pessoa.nmFantasia"));
				document.getElementById("reciboReembolso.filial.idFilial").masterLink = true;
				setDisabled("reciboReembolso.filial.sgFilial",true);
				
				setDisabled("reciboReembolso.nrReciboReembolso",false);
			}
			
			var idFilialReciboCad = getElementValueFromCad("reciboReembolso.filial.idFilial");
			if (blGridVazia &&
					idFilialReciboCad != undefined &&
					idFilialReciboCad == getElementValue("reciboReembolso.filial.idFilial") &&
					(eventName == "newItemButton_click" || eventName == "tab_click")) {
				setElementValue("reciboReembolso.filial.idFilial",idFilialReciboCad);
				setElementValue("reciboReembolso.filial.sgfilial",getElementValueFromCad("reciboReembolso.filial.sgFilial"));
				setElementValue("reciboReembolso.filial.pessoa.nmFantasia",getElementValueFromCad("reciboReembolso.filial.pessoa.nmFantasia"));
				setElementValue("reciboReembolso.idDoctoServico",getElementValueFromCad("reciboReembolso.idDoctoServico"));
				setElementValue("reciboReembolso.nrReciboReembolso",getElementValueFromCad("reciboReembolso.nrReciboReembolso"));
				setFocus("reciboReembolso.nrReciboReembolso");
				document.getElementById("reciboReembolso.nrReciboReembolso").previousValue = "";
			}
		}
	}
		
	function getElementValueFromCad(property) {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		return tabDet.getFormProperty(property);
	}
	
	var isPopup = false;
	function doctoServicoDataLoad_cb(data) {
		var blRetorno = reciboReembolso_filial_sgFilial_exactMatch_cb(data);
		if (!isPopup) {
			setDisabled("reciboReembolso.nrReciboReembolso", !blRetorno);
			if (blRetorno) {
				setFocus(document.getElementById("reciboReembolso.nrReciboReembolso"));
			}
		}
	}
	
	function filialDoctoServicoPopup(data) {
		isPopup = true;
		setDisabled("reciboReembolso.nrReciboReembolso",false);
		//setFocus("reciboReembolso.nrReciboReembolso");
	}
</script>