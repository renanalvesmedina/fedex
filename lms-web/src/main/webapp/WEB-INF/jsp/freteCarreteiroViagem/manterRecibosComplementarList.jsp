<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.fretecarreteiroviagem.manterRecibosComplementarAction" >
	<adsm:form action="/freteCarreteiroViagem/manterRecibosComplementar" height="102" idProperty="idReciboFreteCarreteiro" >
		
		<adsm:lookup dataType="text" property="filial" 
				idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.fretecarreteiroviagem.manterRecibosComplementarAction.findLookupFilial"
    			action="/municipios/manterFiliais" onchange="return filialChange(this);"
    			onPopupSetValue="popupFilial"
    			label="filialEmissao" size="3" maxLength="3" labelWidth="18%" width="32%" exactMatch="true" >
         	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:propertyMapping relatedProperty="reciboComplementado.filial.sgFilial" modelProperty="sgFilial" />
         	
         	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />
	    </adsm:lookup>

		<adsm:combobox property="tpReciboFreteCarreteiro" domain="DM_TIPO_RECIBO_PAGAMENTO_FRETE_CARRETEIR"
				label="tipoRecibo" labelWidth="18%" width="32%" onchange="return tpReciboChange(this);" onDataLoadCallBack="tipoReciboFreteCarreteiro"/>
		

		<%--Lookup Recibo Frete Carreteiro ------------------------------------------------------------------------------------------------------%>
		<adsm:hidden property="blComplementar" value="N" />
		<adsm:hidden property="blAdiantamento" value="N" />
		<adsm:textbox dataType="text" property="reciboComplementado.filial.sgFilial"
				label="recibo" size="3" maxLength="3" labelWidth="18%" width="32%" cellStyle="vertical-align=bottom;"
				disabled="true" serializable="false" picker="false" >
							
			<adsm:lookup dataType="integer" property="reciboComplementado"
					idProperty="idReciboFreteCarreteiro" criteriaProperty="nrReciboFreteCarreteiro"
					service="lms.fretecarreteiroviagem.manterRecibosComplementarAction.findLookupRecibo"
					action="/freteCarreteiroViagem/manterRecibos"
					size="10" maxLength="10" cellStyle="vertical-align=bottom;" exactMatch="true" mask="0000000000"
					picker="true" disabled="true" serializable="true" >
	        	
	        	<adsm:propertyMapping modelProperty="tpReciboFreteCarreteiro" criteriaProperty="tpReciboFreteCarreteiro" />
	        	
	        	<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filial.idFilial" />
	        	<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filial.sgFilial" inlineQuery="false" />
	        	<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
	        	
	        	<adsm:propertyMapping modelProperty="blComplementar" criteriaProperty="blComplementar" />
	        	<adsm:propertyMapping modelProperty="blAdiantamento" criteriaProperty="blAdiantamento" />
	        </adsm:lookup>
	    </adsm:textbox>
		<%--Lookup Recibo Frete Carreteiro ------------------------------------------------------------------------------------------------------%>
       
       
		<adsm:textbox dataType="integer" property="nrReciboFreteCarreteiro" 
				label="reciboComplementar" size="10" maxLength="10" labelWidth="18%" width="32%" 
				cellStyle="vertical-align=bottom;" mask="0000000000" />
		
		<adsm:combobox property="tpSituacaoRecibo" domain="DM_STATUS_RECIBO_PAGAMENTO_FRETE_CARRETE" renderOptions="true"
				label="situacao" labelWidth="18%" width="32%" />
		
		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem" 
				idProperty="idFilial" criteriaProperty="sgFilial"
				service="lms.fretecarreteiroviagem.manterRecibosComplementarAction.findLookupFilial"
				action="municipios/manterFiliais" onchange="return filialControleCargaChange(this);"
				label="controleCarga" size="3" maxLength="3" labelWidth="18%" width="32%" picker="false" popupLabel="pesquisarFilial">
        	
        	<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialOrigem.nmFantasia" 
						modelProperty="pessoa.nmFantasia" />
        	
        	<adsm:lookup dataType="integer"
					property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga"
					service="lms.fretecarreteiroviagem.manterRecibosComplementarAction.findLookupControleCarga"
					action="carregamento/manterControleCargas" size="8" mask="00000000" disabled="true" popupLabel="pesquisarControleCarga">
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" 
						modelProperty="filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" 
						modelProperty="filialByIdFilialOrigem.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.nmFantasia" 
						modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" inlineQuery="false" />
	        </adsm:lookup>
	        <adsm:hidden property="controleCarga.filialByIdFilialOrigem.nmFantasia" serializable="false" />
        </adsm:lookup>

		<adsm:lookup dataType="text" property="proprietario" idProperty="idProprietario"
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.fretecarreteiroviagem.manterRecibosComplementarAction.findLookupProprietario"
				action="/contratacaoVeiculos/manterProprietarios" 
				label="proprietario" labelWidth="18%" width="82%" size="20" maxLength="20" >
			<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa"
					modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" serializable="false" size="30" disabled="true"/>
		</adsm:lookup>


		<%--Lookup Meio de Transporte------------------------------------------------------------------------------------------------------------%>
		<adsm:lookup dataType="text" property="meioTransporteRodoviario" 
				idProperty="idMeioTransporte" criteriaProperty="meioTransporte.nrFrota"
				service="lms.fretecarreteiroviagem.manterRecibosAction.findLookupMeioTransporte" 
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" 
				label="meioTransporte" labelWidth="18%" width="82%" size="8" maxLength="6"
				cellStyle="vertical-Align:bottom" picker="false" >
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
					modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
					
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" 
					modelProperty="proprietario.idProprietario" />
					
			<adsm:lookup dataType="text" property="meioTransporteRodoviario2"
					idProperty="idMeioTransporte" criteriaProperty="meioTransporte.nrIdentificador"
					service="lms.fretecarreteiroviagem.manterRecibosAction.findLookupMeioTransporte"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo"
					serializable="false" size="20" maxLength="25" picker="true" cellStyle="vertical-Align:bottom" >
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
						modelProperty="idMeioTransporte" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
						
				<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" 
						modelProperty="proprietario.idProprietario" />
				<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" 
						modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" 
						modelProperty="proprietario.pessoa.nmPessoa" inlineQuery="false" />
			</adsm:lookup>
		
		</adsm:lookup>
		
		
		<%--Lookup Meio de Transporte------------------------------------------------------------------------------------------------------------%>
		
		
		<adsm:range label="periodoEmissao" labelWidth="18%" width="32%" maxInterval="60" >
			<adsm:textbox dataType="JTDate" property="dhEmissaoInicial" cellStyle="vertical-align=bottom;" />
			<adsm:textbox dataType="JTDate" property="dhEmissaoFinal" cellStyle="vertical-align=bottom;"/>
		</adsm:range>
		<adsm:range label="periodoPagamentoReal" labelWidth="18%" width="32%" maxInterval="60">
			<adsm:textbox dataType="JTDate" property="dtPagtoRealInicial" cellStyle="vertical-align=bottom;"/>
			<adsm:textbox dataType="JTDate" property="dtPagtoRealFinal" cellStyle="vertical-align=bottom;"/>
		</adsm:range>

		<adsm:textbox dataType="integer" property="relacaoPagamento.nrRelacaoPagamento" mask="0000000000"
        		label="relacaoPagamento" size="10" maxLength="10" labelWidth="18%" width="82%" />

		<adsm:buttonBar freeLayout="true" >
			<adsm:findButton callbackProperty="reciboFreteCarreteiro" />
			<adsm:resetButton />
		</adsm:buttonBar>
		
		<script>
			<!--
				var lms_24010 = "<adsm:label key="LMS-24010"/>";
			//-->
		</script>
		
	</adsm:form>

	<adsm:grid property="reciboFreteCarreteiro" idProperty="idReciboFreteCarreteiro"
			service="lms.fretecarreteiroviagem.manterRecibosComplementarAction.findPaginatedCustom"
			rowCountService="lms.fretecarreteiroviagem.manterRecibosComplementarAction.getRowCountCustom"
			selectionMode="none" unique="true" scrollBars="horizontal" gridHeight="168" rows="8" >
		
		<adsm:gridColumn title="tipoRecibo" property="tpReciboFreteCarreteiro" width="90" />
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="recibo" property="reciboFreteCarreteiro.filial.sgFilial" width="55" />
			<adsm:gridColumn title="" property="reciboFreteCarreteiro.nrReciboFreteCarreteiro" 
					width="35" dataType="integer" mask="0000000000" />
		</adsm:gridColumnGroup> 
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="reciboComplementar" property="filial.sgFilial" width="95" />
			<adsm:gridColumn title="" property="nrReciboFreteCarreteiro" 
					width="35" dataType="integer" mask="0000000000" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="dataEmissao" property="dhEmissao" width="140" dataType="JTDateTimeZone" />
		<adsm:gridColumn title="situacao" property="tpSituacaoRecibo" isDomain="true" width="125" />
				
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA" >
			<adsm:gridColumn title="controleCarga" property="controleCarga.sgFilialOrigem" width="60" />
			<adsm:gridColumn title="" property="controleCarga.nrControleCarga" width="60" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="identificacao" property="proprietario.tpIdentificacao" width="50" />
		<adsm:gridColumn title="" property="proprietario.nrIdentificacao" align="right" width="120"/>
		<adsm:gridColumn title="proprietario" property="proprietario.nmPessoa" width="140" />
		
		<adsm:gridColumn title="meioTransporte"
				property="meioTransporte_nrFrota" width="60" />
		<adsm:gridColumn title="" 
				property="meioTransporte_nrIdentificador" width="70" />
		
		<adsm:gridColumn title="dataPagamentoReal" property="dtPgtoReal" width="150" dataType="JTDate" />
		
		<adsm:gridColumn title="valorLiquido" property="dsMoeda_01" width="70" />
		<adsm:gridColumn title="" property="vlLiquido" width="80" dataType="currency" />
		
		<adsm:buttonBar />
	</adsm:grid>
			
</adsm:window>

<script type="text/javascript">
<!--
	document.getElementById("blAdiantamento").masterLink = true;
	document.getElementById("blComplementar").masterLink = true;

	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			setDisabled("reciboComplementado.idReciboFreteCarreteiro",true);
			setDisabled("controleCarga.idControleCarga",true);
		}
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
	

	function validateTab() {
		if (validateTabScript(document.forms)) {
			if (getElementValue("reciboComplementado.idReciboFreteCarreteiro") != "" ||
					getElementValue("nrReciboFreteCarreteiro") != "" ||
					getElementValue("controleCarga.idControleCarga") != "" ||
					getElementValue("proprietario.idProprietario") != "" ||
					getElementValue("meioTransporteRodoviario.idMeioTransporte") != "" ||
					(getElementValue("dtPagtoRealInicial") != "" && getElementValue("dtPagtoRealFinal") != "") ||
					(getElementValue("dhEmissaoInicial") != "" && getElementValue("dhEmissaoFinal") != ""))
				return true;
			else
				alert(lms_24010);
		}
		return false;
	}

	function filialChange(e) {
		manipulaEstadoRecibo(e.value != "",undefined);
		return filial_sgFilialOnChangeHandler();
	}

	function popupFilial(data) {
		manipulaEstadoRecibo(true,undefined);
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

	function filialControleCargaChange(e) {
		setDisabled("controleCarga.idControleCarga",e.value == "");
		return controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}

//-->
</script>
