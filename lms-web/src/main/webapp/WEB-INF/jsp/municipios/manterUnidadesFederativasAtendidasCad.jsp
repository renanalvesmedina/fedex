<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.municipioFilialUFOrigemService">
	<adsm:form action="/municipios/manterUnidadesFederativasAtendidas" idProperty="idMunicipioFilialUFOrigem" service="lms.municipios.municipioFilialUFOrigemService.findByIdDetalhamento" onDataLoadCallBack="pageLoad">

		<adsm:hidden property="municipioFilial.idMunicipioFilial"/>
		<adsm:hidden property="municipioFilial.municipio.idMunicipio"/>
		<adsm:hidden property="situacaoAtiva" value="A" />

 		<adsm:textbox dataType="text" property="municipioFilial.filial.sgFilial"
				label="filial" labelWidth="17%" width="83%" size="3" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="municipioFilial.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox dataType="text" property="municipioFilial.municipio.nmMunicipio" label="municipio" labelWidth="17%" width="83%" size="30" disabled="true" serializable="false"/>

		<adsm:textbox
			label="uf"
			property="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa"
			dataType="text"
			labelWidth="17%"
			width="33%"
			disabled="true"
			serializable="false"
			maxLength="3"
			size="3"
		>
			<adsm:textbox
				dataType="text"
				property="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa"
				size="30"
				disabled="true"
				serializable="false"
			/>
		</adsm:textbox>

		<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.pais.nmPais" label="pais" labelWidth="17%" width="33%" size="30" disabled="true" serializable="false"/>

		<adsm:checkbox property="municipioFilial.municipio.blDistrito" label="indDistrito" width="33%" labelWidth="17%" disabled="true" serializable="false"/>

		<adsm:textbox dataType="text" property="municipioFilial.municipio.municipioDistrito.nmMunicipio" label="municDistrito" labelWidth="17%" width="33%" size="30" disabled="true" serializable="false" />

		<adsm:lookup
			label="ufAtendida"
			idProperty="idUnidadeFederativa"
			property="unidadeFederativa"
			criteriaProperty="sgUnidadeFederativa"
			service="lms.municipios.unidadeFederativaService.findLookup"
			action="/municipios/manterUnidadesFederativas"
			dataType="text"
			labelWidth="17%"
			width="9%"
			maxLength="3"
			size="5"
			required="true"
		>
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa"/>
			<adsm:propertyMapping criteriaProperty="situacaoAtiva" modelProperty="tpSituacao"/>
			<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" size="28" width="24%" disabled="true"/>
		</adsm:lookup>

		<adsm:range label="vigencia" labelWidth="17%" width="33%" >
			<adsm:textbox dataType="JTDate" required="true" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:storeButton service="lms.municipios.municipioFilialUFOrigemService.storeMap" callbackProperty="storeUF"/>
			<adsm:newButton/>
			<adsm:removeButton/>
			
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
<!--
	function storeUF_cb(data, error, key){
		store_cb(data, error, key);
		if (error == undefined) {
			comportamentoVigencia(data);
			setFocusOnNewButton();
		}
	}

	function pageLoad_cb(data, exception,key) {
		if (exception != undefined) {
			alert(exception);
			return;
		}
		onDataLoad_cb(data, exception); 	

		var acaoVigenciaAtual = getNestedBeanPropertyValue(data,"acaoVigenciaAtual");
		comportamentoVigencia(data);

		if (acaoVigenciaAtual == 2)
			setFocusOnNewButton();
		else 
			setFocusOnFirstFocusableField();
	}
	
	function comportamentoVigencia(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data,"acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			enabledFields();
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
			setDisabled("__buttonBar:0.storeButton", false);
			setDisabled("dtVigenciaFinal",false);
			
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
			setFocusOnNewButton();
		} 
	}
	
	function enabledFields() {
		setDisabled("unidadeFederativa.idUnidadeFederativa",false);
		setDisabled("dtVigenciaInicial",false);
		setDisabled("dtVigenciaFinal",false);
		setFocusOnFirstFocusableField();
	}

	function initWindow(eventObj) {		
		if ((eventObj.name != "gridRow_click") && (eventObj.name != "storeButton")) { 
			enabledFields();			
		}
	}
//-->
</script>
