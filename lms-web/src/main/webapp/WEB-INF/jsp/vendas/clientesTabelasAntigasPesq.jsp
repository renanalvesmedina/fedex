<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.clientesTabelasAntigasAction" onPageLoadCallBack="myPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-30050"/>
	</adsm:i18nLabels>

	<adsm:form action="/vendas/clientesTabelasAntigas">
		<adsm:hidden property="regional.siglaDescricao"/>
		<adsm:hidden property="tpAcesso" value="F"/>

		<adsm:combobox
			label="regional"
			property="regional.idRegional"
			optionLabelProperty="siglaDescricao"
			optionProperty="idRegional"
			service="lms.vendas.clientesTabelasAntigasAction.findRegional"
			boxWidth="240">

			<adsm:propertyMapping relatedProperty="regional.siglaDescricao" modelProperty="siglaDescricao"/>
		</adsm:combobox>

		<adsm:lookup
			label="filial"
			labelWidth="15%"
			width="8%"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			action="/municipios/manterFiliais"
			service="lms.vendas.clientesTabelasAntigasAction.findLookupFilial"
			dataType="text"
			size="3"
			maxLength="3"
			exactMatch="true"
			minLengthForAutoPopUpSearch="3"
			criteriaSerializable="true">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="filial.sgFilial" modelProperty="sgFilial"/>

			<adsm:propertyMapping criteriaProperty="regional.idRegional" modelProperty="regionalFiliais.regional.idRegional"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="sgFilial"/>
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>

			<adsm:textbox
				dataType="text" 
				property="filial.pessoa.nmFantasia" 
				width="27%"
				size="30"
				serializable="false"
				maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup 
			property="usuario"
			idProperty="idUsuario"
			criteriaProperty="nrMatricula"
			service="lms.vendas.clientesTabelasAntigasAction.findPromotor" 
			dataType="text" 
			label="promotor" 
			size="14" 
			maxLength="16" 
			action="/configuracoes/consultarFuncionarios"
			cmd="promotor"
			onDataLoadCallBack="lookup_userPromotor"
			onPopupSetValue="onPopupSetPromotor">
			
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" inlineQuery="true"/>

			<adsm:textbox dataType="text" property="usuario.nmUsuario" serializable="true" size="25" disabled="true"/>
		</adsm:lookup>

		<adsm:combobox
			property="tipo"
			required="true"
			label="tipo"
			domain="DM_TIPO_ANALITICO_SINTETICO"/>

		<adsm:combobox width="35%" label="formatoRelatorio" 
			property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" 
			serializable="false" required="true" defaultValue="pdf" >
			<adsm:propertyMapping relatedProperty="tpFormatoRelatorio.valor" modelProperty="value"/>
			<adsm:propertyMapping relatedProperty="tpFormatoRelatorio.descricao" modelProperty="description"/>
		</adsm:combobox>
		<adsm:hidden property="tpFormatoRelatorio.valor"/>
		<adsm:hidden property="tpFormatoRelatorio.descricao"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.vendas.clientesTabelasAntigasAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
	function validateTab() {
		if(getElementValue("regional.idRegional") == "" && getElementValue("filial.sgFilial") == "") {
			alertI18nMessage("LMS-30050");
			setFocusOnFirstFocusableField();
			return false;
		}
		return validateTabScript(document.forms);
	}

	function lookup_filial_cb(dados, erros) {
		ajustaRegional(dados[0]);
		return lookupExactMatch({e:document.getElementById("filial.idFilial"), callBack:"lookup_filial_like", data:dados});
	}

	function lookup_filial_like_cb(dados, erros) {
		ajustaRegional(dados[0]);
		return lookupLikeEndMatch({e:document.getElementById("filial.idFilial"), data:dados});
	}

	function lookup_userPromotor_cb(dados, erros){
		ajustaRegional(dados[0]);
		return lookupExactMatch({e:document.getElementById("usuario.idUsuario"), data:dados});
	}

	function ajustaRegional(dados){
		if(dados) {
			var r = getNestedBeanPropertyValue(dados, "idRegional");
			if(r != undefined){
				setElementValue("regional.idRegional", r);
			}
		}
		return true;
	}

	var defaultData;
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click"){
			setDefaultData();
		}
	}

	function myPageLoad_cb() {
		findDefaultData();
	}

	function findDefaultData() {
		var sdo = createServiceDataObject("lms.vendas.clientesTabelasAntigasAction.findDadosDefault", "findDefaultData");
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
		setElementValue("tipo", 'A');

		setElementValue("filial.idFilial", defaultData.idFilial)
		setElementValue("filial.sgFilial", defaultData.sgFilial)
		setElementValue("filial.pessoa.nmFantasia", defaultData.nmFilial)

		setElementValue("regional.idRegional", defaultData.idRegional)
		setElementValue("regional.siglaDescricao", defaultData.siglaDescricaoRegional)
	}

	function onPopupSetPromotor(data) {
		var nrMatricula = data.nrMatricula
		var sdo = createServiceDataObject("lms.vendas.clientesTabelasAntigasAction.findPromotor", "retornoLookupPromotor", {nrMatricula:nrMatricula});
		xmit({serviceDataObjects:[sdo]});
	}

	function retornoLookupPromotor_cb(data, error){
		if (error != undefined){
			alert(error);
		} else {
			ajustaRegional(data[0]);
		}
	}
</script>