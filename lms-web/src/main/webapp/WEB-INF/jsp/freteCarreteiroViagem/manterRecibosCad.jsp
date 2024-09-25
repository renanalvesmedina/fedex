<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.fretecarreteiroviagem.manterRecibosAction" >
	<adsm:form action="/freteCarreteiroViagem/manterRecibos" height="385" onDataLoadCallBack="dataLoadCustom"
			 service="lms.fretecarreteiroviagem.manterRecibosAction.findByIdCustom" idProperty="idReciboFreteCarreteiro" >

		<adsm:hidden property="blComplementar" />
		
		<adsm:hidden property="filial.idFilial" />		
		<adsm:textbox dataType="text" property="filial.sgFilial"
				label="filial" labelWidth="18%" width="82%" size="3"
				disabled="true" serializable="false" >
        	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30"
        			disabled="true" serializable="false" />
        </adsm:textbox>

		<adsm:hidden property="nrReciboFreteCarreteiro" />
		<adsm:textbox dataType="text" property="nrReciboFreteCarreteiro2"
				label="numero" size="12" labelWidth="18%" width="32%" 
				disabled="true" serializable="false" style="text-align:right" />
		
		<adsm:hidden property="tpSituacaoRecibo.value" />
		<adsm:textbox dataType="text" property="tpSituacaoRecibo.description"
				label="situacao" labelWidth="18%" width="32%" size="25" disabled="true" />
		<adsm:checkbox property="blAdiantamento" label="adiantamento" labelWidth="18%" width="82%" disabled="true" serializable="false" />

		<adsm:hidden property="proprietario.idProprietario" serializable="false" />
		<adsm:textbox dataType="text" property="proprietario.pessoa.nrIdentificacaoFormatado"
				label="proprietario" size="18" maxLength="20" labelWidth="18%" width="82%"
				disabled="true" serializable="false" >
        	<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="30"
        			disabled="true" serializable="false" />
        </adsm:textbox>

		<adsm:hidden property="beneficiario.idBeneficiario" serializable="false" />
		<adsm:textbox dataType="text" property="beneficiario.pessoa.nrIdentificacaoFormatado"
				label="beneficiario" size="18" maxLength="20" labelWidth="18%" width="82%"
				disabled="true" serializable="false" >
        	<adsm:textbox dataType="text" property="beneficiario.pessoa.nmPessoa" size="30"
        			disabled="true" serializable="false" />
        </adsm:textbox>

		<adsm:textbox label="banco" dataType="integer" property="banco.nrBanco" 
				size="10" labelWidth="18%" width="82%" disabled="true" />
		
		<adsm:complement label="agencia" labelWidth="18%" width="32%" >
			<adsm:textbox dataType="integer" property="agenciaBancaria.nrAgenciaBancaria" size="5" disabled="true"/>
			<adsm:textbox dataType="text" property="agenciaBancaria.nrDigito" size="2" style="width: 15px;" disabled="true" />
		</adsm:complement>
		
		<adsm:complement label="conta" labelWidth="18%" width="32%" >
			<adsm:textbox dataType="integer" property="contaBancaria.nrContaBancaria" size="14" disabled="true"/>
			<adsm:textbox dataType="text" property="contaBancaria.dvContaBancaria" style="width:30px;" disabled="true" />
		</adsm:complement>

		<adsm:textbox dataType="text" property="meioTransporte.nrFrota"
				label="meioTransporte" size="8" labelWidth="18%" width="82%" cellStyle="vertical-align=bottom;"
				disabled="true" serializable="false" >
        	<adsm:textbox dataType="text" property="meioTranporte.nrIdentificador"
        			size="20" cellStyle="vertical-align=bottom;"
        			disabled="true" serializable="false" />
        </adsm:textbox>

		<adsm:textbox dataType="text" property="dsMarcaMeioTransporte"
				label="marca" labelWidth="18%" width="32%" disabled="true" serializable="false" />
        <adsm:textbox dataType="text" property="dsModeloMeioTransporte"
        		label="modelo" labelWidth="18%" width="32%" disabled="true" serializable="false" />
		
		<adsm:textbox dataType="text" property="nrNfCarreteiro"
				label="notaFiscalCarreteiro" labelWidth="18%" width="82%" maxLength="15" />
		
		<adsm:textarea property="obReciboFreteCarreteiro"
				label="observacao" columns="90" labelWidth="18%" width="82%" maxLength="500" />

		<adsm:textbox dataType="JTDateTimeZone" property="dhEmissao"
				label="dataEmissao" labelWidth="18%" width="32%"
				disabled="true" serializable="false" />
		<adsm:textbox dataType="JTDate" property="dtContabilizacao"
				label="dataContabilizacao" labelWidth="18%" width="32%" disabled="true" serializable="false" />

		<adsm:textbox dataType="JTDate" property="dtSugeridaPagto" 
				label="dataPagamentoSugerida" labelWidth="18%" width="32%" cellStyle="vertical-align=bottom;" 
				disabled="true" serializable="false" />
		<adsm:textbox dataType="JTDate" property="dtPagtoReal" 
				label="dataPagamentoReal" labelWidth="18%" width="32%" cellStyle="vertical-align=bottom;" 
				disabled="true" serializable="false" />
		<adsm:textbox dataType="JTDateTimeZone" property="dhGeracaoMovimento" 
				label="dataGeracaoMovimento" labelWidth="18%" width="82%" 
				disabled="true" serializable="true" cellStyle="vertical-align=bottom;" />

        <adsm:textbox dataType="integer" property="relacaoPagamento.nrRelacaoPagamento"
        		label="relacaoPagamento" size="10" maxLength="10" labelWidth="18%" width="82%" 
        		disabled="true" serializable="false" mask="0000000000"/>

		<adsm:section caption="controleCargas" />
		<adsm:hidden property="controleCarga.idControleCarga" serializable="false" />
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.idFilial" serializable="false" />
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false" />
		<adsm:textbox dataType="text" property="controleCarga.filialByIdFilialOrigem.sgFilial"
				label="controleCarga" labelWidth="18%" width="32%" size="3"
				disabled="true" serializable="false" >
        	<adsm:textbox dataType="integer" property="controleCarga.nrControleCarga"
					size="10" disabled="true" serializable="false" mask="00000000" />
        </adsm:textbox>
		
		<adsm:textbox dataType="JTDateTimeZone" property="controleCarga.dhEvento" 
				label="dataEmissao" labelWidth="18%" width="32%" disabled="true" serializable="false" />
        <adsm:textbox dataType="text" property="controleCarga.filialByIdFilialDestino.sgFilial"
				label="filialDestino" labelWidth="18%" width="82%" size="3"
				disabled="true" serializable="false" >
        	<adsm:textbox dataType="text" property="controleCarga.filialByIdFilialDestino.pessoa.nmFantasia" size="30"
        			disabled="true" serializable="false" />
        </adsm:textbox>

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
		<adsm:textbox dataType="text" property="moedaPais.moeda.siglaSimbolo"
				label="moeda" labelWidth="18%" width="32%" size="8"
				disabled="true" serializable="false" />
		<adsm:textbox dataType="currency" property="vlBruto" 
				label="valorBruto" labelWidth="18%" width="32%" size="15" 
				disabled="true" serializable="false" cellStyle="vertical-align=bottom;"/>
				
		<adsm:textbox dataType="currency" property="vlPostoPassagem" 
				label="valorPostoPassagem" labelWidth="18%" width="32%" size="15" 
				disabled="true" serializable="false" cellStyle="vertical-align=bottom;"/>
		<adsm:textbox dataType="integer" property="nrCartao" 
        		label="cartaoValePedagio" size="17" maxLength="16" labelWidth="18%" width="32%"
        		disabled="true" serializable="false" cellStyle="vertical-align=bottom;"/>
        		
		<adsm:textbox dataType="currency" property="vlPremio" 
				label="valorPremio" labelWidth="18%" width="32%" size="15" 
				disabled="true" serializable="false" />
		<adsm:textbox dataType="currency" property="vlDiaria" 
				label="valorDiaria" labelWidth="18%" width="32%" size="15" 
				disabled="true" serializable="false" />
		<adsm:textbox dataType="currency" property="vlDesconto"
				label="valorDesconto" labelWidth="18%" width="32%" size="15"
				disabled="true" serializable="false" />
		<adsm:textbox dataType="currency" property="vlAdiantamento" 
				label="valorAdiantamento" labelWidth="18%" width="32%" size="15" 
				disabled="true" serializable="false" />		
		<adsm:textbox dataType="currency" property="vlLiquido"
				label="valorLiquido" labelWidth="18%" width="82%" size="15"
				disabled="true" serializable="false" />

		<adsm:hidden property="tpReciboFreteCarreteiro" value="V" />

		<adsm:buttonBar>
			<adsm:button caption="emitir" id="btnEmitir" onclick="emitirRecibo()" />
			<adsm:button caption="recibosComplementares" id="btnComplentar" action="/freteCarreteiroViagem/manterRecibosComplementar" cmd="main" >
				<adsm:linkProperty src="idReciboFreteCarreteiro" target="reciboComplementado.idReciboFreteCarreteiro" disabled="true" />
				<adsm:linkProperty src="nrReciboFreteCarreteiro" target="reciboComplementado.nrReciboFreteCarreteiro" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="reciboComplementado.filial.sgFilial" disabled="true" />
				
				<adsm:linkProperty src="filial.idFilial" target="filial.idFilial" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="filial.pessoa.nmFantasia" disabled="true" />
				
				<adsm:linkProperty src="tpReciboFreteCarreteiro" target="tpReciboFreteCarreteiro" disabled="true" />
			</adsm:button>
			<adsm:button caption="ocorrencias" id="btnOcorrencias" action="/freteCarreteiroViagem/manterOcorrenciasRecibo" cmd="main" >
				<adsm:linkProperty src="idReciboFreteCarreteiro" target="reciboFreteCarreteiro.idReciboFreteCarreteiro" disabled="true" />
				<adsm:linkProperty src="nrReciboFreteCarreteiro" target="reciboFreteCarreteiro.nrReciboFreteCarreteiro" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="reciboFreteCarreteiro2.filial.sgFilial" disabled="true" />
				
				<adsm:linkProperty src="filial.idFilial" target="reciboFreteCarreteiro.filial.idFilial" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="reciboFreteCarreteiro.filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="reciboFreteCarreteiro.filial.pessoa.nmFantasia" disabled="true" />
				
				<adsm:linkProperty src="controleCarga.idControleCarga" target="reciboFreteCarreteiro.controleCarga.idControleCarga" />
				<adsm:linkProperty src="controleCarga.filialByIdFilialOrigem.sgFilial" 
						target="reciboFreteCarreteiro.controleCarga.filialByIdFilialOrigem.sgFilial" />
				<adsm:linkProperty src="controleCarga.nrControleCarga" target="reciboFreteCarreteiro.controleCarga.nrControleCarga" />
				
				<adsm:linkProperty src="tpReciboFreteCarreteiro" target="tpReciboFreteCarreteiro" disabled="true" />
				<adsm:linkProperty src="blComplementar" target="blComplementar" disabled="true" />
			</adsm:button>
			<adsm:button caption="controleCargas" id="btnControleCargas" action="carregamento/consultarControleCargas" cmd="main" >
				<adsm:linkProperty src="controleCarga.idControleCarga" target="idControleCarga" disabled="true" />
				<adsm:linkProperty src="controleCarga.nrControleCarga" target="nrControleCarga" disabled="true" />
				<adsm:linkProperty src="controleCarga.filialByIdFilialOrigem.idFilial"
						target="filialByIdFilialOrigem.idFilial" disabled="true" />
				<adsm:linkProperty src="controleCarga.filialByIdFilialOrigem.sgFilial"
						target="filialByIdFilialOrigem.sgFilial" disabled="true" />
				<adsm:linkProperty src="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"
						target="filialByIdFilialOrigem.pessoa.nmFantasia" disabled="true" />
						
			</adsm:button>
			<adsm:button caption="cancelar" id="btnCancelar" service="lms.fretecarreteiroviagem.manterRecibosAction.storeCancelarRecibo" 
					callbackProperty="storeCancelarRecibo" />
			<adsm:storeButton service="lms.fretecarreteiroviagem.manterRecibosAction.storeCustom" id="storeButtonTag" />
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
	function dataLoadCustom_cb(data,error) {
		onDataLoad_cb(data,error);
	
		if (error == undefined) {
			var blAdiantamento = getNestedBeanPropertyValue(data,"blAdiantamento");
			var blComplementar = getNestedBeanPropertyValue(data,"blComplementar");
			
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab('adi', blAdiantamento == "true" || blComplementar == "true");
			
			var vlPostoPassagem = getNestedBeanPropertyValue(data,"vlPostoPassagem");
			var blPedagio = vlPostoPassagem != undefined && vlPostoPassagem != "";
			setVisibility("nrCartao", getNestedBeanPropertyValue(data,"nrCartao") != undefined &&  getNestedBeanPropertyValue(data,"nrCartao") != "" );
			setVisibility("vlAdiantamento", blComplementar != "true");
			comportamentoDetalhe();
		}
	}
	
	function afterStore_cb(data,error,key) {
		store_cb(data,error,key);
		if (error == undefined) {
			comportamentoDetalhe();
		}
	}

	function comportamentoDetalhe() {
		var blAdiantamento = getElementValue("blAdiantamento");
		var blComplementar = getElementValue("blComplementar");
		var tpSituacaoRecibo = getElementValue("tpSituacaoRecibo.value");
		
		// Desabilita campos e botões de acordo com os tipos.
		setDisabled("btnEmitir",tpSituacaoRecibo == "CA");
		setDisabled("btnCancelar",tpSituacaoRecibo == "CA" || tpSituacaoRecibo == "PA");
		setDisabled("btnControleCargas",tpSituacaoRecibo == "CA");
		setDisabled("btnOcorrencias",tpSituacaoRecibo == "CA");
		setDisabled("storeButtonTag",tpSituacaoRecibo == "CA" || tpSituacaoRecibo == "ER" || tpSituacaoRecibo == "PA");
		if(blComplementar == "false"){
			setDisabled("btnComplentar", true)
		}else{
		setDisabled("btnComplentar",blComplementar == "true" || blAdiantamento == true || tpSituacaoRecibo == "CA");
		}
		setDisabled("nrNfCarreteiro",tpSituacaoRecibo == "CA" || tpSituacaoRecibo == "ER" || tpSituacaoRecibo == "PA");
		setDisabled("obReciboFreteCarreteiro",tpSituacaoRecibo == "CA" || tpSituacaoRecibo == "ER" || tpSituacaoRecibo == "PA");
		setFocusOnFirstFocusableField();
	}

	function emitirRecibo(){
		var data = new Array();
		_serviceDataObjects = new Array();
		
		data.idReciboFreteCarreteiro = getElementValue("idReciboFreteCarreteiro");
		addServiceDataObject(createServiceDataObject("lms.fretecarreteiroviagem.manterRecibosAction.execute", "emitirRecibo", data));
		xmit(false);
	}

	function emitirRecibo_cb(strFile,error) {
		openReportWithLocator(strFile,error);
		if (error == undefined) {
			if (getElementValue("tpSituacaoRecibo.value") != "EM") {
				onDataLoad(getElementValue("idReciboFreteCarreteiro"));
			}
		}
	}

	function storeCancelarRecibo_cb(data,error,key) {
		if (error != undefined && key == "LMS-24009") {
			if (confirm(error)) {
				cancelarReciboAfterValidation();
			}
			return false
		}
		
		afterStore_cb(data,error,key)
	}
	
	function setDisabledAllButtons(value) {
		setDisabled("btnEmitir",value);
		setDisabled("btnCancelar",value);
		setDisabled("btnComplentar",value);	
	
		setDisabled("btnOcorrencias",value);
		setDisabled("btnControleCargas",value);
	}
	
	function initWindow(eventObj) {
		var blDetalhamento = eventObj.name == "gridRow_click" || eventObj.name == "storeButton"
				|| (eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id != "pesq");

		if (!blDetalhamento) {
			setDisabledAllButtons(false);
		} else if (eventObj.name == "tab_click") {
			comportamentoDetalhe();
		}
	}
	
	function cancelarReciboAfterValidation() {
		var data = new Array();
		setNestedBeanPropertyValue(data,"idReciboFreteCarreteiro",getElementValue("idReciboFreteCarreteiro"));
		var sdo = createServiceDataObject("lms.fretecarreteiroviagem.manterRecibosAction.storeCancelarReciboAfterValidation",
				"storeCancelarReciboAfterValidation",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function storeCancelarReciboAfterValidation_cb(data,error,key) {
		afterStore_cb(data,error,key)
	}

//-->
</script>