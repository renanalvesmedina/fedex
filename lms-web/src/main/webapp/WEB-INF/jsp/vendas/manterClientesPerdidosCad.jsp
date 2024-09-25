<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.vendas.manterClientePerdidoAction" onPageLoad="loadDataObjectsCad">

<adsm:form action="/vendas/manterClientesPerdidos" idProperty="idClientePerdido" service="lms.vendas.manterClientePerdidoAction.findById" onDataLoadCallBack="clientePerdidoCallBack">

	<adsm:hidden property="idGerente" serializable="false"/>
	<adsm:hidden property="filial.idFilial"/>
	<adsm:hidden property="dtAtual" serializable="false"/>
		
	<adsm:textbox
		label="filial"
		dataType="text"
		property="filial.sgFilial"
		size="3"
		maxLength="3"
		required="true"
		disabled="true"
		serializable="false"
		labelWidth="13%"
		width="44%" >
		<adsm:textbox
			dataType="text"
			property="filial.pessoa.nmFantasia"
			size="25"
			maxLength="60"
			disabled="true"
			serializable="false"
		/>
	</adsm:textbox>

	
	<adsm:textbox
		dataType="text" 
		label="regional"
		property="siglaDescricao" 
		disabled="true"
		size="30"
		maxLength="60"
		width="28%"
		labelWidth="15%"
		serializable="false"
		required="true"
	/>
	
	<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			service="lms.sim.manterPedidosComprasAction.findLookupCliente" 
			required="true"
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="cliente" 
			size="17" 
			maxLength="18"
			dataType="text"
			width="44%"
			labelWidth="13%">
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping
				relatedProperty="cliente.tpCliente" 
				modelProperty="tpCliente"/>
			<adsm:textbox
				dataType="text" 
				property="cliente.pessoa.nmPessoa" 
				size="35" 
				maxLength="50"
				disabled="true" 
				serializable="false" required="true"/>
		</adsm:lookup>
		
		<adsm:combobox
			property="cliente.tpCliente" 
			label="tipoCliente"
			domain="DM_TIPO_CLIENTE"
			onlyActiveValues="true"
			disabled="true"
			labelWidth="15%"
			width="28%"
			serializable="false"/>
			
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" serializable="true" labelWidth="13%" width="44%"/>
		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" serializable="true" labelWidth="15%" width="28%"/>
		
		<adsm:textbox  dataType="JTDate" property="dtPerda" width="44%" labelWidth="13%" label="dataPerda" required="true"/>
		<adsm:textbox  dataType="JTDate" property="dtFinalOperacao" width="28%" labelWidth="15%" label="dataTerminoOperacao" required="true"/>
		
		<adsm:combobox property="tpPerda" label="tipoPerda" domain="DM_TIPO_PERDA" serializable="true" labelWidth="13%" width="87%" required="true"/>
		
		<adsm:combobox property="ramoAtividade.idRamoAtividade" optionLabelProperty="dsRamoAtividade" optionProperty="idRamoAtividade"
				label="ramoAtividade" service="lms.vendas.manterClientePerdidoAction.findComboRamoAtividade" labelWidth="13%" width="44%" boxWidth="220" required="false" />
	
		<adsm:combobox property="segmentoMercado.idSegmentoMercado" optionLabelProperty="dsSegmentoMercado" optionProperty="idSegmentoMercado"
				label="segmentoMercado" service="lms.vendas.segmentoMercadoService.find" labelWidth="15%" width="28%" boxWidth="172" required="true" cellStyle="vertical-align:bottom"/>
	
	<adsm:complement label="receitaPerdida" labelWidth="13%" width="44%" required="true" separator="branco" >
		<adsm:combobox property="moeda.idMoeda" 
			optionLabelProperty="siglaSimbolo"
			optionProperty="idMoeda" 
			service="lms.vendas.manterClientePerdidoAction.findComboMoeda"
			cellStyle="vertical-align:bottom" serializable="true" onchange="return myOnChangeComboMoeda(this)">
			<adsm:textbox dataType="currency" mask="###,###,###,##0.00" property="nrReceitaPerdida" size="18" disabled="false" cellStyle="vertical-align:bottom" serializable="true"/>
		</adsm:combobox>
	</adsm:complement>
		
		<adsm:complement label="receitaMediaMensal" labelWidth="15%" width="28%" required="true" separator="branco" >
			<adsm:combobox property="moeda1.idMoeda" 
				optionLabelProperty="siglaSimbolo"
				optionProperty="idMoeda" 
				service="lms.vendas.manterClientePerdidoAction.findComboMoeda"
				cellStyle="vertical-align:bottom" serializable="false" onchange="return myOnChangeComboMoeda(this)">
				<adsm:textbox dataType="currency" mask="###,###,###,##0.00" property="nrReceitaMedia" size="18" disabled="false" cellStyle="vertical-align:bottom" serializable="true" />
			</adsm:combobox>
	</adsm:complement>	
	<adsm:textbox
		dataType="integer" 
		label="pesoMedioMensal"
		property="nrPesoMedio" 
		maxLength="10"
		width="44%"
		labelWidth="13%"
		serializable="true"
		required="true"
		unit="kg"
	/>
	
	<adsm:textbox
		cellStyle="vertical-align:bottom"
		dataType="integer" 
		label="mediaMensalEnvios"
		property="nrMediaEnvio" 
		maxLength="10"
		width="28%"
		labelWidth="15%"
		serializable="true"
		required="true"
	/>	
	
	<adsm:textbox
		cellStyle="vertical-align:bottom"
		dataType="integer" 
		label="mediaMensalCTRCs"
		property="nrMediaCTRC" 
		maxLength="10"
		width="44%"
		labelWidth="13%"
		serializable="true"
		required="true"
	/>	
	
	<adsm:combobox cellStyle="vertical-align:bottom" property="tpMotivoPerda" label="motivoPerda" required="true" domain="DM_MOTIVO_PERDA" serializable="true" boxWidth="196" labelWidth="15%" width="28%"/>
	
	<adsm:buttonBar>
		<adsm:storeButton id="storeButton" />
		<adsm:newButton id="newButton"/>
		<adsm:removeButton id="removeButton"/>
	</adsm:buttonBar>	
</adsm:form>
</adsm:window>
<script>
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	var idRegional = null;
	var siglaRegional = null;
	
	function myOnChangeComboMoeda(elem){
		var retorno = comboboxChange({e:elem});
		setaOutraMoeda(elem);
		return retorno;
	}
	
	function setaOutraMoeda(elem){
		if( elem.selectedIndex != 0 ){
			if(elem.id == 'moeda.idMoeda')
				setElementValue('moeda1.idMoeda',elem.options[elem.selectedIndex]);
			else	
				setElementValue('moeda.idMoeda',elem.options[elem.selectedIndex]);
		}
	}

	function clientePerdidoCallBack_cb(data, error) {
		onDataLoad_cb(data, error);
		setElementValue("moeda1.idMoeda", getNestedBeanPropertyValue(data, "moeda.idMoeda"))
		var sdo = createServiceDataObject("lms.vendas.manterClientePerdidoAction.getRegionalByFilial", "regionalSession", {idFilial:getNestedBeanPropertyValue(data, "filial.idFilial")});
		xmit({serviceDataObjects:[sdo]});
		
	}
	
	function regionalSession_cb(data,error) {
		siglaRegional = getNestedBeanPropertyValue(data, "regional.siglaDescricao");
		setElementValue("siglaDescricao", siglaRegional);
	}
	
	function loadDataObjectsCad() {	
		onPageLoad(); 
		var data = new Array();	
		var sdo = createServiceDataObject("lms.vendas.manterClientePerdidoAction.getBasicData", "dataSession", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function buscaDataSession(){	
		var data = new Array();	
		var sdo = createServiceDataObject("lms.vendas.manterClientePerdidoAction.getBasicData", "dataSession", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function dataSession_cb(data,error) {
		idFilial = getNestedBeanPropertyValue(data, "filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data, "filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia");
    	siglaRegional = getNestedBeanPropertyValue(data, "regional.siglaDescricao");
	}	

	function initWindow(eventObj) {
		if(eventObj.name == "tab_click"){
			writeDataSession();
		}
		if(eventObj.name == "newButton_click" || eventObj.name == "removeButton"){
			buscaDataSession();
			writeDataSession();
		}
	}		

	/**
	 * Preenche os dados basicos da tela
	 */
	function writeDataSession() {
		setElementValue("filial.idFilial", idFilial);
		setElementValue("filial.sgFilial", sgFilial);
		setElementValue("filial.pessoa.nmFantasia", nmFilial);
		setElementValue("siglaDescricao", siglaRegional);
  	}



</script>
