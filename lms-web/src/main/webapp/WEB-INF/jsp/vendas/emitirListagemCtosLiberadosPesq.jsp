<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.emitirListagemCtosLiberadosAction" onPageLoad="myPageLoad" onPageLoadCallBack="myPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-30050"/>
	</adsm:i18nLabels>

	<adsm:form action="/vendas/emitirListagemCtosLiberados">

		<adsm:combobox
			label="regional"
			property="regional.idRegional"
			optionLabelProperty="siglaDescricao"
			optionProperty="idRegional"
			service="lms.vendas.emitirListagemCtosLiberadosAction.findRegional"
			labelWidth="12%"
			boxWidth="280"
			width="38%">

			<adsm:propertyMapping
				relatedProperty="regional.siglaDescricao"
				modelProperty="siglaDescricao"/>
		</adsm:combobox>

		<adsm:hidden property="regional.siglaDescricao" />
		<adsm:hidden property="tpAcesso" value="F"/>

		<adsm:lookup
			label="filial"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			action="/municipios/manterFiliais"
			service="lms.vendas.emitirListagemCtosLiberadosAction.findLookupFilial"
			dataType="text"
			size="3"
			maxLength="3"
			exactMatch="true"
			labelWidth="15%" width="8%"
			minLengthForAutoPopUpSearch="3"
			criteriaSerializable="true"
		>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="filial.sgFilial" modelProperty="sgFilial"/>

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

		<adsm:combobox
			label="tipoLiberacao"
			property="tipoLiberacao.idTipoLiberacaoEmbarque"
			optionLabelProperty="dsTipoLiberacaoEmbarque"
			optionProperty="idTipoLiberacaoEmbarque"
			service="lms.vendas.emitirListagemCtosLiberadosAction.findTipoLiberacao"
			labelWidth="12%"
			boxWidth="280"
			width="38%">

			<adsm:propertyMapping
				relatedProperty="tipoLiberacao.dsTipoLiberacaoEmbarque"
				modelProperty="dsTipoLiberacaoEmbarque"/>
		</adsm:combobox>

		<adsm:hidden
			property="tipoLiberacao.dsTipoLiberacaoEmbarque"/>

		<adsm:range
			label="periodoEmissao"
			maxInterval="31"
			labelWidth="15%"
			required="true">

			<adsm:textbox
				dataType="JTDate"
				property="dataEmissaoInicial"/>

			<adsm:textbox
				dataType="JTDate"
				property="dataEmissaoFinal"/>
		</adsm:range>

		<adsm:hidden
			property="tpFormatoRelatorio.valor"/>

		<adsm:hidden
			property="tpFormatoRelatorio.descricao"/>

		<adsm:combobox
			label="bloqueioLiberado"
			property="tpBloqueioLiberado"
			domain="DM_TIPO_BLOQUEIO_LIBERADO"
			serializable="false"
			required="false"
			labelWidth="12%"
			width="38%">

			<adsm:propertyMapping
				relatedProperty="tpBloqueioLiberado.valor"
				modelProperty="value"/>

			<adsm:propertyMapping
				relatedProperty="tpBloqueioLiberado.descricao"
				modelProperty="description"/>

			<adsm:hidden
				property="tpBloqueioLiberado.valor"/>

			<adsm:hidden
				property="tpBloqueioLiberado.descricao"/>
		</adsm:combobox>

		<adsm:combobox
			width="15%"
			label="formatoRelatorio"
			property="tpFormatoRelatorio"
			domain="DM_FORMATO_RELATORIO"
			serializable="false"
			required="true"
			defaultValue="pdf">

			<adsm:propertyMapping
				relatedProperty="tpFormatoRelatorio.valor"
				modelProperty="value"/>

			<adsm:propertyMapping
				relatedProperty="tpFormatoRelatorio.descricao"
				modelProperty="description"/>

		</adsm:combobox>

		<adsm:buttonBar>
			<!-- vendas/emitirListagemCtosLiberados.jasper -->
			<adsm:reportViewerButton
				service="lms.vendas.emitirListagemCtosLiberadosAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
	var defaultData;
	function initWindow(eventObj) {
		var event = eventObj.name;
		if(event == "cleanButton_click") {
			setDefaultData();
		}
	}

	function myPageLoad() {
		onPageLoad();

		var pms = document.getElementById("filial.idFilial").propertyMappings;
		var pmsn = new Array();
		for (var i = 2; i < pms.length; i++)
			pmsn[i - 2] = pms[i];
		document.getElementById("filial.idFilial").propertyMappings = pmsn;
	}

	function myPageLoad_cb() {
		findDefaultData();
	}

	function findDefaultData() {
		var sdo = createServiceDataObject("lms.vendas.emitirListagemCtosLiberadosAction.findDadosDefault", "findDefaultData");
		xmit({serviceDataObjects:[sdo]});
	}

	function findDefaultData_cb(data, error) {
		if(error) {
			alert(error);
			return;
		}
		if(data) {
			defaultData = data;
			setDefaultData();
		}
	}

	function setDefaultData() {
		setElementValue("filial.idFilial", defaultData.idFilial)
		setElementValue("filial.sgFilial", defaultData.sgFilial)
		setElementValue("filial.pessoa.nmFantasia", defaultData.nmFilial)

		setElementValue("regional.idRegional", defaultData.idRegional)
		setElementValue("regional.siglaDescricao", defaultData.siglaDescricaoRegional)

		setElementValue("dataEmissaoInicial", setFormat("dataEmissaoInicial", defaultData.dtEmissaoInicial));
		setElementValue("dataEmissaoFinal", setFormat("dataEmissaoFinal", defaultData.dtEmissaoFinal));
	}

	function validateTab() {
		if(getElementValue("regional.idRegional") == "" && getElementValue("filial.sgFilial") == "") {
			alertI18nMessage("LMS-30050");
			setFocusOnFirstFocusableField();
			return false;
		}
		return validateTabScript(document.forms);
	}

	function lookup_filial_cb(dados, erros) {
		var r = lookupExactMatch({e:document.getElementById("filial.idFilial"), callBack:"lookup_filial_like", data:dados})
		ajustaRegional(dados[0]);
		return r;
	}
	
	function lookup_filial_like_cb(dados, erros) {
		var r = lookupLikeEndMatch({e:document.getElementById("filial.idFilial"), data:dados})
		ajustaRegional(dados[0]);
		return r;
	}
	
	function ajustaRegional(dados) {
		if(dados) {
			var r = getNestedBeanPropertyValue(dados, "idRegional");
			if(r != undefined){
				var cmb = getElement("regional.idRegional");
				setElementValue(cmb, r);
				setElementValue("regional.siglaDescricao", cmb.options[cmb.selectedIndex].text);
			}
		}
	}

</script>