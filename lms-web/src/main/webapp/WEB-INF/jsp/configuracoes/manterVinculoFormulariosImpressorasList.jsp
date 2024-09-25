<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script>
	/**
	 * Chamada no load da página
	 */
	function myOnPageLoad(){
		onPageLoad();
		setDisabled("controleFormulario.controleForm", true);
	}
</script>
<adsm:window service="lms.configuracoes.manterVinculoFormulariosImpressorasAction" onPageLoadCallBack="impressoraFormularioLoad" onPageLoad="myOnPageLoad">

	<adsm:i18nLabels>
		<adsm:include key="LMS-27075"/>
		<adsm:include key="LMS-27073"/>
	</adsm:i18nLabels>
	
	<adsm:form action="/configuracoes/manterVinculoFormulariosImpressoras">

		<adsm:hidden property="sgFilialUsuario" serializable="true"/>
		
		<adsm:lookup label="controleFormulario" property="controleFormulario" service="lms.configuracoes.controleFormularioService.findLookup"
			idProperty="idControleFormulario" criteriaProperty="controleForm"
			width="27%" labelWidth="18%"
			dataType="text" action="/configuracoes/manterControleFormularios" onPopupSetValue="findImpressoraByControleForm"/>
		<adsm:hidden property="controleFormulario.filial.idFilial" serializable="false"/>

		<adsm:combobox label="impressora" property="impressora.idImpressora"
			service="lms.expedicao.impressoraService.findCombo" style="width:240px"
			width="35%" labelWidth="20%"
			optionProperty="idImpressora" optionLabelProperty="checkinLocalizacao">
		</adsm:combobox>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="impressoraFormularios" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid
		property="impressoraFormularios"
		idProperty="idImpressoraFormulario"
		gridHeight="235" 
		rows="11"
		defaultOrder="controleFormulario_filial_sgFilial,controleFormulario_nrFormularioInicial, controleFormulario_nrFormularioFinal, impressora_dsLocalizacao, controleFormulario_tpFormulario,nrFormularioInicial,nrFormularioFinal"
		scrollBars="horizontal"
	>
		<adsm:gridColumnGroup customSeparator=" - ">
			<adsm:gridColumn title="controleFormulario" property="controleFormulario.filial.sgFilial" width="50"/>
			<adsm:gridColumn title="" property="controleFormulario.nrFormularioInicial" width="50"/>
			<adsm:gridColumn title="" property="controleFormulario.nrFormularioFinal" width="50"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="impressora" property="impressora.dsLocalizacao" width="120"/>
		<adsm:gridColumn title="tipoDocumento" property="controleFormulario.tpFormulario" isDomain="true" width="100"/>
		<adsm:gridColumn title="formularioInicial" property="nrFormularioInicial" dataType="integer" width="80"/>
		<adsm:gridColumn title="formularioFinal" property="nrFormularioFinal" dataType="integer" width="80"/>
		<adsm:gridColumn title="serie" property="cdSerie" width="60"/>
		<adsm:gridColumn title="seloInicial" property="nrSeloFiscalInicial" dataType="integer" width="80"/>
		<adsm:gridColumn title="seloFinal" property="nrSeloFiscalFinal" dataType="integer" width="80"/>
		<adsm:gridColumn title="ultimoFormularioImpresso" property="nrUltimoFormulario" dataType="integer" width="80"/>
		<adsm:gridColumn title="ultimoSeloImpresso" property="nrUltimoSeloFiscal" dataType="integer" width="80"/>
		<adsm:buttonBar>
			<adsm:removeButton id="remove"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
	/**
	 * Chamada no callback da página
	 */
	function impressoraFormularioLoad_cb(data,errors){
		onPageLoad_cb();
	}
	
	/**
	 * PopUpSetValue da lookup de controle de formulário
	 */
	function findImpressoraByControleForm(data) {
		carregaImpressorasFilial(data);
		return true;
	}
	
	/**
	 * Busca as impressoras de acordo com a filial do controle de formulário para popular a comboBox
	 */
	function carregaImpressorasFilial(data) {
		var idFilial;
		if (data == undefined)
			idFilial = getElementValue("controleFormulario.filial.idFilial");
		else
			idFilial = getNestedBeanPropertyValue(data, "filial.idFilial");

		var dataCombo = new Array();
		setNestedBeanPropertyValue(dataCombo, "filial.idFilial", idFilial);
		var sdo = createServiceDataObject(document.getElementById("impressora.idImpressora").service,
			"impressora_idImpressora", dataCombo);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function initWindow(eventObj){
		setFocus("controleFormulario_lupa", false);
	}
	
</script>