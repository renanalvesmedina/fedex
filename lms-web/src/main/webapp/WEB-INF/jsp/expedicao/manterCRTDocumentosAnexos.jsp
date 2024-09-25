<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	onPageLoad="myOnPageLoad"
	service="lms.expedicao.manterCRTDocumentosAnexosAction">
	<adsm:form
		action="/expedicao/manterCRTDocumentosAnexos">

		<adsm:section
			caption="documentosAnexos17"
			width="80"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%" />

		<adsm:combobox
			property="anexoDoctoServico.idAnexoDoctoServico"
			label="documento"
			optionLabelProperty="dsAnexoDoctoServico"
			optionProperty="idAnexoDoctoServico"
			onchange="setDsAnexoDoctoServico(this)"
			service="lms.expedicao.manterCRTDocumentosAnexosAction.findComboAnexoDoctoServico"
			width="20%"
			required="true"
			onlyActiveValues="true"
			labelWidth="15%">
			<adsm:hidden
				property="anexoDoctoServico.dsAnexoDoctoServico"/>
		</adsm:combobox>

		<adsm:textbox
			dataType="text"
			property="dsDocumento"
			label="numero"
			size="10"
			maxLength="20"
			labelWidth="20%"
			required="true"
			width="15%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button
				disabled="false"
				id="btnSalvar"
				onclick="storeInSession()"
				caption="salvar"/>

			<adsm:button 
				caption="limpar"
				disabled="false"
				id="btnLimpar"
				onclick="limpar()"/>

			<adsm:button
				caption="fechar"
				buttonType="closeButton"
				id="closeButton"
				disabled="false"
				onclick="self.close();"/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid
		idProperty="idDocumentoAnexo"
		property="documentoAnexo"
		showGotoBox="false"
		onDataLoadCallBack="gridOnDataLoadCallBack"
		onRowClick="nothing"
		rows="5"
		unique="true"
		mode="main"
		showTotalPageCount="false">

		<adsm:gridColumn
			title="documento"
			property="anexoDoctoServico.dsAnexoDoctoServico"
			width="60%"/>

		<adsm:gridColumn
			title="numero"
			property="dsDocumento"
			width="40%"/>

		<adsm:buttonBar>
			<adsm:removeButton
				disabled="false"
				id="btnRemove" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
	function storeInSession(){
		var service = "lms.expedicao.manterCRTDocumentosAnexosAction.storeInSession";
		storeButtonScript(service, 'storeSession', document.forms[0]);
	}
	/************************************************************\
	*
	\************************************************************/
    function limparCampos(){
    	var objForm = document.forms[0];
    	for(var i = 0; i < objForm.elements.length; i++){
    		resetValue(objForm.elements[i].id);
    	}
    	setFocusOnFirstFocusableField();
    }
	/************************************************************\
	*
	\************************************************************/
	function storeSession_cb(data,erros){
		if (erros != undefined){
			alert(erros);
			return false;
		}

		limparCampos();
		populaGrid();
		setFocus(document.getElementById("closeButton"), false);

		notifyModified();

		return true;
	}
	/************************************************************\
	*
	\************************************************************/
	function populaGrid() {
		documentoAnexoGridDef.executeSearch({},true);
	}
	/************************************************************\
	*
	\************************************************************/
	function myOnPageLoad(){
		onPageLoad();
		populaGrid();
    }
    /************************************************************\
	*
	\************************************************************/
	function setDsAnexoDoctoServico(objTrigger){
		var value = '';

		if(objTrigger.value != ''){
			value = objTrigger.options[objTrigger.selectedIndex].text;
		}

		setElementValue('anexoDoctoServico.dsAnexoDoctoServico', value);
	}
	/************************************************************\
	*
	\************************************************************/
	function gridOnDataLoadCallBack_cb(data, error){
		var modo = dialogArguments.MODO_TELA;
		if(modo == 'VISUALIZACAO'){
			setDisabled(document, true);
			setDisabled('closeButton', false);
		} else {
			setDisabled('btnSalvar', false);
			setDisabled('btnLimpar', false);
		}
		setFocusOnFirstFocusableField();
	}
	/************************************************************\
	*
	\************************************************************/
	function nothing(){	return false; }
	/************************************************************\
	*
	\************************************************************/
	function limpar() {
		newButtonScript(document, false);
	}
	/************************************************************\
	*
	\************************************************************/
	function initWindow(e){
		notifyModified();
	}
	/************************************************************\
	*
	\************************************************************/
	function notifyModified(){
		window.returnValue = "MODIFICOU_DADOS";
	}
</script>