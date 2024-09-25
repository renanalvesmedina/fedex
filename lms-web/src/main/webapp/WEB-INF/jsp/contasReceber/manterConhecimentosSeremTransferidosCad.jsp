<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.manterConhecimentosSeremTransferidosAction" >
	<adsm:form action="/contasReceber/manterConhecimentosSeremTransferidos"
	service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findById"
	idProperty="idAgendaTransferencia" onDataLoadCallBack="myOnDataLoad">

		<adsm:hidden property="tpOrigem" value="M"/>
		<adsm:hidden property="tpSituacao" value="A"/>
		
		<adsm:hidden property="tpModal" serializable="false"/>
		<adsm:hidden property="tpAbrangencia" serializable="false"/>
		
		<adsm:hidden property="filialByIdFilialOrigem.idFilial"/>
		<adsm:textbox dataType="text" property="filialByIdFilialOrigem.sgFilial" size="3" width="80%" label="filialOrigem" 
			serializable="false" disabled="true" labelWidth="20%">				
			<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" 
			size="30" serializable="false" disabled="true"/>		
		</adsm:textbox>			
		
		<adsm:hidden property="tpSituacaoDevedorDocServFatValido" serializable="true" value="0"/>
		
		<adsm:combobox property="devedorDocServFat.doctoServico.tpDocumentoServico"
			   label="documentoServico" 
			   width="80%"
			   labelWidth="20%"
			   service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findTipoDocumentoServico"
			   optionProperty="value" 
			   optionLabelProperty="description"
			   onchange="return tpDocumentoServicoOnChange();"> 			

			<adsm:lookup dataType="text"
						 property="devedorDocServFat.doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" 
					 	 criteriaProperty="sgFilial"
						 service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findLookupFilial"
						 disabled="true"
						 action=""
						 size="3" 
						 maxLength="3" 
						 picker="false" 
						 popupLabel="pesquisarFilial"
						 exactMatch="true"
						 onchange="return filialOnChange();"
						 onDataLoadCallBack="filialOnChange"/>		
						<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia"/>				 
			<adsm:lookup dataType="integer"
						 property="devedorDocServFat"
						 idProperty="idDevedorDocServFat" 
						 criteriaProperty="doctoServico.nrDoctoServico"
						 service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findDevedorServDocFat"
						 action="/contasReceber/pesquisarDevedorDocServFatLookUp"
						 cmd="pesq"
						 size="10" 
						 maxLength="8" 
						 serializable="true" 
						 disabled="true" 
						 exactMatch="true"
						 popupLabel="pesquisarDocumentoServico"
						 onPopupSetValue="myOnPopupSetValue"
						 onDataLoadCallBack="myOnDataLoadDocumento"
						 onchange="return onChangeDevedor(this);"
						 mask="00000000"
						 required="true">
 						 <adsm:propertyMapping modelProperty="tpSituacaoDevedorDocServFatValido" criteriaProperty="tpSituacaoDevedorDocServFatValido"/>
						 <adsm:propertyMapping modelProperty="doctoServico.tpDocumentoServico" criteriaProperty="devedorDocServFat.doctoServico.tpDocumentoServico"/>						 						 
						 <adsm:propertyMapping modelProperty="doctoServico.filialByIdFilialOrigem.idFilial" criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"/>						 
						 <adsm:propertyMapping modelProperty="doctoServico.filialByIdFilialOrigem.sgFilial" criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial" inlineQuery="true"/>						 						 
						 <adsm:propertyMapping modelProperty="nrIdentificacaoResponsavelAnterior" relatedProperty="devedorDocServFat.cliente.pessoa.nrIdentificacao"/>
						 <adsm:propertyMapping modelProperty="nmResponsavelAnterior" relatedProperty="devedorDocServFat.cliente.pessoa.nmPessoa"/>
						 <adsm:propertyMapping modelProperty="dsDivisaoCliente" relatedProperty="dsDivisaoCliente"/>
						 <adsm:propertyMapping modelProperty="idDoctoServico" relatedProperty="devedorDocServFat.doctoServico.idDoctoServico"/>						 						 
						 <adsm:propertyMapping modelProperty="tpModal.value" relatedProperty="tpModal"/>						 						 
						 <adsm:propertyMapping modelProperty="tpAbrangencia.value" relatedProperty="tpAbrangencia"/>						 						 
			</adsm:lookup>			 
		</adsm:combobox>
		<adsm:hidden property="devedorDocServFat.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia"/>
		<adsm:hidden property="devedorDocServFat.doctoServico.idDoctoServico"/>				
		<adsm:hidden property="divisaoCliente.idDivisaoClienteTmp"/>				
		
		<adsm:textbox dataType="text" property="devedorDocServFat.cliente.pessoa.nrIdentificacao" 
			size="20" width="80%" label="responsavelAnterior" 
			serializable="false" disabled="true" labelWidth="20%">				
			<adsm:textbox dataType="text" property="devedorDocServFat.cliente.pessoa.nmPessoa" 
			size="50" serializable="false" disabled="true"/>		
		</adsm:textbox>				
		
		<adsm:textbox dataType="text" property="dsDivisaoCliente" label="divisaoResponsavelAnterior"
			size="60" serializable="false" labelWidth="20%" width="80%" disabled="true"/>		
			
		<adsm:lookup label="novoResponsavel" width="80%" labelWidth="20%" size="20" maxLength="20"  serializable="true"
					 service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findLookupClienteAtivo" 
					 action="/vendas/manterDadosIdentificacao"
					 dataType="text" 
					 property="cliente" 
					 idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 onDataLoadCallBack="novoResponsavel"
					 onPopupSetValue="novoResponsavelPopUpSetValue"
					 onchange="return clienteOnChange();"
					 exactMatch="false"
					 disabled="true"
					 required="true">
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.idFilial" modelProperty="filialByIdFilialCobranca.idFilial"/>			
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.sgFilial" modelProperty="filialByIdFilialCobranca.sgFilial"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="filialByIdFilialCobranca.pessoa.nmFantasia"/>			
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="50" maxLength="50" 
						  disabled="true" serializable="true"/>
		</adsm:lookup>
		
		<adsm:hidden property="descricaoDivisaoCliente"/>	
		<adsm:combobox property="divisaoCliente.idDivisaoCliente" label="divisaoNovoResponsavel" 
					   autoLoad="false"
					   disabled="true"
        			   service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findComboDivisaoCliente" 
					   optionLabelProperty="dsDivisaoCliente"				
					   labelWidth="20%"
					   boxWidth="150"
					   width="80%"
					   optionProperty="idDivisaoCliente">
					   
					  <adsm:propertyMapping 
							relatedProperty="descricaoDivisaoCliente" 
							modelProperty="dsDivisaoCliente"/>
		</adsm:combobox>
		
		<adsm:hidden property="filialByIdFilialDestino.idFilial"/>
		<adsm:textbox property="filialByIdFilialDestino.sgFilial" label="filialDestino" size="3" maxLength="3" width="35%" labelWidth="20%" disabled="true" dataType="text">
			<adsm:textbox property="filialByIdFilialDestino.pessoa.nmFantasia" size="30" disabled="true" dataType="text"/>
		</adsm:textbox>					
				
		<adsm:textbox label="dataPrevistaTransferencia" dataType="JTDate" property="dtPrevistaTransferencia" labelWidth="25%" width="20%" disabled="true"/>

		<adsm:combobox service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findMotivoTransferencia" 
		optionLabelProperty="dsMotivoTransferencia" onlyActiveValues="true"
		labelWidth="20%" width="80%"
		optionProperty="idMotivoTransferencia" property="motivoTransferencia.idMotivoTransferencia" 
		label="motivo" required="true" boxWidth="240"/>
						
		<adsm:textarea maxLength="500" property="obAgendaTransferencia" columns="80" rows="4" label="observacao" width="80%" labelWidth="20%"/>
		
		<adsm:buttonBar>
			<adsm:button caption="emitirTransferencia"  onclick="return emitirTransferencia();"/>
			<adsm:button caption="carregarPlanilha" id="carregarPlanilha" onclick="openPopupCarregarPlanilhaConhecimentos();"/>	
			<adsm:storeButton callbackProperty="myStore"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script src="/<%=request.getContextPath().substring(1)%>/lib/formatNrDocumento.js" type="text/javascript"></script>
<script>
	
	getElement('tpSituacaoDevedorDocServFatValido').masterLink = 'true';

	getElement('devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial').required = 'true'; 
	getElement('devedorDocServFat.doctoServico.tpDocumentoServico').required = 'true';
	
	function openPopupCarregarPlanilhaConhecimentos(){
		var url = '/contasReceber/manterConhecimentosSeremTransferidos.do?cmd=popupCarregarPlanilhaConhecimentos';
		var data = showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:900px;dialogHeight:200px;');
	}
	function myStore_cb(d,e,c,x){
		store_cb(d,e,c,x);
		
		if (e == undefined){
			setDisabled("cliente.idCliente", true);
			setDisabled("divisaoCliente.idDivisaoCliente", true);
	    	setDisabled("devedorDocServFat.idDevedorDocServFat", true);
	    	setDisabled("devedorDocServFat.doctoServico.tpDocumentoServico", true);
	    	setDisabled("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial", true);
	    }
	}

	/*
	 * On Change da lookup de Filial
	 */
	function resetDevedor(resetDocument){
	
		if( resetDocument == true ){
			resetValue("devedorDocServFat.idDevedorDocServFat");
			resetValue("devedorDocServFat.doctoServico.nrDoctoServico");
		}

		if (getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico") == "") {
			setDisabled("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",true);
			setDisabled("devedorDocServFat.idDevedorDocServFat",true);
		} else {
			setDisabled("devedorDocServFat.doctoServico.tpDocumentoServico",false);
			setDisabled("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",false);
			habilitaLupa();
			if (getElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial") != ""){
				setDisabled("devedorDocServFat.idDevedorDocServFat",false);		
			}
		}		

	}	
	
	/*
		Habilita a lupa de documento de serviço mesmo com o número do documento desabilitado
	*/
	function habilitaLupa() {
		setDisabled("devedorDocServFat.idDevedorDocServFat", false);
		setDisabled("devedorDocServFat.doctoServico.nrDoctoServico", true);
	}

	function initWindow(eventObj) {
	  	loadFilial();	    
	    if (eventObj.name == "newButton_click" || eventObj.name == "tab_click" || eventObj.name == "gridRow_click"){
	    	resetDevedor(true);
			
			// desabilita a lookup de novo responsável
			setDisabled("cliente.idCliente", true);
			setDisabled("divisaoCliente.idDivisaoCliente", true);
			
		    // setando o valor padrao da combo Documento de Serviço
			setElementValue("devedorDocServFat.doctoServico.tpDocumentoServico","CTR");
			tpDocumentoServicoOnChange();
			resetComboDivisao();
			setFocusOnFirstFocusableField();
	    } else if (eventObj.name == "storeButton"){
		    resetDevedor(false);
		    return;
	    }	  

	    if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton_click") {
	    	setDisabled("devedorDocServFat.idDevedorDocServFat", true);
	    	setDisabled("devedorDocServFat.doctoServico.tpDocumentoServico", true);
	    	setDisabled("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial", true);
	    	setFocusOnFirstFocusableField();
	    }
	    
	    if(eventObj.name == "removeButton"){
	    	newButtonScript(document);
	    	setFocusOnFirstFocusableField(document);//('devedorDocServFat.doctoServico.tpDocumentoServico', false);		
	    }
	    setDisabled("carregarPlanilha", false);
	}
	
	function myOnDataLoad_cb(d,e,c,x){
	
		onDataLoad_cb(d,e,c,x);
		findComboDivisaoCliente(getElementValue("cliente.idCliente"), getElementValue("divisaoCliente.idDivisaoClienteTmp"));
	
	}			
		
	function loadFilial(){
		if (getTabGroup(this.document).getTab("pesq").tabOwnerFrame.constFilial.idFilial != undefined){
			var constFilial = getTabGroup(this.document).getTab("pesq").tabOwnerFrame.constFilial;
			setElementValue("filialByIdFilialOrigem.idFilial", constFilial.idFilial);
			setElementValue("filialByIdFilialOrigem.sgFilial", constFilial.sgFilial);
			setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", constFilial.nmFantasia);	
			setElementValue("dtPrevistaTransferencia", setFormat("dtPrevistaTransferencia", constFilial.dataAtual));	
		} else {	
			setTimeout("loadFilial()",1000);
		}		
	}		

	/*
	 * On change da combo de Tipo de Documento de Serivo.<BR>
	 * Altera lookup de conhecimento
	 * @see changeLookupConhecimento
	 */
	function tpDocumentoServicoOnChange(){	
	
		resetDevedor(true);	
		resetValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial");
		filialOnChange();						
		setMaskNrDocumento("devedorDocServFat.doctoServico.nrDoctoServico", getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico"))	
		validaDevedor();
			
	}
	
	/*
	 * On Change da lookup de Filial
	 */
	function filialOnChange(){
	
		resetDevedor(true);	
		
		var siglaFilial = getElement('devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial');
		var siglaAnterior = siglaFilial.previousValue;
		
		var retorno = devedorDocServFat_doctoServico_filialByIdFilialOrigem_sgFilialOnChangeHandler();
		
		if (siglaFilial.value == ''){
			setDisabled('cliente.idCliente',true);
		}		
		
		if( siglaAnterior != '' && siglaFilial.value == '' ){
			setFocus('devedorDocServFat_lupa',false);		
		}
		
		validaDevedor();	
		return retorno;
	
	}	
	
	/*
	 * On Change callBack da lookup de Filial
	 */	
	function filialOnChange_cb(data,e,c,x){
		resetDevedor(true);
		if (data.length == 1) {			
			setDisabled("devedorDocServFat.idDevedorDocServFat",false);			
			__lookupSetValue({e:getElement("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"), data:data[0]});
			return true;
		} else {
			alert(lookup_noRecordFound);
		}			
	}
	
	function emitirTransferencia(){
		var data = new Object();
		data.filial = {idFilial: getElementValue("filialByIdFilialOrigem.idFilial")};
		data.tpOrigem = getElementValue("tpOrigem");
		data.siglaNomeFilial = getElementValue("filialByIdFilialOrigem.sgFilial") + ' - ' + getElementValue("filialByIdFilialOrigem.pessoa.nmFantasia");
		
		var sdo = createServiceDataObject('lms.contasreceber.emitirTransferenciasDebitoAction', 'openPdf', data); 
		executeReportWindowed(sdo, 'pdf');
	}			
	
	function myOnPopupSetValue(data, dialogWindow){
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false};
		var storeSDO = createServiceDataObject("lms.contasreceber.manterConhecimentosSeremTransferidosAction.validateMonitoramentoEletronicoAutorizado", "myOnPopupSetValue", {data:data});
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
		
		return true;
	}
	
	function myOnPopupSetValue_cb(data, error){
		if (error) {
			alert(error);
			resetValue("devedorDocServFat.doctoServico.idDoctoServico");
			resetDevedor(true);
			validaDevedor();
		} else {
			if (data.data.idDevedorDocServFat != null) {			
				setElementValue("devedorDocServFat.doctoServico.idDoctoServico",data.data.idDoctoServico);
				setElementValue("devedorDocServFat.doctoServico.nrDoctoServico",setFormat(getElement("devedorDocServFat.doctoServico.nrDoctoServico"),data.data.doctoServico.nrDoctoServico));
				setElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",data.data.idFilialOrigem);
				setElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial",data.data.sgFilialOrigem);	
				
				setDisabled("devedorDocServFat.idDevedorDocServFat", false);
				setDisabled("devedorDocServFat.doctoServico.tpDocumentoServico", false);
				
				// Seta o foco na lookup de novoResponsavel
				setDisabled("cliente.idCliente", false);
				document.getElementById("cliente.pessoa.nrIdentificacao").focus();
				
			} else {
				habilitaLupa();
			}
		}
		
		return true;
	}
	
	function myOnDataLoadDocumento_cb(d,e){
		if ( e != undefined ) {
			/*alert(e);*/
		}
		if (d.length == 1){
			myOnPopupSetValue(d[0], undefined);
		}
		
		var retorno = devedorDocServFat_doctoServico_nrDoctoServico_exactMatch_cb(d);
		var cliente = getElementValue("cliente.idCliente")
		if (cliente != "")
			findComboDivisaoCliente(cliente);
		
		return retorno;
	}
	
	/** Busca as divisões do cliente */
	function findComboDivisaoCliente(idCliente, idDivisao){
	
		var data = new Array();	   
		
		setNestedBeanPropertyValue(data, "idCliente", idCliente);
		setNestedBeanPropertyValue(data, "tpModal", getElementValue("tpModal"));
		setNestedBeanPropertyValue(data, "tpAbrangencia", getElementValue("tpAbrangencia"));
		setNestedBeanPropertyValue(data, "idDivisao", idDivisao);
		
		var sdo = createServiceDataObject("lms.contasreceber.manterConhecimentosSeremTransferidosAction.findComboDivisaoCliente",
			"findComboDivisaoCliente", data);
		xmit({serviceDataObjects:[sdo]});

	}
	
	/** 
	  * CallBack da função findComboDivisaoCliente 
	  */
	function findComboDivisaoCliente_cb(data, error){
		
		if(error != undefined){
			alert(error);
		}
		
		divisaoCliente_idDivisaoCliente_cb(data);
		
		if(getElement("divisaoCliente.idDivisaoCliente").options.length > 1){
			getElement("divisaoCliente.idDivisaoCliente").required = "true";
			setDisabled("divisaoCliente.idDivisaoCliente", false);
			
			// Combo tem uma divisão e selecione
			if(getElement("divisaoCliente.idDivisaoCliente").options.length == 2){
				getElement("divisaoCliente.idDivisaoCliente").options[1].selected = true;
			// Combo tem mais que uma divisão
			} else {
				if (getElementValue("divisaoCliente.idDivisaoClienteTmp") == ""){
					getElement("divisaoCliente.idDivisaoCliente").focus();
				}
			}
		
		// Combo não tem nenhuma divisão	
		}else{
			resetComboDivisao();
		}
		
		// Caso  o hidden tenha a divisão da agendaTransferencia
		if (getElementValue("divisaoCliente.idDivisaoClienteTmp") != ""){
			setElementValue("divisaoCliente.idDivisaoCliente", getElementValue("divisaoCliente.idDivisaoClienteTmp"));
			setDisabled("divisaoCliente.idDivisaoCliente", true);
		// Caso o hidden esteja em nulo
		} else {
			// Caso a combo tenha uma divisão e o selecione
			if(getElement("divisaoCliente.idDivisaoCliente").options.length == 2){
				getElement("divisaoCliente.idDivisaoCliente").options[1].selected = true;
			// Caso a combo não tenha nenhuma divisão ou tenha mais de uma
			} else {
				getElement("divisaoCliente.idDivisaoCliente").options[0].selected = true;
			}
		}
	}
	
	// CallBack da lookup de novo cliente
	function novoResponsavel_cb(data, error){
		
		if(error != undefined)
			alert(error);
		
		if(cliente_pessoa_nrIdentificacao_exactMatch_cb(data)){
			findComboDivisaoCliente(data[0].idCliente);
		}
	}
	
	// PopUpSetValue da lookup novo responsavel
	function novoResponsavelPopUpSetValue(data, error){
		findComboDivisaoCliente(data.idCliente);
	}
	
	// OnChange da lookup de cliente
	function clienteOnChange(){
		var retorno = cliente_pessoa_nrIdentificacaoOnChangeHandler();	
		
		setElementValue("divisaoCliente.idDivisaoClienteTmp", "");
		
		if(getElementValue("cliente.pessoa.nrIdentificacao") == "" && getElementValue("cliente.pessoa.nmPessoa") == "")
			resetComboDivisao();
			
		return retorno;	
	}
	
	// Reseta a combo de divisao
	function resetComboDivisao(){
		
		setDisabled("divisaoCliente.idDivisaoCliente", true);
		getElement("divisaoCliente.idDivisaoCliente").required = "false";
		getElement("divisaoCliente.idDivisaoCliente").length = 1;
		getElement("divisaoCliente.idDivisaoCliente").selecttedIndex = 0;
		
	}
	
	/**
	 * Função invocada no onChange da lookup de devedorDocServFat.
	 */
	function onChangeDevedor(object){
		var retorno = devedorDocServFat_doctoServico_nrDoctoServicoOnChangeHandler();
		// Caso o documento não esteja preenchido, desabilita o novo responsável.
		validaDevedor();
				
		return retorno;
	}
	
	function validaDevedor(){
		if ( getElementValue("devedorDocServFat.doctoServico.nrDoctoServico") == "" ) {
			setDisabled("cliente.idCliente", true);
			setDisabled("divisaoCliente.idDivisaoCliente",true);
			getElement("divisaoCliente.idDivisaoCliente").selectedIndex = 0;
			getElement("cliente.pessoa.nrIdentificacao").value = "";
			getElement("cliente.pessoa.nmPessoa").value = "";
			getElement("cliente.idCliente").value = "";			
			getElement("filialByIdFilialDestino.sgFilial").value = "";
			getElement("filialByIdFilialDestino.idFilial").value = "";
			getElement("filialByIdFilialDestino.pessoa.nmFantasia").value = "";
	 	}
	}
	
</script>