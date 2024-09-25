<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script>
	/*
	 * Remove as criteriaProperty da lookup.
	 */
	function myOnPageLoad(){
		onPageLoad();
		
		_serviceDataObjects = new Array();
		
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterNotasDebitoInternacionaisAction.findInitialValueItem", "initPage", null)); 
		xmit(false);					
	}	
</script>
<adsm:window onPageLoad="myOnPageLoad">
	<adsm:form action="/contasReceber/manterNotasDebitoInternacionais"
		service="lms.contasreceber.manterNotasDebitoInternacionaisAction.findByIdItemFatura"
		idProperty="idItemFatura" onDataLoadCallBack="myOnDataLoad">

		<adsm:hidden property="idCliente" />

		<adsm:masterLink showSaveAll="true" idProperty="idFatura">
			<adsm:masterLinkItem property="nrFaturaFormatada" label="fatura" />
		</adsm:masterLink>

		<adsm:combobox
			property="devedorDocServFat.doctoServico.tpDocumentoServico"
			label="documentoServico" width="35%"
			service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.findTipoDocumentoServico"
			optionProperty="value" optionLabelProperty="description"
			onchange="return tpDocumentoServicoOnChange();">

			<%--adsm:ctrc label="numeroDocumento" property="numeroDocumento" labelWidth="20%" width="30%" /--%>

			<adsm:lookup dataType="text"
				property="devedorDocServFat.doctoServico.filialByIdFilialOrigem"
				idProperty="idFilial" criteriaProperty="sgFilial"
				service="lms.contasreceber.manterNotasDebitoInternacionaisAction.findLookupFilial"
				disabled="true" action="" size="3" maxLength="3" picker="false"
				exactMatch="true" onchange="return filialOnChange();"
				onDataLoadCallBack="filialOnChange" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia"
				relatedProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" />
			<adsm:lookup dataType="integer" property="devedorDocServFat"
				idProperty="idDevedorDocServFat"
				criteriaProperty="doctoServico.nrDoctoServico"
				service="lms.contasreceber.manterNotasDebitoInternacionaisAction.findDevedorServDocFat"
				action="/contasReceber/pesquisarDevedorDocServFatLookUp" cmd="pesq"
				size="10" maxLength="8" serializable="true" disabled="true"
				exactMatch="true" mask="00000000"
				onchange="return lookUpDevedorOnChange();"
				onDataLoadCallBack="lookUpDevedor" onPopupSetValue="lookUpDevedor"
				required="true">
				<adsm:propertyMapping
					modelProperty="doctoServico.tpDocumentoServico"
					criteriaProperty="devedorDocServFat.doctoServico.tpDocumentoServico" />
				<adsm:propertyMapping
					modelProperty="doctoServico.filialByIdFilialOrigem.idFilial"
					criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping
					modelProperty="doctoServico.filialByIdFilialOrigem.sgFilial"
					criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial"
					inlineQuery="true" />
				<adsm:propertyMapping modelProperty="cliente.idCliente"
					criteriaProperty="idCliente" />
				<adsm:propertyMapping modelProperty="vlTotalDocServico"
					relatedProperty="devedorDocServFat.doctoServico.vlTotalDocServico" />
				<adsm:propertyMapping modelProperty="vlDesconto"
					relatedProperty="devedorDocServFat.desconto.vlDesconto" />
				<adsm:propertyMapping modelProperty="idDesconto"
					relatedProperty="devedorDocServFat.desconto.idDesconto" />
				<adsm:propertyMapping modelProperty="idDoctoServico"
					relatedProperty="devedorDocServFat.doctoServico.idDoctoServico" />
				<adsm:propertyMapping modelProperty="doctoServico.nrDoctoServico"
					relatedProperty="devedorDocServFat.doctoServico.nrDoctoServicoTmp" />
				<adsm:propertyMapping modelProperty="idMoeda"
					relatedProperty="devedorDocServFat.doctoServico.moeda.idMoeda" />
			</adsm:lookup>
		</adsm:combobox>

		<adsm:hidden property="devedorDocServFat.doctoServico.idDoctoServico" />
		<adsm:hidden property="devedorDocServFat.doctoServico.moeda.idMoeda" />
		<adsm:hidden
			property="devedorDocServFat.doctoServico.nrDoctoServicoTmp" />
		<adsm:hidden
			property="devedorDocServFat.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" />
		<adsm:hidden
			property="devedorDocServFat.desconto.tpSituacaoAprovacaoVal" />
		<adsm:hidden property="devedorDocServFat.desconto.idDesconto" />



		<adsm:textbox label="valorDocumento" dataType="currency" size="10"
			property="devedorDocServFat.doctoServico.vlTotalDocServico"
			disabled="true" maxLength="18" />
		<adsm:textbox label="valorDesconto" dataType="currency" size="10"
			property="devedorDocServFat.desconto.vlDesconto" disabled="false"
			maxLength="18" />
		<adsm:textbox label="situacaoDesconto" dataType="text" size="20"
			property="devedorDocServFat.desconto.tpSituacaoAprovacaoDesc"
			disabled="true" maxLength="30" />

		<adsm:section caption="totaisFatura" />

		<adsm:textbox dataType="integer" label="qtdeTotalDocumentos"
			property="qtdeTotalDocumentos" size="10" labelWidth="20%"
			maxLength="6" disabled="true" width="13%" />
		<adsm:textbox dataType="currency" label="valorTotalDocumentos"
			property="valorTotalDocumentos" size="10" labelWidth="21%"
			maxLength="18" disabled="true" width="12%" />
		<adsm:textbox dataType="currency" label="valorTotalDesconto"
			property="valorTotalDesconto" size="10" labelWidth="21%"
			maxLength="18" width="13%" disabled="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton id="storeButton" caption="salvarDocumentoServico"
				service="lms.contasreceber.manterNotasDebitoInternacionaisAction.storeItemFatura" />
			<adsm:newButton onclick="return myNewButtonScript();" />
			<adsm:button caption="consultaDocumentoServico" boxWidth="200"
				action="/contasReceber/consultarDadosCobrancaDocumentoServico"
				cmd="main" />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idItemFatura" property="itemFatura"
		unique="true" autoSearch="false" rows="5" showGotoBox="true"
		showPagging="true" detailFrameName="documentoServico"
		service="lms.contasreceber.manterNotasDebitoInternacionaisAction.findPaginatedItemFatura"
		rowCountService="lms.contasreceber.manterNotasDebitoInternacionaisAction.getRowCountItemFatura">

		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="documentoServico" isDomain="true"
				property="tpDocumentoServico" width="100" />
			<adsm:gridColumn title="" property="sgFilial" width="100" />
			<adsm:gridColumn title="" property="nrDoctoServico" width="200" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="valorDocumento" dataType="currency"
			property="vlTotalDocServico" width="20%" />
		<adsm:gridColumn title="valorDesconto" dataType="currency"
			property="vlDesconto" width="20%" />
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"
				caption="excluirDocumentoServico"
				service="lms.contasreceber.manterNotasDebitoInternacionaisAction.removeByIdsItemFatura" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	var constTpSituacaoAprovacao = new Array();
	document.getElementById("idCliente").masterLink = "true";	
	document.getElementById("valorTotalDocumentos").masterLink = "true";
	document.getElementById("valorTotalDesconto").masterLink = "true";
	
	/*
	 * Monta as duas constantes que tem a lista de situação de fatura	
	 */
	function initPage_cb(d,e,o){
		if (d != null) {	
			constTpSituacaoAprovacao = d;		
		}
	}
	
	/*
	 * On change da combo de Tipo de Documento de Serivo.<BR>
	 * Altera lookup de conhecimento
	 * @see changeLookupConhecimento
	 */
	function tpDocumentoServicoOnChange(){
		filialOnChange();		
		resetValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial");
	}
	
	/*
	 * On Change da lookup de Filial
	 */
	function resetDevedor(){
		disableDocumentoServico();
		resetValue("devedorDocServFat.idDevedorDocServFat");
		resetValue("devedorDocServFat.doctoServico.nrDoctoServico");		
	}	
	
	/*
	 * On Change da lookup de Filial
	 */
	function filialOnChange(){
		resetDevedor();
		return devedorDocServFat_doctoServico_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}	
	
	/*
	 * On Change callBack da lookup de Filial
	 */	
	function filialOnChange_cb(data,e,c,x){
		resetDevedor();
		if (data.length == 1) {			
			setDisabled("devedorDocServFat.idDevedorDocServFat",false);
			setMaskNrDocumento("devedorDocServFat.doctoServico.nrDoctoServico", getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico"))					
	
			__lookupSetValue({e:getElement("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"), data:data[0]});
			return true;
		} else {
			alert(lookup_noRecordFound);
		}		
	}
	
	/*
	 * Habilita ou desabilita os componentes da tag documento de serviço
	 */
	function disableDocumentoServico(){
		//Se não tem tipo de documento, desabilitar a filial
		if (getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico") == "") {
			setDisabled("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",true);
			setDisabled("devedorDocServFat.idDevedorDocServFat",true);
		} else {
			setDisabled("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",false);
		}

		//Se não tem filial, desabilitar o documento de serviço
		if (getElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial") == ""){
			setDisabled("devedorDocServFat.idDevedorDocServFat",true);
		} else {
			setDisabled("devedorDocServFat.idDevedorDocServFat",false);		
		}
	}	
	
	function lookUpDevedor(data, dialogWindow){		
		if (data.tpSituacaoAprovacao != undefined){	
			setElementValue("devedorDocServFat.desconto.tpSituacaoAprovacaoDesc", data.tpSituacaoAprovacao.description);
			setElementValue("devedorDocServFat.desconto.tpSituacaoAprovacaoVal", data.tpSituacaoAprovacao.value);	
		}
		disableDesconto(data.idDesconto);
	}
		
	function lookUpDevedor_cb(data){
		devedorDocServFat_doctoServico_nrDoctoServico_exactMatch_cb(data)
		if (data.length <= 0){
			return;
		}
		if (data.length > 1){
			lookupClickPicker({e:document.forms[0].elements['devedorDocServFat.idDevedorDocServFat']});
		} else {
			if (data[0].tpSituacaoAprovacao != undefined){	
				setElementValue("devedorDocServFat.desconto.tpSituacaoAprovacaoDesc", data[0].tpSituacaoAprovacao.description);
				setElementValue("devedorDocServFat.desconto.tpSituacaoAprovacaoVal", data[0].tpSituacaoAprovacao.value);			
			}
			disableDesconto(data[0].idDesconto);
		}
	}
	
	function lookUpDevedorOnChange(){
		if (getElementValue("devedorDocServFat.doctoServico.nrDoctoServico") == ""){
			resetValue("devedorDocServFat.desconto.tpSituacaoAprovacaoDesc");
			resetValue("devedorDocServFat.desconto.tpSituacaoAprovacaoVal");	
		}
		return devedorDocServFat_doctoServico_nrDoctoServicoOnChangeHandler();
	}
		
	
	function myOnShow(e){
		var frameCad = getTabGroup(this.document).getTab("cad").tabOwnerFrame
		setElementValue("idCliente", getElementValue(frameCad.document.getElementById("cliente.idCliente")));
		return false;
	}
	
	function myOnDataLoad_cb(d,e,c,x){
		onDataLoad_cb(d,e,c,x);
		disableDesconto(getElementValue("devedorDocServFat.desconto.idDesconto"));
		populaSomatorios();
	}
	
	function disableDesconto(value){
		var blDisable = true;
		if (value == undefined || value ==  ""){
			blDisable = false;
		}
		setDisabled("devedorDocServFat.desconto.vlDesconto",blDisable);
		setDisabled("devedorDocServFat.desconto.obDesconto",blDisable);			
		setDisabled("devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto",blDisable);
	}
	
	function myNewButtonScript(){
		newButtonScript();
		populaSomatorios();
	}
	
	/** Função para buscar os somatórios dos itens de fatura */
	
	function populaSomatorios(){
	      var remoteCall = {serviceDataObjects:new Array()};
	      var dataCall = createServiceDataObject("lms.contasreceber.manterNotasDebitoInternacionaisAction.findSomatorios", "populaSomatorios", 
	            {
	                  idFatura:getElementValue("masterId")
	            }
	      );
	      remoteCall.serviceDataObjects.push(dataCall);
	      xmit(remoteCall);  
	
	}
	
	/** Função (callBack) para popular os dados dos somatórios */
	
	function populaSomatorios_cb(data, error, erromsg){
		if (error != undefined) {
			alert(error+'');
			return;
		}
		
		setElementValue(getElement("valorTotalDocumentos"), setFormat(getElement("valorTotalDocumentos"), data.valorTotalDocumentos));
		setElementValue(getElement("valorTotalDesconto"), setFormat(getElement("valorTotalDesconto"), data.valorTotalDesconto));		
	    setElementValue("qtdeTotalDocumentos",data.qtdeTotalDocumentos);
	    	    
	}
	
	function initWindow(eventObj){
		if(eventObj.name == "tab_click" || eventObj.name == "storeItemButton" || eventObj.name == "removeButton_grid"){
		    populaSomatorios();
			//disabledButtonsItens();		    
		}
	}

	function setMaskNrDocumento(element, type){
		var obj;
		obj = getElement(element);
		
		switch(type) {
		case "CRT":
			obj.mask = "000000";
			break;
		case "CTR":
			obj.mask = "00000000";
			break;
		case "NDN":
			obj.mask = "0000000000";
			break;
		case "NFS":
			obj.mask = "000000";
			break;		
		case "NFT":
			obj.mask = "00000000";			
			break;
		}
	}

	
</script>
