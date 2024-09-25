<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function pageLoadCustom_cb(data,error) {		
		onPageLoad_cb(data,error);
		findFilialUsuarioLogado();
	}
	
	function findFilialUsuarioLogado() {
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.emitirReciboAction.findFilialUsuarioLogado",
				"findFilialUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	// variaveis armazenarão temporiariamente informações da filial do usuário logado.
	var idFilial = undefined;
	var sgFilial = undefined;
	var nmFilial = undefined;
	
	function findFilialUsuarioLogado_cb(data,error) {
		idFilial = getNestedBeanPropertyValue(data,"idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
		populateFilial();
	}

	function populateFilial() {
		setElementValue("filial.idFilial",idFilial);
		setElementValue("filial.sgFilial",sgFilial);
		setElementValue("filial.pessoa.nmFantasia",nmFilial);
		
		setElementValue("reciboFreteCarreteiro2.filial.sgFilial",sgFilial);
	}
	
//-->
</script>
<adsm:window title="emitirExtratoFrota" onPageLoadCallBack="pageLoadCustom"
		service="lms.fretecarreteirocoletaentrega.emitirReciboAction" >
	<adsm:form action="/freteCarreteiroColetaEntrega/emitirRecibo">
		
		<adsm:lookup dataType="text" property="filial"
				idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.fretecarreteirocoletaentrega.emitirReciboAction.findLookupFilial"
    			action="/municipios/manterFiliais"
    			label="filialEmissao" size="3" maxLength="3" labelWidth="25%" width="75%" exactMatch="true" required="true"
    			onPopupSetValue="sgFilialReciboPopup"
    			onchange="return sgFilialReciboChange();" disabled="true">
         	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:propertyMapping relatedProperty="reciboFreteCarreteiro2.filial.sgFilial" modelProperty="sgFilial" />
         	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia"
         			size="30" disabled="true" serializable="false" />
	    </adsm:lookup>

		<%--Lookup Meio de Transporte------------------------------------------------------------------------------------------------------------%>
		<adsm:lookup dataType="text" property="meioTransporteRodoviario" 
				idProperty="idMeioTransporte" criteriaProperty="meioTransporte.nrFrota"
				service="lms.fretecarreteirocoletaentrega.emitirReciboAction.findLookupMeioTransporte" 
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" 
				onDataLoadCallBack="dataLoadMeioTransp"
				onchange="return changeMT();"
				label="meioTransporte" labelWidth="25%" width="75%" size="8" maxLength="6"
				cellStyle="vertical-Align:bottom" picker="false" >
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
					modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
					
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" 
					modelProperty="proprietario.idProprietario" />
								
			<adsm:propertyMapping relatedProperty="modeloMeioTransporte"
					modelProperty="meioTransporte.modeloMeioTransporte.dsModeloMeioTransporte" />
			<adsm:propertyMapping relatedProperty="marcaMeioTransporte"
					modelProperty="meioTransporte.modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte" />
					
			<adsm:lookup dataType="text" property="meioTransporteRodoviario2"
					idProperty="idMeioTransporte" criteriaProperty="meioTransporte.nrIdentificador"
					service="lms.fretecarreteirocoletaentrega.emitirReciboAction.findLookupMeioTransporte"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo"
					onDataLoadCallBack="dataLoadMeioTransp2" onPopupSetValue="popupMeioTransp"
					onchange="return changeMT2();"
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
				
				<adsm:propertyMapping relatedProperty="modeloMeioTransporte"
						modelProperty="meioTransporte.modeloMeioTransporte.dsModeloMeioTransporte" />
				<adsm:propertyMapping relatedProperty="marcaMeioTransporte"
						modelProperty="meioTransporte.modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte" />
			</adsm:lookup>
		</adsm:lookup>
		
		<%--Lookup Meio de Transporte------------------------------------------------------------------------------------------------------------%>
		
		<adsm:textbox dataType="text" property="marcaMeioTransporte" 
				size="24" maxLength="50" width="75%" labelWidth="25%" label="marcaMeioTransporte" disabled="true" />
		<adsm:textbox dataType="text" property="modeloMeioTransporte" 
				size="24" maxLength="50" width="75%" labelWidth="25%" label="modeloMeioTransporte" disabled="true" />

		<adsm:lookup dataType="text" property="proprietario" 
				idProperty="idProprietario" criteriaProperty="pessoa.nrIdentificacao" 
				relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.fretecarreteirocoletaentrega.emitirReciboAction.findLookupProprietario"
				action="/contratacaoVeiculos/manterProprietarios" 
				label="proprietario" labelWidth="25%" width="75%" size="20" maxLength="20" >
			<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa"
					modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" serializable="false" size="30" disabled="true"/>
		</adsm:lookup>


		<adsm:section caption="criteriosParaReemissao" />

		<adsm:textbox dataType="text" 
				property="reciboFreteCarreteiro2.filial.sgFilial" 
				label="preFatura" width="75%" labelWidth="25%" size="3" maxLength="3" 					
				serializable="false" disabled="true" >

			<adsm:lookup dataType="integer"
					property="reciboFreteCarreteiro" idProperty="idReciboFreteCarreteiro" criteriaProperty="nrReciboFreteCarreteiro" 
					service="lms.fretecarreteirocoletaentrega.emitirReciboAction.findLookupRecibo"
					action="/freteCarreteiroColetaEntrega/manterRecibos"
					mask="0000000000" maxLength="10" size="10" picker="true" exactMatch="true" >
	        	     	        	
     	       	<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filial.idFilial" />
     	       	<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filial.sgFilial" inlineQuery="false" />
     	       	<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
	        	<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="filial.idFilial" blankFill="false" />
	        	<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="filial.sgFilial" blankFill="false" />
	        	<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" 
	        			relatedProperty="filial.pessoa.nmFantasia" blankFill="false" />
	
				<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario" />
				<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" 
						modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" 
						modelProperty="proprietario.pessoa.nmPessoa" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.idMeioTransporte" 
						modelProperty="meioTransporteRodoviario.idMeioTransporte" />
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador" 
						modelProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrFrota" 
						modelProperty="meioTransporteRodoviario.meioTransporte.nrFrota" inlineQuery="false" />	
				
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte" 
						modelProperty="meioTransporteRodoviario.idMeioTransporte" blankFill="false" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte" 
						modelProperty="meioTransporteRodoviario.idMeioTransporte" blankFill="false" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador" 
						modelProperty="meioTransporte.nrIdentificador" blankFill="false" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrFrota" 
						modelProperty="meioTransporte.nrFrota" blankFill="false" />
				<adsm:propertyMapping relatedProperty="marcaMeioTransporte" 
						modelProperty="meioTransporte.dsMarca" blankFill="false" />
				<adsm:propertyMapping relatedProperty="modeloMeioTransporte" 
						modelProperty="meioTransporte.dsModelo" blankFill="false" />
				<adsm:propertyMapping relatedProperty="proprietario.idProprietario" 
						modelProperty="proprietario.idProprietario" blankFill="false" />
				<adsm:propertyMapping relatedProperty="proprietario.pessoa.nrIdentificacao" 
						modelProperty="proprietario.nrIdentificacao" blankFill="false" />
				<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" 
						modelProperty="proprietario.nmPessoa" blankFill="false" />
		
			</adsm:lookup>
		</adsm:textbox>

		<adsm:range label="periodoEmissao" labelWidth="25%" width="75%" >
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" />
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" />
		</adsm:range>
		
		<adsm:buttonBar>
			<adsm:button buttonType="reportViewerButton" id="btnVisualizar" caption="visualizar" onclick="imprimeRelatorio()"  />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
	
	function imprimeRelatorio() {
	    executeReportWithCallback('lms.fretecarreteirocoletaentrega.emitirReciboAction.execute', 'verificaEmissao', document.forms[0]);
	}
	
	function verificaEmissao_cb(strFile, error) {
		
		if (error != undefined) {
		        alert(error);
		}else{
			var msn = getNestedBeanPropertyValue(strFile, "_value");
			
			if (msn.indexOf("LMS-25134") > -1){
				alert(msn);
			}else{
			    openReportWithLocator(strFile, error);
			    
			    if (error == undefined) {
			    	var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.emitirReciboAction.validateEmissaoRecibo",
			    			"validateEmissaoRecibo",buildFormBeanFromForm(this.document.forms[0]));
			    	xmit({serviceDataObjects:[sdo]});
			    }
			}
		}
		
		
	}
	
	function validateEmissaoRecibo_cb(data, error) {
	    if (error != undefined) {
	        alert(error);
	    }
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			populateFilial();
		}
	}

	
	/**************************************************************************************************************************
	 * Funções referentes ao comportamento da lookup de recibo
	 **************************************************************************************************************************/	

	function setDisabledNrRecibo(disable) {
		setDisabled("reciboFreteCarreteiro.idReciboFreteCarreteiro",disable);
	}
	
	function sgFilialReciboPopup(data) {
		if (data != undefined) {
			setDisabledNrRecibo(false);
			return true;
		}
	}
		
	function sgFilialReciboChange(data,error) {
		if (getElementValue("filial.sgFilial") == "") {
			setDisabledNrRecibo(true);
			resetValue("reciboFreteCarreteiro.idReciboFreteCarreteiro");
		} else {
			setDisabledNrRecibo(false);
		}
		return lookupChange({e:document.forms[0].elements["filial.idFilial"]});
	}
	
	
	function dataLoadMeioTransp_cb(data) {
		meioTransporteRodoviario_meioTransporte_nrFrota_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			loadProprietarioByMeioTransp(getNestedBeanPropertyValue(data[0],"idMeioTransporte"));
	}
	function dataLoadMeioTransp2_cb(data) {
		meioTransporteRodoviario2_meioTransporte_nrIdentificador_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			loadProprietarioByMeioTransp(getNestedBeanPropertyValue(data[0],"idMeioTransporte"));
	}
 	function popupMeioTransp(data) {
		loadProprietarioByMeioTransp(getNestedBeanPropertyValue(data,"idMeioTransporte"));
		return true;
	}
	function loadProprietarioByMeioTransp(idMeioTransporte) {
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.emitirReciboAction.findProprietarioByMeioTransporte",
				"fillFormCustom",{e:idMeioTransporte});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function fillFormCustom_cb(data,error) {
		if (error != undefined)
			alert(error);
		else
			fillFormWithFormBeanData(document.forms[0].tabIndex, data);
	}
	
	function changeMT() {
		var flag = meioTransporteRodoviario_meioTransporte_nrFrotaOnChangeHandler();
		if (getElementValue("meioTransporteRodoviario.meioTransporte.nrFrota") == "") {
			resetValue("proprietario.idProprietario");
		}
		return flag;
	}
	
	function changeMT2() {
		var flag = meioTransporteRodoviario2_meioTransporte_nrIdentificadorOnChangeHandler();
		if (getElementValue("meioTransporteRodoviario2.meioTransporte.nrIdentificador") == "") {
			resetValue("proprietario.idProprietario");
		}
		return flag;
	}
	
//-->
</script>

