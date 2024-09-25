<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.municipioService">

	<adsm:i18nLabels>
		<adsm:include key="LMS-29049"/>
	</adsm:i18nLabels>

	<adsm:form action="/municipios/manterMunicipios" idProperty="idMunicipio" onDataLoadCallBack="municipio_dataLoad">

		<adsm:textbox dataType="text" property="nmMunicipio" label="municipio" maxLength="60" size="36" labelWidth="18%" width="78%" required="true" />

		<adsm:lookup
			label="uf"
			property="unidadeFederativa"
			idProperty="idUnidadeFederativa"
			criteriaProperty="sgUnidadeFederativa"
			service="lms.municipios.unidadeFederativaService.findLookup"
			action="/municipios/manterUnidadesFederativas"
			dataType="text"
			required="true"
			labelWidth="18%"
			width="32%"
			size="3"
			maxLength="3"
		>
			<adsm:hidden property="tpSituacaoUnidadeFederativa" value="A" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="tpSituacaoUnidadeFederativa" modelProperty="tpSituacao"/>
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais"/>
			<adsm:propertyMapping relatedProperty="unidadeFederativa.pais.idPais" modelProperty="pais.idPais"/>
			<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" size="25" serializable="false" disabled="true" />
		</adsm:lookup>

		<adsm:hidden property="unidadeFederativa.siglaDescricao" serializable="false"/>

		<adsm:textbox property="unidadeFederativa.pais.nmPais" disabled="true" dataType="text" label="pais" size="20" labelWidth="18%" width="32%" serializable="false"/>
		<adsm:hidden property="unidadeFederativa.pais.idPais"/>
		<adsm:textbox dataType="text" property="nrCep" label="cep" size="10" maxLength="8" labelWidth="18%" width="32%"/>

		<adsm:textbox dataType="integer" property="cdIbge" label="codIBGE" maxLength="8" size="8" labelWidth="18%" width="32%" required="false"/>
		<adsm:textbox dataType="integer" property="nrPopulacao" label="populacao" maxLength="8" size="8" labelWidth="18%" width="32%" required="true"/>
		<adsm:textbox dataType="integer" property="cdSiafi" label="codigoSiafi" maxLength="6" size="8" labelWidth="18%" width="32%" required="false"/>
		<adsm:textbox dataType="integer" property="cdEstadual" label="codEstadual" maxLength="5" size="5" labelWidth="18%" width="32%"/>
		<adsm:textbox dataType="integer" property="nrDistanciaCapital" label="distanciaCapital" unit="km" maxLength="6" size="6" labelWidth="18%" width="32%"/>

		<adsm:checkbox
			property="blDistrito"
			label="indDistrito"
			labelWidth="18%"
			width="32%"
			onclick="verifyMunicipioDistrito()"
		/>

		<adsm:hidden property="municipioDistrito.blDistrito.Flag" value="N" serializable="false"/>

		<adsm:lookup
			label="municDistrito"
			property="municipioDistrito"
			idProperty="idMunicipio"
			criteriaProperty="nmMunicipio" 
			service="lms.municipios.municipioService.findLookup"
			action="/municipios/manterMunicipios"
			onDataLoadCallBack="implLookupDistritoDataLoad"
			dataType="text"
			labelWidth="18%"
			width="32%"
			size="30" 
			minLengthForAutoPopUpSearch="3"
			exactMatch="false"
			maxLength="60"
			disabled="true"
			required="false"
		>
			<adsm:hidden property="tpSituacaoMunicipioDistrito" value="A" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="tpSituacaoMunicipioDistrito" modelProperty="tpSituacao"/>
			<adsm:propertyMapping criteriaProperty="municipioDistrito.blDistrito.Flag" modelProperty="blDistrito"/>
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" />
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" />
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" />
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" />
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais" />
		</adsm:lookup>

		<adsm:combobox property="tpSituacao" domain="DM_STATUS" required="true" label="situacao" labelWidth="18%" width="82%"/>	
		<adsm:buttonBar> 
			<adsm:button caption="rodizios" action="/municipios/manterRodizioVeiculosMunicipio" cmd="main">
				<adsm:linkProperty src="nmMunicipio" target="municipio.nmMunicipio" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.nmUnidadeFederativa" target="municipio.unidadeFederativa.nmUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.sgUnidadeFederativa" target="municipio.unidadeFederativa.sgUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.pais.nmPais" target="municipio.unidadeFederativa.pais.nmPais" disabled="true"/>
				<adsm:linkProperty src="blDistrito" target="municipio.blDistrito" disabled="true"/>
				<adsm:linkProperty src="idMunicipio" target="municipio.idMunicipio" disabled="true"/>
				<adsm:linkProperty src="municipioDistrito.nmMunicipio" target="municipio.municipioDistrito.nmMunicipio" disabled="true"/>
				<adsm:linkProperty src="nrCep" target="municipio.nrCep" disabled="true"/>
			</adsm:button>

			<adsm:button caption="feriados" action="/municipios/manterFeriados" cmd="main">
				<adsm:linkProperty src="nmMunicipio" target="municipio.nmMunicipio" disabled="true"/>
				<adsm:linkProperty src="idMunicipio" target="municipio.idMunicipio" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.sgUnidadeFederativa" target="unidadeFederativa.sgUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.idUnidadeFederativa" target="unidadeFederativa.idUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.nmUnidadeFederativa" target="unidadeFederativa.nmUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.pais.nmPais" target="pais.nmPais" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.pais.idPais" target="pais.idPais" disabled="true"/>
			</adsm:button>

			<adsm:button id="btnCep" caption="cep" action="/municipios/manterMunicipiosCEP" cmd="main" disabled="true">
				<adsm:linkProperty src="nmMunicipio" target="municipio.nmMunicipio" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.sgUnidadeFederativa" target="municipio.unidadeFederativa.sgUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.nmUnidadeFederativa" target="municipio.unidadeFederativa.nmUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.pais.nmPais" target="municipio.unidadeFederativa.pais.nmPais" disabled="true"/>
				<adsm:linkProperty src="blDistrito" target="municipio.blDistrito" disabled="true"/>
				<adsm:linkProperty src="idMunicipio" target="municipio.idMunicipio" disabled="true"/>
				<adsm:linkProperty src="municipioDistrito.nmMunicipio" target="municipio.municipioDistrito.nmMunicipio" disabled="true"/>
			</adsm:button>

			<adsm:button id="btnAtendimentos" caption="atendimentos" action="/municipios/manterMunicipiosAtendidos" cmd="main" disabled="true">
				<adsm:linkProperty src="idMunicipio" target="municipio.idMunicipio" disabled="true"/>
				<adsm:linkProperty src="nmMunicipio" target="municipio.nmMunicipio" disabled="true"/>
				<adsm:linkProperty src="nrCep" target="municipio.nrCep" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.idUnidadeFederativa" target="municipio.unidadeFederativa.idUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.sgUnidadeFederativa" target="municipio.unidadeFederativa.sgUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.nmUnidadeFederativa" target="municipio.unidadeFederativa.nmUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.pais.idPais" target="municipio.unidadeFederativa.pais.idPais" disabled="true"/>
				<adsm:linkProperty src="unidadeFederativa.pais.nmPais" target="municipio.unidadeFederativa.pais.nmPais" disabled="true"/>
				<adsm:linkProperty src="blDistrito" target="municipio.blDistrito" disabled="true"/>
				<adsm:linkProperty src="municipioDistrito.idMunicipio" target="municipio.municipioDistrito.idMunicipio" disabled="true"/>
				<adsm:linkProperty src="municipioDistrito.nmMunicipio" target="municipio.municipioDistrito.nmMunicipio" disabled="true"/>
			</adsm:button>	

			<adsm:storeButton callbackProperty="storeCallBack"/>
			<adsm:newButton />
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
<!--
 	var cepLinkProperty = false;

	function verifyMunicipioDistrito() {
		if(getElement("blDistrito").checked == true) {
			setDisabled("municipioDistrito.idMunicipio", false);
			getElement("municipioDistrito.idMunicipio").required = "true";
		} else {
			resetValue("municipioDistrito.idMunicipio");
			setDisabled("municipioDistrito.idMunicipio", true);
			getElement("municipioDistrito.idMunicipio").required = "false";
		}
	}

	function implLookupDistritoDataLoad_cb(data) {
		if (getElementValue("idMunicipio") != "" && getElementValue("idMunicipio") == getNestedBeanPropertyValue(data,":0.idMunicipio")) {
			alertI18nMessage("LMS-29049");
			return false;
		}
		return municipioDistrito_nmMunicipio_exactMatch_cb(data);
	}

	function municipio_dataLoad_cb(data, error) {
		setDisabled("nrCep", false);
		onDataLoad_cb(data, error);
		verificaCampos();
	}

	function verificaCampos() {
		verifyMunicipioDistrito();
		if(getElementValue("nrCep") == "") {
			setDisabled("btnCep", true);
			setDisabled("btnAtendimentos", true);
		} else {
			setDisabled("btnCep", false);
			setDisabled("btnAtendimentos", false);
		}
	}

	function initWindow(eventObj) {
		verifyMunicipioDistrito();
		if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton") {
			setDisabled("municipioDistrito.idMunicipio", true);
			setDisabled("nrCep", document.getElementById("unidadeFederativa.idUnidadeFederativa").masterLink == "false");
		} 
	}

	function storeCallBack_cb(data,error,key) {
		setFocusOnFirstFocusableField(document);
		if (error != undefined) {
			alert(error);
			return false;
		}
		store_cb(data,error);
		var siglaDescricao = getElementValue("unidadeFederativa.sgUnidadeFederativa") + " - " + getElementValue("unidadeFederativa.nmUnidadeFederativa");
		setElementValue("unidadeFederativa.siglaDescricao", siglaDescricao);
		verificaCampos();
	}

//-->
</Script>
