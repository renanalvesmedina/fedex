<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.sim.registrarSolicitacoesRetiradaAction">
	<adsm:form action="/municipios/manterRegionais" id="form_doc" 
			   idProperty="idDocumentoServicoRetirada" service="lms.sim.registrarSolicitacoesRetiradaAction.findByIdDoc"
			   onDataLoadCallBack="documentoServicoRetiradaDataLoad">
	
		<adsm:masterLink  idProperty="idSolicitacaoRetirada" showSaveAll="true" >
			<adsm:masterLinkItem property="remetente.pessoa.nmPessoa" label="remetente" itemWidth="45"/>
			<adsm:masterLinkItem property="destinatario.pessoa.nmPessoa" label="destinatario" itemWidth="45"/>			
			<adsm:masterLinkItem property="tpRegistroLiberacao" label="liberadoPor"  itemWidth="45"/>
			<adsm:masterLinkItem property="nmResponsavelAutorizacao" 	boxWidth="100"   label="responsavelPelaLiberacao" itemWidth="45" />
		</adsm:masterLink>
		
		<adsm:hidden property="idProcessoWorkflow"/>
		<adsm:hidden property="idFilialRetirada" serializable="false"/>
		<adsm:hidden property="sgFilialRetirada" serializable="false"/>
		<adsm:hidden property="idDoctoServico" />
		
		<adsm:combobox property="doctoServico.tpDocumentoServico" 
					   label="documentoServico" labelWidth="20%" width="30%" 
					   service="lms.sim.registrarSolicitacoesRetiradaAction.findTipoDocumentoServico" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="return changeTpDoctoServico(this);">
			
			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 	 criteriaSerializable="true"
					 	 serializable="true"
						 service="" 
						 disabled="true"
						 action="" 
						 size="3" maxLength="3" picker="false" 
						 onchange="return changeDocumentWidgetFilial({
											 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
											 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
										  	});
						  			 "
						 popupLabel="pesquisarFilial"/>

			<adsm:lookup dataType="integer" 
						 property="doctoServico" 
						 popupLabel="pesquisarDocumentoServico"
						 onchange="return doctoServicoOnChange(this)"
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" 			
						 onDataLoadCallBack="documentoServicoDataLoad" onPopupSetValue="documentoServicoPopup"			 
						 service="" 
						 action="" 
						 size="12" maxLength="8" mask="00000000" serializable="true" disabled="true" required="true">
			</adsm:lookup>		 
		</adsm:combobox>
		
		<adsm:hidden property="doctoServico.clienteByIdClienteRemetente.idCliente" />
		<adsm:hidden property="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" />
		<adsm:hidden property="doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacao" />
		
		<adsm:hidden property="doctoServico.clienteByIdClienteConsignatario.idCliente" />
		<adsm:hidden property="doctoServico.clienteByIdClienteConsignatario.pessoa.nmPessoa" />
		<adsm:hidden property="doctoServico.clienteByIdClienteConsignatario.pessoa.nrIdentificacao" />
		
		<adsm:hidden property="doctoServico.clienteByIdClienteDestinatario.idCliente" />
		<adsm:hidden property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" />
		<adsm:hidden property="doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao" />
		
		<adsm:hidden property="doctoServico.clienteByIdClienteDestinatario.idCliente" />
		<adsm:hidden property="doctoServico.clienteByIdClienteConsignatario.idCliente" />
		<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" />
			
		<%--adsm:lookup label="notaFiscal" action="/expedicao/pesquisarConhecimento" service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupNotaFiscal"
					 dataType="integer" property="notaFiscalConhecimento"  idProperty="idDoctoServico" labelWidth="20%" width="30%" 
					 criteriaProperty="nrConhecimento" mask="00000000" maxLength="30" exactMatch="false" minLengthForAutoPopUpSearch="3"
					 popupLabel="pesquisarNotaFiscal" onDataLoadCallBack="notaFiscalDataLoad" onPopupSetValue="notaFiscalPopup">		
					 
			<adsm:propertyMapping relatedProperty="doctoServico.tpDocumentoServico" modelProperty="tpDocumentoServico.value" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="doctoServico.nrDoctoServico" modelProperty="nrConhecimento"  blankFill="false"/>
			<adsm:propertyMapping relatedProperty="doctoServico.idDoctoServico" modelProperty="idDoctoServico"  blankFill="false"/>
			<adsm:propertyMapping relatedProperty="idDoctoServico" modelProperty="idDoctoServico"  blankFill="false"/>
			<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial"  blankFill="false"/>
			<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial"  blankFill="false"/>
			
			<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteRemetente.idCliente" modelProperty="clienteByIdClienteRemetente.idCliente"/>
			<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacao" modelProperty="clienteByIdClienteRemetente.pessoa.nrIdentificacao"/>
			<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" modelProperty="clienteByIdClienteRemetente.pessoa.nmPessoa"/>
			
			<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteDestinatario.idCliente" modelProperty="clienteByIdClienteDestinatario.idCliente"/>
			<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao" modelProperty="clienteByIdClienteDestinatario.pessoa.nrIdentificacao"/>
			<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" modelProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa"/>
			
		</adsm:lookup--%>
		
		<adsm:lookup label="notaFiscal" action="/expedicao/consultarNotaFiscalCliente" 
					 service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupNotaFiscal"
					 dataType="integer" property="notaFiscalCliente" popupLabel="pesquisarNotaFiscal" 
					 idProperty="idNotaFiscalConhecimento" labelWidth="15%" width="25%" criteriaProperty="nrNotaFiscal" 
					 mask="000000000" maxLength="30" exactMatch="false" minLengthForAutoPopUpSearch="3" onchange="return notaFiscalChange(this)"
					 onPopupSetValue="notaFiscalPopup">

				<adsm:propertyMapping relatedProperty="doctoServico.tpDocumentoServico" modelProperty="tpDocumentoServico.value" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.sgFilial" modelProperty="sgFilialOrigem" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.idFilial" modelProperty="idFilialOrigem" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="nmFantasiaOrigem" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.idDoctoServico" modelProperty="idDoctoServico" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.nrDoctoServico" modelProperty="nrDoctoServico" blankFill="false"/>
				
				<adsm:propertyMapping criteriaProperty="doctoServico.tpDocumentoServico" modelProperty="doctoServico.tpDocumentoServico" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="doctoServico.filialByIdFilialOrigem.sgFilial" modelProperty="doctoServico.filialByIdFilialOrigem.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="doctoServico.filialByIdFilialOrigem.idFilial" modelProperty="doctoServico.filialByIdFilialOrigem.idFilial" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="doctoServico.idDoctoServico" modelProperty="doctoServico.idDoctoServico" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="doctoServico.nrDoctoServico" modelProperty="doctoServico.nrDoctoServico" inlineQuery="false"/>
				
				
				<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteRemetente.idCliente" modelProperty="remetente.idCliente" inlineQuery="true"/>
				<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacao" modelProperty="remetente.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" modelProperty="remetente.pessoa.nmPessoa" inlineQuery="false"/>
				
				<adsm:propertyMapping criteriaProperty="doctoServico.idDoctoServico" modelProperty="idDoctoServico" />
				<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteDestinatario.idCliente" modelProperty="destinatario.idCliente" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao" modelProperty="destinatario.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" modelProperty="destinatario.pessoa.nmPessoa" inlineQuery="false"/>
			
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton id="storeButton" service="lms.sim.registrarSolicitacoesRetiradaAction.saveDoc" caption="salvarDocumento"/>
			<adsm:newButton id="newButton"/>			
		</adsm:buttonBar>

	</adsm:form>
	
	<adsm:grid idProperty="idDocumentoServicoRetirada" property="documento" autoSearch="false"
			   unique="true" rows="9" scrollBars="horizontal" detailFrameName="doc"
			   service="lms.sim.registrarSolicitacoesRetiradaAction.findPaginatedDoc"
			   rowCountService="lms.sim.registrarSolicitacoesRetiradaAction.getRowCountDoc"
			   onDataLoadCallBack="gridDataLoad">
			   
		<adsm:gridColumn width="100" title="notaFiscal" property="nrNotaFiscal" dataType="integer" mask="000000000"/>
		
		<adsm:gridColumn title="documentoServico" property="doctoServico.tpDocumentoServico" width="40" isDomain="true"/>
		
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="doctoServico.filialByIdFilialOrigem.sgFilial" width="70" />
			<adsm:gridColumn title="" property="doctoServico.nrDoctoServico" width="60" align="right" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup> 
					
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn width="75" title="filialDestino" property="doctoServico.filialByIdFilialDestino.sgFilial"/>
			<adsm:gridColumn width="75" title="" property="doctoServico.filialByIdFilialDestino.pessoa.nmFantasia"/>
		</adsm:gridColumnGroup>	
			
		<adsm:gridColumn width="60" title="identificacao" property="remetente.pessoa.tpIdentificacao" align="left" />
		
		<adsm:gridColumn width="125" title="" property="remetente.pessoa.nrIdentificacao" align="right"/>
		
		<adsm:gridColumn width="150" title="remetente" property="remetente.pessoa.nmPessoa" />
		
		<adsm:gridColumn width="150" title="priorizado"  property="blPriorizado"  renderMode="image-check"/>
		
		<adsm:buttonBar>
			<adsm:button caption="associarDocumentos" id="associarDocumentos" onclick="openDocumentos()" disabled="false" />
			<adsm:removeButton caption="excluirDocumento" service="lms.sim.registrarSolicitacoesRetiradaAction.removeByIdsDoc"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script><!--

		document.getElementById("doctoServico.filialByIdFilialOrigem.sgFilial").serializable = "true";
	
		document.getElementById("doctoServico.clienteByIdClienteDestinatario.idCliente").masterLink = "true";
		document.getElementById("doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa").masterLink = "true";
		document.getElementById("doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao").masterLink = "true";
		
		document.getElementById("doctoServico.clienteByIdClienteConsignatario.idCliente").masterLink = "true";
		document.getElementById("doctoServico.clienteByIdClienteConsignatario.pessoa.nmPessoa").masterLink = "true";
		document.getElementById("doctoServico.clienteByIdClienteConsignatario.pessoa.nrIdentificacao").masterLink = "true";
		
		document.getElementById("doctoServico.clienteByIdClienteRemetente.idCliente").masterLink = "true";
		document.getElementById("doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa").masterLink = "true";
		document.getElementById("doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacao").masterLink = "true";
		
		document.getElementById("idFilialRetirada").masterLink = "true";
		document.getElementById("sgFilialRetirada").masterLink = "true";
		
	function initWindow(evt){
	
		var tabGroup = getTabGroup(this.document);
		var tab = tabGroup.getTab("cad");
			
		var cad = tab.tabOwnerFrame.document;

		if (getElementValue("idProcessoWorkflow") != '' || cad.getElementById("idSolicitacaoRetirada").value != ''){
			setDisabled(document, true);

		} else if (evt.name == 'tab_click'){
			copyValuesFromCad();
			
			setElementValue("doctoServico.tpDocumentoServico", "");
			changeTpDoctoServico("");
			
			setDisabled(document, false);
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", getElementValue("doctoServico.tpDocumentoServico") == '');
			setDisabled("doctoServico.idDoctoServico", getElementValue("doctoServico.filialByIdFilialOrigem.idFilial") == '', null, getElementValue("doctoServico.tpDocumentoServico") == '');
			setDisabled("notaFiscalCliente.idNotaFiscalConhecimento", false);
			setDisabled("associarDocumentos", false);
			

			if (getElementValueFromCad("idFilialRetiradaOld") != getElementValueFromCad("filialRetirada.idFilial") ||
					getElementValueFromCad("idRemetenteOld") != getElementValueFromCad("remetente.idCliente") ||
					getElementValueFromCad("idDestinatarioOld") != getElementValueFromCad("destinatario.idCliente")) {
				newButtonScript(this.document, true, {name:'newItemButton_click'});
			}
			
		} else if (evt.name == 'newItemButton_click'){
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
			setDisabled("doctoServico.idDoctoServico", true);
			setDisabled("associarDocumentos", false);
		}		
	}
	
	function getElementValueFromCad(property) {
		var tabGroup = getTabGroup(this.document);
		var tab = tabGroup.getTab("cad");
		var cad = tab.tabOwnerFrame.document;
		return cad.getElementById(property).value
	}
	
	function copyValuesFromCad(){
		var tabGroup = getTabGroup(this.document);
		var tab = tabGroup.getTab("cad");
			
		var cad = tab.tabOwnerFrame.document;
					
		setElementValue("doctoServico.clienteByIdClienteDestinatario.idCliente", cad.getElementById("destinatario.idCliente").value);
		setElementValue("doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa", cad.getElementById("destinatario.pessoa.nmPessoa").value);
		setElementValue("doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao", cad.getElementById("destinatario.pessoa.nrIdentificacao").value);
		
		setElementValue("doctoServico.clienteByIdClienteConsignatario.idCliente", cad.getElementById("consignatario.idCliente").value);
		setElementValue("doctoServico.clienteByIdClienteConsignatario.pessoa.nmPessoa", cad.getElementById("consignatario.pessoa.nmPessoa").value);
		setElementValue("doctoServico.clienteByIdClienteConsignatario.pessoa.nrIdentificacao", cad.getElementById("consignatario.pessoa.nrIdentificacao").value);
		
		setElementValue("doctoServico.clienteByIdClienteRemetente.idCliente", cad.getElementById("remetente.idCliente").value);
		setElementValue("doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa", cad.getElementById("remetente.pessoa.nmPessoa").value);
		setElementValue("doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacao", cad.getElementById("remetente.pessoa.nrIdentificacao").value);
		
		setElementValue("idFilialRetirada", cad.getElementById("filialRetirada.idFilial").value);
		setElementValue("sgFilialRetirada", cad.getElementById("filialRetirada.sgFilial").value);
		
		setElementValue("idProcessoWorkflow", cad.getElementById("idProcessoWorkflow").value);
		
	}
	
	function documentoServicoRetiradaDataLoad_cb(data){
		
		var tabGroup = getTabGroup(this.document);
		var tab = tabGroup.getTab("cad");
			
		var cad = tab.tabOwnerFrame.document;
				
		if (getElementValue("idProcessoWorkflow") != '' || cad.getElementById("idSolicitacaoRetirada").value != ''){
			onDataLoad_cb(data);	
	
			setDisabled(document, true);
			setDisabled("newButton", false);

		} else {
			setElementValue("doctoServico.tpDocumentoServico", getNestedBeanPropertyValue(data, "doctoServico.tpDocumentoServico"));
			changeTpDoctoServico(document.getElementById("doctoServico.tpDocumentoServico"));
			habilitaDoctoServico();
			
			onDataLoad_cb(data);
			
			setDisabled("associarDocumentos", false);
		}
				
		if (data != undefined){
			setElementValue("idDoctoServico", getElementValue("doctoServico.idDoctoServico"));
		}
	}

	function desabilitaDoctoServico(){
		setDisabled("doctoServico.idDoctoServico", true);
		setDisabled("doctoServico.tpDocumentoServico", true);
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
	}

	function habilitaDoctoServico(){
		setDisabled("doctoServico.idDoctoServico", false);
		setDisabled("doctoServico.tpDocumentoServico", false);
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", false);
	}

	function openDocumentos(){
			
		showModalDialog('sim/registrarSolicitacoesRetirada.do?cmd=documentosServicos',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:510px;');
	}
	
	function documentoServicoDataLoad_cb(data) {
		doctoServico_nrDoctoServico_exactMatch_cb(data);
		
		if (data != undefined && data.length == 1) {
			var idDoctoServico = getNestedBeanPropertyValue(data,":0.idDoctoServico");
			validateDoctoServico(idDoctoServico);
		}
	}
	
	function documentoServicoPopup(data) {
		if (data != undefined) {
			var idDoctoServico = getNestedBeanPropertyValue(data,"idDoctoServico");
			validateDoctoServico(idDoctoServico);
		}
		return true;
	}
	
	function doctoServicoOnChange(obj){
		if (obj.value != '') {
			var tabGroup = getTabGroup(this.document);
			var tab = tabGroup.getTab("cad");				
			var cad = tab.tabOwnerFrame.document;			
		} 
		
		return doctoServico_nrDoctoServicoOnChangeHandler();
	}
	
	function validateDoctoServico(idDoctoServico) {
		var data = new Array();
		setNestedBeanPropertyValue(data,'idDoctoServico',idDoctoServico);	
		setNestedBeanPropertyValue(data,'idFilial', getElementValue("idFilialRetirada"));	
		setNestedBeanPropertyValue(data,'sgFilial', getElementValue("sgFilialRetirada"));
		setNestedBeanPropertyValue(data,'idSolicitacaoRetirada', getElementValue("masterId"));	
	
		var sdo = createServiceDataObject("lms.sim.registrarSolicitacoesRetiradaAction.validateDoctoServico",
				"validateDoctoServico",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validateDoctoServico_cb(data,error) {
		if (error != undefined) {
			alert(error);
			resetValue("idDoctoServico");
			resetValue("doctoServico.idDoctoServico");
			setFocus("doctoServico.nrDoctoServico");
			
			return false;
		}
		setElementValue("notaFiscalCliente.nrNotaFiscal", getNestedBeanPropertyValue(data, "nrNotaFiscal"));
		onDataLoad_cb(data,error);
	}
	
	function changeTpDoctoServico(field) {
		var flag = changeDocumentWidgetType({
						   documentTypeElement:document.getElementById("doctoServico.tpDocumentoServico"), 
						   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
						   documentGroup:'DOCTOSERVICE',
						   parentQualifier:"doctoServico",
						   actionService:'lms.sim.registrarSolicitacoesRetiradaAction'});

		var pms = document.getElementById("doctoServico.idDoctoServico").propertyMappings;
		pms[pms.length] = {modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico"};		
		
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.idCliente", criteriaProperty:"doctoServico.clienteByIdClienteRemetente.idCliente", inlineQuery:true };
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.pessoa.nrIdentificacao", criteriaProperty:"doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacao"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.pessoa.nmPessoa", criteriaProperty:"doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa"};
		
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.idCliente", criteriaProperty:"doctoServico.clienteByIdClienteDestinatario.idCliente", inlineQuery:true};
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.pessoa.nrIdentificacao", criteriaProperty:"doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacao"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.pessoa.nmPessoa", criteriaProperty:"doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa"};
		
		if (field.value == "MDA"){
			pms[pms.length] = {modelProperty:"clienteByIdClienteConsignatario.idCliente", criteriaProperty:"doctoServico.clienteByIdClienteConsignatario.idCliente", inlineQuery:true};
			pms[pms.length] = {modelProperty:"clienteByIdClienteConsignatario.pessoa.nrIdentificacao", criteriaProperty:"doctoServico.clienteByIdClienteConsignatario.pessoa.nrIdentificacao"};
			pms[pms.length] = {modelProperty:"clienteByIdClienteConsignatario.pessoa.nmPessoa", criteriaProperty:"doctoServico.clienteByIdClienteConsignatario.pessoa.nmPessoa"};
		}
		
		return flag;
	}
	
	function notaFiscalDataLoad_cb(data){
		
		if (data != undefined && data.length == 1) {
			setElementValue("doctoServico.tpDocumentoServico", getNestedBeanPropertyValue(data, ":0.tpDocumentoServico.value"));
			changeTpDoctoServico(document.getElementById("doctoServico.tpDocumentoServico"));
			setDisabled("doctoServico.idDoctoServico", false);
			validateDoctoServico(getNestedBeanPropertyValue(data, ":0.idDoctoServico"));
		}
		
		notaFiscalCliente_nrNotaFiscal_exactMatch_cb(data);
		
	}
		
	function notaFiscalPopup(obj){
		return notaFiscalChange(obj);
	}
	
	function atualizaGrid(){
		var data = new Object();
		data.idSolicitacaoRetirada = getElementValue("masterId");
		documentoGridDef.executeSearch(data);
	}
	
	function gridDataLoad_cb(data){

		var tabGroup = getTabGroup(this.document);
		var tab = tabGroup.getTab("cad");
			
		var cad = tab.tabOwnerFrame.document;

		if (data != undefined && data.list != undefined && data.list.length > 0){			
			tab.tabOwnerFrame.disableClientes(true);
			
		} else {
			tab.tabOwnerFrame.disableClientes(false);
		}
	}
	
	function notaFiscalChange(obj) {
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", false);
		setDisabled("doctoServico.idDoctoServico", false);

		return notaFiscalCliente_nrNotaFiscalOnChangeHandler();
	}

--></script>