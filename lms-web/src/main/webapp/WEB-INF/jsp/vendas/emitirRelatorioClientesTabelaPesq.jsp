<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoad="pl" onPageLoadCallBack="onPageLoadCallBack">
	<adsm:i18nLabels>
		<adsm:include key="LMS-30050" />
	</adsm:i18nLabels>
	<adsm:form action="/vendas/emitirRelatorioClientesTabela">

		<adsm:hidden property="tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" serializable="false" />
		<adsm:hidden property="tabelaPreco.tipoTabelaPreco.nrVersao" serializable="false"/>
		<adsm:hidden property="tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco" serializable="false"/>
		<adsm:hidden property="uf.sgUnidadeFederativa" />
		<adsm:hidden property="pais.nmPaisHidden" />
		<adsm:hidden property="regional.siglaDescricao"/>
		<adsm:hidden property="tabelaPreco.tabelaPrecoStringHidden" />
		<adsm:hidden property="filial.sgFilialHidden" />
		<adsm:hidden property="tpFormatoRelatorio.valor" />
		<adsm:hidden property="tpFormatoRelatorio.descricao" />
		<adsm:hidden property="tpAcesso" value="F"/>

		<!-- Lookup de tabelas -->
		<adsm:lookup label="tabela" property="tabelaPreco"
					 service="lms.vendas.emitirRelatorioClientesTabelaAction.findLookupTabelaPreco"
					 action="/tabelaPrecos/manterTabelasPreco"
					 idProperty="idTabelaPreco"
					 criteriaProperty="tabelaPrecoString"
					 onclickPicker="onclickPickerLookupTabelaPreco()"
					 dataType="text"
					 size="10"
					 maxLength="9"
					 width="40%">

			<adsm:propertyMapping
					relatedProperty="tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco"
					modelProperty="tipoTabelaPreco.tpTipoTabelaPreco.value" />

			<adsm:propertyMapping
					relatedProperty="tabelaPreco.tipoTabelaPreco.nrVersao"
					modelProperty="tipoTabelaPreco.nrVersao" />

			<adsm:propertyMapping
					relatedProperty="tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco"
					modelProperty="subtipoTabelaPreco.tpSubtipoTabelaPreco" />

			<adsm:propertyMapping modelProperty="dsDescricao"
								  relatedProperty="tabelaPreco.dsDescricao"/>

			<adsm:propertyMapping modelProperty="tabelaPrecoString"
								  relatedProperty="tabelaPreco.tabelaPrecoStringHidden" />

			<adsm:textbox dataType="text" property="tabelaPreco.dsDescricao"
						  size="30" maxLength="30" disabled="true"/>

		</adsm:lookup>

		<!-- Combo de regionais -->
		<adsm:combobox label="regional" property="regional.idRegional"
					   optionLabelProperty="siglaDescricao" optionProperty="idRegional"
					   service="lms.vendas.emitirRelatorioClientesTabelaAction.findRegional"
					   boxWidth="240">

			<adsm:propertyMapping relatedProperty="regional.siglaDescricao"
								  modelProperty="siglaDescricao" blankFill="false" />

		</adsm:combobox>

		<adsm:lookup
				label="filial"
				property="filial"
				idProperty="idFilial"
				criteriaProperty="sgFilial"
				action="/municipios/manterFiliais"
				service="lms.vendas.emitirRelatorioClientesTabelaAction.findLookupFilial"
				dataType="text"
				size="3"
				maxLength="3"
				exactMatch="true"
				labelWidth="15%"
				width="8%"
				minLengthForAutoPopUpSearch="3"
				criteriaSerializable="true">

			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="filial.sgFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping relatedProperty="filial.sgFilialHidden" modelProperty="sgFilial"/>

			<adsm:propertyMapping criteriaProperty="regional.idRegional" modelProperty="regionalFiliais.regional.idRegional"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>

			<adsm:textbox dataType="text"
						  property="filial.pessoa.nmFantasia"
						  serializable="false"
						  width="27%"
						  size="30"
						  maxLength="50"
						  disabled="true"
			/>
		</adsm:lookup>

		<!-- Lookup de paises -->
		<adsm:lookup
				service="lms.vendas.emitirRelatorioClientesTabelaAction.findLookupPais"
				action="/municipios/manterPaises"
				property="pais"
				idProperty="idPais"
				criteriaProperty="nmPais"
				label="pais"
				minLengthForAutoPopUpSearch="3"
				exactMatch="false"
				onchange="return changePais();"
				dataType="text" labelWidth="15%" size="25" maxLength="60">

			<adsm:propertyMapping relatedProperty="pais.nmPaisHidden"
								  modelProperty="nmPais"/>

		</adsm:lookup>

		<!-- Combo de UFs -->
		<adsm:combobox
				service="lms.vendas.emitirRelatorioClientesTabelaAction.findUnidadeFederativaByPais"
				property="uf.idUnidadeFederativa"
				optionLabelProperty="siglaDescricao"
				optionProperty="idUnidadeFederativa"
				label="uf" width="30%" boxWidth="150">

			<adsm:propertyMapping criteriaProperty="pais.idPais"
								  modelProperty="pais.idPais" />

			<adsm:propertyMapping relatedProperty="uf.sgUnidadeFederativa"
								  modelProperty="sgUnidadeFederativa"/>
		</adsm:combobox>

		<adsm:combobox width="35%" label="formatoRelatorio"
					   property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO"
					   serializable="false" required="true" onDataLoadCallBack="setFormatoDefault">
			<adsm:propertyMapping relatedProperty="tpFormatoRelatorio.valor" modelProperty="value"/>
			<adsm:propertyMapping relatedProperty="tpFormatoRelatorio.descricao" modelProperty="description"/>
		</adsm:combobox>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.vendas.emitirRelatorioClientesTabelaAction"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	function lookup_filial_cb(dados, erros) {
		var r = lookupExactMatch({e:document.getElementById("filial.idFilial"), callBack:"lookup_filial_like", data:dados});
		ajustaRegional(dados[0]);
		return r;
	}

	function lookup_filial_like_cb(dados, erros) {
		var r = lookupLikeEndMatch({e:document.getElementById("filial.idFilial"), data:dados});
		ajustaRegional(dados[0]);
		return r;
	}

	function ajustaRegional(dados){
		if(dados) {
			var r = getNestedBeanPropertyValue(dados, "idRegional");
			if(r != undefined){
				setElementValue("regional.idRegional", r);
				var comboRegional = document.getElementById("regional.idRegional");
				setElementValue("regional.siglaDescricao", comboRegional.options[comboRegional.selectedIndex].text);
			}
		}
		return true;
	}

	function onPageLoadCallBack_cb(data, error) {
		onPageLoad_cb(data, error);
		findDadosDefault();
	}

	/**
	 *  OnChange da lookup de Pais.
	 */
	function changePais(){
		var r = true;
		eval("r = pais_nmPaisOnChangeHandler();");
		setElementValue("uf.idUnidadeFederativa", "");
		return r;
	}

	function pl() {

		onPageLoad();

		var pms = document.getElementById("filial.idFilial").propertyMappings;
		var pmsn = new Array();
		for (var i = 2; i < pms.length; i++)
			pmsn[i - 2] = pms[i];
		document.getElementById("filial.idFilial").propertyMappings = pmsn;
	}

	function initWindow(eventObj) {
//alert("eventObj.name="+eventObj.name);
		if (eventObj.name == "cleanButton_click"){
			findDadosDefault();
			setFormatoDefault_cb(null, null);
		}
	}


	function onclickPickerLookupTabelaPreco()
	{
		var tabelaPrecoString = getElementValue("tabelaPreco.tabelaPrecoString");
		if(tabelaPrecoString != "")
		{
			setElementValue("tabelaPreco.tabelaPrecoString","");
		}
		lookupClickPicker({e:document.forms[0].elements['tabelaPreco.idTabelaPreco']});

		if(getElementValue("tabelaPreco.tabelaPrecoString")=='' && tabelaPrecoString != "")
		{
			setElementValue("tabelaPreco.tabelaPrecoString",tabelaPrecoString);
		}
	}

	function setFormatoDefault_cb(data, error) {
		if(data) {
			tpFormatoRelatorio_cb(data);
		}
		setElementValue("tpFormatoRelatorio", "pdf");
		ajustaFormatoRelatorio();
	}

	function ajustaFormatoRelatorio() {
		var combo = document.getElementById("tpFormatoRelatorio");
		setElementValue("tpFormatoRelatorio.descricao", combo.options[combo.selectedIndex].text);
		setElementValue("tpFormatoRelatorio.valor", combo.value);
	}

	function validateTab() {
		if(getElementValue("regional.idRegional") == "" && getElementValue("filial.sgFilial") == "") {
			alertI18nMessage("LMS-30050");
			setFocusOnFirstFocusableField();
			return false;
		}
		return validateTabScript(document.forms);
	}

	function findDadosDefault() {
		var sdo = createServiceDataObject("lms.vendas.emitirRelatorioClientesTabelaAction.findDadosDefault", "findDadosDefault");
		xmit({serviceDataObjects:[sdo]});
	}

	function findDadosDefault_cb(data, error) {
		if(error) {
			alert(error);
			return;
		}
		if(data) {
			setDadosDefault(data);
		}
	}

	function setDadosDefault(defaultData) {
		setElementValue("filial.idFilial", defaultData.idFilial);
		setElementValue("filial.sgFilial", defaultData.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", defaultData.nmFilial);

		setElementValue("regional.idRegional", defaultData.idRegional);
		setElementValue("regional.siglaDescricao", defaultData.siglaDescricaoRegional);

		setElementValue("pais.idPais", defaultData.idPais);
		setElementValue("pais.nmPais", defaultData.nmPais);
		setElementValue("pais.nmPaisHidden", defaultData.nmPais);
		notifyElementListeners({e:document.getElementById("pais.idPais")});
	}

</script>
