<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.expedicao.digitarServicosAdicionaisAction"
	onPageLoadCallBack="myOnPageLoad" >

	<adsm:form
		action="/expedicao/digitarServicosAdicionais"
		idProperty="idServAdicionalDocServ">

		<adsm:section
			caption="servicosAdicionais"
			width="88"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%"/>

		<adsm:combobox
			property="servicoAdicional.idServicoAdicional"
			label="servicoAdicional" 
			optionLabelProperty="dsServicoAdicional"
			optionProperty="idServicoAdicional"
			required="true"
			service="lms.expedicao.digitarServicosAdicionaisAction.findServicosAdicionais"
			onchange="return habilitarCampos(this);"
			width="73%"
			labelWidth="26%"
			boxWidth="230">

			<adsm:propertyMapping
				modelProperty="dsServicoAdicional"
				relatedProperty="servicoAdicional.dsServicoAdicional"/>

			<adsm:propertyMapping
				modelProperty="cdParcelaPreco"
				relatedProperty="cdParcelaPreco"/>
		</adsm:combobox>

		<adsm:hidden
			property="servicoAdicional.dsServicoAdicional"/>

		<adsm:hidden
			property="cdParcelaPreco"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%"/>

		<adsm:textbox
			dataType="currency"
			property="vlMercadoria"
			label="valorMercadoriaReais"
			minValue="0.01"
			size="10"
			labelWidth="26%"
			width="19%"/>

		<adsm:textbox
			dataType="integer"
			property="qtCheques"
			label="qtdeCheques"
			minValue="1"
			maxValue="999999"
			size="10"
			labelWidth="26%"
			width="28%"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%"/>

		<adsm:textbox
			dataType="JTDate"
			property="dtPrimeiroCheque"
			label="dataPrimeiroCheque"
			size="10"
			labelWidth="26%"
			width="19%"/>

		<adsm:textbox
			dataType="integer"
			property="qtDias"
			label="qtdeDias"
			minValue="1"
			maxValue="999999"
			size="10"
			labelWidth="26%"
			width="28%"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%"/>

		<adsm:textbox
			dataType="integer"
			property="nrKmRodado"
			label="quilometragemRodada"
			minValue="1"
			maxValue="999999"
			size="10"
			labelWidth="26%"
			width="19%"/>

		<adsm:textbox
			dataType="integer"
			property="qtSegurancasAdicionais"
			label="quantidadeSegurancasAdicionais"
			minValue="0"
			maxValue="999999"
			size="10"
			labelWidth="26%"
			width="28%"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%"/>

		<adsm:textbox
			dataType="integer"
			property="qtColetas"
			label="quantidadeColetas"
			minValue="1"
			maxValue="999999"
			size="10"
			labelWidth="26%"
			width="19%"/>

		<adsm:textbox
			dataType="integer"
			property="qtPaletes"
			label="quantidadePaletes"
			minValue="1"
			maxValue="999999"
			size="10"
			labelWidth="26%"
			width="28%"/>

		<adsm:buttonBar
			freeLayout="true">

			<adsm:storeButton
				service="lms.expedicao.digitarServicosAdicionaisAction.storeInSession"
				id="storeButton"
				callbackProperty="storeSession"/>

			<adsm:button
				caption="novo"
				id="newButton"
				onclick="limparTela();"
				disabled="false"
				buttonType="cleanButton"/>

			<adsm:button
				caption="fechar"
				buttonType="closeButton"
				id="closeButton"
				disabled="false"
				onclick="self.close();"/>

		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		idProperty="idServAdicionalDocServ"
		rows="4"
		property="servAdicionalDocServ"
		onRowClick="populaForm"
		gridHeight="115"
		unique="true"
		service="lms.expedicao.digitarServicosAdicionaisAction.findPaginated"
		rowCountService="lms.expedicao.digitarServicosAdicionaisAction.getRowCount"
		showTotalPageCount="false">

		<adsm:gridColumn
			title="servicoSelecionado"
			property="servicoAdicional.dsServicoAdicional"
			width="100%"/>

		<adsm:buttonBar>
			<adsm:removeButton
				id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript" src="../lib/expedicao.js"></script>
<script language="javascript" type="text/javascript">
	/************************************************************\
	*
	\************************************************************/
	function isUpdate() {
		return !isBlank(getElementValue("idServAdicionalDocServ"));
	}
	/************************************************************\
	*
	\************************************************************/
	var idDocumento;
	var tpDocumento;
	function myOnPageLoad_cb() {
		onPageLoad_cb();
		var u = new URL(parent.location.href);
		idDocumento = u.parameters["idDocumento"];
		tpDocumento = u.parameters["tpDocumento"];
		if(!isBlank(idDocumento)) {
			if( isEdit() ){
				setDisabled(document, false);
			}else{ 
				setDisabled(document, true);
				setDisabled("closeButton", false);
				setFocus(getElement("closeButton"), false);
			}
		} else {
			setDisabled(document, false);
			idDocumento = undefined;
		}
		desabilitarCampos();
		populaGrid();
	}

	function isEdit(){
		if( tpDocumento == "COT" ){
			return true;
		}
		return false;
	}
	
	/************************************************************\
	*
	\************************************************************/
	function initWindow(eventObj) {
		if(eventObj.name == "removeButton_grid"){
			setDisabled("removeButton",false);
		} 
	}
	/************************************************************\
	*
	\************************************************************/
	function populaGrid() {
		var param = null;
		if(idDocumento) {
			param = {idDocumento:idDocumento};
		}
		servAdicionalDocServGridDef.executeSearch(param, true); 
	}
	/************************************************************\
	*
	\************************************************************/
	function limparTela() {
		limparCampos();
		desabilitarCampos();
		resetValue("servicoAdicional.idServicoAdicional");
		resetValue("idServAdicionalDocServ");
	}
	/************************************************************\
	*
	\************************************************************/
	function limparCampos() {
		resetValue("vlMercadoria");
		getElement("vlMercadoria").required = "false";
		resetValue("qtCheques");
		getElement("qtCheques").required = "false";
		resetValue("dtPrimeiroCheque");
		getElement("dtPrimeiroCheque").required = "false";
		resetValue("qtSegurancasAdicionais");
		getElement("qtSegurancasAdicionais").required = "false";
		resetValue("qtDias");
		getElement("qtDias").required = "false";
		resetValue("qtColetas");
		getElement("qtColetas").required = "false";
		resetValue("nrKmRodado");
		getElement("nrKmRodado").required = "false";
		resetValue("qtPaletes");
		getElement("qtPaletes").required = "false";
		setFocusOnFirstFocusableField();
	}
	/************************************************************\
	*
	\************************************************************/
	function desabilitarCampos() {
		setDisabled("vlMercadoria", true);
		setDisabled("qtCheques", true);
		setDisabled("dtPrimeiroCheque", true);
		setDisabled("qtSegurancasAdicionais", true);
		setDisabled("qtDias", true);
		setDisabled("qtColetas", true);
		setDisabled("nrKmRodado", true);
		setDisabled("qtPaletes", true);
	}
	/************************************************************\
	*
	\************************************************************/
	function storeSession_cb(data, erros) {
		if (erros != undefined){
			alert(erros);
			return false;
		}
		limparTela();
		desabilitarCampos();
		populaGrid();	
		setFocus(getElement("closeButton"),false);
	}
	/************************************************************\
	*
	\************************************************************/
	function populaForm(valor) {
		var sdo = createServiceDataObject("lms.expedicao.digitarServicosAdicionaisAction.findById", "findId", {idServAdicionalDocServ:valor} );
		xmit({serviceDataObjects:[sdo]});
		return false;
	}
	/************************************************************\
	*
	\************************************************************/
	function findId_cb(data,erros) {
		if (erros!=undefined){
			alert(erros)
			return false;
		}
		setElementValue("servicoAdicional.idServicoAdicional", data.servicoAdicional.idServicoAdicional);
		setElementValue("servicoAdicional.dsServicoAdicional", data.servicoAdicional.dsServicoAdicional);
		setElementValue("idServAdicionalDocServ", data.idServAdicionalDocServ);
		habilitarCampos(getElement("servicoAdicional.idServicoAdicional"));
		setElementValue("vlMercadoria", setFormat("vlMercadoria", data.vlMercadoria));
		setElementValue("qtCheques", data.qtCheques);
		setElementValue("dtPrimeiroCheque", setFormat(getElement("dtPrimeiroCheque"), data.dtPrimeiroCheque));
		setElementValue("qtSegurancasAdicionais", data.qtSegurancasAdicionais);
		setElementValue("qtDias", data.qtDias);
		setElementValue("qtColetas", data.qtColetas);
		setElementValue("nrKmRodado", data.nrKmRodado);
		setElementValue("qtPaletes", data.qtPaletes);
		setElementValue("cdParcelaPreco", data.cdParcelaPreco);
		setFocusOnFirstFocusableField();	
	}
	/************************************************************\
	*
	\************************************************************/
	function habilitarCampos(valCombo) {
		limparCampos();
		desabilitarCampos();
		comboboxChange({e:valCombo});
		var cdParcelaPreco = getElementValue("cdParcelaPreco").toLowerCase();
		if (cdParcelaPreco == "IDReembolso".toLowerCase()) {
			setDisabled("vlMercadoria", false);
			getElement("vlMercadoria").required = "true";
			setDisabled("qtCheques", false);
			getElement("qtCheques").required = "true";
			setDisabled("dtPrimeiroCheque", false);
			if(!isUpdate()) {
				getValorMercadoria();
			}
		} else if(cdParcelaPreco == "IDPaletizacao".toLowerCase()) {
			setDisabled("qtPaletes", false);
			getElement("qtPaletes").required = "true";
		} else if(cdParcelaPreco == "IDArmazenagem".toLowerCase()) {
			setDisabled("qtPaletes", false);
			setDisabled("qtDias", false);
			getElement("qtPaletes").required = "true";
			getElement("qtDias").required = "true";
		} else if(cdParcelaPreco == "IDEscolta".toLowerCase()) {
			setDisabled("nrKmRodado", false);
			getElement("nrKmRodado").required = "true";
			setDisabled("qtSegurancasAdicionais", false);
			getElement("qtSegurancasAdicionais").required = "true";
		} else if(cdParcelaPreco == "IDAgendamentoColeta".toLowerCase()) {
			setDisabled("qtColetas", false);
			getElement("qtColetas").required = "true";
		} else if(cdParcelaPreco.indexOf("IDEstadia".toLowerCase()) > -1) {
			setDisabled("qtDias", false);
			getElement("qtDias").required = "true";
		}
	}
	/************************************************************\
	*
	\************************************************************/
	function getValorMercadoria() {
		var service = "lms.expedicao.digitarServicosAdicionaisAction.findVlMercadoria";
		var filter = {tpDocumentoServico : dialogArguments.tpDocumentoServico};

		var sdo = createServiceDataObject(service, "getValorMercadoria", filter);
		xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function getValorMercadoria_cb(data,erros) {
		if (erros != undefined){
			alert(erros);
			return false;
		}
		if(data._value) {
			setElementValue("vlMercadoria", setFormat("vlMercadoria", data._value))
		} else {
			resetValue("vlMercadoria");
		}
	}
</script>