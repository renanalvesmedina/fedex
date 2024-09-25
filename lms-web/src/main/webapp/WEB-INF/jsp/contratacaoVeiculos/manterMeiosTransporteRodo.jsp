<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript" type="text/javascript">
	function pageLoadCustom() {
		if (window.dialogArguments &&
				window.dialogArguments.window.document.getElementById("blChangeServiceTipoMTCombo") &&
				window.dialogArguments.window.document.getElementById("blChangeServiceTipoMTCombo").value == "S") {

			getElement("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte").service = 
					"lms.contratacaoveiculos.meioTransporteRodoviarioService.findListByIdWithComposto";

			var idTipo = window.dialogArguments.window.document.getElementById("tipoMeioTransporte.idTipoMeioTransporte");

			if (idTipo != undefined && idTipo != "") {
				setElementValue("idTipoMeioTransporteForCombo", idTipo)
			}
		}
		onPageLoad();
	}

	function tipoMeioTransporteDataLoad_cb(data) {
		meioTransporte_modeloMeioTransporte_tipoMeioTransporte_idTipoMeioTransporte_cb(data);

		// @see 'Manter Meios de Transporte'.
		if (getElementValue("setaTipoMeioTransporte") == 'N')
			return;

		if (window.dialogArguments) {
			var openerDocument = window.dialogArguments.window.document;
			if (openerDocument.getElementById("modeloMeioTransporte.tipoMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte")) {
		   		setElementValue("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte", 
		   				getElementValue(openerDocument.getElementById(
		   				"modeloMeioTransporte.tipoMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte")));
			} else if (openerDocument.getElementById("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte")) {
		   		setElementValue("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte", 
		   				getElementValue(openerDocument.getElementById(
		   				"modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte")));
		   	} else if (openerDocument.getElementById("tipoMeioTransporte.idTipoMeioTransporte")) {
		   		setElementValue("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte", 
		   				getElementValue(openerDocument.getElementById(
		   				"tipoMeioTransporte.idTipoMeioTransporte")));
		   	}

			if (openerDocument.getElementById("blChangeServiceTipoMTCombo") &&
				openerDocument.getElementById("blChangeServiceTipoMTCombo").value == "S") {

			   	if (getElement("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte").data.length == 1) {
			   		getElement("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte").disabled = true;
			   	} else {
			   		getElement("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte").disabled = false;
			   		getElement("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte").required = "true";
			   	}
			}		   	
		}
	}
	
	function rodoLoad_cb(data,exception) {
		onPageLoad_cb(data,exception);		
		setElementValue("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte", "R");
		getElement("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte").masterLink = "true";
		notifyElementListeners({e:getElement("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte")});

		getElement("meioTransporte.nrFrota").masterLink = false;
		setDisabled("meioTransporte.nrFrota",false);
	}
</script>

<adsm:window title="consultarMeiosTransporte" service="lms.contratacaoveiculos.meioTransporteRodoviarioService" 
		onPageLoad="pageLoadCustom" onPageLoadCallBack="rodoLoad" >
	<adsm:form action="/contratacaoVeiculos/manterMeiosTransporte" idProperty="idMeioTransporte" height="108">

		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
		</adsm:i18nLabels>

		<adsm:hidden property="meioTransporte.meioTranspProprietarios.proprietario.idProprietario" serializable="false"/>
		<adsm:hidden property="setaTipoMeioTransporte" serializable="false"/>

		<adsm:hidden property="isCalledByLookup" value="false" serializable="true"/>

		<adsm:lookup dataType="text" property="meioTransporte.filial" idProperty="idFilial"
				service="lms.municipios.filialService.findLookup"
				criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
				action="/municipios/manterFiliais" labelWidth="20%" width="80%" exactMatch="true" >
         	<adsm:propertyMapping relatedProperty="meioTransporte.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
   			<adsm:textbox dataType="text" property="meioTransporte.filial.pessoa.nmFantasia" size="28"
					maxLength="50" disabled="true" serializable="false"/>
	    </adsm:lookup>

		<adsm:combobox property="meioTransporte.tpVinculo" label="tipoVinculo" domain="DM_TIPO_VINCULO_VEICULO"
				labelWidth="20%" width="30%"/>
		<adsm:combobox property="meioTransporte.tpModal" label="modal" domain="DM_MODAL"
				labelWidth="20%" width="30%"/>

		<adsm:lookup dataType="text" property="proprietario" idProperty="idProprietario"
				service="lms.contratacaoveiculos.proprietarioService.findLookup"
				action="/contratacaoVeiculos/manterProprietarios" criteriaProperty="pessoa.nrIdentificacao"
				label="proprietario" labelWidth="20%" width="19%" size="20" maxLength="50" onDataLoadCallBack="proprietarioDataLoad" onPopupSetValue="proprietarioPopup">
			<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa"
					modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" serializable="false" size="35" width="55%" disabled="true"/>
		</adsm:lookup>

		<adsm:hidden property="idTipoMeioTransporteForCombo" serializable="false"/>

		<%-- Combo 'Tipo meio transporte' é montada dinamicamente pela combo 'meio transporte' --%>
		<adsm:combobox property="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" domain="DM_TIPO_MEIO_TRANSPORTE"
				label="modalidade" labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;"
				disabled="true"/>
		<adsm:combobox property="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"
				optionProperty="idTipoMeioTransporte" optionLabelProperty="dsTipoMeioTransporte"
				service="lms.contratacaoveiculos.manterMeiosTransporteAction.findComboTipoMeioTransporte"
				label="tipoMeioTransporte" labelWidth="20%" width="30%" boxWidth="180" cellStyle="vertical-align:bottom;" onDataLoadCallBack="tipoMeioTransporteDataLoad">
			<adsm:propertyMapping criteriaProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte"
					modelProperty="tpMeioTransporte" />
			<adsm:propertyMapping criteriaProperty="idTipoMeioTransporteForCombo" modelProperty="idTipoMeioTransporteForCombo"/>
		</adsm:combobox>

		<adsm:textbox dataType="text" property="meioTransporte.nrFrota" label="meioTransporte"
			maxLength="6" size="6" labelWidth="20%" width="80%" cellStyle="vertical-align:bottom;">
			<adsm:textbox dataType="text" property="meioTransporte.nrIdentificador" 
				maxLength="25" size="25" cellStyle="vertical-align:bottom;"
				onchange="return onNrIdentificadorChange(this);"/>
		</adsm:textbox>

		<%-- Combo 'Modelo meio transporte' é montada dinamicamente pelas combos
				'Tipo meio transporte' e 'Marca meio transporte' --%>
		<adsm:combobox property="meioTransporte.modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte"
			optionProperty="idMarcaMeioTransporte" optionLabelProperty="dsMarcaMeioTransporte"
			service="lms.contratacaoveiculos.manterMeiosTransporteAction.findComboMarcaMeioTransporte"
			label="marca" labelWidth="20%" width="30%" boxWidth="180">
			<adsm:propertyMapping criteriaProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte"
					modelProperty="tpMeioTransporte"/>
		</adsm:combobox>
		<adsm:combobox property="meioTransporte.modeloMeioTransporte.idModeloMeioTransporte"
			optionProperty="idModeloMeioTransporte" optionLabelProperty="dsModeloMeioTransporte"
			service="lms.contratacaoveiculos.manterMeiosTransporteAction.findComboModeloMeioTransporte"
			label="modelo" labelWidth="20%" width="30%" boxWidth="180" >
			<adsm:propertyMapping criteriaProperty="meioTransporte.modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte"
				modelProperty="marcaMeioTransporte.idMarcaMeioTransporte" />
			<adsm:propertyMapping criteriaProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"
				modelProperty="tipoMeioTransporte.idTipoMeioTransporte" />
		</adsm:combobox>

		<adsm:textbox dataType="integer" property="meioTransporte.nrAnoFabricao" label="anoFabricacao"
				maxLength="4" size="22" labelWidth="20%" width="30%"/>
				
		<adsm:combobox property="meioTransporte.tpSituacao" label="situacao" domain="DM_STATUS_PESSOA"
			labelWidth="20%" width="30%" />
		<adsm:section caption="informacoesBloqueioLiberacao"/>
		<adsm:combobox property="tpStatus" label="situacao" domain="DM_STATUS_BLOQUEIO"
				labelWidth="20%" width="30%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="meioTransporteRodoviario" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="meioTransporteRodoviario" idProperty="idMeioTransporte" gridHeight="164" rows="8" unique="true" scrollBars="horizontal">
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filial" property="meioTransporte.filial.sgFilial" width="75" />
			<adsm:gridColumn title="" property="meioTransporte.filial.pessoa.nmFantasia" width="75" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="tipoVinculo" property="meioTransporte.tpVinculo" isDomain="true" width="80" />
		<adsm:gridColumn title="modalidade"
				property="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" width="100" isDomain="true" />
		<adsm:gridColumn title="tipoMeioTransporte"
				property="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte" width="100" />
				
		<adsm:gridColumn title="meioTransporte" property="meioTransporte.nrFrota" width="60" />
		<adsm:gridColumn title="" property="meioTransporte.nrIdentificador" width="120" />
		<adsm:gridColumn title="marca" property="meioTransporte.modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte" width="100" />
		<adsm:gridColumn title="modelo" property="meioTransporte.modeloMeioTransporte.dsModeloMeioTransporte" width="90" />
		<adsm:gridColumn title="anoFabricacao" property="meioTransporte.nrAnoFabricao" width="100" dataType="integer" />
		<adsm:gridColumn title="bloqueioLiberacao" property="tpStatus" width="150" />
		<adsm:gridColumn title="situacao" property="meioTransporte.tpSituacao" isDomain="true" width="100" />
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
	getElement("idTipoMeioTransporteForCombo").masterLink = "true";

	function proprietarioDataLoad_cb(data){
		proprietario_pessoa_nrIdentificacao_exactMatch_cb(data);
		var nrIdentificacaoFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
		if (nrIdentificacaoFormatado != undefined && nrIdentificacaoFormatado != ''){
			setElementValue("proprietario.pessoa.nrIdentificacao", nrIdentificacaoFormatado);
		}
	}

	function proprietarioPopup(data) {
		var nrIdentificacaoFormatado = getNestedBeanPropertyValue(data, "pessoa.nrIdentificacaoFormatado");
		if (nrIdentificacaoFormatado != undefined && nrIdentificacaoFormatado != ''){
			setNestedBeanPropertyValue(data, "pessoa.nrIdentificacao", nrIdentificacaoFormatado);
		}
	}

	function onNrIdentificadorChange(elem) {
		elem.value = elem.value.toUpperCase();
		return validate(elem);
	}

	function validateLookupForm() {
		if (validateTabScript(document.Lazy)) {
			if (getElementValue("meioTransporte.nrFrota") != ""
				|| getElementValue("meioTransporte.nrIdentificador") != ""
				|| getElementValue("meioTransporte.tpVinculo") != ""
				|| getElementValue("proprietario.idProprietario") != ""
				|| getElementValue("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte") != ""
			) {
				return true;
			} else {
				alert(i18NLabel.getLabel("LMS-00013") +
					getElement("meioTransporte.nrFrota").label + ", " +
					getElement("meioTransporte.tpVinculo").label + ", " +
					getElement("proprietario.idProprietario").label + ", " +
					getElement("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte").label + "."
				);
			}
		}
		return false;
	}

	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			notifyElementListeners({e:getElement("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte")});
		}
	}

</script>