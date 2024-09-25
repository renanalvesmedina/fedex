<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">

<!--
	function validateTab() {
		if (validateTabScript(document.forms)) {
			if (getElementValue("regionalDestino.idRegional") == "" && 
					getElementValue("filialDestino.idFilial") == "" &&
					getElementValue("regionalOrigem.idRegional") == "" && 
					getElementValue("filialOrigem.idFilial") == "" &&
					getElementValue("dtVigenciaInicial") == "" &&
					getElementValue("dtVigenciaFinal") == "" &&
					getElementValue("dsSimulacaoReajusteRota") == "" ) {

				alert(i18NLabel.getLabel("LMS-00013") +
						i18NLabel.getLabel("regionalOrigem") + ", " + 
						i18NLabel.getLabel("filialOrigem") + ", " + 
						i18NLabel.getLabel("regionalDestino") + ", " + 
						i18NLabel.getLabel("filialDestino") + ", " + 
						i18NLabel.getLabel("vigenciaReajuste") + ", " + 
						i18NLabel.getLabel("descricaoReajuste") + ".");
				return false;
			}else
				return true;
		}
		return false; 
	}
	
	function changeCombo(field,isRegionalOrigem) {
		if (field != undefined)
			comboboxChange({e:field});
		else
			field = document.getElementById(((isRegionalOrigem) ? "regionalOrigem.idRegional" : "regionalDestino.idRegional"));
		var dtVigenciaI = document.getElementById("dtVigenciaInicialR" + ((isRegionalOrigem) ? "O" : "D"));
		var dtVigenciaF = document.getElementById("dtVigenciaFinalR" + ((isRegionalOrigem) ? "O" : "D"));
		if (field.selectedIndex > 0) {
			var data = field.data[field.selectedIndex - 1];
			setElementValue(dtVigenciaI,setFormat(dtVigenciaI,getNestedBeanPropertyValue(data,"dtVigenciaInicial")));
			setElementValue(dtVigenciaF,setFormat(dtVigenciaF,getNestedBeanPropertyValue(data,"dtVigenciaFinal")));
		}else{
			resetValue(dtVigenciaI);
			resetValue(dtVigenciaF);
		}		
	}
	
	function filialDChange() {
		var flag = filialDestino_sgFilialOnChangeHandler();
		changeCombo(undefined,false);
		return flag;
	}
	function filialDCallBack_cb(data) {
		filialDestino_sgFilial_exactMatch_cb(data);
		changeCombo(undefined,false);
	}
	function filialDPopUp(data) {
		setElementValue("regionalDestino.idRegional",getNestedBeanPropertyValue(data,"lastRegional.idRegional"));
		changeCombo(undefined,false);
		return true;
	}
	
	function filialOChange() {
		var flag = filialOrigem_sgFilialOnChangeHandler();
		changeCombo(undefined,true);
		return flag;
	}
	function filialOCallBack_cb(data) {
		filialOrigem_sgFilial_exactMatch_cb(data);
		changeCombo(undefined,true);
	}
	function filialOPopUp(data) {
		setElementValue("regionalOrigem.idRegional",getNestedBeanPropertyValue(data,"lastRegional.idRegional"));
		changeCombo(undefined,true);
		return true;
	}
	
	function pageLoad_cb(data) {
		onPageLoad_cb(data)
		var sdo = createServiceDataObject("lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findPaisSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}
	
	var idRegional = null;
	
	function dataSession_cb(data) {
		idRegional = getNestedBeanPropertyValue(data,"regional.id");
		writeDataSession();
	}
	function writeDataSession() {
		if (idRegional != null) {
			setElementValue("regionalOrigem.idRegional",idRegional);
			setDisabled("regionalOrigem.idRegional",true);
			document.getElementById("regionalOrigem.idRegional").masterLink = "true";
			changeCombo(undefined,true);
			document.getElementById("dtVigenciaInicialRO").masterLink = "true";
			document.getElementById("dtVigenciaFinalRO").masterLink = "true";
		}
	}
	
	function regionalOrigem_cb(data) {
		regionalOrigem_idRegional_cb(data);
		if (idRegional != null) {
			setElementValue("regionalOrigem.idRegional",idRegional);
			setDisabled("regionalOrigem.idRegional",true);
			document.getElementById("regionalOrigem.idRegional").masterLink = "true";
			changeCombo(undefined,true);
			document.getElementById("dtVigenciaInicialRO").masterLink = "true";
			document.getElementById("dtVigenciaFinalRO").masterLink = "true";
		}
	}
//-->
</script>
<adsm:window title="simularEfetivarReajustesTabelasFrete" service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/freteCarreteiroViagem/simularEfetivarReajustesTabelasFrete" height="120">
		<adsm:textbox dataType="text" property="dsSimulacaoReajusteRota" label="descricaoReajuste" maxLength="60" size="30" labelWidth="17%" width="35%" />
		
		<adsm:combobox property="regionalOrigem.idRegional" label="regionalOrigem" onchange="changeCombo(this,true)"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findComboRegional" onDataLoadCallBack="regionalOrigem"
				optionLabelProperty="siglaDescricao" optionProperty="idRegional" labelWidth="17%" width="33%" boxWidth="200" />

		<adsm:lookup property="filialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" exactMatch="false" labelWidth="17%"
				dataType="text" size="3" maxLength="3"  minLengthForAutoPopUpSearch="3" action="/municipios/manterFiliais" label="filialOrigem"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findLookupFilial" width="33%"
				onDataLoadCallBack="filialOCallBack" onPopupSetValue="filialOPopUp" onchange="return filialOChange();">
			<adsm:propertyMapping criteriaProperty="regionalOrigem.idRegional" modelProperty="regionalFiliais.regional.idRegional"/>
			<adsm:propertyMapping relatedProperty="regionalOrigem.idRegional" modelProperty="lastRegional.idRegional" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" disabled="true" serializable="false" size="30"/>
		</adsm:lookup>  
		
		<adsm:range label="vigencia" labelWidth="17%" width="82%">
			<adsm:textbox size="12" property="dtVigenciaInicialRO" dataType="JTDate" picker="false" disabled="true" serializable="false"/>
			<adsm:textbox size="12" property="dtVigenciaFinalRO" dataType="JTDate" picker="false" disabled="true" serializable="false"/>
		</adsm:range>
		
		<adsm:combobox property="regionalDestino.idRegional" label="regionalDestino" onchange="changeCombo(this,false)"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findComboRegional"
				optionLabelProperty="siglaDescricao" optionProperty="idRegional" labelWidth="17%" width="33%" boxWidth="200" />

		<adsm:lookup property="filialDestino" idProperty="idFilial" criteriaProperty="sgFilial" exactMatch="false" labelWidth="17%"
				dataType="text" size="3" maxLength="3"  minLengthForAutoPopUpSearch="3" action="/municipios/manterFiliais" label="filialDestino"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findLookupFilial" width="33%"
				onDataLoadCallBack="filialDCallBack" onPopupSetValue="filialDPopUp" onchange="return filialDChange();">
			<adsm:propertyMapping criteriaProperty="regionalDestino.idRegional" modelProperty="regionalFiliais.regional.idRegional"/>
			<adsm:propertyMapping relatedProperty="regionalDestino.idRegional" modelProperty="lastRegional.idRegional" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" disabled="true" serializable="false" size="30"/>
		</adsm:lookup>
		
		<adsm:range label="vigencia" labelWidth="17%" width="82%">
			<adsm:textbox size="12" property="dtVigenciaInicialRD" dataType="JTDate" picker="false" disabled="true" serializable="false"/>
			<adsm:textbox size="12" property="dtVigenciaFinalRD" dataType="JTDate" picker="false" disabled="true" serializable="false"/>
		</adsm:range>
			
		<adsm:combobox property="tpRota" label="tipoRota" domain="DM_TIPO_ROTA_VIAGEM" labelWidth="17%" width="33%"/>
		
		<adsm:combobox property="tpSituacaoRota" label="situacaoReajuste" domain="DM_STATUS_REAJUSTE_ROTA" labelWidth="17%" width="33%"/>



		<adsm:lookup dataType="text" property="proprietario" idProperty="idProprietario" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction.findLookupProprietario" width="83%" size="20" maxLength="20"
				action="/contratacaoVeiculos/manterProprietarios" criteriaProperty="pessoa.nrIdentificacao" label="proprietario" labelWidth="17%">
			<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" serializable="false" size="35" disabled="true"/>
		</adsm:lookup>
		
		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" boxWidth="180" cellStyle="vertical-align:bottom;" labelWidth="17%"
				optionProperty="idTipoMeioTransporte" optionLabelProperty="dsTipoMeioTransporte" label="tipoMeioTransporte" width="33%"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findComboTipoMeioTransporte">
			<adsm:propertyMapping relatedProperty="tpMeioTransporte" modelProperty="tpMeioTransporte.value"/>
		</adsm:combobox>
		<adsm:hidden property="tpMeioTransporte" serializable="false"/>
		
		<adsm:lookup dataType="text" property="meioTransporte2" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom;" criteriaProperty="nrFrota"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findLookupMeioTransporte" picker="false" maxLength="6" size="7"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" label="meioTransporte" labelWidth="17%" width="33%" serializable="false">
			<adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador"/>
			<adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte"/>		
			<adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador"/>
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
			<adsm:propertyMapping relatedProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" blankFill="false"/>
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario"/>
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario2.pessoa.nmPessoa" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="tpMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte.value" inlineQuery="false"/>
		
			<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporte" criteriaProperty="nrIdentificador"
					service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findLookupMeioTransporte" picker="true"
					maxLength="25" action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list"
					size="20" cellStyle="vertical-align:bottom;">
				<adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota" modelProperty="nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte" modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporte2.nrFrota" modelProperty="nrFrota" />	
				<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
				<adsm:propertyMapping relatedProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" blankFill="false" />
				<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario"/>
				<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario2.pessoa.nmPessoa" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" inlineQuery="false"/>
				<adsm:propertyMapping relatedProperty="tpMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte.value" inlineQuery="false"/>
			</adsm:lookup>
		
		</adsm:lookup>
		

		<adsm:lookup service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findLookupRotasViagem" dataType="integer" property="rotaIdaVolta" idProperty="idRotaIdaVolta" 
					criteriaProperty="nrRota" size="4" label="rotaViagem" action="/municipios/consultarRotas" cmd="idaVolta" disabled="false"
					maxLength="4" exactMatch="false" labelWidth="17%" cellStyle="vertical-align:bottom;" mask="0000" width="33%">
                  <adsm:propertyMapping relatedProperty="rotaViagem.dsRota" modelProperty="rota.dsRota"/>
                  <adsm:propertyMapping relatedProperty="rotaViagem.rota.idRota" modelProperty="rota.idRota"/>
                  <adsm:hidden property="rotaViagem.rota.idRota" serializable="true"/>
                  <adsm:textbox dataType="text" property="rotaViagem.dsRota" size="30" cellStyle="vertical-align:bottom;" disabled="true" serializable="false"/>
        </adsm:lookup>
        
        <adsm:i18nLabels>
        	<adsm:include key="LMS-00013"/>
        	<adsm:include key="regionalDestino"/>
        	<adsm:include key="filialDestino"/>
       	    <adsm:include key="regionalOrigem"/>
        	<adsm:include key="filialOrigem"/>
        	<adsm:include key="vigenciaReajuste"/>
        	<adsm:include key="descricaoReajuste"/>
        </adsm:i18nLabels>
		
		
		<adsm:range label="vigenciaReajuste" labelWidth="17%" width="83%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>		
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>		
		</adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="SimulacaoReajusteRota"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="SimulacaoReajusteRota" idProperty="idSimulacaoReajusteRota" rows="8" 
			   scrollBars="horizontal" gridHeight="162">
		<adsm:gridColumn width="100" title="tipoRota" property="tpRota" isDomain="true"/>
		<adsm:gridColumn width="150" title="descricaoReajuste" property="dsSimulacaoReajusteRota"/>
		<adsm:gridColumn width="150" title="situacaoReajuste" property="tpSituacaoRota" isDomain="true"/>
		<adsm:gridColumn width="150" title="tipoMeioTransporte" property="tipoMeioTransporte_dsTipoMeioTransporte"/>

		<adsm:gridColumn width="50" title="meioTransporte" property="meioTransporte_nrFrota" />
		<adsm:gridColumn width="100" title="" property="meioTransporte_nrIdentificador" align="left"/>
		
		<adsm:gridColumn width="50" title="identificacao" property="proprietario_tpIdentificacao" isDomain="true"/>
		<adsm:gridColumn width="100" title="" property="proprietario_nrIdentificacao" align="right"/>
		<adsm:gridColumn width="150" title="proprietario" property="proprietario_nmPessoa"/>

		<adsm:gridColumn width="110" title="tipoReajuste" property="tpReajuste" isDomain="true" />
		<adsm:gridColumn width="110" title="valorDoReajuste" property="vlReajuste" align="right" dataType="currency"/>
		
		<adsm:gridColumn width="100" title="vigenciaInicial" property="dtVigenciaInicial" align="center" dataType="JTDate"/>
		<adsm:gridColumn width="100" title="vigenciaFinal" property="dtVigenciaFinal" align="center" dataType="JTDate"/>	
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
