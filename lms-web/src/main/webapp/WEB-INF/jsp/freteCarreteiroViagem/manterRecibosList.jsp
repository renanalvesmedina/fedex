<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.fretecarreteiroviagem.manterRecibosAction" onPageLoadCallBack="pageLoadCallBackCustom" >
	
	<adsm:form action="/freteCarreteiroViagem/manterRecibos" height="100" idProperty="idReciboFreteCarreteiro" >
		<adsm:hidden property="sgFilialSugeridaLookup" />
		<adsm:hidden property="tpReciboFreteCarreteiro" serializable="false" />
		<adsm:lookup dataType="text" property="filial" 
				idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.fretecarreteiroviagem.manterRecibosAction.findLookupFilial"
    			action="/municipios/manterFiliais"
    			label="filial" size="3" maxLength="3" labelWidth="18%" width="32%" exactMatch="true" required="true" disabled="true">
         	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />
	    </adsm:lookup>
	    
	    <adsm:hidden property="blComplementar" />
		<adsm:textbox dataType="integer" property="nrReciboFreteCarreteiro" 
				label="numero" size="10" maxLength="10" labelWidth="18%" width="32%" mask="0000000000" />
		<adsm:combobox property="blAdiantamento" domain="DM_SIM_NAO"
				label="adiantamento" labelWidth="18%" width="32%" />

		<adsm:combobox property="tpSituacaoRecibo" domain="DM_STATUS_RECIBO_PAGAMENTO_FRETE_CARRETE"
				label="situacao" labelWidth="18%" width="32%" />

		<adsm:lookup dataType="text" property="proprietario" 
				idProperty="idProprietario" criteriaProperty="pessoa.nrIdentificacao"
				relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.fretecarreteiroviagem.manterRecibosAction.findLookupProprietario"
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
				label="meioTransporte" labelWidth="18%" width="32%" size="8" maxLength="6"
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

		<adsm:lookup dataType="text" 
				property="controleCarga.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
				service="lms.fretecarreteiroviagem.manterRecibosAction.findLookupFilial"
				label="controleCarga" size="3" maxLength="3" labelWidth="18%" width="32%" picker="false" serializable="false"
				action="/municipios/manterFiliais" onchange="return onFilialControleCargaChange(this);" >
			
			<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialOrigem.nmFantasia" 
						modelProperty="pessoa.nmFantasia" />
				
			<adsm:lookup dataType="integer"
					property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga"
					service="lms.fretecarreteiroviagem.manterRecibosAction.findLookupControleCarga"
					action="carregamento/manterControleCargas" size="8" mask="00000000" disabled="true"
					popupLabel="pesquisarControleCarga" >
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial"
						modelProperty="filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" 
						modelProperty="filialByIdFilialOrigem.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.nmFantasia" 
						modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" inlineQuery="false" />
	        </adsm:lookup>
	        <adsm:hidden property="controleCarga.filialByIdFilialOrigem.nmFantasia" serializable="false" />
		</adsm:lookup>

		<adsm:range label="periodoEmissao" labelWidth="18%" width="32%" maxInterval="60">
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
				var lms_24007 = "<adsm:label key="LMS-24007"/>";
			//-->
		</script>
		
	</adsm:form>
	
	<adsm:grid property="reciboFreteCarreteiro" idProperty="idReciboFreteCarreteiro"
			service="lms.fretecarreteiroviagem.manterRecibosAction.findPaginatedCustom" 
			rowCountService="lms.fretecarreteiroviagem.manterRecibosAction.getRowCountCustom"
			selectionMode="none" unique="true" scrollBars="horizontal" gridHeight="180" rows="9" onRowClick="onRowClickDef" >
		
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="numeroRecibo" property="filial.sgFilial" width="65" />
			<adsm:gridColumn title="" property="nrReciboFreteCarreteiro2" width="35" />
		</adsm:gridColumnGroup> 

		<adsm:gridColumn title="dataEmissao" property="dhEmissao" width="140" dataType="JTDateTimeZone" />
		<adsm:gridColumn title="situacao" property="tpSituacaoRecibo" isDomain="true" width="125" />
		<adsm:gridColumn title="adiantamento" property="blAdiantamento" width="100" renderMode="image-check" />
				
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA" >
			<adsm:gridColumn title="controleCarga" property="controleCarga.sgFilialOrigem" width="60" />
			<adsm:gridColumn title="" property="controleCarga.nrControleCarga" width="60" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="identificacao" property="proprietario.tpIdentificacao" isDomain="true" width="50" />
		<adsm:gridColumn title="" property="proprietario.nrIdentificacao" align="right" width="120"/>
		<adsm:gridColumn title="proprietario" property="proprietario.nmPessoa" width="140" />
		
		<adsm:gridColumn title="meioTransporte"
				property="meioTransporte.nrFrota" width="60" />
		<adsm:gridColumn title="" 
				property="meioTransporte.nrIdentificador" width="70" />
		
		<adsm:gridColumn title="dataPagamentoReal" property="dtPgtoReal" width="150" dataType="JTDate" />
		
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="valorLiquido" property="sgMoeda" width="50" />
			<adsm:gridColumn title="" property="dsSimbolo" width="20" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" property="vlLiquido" width="80" dataType="currency" />
				
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />
		</adsm:buttonBar>
	</adsm:grid>
	 
</adsm:window>
<script>
<!--
//	var isLookup = false;
	var isLookup = window.dialogArguments && window.dialogArguments.window;
	if (isLookup) {
		document.getElementById('btnFechar').property = ".closeButton";
		setDisabled('btnFechar',false);
	} else {
		setVisibility('btnFechar', false);
	}	
		
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			if (!isLookup) {
				populateFilial();
			}
			setDisabled("controleCarga.idControleCarga",true);
		} else if (eventObj.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("cad", true );
			tabGroup.setDisabledTab("adi", true );
		}
	}
	
	function pageLoadCallBackCustom_cb(data,error) {		
		onPageLoad_cb(data,error);
		
		
		if (isLookup) {
			document.getElementById("nrReciboFreteCarreteiro").masterLink = false;
			setDisabled("nrReciboFreteCarreteiro",false);
		}
		
		setFocusOnFirstFocusableField();
		
		findFilialUsuarioLogado();
	}

	function findFilialUsuarioLogado() {
		var sdo = createServiceDataObject("lms.fretecarreteiroviagem.manterRecibosAction.findFilialUsuarioLogado",
				"findFilialUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	var idFilial = undefined;
	var sgFilial = undefined;
	var nmFilial = undefined;
	
	function findFilialUsuarioLogado_cb(data,error) {
		idFilial = getNestedBeanPropertyValue(data,"idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
		if (!isLookup)
			populateFilial();
	}

	function populateFilial() {
		if (document.getElementById("filial.idFilial").masterLink != "true") {
			setElementValue("filial.idFilial",idFilial);
			setElementValue("filial.sgFilial",sgFilial);
			setElementValue("filial.pessoa.nmFantasia",nmFilial);
		}
	}
 
	function validateTab() {
		if (validateTabScript(document.forms)) {
			if (getElementValue("nrReciboFreteCarreteiro") != "" ||
					getElementValue("controleCarga.idControleCarga") != "" ||
					getElementValue("proprietario.idProprietario") != "" ||
					getElementValue("meioTransporteRodoviario.idMeioTransporte") != "" ||
					(getElementValue("dhEmissaoInicial") != "" && getElementValue("dhEmissaoFinal") != "") ||
					(getElementValue("dtPagtoRealInicial") != "" && getElementValue("dtPagtoRealFinal") != "") ) {
				return true;
			} else {
				alert(lms_24007);
			}
		}
		return false;
	}
	
	function onRowClickDef() {
		if (!isLookup) {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab('cad', false );
			// será desabilitado no detalhamento
			//tabGroup.setDisabledTab('adi', false );
		}
	}
	
	function onFilialControleCargaChange(elem) {
		setDisabled("controleCarga.idControleCarga",elem.value == "");
		return controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}
	
//-->
</script>