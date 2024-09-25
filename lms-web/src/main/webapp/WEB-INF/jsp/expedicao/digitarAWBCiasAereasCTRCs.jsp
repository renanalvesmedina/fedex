<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	title="digitarPreAWBIncluirExcluirCTRCs"
	service="lms.expedicao.digitarAWBCiasAereasCTRCsAction"
	onPageLoadCallBack="myOnPageLoad">
	<adsm:form
		action="/expedicao/digitarAWBCiasAereasCTRCs">

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
			width="42%"
			boxWidth="10%"
			service="lms.expedicao.digitarAWBCiasAereasCTRCsAction.findTipoDocumento"
			onchange="return tipoDocumentoChange();"
			label="doctoServico"
			labelWidth="10%">
			<adsm:lookup
				required="true"
				dataType="text"
				property="doctoServicoOriginal.filialByIdFilialOrigem"
				idProperty="idFilial"
				criteriaProperty="sgFilial"
				disabled="true"
				service="lms.expedicao.digitarAWBCiasAereasCTRCsAction.findFilialConhecimento"
				onDataLoadCallBack="filialConhecimento"
				onchange="return filialConhecimentoChange();"
				action="/municipios/manterFiliais"
				serializable="false"
				size="3"
				maxLength="3"
				picker="false">
				
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
				size="8" mask="00000000"
					maxLength="8"
				onchange="return nrConhecimentoChange(this);"
					property="doctoServicoOriginal.nrConhecimento"
					serializable="false">
					 	
				
					<adsm:label key="hifen"/>
					<adsm:lookup
						dataType="integer"
						property="doctoServicoOriginal"
						popupLabel="pesquisarNumeroCTRC"
						idProperty="idDoctoServico"
						criteriaProperty="dvConhecimento" 
						service="lms.expedicao.digitarAWBCiasAereasCTRCsAction.findDoctoServico"
						action="/expedicao/pesquisarConhecimento"
						onchange="return doctoServicoOriginalChange()"
						onDataLoadCallBack="numCtrcOnDataLoadCallBack"
						onPopupSetValue="listenerPopupDataCallBack"
					afterPopupSetValue="afterDataLoad"
						size="1"
						maxLength="1"
						disabled="true">
						
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
			width="31%">
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

		<adsm:buttonBar freeLayout="true">
			<adsm:button
				caption="salvar"
				id="btnSalvar"
				disabled="false"
				onclick="salvar()"/>
			<adsm:newButton
				id="newButton"/>
			<adsm:button
				caption="fechar"
				buttonType="closeButton"
				id="closeButton"
				disabled="false"
				onclick="self.close();"/>
		</adsm:buttonBar>
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-04082"/>
			<adsm:include key="numeroCTRC"/>
		</adsm:i18nLabels>

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
		<adsm:gridColumn title="aeroportoDeDestino" property="sgAeroportoDestino" width="20%"/>
		<adsm:gridColumn title="peso" property="psReal" width="16%" align="right" unit="kg"/>
		<adsm:gridColumn title="pesoCubado" property="psAforado" width="17%" align="right" unit="kg"/>
		<adsm:gridColumn title="qtdeVolumes" property="qtVolumes" width="17%" align="right" />
		<adsm:gridColumn title="valorMerc" property="vlMercadoria" width="16%" align="right" dataType="currency" unit="reais"/>
		<adsm:buttonBar>
			<adsm:removeButton
				id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type='text/javascript'>

	function salvar(){
		var nm = "doctoServicoOriginal";
		var nrF = getElement(nm + ".filialByIdFilialOrigem.idFilial");
		var nrC = getElement(nm + ".nrConhecimento");
		var nrD = getElement(nm + ".dvConhecimento");
		var tpDoctoServico = getElementValue('tpDocumentoServico');
		var isSalvar;

		if(tpDoctoServico == 'CTE'){
			nrF.required = nrC.required = true;
			isSalvar = (nrF.value != '' && nrC.value != '');
		} else {
		nrF.required = nrC.required = nrD.required = true;
			isSalvar = (nrF.value != '' && nrC.value != '' && nrD.value != '');
		}

		if(isSalvar){
			var service = 'lms.expedicao.digitarAWBCiasAereasCTRCsAction.storeInSession';
			storeButtonScript(service, 'storeSession', document.forms[0]);
		} else {
			alert(getMessage(erRequired, [i18NLabel.getLabel('numeroCTRC')]));
			setFocus(nm + '.filialByIdFilialOrigem.sgFilial', false);
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function storeSession_cb(data, erros){
		if (erros != undefined){
			alert(erros);
			return false;
		}
		limparCampos();
		populaGrid();

		var nm = "doctoServicoOriginal";
		setDisabled(nm + ".idDoctoServico", true);
		setDisabled(nm + ".nrConhecimento", true);
		setDisabled(nm + ".dvConhecimento", true);

		setFocus(document.getElementById("closeButton"), false);
		return true;
	}
	/************************************************************\
	*
	\************************************************************/
	function myOnPageLoad_cb(){
		onPageLoad_cb();

		var o = getElement("doctoServicoOriginal.nrConhecimento");
		o.required = true;
		o.label = i18NLabel.getLabel('numeroCTRC');
		setDisabled("newButton", false);
		populaGrid();
		
		setDisabled('doctoServicoOriginal.dvConhecimento', true);
		setDisabled(getElement('doctoServicoOriginal.idDoctoServico'), true);
		setDisabled(getElement('doctoServicoOriginal.filialByIdFilialOrigem.idFilial'), true);
    }
	/************************************************************\
	*
	\************************************************************/
	function filialConhecimento_cb(data) {
		disableControl(data);
		return doctoServicoOriginal_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	}
	/************************************************************\
	*
	\************************************************************/
	function disableControl(data){
		var tpDoctoServico = getElementValue('tpDocumentoServico');
		var isAction = (data == undefined || data.length == 0);
		var nm = "doctoServicoOriginal";
		
		if(isAction) resetValue(nm + ".nrConhecimento");
		setDisabled(nm + ".nrConhecimento", isAction);
		setDisabled(nm + ".idDoctoServico", isAction);
		
		if(tpDoctoServico == 'CTE' || isAction){
			setDisabled(nm + ".dvConhecimento", true);
		} else {
			setDisabled(nm + ".dvConhecimento", false);			
	}
	}
	/************************************************************\
	*
	\************************************************************/
	
	function tipoDocumentoChange() {
		var tpDoctoServico = getElementValue('tpDocumentoServico');
		var isDisable = (tpDoctoServico == '');
		
		resetValue('doctoServicoOriginal.nrConhecimento');
		resetValue('doctoServicoOriginal.filialByIdFilialOrigem.sgFilial');
		resetValue('doctoServicoOriginal.dvConhecimento');
		setDisabled(getElement('doctoServicoOriginal.idDoctoServico'), isDisable);
		setDisabled(getElement('doctoServicoOriginal.filialByIdFilialOrigem.idFilial'), isDisable);
		setDisabled('doctoServicoOriginal.nrConhecimento', isDisable);
				
		if(tpDoctoServico == 'CTE' || isDisable){
			setDisabled('doctoServicoOriginal.dvConhecimento', true);
		} else {
			setDisabled('doctoServicoOriginal.dvConhecimento', false);			
	}
	}
	
	function filialConhecimentoChange() {
		var isDisable = getElementValue("doctoServicoOriginal.filialByIdFilialOrigem.sgFilial") == "";
		var tpDoctoServico = getElementValue('tpDocumentoServico');
		var nm = "doctoServicoOriginal";

		if(isDisable) {
			resetValue(nm + ".nrConhecimento");
			setDisabled(nm + ".nrConhecimento", true);
		}

		if(tpDoctoServico == 'CTE' || isDisable){
			setDisabled(nm + ".dvConhecimento", true);
		} else {
			setDisabled(nm + ".dvConhecimento", false);			
		}

		return doctoServicoOriginal_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}
	/************************************************************\
	*
	\************************************************************/
	function doctoServicoOriginalChange() {
		var nm = "doctoServicoOriginal";
		var dv = getElementValue(nm + ".dvConhecimento");

		if(dv != '') {
			var dvGer = getDigitoVerificadorCTRC(getElementValue(nm + ".nrConhecimento"));
			if(dv != dvGer) {
				alert(i18NLabel.getLabel('LMS-04082'));
				return false;
			}
		}
		return doctoServicoOriginal_dvConhecimentoOnChangeHandler();
	}
	/************************************************************\
	*
	\************************************************************/
	function getDigitoVerificadorCTRC(nbr) {
		var n = parseInt(stringToNumber(nbr));
		n = (n - (parseInt(n / 11) * 11)).toString().substring(0, 1);

		return n > 9 ? 0 : n;
	}
	/************************************************************\
	*
	\************************************************************/
    function limparCampos() {
    	var objForm = document.forms[0];
    	for(var i = 0; i < objForm.elements.length; i++){
    		resetValue(objForm.elements[i].id);
    	}
    	setDisabled(getElement('doctoServicoOriginal.idDoctoServico'), true);
    	setDisabled(getElement('doctoServicoOriginal.filialByIdFilialOrigem.idFilial'), true);
    	setFocusOnFirstFocusableField();
    }
    /************************************************************\
	*
	\************************************************************/
	function populaGrid() {
		ctoAwbGridDef.executeSearch({},true);
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
	function numCtrcOnDataLoadCallBack_cb(data){
		doctoServicoOriginal_dvConhecimento_exactMatch_cb(data);
		
		if(data != undefined) {
			setFocus("btnSalvar", false);
		}
		return;
	}
	/************************************************************\
	*
	\************************************************************/
	function gridOnDataLoadCallBack_cb(){
    	setFocus("closeButton");
    }
    /************************************************************\
	*
	\************************************************************/
    function listenerPopupDataCallBack(data){
	    disableControl([data]);

	    if(data && data != undefined){
	    	setElementValue('doctoServicoOriginal.filialByIdFilialOrigem.sgFilial', data.sgFilialOrigem);
	    	setElementValue('doctoServicoOriginal.filialByIdFilialOrigem.idFilial', data.idFilialOrigem);
	    	setElementValue('filialByIdFilialOrigem.idFilial', data.idFilialOrigem);
	    	setElementValue('sgAeroporto', data.sgAeroporto);
	    	setElementValue('aeroportoByIdAeroportoDestino.pessoa.nmPessoa', data.nmPessoa);

	    	if(data.dvConhecimento){
		    	setElementValue('doctoServicoOriginal.dvConhecimento', data.dvConhecimento);
	    }
	    }
		numCtrcOnDataLoadCallBack_cb([data]);
    }
    /************************************************************\
	*
	\************************************************************/
    function initWindow(eventObj){
    	switch(eventObj.name){
    		case 'removeButton_grid':
    			setDisabled(getElement('doctoServicoOriginal.idDoctoServico'), true);
    			setDisabled(getElement('doctoServicoOriginal.filialByIdFilialOrigem.idFilial'), true);
    			setDisabled('btnSalvar', false);
    			break;
    		case 'newButton_click':
    			setDisabled('btnSalvar', false);
    			setDisabled(getElement('doctoServicoOriginal.idDoctoServico'), true);
    			setDisabled(getElement('doctoServicoOriginal.filialByIdFilialOrigem.idFilial'), true);
    			setDisabled('doctoServicoOriginal.dvConhecimento', true);
    			break;
    	}
    }
    
   
    function nrConhecimentoChange(data) {
    	var nrConhecimento = getElementValue("doctoServicoOriginal.nrConhecimento");
    	var filter = new Array();

    	if(nrConhecimento != ""){
	    	setNestedBeanPropertyValue(filter, "tpDocumentoServico", getElementValue("tpDocumentoServico"));
	    	setNestedBeanPropertyValue(filter, "filialByIdFilialOrigem.idFilial", getElementValue("filialByIdFilialOrigem.idFilial"));
	    	setNestedBeanPropertyValue(filter, "nrConhecimento", getElementValue("doctoServicoOriginal.nrConhecimento"));
	    	
	    	var sdo = createServiceDataObject("lms.expedicao.digitarAWBCiasAereasCTRCsAction.findDoctoServico", "loadRelated", filter);
	    	xmit({serviceDataObjects:[sdo]});
    	} else {
    		resetValue('doctoServicoOriginal.dvConhecimento');
    		resetValue('sgAeroporto');
    		resetValue('aeroportoByIdAeroportoDestino.pessoa.nmPessoa');
    	}
    }
    
    function loadRelated_cb(data, error) {
    	if(error != undefined) {
    		alert(error);
    		return;
    	}
    	
    	if(data && data != undefined){
	    	setElementValue('doctoServicoOriginal.idDoctoServico', data[0].idDoctoServico);
	    	setElementValue('nrConhecimento', data[0].nrConhecimento);
	    	setElementValue('sgAeroporto', data[0].sgAeroporto);
	    	setElementValue('aeroportoByIdAeroportoDestino.pessoa.nmPessoa', data[0].nmPessoa);
    	}
    }
    
    function afterDataLoad(data){
    	var tpDoctoServico = getElementValue('tpDocumentoServico');
    	
    	if(tpDoctoServico == 'CTR'){
	    	setElementValue('doctoServicoOriginal.dvConhecimento', data.dvConhecimento);
    	} else {
    		setElementValue('doctoServicoOriginal.dvConhecimento', '');
    	}
    }
</script>