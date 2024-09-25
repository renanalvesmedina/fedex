<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.municipios.manterMunicipiosAtendidosAction" onPageLoadCallBack="pageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-00013"/>
		<adsm:include key="municipio"/>
		<adsm:include key="filial"/>
	</adsm:i18nLabels>

	<adsm:form action="/municipios/manterMunicipiosAtendidos" height="100" idProperty="idMunicipioFilial">
		<adsm:hidden property="tpAcesso" serializable="false" value="A"></adsm:hidden> 
		<adsm:hidden property="flag" serializable="false"/>
		
		<adsm:lookup property="filial.empresa" idProperty="idEmpresa" criteriaProperty="pessoa.nrIdentificacao" onDataLoadCallBack="empresa_dataLoad"
			service="lms.municipios.manterMunicipiosAtendidosAction.findLookupEmpresa" dataType="text" label="empresa" size="18" action="/municipios/manterEmpresas" onPopupSetValue="empresa_onPopup"
			labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="3" exactMatch="true" maxLength="18" disabled="false">
			<adsm:propertyMapping relatedProperty="filial.empresa.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" inlineQuery="false" />
			<adsm:textbox property="filial.empresa.pessoa.nmPessoa" dataType="text" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
			service="lms.municipios.manterMunicipiosAtendidosAction.findLookupFilial" dataType="text" label="filial" size="3" action="/municipios/manterFiliais"
			labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping criteriaProperty="filial.empresa.idEmpresa" modelProperty="empresa.idEmpresa" />
			<adsm:propertyMapping criteriaProperty="filial.empresa.idEmpresa" modelProperty="empresa.idEmpresa" />
			<adsm:propertyMapping criteriaProperty="filial.empresa.pessoa.nrIdentificacao" modelProperty="empresa.pessoa.nrIdentificacao" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filial.empresa.pessoa.nmPessoa" modelProperty="empresa.pessoa.nmPessoa" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" />

			<adsm:propertyMapping relatedProperty="filial.empresa.pessoa.nrIdentificacao" modelProperty="empresa.pessoa.nrIdentificacao" blankFill="false" />
			<adsm:propertyMapping relatedProperty="filial.empresa.pessoa.nmPessoa" modelProperty="empresa.pessoa.nmPessoa" blankFill="false" />
			<adsm:propertyMapping relatedProperty="filial.empresa.idEmpresa" modelProperty="empresa.idEmpresa" blankFill="false"/>

			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:lookup> 

		<adsm:lookup
			label="municipio"
			property="municipio"
			idProperty="idMunicipio"
			criteriaProperty="nmMunicipio"
			service="lms.municipios.manterMunicipiosAtendidosAction.findLookupMunicipio"
			action="/municipios/manterMunicipios"
			dataType="text"
			disabled="false"
			size="30"
			maxLength="30"
			labelWidth="20%"
			width="30%"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"
			onDataLoadCallBack="implLookupMunicipioCalBack"
		>
			<adsm:propertyMapping relatedProperty="municipio.nmMunicipio" modelProperty="nmMunicipio" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" />
			<adsm:propertyMapping relatedProperty="municipio.blDistrito" modelProperty="blDistrito" />
			<adsm:propertyMapping relatedProperty="municipio.municipioDistrito.idMunicipio" modelProperty="municipioDistrito.idMunicipio" />
			<adsm:propertyMapping relatedProperty="municipio.municipioDistrito.nmMunicipio" modelProperty="municipioDistrito.nmMunicipio" />
		</adsm:lookup>

		<adsm:lookup
			label="uf"
			property="municipio.unidadeFederativa"
			idProperty="idUnidadeFederativa"
			criteriaProperty="sgUnidadeFederativa"
			service="lms.municipios.manterMunicipiosAtendidosAction.findLookupUnidadeFederativa"
			action="/municipios/manterUnidadesFederativas"
			dataType="text"
			labelWidth="20%"
			width="10%"
			size="3"
			maxLength="3"
		>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.siglaDescricao" modelProperty="siglaDescricao" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="pais.idPais" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais" blankFill="false"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais" />
		</adsm:lookup>

		<adsm:hidden property="municipio.unidadeFederativa.siglaDescricao" serializable="false"/>			
		<adsm:textbox dataType="text" property="municipio.unidadeFederativa.nmUnidadeFederativa" width="20%" size="20" serializable="false"
			disabled="true" />

		<adsm:lookup property="municipio.unidadeFederativa.pais" idProperty="idPais" service="lms.municipios.manterMunicipiosAtendidosAction.findLookupPais"
			dataType="text" criteriaProperty="nmPais" label="pais" size="30" action="/municipios/manterPaises" labelWidth="20%" width="30%"
			exactMatch="false" minLengthForAutoPopUpSearch="3" />

		<adsm:combobox property="municipio.blDistrito" label="indDistrito" labelWidth="20%" width="30%" domain="DM_SIM_NAO" disabled="false" renderOptions="true"/>

		<adsm:lookup
			label="municDistrito"
			property="municipio.municipioDistrito"
			idProperty="idMunicipio"
			criteriaProperty="nmMunicipio"
			service="lms.municipios.manterMunicipiosAtendidosAction.findLookupMunicipio"
			action="/municipios/manterMunicipios"
			dataType="text"
			disabled="false"
			size="30"
			maxLength="30"
			labelWidth="20%"
			width="30%"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"
		/>
		<adsm:combobox property="blRestricaoTransporte" label="restricaoTransporte" domain="DM_SIM_NAO" labelWidth="20%" width="30%" renderOptions="true"/>

		<adsm:combobox property="blRecebeColetaEventual" label="recebeColetaEventual" domain="DM_SIM_NAO" labelWidth="20%" width="30%" renderOptions="true"/>
		<adsm:combobox property="blDificuldadeEntrega" label="dificuldadeEntrega" domain="DM_SIM_NAO" labelWidth="20%" width="30%" renderOptions="true"/>

		<adsm:combobox property="blPadraoMcd" label="padraoMCD" domain="DM_SIM_NAO" labelWidth="20%" width="30%" renderOptions="true"/>
		<adsm:combobox property="blRestricaoAtendimento" label="restricaoAtendimento" domain="DM_SIM_NAO" labelWidth="20%" width="30%" renderOptions="true"/>

		<adsm:range label="vigencia" labelWidth="20%" width="30%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>

		<adsm:combobox property="vigentes" label="vigentes" domain="DM_SIM_NAO" labelWidth="20%" width="30%" renderOptions="true"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="__buttonBar:0.findButton"
					buttonType="findButton" onclick="consulta()" disabled="false" />
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid idProperty="idMunicipioFilial" property="municipioFilial" selectionMode="check" scrollBars="horizontal" unique="true"
			gridHeight="182" rows="9" defaultOrder="filial_.sgFilial, municipio_.nmMunicipio, dtVigenciaInicial"
			service="lms.municipios.manterMunicipiosAtendidosAction.findPaginatedCustom"
			rowCountService="lms.municipios.manterMunicipiosAtendidosAction.getRowCountCustom">
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filial" property="filial.sgFilial" width="75" />
			<adsm:gridColumn title="" property="filial.pessoa.nmFantasia" width="75" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="municipio" property="municipio.nmMunicipio" width="180" />
		<adsm:gridColumn title="uf" property="municipio.unidadeFederativa.sgUnidadeFederativa" width="40" />
		<adsm:gridColumn title="pais" property="municipio.unidadeFederativa.pais.nmPais" width="180" />
		<adsm:gridColumn title="indDistrito" property="municipio.blDistrito" width="135" renderMode="image-check"/>
		<adsm:gridColumn title="municDistrito" property="municipio.municipioDistrito.nmMunicipio" width="180" />
		<adsm:gridColumn title="distanciaChao" property="nrDistanciaChao" width="130" align="right" />
		<adsm:gridColumn title="distanciaAsfalto" property="nrDistanciaAsfalto" width="130" align="right" />
		<adsm:gridColumn title="recebeColetaEventual" property="blRecebeColetaEventual" width="165" renderMode="image-check" />
		<adsm:gridColumn title="dificuldadeEntrega" property="blDificuldadeEntrega" width="135" renderMode="image-check" />
		<adsm:gridColumn title="padraoMCD" property="blPadraoMcd" width="135" renderMode="image-check" />
		<adsm:gridColumn title="restricaoAtendimento" property="blRestricaoAtendimento" width="160" renderMode="image-check" />
		<adsm:gridColumn title="restricaoTransporte" property="blRestricaoTransporte" width="160" renderMode="image-check" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="90" dataType="JTDate" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="80" dataType="JTDate" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
<script language="javascript" type="text/javascript">
	function pageLoad_cb(data) {
		/* Utilizar apenas para os testes automatizados!!!! */
		var url = new URL(location.href);
		if ((url.parameters != undefined) && (url.parameters["isTestModeOnlyUseForTest"] != undefined ) && url.parameters["isTestModeOnlyUseForTest"]) {
			if((window.dialogArguments == null || window.dialogArguments.window == null) && window.opener != 'undefined' && window.opener != null) {
				var arguments = new Object();
				arguments.window=window.opener;
				window.dialogArguments=arguments;
			}
		}
		onPageLoad_cb(data);
		if (getElementValue("municipio.nmMunicipio")!="") {
			if(document.getElementById("municipio.nmMunicipio").masterLink == undefined)
				lookupChange({e:document.forms[0].elements["municipio.idMunicipio"],forceChange:true});
		}
	}

	function implLookupMunicipioCalBack_cb(data) {
		retorno = municipio_nmMunicipio_exactMatch_cb(data);
		if (retorno && getElementValue("flag") == "01") {
			document.getElementById("__buttonBar:0.findButton").click();
			setElementValue("flag","");
		}		
	}
</script>

<script language="javascript" type="text/javascript">
	function empresa_dataLoad_cb(data){
		if (data != undefined && data.length >= 1){
			var nrIdenfitificacaoFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
			setNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacao", nrIdenfitificacaoFormatado);
		}
		filial_empresa_pessoa_nrIdentificacao_exactMatch_cb(data);
	}

	function empresa_onPopup(data) {
		var nrIdenfitificacaoFormatado = getNestedBeanPropertyValue(data, "pessoa.nrIdentificacaoFormatado");
		setNestedBeanPropertyValue(data, "pessoa.nrIdentificacao", nrIdenfitificacaoFormatado);

		return true;
	}

	function consulta() {
		if (getElementValue("municipio.idMunicipio") == ""
			&& getElementValue("filial.idFilial") == "")
		{
			alert(getI18nMessage("LMS-00013") + ' ' + getI18nMessage("municipio") + ', ' + getI18nMessage("filial") + '.');
		} else {
			findButtonScript('municipioFilial', document.getElementById("form_idMunicipioFilial"));
		}
	}
</script>

</adsm:window>
