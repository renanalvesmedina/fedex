<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.municipios.municipioService">
	<adsm:i18nLabels>
		<adsm:include key="LMS-00013"/>
		<adsm:include key="municipio"/>
		<adsm:include key="uf"/>
	</adsm:i18nLabels>

	<adsm:form action="/municipios/manterMunicipios" idProperty="idMunicipio">
		<adsm:hidden property="municipioFiliais.filial.idFilial"/>
		<adsm:hidden property="municipioFiliais.dtVigenciaInicial"/>
		<adsm:hidden property="municipioFiliais.dtVigenciaFinal"/>
		<adsm:hidden property="blAtendimentoTemporario"/>		

		<adsm:textbox dataType="text" property="nmMunicipio" label="municipio" maxLength="50" size="36" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="text" property="nrCep" label="cep" size="8" maxLength="8" labelWidth="18%" width="32%"/>

		<adsm:lookup
			label="uf"
			property="unidadeFederativa"
			idProperty="idUnidadeFederativa"
			criteriaProperty="sgUnidadeFederativa"
			service="lms.municipios.unidadeFederativaService.findLookup"
			action="/municipios/manterUnidadesFederativas"
			dataType="text"
			labelWidth="18%"
			width="7%"
			size="3"
			maxLength="3"
		>
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.pais.idPais" modelProperty="pais.idPais" />
		</adsm:lookup>
		<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" width="25%" size="25" serializable="false" disabled="true"/>

		<adsm:lookup
			label="pais"
			property="unidadeFederativa.pais"
			idProperty="idPais"
			criteriaProperty="nmPais"
			service="lms.municipios.paisService.findLookup"
			action="/municipios/manterPaises"
			dataType="text"
			size="30"
			labelWidth="18%"
			width="32%"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3">
		</adsm:lookup>

		<adsm:combobox
			property="blDistrito"
			label="indDistrito"
			domain="DM_SIM_NAO"
			labelWidth="18%"
			width="32%"
			onchange="return onChangeBlDistrito();"
		/>

		<adsm:hidden property="municipioDistrito.blDistrito.Flag" value="N" serializable="false"/>
		<adsm:lookup
			label="municDistrito"
			property="municipioDistrito"
			idProperty="idMunicipio"
			criteriaProperty="nmMunicipio"
			service="lms.municipios.municipioService.findLookup"
			action="/municipios/manterMunicipios"
			labelWidth="18%"
			width="32%"
			maxLength="60"
			dataType="text"
			size="30"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"
			disabled="true"
		>
			<adsm:propertyMapping criteriaProperty="municipioDistrito.blDistrito.Flag" modelProperty="blDistrito" />
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" />
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" />
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" />
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais" />
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" />
		</adsm:lookup>

		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="18%" width="82%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="__buttonBar:0.findButton"
				buttonType="findButton" onclick="consulta()" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idMunicipio" property="municipio" selectionMode="check" gridHeight="200" unique="true">
		<adsm:gridColumn title="municipio" property="nmMunicipio" width="25%" />
		<adsm:gridColumn title="cep" property="nrCep" width="15%" />
		<adsm:gridColumn title="uf" property="unidadeFederativa.sgUnidadeFederativa" width="10%" />
		<adsm:gridColumn title="indDistrito" property="blDistrito" width="15%" renderMode="image-check"/>
		<adsm:gridColumn title="municDistrito" property="municipioDistrito.nmMunicipio" width="25%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar> 
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	function initWindow(eventObj) {
		onChangeBlDistrito();
	}

	function deletaUFOnChange() {
		var flag = unidadeFederativa_pais_nmPaisOnChangeHandler();
		if(document.getElementById("unidadeFederativa.pais.nmPais").value == "" ) {
			setElementValue(document.getElementById("unidadeFederativa.idUnidadeFederativa"), '');
			setElementValue(document.getElementById("unidadeFederativa.nmUnidadeFederativa"), '');
			setElementValue(document.getElementById("unidadeFederativa.sgUnidadeFederativa"), '');
		}
		return flag;
	}

	function consulta() {
		if(getElementValue("nmMunicipio") != ""
			|| getElementValue("unidadeFederativa.idUnidadeFederativa") != ""
		) {
			findButtonScript('municipio', document.getElementById("form_idMunicipio"));
		} else {
			alert(getI18nMessage("LMS-00013") + ' ' + getI18nMessage("municipio") + ', ' + getI18nMessage("uf") + '.');
		}
	}

	function onChangeBlDistrito() {
		if(getElementValue("blDistrito") == "S") {
			setDisabled("municipioDistrito.idMunicipio", false);
		} else {
			resetValue("municipioDistrito.idMunicipio");
			setDisabled("municipioDistrito.idMunicipio", true);
		}
		return true;
	}
</script>