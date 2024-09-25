<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.fretecarreteiroviagem.manterRecibosComplementarAction" >
	<adsm:form action="/freteCarreteiroViagem/manterRecibosComplementar" height="390" idProperty="idReciboFreteCarreteiro"
			service="lms.fretecarreteiroviagem.manterRecibosComplementarAction.findByIdCustom" onDataLoadCallBack="dataLoadCustom">
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-24032"/> 
		</adsm:i18nLabels>
		
		<adsm:lookup dataType="text" property="filial"
				idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.fretecarreteiroviagem.manterRecibosComplementarAction.findLookupFilial"
    			action="/municipios/manterFiliais" onchange="return filialChange(this);"
    			onDataLoadCallBack="dataLoadFilial"
    			onPopupSetValue="popupFilial"
    			label="filialEmissao" size="3" maxLength="3" labelWidth="18%" width="32%" 
    			exactMatch="true" required="true" >
         	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:propertyMapping relatedProperty="reciboComplementado.filial.sgFilial" modelProperty="sgFilial" />
         	
         	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />
	    </adsm:lookup>
	    
	    <adsm:combobox property="tpReciboFreteCarreteiro" domain="DM_TIPO_RECIBO_PAGAMENTO_FRETE_CARRETEIR" onDataLoadCallBack="tipoReciboFreteCarreteiro"
				label="tipoRecibo" labelWidth="18%" width="32%" onchange="return tpReciboChange(this);" />

		<adsm:textbox dataType="integer" property="nrReciboFreteCarreteiro" 
				label="reciboComplementar" size="10" maxLength="10" labelWidth="18%" width="32%" disabled="true" serializable="false"
				cellStyle="vertical-align=bottom;" mask="0000000000" />

		<adsm:hidden property="tpSituacaoRecibo.value" />
		<adsm:textbox dataType="text" property="tpSituacaoRecibo.description"
				label="situacao" labelWidth="18%" width="32%" size="25" disabled="true"
				cellStyle="vertical-align=bottom;" />

		<%--Lookup Recibo Frete Carreteiro ------------------------------------------------------------------------------------------------------%>
		<adsm:hidden property="blComplementar" value="N" serializable="false" />
		<adsm:hidden property="blAdiantamento" value="N" serializable="false" />
		<adsm:textbox dataType="text" property="reciboComplementado.filial.sgFilial"
				label="recibo" size="3" maxLength="3" labelWidth="18%" width="32%" cellStyle="vertical-align=bottom;"
				disabled="true" serializable="false" picker="false" >
        
	        <adsm:lookup dataType="integer" property="reciboComplementado"
					idProperty="idReciboFreteCarreteiro" criteriaProperty="nrReciboFreteCarreteiro"
					service="lms.fretecarreteiroviagem.manterRecibosComplementarAction.findLookupRecibo"
					action="/freteCarreteiroViagem/manterRecibos" onDataLoadCallBack="reciboDataLoad"
					size="10" maxLength="10" cellStyle="vertical-align=bottom;" exactMatch="true" mask="0000000000"
					picker="true" serializable="true" disabled="true" required="true" > 
				
				<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filial.idFilial" />
				<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filial.sgFilial" inlineQuery="false" />
	        	<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filial.pessoa.nmFantasia" inlineQuery="false" />        	        	
	        	
				<adsm:propertyMapping modelProperty="controleCarga.idControleCarga" relatedProperty="idControleCargaComp" />
	        	<adsm:propertyMapping modelProperty="controleCarga.nrControleCarga" relatedProperty="controleCarga.nrControleCarga" />
	        	<adsm:propertyMapping modelProperty="controleCarga.idFilialOrigem" relatedProperty="controleCarga.idFilial" />
	        	<adsm:propertyMapping modelProperty="controleCarga.sgFilialOrigem" relatedProperty="controleCarga.sgFilial" />
	        	<adsm:propertyMapping modelProperty="controleCarga.nmFilialOrigem" relatedProperty="controleCarga.nmFilial" />
	        	
	        	<adsm:propertyMapping modelProperty="meioTransporteRodoviario.idMeioTransporte" 
	        			relatedProperty="idMeioTransporteComp" />
	        	<adsm:propertyMapping modelProperty="meioTransporte.nrFrota" 
	        			relatedProperty="meioTransporte.nrFrota" />
	        	<adsm:propertyMapping modelProperty="meioTransporte.nrIdentificador" 
	        			relatedProperty="meioTransporte.nrIdentificador" />
	        	<adsm:propertyMapping modelProperty="meioTransporte.dsMarca"
						relatedProperty="meioTransporte.dsMarca" />
	        	<adsm:propertyMapping modelProperty="meioTransporte.dsModelo" 
	        			relatedProperty="meioTransporte.dsModelo" />
	        	
	        	<adsm:propertyMapping modelProperty="proprietario.idProprietario" relatedProperty="idProprietarioComp" />
	        	<adsm:propertyMapping modelProperty="proprietario.nrIdentificacao" relatedProperty="proprietario.nrIdentificacao" />
	        	<adsm:propertyMapping modelProperty="proprietario.nmPessoa" relatedProperty="proprietario.nmPessoa" />
	        	
	        	<adsm:propertyMapping modelProperty="motorista.idMotorista" relatedProperty="idMotoristaComp" />
	        	
	        	<adsm:propertyMapping criteriaProperty="tpReciboFreteCarreteiro"
	        			modelProperty="tpReciboFreteCarreteiro" />
	        			
	        	<adsm:propertyMapping modelProperty="blComplementar" criteriaProperty="blComplementar" />
	        	<adsm:propertyMapping modelProperty="blAdiantamento" criteriaProperty="blAdiantamento" />
	        </adsm:lookup>
	    </adsm:textbox>
		<%--Lookup Recibo Frete Carreteiro ------------------------------------------------------------------------------------------------------%>
		
        <adsm:hidden property="idControleCargaComp" />
        <adsm:hidden property="idProprietarioComp" />
        <adsm:hidden property="idMotoristaComp" />
        <adsm:hidden property="idMeioTransporteComp" />
        
        <adsm:hidden property="controleCarga.idFilial" />
        <adsm:hidden property="controleCarga.nmFilial" />
		<adsm:textbox dataType="text" property="controleCarga.sgFilial"
				label="controleCarga" size="3" labelWidth="18%" width="82%"
				disabled="true" serializable="false" >
        	<adsm:textbox dataType="integer" property="controleCarga.nrControleCarga" size="8" mask="00000000"
        			disabled="true" serializable="false" />
        </adsm:textbox>
        		
		<adsm:textbox dataType="text" property="proprietario.nrIdentificacao"
				label="proprietario" size="18" labelWidth="18%" width="82%"
				disabled="true" serializable="false" >
        	<adsm:textbox dataType="text" property="proprietario.nmPessoa" size="30"
        			disabled="true" serializable="false" />
        </adsm:textbox>
        
        <adsm:textbox dataType="text" property="meioTransporte.nrFrota"
				label="meioTransporte" size="8" labelWidth="18%" width="82%" cellStyle="vertical-align=bottom;"
				disabled="true" serializable="false" >
        	<adsm:textbox dataType="text" property="meioTransporte.nrIdentificador"
        			size="20" cellStyle="vertical-align=bottom;"
        			disabled="true" serializable="false" />
        </adsm:textbox>
 
		<adsm:textbox dataType="text"
				property="meioTransporte.dsMarca"
				label="marca" labelWidth="18%" width="32%" disabled="true" serializable="false" />
        <adsm:textbox dataType="text"
        		property="meioTransporte.dsModelo"
        		label="modelo" labelWidth="18%" width="32%" disabled="true" serializable="false" />

		<adsm:textbox dataType="text" property="nrNfCarreteiro"
				label="notaFiscalCarreteiro" labelWidth="18%" width="82%" maxLength="15" />

		<adsm:textarea property="obReciboFreteCarreteiro"
				label="motivo" columns="90" labelWidth="18%" width="82%" maxLength="500" />
				
		<adsm:textbox dataType="JTDate" property="dtContabilizacao"
				label="dataContabilizacao" labelWidth="18%" width="32%" disabled="true" serializable="false" />

		<adsm:textbox dataType="JTDate" property="dtSugeridaPagto" 
				label="dataPagamentoSugerida" labelWidth="18%" width="32%" cellStyle="vertical-align=bottom;" 
				disabled="true" serializable="false" />
		<adsm:textbox dataType="JTDate" property="dtPagtoReal" 
				label="dataPagamentoReal" labelWidth="18%" width="32%" cellStyle="vertical-align=bottom;" 
				disabled="true" />
		<adsm:textbox dataType="JTDateTimeZone" property="dhGeracaoMovimento" 
				label="dataGeracaoMovimento" labelWidth="18%" width="32%" 
				disabled="true" serializable="true" cellStyle="vertical-align=bottom;" />

        <adsm:textbox dataType="integer" property="relacaoPagamento.nrRelacaoPagamento" mask="0000000000"
        		label="relacaoPagamento" size="10" maxLength="10" labelWidth="18%" width="82%" 
        		disabled="true" serializable="false" />
		
		<%--adsm:hidden property="moedaPais.idMoedaPais" />
		<adsm:textbox dataType="text" property="moedaPais.moeda.siglaSimbolo"
        		label="moeda" labelWidth="18%" width="32%" size="15"
        		disabled="true" serializable="false" /--%>		
		<adsm:combobox property="moedaPais.idMoedaPais" autoLoad="true"
				optionProperty="idMoedaPais" optionLabelProperty="moeda.siglaSimbolo"
				service="lms.fretecarreteiroviagem.manterRecibosComplementarAction.findMoedaByPais"
				label="moeda" labelWidth="18%" width="32%" required="true" >
		</adsm:combobox>
		
        <adsm:textbox dataType="currency" property="vlBruto" 
        		label="valorComplementar" size="15" maxLength="18" labelWidth="18%" width="32%" required="true" />


		<adsm:section caption="inss"/>
		<adsm:textbox dataType="currency" property="vlSalarioContribuicao"
				label="salarioContribuicao" size="15" maxLength="10" labelWidth="18%" width="32%"
				disabled="true" serializable="false" />		
		<adsm:textbox dataType="currency" property="pcAliquotaInss"
				label="percentualAliquotaINSS" size="15" maxLength="10" labelWidth="18%" width="32%" 
				disabled="true" serializable="false" />
        <adsm:textbox dataType="currency" property="vlOutrasFontes"
        		label="valorOutrasFontes" size="15" maxLength="10" labelWidth="18%" width="32%"
        		disabled="true" serializable="false" />
        <adsm:textbox dataType="currency" property="vlApuradoInss"
        		label="valorApuradoINSS" size="15" maxLength="10" labelWidth="18%" width="32%" 
        		disabled="true" serializable="false" />
		
		
		<adsm:section caption="outrosImpostos"/>
		<adsm:textbox dataType="currency" property="pcAliquotaIssqn"
				label="percentualAliquotaISSQN" size="15" maxLength="10" labelWidth="18%" width="32%" 
				disabled="true" serializable="false" />
		<adsm:textbox dataType="currency" property="vlIssqn"
				label="valorISSQN" size="15" maxLength="10" labelWidth="18%" width="32%" 
				disabled="true" serializable="false" />
		<adsm:textbox dataType="currency" property="pcAliquotaIrrf"
				label="percentualAliquotaIRRF" size="15" maxLength="10" labelWidth="18%" width="32%" 
				disabled="true" serializable="false" />
		<adsm:textbox dataType="currency" property="vlIrrf"
				label="valorIRRF" size="15" maxLength="10" labelWidth="18%" width="32%" 
				disabled="true" serializable="false" />

		<adsm:section caption="valores"/>
		<adsm:textbox dataType="currency" property="vlLiquido"
				label="valorLiquido" labelWidth="18%" width="32%" size="15"
				disabled="true" serializable="false" />
		
		<adsm:hidden property="blComplementarFlag" value="S" serializable="false" />
		
		<adsm:buttonBar>
			<adsm:button caption="emitir" id="btnEmitir" onclick="emitirRecibo();" />
			<adsm:button caption="ocorrencias" action="/freteCarreteiroViagem/manterOcorrenciasRecibo" cmd="main"
					id="btnOcorrencias" >
				<adsm:linkProperty src="idReciboFreteCarreteiro" target="reciboFreteCarreteiro.idReciboFreteCarreteiro" disabled="true" />
				<adsm:linkProperty src="idReciboFreteCarreteiro" target="reciboFreteCarreteiro2.idReciboFreteCarreteiro" disabled="true" />
				<adsm:linkProperty src="nrReciboFreteCarreteiro" target="reciboFreteCarreteiro.nrReciboFreteCarreteiro" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="reciboFreteCarreteiro2.filial.sgFilial" disabled="true" />
				
				<adsm:linkProperty src="filial.idFilial" target="reciboFreteCarreteiro.filial.idFilial" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="reciboFreteCarreteiro.filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="reciboFreteCarreteiro.filial.pessoa.nmFantasia" disabled="true" />
				
				<adsm:linkProperty src="idControleCargaComp" target="reciboFreteCarreteiro.controleCarga.idControleCarga" />
				<adsm:linkProperty src="controleCarga.sgFilial" target="reciboFreteCarreteiro.controleCarga.filialByIdFilialOrigem.sgFilial" />
				<adsm:linkProperty src="controleCarga.nrControleCarga" target="reciboFreteCarreteiro.controleCarga.nrControleCarga" />
				
				<adsm:linkProperty src="tpReciboFreteCarreteiro" target="tpReciboFreteCarreteiro" disabled="true" />
				<adsm:linkProperty src="blComplementarFlag" target="blComplementar" disabled="true" />
			</adsm:button>

			<adsm:button caption="controleCargas" id="btnControleCargas" action="carregamento/consultarControleCargas" cmd="main" >
				<adsm:linkProperty src="idControleCargaComp" target="idControleCarga" disabled="true" />
				<adsm:linkProperty src="controleCarga.nrControleCarga" target="nrControleCarga" disabled="true" />
				<adsm:linkProperty src="controleCarga.idFilial"
						target="filialByIdFilialOrigem.idFilial" disabled="true" />
				<adsm:linkProperty src="controleCarga.sgFilial"
						target="filialByIdFilialOrigem.sgFilial" disabled="true" />
				<adsm:linkProperty src="controleCarga.nmFilial"
						target="filialByIdFilialOrigem.pessoa.nmFantasia" disabled="true" />			
			</adsm:button>
			
			<adsm:button caption="cancelar" id="btnCancelar" onclick="cancelarRecibo();" />
			<adsm:storeButton service="lms.fretecarreteiroviagem.manterRecibosComplementarAction.storeCustom" callbackProperty="storeRecibo"
					id="storeButtonTag" />
			<adsm:newButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
<!--

	document.getElementById("blAdiantamento").masterLink = true;
	document.getElementById("blComplementar").masterLink = true;
	document.getElementById("blComplementarFlag").masterLink = true;

	var reciboCache = undefined;
	var newState = false;

	function initWindow(eventObj) {
		var blDetalhamento = (eventObj.name == "gridRow_click" || eventObj.name == "storeButton");
				
		if (!blDetalhamento) {
			if (isMasterLinkCall()) {
				if (reciboCache == undefined) {
					newState = true;
					lookupChange({e:document.forms[0].elements["reciboComplementado.idReciboFreteCarreteiro"],forceChange:true});
				} else {
					setRelatedReciboValues();
				}
			} else {
				setDisabled("reciboComplementado.idReciboFreteCarreteiro",true);
			}
			
			setDisabled("moedaPais.idMoedaPais", false);
			setDisabled("vlBruto", false);
			setDisabled("nrNfCarreteiro", false);
			setDisabled("obReciboFreteCarreteiro", false);
			
		} else {
			setDisabled("reciboComplementado.idReciboFreteCarreteiro",true);
		}
		
		setDisabled("filial.idFilial",blDetalhamento);
		setDisabled("tpReciboFreteCarreteiro",blDetalhamento);
		
		if (!blDetalhamento)
			setFocusOnFirstFocusableField();
	}
		
	function tipoReciboFreteCarreteiro_cb(data){
		var comboData = [];
		
		for (i = 0; i < data.length;i++){
			if (data[i].value != 'C'){
				comboData.push(data[i]);
			}
		}
		
		comboboxLoadOptions({e:document.getElementById("tpReciboFreteCarreteiro"), data:comboData});
		
	}	

	function reciboDataLoad_cb(data,exception) {
		if (data != undefined && data.length == 1) {
			if (isMasterLinkCall()) {
				reciboCache = data;
				findMoedaPaisByIdFilial(getElementValue("filial.idFilial"));
			}
		}
		
		var retorno = reciboComplementado_nrReciboFreteCarreteiro_exactMatch_cb(data);
		if (newState) {
			var tabGroup = getTabGroup(this.document);
			var tabCad = tabGroup.getTab("cad");
			tabCad.changed = false;
			newState = false;
		}
		return retorno;
	}
		
	function cancelarRecibo() {
		if (confirm(i18NLabel.getLabel("LMS-24032"))) {
			storeButtonScript('lms.fretecarreteiroviagem.manterRecibosAction.storeCancelarRecibo', 'store', document.Lazy);
		}
	}
			
	function setRelatedReciboValues() {
		setElementValueAndFormat("idControleCargaComp",getNestedBeanPropertyValue(reciboCache,":0.controleCarga.idControleCarga"));
		setElementValueAndFormat("controleCarga.sgFilial",getNestedBeanPropertyValue(reciboCache,":0.controleCarga.sgFilialOrigem"));
		setElementValueAndFormat("controleCarga.nrControleCarga",getNestedBeanPropertyValue(reciboCache,":0.controleCarga.nrControleCarga"));
		setElementValueAndFormat("idMeioTransporteComp",getNestedBeanPropertyValue(reciboCache,":0.meioTransporteRodoviario.idMeioTransporte"));
		setElementValueAndFormat("meioTransporte.nrFrota",getNestedBeanPropertyValue(reciboCache,":0.meioTransporte.nrFrota"));
		setElementValueAndFormat("meioTransporte.nrIdentificador",getNestedBeanPropertyValue(reciboCache,":0.meioTransporte.nrIdentificador"));
		setElementValueAndFormat("meioTransporte.dsMarca",getNestedBeanPropertyValue(reciboCache,":0.meioTransporte.dsMarca"));
		setElementValueAndFormat("meioTransporte.dsModelo",getNestedBeanPropertyValue(reciboCache,":0.meioTransporte.dsModelo"));
		setElementValueAndFormat("idProprietarioComp",getNestedBeanPropertyValue(reciboCache,":0.proprietario.idProprietario"));
		setElementValueAndFormat("proprietario.nrIdentificacao",getNestedBeanPropertyValue(reciboCache,":0.proprietario.nrIdentificacao"));
		setElementValueAndFormat("proprietario.nmPessoa",getNestedBeanPropertyValue(reciboCache,":0.proprietario.nmPessoa"));
		setElementValueAndFormat("idMotoristaComp",getNestedBeanPropertyValue(reciboCache,":0.motorista.idMotorista"));
		setElementValueAndFormat("moedaPais.idMoedaPais",getNestedBeanPropertyValue(reciboCache,":0.moedaPais.idMoedaPais"));
	}	
	
	function setElementValueAndFormat(elem,value) {
		setElementValue(elem,setFormat(elem,value));
	}
	
	function filialChange(e) {
		manipulaEstadoRecibo(e.value != "",undefined);
		if (e.value != "") {
			resetValue("moedaPais.idMoedaPais");
		}
		return filial_sgFilialOnChangeHandler();
	}
	
	function dataLoadFilial_cb(data) {
		var retorno = filial_sgFilial_exactMatch_cb(data);
		if (retorno && data != undefined && data.length  == 1) {
			var id = getNestedBeanPropertyValue(data,":0.idFilial");
			findMoedaPaisByIdFilial(id);
		}
		return retorno;
	}
	
	function popupFilial(data) {
		manipulaEstadoRecibo(true,undefined);
		
		var id = getNestedBeanPropertyValue(data,"idFilial");
		findMoedaPaisByIdFilial(id);
	}
	
	
	function findMoedaPaisByIdFilial(id) {
		var data = new Array();
		data.idFilial = id;

		var sdo = createServiceDataObject(
				"lms.fretecarreteiroviagem.manterRecibosComplementarAction.findMoedaPaisByIdFilial",
				"findMoedaPaisByIdFilial",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findMoedaPaisByIdFilial_cb(data,error) {
		setElementValue("moedaPais.idMoedaPais",getNestedBeanPropertyValue(data,"moedaPais.idMoedaPais"));
		if (isMasterLinkCall()) {
			setNestedBeanPropertyValue(reciboCache,":0.moedaPais.idMoedaPais",
					getNestedBeanPropertyValue(data,"moedaPais.idMoedaPais"));
		}
	}
	
	
	function tpReciboChange(elem) {
		comboboxChange({e:elem});
		manipulaEstadoRecibo(undefined,elem.value != "");
		
		if (elem.value == 'V') {
			document.getElementById("reciboComplementado.idReciboFreteCarreteiro").url = 
					contextRoot + "/freteCarreteiroViagem/manterRecibos.do";
		} else if (elem.value == 'C') {
			document.getElementById("reciboComplementado.idReciboFreteCarreteiro").url = 
					contextRoot + "/freteCarreteiroColetaEntrega/manterRecibos.do";
		}
	}
	
	function manipulaEstadoRecibo(blFilialFilled,blTipoFilled) {
		if (blFilialFilled == undefined) {
			blFilialFilled = (getElementValue("filial.idFilial") != "");
			
		}
		if (blTipoFilled == undefined) {
			blTipoFilled = (getElementValue("tpReciboFreteCarreteiro") != "");		
		}
		
		setDisabled("reciboComplementado.idReciboFreteCarreteiro",!(blFilialFilled && blTipoFilled));
	}
		
	
	function isMasterLinkCall() {
		return document.getElementById("reciboComplementado.idReciboFreteCarreteiro").masterLink;
	}
		
	function emitirRecibo(){
		var data = new Array();
		_serviceDataObjects = new Array(); 
		
		data.idReciboFreteCarreteiro = getElementValue("idReciboFreteCarreteiro");
		data.tpReciboFreteCarreteiro = getElementValue("tpReciboFreteCarreteiro");
		addServiceDataObject(createServiceDataObject("lms.fretecarreteiroviagem.manterRecibosComplementarAction.execute", "emitirRecibo", data));
		xmit(false);
	}
	
	function emitirRecibo_cb(strFile,error) {
		openReport(strFile._value, '');
		if (error == undefined) {
			if (getElementValue("tpSituacaoRecibo.value") != "EM") {
				onDataLoad(getElementValue("idReciboFreteCarreteiro"));
			}
		}
	}
	
	function dataLoadCustom_cb(data,error) {
		onDataLoad_cb(data,error);
		if (error == undefined) {
			comportamentoDetalhe();
		}
	}
			
	function storeRecibo_cb(data,error,key) {
		store_cb(data, error, key);
		if(error == undefined){			
			comportamentoDetalhe("store");
		}
	}
	
	function comportamentoDetalhe(evento) {
		var tpSituacaoRecibo = getElementValue("tpSituacaoRecibo.value");
		
		// Desabilita campos e botões de acordo com os tipos.
		setDisabled("btnEmitir",tpSituacaoRecibo == "CA");
		setDisabled("btnCancelar",tpSituacaoRecibo == "CA" || tpSituacaoRecibo == "PA");
		setDisabled("btnControleCargas",tpSituacaoRecibo == "CA");
		setDisabled("btnOcorrencias",tpSituacaoRecibo == "CA");
		setDisabled("storeButtonTag",tpSituacaoRecibo == "CA" || tpSituacaoRecibo == "ER" || tpSituacaoRecibo == "PA");
		setDisabled("nrNfCarreteiro",tpSituacaoRecibo == "CA" || tpSituacaoRecibo == "ER" || tpSituacaoRecibo == "PA");
		setDisabled("obReciboFreteCarreteiro",tpSituacaoRecibo == "CA" || tpSituacaoRecibo == "ER" || tpSituacaoRecibo == "PA");
		
		setDisabled("moedaPais.idMoedaPais", tpSituacaoRecibo != "GE");
		setDisabled("vlBruto", tpSituacaoRecibo != "GE");
		
		if ((evento != undefined && evento == "store") ||
				tpSituacaoRecibo == "CA" || tpSituacaoRecibo == "ER" || tpSituacaoRecibo == "PA")
			setFocusOnNewButton();
		else
			setFocusOnFirstFocusableField();
	}
	
//-->
</script>
