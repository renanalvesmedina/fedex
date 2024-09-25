<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterFiliaisAtendidasAction">
	<adsm:form action="/municipios/manterFiliaisAtendidas" idProperty="idMunicipioFilialFilOrigem" onDataLoadCallBack="pageLoad">
		<adsm:hidden property="municipioFilial.idMunicipioFilial"/>
		<adsm:hidden property="municipioFilial.municipio.idMunicipio"/>

		<adsm:hidden property="municipioFilial.filial.empresa.idEmpresa" />
		<adsm:hidden property="municipioFilial.filial.empresa.pessoa.nmPessoa" />
		<adsm:hidden property="municipioFilial.filial.empresa.pessoa.nrIdentificacao" />
				
		<adsm:textbox dataType="text" property="municipioFilial.filial.sgFilial"
				label="filial" labelWidth="17%" width="83%" size="3" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="municipioFilial.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:textbox>
		<adsm:textbox dataType="text" property="municipioFilial.municipio.nmMunicipio" label="municipio" labelWidth="17%" width="83%" size="30" disabled="true" serializable="false"/>
		<adsm:textbox property="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" labelWidth="17%" width="33%" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.pais.nmPais" label="pais" labelWidth="17%" width="33%" size="30" disabled="true" serializable="false"/>
		<adsm:checkbox property="municipioFilial.municipio.blDistrito" label="indDistrito" width="33%" labelWidth="17%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="municipioFilial.municipio.municipioDistrito.nmMunicipio" label="municDistrito" labelWidth="17%" width="33%" size="30" disabled="true" serializable="false"/>
		
		<adsm:lookup
			label="filialAtendida"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.municipios.manterFiliaisAtendidasAction.findLookupFilial"
			action="/municipios/manterFiliais"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="17%"
			width="33%"
			exactMatch="true"
			minLengthForAutoPopUpSearch="3"
			required="true"
		>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping criteriaProperty="municipioFilial.filial.empresa.idEmpresa" modelProperty="empresa.idEmpresa" />
			<adsm:propertyMapping criteriaProperty="municipioFilial.filial.empresa.pessoa.nmPessoa" modelProperty="empresa.pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="municipioFilial.filial.empresa.pessoa.nrIdentificacao" modelProperty="empresa.pessoa.nrIdentificacao" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="25" disabled="true" />
		</adsm:lookup>

		<adsm:range label="vigencia" labelWidth="17%" width="33%">
			<adsm:textbox dataType="JTDate" required="true" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="storeFilial"/>
			<adsm:newButton/>		
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
<!--
	function storeFilial_cb(data, error, key) {
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

		if(acaoVigenciaAtual == 2)
			setFocusOnNewButton();
		else
			setFocusOnFirstFocusableField();
	}

	function comportamentoVigencia(data){
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
		} 
	}
	
	function enabledFields() {
		setDisabled("filial.idFilial",false);
		setDisabled("dtVigenciaInicial",false);
		setDisabled("dtVigenciaFinal",false);
		setFocusOnFirstFocusableField();
	}

	function initWindow(eventObj) {		
		if ((eventObj.name != "gridRow_click") && (eventObj.name != "storeButton"))
			enabledFields();			
	}
//-->
</Script>
