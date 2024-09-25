<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarMeiosTransporte" service="lms.contratacaoveiculos.manterMeiosTransporteAction"
		onPageLoadCallBack="pageLoadCustom" >
	<adsm:form action="/contratacaoVeiculos/manterMeiosTransporte" idProperty="idMeioTransporte" height="100" >
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
		</adsm:i18nLabels>
		
		<adsm:hidden property="idFilialSessao" />
		<adsm:hidden property="sgFilialSessao" />
		<adsm:hidden property="nmFilialSessao" />
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		
		<adsm:hidden property="tipoEmpresaFilial" value="M" serializable="false" />
		<adsm:lookup dataType="text" property="filial" idProperty="idFilial"
				service="lms.contratacaoveiculos.manterMeiosTransporteAction.findLookupFilial"
				criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
				action="/municipios/manterFiliais" labelWidth="20%" width="80%" exactMatch="true" >
         	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
         	<adsm:propertyMapping criteriaProperty="tipoEmpresaFilial" modelProperty="empresa.tpEmpresa"/>
   			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="28"
					maxLength="50" disabled="true" serializable="false" />
	    </adsm:lookup>

		<adsm:combobox property="tpVinculo" label="tipoVinculo" domain="DM_TIPO_VINCULO_VEICULO"
				labelWidth="20%" width="30%" renderOptions="true"/>
		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL"
				labelWidth="20%" width="30%" renderOptions="true"/>
			
		<adsm:hidden property="dontFillFilial" value="true" />
		<adsm:lookup dataType="text" property="proprietario" idProperty="idProprietario"
				service="lms.contratacaoveiculos.manterMeiosTransporteAction.findLookupProprietario"
				action="/contratacaoVeiculos/manterProprietarios"
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				label="proprietario" labelWidth="20%" width="19%" size="20" maxLength="50" onDataLoadCallBack="proprietarioDataLoad" 
				onPopupSetValue="proprietarioPopup">
			<adsm:propertyMapping criteriaProperty="dontFillFilial" modelProperty="dontFillFilial" />
			<adsm:propertyMapping relatedProperty="proprietario2.pessoa.nmPessoa"
					modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="proprietario2.pessoa.nmPessoa" serializable="false" size="35" width="55%" disabled="true"/>
		</adsm:lookup>
		
		<adsm:textbox dataType="text" property="nrFrota" label="meioTransporte"
				maxLength="6" size="6" labelWidth="20%" width="80%" cellStyle="vertical-align:bottom;">
			<adsm:textbox dataType="text" property="nrIdentificador"
					maxLength="25" size="25" cellStyle="vertical-align:bottom;"
					onchange="return onNrIdentificadorChange(this);" />
		</adsm:textbox>

		<%-- Combo 'Tipo meio transporte' é montada dinamicamente pela combo 'meio transporte' --%>
		<adsm:combobox property="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" domain="DM_TIPO_MEIO_TRANSPORTE"
				label="modalidade" labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;" />	
		<adsm:combobox property="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"
				optionProperty="idTipoMeioTransporte" optionLabelProperty="dsTipoMeioTransporte"
				service="lms.contratacaoveiculos.manterMeiosTransporteAction.findComboTipoMeioTransporte"
				label="tipoMeioTransporte" labelWidth="20%" width="30%" boxWidth="180" cellStyle="vertical-align:bottom;" 
				onDataLoadCallBack="tipoMeioTransporteDataLoad" >
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte"
					modelProperty="tpMeioTransporte" />
		</adsm:combobox>		
		
		<%-- Combo 'Modelo meio transporte' é montada dinamicamente pelas combos
				'Tipo meio transporte' e 'Marca meio transporte' --%>
		<adsm:combobox property="modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte"
				optionProperty="idMarcaMeioTransporte" optionLabelProperty="dsMarcaMeioTransporte"
				service="lms.contratacaoveiculos.manterMeiosTransporteAction.findComboMarcaMeioTransporte"
				label="marca" labelWidth="20%" width="30%" boxWidth="180" >
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte"
					modelProperty="tpMeioTransporte" />
		</adsm:combobox>
		<adsm:combobox property="modeloMeioTransporte.idModeloMeioTransporte"
				optionProperty="idModeloMeioTransporte" optionLabelProperty="dsModeloMeioTransporte"
				service="lms.contratacaoveiculos.manterMeiosTransporteAction.findComboModeloByTipoByMarca"
				label="modelo" labelWidth="20%" width="30%" boxWidth="180" >
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte"
					modelProperty="marcaMeioTransporte.idMarcaMeioTransporte" />
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"
					modelProperty="tipoMeioTransporte.idTipoMeioTransporte" />
		</adsm:combobox>

		<adsm:textbox dataType="integer" property="nrAnoFabricao" label="anoFabricacao"
				maxLength="4" size="22" labelWidth="20%" width="30%"/>
		<adsm:textbox dataType="integer" property="nrCodigoBarra" label="codigoBarras"
				maxLength="12" size="30" labelWidth="20%" width="30%"/>
				
		<adsm:combobox property="tipoCombustivel.idTipoCombustivel"
				optionProperty="idTipoCombustivel" optionLabelProperty="dsTipoCombustivel"
				service="lms.contratacaoveiculos.manterMeiosTransporteAction.findComboTipoCombustivel"
				label="tipoCombustivel" labelWidth="20%" width="60%" >
		</adsm:combobox>
						
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS_PESSOA"
				labelWidth="20%" width="30%" renderOptions="true"/>

		<adsm:range label="periodoAtualizacao" labelWidth="20%"  width="33%"> 
			<adsm:textbox dataType="JTDate" property="dtAtualizacaoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtAtualizacaoFinal"/>
		</adsm:range>	
		<adsm:section caption="informacoesBloqueioLiberacao"/>
		<adsm:combobox property="tpStatus" label="situacao" domain="DM_STATUS_BLOQUEIO"
				labelWidth="20%" width="30%" renderOptions="true"/>
 
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="meioTransporte" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="meioTransporte" idProperty="idMeioTransporte"
			service="lms.contratacaoveiculos.manterMeiosTransporteAction.findPaginatedCustom"
			rowCountService="lms.contratacaoveiculos.manterMeiosTransporteAction.getRowCountCustom"
			gridHeight="180" rows="9" unique="true" scrollBars="horizontal" >
		 
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filial" property="filial.sgFilial" width="90" />
			<adsm:gridColumn title="" property="filial.pessoa.nmFantasia" width="90" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="tipoVinculo" property="tpVinculo" isDomain="true" width="110" />
		<adsm:gridColumn title="modalidade"
				property="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" width="100" isDomain="true" />
		<adsm:gridColumn title="tipoMeioTransporte"
				property="modeloMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte" width="150" />
		<adsm:gridColumn title="meioTransporte" property="nrFrota" width="60" />
		<adsm:gridColumn title="" property="nrIdentificador" width="120" />
		<adsm:gridColumn title="marca" property="modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte" width="100" />
		<adsm:gridColumn title="modelo" property="modeloMeioTransporte.dsModeloMeioTransporte" width="90" />
		<adsm:gridColumn title="anoFabricacao" property="nrAnoFabricao" width="130" dataType="integer" />
		<adsm:gridColumn title="bloqueioLiberacao" property="tpStatus" width="150" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="100" />
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script> 

	document.getElementById("idFilialSessao").masterLink = true;
	document.getElementById("sgFilialSessao").masterLink = true;
	document.getElementById("nmFilialSessao").masterLink = true;

	var isLookup = null;
	
	function pageLoadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		isLookup = window.dialogArguments && window.dialogArguments.window;
		findFilialUsuarioLogado();
	 	
		if (isLookup) {
			document.getElementById("nrFrota").masterLink = false;
			setDisabled("nrFrota",false);
		}
		if (getElementValue("modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte") != "") {
			notifyElementListeners({e:document.getElementById("modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte")});
		}
	}

	function findFilialUsuarioLogado() {
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterMeiosTransporteAction.findFilialUsuarioLogado",
				"findFilialUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findFilialUsuarioLogado_cb(data,error) {
		setElementValue("idFilialSessao",getNestedBeanPropertyValue(data,"idFilial"));
		setElementValue("sgFilialSessao",getNestedBeanPropertyValue(data,"sgFilial"));
		setElementValue("nmFilialSessao",getNestedBeanPropertyValue(data,"pessoa.nmFantasia"));
	}

	function populateFilial() {
		if (!isLookup) {
			setElementValue("filial.idFilial",getElementValue("idFilialSessao"));
			setElementValue("filial.sgFilial",getElementValue("sgFilialSessao"));
			setElementValue("filial.pessoa.nmFantasia",getElementValue("nmFilialSessao"));
		}
	}
	
	function disableTabAnexos(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("anexo", disabled);
	}
	
	function initWindow(eventObj) {
		var event = eventObj.name;
		
		if(event == "tab_click"){
			disableTabAnexos(true);
		}
	}

	function proprietarioDataLoad_cb(data){
		proprietario_pessoa_nrIdentificacao_exactMatch_cb(data);
		var nrIdentificacaoFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
		if (nrIdentificacaoFormatado != undefined && nrIdentificacaoFormatado != ''){
			setElementValue("proprietario.pessoa.nrIdentificacao", nrIdentificacaoFormatado);
		}
	}
		
	function proprietarioPopup(data){
		var nrIdentificacaoFormatado = getNestedBeanPropertyValue(data, "pessoa.nrIdentificacaoFormatado");
		if (nrIdentificacaoFormatado != undefined && nrIdentificacaoFormatado != ''){
			setNestedBeanPropertyValue(data, "pessoa.nrIdentificacao", nrIdentificacaoFormatado);
		}
	}

	function onNrIdentificadorChange(elem) {
		elem.value = elem.value.toUpperCase();
		return validate(elem);
	}

	function tipoMeioTransporteDataLoad_cb(data) {
		modeloMeioTransporte_tipoMeioTransporte_idTipoMeioTransporte_cb(data);
		if (window.dialogArguments && window.dialogArguments.window.document.getElementById("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte")) {
	   		setElementValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte", getElementValue(window.dialogArguments.window.document.getElementById("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte")));
	   	}
	   	if (window.dialogArguments && window.dialogArguments.window.document.getElementById("tipoMeioTransporte.idTipoMeioTransporte")) {
	   		setElementValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte", getElementValue(window.dialogArguments.window.document.getElementById("tipoMeioTransporte.idTipoMeioTransporte")));
	   	}
	}
	
	function validateTab() {
		if (validateTabScript(document.Lazy)) {
			if (getElementValue("nrCodigoBarra") != "" ||
				getElementValue("nrFrota") != "" ||
				getElementValue("nrIdentificador") != "" ||
				getElementValue("tpVinculo") != "" ||
				getElementValue("proprietario.idProprietario") != "" ||
				getElementValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte") != "")
					return true;
			else{
				alert(i18NLabel.getLabel("LMS-00013") +
						document.getElementById("nrCodigoBarra").label + ", " +
						document.getElementById("nrFrota").label + ", " +
						document.getElementById("tpVinculo").label + ", " +
						document.getElementById("proprietario.idProprietario").label + ", " +
						document.getElementById("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte").label + ".");
			}
		}
		return false;
	}
	
	function validateLookupForm() {
      	return validateTab();
	}
	
</script>