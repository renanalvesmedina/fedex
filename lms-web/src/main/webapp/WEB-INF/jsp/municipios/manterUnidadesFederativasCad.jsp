<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.unidadeFederativaService">
	<adsm:i18nLabels>
		<adsm:include key="LMS-29013"/>
	</adsm:i18nLabels>

	<adsm:form action="/municipios/manterUnidadesFederativas" idProperty="idUnidadeFederativa">

		<adsm:lookup
			label="pais"
			property="pais"
			idProperty="idPais"
			criteriaProperty="nmPais"
			service="lms.municipios.paisService.findLookup"
			action="/municipios/manterPaises"
			dataType="text"
			size="30"
			labelWidth="16%"
			width="32%"
			required="true"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"
		>
			<adsm:propertyMapping relatedProperty="pais.tpSituacao.description" modelProperty="tpSituacao.description"/>
			<adsm:propertyMapping relatedProperty="pais.sgPais" modelProperty="sgPais"/>
			<adsm:hidden property="tpSituacaoPais" value="A" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="tpSituacaoPais" modelProperty="tpSituacao"/>
		</adsm:lookup>	
		<adsm:hidden property="pais.sgPais" serializable="true"/>
		
		<adsm:textbox dataType="text" property="pais.tpSituacao.description" label="situacao" labelWidth="16%" width="34%" disabled="true" serializable="false"/>
		
		<adsm:combobox property="regiaoGeografica.idRegiaoGeografica" label="regiaoGeografica" service="lms.municipios.regiaoGeograficaService.find" optionLabelProperty="dsRegiaoGeografica" optionProperty="idRegiaoGeografica" labelWidth="16%" width="32%" boxWidth="170" required="true" onlyActiveValues="true"/>
		
		<adsm:textbox label="codIBGE" property="nrIbge" dataType="integer"  size="15" maxLength="10" labelWidth="16%" width="34%" serializable="true"/>	
		
		<adsm:textbox dataType="text" property="sgUnidadeFederativa" onchange="validaSigla()" label="siglaUf" size="3" maxLength="3" labelWidth="16%" width="32%" required="true"/>
		<adsm:textbox dataType="text" property="nmUnidadeFederativa" label="nomeUf" size="30" maxLength="60" labelWidth="16%" width="36%" required="true"/>

		<adsm:lookup
			label="capital"
			property="municipio"
			idProperty="idMunicipio"
			criteriaProperty="nmMunicipio"
			service="lms.municipios.municipioService.findLookup"
			action="/municipios/manterMunicipios"
			dataType="text"
			disabled="true"
			size="30"
			maxLength="30"
			labelWidth="16%"
			width="84%"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"
		>
			<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="unidadeFederativa.pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais"/>
			<adsm:propertyMapping criteriaProperty="idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa"/>
			<adsm:propertyMapping criteriaProperty="sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa"/>
			<adsm:propertyMapping criteriaProperty="nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa"/>
			<adsm:hidden property="tpSituacaoCapital" value="A" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="tpSituacaoCapital" modelProperty="tpSituacao"/>
		</adsm:lookup>

		<adsm:checkbox property="blIcmsPedagio" label="icmsPedagio" labelWidth="16%" width="32%" />
		<adsm:checkbox property="blFronteiraRapida" label="fronteiraRapida" labelWidth="16%" width="36%" />
		<adsm:checkbox property="blIncideIss" label="incideIss" labelWidth="16%" width="32%"/>
		<adsm:checkbox property="blCobraTas" label="cobraTAS" labelWidth="16%" width="32%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" required="true" label="situacao" labelWidth="16%" width="32%"/>
		<adsm:checkbox property="blCobraSuframa" label="cobraSuframa" labelWidth="16%" width="16%"/>
		<br />
		<adsm:textbox dataType="integer" property="nrPrazoCancCte" label="nrPrazoCancCte" size="10" maxLength="3" labelWidth="16%" width="32%" unit="horas" />
		<adsm:combobox property="unidadeFederativaSefazVirtual.idUnidadeFederativa" service="lms.municipios.unidadeFederativaService.findComboAtivo" label="unidadeFederativaSVC" optionLabelProperty="siglaDescricao" optionProperty="idUnidadeFederativa" labelWidth="16%" width="34%" onlyActiveValues="true">
			<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="pais.idPais"/>
		</adsm:combobox>
		<adsm:buttonBar>
			<adsm:button caption="municipios" action="/municipios/manterMunicipios" cmd="main">
				<adsm:linkProperty src="sgUnidadeFederativa" target="unidadeFederativa.sgUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="idUnidadeFederativa" target="unidadeFederativa.idUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="nmUnidadeFederativa" target="unidadeFederativa.nmUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="pais.idPais" target="unidadeFederativa.pais.idPais" disabled="true"/>
				<adsm:linkProperty src="pais.nmPais" target="unidadeFederativa.pais.nmPais" disabled="true"/>
			</adsm:button>

			<adsm:button caption="feriados" action="/municipios/manterFeriados" cmd="main" >
				<adsm:linkProperty src="sgUnidadeFederativa" target="unidadeFederativa.sgUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="idUnidadeFederativa" target="unidadeFederativa.idUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="nmUnidadeFederativa" target="unidadeFederativa.nmUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="pais.idPais" target="pais.idPais" disabled="true"/>
				<adsm:linkProperty src="pais.nmPais" target="pais.nmPais" disabled="true"/>
			</adsm:button>

			<adsm:button caption="intervalosCep" action="/municipios/manterIntervaloUF" cmd="main">
				<adsm:linkProperty src="nmUnidadeFederativa" target="unidadeFederativa.nmUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="sgUnidadeFederativa" target="unidadeFederativa.sgUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="idUnidadeFederativa" target="unidadeFederativa.idUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="pais.idPais" target="unidadeFederativa.pais.idPais" disabled="true"/>
				<adsm:linkProperty src="pais.sgPais" target="unidadeFederativa.pais.sgPais" disabled="true"/> 
				<adsm:linkProperty src="pais.nmPais" target="unidadeFederativa.pais.nmPais" disabled="true"/> 
			</adsm:button>

			<adsm:storeButton callbackProperty="afterStore"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	function validaSigla() {
		var sigla = document.getElementById("sgUnidadeFederativa")
		sigla.value = sigla.value.toUpperCase();
	}

	// #####################################################################
	// funções para chamar o servico que compara os ceps inicial e final
	// de maneira a não possibilitar que o cep inicial seja maior que o final
	// #####################################################################
	function validaIntervaloCeps(focusField) {
		var cepInicial = document.getElementById('nrCepInicial');
		var cepFinal = document.getElementById('nrCepFinal');		
		if ((cepInicial.value != '') && (cepFinal.value != '')) {
			var sdo = createServiceDataObject("lms.municipios.unidadeFederativaService.validaIntervaloCeps", "validaIntervaloCeps",
					{nrCepInicial:cepInicial.value, nrCepFinal:cepFinal.value, focusField:focusField});
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function validaIntervaloCeps_cb(data,exception) {
		if (exception != undefined) {
			alert(exception);
			return;
		}
		if (data != undefined && getNestedBeanPropertyValue(data,":resultado") != undefined) {	
			if (getNestedBeanPropertyValue(data,":resultado") == 'FALSE') {
				if (getNestedBeanPropertyValue(data,":focusField") == 'CAMPO_CEP_INICIAL') {
					document.getElementById('nrCepInicial').value = "";
					setFocus(document.getElementById('nrCepInicial'));
				} else {
					document.getElementById('nrCepFinal').value = "";
					setFocus(document.getElementById('nrCepFinal'));
				}
				alertI18nMessage("LMS-29013")
			}
		}
	}

	// ############################################################
	// tratamento dos eventos da initWindow para <tab_click>, 
	// <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
	// ############################################################
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click" || eventObj.name == "newButton_click") {
			setDisabled("municipio.idMunicipio", true);
		} else if (eventObj.name == "gridRow_click") {
			setDisabled("municipio.idMunicipio", false);
		}
	}

	function afterStore_cb(data, error) {
		store_cb(data, error);
		setDisabled("municipio.idMunicipio", false);
		if(document.getElementById('nrPrazoCancCte').value == "0"){
			document.getElementById('nrPrazoCancCte').value = "";
		}
	}
</script>