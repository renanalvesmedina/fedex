<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.fretecarreteiroviagem.manterOcorrenciasReciboAction" >
	<adsm:form action="/freteCarreteiroViagem/manterOcorrenciasRecibo"
			idProperty="idOcorrenciaFreteCarreteiro" onDataLoadCallBack="onDataLoadCustom"  >

		<adsm:hidden property="dtInclusao" serializable="true" />

		<adsm:lookup dataType="text" property="reciboFreteCarreteiro.filial"
				idProperty="idFilial" criteriaProperty="sgFilial"
				service="lms.fretecarreteiroviagem.manterOcorrenciasReciboAction.findLookupFilial"
				action="/municipios/manterFiliais" label="filialOrigem" size="3"
				maxLength="3" labelWidth="18%" width="32%"
				onchange="return filialChange(this);" onPopupSetValue="popupFilial" exactMatch="true"
				required="true" serializable="false">
			<adsm:propertyMapping
					relatedProperty="reciboFreteCarreteiro.filial.pessoa.nmFantasia"
					modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping
					relatedProperty="reciboFreteCarreteiro2.filial.sgFilial"
					modelProperty="sgFilial" />
			<adsm:textbox dataType="text"
					property="reciboFreteCarreteiro.filial.pessoa.nmFantasia" size="30"
					disabled="true" serializable="false" />
		</adsm:lookup>
		
		<adsm:combobox property="tpReciboFreteCarreteiro" domain="DM_TIPO_RECIBO_PAGAMENTO_FRETE_CARRETEIR"
				label="tipoRecibo" labelWidth="18%" width="32%" onchange="return tpReciboChange(this);" />
		
		<%--Lookup Recibo Frete Carreteiro ------------------------------------------------------------------------------------------------------%>

		<adsm:textbox dataType="text"
				property="reciboFreteCarreteiro2.filial.sgFilial" label="recibo"
				size="3" maxLength="3" labelWidth="18%" width="32%"
				cellStyle="vertical-align=bottom;" disabled="true"
				serializable="false">
			<adsm:lookup dataType="integer" property="reciboFreteCarreteiro"
					idProperty="idReciboFreteCarreteiro"
					criteriaProperty="nrReciboFreteCarreteiro"
					service="lms.fretecarreteiroviagem.manterOcorrenciasReciboAction.findLookupRecibo"
					action="/freteCarreteiroViagem/manterRecibos"
					onDataLoadCallBack="reciboDataLoad" onPopupSetValue="reciboPopup"
					onchange="return reciboChange(this);" size="10" maxLength="10"
					cellStyle="vertical-align=bottom;" exactMatch="true"
					mask="0000000000" picker="true" serializable="true" disabled="true" required="true">
				<adsm:propertyMapping modelProperty="filial.sgFilial"
						criteriaProperty="reciboFreteCarreteiro.filial.sgFilial"
						inlineQuery="false" />

				<adsm:propertyMapping modelProperty="filial.idFilial"
						criteriaProperty="reciboFreteCarreteiro.filial.idFilial" />
				<adsm:propertyMapping modelProperty="filial.sgFilial"
						criteriaProperty="reciboFreteCarreteiro.filial.sgFilial"
						inlineQuery="false" />
				<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia"
						criteriaProperty="reciboFreteCarreteiro.filial.pessoa.nmFantasia"
						inlineQuery="false" />

				<adsm:propertyMapping modelProperty="controleCarga.sgFilialOrigem"
						relatedProperty="controleCarga.sgFilial" />
				<adsm:propertyMapping modelProperty="controleCarga.nrControleCarga"
						relatedProperty="controleCarga.nrControleCarga" />

				<adsm:propertyMapping criteriaProperty="tpReciboFreteCarreteiro"
						modelProperty="tpReciboFreteCarreteiro" />
						
				<adsm:propertyMapping modelProperty="tpSituacaoRecibo.description" 
	        			relatedProperty="tpSituacaoRecibo.description" />
	        			
	        	<adsm:propertyMapping modelProperty="blComplementar" relatedProperty="blComplementar" />
			</adsm:lookup>
		</adsm:textbox>

		<adsm:checkbox property="blComplementar" serializable="false"
        		label="complementar" labelWidth="18%" width="32%" disabled="true" />
		<%--Lookup Recibo Frete Carreteiro ------------------------------------------------------------------------------------------------------%>

		<adsm:textbox dataType="text" property="tpSituacaoRecibo.description"
				label="situacaoRecibo" size="26" width="82%" labelWidth="18%" disabled="true" />

		<adsm:textbox dataType="text" property="controleCarga.sgFilial"
				label="controleCarga" size="3" labelWidth="18%" width="32%"
				disabled="true" serializable="false">
			<adsm:textbox dataType="integer"
					property="controleCarga.nrControleCarga" size="8" disabled="true"
					serializable="false" mask="00000000" />
		</adsm:textbox>
				
		<adsm:combobox property="tpOcorrencia"
				domain="DM_TIPO_OCORRENCIA_RECIBO_CARRETEIRO" label="ocorrencia"
				labelWidth="18%" width="32%" required="true"
				onchange="return tpOcorrenciaChange(this);" />

		<adsm:textbox dataType="JTDate" property="dtOcorrenciaFreteCarreteiro"
				label="dataOcorrencia" labelWidth="18%" width="32%" disabled="true"
				required="true" />

		<adsm:textbox dataType="text" property="reciboFreteCarreteiro.dsMoeda"
				label="moeda" size="8" width="32%" labelWidth="18%" disabled="true"
				required="true" />

		<adsm:textbox dataType="currency"
				property="reciboFreteCarreteiro.vlBruto" label="valorRecibo"
				size="20" maxLength="10" width="32%" labelWidth="18%" disabled="true" />

		<adsm:listbox property="descontos" optionProperty="fakeId"
				optionLabelProperty="_value" size="3" label="descontos"
				labelWidth="18%" width="82%" boxWidth="120" />
		<adsm:hidden property="somaDescontos" />

		<adsm:textbox dataType="currency" property="vlDesconto"
				label="valorDesconto" size="20" maxLength="10" width="32%"
				labelWidth="18%" disabled="true"
				onchange="return onVlDescontoChange(this);" />
		<adsm:textbox dataType="currency" property="valorReciboComDesconto"
				label="valorReciboComDesconto" size="20" maxLength="10" width="32%"
				labelWidth="18%" disabled="true" />

		<adsm:checkbox property="blDescontoCancelado" label="descontoCancelado"
				labelWidth="18%" width="82%" disabled="true" />

		<adsm:textarea maxLength="500" property="obMotivo" label="motivo"
				labelWidth="18%" width="82%" columns="97" required="true" />

		<adsm:buttonBar>
			<adsm:button caption="cancelarDesconto" id="btnCancelar"
					service="lms.fretecarreteiroviagem.manterOcorrenciasReciboAction.storeCancelarCancelarDesconto"
					callbackProperty="storeCancelarRecibo" />
			<adsm:storeButton id="btnSalvar"
					service="lms.fretecarreteiroviagem.manterOcorrenciasReciboAction.storeCustom"
					callbackProperty="afterStore" />
			<adsm:newButton />
			<adsm:removeButton id="btnExcluir" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
<!--

	var reciboCache = new Array();
	var valoresRecibo = new Array();
	
	function populateFilial() {
		var tabGroup = getTabGroup(this.document);
		var tabPesq = tabGroup.getTab("pesq");
		setElementValue("reciboFreteCarreteiro.filial.idFilial",tabPesq.getFormProperty("idFilialSessao"));
		setElementValue("reciboFreteCarreteiro.filial.sgFilial",tabPesq.getFormProperty("sgFilialSessao"));
		setElementValue("reciboFreteCarreteiro2.filial.sgFilial",tabPesq.getFormProperty("sgFilialSessao"));
		setElementValue("reciboFreteCarreteiro.filial.pessoa.nmFantasia",tabPesq.getFormProperty("nmFilialSessao"));
	}
	
	var newState = false;
	
	function initWindow(eventObj) {
		var blDetalhamento = (eventObj.name == "storeButton" || eventObj.name == "gridRow_click");
				
		if (!blDetalhamento) {
			setDisabled("reciboFreteCarreteiro.filial.idFilial",false);		
			if (!document.getElementById('reciboFreteCarreteiro.filial.idFilial').masterLink) {
				populateFilial();
			}
			if (isMasterLinkCall()) {
				if (reciboCache.id == undefined) {
					newState = true;
					lookupChange({e:document.forms[0].elements["reciboFreteCarreteiro.idReciboFreteCarreteiro"],forceChange:true});
				} else {
					preencheReciboRelateds();
				}
			}
			setFocusOnFirstFocusableField();
		}
		
		setDisabled("reciboFreteCarreteiro.filial.idFilial",blDetalhamento || isMasterLinkCall());
//		setDisabled("reciboFreteCarreteiro.idReciboFreteCarreteiro",blDetalhamento || isMasterLinkCall());
		setDisabled("reciboFreteCarreteiro.idReciboFreteCarreteiro",true);
		setDisabled("tpReciboFreteCarreteiro",blDetalhamento || isMasterLinkCall());
		setDisabled("vlDesconto",true);
		setDisabled("tpOcorrencia",blDetalhamento);
		setDisabled("obMotivo",blDetalhamento);
		
		if (!blDetalhamento)
			setFocusOnFirstFocusableField();
	}	
	
	function reciboDataLoad_cb(data,exception) {
		if (data != undefined && data.length == 1) {
			var id = getNestedBeanPropertyValue(data,":0.idReciboFreteCarreteiro");
			
			if (isMasterLinkCall()) {
				reciboCache.id = id;
				reciboCache.sgFilialOrigem = getNestedBeanPropertyValue(data,":0.controleCarga.sgFilialOrigem");
	        	reciboCache.nrControleCarga = getNestedBeanPropertyValue(data,":0.controleCarga.nrControleCarga");
	        	reciboCache.tpSituacaoRecibo = getNestedBeanPropertyValue(data,":0.tpSituacaoRecibo.description");
			}
			
			if (id != undefined && id != "") {
				findValoresRecibo(id,getElementValue("idOcorrenciaFreteCarreteiro"),"findValoresRecibo");
			}
		} else {
			resetAllRelatedValues();
		}
		
		var retorno = reciboFreteCarreteiro_nrReciboFreteCarreteiro_exactMatch_cb(data);
		if (newState) {
			var tabGroup = getTabGroup(this.document);
			var tabCad = tabGroup.getTab("cad");
			tabCad.changed = false;
			newState = false;
		}
		
		return retorno;
	}

	function reciboPopup(data,dialogWindow) {
		if (data != undefined) {
			var id = getNestedBeanPropertyValue(data,":idReciboFreteCarreteiro");
			if (id != undefined && id != "") {
				findValoresRecibo(id,getElementValue("idOcorrenciaFreteCarreteiro"),"findValoresRecibo");
			}
		}
		return true;
	}
	
	function filialChange(e) {
		if (e.value == '') {
			resetAllRelatedValues();
		}
		
		manipulaEstadoRecibo(e.value != "",undefined);
		return reciboFreteCarreteiro_filial_sgFilialOnChangeHandler();
	}
	
	function popupFilial(data) {
		manipulaEstadoRecibo(true,undefined);
	}
	
	function tpReciboChange(elem) {
		comboboxChange({e:elem});
		resetAllRelatedValues();
		manipulaEstadoRecibo(undefined,elem.value != "");
		
		if (elem.value == 'V') {
			document.getElementById("reciboFreteCarreteiro.idReciboFreteCarreteiro").url = 
					contextRoot + "/freteCarreteiroViagem/manterRecibos.do";
		} else if (elem.value == 'C') {
			document.getElementById("reciboFreteCarreteiro.idReciboFreteCarreteiro").url = 
					contextRoot + "/freteCarreteiroColetaEntrega/manterRecibos.do";
		}
	}
	
	function manipulaEstadoRecibo(blFilialFilled,blTipoFilled) {
		if (blFilialFilled == undefined) {
			blFilialFilled = (getElementValue("reciboFreteCarreteiro.filial.idFilial") != "");
			
		}
		if (blTipoFilled == undefined) {
			blTipoFilled = (getElementValue("tpReciboFreteCarreteiro") != "");		
		}
		
		setDisabled("reciboFreteCarreteiro.idReciboFreteCarreteiro",!(blFilialFilled && blTipoFilled));
	}
	
	function reciboChange(e) {
		if (e.value == '') {
			resetAllRelatedValues();
		}
		
		if (e.id == 'reciboFreteCarreteiro2.filial.sgFilial') {
			return reciboFreteCarreteiro2_filial_sgFilialOnChangeHandler();
		} else if (e.id == 'reciboFreteCarreteiro.nrReciboFreteCarreteiro') {
			return reciboFreteCarreteiro_nrReciboFreteCarreteiroOnChangeHandler();
		}
	}
	
	function findValoresRecibo(idRecibo,idOcorrencia,callBack) {
		var data = new Array();
				
		setNestedBeanPropertyValue(data,"idReciboFreteCarreteiro",idRecibo);
		setNestedBeanPropertyValue(data,"idOcorrenciaFreteCarreteiro",idOcorrencia);
		
		var sdo = createServiceDataObject("lms.fretecarreteiroviagem.manterOcorrenciasReciboAction.findValoresRecibo",
				callBack,data);
				
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findValoresRecibo_cb(data,exception) {
		if (exception != null) {
			alert(exception);
			resetAllRelatedValues();
		} else {
			setAllRelatedValues(data);
			
			if (isMasterLinkCall()) {
				carregaValoresReciboCache(data);
			}
		}
	}
	
	function findValoresReciboAfterStore_cb(data,exception) {
		if (exception != null) {
			alert(exception);
		} else {			
			if (isMasterLinkCall()) {
				carregaValoresReciboCache(data);
			}
		}
	}
	
	function carregaValoresReciboCache(data) {
		valoresRecibo = data;
	}
	
	function setAllRelatedValues(data) {
		setElementValueAndFormat("dtOcorrenciaFreteCarreteiro",getNestedBeanPropertyValue(data,"dtOcorrenciaFreteCarreteiro"));
		setElementValueAndFormat("reciboFreteCarreteiro.dsMoeda",getNestedBeanPropertyValue(data,"reciboFreteCarreteiro.dsMoeda"));
		setElementValueAndFormat("reciboFreteCarreteiro.vlBruto",getNestedBeanPropertyValue(data,"reciboFreteCarreteiro.vlBruto"));
		setElementValueAndFormat("somaDescontos",getNestedBeanPropertyValue(data,"somaDescontos"));
		setElementValueAndFormat("vlDesconto",getNestedBeanPropertyValue(data,"vlDesconto"));
		setElementValueAndFormat("valorReciboComDesconto",getNestedBeanPropertyValue(data,"valorReciboComDesconto"));
		
		limpaDescontos();
		descontos_cb(data);
	}
		
	function preencheReciboRelateds() {
		setElementValueAndFormat("controleCarga.sgFilial",reciboCache.sgFilialOrigem);
	    setElementValueAndFormat("controleCarga.nrControleCarga",reciboCache.nrControleCarga);
	    setElementValueAndFormat("tpSituacaoRecibo.description",reciboCache.tpSituacaoRecibo);
	    setAllRelatedValues(valoresRecibo);
	}
	
	function onVlDescontoChange(elem) {
		recalculaDesconto(getElementValue(elem));
	}
	
	function recalculaDesconto(value) {
		var data = new Array();
		
		setNestedBeanPropertyValue(data,"vlRecibo",getElementValue("reciboFreteCarreteiro.vlBruto"));
		setNestedBeanPropertyValue(data,"vlDesconto",value);
		
		var listDescontos = getElementValue("descontos");
		if (listDescontos != undefined) {
			setNestedBeanPropertyValue(data,"descontos",listDescontos);
		}
		
		var sdo = createServiceDataObject("lms.fretecarreteiroviagem.manterOcorrenciasReciboAction.recalculaDesconto",
				"recalculaDesconto",data);
		
		xmit({serviceDataObjects:[sdo]});
	}
	
	function recalculaDesconto_cb(data,error) {
		var value = getNestedBeanPropertyValue(data,"valorReciboComDesconto");
		setElementValueAndFormat("valorReciboComDesconto",value)
	}
	
	function setElementValueAndFormat(elem,value) {
		setElementValue(elem,setFormat(elem,value));
	}
	
	function resetAllRelatedValues() {
		resetValue("dtOcorrenciaFreteCarreteiro");
		resetValue("reciboFreteCarreteiro.dsMoeda");
		resetValue("reciboFreteCarreteiro.vlBruto");
		resetValue("vlDesconto");
		resetValue("valorReciboComDesconto");
		
		limpaDescontos();
	}
	
	function limpaDescontos() {
		while (document.getElementById("descontos").length != 0)
			document.getElementById("descontos")[0] = null;
	}
	
	function storeCancelarRecibo_cb(data,error) {
		onDataLoad_cb(data,error);
		if (error == undefined) {
			comportamentoDetalhe();
			manipulaBotoes();
			manipulaBtnCancelar();
			
			if (isMasterLinkCall()) {
				findValoresRecibo(getElementValue("reciboFreteCarreteiro.idReciboFreteCarreteiro"),
						undefined,
						"findValoresReciboAfterStore");
			}
		}
	}
	
	function afterStore_cb(data,error,key) {
		store_cb(data,error,key);
		if (error != undefined && key == "LMS-24017") {
			setFocus("vlDesconto");
		}
		
		if (error == undefined) {
			comportamentoDetalhe();
			manipulaBotoes();
			manipulaBtnCancelar(data);
			
			if (isMasterLinkCall()) {
				findValoresRecibo(getElementValue("reciboFreteCarreteiro.idReciboFreteCarreteiro"),
						undefined,
						"findValoresReciboAfterStore");
			}
		}
	}
	
	function onDataLoadCustom_cb(data,error) {
		onDataLoad_cb(data,error);
		manipulaBotoes();
		manipulaBtnCancelar();
	}
	
	function comportamentoDetalhe() {
		setDisabled("reciboFreteCarreteiro.filial.idFilial",true);
		setDisabled("tpReciboFreteCarreteiro",true);
		setDisabled("reciboFreteCarreteiro.idReciboFreteCarreteiro",true);
	}

	function manipulaBotoes() {
		var tpOcorrencia = getElementValue("tpOcorrencia");
		setDisabled("btnSalvar",true);
		setDisabled("btnExcluir",tpOcorrencia == 'D');
		setFocusOnNewButton();
	}

	function manipulaBtnCancelar() {
		var tpOcorrencia = getElementValue("tpOcorrencia");
		var blDescontoCancelado = getElementValue("blDescontoCancelado");
		
		if (blDescontoCancelado == true){
			setDisabled("btnSalvar",true);
			setDisabled("btnCancelar",true);
		} else if (tpOcorrencia == 'D') {
			setDisabled("btnCancelar",false);
		} else {
			setDisabled("btnCancelar",true);
		}
	}
	
	function tpOcorrenciaChange(elem) {
		var value = getElementValue(elem);
		manipulateVlDesconto(value);
		return true;
	}
	
	function manipulateVlDesconto(value) {
		var blDesconto = value == "D";
		setDisabled("vlDesconto",!blDesconto);
		if (!blDesconto) {
			resetValue("vlDesconto");
		}
	}
	
	function isMasterLinkCall() {
		return document.getElementById('reciboFreteCarreteiro.idReciboFreteCarreteiro').masterLink;
	}

//-->
</script>
